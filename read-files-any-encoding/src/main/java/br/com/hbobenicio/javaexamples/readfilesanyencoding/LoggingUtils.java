package br.com.hbobenicio.javaexamples.readfilesanyencoding;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import org.slf4j.LoggerFactory;

public class LoggingUtils {
    public static void configureRootLogger() {
        ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        LoggerContext rootLoggerContext = rootLogger.getLoggerContext();
        rootLoggerContext.reset();

        PatternLayoutEncoder logEncoder = new PatternLayoutEncoder();
        logEncoder.setContext(rootLoggerContext);
        logEncoder.setPattern("[%date] %highlight([%level] %msg%n)");
        logEncoder.start();

        ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<ILoggingEvent>();
        appender.setContext(rootLoggerContext);
        appender.setEncoder(logEncoder);
        appender.setOutputStream(System.err);
        appender.start();

        rootLogger.addAppender(appender);
    }
}
