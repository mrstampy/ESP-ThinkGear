# ESP-ThinkGear - EEG Signal Processing for NeuroSky ThinkGear Devices

This library provides the [NeuroSky](http://www.neurosky.com) MindWave ThinkGear connector implementation of the [ESP](http://mrstampy.github.io/ESP/) library base classes. It is designed to be fast and efficient, using high performance Java libraries:

* [Disruptor](https://github.com/LMAX-Exchange/disruptor)
* [Javolution](http://javolution.org/)
* [Apache MINA](http://mina.apache.org/)

## Maven Dependency
       <dependency>
           <groupId>com.github.mrstampy</groupId>
           <artifactId>esp-thinkgear</artifactId>
           <version>1.3</version>
       </dependency>

Usage of the library is straight-forward. One only needs to instantiate the MultiConnectionThinkGearSocket implementation, add a listener, start the socket and deal with the events as they occur.

## Multiple Connections

As implied by the name of the class, the MultiConnectionThinkGearSocket is capable of not only processing ThinkGear messages for a single application but also of broadcasting the messages to separate applications, even on separate machines and devices! This functionality is disabled by default. To enable it either set the System property 'broadcast.messages' to true or instantiate the MultiConnectionThinkGearSocket using one of its other constructors:

	MultiConnectionThinkGearSocket socket = new MultiConnectionThinkGearSocket("ThinkGear socket hostname", true);
	...
	socket.start();

	...and then in another application:

ThinkGearSocketConnector connector = new ThinkGearSocketConnector("Hostname running MultiConnectionThinkGearSocket");
		
	connector.addThinkGearEventListener(new ThinkGearEventListenerAdapter() {
		// overriding methods as appropriate
	});
	
	connector.connect();
	
	//after successful connection...
	connector.subscribe(EventType...);

## Raw Signal Processing (as of version 1.3)

In the development of the [ESP-Nia](http://mrstampy.github.com/ESP-Nia/) implementation the base classes for raw signal processing were developed.  Multi connection socket implementations contain a raw data buffer which contains the current second's worth of data (for the OCZ Nia this translates to 3910 data points, for the ThinkGear devices this translates to 512 data points).  A sampler snapshots the buffer at a rate of 100Hz and raw data events are generated from the snapshots (for both the ThinkGear and ThinkGear implementations each snapshot consists of 512 evenly spaced data points).

Additional classes have been added to assist with raw signal processing. The examples shown will work with the NeuroSky raw output however these classes can be used for any DSP work with low sample sizes (ie. < 1000 samples / sec).  For greater sample sizes the utilization of	primitive arrays is recommended.

Additional functionality is described in the JavaDocs. This work is released under the GPL 3.0 license. No warranty of any kind is offered.

[ESP-ThinkGear](http://mrstampy.github.io/ESP-ThinkGear/) Copyright (C) 2014 Burton Alexander. 

eSense, ThinkGear, MDT, NeuroBoy and NeuroSky are trademarks of [NeuroSky Inc](http://www.neurosky.com).
