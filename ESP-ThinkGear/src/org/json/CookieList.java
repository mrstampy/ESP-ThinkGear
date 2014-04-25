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
package org.json;

/*
 Copyright (c) 2002 JSON.org

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 The Software shall be used for Good, not Evil.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */

import java.io.Serializable;
import java.util.Iterator;

/**
 * Convert a web browser cookie list string to a JSONObject and back.
 * 
 * @author JSON.org
 * @version 2010-12-24
 */
public class CookieList implements Serializable {

	private static final long serialVersionUID = 4943005710538811920L;

	/**
	 * Convert a cookie list into a JSONObject. A cookie list is a sequence of
	 * name/value pairs. The names are separated from the values by '='. The pairs
	 * are separated by ';'. The names and the values will be unescaped, possibly
	 * converting '+' and '%' sequences. To add a cookie to a cooklist,
	 * cookielistJSONObject.put(cookieJSONObject.getString("name"),
	 * cookieJSONObject.getString("value"));
	 * 
	 * @param string
	 *          A cookie list string
	 * @return A JSONObject
	 * @throws JSONException
	 */
	public static JSONObject toJSONObject(String string) throws JSONException {
		JSONObject jo = new JSONObject();
		JSONTokener x = new JSONTokener(string);
		while (x.more()) {
			String name = Cookie.unescape(x.nextTo('='));
			x.next('=');
			jo.put(name, Cookie.unescape(x.nextTo(';')));
			x.next();
		}
		return jo;
	}

	/**
	 * Convert a JSONObject into a cookie list. A cookie list is a sequence of
	 * name/value pairs. The names are separated from the values by '='. The pairs
	 * are separated by ';'. The characters '%', '+', '=', and ';' in the names
	 * and values are replaced by "%hh".
	 * 
	 * @param jo
	 *          A JSONObject
	 * @return A cookie list string
	 * @throws JSONException
	 */
	public static String toString(JSONObject jo) throws JSONException {
		boolean b = false;
		Iterator keys = jo.keys();
		String string;
		StringBuffer sb = new StringBuffer();
		while (keys.hasNext()) {
			string = keys.next().toString();
			if (!jo.isNull(string)) {
				if (b) {
					sb.append(';');
				}
				sb.append(Cookie.escape(string));
				sb.append("=");
				sb.append(Cookie.escape(jo.getString(string)));
				b = true;
			}
		}
		return sb.toString();
	}
}
