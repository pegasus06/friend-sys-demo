package com.ryan.userCenter.domain.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class TeamUserVO implements Serializable {

    private static final long serialVersionUID = -2835941882806536293L;
    private Long id;
    private String name;
    private String description;
    private Integer maxNum;
    private Integer currentNum;
    private UserVo createUser;

}
