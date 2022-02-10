package inter;

import lexer.Token;
import lexer.Word;
import symbols.Type;

/**
 * @author A3bz
 */
public class Temp extends Expr {
    static int count = 0;
    int number = 0;

    Temp(Type p) {
        super(Word.temp, p);
        number = ++count;
    }

    @Override
    public String toString() {
        return "t" + number;
    }
}
