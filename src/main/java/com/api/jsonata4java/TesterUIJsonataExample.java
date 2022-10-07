package com.api.jsonata4java;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public enum TesterUIJsonataExample {
	NONE("-"),
	INVOICE("Invoice"),
	ADDRESS("Address"),
	SCHEMA("Schema");

	private final String uiName;

	private static final String PATH_EXAMPLES = "src/test/resources/exerciser";

	private TesterUIJsonataExample(final String uiName) {
		this.uiName = uiName;
	}

	public String toString() {
		return uiName;
	}

	public Path getPathInput() {
		return new File(PATH_EXAMPLES, this.uiName.toLowerCase() + ".json").toPath();
	}

	public Path getPathJsonata() {
		return new File(PATH_EXAMPLES, this.uiName.toLowerCase() + ".jsonata").toPath();
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
