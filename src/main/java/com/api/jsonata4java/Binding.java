/**
 * 
 */
package com.api.jsonata4java;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.CommonTokenFactory;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import com.api.jsonata4java.expressions.Expressions;
import com.api.jsonata4java.expressions.ParseException;
import com.api.jsonata4java.expressions.functions.DeclaredFunction;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.BooleanContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprListContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.NullContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.NumberContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.StringContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class mapping a variable name to a variable or function declaration
 * expression
 */
public class Binding implements Serializable {

    private static final long serialVersionUID = -7183986949584334476L;
    static private final ObjectMapper objectMapper = new ObjectMapper();

    String _varname = null;
    ExprContext _expr = null;
    DeclaredFunction _fct = null;
    BindingType _type;

    /**
     * Constructor using a variable assignment expression
     * 
     * @param expression
     *                   the variable assignment expression
     * @throws ParseException
     * @throws IOException
     */
    public Binding(String expression) throws ParseException, IOException {
        Expressions exprCTX = Expressions.parse(expression);
        ParseTree tree = exprCTX.getTree();
        try {
            if (tree instanceof MappingExpressionParser.Var_assignContext) {
                MappingExpressionParser.Var_assignContext ctx = (MappingExpressionParser.Var_assignContext) tree;
                _varname = ctx.VAR_ID().getText();
                ExprContext expr = ctx.expr();
                _expr = expr;
                _type = BindingType.VARIABLE;
            }
        } catch (ClassCastException cce) {
            throw new ParseException("Cannot transform expression to a variable assignment");
        }
    }

    /**
     * Constructor using a simple element (not an expression needing to be parsed)
     * 
     * @param varName
     *                name of the variable to be assigned a value
     * @param value
     *                the value to be assigned to the variable name
     * @throws ParseException 
     */
    public Binding(String varName, JsonNode value) throws ParseException {
        if (varName.startsWith("$") == false) {
            varName = "$" + varName;
        }
        _expr = (ExprContext) new MappingExpressionParser.StringContext(new ExprContext());
        CommonToken token = null;
        ExprContext ctx = new ExprContext();
        ctx.children = new ArrayList<>();
        switch (value.getNodeType()) {
            case OBJECT: {
                try {
                    String strValue = objectMapper.writeValueAsString(value);
                    Expressions exprCTX = Expressions.parse(strValue);
                    ParseTree tree = exprCTX.getTree();
                    _expr = (ExprContext)tree;
                } catch (IOException e) {
                    throw new ParseException("ERROR IN BINDINGS: "+e.getLocalizedMessage());
                }
                break;
            }
            case ARRAY: {
                try {
                    String strValue = objectMapper.writeValueAsString(value);
                    Expressions exprCTX = Expressions.parse(strValue);
                    ParseTree tree = exprCTX.getTree();
                    _expr = (ExprContext)tree;
                } catch (IOException e) {
                    throw new ParseException("ERROR IN BINDINGS: "+e.getLocalizedMessage());
                }
                break;
            }
            case BINARY:
            case POJO: {
                break;
            }
            case BOOLEAN: {
                token = (value.asBoolean() ? CommonTokenFactory.DEFAULT.create(MappingExpressionParser.TRUE, value.asText())
                    : CommonTokenFactory.DEFAULT.create(MappingExpressionParser.FALSE, value.asText()));
                TerminalNodeImpl tn = new TerminalNodeImpl(token);
                BooleanContext bc = new MappingExpressionParser.BooleanContext(ctx);
                bc.children.add(tn);
                _expr.addChild(bc);
                break;
            }
            case MISSING:
            case NULL: {
                token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.NULL, null);
                TerminalNodeImpl tn = new TerminalNodeImpl(token);
                NullContext nc = new MappingExpressionParser.NullContext(ctx);
                nc.children.add(tn);
                _expr.addChild(nc);
                break;
            }
            case NUMBER: {
                token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.NUMBER, value.asText());
                TerminalNodeImpl tn = new TerminalNodeImpl(token);
                NumberContext nc = new NumberContext(ctx);
                nc.children.add(tn);
                _expr.addChild(nc);
                break;
            }
            case STRING:
            default: {
                token = CommonTokenFactory.DEFAULT.create(MappingExpressionParser.STRING, value.asText());
                TerminalNodeImpl tn = new TerminalNodeImpl(token);
                StringContext sc = new StringContext(ctx);
                sc.children.add(tn);
                _expr.addChild(sc);
                break;
            }
        } // end switch
        _type = BindingType.VARIABLE;
        _varname = varName;
    }

    /**
     * Constructor using a variable name and either a variable or function
     * declaration expression
     * 
     * @param varName
     *                   name of the variable
     * @param expression
     *                   a variable or function declaration expression
     * @throws ParseException
     * @throws IOException
     */
    public Binding(String varName, String expression) throws ParseException, IOException {
        if (varName.startsWith("$") == false) {
            varName = "$" + varName;
        }
        Expressions exprCTX = Expressions.parse(expression);
        ParseTree tree = exprCTX.getTree();
        try {
            if (tree instanceof MappingExpressionParser.Function_declContext) {
                MappingExpressionParser.Function_declContext fctDeclCtx = (MappingExpressionParser.Function_declContext) tree;
                MappingExpressionParser.VarListContext varList = fctDeclCtx.varList();
                MappingExpressionParser.ExprListContext exprList = fctDeclCtx.exprList();
                DeclaredFunction fct = new DeclaredFunction(varList, exprList);
                _fct = fct;
                _type = BindingType.FUNCTION;
                _varname = varName;
            } else if (tree instanceof MappingExpressionParser.Var_assignContext) {
                MappingExpressionParser.Var_assignContext ctx = (MappingExpressionParser.Var_assignContext) tree;
                _varname = ctx.VAR_ID().getText();
                ExprContext expr = ctx.expr();
                _expr = expr;
                _type = BindingType.VARIABLE;
                _varname = varName;
            } else if (tree instanceof MappingExpressionParser.IdContext) {
                _expr = (ExprContext) new MappingExpressionParser.StringContext(new ExprContext());
                _expr.addChild((TerminalNode) tree.getChild(0));
                _type = BindingType.VARIABLE;
                _varname = varName;
            } else {
                _expr = (ExprContext) tree;
                _type = BindingType.VARIABLE;
                _varname = varName;
            }

        } catch (ClassCastException cce) {
            throw new ParseException("Cannot transform expression to a function declaration");
        }
    }

    /**
     * @return the name of the variable or function declaration
     */
    public String getVarName() {
        return _varname;
    }

    /**
     * 
     * @return the function declaration if the type is BindingType.FUNCTION,
     *         otherwise returns null
     */
    public DeclaredFunction getFunction() {
        return _fct;
    }

    /**
     * @return the expression defining the variable or function declaration
     */
    public ExprContext getExpression() {
        return _expr;
    }

    /**
     * 
     * @return the type of binding (either BindingType.VARIABLE or
     *         BindingType.FUNCTION)
     */
    public BindingType getType() {
        return _type;
    }

    /**
     * @return the string representation of the variable or function declaration
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        switch (_type) {
            case VARIABLE: {
                sb.append(_varname);
                sb.append("=");
                sb.append(_expr.getText());
                break;
            }
            case FUNCTION: {
                sb.append("function(");
                List<TerminalNode> varList = _fct.getVariables();
                int count = 0;
                for (TerminalNode tn : varList) {
                    if (count > 0) {
                        sb.append(",");
                    }
                    sb.append(tn.getText());
                    count++;
                }
                sb.append("){");
                ExprListContext expListCtx = _fct.getExpressionList();
                sb.append(expListCtx.getText());
                sb.append("}");
                break;
            }
        }
        return sb.toString();
    }

    /**
     * Test routine for Binding construction
     * 
     * @param args
     *             not used
     */
    public static void main(String[] args) {
        try {
            Binding test = new Binding("$x:=4");
            System.out.println(test);
            test = new Binding("addx", "function($a,$b){$a+$b}");
            System.out.println(test);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

}

/**
 * Enumeration for types of Bindings -- VARIABLE or FUNCTION
 */
enum BindingType {
        VARIABLE, FUNCTION;
}
