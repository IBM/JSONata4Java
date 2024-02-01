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
 * For simplicity, these tests don't rely on $state/$event/$instance access;
 * instead providing the "input" inlined with the expression itself (e.g. ["a",
 * "b"][0]=="a"). Separate test cases verify that variable access works as
 * expected.
 */
@RunWith(Parameterized.class)
public class SingletonArrayHandlingTests implements Serializable {

    private static final long serialVersionUID = 6300599105862480981L;

    @Parameter(0)
    public String expression;

    @Parameter(1)
    public String expectedResultJsonString;

    @Parameter(2)
    public String expectedRuntimeExceptionMessage;

    @Parameters(name = "{index}: {0} -> {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {

            // Array flattening behaviour

            // arrays build using constructors are not flattened
            /* 0 */ {
                "    1", "        1", null
            }, //
            /* 1 */ {
                "   [1]", "       [1]", null
            }, //
            /* 2 */ {
                "  [[1]]", "     [[1]]", null
            }, //
            /* 3 */ {
                " [[[1]]]", "   [[[1]]]", null
            }, //
            /* 4 */ {
                "[[[[1]]]]", " [[[[1]]]]", null
            }, //

            // flattening applied when selecting from an associative array

            /* 5 */ {
                "{'a':     1     }.a", "  1", null
            }, //
            /* 6 */ {
                "{'a':    [1]    }.a", "  [1]", null
            }, // jsonata 1.8.2 1", null }, //
            /* 7 */ {
                "{'a':   [[1]]   }.a", "  [[1]]", null
            }, // jsonata 1.8.2 1", null }, //
            /* 8 */ {
                "{'a':  [[[1]]]  }.a", " [[[1]]]", null
            }, // jsonata 1.8.2 [1]", null }, //
            /* 9 */ {
                "{'a': [[[[1]]]] }.a", "[[[[1]]]]", null
            }, // jsonata 1.8.2 [[1]]", null }, //

            // some of these corner case tests are disabled until weirdness in JSONata is resolved (see https://github.com/jsonata-js/jsonata/issues/93)
            /* 10 */ {
                "[{'a':     1     }.a]", " [1]", null
            }, //
            /* 11 */ {
                "[{'a':    [1]    }.a]", " [1]", null
            }, //
            /* 12 */ {
                "[{'a':   [[1]]   }.a]", " [[1]]", null
            }, // jsonata 1.8.2 [1]", null }, //
            //			/*13*/ {"[{'a':  [[[1]]]  }.a]", " [1]", null}, //
            //			/*14*/ {"[{'a': [[[[1]]]] }.a]", "[[1]]", null}, //

            //			/*15*/ {"{'a':     1     }.[a]", "  [1]", null}, //
            //			/*16*/ {"{'a':    [1]    }.[a]", "  [1]", null}, //
            /* 17 */ {
                "{'a':   [[1]]   }.[a]", "  [[1]]", null
            }, // jsonata 1,8.2 [1]", null }, //
            //			/*18*/ {"{'a':  [[[1]]]  }.[a]", "  [1]", null}, //
            //			/*19*/ {"{'a': [[[[1]]]] }.[a]", " [[1]]", null}, //

            // test array flattening suppression using [] (with a single-level path)
            // not supported yet
            //			/*20*/ {"{'a':     1     }.a[]", "  [1]", null}, //
            //			/*21*/ {"{'a':    [1]    }.a[]", "  [1]", null}, //
            //			/*22*/ {"{'a':   [[1]]   }.a[]", "  [1]", null}, //
            //			/*23*/ {"{'a':  [[[1]]]  }.a[]", " [[1]]", null}, //
            //			/*24*/ {"{'a': [[[[1]]]] }.a[]", "[[[1]]]", null}, //

            // test array flattening suppression using [] (with a 2-level path, the []s on
            // the first step)
            // not supported yet
            //			/*25*/ {"{'a':{'b':     1}     }.a[].b", "  [1]", null}, //
            //			/*26*/ {"{'a':{'b':    [1]}    }.a[].b", "  [1]", null}, //
            //			/*27*/ {"{'a':{'b':   [[1]]}   }.a[].b", "  [1]", null}, //    
            //			/*28*/ {"{'a':{'b':  [[[1]]]}  }.a[].b", " [[1]]", null}, // 
            //			/*29*/ {"{'a':{'b': [[[[1]]]]} }.a[].b", "[[[1]]]", null}, //

            // test array flattening suppression using [] (with a 2-level path, the []s on
            // the last step)
            // not supported yet
            //			/*30*/ {"{'a':{'b':     1}     }.a.b[]", "  [1]", null}, //
            //			/*31*/ {"{'a':{'b':    [1]}    }.a.b[]", "  [1]", null}, //
            //			/*32*/ {"{'a':{'b':   [[1]]}   }.a.b[]", "  [1]", null}, //    
            //			/*33*/ {"{'a':{'b':  [[[1]]]}  }.a.b[]", " [[1]]", null}, // 
            //			/*34*/ {"{'a':{'b': [[[[1]]]]} }.a.b[]", "[[[1]]]", null}, //

            // flattening applied when selecting from a (normal) array
            /* 35 */ {
                "    [1]    [0]", "   1", null
            }, //
            /* 36 */ {
                "   [[1]]   [0]", "   [1]", null
            }, // jsonata 1.8.2 1", null }, //
            /* 37 */ {
                "  [[[1]]]  [0]", "  [[1]]", null
            }, // jsonata 1.8.2 [1]", null }, //
            /* 38 */ {
                " [[[[1]]]] [0]", " [[[1]]]", null
            }, // jsonata 1.8.2 [[1]]", null }, //
            /* 39 */ {
                "[[[[[1]]]]][0]", "[[[[1]]]]", null
            }, // jsonata 1.8.2 [[[1]]]", null }, //

                        //			{"{\"a\":[[[1]]]}.[a]", "[1]", null}, // 
                        //			{"[{\"a\":[[[1]]]}.a]", "[1]", null}, //
                        //			{"[{\"a\":[[[1]]]}.a[]]", "[[1]]", null}, //
                        //
                        //			{"{\"a\":[1]}.a[]", "[1]", null}, //
                        //			
                        //			// [] can used at any point in the path
                        //			{"{\"a\":{\"b\":[1]}}.a[].b", "[1]", null}, //
                        //			{"{\"a\":{\"b\":[1]}}.a.b[]", "[1]", null}, //
                        //			
                        //			{"{\"a\":[[[1]]]}.a[]", "[[1]]", null}, //
                        //			
                        //			// weird edge cases involving nested arrays and path expressions surrounded in [], where JSONata doesn't seem to follow consistent rules
                        //			{"[ {\"a\":[[[1]]]}.a[],  {\"a\":[[[1]]]}.a ]", "[[1],1]", null}, //
                        //			{"[ {\"a\":{\"b\":[[[1]]]}}.a.b[],  {\"a\":{\"b\":[[[1]]]}}.a.b ]", "[[1],1]", null}, //
                        //			{"[ {\"a\":{\"b\":[[[1]]]}}.a[].b,  {\"a\":{\"b\":[[[1]]]}}.a.b ]", "[[1],1]", null}, //
                        //			
                        // TODO: when a is not an array (or is a singleton array)
        });
    }

    @Test
    public void runTest() throws Exception {
        test(this.expression, expectedResultJsonString, expectedRuntimeExceptionMessage, null);
    }

}
