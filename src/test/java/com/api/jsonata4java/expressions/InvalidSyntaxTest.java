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
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/**
 * Check that we correctly reject invalid uses of JSONata syntax
 */
@RunWith(Parameterized.class)
public class InvalidSyntaxTest implements Serializable {

    private static final long serialVersionUID = 126753089439983629L;

    @Parameter(0)
    public String expression;

    @Parameter(1)
    public boolean syntaxIsValid;

    @Parameters(name = "{0} -> {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {

            // sequence used outside of square brackets
            {
                "1..2", false
            },
            {
                "$count(1..2)", false
            }, // fixed #156

            // valid variable, note: evaluation will fail
            {
                "$notavar", true
            }, //

            {
                "$0", false
            }, // fixed #156 // (vars/functions cannot start with a number - lexer error)

            // Rule Parsing
            {
                "$exists(payload.entities)", true
            }, //
            {
                "function($v,$k){$v.value}", true
            }, //
            {
                "$exists(payload.results.bindings) and $count(payload.results.bindings) < 10", true
            }, //
            {
                "($array1:=payload.results.bindings;  $array2:=payload.results.vars; $append([[$array2]],$map($array1,function($obj1){$each($obj1,function($v,$k){$v.value})})))", //
                true
            }, //
            {
                "payload.context.cohibasAction=\"cohibasRestart\"", true
            }, //
            {
                "payload.countSummary.reductionType = \"\"", true
            }, //
            {
                "payload.cohibas.workflow.flowPointer+1", true
            }, //
            {
                "$exists(payload.metaPrefix) and $exists(payload.metaPrefixURI)", true
            }, //
            {
                "$exists(payload.setID) and $exists(payload.facets) and $exists(payload.prefixes) and $exists(payload.prefixTypes)", //
                true
            }, //
            {
                "$contains($lowercase(user_msg), \"new conversation\")", true
            }, //
            {
                "$substring(payload.input.text,0,$length(payload.input.text)-2)", true
            }, //
            {
                "\"The \" & payload.context.flight_route & \" is \" & payload.context.airport & \" and is located at (\" & payload.context.airportCoordinates.latitude & \",\" & payload.context.airportCoordinates.longitude & \")\"", //
                true
            }, //
            {
                "payload.context.flight_route and payload.context.airport and payload.context.airportCoordinates.latitude and payload.context.airportCoordinates.longitude", //
                true
            }, //
            {
                "\"actions\":[\"getFlightRouteReference\",\"getCoordinatesForAirportICAO\"]", false
            }, // added wnm3 #156
            {
                "{\"actions\":[\"getFlightRouteReference\",\"getCoordinatesForAirportICAO\"]}", true
            }, //
            {
                "payload.entities[entity=\"flight_route\"].value", true
            } //
        });
    }

    @Test
    public void runTest() throws Exception {
        try {
            Expressions.parse(expression);
            if (!syntaxIsValid) {
                Assert.fail("Expected a ParseException to be thrown when attempting to parse expression '" + expression
                    + "', but it was not");
            }
        } catch (ParseException ex) {
            if (syntaxIsValid) {
                ex.printStackTrace();
                Assert.fail("Expected expression '" + expression + "' to parse successfully, but it did not");
            }
        }
    }

}
