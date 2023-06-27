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

package com.api.jsonata4java.test.expressions;

import static com.api.jsonata4java.expressions.utils.Utils.test;
import org.junit.Test;

public class OpOrderByTest {

    @Test
    public void testOrdered() throws Exception {
        test("$^(id, content)",
            "[{\"id\":\"1\",\"content\":\"1\"},{\"id\":\"2\",\"content\":\"1\"},{\"id\":\"2\",\"content\":\"2\"}]", null,
            "[{\"id\":\"2\",\"content\":\"2\"},{\"id\":\"2\",\"content\":\"1\"},{\"id\":\"1\",\"content\":\"1\"}]");
    }

    @Test
    public void testOrderedAscending() throws Exception {
        test("$^(>id, >content)",
            "[{\"id\":\"1\",\"content\":\"1\"},{\"id\":\"2\",\"content\":\"1\"},{\"id\":\"2\",\"content\":\"2\"}]", null,
            "[{\"id\":\"2\",\"content\":\"2\"},{\"id\":\"2\",\"content\":\"1\"},{\"id\":\"1\",\"content\":\"1\"}]");
    }

    @Test
    public void testOrderedDescending() throws Exception {
        test("$^(<id, <content)",
            "[{\"id\":\"2\",\"content\":\"2\"},{\"id\":\"2\",\"content\":\"1\"},{\"id\":\"1\",\"content\":\"1\"}]", null,
            "[{\"id\":\"2\",\"content\":\"1\"},{\"id\":\"2\",\"content\":\"2\"},{\"id\":\"1\",\"content\":\"1\"}]");
    }

    @Test
    public void testOrderedMixed() throws Exception {
        test("$^(<id, content)",
            "[{\"id\":\"2\",\"content\":\"1\"},{\"id\":\"2\",\"content\":\"2\"},{\"id\":\"1\",\"content\":\"1\"}]", null,
            "[{\"id\":\"1\",\"content\":\"1\"},{\"id\":\"2\",\"content\":\"1\"},{\"id\":\"2\",\"content\":\"2\"}]");
    }

    @Test
    public void testUnordered() throws Exception {
        test("$",
            "[{\"id\":\"1\",\"content\":\"1\"},{\"id\":\"2\",\"content\":\"1\"},{\"id\":\"2\",\"content\":\"2\"}]", null,
            "[{\"id\":\"1\",\"content\":\"1\"},{\"id\":\"2\",\"content\":\"1\"},{\"id\":\"2\",\"content\":\"2\"}]");
    }
	
    @Test
    public void testOrderedNumeric() throws Exception {
        test("$^(id, content)",
                "[{\"id\":1,\"content\":\"1\"},{\"id\":2,\"content\":\"1\"},{\"id\":2,\"content\":\"2\"}]", null,
                "[{\"id\":2,\"content\":\"2\"},{\"id\":2,\"content\":\"1\"},{\"id\":1,\"content\":\"1\"}]");
     }
}
