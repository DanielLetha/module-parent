package com.simpletour.dao.company.support;

import com.simpletour.common.core.dao.IBaseDao;
import com.simpletour.common.core.dao.query.ConditionOrderByQuery;

import java.util.Map;

/**
 * Author：XuHui/xuhui@simpletour.com
 * Brief：
 * Date: 2016/4/8
 * Time: 10:31
 */
public class JoinConditionOrderByQuery extends ConditionOrderByQuery {
    public JoinConditionOrderByQuery(){
        this.addSortByField("c.id", IBaseDao.SortBy.DESC);
    }
    public Map<String,String> getJoinMap(){
        return null;
    }
}
