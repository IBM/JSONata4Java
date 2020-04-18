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

import java.util.Iterator;

import com.api.jsonata4java.expressions.EvaluateRuntimeException;
import com.api.jsonata4java.expressions.ExpressionsVisitor;
import com.api.jsonata4java.expressions.ExpressionsVisitor.SelectorArrayNode;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_callContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_declContext;
import com.api.jsonata4java.expressions.utils.Constants;
import com.api.jsonata4java.expressions.utils.FunctionUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

/**
 * From http://docs.jsonata.org/object-functions.html
 * 
 * $spread(object)
 * 
 * Splits an object containing key/value pairs into an array of objects, each of
 * which has a single key/value pair from the input object. If the parameter is
 * an array of objects, then the resultant array contains an object for every
 * key/value pair in every object in the supplied array.
 * 
 */
public class SpreadFunction extends FunctionBase implements Function {

   public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_SPREAD);
   public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_SPREAD);
   public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_SPREAD);
   public static String ERR_ARG1_MUST_BE_ARRAY_OF_OBJECTS = String
         .format(Constants.ERR_MSG_ARG1_MUST_BE_ARRAY_OF_OBJECTS, Constants.FUNCTION_SPREAD);

   public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {
      // Create the variable to return
      SelectorArrayNode result = new SelectorArrayNode(JsonNodeFactory.instance);

      // Retrieve the number of arguments
      JsonNode argObject = JsonNodeFactory.instance.nullNode();
      boolean useContext = FunctionUtils.useContextVariable(ctx, getSignature());
      int argCount = getArgumentCount(ctx);
      if (useContext) {
         argObject = FunctionUtils.getContextVariable(expressionVisitor);
         argCount++;
      }
      boolean argIsArray = false;
      // Make sure that we have the right number of arguments
      if (argCount == 1) {
         if (!useContext) {
            argObject = FunctionUtils.getValuesListExpression(expressionVisitor, ctx, 0);
            if (argObject == null) {
               ExprContext exprCtx = ctx.exprValues().exprList().expr(0);
               if (exprCtx instanceof Function_declContext) {
                  argObject = new TextNode("");
               }
            }
         }

         if (argObject != null) {
            if (argObject.isObject()) {
               ObjectNode obj = (ObjectNode) argObject;
               if (obj.size() > 0) {
                  addObject(result, obj);
               } else {
                  return null;
               }
            } else if (argObject.isArray()) {
               argIsArray = true;
               ArrayNode objArray = (ArrayNode) argObject;
               if (objArray.size() == 0) {
                  return null;
               }
               // process each object in the array
               for (int i = 0; i < objArray.size(); i++) {
                  JsonNode node = objArray.get(i);
                  if (node.isObject()) {
                     ObjectNode obj = (ObjectNode) node;
                     addObject(result, obj);
                  } else {
                     // jsonata.js 1.8 just keeps non-objects
                     // throw new EvaluateRuntimeException(ERR_ARG1_MUST_BE_ARRAY_OF_OBJECTS);
                     result.add(node);
                  }

               }
            } else {
               /*
                * The input argument is not an object nor array of objects. Throw a suitable
                * exception
                */
               // throw new EvaluateRuntimeException(ERR_ARG1BADTYPE);
               // jsonata.js 1.8 just adds the argument
               result.add(argObject);
            }
         } else {
            result = null;
         }
      } else {
         throw new EvaluateRuntimeException(argCount == 0 ? ERR_BAD_CONTEXT : ERR_ARG2BADTYPE);
      }

      if (result != null && argIsArray == false) {
         JsonNode test = ExpressionsVisitor.unwrapArray(result);
         if (test.isArray() && test instanceof SelectorArrayNode) {
            result = (SelectorArrayNode) test;
         } else {
            return test;
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
		return 1;
	}

   @Override
   public String getSignature() {
      // accepts anything (or context variable), returns an array of objects
      return "<x-:a<o>";
   }

   public void addObject(SelectorArrayNode result, ObjectNode obj) {
      for (Iterator<String> it = obj.fieldNames(); it.hasNext();) {
         String key = it.next();
         ObjectNode cell = JsonNodeFactory.instance.objectNode();
         cell.set(key, obj.get(key));
         result.add(cell);
      }
   }
}
