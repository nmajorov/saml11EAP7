package org.saml11eap7.soap;

import java.util.HashMap;
import java.util.Map;

import org.jboss.wsf.stack.cxf.extensions.security.PasswordCallbackHandler;

/**
 * Created by nmajorov on 01.02.17.
 */
public class KeystorePasswordCallback extends PasswordCallbackHandler {
    public KeystorePasswordCallback() {
        super(getInitMap());
    }

    private static Map<String, String> getInitMap() {
        Map<String, String> passwords = new HashMap<String, String>();
        passwords.put("alice", "password");
        passwords.put("bob", "password");
        return passwords;
    }
}
