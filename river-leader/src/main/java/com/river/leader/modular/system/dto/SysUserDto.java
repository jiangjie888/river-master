package com.river.leader.modular.system.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

/**
 * @program: river-master
 * @description:
 * @author: jason
 * @create: 2018-11-27 15:35
 **/

@Data
public class SysUserDto {

    private Long id;
    @NotBlank(message = "账号不允许为空")
    private String userAccout;

    //@Max(value=120,message="年龄最大不能查过120")
    @Email(message="邮箱格式错误")
    private String email;

    private Date lastLoginTime;

    @Length(max = 200, message="备注最大长度不能超过200")
    private String remarks;

    private Integer userStatus;
    private String username;
    private String password;
    //private String nameCn;

    //@AssertTrue(message="")
    private Boolean onLine;

    /*@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date deletionTime;
    private Boolean isDeleted;
    private long deleterUserId;


    private Long lastModifierUserId;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date lastModificationTime;

    private Long creatorUserId;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date creationTime;*/
}
