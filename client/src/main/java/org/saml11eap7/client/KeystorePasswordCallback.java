package org.saml11eap7.client;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by nmajorov on 01.02.17.
 */
import org.jboss.wsf.stack.cxf.extensions.security.PasswordCallbackHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeystorePasswordCallback extends PasswordCallbackHandler {
    private static final Logger LOG = LoggerFactory.getLogger(KeystorePasswordCallback.class);

    public KeystorePasswordCallback() {
        super(getInitMap());
        LOG.info("callback created ");
    }

    private static Map<String, String> getInitMap() {
        Map<String, String> passwords = new HashMap<String, String>();
        passwords.put("alice", "password");
        passwords.put("bob", "password");
        passwords.put("john", "password");
        return passwords;
    }
}