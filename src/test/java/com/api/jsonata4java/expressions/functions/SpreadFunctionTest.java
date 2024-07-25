package com.api.jsonata4java.expressions.functions;

import static com.api.jsonata4java.expressions.utils.Utils.test;

import org.junit.Test;

public class SpreadFunctionTest {

    @Test
    public void testSpreadForGeneratingCombinationList() throws Exception {
        test("$spread($$.array1 ~> $map(function($v1) {\n" +
                "            $$.array2 ~> $map(function($v2) {\n" +
                "                $v1 & '#' & $v2\n" +
                "            })\n" +
                "        }))", "[\n" +
                "  \"abcd#pp\",\n" +
                "  \"abcd#uu\",\n" +
                "  \"abcd#tt\",\n" +
                "  \"abcd#uu\",\n" +
                "  \"abcd#ww\",\n" +
                "  \"abcd#gg\",\n" +
                "  \"abcd#qq\",\n" +
                "  \"abcd#nn\",\n" +
                "  \"abcd#ff\",\n" +
                "  \"cdef#pp\",\n" +
                "  \"cdef#uu\",\n" +
                "  \"cdef#tt\",\n" +
                "  \"cdef#uu\",\n" +
                "  \"cdef#ww\",\n" +
                "  \"cdef#gg\",\n" +
                "  \"cdef#qq\",\n" +
                "  \"cdef#nn\",\n" +
                "  \"cdef#ff\",\n" +
                "  \"efgh#pp\",\n" +
                "  \"efgh#uu\",\n" +
                "  \"efgh#tt\",\n" +
                "  \"efgh#uu\",\n" +
                "  \"efgh#ww\",\n" +
                "  \"efgh#gg\",\n" +
                "  \"efgh#qq\",\n" +
                "  \"efgh#nn\",\n" +
                "  \"efgh#ff\",\n" +
                "  \"ghij#pp\",\n" +
                "  \"ghij#uu\",\n" +
                "  \"ghij#tt\",\n" +
                "  \"ghij#uu\",\n" +
                "  \"ghij#ww\",\n" +
                "  \"ghij#gg\",\n" +
                "  \"ghij#qq\",\n" +
                "  \"ghij#nn\",\n" +
                "  \"ghij#ff\",\n" +
                "  \"ijkl#pp\",\n" +
                "  \"ijkl#uu\",\n" +
                "  \"ijkl#tt\",\n" +
                "  \"ijkl#uu\",\n" +
                "  \"ijkl#ww\",\n" +
                "  \"ijkl#gg\",\n" +
                "  \"ijkl#qq\",\n" +
                "  \"ijkl#nn\",\n" +
                "  \"ijkl#ff\",\n" +
                "  \"klmn#pp\",\n" +
                "  \"klmn#uu\",\n" +
                "  \"klmn#tt\",\n" +
                "  \"klmn#uu\",\n" +
                "  \"klmn#ww\",\n" +
                "  \"klmn#gg\",\n" +
                "  \"klmn#qq\",\n" +
                "  \"klmn#nn\",\n" +
                "  \"klmn#ff\",\n" +
                "  \"mnop#pp\",\n" +
                "  \"mnop#uu\",\n" +
                "  \"mnop#tt\",\n" +
                "  \"mnop#uu\",\n" +
                "  \"mnop#ww\",\n" +
                "  \"mnop#gg\",\n" +
                "  \"mnop#qq\",\n" +
                "  \"mnop#nn\",\n" +
                "  \"mnop#ff\"\n" +
                "]", null, "{\n" +
                "  \"array1\": [\n" +
                "    \"abcd\",\n" +
                "    \"cdef\",\n" +
                "    \"efgh\",\n" +
                "    \"ghij\",\n" +
                "    \"ijkl\",\n" +
                "    \"klmn\",\n" +
                "    \"mnop\"\n" +
                "  ],\n" +
                "  \"array2\": [\n" +
                "    \"pp\",\n" +
                "    \"uu\",\n" +
                "    \"tt\",\n" +
                "    \"uu\",\n" +
                "    \"ww\",\n" +
                "    \"gg\",\n" +
                "    \"qq\",\n" +
                "    \"nn\",\n" +
                "    \"ff\"\n" +
                "  ]\n" +
                "}");
    }

}