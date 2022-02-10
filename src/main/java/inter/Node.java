package inter;

import lexer.Lexer;

/**
 * @author A3bz
 */
public class Node {
    int lexline = 0;
    static int labels = 0;

    public Node() {
        this.lexline = Lexer.line;
    }

    public void error(String s) {
        throw new Error("near line " + lexline + ": " + s);
    }

    public int newlabel() {
        return ++labels;
    }

    public void emitlabel(int i ) {
        System.out.println("L" + i + ":");
    }

    public void emit(String s) {
        System.out.println("\t" + s);
    }
}
