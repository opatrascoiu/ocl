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
package org.omg.ocl.analysis.syntax.lexer;

import org.antlr.v4.runtime.Token;
import org.omg.ocl.analysis.syntax.Position;

/**
 * Created by Octavian Patrascoiu on 23-Oct-16.
 */
public class ANTLRTokenAdapter implements org.omg.ocl.analysis.syntax.lexer.Token {
    private Token token;

    public ANTLRTokenAdapter(Token token) {
        this.token = token;
    }

    @Override
    public int getKind() {
        return token.getType();
    }

    @Override
    public String getLexeme() {
        return token.getText();
    }

    @Override
    public int getBeginLine() {
        return token.getLine();
    }

    @Override
    public int getBeginColumn() {
        return token.getCharPositionInLine() + 1;
    }

    @Override
    public int getBeginOffset() {
        return token.getStartIndex();
    }

    @Override
    public int getEndLine() {
        int lineNo = countLines(token.getText());
        return token.getLine() + lineNo -1;
    }

    @Override
    public int getEndColumn() {
        int i = token.getText().lastIndexOf('\n');
        if (i == -1) {
            return token.getCharPositionInLine() + token.getText().length();
        } else {
            return token.getText().length() - i;
        }
    }

    @Override
    public int getEndOffset() {
        return token.getStopIndex() + 1;
    }

    private int countLines(String text) {
        int lineNo = 1;
        for(int i=0; i<text.length(); i++) {
            if (text.charAt(i) == '\n') {
                lineNo++;
            }
        }
        return lineNo;
    }

    @Override
    public Position getPosition() {
        return new Position(getBeginLine(), getBeginColumn(), getBeginOffset(),
                getEndLine(), getEndColumn(), getEndOffset()
        );
    }
}
