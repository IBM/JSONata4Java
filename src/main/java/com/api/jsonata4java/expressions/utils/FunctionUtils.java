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
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.CommonTokenFactory;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import com.api.jsonata4java.expressions.EvaluateRuntimeException;
import com.api.jsonata4java.expressions.ExpressionsVisitor;
import com.api.jsonata4java.expressions.functions.DeclaredFunction;
import com.api.jsonata4java.expressions.functions.FunctionBase;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Array_constructorContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.BooleanContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Context_refContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprListContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprOrSeqContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprOrSeqListContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprValuesContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Fct_chainContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.FieldListContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_callContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Function_declContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.IdContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.NullContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.NumberContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Object_constructorContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.PathContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.SeqContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.StringContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Unary_opContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Var_recallContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class FunctionUtils implements Serializable {

    private static final long serialVersionUID = -3388958465533493227L;

    /**
     * Extends a supplied {@link ExprValuesContext} using the supplied context and
     * array node content
     * 
     * @param ctx
     *                  context to serve as a parent context and provide invoking
     *                  state
     * @param evc
     *                  expression values context to be updated
     * @param arrayNode
     *                  array values to be added to the expression values context
     * @return {@link ExprValuesContext} updated with the supplied context and array
     *         context
     */
    public static ExprValuesContext addArrayExprVarContext(ExprContext ctx, ExprValuesContext evc, ArrayNode arrayNode) {
        if (evc == null) {
            evc = new ExprValuesContext(ctx, ctx.invokingState);
        }
        ExprListContext elc = (ExprListContext) evc.getChild(evc.getChildCount() - 2);
        CommonToken token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.T__2, ",");
        TerminalNodeImpl comma = new TerminalNodeImpl(token);
        elc.addChild(comma);
        ExprContext arrayCtx = getArrayConstructorContext(ctx, arrayNode);
        elc.addAnyChild(arrayCtx);
        return evc;
    }

    /**
     * Extends an {@link ExprValuesContext} using the context containing the
     * supplied index value
     * 
     * @param ctx
     *              context to serve as a parent context and provide invoking state
     * @param evc
     *              expression values context to be updated
     * @param index
     *              value to be added to the expression values context
     * @return {@link ExprValuesContext} updated using the context containing the
     *         supplied index value
     */
    public static ExprValuesContext addIndexExprVarContext(ExprContext ctx, ExprValuesContext evc, Integer index) {
        if (evc == null) {
            evc = new ExprValuesContext(ctx, ctx.invokingState);
        }
        ExprListContext elc = (ExprListContext) evc.getChild(evc.getChildCount() - 2);
        CommonToken token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.T__2, ",");
        TerminalNodeImpl comma = new TerminalNodeImpl(token);
        elc.addChild(comma);
        token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.NUMBER, index.toString());
        TerminalNodeImpl tn = new TerminalNodeImpl(token);
        NumberContext nc = new NumberContext(ctx);
        nc.addAnyChild(tn);
        elc.addAnyChild(nc);
        return evc;
    }

    /**
     * Extends an {@link ExprValuesContext} using the context containing the
     * supplied object value
     * 
     * @param ctx
     *               context to serve as a parent context and provide invoking state
     * @param evc
     *               expression values context to be updated
     * @param object
     *               value to be added to the expression values context
     * @return {@link ExprValuesContext} updated using the context containing the
     *         supplied index value
     */
    public static ExprValuesContext addObjectExprVarContext(ExprContext ctx, ExprValuesContext evc, ObjectNode object) {
        if (evc == null) {
            evc = new ExprValuesContext(ctx, ctx.invokingState);
        }
        ExprListContext elc = (ExprListContext) evc.getChild(evc.getChildCount() - 2);
        CommonToken token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.T__2, ",");
        TerminalNodeImpl comma = new TerminalNodeImpl(token);
        elc.addChild(comma);
        elc.addAnyChild(getObjectConstructorContext(ctx, object));
        return evc;
    }

    /**
     * Extends an {@link ExprValuesContext} using the context containing the
     * supplied string value
     * 
     * @param ctx
     *               context to serve as the parent of the generated context
     * @param evc
     *               expression values context
     * @param string
     *               value to be added to the ExprValuesContext
     * @return {@link ExprValuesContext} updated using the context containing the
     *         supplied index value
     */
    public static ExprValuesContext addStringExprVarContext(ExprContext ctx, ExprValuesContext evc, String string) {
        if (evc == null) {
            evc = new ExprValuesContext(ctx, ctx.invokingState);
        }
        ExprListContext elc = (ExprListContext) evc.getChild(evc.getChildCount() - 2);
        CommonToken token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.T__2, ",");
        TerminalNodeImpl comma = new TerminalNodeImpl(token);
        elc.addChild(comma);
        token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.STRING, string);
        TerminalNodeImpl tn = new TerminalNodeImpl(token);
        StringContext sc = new StringContext(ctx);
        sc.addAnyChild(tn);
        elc.addAnyChild(sc);
        return evc;
    }

    /**
     * Creates an {@link ExprValuesContext} containing a parenthesized comma
     * separated variable list of the supplied elements
     * 
     * @param fctVarCount
     *                 the maximum number of  parameters the receiving function will accept
     * @param ctx
     *                 context used to create the {@link ExprValuesContext}
     * @param elements
     *                 the values to be added to the list
     * @return {@link ExprValuesContext} containing a parenthesized comma separated
     *         variable list of the supplied elements
     */
    public static ExprValuesContext fillExprVarContext(int fctVarCount, ExprContext ctx, JsonNode... elements) {
        ExprValuesContext evc = new ExprValuesContext(ctx, ctx.invokingState);
        ExprListContext elc = new ExprListContext(ctx.getParent(), ctx.invokingState);
        evc.addAnyChild(new TerminalNodeImpl(CommonTokenFactory.DEFAULT.create(MappingExpressionParser.T__1, "(")));
        CommonToken token = null;
        for (int i = 0; i < elements.length; i++) {
            // only fill as many parameters as the receiving function will accept
            if (i > fctVarCount) {
                break;
            }
            JsonNode element = elements[i];
            if (i > 1) {
                token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.T__2, ",");
                TerminalNode tn = new TerminalNodeImpl(token);
                elc.addAnyChild(tn);
            }
            switch (element.getNodeType()) {
                case BINARY:
                case POJO: {
                    break;
                }
                case BOOLEAN: {
                    token = (element.asBoolean()
                        ? CommonTokenFactory.DEFAULT.create(MappingExpressionParser.TRUE, element.asText())
                        : CommonTokenFactory.DEFAULT.create(MappingExpressionParser.FALSE, element.asText()));
                    TerminalNodeImpl tn = new TerminalNodeImpl(token);
                    BooleanContext bc = new MappingExpressionParser.BooleanContext(ctx);
                    bc.op = token;
                    bc.addAnyChild(tn);
                    elc.addAnyChild(bc);
                    break;
                }
                case MISSING:
                case NULL: {
                    token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.NULL, null);
                    TerminalNodeImpl tn = new TerminalNodeImpl(token);
                    NullContext nc = new NullContext(ctx);
                    nc.addAnyChild(tn);
                    elc.addAnyChild(nc);
                    break;
                }
                case NUMBER: {
                    token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.NUMBER, element.asText());
                    TerminalNodeImpl tn = new TerminalNodeImpl(token);
                    NumberContext nc = new NumberContext(ctx);
                    nc.addAnyChild(tn);
                    elc.addAnyChild(nc);
                    break;
                }
                case OBJECT: {
                    // create an Object_constructorContext
                    elc.addAnyChild(getObjectConstructorContext(ctx, (ObjectNode) element));
                    break;
                }
                case ARRAY: {
                    elc.addAnyChild(getArrayConstructorContext(ctx, (ArrayNode) element));
                    break;
                }
                case STRING:
                default: {
                    token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.STRING, element.toString());
                    TerminalNodeImpl tn = new TerminalNodeImpl(token);
                    StringContext sc = new StringContext(ctx);
                    sc.addAnyChild(tn);
                    elc.addAnyChild(sc);
                    break;
                }
            }
        }
        evc.addAnyChild(elc);
        evc.addAnyChild(new TerminalNodeImpl(CommonTokenFactory.DEFAULT.create(MappingExpressionParser.T__3, ")")));
        return evc;
    }

    /**
     * Creates an {@link Array_constructorContext} from the supplied context and
     * array
     * 
     * @param ctx
     *              context to serve as parent of generated context.
     * @param array
     *              ArrayNode whose content is used to create the Array_constructor
     *              context
     * @return {@link Array_constructorContext} created from the supplied context
     *         and array
     */
    public static Array_constructorContext getArrayConstructorContext(ExprContext ctx, ArrayNode array) {
        Array_constructorContext arrayObj = new Array_constructorContext(ctx);
        ExprOrSeqListContext eosListCtx = new ExprOrSeqListContext(ctx.getParent(), ctx.invokingState);
        ExprOrSeqContext eosCtx = new ExprOrSeqContext(eosListCtx.getParent(), eosListCtx.invokingState);
        eosListCtx.addAnyChild(eosCtx);
        List<ParseTree> children = arrayObj.children;
        children.add(new TerminalNodeImpl(CommonTokenFactory.DEFAULT.create(MappingExpressionParser.ARR_OPEN, "[")));
        CommonToken token = null;
        // iterate through the array's nodes converting them to their respective
        // types
        for (int i = 0; i < array.size(); i++) {
            if (i > 0) {
                token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.T__2, ",");
                TerminalNode tn = new TerminalNodeImpl(token);
                eosCtx.addAnyChild(tn);
            }
            JsonNode element = array.get(i);
            switch (element.getNodeType()) {
                case BINARY:
                case POJO: {
                    break;
                }
                case ARRAY: {
                    eosCtx.addAnyChild(getArrayConstructorContext(ctx, (ArrayNode) element));
                    break;
                }
                case BOOLEAN: {
                    token = (element.asBoolean()
                        ? CommonTokenFactory.DEFAULT.create(MappingExpressionParser.TRUE, element.asText())
                        : CommonTokenFactory.DEFAULT.create(MappingExpressionParser.FALSE, element.asText()));
                    TerminalNodeImpl tn = new TerminalNodeImpl(token);
                    BooleanContext bc = new MappingExpressionParser.BooleanContext(ctx);
                    bc.op = token;
                    bc.addAnyChild(tn);
                    eosCtx.addAnyChild(bc);
                    break;
                }
                case MISSING:
                case NULL: {
                    token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.NULL, null);
                    TerminalNodeImpl tn = new TerminalNodeImpl(token);
                    NullContext nc = new MappingExpressionParser.NullContext(ctx);
                    nc.addAnyChild(tn);
                    eosCtx.addAnyChild(nc);
                    break;
                }
                case NUMBER: {
                    token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.NUMBER, element.asText());
                    TerminalNodeImpl tn = new TerminalNodeImpl(token);
                    NumberContext nc = new MappingExpressionParser.NumberContext(ctx);
                    nc.addAnyChild(tn);
                    eosCtx.addAnyChild(nc);
                    break;
                }
                case OBJECT: {
                    eosCtx.addAnyChild(getObjectConstructorContext(ctx, (ObjectNode) element));
                    break;
                }
                case STRING:
                default: {
                    token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.STRING, element.toString());
                    TerminalNodeImpl tn = new TerminalNodeImpl(token);
                    StringContext sc = new MappingExpressionParser.StringContext(ctx);
                    sc.addAnyChild(tn);
                    eosCtx.addAnyChild(sc);
                    break;
                }
            }
        }
        if (eosListCtx.exprOrSeq().size() > 0) {
            children.add(eosListCtx);
        }
        children.add(new TerminalNodeImpl(CommonTokenFactory.DEFAULT.create(MappingExpressionParser.ARR_CLOSE, "]")));
        return arrayObj;
    }

    /**
     * Retrieve the latest context variable from the {@link ExpressionsVisitor}
     * stack, allowing for a null to be returned (in lieu of a NullNode)
     * 
     * @param exprVisitor
     *                    the expression visitor whose stack is being used
     * @return the latest context variable, or a NullNode if none are available.
     */
    public static JsonNode getContextVariable(ExpressionsVisitor exprVisitor) {
        JsonNode result = null; // JsonNodeFactory.instance.nullNode();
        if (exprVisitor.getContextStack().isEmpty() == false) {
            result = exprVisitor.getContextStack().pop();
            exprVisitor.getContextStack().push(result);
        }
        return result;
    }

    /**
     * Retrieve the latest context variable from the {@link ExpressionsVisitor}
     * stack
     * 
     * @param exprVisitor
     * @return the latest context variable, or a NullNode if none are available.
     */
    public static JsonNode getContextVariableOrNullNode(ExpressionsVisitor exprVisitor) {
        JsonNode result = getContextVariable(exprVisitor);
        if (result == null) {
            result = JsonNodeFactory.instance.nullNode();
        }
        return result;
    }

    /**
     * Creates an {@link Object_constructorContext} from the supplied context and
     * object
     * 
     * @param ctx
     *               ObjectConstructorContext
     * @param object
     *               ObjectNode whose context is sought
     * @return {@link Object_constructorContext} created from the supplied context
     *         and object
     */
    public static Object_constructorContext getObjectConstructorContext(ExprContext ctx, ObjectNode object) {
        Object_constructorContext obj = new Object_constructorContext(ctx);
        List<ParseTree> children = obj.children;
        children.add(new TerminalNodeImpl(CommonTokenFactory.DEFAULT.create(MappingExpressionParser.OBJ_OPEN, "{")));
        // FieldListContext flc = new FieldListContext(ctx); // obj.getParent(),
        // obj.invokingState);
        FieldListContext flc = new FieldListContext(obj.getParent(), obj.invokingState);
        int count = 0;
        CommonToken token = null;
        TerminalNode tn = null;
        for (Iterator<Entry<String, JsonNode>> it = object.fields(); it.hasNext();) {
            Entry<String, JsonNode> field = it.next();
            count++;
            if (count > 1) {
                token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.T__2, ",");
                tn = new TerminalNodeImpl(token);
                flc.addAnyChild(tn);
            }
            token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.STRING, field.getKey());
            tn = new TerminalNodeImpl(token);
            flc.addAnyChild(tn);
            token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.T__5, ":");
            tn = new TerminalNodeImpl(token);
            flc.addAnyChild(tn);
            JsonNode fieldObj = field.getValue();
            switch (fieldObj.getNodeType()) {
                case BINARY:
                case POJO: {
                    break;
                }
                case ARRAY: {
                    flc.addAnyChild(getArrayConstructorContext(ctx, (ArrayNode) fieldObj));
                    break;
                }
                case BOOLEAN: {
                    token = (fieldObj.asBoolean()
                        ? CommonTokenFactory.DEFAULT.create(MappingExpressionParser.TRUE, fieldObj.asText())
                        : CommonTokenFactory.DEFAULT.create(MappingExpressionParser.FALSE, fieldObj.asText()));
                    tn = new TerminalNodeImpl(token);
                    BooleanContext bc = new BooleanContext(ctx);
                    bc.op = token;
                    bc.addAnyChild(tn);
                    flc.addAnyChild(bc);
                    break;
                }
                case MISSING:
                case NULL: {
                    token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.NULL, null);
                    tn = new TerminalNodeImpl(token);
                    NullContext nc = new NullContext(ctx);
                    nc.addAnyChild(tn);
                    flc.addAnyChild(nc);
                    break;
                }
                case NUMBER: {
                    token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.NUMBER, fieldObj.asText());
                    tn = new TerminalNodeImpl(token);
                    NumberContext nc = new NumberContext(ctx);
                    nc.addAnyChild(tn);
                    flc.addAnyChild(nc);
                    break;
                }
                case OBJECT: {
                    flc.addAnyChild(getObjectConstructorContext(ctx, (ObjectNode) fieldObj));

                    break;
                }
                case STRING:
                default: {
                    token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.STRING, fieldObj.toString());
                    tn = new TerminalNodeImpl(token);
                    StringContext sc = new StringContext(ctx);
                    sc.addAnyChild(tn);
                    flc.addAnyChild(sc);
                    break;
                }
            }
        }
        if (flc.children != null && flc.children.size() > 0) {
            children.add(flc);
        }
        children.add(new TerminalNodeImpl(CommonTokenFactory.DEFAULT.create(MappingExpressionParser.OBJ_CLOSE, "}")));
        return obj;
    }

    /**
     * Gets the expression at the supplied index from the context's
     * {@link ExprValuesContext} {@link ExprListContext}, preserving the null value
     * (or returning null if the index is invalid)
     * 
     * @param exprVisitor
     *                    used to visit the context's values
     * @param ctx
     *                    context providing {@link ExprValuesContext}
     *                    {@link ExprListContext}
     * @param index
     *                    non-negative index into the {@link ExprListContext}
     * @return value of the interpretation of the index-th expression from the
     *         context's {@link ExprValuesContext} {@link ExprListContext}, or a
     *         NullNode if none exists
     */
    public static JsonNode getValuesListExpression(ExpressionsVisitor exprVisitor, Function_callContext ctx, int index) {
        JsonNode result = null;
        try {
            if (ctx != null && ctx.exprValues() != null) {
                ExprContext exprCtx = ctx.exprValues().exprList().expr(index);
                result = exprVisitor.visit(exprCtx);
            }
        } catch (IndexOutOfBoundsException e) {
            ; // return result = null;
        } catch (ArithmeticException e) {
            /*
             * There do not appear to be sub-classes of ArithmeticException for specific
             * scenarios like dividing by zero... and the exception message appears to vary
             * by JVM... but it still appears to be the best way to determine what caused
             * the exception. For now, cover our bases, by checking for all of the different
             * messages that can be generated on the various JVMs that we use.
             */
            if (e.getMessage().equals("/ by zero") || e.getMessage().equals("divide by zero")) {
                result = new DoubleNode(Double.POSITIVE_INFINITY);
            }
        }
        return result;
    }

    /**
     * Checks the expression at the supplied index from the context's
     * {@link ExprValuesContext} {@link ExprListContext}, preserving the null value
     * (or returning null if the index is invalid) to determine if the arguments are
     * valid according to the supplied signature
     * 
     * @param exprVisitor
     *                    used to visit the context's values
     * @param ctx
     *                    context providing {@link ExprValuesContext}
     *                    {@link ExprListContext}
     * @param index
     *                    non-negative index into the {@link ExprListContext}
     * @param signature
     *                    the acceptable arguments signature
     * @throws EvaluateRuntimeException
     *                                  if the variables do not match the signature
     */
    public static void validateArguments(String possibleException, ExpressionsVisitor exprVisitor,
        Function_callContext ctx, int index, String signature) {
        try {
            if (ctx != null && ctx.exprValues() != null) {
                ExprContext exprCtx = ctx.exprValues().exprList().expr(index);
                // determine the type of this expression to see if it matches the signature
                if (checkArgument(exprVisitor, exprCtx, signature)) {
                    return;
                } // else throw exception
            }
        } catch (IndexOutOfBoundsException e) {
            ; // return result = null;
        } catch (ArithmeticException e) {
            ;
        }
        throw new EvaluateRuntimeException(possibleException);
    }

    /**
     * Tests whether the supplied exprCtx meets the signature expectations. Note: $
     * or $$ references to context are not explicitly tested as they are resolved
     * after this point. This test is for explicit variable declarations.
     * 
     * @param exprCtx
     *                  argument to be tested
     * @param signature
     *                  test to be performed
     * @return true if the argument meets the test
     */
    public static boolean checkArgument(ExpressionsVisitor exprVisitor, ExprContext exprCtx, String signature) {
        boolean result = false; // pessimistic
        switch (signature) {
            case "<a<n>-:n>":
            case "<a<n>:n>": {
                if (exprCtx instanceof Array_constructorContext) {
                    // must be array of numbers or a number
                    ParseTree ruleCtx = exprCtx.getChild(ExprOrSeqListContext.class, 0);
                    if (ruleCtx == null) {
                        // empty array
                        return true;
                    }
                    ruleCtx = ((ExprOrSeqListContext) ruleCtx).getChild(ExprOrSeqContext.class, 0);
                    if (ruleCtx != null) {
                        ParseTree test = ((ExprOrSeqContext) ruleCtx).getChild(Context_refContext.class, 0);
                        if (test != null) {
                            return true;
                        }
                        test = ((ExprOrSeqContext) ruleCtx).getChild(NumberContext.class, 0);
                        if (test != null) {
                            return true;
                        }
                    }
                    if (ruleCtx instanceof Array_constructorContext) {
                        ruleCtx = ((Array_constructorContext) ruleCtx).getPayload();
                        if (ruleCtx instanceof Context_refContext) {
                            return true;
                        }
                    }
                    if (ruleCtx instanceof ExprOrSeqContext) {
                        ParseTree seqCtx = ((ExprOrSeqContext) ruleCtx).getChild(SeqContext.class, 0);
                        if (seqCtx != null) {
                            return true;
                        }
                    }
                    JsonNode test = exprVisitor.visit(ruleCtx);
                    if (test.isNumber()) {
                        return true;
                    }
                } else if (exprCtx instanceof NumberContext || exprCtx instanceof Unary_opContext) {
                    // must be array of numbers or a number
                    return true;
                } else if (exprCtx instanceof PathContext) {
                    return true;
                } else if (exprCtx instanceof IdContext) {
                    return true;
                }
            }
        }
        return result;
    }

    /**
     * Gets the expression at the supplied index from the context's {link
     * ExprValuesContext} {link ExprListContext]
     * 
     * @param exprVisitor
     *                    used to visit the context's values
     * @param ctx
     *                    context providing {@link ExprValuesContext}
     *                    {@link ExprListContext}
     * @param index
     *                    non-negative index into the {@link ExprListContext}
     * @return value of the interpretation of the index-th expression from the
     *         context's {@link ExprValuesContext} {@link ExprListContext}, or a
     *         NullNode if none exists
     */
    public static JsonNode getValuesListExpressionOrNullNode(ExpressionsVisitor exprVisitor, Function_callContext ctx,
        int index) {
        JsonNode result = getValuesListExpression(exprVisitor, ctx, index);
        if (result == null) {
            result = JsonNodeFactory.instance.nullNode();
        }
        return result;
    }

    /**
     * Creates an {@link ExprListContext} containing the parenthesized comma
     * separated variable list of JsonNode elements, sets it in the supplied context
     * (ctx) and then executes the function to return its result.
     * 
     * @param exprVisitor
     *                    used to invoke the function after updating its context
     *                    variables
     * @param function
     *                    the function to be invoked
     * @param varid
     *                    the variable to be associated with the elements
     * @param ctx
     *                    the context to be updated and used by the function when
     *                    invoked
     * @param elements
     *                    the values to be set in the {@link ExprValuesContext} used
     *                    by the function when invoked
     * @return the result of calling the function with the updated context
     */
    public static JsonNode processVariablesCallFunction(ExpressionsVisitor exprVisitor, FunctionBase function,
        TerminalNode varid, Function_callContext ctx, JsonNode... elements) {
        ExprListContext elc = new ExprListContext(ctx.getParent(), ctx.invokingState);
        ExprValuesContext evc = new ExprValuesContext(ctx.getParent(), ctx.invokingState);
        evc.addAnyChild(new TerminalNodeImpl(CommonTokenFactory.DEFAULT.create(MappingExpressionParser.T__1, "(")));
        CommonToken token = null;
        for (int i = 0; i < elements.length; i++) {
            if (i > 0) {
                token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.T__2, ",");
                TerminalNode tn = new TerminalNodeImpl(token);
                elc.addAnyChild(tn);
            }
            JsonNode element = elements[i];
            switch (element.getNodeType()) {
                case BINARY:
                case POJO: {
                    break;
                }
                case BOOLEAN: {
                    token = (element.asBoolean()
                        ? CommonTokenFactory.DEFAULT.create(MappingExpressionParser.TRUE, element.asText())
                        : CommonTokenFactory.DEFAULT.create(MappingExpressionParser.FALSE, element.asText()));
                    TerminalNodeImpl tn = new TerminalNodeImpl(token);
                    BooleanContext bc = new MappingExpressionParser.BooleanContext(ctx);
                    bc.op = token;
                    bc.addAnyChild(tn);
                    elc.addAnyChild(bc);
                    break;
                }
                case MISSING:
                case NULL: {
                    token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.NULL, null);
                    TerminalNodeImpl tn = new TerminalNodeImpl(token);
                    NullContext nc = new NullContext(ctx);
                    nc.addAnyChild(tn);
                    elc.addAnyChild(nc);
                    break;
                }
                case NUMBER: {
                    token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.NUMBER, element.asText());
                    TerminalNodeImpl tn = new TerminalNodeImpl(token);
                    NumberContext nc = new NumberContext(ctx);
                    nc.addAnyChild(tn);
                    elc.addAnyChild(nc);
                    break;
                }
                case OBJECT: {
                    elc.addAnyChild(getObjectConstructorContext(ctx, (ObjectNode) element));
                    break;
                }
                case ARRAY: {
                    elc.addAnyChild(getArrayConstructorContext(ctx, (ArrayNode) element));
                    break;
                }
                case STRING:
                default: {
                    token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.STRING, element.toString());
                    TerminalNodeImpl tn = new TerminalNodeImpl(token);
                    StringContext sc = new StringContext(ctx);
                    sc.addAnyChild(tn);
                    elc.addAnyChild(sc);
                    break;
                }
            }
        }
        evc.addAnyChild(elc);
        evc.addAnyChild(new TerminalNodeImpl(CommonTokenFactory.DEFAULT.create(MappingExpressionParser.T__3, ")")));
        ctx.addAnyChild(varid);
        ctx.addAnyChild(evc);
        return function.invoke(exprVisitor, ctx);
    }

    /**
     * Creates an {@link ExprListContext} containing the parenthesized comma
     * separated variable list of JsonNode elements, sets it in the supplied context
     * (ctx) and then executes the function to return its result.
     * 
     * @param exprVisitor
     *                    used to invoke the function after updating its context
     *                    variables
     * @param function
     *                    the function to be invoked
     * @param varid
     *                    the variable to be associated with the elements
     * @param ctx
     *                    the context to be updated and used by the function when
     *                    invoked
     * @param value
     *                    the value to be set in the {@link ExprValuesContext} used
     *                    by the function when invoked
     * @param key
     *                    the key to be set in the {@link ExprValuesContext} used by
     *                    the function when invoked
     * @param object
     *                    the object to be set in the {@link ExprValuesContext} used
     *                    by the function when invoked
     * @return the result of calling the function with the updated context
     */
    public static JsonNode processFctCallVariables(ExpressionsVisitor exprVisitor, FunctionBase function, TerminalNode varid,
        Function_callContext ctx, JsonNode value, String key, ObjectNode object) {
        ExprListContext elc = new ExprListContext(ctx.getParent(), ctx.invokingState);
        ExprValuesContext evc = new ExprValuesContext(ctx.getParent(), ctx.invokingState);
        evc.addAnyChild(new TerminalNodeImpl(CommonTokenFactory.DEFAULT.create(MappingExpressionParser.T__1, "(")));
        CommonToken token = null;
        int fctVarCount = function.getMaxArgs();

        // add the value
        JsonNode element = value;
        switch (element.getNodeType()) {
            case BINARY:
            case POJO: {
                break;
            }
            case BOOLEAN: {
                token = (element.asBoolean()
                    ? CommonTokenFactory.DEFAULT.create(MappingExpressionParser.TRUE, element.asText())
                    : CommonTokenFactory.DEFAULT.create(MappingExpressionParser.FALSE, element.asText()));
                TerminalNodeImpl tn = new TerminalNodeImpl(token);
                BooleanContext bc = new MappingExpressionParser.BooleanContext(ctx);
                bc.op = token;
                bc.addAnyChild(tn);
                elc.addAnyChild(bc);
                break;
            }
            case MISSING:
            case NULL: {
                token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.NULL, null);
                TerminalNodeImpl tn = new TerminalNodeImpl(token);
                NullContext nc = new NullContext(ctx);
                nc.addAnyChild(tn);
                elc.addAnyChild(nc);
                break;
            }
            case NUMBER: {
                token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.NUMBER, element.asText());
                TerminalNodeImpl tn = new TerminalNodeImpl(token);
                NumberContext nc = new NumberContext(ctx);
                nc.addAnyChild(tn);
                elc.addAnyChild(nc);
                break;
            }
            case OBJECT: {
                elc.addAnyChild(getObjectConstructorContext(ctx, (ObjectNode) element));
                break;
            }
            case ARRAY: {
                elc.addAnyChild(getArrayConstructorContext(ctx, (ArrayNode) element));
                break;
            }
            case STRING:
            default: {
                token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.STRING, element.toString());
                TerminalNodeImpl tn = new TerminalNodeImpl(token);
                StringContext sc = new StringContext(ctx);
                sc.addAnyChild(tn);
                elc.addAnyChild(sc);
                break;
            }
        }
        if (fctVarCount > 1) {
            token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.T__2, ",");
            TerminalNode tn = new TerminalNodeImpl(token);
            elc.addAnyChild(tn);
            token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.STRING, key);
            tn = new TerminalNodeImpl(token);
            StringContext sc = new StringContext(ctx);
            sc.addAnyChild(tn);
            elc.addAnyChild(sc);
        }

        if (fctVarCount > 2) {
            token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.T__2, ",");
            TerminalNode tn = new TerminalNodeImpl(token);
            elc.addAnyChild(tn);
            elc.addAnyChild(getObjectConstructorContext(ctx, object));
        }
        evc.addAnyChild(elc);
        evc.addAnyChild(new TerminalNodeImpl(CommonTokenFactory.DEFAULT.create(MappingExpressionParser.T__3, ")")));
        ctx.addAnyChild(varid);
        ctx.addAnyChild(evc);
        return function.invoke(exprVisitor, ctx);
    }

    /**
     * Determine if the function should use the context variable as its first
     * parameter
     * 
     * @param fct     
     *                  the function checking whether to use the context variable
     * @param ctx
     *                  the context used to invoke the function
     * @param signature
     *                  the function signature for the function being tested. If
     *                  this contains a hyphen, then it can use the context
     * @return true if the context variable should be used as the first parameter
     */
    public static boolean useContextVariable(FunctionBase fct, Function_callContext ctx, String signature) {
        if (ctx == null) { // || ctx.getParent() == null) {
            return false;
        }

        int argCount = FunctionBase.getArgumentCount(ctx);
        if (argCount == 0) {
            // no arguments so try to get from context
            return true;
        }

        ParserRuleContext prc = ctx.getParent();
        if (prc != null && prc instanceof Fct_chainContext) {
            // you are called from a chain so if you are not the first child use context
            for (int i = 0; i < prc.children.size(); i++) {
                if (prc.getChild(i) == ctx) {
                    if (i > 0) {
                        return true;
                    }
                    break;
                }
            }
        }

        int optional = getOptionalArgCount(signature);

        int min = fct.getMinArgs();
        int max = fct.getMaxArgs();
        // check when no optional arguments presented
        if (min == max) {
            return argCount < min;
        }

        if (argCount < max - optional) {
            // should have required argument in context
            return true;
        }

        // does the signature permit use of the context as a argument
        if (signature.indexOf("-") >= 0 && prc != null && prc instanceof PathContext) {
            return true;
        }

        return false;
    }

    public static int getOptionalArgCount(String signature) {
        int optional = 0;
        int optionIndex = signature.indexOf("?");
        while (optionIndex != -1) {
            optional++;
            signature = signature.substring(optionIndex + 1);
            optionIndex = signature.indexOf("?");

        }
        return optional;
    }

    public static DeclaredFunction getFunctionArgFromCtx(ExpressionsVisitor expressionVisitor, Function_callContext ctx, boolean useContext) {
        if (ctx.exprValues() == null
            || ctx.exprValues().exprList() == null
            || ctx.exprValues().exprList().expr() == null
            || ctx.exprValues().exprList().expr().size() == 0) {
            return null;
        }
        final ExprContext varid = ctx.exprValues().exprList().expr(useContext ? 0 : 1);
        if (varid instanceof Var_recallContext) {
            return expressionVisitor.getDeclaredFunction(ctx.exprValues().exprList().expr(0).getText());
        } else if (varid instanceof Function_declContext) {
            final Function_declContext fctDeclCtx = (Function_declContext) ctx.exprValues().exprList().expr(useContext ? 0 : 1);
            return new DeclaredFunction(fctDeclCtx.varList(), fctDeclCtx.exprList());
        }
        return null;
    }

}
