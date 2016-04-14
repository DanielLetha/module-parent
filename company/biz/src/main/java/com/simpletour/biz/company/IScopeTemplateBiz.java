package com.simpletour.biz.company;

import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.dao.company.query.ScopeTemplateDaoQuery;
import com.simpletour.domain.company.ScopeTemplate;

import java.util.List;

/**
 * Author：XuHui/xuhui@simpletour.com
 * Brief：
 * Date: 2016/4/8
 * Time: 19:07
 */
public interface IScopeTemplateBiz {

    /**
     * 添加权限范围
     * throws：
     *  ScopeTemplateBizError.SCOPE_TEMPLATE_NAME_EXIST 名称已存在
     * @param scopeTemplate
     * @return
     */
    ScopeTemplate addScopeTemplate(ScopeTemplate scopeTemplate);

    /**
     * 更新权限范围
     * throws：
     *  ScopeTemplateBizError.SCOPE_TEMPLATE_NAME_EXIST 名称已存在
     * @param scopeTemplate
     * @return
     */
    ScopeTemplate updateScopeTemplate(ScopeTemplate scopeTemplate);

    /**
     * 删除权限范围
     * @param id
     */
    void deleteScopeTemplate(long id);

    /**
     * 根据query查询权限范围分页
     * @param query
     * @return
     */
    DomainPage findScopeTemplatePage(ScopeTemplateDaoQuery query);

    /**
     * 根据query查询权限范围list
     * @param query
     * @return
     */
    List<ScopeTemplate> findScopeTemplateList(ScopeTemplateDaoQuery query);

    /**
     * 根据权限范围名称（全匹配）查询权限范围
     * @param name
     * @return
     */
    List<ScopeTemplate> findScopeTemplateByName(String name);

    /**
     * 根据id获取权限范围
     * @param id
     * @return
     */
    ScopeTemplate getScopeTemplateById(long id);

    /**
     * 根据id判断权限范围是否存在
     * @param id
     * @return
     */
    boolean isExisted(Long id);
}
