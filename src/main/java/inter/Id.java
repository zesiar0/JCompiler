package inter;

import lexer.Token;
import symbols.Type;

/**
 * @author A3bz
 */
public class Id extends Expr {
    public int offset;

    public Id(Token tok, Type p, int b) {
        super(tok, p);
        this.offset = b;
    }
}
