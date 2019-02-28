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

/* The start rule; begin parsing here.
   operator precedence is implied by the ordering in this list */


// =======================
// = PARSER RULES
// =======================

expr:
   ID                                                     # id
 | DOLLAR (('.' expr) | (ARR_OPEN expr ARR_CLOSE))        # context_ref
 | ROOT '.' expr                                          # root_path
 | expr '.' expr                                          # path
 | expr ARR_OPEN expr ARR_CLOSE                           # array
 | ARR_OPEN exprOrSeqList? ARR_CLOSE                      # array_constructor
 | OBJ_OPEN fieldList? OBJ_CLOSE					      # object_constructor
 | VAR_ID (emptyValues | exprValues)                      # function_call
 | FUNCTIONID varList '{' exprList? '}'                   # function_decl
 | VAR_ID ASSIGN (expr | (FUNCTIONID varList '{' exprList? '}'))                   # var_assign
 | EACH '(' exprList ',' (VAR_ID | (FUNCTIONID varList '{' exprList? '}')) ')'     # each_function
 | FILTER '(' exprList ',' (VAR_ID | (FUNCTIONID varList '{' exprList? '}')) ')'   # filter_function
 | MAP '(' exprList ',' (VAR_ID | (FUNCTIONID varList '{' exprList? '}')) ')'      # map_function
 | REDUCE '(' exprList ',' (VAR_ID | (FUNCTIONID varList '{' exprList? '}')) (',' exprOrSeq)* ')'   # reduce_function
 | SIFT '(' exprList ',' (VAR_ID | (FUNCTIONID varList '{' exprList? '}')) ')'     # sift_function
 | (FUNCTIONID varList '{' exprList? '}') exprValues                               # function_exec
 | op=(TRUE|FALSE)                                        # boolean
 | op='-' expr                                            # unary_op
 | expr op=('*'|'/'|'%') expr                             # muldiv_op
 | expr op=('+'|'-') expr                                 # addsub_op
 | expr '&' expr                                          # concat_op
 | expr op=('<'|'<='|'>'|'>='|'!='|'=') expr              # comp_op
 | expr 'in' expr                                         # membership
 | expr 'and' expr                                        # logand
 | expr 'or' expr                                         # logor
 | expr '?' expr ':' expr                                 # conditional
 | expr CHAIN expr                                        # fct_chain
 | '(' expr (';' expr)* ')'                               # parens
 | VAR_ID                                                 # var_recall
 | NUMBER                                                 # number
 | STRING                                                 # string
 | NULL                                                   # null
 ;

fieldList : STRING ':' expr (',' STRING ':' expr)*;
exprList : expr (',' expr)* ;
varList : '('  (VAR_ID (',' VAR_ID)*)* ')' ;
exprValues : '(' exprList ')' ;
emptyValues : '(' ')' ;
seq : expr '..' expr ;

exprOrSeq : seq | expr ;
exprOrSeqList : exprOrSeq (',' exprOrSeq)* ;

// =======================
// = LEXER RULES
// =======================

TRUE : 'true';
FALSE : 'false';


STRING 
	: '\'' (ESC | ~['\\])* '\''
	| '"'  (ESC | ~["\\])* '"'
	;
	

AND : 'and' ;
OR : 'or' ;
IN : 'in' ;
NULL : 'null';

ARR_OPEN  : '[';
ARR_CLOSE : ']';

OBJ_OPEN  : '{';
OBJ_CLOSE : '}';

DOLLAR : '$';
ROOT : '$$' ;

NUMBER
    :   INT '.' [0-9]+ EXP? // 1.35, 1.35E-9, 0.3
    |   INT EXP             // 1e10 3e4
    |   INT                 // 3, 45
    ;
    
FUNCTIONID : 'function' ;

WS: [ \t\n]+ -> skip ;                // ignore whitespace
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

EACH : '$each' ;
SIFT : '$sift' ;
REDUCE : '$reduce' ;
FILTER : '$filter' ;
MAP : '$map' ;

VAR_ID : '$' ID ;

ID 
	: [a-zA-Z] [a-zA-Z0-9_]* 
	| BACK_QUOTE ~[`]* BACK_QUOTE;


// =======================
// = LEXER FRAGMENTS
// =======================


fragment ESC :   '\\' (["'\\/bfnrt] | UNICODE) ;
fragment UNICODE : 'u' HEX HEX HEX HEX ;
fragment HEX : [0-9a-fA-F] ;

fragment INT :   '0' | [1-9] [0-9]* ; // no leading zeros
fragment EXP :   [Ee] [+\-]? INT ;    // \- since - means "range" inside [...]

fragment SINGLE_QUOTE : '\'';
fragment DOUBLE_QUOTE : '"';
fragment BACK_QUOTE : '`';
