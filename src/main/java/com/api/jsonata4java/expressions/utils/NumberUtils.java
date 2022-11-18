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

package com.api.jsonata4java.expressions.utils;

import java.io.Serializable;
import com.api.jsonata4java.expressions.EvaluateRuntimeException;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.ValueNode;

public class NumberUtils implements Serializable {

    private static final long serialVersionUID = 1471647554518125118L;

    /**
     * The convertNumberToValueNode method converts the string passed in to a
     * suitable subclass of ValueNode depending on whether it is an integer or
     * decimal.
     * 
     * For consistency with the JavaScript implementation of JSONata, we limit the
     * size of the numbers that we handle to be within the range Double.MAX_VALUE
     * and -Double.MAX_VALUE. If we did not do this we would need to implement a lot
     * of extra code to handle BigInteger and BigDecimal.
     * 
     * @param number
     *               The string representation of the number to convert
     * @return ValueNode The ValueNode representation of the number contained in the
     *         input string
     * @throws EvaluateRuntimeException
     *                                  If the number represented by the string is
     *                                  outside of the valid range or if the string
     *                                  does not contain a valid number.
     */
    public static final ValueNode convertNumberToValueNode(String number) {

        // Create the variable to return
        ValueNode result = null;

        try {
            // Try to convert the number to a double
            Double doubleValue = Double.valueOf(number);

            // Check to see if the converted number is within the acceptable range
            if (!doubleValue.isInfinite() && !doubleValue.isNaN()) {
                if ((doubleValue >= 0.0d && doubleValue <= Double.valueOf(Long.MAX_VALUE))
                    || (doubleValue < 0.0d && doubleValue >= Double.valueOf(Long.MIN_VALUE))) {
                    if (doubleValue - doubleValue.longValue() == 0.0d) {
                        result = new LongNode((long) doubleValue.doubleValue());
                    } else {
                        result = new DoubleNode(doubleValue.doubleValue());
                    }
                } else {
                    result = new DoubleNode(doubleValue.doubleValue());
                }
            } else {
                final String msg = String.format(Constants.ERR_MSG_NUMBER_OUT_OF_RANGE, number);
                throw new EvaluateRuntimeException(msg);
            }
        } catch (NumberFormatException e2) {
            final String msg = String.format(Constants.ERR_MSG_UNABLE_TO_CAST_VALUE_TO_NUMBER, number);
            throw new EvaluateRuntimeException(msg);
        }

        return result;
    }
}
