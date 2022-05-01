package br.com.hbobenicio.javaexamples.parsingexamples;

import java.io.*;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class Tokenizer implements AutoCloseable {

    private static class PatternTokenProducer {
        private final Pattern regexPattern;
        private final Supplier<Token> tokenProducer;

        private PatternTokenProducer(Pattern regexPattern, Supplier<Token> tokenProducer) {
            this.regexPattern = regexPattern;
            this.tokenProducer = tokenProducer;
        }
    }

    /**
     * The input Scanner. Scanner is a really nice convenient class for writing Lexers.
     */
    private final Scanner inputScanner;

    /**
     * Represents the current position of the input source (starting from index 1).
     */
    private final SourcePosition currentPosition = new SourcePosition();

    /**
     * The pattern of characters to be skipped.
     */
    private final Pattern skipPattern = Pattern.compile("\\G[ \t\f\r]+");

    /**
     * Sequence of regex patterns used to match the input.
     * This order is the matching precedence.
     */
    private final PatternTokenProducer[] patternTokenProducers = new PatternTokenProducer[] {
            new PatternTokenProducer(Pattern.compile("\\G\r?\n"), Token::eol),
            new PatternTokenProducer(Pattern.compile("\\G[0-9]+"), Token::integer)
    };

    public Tokenizer(Reader inputReader) {
        this.inputScanner = new Scanner(inputReader);
        this.inputScanner.useDelimiter("$");
    }

    public Tokenizer(String input) {
        this(new StringReader(input));
    }

    public Tokenizer(InputStream inputStream) {
        this(new InputStreamReader(inputStream));
    }

    @Override
    public void close() {
        this.inputScanner.close();
    }

    public Token getNextToken() throws ParsingException {
        if (!this.inputScanner.hasNext()) {
            return Token.eof();
        }

        skipIgnored();
        if (!this.inputScanner.hasNext()) {
            return Token.eof();
        }

        for (var patternTokenProducer: this.patternTokenProducers) {
            Pattern regexPattern = patternTokenProducer.regexPattern;
            Supplier<Token> tokenProducer = patternTokenProducer.tokenProducer;

            Optional<String> maybeLexeme = findPattern(regexPattern);
            if (maybeLexeme.isEmpty()) {
                continue;
            }

            String lexeme = maybeLexeme.get();
            Token tok = tokenProducer.get();
            tok.setLexeme(lexeme);
            tok.setSourcePosition(this.currentPosition);

            if (tok.isEOL()) {
                this.currentPosition.advanceLine();
                return tok;
            }

            // if we're dealing with new-line tokens, then use the appropriate method to update source position
            // accordingly
            this.currentPosition.advance(tok.getLength());
            return tok;
        }

        if (this.inputScanner.hasNext()) {
            throw new ParsingException("Unexpected Token", this.currentPosition);
        }

        return Token.eof();
    }

    private void skipIgnored() {
        findPattern(this.skipPattern)
            .ifPresent(ignoredString -> this.currentPosition.advance(ignoredString.length()));
    }

    private Optional<String> findPattern(Pattern pattern) {
        return Optional.ofNullable(this.inputScanner.findWithinHorizon(pattern, 0));
    }
}
