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

/* Antlr grammar defining the mapping expression language */
 
grammar MappingExpression;

//options {
//    // Allow any char but \uFFFF (16 bit -1)
//    charVocabulary='\u0000'-'\uFFFE';
//}

/* The start rule; begin parsing here.
   operator precedence is implied by the ordering in this list */


// =======================
// = PARSER RULES
// =======================

expr_to_eof : expr EOF ;

expr:
   ID                                                     # id
 | '*'                                                    # field_values
 | DESCEND                                                # descendant
 | DOLLAR                                                 # context_ref
 | ROOT                                                   # root_path
 | ARR_OPEN exprOrSeqList? ARR_CLOSE                      # array_constructor
 | OBJ_OPEN fieldList? OBJ_CLOSE					      # object_constructor
 | expr '.' expr                                          # path
 | (('%' '.')+ expr)                                      # parent_path
 | '%'                                                    # parent_path_solitary
 | expr '^' '(' (('>'|'<')? ID (',' ('>'|'<')? ID)*)* ')' # op_orderby
 | expr ARR_OPEN ARR_CLOSE                                # to_array
 | expr ARR_OPEN expr ARR_CLOSE                           # array
 | expr OBJ_OPEN fieldList? OBJ_CLOSE                     # object
 | VAR_ID (emptyValues | exprValues)                      # function_call
 | FUNCTIONID varList '{' exprList? '}'                   # function_decl
 | VAR_ID ASSIGN (expr | (FUNCTIONID varList '{' exprList? '}'))                   # var_assign
 | (FUNCTIONID varList '{' exprList? '}') exprValues                               # function_exec
 | op=(TRUE|FALSE)                                        # boolean
 | op='-' expr                                            # unary_op
 | expr op=('*'|'/'|'%') expr                             # muldiv_op
 | expr op=('+'|'-') expr                                 # addsub_op
 | expr op='&' expr                                       # concat_op
 | expr op=('<'|'<='|'>'|'>='|'!='|'=') expr              # comp_op
 | expr 'in' expr                                         # membership
 | expr 'and' expr                                        # logand
 | expr 'or' expr                                         # logor
 | expr '?' expr (':' expr)?                              # conditional
 | expr CHAIN expr                                        # fct_chain
 | regularExpression                                      # regular_expression
 | regularExpressionCaseInsensitive                       # regular_expression_caseinsensitive
 | regularExpressionMultiline                             # regular_expression_multiline
 | '(' (expr (';' (expr)?)*)? ')'                         # parens
 | VAR_ID                                                 # var_recall
 | NUMBER                                                 # number
 | STRING                                                 # string
 | 'null'                                                 # null
 ;

fieldList : (STRING | expr) ':' expr (',' (STRING | expr) ':' expr)*;
exprList : expr (',' expr)* ;
varList : '(' (VAR_ID (',' VAR_ID)*)* ')' ;
exprValues : '(' exprList ')' ((',' exprOrSeq)* ')')?;
emptyValues : '(' ')' ;
seq : expr '..' expr ;

exprOrSeq : seq | expr ;
exprOrSeqList : exprOrSeq (',' exprOrSeq)* ;

regularExpressionCaseInsensitive : '/' regexPattern '/' 'i';
regularExpressionMultiline : '/' regexPattern '/' 'm';
regularExpression : '/' regexPattern '/';
regexPattern : (~DIV | '\\' DIV)*;

// =======================
// = LEXER RULES
// =======================

TRUE : 'true';
FALSE : 'false';


STRING
	: '\'' (ESC | ~['\\])* '\''
	| '"'  (ESC | ~["\\])* '"'
	;

NULL : 'null';

ARR_OPEN  : '[';
ARR_CLOSE : ']';

OBJ_OPEN  : '{';
OBJ_CLOSE : '}';

DOLLAR : '$';
ROOT : '$$' ;
DESCEND : '**';

NUMBER
    :   INT '.' [0-9]+ EXP? // 1.35, 1.35E-9, 0.3
    |   INT EXP             // 1e10 3e4
    |   INT                 // 3, 45
    ;

FUNCTIONID : ('function' | 'λ') ;

WS : [ \t\r\n]+ -> skip;              // ignore whitespace
// WS: [ \t\n]+ -> skip ;                // ignore whitespace
COMMENT:  '/*' .*? '*/' -> skip;      // allow comments

// Assign token names used in above grammar
CHAIN : '~>' ;
ASSIGN : ':=' ;
MUL : '*' ;
DIV : '/' ;
ADD : '+' ;
SUB : '-' ;
REM : '%' ;
EQ  : '=' ;
NOT_EQ  : '!=' ;
LT  : '<' ;
LE  : '<=' ;
GT  : '>' ;
GE  : '>=' ;
CONCAT : '&';
CIRCUMFLEX : '^';
PIPE : '|';
UNDER : '_';
AND : 'and';
OR : 'or';

VAR_ID : '$' ID ;

ID
	: [\p{L}_] [\p{L}0-9_]*
	| BACK_QUOTE ~[`]* BACK_QUOTE;


// =======================
// = LEXER FRAGMENTS
// =======================


fragment ESC :   '\\' (["'\\/bfnrt] | UNICODE) ;
fragment UNICODE : ([\u0080-\uFFFF] | 'u' HEX HEX HEX HEX) ;
fragment HEX : [0-9a-fA-F] ;

fragment INT :   '0' | [1-9] [0-9]* ; // no leading zeros
fragment EXP :   [Ee] [+\-]? INT ;    // \- since - means "range" inside [...]

fragment SINGLE_QUOTE : '\'';
fragment DOUBLE_QUOTE : '"';
fragment BACK_QUOTE : '`';
