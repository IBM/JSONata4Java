/**
 * (c) Copyright 2024 IBM Corporation
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

// Thanks to @uw4 for fixing the bug that this tests in #310/#311
package com.api.jsonata4java.expressions.functions;

import static com.api.jsonata4java.expressions.utils.Utils.test;
import org.junit.Test;

public class DateTimeFunctionTest {
  @Test
  public void testFormatInteger() throws Exception {
     test("$toMillis('2018th', '[Y0001;o]')", "1514764800000", null, (String)null );
  }
}