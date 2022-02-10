package inter;

import lexer.Token;
import symbols.Type;

/**
 * @author A3bz
 */
public class Op extends Expr {

    Op(Token tok, Type p) {
        super(tok, p);
    }

    @Override
    public Expr reduce() {
        Expr x = gen();
        Temp t = new Temp(type);
        emit(t.toString() + " = " + x.toString());
        return t;
    }
}
