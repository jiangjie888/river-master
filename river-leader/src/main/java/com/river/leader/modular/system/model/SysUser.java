package com.river.leader.modular.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.river.leader.core.model.common.FullAuditedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户实体
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "sys_userinfo")
public class SysUser extends FullAuditedEntity<SysUser> {

	private static final long serialVersionUID = -7311959782558761790L;

	/**
	 * 主键
	 */
	//@TableId(value = "id", type = IdType.UUID)
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	private String userAccout;
	private String email;
	private Date lastLoginTime;
	private String remarks;
	private Integer userStatus;
	private String username;

	@TableField("UserPassword")
	private String password;
	//private String nameCn;
	private Boolean onLine;
	//private String oldPassword;
	//private String newPassword;
	//private Boolean enabled;


	@TableField(exist = false)
	private List<SysRole> roles;
	//private String roleId;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
