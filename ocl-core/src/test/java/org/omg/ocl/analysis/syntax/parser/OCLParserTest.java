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
package org.omg.ocl.analysis.syntax.parser;

import org.antlr.v4.runtime.*;
import org.junit.Test;
import org.omg.ocl.analysis.syntax.antlr.OCLLexer;
import org.omg.ocl.analysis.syntax.antlr.OCLParser;
import org.omg.ocl.analysis.syntax.ast.ASTFactory;
import org.omg.ocl.analysis.syntax.ast.expression.OCLExpression;

import java.io.StringReader;

import static org.junit.Assert.*;

/**
 * Created by Octavian Patrascoiu on 23-Oct-16.
 */
public class OCLParserTest {
    @Test
    public void testLogicalExp() {
        OCLExpression exp = parseExpression("a implies b");
        assertEquals("ImpliesExp(PathnameExp(Pathname(a)), PathnameExp(Pathname(b)))", exp.toString());

        exp = parseExpression("a xor b xor c");
        assertEquals("XorExp(XorExp(PathnameExp(Pathname(a)), PathnameExp(Pathname(b))), PathnameExp(Pathname(c)))", exp.toString());

        exp = parseExpression("a or b or c");
        assertEquals("OrExp(OrExp(PathnameExp(Pathname(a)), PathnameExp(Pathname(b))), PathnameExp(Pathname(c)))", exp.toString());

        exp = parseExpression("a and b and c");
        assertEquals("AndExp(AndExp(PathnameExp(Pathname(a)), PathnameExp(Pathname(b))), PathnameExp(Pathname(c)))", exp.toString());

        exp = parseExpression("not a");
        assertEquals("NotExp(PathnameExp(Pathname(a)), null)", exp.toString());

        exp = parseExpression("not (not a)");
        assertEquals("NotExp(NotExp(PathnameExp(Pathname(a)), null), null)", exp.toString());

        exp = parseExpression("not not a");
        assertEquals("NotExp(NotExp(PathnameExp(Pathname(a)), null), null)", exp.toString());
    }

    @Test
    public void testRelationalExp() {
        OCLExpression exp = parseExpression("1 = 2");
        assertEquals("EqualExp(IntegerLiteralExp(1), IntegerLiteralExp(2))", exp.toString());

        exp = parseExpression("1 != 2");
        assertEquals("EqualExp(IntegerLiteralExp(1), IntegerLiteralExp(2))", exp.toString());

        exp = parseExpression("1 < 2");
        assertEquals("LTExp(IntegerLiteralExp(1), IntegerLiteralExp(2))", exp.toString());

        exp = parseExpression("1 <= 2");
        assertEquals("LEExp(IntegerLiteralExp(1), IntegerLiteralExp(2))", exp.toString());

        exp = parseExpression("1 > 2");
        assertEquals("GTExp(IntegerLiteralExp(1), IntegerLiteralExp(2))", exp.toString());

        exp = parseExpression("1 >= 2");
        assertEquals("GEExp(IntegerLiteralExp(1), IntegerLiteralExp(2))", exp.toString());
    }

    @Test
    public void testPathnameExp() {
        OCLExpression exp = parseExpression("abc");
        assertEquals("PathnameExp(Pathname(abc))", exp.toString());

        exp = parseExpression("self");
        assertEquals("PathnameExp(Pathname(self))", exp.toString());

        exp = parseExpression("a::b::c");
        assertEquals("PathnameExp(Pathname(a::b::c))", exp.toString());

        exp = parseExpression("a::string::oclAny::Tuple");
        assertEquals("PathnameExp(Pathname(a::string::oclAny::Tuple))", exp.toString());
    }

    @Test
    public void testArithmeticExp() {
        OCLExpression exp = parseExpression("1 + 2 + 3");
        assertEquals("PlusExp(PlusExp(IntegerLiteralExp(1), IntegerLiteralExp(2)), IntegerLiteralExp(3))", exp.toString());

        exp = parseExpression("1 - 2 - 3");
        assertEquals("MinusExp(MinusExp(IntegerLiteralExp(1), IntegerLiteralExp(2)), IntegerLiteralExp(3))", exp.toString());

        exp = parseExpression("1 * 2 * 3");
        assertEquals("MultiplyExp(MultiplyExp(IntegerLiteralExp(1), IntegerLiteralExp(2)), IntegerLiteralExp(3))", exp.toString());

        exp = parseExpression("1 / 2 / 3");
        assertEquals("DivideExp(DivideExp(IntegerLiteralExp(1), IntegerLiteralExp(2)), IntegerLiteralExp(3))", exp.toString());
    }

    @Test
    public void testPrimitiveLiterals() {
        OCLExpression exp = parseExpression("1234");
        assertEquals("IntegerLiteralExp(1234)", exp.toString());

        exp = parseExpression("1234.56");
        assertEquals("RealLiteralExp(1234.56)", exp.toString());

        exp = parseExpression("'1234'");
        assertEquals("StringLiteralExp('1234')", exp.toString());

        exp = parseExpression("true");
        assertEquals("BooleanLiteralExp(true)", exp.toString());

        exp = parseExpression("false");
        assertEquals("BooleanLiteralExp(false)", exp.toString());

        exp = parseExpression("null");
        assertEquals("NullLiteralExp(null)", exp.toString());

        exp = parseExpression("invalid");
        assertEquals("InvalidLiteralExp(invalid)", exp.toString());

        exp = parseExpression("*");
        assertEquals("UnlimitedNaturalLiteralExp(*)", exp.toString());

        exp = parseExpression("-1");
        assertEquals("UnaryMinusExp(IntegerLiteralExp(1), null)", exp.toString());
    }

    @Test
    public void testCollectionLiterals() {
        OCLExpression exp = parseExpression("Collection{}");
        assertEquals("CollectionLiteralExp(Collection, {})", exp.toString());

        exp = parseExpression("Set{}");
        assertEquals("CollectionLiteralExp(Set, {})", exp.toString());

        exp = parseExpression("OrderedSet{}");
        assertEquals("CollectionLiteralExp(OrderedSet, {})", exp.toString());

        exp = parseExpression("Bag{}");
        assertEquals("CollectionLiteralExp(Bag, {})", exp.toString());

        exp = parseExpression("Sequence{}");
        assertEquals("CollectionLiteralExp(Sequence, {})", exp.toString());

        exp = parseExpression("Set{1}");
        assertEquals("CollectionLiteralExp(Set, {CollectionItem(IntegerLiteralExp(1))})", exp.toString());

        exp = parseExpression("Set{1+2}");
        assertEquals("CollectionLiteralExp(Set, {CollectionItem(PlusExp(IntegerLiteralExp(1), IntegerLiteralExp(2)))})", exp.toString());

        exp = parseExpression("Set{1, 2}");
        assertEquals("CollectionLiteralExp(Set, {CollectionItem(IntegerLiteralExp(1)), CollectionItem(IntegerLiteralExp(2))})", exp.toString());

        exp = parseExpression("Set{1,2,3,4..10}");
        assertEquals("CollectionLiteralExp(Set, {CollectionItem(IntegerLiteralExp(1)), CollectionItem(IntegerLiteralExp(2)), CollectionItem(IntegerLiteralExp(3)), CollectionRange(IntegerLiteralExp(4)..IntegerLiteralExp(10))})", exp.toString());
    }

    @Test
    public void testTupleLiterals() {
        OCLExpression exp = parseExpression("Tuple{}");
        assertEquals("TupleLiteralExp()", exp.toString());

        exp = parseExpression("Tuple{age:Integer=10}");
        assertEquals("TupleLiteralExp(VariableDeclaration(age, IntegerType, IntegerLiteralExp(10)))", exp.toString());

        exp = parseExpression("Tuple{age:Real=10}");
        assertEquals("TupleLiteralExp(VariableDeclaration(age, RealType, IntegerLiteralExp(10)))", exp.toString());

        exp = parseExpression("Tuple{age:Set(Integer)=Set{}}");
        assertEquals("TupleLiteralExp(VariableDeclaration(age, SetType(IntegerType), CollectionLiteralExp(Set, {})))", exp.toString());

        exp = parseExpression("Tuple{age:Bag(Real)=Bag{1.4, 1.5}}");
        assertEquals("TupleLiteralExp(VariableDeclaration(age, BagType(RealType), CollectionLiteralExp(Bag, {CollectionItem(RealLiteralExp(1.4)), CollectionItem(RealLiteralExp(1.5))})))", exp.toString());

        exp = parseExpression("Tuple{age=5}");
        assertEquals("TupleLiteralExp(VariableDeclaration(age, null, IntegerLiteralExp(5)))", exp.toString());

        exp = parseExpression("Tuple{age}");
        assertEquals("TupleLiteralExp(VariableDeclaration(age, null, null))", exp.toString());
    }

    private OCLExpression parseExpression(String text) {
        try {
            OCLParser oclParser = new OCLParser(text, new ASTFactory());
            oclParser.setErrorHandler(new DefaultErrorStrategy());
            return oclParser.expressionRoot().ast;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
