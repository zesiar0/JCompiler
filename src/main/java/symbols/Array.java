package symbols;

import lexer.Tag;

/**
 * @author A3bz
 */
public class Array extends Type {
    // 数组的元素类型
    public Type of;
    // 元素个数
    public int size = 1;

    public Array(int sz, Type p) {
        super("[]", Tag.INDEX, sz * p.width);
        this.size = sz;
        this.of = p;
    }

    @Override
    public String toString() {
        return "[" + size + "] " + of.toString();
    }
}
