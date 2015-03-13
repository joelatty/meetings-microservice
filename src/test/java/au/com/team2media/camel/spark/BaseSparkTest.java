package au.com.team2media.camel.spark;

import org.apache.camel.CamelContext;
import org.apache.camel.component.sparkrest.SparkComponent;
import org.apache.camel.test.AvailablePortFinder;
import org.apache.camel.test.junit4.CamelTestSupport;

public abstract class BaseSparkTest extends CamelTestSupport {

    protected int port;

    public int getPort() {
        return port;
    }

    @Override
    public void setUp() throws Exception {
        port = AvailablePortFinder.getNextAvailable(25500);
        super.setUp();
        Thread.sleep(200);
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = super.createCamelContext();

        SparkComponent spark = context.getComponent("spark-rest", SparkComponent.class);
        spark.setPort(port);

        return context;
    }
}