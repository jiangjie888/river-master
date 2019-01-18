package com.river.leader.modular.system.warpper;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.river.leader.core.common.constant.factory.ConstantFactory;
import com.river.leader.core.common.warpper.BaseControllerWrapper;
import com.river.leader.core.model.page.PageResultPlus;

import java.util.List;
import java.util.Map;

/**
 * 用户管理的包装类
 */
public class UserWarpper extends BaseControllerWrapper {

    public UserWarpper(Map<String, Object> single) {
        super(single);
    }

    public UserWarpper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public UserWarpper(Page<Map<String, Object>> page) {
        super(page);
    }

    public UserWarpper(PageResultPlus<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        //map.put("sexName", ConstantFactory.me().getSexName((Integer) map.get("sex")));
        //map.put("roleName", ConstantFactory.me().getRoleName((String) map.get("roleid")));
        //map.put("deptName", ConstantFactory.me().getDeptName((Integer) map.get("deptid")));
        map.put("statusName", ConstantFactory.me().getStatusName((Integer) map.get("userStatus")));
    }

}
