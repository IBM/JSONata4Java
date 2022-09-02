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

import java.io.Serializable;

import com.api.jsonata4java.expressions.antlr.MappingExpressionParser.Function_callContext;

public abstract class FunctionBase implements Serializable {

	private static final long serialVersionUID = -2198474650471622735L;

	/**
	 * The getFunctionName method retrieves the name of the function from the
	 * context
	 *
	 * @param ctx The Function_callContext for the function.
	 * @return String The name of the function.
	 */
	public String getFunctionName(Function_callContext ctx) {
		return ctx.VAR_ID().getText();
	}

	/**
	 * The getArgumentCount method counts the number of expressions in the
	 * expression list.
	 *
	 * @param ctx The Function_callContext for the function.
	 * @return int The number of arguments for the function
	 */
	static public int getArgumentCount(Function_callContext ctx) {
		if (ctx.emptyValues() != null)
			return 0; // no variables
		if (ctx.exprValues().exprList() == null)
			return 0;

		return ctx.exprValues().exprList().expr().size();
	}
}
