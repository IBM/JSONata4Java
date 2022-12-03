package testmanually;

import java.io.Serializable;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import com.api.jsonata4java.Expression;
import com.api.jsonata4java.expressions.utils.Utils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Ignore
public class TestBindingReference implements Serializable {

    private static final long serialVersionUID = -7721819254928734600L;

    static ObjectMapper JACKSON = new ObjectMapper();

    static public void supportsContextualizedConstructionX() throws Exception {
        String json = "{ \n" +
            "  \"nested\": {\n" +
            "    \"greeting\": \"hello\",\n" +
            "    \"thinking\": \"bored\"\n" +
            "  } \n" +
            "}";
        JsonNode rootContextNode = JACKSON.readTree(json);

        String jsonata = "{ \n" +
            "    \"utter\": nested {\n" +
            "        \"say\": greeting,\n" +
            "        \"think\": thinking\n" +
            "    },\n" +
            "    \"nestedBinding\": $nested {\n" +
            "        \"say\": greeting,\n" +
            "        \"think\": thinking\n" +
            "    }\n"
            + "}";
        Expression jsonataExpr = Expression.jsonata(jsonata);

        ObjectNode bindingNode = JACKSON.createObjectNode();
        bindingNode //
            .put("nested",
                "{\n"
                    + "    \"nested\": {\n"
                    + "         \"greeting\": \"welcome\",\n"
                    + "         \"thinking\": \"ayaah!\" \n"
                    + "    }\n"
                    + "}");

        JsonNode transformed = jsonataExpr.evaluate(rootContextNode, bindingNode);
        System.out.println(transformed);
    }

    @Test
    public void supportsContextualizedConstruction() throws Exception {
        String json = "{ \n" +
            "  \"nested\": {\n" +
            "    \"greeting\": \"hello\",\n" +
            "    \"thinking\": \"bored\"\n" +
            "  } \n" +
            "}";
        JsonNode rootContextNode = JACKSON.readTree(json);

        String jsonata = "{ \n" +
            "    \"utter\": nested {\n" +
            "        \"say\": greeting,\n" +
            "        \"think\": thinking\n" +
            "    },\n" +
            "    \"nestedBinding\": $nested {\n" +
            "        \"say\": greeting,\n" +
            "        \"think\": thinking\n" +
            "    }\n"
            + "}";
        Expression jsonataExpr = Expression.jsonata(jsonata);

        ObjectNode bindingNode = JACKSON.createObjectNode();
        bindingNode //
            .put("nested",
                "{\n"
                    + "    \"nested\": {\n"
                    + "         \"greeting\": \"welcome\",\n"
                    + "         \"thinking\": \"ayaah!\" \n"
                    + "    }\n"
                    + "}");

        JsonNode expected = Utils.toJson("{\"utter\":{\"say\":\"hello\",\"think\":\"bored\"},\"nestedBinding\":{\"say\":\"welcome\",\"think\":\"ayaah!\"}}");
        JsonNode actual = jsonataExpr.evaluate(rootContextNode, bindingNode);
        System.err.println("* " + actual);
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
