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
package com.api.jsonata4java.testerui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Defines a set of examples that can be used to demonstrate JSONata4Java's capabilities.
 * One example consists in 2 files in folder <b>src/test/resources/exerciser</b>.
 * <ul>
 *   <li><b>File 1:</b> <i>&lt;example name in lower case&gt;</i><b>.json</b> or <b>.xml</b></li>
 *   <li><b>File 2:</b> <i>&lt;example name in lower case&gt;</i><b>.jsonata</b></li>
 * </ul>
 *
 * @author Martin Bluemel
 */
public enum TesterUIJsonataExample {

        NONE("-"),
        // Example "Invoice" from the original JSONata Exerciser
        INVOICE("Invoice"),
        // Example "Address" from the original JSONata Exerciser
        ADDRESS("Address"),
        // Example "Schema" from the original JSONata Exerciser
        SCHEMA("Schema"),
        // Derived from Example "Address" from the Exerciser but with input XML
        XMLADDRESS("XML Address");

    private final String uiName;

    private static final String PATH_EXAMPLES = "src/test/resources/exerciser";

    private TesterUIJsonataExample(final String uiName) {
        this.uiName = uiName;
    }

    public String toString() {
        return uiName;
    }

    public Path getPathInput() {
        final File jsonFile = new File(PATH_EXAMPLES, this.uiName.toLowerCase() + ".json");
        if (jsonFile.exists()) {
            return jsonFile.toPath();
        }
        return new File(PATH_EXAMPLES, this.name().toLowerCase() + ".xml").toPath();
    }

    public Path getPathJsonata() {
        return new File(PATH_EXAMPLES, this.name().toLowerCase() + ".jsonata").toPath();
    }

    public static TesterUIJsonataExample fromContent(String input, String jsonata) throws IOException {
        for (final TesterUIJsonataExample example : TesterUIJsonataExample.values()) {
            if (example == NONE) {
                continue;
            }
            if (TesterUI.readFile(example.getPathInput()).trim().equals(input.trim())
                && TesterUI.readFile(example.getPathJsonata()).trim().equals(jsonata.trim())) {
                return example;
            }
        }
        return TesterUIJsonataExample.NONE;
    }
}
