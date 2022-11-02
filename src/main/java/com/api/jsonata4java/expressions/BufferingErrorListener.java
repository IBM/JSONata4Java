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

package com.api.jsonata4java.expressions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

/**
 * ANTLR error listener implementation for registration with lexers and parsers.
 * Buffers error messages as they are produced and allows them to be retrieved
 * later via getErrorsAsString().
 */
public class BufferingErrorListener extends BaseErrorListener implements Serializable {

    private static final long serialVersionUID = -7132728543942685913L;

    private List<String> errors = new ArrayList<>();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
        String msg, RecognitionException e) {
        String message = "line " + line + ":" + charPositionInLine + " at " + offendingSymbol + ": " + msg + "\n";
        errors.add(message);
    }

    public boolean heardErrors() {
        return !this.errors.isEmpty();
    }

    public String getErrorsAsString() {
        StringBuffer sb = new StringBuffer();
        boolean first = true;
        for (String msg : this.errors) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(msg);
        }
        return sb.toString();
    }

}
