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

import java.util.regex.Pattern;

/**
 * A helper class to store information about a parsed regular expression.
 * 
 * @author Martin Bluemel
 */
public class RegularExpression {

    public enum Type {
            NORMAL, CASEINSENSITIVE, MULTILINE
    };

    private final Type type;

    private String regexPattern;

    private Pattern pattern;

    public RegularExpression(String string) {
        this(Type.NORMAL, string);
    }

    public RegularExpression(final Type type, final String regex) {
        this.type = type;
        switch (type) {
            case CASEINSENSITIVE:
            case MULTILINE:
                regexPattern = regex.substring(1, regex.length() - 2);
                break;
            default:
                regexPattern = regex.substring(1, regex.length() - 1);
                break;
        }
        compile();
    }

    private void compile() {
        switch (this.type) {
            case CASEINSENSITIVE:
                this.pattern = Pattern.compile(regexPattern, Pattern.CASE_INSENSITIVE);
                break;
            case MULTILINE:
                this.pattern = Pattern.compile(regexPattern, Pattern.MULTILINE);
                break;
            default:
                this.pattern = Pattern.compile(regexPattern);
                break;
        }
    }

    @Override
    public String toString() {
        return this.regexPattern;
    }

    public Type getType() {
        return this.type;
    }

    public Pattern getPattern() {
        return this.pattern;
    }
}
