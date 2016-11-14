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
package org.omg.ocl.analysis.syntax.ast.declaration;

import org.omg.ocl.analysis.semantics.type.Type;
import org.omg.ocl.analysis.syntax.Position;
import org.omg.ocl.analysis.syntax.PositionableElement;
import org.omg.ocl.analysis.syntax.Visitor;
import org.omg.ocl.analysis.syntax.ast.expression.OCLExpression;

import java.util.Map;

/**
 * Created by Octavian Patrascoiu on 15-Oct-16.
 */
public class VariableDeclaration extends PositionableElement {
    private final String name;
    private final Type type;
    private final OCLExpression initExp;

    public VariableDeclaration(Position position, String name, Type type, OCLExpression initExp) {
        super(position);
        this.name = name;
        this.type = type;
        this.initExp = initExp;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public OCLExpression getInitExp() {
        return initExp;
    }

    @Override
    public Object accept(Visitor visitor, Map<String, Object> params) {
        return visitor.visit(this, params);
    }

    @Override
    public String toString() {
        return String.format("%s(%s, %s, %s)", getClass().getSimpleName(), name, type, initExp);
    }

}
