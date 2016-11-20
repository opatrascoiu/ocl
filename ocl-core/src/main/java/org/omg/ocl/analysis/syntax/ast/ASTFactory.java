package org.omg.ocl.analysis.syntax.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.omg.ocl.analysis.semantics.type.*;
import org.omg.ocl.analysis.syntax.Position;
import org.omg.ocl.analysis.syntax.Positionable;
import org.omg.ocl.analysis.syntax.ast.constraint.Constraint;
import org.omg.ocl.analysis.syntax.ast.declaration.PositionableType;
import org.omg.ocl.analysis.syntax.ast.declaration.VariableDeclaration;
import org.omg.ocl.analysis.syntax.ast.expression.IfExp;
import org.omg.ocl.analysis.syntax.ast.expression.OCLExpression;
import org.omg.ocl.analysis.syntax.ast.expression.OCLLetExpression;
import org.omg.ocl.analysis.syntax.ast.expression.PathnameExp;
import org.omg.ocl.analysis.syntax.ast.expression.arithmetic.*;
import org.omg.ocl.analysis.syntax.ast.expression.literal.*;
import org.omg.ocl.analysis.syntax.ast.expression.logical.*;
import org.omg.ocl.analysis.syntax.ast.expression.message.OCLExpressionMessageArgument;
import org.omg.ocl.analysis.syntax.ast.expression.message.OCLMessageArgument;
import org.omg.ocl.analysis.syntax.ast.expression.message.OCLMessageExp;
import org.omg.ocl.analysis.syntax.ast.expression.message.OCLTypeMessageArgument;
import org.omg.ocl.analysis.syntax.ast.expression.model.*;
import org.omg.ocl.analysis.syntax.ast.expression.relational.*;

import java.util.Arrays;
import java.util.List;

import static org.omg.ocl.analysis.syntax.antlr.OCLLexer.*;

/**
 * Created by Tavi on 12-Oct-16.
 */
public class ASTFactory {
    public Program toProgram(Position position, Pathname pathname, List<Constraint> constraints) {
        throw new RuntimeException("Not supported yet");
    }

    public Constraint toInvariant(Position position, Pathname context, Pathname invariantName, OCLExpression expression) {
        throw new RuntimeException("Not supported yet");
    }

    public OCLExpression toImpliesExp(Position position, OCLExpression left, OCLExpression right) {
        return new ImpliesExp(position, left, right);
    }

    public OCLExpression toLogicalExp(Position position, Token operator, OCLExpression left, OCLExpression right) {
        if (operator.getType() == AND) {
            return new AndExp(position, left, right);
        } else if (operator.getType() == OR) {
            return new OrExp(position, left, right);
        } else if (operator.getType() == XOR) {
            return new XorExp(position, left, right);
        } else {
            throw new IllegalArgumentException(String.format("Incorrect logical operator '%s'", operator.getText()));
        }
    }

    public OCLExpression toEqualityExp(Position position, Token operator, OCLExpression left, OCLExpression right) {
        if (operator.getType() == EQUAL) {
            return new EqualExp(position, left, right);
        } else if (operator.getType() == NOT_EQUAL) {
            return new NotEqualExp(position, left, right);
        } else {
            throw new IllegalArgumentException(String.format("Incorrect equality operator '%s'", operator.getText()));
        }
    }

    public OCLExpression toRelationalExp(Position position, Token operator, OCLExpression left, OCLExpression right) {
        if (operator.getType() == LT) {
            return new LTExp(position, left, right);
        } else if (operator.getType() == LE) {
            return new LEExp(position, left, right);
        } else if (operator.getType() == GT) {
            return new GTExp(position, left, right);
        } else if (operator.getType() == GE) {
            return new GEExp(position, left, right);
        } else {
            throw new IllegalArgumentException(String.format("Incorrect relational operator '%s'", operator.getText()));
        }
    }

    public OCLExpression toIfExp(Position position, OCLExpression condition, OCLExpression left, OCLExpression right) {
        return new IfExp(position, condition, left, right);
    }

    public OCLExpression toAdditiveExp(Position position, Token operator, OCLExpression left, OCLExpression right) {
        if (operator.getType() == PLUS) {
            return new PlusExp(position, left, right);
        } else if (operator.getType() == MINUS) {
            return new MinusExp(position, left, right);
        } else {
            throw new IllegalArgumentException(String.format("Incorrect additive operator '%s'", operator.getText()));
        }
    }

    public OCLExpression toMultiplicativeExp(Position position, Token operator, OCLExpression left, OCLExpression right) {
        if (operator.getType() == STAR) {
            return new MultiplyExp(position, left, right);
        } else if (operator.getType() == FORWARD_SLASH) {
            return new DivideExp(position, left, right);
        } else {
            throw new IllegalArgumentException(String.format("Incorrect multiplicative operator '%s'", operator.getText()));
        }
    }

    public OCLExpression toUnaryExp(Position position, Token operator, OCLExpression operand) {
        if (operator == null) {
            return operand;
        }
        if (operator.getType() == NOT) {
            return new NotExp(position, operand);
        } else if (operator.getType() == MINUS) {
            return new UnaryMinusExp(position, operand);
        } else {
            throw new IllegalArgumentException(String.format("Incorrect unary operator '%s'", operator.getText()));
        }
    }

    public OCLExpression toPathnameExp(Position position, Pathname pathname) {
        return new PathnameExp(position, pathname);
    }

    public OCLExpression toPathnameExp(Position position, String name) {
        return new PathnameExp(position, toPathname(position, Arrays.asList(name)));
    }

    public OCLExpression toModelPropertyNavigationExp(Position position, OCLExpression source, String name) {
        return new ModelPropertyNavigationExp(position, source, name);
    }

    public OCLExpression toQualifiedModelPropertyNavigation(Position position, OCLExpression source, String name, List<OCLExpression> arguments) {
        return new QualifiedModelPropertyNavigationExp(position, source, name, arguments);
    }

    public OCLExpression toModelOperationCallExp(Position position, OCLExpression source, String name, List<OCLExpression> arguments) {
        return new ModelOperationCallExp(position, source, name, arguments);
    }

    public OCLExpression toCollectionIteratorExp(Position position, OCLExpression source, String name, VariableDeclaration iterator1, VariableDeclaration iterator2, OCLExpression body) {
        return new CollectionIteratorExp(position, source, name, iterator1, iterator2, body);
    }

    public OCLExpression toCollectionIterateExp(Position position, OCLExpression source, String name, VariableDeclaration iterator1, VariableDeclaration iterator2, OCLExpression body) {
        return new CollectionIterateExp(position, source, name, iterator1, iterator2, body);
    }

    public OCLExpression toCollectionOperationCallExp(Position position, OCLExpression source, String name, List<OCLExpression> args) {
        return new CollectionOperationCallExp(position, source, name, args);
    }

    public CollectionLiteralExp toCollectionLiteralExp(Position position, Token token, List<CollectionLiteralPart> parts) {
        return new CollectionLiteralExp(position, CollectionKind.find(token), parts);
    }

    public Pathname toPathname(Position position, List<String> simpleNames) {
        return new Pathname(position, simpleNames);
    }

    public CollectionLiteralPart toCollectionLiteralPart(Position position, OCLExpression first, OCLExpression last) {
        if (last == null) {
            return new CollectionItem(position, first);
        } else {
            return new CollectionRange(position, first, last);
        }
    }

    public TupleLiteralExp toTupleLiteralExp(Position position, List<VariableDeclaration> decls) {
        return new TupleLiteralExp(position, decls);
    }

    public VariableDeclaration toVariableDeclaration(Position position, String name, PositionableType type, Positionable init) {
        return new VariableDeclaration(position, name, type == null ? null : type.getType(), (OCLExpression) init);
    }

    public IntegerLiteralExp toIntegerLiteralExp(Position position, String text) {
        return new IntegerLiteralExp(position, Integer.parseInt(text));
    }

    public RealLiteralExp toRealLiteralExp(Position position, String text) {
        return new RealLiteralExp(position, Double.parseDouble(text));
    }

    public StringLiteralExp toStringLiteralExp(Position position, String text) {
        return new StringLiteralExp(position, text);
    }

    public BooleanLiteralExp toBooleanLiteralExp(Position position, String text) {
        return new BooleanLiteralExp(position, Boolean.parseBoolean(text));
    }

    public UnlimitedNaturalLiteralExp toUnlimitedNaturalLiteralExp(Position position, String text) {
        return new UnlimitedNaturalLiteralExp(position, text);
    }

    public NullLiteralExp toNullLiteralExp(Position position, String text) {
        return new NullLiteralExp(position, text);
    }

    public InvalidLiteralExp toInvalidLiteralExp(Position position, String text) {
        return new InvalidLiteralExp(position, text);
    }

    public Type toIntegerType() {
        return IntegerType.INTEGER_TYPE;
    }

    public Type toRealType() {
        return RealType.REAL_TYPE;
    }

    public Type toBooleanType() {
        return BooleanType.BOOLEAN_TYPE;
    }

    public Type toStringType() {
        return StringType.STRING_TYPE;
    }

    public Type toUnlimitedNaturalType() {
        throw new RuntimeException("Not supported yet");
    }

    public Type toTupleType(List<VariableDeclaration> decls) {
        return new TupleType(decls);
    }

    public Type toCollectionType(Token kind, PositionableType elementType) {
        if (elementType == null) {
            throw new RuntimeException("Incorrect element type");
        }
        CollectionKind enumerator = CollectionKind.find(kind);
        return this.toCollectionType(enumerator, elementType.getType());
    }

    public Type toCollectionType(CollectionKind kind, Type elementType) {
        if (CollectionKind.Collection.equals(kind)) {
            return new CollectionType(elementType);
        } else if (CollectionKind.Set.equals(kind)) {
            return new SetType(elementType);
        } else if (CollectionKind.OrderedSet.equals(kind)) {
            return new OrderedSetType(elementType);
        } else if (CollectionKind.Bag.equals(kind)) {
            return new BagType(elementType);
        } else if (CollectionKind.Sequence.equals(kind)) {
            return new SequenceType(elementType);
        } else {
            throw new RuntimeException("Incorrect collection kind");
        }
    }

    public Type toModelType(Pathname pathname) {
        throw new RuntimeException("Not supported yet");
    }

    public Position toPosition(ParserRuleContext localctx) {
        org.antlr.v4.runtime.Token start = localctx.start;
        org.antlr.v4.runtime.Token end = localctx.start;

        String sourceName = start.getTokenSource().getSourceName();
        int beginLine = start.getLine();
        int beginColumn = start.getCharPositionInLine();
        int beginOffset = start.getStartIndex();
        int endLine = end.getLine();
        int endColumn = start.getCharPositionInLine();
        int endOffset = end.getStopIndex();

        return new Position(sourceName, beginLine, beginColumn, beginOffset, endLine, endColumn, endOffset);
    }

    public OCLExpression toMessageExp(Position position, OCLExpression source, String name, List<OCLMessageArgument> arguments) {
        return new OCLMessageExp(position, source, name, arguments);
    }

    public OCLMessageArgument toOCLMessageArgument(Position position, OCLExpression expression) {
        return new OCLExpressionMessageArgument(position, expression);
    }

    public OCLMessageArgument toOCLMessageArgument(Position position, PositionableType type) {
        return new OCLTypeMessageArgument(position, type.getType());
    }

    public OCLExpression toAtPreExp(Position position, OCLExpression expression, Boolean isAtPre) {
        if (isAtPre) {
            throw new RuntimeException("@pre not supporte yet");
        }
        return expression;
    }

    public OCLExpression toLetExpression(Position position, List<VariableDeclaration> variableDeclarations, OCLExpression body) {
        return new OCLLetExpression(position, variableDeclarations, body);
    }

    public PositionableType toPositionableType(Position position, Type type) {
        return new PositionableType(position, type);
    }
}
