import org.apache.http.HttpHost;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Cherish
 * @version 1.0
 * @date 2017/8/16 11:08
 */
public class ElasticTest {


    public static void main(String[] args) throws IOException {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            System.err.println(t);
            e.printStackTrace(System.err);
        });

        RestClient restClient = RestClient.builder(new HttpHost("39.108.67.111", 9200, "http")).build();

        NStringEntity matchAllEntity = new NStringEntity("{}", StandardCharsets.UTF_8);

        restClient.performRequest("POST", "/cherish_blog/blog/",null,matchAllEntity);


        restClient.close();
    }

}
