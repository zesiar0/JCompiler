package inter;

import symbols.Type;

/**
 * @author A3bz
 */
public class Else extends Stmt {
    public Expr expr;
    public Stmt stmt1;
    public Stmt stmt2;

    public Else(Expr expr, Stmt stmt1, Stmt stmt2) {
        this.expr = expr;
        this.stmt1 = stmt1;
        this.stmt2 = stmt2;

        if (expr.type != Type.Bool) {
            expr.error("boolean required in if");
        }
    }

    @Override
    public void gen(int b, int a) {
        int label1 = newlabel();
        int label2 = newlabel();

        expr.jumping(0, label2);
        emitlabel(label1);
        stmt1.gen(label1, a);
        emit("goto L" + a);
        emitlabel(label2);
        stmt2.gen(label2, a);
    }
}
