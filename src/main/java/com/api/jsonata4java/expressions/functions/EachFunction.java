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

import org.antlr.v4.runtime.tree.TerminalNode;

import com.api.jsonata4java.expressions.EvaluateRuntimeException;
import com.api.jsonata4java.expressions.ExpressionsVisitor;
import com.api.jsonata4java.expressions.ExpressionsVisitor.SelectorArrayNode;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprListContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprValuesContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_callContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_declContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.VarListContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Var_recallContext;
import com.api.jsonata4java.expressions.utils.Constants;
import com.api.jsonata4java.expressions.utils.FunctionUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * From https://docs.jsonata.org/object-functions#each
 * 
 * $each(object,function($value, $key)
 * 
 * Returns an array containing the values return by the function when applied to
 * each key/value pair in the object.
 *
 * The function parameter will get invoked with two arguments:
 *
 * function(value, name)
 *
 * where the value parameter is the value of each name/value pair in the object
 * and name is its name. The name parameter is optional.
 *
 * Examples
 *
 * $each(Address, function($v, $k) {$k & ": " & $v})
 *
 * =>
 *
 * [ "Street: Hursley Park", "City: Winchester", "Postcode: SO21 2JN" ]
 * 
 */
public class EachFunction extends FunctionBase implements Function {

   public static String ERR_BAD_CONTEXT = String.format(Constants.ERR_MSG_BAD_CONTEXT, Constants.FUNCTION_EACH);
   public static String ERR_ARG1BADTYPE = String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_EACH);
   public static String ERR_ARG2BADTYPE = String.format(Constants.ERR_MSG_ARG2_BAD_TYPE, Constants.FUNCTION_EACH);
   public static String ERR_ARG1_MUST_BE_ARRAY_OF_OBJECTS = String
         .format(Constants.ERR_MSG_ARG1_MUST_BE_ARRAY_OF_OBJECTS, Constants.FUNCTION_EACH);

   public JsonNode invoke(ExpressionsVisitor expressionVisitor, Function_callContext ctx) {
      SelectorArrayNode resultArray = new SelectorArrayNode(JsonNodeFactory.instance);
      boolean useContext = ((ctx.getParent() instanceof MappingExpressionParser.Fct_chainContext)
            || (ctx.getParent() instanceof MappingExpressionParser.PathContext));
      JsonNode objNode = null;
      ExprValuesContext valuesCtx = ctx.exprValues();
      ExprListContext exprList = valuesCtx.exprList();
      if (useContext) {
         // pop context var from stack
         objNode = FunctionUtils.getContextVariable(expressionVisitor);
      } else {

         objNode = expressionVisitor.visit(exprList.expr(0));
      }
      // expect something that evaluates to an object and a function declaration

      if (objNode == null || !objNode.isObject()) {
         throw new EvaluateRuntimeException(String.format(Constants.ERR_MSG_ARG1_BAD_TYPE, Constants.FUNCTION_EACH));
      }
      ObjectNode object = (ObjectNode) objNode;

      ExprContext varid = exprList.expr((useContext ? 0 : 1));
      if (varid instanceof Var_recallContext) {
         TerminalNode VAR_ID = ((Var_recallContext)varid).VAR_ID();
         String varID = varid.getText();
         // get the function to be executed from the functionMap and execute
         DeclaredFunction fct = expressionVisitor.getDeclaredFunction(varID);
         if (fct != null) {
            int varCount = fct.getVariableCount();
	         int fctVarCount = fct.getMaxArgs();
	         if (varCount > fctVarCount) {
	         	// only send variables function can consume
	         	varCount = fctVarCount;
	         }
            for (Iterator<String> it = object.fieldNames(); it.hasNext();) {
               String key = it.next();
               JsonNode field = object.get(key);
               ExprValuesContext evc = new ExprValuesContext(ctx, ctx.invokingState);
               switch (varCount) {
               case 1: {
                  // just pass the field value
                  evc = FunctionUtils.fillExprVarContext(varCount, ctx, field);
                  break;
               }
               case 2: {
                  // pass the field value and key
                  evc = FunctionUtils.fillExprVarContext(varCount, ctx, field);
                  evc = FunctionUtils.addStringExprVarContext(ctx, evc, key);
                  break;
               }
               case 3: {
                  // pass the field value, key, and object
                  evc = FunctionUtils.fillExprVarContext(varCount, ctx, field);
                  evc = FunctionUtils.addStringExprVarContext(ctx, evc, key);
                  evc = FunctionUtils.addObjectExprVarContext(ctx, evc, object);
                  break;
               }
               }
               JsonNode fctResult = fct.invoke(expressionVisitor, evc);
               if (fctResult != null) {
                  resultArray.add(fctResult);
               }
            }
         } else {
	         Function function = expressionVisitor.getJsonataFunction(varid.getText());
	         if (function != null) {
	            for (Iterator<String> it = object.fieldNames(); it.hasNext();) {
	               String key = it.next();
	               JsonNode field = object.get(key);
	               Function_callContext callCtx = new Function_callContext(ctx);
	               // note: callCtx.children should be empty unless carrying an
	               // exception
	               resultArray.add(FunctionUtils.processVariablesCallFunction(expressionVisitor, function, VAR_ID, callCtx, field));
	            }
	         } else {
	            throw new EvaluateRuntimeException(
	                  "Expected function variable reference " + varID + " to resolve to a declared nor Jsonata function.");
	         }
         }
      } else if (varid instanceof Function_declContext) {
         Function_declContext fctDeclCtx = (Function_declContext) exprList.expr((useContext ? 0 : 1));
   
         // we have a declared function for each
         VarListContext varList = fctDeclCtx.varList();
         ExprListContext fctBody = fctDeclCtx.exprList();
         DeclaredFunction fct = new DeclaredFunction(varList, fctBody);
         int varCount = fct.getVariableCount();
         int fctVarCount = fct.getMaxArgs();
         if (varCount > fctVarCount) {
         	// only send variables function can consume
         	varCount = fctVarCount;
         }
         for (Iterator<String> it = object.fieldNames(); it.hasNext();) {
            String key = it.next();
            JsonNode field = object.get(key);
            ExprValuesContext evc = new ExprValuesContext(ctx, ctx.invokingState);
            switch (varCount) {
            case 1: {
               // just pass the field value
               evc = FunctionUtils.fillExprVarContext(varCount, ctx, field);
               break;
            }
            case 2: {
               // pass the field value and key
               evc = FunctionUtils.fillExprVarContext(varCount, ctx, field);
               evc = FunctionUtils.addStringExprVarContext(ctx, evc, key);
               break;
            }
            case 3: {
               // pass the field value, key, and object
               evc = FunctionUtils.fillExprVarContext(varCount, ctx, field);
               evc = FunctionUtils.addStringExprVarContext(ctx, evc, key);
               evc = FunctionUtils.addObjectExprVarContext(ctx, evc, object);
               break;
            }
            }
            JsonNode fctResult = fct.invoke(expressionVisitor, evc);
            if (fctResult != null) {
               resultArray.add(fctResult);
            }
         }
      }
      return resultArray;
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
