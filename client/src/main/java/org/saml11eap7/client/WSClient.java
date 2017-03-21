package org.saml11eap7.client;

import org.apache.cxf.Bus;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.rt.security.SecurityConstants;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.saml11eap7.api.Echo;
import org.saml11eap7.api.EchoBeanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import org.jboss.ws.api.configuration.ClientConfigUtil;
import org.jboss.ws.api.configuration.ClientConfigurer;


/**
 * simple web-service client for echo service
 *
 * @author <a href="mailto:nikolaj.majorov@baloise.ch">Nikolaj Majorov</a>
 **/
public class WSClient {
    private Bus cxfBus;
    private Properties encryptionProperties = new Properties();

    private static final Logger LOG = LoggerFactory.getLogger(WSClient.class);

    Echo   echoServiceClient;

    /**
     * Create client and
     * use default properties provided with client
     *
     * @param WSDL wsdl file to read
     **/
    public WSClient(String WSDL) throws MalformedURLException {

        this.echoServiceClient = createClient(WSDL, Echo.class);


    }

    public String echo(String caller){
        LOG.info("in method echo caller: " + caller );

        BindingProvider bp = (BindingProvider)this.echoServiceClient;

        //ClientConfigurer configurer = ClientConfigUtil.resolveClientConfigurer();

        //configurer.setConfigHandlers(bp,"META-INF/jaxws-client-config.xml","NMClientConf");

        ///configure programmatically
        Map<String, Object> reqCtx = bp.getRequestContext();
        SamlCallbackHandler cbh = new SamlCallbackHandler();
        cbh.setConfirmationMethod("urn:oasis:names:tc:SAML:1.0:cm:sender-vouches");
        reqCtx.put(SecurityConstants.SAML_CALLBACK_HANDLER, cbh);

        reqCtx.put(SecurityConstants.CALLBACK_HANDLER, new KeystorePasswordCallback());
        reqCtx.put(SecurityConstants.SIGNATURE_PROPERTIES, Thread.currentThread().getContextClassLoader().getResource("META-INF/alice.properties"));
        reqCtx.put(SecurityConstants.ENCRYPT_PROPERTIES, Thread.currentThread().getContextClassLoader().getResource("META-INF/alice.properties"));
        reqCtx.put(SecurityConstants.SIGNATURE_USERNAME, "alice");
        reqCtx.put(SecurityConstants.ENCRYPT_USERNAME, "bob");


        ArrayList<Feature> features = new ArrayList<>();

        //add logging feature
        LoggingFeature loggingFeature = new LoggingFeature();
        loggingFeature.setPrettyLogging(true);

        features.add(loggingFeature);

        Client cxfClient = ClientProxy.getClient(this.echoServiceClient);
        cxfClient.getBus().setFeatures(features);


        LOG.info("Client configured.." );



        return this.echoServiceClient.echo(caller);
    }

// ------------------------------------------------------------------------
    // Private helper methods
    // ------------------------------------------------------------------------

    /**
     * Generates a dynamic proxy that can talk SOAP with wildfly's JAX-WS stack.
     */
    private static <T> T createClient(String wsdl, Class<T> intf) throws MalformedURLException {
        URL wsdlUrl = new URL(wsdl);

        String namespace = inferXmlNamespace(intf);

        QName service = new QName(namespace, String.format("%sBeanService", intf.getSimpleName()));

        return (T) new Service(wsdlUrl, service) {
        }.getPort(new QName(namespace, String.format("%sBeanPort", intf.getSimpleName())), intf);
    }

    private static String inferXmlNamespace(Class<?> intf) {
        StringBuilder result = new StringBuilder("http://");
        result.append("soap.saml11eap7.org/");
        return result.toString();
    }


    public static void main(String[] args) throws Exception {


        String url = System.getProperty("serviceUrl", "http://localhost:8080/server/EchoBean?wsdl");
        LOG.info("Starting client for echo service for url: " + url);
        WSClient client = new WSClient(url);
        String result = client.echo("RH");

        LOG.info(" got result: " + result);

    }

}