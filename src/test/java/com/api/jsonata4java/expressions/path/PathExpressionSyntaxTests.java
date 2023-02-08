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

package com.api.jsonata4java.expressions.path;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import com.api.jsonata4java.expressions.ParseException;

@RunWith(Parameterized.class)
public class PathExpressionSyntaxTests implements Serializable {

    private static final long serialVersionUID = -4531191957074603742L;

    @Parameter(0)
    public String expression;

    @Parameter(1)
    public boolean syntaxIsValid;

    @Parameters(name = "{index}: {0} {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {

            {
                "a", true
            },
            {
                "	a  	", true
            },
            {
                "a.b", true
            },
            {
                "a[0]", true
            },
            {
                " a	[	0 ]  ", true
            },
            {
                " `a`	[	0 ]  ", true
            },
            {
                " ` 	a `	[	0 ]  ", true
            },
            {
                "`a`.b", true
            },
            {
                "	`a` .	`b` ", true
            },
            // wnm3 out {"a[$index]", true},
            {
                "a[0][1]", true
            },
            // wnm3 out {"a[$index][1]", true},
            // wnm3 out {"a[0][$index]", true},
            // wnm3 out {"a[$index][$index].b", true},
            // wnm3 out {"a[$index][$index].b[0]", true},
            // wnm3 out {"a[0][$index].b[0][$index]", true},

            // parser errors
            {
                ".a", false
            },
            {
                "a.", false
            },
            {
                "`", false
            },
            {
                "[", false
            },
            {
                "0", false
            },
            {
                "[0]", false
            },
            {
                "a[0", false
            },
            // wnm3 out {"$index", false},
            {
                "a[index]", false
            },

            // lexical errors (e.g. unrecognised tokens)
            {
                "+++", false
            },
                        // wnm3 out { "$event.s", 	false},
                        // wnm3 out { "a[$event.s]", 	false},
        });
    }

    @Test
    public void runTest() throws Exception {
        try {
            PathExpression.parse(expression);
            if (!syntaxIsValid) {
                Assert.fail("Expected a ParseException to be thrown when attempting to parse expression '" + expression + "', but it was not");
            }
        } catch (ParseException ex) {
            if (syntaxIsValid) {
                Assert.fail("Expected expression '" + expression + "' to parse successfully, but it did not");
            }
        }
    }

}
