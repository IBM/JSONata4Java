/**
 * (c) Copyright 2018, 2019 IBM Corporation
 * 1 New Orchard Road, 
 * Armonk, New York, 10504-1722
 * United States
 * +1 914 499 1900
 * support: Nathaniel Mills wnm3@us.ibm.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.api.jsonata4java;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import com.api.jsonata4java.expressions.EvaluateException;
import com.api.jsonata4java.expressions.EvaluateRuntimeException;
import com.api.jsonata4java.expressions.Expressions;
import com.api.jsonata4java.expressions.ParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;

/**
 * Expression evaluation test utility
 */
public class Tester implements Serializable {

    private static final long serialVersionUID = 6610473272854611804L;

    static String json = "{\n" + //
        "  \"Account\": {\n" + //
        "    \"Account Name\": \"Firefly\",\n" + //
        "    \"Order\": [\n" + //
        "      {\n" + //
        "        \"OrderID\": \"order103\",\n" + //
        "        \"Product\": [\n" + //
        "          {\n" + //
        "            \"Product Name\": \"Bowler Hat\",\n" + //
        "            \"ProductID\": 858383,\n" + //
        "            \"SKU\": \"0406654608\",\n" + //
        "            \"Description\": {\n" + //
        "              \"Colour\": \"Purple\",\n" + //
        "              \"Width\": 300,\n" + //
        "              \"Height\": 200,\n" + //
        "              \"Depth\": 210,\n" + //
        "              \"Weight\": 0.75\n" + //
        "            },\n" + //
        "            \"Price\": 34.45,\n" + //
        "            \"Quantity\": 2\n" + //
        "          },\n" + //
        "          {\n" + //
        "            \"Product Name\": \"Trilby hat\",\n" + //
        "            \"ProductID\": 858236,\n" + //
        "            \"SKU\": \"0406634348\",\n" + //
        "            \"Description\": {\n" + //
        "              \"Colour\": \"Orange\",\n" + //
        "              \"Width\": 300,\n" + //
        "              \"Height\": 200,\n" + //
        "              \"Depth\": 210,\n" + //
        "              \"Weight\": 0.6\n" + //
        "            },\n" + //
        "            \"Price\": 21.67,\n" + //
        "            \"Quantity\": 1\n" + //
        "          }\n" + //
        "        ]\n" + //
        "      },\n" + //
        "      {\n" + //
        "        \"OrderID\": \"order104\",\n" + //
        "        \"Product\": [\n" + //
        "          {\n" + //
        "            \"Product Name\": \"Bowler Hat\",\n" + //
        "            \"ProductID\": 858383,\n" + //
        "            \"SKU\": \"040657863\",\n" + //
        "            \"Description\": {\n" + //
        "              \"Colour\": \"Purple\",\n" + //
        "              \"Width\": 300,\n" + //
        "              \"Height\": 200,\n" + //
        "              \"Depth\": 210,\n" + //
        "              \"Weight\": 0.75\n" + //
        "            },\n" + //
        "            \"Price\": 34.45,\n" + //
        "            \"Quantity\": 4\n" + //
        "          },\n" + //
        "          {\n" + //
        "            \"ProductID\": 345664,\n" + //
        "            \"SKU\": \"0406654603\",\n" + //
        "            \"Product Name\": \"Cloak\",\n" + //
        "            \"Description\": {\n" + //
        "              \"Colour\": \"Black\",\n" + //
        "              \"Width\": 30,\n" + //
        "              \"Height\": 20,\n" + //
        "              \"Depth\": 210,\n" + //
        "              \"Weight\": 2\n" + //
        "            },\n" + //
        "            \"Price\": 107.99,\n" + //
        "            \"Quantity\": 1\n" + //
        "          }\n" + //
        "        ]\n" + //
        "      }\n" + //
        "    ]\n" + //
        "  }\n" + //
        "}\n"; //

    /**
     * @param args a fully qualified path and filename for test JSON could be provided
     */
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.getFactory().configure(JsonWriteFeature.ESCAPE_NON_ASCII.mappedFeature(), true);
        JsonNode jsonObj = null;
        try {
            jsonObj = mapper.readTree(json);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        if (args.length > 0) {
            if (args[0].equals("null") == false) {
                File file = new File(args[0]);
                if (file.exists()) {
                    System.out.println("Attempting to load JSON from file: " + args[0]);
                    try {
                        jsonObj = mapper.readTree(file);
                    } catch (JsonProcessingException e) {
                        System.err.println(e.getLocalizedMessage());
                    } catch (IOException e) {
                        System.err.println(e.getLocalizedMessage());
                    }
                } else {
                    jsonObj = new TextNode(args[0]);
                }
            } else {
                jsonObj = null;
            }
        }
        try {
            System.out.println("Using json:\n" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObj));
        } catch (JsonProcessingException e1) {
            e1.printStackTrace();
        }
        while (true) {
            String expression = JSONataUtils.prompt("Enter jsonata expression (or q to quit):");
            if (expression.length() == 0 || "q".equalsIgnoreCase(expression)) {
                break;
            }
            Expressions expr = null;
            try {
                expr = Expressions.parse(expression);
            } catch (ParseException e) {
                String errMsg = e.getLocalizedMessage();
                int index = errMsg.indexOf("extraneous input '");
                if (index >= 0) {
                    errMsg = errMsg.substring(index + "extraneous input '".length());
                    index = errMsg.indexOf("'");
                    if (index >= 0) {
                        errMsg = errMsg.substring(0, index);
                        System.err.println("Syntax error: \"" + errMsg + "\"");
                        continue;
                    }
                }
                System.err.println(e.getLocalizedMessage());
                continue;
            } catch (EvaluateRuntimeException ere) {
                System.err.println(ere.getLocalizedMessage());
                continue;
            } catch (IOException ioe) {
                System.err.println(ioe.getLocalizedMessage());
            }
            try {
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
        System.out.println("Goodbye");
    }

}
