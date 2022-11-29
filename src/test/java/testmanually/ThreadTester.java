package testmanually;
import org.junit.Test;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import com.api.jsonata4java.expressions.Expressions;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ThreadTester {

    static boolean showError = false;

    @Test
    public void threadSafeTest() throws Exception {
        String json1 = "{ \"a\":\"aaa\" }";
        String json2 = "{ \"a\":\"bbb\" }";
        Expressions exp = Expressions.parse("a");
        TaskExecutor theExecutor = new SimpleAsyncTaskExecutor();
        if (showError) {
            theExecutor.execute(new ThreadTest(exp, new ObjectMapper().readTree(json1)));
            theExecutor.execute(new ThreadTest(exp, new ObjectMapper().readTree(json2)));
        } else {
            theExecutor.execute(new ThreadTest(Expressions.parse("a"), new ObjectMapper().readTree(json1)));
            theExecutor.execute(new ThreadTest(Expressions.parse("a"), new ObjectMapper().readTree(json2)));

        }

        while (true) {
            Thread.sleep(1000);
        }
    }

    public class ThreadTest implements Runnable {

        JsonNode data;
        Expressions exp;

        public ThreadTest(Expressions exp, JsonNode data) {
            this.data = data;
            this.exp = exp;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    JsonNode result = exp.evaluate(data);
                    if (result != null) {
                        if (!this.data.get("a").asText().equals(result.asText())) {
                            System.out.println(String.format("\n%s %s", this.data.get("a").asText(), result.asText()));
                        }
                    } else {
                        System.out.println("Got null result from " + exp.hashCode() + " data: " + data);
                    }
                } catch (Exception ex) {
                    // System.out.println(ex);
                    ex.printStackTrace();
                }
            }
        }
    }
}
