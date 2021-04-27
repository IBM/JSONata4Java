/**
 * (c) Copyright 2021 IBM Corporation
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

import java.util.Iterator;

import com.api.jsonata4java.expressions.ExpressionsVisitor;
import com.api.jsonata4java.expressions.ExpressionsVisitor.SelectorArrayNode;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprListContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprValuesContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_callContext;
import com.api.jsonata4java.expressions.utils.Constants;
import com.api.jsonata4java.expressions.utils.FunctionUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

/**
 * From https://docs.jsonata.org/array-functions#distinct
 * 
 * $distinct(array)
 * 
 * Returns an array containing all the values from the array parameter, 
 * but with any duplicates removed. Values are tested for deep equality 
 * as if by using the equality operator.
 *
 * Examples
 *
 * $distinct([1,2,3,3,4,3,5]) => [1, 2, 3, 4, 5]
 * $distinct(Account.Order.Product.Description.Colour) => [ "Purple", "Orange", "Black" ]
 *
 */
public class DistinctFunction extends FunctionBase implements Function {

   public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_EACH);
   public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_EACH);
   public static String ERR_ARG1_MUST_BE_ARRAY_OF_OBJECTS = String
         .format(Constants.ERR_MSG_ARG1_MUST_BE_ARRAY_OF_OBJECTS, Constants.FUNCTION_EACH);

   public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {
      JsonNode result = JsonNodeFactory.instance.nullNode();
      boolean useContext = FunctionUtils.useContextVariable(this, ctx, getSignature());
      JsonNode inputNode = null;
      ExprValuesContext valuesCtx = ctx.exprValues();
      ExprListContext exprList = valuesCtx.exprList();
		int argCount = getArgumentCount(ctx);
      if (useContext) {
         // pop context var from stack
         inputNode = FunctionUtils.getContextVariable(expressionVisitor);
			if (inputNode != null && inputNode.isNull() == false) {
				argCount++;
			} else {
				useContext = false;
			}
      }
      if (argCount <= 1) {
	      if (!useContext) {
	         inputNode = expressionVisitor.visit(exprList.expr(0));
	      }
	
	      if (inputNode == null) {
	         return null; // signal no match
	      }

	      if (!inputNode.isArray() || ((ArrayNode)inputNode).size() < 1) {
	      	result = inputNode;
	      } else { // isArray()==true
	      	// run through the array to find distinct members to fill the resultArray
		      // expect something that evaluates to an element, object or array
		      ArrayNode newResult = ((inputNode instanceof SelectorArrayNode) ? new SelectorArrayNode(JsonNodeFactory.instance) : JsonNodeFactory.instance.arrayNode());
		      ArrayNode array = (ArrayNode)inputNode;
		      if (inputNode instanceof SelectorArrayNode) {
		      	array = (SelectorArrayNode)inputNode;
		      }
		      JsonNode node1, node2;
		      boolean foundMatch = false;
		      for (Iterator<JsonNode> it = array.iterator();it.hasNext();) {
		      	node1 = it.next();
		      	for (Iterator<JsonNode>it2 = newResult.iterator();it2.hasNext();) {
		      		node2 = it2.next();
		      		if (node1 != null && node1.equals(node2)) {
		      			foundMatch = true;
		      			break;
		      		}
		      	}
		      	if (!foundMatch) {
		      		newResult.add(node1);
		      	}
		      	foundMatch = false;
		      }
		      result = newResult;
	      }
      }
      return result;
   }

	@Override
	public int getMaxArgs() {
		return 1;
	}
	@Override
	public int getMinArgs() {
		return 1; // account for context variable
	}

   @Override
   public String getSignature() {
      // accepts anything (or context variable), and a function, returns an array of objects
      return "<x:x>";
   }

}
