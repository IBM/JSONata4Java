package com.api.jsonata4java.testerui;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Paths;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

public class TesterUITest {

    TesterUI testerUi;

    @Before
    public void setUp() throws IOException {
        if (!isOnWindows()) {
            return;
        }
        testerUi = new TesterUI();
    }

    @Test
    public void testXmlToJson() throws IOException {
        if (!isOnWindows()) {
            return;
        }
        assertEquals(minifyJson(TesterUI.readFile(Paths.get("src/test/resources/exerciser/xmladdress.json"))),
            minifyJson(testerUi
                .xmlToJson(TesterUI.readFile(Paths.get("src/test/resources/exerciser/xmladdress.xml")))));
    }

    public static String minifyJson(final String in) {
        final StringWriter sw = new StringWriter();
        final JsonFactory factory = new JsonFactory();
        try (final JsonGenerator gen = factory.createGenerator(sw)) {
            final JsonParser parser = factory.createParser(in);
            while (parser.nextToken() != null) {
                gen.copyCurrentEvent(parser);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sw.getBuffer().toString();
    }

    private boolean isOnWindows() {
        return System.getProperty("os.name").startsWith("Windows");
    }
}
