package com.simpletour.dao.company.query;

import com.simpletour.commons.data.dao.query.condition.AndConditionSet;
import com.simpletour.commons.data.dao.query.condition.Condition;
import com.simpletour.commons.data.dao.query.condition.FieldMatcher;
import com.simpletour.dao.company.support.JoinConditionOrderByQuery;

import java.util.HashMap;
import java.util.Map;

/**
 * Author：XuHui/xuhui@simpletour.com
 * Brief：
 * Date: 2016/4/7
 * Time: 11:49
 */
public class ModuleDaoQuery extends JoinConditionOrderByQuery {
    public static FieldMatcher fieldMatcher = new FieldMatcher();
    static{
        fieldMatcher.put("name","c.name")
                .put("id","c.id")
                .put("domain","c.domain")
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

    public ModuleDaoQuery(){
    }

    public ModuleDaoQuery(String name){
        AndConditionSet conditionSet=new AndConditionSet();
        setIfNotEmpty(conditionSet,"name",name, Condition.MatchType.like);
        setCondition(conditionSet);
    }
}
