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
import com.api.jsonata4java.expressions.EvaluateRuntimeException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(Parameterized.class)
public class PathExpressionTests implements Serializable {

    private static final long serialVersionUID = 3154348884576425315L;

    // just some shorthands to make our inlined schema defs a bit shorter
    private final static String P = "\"properties\"";
    private final static String T = "\"type\"";
    private final static String I = "\"items\"";
    private final static String N = "\"number\"";
    private final static String A = "\"array\"";
    private final static String S = "\"string\"";
    private final static String B = "\"boolean\"";

    private final static String TN = T + ":" + N;
    private final static String TNW = "{" + TN + "}";

    private final static String TS = T + ":" + S;
    private final static String TSW = "{" + TS + "}";

    private final static String TA = T + ":" + A;
    //	private final static String TAW = "{"+TA+"}";

    private final static String TB = T + ":" + B;
    private final static String TBW = "{" + TB + "}";

    @Parameter(0)
    public String expression;

    @Parameter(1)
    public String inputJsonString;

    @Parameter(2)
    public Integer indexVarValue;

    @Parameter(3)
    public String schemaJsonString;

    @Parameter(4)
    public String valueToAssignJsonString;

    @Parameter(5)
    public String expectedOutputJsonString;

    @Parameter(6)
    public String expectedRuntimeExceptionMessage;

    @Parameters(name = "{index}: {0},{1},{2},{3},{4} -> {5} ({6})")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {

            // object nesting
            {
                "a", "{\"a\":0}", null, "{" + P + ": {\"a\":" + TNW + "}}", "1", "{\"a\":1}", null
            },
            {
                "a.b", "{\"a\":{\"b\":0}}", null, "{" + P + ": {\"a\": {" + P + ":{\"b\":" + TNW + "}}}}", "1", "{\"a\":{\"b\":1}}", null
            },
            {
                "a.b", "{\"a\":{\"x\": 2, \"b\":0}}", null, "{" + P + ": {\"a\": {" + P + ":{\"x\":" + TNW + ", \"b\":" + TNW + "}}}}", "1", "{\"a\":{\"x\": 2, \"b\":1}}", null
            },
            {
                "a.b.c", "{\"a\":{\"b\":{\"c\":0}}, \"x\":2}", null, "{" + P + ": {\"a\": {" + P + ":{\"b\":{" + P + ":{\"c\":" + TNW + "}}}}}}", "1",
                "{\"a\":{\"b\":{\"c\":1}}, \"x\":2}", null
            },

            // attempt to reference field of non-object
            {
                "a.b", "{\"a\":0}", null, "{" + P + ": {\"a\":" + TNW + "}}", null, null, PathExpressionVisitor.ERR_FIELD_ON_NON_OBJECT("b")
            },
            {
                "`a`.b.c", "{\"a\":0}", null, "{" + P + ": {\"a\":" + TNW + "}}", null, null, PathExpressionVisitor.ERR_FIELD_ON_NON_OBJECT("b")
            },
            {
                "a.`d`", "{\"a\":[]}", null, "{" + P + ": {\"a\":{" + TA + ", " + I + ":" + TNW + "}}}", null, null, PathExpressionVisitor.ERR_FIELD_ON_NON_OBJECT("d")
            },

            // arrays
            {
                "a[0]", "{\"a\":[0]}", null, "{" + P + ": {\"a\":{" + TA + ", " + I + ":" + TNW + "}}}", "1", "{\"a\":[1]}", null
            },
            {
                "a[0]", "{\"a\":[0,1]}", null, "{" + P + ": {\"a\":{" + TA + ", " + I + ":" + TNW + "}}}", "2", "{\"a\":[2,1]}", null
            },
            {
                "a[1]", "{\"a\":[0,1]}", null, "{" + P + ": {\"a\":{" + TA + ", " + I + ":" + TNW + "}}}", "2", "{\"a\":[0,2]}", null
            },
            {
                "a[0]", "{\"a\":[0,1,2]}", null, "{" + P + ": {\"a\":{" + TA + ", " + I + ":" + TNW + "}}}", "3", "{\"a\":[3,1,2]}", null
            },
            {
                "a[1]", "{\"a\":[0,1,2]}", null, "{" + P + ": {\"a\":{" + TA + ", " + I + ":" + TNW + "}}}", "3", "{\"a\":[0,3,2]}", null
            },
            {
                "a[2]", "{\"a\":[0,1,2]}", null, "{" + P + ": {\"a\":{" + TA + ", " + I + ":" + TNW + "}}}", "3", "{\"a\":[0,1,3]}", null
            },

            // nested arrays
            {
                "a[0][0]", "{\"a\":[[0]]}", null, "{" + P + ": {\"a\": {" + TA + ", " + I + ":{" + TA + ", " + I + ":" + TNW + "}}}}", "1", "{\"a\":[[1]]}", null
            },
            {
                "a[1][0]", "{\"a\":[[0],[0]]}", null, "{" + P + ": {\"a\": {" + TA + ", " + I + ":{" + TA + ", " + I + ":" + TNW + "}}}}", "1", "{\"a\":[[0], [1]]}", null
            },
            {
                "a[1][0]", "{\"a\":[[0,1],[0],2]}", null, "{" + P + ": {\"a\": {" + TA + ", " + I + ":{" + TA + ", " + I + ":" + TNW + "}}}}", "1", "{\"a\":[[0,1],[1],2]}", null
            },
            {
                "a[0][0][0]", "{\"a\":[[[0]]]}", null, "{" + P + ": {\"a\": {" + TA + ", " + I + ":{" + TA + ", " + I + ":{" + TA + ", " + I + ":" + TNW + "}}}}}", "1",
                "{\"a\":[[[1]]]}", null
            },
            {
                "a[0][1][0]", "{\"a\":[[[0], [0]]]}", null, "{" + P + ": {\"a\": {" + TA + ", " + I + ":{" + TA + ", " + I + ":{" + TA + ", " + I + ":" + TNW + "}}}}}", "1",
                "{\"a\":[[[0], [1]]]}", null
            },

            // out-of-bounds
            {
                "a[0]", "{\"a\":[]}", null, "{" + P + ": {\"a\":{" + TA + ", " + I + ":" + TNW + "}}}", null, null, PathExpressionVisitor.ERR_ARR_INDEX_OUT_OF_BOUNDS("a", 0)
            },
            {
                "b[1]", "{\"b\":[0]}", null, "{" + P + ": {\"b\":{" + TA + ", " + I + ":" + TNW + "}}}", null, null, PathExpressionVisitor.ERR_ARR_INDEX_OUT_OF_BOUNDS("b", 1)
            },
            {
                "b[1]", "{\"b\":[0]}", null, "{" + P + ": {\"b\":{" + TA + ", " + I + ":" + TNW + "}}}", null, null, PathExpressionVisitor.ERR_ARR_INDEX_OUT_OF_BOUNDS("b", 1)
            },

            {
                "a[0][0]", "{\"a\":[]}", null, "{" + P + ": {\"a\": {" + TA + ", " + I + ":{" + TA + ", " + I + ":" + TNW + "}}}}", null, null,
                PathExpressionVisitor.ERR_ARR_INDEX_OUT_OF_BOUNDS("a", 0)
            },
            {
                "a[0][0]", "{\"a\":[[]]}", null, "{" + P + ": {\"a\": {" + TA + ", " + I + ":{" + TA + ", " + I + ":" + TNW + "}}}}", null, null,
                PathExpressionVisitor.ERR_ARR_INDEX_OUT_OF_BOUNDS("a[0]", 0)
            },
            {
                "a[1][2]", "{\"a\":[[0],[0,0]]}", null, "{" + P + ": {\"a\": {" + TA + ", " + I + ":{" + TA + ", " + I + ":" + TNW + "}}}}", null, null,
                PathExpressionVisitor.ERR_ARR_INDEX_OUT_OF_BOUNDS("a[1]", 2)
            },
            // wnm3 out { "a[$index][2]", "{\"a\":[[0],[0,0]]}", 	1, 	  "{"+P+": {\"a\": {"+TA+", "+I+":{"+TA+", "+I+":"+TNW+"}}}}", null, null, PathExpressionVisitor.ERR_ARR_INDEX_OUT_OF_BOUNDS("a[1]", 2)},
            {
                "a[1][1].b[0][1].c", "{\"a\":[[], [[], {\"b\":[[{\"c\":0}]]}]]}", null, "{" + P + ": {\"a\":{" + TA + ", " + I + ":{" + TA + ", " + I + ":{" + P + ":{\"b\":{" + TA
                    + ", " + I + ":{" + TA + ", " + I + ":{" + P + ":{\"c\":" + TNW + "}}}}}}}}}}",
                "1", null, PathExpressionVisitor.ERR_ARR_INDEX_OUT_OF_BOUNDS("b[0]", 1)
            },

            // attempt to reference element of non-array
            {
                "a[0]", "{\"a\":0}", null, "{" + P + ": {\"a\":{" + TA + ", " + I + ":" + TNW + "}}}", null, null, PathExpressionVisitor.ERR_INDEX_ON_NON_ARRAY("a", 0)
            },
            {
                "a[99]", "{\"a\":0}", null, "{" + P + ": {\"a\":{" + TA + ", " + I + ":" + TNW + "}}}", null, null, PathExpressionVisitor.ERR_INDEX_ON_NON_ARRAY("a", 99)
            },
            {
                "a[0][0]", "{\"a\":[0]}", null, "{" + P + ": {\"a\":{" + TA + ", " + I + ":" + TNW + "}}}", null, null, PathExpressionVisitor.ERR_INDEX_ON_NON_ARRAY("a[0]", 0)
            },
            // wnm3 out { "a[$index][2]", 	"{\"a\":[0,1]}", 	1,		"{"+P+": {\"a\":{"+TA+", "+I+":"+TNW+"}}}", 			null, null, PathExpressionVisitor.ERR_INDEX_ON_NON_ARRAY("a[1]", 2)},

            // arrays + object nesting
            {
                "a[1].b", "{\"a\":[{\"b\":0}, {\"b\":1}, {\"b\":2}]}", null, "{" + P + ": {\"a\":{" + TA + ", " + I + ":{" + P + ":{\"b\":" + TNW + "}}}}}", "3",
                "{\"a\":[{\"b\":0}, {\"b\":3}, {\"b\":2}]}", null
            },
            {
                "a.b[1]", "{\"a\":{\"b\":[0,1,2]}}", null, "{" + P + ": {\"a\":{" + P + ":{\"b\":{" + TA + ", " + I + ":" + TNW + "}}}}}", "3", "{\"a\":{\"b\":[0,3,2]}}", null
            },
            {
                "a.b[1].c", "{\"a\":{\"b\":[{\"c\":0}, {\"c\":1}, {\"c\":2}]}}", null,
                "{" + P + ": {\"a\":{" + P + ":{\"b\":{" + TA + ", " + I + ":{" + P + ":{\"c\":" + TNW + "}}}}}}}", "3", "{\"a\":{\"b\":[{\"c\":0}, {\"c\":3}, {\"c\":2}]}}", null
            },

            // nested arrays + object nesting
            {
                "a[0][0].b[0][0].c", "{\"a\":[[{\"b\":[[{\"c\":0}]]}]]}", null, "{" + P + ": {\"a\":{" + TA + ", " + I + ":{" + TA + ", " + I + ":{" + P + ":{\"b\":{" + TA + ", "
                    + I + ":{" + TA + ", " + I + ":{" + P + ":{\"c\":" + TNW + "}}}}}}}}}}",
                "1", "{\"a\":[[{\"b\":[[{\"c\":1}]]}]]}", null
            },
            {
                "a[1][0].b[0][0].c", "{\"a\":[[], [{\"b\":[[{\"c\":0}]]}]]}", null, "{" + P + ": {\"a\":{" + TA + ", " + I + ":{" + TA + ", " + I + ":{" + P + ":{\"b\":{" + TA
                    + ", " + I + ":{" + TA + ", " + I + ":{" + P + ":{\"c\":" + TNW + "}}}}}}}}}}",
                "1", "{\"a\":[[], [{\"b\":[[{\"c\":1}]]}]]}", null
            },
            {
                "a[1][1].b[0][0].c", "{\"a\":[[], [[], {\"b\":[[{\"c\":0}]]}]]}", null, "{" + P + ": {\"a\":{" + TA + ", " + I + ":{" + TA + ", " + I + ":{" + P + ":{\"b\":{" + TA
                    + ", " + I + ":{" + TA + ", " + I + ":{" + P + ":{\"c\":" + TNW + "}}}}}}}}}}",
                "1", "{\"a\":[[], [[], {\"b\":[[{\"c\":1}]]}]]}", null
            },
            {
                "a[1][1].b[1][0].c", "{\"a\":[[], [[], {\"b\":[[], [{\"c\":0}]]}]]}", null, "{" + P + ": {\"a\":{" + TA + ", " + I + ":{" + TA + ", " + I + ":{" + P + ":{\"b\":{"
                    + TA + ", " + I + ":{" + TA + ", " + I + ":{" + P + ":{\"c\":" + TNW + "}}}}}}}}}}",
                "1", "{\"a\":[[], [[], {\"b\":[[], [{\"c\":1}]]}]]}", null
            },
            {
                "a[1][1].b[1][1].c", "{\"a\":[[], [[], {\"b\":[[], [[], {\"c\":0}]]}]]}", null, "{" + P + ": {\"a\":{" + TA + ", " + I + ":{" + TA + ", " + I + ":{" + P
                    + ":{\"b\":{" + TA + ", " + I + ":{" + TA + ", " + I + ":{" + P + ":{\"c\":" + TNW + "}}}}}}}}}}",
                "1", "{\"a\":[[], [[], {\"b\":[[], [[], {\"c\":1}]]}]]}", null
            },
            // wnm3 out { "a[$index][1].b[1][$index].c", 	"{\"a\":[[], [[], {\"b\":[[], [[], {\"c\":0}]]}]]}", 	1, 		"{"+P+": {\"a\":{"+TA+", "+I+":{"+TA+", "+I+":{"+P+":{\"b\":{"+TA+", "+I+":{"+TA+", "+I+":{"+P+":{\"c\":"+TNW+"}}}}}}}}}}", 	"1", "{\"a\":[[], [[], {\"b\":[[], [[], {\"c\":1}]]}]]}", 	null},

            // backtick-wrapped IDs
            {
                "`a`", "{\"a\":0}", null, "{" + P + ": {\"a\":" + TNW + "}}", "1", "{\"a\":1}", null
            },
            {
                "`a.b`", "{\"a.b\":0}", null, "{" + P + ": {\"a.b\":" + TNW + "}}", "1", "{\"a.b\":1}", null
            },
            {
                "`a[0].b[1]`", "{\"a[0].b[1]\":0}", null, "{" + P + ": {\"a[0].b[1]\":" + TNW + "}}", "1", "{\"a[0].b[1]\":1}", null
            },
            {
                "`a b`", "{\"a b\":0}", null, "{" + P + ": {\"a b\":" + TNW + "}}", "1", "{\"a b\":1}", null
            },
            {
                "`a b`.c", "{\"a b\":{\"c\":0}}", null, "{" + P + ": {\"a b\": {" + P + ":{\"c\":" + TNW + "}}}}", "1", "{\"a b\":{\"c\":1}}", null
            },
            {
                "`a b`.`c`", "{\"a b\":{\"c\":0}}", null, "{" + P + ": {\"a b\": {" + P + ":{\"c\":" + TNW + "}}}}", "1", "{\"a b\":{\"c\":1}}", null
            },
            {
                "a.`b c`", "{\"a\":{\"b c\":0}}", null, "{" + P + ": {\"a\": {" + P + ":{\"b c\":" + TNW + "}}}}", "1", "{\"a\":{\"b c\":1}}", null
            },
            {
                "`a`.`b c`", "{\"a\":{\"b c\":0}}", null, "{" + P + ": {\"a\": {" + P + ":{\"b c\":" + TNW + "}}}}", "1", "{\"a\":{\"b c\":1}}", null
            },
            {
                "`a b`.`c d`", "{\"a b\":{\"c d\":0}}", null, "{" + P + ": {\"a b\": {" + P + ":{\"c d\":" + TNW + "}}}}", "1", "{\"a b\":{\"c d\":1}}", null
            },

            {
                "`a b`.c[1]", "{\"a b\":{\"c\":[0,1,2]}}", null, "{" + P + ": {\"a b\":{" + P + ":{\"c\":{" + TA + ", " + I + ":" + TNW + "}}}}}", "3", "{\"a b\":{\"c\":[0,3,2]}}",
                null
            },
            {
                "`a b`[1].`c[1]`", "{\"a b\":[{\"c[1]\":0}, {\"c[1]\":1}, {\"c[1]\":2}]}", null,
                "{" + P + ": {\"a b\":{" + TA + ", " + I + ":{" + P + ":{\"c[1]\":" + TNW + "}}}}}", "3", "{\"a b\":[{\"c[1]\":0}, {\"c[1]\":3}, {\"c[1]\":2}]}", null
            },

            // wnm3 out { "`$index`[0]", "{\"$index\":[0]}", 	null, "{"+P+": {\"$index\":{"+TA+", "+I+":"+TNW+"}}}", "1", "{\"$index\":[1]}", 	null},

            // whitespace handling
            {
                " 	 a 	 ", "{\"a\":0}", null, "{" + P + ": {\"a\":" + TNW + "}}", "1", "{\"a\":1}", null
            },
            {
                " a 	.   b 	", "{\"a\":{\"b\":0}}", null, "{" + P + ": {\"a\": {" + P + ":{\"b\":" + TNW + "}}}}", "1", "{\"a\":{\"b\":1}}", null
            },
            {
                "	 a 	[  	0 		 ] 	", "{\"a\":[0]}", null, "{" + P + ": {\"a\":{" + TA + ", " + I + ":" + TNW + "}}}", "1", "{\"a\":[1]}", null
            },
            {
                "	 `a` 	[  	0 		 ] 	", "{\"a\":[0]}", null, "{" + P + ": {\"a\":{" + TA + ", " + I + ":" + TNW + "}}}", "1", "{\"a\":[1]}", null
            },

            // assigning different types of json value
            {
                "a", "{\"a\":false}", null, "{" + P + ": {\"a\":" + TBW + "}}", "true", "{\"a\":true}", null
            },
            {
                "a", "{\"a\":0.0}", null, "{" + P + ": {\"a\":" + TNW + "}}", "0.1", "{\"a\":0.1}", null
            },
            {
                "a", "{\"a\":\"hello\"}", null, "{" + P + ": {\"a\":" + TSW + "}}", "\"world\"", "{\"a\":\"world\"}", null
            },
            {
                "a", "{\"a\":[0]}", null, "{" + P + ": {\"a\":{" + TA + ", " + I + ":" + TNW + "}}}", "[1]", "{\"a\":[1]}", null
            },
            {
                "a", "{\"a\":{\"b\":0}}", null, "{" + P + ": {\"a\": {" + P + ":{\"b\":" + TNW + "}}}}", "{\"b\":1}", "{\"a\":{\"b\":1}}", null
            },
            {
                "a[0]", "{\"a\":[[0]]}", null, "{" + P + ": {\"a\": {" + TA + ", " + I + ":{" + TA + ", " + I + ":" + TNW + "}}}}", "[1]", "{\"a\":[[1]]}", null
            },

            // all out below as we don't have a schema for coercion            
            //            // use of $index variable
            //         // wnm3 out { "a[$index]", "{\"a\":[0,1,2]}", 	0, "{"+P+": {\"a\":{"+TA+", "+I+":"+TNW+"}}}", "3", "{\"a\":[3,1,2]}", 	null},
            //         // wnm3 out { "a[$index]", "{\"a\":[0,1,2]}", 	1, "{"+P+": {\"a\":{"+TA+", "+I+":"+TNW+"}}}", "3", "{\"a\":[0,3,2]}", 	null},
            //         // wnm3 out { "a[$index]", "{\"a\":[0,1,2]}", 	2, "{"+P+": {\"a\":{"+TA+", "+I+":"+TNW+"}}}", "3", "{\"a\":[0,1,3]}", 	null},
            //            
            //            // use of $index variable when it is not bound
            //         // wnm3 out { "a[$index]", "{\"a\":[0,1,2]}", 	null, "{"+P+": {\"a\":{"+TA+", "+I+":"+TNW+"}}}", null, null, 	PathExpressionVisitor.ERR_INDEX_VAR_UNSET},
            //            
            //            // type coercion based on schema
            //            
            //            // string -> number
            //            { "a", 		"{\"a\":0}", 	null, "{"+P+": {\"a\":"+TNW+"}}", 	"\"1\"", 						"{\"a\":1}", 						null},
            //            { "a", 		"{\"a\":0}", 	null, "{"+P+": {\"a\":"+TNW+"}}", 	"\"2147483647\"", 				"{\"a\":2147483647}", 				null}, // MAX INT
            //            { "a", 		"{\"a\":0}", 	null, "{"+P+": {\"a\":"+TNW+"}}", 	"\"2147483648\"", 				"{\"a\":2147483648}", 				null}, // MAX INT+1 should coerce to long
            //            { "a", 		"{\"a\":0}", 	null, "{"+P+": {\"a\":"+TNW+"}}", 	"\"9223372036854775807\"", 		"{\"a\":9223372036854775807}", 		null}, // MAX LONG
            //            { "a", 		"{\"a\":0}", 	null, "{"+P+": {\"a\":"+TNW+"}}", 	"\"9223372036854775808\"", 		"{\"a\":9223372036854775808}", 		null}, // MAX LONG+1 should coerce to BigInteger
            //            { "a", 		"{\"a\":0}", 	null, "{"+P+": {\"a\":"+TNW+"}}", 	"\"1.3\"", 						"{\"a\":1.3}", 						null}, // non-integers should be coerced to double nodes
            //            { "a", 		"{\"a\":0}", 	null, "{"+P+": {\"a\":"+TNW+"}}", 	"\"1.7976931348623157E308\"", 	"{\"a\":1.7976931348623157E308}", 	null}, // MAX DOUBLE
            //            
            //            // number -> string
            //            { "a", 		"{\"a\":\"0\"}", 	null, "{"+P+": {\"a\":"+TSW+"}}", 	"1", 						"{\"a\":\"1\"}", 					null},
            //            
            //            // coercion when polymorphic types are used in schema
            //            
            //            // check that type coercions are attempted only when necessary, and according to order of polymorphic type definitions
            //            { "a", 		"{\"a\":0}", 		null, "{"+P+": {\"a\":{\"type\":[\"number\", \"string\"]}}}", 	"1", 	"{\"a\":1}", 					null}, // input is a number, which is one of the defined types, no coercion should be attempted
            //            { "a", 		"{\"a\":0}", 		null, "{"+P+": {\"a\":{\"type\":[\"string\", \"number\"]}}}", 	"22.2", 	"{\"a\":22.2}", 					null}, // input is a number, which is one of the defined types, no coercion should be attempted
            //            { "a", 		"{\"a\":0}", 		null, "{"+P+": {\"a\":{\"type\":[\"string\", \"boolean\", \"number\"]}}}", 	"22", 	"{\"a\":22}", 		null}, // input is a number, which is one of the defined types, no coercion should be attempted
            //            
            //            { "a", 		"{\"a\":0}", 		null, "{"+P+": {\"a\":{\"type\":[\"string\", \"number\"]}}}", 	"\"boo\"", 	"{\"a\":\"boo\"}", 			null}, // input is a string, which is one of the defined types, no coercion should be attempted
            //            { "a", 		"{\"a\":0}", 		null, "{"+P+": {\"a\":{\"type\":[\"number\", \"string\"]}}}", 	"\"boo\"", 	"{\"a\":\"boo\"}", 			null}, // input is a string, which is one of the defined types, no coercion should be attempted
            //            
            //            { "a", 		"{\"a\":0}", 		null, "{"+P+": {\"a\":{\"type\":[\"boolean\", \"string\"]}}}", 	"22", 		"{\"a\":\"22\"}", 			null}, // input is a number, this does not match any of the defined types so coerce the number to the first defined type for which coercion from number is possible (string)
            //            { "a", 		"{\"a\":0}", 		null, "{"+P+": {\"a\":{\"type\":[\"boolean\", \"number\"]}}}", 	"\"22\"", 	"{\"a\":22}", 				null}, // input is a string, this does not match any of the defined types so coerce the string to the first defined type for which coercion from string is possible (number)
            //            { "a", 		"{\"a\":0}", 		null, "{"+P+": {\"a\":{\"type\":[\"boolean\", \"number\"]}}}", 	"\"boo\"", 	"{\"a\":\"boo\"}", 				null}, // input is a string, this does not match any of the defined types so coerce but coercion is not possible, so return it as a string
            //            
            //            
            //            
            //            { "a", 		"{\"a\":0}", 		null, "{"+P+": {\"a\":{\"type\":[\"number\"]}}}", 	"\"boo\"", 	"{\"a\":\"boo\"}", 						null}, // coercion to number fails, string should be output
            //            { "a", 		"{\"a\":0}", 		null, "{"+P+": {\"a\":{\"type\":[\"number\", \"string\", \"null\"]}}}", 	"null", 	"{\"a\":null}", null}, // null is one of the defined types, no coercion should be attempted
            //            { "a", 		"{\"a\":0}", 		null, "{"+P+": {\"a\":{\"type\":[\"number\", \"string\"]}}}", 	"null", 	"{\"a\":null}", null}, 			   // null is not one of the defined types, but we don't attempt to coerce null, so it should be returned uncoerced
            //            
            //            { "a", 		"{\"a\":0}", 		null, "{"+P+": {\"a\":{\"type\":[\"string\"]}}}", 				"false", 	"{\"a\":false}", 			null}, // do not attempt coercion to string from boolean
            //            
            //            { "a", 		"{\"a\":0}", 		null, "{"+P+": {\"a\":{\"type\":[\"boolean\", \"string\"]}}}", 	"false", 	"{\"a\":false}", 			null}, // boolean is one of the defined types, so no coercion
            //            { "a", 		"{\"a\":0}", 		null, "{"+P+": {\"a\":{\"type\":[\"string\", \"boolean\"]}}}", 	"true", 	"{\"a\":true}", 			null}, // boolean is one of the defined types, so no coercion
            //            
            //            { "a", 		"{\"a\":0}", 		null, "{"+P+": {\"a\":{\"type\":[\"boolean\", \"string\"]}}}", 	"\"false\"","{\"a\":\"false\"}", 		null}, // string is one of the defined types, so no coercion
            //            { "a", 		"{\"a\":0}", 		null, "{"+P+": {\"a\":{\"type\":[\"string\", \"boolean\"]}}}", 	"\"false\"", 	"{\"a\":\"false\"}", 	null}, // string is one of the defined types, so no coercion

            // missing parent objects (can occur when optional properties are used in schemas)

            // check empty object inserted where necessary
            {
                "a", "{}", null, "{" + P + ": {\"a\":" + TNW + "}}", "1", "{\"a\":1}", null
            },
            {
                "a.b", "{}", null, "{" + P + ": {\"a\": {" + P + ":{\"b\":" + TNW + "}}}}", "1", "{\"a\":{\"b\":1}}", null
            },

            // check empty array inserted where necessary (leading to expected out-of-bounds error)
            {
                "a[0]", "{}", null, "{" + P + ": {\"a\":{" + TA + ", " + I + ":" + TNW + "}}}", "1", null, PathExpressionVisitor.ERR_ARR_INDEX_OUT_OF_BOUNDS("a", 0)
            },

                        // TODO: awaiting info on how to handle missing optional arrays
                        //{"a[0]", "{}", 			null, "{"+P+": {\"a\":{"+TA+", "+I+":"+TNW+"}}}", "1", "{\"a\":[1]}", null},
                        //{"a[0]", "{\"a\":[]}", 	null, "{"+P+": {\"a\":{"+TA+", "+I+":"+TNW+"}}}", "1", "{\"a\":[1]}", null},

                        // schemas that are invalid wrt state JSON
        });
    }

    @Test
    public void runTest() throws Exception {

        JsonNode inputJson = inputJsonString == null ? null : (new ObjectMapper()).readTree(inputJsonString);
        JsonNode expectedOutputJson = expectedOutputJsonString == null ? null : (new ObjectMapper()).readTree(expectedOutputJsonString);
        JsonNode valueToAssignJson = valueToAssignJsonString == null ? null : (new ObjectMapper()).readTree(valueToAssignJsonString);

        PathExpression e = PathExpression.parse(expression);

        JsonNode setterReturn = null;
        // test the setter
        try {
            setterReturn = e.set(inputJson, indexVarValue, valueToAssignJson);
            if (expectedRuntimeExceptionMessage != null) {
                Assert.fail("set() - Expected runtime exception with message '" + expectedRuntimeExceptionMessage + "' was not thrown");
            }
            Assert.assertEquals(expectedOutputJson, inputJson);
        } catch (EvaluateRuntimeException ex) {
            if (expectedRuntimeExceptionMessage == null) {
                throw ex;
            } else {
                Assert.assertEquals(expectedRuntimeExceptionMessage, ex.getMessage());
            }
        }

        // NOTE: setterReturn might be different to  valueToAssign due to type coercion

        // test the getter
        try {
            JsonNode getterReturn = e.get(inputJson, indexVarValue);
            if (expectedRuntimeExceptionMessage != null) {
                Assert.fail("get() - Expected runtime exception with message '" + expectedRuntimeExceptionMessage + "' was not thrown");
            }
            Assert.assertEquals(expectedOutputJson, inputJson);

            // the output of the getter should be equal to the (possibly coerced) value assigned by the setter
            Assert.assertEquals(setterReturn, getterReturn);
        } catch (EvaluateRuntimeException ex) {
            if (expectedRuntimeExceptionMessage == null) {
                throw ex;
            } else {
                Assert.assertEquals(expectedRuntimeExceptionMessage, ex.getMessage());
            }
        }

    }

}
