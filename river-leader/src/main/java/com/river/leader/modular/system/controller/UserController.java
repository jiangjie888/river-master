package com.river.leader.modular.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.river.leader.config.properties.AppProperties;
import com.river.leader.core.common.constant.BizExceptionEnum;
import com.river.leader.core.common.constant.Const;
import com.river.leader.core.common.constant.factory.PageFactory;
import com.river.leader.core.common.constant.state.ManagerStatus;
import com.river.leader.core.model.exception.ServiceException;
import com.river.leader.core.model.page.PageQuery;
import com.river.leader.core.model.page.PageResultPlus;
import com.river.leader.core.model.reqres.response.ResponseData;
import com.river.leader.core.shiro.ShiroKit;
import com.river.leader.core.utils.ToolUtil;
import com.river.leader.modular.system.dto.SysUserDto;
import com.river.leader.modular.system.model.SysUser;
import com.river.leader.modular.system.service.IUserService;
import com.river.leader.modular.system.warpper.UserWarpper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @program: river-master
 * @description:
 * @author: jason
 * @create: 2018-11-26 14:34
 **/

@Api(tags = "用户信息管理")
@RequestMapping("/users")
@Controller
public class UserController {

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private IUserService userService;


    @ResponseBody
    @ApiOperation(value = "分页查询用户信息")
    //@BussinessLog(value = "删除用户", key = "userId", dict = UserDict.class)
    @RequestMapping(value = "/listPage", method = RequestMethod.POST, produces = "application/json")
    public PageResultPlus<SysUser> listPage(@RequestBody PageQuery pageParam) {
        Page<SysUser> page = PageFactory.createPage(pageParam);
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("isDeleted",0);
        //wrapper.lt("creationTime", ToolUtil.getCreateTimeBefore(messageProperties.getCheckInterval()));
        //wrapper.and(q->q.eq("status", MessageStatusEnum.WAIT_VERIFY.name()));
        // .eq("status", MessageStatusEnum.WAIT_VERIFY.name());
        Page<SysUser> sysUserPage = (Page<SysUser>) this.userService.page(page,wrapper);
        return new PageResultPlus<>(sysUserPage);
    }


    @ResponseBody
    @ApiOperation(value = "查询用户列表信息")
    @RequestMapping(value = "/list", method = RequestMethod.POST, produces = "application/json")
    public ResponseData list(@RequestParam(required = false) String name, @RequestParam(required = false) String beginTime, @RequestParam(required = false) String endTime) {
        List<Map<String, Object>> users = userService.selectUsers(name, beginTime, endTime);
        Object result = new UserWarpper(users).wrap();
        return ResponseData.success(result);
    }

    @ResponseBody
    @ApiOperation(value = "查询用户列表信息2")
    @RequestMapping(value = "/list2", method = RequestMethod.POST, produces = "application/json")
    public ResponseData list2(@RequestBody SysUser user) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("isDeleted",0);
        wrapper.and(q->q.eq("username", user.getUsername()));
        List<SysUser> result = userService.list(wrapper);
        return ResponseData.success(result);
    }


    @ResponseBody
    @ApiOperation(value = "根据Id获取单个用户信息")
    @RequestMapping(value = "/getEntityById/{userId}", method = RequestMethod.POST, produces = "application/json")
    public ResponseData getEntityById(@PathVariable Integer userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        return ResponseData.success(this.userService.getById(userId));
    }

    @ResponseBody
    @ApiOperation(value = "新增or更新用户信息")
    @RequestMapping(value = "/insetOrUpdate", method = RequestMethod.POST, produces = "application/json")
    public ResponseData insetOrUpdate(@RequestBody @Valid SysUserDto userDto, BindingResult bindingResult) {
        //获取会话用户
        //Integer userId = ShiroKit.getUser().getId();
        /*if (ToolUtil.isEmpty(userId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }*/
        if (bindingResult.hasErrors()){
            String message = bindingResult.getFieldError().getDefaultMessage();
            //自定义的返回，并将错误信息返回
            return ResponseData.error(message);
            /*List<ObjectError> errorList = result.getAllErrors();
            for(ObjectError error : errorList){
                System.out.println(error.getDefaultMessage());
            }
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);*/

        }


        if(ToolUtil.isEmpty(userDto.getId()) || userDto.getId()==0){
            //新增
            SysUser theUser = userService.getByAccount(userDto.getUserAccout());
            // 判断账号是否重复
            if (theUser != null) {
                throw new ServiceException(BizExceptionEnum.USER_ALREADY_REG);
            }
            // 完善账号信息
            //userDto.setSalt(ShiroKit.getRandomSalt(5));
            userDto.setPassword(ShiroKit.md5(userDto.getPassword(), "abcdefg"));
            userDto.setUserStatus(ManagerStatus.OK.getCode());
            //entity.setCreatorUserId(new Date());

            SysUser entity = new SysUser();
            BeanUtils.copyProperties(userDto, entity);
            this.userService.save(entity);
        } else {
            //修改
            SysUser entity = userService.getByAccount(userDto.getUserAccout());
            // 没有此用户
            if (entity == null) {
                throw new ServiceException(BizExceptionEnum.NO_THIS_USER);
            }
            entity.setEmail(userDto.getEmail());
            this.userService.updateById(entity);
        }
        return ResponseData.success();
    }


    @ResponseBody
    @ApiOperation(value = "删除用户信息")
    @RequestMapping(value = "/delete", method = { RequestMethod.GET }, produces="application/json;charset=UTF-8")
    public ResponseData delete(@RequestParam Integer userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        //不能删除超级管理员
        if (userId.equals(Const.ADMIN_ID)) {
            throw new ServiceException(BizExceptionEnum.CANT_DELETE_ADMIN);
        }
        assertAuth(userId);
        this.userService.setStatus(userId, ManagerStatus.DELETED.getCode());
        return ResponseData.success();
    }

    /*@ResponseBody
    @ApiOperation(value = "修改密码")
    @RequestMapping(value = { "/chppwd" }, method = RequestMethod.POST, produces = "application/json")
    public ResponseData changePwd(@RequestParam String oldPwd, @RequestParam String newPwd, @RequestParam String rePwd) {
        if (!newPwd.equals(rePwd)) {
            throw new ServiceException(BizExceptionEnum.TWO_PWD_NOT_MATCH);
        }
        Integer userId = ShiroKit.getUser().getId();
        SysUser user = userService.getById(userId);
        String oldMd5 = ShiroKit.md5(oldPwd, "abcdefg");
        if (user.getPassword().equals(oldMd5)) {
            String newMd5 = ShiroKit.md5(newPwd, "abcdefg");
            user.setPassword(newMd5);
            user.updateById();
            return ResponseData.success();
        } else {
            throw new ServiceException(BizExceptionEnum.OLD_PWD_NOT_RIGHT);
        }
    }*/


    /**
     * 重置管理员的密码
     */
    /*@RequestMapping("/reset")
    @BussinessLog(value = "重置管理员密码", key = "userId", dict = UserDict.class)
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public ResponseData reset(@RequestParam Integer userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        assertAuth(userId);
        User user = this.userService.selectById(userId);
        user.setSalt(ShiroKit.getRandomSalt(5));
        user.setPassword(ShiroKit.md5(Const.DEFAULT_PWD, user.getSalt()));
        this.userService.updateById(user);
        return SUCCESS_TIP;
    }*/


    @ResponseBody
    @ApiOperation(value = "上传图片")
    @RequestMapping(value = { "/upload" }, method = RequestMethod.POST)
    public String upload(@RequestPart("file") MultipartFile picture) {

        String pictureName = UUID.randomUUID().toString() + "." + ToolUtil.getFileSuffix(picture.getOriginalFilename());
        try {
            String fileSavePath = appProperties.getFileUploadPath();
            picture.transferTo(new File(fileSavePath + pictureName));
        } catch (Exception e) {
            throw new ServiceException(BizExceptionEnum.UPLOAD_ERROR);
        }
        return pictureName;
    }

    /**
     * 判断当前登录的用户是否有操作这个用户的权限(默认只有admin才有权限)
     */
    private void assertAuth(Integer userId) {
        if (ShiroKit.isAdmin()) {
            return;
        } else  {
            throw new ServiceException(BizExceptionEnum.NO_PERMITION);
        }
    }
}

