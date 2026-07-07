package testmanually;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import com.api.jsonata4java.Binding;
import com.api.jsonata4java.Expression;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.IntNode;
import tools.jackson.databind.node.StringNode;

public class TestBindingReference implements Serializable {

    private static final long serialVersionUID = -7721819254928734600L;

    static ObjectMapper JACKSON = new ObjectMapper();

    static public void supportsContextualizedConstructionX() throws Exception {
        String json = "{\n"
            + "  \"bar\": \"baz\"\n"
            + "}";
        JsonNode rootContextNode = JACKSON.readTree(json);

        String jsonata = "$notification.foo";
        Expression jsonataExpr = Expression.jsonata(jsonata);

        JsonNode jObj = JACKSON.readTree("{ \"foo\": \"bar\" }");

        // could also use "$notification" below
        Binding bindingNode = new Binding("notification", jObj);
        List<Binding> bindingList = new ArrayList<Binding>();
        bindingList.add(bindingNode);

        JsonNode transformed = jsonataExpr.evaluate(rootContextNode, bindingList);
        System.out.println(transformed);
        
        json = "{\"bar\": [ 4, 5, 6]}";
        rootContextNode = JACKSON.readTree(json);
        jsonata = "bar[$notification[1]]";
        jsonataExpr = Expression.jsonata(jsonata);
        jObj = JACKSON.readTree("[1,2,3]" );
        bindingNode = new Binding("notification", jObj);
        bindingList = new ArrayList<Binding>();
        bindingList.add(bindingNode);
        
        transformed = jsonataExpr.evaluate(rootContextNode, bindingList);
        System.out.println(transformed);
    }

    @Test
    public void supportsContextualizedConstruction() throws Exception {
        String json = "{\n"
            + "  \"bar\": \"baz\"\n"
            + "}";
        JsonNode rootContextNode = JACKSON.readTree(json);

        String jsonata = "$notification.foo";
        Expression jsonataExpr = Expression.jsonata(jsonata);

        JsonNode jObj = JACKSON.readTree("{ \"foo\": \"bar\" }");

        // could also use "$notification" below
        Binding bindingNode = new Binding("notification", jObj);
        List<Binding> bindingList = new ArrayList<Binding>();
        bindingList.add(bindingNode);

        JsonNode actual = jsonataExpr.evaluate(rootContextNode, bindingList);
        JsonNode expected = new StringNode("bar");
        Assert.assertEquals(expected, actual);

        json = "{\"bar\": [ 4, 5, 6]}";
        rootContextNode = JACKSON.readTree(json);
        jsonata = "bar[$notification[1]]";
        jsonataExpr = Expression.jsonata(jsonata);
        jObj = JACKSON.readTree("[1,2,3]" );
        bindingNode = new Binding("notification", jObj);
        bindingList = new ArrayList<Binding>();
        bindingList.add(bindingNode);

        expected = new IntNode(6);
        actual = jsonataExpr.evaluate(rootContextNode, bindingList);
        Assert.assertEquals(expected, actual);
    }

    public static void main(String[] args) {
        try {
            supportsContextualizedConstructionX();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
