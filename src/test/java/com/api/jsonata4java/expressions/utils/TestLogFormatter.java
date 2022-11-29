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

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * A Java log formatter that puts all the data on one line with a nicely
 * formatted date, unlike java.util.logging.SimpleFormatter.
 * <p>
 * Concept from java.util.logging.SimpleFormatter
 */

public class TestLogFormatter extends Formatter implements Serializable {

    private static final long serialVersionUID = -8273377732120306686L;

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    // private final DateFormat WLP_DATE_FORMATTER = new SimpleDateFormat("MM/dd/yy
    // H:mm:ss:SSS z");
    private final DateFormat WLP_DATE_FORMATTER = new SimpleDateFormat("H:mm:ss:SSS");

    /**
     * Aim to match the format of the WLP trace log
     * 
     * e.g. [12/18/14 21:15:31:077 UTC] 00000018 id=
     * com.ibm.ws.kernel.feature.internal.FeatureManager I CWWKF0007I: Feature
     * update started.
     * 
     * Which is parsed in logstash as follows: \[%{DATA:timestamp}\]
     * %{BASE16NUM:threadid}
     * ...\s*%{NOTSPACE:package}\.%{WORD:class}\s*%{NOTSPACE:level}\s%{NOTSPACE:method}\s%{GREEDYDATA:message}
     */
    @Override
    public String format(LogRecord r) {
        String className;
        try {
            className = Class.forName(r.getSourceClassName()).getSimpleName();
        } catch (ClassNotFoundException e) {
            className = "UNKNOWN";
        }
        String date = WLP_DATE_FORMATTER.format(new Date(r.getMillis()));
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        sb.append(date);
        sb.append("] ");
        sb.append(r.getThreadID());
        sb.append("    ");
        sb.append(className);
        sb.append(' ');
        sb.append(r.getLevel().getName());
        sb.append(' ');
        sb.append(r.getSourceMethodName());
        sb.append(' ');
        sb.append(formatMessage(r));
        sb.append(LINE_SEPARATOR);

        if (null != r.getThrown()) {
            sb.append("	Throwable occurred: ");
            sb.append(LINE_SEPARATOR);
            Throwable t = r.getThrown();
            PrintWriter pw = null;
            try {
                StringWriter sw = new StringWriter();
                pw = new PrintWriter(sw);
                t.printStackTrace(pw);
                for (String line : sw.toString().split("\n")) {
                    sb.append(line);
                    sb.append(LINE_SEPARATOR);
                }
            } finally {
                if (pw != null) {
                    pw.close();
                }
            }
        }
        return sb.toString();
    }
}
