package inter;

import lexer.Token;
import symbols.Type;

/**
 * @author A3bz
 */
public class Expr extends Node {
    public Token op;
    public Type type;

    Expr(Token tok, Type p) {
        this.op = tok;
        this.type = p;
    }

    public Expr gen() {
        return this;
    }

    public Expr reduce() {
        return this;
    }

    public void jumping(int t, int f) {
        emitjumps(toString(), t, f);
    }

    public void emitjumps(String test, int t, int f) {
        if (t != 0 && f != 0) {
            emit("if " + test + " goto L" + t);
            emit("goto L" + f);
        } else if (t != 0) {
            emit("if " + test + " goto L" + t);
        } else if (f != 0) {
            emit("ifFalse " + test + " goto L" + f);
        }
    }

    @Override
    public String toString() {
        return op.toString();
    }

}
