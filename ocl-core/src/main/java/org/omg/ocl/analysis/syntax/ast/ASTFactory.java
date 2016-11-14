package org.omg.ocl.analysis.syntax.ast;

import org.omg.ocl.analysis.semantics.type.*;
import org.omg.ocl.analysis.syntax.Position;
import org.omg.ocl.analysis.syntax.Positionable;
import org.omg.ocl.analysis.syntax.ast.constraint.Constraint;
import org.omg.ocl.analysis.syntax.ast.declaration.PositionableType;
import org.omg.ocl.analysis.syntax.ast.declaration.VariableDeclaration;
import org.omg.ocl.analysis.syntax.ast.expression.OCLExpression;
import org.omg.ocl.analysis.syntax.ast.expression.arithmetic.*;
import org.omg.ocl.analysis.syntax.ast.expression.literal.*;
import org.omg.ocl.analysis.syntax.ast.expression.logical.AndExp;
import org.omg.ocl.analysis.syntax.ast.expression.logical.ImpliesExp;
import org.omg.ocl.analysis.syntax.ast.expression.logical.NotExp;
import org.omg.ocl.analysis.syntax.ast.expression.logical.OrExp;
import org.omg.ocl.analysis.syntax.ast.expression.relational.*;
import org.omg.ocl.analysis.syntax.lexer.Token;

import java.util.List;

import static org.omg.ocl.analysis.syntax.antlr.OCLLexer.*;

/**
 * Created by Tavi on 12-Oct-16.
 */
public class ASTFactory {
    public Position toPosition(Positionable positionable) {
        return positionable.getPosition();
    }

    public Position toPosition(Positionable begin, Positionable end) {
        return calculatePosition(begin, end);
    }

    public Position toPosition(Positionable name, Positionable type, Positionable init) {
        return calculatePosition(name, type, init);
    }

    private Position calculatePosition(Positionable begin, Positionable end) {
        if (begin.getPosition() == null && end.getPosition() == null) {
            throw new IllegalArgumentException("Cannot build position");
        }
        if (begin.getPosition() == null) {
            begin = end;
        }
        if (end.getPosition() == null) {
            end = begin;
        }

        Position beginPosition = begin.getPosition();
        Position endPosition = end.getPosition();
        return new Position(beginPosition.getBeginLine(), beginPosition.getBeginColumn(), beginPosition.getBeginOffset(),
                endPosition.getEndLine(), endPosition.getEndColumn(), endPosition.getEndOffset());
    }

    private Position calculatePosition(Positionable name, Positionable type, Positionable init) {
        if (name.getPosition() == null) {
            throw new RuntimeException("Name position cannot be null");
        }
        if (init != null) {
            return calculatePosition(name, init);
        } else {
            if (type != null) {
                return calculatePosition(name, type);
            } else {
                return calculatePosition(name, name);
            }
        }
    }

    public Program toProgram(Position position, Pathname pathname, List<Constraint> constraints) {
        return null;
    }

    public Constraint toInvariant(Position position, Pathname context, Pathname invariantName, OCLExpression expression) {
        return null;
    }

    public OCLExpression toImpliesExp(Position position, OCLExpression left, OCLExpression right) {
        return new ImpliesExp(position, left, right);
    }

    public OCLExpression toLogicalExp(Position position, Token operator, OCLExpression left, OCLExpression right) {
        if (operator.getKind() == AND) {
            return new AndExp(position, left, right);
        } else if (operator.getKind() == OR) {
            return new OrExp(position, left, right);
        } else if (operator.getKind() == XOR) {
            return new AndExp(position, left, right);
        } else {
            throw new IllegalArgumentException(String.format("Incorrect logical operator '%s'", operator.getLexeme()));
        }
    }

    public OCLExpression toEqualityExp(Position position, Token operator, OCLExpression left, OCLExpression right) {
        if (operator.getKind() == EQUAL) {
            return new EqualExp(position, left, right);
        } else if (operator.getKind() == NOT_EQUAL) {
            return new NotEqualExp(position, left, right);
        } else {
            throw new IllegalArgumentException(String.format("Incorrect equality operator '%s'", operator.getLexeme()));
        }
    }

    public OCLExpression toRelationalExp(Position position, Token operator, OCLExpression left, OCLExpression right) {
        if (operator.getKind() == LT) {
            return new LTExp(position, left, right);
        } else if (operator.getKind() == LE) {
            return new LEExp(position, left, right);
        } else if (operator.getKind() == GT) {
            return new GEExp(position, left, right);
        } else if (operator.getKind() == GE) {
            return new GEExp(position, left, right);
        } else {
            throw new IllegalArgumentException(String.format("Incorrect relational operator '%s'", operator.getLexeme()));
        }
    }

    public OCLExpression toIfExp(Position position, OCLExpression condition, OCLExpression left, OCLExpression right) {
        return null;
    }

    public OCLExpression toAdditiveExp(Position position, Token operator, OCLExpression left, OCLExpression right) {
        if (operator.getKind() == PLUS) {
            return new PlusExp(position, left, right);
        } else if (operator.getKind() == MINUS) {
            return new MinusExp(position, left, right);
        } else {
            throw new IllegalArgumentException(String.format("Incorrect additive operator '%s'", operator.getLexeme()));
        }
    }

    public OCLExpression toMultiplicativeExp(Position position, Token operator, OCLExpression left, OCLExpression right) {
        if (operator.getKind() == STAR) {
            return new MultiplyExp(position, left, right);
        } else if (operator.getKind() == FORWARD_SLASH) {
            return new DivideExp(position, left, right);
        } else {
            throw new IllegalArgumentException(String.format("Incorrect multiplicative operator '%s'", operator.getLexeme()));
        }
    }

    public OCLExpression toUnaryExp(Position position, Token operator, OCLExpression operand) {
        if (operator == null) {
            return operand;
        }
        if (operator.getKind() == NOT) {
            return new NotExp(position, operand);
        } else if (operator.getKind() == MINUS) {
            return new UnaryMinusExp(position, operand);
        } else {
            throw new IllegalArgumentException(String.format("Incorrect unary operator '%s'", operator.getLexeme()));
        }
    }

    public OCLExpression toPathnameExp(Pathname pathname) {
        return null;
    }

    public OCLExpression toQualifiedModelPropertyNavigation(Position position, OCLExpression source, String name, List<OCLExpression> args) {
        return null;
    }

    public OCLExpression toModelOperationCallExp(Position position, OCLExpression source, String text, List<OCLExpression> oclExpressionList) {
        return null;
    }

    public OCLExpression toModelPropertyNavigationExp(Position position, OCLExpression source, String text) {
        return null;
    }

    public OCLExpression toCollectionIteratorExp(Position position, OCLExpression source, String text, VariableDeclaration iterator, Token token, VariableDeclaration ast, OCLExpression ast1) {
        return null;
    }

    public OCLExpression toCollection0perationCallExp(Position position, OCLExpression source, String text, List<OCLExpression> ast) {
        return null;
    }

    public boolean isIterator(Token token) {
        return false;
    }

    public CollectionLiteralExp toCollectionLiteralExp(Position position, Token token, List<CollectionLiteralPart> parts) {
        return new CollectionLiteralExp(position, CollectionKind.find(token), parts);
    }

    public Pathname toPathname(Position position, List<String> simpleNames) {
        return null;
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

    public PositionableType toPositionableType(Position position, Type type) {
        return new PositionableType(position, type);
    }

    public PositionableType toPositionableType(Position position, Pathname path) {
        throw new RuntimeException("Not implemented yet;");
    }

}

class PositionableAdapter {
    private Token token;
    private Positionable element;

    public PositionableAdapter(Token token) {
        this.token = token;
    }

    public PositionableAdapter(Positionable element) {
        this.element = element;
    }

    public Position getPosition() {
        if (element != null) {
            return element.getPosition();
        } else if (token != null) {
            return new Position(token.getBeginLine(), token.getBeginColumn(), token.getBeginOffset(), token.getEndLine(), token.getEndColumn(), token.getEndOffset());
        } else {
            throw new RuntimeException("Both token and element are null;");
        }
    }
}