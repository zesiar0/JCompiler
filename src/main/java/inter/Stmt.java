package inter;

/**
 * @author A3bz
 */
public class Stmt extends Node {
    public int after = 0;

    public Stmt() {}

    public static Stmt Null = new Stmt();

    public static Stmt Enclosing = Stmt.Null;

    public void gen(int b, int a) {

    }
}
