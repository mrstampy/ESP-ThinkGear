/*
 * ESP-ThinkGear Copyright (C) 2014 Burton Alexander
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 * 
 */

/*
 * 
 * This provides a simple socket connector to the NeuroSky MindWave ThinkGear connector.
 * For more info visit http://crea.tion.to/processing/thinkgear-java-socket
 * 
 * No warranty or any stuffs like that.
 * 
 * Have fun!
 * Andreas Borg
 * borg@elevated.to
 * 
 * 
 * (c) 2010
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 * 
 * @author		Andreas Borg, borg@elevated.to
 * @modified	June, 2011
 * @version		1.0
 * 
 * 
 * This library is following the same design as the one developed by Jorge C. S. Cardoso for the MindSet device.
 * The MindWave device can communicate to a socket over JSON instead of the serial port. That makes it easier and tidier
 * to talk between the device and Java. For instructions on how to use the callback listeners please refer to
 * 
 * http://jorgecardoso.eu/processing/MindSetProcessing/
 * 
 * 
 * Data is passed back to the application via the following callback methods:
 * 
 * 
 * public void attentionEvent(int attentionLevel)
 * Returns the current attention level [0, 100].
 * Values in [1, 20] are considered strongly lowered.
 * Values in [20, 40] are considered reduced levels.
 * Values in [40, 60] are considered neutral.
 * Values in [60, 80] are considered slightly elevated.
 * Values in [80, 100] are considered elevated.
 * 
 * public void meditationEvent(int meditationLevel)
 * Returns the current meditation level [0, 100].
 * The interpretation of the values is the same as for the attentionLevel.
 * 
 * 
 * public void poorSignalEvent(int signalLevel)
 * Returns the signal level [0, 200]. The greater the value, the more noise is detected in the signal.
 * 200 is a special value  that means that the ThinkGear contacts are not touching the skin.
 * 
 * 
 * public void eegEvent(int delta, int theta, int low_alpha, int high_alpha, int low_beta, int high_beta, int low_gamma, int mid_gamma) </code><br>
 * Returns the EEG data. The values have no units.
 * 
 * 
 * 
 * public void rawEvent(int [])
 * Returns the the current 512 raw signal samples [-32768, 32767]. 
 * 
 * 
 */
package com.github.mrstampy.esp.neurosky;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import javolution.util.FastList;

import org.apache.mina.core.service.IoHandler;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.Observable;
import rx.Scheduler;
import rx.Scheduler.Inner;
import rx.Scheduler.Recurse;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import com.github.mrstampy.esp.multiconnectionsocket.AbstractMultiConnectionSocket;
import com.github.mrstampy.esp.multiconnectionsocket.MultiConnectionSocketException;
import com.github.mrstampy.esp.multiconnectionsocket.ConnectionEvent.State;
import com.github.mrstampy.esp.multiconnectionsocket.event.AbstractMultiConnectionEvent;
import com.github.mrstampy.esp.neurosky.event.AbstractThinkGearEvent;
import com.github.mrstampy.esp.neurosky.event.BlinkStrengthThinkGearEvent;
import com.github.mrstampy.esp.neurosky.event.EEGPowerThinkGearEvent;
import com.github.mrstampy.esp.neurosky.event.ESenseThinkGearEvent;
import com.github.mrstampy.esp.neurosky.event.EventType;
import com.github.mrstampy.esp.neurosky.event.PoorSignalLevelThinkGearEvent;
import com.github.mrstampy.esp.neurosky.event.RawEEGThinkGearEvent;
import com.github.mrstampy.esp.neurosky.subscription.ThinkGearEventListener;
import com.github.mrstampy.esp.neurosky.subscription.ThinkGearSocketConnector;

/**
 * This class connects to the socket described in the ThinkGear socket protocol
 * documentation. The events received are converted into strongly typed events
 * located in the net.sourceforge.entrainer.neurosky.event package and are
 * queued for transport. An Apache MINA socket acceptor is bound to the
 * broadcaster port defined in {@link ThinkGearSocketConnector} and the events
 * are read from the queue and sent to subscribers connected using the
 * {@link ThinkGearSocketConnector} class. <br>
 * <br>
 * Broadcasting is turned off by default. To enable set the System property
 * 'broadcast.messages' to true ie. java -Dbroadcast.messages=true ...
 * 
 * @author burton
 */
public class MultiConnectionThinkGearSocket extends AbstractMultiConnectionSocket<String> implements ThinkGearConstants {
	private static final Logger log = LoggerFactory.getLogger(MultiConnectionThinkGearSocket.class);

	private String host = LOCAL_HOST;

	private Socket neuroSocket;
	private PrintWriter out;
	private BufferedReader stdIn;

	private SubscriptionHandlerAdapter subscriptionHandlerAdapter;

	private List<ThinkGearEventListener> listeners = new FastList<ThinkGearEventListener>();
	private ReentrantReadWriteLock listenerLock = new ReentrantReadWriteLock(true);
	private ReadLock readLock = listenerLock.readLock();
	private WriteLock writeLock = listenerLock.writeLock();

	private boolean rawData;

	private boolean canSendNeuroskyMessages;

	private SampleBuffer sampleBuffer = new SampleBuffer();
	private Scheduler scheduler = Schedulers.executor(Executors.newScheduledThreadPool(3));
	private Subscription subscription;

	/**
	 * Connects to the ThinkGear socket on the local host. The system property
	 * 'broadcast.messages' is used to enable/disable broadcasting for
	 * {@link ThinkGearSocketConnector}s, and the system property
	 * 'send.neurosky.messages' is used to enable/disable remote
	 * {@link ThinkGearSocketConnector}s sending messages to the Neurosky device.
	 * 
	 * @throws IOException
	 */
	public MultiConnectionThinkGearSocket() throws IOException {
		this(LOCAL_HOST, Boolean.getBoolean(BROADCAST_MESSAGES), Boolean.getBoolean(SEND_NEUROSKY_MESSAGES));
	}

	/**
	 * Connects to the ThinkGear socket on the specified host. The system property
	 * 'broadcast.messages' is used to enable/disable broadcasting for
	 * {@link ThinkGearSocketConnector}s, and the system property
	 * 'send.neurosky.messages' is used to enable/disable remote
	 * {@link ThinkGearSocketConnector}s sending messages to the Neurosky device.
	 * 
	 * @param host
	 * @throws IOException
	 */
	public MultiConnectionThinkGearSocket(String host) throws IOException {
		this(host, Boolean.getBoolean(BROADCAST_MESSAGES), Boolean.getBoolean(SEND_NEUROSKY_MESSAGES));
	}

	/**
	 * Connects to the ThinkGear socket on the specified host, allowing
	 * programmatic control of broadcasting for {@link ThinkGearSocketConnector}s,
	 * and the system property 'send.neurosky.messages' is used to enable/disable
	 * remote {@link ThinkGearSocketConnector}s sending messages to the Neurosky
	 * device.
	 * 
	 * @param host
	 * @param broadcasting
	 * @throws IOException
	 */
	public MultiConnectionThinkGearSocket(String host, boolean broadcasting) throws IOException {
		this(host, broadcasting, Boolean.getBoolean(SEND_NEUROSKY_MESSAGES));
	}

	/**
	 * Connects to the ThinkGear socket on the specified host, allowing
	 * programmatic control of broadcasting for {@link ThinkGearSocketConnector}s,
	 * and of enabling/disabling remote {@link ThinkGearSocketConnector}s sending
	 * messages to the Neurosky device.
	 * 
	 * @param host
	 * @param broadcasting
	 * @param canSendNeuroskyMessages
	 * @throws IOException
	 */
	public MultiConnectionThinkGearSocket(String host, boolean broadcasting, boolean canSendNeuroskyMessages)
			throws IOException {
		this(host, broadcasting, canSendNeuroskyMessages, Boolean.getBoolean(RAW_DATA_KEY));
	}

	/**
	 * Connects to the ThinkGear socket on the specified host, allowing
	 * programmatic control of broadcasting for {@link ThinkGearSocketConnector}s,
	 * of enabling/disabling remote {@link ThinkGearSocketConnector}s sending
	 * messages to the Neurosky device and of the raw data acquisition from the
	 * Neurosky device.
	 * 
	 * @param host
	 * @param broadcasting
	 * @param canSendNeuroskyMessages
	 * @throws IOException
	 */
	public MultiConnectionThinkGearSocket(String host, boolean broadcasting, boolean canSendNeuroskyMessages,
			boolean rawData) throws IOException {
		super(broadcasting);

		this.host = host;
		this.rawData = rawData;
		this.canSendNeuroskyMessages = canSendNeuroskyMessages;
		
		addConnectionEventListener(sampleBuffer);

		log.info("MultiConnectonThinkGearSocket is {} events", broadcasting ? "broadcasting" : "not broadcasting");
	}

	/**
	 * Adds the specified {@link ThinkGearEventListener} to the
	 * {@link MultiConnectionThinkGearSocket} directly. Use this method to receive
	 * events in preference to {@link ThinkGearSocketConnector} if running in the
	 * same JVM as the {@link MultiConnectionThinkGearSocket}.
	 * 
	 * @param listener
	 */
	public void addListener(ThinkGearEventListener listener) {
		writeLock.lock();
		try {
			listeners.add(listener);
		} finally {
			writeLock.unlock();
		}
	}

	public void removeListener(ThinkGearEventListener listener) {
		writeLock.lock();
		try {
			listeners.remove(listener);
		} finally {
			writeLock.unlock();
		}
	}

	public void clearListeners() {
		writeLock.lock();
		try {
			listeners.clear();
		} finally {
			writeLock.unlock();
		}
	}

	/**
	 * When invoked the tuning functionality of the {@link SampleBuffer} will be
	 * activated. The tuning process takes ~ 10 seconds, during which the number
	 * of samples will be counted and used to resize the buffer to more closely
	 * represent 1 seconds' worth of data.
	 */
	public void tune() {
		if (!isConnected()) {
			log.warn("Must be connected to the Nia to tune");
			return;
		}

		log.info("Tuning sample buffer");

		sampleBuffer.tune();

		scheduler.schedule(new Action1<Scheduler.Inner>() {

			@Override
			public void call(Inner t1) {
				sampleBuffer.stopTuning();
			}
		}, 10, TimeUnit.SECONDS);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sourceforge.entrainer.neurosky.MultiConnectionSocket#start()
	 */
	@Override
	protected void startImpl() throws MultiConnectionSocketException {
		closeSocket();

		try {
			createSocketConnection();
		} catch (MultiConnectionSocketException e) {
			closeSocket();
			throw e;
		}

		sendFormats();

		startReadThread();

		if (isRawData()) startSampleCollection();
	}

	private void startSampleCollection() {
		ThinkGearDSPValues values = ThinkGearDSPValues.getInstance();

		long pause = values.getSampleRate() * 2;
		long snooze = values.getSampleRateSleepTime();
		TimeUnit tu = values.getSampleRateUnits();

		subscription = scheduler.schedulePeriodically(new Action1<Scheduler.Inner>() {

			@Override
			public void call(Inner t1) {
				processSnapshot(sampleBuffer.getSnapshot());
			}
		}, pause, snooze, tu);
	}

	protected void processSnapshot(double[] snapshot) {
		eventPerformed(new RawEEGThinkGearEvent(snapshot));
	}

	private void eventPerformed(AbstractThinkGearEvent event) {
		Observable.just(event).subscribe(new Action1<AbstractThinkGearEvent>() {

			@Override
			public void call(AbstractThinkGearEvent t1) {
				notifyListeners(t1);
				if (canBroadcast()) subscriptionHandlerAdapter.sendMultiConnectionEvent(t1);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sourceforge.entrainer.neurosky.MultiConnectionSocket#stop()
	 */
	@Override
	protected void stopImpl() {
		try {
			if (subscription != null) subscription.unsubscribe();
			closeSocket();
		} catch (Throwable e) {
			log.error("Unexpected exception on stop", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sourceforge.entrainer.neurosky.MultiConnectionSocket#isConnected()
	 */
	@Override
	public boolean isConnected() {
		return neuroSocket != null && neuroSocket.isConnected();
	}

	/**
	 * Sends the specified message to the ThinkGear socket.
	 * 
	 * @param msg
	 */
	public void sendMessage(String msg) {
		if (!isConnected()) {
			log.error("Cannot send message {} to NeuroSky, not connected", msg);
			return;
		}

		log.info("Sending message to NeuroSky: {}", msg);
		out.println(msg);
	}

	/**
	 * Returns true if {@link ThinkGearSocketConnector}s can send messages to the
	 * Neurosky device.
	 * 
	 * @return
	 * @see SubscriptionHandlerAdapter
	 * @see ThinkGearSocketConnector
	 */
	public boolean canSendNeuroskyMessages() {
		return canBroadcast() && subscriptionHandlerAdapter.canSendNeuroskyMessages();
	}

	/**
	 * Returns true if the device is to acquire raw data.
	 */
	public boolean isRawData() {
		return rawData;
	}

	/**
	 * Enable/disable raw data acquisition after the next connect to the ThinkGear
	 * socket.
	 */
	public void setRawData(boolean rawData) {
		this.rawData = rawData;
	}

	private void createSocketConnection() throws MultiConnectionSocketException {
		try {
			neuroSocket = new Socket(host, NEUROSOCKET_PORT);
			out = new PrintWriter(neuroSocket.getOutputStream(), true);
			stdIn = new BufferedReader(new InputStreamReader(neuroSocket.getInputStream()));
		} catch (Throwable e) {
			log.error("Could not connect to ThinkGearSocket", e);
			throw new MultiConnectionSocketException(e);
		}
	}

	private void closeSocket() throws MultiConnectionSocketException {
		if (neuroSocket == null) return;

		try {
			neuroSocket.close();
			out.close();
			stdIn.close();

			neuroSocket = null;
			out = null;
			stdIn = null;
		} catch (Throwable e) {
			throw new MultiConnectionSocketException(e);
		}
	}

	private void sendFormats() {
		scheduler.schedule(new Action1<Scheduler.Inner>() {

			@Override
			public void call(Inner t1) {
				JSONObject format = new JSONObject();

				try {
					format.put("enableRawOutput", rawData);
					format.put("format", "Json");
				} catch (JSONException e) {
					log.error("Could not create formats object", e);
				}

				sendMessage(format.toString());
			}
		});
	}

	private void startReadThread() {
		scheduler.scheduleRecursive(new Action1<Scheduler.Recurse>() {

			@Override
			public void call(Recurse t1) {
				String userInput;
				try {
					if (!neuroSocket.isConnected() || (userInput = stdIn.readLine()) == null) return;

					String[] packets = userInput.split("/\r/");
					for (int s = 0; s < packets.length; s++) {
						if (((String) packets[s]).indexOf("{") > -1) {
							publishMessage(packets[s]);
						}
					}

					t1.schedule();
				} catch (Exception e) {
					log.error("Unexpected exception", e);
					if(isConnected()) notifyConnectionEventListeners(State.ERROR_STOPPED);
				}
			}
		});
	}

	@Override
	protected void parseMessage(String message) {
		try {
			log.trace("Processing message {}", message);

			int first = message.indexOf("{");
			int last = message.lastIndexOf("}");

			if (first == -1 || last == -1) {
				log.info("Cannot parse message {}", message);
				return;
			}

			if (first > 0) message = message.substring(first);
			if (last < message.length()) message = message.substring(0, last + 1);

			parsePacket(new JSONObject(message));
		} catch (Throwable e) {
			log.error("Could not process message {}", e, message);
		}
	}

	@SuppressWarnings("rawtypes")
	private void parsePacket(JSONObject jsonObject) throws JSONException {
		Iterator itr = jsonObject.keys();
		while (itr.hasNext()) {

			Object e = itr.next();
			String key = e.toString();

			EventType type = EventType.valueOf(key);

			AbstractThinkGearEvent event = null;

			switch (type) {
			case blinkStrength:
				event = getBlinkStrengthThinkGearEvent(jsonObject);
				break;
			case eSense:
				JSONObject esense = jsonObject.getJSONObject(EventType.eSense.name());
				event = getESenseThinkGearEvent(esense);
				break;
			case eegPower:
				JSONObject eegPower = jsonObject.getJSONObject(EventType.eegPower.name());
				event = getEegPowerThinkGearEvent(eegPower);
				break;
			case poorSignalLevel:
				event = getPoorSignalLevelThinkGearEvent(jsonObject);
				break;
			case rawEeg:
				sampleBuffer.addSample(getDouble(jsonObject, EventType.rawEeg.name()));
				break;
			default:
				break;
			}

			if (event != null) eventPerformed(event);
		}
	}

	public void notifyListeners(AbstractMultiConnectionEvent<EventType> event) {
		readLock.lock();
		try {
			if (listeners.isEmpty()) return;

			for (ThinkGearEventListener listener : listeners) {
				listener.thinkGearEventPerformed(event);
			}
		} finally {
			readLock.unlock();
		}
	}

	private PoorSignalLevelThinkGearEvent getPoorSignalLevelThinkGearEvent(JSONObject jsonObject) {
		PoorSignalLevelThinkGearEvent event = new PoorSignalLevelThinkGearEvent();

		event.setPoorSignalLevel(getInt(jsonObject, EventType.poorSignalLevel.name()));

		return event;
	}

	private EEGPowerThinkGearEvent getEegPowerThinkGearEvent(JSONObject jsonObject) {
		EEGPowerThinkGearEvent event = new EEGPowerThinkGearEvent();

		event.setDelta(getDouble(jsonObject, DELTA));
		event.setHighAlpha(getDouble(jsonObject, HIGH_ALPHA));
		event.setHighBeta(getDouble(jsonObject, HIGH_BETA));
		event.setHighGamma(getDouble(jsonObject, HIGH_GAMMA));
		event.setLowAlpha(getDouble(jsonObject, LOW_ALPHA));
		event.setLowBeta(getDouble(jsonObject, LOW_BETA));
		event.setLowGamma(getDouble(jsonObject, LOW_GAMMA));
		event.setTheta(getDouble(jsonObject, THETA));

		return event;
	}

	private BlinkStrengthThinkGearEvent getBlinkStrengthThinkGearEvent(JSONObject jsonObject) {
		BlinkStrengthThinkGearEvent event = new BlinkStrengthThinkGearEvent();

		event.setBlinkStrength(getInt(jsonObject, EventType.blinkStrength.name()));

		return event;
	}

	private ESenseThinkGearEvent getESenseThinkGearEvent(JSONObject jsonObject) {
		ESenseThinkGearEvent event = new ESenseThinkGearEvent();

		event.setAttention(getInt(jsonObject, ATTENTION));
		event.setMeditation(getInt(jsonObject, MEDITATION));

		return event;
	}

	private int getInt(JSONObject jsonObject, String key) {
		try {
			return jsonObject.getInt(key);
		} catch (JSONException e) {
			log.error("Could not extract {} from JSON object {}", e, key, jsonObject);
		}

		return -1;
	}

	private double getDouble(JSONObject jsonObject, String key) {
		try {
			return jsonObject.getDouble(key);
		} catch (JSONException e) {
			log.error("Could not extract {} from JSON object {}", e, key, jsonObject);
		}

		return -1;
	}

	@Override
	protected IoHandler getHandlerAdapter() {
		subscriptionHandlerAdapter = new SubscriptionHandlerAdapter(this, canSendNeuroskyMessages);
		return subscriptionHandlerAdapter;
	}
}
