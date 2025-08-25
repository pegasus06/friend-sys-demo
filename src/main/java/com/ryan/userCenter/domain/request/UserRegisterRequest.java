package com.ryan.userCenter.domain.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRegisterRequest implements Serializable {


    private static final long serialVersionUID = 6629599933982191016L;

    private String userAccount;
    private String userPassword;
    private String checkPassword;
}
