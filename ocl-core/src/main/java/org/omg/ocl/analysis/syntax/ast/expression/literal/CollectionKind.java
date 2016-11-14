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

import org.omg.ocl.analysis.syntax.lexer.Token;

import static org.omg.ocl.analysis.syntax.antlr.OCLLexer.*;

/**
 * Created by Octavian Patrascoiu on 15-Oct-16.
 */
public enum CollectionKind {
    Collection, Set, OrderedSet, Bag, Sequence;

    public static CollectionKind find(Token kind) {
        if (kind == null) {
            throw new IllegalArgumentException(String.format("Incorrect collection kind '%s'", kind));
        }
        if (kind.getKind() == COLLECTION) {
            return Collection;
        } else if (kind.getKind() == SET) {
            return Set;
        } else if (kind.getKind() == ORDERED_SET) {
            return OrderedSet;
        } else if (kind.getKind() == BAG) {
            return Bag;
        } else if (kind.getKind() == SEQUENCE) {
            return Sequence;
        } else {
            throw new IllegalArgumentException(String.format("Incorrect collection kind '%s'", kind.getLexeme()));
        }
    }
}
