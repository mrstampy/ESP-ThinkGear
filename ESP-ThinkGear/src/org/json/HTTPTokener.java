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

/**
 * The HTTPTokener extends the JSONTokener to provide additional methods for the
 * parsing of HTTP headers.
 * 
 * @author JSON.org
 * @version 2010-12-24
 */
public class HTTPTokener extends JSONTokener {

	private static final long serialVersionUID = 418614599289785145L;

	/**
	 * Construct an HTTPTokener from a string.
	 * 
	 * @param string
	 *          A source string.
	 */
	public HTTPTokener(String string) {
		super(string);
	}

	/**
	 * Get the next token or string. This is used in parsing HTTP headers.
	 * 
	 * @throws JSONException
	 * @return A String.
	 */
	public String nextToken() throws JSONException {
		char c;
		char q;
		StringBuffer sb = new StringBuffer();
		do {
			c = next();
		} while (Character.isWhitespace(c));
		if (c == '"' || c == '\'') {
			q = c;
			for (;;) {
				c = next();
				if (c < ' ') {
					throw syntaxError("Unterminated string.");
				}
				if (c == q) {
					return sb.toString();
				}
				sb.append(c);
			}
		}
		for (;;) {
			if (c == 0 || Character.isWhitespace(c)) {
				return sb.toString();
			}
			sb.append(c);
			c = next();
		}
	}
}
