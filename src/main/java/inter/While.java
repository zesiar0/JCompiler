package inter;

import symbols.Type;

/**
 * @author zeng9
 */
public class While extends Stmt {
    public Expr expr;
    public Stmt stmt;

    public While() {
        this.expr = null;
        this.stmt = null;
    }

    public void init(Expr x, Stmt s) {
        expr = x;
        stmt = s;

        if (expr.type != Type.Bool) {
            expr.error("boolean required in while");
        }
    }

    @Override
    public void gen(int b, int a) {
        int label = newlabel();

        after = a;
        expr.jumping(0, a);
        emitlabel(label);
        stmt.gen(label, a);
        emit("goto L" + b);
    }
}
