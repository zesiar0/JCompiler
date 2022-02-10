package inter;

import lexer.Token;

/**
 * @author A3bz
 */
public class Not extends Logical {
    public Not(Token tok, Expr expr1) {
        super(tok, expr1, expr1);
    }

    @Override
    public void jumping(int t, int f) {
        expr1.jumping(f, t);
    }

    @Override
    public String toString() {
        return op.toString() + " " + expr1.toString();
    }
}
