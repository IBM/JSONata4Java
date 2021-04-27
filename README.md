# JSONata4Java
This project provides an Open Source Java version of the JSONata project from https://jsonata.org

JSONata was created by Andrew Coleman to provide many of the features that XPath and XQuery provide for XML, except these capabilities apply to JSON-structured data. The JavaScript implementation is described at the following websites:

* [JSONata main site](http://jsonata.org/)
* [Try JSONata -- experiment with commands](http://try.jsonata.org/)
* [JSONata Documentation](http://docs.jsonata.org/overview.html)
* [JavaScript JSONata github repository](https://github.com/jsonata-js/jsonata)

JSONata4Java is an attempt to port the jsonata.js 1.8.4 capabilities to Java v1.8. 
The easiest way to use this library is to include it as a dependency in your Maven pom.xml using these line:
```
<dependency>
  <groupId>com.ibm.jsonata4java</groupId>
  <artifactId>JSONata4Java</artifactId>
  <version>1.5.5</version>
</dependency>
```

You can also opt to clone this repository using the command line below and build the jars yourself:
```
git clone https://github.com/IBM/JSONata4Java.git
```
The code was created using OpenJDK Runtime Environment (build 1.8.0_252-b09). You can import the project into Eclipse 2021-03 or newer. 

### Building the jar files

To build the code, 
you'll want to right click on the project and select Maven / Update Project to ensure the dependencies are available, then 
you can right click on the pom.xml file and select **Run as... / Maven build...** then fill in clean install as the goals:
**clean install** as shown below:

![Launcher Image](images/Launcher.png)

Once you have run the launcher, you can find the jar files in the /target directory. There are two:
* **JSONata4Java-1.5.5-jar-with-dependencies.jar** (thinks includes dependent jar files)
* **JSONata4Java-1.5.5.jar** (only the JSONata4Java code)

The com.api.jsonata4java.Tester program enables you to enter an expression and run it 
against the same JSON as is used at the https://try.jsonata.org site. You can also 
provide a fully qualified filename of a json file on the command line to test expressions 
against your own data.

There is a tester.sh you can run in the project to enable you to test expressions 
against the same JSON as is used at the https://try.jsonata.org site.

The API's to embed JSONata execution in your code are simple. The code below is copied from the Test utility, and uses the 
jackson core ObjectMapper to parse a JSON formatted String into a JsonNode object. The dependency for the 
jackson core is below:
``` 
<dependency>
   <groupId>com.fasterxml.jackson.core</groupId>
   <artifactId>jackson-databind</artifactId>
   <version>2.9.8</version>
</dependency>
```
Here is example code to parse and execute an expression against a jsonObj created from 
a json String.
```
package com.api.jsonata4java;

import java.io.IOException;

import com.api.jsonata4java.expressions.EvaluateException;
import com.api.jsonata4java.expressions.EvaluateRuntimeException;
import com.api.jsonata4java.expressions.Expressions;
import com.api.jsonata4java.expressions.ParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Test {

   public static void main(String[] args) {
      Expressions expr = null;
      ObjectMapper mapper = new ObjectMapper();
      JsonNode jsonObj = null;
      String json = "{ \"a\":1, \"b\":2, \"c\":[1,2,3,4,5] }";
      String expression = "$sum(c)";
      try {
         jsonObj = mapper.readTree(json);
      } catch (IOException e1) {
         e1.printStackTrace();
      }

      try {
         System.out.println("Using json:\n" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObj));
         System.out.println("expression=" + expression);
         expr = Expressions.parse(expression);
      } catch (ParseException e) {
         System.err.println(e.getLocalizedMessage());
      } catch (EvaluateRuntimeException ere) {
         System.out.println(ere.getLocalizedMessage());
      } catch (JsonProcessingException e) {
         e.printStackTrace();
      }
      try {
         System.out.println("evaluate returns:");
         JsonNode result = expr.evaluate(jsonObj);
         if (result == null) {
            System.out.println("** no match **");
         } else {
            System.out.println("" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));
         }
      } catch (EvaluateException | JsonProcessingException e) {
         System.err.println(e.getLocalizedMessage());
      }
   }
}

```

The expression is text describing access to, and manipulation of JSON-structured data. 
In this case we are getting the value of the "c" variable and summing up its array values.
JSONata is very powerful, allowing many built in functions as described at the http://docs.jsonata.org/overview.html site.
 
We begin by parsing the expression to ensure there are no syntax errors. 
If the content parses correctly, you can then execute the parsed expression (expr) 
against the jsonObj object. 

When you run the Test program above, you will see:
```
Using json:
{
  "a" : 1,
  "b" : 2,
  "c" : [ 1, 2, 3, 4, 5 ]
}
expression=$sum(c)
evaluate returns:
15
```

Running this in the https://try.jsonata.org we get:

![TryImage](images/TryJsonata.png)

The various functions and syntax for the expression are documented at https://docs.jsonata.org/overview.html 

* Short, 5 minute youtube video here: https://www.youtube.com/watch?v=ZBaK40rtIBM
* Medium, 27 minute youtube video here: https://www.youtube.com/watch?v=TDWf6R8aqDo
* Long, 1 hour presentation here: https://www.youtube.com/watch?v=ZRtlkIj0uDY

This initial release of JSONata4Java has attempted to cover the large majority of features described in the JSONata documentation.

### Current Limitations:
There are a few functions that have not been implemented:

From: https://docs.jsonata.org/control-operators we did not implement:
* ^(...) (Order-by)
* ... ~> | ... | ...| (Transform)

From: https://docs.jsonata.org/string-functions
* we only recognize patterns as strings "xxx" but not delimited with slashes /xxx/

From: https://docs.jsonata.org/numeric-functions we did not implement:
* $formatInteger()
* $parseInteger()

There are some jsonata.org 1.8.4 tests that we skip because we fail on them in the AgnosticTestSuite.java.


