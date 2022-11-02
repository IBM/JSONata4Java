package com.api.jsonata4java;

import java.io.IOException;
import java.io.Serializable;
import com.api.jsonata4java.expressions.EvaluateException;
import com.api.jsonata4java.expressions.EvaluateRuntimeException;
import com.api.jsonata4java.expressions.Expressions;
import com.api.jsonata4java.expressions.ParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Test implements Serializable {

    private static final long serialVersionUID = -7874581033151372290L;

    public static void main(String[] args) {
        Expressions expr = null;
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonObj = null;
        String json = "{ \"a\":1, \"b\":2, \"c\":[1,2,3,4,5] }";
        String expression = "$sum(c)";
        try {
            jsonObj = mapper.readTree(json);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        try {
            System.out.println("Using json:\n" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObj));
            System.out.println("expression=" + expression);
            expr = Expressions.parse(expression);
        } catch (ParseException e) {
            System.err.println(e.getLocalizedMessage());
        } catch (EvaluateRuntimeException ere) {
            System.err.println(ere.getLocalizedMessage());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException ioe) {
            System.err.println(ioe.getLocalizedMessage());
        }
        try {
            System.out.println("evaluate returns:");
            JsonNode result = expr.evaluate(jsonObj);
            if (result == null) {
                System.out.println("** no match **");
            } else {
                System.out.println("" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));
            }
        } catch (EvaluateException | JsonProcessingException e) {
            System.err.println(e.getLocalizedMessage());
        }
    }

}
