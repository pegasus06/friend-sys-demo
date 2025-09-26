package com.ryan.userCenter.domain.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserVo implements Serializable {
    private Long id;
    private String userName;
    private String userAccount;
    private String userAvatar;
    private Integer gender;
}
