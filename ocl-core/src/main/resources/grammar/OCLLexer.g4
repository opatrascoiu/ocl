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
COMMENT :                       '/*' .*? '*/' -> skip ;
LINE_COMMENT :                  '//' ~[\r\n]* -> skip ;

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
ITERATE :                       'iterate' ;
PRE :                           'pre' ;
LET :                           'let' ;
IN :                            'in' ;
NULL :                          'null' ;
INVALID :                       'invalid' ;
UNLIMITED_NATURAL :             'UnlimitedNatural' ;
OCL_ANY :                       'OclAny' ;
OCL_INVALID :                   'OclInvalid' ;
OCL_MESSAGE :                   'OclMessage' ;
OCL_VOID :                      'OclVoid' ;

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
SIMPLE_NAME :                   JAVA_LETTER JAVA_LETTER_OR_DIGIT* ;

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
fragment JAVA_LETTER :
    [a-zA-Z$_] // these are the "java letters" below 0x7F
	|	// covers all characters above 0x7F which are not a surrogate
		~[\u0000-\u007F\uD800-\uDBFF]
		{Character.isJavaIdentifierStart(_input.LA(-1))}?
	|	// covers UTF-16 surrogate pairs encodings for U+10000 to U+10FFFF
		[\uD800-\uDBFF] [\uDC00-\uDFFF]
		{Character.isJavaIdentifierStart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)))}?
	;

fragment JAVA_LETTER_OR_DIGIT :
    [a-zA-Z0-9$_] // these are the "java letters or digits" below 0x7F
	|	// covers all characters above 0x7F which are not a surrogate
		~[\u0000-\u007F\uD800-\uDBFF]
		{Character.isJavaIdentifierPart(_input.LA(-1))}?
	|	// covers UTF-16 surrogate pairs encodings for U+10000 to U+10FFFF
		[\uD800-\uDBFF] [\uDC00-\uDFFF]
		{Character.isJavaIdentifierPart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)))}?
	;

fragment DECIMAL_DIGIT :        [0-9] ;
fragment EXPONENT :             ('e'|'E') ('+' | '-')? [0-9]+ ;

fragment STRING_CHARACTERS :    STRING_CHARACTER+ ;
fragment STRING_CHARACTER :     ~['\\] | ESCAPE_SEQUENCE ;
fragment ESCAPE_SEQUENCE :      '\\' [btnfr"'\\] |  UNICODE_ESCAPE ;
fragment UNICODE_ESCAPE :       '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT ;
fragment HEX_DIGIT :            [0-9a-fA-F] ;

