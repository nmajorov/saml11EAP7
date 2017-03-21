package org.saml11eap7.soap;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * simple soap webservice
 * @author  Nikolaj Majorov
 */
@WebService
public interface Echo {



  String echo(String text);

}
