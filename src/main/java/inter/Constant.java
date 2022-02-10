package inter;

import lexer.Num;
import lexer.Tag;
import lexer.Token;
import lexer.Word;
import symbols.Type;

/**
 * @author A3bz
 */
public class Constant extends Expr {
    public Constant(Token tok, Type p) {
        super(tok, p);
    }

    public Constant(int i) {
        super(new Num(i), Type.Int);
    }

    public static final Constant
        True = new Constant(Word.True, Type.Bool),
        False = new Constant(Word.False, Type.Bool);

    @Override
    public void jumping(int t, int f) {
        if (t != 0 && this == True) {
            emit("goto L" + t);
        } else if (f != 0 && this == False) {
            emit("goto L" + f);
        }
    }
}
