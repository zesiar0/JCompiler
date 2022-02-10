package lexer;

/**
 * @author A3bz
 */
public class Real extends Token {
    public final float value;

    public Real(float v) {
        super(Tag.REAL);
        this.value = v;
    }
}
