package org.saml11eap7.soap;

import javax.ejb.Stateless;
import javax.jws.WebService;

import org.apache.cxf.feature.Features;
import org.jboss.ws.api.annotation.EndpointConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * Soap webservice with saml11 sign token support
 *
 * Created by nmajorov@redhat.com on 01.02.17
 */
@Stateless
@WebService(
        wsdlLocation = "/wsdl/Echo.wsdl"
)

/**
@EndpointProperties(value = {
        @EndpointProperty(key = "ws-security.signature.properties", value = "WEB-INF/bob.properties"),
        @EndpointProperty(key = "ws-security.signature.username", value = "bob"),
        @EndpointProperty(key = "ws-security.encryption.properties", value = "WEB-INF/bob.properties"),
        @EndpointProperty(key = "ws-security.encryption.username", value = "useReqSigCert"),
        @EndpointProperty(key = "ws-security.callback-handler", value = "org.saml11eap7.soap.KeystorePasswordCallback")
})
**/
@Features(features = "org.apache.cxf.feature.LoggingFeature")
@EndpointConfig(configFile = "/WEB-INF/jaxws-endpoint-config.xml", configName = "NM WS-Security Endpoint")
public class EchoBean implements Echo {

    private static Logger LOG = LoggerFactory.getLogger(EchoBean.class);

    @Override
    public String echo(String text) {
        LOG.info("echo called: " + text);
        return text;
    }
}
