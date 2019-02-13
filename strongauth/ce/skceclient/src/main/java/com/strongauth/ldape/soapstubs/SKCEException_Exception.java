
package com.strongauth.ldape.soapstubs;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.6-1b01 
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "SKCEException", targetNamespace = "http://ldapews.strongauth.com/")
public class SKCEException_Exception
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private SKCEException faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public SKCEException_Exception(String message, SKCEException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public SKCEException_Exception(String message, SKCEException faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: com.strongauth.ldape.soapstubs.SKCEException
     */
    public SKCEException getFaultInfo() {
        return faultInfo;
    }

}
