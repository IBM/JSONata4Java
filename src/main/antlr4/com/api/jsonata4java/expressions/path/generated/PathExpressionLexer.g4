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
 
/* Grammar defining the subset of JSONata we support on the LHS of mapping expressions */

lexer grammar PathExpressionLexer;

 WS: [ \t\n]+ -> skip ;                // ignore whitespace
	
NON_BACK_QUOTED_ID
	: [a-zA-Z] [a-zA-Z0-9_]*;

BACK_QUOTED_ID
	: BACK_QUOTE -> pushMode(MODE_BACK_QUOTE);
	
	
 
ARR_OPEN  : '[';
ARR_CLOSE : ']';

PATH_DELIM : '.';


// positive integer indexes-only supported for now
NUMBER
    :   INT                 // 3, 45
    ;
    
    



mode MODE_BACK_QUOTE;

	BACK_QUOTE_CONTENT: NOT_BACK_QUOTE+;
	
	BACK_QUOTE_EXIT: BACK_QUOTE -> popMode;
	
	
	
fragment INT :   '0' | [1-9] [0-9]* ; // no leading zeros

fragment BACK_QUOTE : '`';
fragment NOT_BACK_QUOTE : ~[`];