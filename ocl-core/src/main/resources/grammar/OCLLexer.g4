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

// White spaces
WS :
    (' ' | '\t' | '\n' | '\r' | '\f')+  -> skip ;

// Comments
// Extended grammar to support Java like comments
COMMENT :                       '/*' .*? '*/' -> skip ;
LINE_COMMENT :                  ('//' | '--') ~[\r\n]* -> skip ;

// Keywords
AND :                           'and' ;
BODY :                          'body' ;
CONTEXT :                       'context' ;
DEF :                           'def' ;
DERIVE :                        'derive' ;
ELSE :                          'else' ;
ENDIF :                         'endif' ;
ENDPACKAGE :                    'endpackage' ;
IF :                            'if' ;
IMPLIES :                       'implies' ;
IN :                            'in' ;
INIT :                          'init' ;
INV :                           'inv' ;
INVALID :                       'invalid' ;
LET :                           'let' ;
NOT :                           'not' ;
NULL :                          'null' ;
OR :                            'or' ;
PACKAGE :                       'package' ;
POST :                          'post' ;
PRE :                           'pre' ;
SELF :                          'self' ;
STATIC :                        'static' ;
THEN :                          'then' ;
XOR :                           'xor' ;

// Restricted words
BAG :                           'Bag' ;
BOOLEAN :                       'Boolean' ;
COLLECTION :                    'Collection' ;
INTEGER :                       'Integer' ;
OCL_ANY :                       'OclAny' ;
OCL_INVALID :                   'OclInvalid' ;
OCL_MESSAGE :                   'OclMessage' ;
OCL_VOID :                      'OclVoid' ;
ORDERED_SET :                   'OrderedSet' ;
REAL :                          'Real' ;
SEQUENCE :                      'Sequence' ;
SET :                           'Set' ;
STRING :                        'String' ;
TUPLE :                         'Tuple' ;
UNLIMITED_NATURAL :             'UnlimitedNatural' ;

// Literals
BOOLEAN_LITERAL :               'true' | 'false' ;

STRING_LITERAL :                '\'' STRING_CHARACTERS? '\'' (WS* '\'' STRING_CHARACTERS? '\'')*;

INTEGER_LITERAL :               DECIMAL_DIGIT+ ;
REAL_LITERAL :
    DECIMAL_DIGIT+ '.' DECIMAL_DIGIT+ EXPONENT?
    |
    DECIMAL_DIGIT+ EXPONENT
;

// Identifier
SIMPLE_NAME :
    (
        NAME_START_CHAR NAME_CHAR*
    )
    |
    (
        ('_\'' STRING_CHARACTERS? '\'') (WS* ('\'' STRING_CHARACTERS? '\''))*
    )
;

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
AT :                            '@' ;
QUESTION_MARK :                 '?' ;
UP_UP :                         '^^' ;
UP :                            '^' ;

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
COLON_COLON :                   '::' ;
DOT :                           '.';
RANGE :                         '..';

//
// Fragments
//
fragment NAME_START_CHAR :
    [a-zA-Z$_]
    |
	[\u00C0-\u00D6] | [\u00D8-\u00F6] | [\u00F8-\u02FF]
    |
	[\u0370-\u037D] | [\u037F-\u1FFF]
    |
	[\u200C-\u200D] | [\u2070-\u218F] | [\u2C00-\u2FEF]
    |
	[\u3001-\uD7FF] | [\uF900-\uFDCF] | [\uFDF0-\uFFFD]
	;

fragment NAME_CHAR :
    NAME_START_CHAR
    |
	[0-9]
	;

fragment DECIMAL_DIGIT :        [0-9] ;
fragment EXPONENT :             ('e'|'E') ('+' | '-')? [0-9]+ ;

fragment STRING_CHARACTERS :    STRING_CHARACTER+ ;
fragment STRING_CHARACTER :     ~['\\] | ESCAPE_SEQUENCE ;
fragment ESCAPE_SEQUENCE :      '\\' [btnfr"'\\] |  UNICODE_ESCAPE ;
fragment UNICODE_ESCAPE :       '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT ;
fragment HEX_DIGIT :            [0-9a-fA-F] ;

