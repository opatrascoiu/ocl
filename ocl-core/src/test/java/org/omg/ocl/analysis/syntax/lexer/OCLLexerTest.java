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
package org.omg.ocl.analysis.syntax.lexer;

import static org.junit.Assert.*;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.junit.Test;

import org.omg.ocl.analysis.syntax.antlr.OCLLexer;
import org.omg.ocl.analysis.syntax.ast.ASTFactory;

import static org.omg.ocl.analysis.syntax.antlr.OCLLexer.*;

/**
 * Created by Octavian Patrascoiu on 15-Oct-16.
 */
public class OCLLexerTest {
    protected ASTFactory astFactory = new ASTFactory();

    //
    // Main tokens
    //
    @Test
    public void testSimpleName() {
        Token token = checkToken("abcd", SIMPLE_NAME, "abcd");
        checkPosition(token, 1, 0, 3);

        checkToken("A_$aαρετη123a", SIMPLE_NAME, "A_$aαρετη123a");
    }

    @Test
    public void testInteger() {
        Token token = checkToken("1234", INTEGER_LITERAL, "1234");
        checkPosition(token, 1, 0, 3);
    }

    @Test
    public void testReal() {
        Token token = checkToken("1234.56", REAL_LITERAL, "1234.56");
        checkPosition(token, 1, 0, 6);

        checkToken("1234.56", REAL_LITERAL, "1234.56");
        checkToken("1234.56e+23", REAL_LITERAL, "1234.56e+23");
        checkToken(".56", REAL_LITERAL, ".56");
        checkToken(".56e-23", REAL_LITERAL, ".56e-23");
        checkToken("1234e-23", REAL_LITERAL, "1234e-23");
    }

    @Test
    public void testString() {
        Token token = checkToken("'abcd'", STRING_LITERAL, "'abcd'");
        checkPosition(token, 1, 0, 5);

        checkToken("''", STRING_LITERAL, "''");
        checkToken("'\\b\\t\\n\\f\\r\\\"\\'\\\\'", STRING_LITERAL, "'\\b\\t\\n\\f\\r\\\"\\'\\\\'");
        checkToken("'\\u0020'", STRING_LITERAL, "'\\u0020'");
    }

    @Test
    public void testKeywords() {
        checkToken("package", PACKAGE, "package");
        checkToken("context", CONTEXT, "context");
        checkToken("inv", INV, "inv");
        checkToken("def", DEF, "def");
        checkToken("init", INIT, "init");
        checkToken("derive", DERIVE, "derive");
        checkToken("post", POST, "post");
        checkToken("body", BODY, "body");
        checkToken("not", NOT, "not");
        checkToken("and", AND, "and");
        checkToken("or", OR, "or");
        checkToken("xor", XOR, "xor");
        checkToken("if", IF, "if");
        checkToken("then", THEN, "then");
        checkToken("else", ELSE, "else");
        checkToken("endif", ENDIF, "endif");
        checkToken("implies", IMPLIES, "implies");
        checkToken("Collection", COLLECTION, "Collection");
        checkToken("Set", SET, "Set");
        checkToken("OrderedSet", ORDERED_SET, "OrderedSet");
        checkToken("Bag", BAG, "Bag");
        checkToken("Sequence", SEQUENCE, "Sequence");
        checkToken("Tuple", TUPLE, "Tuple");
        checkToken("Integer", INTEGER_TYPE, "Integer");
        checkToken("Real", REAL_TYPE, "Real");
        checkToken("String", STRING_TYPE, "String");
        checkToken("Boolean", BOOLEAN_TYPE, "Boolean");
    }

    @Test
    public void testOperators() {
        checkToken("->", ARROW, "->");
        checkToken("-", MINUS, "-");
        checkToken("*", STAR, "*");
        checkToken("/", FORWARD_SLASH, "/");
        checkToken("+", PLUS, "+");
        checkToken("<=", LE, "<=");
        checkToken(">=", GE, ">=");
        checkToken("<", LT, "<");
        checkToken(">", GT, ">");
        checkToken("=", EQUAL, "=");
        checkToken("<>", NOT_EQUAL, "<>");
        checkToken("|", BAR, "|");
    }

    @Test
    public void testPunctuation() {
        checkToken("(", LEFT_ROUND_BRACKET, "(");
        checkToken(")", RIGHT_ROUND_BRACKET, ")");
        checkToken("[", LEFT_SQUARE_BRACKET, "[");
        checkToken("]", RIGHT_SQUARE_BRACKET, "]");
        checkToken("{", LEFT_CURLY_BRACKET, "{");
        checkToken("}", RIGHT_CURLY_BRACKET, "}");
        checkToken(":", COLON, ":");
        checkToken(",", COMMA, ",");
        checkToken(";", SEMICOLON, ";");
        checkToken("::", ENUM_SEPARATOR, "::");
        checkToken(".", DOT, ".");
        checkToken("..", RANGE, "..");
    }

    private Token checkToken(String inputTape, int expectedCode, String expectedLexeme) {
        OCLLexer lexer = makeLexer(inputTape);
        Token token = lexer.nextToken();
        assertEquals(expectedLexeme, token.getText());
        assertEquals(expectedCode, token.getType());
        return token;
    }

    private void checkPosition(Token token, int expectedLine, int expectedStartIndex, int expectedStopIndex) {
        assertEquals(expectedLine, token.getLine());
        assertEquals(expectedStartIndex, token.getStartIndex());
        assertEquals(expectedStopIndex, token.getStopIndex());
    }

    private OCLLexer makeLexer(String text) {
        CharStream input = new ANTLRInputStream(text);
        return new OCLLexer(input);
    }

}
