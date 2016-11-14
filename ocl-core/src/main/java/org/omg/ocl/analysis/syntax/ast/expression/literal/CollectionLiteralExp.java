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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Octavian Patrascoiu on 15-Oct-16.
 */
public class CollectionLiteralExp extends LiteralExp {
    private final CollectionKind kind;
    private List<CollectionLiteralPart> parts = new ArrayList<>();

    public CollectionLiteralExp(Position position, CollectionKind kind, List<CollectionLiteralPart> parts) {
        super(position);
        this.kind = kind;
        if (parts != null) {
            this.parts = parts;
        }
    }

    public CollectionKind getKind() {
        return kind;
    }

    public List<CollectionLiteralPart> getParts() {
        return parts;
    }

    @Override
    public Object accept(Visitor visitor, Map<String, Object> params) {
        return visitor.visit(this, params);
    }

    @Override
    public String toString() {
        String partsText = parts.stream().map(p -> (String)p.toString()).collect(Collectors.joining(", "));
        return String.format("%s(%s, {%s})", getClass().getSimpleName(), kind, partsText);
    }

}
