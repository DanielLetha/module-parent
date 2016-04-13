package com.simpletour.dao.company.query;

import com.simpletour.common.core.dao.query.condition.FieldMatcher;
import com.simpletour.dao.company.support.JoinConditionOrderByQuery;

import java.util.HashMap;
import java.util.Map;

/**
 * Author:  wangLin
 * Mail  :  wl@simpletour.com
 * Date  :  2016/4/8.
 * Remark:  查询的字段有：公司名称  公司地址 功能名称  对接人名字
 *          全部都是全模糊查询
 */
public class CompanyDaoQuery extends JoinConditionOrderByQuery {

    public static FieldMatcher fieldMatcher = new FieldMatcher();
    static{
        fieldMatcher.put("name","c.name")
                .put("address","c.address")
                .put("buttManName","c.contacts")
                .put("permissionName","p.name");
    }

    public static Map<String, String> joinMap = new HashMap<>();
    static{
        joinMap.put("c.permissions","p");
    }

    @Override
    public Map<String ,String> getJoinMap(){
        return joinMap;
    }

    @Override
    public FieldMatcher getFieldMatcher(){
        return fieldMatcher;
    }

    public CompanyDaoQuery() {
    }
}
