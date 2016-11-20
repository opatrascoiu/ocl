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
package org.omg.ocl.analysis.syntax.ast.expression.logical;

import org.omg.ocl.analysis.semantics.type.StringType;
import org.omg.ocl.analysis.syntax.Position;
import org.omg.ocl.analysis.syntax.ast.expression.OCLExpression;

/**
 * Created by Octavian Patrascoiu on 15-Oct-16.
 */
public abstract class LogicalExp extends OCLExpression {
    private final OCLExpression leftOperand;
    private final OCLExpression rightOperand;

    public LogicalExp(Position position, OCLExpression leftOperand, OCLExpression rightOperand) {
        super(position);
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    public OCLExpression getLeftOperand() {
        return leftOperand;
    }

    public OCLExpression getRightOperand() {
        return rightOperand;
    }

    @Override
    public String toString() {
        return String.format("%s(%s, %s)", getClass().getSimpleName(), leftOperand, rightOperand);
    }
}
