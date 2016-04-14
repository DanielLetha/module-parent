package com.simpletour.service.company.imp;

import com.simpletour.biz.company.IScopeTemplateBiz;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.company.query.ScopeTemplateDaoQuery;
import com.simpletour.domain.company.Permission;
import com.simpletour.domain.company.ScopeTemplate;
import com.simpletour.service.company.IScopeTemplateService;
import com.simpletour.service.company.error.ScopeTemplateServiceError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * User: XuHui/xuhui@simpletour.com
 * Date: 2016/4/8
 * Time: 14:06
 */
@Service
public class ScopeTemplateServiceImp implements IScopeTemplateService {
    @Autowired
    private IScopeTemplateBiz scopeTemplateBiz;
    @Autowired
    private IPermissionBiz permissionBiz;

    @Override
    public Optional<ScopeTemplate> addScopeTemplate(ScopeTemplate scopeTemplate) {
        if(scopeTemplate==null)
            throw new BaseSystemException(ScopeTemplateServiceError.SCOPE_TEMPLATE_NULL);
        if(scopeTemplate.getId()!=null)
            throw new BaseSystemException(ScopeTemplateServiceError.SCOPE_TEMPLATE_DATA_ERROR);
        if(scopeTemplate.getPermissions()==null||scopeTemplate.getPermissions().isEmpty())
            throw new BaseSystemException(ScopeTemplateServiceError.SCOPE_TEMPLATE_PERMISSION_NULL);
        if(scopeTemplate.getPermissions().stream()
                .anyMatch(tmp-> !permissionBiz.isExisted(tmp.getId()))) {
            throw new BaseSystemException(ScopeTemplateServiceError.SCOPE_TEMPLATE_PERMISSION_NOT_EXIST);
        }
        List<Permission> permissions=scopeTemplate.getPermissions().stream().map(tmp-> permissionBiz.getById(tmp.getId())).collect(Collectors.toList());
        scopeTemplate.setPermissions(permissions);
        return Optional.ofNullable(scopeTemplateBiz.addScopeTemplate(scopeTemplate));
    }

    @Override
    public Optional<ScopeTemplate> updateScopeTemplate(ScopeTemplate scopeTemplate) {
        if(scopeTemplate==null||scopeTemplate.getId()==null)
            throw new BaseSystemException(ScopeTemplateServiceError.SCOPE_TEMPLATE_NULL);
        if(!scopeTemplateBiz.isExisted(scopeTemplate.getId()))
            throw new BaseSystemException(ScopeTemplateServiceError.SCOPE_TEMPLATE_NOT_EXIST);
        if(scopeTemplate.getPermissions()==null||scopeTemplate.getPermissions().isEmpty())
            throw new BaseSystemException(ScopeTemplateServiceError.SCOPE_TEMPLATE_PERMISSION_NULL);
        if(scopeTemplate.getPermissions().stream()
                .anyMatch(tmp-> !permissionBiz.isExisted(tmp.getId()))) {
            throw new BaseSystemException(ScopeTemplateServiceError.SCOPE_TEMPLATE_PERMISSION_NOT_EXIST);
        }
        List<Permission> permissions=scopeTemplate.getPermissions().stream().map(tmp-> permissionBiz.getById(tmp.getId())).collect(Collectors.toList());
        scopeTemplate.setPermissions(permissions);
        return Optional.ofNullable(scopeTemplateBiz.updateScopeTemplate(scopeTemplate));
    }

    @Override
    public Optional<ScopeTemplate> getScopeTemplateById(Long id) {
        if(id==null)
            throw new BaseSystemException(ScopeTemplateServiceError.SCOPE_TEMPLATE_NULL);
        return Optional.ofNullable(scopeTemplateBiz.getScopeTemplateById(id));
    }

    @Override
    public void deleteScopeTemplate(long id) {
        if(!scopeTemplateBiz.isExisted(id))
            throw new BaseSystemException(ScopeTemplateServiceError.SCOPE_TEMPLATE_NOT_EXIST);
        scopeTemplateBiz.deleteScopeTemplate(id);
    }

    @Override
    public DomainPage findScopeTemplatePage(ScopeTemplateDaoQuery query) {
        return scopeTemplateBiz.findScopeTemplatePage(query);
    }

    @Override
    public List<ScopeTemplate> findScopeTemplateList(ScopeTemplateDaoQuery query) {
        return scopeTemplateBiz.findScopeTemplateList(query);
    }
}
