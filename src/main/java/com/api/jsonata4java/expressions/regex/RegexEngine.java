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
 * A pluggable regex compiler. Implement this to swap out the JVM's default
 * backtracking {@link java.util.regex.Pattern} for a linear-time engine
 * (e.g. RE2/J) and protect against ReDoS
 * (https://en.wikipedia.org/wiki/ReDoS), mirroring the {@code RegexEngine}
 * option supported by the JSONata JS reference implementation.
 */
@FunctionalInterface
public interface RegexEngine {

    /**
     * Compiles {@code pattern} (the text between the {@code /../} delimiters
     * of a JSONata regex literal, or a plain string pattern) according to
     * {@code flags}.
     */
    RegexPattern compile(String pattern, RegexFlags flags);

    /**
     * @return the default engine, backed by {@link java.util.regex.Pattern}.
     */
    static RegexEngine defaultEngine() {
        return JdkRegexPattern::new;
    }
}
