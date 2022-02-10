package inter;

import symbols.Type;

/**
 * @author A3bz
 */
public class Do extends Stmt {
    public Expr expr;
    public Stmt stmt;

    public Do() {
        expr = null;
        stmt = null;
    }

    public void init(Expr expr, Stmt stmt) {
        this.expr = expr;
        this.stmt = stmt;

        if (expr.type != Type.Bool) {
            expr.error("boolean required in do");
        }
    }

    @Override
    public void gen(int b, int a) {
        int label = newlabel();

        after = a;
        stmt.gen(b, label);
        emitlabel(label);
        expr.jumping(b, 0);
    }
}
