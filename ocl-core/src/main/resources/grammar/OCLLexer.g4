/*
 Copyright 2016.
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
   http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
*/
lexer grammar OCLLexer;

@header {
package org.omg.ocl.analysis.syntax.antlr;
}

// Keywords
PACKAGE :                       'package' ;
CONTEXT :                       'context' ;
INV :                           'inv' ;
DEF :                           'def' ;
INIT :                          'init' ;
DERIVE :                        'derive' ;
POST :                          'post' ;
BODY :                          'body' ;
NOT :                           'not' ;
AND :                           'and' ;
OR :                            'or' ;
XOR :                           'xor' ;
IF :                            'if' ;
THEN :                          'then' ;
ELSE :                          'else' ;
ENDIF :                         'endif' ;
IMPLIES :                       'implies' ;
COLLECTION :                    'Collection' ;
SET :                           'Set' ;
ORDERED_SET :                   'OrderedSet' ;
BAG :                           'Bag' ;
SEQUENCE :                      'Sequence' ;
TUPLE :                         'Tuple' ;
INTEGER_TYPE :                  'Integer' ;
REAL_TYPE :                     'Real' ;
STRING_TYPE :                   'String' ;
BOOLEAN_TYPE :                  'Boolean' ;

// Literals
BOOLEAN_LITERAL :               'true' | 'false' ;

STRING_LITERAL :                '\'' STRING_CHARACTERS? '\'' ;

INTEGER_LITERAL :               DECIMAL_DIGIT+ ;
REAL_LITERAL :
    DECIMAL_DIGIT+ '.' DECIMAL_DIGIT+ EXPONENT?
    |
    DECIMAL_DIGIT+ EXPONENT
    |
    '.' DECIMAL_DIGIT+ EXPONENT?
;

// Identifier
SIMPLE_NAME :                   NAME_START_CHAR (NAME_START_CHAR | DECIMAL_DIGIT)* ;

//
// Operators
//
ARROW :                         '->' ;
MINUS :                         '-' ;
STAR :                          '*' ;
FORWARD_SLASH :                 '/' ;
PLUS :                          '+' ;
LE :                            '<=' ;
GE :                            '>=' ;
LT :                            '<' ;
GT :                            '>' ;
EQUAL :                         '=' ;
NOT_EQUAL :                     '<>' ;
BAR :                           '|' ;

//
// Delimiters
//
LEFT_ROUND_BRACKET :            '(' ;
RIGHT_ROUND_BRACKET :           ')' ;
LEFT_SQUARE_BRACKET :           '[' ;
RIGHT_SQUARE_BRACKET :          ']' ;
LEFT_CURLY_BRACKET :            '{' ;
RIGHT_CURLY_BRACKET :           '}';
COLON :                         ':' ;
COMMA :                         ',' ;
SEMICOLON :                     ';' ;
ENUM_SEPARATOR :                '::' ;
DOT :                           '.';
RANGE :                         '..';

//
// Fragments
//
fragment NAME_START_CHAR :      [A-Z] | '_' | '$' | [a-z]
                                 | [\u00C0-\u00D6] | [\u00D8-\u00F6] | [\u00F8-\u02FF]
                                 | [\u0370-\u037D] | [\u037F-\u1FFF]
                                 | [\u200C-\u200D] | [\u2070-\u218F] | [\u2C00-\u2FEF]
                                 | [\u3001-\uD7FF] | [\uF900-\uFDCF] | [\uFDF0-\uFFFD]
                                 ;

fragment DECIMAL_DIGIT :        [0-9] ;
fragment EXPONENT :             ('e'|'E') ('+' | '-')? [0-9]+ ;

fragment STRING_CHARACTERS :    STRING_CHARACTER+ ;
fragment STRING_CHARACTER :     ~['\\] | ESCAPE_SEQUENCE ;
fragment ESCAPE_SEQUENCE :      '\\' [btnfr"'\\] |  UNICODE_ESCAPE ;
fragment UNICODE_ESCAPE :       '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT ;
fragment HEX_DIGIT :            [0-9a-fA-F] ;
