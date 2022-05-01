package br.com.hbobenicio.javaexamples.parsingexamples;

import java.io.Serializable;
import java.util.Objects;

public class SourcePosition implements Serializable {
    private static final long serialVersionUID = 1L;

    private int line;
    private int column;

    public SourcePosition() {
        this.line = 1;
        this.column = 1;
    }

    public SourcePosition(int line, int column) {
        if (line <= 0 || column <= 0) {
            throw newIllegalArgumentException();
        }
        this.line = line;
        this.column = column;
    }

    private IllegalArgumentException newIllegalArgumentException() {
        return new IllegalArgumentException("SourcePosition must use positive non-zero integers as source positions");
    }

    @Override
    public String toString() {
        return String.format("SourceLine{line=%d, colunm=%d}", this.line, this.column);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SourcePosition that = (SourcePosition) o;
        return line == that.line && column == that.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(line, column);
    }

    public void advance() {
        this.column++;
    }

    public void advance(int n) {
        this.column += n;
    }

    public void advanceLine() {
        this.line++;
        this.column = 1;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        if (line <= 0) {
            throw newIllegalArgumentException();
        }
        this.line = line;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        if (column <= 0) {
            throw newIllegalArgumentException();
        }
        this.column = column;
    }
}
