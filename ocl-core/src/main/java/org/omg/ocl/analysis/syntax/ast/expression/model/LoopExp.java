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
import org.omg.ocl.analysis.syntax.ast.declaration.VariableDeclaration;
import org.omg.ocl.analysis.syntax.ast.expression.OCLExpression;

/**
 * Created by Octavian Patrascoiu on 15-Oct-16.
 */
public abstract class LoopExp extends ModelExp {
    private final String name;
    private final VariableDeclaration iterator1;
    private final VariableDeclaration iterator2;
    private final OCLExpression body;

    public LoopExp(Position position, OCLExpression source, String name, VariableDeclaration iterator1, VariableDeclaration iterator2, OCLExpression body) {
        super(position, source);
        this.name = name;
        this.iterator1 = iterator1;
        this.iterator2 = iterator2;
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public VariableDeclaration getIterator1() {
        return iterator1;
    }

    public VariableDeclaration getIterator2() {
        return iterator2;
    }

    public OCLExpression getBody() {
        return body;
    }
}
