package com.api.jsonata4java.expressions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Jsonata4JavaTestMapper {

    protected final String mappingDescription;

    public Jsonata4JavaTestMapper(final String mappingDescription) {
        this.mappingDescription = mappingDescription;
        this.expressions = parseMappingDescription();
    }

    public Jsonata4JavaTestMapper(final File mappingDescriptionFile) {
        this.mappingDescription = readFile(mappingDescriptionFile);
        this.expressions = parseMappingDescription();
    }

    private final Expressions expressions;

    private final ObjectMapper mapper = new ObjectMapper();

    public String map(final String in) {
        return map(in, false);
    }

    public String map(final String in, final boolean beautify) {
        try {
            final JsonNode outputRootNode = map(mapper.readTree(in));
            if (beautify) {
                return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(outputRootNode);
            } else {
                return mapper.writeValueAsString(outputRootNode);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public JsonNode map(final JsonNode inputRootNode) {
        try {
            return this.expressions.evaluate(inputRootNode);
        } catch (EvaluateException e) {
            throw new RuntimeException(e);
        }
    }

    private Expressions parseMappingDescription() {
        try {
            return Expressions.parse(this.mappingDescription);
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readFile(final File file) {
        try {
            return new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
