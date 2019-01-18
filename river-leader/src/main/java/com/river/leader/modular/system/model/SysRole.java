package com.river.leader.modular.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.river.leader.core.model.common.FullAuditedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 *  角色
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "sys_roleinfo")
public class SysRole extends FullAuditedEntity<SysRole> {

	private static final long serialVersionUID = 4497149010220586111L;

	@TableId(value = "id", type = IdType.UUID)
	private String id;

	private String roleName;
	private String roleCode;
	private Boolean isDefaultRole;
	private String roleDescription;
	//private Long userId;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
