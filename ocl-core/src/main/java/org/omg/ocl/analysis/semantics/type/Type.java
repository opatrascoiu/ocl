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
package org.omg.ocl.analysis.semantics.type;

import org.omg.ocl.analysis.syntax.ast.expression.OCLExpression;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Octavian Patrascoiu on 15-Oct-16.
 */
public abstract class Type {
    private List<Property> properties = new ArrayList<>();
    private List<Operation> operations = new ArrayList<>();

    public boolean isTypeOf(Type other) {
        if (other == null) {
            return false;
        }
        return this.getClass().equals(other.getClass());
    }

    public boolean isKindOf(Type other) {
        if (other == null) {
            return false;
        }
        return other.getClass().isAssignableFrom(this.getClass());
    }

    public void addProperty(Property property) {
        if (property != null && !properties.contains(property)) {
            properties.add(property);
        }
    }

    public void addOperation(Operation operation) {
        if (operation != null && !properties.contains(operation)) {
            operations.add(operation);
        }
    }

    public Property findProperty(String name) {
        List<Property> list = this.properties.stream().filter(p -> p.getName().equals(name) && !p.hasQualifiers()).collect(Collectors.toList());
        if (list.isEmpty()) {
            return null;
        } else if (list.size() == 1) {
            return list.get(0);
        } else {
            throw new RuntimeException(String.format("Found multiple properties with name '%s'", name));
        }
    }

    public Property findProperty(String name, List<OCLExpression> actualQualifiers) {
        List<Property> list = this.properties.stream().filter(p -> match(p, name, actualQualifiers)).collect(Collectors.toList());
        if (list.isEmpty()) {
            return null;
        } else if (list.size() == 1) {
            return list.get(0);
        } else {
            throw new RuntimeException(String.format("Found multiple properties with name '%s' and qualifiers '%s'", name, actualQualifiers.toString()));
        }
    }

    public Operation findOperation(String name, List<Type> actualTypes) {
        List<Operation> list = this.operations.stream().filter(p -> match(p, name, actualTypes)).collect(Collectors.toList());
        if (list.isEmpty()) {
            return null;
        } else if (list.size() == 1) {
            return list.get(0);
        } else {
            throw new RuntimeException(String.format("Found multiple operations with name '%s' and types '%s'", name, actualTypes.toString()));
        }
    }

    private boolean match(Property property, String name, List<OCLExpression> actualQualifiers) {
        if (property.getName().equals(name) && property.hasQualifiers()) {
            if (property.getQualifiers().size() == actualQualifiers.size()) {
                for(int i=0; i < property.getQualifiers().size(); i++) {
                    Type formalType = property.getQualifiers().get(i).getType();
                    Type actualType = actualQualifiers.get(i).getType();
                    if (!actualType.isKindOf(formalType)) {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean match(Operation operation, String name, List<Type> actualTypes) {
        if (operation.getName().equals(name)) {
            if (operation.getParameters().size() == actualTypes.size()) {
                for(int i=0; i < operation.getParameters().size(); i++) {
                    Type formalType = operation.getParameters().get(i).getType();
                    Type actualType = actualTypes.get(i);
                    if (!actualType.isKindOf(formalType)) {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }
        return true;
    }

}
