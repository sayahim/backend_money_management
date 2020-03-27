package com.himorfosis.moneymanagement.model;

import java.io.Serializable;

public class AuthenticateResponse implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;

    private final String SecurityToken;

    public AuthenticateResponse(String securityToken) {

        this.SecurityToken = securityToken;
    }

    public String getToken() {

        return this.SecurityToken;

    }

}
