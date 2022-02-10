package inter;

import lexer.Token;
import symbols.Type;

/**
 * @author A3bz
 */
public class Unary extends Op {
    public Expr expr;

    public Unary(Token tok, Expr x) {
        super(tok, null);
        type = Type.max(Type.Int, x.type);
        if (type == null) {
            error("type error");
        }
    }

    @Override
    public Expr gen() {
        return new Unary(op, expr.reduce());
    }

    @Override
    public String toString() {
        return op.toString() + " " + expr.toString();
    }
}
