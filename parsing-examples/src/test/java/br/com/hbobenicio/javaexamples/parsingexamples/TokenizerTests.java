package br.com.hbobenicio.javaexamples.parsingexamples;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

class TokenizerTests {

    private static class TestCase {
        public final String input;
        public final List<Token> expectedTokens;

        public TestCase(String input, List<Token> expectedTokens) {
            this.input = input;
            this.expectedTokens = expectedTokens;
        }
    }

    private static Stream<TestCase> testGetNextTestCaseProvider() {
        return Stream.of(
                new TestCase("", Arrays.asList(Token.eof())),
                new TestCase(" ", Arrays.asList(Token.eof())),
                new TestCase("123", Arrays.asList(
                        new Token(TokenKind.INTEGER, new SourcePosition(1, 2), "123"),
                        Token.eof())
                ),
                new TestCase(" \t 123\t \t", Arrays.asList(
                        new Token(TokenKind.INTEGER, new SourcePosition(1, 2), "123"),
                        Token.eof())
                )
        );
    }

    @ParameterizedTest
    @MethodSource("testGetNextTestCaseProvider")
    void testGetNext(TestCase tc) {
        List<Token> actualTokens = new ArrayList<>();
        try (var tokenizer = new Tokenizer(tc.input)) {
            var token = tokenizer.getNextToken();
            actualTokens.add(token);
            while (!token.isEOF()) {
                token = tokenizer.getNextToken();
                actualTokens.add(token);
            }

            Assertions.assertEquals(tc.expectedTokens, actualTokens);

        } catch (ParsingException e) {
            Assertions.fail(e);
        }
    }
}
