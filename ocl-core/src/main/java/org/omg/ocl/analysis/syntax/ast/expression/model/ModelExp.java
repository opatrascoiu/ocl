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
package org.omg.ocl.analysis.syntax.ast.expression.model;

import org.omg.ocl.analysis.syntax.Position;
import org.omg.ocl.analysis.syntax.ast.expression.OCLExpression;

/**
 * Created by Octavian Patrascoiu on 15-Oct-16.
 */
public abstract class ModelExp extends OCLExpression {
    private final OCLExpression source;

    public ModelExp(Position position, OCLExpression source) {
        super(position);
        this.source = source;
    }

    public OCLExpression getSource() {
        return source;
    }
}
