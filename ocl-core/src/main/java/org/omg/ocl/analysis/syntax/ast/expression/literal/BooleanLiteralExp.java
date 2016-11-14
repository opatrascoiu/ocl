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
package org.omg.ocl.analysis.syntax.ast.expression.literal;

import org.omg.ocl.analysis.syntax.Position;
import org.omg.ocl.analysis.syntax.Visitor;

import java.util.Map;

/**
 * Created by Octavian Patrascoiu on 15-Oct-16.
 */
public class BooleanLiteralExp extends PrimitiveLiteralExp {
    private final Boolean value;

    public BooleanLiteralExp(Position position, Boolean value) {
        super(position);
        this.value = value;
    }

    public Boolean getValue() {
        return value;
    }

    @Override
    public Object accept(Visitor visitor, Map<String, Object> params) {
        return visitor.visit(this, params);
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", getClass().getSimpleName(), value);
    }
}
