package parser;

import inter.*;
import lexer.*;
import symbols.Array;
import symbols.Env;
import symbols.Type;

import java.io.IOException;

/**
 * @author A3bz
 */
public class Parser {
    private Lexer lexer;
    private Token look;
    public Env top = null;
    public int used = 0;

    public Parser(Lexer l) throws IOException {
        this.lexer = l;
        move();
    }

    private void move() throws IOException {
        look = lexer.scan();
    }

    private void error(String s) {
        throw new Error("near line " + Lexer.line + ": " + s);
    }

    private void match(int t) throws IOException {
        if (look.tag == t) {
            move();
        } else {
            error("syntax error");
        }
    }

    // program -> block
    public void program() throws IOException {
        Stmt stmt = block();
        int begin = stmt.newlabel();
        int after = stmt.newlabel();
        stmt.emitlabel(begin);
        stmt.gen(begin, after);
        stmt.emitlabel(after);
    }

    // block -> { decls stmts }
    public Stmt block() throws IOException {
        match('{');
        Env savedEnv = top;
        top = new Env(top);
        decls();
        Stmt s = stmts();
        match('}');
        top = savedEnv;

        return s;
    }

    // D -> type ID ;
    public void decls() throws IOException {
        while(look.tag == Tag.BASIC) {
            Type p = type();
            Token tok = look;
            match(Tag.ID);
            match(';');

            Id id = new Id(tok, p, used);
            top.put(tok, id);

            used = used + p.width;
        }
    }

    public Type type() throws IOException {
        Type p = (Type) look;
        match(Tag.BASIC);

        if (look.tag != '[') {
            return p;
        } else {
            return dims(p);
        }
    }

    public Type dims(Type p) throws IOException {
        match('[');
        Token tok = look;
        match(Tag.NUM);
        match(']');

        if (look.tag == '[') {
            p = dims(p);
        }

        return new Array(((Num)tok).value, p);
    }

    public Stmt stmts() throws IOException {
        if (look.tag == '}') {
            return Stmt.Null;
        } else {
            return new Seq(stmt(), stmts());
        }
    }

    public Stmt stmt() throws IOException {
        Expr x;
        Stmt s;
        Stmt s1;
        Stmt s2;
        Stmt savedStmt;

        switch (look.tag) {
            case ';':
                move();
                return Stmt.Null;
            case Tag.IF:
                match(Tag.IF);
                match('(');
                x = bool();
                match(')');

                s1 = stmt();
                if (look.tag != Tag.ELSE) {
                    return new If(x, s1);
                }
                match(Tag.ELSE);
                s2 = stmt();
                return new Else(x, s1, s2);
            case Tag.WHILE:
                While whileNode = new While();

                savedStmt = Stmt.Enclosing;
                Stmt.Enclosing = whileNode;

                match(Tag.WHILE);
                match('(');
                x = bool();
                match(')');

                s1 = stmt();
                whileNode.init(x, s1);
                Stmt.Enclosing = savedStmt;
                return whileNode;
            case Tag.DO:
                Do doNode = new Do();

                savedStmt = Stmt.Enclosing;
                Stmt.Enclosing = doNode;

                match(Tag.DO);
                s1 = stmt();
                match(Tag.WHILE);
                match('(');
                x = bool();
                match(')');
                match(';');

                doNode.init(x, s1);
                Stmt.Enclosing = savedStmt;
                return doNode;
            case Tag.BREAK:
                match(Tag.BREAK);
                match(';');

                return new Break();
            case '{':
                return block();
            default:
                return assign();
        }
    }

    public Stmt assign() throws IOException {
        Stmt stmt;
        Token t = look;

        match(Tag.ID);

        Id id = top.get(t);
        if (id == null) {
            error(t.toString() + " undeclared");
        }
        // S -> id = E ;
        if (look.tag == '=') {
            move();
            stmt = new Set(id, bool());
        } else { // S -> L = E ;
            Access x = offset(id);
            match('=');
            stmt = new SetElem(x, bool());
        }

        match(';');
        return stmt;
    }

    public Expr bool() throws IOException {
        Expr x = join();

        while (look.tag == Tag.OR) {
            Token tok = look;
            move();
            x = new Or(tok, x, join());
        }

        return x;
    }

    public Expr join() throws IOException {
        Expr x = equality();

        while (look.tag == Tag.AND) {
            Token tok = look;
            move();
            x = new And(tok, x, equality());
        }

        return x;
    }

    public Expr equality() throws IOException {
        Expr x = rel();

        while (look.tag == Tag.EQ || look.tag == Tag.NE) {
            Token tok = look;
            move();
            x = new Rel(tok, x, rel());
        }

        return x;
    }

    public Expr rel() throws IOException {
        Expr x = expr();

        switch (look.tag) {
            case '<':
            case Tag.LE:
            case Tag.GE:
            case '>':
                Token tok = look;
                move();
                return new Rel(tok, x, expr());
            default:
                return x;
        }
    }

    public Expr expr() throws IOException {
        Expr x = term();

        while (look.tag == '+' || look.tag == '-') {
            Token tok = look;
            move();
            x = new Arith(tok, x, unary());
        }

        return x;
    }

    public Expr term() throws IOException {
        Expr x = unary();

        while (look.tag == '*' || look.tag == '/') {
            Token tok = look;
            move();
            x = new Arith(tok, x, unary());
        }

        return x;
    }

    public Expr unary() throws IOException {
        if (look.tag == '-') {
            move();
            return new Unary(Word.minus, unary());
        } else if (look.tag == '!') {
            Token tok = look;
            move();
            return new Not(tok, unary());
        } else {
            return factor();
        }
    }

    public Expr factor() throws IOException {
        Expr x = null;

        switch (look.tag) {
            case '(':
                move();
                x = bool();
                match(')');

                return x;
            case Tag.NUM:
                x = new Constant(look, Type.Int);
                move();
                return x;
            case Tag.REAL:
                x = new Constant(look, Type.Float);
                move();
                return x;
            case Tag.TRUE:
                x = Constant.True;
                move();
                return x;
            case Tag.FALSE:
                x = Constant.False;
                move();
                return x;
            case Tag.ID:
                String s = look.toString();
                Id id = top.get(look);

                if (id == null) {
                    error(look.toString() + " undeclared");
                }

                move();
                if (look.tag != '[') {
                    return id;
                } else {
                    return offset(id);
                }
            default:
                error("syntax error");
                return x;
        }
    }

    // I -> [E] | [E] I
    public Access offset(Id a) throws IOException {
        Expr i;
        Expr w;
        Expr t1;
        Expr t2;
        Expr loc;

        Type type = a.type;
        match('[');
        i = bool();
        match(']');

        type = ((Array)type).of;
        w = new Constant(type.width);
        t1 = new Arith(new Token('*'), i, w);
        loc = t1;

        // I -> [E]I
        while (look.tag == '[') {
            match('[');
            i = bool();
            match(']');

            type = ((Array)type).of;
            w = new Constant(type.width);
            t1 = new Arith(new Token('*'), i, w);
            t2 = new Arith(new Token('+'), loc, t1);
            loc = t2;
        }

        return new Access(a, loc, type);
    }
}
