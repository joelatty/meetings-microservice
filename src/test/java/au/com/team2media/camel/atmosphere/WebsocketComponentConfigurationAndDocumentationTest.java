package au.com.team2media.camel.atmosphere;

import org.apache.camel.ComponentConfiguration;
import org.apache.camel.EndpointConfiguration;
import org.apache.camel.component.atmosphere.websocket.WebsocketComponent;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;


public class WebsocketComponentConfigurationAndDocumentationTest extends CamelTestSupport {

    @Override
    public boolean isUseRouteBuilder() {
        return false;
    }

    @Test
    public void testComponentConfiguration() throws Exception {
        WebsocketComponent comp = context.getComponent("atmosphere-websocket", WebsocketComponent.class);
        EndpointConfiguration conf = comp.createConfiguration("atmosphere-websocket://localhost:8088/hola?sendToAll=true&useStreaming=false");

        assertEquals("true", conf.getParameter("sendToAll"));
        assertEquals("false", conf.getParameter("useStreaming"));

        ComponentConfiguration compConf = comp.createComponentConfiguration();
        String json = compConf.createParameterJsonSchema();
        assertNotNull(json);
    }

}

