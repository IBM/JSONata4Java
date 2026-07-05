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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A compiled regular expression, abstracted away from any particular regex
 * engine. Implementations back the four regex-consuming JSONata functions:
 * $contains ({@link #test}), $match ({@link #findFirst}/{@link #findAll}),
 * $replace ({@link #findFirst}/{@link #findAll}) and $split ({@link #split}).
 *
 * @see RegexEngine
 * @see JdkRegexPattern
 */
public interface RegexPattern {

    /**
     * @return true if this pattern matches anywhere within {@code input}.
     */
    boolean test(String input);

    /**
     * Finds the first match at or after {@code fromIndex}.
     *
     * @param input     the string to search.
     * @param fromIndex the character offset to start searching from. Callers
     *                  may legitimately pass a {@code fromIndex} beyond the
     *                  end of {@code input} (e.g. while streaming through
     *                  matches after one that ends exactly at the end of the
     *                  string); implementations must return
     *                  {@link Optional#empty()} in that case rather than
     *                  throwing.
     * @return the match, or {@link Optional#empty()} if none is found.
     */
    Optional<RegexMatch> findFirst(String input, int fromIndex);

    /**
     * Finds every non-overlapping match in {@code input}, in order.
     *
     * <p>
     * The default implementation streams through {@link #findFirst} so that
     * implementations only need to provide that one primitive; override this
     * method if the underlying engine offers a more efficient bulk-match API.
     */
    default List<RegexMatch> findAll(String input) {
        List<RegexMatch> matches = new ArrayList<>();
        int pos = 0;
        while (pos <= input.length()) {
            Optional<RegexMatch> match = findFirst(input, pos);
            if (!match.isPresent()) {
                break;
            }
            RegexMatch m = match.get();
            matches.add(m);
            pos = m.getIndex() + Math.max(m.getLength(), 1);
        }
        return matches;
    }

    /**
     * Splits {@code input} around every match of this pattern, following
     * {@link java.util.regex.Pattern#split(CharSequence)} semantics (no
     * trailing empty strings).
     */
    String[] split(String input);
}
