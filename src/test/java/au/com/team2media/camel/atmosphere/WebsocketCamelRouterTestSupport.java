package au.com.team2media.camel.atmosphere;

import org.apache.camel.component.atmosphere.websocket.CamelWebSocketServlet;
import org.apache.camel.test.AvailablePortFinder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.After;
import org.junit.Before;

public class WebsocketCamelRouterTestSupport extends CamelTestSupport {
    public static final String CONTEXT = "/mycontext";
    public static final String CONTEXT_URL = "http://localhost/mycontext";
    protected static final int PORT = AvailablePortFinder.getNextAvailable();
    protected boolean startCamelContext = true;

    protected Server server;

    @Before
    public void setUp() throws Exception {
        server = new Server();
        Connector connector = new SelectChannelConnector();
        connector.setHost("localhost");
        connector.setPort(PORT);
        server.addConnector(connector);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        ServletHolder servletHolder = new ServletHolder(new CamelWebSocketServlet());
        servletHolder.setName("CamelWsServlet");
        context.addServlet(servletHolder, "/*");

        server.start();

        if (startCamelContext) {
            super.setUp();
        }
    }

    @After
    public void tearDown() throws Exception {
        if (startCamelContext) {
            super.tearDown();
        }

        server.stop();
        server.destroy();
    }


}

