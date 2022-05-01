package br.com.hbobenicio.javaexamples.parsingexamples;

public class ParsingException extends Exception {
    private final SourcePosition sourcePosition;

    public ParsingException(String message, SourcePosition sourcePosition, Throwable cause) {
        super(String.format("%s at line %d, column %s", message, sourcePosition.getLine(), sourcePosition.getColumn()), cause);
        this.sourcePosition = sourcePosition;
    }

    public ParsingException(String message, SourcePosition sourcePosition) {
        this(message, sourcePosition, null);
    }

    public SourcePosition getSourcePosition() {
        return sourcePosition;
    }
}
