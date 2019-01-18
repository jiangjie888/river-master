package com.river.leader.core.common.constant.factory;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.river.leader.core.model.page.PageQuery;
import com.river.leader.core.utils.ToolUtil;


/**
 * 默认分页参数构建
 */
public class PageFactory {

    /**
     * 排序，升序
     */
    private static final String ASC = "asc";

    /**
     * 排序，降序
     */
    //private static final String DESC = "desc";

    /**
     * 每页大小的param名称
     */
    private static final String PAGE_SIZE_PARAM_NAME = "pageSize";

    /**
     * 第几页的param名称
     */
    private static final String PAGE_NO_PARAM_NAME = "pageNo";

    /**
     * 升序还是降序的param名称
     */
    private static final String SORT_PARAM_NAME = "sort";

    /**
     * 根据那个字段排序的param名称
     */
    private static final String ORDER_BY_PARAM_NAME = "orderBy";

    /**
     * 默认规则的分页
     */
    /*public static <T> Page<T> defaultPage() {

        int pageSize = 20;
        int pageNo = 1;

        HttpServletRequest request = HttpContext.getRequest();

        if (request == null) {
            return new Page<>(pageNo, pageSize);
        } else {
            //每页条数
            String pageSizeString = getFieldValue(request, PAGE_SIZE_PARAM_NAME);
            if (ToolUtil.isNotEmpty(pageSizeString)) {
                pageSize = Integer.valueOf(pageSizeString);
            }

            //第几页
            String pageNoString = getFieldValue(request, PAGE_NO_PARAM_NAME);
            if (ToolUtil.isNotEmpty(pageNoString)) {
                pageNo = Integer.valueOf(pageNoString);
            }

            //获取排序字段和排序类型(asc/desc)
            String sort = getFieldValue(request, SORT_PARAM_NAME);
            String orderByField = getFieldValue(request, ORDER_BY_PARAM_NAME);

            if (ToolUtil.isEmpty(sort)) {
                Page<T> page = new Page<>(pageNo, pageSize);
                page.setDesc("creationTime");
                return page;
            } else {
                Page<T> page = new Page<>(pageNo, pageSize);
                if (ASC.equalsIgnoreCase(sort)) {
                    page.setAsc(orderByField);
                } else {
                    page.setDesc(orderByField);
                }
                return page;
            }
        }

    }*/

    /**
     * 自定义参数的分页
     */
    public static <T> Page<T> createPage(PageQuery pageQuery) {

        int pageSize = 20;
        int pageNo = 1;

        if (pageQuery != null && ToolUtil.isNotEmpty(pageQuery.getPageSize())) {
            pageSize = pageQuery.getPageSize();
        }

        if (pageQuery != null && ToolUtil.isNotEmpty(pageQuery.getPageNo())) {
            pageNo = pageQuery.getPageNo();
        }

        if (pageQuery == null) {
            Page<T> page = new Page<>(pageNo, pageSize);
            page.setDesc("creationTime");
            return page;
        } else {
            if (ToolUtil.isEmpty(pageQuery.getSort())) {
                Page<T> page = new Page<>(pageNo, pageSize);
                page.setDesc("creationTime");
                return page;
            } else {
                Page<T> page = new Page<>(pageNo, pageSize);
                if (ASC.equalsIgnoreCase(pageQuery.getSort())) {
                    page.setAsc(pageQuery.getOrderByField());
                } else {
                    page.setDesc(pageQuery.getOrderByField());
                }
                return page;
            }
        }
    }

    /**
     * 获取参数值，通过param或从requestBody中取
     */
    /*private static String getFieldValue(HttpServletRequest request, String fieldName) {
        String parameter = request.getParameter(fieldName);
        if (parameter == null) {
            RequestData requestData = RequestDataHolder.get();
            if (requestData == null) {
                return null;
            } else {
                Object fieldValue = requestData.get(fieldName);
                if (fieldValue == null) {
                    return null;
                } else {
                    return fieldValue.toString();
                }
            }
        } else {
            return null;
        }
    }*/
}
