package com.river.leader.core.model.page;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @program: open-smartcloud
 * @description:封装分页结果集
 * @author: jason
 * @create: 2018-11-15 14:34
 **/
@Data
public class PageResultPlus<T> implements Serializable {

    private static final long serialVersionUID = -4071521319254024213L;

    private Long page = 1L;// 要查找第几页
    private Long pageSize = 20L;// 每页显示多少条
    private Long totalPage = 0L;// 总页数
    private Long totalRows = 0L;// 总记录数
    private List<T> rows;// 结果集

    public PageResultPlus() {
    }

    public PageResultPlus(Page<T> page) {
        this.setRows(page.getRecords());
        this.setTotalRows(page.getTotal());
        this.setPage(page.getCurrent());
        this.setPageSize(page.getSize());
    }

}
