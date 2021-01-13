/**
 * 
 */
package com.api.jsonata4java;

import java.io.IOException;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.api.jsonata4java.expressions.Expressions;
import com.api.jsonata4java.expressions.ParseException;
import com.api.jsonata4java.expressions.functions.DeclaredFunction;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprContext;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.ExprListContext;

/**
 * Class mapping a variable name to a variable or function declaration
 * expression
 */
public class Binding {
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
         throw new ParseException("Can not transform expression to a variable assignment");
      }
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
         	_expr = (ExprContext)new MappingExpressionParser.StringContext(new ExprContext());
         	_expr.addChild((TerminalNode)tree.getChild(0));
         	_type = BindingType.VARIABLE;
         	_varname = varName;
         } else {
            _expr = (ExprContext) tree;
            _type = BindingType.VARIABLE;
            _varname = varName;
         }

      } catch (ClassCastException cce) {
         throw new ParseException("Can not transform expression to a function declaration");
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