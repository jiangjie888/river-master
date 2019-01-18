package com.river.leader.modular.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.river.leader.config.properties.AppProperties;
import com.river.leader.core.common.constant.BizExceptionEnum;
import com.river.leader.core.common.constant.factory.PageFactory;
import com.river.leader.core.model.exception.ServiceException;
import com.river.leader.core.model.page.PageQuery;
import com.river.leader.core.model.page.PageResultPlus;
import com.river.leader.core.model.reqres.response.ResponseData;
import com.river.leader.core.utils.ToolUtil;
import com.river.leader.modular.system.model.SysRole;
import com.river.leader.modular.system.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @program: river-master
 * @description:
 * @author: jason
 * @create: 2018-11-26 14:34
 **/

@Api(tags = "角色信息管理")
@RequestMapping("/roles")
@Controller
public class RoleController {

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private IRoleService roleService;


    @ResponseBody
    @ApiOperation(value = "分页查询角色信息")
    //@BussinessLog(value = "删除用户", key = "userId", dict = UserDict.class)
    @RequestMapping(value = "/listPage", method = RequestMethod.POST, produces = "application/json")
    public PageResultPlus<SysRole> listPage(@RequestBody PageQuery pageParam) {
        Page<SysRole> page = PageFactory.createPage(pageParam);
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.eq("isDeleted",0);
        //wrapper.lt("creationTime", ToolUtil.getCreateTimeBefore(messageProperties.getCheckInterval()));
        //wrapper.and(q->q.eq("status", MessageStatusEnum.WAIT_VERIFY.name()));
        // .eq("status", MessageStatusEnum.WAIT_VERIFY.name());
        Page<SysRole> pageResults = (Page<SysRole>) this.roleService.page(page,wrapper);
        return new PageResultPlus<>(pageResults);
    }


    @ResponseBody
    @ApiOperation(value = "根据用户Id获取角色信息")
    @RequestMapping(value = "/listByUserId", method = RequestMethod.POST, produces = "application/json")
    public ResponseData getEntityById(@RequestParam Long userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        return ResponseData.success(this.roleService.selectRolesByUserid(userId));
    }

    @ResponseBody
    @ApiOperation(value = "删除角色信息")
    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json")
    public ResponseData delete(@RequestParam String roleId) {
        if (ToolUtil.isEmpty(roleId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.roleService.delRoleById(roleId);
        return ResponseData.success();
    }

}

