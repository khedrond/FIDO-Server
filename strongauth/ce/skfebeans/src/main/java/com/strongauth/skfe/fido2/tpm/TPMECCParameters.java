/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License, as published by the Free Software Foundation and
 * available at http://www.fsf.org/licensing/licenses/lgpl.html,
 * version 2.1 or above.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * Copyright (c) 2001-2018 StrongAuth, Inc.
 *
 * $Date: 
 * $Revision:
 * $Author$
 * $URL: 
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
 *
 *
 */
package com.strongauth.skfe.fido2.tpm;

/**
 *
 * @author mishimoto
 */
public class TPMECCParameters implements TPMParameters {
    private final TPMSymmetricStruct symmBits;
    
    private final TPMScheme scheme;
    
    private final short curveID;
    
    //These variables are a bit of a stub in the specification, as they do not
    //really do anything and are suppose to be hardcoded to null values
    private final TPMScheme kdfScheme;
    
    
    public TPMECCParameters(TPMSymmetricStruct symmBits, TPMScheme scheme, 
            short curveID, TPMScheme kdfScheme) {
        this.symmBits = symmBits;
        this.scheme = scheme;
        this.curveID = curveID;
        this.kdfScheme = kdfScheme;
    }

    public short getCurveID() {
        return curveID;
    }

    @Override
    public byte[] marshalData() {
        return Marshal.marshalObjects(
                symmBits,
                scheme,
                curveID,
                kdfScheme);
    }
}
