/*
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License, as published by the Free
 * Software Foundation and available at
 * http://www.fsf.org/licensing/licenses/lgpl.html, version 2.1 or above.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * Copyright (c) 2001-2018 StrongAuth, Inc.
 *
 * $Date$ $Revision$
 * $Author$ $URL:
 * https://svn.strongauth.com/repos/jade/trunk/skce/skcebeans/src/main/java/com/strongauth/skfe/core/U2FAuthenticationResponse.java
 * $
 *
 * *********************************************
 *                    888
 *                    888
 *                    888
 *  88888b.   .d88b.  888888  .d88b.  .d8888b
 *  888 "88b d88""88b 888    d8P  Y8b 88K
 *  888  888 888  888 888    88888888 "Y8888b.
 *  888  888 Y88..88P Y88b.  Y8b.          X88
 *  888  888  "Y88P"   "Y888  "Y8888   88888P'
 *
 * *********************************************
 *
 * Derived class POJO to hold U2F Authentication response that comes back to the
 * FIDO server from the RP application. This class is a derivation of
 * U2FResponse class.
 *
 * This class object once constructed successfully is capable of processing the
 * authentication response.
 *
 */
package com.strongauth.skfe.core;

import com.strongauth.crypto.utility.cryptoCommon;
import com.strongauth.skfe.utilities.skfeLogger;
import com.strongauth.skfe.utilities.skfeCommon;
import com.strongauth.skfe.utilities.skfeConstants;
import com.strongauth.skfe.utilities.SKFEException;
import java.io.Serializable;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Level;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

/**
 * Derived class for U2F Authentication response that comes back to the FIDO
 * server from the RP application.
 */
public class U2FAuthenticationResponse extends U2FResponse implements Serializable {

    /**
     * This class' name - used for logging
     */
    private String classname = this.getClass().getName();

    private String challenge;
    private String appid;
    private String signdata;

    //Class internal use
    private final String userpublickeybytes;

    private int counter;
    private int usertouch;

    /**
     * The constructor of this class takes the U2F authentication response
     * parameters in the form of stringified Json. The method parses the Json to
     * extract needed fileds compliant with the u2fversion specified.
     *
     * @param u2fversion - Version of the U2F protocol being communicated in;
     * example : "U2F_V2"
     * @param authresponseJson - U2F Auth Response params in stringified Json
     * form
     * @param userPublicKeyBytes - User public key in bytes form
     * @param challenge
     * @param appid
     * @throws SKFEException - In case of any error
     */
    public U2FAuthenticationResponse(String u2fversion, String authresponseJson, String userPublicKeyBytes, String challenge, String appid) throws SKFEException {

        //  Input checks
        if (u2fversion == null || u2fversion.trim().isEmpty()) {
            skfeLogger.logp(skfeConstants.SKFE_LOGGER,Level.SEVERE, classname, "U2FAuthenticationResponse", skfeCommon.getMessageProperty("FIDO-ERR-5001"), " u2f version");
            throw new SKFEException(skfeCommon.getMessageProperty("FIDO-ERR-5001") + " username");
        }

        if (authresponseJson == null || authresponseJson.trim().isEmpty()) {
            skfeLogger.logp(skfeConstants.SKFE_LOGGER,Level.SEVERE, classname, "U2FAuthenticationResponse", skfeCommon.getMessageProperty("FIDO-ERR-5001"), " authresponseJson");
            throw new SKFEException(skfeCommon.getMessageProperty("FIDO-ERR-5001") + " authresponseJson");
        }

        //  u2f protocol version specific processing.
        if (u2fversion.equalsIgnoreCase(U2F_VERSION_V2)) {

            try {
                //  Parse the reg response json string
                JsonReader jsonReader = Json.createReader(new StringReader(authresponseJson));
                JsonObject jsonObject = jsonReader.readObject();
                jsonReader.close();

                this.browserdata = jsonObject.getString(skfeConstants.JSON_KEY_CLIENTDATA);
                this.signdata = jsonObject.getString(skfeConstants.JSON_KEY_SIGNATUREDATA);
                this.challenge = challenge;
                this.appid = appid;
            } catch (Exception ex) {
                ex.printStackTrace();
                skfeLogger.logp(skfeConstants.SKFE_LOGGER,Level.SEVERE, classname, "U2FAuthenticationResponse", skfeCommon.getMessageProperty("FIDO-ERR-5011"),
                        ex.getLocalizedMessage());
                throw new SKFEException(skfeCommon.getMessageProperty("FIDO-ERR-5011") + ex.getLocalizedMessage());
            }

            //  Generate new browser data
            bd = new BrowserData(this.browserdata, BrowserData.AUTHENTICATION_RESPONSE);

            //  Make sure challenge from BrowserData is the same as the challenge
            //  in the Authresp
            System.out.println("BDCHALLENGE - CHALLENGE : "+ bd.getChallenge() + " - " + this.challenge);
            if (!bd.getChallenge().equals(this.challenge)) {
                skfeLogger.logp(skfeConstants.SKFE_LOGGER,Level.SEVERE, classname, "U2FAuthenticationResponse", skfeCommon.getMessageProperty("FIDO-ERR-5012"), "");
                throw new SKFEException(skfeCommon.getMessageProperty("FIDO-ERR-5012"));
            }

            this.userpublickeybytes = userPublicKeyBytes;

        } else {
            skfeLogger.logp(skfeConstants.SKFE_LOGGER,Level.SEVERE, classname, "U2FAuthenticationResponse", skfeCommon.getMessageProperty("FIDO-ERR-5002"), " version passed=" + u2fversion);
            throw new SKFEException(skfeCommon.getMessageProperty("FIDO-ERR-5002") + " version passed=" + u2fversion);
        }
    }

    /**
     * Get methods to access the response parameters
     *
     * @return
     */
    public int getCounter() {
        return counter;
    }

    public int getUsertouch() {
        return usertouch;
    }
    
    public String getChallenge() {
        return challenge;
    }

    /**
     * Converts this POJO into a JsonObject and returns the same.
     *
     * @return JsonObject
     */
    public final JsonObject toJsonObject() {
        JsonObject jsonObj = Json.createObjectBuilder()
                .add(skfeConstants.JSON_USER_COUNTER_SERVLET, this.counter)
                .add(skfeConstants.JSON_USER_PRESENCE_SERVLET, this.usertouch)
                .build();

        return jsonObj;
    }

    /**
     * Converts this POJO into a JsonObject and returns the String form of it.
     *
     * @return String containing the Json representation of this POJO.
     */
    public final String toJsonString() {
        return toJsonObject().toString();
    }

    /**
     * Once this class object is successfully constructed, calling verify method
     * will actually process the authentication response params.
     *
     * The first step in verification is sessionid validation, which if found
     * valid goes ahead and processes the authentication data.
     *
     * @return
     * @throws SKFEException - In case of any error
     */
    public final boolean verify() throws SKFEException {
        skfeLogger.logp(skfeConstants.SKFE_LOGGER,Level.FINE, classname, "verify", skfeCommon.getMessageProperty("FIDO-MSG-5011"), "");

        try {
            return processAuthenticationData(this.signdata, this.browserdata, this.userpublickeybytes, appid);
        } catch (SKFEException ex) {
            skfeLogger.logp(skfeConstants.SKFE_LOGGER,Level.SEVERE, classname, "verify", skfeCommon.getMessageProperty("FIDO-ERR-5006"), ex.getLocalizedMessage());
            throw new SKFEException(skfeCommon.getMessageProperty("FIDO-ERR-5006") + ex.getLocalizedMessage());
        }
    }

    /**
     * Processes the authentication data
     *
     * @param signData
     * @param browserData
     * @param userPublicKeyB64
     * @param appid
     * @return
     * @throws FidoEngineException - In case of any error
     */
    private boolean processAuthenticationData(String signData,
            String browserData,
            String userPublicKeyB64,
            String appid) throws SKFEException {
        skfeLogger.logp(skfeConstants.SKFE_LOGGER,Level.FINE, classname, "processAuthenticationData", skfeCommon.getMessageProperty("FIDO-MSG-5013"), "");

        try {
            byte[] signDataBytes = Base64.decodeBase64(signData);
            int sdL = signDataBytes.length;

            //  userpresense byte
            this.usertouch = (int) signDataBytes[0];
            skfeLogger.logp(skfeConstants.SKFE_LOGGER,Level.FINE, classname, "processAuthenticationData",
                    skfeCommon.getMessageProperty("FIDO-MSG-5026"), this.usertouch);

            //  counter
            int tot = 1;
            byte[] counterValue = new byte[skfeConstants.COUNTER_VALUE_BYTES];
            System.arraycopy(signDataBytes, tot, counterValue, 0, skfeConstants.COUNTER_VALUE_BYTES);
            tot += skfeConstants.COUNTER_VALUE_BYTES;
            this.counter = Integer.parseInt(Hex.encodeHexString(counterValue), 16);
            skfeLogger.logp(skfeConstants.SKFE_LOGGER,Level.FINE, classname, "processAuthenticationData",
                    skfeCommon.getMessageProperty("FIDO-MSG-5027"), this.counter);

            //  signaturebytes
            byte[] signatureBytes = new byte[sdL - 1 - skfeConstants.COUNTER_VALUE_BYTES];
            System.arraycopy(signDataBytes, tot, signatureBytes, 0, signatureBytes.length);
            skfeLogger.logp(skfeConstants.SKFE_LOGGER,Level.FINE, classname, "processAuthenticationData",
                    skfeCommon.getMessageProperty("FIDO-MSG-5019"),
                    Hex.encodeHexString(signatureBytes));

            //  create the object that has been signed
            //  get challenge parameter
            String bdhash = skfeCommon.getDigest(new String(Base64.decodeBase64(browserData), "UTF-8"), "SHA-256");
            skfeLogger.logp(skfeConstants.SKFE_LOGGER,Level.FINE, classname, "processAuthenticationData",
                    skfeCommon.getMessageProperty("FIDO-MSG-5022"),
                    Hex.encodeHexString(Base64.decodeBase64(bdhash)));

            String appIDHash = skfeCommon.getDigest(appid, "SHA-256");
            skfeLogger.logp(skfeConstants.SKFE_LOGGER,Level.FINE, classname, "processAuthenticationData",
                    skfeCommon.getMessageProperty("FIDO-MSG-5021"),
                    Hex.encodeHexString(Base64.decodeBase64(appIDHash)));

            String objectSigned = objectTBS(appIDHash, signDataBytes[0], this.counter, bdhash);
            skfeLogger.logp(skfeConstants.SKFE_LOGGER,Level.FINE, classname, "processAuthenticationData",
                    skfeCommon.getMessageProperty("FIDO-MSG-5023"),
                    Hex.encodeHexString(Base64.decodeBase64(objectSigned)));

            //  verify signature; return counter received and userpresence or null on error
            //  convert publickey[] to PublicKey
            byte[] publickeyBytes = Base64.decodeBase64(userPublicKeyB64);
            KeyFactory kf = KeyFactory.getInstance("ECDSA", "BCFIPS");
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(publickeyBytes);
            PublicKey pub = kf.generatePublic(pubKeySpec);
            if (cryptoCommon.verifySignature(signatureBytes, pub, objectSigned)) {
                skfeLogger.logp(skfeConstants.SKFE_LOGGER,Level.FINE, classname, "processAuthenticationData",
                        skfeCommon.getMessageProperty("FIDO-MSG-5024"), "");
                return true;
            } else {
                skfeLogger.logp(skfeConstants.SKFE_LOGGER,Level.SEVERE, classname, "processAuthenticationData",
                        skfeCommon.getMessageProperty("FIDO-ERR-5005"), "");
                return false;
            }
        } catch (NumberFormatException | UnsupportedEncodingException | InvalidKeySpecException | 
                NoSuchAlgorithmException | NoSuchProviderException ex) {

            skfeLogger.logp(skfeConstants.SKFE_LOGGER,Level.SEVERE, classname, "processAuthenticationData",
                    skfeCommon.getMessageProperty("FIDO-ERR-5006"), ex.getLocalizedMessage());
            throw new SKFEException(ex);
        }
    }

    /**
     *
     * @param appParam
     * @param userpresence
     * @param counterValue
     * @param challParam
     * @return
     */
    public static String objectTBS(String appParam, byte userpresence, int counterValue, String challParam) {

        byte[] appparam = Base64.decodeBase64(appParam);
        int apL = appparam.length;
        byte[] challparam = Base64.decodeBase64(challParam);
        int cpL = challparam.length;

        byte[] ob2sign = new byte[apL + 1 + skfeConstants.COUNTER_VALUE_BYTES + cpL];
        int tot = 0;

        System.arraycopy(appparam, 0, ob2sign, 0, apL);
        tot += apL;
        ob2sign[tot] = userpresence;
        tot++;
        System.arraycopy(int2bytearray(counterValue), 0, ob2sign, tot, 4);
        tot += 4;
        System.arraycopy(challparam, 0, ob2sign, tot, cpL);
        tot += cpL;

        return Base64.encodeBase64String(ob2sign);
    }

    /**
     *
     * @param a
     * @return
     */
    public static byte[] int2bytearray(int a) {
        if (a >= Integer.MAX_VALUE) {
            System.out.println("Counter wrap around reached...");
            a = 0;
        }

        return ByteBuffer.allocate(4).putInt(a).array();
    }
}