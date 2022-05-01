package br.com.hbobenicio.javaexamples.parsingexamples;

import java.util.Objects;

public class Token {
    private final TokenKind kind;
    private SourcePosition sourcePosition;
    private String lexeme;

    public Token(TokenKind kind, SourcePosition sourcePosition, String lexeme) {
        this.kind = kind;
        this.sourcePosition = sourcePosition;
        this.lexeme = lexeme;
    }

    public Token(TokenKind kind) {
        this(kind, new SourcePosition(), "");
    }

    public static Token eof() {
        return new Token(TokenKind.EOF);
    }

    public static Token eol() {
        return new Token(TokenKind.EOL);
    }

    public static Token integer() {
        return new Token(TokenKind.INTEGER);
    }

    public int getLength() {
        return this.lexeme.length();
    }

    public boolean isEOF() {
        return this.kind == TokenKind.EOF;
    }

    public boolean isEOL() {
        return this.kind == TokenKind.EOL;
    }

    public boolean isInteger() {
        return this.kind == TokenKind.INTEGER;
    }

    @Override
    public String toString() {
        switch (this.kind) {
            case INTEGER:
                return String.format("<INTEGER(%s)>", this.lexeme);

            case EOL: {
                var escapedlexeme = this.lexeme
                        .replaceAll("\n", "\\\\n")
                        .replaceAll("\r", "\\\\r");
                return String.format("<EOL(%s)>", escapedlexeme);
            }

            case EOF:
                return "<EOF>";
            default:
                throw new IllegalStateException("Trying to convert unknown token kind to string");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return kind == token.kind && Objects.equals(lexeme, token.lexeme);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kind, lexeme);
    }

    public TokenKind getKind() {
        return kind;
    }

    public SourcePosition getSourcePosition() {
        return sourcePosition;
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setSourcePosition(SourcePosition sourcePosition) {
        this.sourcePosition = sourcePosition;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }
}
