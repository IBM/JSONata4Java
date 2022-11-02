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

/**
 * Thrown when attempting to evaluate "path" statements that are outside of a
 * predicate e.g. "a.b.c" rather than [{...},...][a.b.c=1]
 * 
 * Ideally we'd prevent expressions like this from parsing in the first place.
 */
public class PathUsedOutsideOfPredicateException extends EvaluateRuntimeException {

    private static final long serialVersionUID = 5132735269556205679L;

    /**
     * Constructor
     */
    public PathUsedOutsideOfPredicateException() {
        super("Path statement used outside of predicate");
    }
}
