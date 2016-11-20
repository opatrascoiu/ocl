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
import org.omg.ocl.analysis.syntax.ast.expression.message.*;
import org.omg.ocl.analysis.syntax.ast.expression.model.*;
import org.omg.ocl.analysis.semantics.type.*;

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
}

//
// OCL program
//
//program returns [Program ast]:
//    {List<Constraint> constraints = new ArrayList<Constraint>();}
//    (PACKAGE pkg=pathname SEMICOLON)?
//    cons=constraint {constraints.add($cons.ast);} SEMICOLON
//    (cons=constraint {constraints.add($cons.ast);} SEMICOLON)*
//    {$ast = astFactory.toProgram(astFactory.toPosition($ctx), $pkg.ast, constraints);}
//    EOF
//;
//
//// Constraints
//constraint returns [Constraint ast]:
//    CONTEXT context=pathname INV (name=pathname)? COLON exp=expression
//    {$ast=astFactory.toInvariant(astFactory.toPosition($ctx), $context.ast, $name.ast, $exp.ast);}
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
    exp1=xorDisjunctiveLogicalExp
    {$ast = $exp1.ast;}
    (
        IMPLIES exp2=xorDisjunctiveLogicalExp
        {$ast = astFactory.toImpliesExp(astFactory.toPosition($ctx), $exp1.ast, $exp2.ast);}
    )?
;

xorDisjunctiveLogicalExp returns [OCLExpression ast] :
    exp1=orDisjunctiveLogicalExp
    {$ast = $exp1.ast;}
    (
        op=XOR exp2=orDisjunctiveLogicalExp
        {$ast = astFactory.toLogicalExp(astFactory.toPosition($ctx), $op, $ast, $exp2.ast);}
    )*
;

orDisjunctiveLogicalExp returns [OCLExpression ast] :
    exp1=conjunctiveLogicalExp
    {$ast = $exp1.ast;}
    (
        op=OR exp2=conjunctiveLogicalExp
        {$ast = astFactory.toLogicalExp(astFactory.toPosition($ctx), $op, $ast, $exp2.ast);}
    )*
;

conjunctiveLogicalExp returns [OCLExpression ast] :
    exp1=equalityExp
    {$ast = $exp1.ast;}
    (
        op=AND exp2=equalityExp
        {$ast = astFactory.toLogicalExp(astFactory.toPosition($ctx), $op, $ast, $exp2.ast);}
    )*
;

equalityExp returns [OCLExpression ast] :
    exp1=relationalExp
    {$ast = $exp1.ast;}
    (
        (op=EQUAL | op=NOT_EQUAL ) exp2=relationalExp
        {$ast = astFactory.toEqualityExp(astFactory.toPosition($ctx), $op, $ast, $exp2.ast);}
    )?
;

relationalExp returns [OCLExpression ast] :
    exp1=additiveExp
    {$ast = $exp1.ast;}
    (
        (op=LT | op=LE | op=GT | op=GE ) exp2=additiveExp
        {$ast = astFactory.toRelationalExp(astFactory.toPosition($ctx), $op, $ast, $exp2.ast);}
    )?
;

additiveExp returns [OCLExpression ast] :
    exp1=multiplicativeExp
    {$ast = $exp1.ast;}
    (
        (op=PLUS| op=MINUS) exp2=multiplicativeExp
        {$ast = astFactory.toAdditiveExp(astFactory.toPosition($ctx), $op, $ast, $exp2.ast);}
    )*
;

multiplicativeExp returns [OCLExpression ast] :
    exp1=unaryExp
    {$ast = $exp1.ast;}
    (
        (op=STAR | op=FORWARD_SLASH) exp2=unaryExp
        {$ast = astFactory.toMultiplicativeExp(astFactory.toPosition($ctx), $op, $ast, $exp2.ast);}
    )*
;

unaryExp returns [OCLExpression ast] :
    (
        (op=NOT | op=MINUS) exp2=unaryExp
        {$ast = astFactory.toUnaryExp(astFactory.toPosition($ctx), $op, $exp2.ast);}
    )
    |
    (
        postfixExp
        {$ast = $postfixExp.ast;}
    )
;

postfixExp returns [OCLExpression ast] :
    atPreExp
    {$ast = $atPreExp.ast;}
    (
        (
            dotSelectionExp[$ctx, $ast]
            {$ast = $dotSelectionExp.ast;}
        )
        |
        (
            arrowSelectionExp[$ctx, $ast]
            {$ast = $arrowSelectionExp.ast;}
        )
        |
        (
            messageExp[$ctx, $ast]
            {$ast = $messageExp.ast;}
        )
    )*
;

dotSelectionExp[ParserRuleContext ctx, OCLExpression source] returns [OCLExpression ast] :
    DOT name=SIMPLE_NAME
    {$ast = astFactory.toModelPropertyNavigationExp(astFactory.toPosition(ctx), source, $name.getText());}
    (
        (
            LEFT_SQUARE_BRACKET args1=argList RIGHT_SQUARE_BRACKET
            {$ast = astFactory.toQualifiedModelPropertyNavigation(astFactory.toPosition(ctx), source, $name.getText(), $args1.ast);}
        )
        |
        (
            LEFT_ROUND_BRACKET (args2=argList)? RIGHT_ROUND_BRACKET
            {$ast = astFactory.toModelOperationCallExp(astFactory.toPosition(ctx), source, $name.getText(), $args2.ast);}
        )
    )?
;

arrowSelectionExp[ParserRuleContext ctx, OCLExpression source] returns [OCLExpression ast] :
    (
        ARROW name=SIMPLE_NAME
        LEFT_ROUND_BRACKET
        (iter1=variableDeclaration (COMMA iter2=variableDeclaration)? BAR)? body=expression
        RIGHT_ROUND_BRACKET
        {$ast = astFactory.toCollectionIteratorExp(astFactory.toPosition(ctx), source, $name.getText(), $iter1.ast, $iter2.ast, $body.ast);}
    )
    |
    (
        ARROW iterate=SIMPLE_NAME {"iterate".equals($iterate.getText())}?
        LEFT_ROUND_BRACKET
        (iter1=variableDeclaration SEMICOLON)? iter2=variableDeclaration BAR body=expression
        RIGHT_ROUND_BRACKET
        {$ast = astFactory.toCollectionIterateExp(astFactory.toPosition(ctx), source, $iterate.getText(), $iter1.ast, $iter2.ast, $body.ast);}
    )
    |
    (
        ARROW name=SIMPLE_NAME
        LEFT_ROUND_BRACKET
        arguments=argList?
        RIGHT_ROUND_BRACKET
        {$ast = astFactory.toCollectionOperationCallExp(astFactory.toPosition(ctx), source, $name.getText(), $arguments.ast);}
    )
;

argList returns [List<OCLExpression> ast] :
    {$ast = new ArrayList<>();}
    exp1=expression
    {$ast.add($exp1.ast);}
    (
        COMMA exp2=expression
        {$ast.add($exp2.ast);}
    )*
;

//
// Message expression
//
messageExp[ParserRuleContext ctx, OCLExpression source] returns [OCLExpression ast] :
    (
        UP_UP name=SIMPLE_NAME LEFT_ROUND_BRACKET messageArguments? RIGHT_ROUND_BRACKET
        {$ast = astFactory.toMessageExp(astFactory.toPosition(ctx), source, $name.getText(), $messageArguments.ast);}
    )
    |
    (
        UP name=SIMPLE_NAME LEFT_ROUND_BRACKET messageArguments? RIGHT_ROUND_BRACKET
        {$ast = astFactory.toMessageExp(astFactory.toPosition(ctx), source, $name.getText(), $messageArguments.ast);}
    )
;

messageArguments returns [List<OCLMessageArgument> ast] :
    {$ast = new ArrayList();}
    arg1=messageArgument
    {$ast.add($arg1.ast);}
    (
        COMMA
        arg2=messageArgument
        {$ast.add($arg2.ast);}
    )*
;

messageArgument returns [OCLMessageArgument ast] :
    (
        QUESTION_MARK (COLON type)?
        {$ast = astFactory.toOCLMessageArgument(astFactory.toPosition($ctx), $type.ast);}
    )
    |
    (
        expression
        {$ast = astFactory.toOCLMessageArgument(astFactory.toPosition($ctx), $expression.ast);}
    )
;

atPreExp returns [OCLExpression ast] :
    letExp isAtPre
    {$ast = astFactory.toAtPreExp(astFactory.toPosition($ctx), $letExp.ast, $isAtPre.ast);}
;

isAtPre returns [Boolean ast] :
    {$ast = false;} (AT PRE {$ast = true;})?
;

letExp returns [OCLExpression ast] :
    (
        {List<VariableDeclaration> variableDeclarations = new ArrayList();}
        LET var1=variableDeclaration
        {variableDeclarations.add($var1.ast);}
        (
            COMMA var2=variableDeclaration
            {variableDeclarations.add($var2.ast);}
        )*
        IN body=expression
        {$ast = astFactory.toLetExpression(astFactory.toPosition($ctx), variableDeclarations, $body.ast);}
    )
    |
    (
        primaryExp
        {$ast = $primaryExp.ast;}
    )
;

primaryExp returns [OCLExpression ast] :
    (
        name=SELF
        {$ast = astFactory.toPathnameExp(astFactory.toPosition($ctx), $name.getText());}
    )
    |
    (
        pathName
        {$ast = astFactory.toPathnameExp(astFactory.toPosition($ctx), $pathName.ast);}
    )
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
    (
        ifExp
        {$ast = $ifExp.ast;}
    )
;

ifExp returns [OCLExpression ast] :
    IF expression THEN expression ELSE expression ENDIF
;

//
// Literal Exp
//
literalExp returns [OCLExpression ast] :
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
    {$ast = astFactory.toIntegerLiteralExp(astFactory.toPosition($ctx), $literal.getText());}
;

realLiteralExp returns [RealLiteralExp ast]:
    literal = REAL_LITERAL
    {$ast = astFactory.toRealLiteralExp(astFactory.toPosition($ctx), $literal.getText());}
;

stringLiteralExp returns [StringLiteralExp ast]:
    literal = STRING_LITERAL
    {$ast = astFactory.toStringLiteralExp(astFactory.toPosition($ctx), $literal.getText());}
;

booleanLiteralExp returns [OCLExpression ast] :
    literal = BOOLEAN_LITERAL
    {$ast = astFactory.toBooleanLiteralExp(astFactory.toPosition($ctx), $literal.getText());}
;

unlimitedNaturalLiteralExp returns [OCLExpression ast] :
    (
        literal=STAR
        {$ast = astFactory.toUnlimitedNaturalLiteralExp(astFactory.toPosition($ctx), $literal.text);}
    )
;

nullLiteralExp returns [OCLExpression ast] :
    literal=NULL
    {$ast = astFactory.toNullLiteralExp(astFactory.toPosition($ctx), $literal.getText());}
;

invalidLiteralExp returns [OCLExpression ast] :
    literal=INVALID
    {$ast = astFactory.toInvalidLiteralExp(astFactory.toPosition($ctx), $literal.getText());}
;

//
// Collection Literal Exp
//
collectionLiteralExp returns [OCLExpression ast] :
    {List<CollectionLiteralPart> parts = new ArrayList<CollectionLiteralPart>();}
    kind=collectionTypeIdentifier
    LEFT_CURLY_BRACKET
    (list=collectionLiteralPartList {parts.addAll($list.ast);})?
    RIGHT_CURLY_BRACKET
    {$ast = astFactory.toCollectionLiteralExp(astFactory.toPosition($ctx), $kind.ast, parts);}
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
    exp1=expression
    {$ast = astFactory.toCollectionLiteralPart(astFactory.toPosition($ctx), $exp1.ast, null);}
    (
        RANGE exp2=expression
        {$ast = astFactory.toCollectionLiteralPart(astFactory.toPosition($ctx), $exp1.ast, $exp2.ast);}
    )?
;

//
// Tuple Literal Exp
//
tupleLiteralExp returns [TupleLiteralExp ast]:
    {List<VariableDeclaration> decls = new ArrayList<VariableDeclaration>();}
    TUPLE LEFT_CURLY_BRACKET (variableDeclarationList {decls.addAll($variableDeclarationList.ast);})? RIGHT_CURLY_BRACKET
    {$ast = astFactory.toTupleLiteralExp(astFactory.toPosition($ctx), decls);}
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
    {$ast = astFactory.toVariableDeclaration(astFactory.toPosition($ctx), $name.getText(), posType, init);}
;

//
// Pathname
//
pathName returns [Pathname ast] :
    {List<String> simpleNames = new ArrayList();}
    name1=SIMPLE_NAME
    {simpleNames.add($name1.getText());}
    (
        COLON_COLON name2=unreservedSimpleName
        {simpleNames.add($name2.ast);}
    )*
    {$ast = astFactory.toPathname(astFactory.toPosition($ctx), simpleNames);}
;

unreservedSimpleName returns [String ast]:
    (
        name=SIMPLE_NAME
        {$ast = $name.getText();}
    )
    |
    (
        restrictedKeyword
        {$ast = $restrictedKeyword.ast;}
    )
;

restrictedKeyword returns [String ast]:
    (
        collectionTypeIdentifier
    )
    |
    (
        primitiveType
    )
    |
    (
        oclType
        {$ast = $oclType.ast;}
    )
    |
    (
        TUPLE
        {$ast = $TUPLE.getText();}
    )
;

//
// Types
//
type returns [PositionableType ast] :
    (
        pathName
        {$ast = astFactory.toPositionableType(astFactory.toPosition($ctx), astFactory.toModelType($pathName.ast));}
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
    kind=collectionTypeIdentifier LEFT_ROUND_BRACKET elementType=type RIGHT_ROUND_BRACKET
    {$ast = astFactory.toPositionableType(astFactory.toPosition($ctx), astFactory.toCollectionType($kind.ast, $elementType.ast));}
;

tupleType returns [PositionableType ast]:
    TUPLE LEFT_ROUND_BRACKET decls=variableDeclarationList RIGHT_ROUND_BRACKET
    {$ast = astFactory.toPositionableType(astFactory.toPosition($ctx), astFactory.toTupleType($decls.ast));}
;

primitiveType returns [PositionableType ast]:
    {Type type = null;}
    (
        (
            INTEGER
            {type = astFactory.toIntegerType();}
        )
        |
        (
            REAL
            {type = astFactory.toRealType();}
        )
        |
        (
            BOOLEAN
            {type = astFactory.toBooleanType();}
        )
        |
        (
            STRING
            {type = astFactory.toStringType();}
        )
        |
        (
            UNLIMITED_NATURAL
            {type = astFactory.toUnlimitedNaturalType();}
        )
    )
    {$ast = astFactory.toPositionableType(astFactory.toPosition($ctx), type);}
;

oclType returns [String ast]:
    (
        name=OCL_ANY
        {$ast = $name.getText();}
    )
    |
    (
        name=OCL_INVALID
        {$ast = $name.getText();}
    )
    |
    (
        name=OCL_MESSAGE
        {$ast = $name.getText();}
    )
    |
    (
        name=OCL_VOID
        {$ast = $name.getText();}
    )
;
