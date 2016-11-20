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
package org.omg.ocl.analysis.syntax;

import org.omg.ocl.analysis.syntax.ast.Pathname;
import org.omg.ocl.analysis.syntax.ast.Program;
import org.omg.ocl.analysis.syntax.ast.constraint.Invariant;
import org.omg.ocl.analysis.syntax.ast.declaration.PositionableType;
import org.omg.ocl.analysis.syntax.ast.declaration.VariableDeclaration;
import org.omg.ocl.analysis.syntax.ast.expression.IfExp;
import org.omg.ocl.analysis.syntax.ast.expression.OCLLetExpression;
import org.omg.ocl.analysis.syntax.ast.expression.PathnameExp;
import org.omg.ocl.analysis.syntax.ast.expression.arithmetic.*;
import org.omg.ocl.analysis.syntax.ast.expression.literal.*;
import org.omg.ocl.analysis.syntax.ast.expression.logical.*;
import org.omg.ocl.analysis.syntax.ast.expression.message.OCLMessageArgument;
import org.omg.ocl.analysis.syntax.ast.expression.message.OCLMessageExp;
import org.omg.ocl.analysis.syntax.ast.expression.model.*;
import org.omg.ocl.analysis.syntax.ast.expression.relational.*;

import java.util.Map;

/**
 * Created by Octavian Patrascoiu on 15-Oct-16.
 */
public interface Visitor {
    //
    // Top level constructs
    //
    Object visit(Program element, Map<String, Object> params);
    Object visit(Invariant element, Map<String, Object> params);
    Object visit(Pathname element, Map<String, Object> params);

    //
    // Special expressions
    //
    Object visit(IfExp element, Map<String, Object> params);
    Object visit(PathnameExp element, Map<String, Object> params);

    //
    // Logical expressions
    //
    Object visit(ImpliesExp impliesExp, Map<String, Object> params);
    Object visit(OrExp element, Map<String, Object> params);
    Object visit(XorExp xorExp, Map<String, Object> params);
    Object visit(AndExp element, Map<String, Object> params);
    Object visit(NotExp element, Map<String, Object> params);

    //
    // Relational expressions
    //
    Object visit(EqualExp element, Map<String, Object> params);
    Object visit(NotEqualExp element, Map<String, Object> params);
    Object visit(LTExp element, Map<String, Object> params);
    Object visit(LEExp element, Map<String, Object> params);
    Object visit(GTExp element, Map<String, Object> params);
    Object visit(GEExp element, Map<String, Object> params);

    //
    // Arithmetic expressions
    //
    Object visit(PlusExp element, Map<String, Object> params);
    Object visit(MinusExp element, Map<String, Object> params);
    Object visit(MultiplyExp element, Map<String, Object> params);
    Object visit(DivideExp element, Map<String, Object> params);
    Object visit(UnaryMinusExp element, Map<String, Object> params);

    //
    // Model expressions
    //
    Object visit(ModelPropertyNavigationExp element, Map<String, Object> params);
    Object visit(QualifiedModelPropertyNavigationExp element, Map<String, Object> params);
    Object visit(ModelOperationCallExp element, Map<String, Object> params);
    Object visit(CollectionOperationCallExp element, Map<String, Object> params);
    Object visit(CollectionIterateExp element, Map<String, Object> params);
    Object visit(CollectionIteratorExp element, Map<String, Object> params);

    //
    // Let expression
    //
    Object visit(OCLLetExpression element, Map<String, Object> params);

    //
    // Literal expressions
    //
    Object visit(IntegerLiteralExp element, Map<String, Object> params);
    Object visit(RealLiteralExp element, Map<String, Object> params);
    Object visit(BooleanLiteralExp element, Map<String, Object> params);
    Object visit(StringLiteralExp element, Map<String, Object> params);
    Object visit(CollectionLiteralExp element, Map<String, Object> params);
    Object visit(CollectionItem element, Map<String, Object> params);
    Object visit(CollectionRange element, Map<String, Object> params);
    Object visit(TupleLiteralExp element, Map<String, Object> params);
    Object visit(NullLiteralExp element, Map<String, Object> params);
    Object visit(InvalidLiteralExp element, Map<String, Object> params);
    Object visit(UnlimitedNaturalLiteralExp element, Map<String, Object> params);

    Object visit(VariableDeclaration element, Map<String, Object> params);
    Object visit(PositionableType element, Map<String, Object> params);

    //
    // Messages
    //
    Object visit(OCLMessageExp element, Map<String, Object> params);
    Object visit(OCLMessageArgument element, Map<String, Object> params);
}
