package com.ryan.userCenter.domain.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginRequest implements Serializable {


    private static final long serialVersionUID = -921994975818582322L;

    private String userAccount;
    private String userPassword;
}
