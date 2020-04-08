package com.api.jsonata4java.expressions.functions;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.JsonNodeType;

public class StringNode extends JsonNode {

   @Override
   public JsonToken asToken() {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public NumberType numberType() {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public JsonParser traverse() {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public JsonParser traverse(ObjectCodec codec) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
      // TODO Auto-generated method stub

   }

   @Override
   public void serializeWithType(JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer)
         throws IOException {
      // TODO Auto-generated method stub

   }

   @Override
   public <T extends JsonNode> T deepCopy() {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public JsonNode get(int index) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public JsonNode path(String fieldName) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public JsonNode path(int index) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   protected JsonNode _at(JsonPointer ptr) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public JsonNodeType getNodeType() {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public String asText() {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public JsonNode findValue(String fieldName) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public JsonNode findPath(String fieldName) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public JsonNode findParent(String fieldName) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public List<JsonNode> findValues(String fieldName, List<JsonNode> foundSoFar) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public List<String> findValuesAsText(String fieldName, List<String> foundSoFar) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public List<JsonNode> findParents(String fieldName, List<JsonNode> foundSoFar) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public String toString() {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public boolean equals(Object o) {
      // TODO Auto-generated method stub
      return false;
   }

}
