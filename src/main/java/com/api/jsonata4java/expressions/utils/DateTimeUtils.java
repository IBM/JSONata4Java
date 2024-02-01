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
package com.api.jsonata4java.expressions.utils;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.IsoFields;
import java.time.temporal.WeekFields;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TimeZone;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.api.jsonata4java.expressions.EvaluateRuntimeException;

public class DateTimeUtils implements Serializable {

    private static final long serialVersionUID = 365963860104380193L;

    private static String[] few = {
        "Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten",
        "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"
    };
    private static String[] ordinals = {
        "Zeroth", "First", "Second", "Third", "Fourth", "Fifth", "Sixth", "Seventh", "Eighth", "Ninth", "Tenth",
        "Eleventh", "Twelfth", "Thirteenth", "Fourteenth", "Fifteenth", "Sixteenth", "Seventeenth", "Eighteenth", "Nineteenth"
    };
    private static String[] decades = {
        "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety", "Hundred"
    };
    private static String[] magnitudes = {
        "Thousand", "Million", "Billion", "Trillion"
    };

    public static String numberToWords(int value, boolean ordinal) {
        return lookup(value, false, ordinal);
    }

    private static String lookup(int num, boolean prev, boolean ord) {
        String words = "";
        if (num <= 19) {
            words = (prev ? " and " : "") + (ord ? ordinals[num] : few[num]);
        } else if (num < 100) {
            int tens = num / 10;
            int remainder = num % 10;
            words = (prev ? " and " : "") + decades[tens - 2];
            if (remainder > 0) {
                words += "-" + lookup(remainder, false, ord);
            } else if (ord) {
                words = words.substring(0, words.length() - 1) + "ieth";
            }
        } else if (num < 1000) {
            int hundreds = num / 100;
            int remainder = num % 100;
            words = (prev ? ", " : "") + few[hundreds] + " Hundred";
            if (remainder > 0) {
                words += lookup(remainder, true, ord);
            } else if (ord) {
                words += "th";
            }
        } else {
            int mag = (int) Math.floor(Math.log10(num) / 3);
            if (mag > magnitudes.length) {
                mag = magnitudes.length; // the largest word
            }
            int factor = (int) Math.pow(10, mag * 3);
            int mant = (int) Math.floor(num / factor);
            int remainder = num - mant * factor;
            words = (prev ? ", " : "") + lookup(mant, false, false) + " " + magnitudes[mag - 1];
            if (remainder > 0) {
                words += lookup(remainder, true, ord);
            } else if (ord) {
                words += "th";
            }
        }
        return words;
    }

    private static Map<String, Integer> wordValues = new HashMap<>();
    static {
        for (int i = 0; i < few.length; i++) {
            wordValues.put(few[i].toLowerCase(), i);
        }
        for (int i = 0; i < ordinals.length; i++) {
            wordValues.put(ordinals[i].toLowerCase(), i);
        }
        for (int i = 0; i < decades.length; i++) {
            String lword = decades[i].toLowerCase();
            wordValues.put(lword, (i + 2) * 10);
            wordValues.put(lword.substring(0, lword.length() - 1) + "ieth", wordValues.get(lword));
        }
        wordValues.put("hundredth", 100);
        for (int i = 0; i < magnitudes.length; i++) {
            String lword = magnitudes[i].toLowerCase();
            int val = (int) Math.pow(10, (i + 1) * 3);
            wordValues.put(lword, val);
            wordValues.put(lword + "th", val);
        }
    }

    private static int wordsToNumber(String text) {
        String[] parts = text.split(",\\s|\\sand\\s|[\\s\\-]");
        Integer[] values = new Integer[parts.length];
        for (int i = 0; i < parts.length; i++) {
            values[i] = wordValues.get(parts[i]);
        }
        Stack<Integer> segs = new Stack<>();
        segs.push(0);
        for (Integer value : values) {
            if (value < 100) {
                Integer top = segs.pop();
                if (top >= 1000) {
                    segs.push(top);
                    top = 0;
                }
                segs.push(top + value);
            } else {
                segs.push(segs.pop() * value);
            }
        }
        return segs.stream().mapToInt(i -> i.intValue()).sum();
    }

    private static class RomanNumeral {

        private int value;
        private String letters;

        public RomanNumeral(int value, String letters) {
            this.value = value;
            this.letters = letters;
        }

        public int getValue() {
            return this.value;
        }

        public String getLetters() {
            return this.letters;
        }
    }

    private static RomanNumeral[] romanNumerals = {
        new RomanNumeral(1000, "m"),
        new RomanNumeral(900, "cm"),
        new RomanNumeral(500, "d"),
        new RomanNumeral(400, "cd"),
        new RomanNumeral(100, "c"),
        new RomanNumeral(90, "xc"),
        new RomanNumeral(50, "l"),
        new RomanNumeral(40, "xl"),
        new RomanNumeral(10, "x"),
        new RomanNumeral(9, "ix"),
        new RomanNumeral(5, "v"),
        new RomanNumeral(4, "iv"),
        new RomanNumeral(1, "i")
    };

    private static Map<String, Integer> romanValues = createRomanValues();

    private static Map<String, Integer> createRomanValues() {
        Map<String, Integer> values = new HashMap<>();
        values.put("M", 1000);
        values.put("D", 500);
        values.put("C", 100);
        values.put("L", 50);
        values.put("X", 10);
        values.put("V", 5);
        values.put("I", 1);
        return values;
    }

    private static String decimalToRoman(int value) {
        for (int i = 0; i < romanNumerals.length; i++) {
            RomanNumeral numeral = romanNumerals[i];
            if (value >= numeral.getValue()) {
                return numeral.getLetters() + decimalToRoman(value - numeral.getValue());
            }
        }
        return "";
    }

    private static int romanToDecimal(String roman) {
        int decimal = 0;
        int max = 1;
        for (int i = roman.length() - 1; i >= 0; i--) {
            String digit = Character.toString(roman.charAt(i));
            int value = romanValues.get(digit);
            if (value < max) {
                decimal -= value;
            } else {
                max = value;
                decimal += value;
            }
        }
        return decimal;
    }

    private static String decimalToLetters(int value, String aChar) {
        Vector<String> letters = new Vector<>();
        char aCode = aChar.charAt(0);
        while (value > 0) {
            letters.insertElementAt(Character.toString((char) ((value - 1) % 26 + aCode)), 0);
            value = (value - 1) / 26;
        }
        return letters.stream().reduce("", (a, b) -> a + b);
    }

    private static String formatInteger(int value, String picture) {
        Format format = analyseIntegerPicture(picture);
        return formatInteger(value, format);
    }

    enum formats {

            DECIMAL("decimal"), LETTERS("letters"), ROMAN("roman"), WORDS("words"), SEQUENCE("sequence");

        public String value;

        private formats(String value) {
            this.value = value;
        }
    }

    enum tcase {

            UPPER("upper"), LOWER("lower"), TITLE("title");

        public String value;

        private tcase(String value) {
            this.value = value;
        }
    }

    private static class Format {

        @SuppressWarnings("unused")
        public String type = "integer";
        public formats primary = formats.DECIMAL;
        public tcase case_type = tcase.LOWER;
        public boolean ordinal = false;
        public int zeroCode = 0;
        public int mandatoryDigits = 0;
        public int optionalDigits = 0;
        public boolean regular = false;
        public Vector<GroupingSeparator> groupingSeparators = new Vector<>();
        public String token;
    }

    private static class GroupingSeparator {

        public int position;
        public String character;

        public GroupingSeparator(int position, String character) {
            this.position = position;
            this.character = character;
        }
    }

    private static Map<String, String> suffix123 = createSuffixMap();

    private static Map<String, String> createSuffixMap() {
        Map<String, String> suffix = new HashMap<>();
        suffix.put("1", "st");
        suffix.put("2", "nd");
        suffix.put("3", "rd");
        return suffix;
    }

    private static String formatInteger(int value, Format format) {
        String formattedInteger = "";
        boolean negative = value < 0;
        value = Math.abs(value);
        switch (format.primary) {
            case LETTERS:
                formattedInteger = decimalToLetters(value, format.case_type == tcase.UPPER ? "A" : "a");
                break;
            case ROMAN:
                formattedInteger = decimalToRoman(value);
                if (format.case_type == tcase.UPPER) {
                    formattedInteger = formattedInteger.toUpperCase();
                }
                break;
            case WORDS:
                formattedInteger = numberToWords(value, format.ordinal);
                if (format.case_type == tcase.UPPER) {
                    formattedInteger = formattedInteger.toUpperCase();
                } else if (format.case_type == tcase.LOWER) {
                    formattedInteger = formattedInteger.toLowerCase();
                }
                break;
            case DECIMAL:
                formattedInteger = "" + value;
                int padLength = format.mandatoryDigits - formattedInteger.length();
                if (padLength > 0) {
                    formattedInteger = StringUtils.leftPad(formattedInteger, format.mandatoryDigits, '0');
                }
                if (format.zeroCode != 0x30) {
                    char[] chars = formattedInteger.toCharArray();
                    for (int i = 0; i < chars.length; i++) {
                        chars[i] = (char) (chars[i] + format.zeroCode - 0x30);
                    }
                    formattedInteger = new String(chars);
                }
                if (format.regular) {
                    int n = (formattedInteger.length() - 1) / format.groupingSeparators.elementAt(0).position;
                    for (int i = n; i > 0; i--) {
                        int pos = formattedInteger.length() - i * format.groupingSeparators.elementAt(0).position;
                        formattedInteger = formattedInteger.substring(0, pos) + format.groupingSeparators.elementAt(0).character + formattedInteger.substring(pos);
                    }
                } else {
                    Collections.reverse(format.groupingSeparators);
                    for (GroupingSeparator separator : format.groupingSeparators) {
                        int pos = formattedInteger.length() - separator.position;
                        formattedInteger = formattedInteger.substring(0, pos) + separator.character + formattedInteger.substring(pos);
                    }
                }

                if (format.ordinal) {
                    String lastDigit = formattedInteger.substring(formattedInteger.length() - 1);
                    String suffix = suffix123.get(lastDigit);
                    if (suffix == null || (formattedInteger.length() > 1 && formattedInteger.charAt(formattedInteger.length() - 2) == '1')) {
                        suffix = "th";
                    }
                    formattedInteger += suffix;
                }
                break;
            case SEQUENCE:
                throw new EvaluateRuntimeException(String.format(Constants.ERR_MSG_SEQUENCE_UNSUPPORTED, format.token));
        }
        if (negative) {
            formattedInteger = "-" + formattedInteger;
        }

        return formattedInteger;
    }

    private static int[] decimalGroups = {
        0x30, 0x0660, 0x06F0, 0x07C0, 0x0966, 0x09E6, 0x0A66, 0x0AE6, 0x0B66, 0x0BE6, 0x0C66, 0x0CE6, 0x0D66, 0x0DE6, 0x0E50, 0x0ED0, 0x0F20, 0x1040, 0x1090, 0x17E0, 0x1810,
        0x1946, 0x19D0, 0x1A80, 0x1A90, 0x1B50, 0x1BB0, 0x1C40, 0x1C50, 0xA620, 0xA8D0, 0xA900, 0xA9D0, 0xA9F0, 0xAA50, 0xABF0, 0xFF10
    };

    @SuppressWarnings("unused")
    private static Format analyseIntegerPicture(String picture) {
        Format format = new Format();
        String primaryFormat, formatModifier;
        int semicolon = picture.lastIndexOf(";");
        if (semicolon == -1) {
            primaryFormat = picture;
        } else {
            primaryFormat = picture.substring(0, semicolon);
            formatModifier = picture.substring(semicolon + 1);
            if (formatModifier.charAt(0) == 'o') {
                format.ordinal = true;
            }
        }

        switch (primaryFormat) {
            case "A":
                format.case_type = tcase.UPPER;
            case "a":
                format.primary = formats.LETTERS;
                break;
            case "I":
                format.case_type = tcase.UPPER;
            case "i":
                format.primary = formats.ROMAN;
                break;
            case "W":
                format.case_type = tcase.UPPER;
                format.primary = formats.WORDS;
                break;
            case "Ww":
                format.case_type = tcase.TITLE;
                format.primary = formats.WORDS;
                break;
            case "w":
                format.primary = formats.WORDS;
                break;
            default: {
                Integer zeroCode = null;
                int mandatoryDigits = 0;
                int optionalDigits = 0;
                Vector<GroupingSeparator> groupingSeparators = new Vector<>();
                int separatorPosition = 0;
                char[] formatCodepoints = primaryFormat.toCharArray();
                ArrayUtils.reverse(formatCodepoints);
                for (char codePoint : formatCodepoints) {
                    boolean digit = false;
                    for (int i = 0; i < decimalGroups.length; i++) {
                        int group = decimalGroups[i];
                        if (codePoint >= group && codePoint <= group + 9) {
                            digit = true;
                            mandatoryDigits++;
                            separatorPosition++;
                            if (zeroCode == null) {
                                zeroCode = group;
                            } else if (group != zeroCode) {
                                throw new EvaluateRuntimeException(Constants.ERR_MSG_DIFF_DECIMAL_GROUP);
                            }
                        }
                        break;
                    }
                    if (!digit) {
                        if (codePoint == 0x23) {
                            separatorPosition++;
                            optionalDigits++;
                        } else {
                            groupingSeparators.add(new GroupingSeparator(separatorPosition, Character.toString(codePoint)));
                        }
                    }
                }
                if (mandatoryDigits > 0) {
                    format.primary = formats.DECIMAL;
                    format.zeroCode = zeroCode;
                    format.mandatoryDigits = mandatoryDigits;
                    format.optionalDigits = optionalDigits;

                    int regular = getRegularRepeat(groupingSeparators);
                    if (regular > 0) {
                        format.regular = true;
                        format.groupingSeparators.add(new GroupingSeparator(regular, groupingSeparators.elementAt(0).character));
                    } else {
                        format.regular = false;
                        format.groupingSeparators = groupingSeparators;
                    }
                } else {
                    format.primary = formats.SEQUENCE;
                    format.token = primaryFormat;
                }
            }

        }

        return format;
    }

    private static int getRegularRepeat(Vector<GroupingSeparator> separators) {
        if (separators.size() == 0) {
            return 0;
        }

        String sepChar = separators.elementAt(0).character;
        for (int i = 1; i < separators.size(); i++) {
            if (!separators.elementAt(i).character.equals(sepChar)) {
                return 0;
            }
        }

        List<Integer> indexes = separators.stream().map(separator -> separator.position).collect(Collectors.toList());
        int factor = indexes.stream()
            .reduce((a, b) -> BigInteger.valueOf(a).gcd(BigInteger.valueOf(b)).intValue())
            .get();
        for (int index = 1; index <= indexes.size(); index++) {
            if (indexes.indexOf(index * factor) == -1) {
                return 0;
            }
        }
        return factor;
    }

    private static Map<Character, String> defaultPresentationModifiers = createDefaultPresentationModifiers();

    private static Map<Character, String> createDefaultPresentationModifiers() {
        Map<Character, String> map = new HashMap<>();
        map.put('Y', "1");
        map.put('M', "1");
        map.put('D', "1");
        map.put('d', "1");
        map.put('F', "n");
        map.put('W', "1");
        map.put('w', "1");
        map.put('X', "1");
        map.put('x', "1");
        map.put('H', "1");
        map.put('h', "1");
        map.put('P', "n");
        map.put('m', "01");
        map.put('s', "01");
        map.put('f', "1");
        map.put('Z', "01:01");
        map.put('z', "01:01");
        map.put('C', "n");
        map.put('E', "n");
        return map;
    }

    private static class PictureFormat {

        @SuppressWarnings("unused")
        String type;
        Vector<SpecPart> parts = new Vector<>();

        public PictureFormat(String type) {
            this.type = type;
        }

        public void addLiteral(String picture, int start, int end) {
            if (end > start) {
                String literal = picture.substring(start, end);
                literal = String.join("]", literal.split("]]"));
                parts.add(new SpecPart("literal", literal));
            }
        }
    }

    private static class SpecPart {

        String type;
        String value;
        char component;
        Pair<Integer, Integer> width;
        String presentation1;
        Character presentation2;
        boolean ordinal = false;
        public tcase names;
        public Format integerFormat;
        public int n;

        public SpecPart(String type, String value) {
            this.type = type;
            this.value = value;
        }

        public SpecPart(String type, char component) {
            this.type = type;
            this.component = component;
        }
    }

    private static PictureFormat analyseDateTimePicture(String picture) {
        PictureFormat format = new PictureFormat("datetime");
        int start = 0, pos = 0;
        while (pos < picture.length()) {
            if (picture.charAt(pos) == '[') {
                //check it's not a doubled [[
                if (picture.charAt(pos + 1) == '[') {
                    //literal [
                    format.addLiteral(picture, start, pos);
                    format.parts.add(new SpecPart("literal", "["));
                    pos += 2;
                    start = pos;
                    continue;
                }
                format.addLiteral(picture, start, pos);
                start = pos;
                pos = picture.indexOf("]", start);
                if (pos == -1) {
                    throw new EvaluateRuntimeException(Constants.ERR_MSG_NO_CLOSING_BRACKET);
                }
                String marker = picture.substring(start + 1, pos);
                marker = String.join("", marker.split("\\s+"));
                SpecPart def = new SpecPart("marker", marker.charAt(0));
                int comma = marker.lastIndexOf(",");
                String presMod;
                if (comma != -1) {
                    String widthMod = marker.substring(comma + 1);
                    int dash = widthMod.indexOf("-");
                    String min, max = null;
                    if (dash == -1) {
                        min = widthMod;
                    } else {
                        min = widthMod.substring(0, dash);
                        max = widthMod.substring(dash + 1);
                    }
                    def.width = new ImmutablePair<Integer, Integer>(parseWidth(min), parseWidth(max));
                    presMod = marker.substring(1, comma);
                } else {
                    presMod = marker.substring(1);
                }
                if (presMod.length() == 1) {
                    def.presentation1 = presMod;
                } else if (presMod.length() > 1) {
                    char lastChar = presMod.charAt(presMod.length() - 1);
                    if ("atco".indexOf(lastChar) != -1) {
                        def.presentation2 = lastChar;
                        if (lastChar == 'o') {
                            def.ordinal = true;
                        }
                        def.presentation1 = presMod.substring(0, presMod.length() - 1);
                    } else {
                        def.presentation1 = presMod;
                    }
                } else {
                    def.presentation1 = defaultPresentationModifiers.get(def.component);
                }
                if (def.presentation1 == null) {
                    throw new EvaluateRuntimeException(String.format(Constants.ERR_MSG_UNKNOWN_COMPONENT_SPECIFIER, def.component));
                }
                if (def.presentation1.charAt(0) == 'n') {
                    def.names = tcase.LOWER;
                } else if (def.presentation1.charAt(0) == 'N') {
                    if (def.presentation1.length() > 1 && def.presentation1.charAt(1) == 'n') {
                        def.names = tcase.TITLE;
                    } else {
                        def.names = tcase.UPPER;
                    }
                } else if ("YMDdFWwXxHhmsf".indexOf(def.component) != -1) {
                    String integerPattern = def.presentation1;
                    if (def.presentation2 == null) {
                        integerPattern += ";" + def.presentation2;
                    }
                    def.integerFormat = analyseIntegerPicture(integerPattern);
                    def.integerFormat.ordinal = def.ordinal;
                    if (def.width != null && def.width.getLeft() != null) {
                        if (def.integerFormat.mandatoryDigits < def.width.getLeft()) {
                            def.integerFormat.mandatoryDigits = def.width.getLeft();
                        }
                    }
                    if (def.component == 'Y') {
                        def.n = -1;
                        if (def.width != null && def.width.getRight() != null) {
                            def.n = def.width.getRight();
                            def.integerFormat.mandatoryDigits = def.n;
                        } else {
                            int w = def.integerFormat.mandatoryDigits + def.integerFormat.optionalDigits;
                            if (w >= 2) {
                                def.n = w;
                            }
                        }
                    }
                }
                if (def.component == 'Z' || def.component == 'z') {
                    def.integerFormat = analyseIntegerPicture(def.presentation1);
                    def.integerFormat.ordinal = def.ordinal;
                }
                format.parts.add(def);
                start = pos + 1;
            }
            pos++;
        }
        format.addLiteral(picture, start, pos);
        return format;
    }

    private static Integer parseWidth(String wm) {
        if (wm == null || wm.equals("*")) {
            return null;
        } else {
            return Integer.parseInt(wm);
        }
    }

    private static String[] days = {
        "", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
    };
    private static String[] months = {
        "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
    };

    private static PictureFormat iso8601Spec = null;

    public static String formatDateTime(long millis, String picture, String timezone) {
        int offsetHours = 0;
        int offsetMinutes = 0;

        if (timezone != null) {
            int offset = Integer.parseInt(timezone);
            offsetHours = offset / 100;
            offsetMinutes = offset % 100;
        }
        PictureFormat formatSpec;
        if (picture == null) {
            if (iso8601Spec == null) {
                iso8601Spec = analyseDateTimePicture("[Y0001]-[M01]-[D01]T[H01]:[m01]:[s01].[f001][Z01:01t]");
            }
            formatSpec = iso8601Spec;
        } else {
            formatSpec = analyseDateTimePicture(picture);
        }

        int offsetMillis = (60 * offsetHours + offsetMinutes) * 60 * 1000;
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis + offsetMillis), ZoneOffset.UTC);
        String result = "";
        for (SpecPart part : formatSpec.parts) {
            if (part.type.equals("literal")) {
                result += part.value;
            } else {
                result += formatComponent(dateTime, part, offsetHours, offsetMinutes);
            }
        }

        return result;
    }

 	public static String formatDateTimeFromZoneId(long millis, String picture, String zoneId)
 	{
 		String workTimezone = zoneId;

 		if (workTimezone == null)
 		{
 			workTimezone = "Z";
 		}

 		TimeZone zone = TimeZone.getTimeZone(workTimezone);
 		ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis), zone.toZoneId());

 		PictureFormat formatSpec;
 		if (picture == null)
 		{
 			if (iso8601Spec == null)
 			{
 				iso8601Spec = analyseDateTimePicture("[Y0001]-[M01]-[D01]T[H01]:[m01]:[s01].[f001][Z01:01t]");
 			}
 			formatSpec = iso8601Spec;
 		}
 		else
 		{
 			formatSpec = analyseDateTimePicture(picture);
 		}

 		String result = "";
 		for (SpecPart part : formatSpec.parts)
 		{
 			if (part.type.equals("literal"))
 			{
 				result += part.value;
 			}
 			else
 			{
 				result += formatComponent(zonedDateTime, part);
 			}
 		}

 		return result;
 	}

    private static String formatComponent(LocalDateTime date, SpecPart markerSpec, int offsetHours, int offsetMinutes) {
        String componentValue = getDateTimeFragment(date, markerSpec.component);

        if ("YMDdFWwXxHhms".indexOf(markerSpec.component) != -1) {
            if (markerSpec.component == 'Y') {
                if (markerSpec.n != -1) {
                    componentValue = "" + (int) (Integer.parseInt(componentValue) % Math.pow(10, markerSpec.n));
                }
            }
            if (markerSpec.names != null) {
                if (markerSpec.component == 'M' || markerSpec.component == 'x') {
                    componentValue = months[Integer.parseInt(componentValue) - 1];
                } else if (markerSpec.component == 'F') {
                    componentValue = days[Integer.parseInt(componentValue)];
                } else {
                    throw new EvaluateRuntimeException(String.format(Constants.ERR_MSG_INVALID_NAME_MODIFIER, markerSpec.component));
                }
                if (markerSpec.names == tcase.UPPER) {
                    componentValue = componentValue.toUpperCase();
                } else if (markerSpec.names == tcase.LOWER) {
                    componentValue = componentValue.toLowerCase();
                }
                if (markerSpec.width != null && componentValue.length() > markerSpec.width.getRight()) {
                    componentValue = componentValue.substring(0, markerSpec.width.getRight());
                }
            } else {
                componentValue = formatInteger(Integer.parseInt(componentValue), markerSpec.integerFormat);
            }
        } else if (markerSpec.component == 'f') {
            componentValue = formatInteger(Integer.parseInt(componentValue), markerSpec.integerFormat);
        } else if (markerSpec.component == 'Z' || markerSpec.component == 'z') {
            int offset = offsetHours * 100 + offsetMinutes;
            if (markerSpec.integerFormat.regular) {
                componentValue = formatInteger(offset, markerSpec.integerFormat);
            } else {
                int numDigits = markerSpec.integerFormat.mandatoryDigits;
                if (numDigits == 1 || numDigits == 2) {
                    componentValue = formatInteger(offsetHours, markerSpec.integerFormat);
                    if (offsetMinutes != 0) {
                        componentValue += ":" + formatInteger(offsetMinutes, "00");
                    }
                } else if (numDigits == 3 || numDigits == 4) {
                    componentValue = formatInteger(offset, markerSpec.integerFormat);
                } else {
                    throw new EvaluateRuntimeException(Constants.ERR_MSG_TIMEZONE_FORMAT);
                }
            }
            if (offset >= 0) {
                componentValue = "+" + componentValue;
            }
            if (markerSpec.component == 'z') {
                componentValue = "GMT" + componentValue;
            }
            if (offset == 0 && markerSpec.presentation2 != null && markerSpec.presentation2 == 't') {
                componentValue = "Z";
            }
        } else if (markerSpec.component == 'P') {
            // §9.8.4.7 Formatting Other Components
            // Formatting P for am/pm
            // getDateTimeFragment() always returns am/pm lower case so check for UPPER here
            if (markerSpec.names == tcase.UPPER) {
                componentValue = componentValue.toUpperCase();
            }
        }
        return componentValue;
    }

 	private static String formatComponent(ZonedDateTime date, SpecPart markerSpec)
 	{
 		String componentValue = getDateTimeFragment(date, markerSpec.component);

 		if ("YMDdFWwXxHhms".indexOf(markerSpec.component) != -1)
 		{
 			if (markerSpec.component == 'Y')
 			{
 				if (markerSpec.n != -1)
 				{
 					componentValue = "" + (int)(Integer.parseInt(componentValue) % Math.pow(10, markerSpec.n));
 				}
 			}
 			if (markerSpec.names != null)
 			{
 				if (markerSpec.component == 'M' || markerSpec.component == 'x')
 				{
 					componentValue = months[Integer.parseInt(componentValue) - 1];
 				}
 				else if (markerSpec.component == 'F')
 				{
 					componentValue = days[Integer.parseInt(componentValue)];
 				}
 				else
 				{
 					throw new EvaluateRuntimeException(String.format(Constants.ERR_MSG_INVALID_NAME_MODIFIER, markerSpec.component));
 				}
 				if (markerSpec.names == tcase.UPPER)
 				{
 					componentValue = componentValue.toUpperCase();
 				}
 				else if (markerSpec.names == tcase.LOWER)
 				{
 					componentValue = componentValue.toLowerCase();
 				}
 				if (markerSpec.width != null && componentValue.length() > markerSpec.width.getRight())
 				{
 					componentValue = componentValue.substring(0, markerSpec.width.getRight());
 				}
 			}
 			else
 			{
 				componentValue = formatInteger(Integer.parseInt(componentValue), markerSpec.integerFormat);
 			}
 		}
 		else if (markerSpec.component == 'f')
 		{
 			componentValue = formatInteger(Integer.parseInt(componentValue), markerSpec.integerFormat);
 		}
 		else if (markerSpec.component == 'Z' || markerSpec.component == 'z')
 		{
 			ZoneOffset zoneOffset = ZoneOffset.from(date.getOffset());
 			int offsetHours = 0;
 			int offsetMinutes = 0;

 			if (!zoneOffset.equals(ZoneOffset.UTC))
 			{
 				String[] offsetData = zoneOffset.getId().replaceAll("\\+\\-", "").split(":");
 				offsetHours = Integer.parseInt(offsetData[0]);
 				offsetMinutes = Integer.parseInt(offsetData[1]);
 			}

 			int offset = offsetHours * 100 + offsetMinutes;
 			if (markerSpec.integerFormat.regular)
 			{
 				componentValue = formatInteger(offset, markerSpec.integerFormat);
 			}
 			else
 			{
 				int numDigits = markerSpec.integerFormat.mandatoryDigits;
 				if (numDigits == 1 || numDigits == 2)
 				{
 					componentValue = formatInteger(offsetHours, markerSpec.integerFormat);
 					if (offsetMinutes != 0)
 					{
 						componentValue += ":" + formatInteger(offsetMinutes, "00");
 					}
 				}
 				else if (numDigits == 3 || numDigits == 4)
 				{
 					componentValue = formatInteger(offset, markerSpec.integerFormat);
 				}
 				else
 				{
 					throw new EvaluateRuntimeException(Constants.ERR_MSG_TIMEZONE_FORMAT);
 				}
 			}
 			if (offset >= 0)
 			{
 				componentValue = "+" + componentValue;
 			}
 			if (markerSpec.component == 'z')
 			{
 				componentValue = "GMT" + componentValue;
 			}
 			if (offset == 0 && markerSpec.presentation2 != null && markerSpec.presentation2 == 't')
 			{
 				componentValue = "Z";
 			}
 		}
 		else if (markerSpec.component == 'P')
 		{
 			// §9.8.4.7 Formatting Other Components
 			// Formatting P for am/pm
 			// getDateTimeFragment() always returns am/pm lower case so check for UPPER here
 			if (markerSpec.names == tcase.UPPER)
 			{
 				componentValue = componentValue.toUpperCase();
 			}
 		}
 		return componentValue;
 	}

    private static String getDateTimeFragment(LocalDateTime date, Character component) {
        String componentValue = "";
        switch (component) {
            case 'Y': // year
                componentValue = "" + date.getYear();
                break;
            case 'M': // month in year
                componentValue = "" + date.getMonthValue();
                break;
            case 'D': // day in month
                componentValue = "" + date.getDayOfMonth();
                break;
            case 'd': // day in year
                componentValue = "" + date.getDayOfYear();
                break;
            case 'F': // day of week
                componentValue = "" + date.getDayOfWeek().getValue();
                break;
            case 'W': // week in year
                componentValue = "" + date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
                break;
            case 'w': // week in month
                componentValue = "" + date.get(WeekFields.ISO.weekOfMonth());
                break;
            case 'X':
                //TODO work these out once others verified
            case 'x':
                componentValue = "" + -1;
                break;
            case 'H': // hour in day (24 hours)
                componentValue = "" + date.getHour();
                break;
            case 'h': //hour in day (12 hours)
                int hour = date.getHour();
                if (hour > 12) {
                    hour -= 12;
                } else if (hour == 0) {
                    hour = 12;
                }
                componentValue = "" + hour;
                break;
            case 'P':
                componentValue = date.getHour() < 12 ? "am" : "pm";
                break;
            case 'm':
                componentValue = "" + date.getMinute();
                break;
            case 's':
                componentValue = "" + date.getSecond();
                break;
            case 'f':
                componentValue = "" + (date.getNano() / 1000000);
                break;
            case 'Z':
            case 'z':
                break;
            case 'C':
                componentValue = "ISO";
                break;
            case 'E':
                componentValue = "ISO";
                break;
        }
        return componentValue;
    }

 	private static String getDateTimeFragment(ZonedDateTime date, Character component)
 	{
 		String componentValue = "";
 		switch (component)
 		{
 			case 'Y' : // year
 				componentValue = "" + date.getYear();
 				break;
 			case 'M' : // month in year
 				componentValue = "" + date.getMonthValue();
 				break;
 			case 'D' : // day in month
 				componentValue = "" + date.getDayOfMonth();
 				break;
 			case 'd' : // day in year
 				componentValue = "" + date.getDayOfYear();
 				break;
 			case 'F' : // day of week
 				componentValue = "" + date.getDayOfWeek().getValue();
 				break;
 			case 'W' : // week in year
 				componentValue = "" + date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
 				break;
 			case 'w' : // week in month
 				componentValue = "" + date.get(WeekFields.ISO.weekOfMonth());
 				break;
 			case 'X' :
 				// TODO work these out once others verified
 			case 'x' :
 				componentValue = "" + -1;
 				break;
 			case 'H' : // hour in day (24 hours)
 				componentValue = "" + date.getHour();
 				break;
 			case 'h' : // hour in day (12 hours)
 				int hour = date.getHour();
 				if (hour > 12)
 				{
 					hour -= 12;
 				}
 				else if (hour == 0)
 				{
 					hour = 12;
 				}
 				componentValue = "" + hour;
 				break;
 			case 'P' :
 				componentValue = date.getHour() < 12 ? "am" : "pm";
 				break;
 			case 'm' :
 				componentValue = "" + date.getMinute();
 				break;
 			case 's' :
 				componentValue = "" + date.getSecond();
 				break;
 			case 'f' :
 				componentValue = "" + date.getNano() / 1000000;
 				break;
 			case 'Z' :
 			case 'z' :
 				break;
 			case 'C' :
 				componentValue = "ISO";
 				break;
 			case 'E' :
 				componentValue = "ISO";
 				break;
 		}
 		return componentValue;
 	}

    public static Long parseDateTime(String timestamp, String picture) {
        PictureFormat formatSpec = analyseDateTimePicture(picture);
        PictureMatcher matchSpec = generateRegex(formatSpec);
        String fullRegex = "^";
        for (MatcherPart part : matchSpec.parts) {
            fullRegex += "(" + part.regex + ")";
        }
        fullRegex += "$";
        Pattern pattern = Pattern.compile(fullRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(timestamp);
        if (matcher.find()) {
            int dmA = 161;
            int dmB = 130;
            int dmC = 84;
            int dmD = 72;
            int tmA = 23;
            int tmB = 47;

            Map<Character, Integer> components = new HashMap<>();
            for (int i = 1; i <= matcher.groupCount(); i++) {
                MatcherPart mpart = matchSpec.parts.get(i - 1);
                try {
                    components.put(mpart.component, mpart.parse(matcher.group(i)));
                } catch (UnsupportedOperationException e) {
                    //do nothing
                }
            }

            if (components.size() == 0) {
                // nothing specified
                return null;
            }

            int mask = 0;

            for (char part : "YXMxWwdD".toCharArray()) {
                mask <<= 1;
                if (components.get(part) != null) {
                    mask += 1;
                }
            }
            boolean dateA = isType(dmA, mask);
            boolean dateB = !dateA && isType(dmB, mask);
            boolean dateC = isType(dmC, mask);
            boolean dateD = !dateC && isType(dmD, mask);

            mask = 0;
            for (char part : "PHhmsf".toCharArray()) {
                mask <<= 1;
                if (components.get(part) != null) {
                    mask += 1;
                }
                ;
            }

            boolean timeA = isType(tmA, mask);
            boolean timeB = !timeA && isType(tmB, mask);

            String dateComps = dateB ? "YB" : dateC ? "XxwF" : dateD ? "XWF" : "YMD";
            String timeComps = timeB ? "Phmsf" : "Hmsf";
            String comps = dateComps + timeComps;

            LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

            boolean startSpecified = false;
            boolean endSpecified = false;
            for (char part : comps.toCharArray()) {
                if (components.get(part) == null) {
                    if (startSpecified) {
                        components.put(part, "MDd".indexOf(part) != -1 ? 1 : 0);
                        endSpecified = true;
                    } else {
                        components.put(part, Integer.parseInt(getDateTimeFragment(now, part)));
                    }
                } else {
                    startSpecified = true;
                    if (endSpecified) {
                        throw new EvaluateRuntimeException(Constants.ERR_MSG_MISSING_FORMAT);
                    }
                }
            }
            if (components.get('M') != null && components.get('M') > 0) {
                components.put('M', components.get('M') - 1);
            } else {
                components.put('M', 0);
            }
            if (dateB) {
                LocalDateTime firstJan = LocalDateTime.of(components.get('Y'), Month.JANUARY, 1, 0, 0);
                firstJan = firstJan.withDayOfYear(components.get('d'));
                components.put('M', firstJan.getMonthValue() - 1);
                components.put('D', firstJan.getDayOfMonth());
            }
            if (dateC) {
                //TODO implement this
                //parsing this format not currently supported
                throw new EvaluateRuntimeException(Constants.ERR_MSG_MISSING_FORMAT);
            }
            if (dateD) {
                //TODO implement this
                // parsing this format (ISO week date) not currently supported
                throw new EvaluateRuntimeException(Constants.ERR_MSG_MISSING_FORMAT);
            }
            if (timeB) {
                components.put('H', components.get('h') == 12 ? 0 : components.get('h'));
                if (components.get('P') == 1) {
                    components.put('H', components.get('H') + 12);
                }
            }
            LocalDateTime cal = LocalDateTime.of(components.get('Y'), components.get('M') + 1, components.get('D'), components.get('H'), components.get('m'), components.get('s'),
                components.get('f') * 1000000);
            long millis = cal.toInstant(ZoneOffset.UTC).toEpochMilli();
            if (components.get('Z') != null) {
                millis -= components.get('Z') * 60 * 1000;
            } else if (components.get('z') != null) {
                millis -= components.get('z') * 60 * 1000;
            }
            return millis;
        }
        return null;
    }

    private static boolean isType(int type, int mask) {
        return ((~type & mask) == 0) && (type & mask) != 0;
    }

    private static PictureMatcher generateRegex(PictureFormat formatSpec) {
        PictureMatcher matcher = new PictureMatcher();
        for (final SpecPart part : formatSpec.parts) {
            MatcherPart res;
            if (part.type.equals("literal")) {
                Pattern p = Pattern.compile("[.*+?^${}()|\\[\\]\\\\]");
                Matcher m = p.matcher(part.value);

                String regex = m.replaceAll("\\\\$0");
                res = new MatcherPart(regex) {

                    public int parse(String value) {
                        throw new UnsupportedOperationException();
                    }
                };
            } else if (part.component == 'Z' || part.component == 'z') {
                final boolean separator = part.integerFormat.groupingSeparators.size() == 1 && part.integerFormat.regular;
                String regex = "";
                if (part.component == 'z') {
                    regex = "GMT";
                }
                regex += "[-+][0-9]+";
                if (separator) {
                    regex += part.integerFormat.groupingSeparators.get(0).character + "[0-9]+";
                }
                res = new MatcherPart(regex) {

                    public int parse(String value) {
                        if (part.component == 'z') {
                            value = value.substring(3);
                        }
                        int offsetHours = 0, offsetMinutes = 0;
                        if (separator) {
                            offsetHours = Integer.parseInt(value.substring(0, value.indexOf(part.integerFormat.groupingSeparators.get(0).character)));
                            offsetMinutes = Integer.parseInt(value.substring(value.indexOf(part.integerFormat.groupingSeparators.get(0).character) + 1));
                        } else {
                            int numdigits = value.length() - 1;
                            if (numdigits <= 2) {
                                offsetHours = Integer.parseInt(value);
                            } else {
                                offsetHours = Integer.parseInt(value.substring(0, 3));
                                offsetMinutes = Integer.parseInt(value.substring(3));
                            }
                        }
                        return offsetHours * 60 + offsetMinutes;
                    }
                };
            } else if (part.integerFormat != null) {
                res = generateRegex(part.component,part.integerFormat);
            } else {
                String regex = "[a-zA-Z]+";
                final Map<String, Integer> lookup = new HashMap<>();
                if (part.component == 'M' || part.component == 'x') {
                    for (int i = 0; i < months.length; i++) {
                        if (part.width != null && part.width.getRight() != null) {
                            lookup.put(months[i].substring(0, part.width.getRight()), i + 1);
                        } else {
                            lookup.put(months[i], i + 1);
                        }
                    }
                } else if (part.component == 'F') {
                    for (int i = 1; i < days.length; i++) {
                        if (part.width != null && part.width.getRight() != null) {
                            lookup.put(days[i].substring(0, part.width.getRight()), i);
                        } else {
                            lookup.put(days[i], i);
                        }
                    }
                } else if (part.component == 'P') {
                    lookup.put("am", 0);
                    lookup.put("AM", 0);
                    lookup.put("pm", 1);
                    lookup.put("PM", 1);
                } else {
                    throw new EvaluateRuntimeException(String.format(Constants.ERR_MSG_INVALID_NAME_MODIFIER, part.component));
                }
                res = new MatcherPart(regex) {

                    public int parse(String value) {
                        return lookup.get(value);
                    }
                };
            }
            res.component = part.component;
            matcher.parts.add(res);
        }
        return matcher;
    }

    private static MatcherPart generateRegex(char component, Format formatSpec) {
        MatcherPart matcher;
        final boolean isUpper = formatSpec.case_type == tcase.UPPER;
        switch (formatSpec.primary) {
            case LETTERS: {
                String regex = isUpper ? "[A-Z]+" : "[a-z]+";
                matcher = new MatcherPart(regex) {

                    public int parse(String value) {
                        return lettersToDecimal(value, isUpper ? 'A' : 'a');
                    }
                };
                break;
            }
            case ROMAN: {
                String regex = isUpper ? "[MDCLXVI]+" : "[mdclxvi]+";
                matcher = new MatcherPart(regex) {

                    public int parse(String value) {
                        return romanToDecimal(isUpper ? value : value.toUpperCase());
                    }
                };
                break;
            }
            case WORDS: {
                Set<String> words = new HashSet<>();
                words.addAll(wordValues.keySet());
                words.add("and");
                words.add("[\\-, ]");
                String regex = "(?:" + String.join("|", words.toArray(new String[words.size()])) + ")+";
                matcher = new MatcherPart(regex) {

                    public int parse(String value) {
                        return wordsToNumber(value.toLowerCase());
                    }
                };
                break;
            }
            case DECIMAL: {
                String regex = "[0-9]+";
                switch(component) {
                  case 'Y': {
                    regex = "[0-9]{2,4}";
                    break;
                  }
                  case 'M': //
                  case 'D': //
                  case 'H': //
                  case 'h': //
                  case 'm': //
                  case 's': {
                    regex = "[0-9]{1,2}";
                    break;
                  }
                  default: {
                    break;
                  }
                    
                }
                if (formatSpec.ordinal) {
                    regex += "(?:th|st|nd|rd)";
                }
                matcher = new MatcherPart(regex) {

                    public int parse(String value) {
                        String digits = value;
                        if (formatSpec.ordinal) {
                            digits = value.substring(0, value.length() - 2);
                        }
                        if (formatSpec.regular) {
                            digits = String.join("", digits.split(","));
                        } else {
                            for (GroupingSeparator sep : formatSpec.groupingSeparators) {
                                digits = String.join("", digits.split(sep.character));
                            }
                        }
                        if (formatSpec.zeroCode != 0x30) {
                            char[] chars = digits.toCharArray();
                            for (int i = 0; i < chars.length; i++) {
                                chars[i] = (char) (chars[i] - formatSpec.zeroCode + 0x30);
                            }
                            digits = new String(chars);
                        }
                        return Integer.parseInt(digits);
                    }
                };
                break;
            }
            case SEQUENCE:
            default: {
                throw new EvaluateRuntimeException(Constants.ERR_MSG_SEQUENCE_UNSUPPORTED);
            }
        }
        return matcher;
    }

    private static int lettersToDecimal(String letters, char aChar) {
        int decimal = 0;
        char[] chars = letters.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            decimal += (chars[chars.length - i - 1] - aChar + 1) * Math.pow(26, i);
        }
        return decimal;
    }

    private static class PictureMatcher {

        Vector<MatcherPart> parts = new Vector<>();
    }

    private static abstract class MatcherPart {

        String regex;
        char component;

        public abstract int parse(String value);

        public MatcherPart(String regex) {
            this.regex = regex;
        }
    }

}
