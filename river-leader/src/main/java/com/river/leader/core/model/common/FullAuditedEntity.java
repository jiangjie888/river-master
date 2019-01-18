package com.river.leader.core.model.common;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @program: open-smartcloud
 * @description:
 * @author: jason
 * @create: 2018-10-12 10:56
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class FullAuditedEntity<T extends Model>extends AuditedEntity<T> {

    private static final long serialVersionUID = -4556972271613329267L;
    private Boolean isDeleted;//0:正常 1:删除
    private long deleterUserId;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date deletionTime;
}
