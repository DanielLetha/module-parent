package com.simpletour.biz.company.imp;

import com.simpletour.biz.company.IScopeTemplateBiz;
import com.simpletour.biz.company.error.ScopeTemplateBizError;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.company.IScopeTemplateDao;
import com.simpletour.dao.company.query.ScopeTemplateDaoQuery;
import com.simpletour.domain.company.ScopeTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Author：XuHui/xuhui@simpletour.com
 * Brief：
 * Date: 2016/4/8
 * Time: 19:14
 */
@Component
public class ScopeTemplateBizImp implements IScopeTemplateBiz {
    @Autowired
    private IScopeTemplateDao scopeTemplateDao;

    private boolean scopeTemplateValid(ScopeTemplate scopeTemplate){
        List<ScopeTemplate> scopeTemplates=findScopeTemplateByName(scopeTemplate.getName());
        if(scopeTemplates==null||scopeTemplates.isEmpty())
            return true;
        if(scopeTemplate.getId()==null)
            return false;
        return !scopeTemplates.stream().anyMatch(tmp-> !tmp.getId().equals(scopeTemplate.getId()));
    }

    @Override
    public ScopeTemplate addScopeTemplate(ScopeTemplate scopeTemplate) {
        if(!scopeTemplateValid(scopeTemplate))
            throw new BaseSystemException(ScopeTemplateBizError.SCOPE_TEMPLATE_NAME_EXIST);
        return scopeTemplateDao.save(scopeTemplate);
    }

    @Override
    public ScopeTemplate updateScopeTemplate(ScopeTemplate scopeTemplate) {
        if(!scopeTemplateValid(scopeTemplate))
            throw new BaseSystemException(ScopeTemplateBizError.SCOPE_TEMPLATE_NAME_EXIST);
        return scopeTemplateDao.save(scopeTemplate);
    }

    @Override
    public void deleteScopeTemplate(long id) {
        scopeTemplateDao.removeEntityById(ScopeTemplate.class,id);
    }

    @Override
    public DomainPage findScopeTemplatePage(ScopeTemplateDaoQuery query) {
        return scopeTemplateDao.getEntitiesPagesByQuery(ScopeTemplate.class,query);
    }

    @Override
    public List<ScopeTemplate> findScopeTemplateList(ScopeTemplateDaoQuery query) {
        return scopeTemplateDao.getEntitiesByQuery(ScopeTemplate.class,query);
    }

    @Override
    public List<ScopeTemplate> findScopeTemplateByName(String name) {
        if(name==null||name.isEmpty()){
            throw new BaseSystemException(ScopeTemplateBizError.SCOPE_TEMPLATE_NULL);
        }
        return scopeTemplateDao.getEntitiesByField(ScopeTemplate.class,"name",name);
    }

    @Override
    public ScopeTemplate getScopeTemplateById(long id) {
        return scopeTemplateDao.getEntityById(ScopeTemplate.class,id);
    }

    @Override
    public boolean isExisted(Long id) {
        if(id==null)
            throw new BaseSystemException(ScopeTemplateBizError.SCOPE_TEMPLATE_NULL);
        return getScopeTemplateById(id)!=null;
    }
}
