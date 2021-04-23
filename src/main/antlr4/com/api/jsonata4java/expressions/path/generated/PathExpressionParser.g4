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
 
parser grammar PathExpressionParser;

options {tokenVocab = PathExpressionLexer;}

expr
 : id array_index* ((PATH_DELIM rhs=expr)|EOF)                                      # path
 ;
 
 
 id 
 	: BACK_QUOTED_ID (txt=BACK_QUOTE_CONTENT) BACK_QUOTE_EXIT
	| txt=NON_BACK_QUOTED_ID;

array_index
	: ARR_OPEN (NUMBER) ARR_CLOSE;