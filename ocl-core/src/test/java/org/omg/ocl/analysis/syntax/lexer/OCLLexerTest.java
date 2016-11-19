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

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.junit.Test;
import org.omg.ocl.analysis.syntax.antlr.OCLLexer;
import org.omg.ocl.analysis.syntax.ast.ASTFactory;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.omg.ocl.analysis.syntax.antlr.OCLLexer.*;

/**
 * Created by Octavian Patrascoiu on 15-Oct-16.
 */
public class OCLLexerTest {
    protected ASTFactory astFactory = new ASTFactory();

    @Test
    public void testWhiteSpaces() {
        checkTokens(" \t\n\r\f123", Arrays.asList(INTEGER_LITERAL), Arrays.asList("123"));
    }

    @Test
    public void testComments() {
        checkTokens("-- 123\n1234", Arrays.asList(INTEGER_LITERAL), Arrays.asList("1234"));
        checkTokens("// 123\n1234", Arrays.asList(INTEGER_LITERAL), Arrays.asList("1234"));
        checkTokens("/* 123 */1234", Arrays.asList(INTEGER_LITERAL), Arrays.asList("1234"));
    }

    @Test
    public void testSimpleName() {
        Token token = checkToken("abcd", SIMPLE_NAME, "abcd");
        checkPosition(token, 1, 0, 3);

        checkToken("string", SIMPLE_NAME, "string");
        checkToken("A_$aαρετη123a", SIMPLE_NAME, "A_$aαρετη123a");
        checkToken("_'true'", SIMPLE_NAME, "_'true'");
        checkToken("_'>='", SIMPLE_NAME, "_'>='");
        checkToken("_'abc' \n\t' bc'", SIMPLE_NAME, "_'abc' \n\t' bc'");
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

        checkToken("1234.56e+23", REAL_LITERAL, "1234.56e+23");
        checkToken("1234.56", REAL_LITERAL, "1234.56");
        checkToken("1234e-23", REAL_LITERAL, "1234e-23");
    }

    @Test
    public void testString() {
        Token token = checkToken("'abcd'", STRING_LITERAL, "'abcd'");
        checkPosition(token, 1, 0, 5);

        checkToken("''", STRING_LITERAL, "''");
        checkToken("'\\b\\t\\n\\f\\r\\\"\\'\\\\'", STRING_LITERAL, "'\\b\\t\\n\\f\\r\\\"\\'\\\\'");
        checkToken("'\\u0020'", STRING_LITERAL, "'\\u0020'");
        checkToken("'This is a '\n \t'concatenated string'", STRING_LITERAL, "'This is a '\n \t'concatenated string'");
    }

    @Test
    public void testKeywords() {
        checkToken("and", AND, "and");
        checkToken("body", BODY, "body");
        checkToken("context", CONTEXT, "context");
        checkToken("def", DEF, "def");
        checkToken("derive", DERIVE, "derive");
        checkToken("else", ELSE, "else");
        checkToken("endif", ENDIF, "endif");
        checkToken("endpackage", ENDPACKAGE, "endpackage");
        checkToken("if", IF, "if");
        checkToken("implies", IMPLIES, "implies");
        checkToken("in", IN, "in");
        checkToken("init", INIT, "init");
        checkToken("inv", INV, "inv");
        checkToken("invalid", INVALID, "invalid");
        checkToken("let", LET, "let");
        checkToken("not", NOT, "not");
        checkToken("null", NULL, "null");
        checkToken("or", OR, "or");
        checkToken("package", PACKAGE, "package");
        checkToken("post", POST, "post");
        checkToken("pre", PRE, "pre");
        checkToken("self", SELF, "self");
        checkToken("static", STATIC, "static");
        checkToken("then", THEN, "then");
        checkToken("xor", XOR, "xor");
    }

    @Test
    public void testRestrictedWords() {
        checkToken("Bag", BAG, "Bag");
        checkToken("Boolean", BOOLEAN, "Boolean");
        checkToken("Collection", COLLECTION, "Collection");
        checkToken("Integer", INTEGER, "Integer");
        checkToken("OclAny", OCL_ANY, "OclAny");
        checkToken("OclInvalid", OCL_INVALID, "OclInvalid");
        checkToken("OclMessage", OCL_MESSAGE, "OclMessage");
        checkToken("OclVoid", OCL_VOID, "OclVoid");
        checkToken("OrderedSet", ORDERED_SET, "OrderedSet");
        checkToken("Real", REAL, "Real");
        checkToken("Sequence", SEQUENCE, "Sequence");
        checkToken("Set", SET, "Set");
        checkToken("String", STRING, "String");
        checkToken("Tuple", TUPLE, "Tuple");
        checkToken("UnlimitedNatural", UNLIMITED_NATURAL, "UnlimitedNatural");
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
        checkToken("@", AT, "@");
        checkToken("?", QUESTION_MARK, "?");
        checkToken("^^", UP_UP, "^^");
        checkToken("^", UP, "^");
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
        checkToken("::", COLON_COLON, "::");
        checkToken(".", DOT, ".");
        checkToken("..", RANGE, "..");
    }

    @Test
    public void testSequences() {
        checkTokens("Set{1}", Arrays.asList(SET, LEFT_CURLY_BRACKET, INTEGER_LITERAL, RIGHT_CURLY_BRACKET), Arrays.asList("Set", "{", "1", "}"));
    }

    private void checkTokens(String inputTape, List<Integer> expectedCodes, List<String> expectedLexemes) {
        OCLLexer lexer = makeLexer(inputTape);
        for(int i=0; i<expectedCodes.size(); i++) {
            Token token = lexer.nextToken();
            assertEquals(expectedLexemes.get(i), token.getText());
            assertEquals((long)expectedCodes.get(i), token.getType());
        }
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
