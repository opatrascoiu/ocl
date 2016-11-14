parser grammar OCLParser;

options {
    tokenVocab = OCLLexer;
}

@header {
package org.omg.ocl.analysis.syntax.antlr;

import org.omg.ocl.analysis.syntax.*;
import org.omg.ocl.analysis.syntax.ast.*;
import org.omg.ocl.analysis.syntax.ast.constraint.*;
import org.omg.ocl.analysis.syntax.ast.declaration.*;
import org.omg.ocl.analysis.syntax.ast.expression.*;
import org.omg.ocl.analysis.syntax.ast.expression.arithmetic.*;
import org.omg.ocl.analysis.syntax.ast.expression.logical.*;
import org.omg.ocl.analysis.syntax.ast.expression.relational.*;
import org.omg.ocl.analysis.syntax.ast.expression.literal.*;
import org.omg.ocl.analysis.syntax.ast.expression.model.*;
import org.omg.ocl.analysis.semantics.type.*;

// import org.omg.ocl.analysis.syntax.parser.*;
// import org.omg.ocl.analysis.syntax.lexer.*;

import java.io.*;
}

@members {
    private ASTFactory astFactory;

    public OCLParser(TokenStream input, ASTFactory astFactory) throws Exception {
        this(input);
        this.astFactory = astFactory;
    }

    public OCLParser(String text, ASTFactory astFactory) throws Exception {
        this(new CommonTokenStream(new OCLLexer(new ANTLRInputStream(text))));
        this.astFactory = astFactory;
    }

    public ASTFactory getASTFactory() {
        return astFactory;
    }

    private org.omg.ocl.analysis.syntax.lexer.Token convertToken(org.antlr.v4.runtime.Token antlrToken) {
        if (antlrToken == null) {
            return null;
        } else {
            return new org.omg.ocl.analysis.syntax.lexer.ANTLRTokenAdapter(antlrToken);
        }
	}
}

//
// OCL program
//
//program returns [Program ast]:
//    {List<Constraint> constraints = new ArrayList<Constraint>();}
//    (first=PACKAGE pkg=pathname SEMICOLON)?
//    cons=constraint {constraints.add($cons.ast);} last=SEMICOLON
//    (cons=constraint {constraints.add($cons.ast);} last=SEMICOLON)*
//    {$ast = astFactory.toProgram(astFactory.toPosition(convertToken($first), convertToken($last)), $pkg.ast, constraints);}
//    EOF
//;
//
//// Constraints
//constraint returns [Constraint ast]:
//    first=CONTEXT context=pathname INV (name=pathname)? COLON exp=expression
//    {$ast=astFactory.toInvariant(astFactory.toPosition(convertToken($first), $exp.ast), $context.ast, $name.ast, $exp.ast);}
//;

//
// Expressions
//
expressionRoot :
    expression
    EOF
;

expression :
    impliesExp
;

impliesExp :
    xorDisjunctiveLogicalExp
    (
        IMPLIES xorDisjunctiveLogicalExp
    )?
;

xorDisjunctiveLogicalExp :
    orDisjunctiveLogicalExp
    (
        XOR orDisjunctiveLogicalExp
    )*
;

orDisjunctiveLogicalExp :
    conjunctiveLogicalExp
    (
        OR conjunctiveLogicalExp
    )*
;

conjunctiveLogicalExp :
    equalityExp
    (
        AND equalityExp
    )*
;

equalityExp :
    relationalExp
    (
        (EQUAL | NOT_EQUAL ) relationalExp
    )?
;

relationalExp :
    additiveExp
    (
        (GE | LT | LE | GE ) additiveExp
    )?
;

additiveExp :
    multiplicativeExp
    (
        (PLUS| MINUS) multiplicativeExp
    )*
;

multiplicativeExp :
    unaryExp
    (
        (STAR | FORWARD_SLASH) unaryExp
    )*
;

unaryExp :
    (NOT | MINUS)? postfixExp
;

postfixExp :
    atPreExp
    (
        dotSelectionExp
        |
        arrowSelectionExp
        |
        callExp
        |
        messageExp
    )*
;

dotSelectionExp :
    DOT SIMPLE_NAME
    (
        (
            LEFT_SQUARE_BRACKET argList RIGHT_SQUARE_BRACKET
        )
        |
        (
            LEFT_ROUND_BRACKET (argList)? RIGHT_ROUND_BRACKET
        )
    )?
;

arrowSelectionExp :
    (
        ARROW SIMPLE_NAME
        LEFT_ROUND_BRACKET
        (variableDeclaration (COMMA variableDeclaration)? BAR)? expression
        RIGHT_ROUND_BRACKET
    )
    |
    (
        ARROW ITERATE
        LEFT_ROUND_BRACKET
        (variableDeclaration SEMICOLON)? variableDeclaration BAR expression
        RIGHT_ROUND_BRACKET
    )
    |
    (
        ARROW SIMPLE_NAME
        LEFT_ROUND_BRACKET
        argList?
        RIGHT_ROUND_BRACKET
    )
;

callExp  :
    LEFT_ROUND_BRACKET
    argList?
    RIGHT_ROUND_BRACKET
;

argList :
    expression
    (
        COMMA expression
    )*
;

//
// Message expression
//
messageExp :
    UP_UP SIMPLE_NAME LEFT_ROUND_BRACKET messageArguments? RIGHT_ROUND_BRACKET
    |
    UP SIMPLE_NAME LEFT_ROUND_BRACKET messageArguments? RIGHT_ROUND_BRACKET
;

messageArguments :
    messageArgument (COMMA messageArgument)*
;

messageArgument :
    QUESTION_MARK (COLON type)?
    |
    expression
;

atPreExp :
    (AT PRE)? letExp
;

letExp :
    LET variableDeclaration (COMMA variableDeclaration)* IN expression
    |
    primaryExp
;

primaryExp :
    pathName
    |
    literalExp
    |
    LEFT_ROUND_BRACKET expression RIGHT_ROUND_BRACKET
    |
    ifExp
;

ifExp :
    IF expression THEN expression ELSE expression ENDIF
;

//
// Literal Exp
//
literalExp :
    enumLiteralExp
    |
    collectionLiteralExp
    |
    tupleLiteralExp
    |
    primitiveLiteralExp
    |
    typeLiteralExp
;

enumLiteralExp :
    pathName COLON_COLON SIMPLE_NAME
;

typeLiteralExp :
    type
;

//
// Primitive Literal Exp
//
primitiveLiteralExp :
    integerLiteralExp
    |
    realLiteralExp
    |
    stringLiteralExp
    |
    booleanLiteralExp
    |
    unlimitedNaturalLiteralExp
    |
    nullLiteralExp
    |
    invalidLiteralExp
;

integerLiteralExp :
    INTEGER_LITERAL
;

realLiteralExp :
    REAL_LITERAL
;

stringLiteralExp :
    STRING_LITERAL
;

booleanLiteralExp :
    BOOLEAN_LITERAL
;

unlimitedNaturalLiteralExp :
    MINUS INTEGER_LITERAL
    |
    STAR
;

nullLiteralExp :
    NULL
;

invalidLiteralExp :
    INVALID
;

//
// Collection Literal Exp
//
collectionLiteralExp :
    collectionTypeIdentifier LEFT_CURLY_BRACKET collectionLiteralPartList? RIGHT_CURLY_BRACKET
;

collectionLiteralPartList :
    collectionLiteralPart ( COMMA collectionLiteralPartList )?
;

collectionTypeIdentifier :
    COLLECTION | SET | ORDERED_SET | BAG | SEQUENCE
;

collectionLiteralPart :
    expression ( RANGE expression )?
;

//
// Tuple Literal Exp
//
tupleLiteralExp :
    TUPLE LEFT_CURLY_BRACKET (variableDeclarationList)? RIGHT_CURLY_BRACKET
;

variableDeclarationList :
    variableDeclaration ( COMMA variableDeclaration )*
;

variableDeclaration :
    SIMPLE_NAME (COLON type)? (EQUAL expression)?
;

//
// Pathname
//
pathName :
    SIMPLE_NAME (COLON_COLON unreservedSimpleName)*
;

unreservedSimpleName :
    SIMPLE_NAME | restrictedKeyword
;

restrictedKeyword :
    collectionTypeIdentifier
    |
    primitiveType
    |
    oclType
    |
    TUPLE
;

//
// Types
//
type :
    pathName
    |
    collectionType
    |
    tupleType
    |
    primitiveType
    |
    oclType
;

collectionType :
    collectionTypeIdentifier LEFT_ROUND_BRACKET type RIGHT_ROUND_BRACKET
;

tupleType :
    TUPLE LEFT_ROUND_BRACKET variableDeclarationList RIGHT_ROUND_BRACKET
;

primitiveType returns [PositionableType ast]:
    INTEGER_TYPE
    |
    REAL_TYPE
    |
    BOOLEAN_TYPE
    |
    STRING_TYPE
    |
    UNLIMITED_NATURAL
;

oclType :
    OCL_ANY
    |
    OCL_INVALID
    |
    OCL_MESSAGE
    |
    OCL_VOID
;
