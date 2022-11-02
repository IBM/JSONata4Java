/**
 * (c) Copyright 2020 IBM Corporation
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

package com.api.jsonata4java.expressions;

import java.io.Serializable;
import java.util.Arrays;

public class JS4JUtils implements Serializable {

    private static final long serialVersionUID = -1767445323606716680L;

    static public final int s_iDayMilliseconds = 86400000;

    static public final int s_iHourMilliseconds = 3600000;

    static public final int s_iMinuteMilliseconds = 60000;

    /**
    * Converts a timezone of the format +/-hhmm to milliseconds
    * 
    * @param strTimeZone
    *           timezone offset from Greenwich Mean Time (GMT) for example
    *           "-0500" is Eastern Standard Time, "-0400" is Eastern Daylight
    *           Time, "+0000" is Greenwich Mean Time, and "+0100" is the offset
    *           for Europe/Paris.
    * 
    * @return milliseconds from Greenwich Mean Time
    */
    static public int convertTimeZoneToMilliseconds(String strTimeZone) {
        int iMillisecs = 0;
        if (strTimeZone == null || strTimeZone.length() != 5) {
            return iMillisecs;
        }
        // convert timezone (+/-hhmm)
        String strSign = strTimeZone.substring(0, 1);
        String strHours = strTimeZone.substring(1, 3);
        String strMinutes = strTimeZone.substring(3, 5);
        try {
            int iHours = Integer.valueOf(strHours).intValue();
            int iMinutes = Integer.valueOf(strMinutes).intValue();
            iMillisecs = iMinutes * s_iMinuteMilliseconds;
            iMillisecs = iMillisecs + (iHours * s_iHourMilliseconds);
            if (strSign.startsWith("-") == true) {
                iMillisecs *= -1;
            }
        } catch (NumberFormatException nfe) {
            iMillisecs = 0;
        }
        return iMillisecs;
    }

    /**
    * Helper method to create strings of the form "nn000".
    * 
    * @param lIn
    *           long value to be right justified with leading characters in
    *           the returned String.
    * @param iWidth
    *           integer value of the width of the returned String.
    * @param cPad
    *           character value to be used to pad the right portion of the
    *           returned String to make it as wide as the specified iWidth
    *           parameter. For example, calling toRightPaddedString(iNum,4,'0')
    *           would result in "4500" if iNum == 45, or "4000" if iNum == 4.
    * 
    * @return String containing the right justified value, padded to the
    *         specified with the specified pad character.
    */
    static public String padRight(long lIn, int iWidth, char cPad) {
        String strTemp = String.valueOf(lIn);
        return padRight(strTemp, iWidth, cPad);
    }

    /**
    * Helper method to create strings of the form "nn000".
    * 
    * @param iIn
    *           integer value to be right justified with leading characters in
    *           the returned String.
    * @param iWidth
    *           integer value of the width of the returned String.
    * @param cPad
    *           character value to be used to pad the right portion of the
    *           returned String to make it as wide as the specified iWidth
    *           parameter. For example, calling toRightPaddedString(iNum,4,'0')
    *           would result in "4500" if iNum == 45, or "4000" if iNum == 4.
    * 
    * @return String containing the right justified value, padded to the
    *         specified with the specified pad character.
    */
    static public String padRight(int iIn, int iWidth, char cPad) {
        String strTemp = String.valueOf(iIn);
        return padRight(strTemp, iWidth, cPad);
    }

    /**
    * Creates a new String padded on its right side with the supplied pad
    * character guaranteed to be the supplied length. If the supplied length is
    * less than or equal to the length of the supplied string, the supplied
    * string is returned. If the supplied string is null, a new string is
    * returned filled with the supplied pad character that is as long as the
    * supplied length.
    * 
    * @param strInput
    * @param iMax
    * @param cPadChar
    * @return formatted string with padding
    */
    static public String padRight(String strInput, int iMax, char cPadChar) {
        if (strInput == null) {
            char[] padChars = new char[iMax];
            Arrays.fill(padChars, cPadChar);
            return new String(padChars);
        }
        int iLength = strInput.length();
        if (iLength < iMax) {
            char[] padChars = new char[iMax - iLength];
            Arrays.fill(padChars, cPadChar);
            return strInput + new String(padChars);
        }
        // else already bigger so leave it alone
        return strInput;
    }

}
