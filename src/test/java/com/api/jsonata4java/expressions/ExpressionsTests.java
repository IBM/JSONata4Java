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

import static com.api.jsonata4java.expressions.utils.Utils.test;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/**
 * For simplicity, these tests don't rely on $state/$event/$instance access; instead
 * providing the "input" inlined with the expression itself (e.g. ["a", "b"][0]=="a").
 * Separate test cases verify that variable access works as expected.
 */
@RunWith(Parameterized.class)
public class ExpressionsTests implements Serializable {

    private static final long serialVersionUID = -6035633322137261169L;

    @Parameter(0)
    public String expression;

    @Parameter(1)
    public String expectedResultJsonString;

    @Parameter(2)
    public String expectedRuntimeExceptionMessage;

    @Parameters(name = "{index}: {0} -> {1} ({2})")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            // Basic array construction
            {
                "[]", "[]", null
            }, //
            {
                "[0,1,2,3]", "[0,1,2,3]", null
            }, //
            {
                "[\"a\", \"b\", \"c\"]", "[\"a\", \"b\", \"c\"]", null
            }, //
            {
                "[1,\"a\", true, false, 1.1, {}, []]", "[1,\"a\", true, false, 1.1, {}, []]", null
            }, //

            // Basic object construction
            // ---------------------
            // - Sequence (..)
            // ---------------------
            {
                "[1..2]", "[1,2]", null
            }, //
            {
                "[2..1]", "[]", null
            }, //
            {
                "[-2..2]", "[-2,-1,0,1,2]", null
            }, //
            {
                "[1+1,2*2]", "[2,4]", null
            }, //
            {
                "[1.0..2.0]", "[1,2]", null
            }, // 
            {
                "[1..2, 5..7]", "[1,2,5,6,7]", null
            }, //
            {
                "[1..2, 3, 2+2]", "[1,2,3,4]", null
            }, // // mix of sequences and other types of expression
            {
                "[1..2.0, 5.0..7]", "[1,2,5,6,7]", null
            }, //
            {
                "[1+1..2*2]", "[2,3,4]", null
            }, //

            // null handling
            {
                "[1..xxx]", "[]", null
            }, //
            {
                "[xxx..2]", "[]", null
            }, //
            {
                "[xxx..xxx]", "[]", null
            }, //
            {
                "[xxx..xxx, 3..4]", "[3,4]", null
            }, //

            // using non-integer values either side of seq (check correct runtime exception is thrown)
            {
                "[\"hello\"..2]", null, ExpressionsVisitor.ERR_SEQ_LHS_INTEGER
            }, //
            {
                "[true..2]", null, ExpressionsVisitor.ERR_SEQ_LHS_INTEGER
            }, //
            {
                "[[]..2]", null, ExpressionsVisitor.ERR_SEQ_LHS_INTEGER
            }, //
            {
                "[{}..2]", null, ExpressionsVisitor.ERR_SEQ_LHS_INTEGER
            }, //
            {
                "[1.1..2]", null, ExpressionsVisitor.ERR_SEQ_LHS_INTEGER
            }, //
            {
                "[1.00000001..2]", null, ExpressionsVisitor.ERR_SEQ_LHS_INTEGER
            }, //

            {
                "[2..\"hello\"]", null, ExpressionsVisitor.ERR_SEQ_RHS_INTEGER
            }, //
            {
                "[2..true]", null, ExpressionsVisitor.ERR_SEQ_RHS_INTEGER
            }, //
            {
                "[2..[]]", null, ExpressionsVisitor.ERR_SEQ_RHS_INTEGER
            }, //
            {
                "[2..{}]", null, ExpressionsVisitor.ERR_SEQ_RHS_INTEGER
            }, //
            {
                "[1..2.1]", null, ExpressionsVisitor.ERR_SEQ_RHS_INTEGER
            }, //
            {
                "[1..2.00000001]", null, ExpressionsVisitor.ERR_SEQ_RHS_INTEGER
            }, //

            // Using array constructor in paths to control output
            {
                "[{\"a\":1}].a", "1", null
            }, //
            {
                "[[{\"a\":1}].a]", "[1]", null
            }, //
            //{"[{\"a\":1}].a[]", "[1]", null}, // NOT SUPPORTED YET
            //{"[{\"a\":1}].[a]", "[1]", null}, // NOT SUPPORTED YET
            {
                "[{\"a\":[0,1]}, {\"a\":[2,3]}].a", "[0,1,2,3]", null
            }, //
            //{"[{\"a\":[0,1]}, {\"a\":[2,3]}].a[]", "[0,1,2,3]", null}, // NOT SUPPORTED YET
            //{"[{\"a\":[0,1]}, {\"a\":[2,3]}].[a]", "[[0,1],[2,3]]", null}, // NOT SUPPORTED YET

            // Parentheses can be used in path statements
            {
                "{\"a\":{\"b\":1}}.a.(b)", "1", null
            }, //
            {
                "{\"a\":{\"b\":1}}.(a).b", "1", null
            }, //

            // Context is maintained inside field references
            {
                "{\"a\":{\"b\":1, \"c\":2}}.a.b", "1", null
            }, //
            {
                "{\"a\":{\"b\":1, \"c\":2}}.a.c", "2", null
            }, //
            {
                "{\"a\":{\"b\":1, \"c\":2}}.a.(b+c)", "3", null
            }, //

            // null
            {
                "null", "null", null
            }, //

            // basic selection

            // object on LHS
            {
                "{\"a\":1}.a", "1", null
            }, //
            {
                "{\"a\":{\"b\":1}}.a.b", "1", null
            }, //
            {
                "{\"a\":null}.a", "null", null
            }, // // path that resolves to null (different to an unresolvable path)
            {
                "{\"a\":1}.z", null, null
            }, // // unresolvable paths

            // array on LHS
            {
                "[{\"a\":1}].a", "1", null
            }, //
            {
                "[{\"a\":1}, {\"a\":2}].a", "[1,2]", null
            }, //

            // nested array on LHS
            {
                "[ [{\"a\":1}] ].a", "1", null
            }, //
            {
                "[ [{\"a\":1}, {\"a\":2}] ].a", "[1,2]", null
            }, //
            {
                "[ [{\"a\":1}, {\"a\":1}] ].a", "[1,1]", null
            }, // // check that dupes are not suppressed
            {
                "[[ {\"a\":{\"b\":1}}, {\"a\":{\"b\":2}} ]].a", "[{\"b\":1}, {\"b\":2}]", null
            }, //
            {
                "[[ {\"a\":{\"b\":1}}, {\"a\":{\"b\":2}} ]].a.b", "[1,2]", null
            }, //
            {
                "[ [{\"a\":1}], [{\"a\":2}] ].a", "[1,2]", null
            }, //
            {
                "[ [{\"a\":1}], [{\"a\":2}] ].z", null, null
            }, // // unresolvable path
            {
                "[ [{\"a\":1}], [{\"a\":1}] ].a", "[1,1]", null
            }, // // check that dupes are not suppressed

            {
                "[ [{\"a\":null}] ].a", "null", null
            }, // // path on arrays that resolves to null (different to unresolvable paths)
            {
                "[ [{\"a\":null}], [{\"a\":null}] ].a", "[null, null]", null
            }, // // path on arrays that resolves to null (different to unresolvable paths)
            {
                "null.a", null, ExpressionsVisitor.ERR_MSG_INVALID_PATH_ENTRY
            }, //

            // verify selector result semantics
            {
                "[{\"a\":[1,2]}, {\"a\":[3,4]}].a", "[1,2,3,4]", null
            }, // // path resolves to an object in the top-level array (NOTE: not [ [1,2],[3,4] ])

            {
                "[{\"a\":{\"b\":[1,2]}}, {\"a\":{\"b\":[3,4]}}].a.b", "[1,2,3,4]", null
            }, //
            {
                "([{\"a\":[1,2]}, {\"a\":[3,4]}]).a", "[1,2,3,4]", null
            }, // parenthesis do not suppress this behaviour

            // same as the above, but the path has square brackets around it
            {
                "[{\"a\":[1,2]}, {\"a\":[3,4]}].[a]", "[[1,2],[3,4]]", null
            }, // top-level

            // multi-level paths
            {
                "[{\"a\":{\"b\":[1,2]}}, {\"a\":{\"b\":[3,4]}}].[a].b", "[1,2,3,4]", null
            }, //
            {
                "[{\"a\":{\"b\":[1,2]}}, {\"a\":{\"b\":[3,4]}}].a.[b]", "[[1,2],[3,4]]", null
            }, //
            {
                "[{\"a\":{\"b\":[1,2]}}, {\"a\":{\"b\":[3,4]}}].[a.b]", "[[1,2], [3,4]]", null
            }, //

            {
                "[{\"a\":[1,2]}, {\"a\":[3,4]}].[[a]]", "[[[1,2]],[[3,4]]]", null
            }, // // multiple brackets around path expression
            {
                "[{\"a\":[1,2]}, {\"a\":[3,4]}].[[[a]]]", "[[[[1,2]]],[[[3,4]]]]", null
            }, // // multiple brackets around path expression

            // // a has nested arrays (flattening unaffected)
            {
                "[{\"a\":[[1,2]]}, {\"a\":[[3,4]]}].a", "[[1,2],[3,4]]", null
            }, // jsonata 1.8.2 "[1,2,3,4]", null}, // 
            {
                "[{\"a\":[[1,2]]}, {\"a\":[[3,4]]}].[a]", "[[[1,2]],[[3,4]]]", null
            }, // jsonata 1.8.2 "[[1,2],[3,4]]", null}, //
            {
                "[[[{\"a\":[1,2]}, {\"a\":[3,4]}]]].[a]", "[1,2,3,4]", null
            }, // jsonata 1.8.2 "[[1,2],[3,4]]", null}, //

            // basic array indexing
            {
                "[1,2,3][0]", "1", null
            }, //
            {
                "[1,2,3][1]", "2", null
            }, //
            {
                "[1,2,3][2]", "3", null
            }, //
            {
                "[1,2,3][-1]", "3", null
            }, //
            {
                "[1,2,3][-2]", "2", null
            }, //
            {
                "[1,2,3][-3]", "1", null
            }, //
            {
                "[1,2,3][3]", null, null
            }, //
            {
                "[1,2,3][-4]", null, null
            }, //
            {
                "[1,2,3][0.00001]", "1", null
            }, //
            {
                "[1,2,3][0.99999]", "1", null
            }, //
            {
                "[1,2,3][1.00001]", "2", null
            }, //
            {
                "[1,2,3][1.99999]", "2", null
            }, //
            {
                "[1,2,3][-1.00001]", "2", null
            }, //
            {
                "[1,2,3][-1.99999]", "2", null
            }, //

            // using arrays as an index into another array
            {
                "[1,2,3][[0,1]]", "[1,2]", null
            }, //
            {
                "[1,2,3][[1,0]]", "[1,2]", null
            }, // // check ordering of input array is retained
            {
                "[1,2,3][[0,0,1,1,2,2]]", "[1,1,2,2,3,3]", null
            }, // // check dupes are not suppressed
            {
                "[1,2,3][[0.00001,1.999999, -1.99999, -1.00001]]", "[1,2,2,2]", null
            }, //
            {
                "[1,2,3][[3,-4]]", null, null
            }, //
            {
                "[1,2,3][[3,-3]]", "1", null
            }, //

            // >
            {
                "a", null, null
            }, //
            {
                "a>1", null, null
            }, //
            // weird edge cases involving nested arrays and path expressions surrounded in [], where JSONata doesn't seem to follow consistent rules
            //{"[[{\"a\":[1,2]}, {\"a\":[3,4]}]].[a]", "[1,2,3,4]", null}, // why is this not [[1,2],[3,4]]?
            //{"[[[{\"a\":[1,2]}, {\"a\":[3,4]}]]].[[a]]", "[[[1,2],[3,4]]]", null}, //
            //{"[[{\"a\":[1,2]}, {\"a\":[3,4]}]].a", "[[1,2],[3,4]]", null}, // path resolves to an object nested 1 array deep. why is this not [1,2,3,4]?
            //{"[[[{\"a\":[1,2]}, {\"a\":[3,4]}]]].a", "[[1,2],[3,4]]", null}, // path resolves to an object nested 2 arrays deep. why is this not [1,2,3,4]?
            //{"[[{\"a\":[1,2]}, {\"a\":[3,4]}], {\"a\":[5,6]}, {\"a\":[7,8]}].a", "[[1,2],[3,4],5,6,7,8]", null}, // // mix of nesting levels. why is this not [1,2,3,4,5,6,7,8]?

            // ---------------------
            // - Operators
            // ---------------------

            // =
            {
                "1=1", "true", null
            }, //
            {
                "1=2", "false", null
            }, //
            {
                "noexisty=1", "false", null
            }, // jsonata 1.8.2 null, null}, //
            {
                "1=noexisty", "false", null
            }, // jsonata 1.8.2 null, null}, //
            {
                "noexist1=noexist2", "false", null
            }, //

            // <
            {
                "1<2", "true", null
            }, //
            {
                "noexisty<2", null, null
            }, //
            {
                "2<noexisty", null, null
            }, //
            {
                "noexist1<noexist2", "false", null
            }, //
            {
                "null<2", null, "The expressions either side of operator \"<\" must evaluate to numeric or string values"
            }, //

            // +
            {
                "1+2", "3", null
            }, //
            {
                "noexisty+1", null, null
            }, //
            {
                "1+noexisty", null, null
            }, //
            {
                "noexist1+noexist2", null, null
            }, //

            // -
            {
                "1-2", "-1", null
            }, //
            {
                "noexisty-1", null, null
            }, //
            {
                "1-noexisty", null, null
            }, //
            {
                "noexist1-noexist2", null, null
            }, //
            {
                "null-1", null, "- expects two numeric arguments"
            }, //

            // *
            {
                "2*2", "4", null
            }, //
            {
                "noexisty*1", null, null
            }, //
            {
                "1*noexisty", null, null
            }, //
            {
                "noexist1*noexist2", null, null
            }, //
            {
                "null*1", null, "* expects two numeric arguments"
            }, //
            {
                "\"1\"*1", null, "* expects two numeric arguments"
            }, //

            // ---------------------
            // - unary - (negate, not subtract)
            // ---------------------
            {
                "-22", "-22", null
            }, //
            {
                "-22.2", "-22.2", null
            }, //
            {
                "-xxx", null, null
            }, // // NOTE: JavaScript JSONata engine actually throws an exception here (but it shouldn't.. this is a bug in that engine that will be fixed)
            {
                "-null", null, ExpressionsVisitor.ERR_NEGATE_NON_NUMERIC
            }, //
            {
                "-\"22\"", null, ExpressionsVisitor.ERR_NEGATE_NON_NUMERIC
            }, //
            {
                "-{}", null, ExpressionsVisitor.ERR_NEGATE_NON_NUMERIC
            }, //
            {
                "-{\"h\":2}", null, ExpressionsVisitor.ERR_NEGATE_NON_NUMERIC
            }, //
            {
                "-[]", null, ExpressionsVisitor.ERR_NEGATE_NON_NUMERIC
            }, //
            {
                "-[1]", null, ExpressionsVisitor.ERR_NEGATE_NON_NUMERIC
            }, //

            // ---------------------
            // - and
            // ---------------------
            {
                "true and true", "true", null
            }, //
            {
                "true and false", "false", null
            }, //
            {
                "false and true", "false", null
            }, //
            {
                "false and false", "false", null
            }, //

            // verify implicit type coercion
            // (see http://docs.jsonata.org/boolean-functions.html)

            // coercion on LHS
            {
                "1 and true", "true", null
            }, //
            {
                "\"\" and true", "false", null
            }, //
            {
                "\"blah\" and true", "true", null
            }, //
            {
                "\"false\" and true", "true", null
            }, //
            {
                "0 and true", "false", null
            }, //
            {
                "1 and true", "true", null
            }, //
            {
                "2 and true", "true", null
            }, //
            {
                "null and true", "false", null
            }, //
            {
                "[] and true", "false", null
            }, //
            {
                "[false, true, false] and true", "true", null
            }, //
            {
                "[false, 0] and true", "false", null
            }, //
            {
                "{} and true", "false", null
            }, //
            {
                "{\"a\":\"b\"} and true", "true", null
            }, //

            // coercion on RHS
            {
                "true and 1", "true", null
            }, //
            {
                "true and \"\"", "false", null
            }, //
            {
                "true and \"blah\"", "true", null
            }, //
            {
                "true and \"false\"", "true", null
            }, //
            {
                "true and 0", "false", null
            }, //
            {
                "true and 1", "true", null
            }, //
            {
                "true and 2", "true", null
            }, //
            {
                "true and null", "false", null
            }, //
            {
                "true and []", "false", null
            }, //
            {
                "true and [false, true, false]", "true", null
            }, //
            {
                "true and [false, 0]", "false", null
            }, //
            {
                "true and {}", "false", null
            }, //
            {
                "true and {\"a\":\"b\"}", "true", null
            }, //

            // test null handling
            {
                "noexist and true", "false", null
            }, // jsonata 1.8.2 null, null}, //
            {
                "true and noexist", "false", null
            }, // jsonata 1.8.2 null, null}, //
            {
                "noexist and noexist", "false", null
            }, // jsonata 1.8.2 null, null}, //

            // ---------------------
            // - or
            // ---------------------
            {
                "true or true", "true", null
            }, //
            {
                "true or false", "true", null
            }, //
            {
                "false or true", "true", null
            }, //
            {
                "false or false", "false", null
            }, //

            // verify implicit type coercion
            // (see http://docs.jsonata.org/boolean-functions.html)

            // coercion on LHS
            {
                "1 or false", "true", null
            }, //
            {
                "\"\" or false", "false", null
            }, //
            {
                "\"blah\" or false", "true", null
            }, //
            {
                "\"false\" or false", "true", null
            }, //
            {
                "0 or false", "false", null
            }, //
            {
                "1 or false", "true", null
            }, //
            {
                "2 or false", "true", null
            }, //
            {
                "null or false", "false", null
            }, //
            {
                "[] or false", "false", null
            }, //
            {
                "[false, true, false] or false", "true", null
            }, //
            {
                "[false, 0] or false", "false", null
            }, //
            {
                "{} or false", "false", null
            }, //
            {
                "{\"a\":\"b\"} or false", "true", null
            }, //

            // coercion on RHS
            {
                "false or 1", "true", null
            }, //
            {
                "false or \"\"", "false", null
            }, //
            {
                "false or \"blah\"", "true", null
            }, //
            {
                "false or \"false\"", "true", null
            }, //
            {
                "false or 0", "false", null
            }, //
            {
                "false or 1", "true", null
            }, //
            {
                "false or 2", "true", null
            }, //
            {
                "false or null", "false", null
            }, //
            {
                "false or []", "false", null
            }, //
            {
                "false or [false, true, false]", "true", null
            }, //
            {
                "false or [false, 0]", "false", null
            }, //
            {
                "false or {}", "false", null
            }, //
            {
                "false or {\"a\":\"b\"}", "true", null
            }, //

            // test null handling
            {
                "noexist and true", "false", null
            }, // jsonata 1.8.2 null, null}, //
            {
                "true and noexist", "false", null
            }, // jsonata 1.8.2 null, null}, //
            {
                "noexist and noexist", "false", null
            }, // jsonata 1.8.2 null, null}, //

            // ---------------------
            // - concat (&)
            // ---------------------

            // implicit coercion to string-types
            {
                "null & null", "\"nullnull\"", null
            }, //
            {
                "2 & 3", "\"23\"", null
            }, //
            {
                "true & false", "\"truefalse\"", null
            }, //
            {
                "1.2&3.4", "\"1.23.4\"", null
            }, //

            {
                "{} & {}", "\"{}{}\"", null
            }, //
            {
                "[] & []", "\"[][]\"", null
            }, //
            {
                "{\"hello\":1} & {}", "\"{\\\"hello\\\":1}{}\"", null
            }, //
            {
                "[\"hello\"]&[1,2]", "\"[\\\"hello\\\"][1,2]\"", null
            }, //

            // test null handling
            {
                "xxx & \"a\"", "\"a\"", null
            }, //
            {
                "\"a\" & xxx", "\"a\"", null
            }, //
            {
                "xxx & xxx", "\"\"", null
            }, //

            // -----------------------
            // - ternary? conditional (a?b:c)
            // ------------------------

            // verify implicit boolean coercion
            {
                "0?0:1", "1", null
            }, //
            {
                "1?0:1", "0", null
            }, //
            {
                "2?0:1", "0", null
            }, //
            // ...etc

            // test null handling
            {
                "xxx?0:1", "1", null
            }, //

        });
    }

    @Test
    public void runTest() throws Exception {
        test(this.expression, expectedResultJsonString, expectedRuntimeExceptionMessage, null);
    }

}
