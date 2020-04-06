/**
 * (c) Copyright 2018-2020 IBM Corporation
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

package com.api.jsonata4java.expressions;

import java.util.HashMap;
import java.util.Map;

import com.api.jsonata4java.expressions.functions.DeclaredFunction;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Object to manage access to variable and function maps used in the
 * {@link ExpressionsVisitor} to manage the current block's environment.
 */
public class FrameEnvironment {
   FrameEnvironment _enclosingFrame = null;
   private Map<String, DeclaredFunction> _functionMap = new HashMap<String, DeclaredFunction>();
   private boolean _isAsync = false;
   private JS4JDate _timestamp = null;
   private Map<String, JsonNode> _variableMap = new HashMap<String, JsonNode>();

   /**
    * Constructor taking a null enclosing frame to form the root of the environment
    * hierarchy, or an existing enclosing frame
    * 
    * @param enclosingFrame
    */
   public FrameEnvironment(FrameEnvironment enclosingFrame) {
      _enclosingFrame = enclosingFrame;
      _functionMap = new HashMap<String, DeclaredFunction>();
      _variableMap = new HashMap<String, JsonNode>();
      if (enclosingFrame == null) {
         _timestamp = new JS4JDate();
      }
   }

   public boolean async() {
      return _isAsync;
   }

   public DeclaredFunction getFunction(String fctName) {
      DeclaredFunction retFct = _functionMap.get(fctName);
      if (retFct != null) {
         return retFct;
      }
      if (_enclosingFrame != null) {
         return _enclosingFrame.getFunction(fctName);
      }
      return null;
   }

   public JsonNode getVariable(String varName) {
      JsonNode retVar = _variableMap.get(varName);
      if (retVar != null) {
         return retVar;
      }
      if (_enclosingFrame != null) {
         return _enclosingFrame.getVariable(varName);
      }
      return null;
   }

   public Long millis() {
      if (_timestamp != null) {
         return _timestamp.getTime();
      }
      if (_enclosingFrame != null) {
         return _enclosingFrame.millis();
      }
      return null;
   }

   public String now() {
      if (_timestamp != null) {
         return _timestamp.toString();
      }
      if (_enclosingFrame != null) {
         return _enclosingFrame.now();
      }
      return null;
   }

   public void setAsync(boolean isAsync) {
      _isAsync = isAsync;
   }
   
   public void setFunction(String fctName, DeclaredFunction fctValue) throws EvaluateRuntimeException {
      if (fctName == null) {
         throw new EvaluateRuntimeException("function name is null.");
      }
      if (fctValue == null) {
         throw new EvaluateRuntimeException("function value is null.");
      }
      _functionMap.put(fctName, fctValue);
   }
   
   public void setVariable(String varName, JsonNode varValue) throws EvaluateRuntimeException {
      if (varName == null) {
         throw new EvaluateRuntimeException("variable name is null.");
      }
      if (varValue == null) {
         throw new EvaluateRuntimeException("variable value is null.");
      }
      _variableMap.put(varName, varValue);
   }
}
