package com.api.jsonata4java.test.expressions.agnostic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import com.api.jsonata4java.expressions.EvaluateRuntimeException;
import com.api.jsonata4java.expressions.Expressions;
import com.api.jsonata4java.expressions.ParseException;
import com.api.jsonata4java.expressions.path.PathExpression;
import com.api.jsonata4java.expressions.utils.JsonMergeUtils;
import com.api.jsonata4java.test.expressions.AbsFunctionTests;
import com.api.jsonata4java.test.expressions.Base64DecodeFunctionTests;
import com.api.jsonata4java.test.expressions.Base64EncodeFunctionTests;
import com.api.jsonata4java.test.expressions.BasicExpressionsTest;
import com.api.jsonata4java.test.expressions.BooleanFunctionTests;
import com.api.jsonata4java.test.expressions.CeilFunctionTests;
import com.api.jsonata4java.test.expressions.ContainsFunctionTests;
import com.api.jsonata4java.test.expressions.CountFunctionTests;
import com.api.jsonata4java.test.expressions.ExpressionsTests;
import com.api.jsonata4java.test.expressions.FloorFunctionTests;
import com.api.jsonata4java.test.expressions.FormatBaseFunctionTests;
import com.api.jsonata4java.test.expressions.FormatNumberFunctionTests;
import com.api.jsonata4java.test.expressions.FromMillisFunctionTests;
import com.api.jsonata4java.test.expressions.InvalidSyntaxTest;
import com.api.jsonata4java.test.expressions.JoinFunctionTests;
import com.api.jsonata4java.test.expressions.JsonataDotOrgTests;
import com.api.jsonata4java.test.expressions.LengthFunctionTests;
import com.api.jsonata4java.test.expressions.LowercaseFunctionTests;
import com.api.jsonata4java.test.expressions.MatchFunctionTests;
import com.api.jsonata4java.test.expressions.MaxFunctionTests;
import com.api.jsonata4java.test.expressions.MillisFunctionTests;
import com.api.jsonata4java.test.expressions.MinFunctionTests;
import com.api.jsonata4java.test.expressions.NotFunctionTests;
import com.api.jsonata4java.test.expressions.NowFunctionTests;
import com.api.jsonata4java.test.expressions.NumberFunctionTests;
import com.api.jsonata4java.test.expressions.NumericCoercionTests;
import com.api.jsonata4java.test.expressions.PadFunctionTests;
import com.api.jsonata4java.test.expressions.PowerFunctionTests;
import com.api.jsonata4java.test.expressions.RandomFunctionTests;
import com.api.jsonata4java.test.expressions.ReplaceFunctionTests;
import com.api.jsonata4java.test.expressions.RoundFunctionTests;
import com.api.jsonata4java.test.expressions.SingletonArrayHandlingTests;
import com.api.jsonata4java.test.expressions.SplitFunctionTests;
import com.api.jsonata4java.test.expressions.SqrtFunctionTests;
import com.api.jsonata4java.test.expressions.StringFunctionTests;
import com.api.jsonata4java.test.expressions.SubstringAfterFunctionTests;
import com.api.jsonata4java.test.expressions.SubstringBeforeFunctionTests;
import com.api.jsonata4java.test.expressions.SubstringFunctionTests;
import com.api.jsonata4java.test.expressions.SumFunctionTests;
import com.api.jsonata4java.test.expressions.ToMillisFunctionTests;
import com.api.jsonata4java.test.expressions.TrimFunctionTests;
import com.api.jsonata4java.test.expressions.UnpackFunctionTests;
import com.api.jsonata4java.test.expressions.UppercaseFunctionTests;
import com.api.jsonata4java.test.expressions.agnostic.AgnosticTestSuite.TestGroup;
import com.api.jsonata4java.test.expressions.path.PathExpressionSyntaxTests;
import com.api.jsonata4java.test.expressions.path.PathExpressionTests;
import com.api.jsonata4java.text.expressions.utils.JsonMergeUtilsTest;
import com.api.jsonata4java.text.expressions.utils.Utils;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
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

	private static final List<String> SKIP_GROUPS = Arrays.asList(new String[] { "transform", // issue #47
			"transforms", // issue #47
			"function-formatNumber", // issue #49
			"function-tomillis", // issue #52
			"partial-application", // issue #53
			"closures", // issue #56
			"matchers", // issue #57
			"hof-zip-map", // issue #58
			"parent-operator", // issue #60
//			"function-distinct", // issue #63
//			"lambdas", // issue #70
			"higher-order-functions", // issue #70
			"regex", // issue #71
			"function-assert", // issue #72
			"function-eval", // issue #73
			"sorting", // issue #74
			"hof-single", // issue #76
			"tail-recursion", // tail-recursion requires lambda issue #70
			"function-signatures", // #77
//			"flattening" // #78
	});

	private static final ObjectMapper _objectMapper = new ObjectMapper();
	private static final Map<String, List<String>> SKIP_CASES = new HashMap<>();
	static {
	   // address characters > 127 to be escaped
	   _objectMapper.getFactory().configure(JsonWriteFeature.ESCAPE_NON_ASCII.mappedFeature(),true);
		// ensure we don't have scientific notation for numbers
		_objectMapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

		// due to unparsable use of 'in' in the tests
		SKIP_CASES("inclusion-operator", "case004", "case005");
		// issue #43 object construction
		SKIP_CASES("object-constructor", /* "case008", "case009", "case010", */ 
				"case011", 
				/* "case012", */
				"case013", 
				/* "case014", "case015", "case016", */
				"case017", 
				/* "case018", "case019", "case020", */
				"case022", "case025");
		SKIP_CASES("flattening", "case040", "case041");
		// issue #48 @ references
		SKIP_CASES("joins", "employee-map-reduce-11");
		// issue #48 @ references
		SKIP_CASES("joins", "library-joins-10");
		// issue #50 # references
		SKIP_CASES("joins", "index-15");
		// issue #52
		SKIP_CASES("function-fromMillis", "isoWeekDate-18");
		// issue #53
		SKIP_CASES("function-application", "case016");
		// issue #53
		SKIP_CASES("function-applications", "case012", "case016", "case021");
		// issue #53
		SKIP_CASES("hof-map", "case008");
		// issue #53
		SKIP_CASES("hof-reduce", "case010");
		// issue #54 timeouts
		SKIP_CASES("range-operator", "case021", "case024");
		// issue #55 and / or stand alone to get by parser
		SKIP_CASES("boolean-expresssions", "case012", "case013", "case014", "case015");
		// issue #56 closures
		SKIP_CASES("object-constructor", "case023");
		// issue $59
		SKIP_CASES("errors", "case012", "case013", "case014", "case015", "case018", "case020", "case022", "case023");
		// have right answer, but AgnosticTestSuite not comparing correctly
		SKIP_CASES("literals", "case006");
		// issue #61 variable to function chain
		SKIP_CASES("function-applications", "case005", "case009", "case015");
		// issue 70
		SKIP_CASES("function-typeOf", "case011");
		// issue 70
		SKIP_CASES("function-sift", "case000", "case001", "case002", "case004");
		// issue 70
		SKIP_CASES("function-sort", "case010");
		// issue 70
		SKIP_CASES("function-each", "case000");
		// issue 70
		SKIP_CASES("hof-filter", "case000", "case001");
		// issue 70
		SKIP_CASES("function-applications", "case013", "case014", "case017", "case018", "case019");
		// issue 70
		SKIP_CASES("hof-map", "case003", "case004");
		// issue 70
		SKIP_CASES("lambdas", "case004", "case009", "case010", "case012");
		// issue #78
		SKIP_CASES("predicates", "case003");
		// issues #79 and 70
		// issues #80 flattening
		SKIP_CASES("boolean-expresssions", "case016");
		// issue #95
		SKIP_CASES("encoding", "case001", "case003");
		// issue #113
		SKIP_CASES("flattening", "case034", "case037", "case038", "case042", "case043", "case044", "case045",
				"sequence-of-arrays-3");
		// issue #114
		SKIP_CASES("hof-reduce", "case001");
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
	private static final String SUITE_DIR = "./target/jsonata/jsonata-1.8.4/test/test-suite"; // "./target/jsonata/jsonata-1.8.3/test/test-suite";
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
		om.getFactory().configure(JsonWriteFeature.ESCAPE_NON_ASCII.mappedFeature(), true);
		

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
			FileInputStream stream = new FileInputStream(datasetFile);
			BufferedReader in = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			final JsonNode datasetJson = om.readTree(in);
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
				FileInputStream stream = new FileInputStream(caseFile);
				BufferedReader in = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
				final JsonNode caseJson = om.readTree(in);
				TestCase testCase = null;
				if (caseJson.isObject()) {
					testCase = new TestCase(group, caseName, caseJson);
				} else if (caseJson.isArray()) {
					int subTestIndex = 0;
					ArrayNode testCases = (ArrayNode) caseJson;
					for (JsonNode testCaseJson : testCases) {
						testCase = new TestCase(group, caseName + "-" + (subTestIndex++), testCaseJson);
					}
				} else {
					throw new RuntimeException("[" + group.getGroupName() + "." + caseName + "] Not a JSON object or array");
				}

				group.addTestCase(testCase);
			}
		}
		System.out.println("Read " + groupsRead + " groups from " + GROUPS_DIR);
		printFooter();

		runComponentTest(AbsFunctionTests.data());
		runComponentTest(Base64DecodeFunctionTests.data());
		runComponentTest(Base64EncodeFunctionTests.data());
		// runComponentTest(BasicExpressionsTest.data());
		runComponentTest(BooleanFunctionTests.data());
		runComponentTest(CeilFunctionTests.data());
		runComponentTest(ContainsFunctionTests.data());
		runComponentTest(CountFunctionTests.data());
		runComponentTest(ExpressionsTests.data());
		runComponentTest(FloorFunctionTests.data());
		runComponentTest(FormatBaseFunctionTests.data());
		runComponentTest(FormatNumberFunctionTests.data());
		runComponentTest(FromMillisFunctionTests.data());
		runComponentTest(JoinFunctionTests.data());
		runComponentTest(LengthFunctionTests.data());
		runComponentTest(LowercaseFunctionTests.data());
		runComponentTest(MatchFunctionTests.data());
		runComponentTest(MaxFunctionTests.data());
		runComponentTest(MillisFunctionTests.data());
		runComponentTest(MinFunctionTests.data());
		runComponentTest(NotFunctionTests.data());
		runComponentTest(NowFunctionTests.data());
		runComponentTest(NumberFunctionTests.data());
		runComponentTest(NumericCoercionTests.data());
		runComponentTest(PadFunctionTests.data());
		runComponentTest(PowerFunctionTests.data());
		runComponentTest(RandomFunctionTests.data());
		runComponentTest(ReplaceFunctionTests.data());
		runComponentTest(RoundFunctionTests.data());
		// issue 80
		runComponentTest(SingletonArrayHandlingTests.data());
		runComponentTest(SplitFunctionTests.data());
		runComponentTest(SqrtFunctionTests.data());
		runComponentTest(StringFunctionTests.data());
		runComponentTest(SubstringAfterFunctionTests.data());
		runComponentTest(SubstringBeforeFunctionTests.data());
		runComponentTest(SubstringFunctionTests.data());
		runComponentTest(SumFunctionTests.data());
		runComponentTest(ToMillisFunctionTests.data());
		runComponentTest(TrimFunctionTests.data());
		runComponentTest(UppercaseFunctionTests.data());

		runUnpackTest(UnpackFunctionTests.data());

		runExecutionTest(InvalidSyntaxTest.data());

		runPathExpressionSyntaxTest(PathExpressionSyntaxTests.data());

		runPathExpressionTest(PathExpressionTests.data());

		runJsonCompareTest(JsonMergeUtilsTest.data());

		runJsonataTest(JsonataDotOrgTests.data());

		BasicExpressionsTest bet = new BasicExpressionsTest();
		bet.testNewStuff();
		bet.testNullNode();
		bet.testCustomerScenario();
		bet.testArrayToString();
		bet.testStrings();
		bet.testLiterals();
		bet.testNegation();
		bet.testArithmeticOperators();
		bet.testStringOperators();
		bet.eventAccessNoQuotes();
		bet.eventAccessWithQuotes();
		bet.appAccessNoQuotes();
		bet.appAccessWithQuotes();
		bet.testArrays();
		bet.testVariableAssignment();
		bet.testFunctionDecl();
		bet.testObjectFunctions();
		bet.testArrayFlattening();
		bet.exceptions();
		bet.testBooleanOperators();
		bet.testCompOperators();
		bet.testConditionalLazyEval();
		bet.testNullEquality();
		bet.testNullComparison();
		bet.testExistsFunction();
		bet.testArrayConstructor();
		bet.testArraySeqConstructor();
		bet.testAppendFunction();
		bet.testCountFunction();
		bet.testObjectConstructors();
		bet.testSumFunction();
		bet.testAverageFunction();
		bet.testBasicSelection();
		bet.testComplexSelection();

	}

	protected void runPathExpressionTest(Collection<Object[]> data) throws Exception {
		for (Object[] test : data) {

			JsonNode inputJson = test[1] == null ? null
					: test[1].toString() == null ? null
							: (new ObjectMapper()).readTree(test[1] == null ? null : test[1].toString());
			JsonNode expectedOutputJson = test[5] == null ? null
					: test[5].toString() == null ? null
							: (new ObjectMapper()).readTree(test[5] == null ? null : test[5].toString());
			JsonNode valueToAssignJson = test[4] == null ? null
					: test[4].toString() == null ? null
							: (new ObjectMapper()).readTree(test[4] == null ? null : test[4].toString());

			PathExpression e = PathExpression.parse(test[0] == null ? null : test[0].toString());

			JsonNode setterReturn = null;
			// test the setter
			try {
				setterReturn = e.set(inputJson, test[2] == null ? null : (Integer) test[2], valueToAssignJson);
				if ((test[6] == null ? null : test[6].toString()) != null) {
					Assert.fail("set() - Expected runtime exception with message '" + test[6] == null ? null
							: test[6].toString() + "' was not thrown");
				}
				Assert.assertEquals(expectedOutputJson, inputJson);
			} catch (EvaluateRuntimeException ex) {
				if (test[6] == null ? null : test[6].toString() == null) {
					throw ex;
				} else {
					Assert.assertEquals(test[6] == null ? null : test[6].toString(), ex.getMessage());
				}
			}

			// NOTE: setterReturn might be different to valueToAssign due to type coercion

			// test the getter
			try {
				JsonNode getterReturn = e.get(inputJson, test[2] == null ? null : (Integer) test[2]);
				if ((test[6] == null ? null : test[6].toString()) != null) {
					Assert.fail("get() - Expected runtime exception with message '" + test[6] == null ? null
							: test[6].toString() + "' was not thrown");
				}
				Assert.assertEquals(expectedOutputJson, inputJson);

				// the output of the getter should be equal to the (possibly coerced) value
				// assigned by the setter
				Assert.assertEquals(setterReturn, getterReturn);
			} catch (EvaluateRuntimeException ex) {
				if (test[6] == null ? null : test[6].toString() == null) {
					throw ex;
				} else {
					Assert.assertEquals(test[6] == null ? null : test[6].toString(), ex.getMessage());
				}
			}
		}

	}

	protected void runUnpackTest(Collection<Object[]> data) throws Exception {
		for (Object[] test : data) {
			Utils.test(test[0] == null ? null : test[0].toString(), test[1] == null ? null : (JsonNode) test[1],
					test[2] == null ? null : test[2].toString(), null);
		}
	}

	protected void runJsonCompareTest(Collection<Object[]> data) throws Exception {
		for (Object[] test : data) {
			final JsonNode actual = JsonMergeUtils.merge(test[0] == null ? null : (JsonNode) test[0],
					test[1] == null ? null : (JsonNode) test[1]);
			Assert.assertEquals(test[2] == null ? null : (JsonNode) test[2], actual);

		}
	}

	protected void runPathExpressionSyntaxTest(Collection<Object[]> data) throws Exception {
		for (Object[] test : data) {
			try {
				PathExpression.parse(test[0] == null ? null : test[0].toString());
				if (!(test[1] == null ? null : (Boolean) test[1])) {
					Assert.fail(
							"Expected a ParseException to be thrown when attempting to parse expression '" + test[0] == null
									? null
									: test[0].toString() + "', but it was not");
				}
			} catch (ParseException ex) {
				if ((test[1] == null ? null : (Boolean) test[1])) {
					Assert.fail("Expected expression '" + test[0] == null ? null
							: test[0].toString() + "' to parse successfully, but it did not");
				}
			}
		}
	}

	protected void runExecutionTest(Collection<Object[]> data) throws Exception {
		for (Object[] test : data) {
			try {
				System.err.println("* " + test[0] == null ? null : test[0].toString());
				Expressions.parse(test[0] == null ? null : test[0].toString());
				if (!(test[1] == null ? null : (Boolean) test[1])) {
					Assert.fail(
							"Expected a ParseException to be thrown when attempting to parse expression '" + test[0] == null
									? null
									: test[0].toString() + "', but it was not");
				}
			} catch (ParseException ex) {
				if ((test[1] == null ? null : (Boolean) test[1])) {
					ex.printStackTrace();
					Assert.fail("Expected expression '" + test[0] == null ? null
							: test[0].toString() + "' to parse successfully, but it did not");
				}
			}
		}
	}

	protected void runComponentTest(Collection<Object[]> data) throws Exception {
		for (Object[] test : data) {
			Utils.test(test[0] == null ? null : test[0].toString(), test[1] == null ? null : test[1].toString(),
					test[2] == null ? null : test[2].toString(), null);
		}
	}

	protected void runJsonataTest(Collection<Object[]> data) throws Exception {
		for (Object[] test : data) {
			Utils.test(test[1] == null ? null : test[1].toString(), test[2] == null ? null : test[2].toString(), null,
					test[0] == null ? null : test[0].toString());
		}
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

				Description testDesc = testCase.getDescription();
				try {
					if (testDesc.isEmpty() == false) {
						notifier.fireTestStarted(testDesc);
					} else {
						System.out.println(testDesc + " is empty");
					}
				} catch (Exception sbfe) {
					System.out.println("Error in " + testDesc + " Error: " + sbfe.getLocalizedMessage());
				}

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
				setExpr(expressionNode.asText());

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
						// BigDecimal bd = new BigDecimal(d);
						// this.expectedResult = new TextNode(bd.toPlainString());
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
			if (!expectParseOrEvaluateException) {
				if (o.has("error")) {
					ObjectNode error = (ObjectNode) o.get("error");
					expectParseOrEvaluateException = error.has("code");
				}
			}

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

		public void setExpr(String expr) {
			StringBuilder sb = new StringBuilder();
			for (char c : expr.toCharArray()) {
				if (c > 0x00FF) {
					String hexChars = Integer.toHexString(c).toUpperCase();
					hexChars = "0000".substring(0, 4 - hexChars.length()) + hexChars;
					String unicode = "\\u" + hexChars;
					sb.append(unicode);
				} else {
					sb.append((char) c);
				}
			}
			this.expr = sb.toString();
		}

		private boolean shouldSkip() {
			return this.group.shouldSkip() || (SKIP_CASES.containsKey(group.getGroupName())
					&& SKIP_CASES.get(group.getGroupName()).contains(this.getCaseName()));
		}

	}

}
