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

import java.util.Collections;
import java.util.List;

/**
 * Engine-neutral representation of a single regex match, as needed by
 * $match/$replace/$split/$contains. {@code groups} holds the captured
 * groups in order (group 1 first); a group that did not participate in the
 * match is represented as {@code null}, mirroring
 * {@link java.util.regex.Matcher#group(int)}.
 */
public final class RegexMatch {

    private final String match;

    private final int index;

    private final List<String> groups;

    public RegexMatch(String match, int index, List<String> groups) {
        this.match = match;
        this.index = index;
        this.groups = Collections.unmodifiableList(groups);
    }

    public String getMatch() {
        return match;
    }

    public int getIndex() {
        return index;
    }

    public List<String> getGroups() {
        return groups;
    }

    /**
     * @return the length, in characters, consumed by the overall match.
     */
    public int getLength() {
        return match.length();
    }
}
