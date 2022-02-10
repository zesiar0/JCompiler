package inter;

/**
 * @author A3bz
 */
public class Break extends Stmt {
    public Stmt stmt;

    public Break() {
        if (Stmt.Enclosing == Stmt.Null) {
            error("unenclosed break");
        }

        this.stmt = Stmt.Enclosing;
    }

    @Override
    public void gen(int b, int a) {
        emit("goto L" + stmt.after);
    }
}
