package com.simpletour.dao.company.query;

import com.simpletour.commons.data.dao.query.condition.FieldMatcher;
import com.simpletour.dao.company.support.JoinConditionOrderByQuery;

import java.util.HashMap;
import java.util.Map;

/**
 * User: XuHui/xuhui@simpletour.com
 * Date: 2016/4/8
 * Time: 11:45
 */
public class ScopeTemplateDaoQuery extends JoinConditionOrderByQuery {

    public static FieldMatcher fieldMatcher = new FieldMatcher();
    static{
        fieldMatcher.put("name","c.name")
                .put("id","c.id")
                .put("moduleName","p.module.name")
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

    public ScopeTemplateDaoQuery(){
    }

}
