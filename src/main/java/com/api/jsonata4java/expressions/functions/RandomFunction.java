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

package com.api.jsonata4java.expressions.functions;

import com.api.jsonata4java.expressions.EvaluateRuntimeException;
import com.api.jsonata4java.expressions.ExpressionsVisitor;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_callContext;
import com.api.jsonata4java.expressions.utils.Constants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.DoubleNode;

/**
 * http://docs.jsonata.org/numeric-functions.html
 *
 * $random()
 * 
 * Returns a pseudo random number greater than or equal to zero and less than
 * one (0 ≤ n &LT; 1)
 * 
 * Examples
 * 
 * $random()==0.7973541067127 $random()==0.4029142127028 $random() ==
 * 0.6558078550072
 *
 */
public class RandomFunction extends FunctionBase {

    public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_RANDOM);

    public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {
        // Create the variable to return
        JsonNode result = null;

        // Retrieve the number of arguments
        int argCount = getArgumentCount(ctx);

        // Make sure that we have the right number of arguments
        if (argCount == 0) {
            // Generate the random number and create the node to return
            double random = Math.random();
            result = new DoubleNode(random);
        } else {
            throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
        }

        return result;
    }

    @Override
    public int getMaxArgs() {
        return 0;
    }

    @Override
    public int getMinArgs() {
        return 0;
    }

    @Override
    public String getSignature() {
        // accepts nothing, returns a number
        return "<:n>";
    }
}
