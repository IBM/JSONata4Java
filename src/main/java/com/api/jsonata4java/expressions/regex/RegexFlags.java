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

package com.api.jsonata4java.expressions.regex;

/**
 * Engine-neutral representation of the flags carried by a JSONata regex
 * literal (e.g. {@code /pattern/i}). Kept separate from any one regex
 * engine's native flag bits (such as {@link java.util.regex.Pattern#CASE_INSENSITIVE})
 * so that a {@link RegexEngine} implementation is free to map them onto
 * whatever flag convention its underlying engine uses.
 */
public final class RegexFlags {

    private final boolean caseInsensitive;

    private final boolean multiline;

    public RegexFlags(boolean caseInsensitive, boolean multiline) {
        this.caseInsensitive = caseInsensitive;
        this.multiline = multiline;
    }

    public boolean isCaseInsensitive() {
        return caseInsensitive;
    }

    public boolean isMultiline() {
        return multiline;
    }
}
