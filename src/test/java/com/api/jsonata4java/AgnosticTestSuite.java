package com.api.jsonata4java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.Assert;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import com.api.jsonata4java.AgnosticTestSuite.TestGroup;
import com.api.jsonata4java.expressions.Base64DecodeFunctionTests;
import com.api.jsonata4java.expressions.Base64EncodeFunctionTests;
import com.api.jsonata4java.expressions.BasicExpressionsTests;
import com.api.jsonata4java.expressions.BooleanFunctionTests;
import com.api.jsonata4java.expressions.CeilFunctionTests;
import com.api.jsonata4java.expressions.ContainsFunctionTests;
import com.api.jsonata4java.expressions.CountFunctionTests;
import com.api.jsonata4java.expressions.DistinctFunctionTests;
import com.api.jsonata4java.expressions.EvaluateException;
import com.api.jsonata4java.expressions.EvaluateRuntimeException;
import com.api.jsonata4java.expressions.Expressions;
import com.api.jsonata4java.expressions.ExpressionsTests;
import com.api.jsonata4java.expressions.FloorFunctionTests;
import com.api.jsonata4java.expressions.FormatBaseFunctionTests;
import com.api.jsonata4java.expressions.FormatNumberFunctionTests;
import com.api.jsonata4java.expressions.FromMillisFunctionTests;
import com.api.jsonata4java.expressions.FromMillisZonedFunctionTests;
import com.api.jsonata4java.expressions.FunctionChainingTests;
import com.api.jsonata4java.expressions.InvalidSyntaxTest;
import com.api.jsonata4java.expressions.JoinFunctionTests;
import com.api.jsonata4java.expressions.JsonataDotOrgTests;
import com.api.jsonata4java.expressions.LengthFunctionTests;
import com.api.jsonata4java.expressions.LowercaseFunctionTests;
import com.api.jsonata4java.expressions.MatchFunctionTests;
import com.api.jsonata4java.expressions.MaxFunctionTests;
import com.api.jsonata4java.expressions.MillisFunctionTests;
import com.api.jsonata4java.expressions.MinFunctionTests;
import com.api.jsonata4java.expressions.NotFunctionTests;
import com.api.jsonata4java.expressions.NowFunctionTests;
import com.api.jsonata4java.expressions.NumberFunctionTests;
import com.api.jsonata4java.expressions.NumericCoercionTests;
import com.api.jsonata4java.expressions.PadFunctionTests;
import com.api.jsonata4java.expressions.ParseException;
import com.api.jsonata4java.expressions.PathExpressionParentTests;
import com.api.jsonata4java.expressions.PowerFunctionTests;
import com.api.jsonata4java.expressions.RandomFunctionTests;
import com.api.jsonata4java.expressions.ReplaceFunctionTests;
import com.api.jsonata4java.expressions.RoundFunctionTests;
import com.api.jsonata4java.expressions.SingletonArrayHandlingTests;
import com.api.jsonata4java.expressions.SortFunctionTests;
import com.api.jsonata4java.expressions.SplitFunctionTests;
import com.api.jsonata4java.expressions.SqrtFunctionTests;
import com.api.jsonata4java.expressions.StringFunctionTests;
import com.api.jsonata4java.expressions.SubstringAfterFunctionTests;
import com.api.jsonata4java.expressions.SubstringBeforeFunctionTests;
import com.api.jsonata4java.expressions.SubstringFunctionTests;
import com.api.jsonata4java.expressions.SumFunctionTests;
import com.api.jsonata4java.expressions.ToMillisFunctionTests;
import com.api.jsonata4java.expressions.TrimFunctionTests;
import com.api.jsonata4java.expressions.UnpackFunctionTests;
import com.api.jsonata4java.expressions.UppercaseFunctionTests;
import com.api.jsonata4java.expressions.path.PathExpression;
import com.api.jsonata4java.expressions.path.PathExpressionSyntaxTests;
import com.api.jsonata4java.expressions.path.PathExpressionTests;
import com.api.jsonata4java.expressions.utils.JsonMergeUtils;
import com.api.jsonata4java.expressions.utils.JsonMergeUtilsTest;
import com.api.jsonata4java.expressions.utils.Utils;
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
// @ExtendWith(AgnosticTestSuiteExtension.class)
public class AgnosticTestSuite extends ParentRunner<TestGroup> {

    private static final List<String> SKIP_GROUPS = Arrays.asList(new String[] {
        "transform", // issue #47
        "transforms", // issue #47
        "function-formatNumber", // issue #49
        "function-tomillis", // issue #52
        "partial-application", // issue #53
        "closures", // issue #56
        "matchers", // issue #57
        "hof-zip-map", // issue #58
        "higher-order-functions", // issue #70
        "function-assert", // issue #72
        "function-eval", // issue #73
        "sorting", // issue #74
        "hof-single", // issue #76
        "tail-recursion", // tail-recursion requires lambda issue #70
        "function-signatures", // #77
    });

    private static final ObjectMapper _objectMapper = new ObjectMapper();
    private static final Map<String, List<String>> SKIP_CASES = new HashMap<>();
    static {
        // address characters > 127 to be escaped
        _objectMapper.getFactory().configure(JsonWriteFeature.ESCAPE_NON_ASCII.mappedFeature(), true);
        // ensure we don't have scientific notation for numbers
        _objectMapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

        // due to unparsable use of 'in' in the tests
        SKIP_CASES("inclusion-operator", "case004", "case005");
        // issue #43 object construction
        SKIP_CASES("object-constructor", "case013", "case017", "case022",
            /* issue #56 closures*/ "case023");
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
        // issue #60 parent-operator
        SKIP_CASES("parent-operator", "parent-27");
        // issue 71 support regular expressions
        SKIP_CASES("regex",
            "case015",
            //   Referencing undefined capturing groups in the replacement is not allowed in Java:
            //   Java replace() does not allow to use capturing group numbers in the replacement
            //   you have not defined in the regular expression pattern...
            //   Don't know any sensible use of the JSONata behavior simply not to expand such capturing groups references in the replacement.
            //   For me this is a programming error and thus the Java behavior is OK.
            //

            "case022",
            //   Exotic error case throws an error in Java too but another one.
            //

            "case028", "case029",
            //   JSONATA evaluates only the first digit after $ as group number.
            //   $replace("abcdefghijklmno", /ijk/, "$8$5$12$12$18$123") => abcdefgh22823lmno
            //   Java regex allows higher numbers than 9 for groups
            //

            "case031",
            //   JSONata evaluates $replace("abcdefghijklmno", /(ijk)/, "$x$") to "abcdefgh$x$lmno"
            //   We could argue if this is a bug or a feature
            //   but since replacement "$x$$" leads to the same result we might all agree that supporting $x$$ is sufficient.
            //

            "case032", "case033",
            // JSONAata expression
            // Account.Order.Product.$replace($."Product Name", /hat/i, function($match) { "foo" })
            // running against the "invoice" example of the JSONata Exerciser - works fine there
            // In JSONata4Java the arguments for the $replace() function get mixed up
            // argCount = 4
            // arg 1 (string): {"Product Name":"Bowler Hat"}
            // arg 2 (pattern): "Bowler Hat"
            // arg 3 (replacement): {"type":"CASEINSENSITIVE","pattern":"hat"}
            // Since the same problem occurs also if the 2nd $replace() arg is not a regular expression
            // I suppose that this problem also existed before the changes for regular expression support.

            "case034",
            // This is a heavy expression.
            // To be honest I do not know the usage at all.
            // If it is meant to convert degrees of F into C it definitely does not work.
            // But basically the processing has the same problem that 33

            "case037"
        // JSONata expression: $map($, $match(?, /^(\w*\s\w*)/)).match
        // This one seems to be a general problem with JSONata4Java's parser.
        // It has problems to parse the first match argument ?.
        // I suppose that this problem also existed before the changes for regular expression support.
        );
        // issue #55 and / or stand alone to get by parser
        SKIP_CASES("boolean-expresssions", "case012", "case013", "case014", "case015");
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

    // added for issue 203
    private static String readUtf8TextFile(Path path) {
        StringBuilder sb = new StringBuilder();
        try (Stream<String> stream = Files.lines(path, StandardCharsets.UTF_8)) {
            stream.forEach(s -> sb.append(s).append("\n"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    private void init() throws Exception {
        final ObjectMapper om = new ObjectMapper();
        om.getFactory().configure(JsonWriteFeature.ESCAPE_NON_ASCII.mappedFeature(), true);

        // load and parse all the dataset json files into memory
        System.out.println("Loading datasets");
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

        System.out.println("Loading groups");
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

        runComponentTest(Base64DecodeFunctionTests.data());
        runComponentTest(Base64EncodeFunctionTests.data());
        runComponentTest(BooleanFunctionTests.data());
        runComponentTest(CeilFunctionTests.data());
        runComponentTest(ContainsFunctionTests.data());
        runComponentTest(CountFunctionTests.data());
        runComponentTest(DistinctFunctionTests.data());
        runComponentTest(ExpressionsTests.data());
        runComponentTest(FloorFunctionTests.data());
        runComponentTest(FormatBaseFunctionTests.data());
        runComponentTest(FormatNumberFunctionTests.data());
        runComponentTest(FromMillisFunctionTests.data());
        runComponentTest(FromMillisZonedFunctionTests.data());
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
        runComponentTest(SortFunctionTests.data());
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

        runJunitTests(new BasicExpressionsTests());
        
        runJunitTests(new FunctionChainingTests());
        
        runJunitTests(new PathExpressionParentTests());
        
        runJunitTests(new JsonataTimeoutTest());
    }

    private void runJunitTests(final Object testCase) {
        for (final Method method : testCase.getClass().getMethods()) {
            if (method.getAnnotationsByType(org.junit.Test.class).length != 1) {
                continue;
            }
            try {
                method.invoke(testCase);
            } catch (InvocationTargetException e) {
                if (((InvocationTargetException) e).getTargetException() instanceof AssertionError) {
                    throw (AssertionError) ((InvocationTargetException) e).getTargetException();
                } else if (((InvocationTargetException) e).getTargetException() instanceof RuntimeException) {
                    throw (RuntimeException) ((InvocationTargetException) e).getTargetException();
                } else {
                    throw new RuntimeException(e);
                }
            } catch (IllegalAccessException | IllegalArgumentException e) {
                throw new RuntimeException(e);
            }
        }
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
            if (test.length == 3) {
                Utils.test(test[0] == null ? null : test[0].toString(), test[1] == null ? null : test[1].toString(),
                    test[2] == null ? null : test[2].toString(), null);
            } else if (test.length == 4) {
                Utils.test(test[0] == null ? null : test[0].toString(), test[1] == null ? null : test[1].toString(),
                    test[2] == null ? null : test[2].toString(), test[3] == null ? null : test[3].toString());
            } else {
                System.out.println("Received an empty test: " + test);
                System.out.println("for data: " + data);
            }
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

        if (group.shouldSkip()) {
            notifier.fireTestIgnored(group.getDescription());
            return;
        }

        notifier.fireTestStarted(group.getDescription());

        for (final TestCase testCase : group.getTestCases()) {
            if (testCase.shouldSkip()) {
                notifier.fireTestIgnored(testCase.getDescription());
            } else {
                Description testDesc = testCase.getDescription();
                try {
                    if (testDesc.isEmpty() == false) {
                        notifier.fireTestStarted(testDesc);
                    } else {
                        System.err.println(testDesc + " is empty");
                    }
                } catch (Exception sbfe) {
                    System.err.println("FAILURE in AgnosticTestSuite test case: " + group.getGroupName() + "." + testCase.getCaseName()
                        + ": " + testDesc + " Error: " + sbfe.getLocalizedMessage());
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
                        Assert.assertEquals("FAILURE in AgnosticTestSuite test case: " + group.getGroupName() + "." + testCase.getCaseName() + "\n",
                            testCase.expectedResult, actualResult);
                        notifier.fireTestFinished(testCase.getDescription());
                    } catch (AssertionError ex) {
                        try {
                            Assert.assertEquals("FAILURE in AgnosticTestSuite test case: " + group.getGroupName() + "." + testCase.getCaseName() + "\n",
                                _objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(testCase.expectedResult),
                                _objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(actualResult));
                            notifier.fireTestFinished(testCase.getDescription());
                        } catch (AssertionError ex1) {
                            System.err.println("FAILURE in AgnosticTestSuite test case: " + group.getGroupName() + "." + testCase.getCaseName());
                            notifier.fireTestFailure(new Failure(testCase.getDescription(), ex1));
                        }
                    }
                } catch (Throwable ex) {
                    if (testCase.expectParseOrEvaluateException
                        && (ex instanceof ParseException || ex instanceof EvaluateException)) {
                        notifier.fireTestFinished(testCase.getDescription());
                    } else {
                        System.err.println("FAILURE in AgnosticTestSuite test case: " + group.getGroupName() + "." + testCase.getCaseName());
                        notifier.fireTestFailure(new Failure(testCase.getDescription(), ex));
                    }
                }
            }
        }

        notifier.fireTestFinished(group.getDescription());
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
                        // below issue #203
                        this.expr = readUtf8TextFile(expressionFile.getCanonicalFile().toPath());
                        // this.expr = new
                        // String(Files.readAllBytes(expressionFile.getCanonicalFile().toPath()));
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
            // indicating which token the exception should be associated with.
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
