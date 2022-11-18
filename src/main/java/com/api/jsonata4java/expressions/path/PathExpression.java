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

package com.api.jsonata4java.expressions.path;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.tree.ParseTree;
import com.api.jsonata4java.expressions.BufferingErrorListener;
import com.api.jsonata4java.expressions.ParseException;
import com.api.jsonata4java.expressions.path.generated.PathExpressionLexer;
import com.api.jsonata4java.expressions.path.generated.PathExpressionParser;
import com.fasterxml.jackson.databind.JsonNode;

@SuppressWarnings("deprecation")
public class PathExpression {

    private String expression;
    private ParseTree tree;

    private PathExpression(String expr, ParseTree tree) {
        this.expression = expr;
        this.tree = tree;
    }

    public JsonNode get(JsonNode jsonToRead, Integer indexVarValue) {
        PathExpressionVisitor visitor = new PathExpressionVisitor.Getter(jsonToRead, indexVarValue);
        return visitor.visit(tree);
    }

    public JsonNode set(JsonNode jsonToModify, Integer indexVarValue, JsonNode valueToAssign) {
        PathExpressionVisitor visitor = new PathExpressionVisitor.Setter(jsonToModify, indexVarValue, valueToAssign);
        return visitor.visit(tree);
    }

    @Override
    public String toString() {
        return this.getExpression();
    }

    public String getExpression() {
        return expression;
    }

    public static PathExpression parse(final String expr) throws ParseException {

        ANTLRInputStream input = new ANTLRInputStream(expr);
        PathExpressionLexer lexer = new PathExpressionLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PathExpressionParser parser = new PathExpressionParser(tokens);
        ParseTree tree = null;

        BufferingErrorListener errorListener = new BufferingErrorListener();

        try {
            // remove the default error listeners which print to stderr
            parser.removeErrorListeners();
            lexer.removeErrorListeners();

            // replace with error listener that buffer errors and allow us to retrieve them
            // later
            parser.addErrorListener(errorListener);
            lexer.addErrorListener(errorListener);

            tree = parser.expr();
            if (errorListener.heardErrors()) {
                throw new ParseException(errorListener.getErrorsAsString());
            }
        } catch (RecognitionException e) {
            throw new ParseException(e.getMessage());
        }

        return new PathExpression(expr, tree);
    }

}
