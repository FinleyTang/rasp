/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.javaweb.rasp.commons.utils;

import java.io.UnsupportedEncodingException;

/**
 * Converts hexadecimal Strings. The charset used for certain operation can be set, the default is set in
 * {@link #DEFAULT_CHARSET_NAME}
 *
 * @author Apache Software Foundation
 * @version $Id: Hex.java 1157192 2011-08-12 17:27:38Z ggregory $
 * @since 1.1
 */
public class Hex {

	/**
	 * Default charset name is CharEncoding UTF_8
	 *
	 * @since 1.4
	 */
	public static final String DEFAULT_CHARSET_NAME = "UTF-8";

	/**
	 * Used to build output as Hex
	 */
	private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

	/**
	 * Used to build output as Hex
	 */
	private static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

	public static String decodeHex(String data) throws DecoderException {
		if (data != null) {
			return new String(decodeHex(data.toCharArray()));
		}

		return null;
	}

	/**
	 * Converts an array of characters representing hexadecimal values into an array of bytes of those same values. The
	 * returned array will be half the length of the passed array, as it takes two characters to represent any given
	 * byte. An exception is thrown if the passed char array has an odd number of elements.
	 *
	 * @param data An array of characters containing hexadecimal digits
	 * @return A byte array containing binary data decoded from the supplied char array.
	 * @throws DecoderException Thrown if an odd number or illegal of characters is supplied
	 */
	public static byte[] decodeHex(char[] data) throws DecoderException {
		int len = data.length;

		if ((len & 0x01) != 0) {
			throw new DecoderException("Odd number of characters.");
		}

		byte[] out = new byte[len >> 1];

		// two characters form the hex value.
		for (int i = 0, j = 0; j < len; i++) {
			int f = toDigit(data[j], j) << 4;
			j++;
			f = f | toDigit(data[j], j);
			j++;
			out[i] = (byte) (f & 0xFF);
		}

		return out;
	}

	/**
	 * Converts an array of bytes into an array of characters representing the hexadecimal values of each byte in order.
	 * The returned array will be double the length of the passed array, as it takes two characters to represent any
	 * given byte.
	 *
	 * @param data a byte[] to convert to Hex characters
	 * @return A char[] containing hexadecimal characters
	 */
	public static char[] encodeHex(byte[] data) {
		return encodeHex(data, true);
	}

	/**
	 * Converts an array of bytes into an array of characters representing the hexadecimal values of each byte in order.
	 * The returned array will be double the length of the passed array, as it takes two characters to represent any
	 * given byte.
	 *
	 * @param data        a byte[] to convert to Hex characters
	 * @param toLowerCase <code>true</code> converts to lowercase, <code>false</code> to uppercase
	 * @return A char[] containing hexadecimal characters
	 * @since 1.4
	 */
	public static char[] encodeHex(byte[] data, boolean toLowerCase) {
		return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
	}

	/**
	 * Converts an array of bytes into an array of characters representing the hexadecimal values of each byte in order.
	 * The returned array will be double the length of the passed array, as it takes two characters to represent any
	 * given byte.
	 *
	 * @param data     a byte[] to convert to Hex characters
	 * @param toDigits the output alphabet
	 * @return A char[] containing hexadecimal characters
	 * @since 1.4
	 */
	protected static char[] encodeHex(byte[] data, char[] toDigits) {
		int    l   = data.length;
		char[] out = new char[l << 1];
		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
			out[j++] = toDigits[0x0F & data[i]];
		}
		return out;
	}

	/**
	 * Converts an array of bytes into a String representing the hexadecimal values of each byte in order. The returned
	 * String will be double the length of the passed array, as it takes two characters to represent any given byte.
	 *
	 * @param data a byte[] to convert to Hex characters
	 * @return A String containing hexadecimal characters
	 * @since 1.4
	 */
	public static String encodeHexString(byte[] data) {
		return new String(encodeHex(data));
	}

	/**
	 * Converts a hexadecimal character to an integer.
	 *
	 * @param ch    A character to convert to an integer digit
	 * @param index The index of the character in the source
	 * @return An integer
	 * @throws DecoderException Thrown if ch is an illegal hex character
	 */
	protected static int toDigit(char ch, int index) throws DecoderException {
		int digit = Character.digit(ch, 16);
		if (digit == -1) {
			throw new DecoderException("Illegal hexadecimal character " + ch + " at index " + index);
		}
		return digit;
	}

	private final String charsetName;

	/**
	 * Creates a new codec with the default charset name {@link #DEFAULT_CHARSET_NAME}
	 */
	public Hex() {
		// use default encoding
		this.charsetName = DEFAULT_CHARSET_NAME;
	}

	/**
	 * Creates a new codec with the given charset name.
	 *
	 * @param csName the charset name.
	 * @since 1.4
	 */
	public Hex(String csName) {
		this.charsetName = csName;
	}

	/**
	 * Converts an array of character bytes representing hexadecimal values into an array of bytes of those same values.
	 * The returned array will be half the length of the passed array, as it takes two characters to represent any given
	 * byte. An exception is thrown if the passed char array has an odd number of elements.
	 *
	 * @param array An array of character bytes containing hexadecimal digits
	 * @return A byte array containing binary data decoded from the supplied byte array (representing characters).
	 * @throws DecoderException Thrown if an odd number of characters is supplied to this function
	 * @see #decodeHex(char[])
	 */
	public byte[] decode(byte[] array) throws DecoderException {
		try {
			return decodeHex(new String(array, getCharsetName()).toCharArray());
		} catch (UnsupportedEncodingException e) {
			throw new DecoderException(e.getMessage(), e);
		}
	}

	/**
	 * Converts a String or an array of character bytes representing hexadecimal values into an array of bytes of those
	 * same values. The returned array will be half the length of the passed String or array, as it takes two characters
	 * to represent any given byte. An exception is thrown if the passed char array has an odd number of elements.
	 *
	 * @param object A String or, an array of character bytes containing hexadecimal digits
	 * @return A byte array containing binary data decoded from the supplied byte array (representing characters).
	 * @throws DecoderException Thrown if an odd number of characters is supplied to this function or the object is not a String or
	 *                          char[]
	 * @see #decodeHex(char[])
	 */
	public static byte[] decode(Object object) throws DecoderException {
		try {
			char[] charArray = object instanceof String ? ((String) object).toCharArray() : (char[]) object;
			return decodeHex(charArray);
		} catch (ClassCastException e) {
			throw new DecoderException(e.getMessage(), e);
		}
	}

	/**
	 * Converts a String or an array of bytes into an array of characters representing the hexadecimal values of each
	 * byte in order. The returned array will be double the length of the passed String or array, as it takes two
	 * characters to represent any given byte.
	 * <p>
	 * The conversion from hexadecimal characters to bytes to be encoded to performed with the charset named by
	 * {@link #getCharsetName()}.
	 * </p>
	 *
	 * @param object a String, or byte[] to convert to Hex characters
	 * @return A char[] containing hexadecimal characters
	 * @throws EncoderException Thrown if the given object is not a String or byte[]
	 * @see #encodeHex(byte[])
	 */
	public Object encode(Object object) throws EncoderException {
		try {
			byte[] byteArray = object instanceof String ? ((String) object).getBytes(getCharsetName()) : (byte[]) object;
			return encodeHex(byteArray);
		} catch (ClassCastException e) {
			throw new EncoderException(e.getMessage(), e);
		} catch (UnsupportedEncodingException e) {
			throw new EncoderException(e.getMessage(), e);
		}
	}

	/**
	 * Gets the charset name.
	 *
	 * @return the charset name.
	 * @since 1.4
	 */
	public String getCharsetName() {
		return this.charsetName;
	}

	/**
	 * Returns a string representation of the object, which includes the charset name.
	 *
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return super.toString() + "[charsetName=" + this.charsetName + "]";
	}

	/*
	 * Licensed to the Apache Software Foundation (ASF) under one or more
	 * contributor license agreements.  See the NOTICE file distributed with
	 * this work for additional information regarding copyright ownership.
	 * The ASF licenses this file to You under the Apache License, Version 2.0
	 * (the "License"); you may not use this file except in compliance with
	 * the License.  You may obtain a copy of the License at
	 *
	 *      http://www.apache.org/licenses/LICENSE-2.0
	 *
	 * Unless required by applicable law or agreed to in writing, software
	 * distributed under the License is distributed on an "AS IS" BASIS,
	 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	 * See the License for the specific language governing permissions and
	 * limitations under the License.
	 */

	/**
	 * Thrown when there is a failure condition during the decoding process. This exception is thrown when a Decoder
	 * encounters a decoding specific exception such as invalid data, or characters outside of the expected range.
	 *
	 * @author Apache Software Foundation
	 * @version $Id: DecoderException.java 1157192 2011-08-12 17:27:38Z ggregory $
	 */
	public static class DecoderException extends Exception {

		/**
		 * Declares the Serial Version Uid.
		 *
		 * @see <a href="http://c2.com/cgi/wiki?AlwaysDeclareSerialVersionUid">Always Declare Serial Version Uid</a>
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Constructs a new exception with <code>null</code> as its detail message. The cause is not initialized, and may
		 * subsequently be initialized by a call to {@link #initCause}.
		 *
		 * @since 1.4
		 */
		public DecoderException() {
			super();
		}

		/**
		 * Constructs a new exception with the specified detail message. The cause is not initialized, and may subsequently
		 * be initialized by a call to {@link #initCause}.
		 *
		 * @param message The detail message which is saved for later retrieval by the {@link #getMessage()} method.
		 */
		public DecoderException(String message) {
			super(message);
		}

		/**
		 * Constructsa new exception with the specified detail message and cause.
		 *
		 * <p>
		 * Note that the detail message associated with <code>cause</code> is not automatically incorporated into this
		 * exception's detail message.
		 * </p>
		 *
		 * @param message The detail message which is saved for later retrieval by the {@link #getMessage()} method.
		 * @param cause   The cause which is saved for later retrieval by the {@link #getCause()} method. A <code>null</code>
		 *                value is permitted, and indicates that the cause is nonexistent or unknown.
		 * @since 1.4
		 */
		public DecoderException(String message, Throwable cause) {
			super(message, cause);
		}

		/**
		 * Constructs a new exception with the specified cause and a detail message of <code>(cause==null ?
		 * null : cause.toString())</code> (which typically contains the class and detail message of <code>cause</code>).
		 * This constructor is useful for exceptions that are little more than wrappers for other throwables.
		 *
		 * @param cause The cause which is saved for later retrieval by the {@link #getCause()} method. A <code>null</code>
		 *              value is permitted, and indicates that the cause is nonexistent or unknown.
		 * @since 1.4
		 */
		public DecoderException(Throwable cause) {
			super(cause);
		}
	}

	static class EncoderException extends Exception {

		/**
		 * Declares the Serial Version Uid.
		 *
		 * @see <a href="http://c2.com/cgi/wiki?AlwaysDeclareSerialVersionUid">Always Declare Serial Version Uid</a>
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Constructs a new exception with <code>null</code> as its detail message. The cause is not initialized, and may
		 * subsequently be initialized by a call to {@link #initCause}.
		 *
		 * @since 1.4
		 */
		public EncoderException() {
			super();
		}

		/**
		 * Constructs a new exception with the specified detail message. The cause is not initialized, and may subsequently
		 * be initialized by a call to {@link #initCause}.
		 *
		 * @param message a useful message relating to the encoder specific error.
		 */
		public EncoderException(String message) {
			super(message);
		}

		/**
		 * Constructs a new exception with the specified detail message and cause.
		 *
		 * <p>
		 * Note that the detail message associated with <code>cause</code> is not automatically incorporated into this
		 * exception's detail message.
		 * </p>
		 *
		 * @param message The detail message which is saved for later retrieval by the {@link #getMessage()} method.
		 * @param cause   The cause which is saved for later retrieval by the {@link #getCause()} method. A <code>null</code>
		 *                value is permitted, and indicates that the cause is nonexistent or unknown.
		 * @since 1.4
		 */
		public EncoderException(String message, Throwable cause) {
			super(message, cause);
		}

		/**
		 * Constructs a new exception with the specified cause and a detail message of <code>(cause==null ?
		 * null : cause.toString())</code> (which typically contains the class and detail message of <code>cause</code>).
		 * This constructor is useful for exceptions that are little more than wrappers for other throwables.
		 *
		 * @param cause The cause which is saved for later retrieval by the {@link #getCause()} method. A <code>null</code>
		 *              value is permitted, and indicates that the cause is nonexistent or unknown.
		 * @since 1.4
		 */
		public EncoderException(Throwable cause) {
			super(cause);
		}
	}

}
