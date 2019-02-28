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

package com.api.jsonata4java.test.expressions.agnostic;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
import com.api.jsonata4java.text.expressions.utils.Utils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.ObjectNode;



@RunWith(AgnosticTestSuite.class)
public class AgnosticTestSuite extends ParentRunner<TestGroup>{
	
	private static final List<String> SKIP_GROUPS = Arrays.asList(new String[] {
			"wildcards",
			"function-signatures",
			"higher-order-functions",
			"hof-filter",
			"hof-map",
			"hof-reduce",
			"hof-zip-map",
			"lambdas",
			"function-zip",
			"regex",
			"function-shuffle",
			"function-lookup",
			"function-keys",
			"function-spread",
			"function-sort",
			"function-clone",
			"function-each",
			"function-merge",
			"context",
			"closures",
			"sorting", 				// we don't support the order-by operator (^) yet
			"tail-recursion", 		// tail-recursion requires function definition support, which we don't have yet
			"function-applications",
			"partial-application",
			"transforms",
			"blocks",
			"descendent-operator",	// we don't support the ** operator yet
			"variables" 			// we don't support arbitrary variable bindings yet (we have hard-coded $event, $state, and $instance only)
	});
	
	private static final Map<String, List<String>> SKIP_CASES = new HashMap<>();
	static{
		
		// because we don't support $$
		SKIP_CASES("conditionals", "case000", "case001", "case002", "case003", "case004", "case005");
		
		// because we don't support conditionals with no "else" clause (e.g. a?b)
		SKIP_CASES("conditionals", "case006");
		
		// cases 3-6 are mis-categories, actually testing $toMillis (not $sift) which is why we don't ignore the whole group (despite not supporting $sift)
		SKIP_CASES("function-sift", "case000", "case001", "case002");
		
	}
	private static void SKIP_CASES(String group, String... casesArray){
		List<String> cases = SKIP_CASES.get(group);
		if(cases == null){
			cases = new ArrayList<>();
			SKIP_CASES.put(group, cases);
		}
		cases.addAll(Arrays.asList(casesArray));
	}

	
	private static final String SUITE_DIR = "./target/jsonata/jsonata-1.5/test/test-suite";
	private static final String DATASETS_DIR = SUITE_DIR+"/datasets";
	private static final String GROUPS_DIR = SUITE_DIR+"/groups";
	
	
	protected static final Map<String,JsonNode> DATASETS = new HashMap<>();
	
	
	List<TestGroup> groups;

	public AgnosticTestSuite(Class<?> testClass) throws InitializationError {
		super(testClass);
		
		try {
			init();
		} catch (Exception e) {
			throw new InitializationError(e);
		}

	}
	
	private void init() throws Exception{
		final ObjectMapper om = new ObjectMapper();
		
		
		// load and parse all the dataset json files into memory
		printHeader("Loading datasets");
		final File datasetsDir = new File(DATASETS_DIR);
		final File[] datasetFiles = datasetsDir.listFiles(JsonFileFilter.INSTANCE);
		for(File datasetFile : datasetFiles){
			final String datasetName = datasetFile.getName().substring(0, datasetFile.getName().length()-5); // - ".json"
			System.out.println(datasetName);
			final JsonNode datasetJson = om.readTree(datasetFile);
			DATASETS.put(datasetName, datasetJson);
			
		}
		printFooter();
		
		printHeader("Loading groups");
		this.groups = new ArrayList<>();
		final File groupsDir = new File(GROUPS_DIR);
		final File[] groupDirs = groupsDir.listFiles(DirectoryFilter.INSTANCE);
		for(File groupDir : groupDirs){
			final String groupName = groupDir.getName();
			TestGroup group = new TestGroup(groupName);
			this.groups.add(group);
			System.out.println(groupName);
			
			final File[] caseFiles = groupDir.listFiles(JsonFileFilter.INSTANCE);
			for(File caseFile : caseFiles){
				final String caseName = caseFile.getName().substring(0, caseFile.getName().length()-5); // - ".json"
				System.out.println("	"+caseName);
				
				final JsonNode caseJson = om.readTree(caseFile);
				TestCase testCase = new TestCase(group, caseName, caseJson);
				group.addTestCase(testCase);
			}
		}
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
		
		if(!group.shouldSkip()){
			notifier.fireTestStarted(group.getDescription());
		}else{
			notifier.fireTestIgnored(group.getDescription());
		}
		

		
		for(final TestCase testCase : group.getTestCases()){
			if(testCase.shouldSkip()){
				notifier.fireTestIgnored(testCase.getDescription());
			}else{
			
				notifier.fireTestStarted(testCase.getDescription());
			
				try {
					Expressions e = Expressions.parse(testCase.getExpr());
					
					JsonNode actualResult = e.evaluate(testCase.getDataset());
					
					if(actualResult != null){
						actualResult = Utils.ensureAllIntegralsAreLongs(actualResult);
					}
					
					try{
						Assert.assertEquals(testCase.expectedResult, actualResult);
						notifier.fireTestFinished(testCase.getDescription());
					}catch(AssertionError ex){
						notifier.fireTestFailure(new Failure(testCase.getDescription(), ex));
					}
					
					
					
				} catch (Throwable ex) {
					
					if(testCase.expectParseOrEvaluateException && (ex instanceof ParseException || ex instanceof EvaluateException)){
						notifier.fireTestFinished(testCase.getDescription());
					}else{
						notifier.fireTestFailure(new Failure(testCase.getDescription(), ex));
					}
				}
			}
			
			
			
		}
		if(!group.shouldSkip()){
			notifier.fireTestFinished(group.getDescription());
		}
	}

	
	
	private static String HEADER_DELIM = "-";
	private static String HEADER_LINE = StringUtils.repeat(HEADER_DELIM, 10);
	
	private static void printHeader(String msg){
		System.out.println(HEADER_LINE);
		System.out.print(HEADER_DELIM);
		System.out.print(' ');
		System.out.println(msg);
		System.out.println(HEADER_LINE);
	}
	
	private static void printFooter(){
		System.out.println(HEADER_LINE);
		System.out.println();
	}
	
	static class JsonFileFilter implements FilenameFilter{
		public static final FilenameFilter INSTANCE = new JsonFileFilter();
		
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".json");
		}
	}
	
	static class DirectoryFilter implements FileFilter{
		
		public static final DirectoryFilter INSTANCE = new DirectoryFilter();

		@Override
		public boolean accept(File file) {
			return file.isDirectory();
		}
	}
	
	
	static class TestGroup{
		String groupName;
		Description description;
		List<TestCase> testCases;
		
		public TestGroup(String groupName) throws Exception{
			this.groupName = groupName;
			this.description = Description.createSuiteDescription(groupName);
			this.testCases = new ArrayList<>();
		}
		
		
		public void addTestCase(TestCase testCase){
			this.testCases.add(testCase);
			this.description.addChild(testCase.getDescription());
		}
		
		public List<TestCase> getTestCases() {
			return testCases;
		}


		public Description getDescription(){
			return this.description;
		}


		public String getGroupName() {
			return groupName;
		}
		
		
		public boolean shouldSkip(){
			return SKIP_GROUPS.contains(this.getGroupName());
		}
		
		
	}
	
	
	static class TestCase{
		String caseName;
		Description description;
		
		private String expr;
		private JsonNode dataset;
		private JsonNode expectedResult;
		
		private boolean undefinedResult;
		
		private boolean expectParseOrEvaluateException;
		private TestGroup group;
		
		public TestCase(TestGroup group, String caseName, JsonNode caseJson) {
			super();
			this.group = group;
			this.caseName = caseName;
			this.description = Description.createTestDescription(group.getGroupName(), caseName);
			
			
			if(!caseJson.isObject()){
				throw new RuntimeException("["+group.getGroupName()+"."+caseName+"] Not a JSON object");
			}
			ObjectNode o = (ObjectNode)caseJson;
			
			// expr: 
			// The jsonata expression to be evaluated.
			this.expr = o.get("expr").asText();
			
			// data or dataset:
			// If data is defined, use the value of the data field as the input data for the test case. 
			// Otherwise, the dataset field contains the name of the dataset (in the datasets directory) to use as input data. 
			// If value of the dataset field is null, then use undefined as the input data when evaluating the jsonata expression.
			final JsonNode dataNode = o.get("data");
			final JsonNode datasetNode = o.get("dataset");
			
			if(dataNode != null){
				this.dataset = dataNode;
			}else if(datasetNode != null && !datasetNode.isNull()){
				final String datasetName = o.get("dataset").asText();
				this.dataset = DATASETS.get(datasetName);
				if(this.dataset == null){
					throw new RuntimeException("["+group.getGroupName()+"."+caseName+"] Dataset with name "+datasetName+" not found");
				}
			}else{
				this.dataset = null;
			}
			
			// undefinedResult: 
			// A flag indicating the expected result of evaluation will be undefined
			if(o.has("undefinedResult") && o.get("undefinedResult").equals(BooleanNode.TRUE)){
				this.undefinedResult = true;
			}else{
				this.undefinedResult = false;
			}
			
			if(!undefinedResult){
				// result: 
				// The expected result of evaluation (if defined)
				this.expectedResult = o.get("result");
				if(this.expectedResult != null){
					this.expectedResult = Utils.ensureAllIntegralsAreLongs(this.expectedResult);
				}
			}else{
				this.expectedResult = null;
			}

			// TODO: code: 
			// The code associated with the exception that is expected to be thrown when either compiling the expression or evaluating it
			// for now, we just take this to mean "expect a ParseException or an EvaluateException"
			expectParseOrEvaluateException = o.has("code");
			
			// TODO: depth: 
			// If the depth of evaluation should be limited, this specifies the depth.
			
			// TODO: bindings:
			//  Any variable bindings to be applied when evaluating the expression.
			
			// TODO: timelimit: 
			//  If a timelimit should be imposed on the test, this specifies the timelimit in milliseconds.
			
			// TODO: token:
			// If the code field is present, an optional token field may also be present indicating which token token the exception should be associated with.

			
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
		
		
		private boolean shouldSkip(){
			return this.group.shouldSkip() || (SKIP_CASES.containsKey(group.getGroupName()) && SKIP_CASES.get(group.getGroupName()).contains(this.getCaseName()));
		}
		
	}


}
