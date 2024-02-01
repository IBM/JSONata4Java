/**
 * (c) Copyright 2018, 2019 IBM Corporation
 * 1 New Orchard Road, 
 * Armonk, New York, 10504-1722
 * United States
 * +1 914 499 1900
 * support: Nathaniel Mills wnm3@us.ibm.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.api.jsonata4java;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InvalidObjectException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
//import java.rmi.dgc.VMID;
import java.util.UUID;
import javax.management.modelmbean.InvalidTargetObjectTypeException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.NullNode;

public class JSONataUtils implements Serializable {

    static private final long serialVersionUID = 8109772978213632637L;

    static public final DecimalFormat FMT = new DecimalFormat("#0.000");

    static public final byte[] HEX_BYTES = new byte[] {
        48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 65, 66, 67, 68,
        69, 70
    };

    static public final int DAY_MS = 86400000;

    static public final int HOUR_MS = 3600000;

    static public final int MIN_MS = 60000;

    static public final String HEX_CHARS = "0123456789ABCDEF";

    /**
     * If this class is initialized in a different JVM is started at the same
     * millisecond, its s_r will generate the same sequence of random numbers.
     */
    static public Random SEED_RANDOM = new Random();

    static public SecureRandom SEED_SECURE_RANDOM = null;

    static {
        try {
            SEED_SECURE_RANDOM = SecureRandom.getInstance("SHA1PRNG");
            SEED_SECURE_RANDOM.setSeed(UUID.randomUUID().toString().getBytes());
        } catch (NoSuchAlgorithmException nsae) {
            System.out.println("Unable to retrieve algorithm SHA1PRNG for unique id generation.");
        }
    }

    /**
     * Close a buffered reader opened using {@link #openTextFile(String)}
     * 
     * @param br buffered reader to be closed
     */
    public static void closeTextFile(BufferedReader br) {
        if (br != null) {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Converts a byte into an array of char's containing the hexadecimal digits.
     * For example, 0x2f would return char[] {'2','F'}
     * 
     * @param bIn byte to be converted to hexadecimal digits
     * @return array of chars containing the hexadecimal digits for the value of the
     *         input byte.
     */
    static public char[] byteToHexChars(byte bIn) {
        char[] cOut = new char[2];
        int iMasker = bIn & 0x000000ff;
        int iMaskerHigh = iMasker & 0x000000f0;
        iMaskerHigh = iMaskerHigh >> 4;
        int iMaskerLow = iMasker & 0x0000000f;
        if (iMaskerHigh > 9) {
            iMaskerHigh = iMaskerHigh + 'A' - 10;
        } else {
            iMaskerHigh = iMaskerHigh + '0';
        }
        if (iMaskerLow > 9) {
            iMaskerLow = iMaskerLow + 'A' - 10;
        } else {
            iMaskerLow = iMaskerLow + '0';
        }
        cOut[0] = (char) iMaskerHigh;
        cOut[1] = (char) iMaskerLow;
        return cOut;
    }

    /**
     * @return a 40 byte String random number based on invoking the
     *         com.ibm.crypto.fips.provider.SecureRandom class.
     */
    static public synchronized String getUniqueID() {
        byte[] byteID = new byte[20];
        if (SEED_SECURE_RANDOM != null) {
            SEED_SECURE_RANDOM.nextBytes(byteID);
            return hexEncode(byteID);
        }
        // otherwise, use a less sophisticated generator.
        StringBuffer sb = new StringBuffer();
        sb.append("X"); // distinguish from s_sr generated.
        sb.append(Long.toHexString(System.currentTimeMillis()));
        sb.append(Long.toHexString(SEED_RANDOM.nextLong()));
        return sb.toString();
    }

    /**
     * Transform the string of hexadecimal digits into a byte array.
     * 
     * @param strHex a String containing pairs of hexadecimal digits
     * @return a byte array created by transforming pairs of hexadecimal digits into
     *         a byte. For example "7F41" would become byte [] { 0x7f, 0x41}
     * @throws InvalidParameterException thrown if the input string is null or
     *                                   empty, or if it does not contain an even
     *                                   number of hexadecimal digits, or if it
     *                                   contains something other than a hexadecimal
     *                                   digit.
     */
    static public byte[] hexDecode(String strHex) throws InvalidParameterException {
        if (strHex == null || strHex.length() == 0) {
            throw new InvalidParameterException(
                "Null or empty string passed.  Must pass string containing pairs of hexadecimal digits.");
        }
        int iLength = strHex.length();
        if (iLength % 2 > 0) {
            throw new InvalidParameterException(
                "An odd number of bytes was passed in the input string.  Must be an even number.");
        }
        byte[] inBytes = strHex.toUpperCase().getBytes(StandardCharsets.UTF_8);

        byte[] baRC = new byte[iLength / 2];
        int iHighOffset = -1;
        int iLowOffset = -1;
        for (int i = 0; i < iLength; i += 2) {
            iHighOffset = HEX_CHARS.indexOf((int) inBytes[i]);
            if (iHighOffset < 0) {
                throw new InvalidParameterException(
                    "Input string contains non-hexadecimal digit at index " + i + ".  Must be 0-9 or A-F");
            }
            iLowOffset = HEX_CHARS.indexOf((int) inBytes[i + 1]);
            if (iLowOffset < 0) {
                throw new InvalidParameterException(
                    "Input string contains non-hexadecimal digit at index " + i + ".  Must be 0-9 or A-F");
            }
            baRC[i / 2] = (byte) ((iHighOffset * 16) + iLowOffset);
        }
        return baRC;
    }

    /**
     * Convert the byte array into a String of hexadecimal digits. For example, the
     * bytes[] {0x31,0x0a} would become "310A".
     * 
     * @param bArray the array of bytes to be converted.
     * @return a String of hexadecimal digits formed by the hexadecimal digit for
     *         each nibble of the byte.
     */
    static public String hexEncode(byte[] bArray) {
        StringBuffer sb = new StringBuffer();
        // check bad input
        if (bArray == null || bArray.length == 0) {
            return sb.toString();
        }
        // else do real work
        char[] cHexPair = new char[2];
        int iByteCount = 0;
        int iArrayLength = bArray.length;
        while (iByteCount < iArrayLength) {
            cHexPair = byteToHexChars(bArray[iByteCount]);
            sb.append(new String(cHexPair));
            iByteCount++;
        } // end while
        return sb.toString();
    }

    /**
     * @param fqFilename fully qualified name of the text file to be opened
     * @return open buffered reader to allow individual lines of a text file to be
     *         read
     * @throws Exception if the file cannot be found
     * @see #closeTextFile(BufferedReader) to close the reader returned by this
     *      function
     */
    public static BufferedReader openTextFile(String fqFilename) throws Exception {
        BufferedReader input = null;
        File inputFile = new File(fqFilename);
        if (inputFile.exists() == false) {
            throw new Exception(inputFile.getCanonicalPath() + " does not exist.");
        }
        if (inputFile.isFile() == false) {
            throw new IOException(
                "Input is not a file: " + inputFile.getCanonicalPath() + File.separator + inputFile.getName());
        }
        if (inputFile.canRead() == false) {
            throw new IOException(
                "Cannot read file " + inputFile.getCanonicalPath() + File.separator + inputFile.getName());
        }
        input = new BufferedReader(new FileReader(inputFile));
        return input;
    }

    /**
     * Print the supplied prompt (if not null) and return the trimmed response
     * 
     * @param strPrompt the prompt to be displayed
     * @return the trimmed response to the prompt (may be the empty String ("") if
     *         nothing entered)
     */
    static public String prompt(String strPrompt) {
        return prompt(strPrompt, true);
    }

    /**
     * Print the supplied prompt (if not null) and return the trimmed response
     * according to the supplied trim control
     * 
     * @param strPrompt the prompt to be displayed
     * @param bTrim whether or not to trim the input
     * @return the trimmed response (if so commanded) to the prompt (may be the
     *         empty String ("") if nothing entered)
     */
    static public String prompt(String strPrompt, boolean bTrim) {
        String strReply = "";
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            if ((strPrompt != null) && (strPrompt.length() != 0)) {
                System.out.println(strPrompt);
            }
            strReply = in.readLine();
            if (bTrim && strReply != null) {
                strReply = strReply.trim();
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return strReply;
    }

    /**
     * Save the specified JSONObject in serialized form to the specified file or
     * throw the appropriate exception.
     * 
     * @param jsonFileName fully qualified name of the JSON file to be saved
     * @param jsonData     the JSONObject to be saved to a file.
     * @return the jsonData that was saved
     * @throws Exception {@link IOException}) if there is a problem writing the file
     */
    static public JsonNode saveJSONFile(String jsonFileName, JsonNode jsonData) throws Exception {
        if (jsonData == null) {
            throw new InvalidObjectException("jsonData is null");
        }
        if (jsonFileName == null || jsonFileName.trim().length() == 0) {
            throw new InvalidTargetObjectTypeException("Output filename is null or empty.");
        }
        BufferedWriter br = null;
        try {
            File outputFile = new File(jsonFileName);
            // write the JSON file
            br = new BufferedWriter(new FileWriter(outputFile));
            ObjectMapper mapper = new ObjectMapper();
            br.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonData));
        } catch (IOException e) {
            throw new IOException("Cannot write file \"" + jsonFileName + "\"", e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                // error trying to close writer ...
            }
        }

        return jsonData;
    }

    /**
     * Construct and return a sorted list of files in a directory identified by the
     * dir that have extensions matching the ext
     * 
     * @param dir the path to the directory containing files to be returned in the
     *            list
     * @param ext the file extension (without the leading period) used to filter
     *            files in the dir
     * @return sorted list of files in a directory identified by the dir that have
     *         extensions matching the ext
     * @throws IOException if there is difficulty accessing the files in the
     *                     supplied dir
     */
    static public List<Path> listSourceFiles(Path dir, String ext) throws IOException {
        List<Path> result = new ArrayList<Path>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.{" + ext + "}")) {
            for (Path entry : stream) {
                result.add(entry);
            }
        } catch (DirectoryIteratorException ex) {
            // I/O error encountered during the iteration, the cause is an
            // IOException
            throw ex.getCause();
        }
        result.sort(null);
        return result;
    }

    /**
     * Load the specified JSON file from the fully qualified file name or throw the
     * appropriate exception.
     * 
     * @param jsonFQFileName name of the JSON file to be loaded
     * @return the JSONObject contained in the file, or an empty JSONObject if no
     *         object exists
     * @throws Exception If the file can no be located, or if there is a problem
     *                   reading the file
     */
    static public JsonNode loadJSONFile(String jsonFQFileName) throws Exception {
        JsonNode retObj = JsonNodeFactory.instance.nullNode();
        BufferedReader br = null;
        try {
            br = openTextFile(jsonFQFileName);
            if (br != null) {
                ObjectMapper mapper = new ObjectMapper();
                retObj = (JsonNode) mapper.readValue(br, Object.class);

            }
        } catch (IOException ioe) {
            throw new IOException("Cannot parse \"" + jsonFQFileName + "\"", ioe);
        } catch (Exception e) {
            throw new IOException("Cannot load file \"" + jsonFQFileName + "\"", e);
        } finally {
            closeTextFile(br);
        }
        return retObj;
    }

    /**
     * Reads a buffered reader line up to a newline and returns the content read as
     * a String that does not contain the linefeed.
     * 
     * @param br buffered reader
     * @return String containing the characters read through the terminator
     *         character. If the end of file has been reached with nothing available
     *         to be returned, then null is returned.
     * @throws IOException if an error occurs while reading the buffered reader.
     * @see #readLine(BufferedReader, HashSet)
     */
    static public String readLine(BufferedReader br) throws IOException {
        HashSet<Integer> terminators = new HashSet<Integer>();
        terminators.add(10); // newline
        return readLine(br, terminators);
    }

    /**
     * Reads a buffered reader line up to any of the terminator characters (e.g.,
     * 0x0a for newline) and returns the content read as a String that does not
     * contain the terminator.
     * 
     * @param br          buffered reader
     * @param terminators the set of line terminators used to signal return of the
     *                    next "line" from the buffered reader.
     * @return String containing the characters read through the terminator
     *         character. If the end of file has been reached with nothing available
     *         to be returned, then null is returned.
     * @throws IOException if an error occurs while reading the buffered reader.
     */
    static public String readLine(BufferedReader br, HashSet<Integer> terminators) throws IOException {
        StringBuffer sb = new StringBuffer();
        int c;
        c = br.read();
        while (c != -1) {
            if (terminators.contains((Integer) c)) {
                return sb.toString();
            }
            sb.append((char) c);
            c = br.read();
        }
        if (sb.length() > 0) {
            return sb.toString();
        }
        return null;
    }

    static boolean isArrayOfStrings(Object arg) {
        boolean result = false;
        if (arg != null && arg.getClass().isArray()) {
            // two possibilities, String[] or Object[] with all content instances
            // of String
            if (arg instanceof String[]) {
                result = true;
            } else if (arg instanceof Object[]) {
                Object[] testArray = (Object[]) arg;
                boolean tester = true;
                for (Object test : testArray) {
                    if (test instanceof String == false) {
                        tester = false;
                        break;
                    }
                }
                result = tester;
            }
        }
        return result;
    }

    static boolean isArrayOfNumbers(Object arg) {
        boolean result = false;
        if (arg != null && arg.getClass().isArray()) {
            // two possibilities, String[] or Object[] with all content instances
            // of String
            if (arg instanceof Number[]) {
                result = true;
            } else if (arg instanceof Object[]) {
                Object[] testArray = (Object[]) arg;
                boolean tester = true;
                for (Object test : testArray) {
                    if (test instanceof Number == false) {
                        tester = false;
                        break;
                    }
                }
                result = tester;
            } else if (arg instanceof int[] || arg instanceof long[] || arg instanceof float[]
                || arg instanceof double[]) {
                result = true;
            }
        }
        return result;
    }

    public static Sequence createSequence(JsonNode... arguments) {
        Sequence seq = new Sequence();
        if (arguments != null && arguments.length == 1) {
            seq.push(arguments[0]);
        }
        return seq;
    }

    public static boolean isSequence(Object value) {
        return (value instanceof Sequence);
    }

    public static void main(String[] args) throws Exception {
        int[] intArray = new int[] {
            -1, 0, 1, 2, 3
        };
        Integer[] integerArray = new Integer[] {
            -1, 0, 1, 2, 3
        };
        String[] strArray = new String[] {
            "a", "b", "c", "x", "y", "z"
        };
        Object[] objArray = new Object[] {
            "a", "b", "c", "x", "y", "z"
        };
        Object[] mixedArray = new Object[] {
            "a", "b", "c", -1, 0, 1, 2, 3
        };

        System.out.println("intArray isArrayOfStrings = " + isArrayOfStrings(intArray));
        System.out.println("integerArray isArrayOfStrings = " + isArrayOfStrings(integerArray));
        System.out.println("strArray isArrayOfStrings = " + isArrayOfStrings(strArray));
        System.out.println("objArray isArrayOfStrings = " + isArrayOfStrings(objArray));
        System.out.println("mixedArray isArrayOfStrings = " + isArrayOfStrings(mixedArray));

        System.out.println("intArray isArrayOfNumbers = " + isArrayOfNumbers(intArray));
        System.out.println("integerArray isArrayOfNumbers = " + isArrayOfNumbers(integerArray));
        System.out.println("strArray isArrayOfNumbers = " + isArrayOfNumbers(strArray));
        System.out.println("objArray isArrayOfNumbers = " + isArrayOfNumbers(objArray));
        System.out.println("mixedArray isArrayOfNumbers = " + isArrayOfNumbers(mixedArray));

        System.out.println("createSequence()=" + createSequence());
        System.out.println("createSequence(null)=" + createSequence((JsonNode[]) null));
        System.out.println("createSequence(JsonNodeFactory.instance.objectNode())="
            + createSequence(JsonNodeFactory.instance.objectNode()));
        System.out.println("createSequence(new JSONObject())=" + createSequence(JsonNodeFactory.instance.objectNode()));
        System.out.println("createSequence(new JSONArray())=" + createSequence(JsonNodeFactory.instance.arrayNode()));
        System.out.println("createSequence(NullNode.instance)=" + createSequence(NullNode.instance));

    }

    /**
    * Decodes the passed UTF-8 String using an algorithm that's compatible with
    * JavaScript's <code>decodeURIComponent</code> function. Returns
    * <code>null</code> if the String is <code>null</code>.
    * 
    * @param s
    *           The UTF-8 encoded String to be decoded
    * @return the decoded String
    * @throws UnsupportedEncodingException 
    */
    public static String decodeURIComponent(String s) throws UnsupportedEncodingException {
        if (s == null) {
            return null;
        }

        String result = null;

        result = URLDecoder.decode(s, "UTF-8");

        return result;
    }

    /**
    * Encodes the passed String as UTF-8 using an algorithm that's compatible
    * with JavaScript's <code>encodeURIComponent</code> function. Returns
    * <code>null</code> if the String is <code>null</code>.
    * 
    * @param s
    *           The String to be encoded
    * @return the encoded String
    * @throws URISyntaxException 
    */
    public static String encodeURIComponent(String s) throws URISyntaxException {
        if (s == null) {
            return null;
        }
        String test = new URI(null, null, s, null).getRawPath();
        if (test.equals(s)) {
            try {
                // https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/encodeURI
                // Not encoded: A-Z a-z 0-9 ; , / ? : @ & = + $ - _ . ! ~ * ' ( ) #
                test = URLEncoder.encode(s, "UTF-8").replaceAll("\\+", "%20")
                    .replaceAll("%20", " ").replaceAll("\\%21", "!")
                    .replaceAll("\\%27", "'").replaceAll("\\%28", "(")
                    .replaceAll("\\%29", ")").replaceAll("\\%2A", "*")
                    .replaceAll("\\%2D", "-").replaceAll("\\%2E", ".")
                    .replaceAll("\\%5F", "_").replaceAll("\\%7E", "~");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        test = test.replaceAll("=", "%3D"); // overrides standard to match jsonata
        return test;
    }

    /**
     * 
     * @param str
     * @param start  Location at which to begin extracting characters. If a negative
     *               number is given, it is treated as strLength - start where
     *               strLength is the length of the string. For example,
     *               str.substr(-3) is treated as str.substr(str.length - 3)
     * @param length The number of characters to extract. If this argument is null,
     *               all the characters from start to the end of the string are
     *               extracted.
     * @return A new string containing the extracted section of the given string. If
     *         length is 0 or a negative number, an empty string is returned.
     */
    static public String substr(String str, Integer start, Integer length) {

        // below has to convert start and length for emojis and unicode
        int origLen = str.length();

        String strData = Objects.requireNonNull(str).intern();
        int strLen = strData.codePointCount(0, strData.length());
        if (start >= strLen) {
            return "";
        }
        // If start is negative, substr() uses it as a character index from the
        // end of the string; the index of the last character is -1.
        start = strData.offsetByCodePoints(0, start >= 0 ? start : ((strLen + start) < 0 ? 0 : strLen + start));
        if (start < 0) {
            start = 0;
        } // If start is negative and abs(start) is larger than the length of the
        // string, substr() uses 0 as the start index.
        // If length is omitted, substr() extracts characters to the end of the
        // string.
        if (length == null) {
            length = strData.length();
        } else if (length < 0) {
            // If length is 0 or negative, substr() returns an empty string.
            return "";
        } else if (length > strData.length()) {
            length = strData.length();
        }

        length = strData.offsetByCodePoints(0, length);

        if (start >= 0) {
            // If start is positive and is greater than or equal to the length of
            // the string, substr() returns an empty string.
            if (start >= origLen) {
                return "";
            }
        }

        // collect length characters (unless it reaches the end of the string
        // first, in which case it will return fewer)
        int end = start + length;
        if (end > origLen) {
            end = origLen;
        }

        return strData.substring(start, end);
    }

}
