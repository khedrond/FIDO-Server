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
 * $Date$
 * $Revision$
 * $Author$
 * $URL$
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
 * Local interface for authenticateLdapUserBean
 *
 */
package com.strongkey.auth.txbeans;

import com.strongauth.skce.utilities.SKCEException;
import javax.ejb.Local;

/**
 * Local interface for authenticateLdapUserBean
 */
@Local
public interface authenticateLdapUserBeanLocal {
    
    /**
     * This method authenticates a credential - username and password - against the 
     * configured LDAP directory.  Only LDAP-based authentication is currently
     * supported; both Active Directory and a standards-based, open-source LDAP
     * directories are supported.  For the later, this has been tested with
     * OpenDS 2.0 (https://docs.opends.org).
     *
     * @param username String containing the credential's username
     * @param password String containing the user's password
     * @return boolean value indicating either True (for authenticated) or False
     * (for unauthenticated or failure in processing)
     * @throws com.strongauth.skce.utilities.SKCEException
     */
    boolean execute(Long did,String username,
                    String password) throws SKCEException;
}