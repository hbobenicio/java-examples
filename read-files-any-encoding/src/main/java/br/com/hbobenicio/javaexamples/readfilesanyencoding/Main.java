package br.com.hbobenicio.javaexamples.readfilesanyencoding;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

public final class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    static {
        LoggingUtils.configureRootLogger();
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            LOG.error("This program expects exactly one argument: the input file.");
            printUsage();
            System.exit(1);
        }

        var inputFilePath = args[0];
        readFileAnyEncoding(inputFilePath);
    }

    private static void readFileAnyEncoding(String inputFilePath) {
        try (InputStream bis = new BufferedInputStream(new FileInputStream(inputFilePath))) {

            String fileContent = readStringForSupportedEncodings(bis);

            System.out.println("=== File Contents ===");
            System.out.print(fileContent);
            System.out.println("=== End ===");

        } catch (Exception e) {
            LOG.error("Failed to read file '{}'", inputFilePath, e);
            System.exit(1);
        }
    }

    private static String readStringForSupportedEncodings(InputStream is) throws Exception {
        var charsetDetector = new CharsetDetector();
        charsetDetector.setText(is);
        charsetDetector.enableInputFilter(true);

        CharsetMatch cm = charsetDetector.detect();
        if (cm != null) {
            LOG.info("uci4j guessed charset: {}", cm.getName());
            return cm.getString();
        }
        throw new Exception("No supported charset could be guessed");
    }

    private static void printUsage() {
        System.out.println("Usage:\n\tjava -jar read-files-any-encoding.jar -- <INPUT-FILE>\n");
    }
}

