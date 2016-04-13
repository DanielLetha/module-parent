package com.simpletour.service.company;

import com.simpletour.common.core.domain.DomainPage;
import com.simpletour.dao.company.query.ScopeTemplateDaoQuery;
import com.simpletour.domain.company.ScopeTemplate;

import java.util.List;
import java.util.Optional;

/**
 * User: XuHui/xuhui@simpletour.com
 * Date: 2016/4/8
 * Time: 14:02
 */
public interface IScopeTemplateService {
    /**
     * 添加权限范围
     * throws
     *  ScopeTemplateServiceError.SCOPE_TEMPLATE_NULL 权限范围为空
     *  ScopeTemplateServiceError.SCOPE_TEMPLATE_DATA_ERROR 权限范围id不为空
     *  ScopeTemplateServiceError.SCOPE_TEMPLATE_PERMISSION_NULL 权限范围权限列表为空
     *  ScopeTemplateServiceError.SCOPE_TEMPLATE_PERMISSION_NOT_EXIST 权限范围中存在权限不存在
     *  ScopeTemplateBizError.SCOPE_TEMPLATE_NAME_EXIST 名称已存在
     * @param scopeTemplate
     * @return
     */
    Optional<ScopeTemplate> addScopeTemplate(ScopeTemplate scopeTemplate);

    /**
     * 更新权限范围
     * throws
     *  ScopeTemplateServiceError.SCOPE_TEMPLATE_NULL 权限范围为空或者id为空
     *  ScopeTemplateServiceError.SCOPE_TEMPLATE_NOT_EXIST 权限范围不存在
     *  ScopeTemplateServiceError.SCOPE_TEMPLATE_PERMISSION_NULL 权限范围权限列表为空
     *  ScopeTemplateServiceError.SCOPE_TEMPLATE_PERMISSION_NOT_EXIST 权限范围权限列表中存在权限不存在
     *  ScopeTemplateBizError.SCOPE_TEMPLATE_NAME_EXIST 名称已存在
     * @param scopeTemplate
     * @return
     */
    Optional<ScopeTemplate> updateScopeTemplate(ScopeTemplate scopeTemplate);

    /**
     * 根据id获取权限范围
     * throws
     *  ScopeTemplateServiceError.SCOPE_TEMPLATE_NULL id为空
     * @param moduleId
     * @return
     */
    Optional<ScopeTemplate> getScopeTemplateById(Long moduleId);

    /**
     * 根据id删除权限范围
     * throws
     *  ScopeTemplateServiceError.SCOPE_TEMPLATE_NOT_EXIST 权限范围不存在
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
     * 根据query查询权限范围列表
     * @param query
     * @return
     */
    List<ScopeTemplate> findScopeTemplateList(ScopeTemplateDaoQuery query);

}
