package com.api.jsonata4java.expressions;

import java.util.regex.Pattern;

public class RegularExpression {

	public enum Type {
		NORMAL, CASEINSENSITIVE, MULTILINE
	};

	private final Type type;

	private String regexPattern;

	private Pattern pattern;

	public RegularExpression(String string) {
		this(Type.NORMAL, string);
	}

	public RegularExpression(final Type type, final String regex) {
		this.type = type;
		switch (type) {
		case CASEINSENSITIVE:
		case MULTILINE:
			regexPattern = regex.substring(1, regex.length() - 2);
			break;
		default:
			regexPattern = regex.substring(1, regex.length() - 1);
			break;
		}
		compile();
	}

	public RegularExpression extend() {
		if (!(regexPattern.startsWith("^") || regexPattern.startsWith("\\A"))) {
			regexPattern = ".*" + regexPattern;
		}
		if (!(regexPattern.endsWith("$") || regexPattern.endsWith("\\z") || regexPattern.endsWith("\\Z"))) {
			regexPattern = regexPattern + ".*";
		}
		compile();
		return this;
	}

	public boolean matches(final String string) {
		return this.pattern.matcher(string).matches();
	}

	private void compile() {
		switch (this.type) {
		case CASEINSENSITIVE:
			this.pattern = Pattern.compile(regexPattern, Pattern.CASE_INSENSITIVE);
			break;
		case MULTILINE:
			this.pattern = Pattern.compile(regexPattern, Pattern.MULTILINE);
			break;
		default:
			this.pattern = Pattern.compile(regexPattern);
			break;
		}
	}

	@Override
	public String toString() {
		return this.pattern.toString();
	}

	public Type getType() {
		return this.type;
	}

	public Pattern getPattern() {
		return this.pattern;
	}
}
