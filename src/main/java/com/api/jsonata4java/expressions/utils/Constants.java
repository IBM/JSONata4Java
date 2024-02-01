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

package com.api.jsonata4java.expressions.utils;

import java.io.Serializable;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import com.api.jsonata4java.expressions.functions.AbsFunction;
import com.api.jsonata4java.expressions.functions.AppendFunction;
import com.api.jsonata4java.expressions.functions.AverageFunction;
import com.api.jsonata4java.expressions.functions.Base64DecodeFunction;
import com.api.jsonata4java.expressions.functions.Base64EncodeFunction;
import com.api.jsonata4java.expressions.functions.BooleanFunction;
import com.api.jsonata4java.expressions.functions.CeilFunction;
import com.api.jsonata4java.expressions.functions.ContainsFunction;
import com.api.jsonata4java.expressions.functions.CountFunction;
import com.api.jsonata4java.expressions.functions.DistinctFunction;
import com.api.jsonata4java.expressions.functions.EachFunction;
import com.api.jsonata4java.expressions.functions.ErrorFunction;
import com.api.jsonata4java.expressions.functions.EvalFunction;
import com.api.jsonata4java.expressions.functions.ExistsFunction;
import com.api.jsonata4java.expressions.functions.FilterFunction;
import com.api.jsonata4java.expressions.functions.FloorFunction;
import com.api.jsonata4java.expressions.functions.FormatBaseFunction;
import com.api.jsonata4java.expressions.functions.FormatNumberFunction;
import com.api.jsonata4java.expressions.functions.FromMillisFunction;
import com.api.jsonata4java.expressions.functions.FromMillisZonedFunction;
import com.api.jsonata4java.expressions.functions.FunctionBase;
import com.api.jsonata4java.expressions.functions.JoinFunction;
import com.api.jsonata4java.expressions.functions.KeysFunction;
import com.api.jsonata4java.expressions.functions.LengthFunction;
import com.api.jsonata4java.expressions.functions.LookupFunction;
import com.api.jsonata4java.expressions.functions.LowercaseFunction;
import com.api.jsonata4java.expressions.functions.MapFunction;
import com.api.jsonata4java.expressions.functions.MatchFunction;
import com.api.jsonata4java.expressions.functions.MaxFunction;
import com.api.jsonata4java.expressions.functions.MergeFunction;
import com.api.jsonata4java.expressions.functions.MillisFunction;
import com.api.jsonata4java.expressions.functions.MinFunction;
import com.api.jsonata4java.expressions.functions.NotFunction;
import com.api.jsonata4java.expressions.functions.NowFunction;
import com.api.jsonata4java.expressions.functions.NumberFunction;
import com.api.jsonata4java.expressions.functions.PadFunction;
import com.api.jsonata4java.expressions.functions.PowerFunction;
import com.api.jsonata4java.expressions.functions.RandomFunction;
import com.api.jsonata4java.expressions.functions.ReduceFunction;
import com.api.jsonata4java.expressions.functions.ReplaceFunction;
import com.api.jsonata4java.expressions.functions.ReverseFunction;
import com.api.jsonata4java.expressions.functions.RoundFunction;
import com.api.jsonata4java.expressions.functions.ShuffleFunction;
import com.api.jsonata4java.expressions.functions.SiftFunction;
import com.api.jsonata4java.expressions.functions.SortFunction;
import com.api.jsonata4java.expressions.functions.SplitFunction;
import com.api.jsonata4java.expressions.functions.SpreadFunction;
import com.api.jsonata4java.expressions.functions.SqrtFunction;
import com.api.jsonata4java.expressions.functions.StringFunction;
import com.api.jsonata4java.expressions.functions.SubstringAfterFunction;
import com.api.jsonata4java.expressions.functions.SubstringBeforeFunction;
import com.api.jsonata4java.expressions.functions.SubstringFunction;
import com.api.jsonata4java.expressions.functions.SumFunction;
import com.api.jsonata4java.expressions.functions.ToMillisFunction;
import com.api.jsonata4java.expressions.functions.TrimFunction;
import com.api.jsonata4java.expressions.functions.TypeFunction;
import com.api.jsonata4java.expressions.functions.URLDecodeComponentFunction;
import com.api.jsonata4java.expressions.functions.URLDecodeFunction;
import com.api.jsonata4java.expressions.functions.URLEncodeComponentFunction;
import com.api.jsonata4java.expressions.functions.URLEncodeFunction;
import com.api.jsonata4java.expressions.functions.UnpackFunction;
import com.api.jsonata4java.expressions.functions.UppercaseFunction;
import com.api.jsonata4java.expressions.functions.ZipFunction;

public class Constants implements Serializable {

    private static final long serialVersionUID = 439299157239217206L;

    // JSONata string functions
    public static final String FUNCTION_STRING = "$string";
    public static final String FUNCTION_EVAL = "$eval";
    public static final String FUNCTION_SUBSTRING = "$substring";
    public static final String FUNCTION_LENGTH = "$length";
    public static final String FUNCTION_SUBSTRING_BEFORE = "$substringBefore";
    public static final String FUNCTION_SUBSTRING_AFTER = "$substringAfter";
    public static final String FUNCTION_UPPERCASE = "$uppercase";
    public static final String FUNCTION_LOWERCASE = "$lowercase";
    public static final String FUNCTION_TRIM = "$trim";
    public static final String FUNCTION_PAD = "$pad";
    public static final String FUNCTION_CONTAINS = "$contains";
    public static final String FUNCTION_SPLIT = "$split";
    public static final String FUNCTION_JOIN = "$join";
    public static final String FUNCTION_REPLACE = "$replace";
    public static final String FUNCTION_NOW = "$now";
    public static final String FUNCTION_FROM_MILLIS = "$fromMillis";
    public static final String FUNCTION_FROM_MILLIS_ZONED = "$fromMillisZoned";
    public static final String FUNCTION_FORMAT_NUMBER = "$formatNumber";
    public static final String FUNCTION_FORMAT_BASE = "$formatBase";
    public static final String FUNCTION_BASE64_ENCODE = "$base64encode";
    public static final String FUNCTION_BASE64_DECODE = "$base64decode";
    public static final String FUNCTION_MATCH = "$match";
    public static final String FUNCTION_URL_ENCODE = "$encodeUrl";
    public static final String FUNCTION_URL_ENCODE_COMPONENT = "$encodeUrlComponent";
    public static final String FUNCTION_URL_DECODE = "$decodeUrl";
    public static final String FUNCTION_URL_DECODE_COMPONENT = "$decodeUrlComponent";
    public static final String FUNCTION_TYPE = "$type";
    public static final String FUNCTION_ERROR = "$error";

    // JSONata numeric functions
    public static final String FUNCTION_NUMBER = "$number";
    public static final String FUNCTION_ABS = "$abs";
    public static final String FUNCTION_FLOOR = "$floor";
    public static final String FUNCTION_CEIL = "$ceil";
    public static final String FUNCTION_ROUND = "$round";
    public static final String FUNCTION_POWER = "$power";
    public static final String FUNCTION_SQRT = "$sqrt";
    public static final String FUNCTION_RANDOM = "$random";
    public static final String FUNCTION_MILLIS = "$millis";
    public static final String FUNCTION_TO_MILLIS = "$toMillis";
    public static final String FUNCTION_UNPACK = "$unpack";

    // JSONata boolean functions
    public static final String FUNCTION_BOOLEAN = "$boolean";
    public static final String FUNCTION_EXISTS = "$exists";
    public static final String FUNCTION_NOT = "$not";

    // JSONata array functions
    public static final String FUNCTION_APPEND = "$append";
    public static final String FUNCTION_COUNT = "$count";
    public static final String FUNCTION_SUM = "$sum";
    public static final String FUNCTION_AVERAGE = "$average";
    public static final String FUNCTION_MIN = "$min";
    public static final String FUNCTION_MAX = "$max";
    public static final String FUNCTION_REVERSE = "$reverse";
    public static final String FUNCTION_SHUFFLE = "$shuffle";
    public static final String FUNCTION_ZIP = "$zip";
    public static final String FUNCTION_SORT = "$sort";
    public static final String FUNCTION_INDEX_OF = "$indexOf";
    public static final String FUNCTION_DISTINCT = "$distinct";

    // JSONata object functions
    public static final String FUNCTION_KEYS = "$keys";
    public static final String FUNCTION_LOOKUP = "$lookup";
    public static final String FUNCTION_SPREAD = "$spread";
    public static final String FUNCTION_MERGE = "$merge";
    public static final String FUNCTION_EACH = "$each";

    // High Order functions
    public static final String FUNCTION_FILTER = "$filter";
    public static final String FUNCTION_MAP = "$map";
    public static final String FUNCTION_REDUCE = "$reduce";
    public static final String FUNCTION_SIFT = "$sift";

    // Collection of functions
    public static final Map<String, FunctionBase> FUNCTIONS = new HashMap<>();

    // Default DecimalFormatSymbols object
    public static DecimalFormatSymbols DEFAULT_DECIMAL_FORMAT_SYMBOLS = new DecimalFormatSymbols(Locale.US);

    // Static initializer
    static {
        // Populate the functions collection
        FUNCTIONS.put(FUNCTION_SUBSTRING, new SubstringFunction());
        FUNCTIONS.put(FUNCTION_EVAL, new EvalFunction());
        FUNCTIONS.put(FUNCTION_STRING, new StringFunction());
        FUNCTIONS.put(FUNCTION_LENGTH, new LengthFunction());
        FUNCTIONS.put(FUNCTION_SUBSTRING_BEFORE, new SubstringBeforeFunction());
        FUNCTIONS.put(FUNCTION_SUBSTRING_AFTER, new SubstringAfterFunction());
        FUNCTIONS.put(FUNCTION_UPPERCASE, new UppercaseFunction());
        FUNCTIONS.put(FUNCTION_LOWERCASE, new LowercaseFunction());
        FUNCTIONS.put(FUNCTION_TRIM, new TrimFunction());
        FUNCTIONS.put(FUNCTION_PAD, new PadFunction());
        FUNCTIONS.put(FUNCTION_CONTAINS, new ContainsFunction());
        FUNCTIONS.put(FUNCTION_SPLIT, new SplitFunction());
        FUNCTIONS.put(FUNCTION_JOIN, new JoinFunction());
        FUNCTIONS.put(FUNCTION_REPLACE, new ReplaceFunction());
        FUNCTIONS.put(FUNCTION_NOW, new NowFunction());
        FUNCTIONS.put(FUNCTION_FROM_MILLIS, new FromMillisFunction());
        FUNCTIONS.put(FUNCTION_FROM_MILLIS_ZONED, new FromMillisZonedFunction());
        FUNCTIONS.put(FUNCTION_FORMAT_NUMBER, new FormatNumberFunction());
        FUNCTIONS.put(FUNCTION_FORMAT_BASE, new FormatBaseFunction());
        FUNCTIONS.put(FUNCTION_BASE64_ENCODE, new Base64EncodeFunction());
        FUNCTIONS.put(FUNCTION_BASE64_DECODE, new Base64DecodeFunction());
        FUNCTIONS.put(FUNCTION_NUMBER, new NumberFunction());
        FUNCTIONS.put(FUNCTION_EXISTS, new ExistsFunction());
        FUNCTIONS.put(FUNCTION_APPEND, new AppendFunction());
        FUNCTIONS.put(FUNCTION_COUNT, new CountFunction());
        FUNCTIONS.put(FUNCTION_SUM, new SumFunction());
        FUNCTIONS.put(FUNCTION_AVERAGE, new AverageFunction());
        FUNCTIONS.put(FUNCTION_BOOLEAN, new BooleanFunction());
        FUNCTIONS.put(FUNCTION_NOT, new NotFunction());
        FUNCTIONS.put(FUNCTION_UNPACK, new UnpackFunction());
        FUNCTIONS.put(FUNCTION_ABS, new AbsFunction());
        FUNCTIONS.put(FUNCTION_FLOOR, new FloorFunction());
        FUNCTIONS.put(FUNCTION_CEIL, new CeilFunction());
        FUNCTIONS.put(FUNCTION_ROUND, new RoundFunction());
        FUNCTIONS.put(FUNCTION_POWER, new PowerFunction());
        FUNCTIONS.put(FUNCTION_SQRT, new SqrtFunction());
        FUNCTIONS.put(FUNCTION_RANDOM, new RandomFunction());
        FUNCTIONS.put(FUNCTION_MILLIS, new MillisFunction());
        FUNCTIONS.put(FUNCTION_TO_MILLIS, new ToMillisFunction());
        FUNCTIONS.put(FUNCTION_MIN, new MinFunction());
        FUNCTIONS.put(FUNCTION_MAX, new MaxFunction());
        FUNCTIONS.put(FUNCTION_KEYS, new KeysFunction());
        FUNCTIONS.put(FUNCTION_LOOKUP, new LookupFunction());
        FUNCTIONS.put(FUNCTION_SPREAD, new SpreadFunction());
        FUNCTIONS.put(FUNCTION_MERGE, new MergeFunction());
        FUNCTIONS.put(FUNCTION_REVERSE, new ReverseFunction());
        FUNCTIONS.put(FUNCTION_SHUFFLE, new ShuffleFunction());
        FUNCTIONS.put(FUNCTION_ZIP, new ZipFunction());
        FUNCTIONS.put(FUNCTION_SORT, new SortFunction());
        FUNCTIONS.put(FUNCTION_MATCH, new MatchFunction());
        // below not implemented in jsonata.js [yet ;^)]
        // FUNCTIONS.put(FUNCTION_INDEX_OF, new IndexOfFunction());
        FUNCTIONS.put(FUNCTION_EACH, new EachFunction());
        FUNCTIONS.put(FUNCTION_FILTER, new FilterFunction());
        FUNCTIONS.put(FUNCTION_MAP, new MapFunction());
        FUNCTIONS.put(FUNCTION_REDUCE, new ReduceFunction());
        FUNCTIONS.put(FUNCTION_SIFT, new SiftFunction());
        FUNCTIONS.put(FUNCTION_URL_DECODE, new URLDecodeFunction());
        FUNCTIONS.put(FUNCTION_URL_ENCODE, new URLEncodeFunction());
        FUNCTIONS.put(FUNCTION_URL_DECODE_COMPONENT, new URLDecodeComponentFunction());
        FUNCTIONS.put(FUNCTION_URL_ENCODE_COMPONENT, new URLEncodeComponentFunction());
        FUNCTIONS.put(FUNCTION_TYPE, new TypeFunction());
        FUNCTIONS.put(FUNCTION_ERROR, new ErrorFunction());
        FUNCTIONS.put(FUNCTION_DISTINCT, new DistinctFunction());

        // Initialize the default decimal format symbols
        DEFAULT_DECIMAL_FORMAT_SYMBOLS.setExponentSeparator("e");
        DEFAULT_DECIMAL_FORMAT_SYMBOLS.setInfinity("Infinity");
        DEFAULT_DECIMAL_FORMAT_SYMBOLS.setNaN("NaN");
    }

    /**
    * Function signature values used to describe the types of parameters they
    * accept. From https://docs.jsonata.org/programming
    */
    // simple types
    public static String SIG_STRING = "s";
    public static String SIG_NUMBER = "n";
    public static String SIG_BOOLEAN = "b";
    public static String SIG_NULL = "l";

    // complex types
    public static String SIG_OBJECT = "o";
    public static String SIG_ARRAY = "a";
    public static String SIG_FUNCTION = "f";

    // union types
    // Equivalent to (bnsloa) i.e. Boolean, number, string, null, object or array,
    // but not function
    public static String SIG_JSON = "j";
    // null, number, string, or boolean
    public static String SIG_BASIC = "u";
    // any type. Equivalent to (bnsloaf)
    public static String SIG_ANY_TYPE = "x";
    public static String SIG_CONTEXT = "-";
    public static String SIG_OPTIONAL = "?";
    public static String SIG_ONE_OR_MORE = "+";

    // delimiters
    public static String SIG_CHOICE_START = "(";
    public static String SIG_CHOICE_END = ")";
    public static String SIG_TYPE_START = "<";
    public static String SIG_TYPE_END = ">";

    /**
    * Collection of decimal format strings that defined by xsl:decimal-format.
    * 
    * <pre>
    *     &lt;!ELEMENT xsl:decimal-format EMPTY&gt;
    *     &lt;!ATTLIST xsl:decimal-format
    *       name %qname; #IMPLIED
    *       decimal-separator %char; "."
    *       grouping-separator %char; ","
    *       infinity CDATA "Infinity"
    *       minus-sign %char; "-"
    *       NaN CDATA "NaN"
    *       percent %char; "%"
    *       per-mille %char; "&#x2030;"
    *       zero-digit %char; "0"
    *       digit %char; "#"
    *       pattern-separator %char; ";"&GT;
    * </pre>
    * 
    * http://www.w3.org/TR/xslt#format-number} to explain format-number in XSLT
    *      Specification xsl.usage advanced
    */
    public static final String SYMBOL_DECIMAL_SEPARATOR = "decimal-separator";
    public static final String SYMBOL_GROUPING_SEPARATOR = "grouping-separator";
    public static final String SYMBOL_INFINITY = "infinity";
    public static final String SYMBOL_MINUS_SIGN = "minus-sign";
    public static final String SYMBOL_NAN = "NaN";
    public static final String SYMBOL_PERCENT = "percent";
    public static final String SYMBOL_PER_MILLE = "per-mille";
    public static final String SYMBOL_ZERO_DIGIT = "zero-digit";
    public static final String SYMBOL_DIGIT = "digit";
    public static final String SYMBOL_PATTERN_SEPARATOR = "pattern-separator";

    // Error message constants
    // below is out to favor explanation of arguments
    // public static final String ERR_MSG_WRONG_NUM_ARGS = "Wrong number of
    // arguments to function %s";
    public static final String ERR_MSG_ARG1_BAD_TYPE = "Argument 1 of function %s does not match function signature";
    public static final String ERR_MSG_ARG2_BAD_TYPE = "Argument 2 of function %s does not match function signature";
    public static final String ERR_MSG_ARG3_BAD_TYPE = "Argument 3 of function %s does not match function signature";
    public static final String ERR_MSG_ARG4_BAD_TYPE = "Argument 4 of function %s does not match function signature";
    public static final String ERR_MSG_ARG5_BAD_TYPE = "Argument 5 of function %s does not match function signature";
    public static final String ERR_MSG_ARG1_ARR_STR = "Argument 1 of function %s must be an array of strings";
    public static final String ERR_MSG_ARG1_STR_OR_EXPR = "Argument 1 of function %s must be a json string or a jsonata expression";
    public static final String ERR_MSG_ARG2_EMPTY_STR = "Second argument of function %s cannot be an empty string";
    public static final String ERR_MSG_INVALID_RADIX = "The radix of the $formatBase function must be between 2 and 36";
    public static final String ERR_MSG_RUNTIME_ERROR = "A runtime error occurred when invoking function %s";
    public static final String ERR_MSG_INVALID_OPTIONS_SINGLE_CHAR = "Argument 3 of function %s is invalid. The value of the %s property must be a single character";
    public static final String ERR_MSG_INVALID_OPTIONS_STRING = "Argument 3 of function %s is invalid. The value of the %s property must be a string";
    public static final String ERR_MSG_INVALID_OPTIONS_UNKNOWN_PROPERTY = "Argument 3 of function %s is invalid. %s is not a valid property name";
    public static final String ERR_MSG_UNABLE_TO_CAST_VALUE_TO_NUMBER = "Unable to cast value to a number: \"%s\"";
    public static final String ERR_MSG_NUMBER_OUT_OF_RANGE = "Number out of range: \"%s\"";
    public static final String ERR_MSG_POWER_FUNC_RESULT_NOT_NUMBER = "The power function has resulted in a value that cannot be represented as a number: base=\"%s\", exponent=\"%s\"";
    public static final String ERR_MSG_FUNC_CANNOT_BE_APPLIED_NEG_NUM = "The %s function cannot be applied to a negative number: %s";
    public static final String ERR_MSG_TO_MILLIS_ISO_8601_FORMAT = "The argument for the $toMillis function must be an ISO 8601 format datetime string: \"%s\"";
    public static final String ERR_MSG_ARG1_MUST_BE_ARRAY = "Argument 1 of function \"%s\" must be an an array";
    public static final String ERR_MSG_ARG1_MUST_BE_ARRAY_OF_NUMBER = "Argument 1 of function \"%s\" must be an array of \"number\"";
    public static final String ERR_MSG_ARG1_MUST_BE_ARRAY_OF_OBJECTS = "Argument 1 of function \"%s\" must be an object or an array of objects.";
    public static final String ERR_MSG_FCT_CHAIN_NOT_UNARY = "The symbol \"~>\" cannot be used as a unary operator";
    public static final String ERR_MSG_INVALID_PATH_ENTRY = "The literal value %s cannot be used as a step within a path expression";
    public static final String ERR_MSG_BAD_CONTEXT = "Context value is not a compatible type with argument 1 of function \"%s\"";
    public static final String ERR_MSG_FCT_NOT_FOUND = "Cannot find a declared function as second argument of function \"%s\".";
    public static final String ERR_MSG_VARIABLE_FCT_NOT_FOUND = "Cannot find a declared function for variable \"%s\" of function \"%s\".";
    public static final String ERR_MSG_SEQUENCE_UNSUPPORTED = "Formatting or parsing an integer as a sequence starting with %s is not supported by this implementation";
    public static final String ERR_MSG_DIFF_DECIMAL_GROUP = "In a decimal digit pattern, all digits must be from the same decimal group";
    public static final String ERR_MSG_NO_CLOSING_BRACKET = "No matching closing bracket ']' in date/time picture string";
    public static final String ERR_MSG_UNKNOWN_COMPONENT_SPECIFIER = "Unknown component specifier %s in date/time picture string";
    public static final String ERR_MSG_INVALID_NAME_MODIFIER = "The 'name' modifier can only be applied to months and days in the date/time picture string, not %s";
    public static final String ERR_MSG_TIMEZONE_FORMAT = "The timezone integer format specifier cannot have more than four digits";
    public static final String ERR_MSG_MISSING_FORMAT = "The date/time picture string is missing specifiers required to parse the timestamp";
}
