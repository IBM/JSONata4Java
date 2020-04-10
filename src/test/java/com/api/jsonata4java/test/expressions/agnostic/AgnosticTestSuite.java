package com.api.jsonata4java.test.expressions.agnostic;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;

import com.api.jsonata4java.expressions.EvaluateException;
import com.api.jsonata4java.expressions.Expressions;
import com.api.jsonata4java.expressions.ParseException;
import com.api.jsonata4java.test.expressions.agnostic.AgnosticTestSuite.TestGroup;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RunWith(AgnosticTestSuite.class)
public class AgnosticTestSuite extends ParentRunner<TestGroup> {

	private static final List<String> SKIP_GROUPS = Arrays.asList(new String[] {
//			"wildcards",
//			"function-signatures",
//			"higher-order-functions",
//			"hof-filter",
//			"hof-map",
//			"hof-reduce",
//			"hof-zip-map",
			"lambdas",
//			"function-zip",
			"regex", "function-assert",
//            "function-shuffle",
//			"function-lookup",
//			"function-keys",
//			"function-spread",
//			"function-sort",
//			"function-clone",
//			"function-each",
//			"function-merge",
//			"context",
//			"closures",
			"sorting", // we don't support the order-by operator (^) yet
//			"tail-recursion", 		// tail-recursion requires function definition support, which we don't have yet
//			"function-applications",
//			"partial-application",
//			"transforms",
//			"blocks",
//			"descendent-operator",	// we don't support the ** operator yet
//			"variables" 			// we don't support arbitrary variable bindings yet (we have hard-coded $event, $state, and $instance only)
	});

	private static final ObjectMapper _objectMapper = new ObjectMapper();
	private static final Map<String, List<String>> SKIP_CASES = new HashMap<>();
	static {
		// ensure we don't have scientific notation for numbers
		_objectMapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

		// because we don't support $$
//		SKIP_CASES("conditionals", "case000", "case001", "case002", "case003", "case004", "case005");

		// because we don't support conditionals with no "else" clause (e.g. a?b)
//		SKIP_CASES("conditionals", "case006");

		// cases 3-6 are mis-categories, actually testing $toMillis (not $sift) which is
		// why we don't ignore the whole group (despite not supporting $sift)
//		SKIP_CASES("function-sift", "case000", "case001", "case002");

		SKIP_CASES("predicates", "case003");
		SKIP_CASES("inclusion-operator", "case004", "case005");
		SKIP_CASES("comments", "case003");
		SKIP_CASES("function-length", "case004", "case005", "case016");
		SKIP_CASES("higher-order-functions", "case000", "case001", "case002");
		SKIP_CASES("function-encodeUrlComponent", "case000", "case001", "case002");
		SKIP_CASES("object-constructor", "case008", "case009", "case010", "case011", "case012", "case013", "case014",
				"case015", "case016", "case017", "case018", "case019", "case020", "case022", "case025");
		SKIP_CASES("function-exists", "case006");
		SKIP_CASES("range-operator", "case013");
	}

	private static void SKIP_CASES(String group, String... casesArray) {
		List<String> cases = SKIP_CASES.get(group);
		if (cases == null) {
			cases = new ArrayList<>();
			SKIP_CASES.put(group, cases);
		}
		cases.addAll(Arrays.asList(casesArray));
	}

	private static final boolean SHOW_IMPORT_DETAILS = false; // true to see datasets, groups, etc.
	private static final String SUITE_DIR = "./target/jsonata/jsonata-1.8.2/test/test-suite";
	private static final String DATASETS_DIR = SUITE_DIR + "/datasets";
	private static final String GROUPS_DIR = SUITE_DIR + "/groups";

	protected static final Map<String, JsonNode> DATASETS = new HashMap<>();

	List<TestGroup> groups;

	public AgnosticTestSuite(Class<?> testClass) throws InitializationError {
		super(testClass);

		try {
			init();
		} catch (Exception e) {
			throw new InitializationError(e);
		}

	}

	private void init() throws Exception {
		final ObjectMapper om = new ObjectMapper();

		// load and parse all the dataset json files into memory
		printHeader("Loading datasets");
		final File datasetsDir = new File(DATASETS_DIR);
		final File[] datasetFiles = datasetsDir.listFiles(JsonFileFilter.INSTANCE);
		int filesRead = 0;
		for (File datasetFile : datasetFiles) {
			final String datasetName = datasetFile.getName().substring(0, datasetFile.getName().length() - 5); // - ".json"
			if (SHOW_IMPORT_DETAILS) {
				System.out.println(datasetName);
			}
			final JsonNode datasetJson = om.readTree(datasetFile);
			DATASETS.put(datasetName, datasetJson);
			filesRead++;
		}
		System.out.println("Read " + filesRead + " datasets from " + DATASETS_DIR);
		printFooter();

		printHeader("Loading groups");
		this.groups = new ArrayList<>();
		final File groupsDir = new File(GROUPS_DIR);
		final File[] groupDirs = groupsDir.listFiles(DirectoryFilter.INSTANCE);
		int groupsRead = 0;
		for (File groupDir : groupDirs) {
			final String groupName = groupDir.getName();
			TestGroup group = new TestGroup(groupName);
			this.groups.add(group);
			if (SHOW_IMPORT_DETAILS) {
				System.out.println(groupName);
			}
			groupsRead++;

			// int casesRead = 0;
			final File[] caseFiles = groupDir.listFiles(JsonFileFilter.INSTANCE);
			for (File caseFile : caseFiles) {
				final String caseName = caseFile.getName().substring(0, caseFile.getName().length() - 5); // - ".json"
				if (SHOW_IMPORT_DETAILS) {
					System.out.println("	" + caseName);
				}
				// casesRead++;

				final JsonNode caseJson = om.readTree(caseFile);
				TestCase testCase = null;
				if (caseJson.isObject()) {
					testCase = new TestCase(group, caseName, caseJson);
				} else if (caseJson.isArray()) {
					ArrayNode testCases = (ArrayNode) caseJson;
					for (JsonNode testCaseJson : testCases) {
						testCase = new TestCase(group, caseName, testCaseJson);
					}
				} else {
					throw new RuntimeException("[" + group.getGroupName() + "." + caseName + "] Not a JSON object or array");
				}

				group.addTestCase(testCase);
			}
		}
		System.out.println("Read " + groupsRead + " groups from " + GROUPS_DIR);
		printFooter();
	}

	@Override
	protected List<TestGroup> getChildren() {
		return groups;
	}

	@Override
	protected Description describeChild(TestGroup group) {
		return group.getDescription();
	}

	@Override
	protected void runChild(TestGroup group, RunNotifier notifier) {

		if (!group.shouldSkip()) {
			notifier.fireTestStarted(group.getDescription());
		} else {
			notifier.fireTestIgnored(group.getDescription());
		}

		for (final TestCase testCase : group.getTestCases()) {
			if (testCase.shouldSkip()) {
				notifier.fireTestIgnored(testCase.getDescription());
			} else {

				notifier.fireTestStarted(testCase.getDescription());

				try {
					Expressions e = Expressions.parse(testCase.getExpr());

					// introduce bindings if available
					String key = "";
					ObjectNode bindings = testCase.getBindings();
					for (Iterator<String> it = bindings.fieldNames(); it.hasNext();) {
						key = it.next();
						JsonNode value = bindings.get(key);
						e.getEnvironment().setVariable("$" + key, value);
					}

					JsonNode actualResult = e.evaluate(testCase.getDataset());

					try {
						String expected = _objectMapper.writerWithDefaultPrettyPrinter()
								.writeValueAsString(testCase.expectedResult);
						String actual = _objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(actualResult);
						Assert.assertEquals(expected, actual);
						notifier.fireTestFinished(testCase.getDescription());
					} catch (AssertionError ex) {
						try {
							Assert.assertEquals(testCase.expectedResult, actualResult);
							notifier.fireTestFinished(testCase.getDescription());
						} catch (AssertionError ex1) {
							notifier.fireTestFailure(new Failure(testCase.getDescription(), ex1));							
						}
					}
				} catch (Throwable ex) {

					if (testCase.expectParseOrEvaluateException
							&& (ex instanceof ParseException || ex instanceof EvaluateException)) {
						notifier.fireTestFinished(testCase.getDescription());
					} else {
						notifier.fireTestFailure(new Failure(testCase.getDescription(), ex));
					}
				}
			}

		}
		if (!group.shouldSkip()) {
			notifier.fireTestFinished(group.getDescription());
		}
	}

	private static String HEADER_DELIM = "-";
	private static String HEADER_LINE = StringUtils.repeat(HEADER_DELIM, 10);

	private static void printHeader(String msg) {
		System.out.println(HEADER_LINE);
		System.out.print(HEADER_DELIM);
		System.out.print(' ');
		System.out.println(msg);
		System.out.println(HEADER_LINE);
	}

	private static void printFooter() {
		System.out.println(HEADER_LINE);
		System.out.println();
	}

	static class JsonFileFilter implements FilenameFilter {
		public static final FilenameFilter INSTANCE = new JsonFileFilter();

		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".json");
		}
	}

	static class DirectoryFilter implements FileFilter {

		public static final DirectoryFilter INSTANCE = new DirectoryFilter();

		@Override
		public boolean accept(File file) {
			return file.isDirectory();
		}
	}

	static class TestGroup {
		String groupName;
		Description description;
		List<TestCase> testCases;

		public TestGroup(String groupName) throws Exception {
			this.groupName = groupName;
			this.description = Description.createSuiteDescription(groupName);
			this.testCases = new ArrayList<>();
		}

		public void addTestCase(TestCase testCase) {
			this.testCases.add(testCase);
			this.description.addChild(testCase.getDescription());
		}

		public List<TestCase> getTestCases() {
			return testCases;
		}

		public Description getDescription() {
			return this.description;
		}

		public String getGroupName() {
			return groupName;
		}

		public boolean shouldSkip() {
			return SKIP_GROUPS.contains(this.getGroupName());
		}

	}

	static class TestCase {
		String caseName;
		Description description;

		private String expr;
		private JsonNode dataset;
		private JsonNode expectedResult;
		private ObjectNode bindings = JsonNodeFactory.instance.objectNode();

		private boolean undefinedResult;

		private boolean expectParseOrEvaluateException;
		private TestGroup group;

		public TestCase(TestGroup group, String caseName, JsonNode caseJson) {
			super();
			this.group = group;
			this.caseName = caseName;
			this.description = Description.createTestDescription(group.getGroupName(), caseName);

			if (!caseJson.isObject()) {
				throw new RuntimeException("[" + group.getGroupName() + "." + caseName + "] Not a JSON object");
			}
			ObjectNode o = (ObjectNode) caseJson;

			// expr:
			// The jsonata expression to be evaluated.
			JsonNode expressionNode = o.get("expr");
			if (expressionNode != null) {
				this.expr = expressionNode.asText();

			} else {
				JsonNode expressionFileNode = o.get("expr-file");
				if (expressionFileNode != null) {
					String expressionFileName = expressionFileNode.asText();
					try {
						File expressionFile = new File(GROUPS_DIR + "/" + group.getGroupName() + "/" + expressionFileName);
						this.expr = new String(Files.readAllBytes(expressionFile.getCanonicalFile().toPath()));
					} catch (IOException e) {
						throw new RuntimeException(
								"[" + group.getGroupName() + "." + caseName + "] Unable to read .jsonata file", e);
					}
				} else {
					throw new RuntimeException(
							"[" + group.getGroupName() + "." + caseName + "] No JSONata expression specified for test");
				}
			}

			// data or dataset:
			// If data is defined, use the value of the data field as the input data for the
			// test case.
			// Otherwise, the dataset field contains the name of the dataset (in the
			// datasets directory) to use as input data.
			// If value of the dataset field is null, then use undefined as the input data
			// when evaluating the jsonata expression.
			final JsonNode dataNode = o.get("data");
			final JsonNode datasetNode = o.get("dataset");

			if (dataNode != null) {
				this.dataset = dataNode;
			} else if (datasetNode != null && !datasetNode.isNull()) {
				final String datasetName = o.get("dataset").asText();
				this.dataset = DATASETS.get(datasetName);
				if (this.dataset == null) {
					throw new RuntimeException(
							"[" + group.getGroupName() + "." + caseName + "] Dataset with name " + datasetName + " not found");
				}
			} else {
				this.dataset = null;
			}

			// undefinedResult:
			// A flag indicating the expected result of evaluation will be undefined
			if (o.has("undefinedResult") && o.get("undefinedResult").equals(BooleanNode.TRUE)) {
				this.undefinedResult = true;
			} else {
				this.undefinedResult = false;
			}

			if (!undefinedResult) {
				// result:
				// The expected result of evaluation (if defined)
				this.expectedResult = o.get("result");
				if (this.expectedResult != null && this.expectedResult.isDouble()) {
					double d = this.expectedResult.asDouble();
					if (isWholeNumber(d)) {
						this.expectedResult = new LongNode(this.expectedResult.asLong());
					} else {
						// below produced to high precision and is slower
						//	BigDecimal bd = new BigDecimal(d);
						//	this.expectedResult = new TextNode(bd.toPlainString());
					}
				}
			} else {
				this.expectedResult = null;
			}

			// TODO: code:
			// The code associated with the exception that is expected to be thrown when
			// either compiling the expression or evaluating it
			// for now, we just take this to mean "expect a ParseException or an
			// EvaluateException"
			expectParseOrEvaluateException = o.has("code");

			// TODO: depth:
			// If the depth of evaluation should be limited, this specifies the depth.

			// bindings:
			if (o.has("bindings")) {
				JsonNode bindTest = o.get("bindings");
				if (bindTest.isObject()) {
					ObjectNode bindings = (ObjectNode) bindTest;
					// are there keys to be bound as variables to their content?
					String varID = "";
					for (Iterator<String> it = bindings.fieldNames(); it.hasNext();) {
						varID = it.next();
						this.bindings.set(varID, bindings.get(varID));
					}
				}
			}

			// TODO: timelimit:
			// If a timelimit should be imposed on the test, this specifies the timelimit in
			// milliseconds.

			// TODO: token:
			// If the code field is present, an optional token field may also be present
			// indicating which token token the exception should be associated with.

		}

		private boolean isWholeNumber(double n) {
			return n == Math.rint(n) && !Double.isInfinite(n) && !Double.isNaN(n);
		}

		public ObjectNode getBindings() {
			return bindings;
		}

		public Description getDescription() {
			return description;
		}

		public String getExpr() {
			return expr;
		}

		public JsonNode getDataset() {
			return dataset;
		}

		public JsonNode getExpectedResult() {
			return expectedResult;
		}

		public String getCaseName() {
			return caseName;
		}

		private boolean shouldSkip() {
			return this.group.shouldSkip() || (SKIP_CASES.containsKey(group.getGroupName())
					&& SKIP_CASES.get(group.getGroupName()).contains(this.getCaseName()));
		}

	}

}
