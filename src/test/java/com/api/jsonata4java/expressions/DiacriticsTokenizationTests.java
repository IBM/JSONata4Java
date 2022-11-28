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

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import com.api.jsonata4java.expressions.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Allow "any kind of letter from any language" instead of [a-zA-Z] in jsonata expressions
 */
@RunWith(Parameterized.class)
public class DiacriticsTokenizationTests implements Serializable {

    private static final long serialVersionUID = 7061882983195426346L;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Parameter(0)
    public String expression;

    @Parameter(1)
    public String jsonObj;

    @Parameter(2)
    public String expectedResultJsonString;

    @Parameters(name = "{index}: {0} + {1} = {2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            {
                "{ \"Title\": `Tytuł` }", "{ \"Tytuł\" : \"Pan Tadeusz\" }", "{ \"Title\": \"Pan Tadeusz\" }"
            }, //
            {
                "{ \"Title\": `Książka`.`Tytuł` }", "{ \"Książka\" : { \"Tytuł\" : \"Pan Tadeusz\" } }", "{ \"Title\": \"Pan Tadeusz\" }"
            }, //
            {
                "{ \"Title\": Tytuł }", "{ \"Tytuł\" : \"Pan Tadeusz\" }", "{ \"Title\": \"Pan Tadeusz\" }"
            }, //
            {
                "{ \"Title\": Książka.Tytuł }", "{ \"Książka\" : { \"Tytuł\" : \"Pan Tadeusz\" } }", "{ \"Title\": \"Pan Tadeusz\" }"
            }, //
            {
                "{ \"Title\": Żółw.Tytuł }", "{ \"Żółw\" : { \"Tytuł\" : \"Pan Tadeusz\" } }", "{ \"Title\": \"Pan Tadeusz\" }"
            }, //
        });
    }

    @Test
    public void runTest() throws Exception {
        Utils.simpleTest(this.expression, objectMapper.readTree(expectedResultJsonString), objectMapper.readTree(jsonObj));
    }

}
