
package com.strongauth.skee.soapstubs;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.6-1b01 
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "SKEEServlet", targetNamespace = "http://skeews.strongauth.com/", wsdlLocation = "https://deiskce05.strongauth.com:48181/skee/SKEEServlet?wsdl")
public class SKEEServlet_Service
    extends Service
{

    private final static URL SKEESERVLET_WSDL_LOCATION;
    private final static WebServiceException SKEESERVLET_EXCEPTION;
    private final static QName SKEESERVLET_QNAME = new QName("http://skeews.strongauth.com/", "SKEEServlet");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("https://deiskce05.strongauth.com:48181/skee/SKEEServlet?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        SKEESERVLET_WSDL_LOCATION = url;
        SKEESERVLET_EXCEPTION = e;
    }

    public SKEEServlet_Service() {
        super(__getWsdlLocation(), SKEESERVLET_QNAME);
    }

    public SKEEServlet_Service(WebServiceFeature... features) {
        super(__getWsdlLocation(), SKEESERVLET_QNAME, features);
    }

    public SKEEServlet_Service(URL wsdlLocation) {
        super(wsdlLocation, SKEESERVLET_QNAME);
    }

    public SKEEServlet_Service(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, SKEESERVLET_QNAME, features);
    }

    public SKEEServlet_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SKEEServlet_Service(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns SKEEServlet
     */
    @WebEndpoint(name = "SKEEServletPort")
    public SKEEServlet getSKEEServletPort() {
        return super.getPort(new QName("http://skeews.strongauth.com/", "SKEEServletPort"), SKEEServlet.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SKEEServlet
     */
    @WebEndpoint(name = "SKEEServletPort")
    public SKEEServlet getSKEEServletPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://skeews.strongauth.com/", "SKEEServletPort"), SKEEServlet.class, features);
    }

    private static URL __getWsdlLocation() {
        if (SKEESERVLET_EXCEPTION!= null) {
            throw SKEESERVLET_EXCEPTION;
        }
        return SKEESERVLET_WSDL_LOCATION;
    }

}
