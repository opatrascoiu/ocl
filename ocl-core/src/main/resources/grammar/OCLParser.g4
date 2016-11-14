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
expressionRoot returns [OCLExpression ast]:
    expression {$ast = $expression.ast;}
    EOF
;

expression returns [OCLExpression ast] :
    impliesExp {$ast = $impliesExp.ast;}
;

impliesExp returns [OCLExpression ast] :
    first=xorDisjunctiveLogicalExp
    {$ast = $first.ast;}
    (
        IMPLIES second=xorDisjunctiveLogicalExp
        {$ast = astFactory.toImpliesExp(astFactory.toPosition($ast, $second.ast), $first.ast, $second.ast);}
    )?
;

xorDisjunctiveLogicalExp returns [OCLExpression ast] :
    first=orDisjunctiveLogicalExp
    {$ast = $first.ast;}
    (
        op=XOR second=orDisjunctiveLogicalExp
        {$ast = astFactory.toLogicalExp(astFactory.toPosition($ast, $second.ast), convertToken($op), $ast, $second.ast);}
    )*
;

orDisjunctiveLogicalExp returns [OCLExpression ast] :
    first=conjunctiveLogicalExp
    {$ast = $first.ast;}
    (
        op=OR second=conjunctiveLogicalExp
        {$ast = astFactory.toLogicalExp(astFactory.toPosition($ast, $second.ast), convertToken($op), $ast, $second.ast);}
    )*
;

conjunctiveLogicalExp returns [OCLExpression ast] :
    first=equalityExp
    {$ast = $first.ast;}
    (
        op=AND second=equalityExp
        {$ast = astFactory.toLogicalExp(astFactory.toPosition($ast, $second.ast), convertToken($op), $ast, $second.ast);}
    )*
;

equalityExp returns [OCLExpression ast] :
    first=relationalExp
    {$ast = $first.ast;}
    (
        (op=EQUAL | op=NOT_EQUAL ) second=relationalExp
        {$ast = astFactory.toEqualityExp(astFactory.toPosition($ast, $second.ast), convertToken($op), $ast, $second.ast);}
    )?
;

relationalExp returns [OCLExpression ast] :
    first=additiveExp
    {$ast = $first.ast;}
    (
        (op=GE | op=LT | op=LE | op=GE ) second=additiveExp
        {$ast = astFactory.toRelationalExp(astFactory.toPosition($ast, $second.ast), convertToken($op), $ast, $second.ast);}
    )?
;

additiveExp returns [OCLExpression ast] :
    first=multiplicativeExp
    {$ast = $first.ast;}
    (
        (op=PLUS| op=MINUS) second=multiplicativeExp
        {$ast = astFactory.toAdditiveExp(astFactory.toPosition($ast, $second.ast), convertToken($op), $ast, $second.ast);}
    )*
;

multiplicativeExp returns [OCLExpression ast] :
    first=unaryExp
    {$ast = $first.ast;}
    (
        (op=STAR | op=FORWARD_SLASH) second=unaryExp
        {$ast = astFactory.toMultiplicativeExp(astFactory.toPosition($ast, $second.ast), convertToken($op), $ast, $second.ast);}
    )*
;

unaryExp returns [OCLExpression ast] :
    (op=NOT | op=MINUS)? postfixExp
    {$ast = $op==null ? $postfixExp.ast : astFactory.toUnaryExp(astFactory.toPosition(convertToken($op), $postfixExp.ast), convertToken($op), $postfixExp.ast);}
;

postfixExp returns [OCLExpression ast] :
    atPreExp
    {$ast = $atPreExp.ast;}
    (
        (
            dotSelectionExp[$ast]
            {$ast = $dotSelectionExp.ast;}
        )
        |
        (
            arrowSelectionExp[$ast]
            {$ast = $arrowSelectionExp.ast;}
        )
        |
        (
            callExp[$ast]
            {$ast = $callExp.ast;}
        )
        |
        (
            messageExp[$ast]
            {$ast = $messageExp.ast;}
        )
    )*
;

dotSelectionExp[OCLExpression source] returns [OCLExpression ast] :
    DOT name=SIMPLE_NAME
    (
        (
            LEFT_SQUARE_BRACKET args1=argList last=RIGHT_SQUARE_BRACKET
        )
        |
        (
            LEFT_ROUND_BRACKET (args2=argList)? last=RIGHT_ROUND_BRACKET
        )
    )?
;

arrowSelectionExp[OCLExpression source] returns [OCLExpression ast] :
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

callExp[OCLExpression source] returns [OCLExpression ast] :
    LEFT_ROUND_BRACKET
    argList?
    RIGHT_ROUND_BRACKET
;

argList returns [List<OCLExpression> ast] :
    expression
    (
        COMMA expression
    )*
;

//
// Message expression
//
messageExp[OCLExpression source] returns [OCLExpression ast] :
    UP_UP SIMPLE_NAME LEFT_ROUND_BRACKET messageArguments? RIGHT_ROUND_BRACKET
    |
    UP SIMPLE_NAME LEFT_ROUND_BRACKET messageArguments? RIGHT_ROUND_BRACKET
;

messageArguments returns [List<OCLExpression> ast] :
    messageArgument (COMMA messageArgument)*
;

messageArgument returns [OCLExpression ast] :
    QUESTION_MARK (COLON type)?
    |
    expression
;

atPreExp returns [OCLExpression ast] :
    (AT PRE)? letExp
    {$ast = $letExp.ast;}
;

letExp returns [OCLExpression ast] :
    LET variableDeclaration (COMMA variableDeclaration)* IN expression
    |
    (
        primaryExp
        {$ast = $primaryExp.ast;}
    )
;

primaryExp returns [OCLExpression ast] :
    pathName
    |
    (
        literalExp
        {$ast = $literalExp.ast;}
    )
    |
    (
        LEFT_ROUND_BRACKET expression RIGHT_ROUND_BRACKET
        {$ast = $expression.ast;}
    )
    |
    ifExp
;

ifExp returns [OCLExpression ast] :
    IF expression THEN expression ELSE expression ENDIF
;

//
// Literal Exp
//
literalExp returns [OCLExpression ast] :
    (
        enumLiteralExp
        {$ast = $enumLiteralExp.ast;}
    )
    |
    (
        collectionLiteralExp
        {$ast = $collectionLiteralExp.ast;}
    )
    |
    (
        tupleLiteralExp
        {$ast = $tupleLiteralExp.ast;}
    )
    |
    (
        primitiveLiteralExp
        {$ast = $primitiveLiteralExp.ast;}
    )
    |
    (
        typeLiteralExp
        {$ast = $typeLiteralExp.ast;}
    )
;

enumLiteralExp returns [OCLExpression ast] :
    pathName COLON_COLON SIMPLE_NAME
;

typeLiteralExp returns [OCLExpression ast] :
    type
;

//
// Primitive Literal Exp
//
primitiveLiteralExp returns [OCLExpression ast] :
    (
        integerLiteralExp
        {$ast = $integerLiteralExp.ast;}
    )
    |
    (
        realLiteralExp
        {$ast = $realLiteralExp.ast;}
    )
    |
    (
        stringLiteralExp
        {$ast = $stringLiteralExp.ast;}
    )
    |
    (
        booleanLiteralExp
        {$ast = $booleanLiteralExp.ast;}
    )
    |
    (
        unlimitedNaturalLiteralExp
        {$ast = $unlimitedNaturalLiteralExp.ast;}
    )
    |
    (
        nullLiteralExp
        {$ast = $nullLiteralExp.ast;}
    )
    |
    (
        invalidLiteralExp
        {$ast = $invalidLiteralExp.ast;}
    )
;

integerLiteralExp returns [IntegerLiteralExp ast]:
    literal = INTEGER_LITERAL
    {$ast = astFactory.toIntegerLiteralExp(astFactory.toPosition(convertToken($literal)), $literal.getText());}
;

realLiteralExp returns [RealLiteralExp ast]:
    literal = REAL_LITERAL
    {$ast = astFactory.toRealLiteralExp(astFactory.toPosition(convertToken($literal)), $literal.getText());}
;

stringLiteralExp returns [StringLiteralExp ast]:
    literal = STRING_LITERAL
    {$ast = astFactory.toStringLiteralExp(astFactory.toPosition(convertToken($literal)), $literal.getText());}
;

booleanLiteralExp returns [OCLExpression ast] :
    literal = BOOLEAN_LITERAL
    {$ast = astFactory.toBooleanLiteralExp(astFactory.toPosition(convertToken($literal)), $literal.getText());}
;

unlimitedNaturalLiteralExp returns [OCLExpression ast] :
    (
        literal=STAR
        {$ast = astFactory.toUnlimitedNaturalLiteralExp(astFactory.toPosition(convertToken($literal)), $literal.text);}
    )
;

nullLiteralExp returns [OCLExpression ast] :
    literal=NULL
    {$ast = astFactory.toNullLiteralExp(astFactory.toPosition(convertToken($literal)), $literal.getText());}
;

invalidLiteralExp returns [OCLExpression ast] :
    literal=INVALID
    {$ast = astFactory.toInvalidLiteralExp(astFactory.toPosition(convertToken($literal)), $literal.getText());}
;

//
// Collection Literal Exp
//
collectionLiteralExp returns [OCLExpression ast] :
    {List<CollectionLiteralPart> parts = new ArrayList<CollectionLiteralPart>();}
    kind=collectionTypeIdentifier
    LEFT_CURLY_BRACKET
    (list=collectionLiteralPartList {parts.addAll($list.ast);})?
    last=RIGHT_CURLY_BRACKET
    {$ast = astFactory.toCollectionLiteralExp(astFactory.toPosition(convertToken($kind.ast), convertToken($last)), convertToken($kind.ast), parts);}
;

collectionLiteralPartList returns [List<CollectionLiteralPart> ast] :
    {$ast = new ArrayList<CollectionLiteralPart>();}
    part=collectionLiteralPart
    {$ast.add($part.ast);}
    (
        COMMA part=collectionLiteralPart
        {$ast.add($part.ast);}
    )*
;

collectionTypeIdentifier returns [Token ast] :
    (
        left=COLLECTION
        {$ast = $left;}
    )
    |
    (
        left=SET
        {$ast = $left;}
    )
    |
    (
        left=ORDERED_SET
        {$ast = $left;}
    )
    |
    (
        left=BAG
        {$ast = $left;}
    )
    |
    (
        left=SEQUENCE
        {$ast = $left;}
    )
;

collectionLiteralPart returns [CollectionLiteralPart ast]:
    first=expression
    {$ast = astFactory.toCollectionLiteralPart(astFactory.toPosition($first.ast), $first.ast, null);}
    (
        RANGE last=expression
        {$ast = astFactory.toCollectionLiteralPart(astFactory.toPosition($first.ast, $last.ast), $first.ast, $last.ast);}
    )?
;

//
// Tuple Literal Exp
//
tupleLiteralExp returns [TupleLiteralExp ast]:
    {List<VariableDeclaration> decls = new ArrayList<VariableDeclaration>();}
    first=TUPLE LEFT_CURLY_BRACKET (variableDeclarationList {decls.addAll($variableDeclarationList.ast);})?
    last=RIGHT_CURLY_BRACKET
    {$ast = astFactory.toTupleLiteralExp(astFactory.toPosition(convertToken($first), convertToken($last)), decls);}
;

variableDeclarationList returns [List<VariableDeclaration> ast]:
    {$ast = new ArrayList<VariableDeclaration>();}
    decl1=variableDeclaration
    {$ast.add($decl1.ast);}
    (
        COMMA decl2=variableDeclaration
        {$ast.add($decl2.ast);}
    )*
;

variableDeclaration returns [VariableDeclaration ast]:
    {PositionableType posType = null;}
    {Positionable init = null;}
    name=SIMPLE_NAME (COLON type {posType = $type.ast;} )? (EQUAL expression {init = $expression.ast;} )?
    {$ast = astFactory.toVariableDeclaration(astFactory.toPosition(convertToken($name), posType, init), $name.getText(), posType, init);}
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
type returns [PositionableType ast] :
    (
        pathName
    )
    |
    (
        collectionType
        {$ast = $collectionType.ast;}
    )
    |
    (
        tupleType
        {$ast = $tupleType.ast;}
    )
    |
    (
        primitiveType
        {$ast = $primitiveType.ast;}
    )
    |
    (
        oclType
    )
;

collectionType returns [PositionableType ast] :
    kind=collectionTypeIdentifier LEFT_ROUND_BRACKET elementType=type last=RIGHT_ROUND_BRACKET
    {$ast = astFactory.toPositionableType(astFactory.toPosition(convertToken($kind.ast), convertToken($last)), astFactory.toCollectionType(convertToken($kind.ast), $elementType.ast));}
;

tupleType returns [PositionableType ast]:
    first=TUPLE LEFT_ROUND_BRACKET decls=variableDeclarationList last=RIGHT_ROUND_BRACKET
    {$ast = astFactory.toPositionableType(astFactory.toPosition(convertToken($first), convertToken($last)), astFactory.toTupleType($decls.ast));}
;

primitiveType returns [PositionableType ast]:
    {Type type = null;}
    (
        (
            first=INTEGER_TYPE
            {type = astFactory.toIntegerType();}
        )
        |
        (
            first=REAL_TYPE
            {type = astFactory.toRealType();}
        )
        |
        (
            first=BOOLEAN_TYPE
            {type = astFactory.toBooleanType();}
        )
        |
        (
            first=STRING_TYPE
            {type = astFactory.toStringType();}
        )
        |
        (
            first=UNLIMITED_NATURAL
        )
    )
    {$ast = astFactory.toPositionableType(astFactory.toPosition(convertToken($first)), type);}
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
