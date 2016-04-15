package com.simpletour.biz.company;

import com.simpletour.biz.company.data.ScopeData;
import com.simpletour.biz.company.error.ScopeTemplateBizError;
import com.simpletour.commons.data.dao.query.condition.AndConditionSet;
import com.simpletour.commons.data.dao.query.condition.Condition;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.company.IModuleDao;
import com.simpletour.dao.company.IScopeTemplateDao;
import com.simpletour.dao.company.query.ScopeTemplateDaoQuery;
import com.simpletour.domain.company.Module;
import com.simpletour.domain.company.ScopeTemplate;
import com.simpletour.commons.test.generator.IDataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

/**
 * User: XuHui/xuhui@simpletour.com
 * Date: 2016/4/9
 * Time: 15:23
 */
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class ScopeTemplateBizTest extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    private IScopeTemplateBiz scopeTemplateBiz;
    @Autowired
    private IModuleDao moduleDao;
    @Autowired
    private IScopeTemplateDao scopeTemplateDao;

    private IDataGenerator data;

    @BeforeClass
    public void setUp(){
        data=new ScopeData(moduleDao,scopeTemplateDao);
        data.createData();
    }

    @AfterClass
    public void tearDown(){
        int size =data.getDomains().size();
        for(int i=size-1;i>=0;i--){
            moduleDao.remove(data.getDomains().get(i));
        }
    }

    //正常添加
    @Test
    public void testAddScopeTemplate(){
        ScopeTemplate scopeTemplate=generateScopeTemplate();
        scopeTemplate=scopeTemplateBiz.addScopeTemplate(scopeTemplate);
        Assert.assertNotNull(scopeTemplate);
        Assert.assertNotNull(scopeTemplate.getId());
    }

    //测试添加权限范围名称已存在
    @Test
    public void testAddScopeTemplateNameExist(){
        ScopeTemplate scopeTemplate=generateScopeTemplate();
        ScopeTemplate scopeTemplate1= (ScopeTemplate) data.getDomains(ScopeTemplate.class).get(0);
        scopeTemplate.setName(scopeTemplate1.getName());
        try{
            scopeTemplateBiz.addScopeTemplate(scopeTemplate);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(), ScopeTemplateBizError.SCOPE_TEMPLATE_NAME_EXIST);
        }
    }

    //正常更新
    @Test
    public void testUpdate(){
        ScopeTemplate scopeTemplate=generateScopeTemplate();
        scopeTemplate=scopeTemplateBiz.addScopeTemplate(scopeTemplate);
        scopeTemplate.setName(generateName("update"));
        scopeTemplateBiz.updateScopeTemplate(scopeTemplate);
    }

    //更新权限范围名称已经存在
    @Test
    public void testUpdateFail(){
        ScopeTemplate scopeTemplate=generateScopeTemplate();
        scopeTemplate=scopeTemplateBiz.addScopeTemplate(scopeTemplate);
        ScopeTemplate scopeTemplate1= (ScopeTemplate) data.getDomains(ScopeTemplate.class).get(0);
        scopeTemplate.setName(scopeTemplate1.getName());
        try{
            scopeTemplateBiz.updateScopeTemplate(scopeTemplate);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),ScopeTemplateBizError.SCOPE_TEMPLATE_NAME_EXIST);
        }
        scopeTemplate.setName(generateName("update "));
        scopeTemplateBiz.updateScopeTemplate(scopeTemplate);
    }

    //测试查询权限范围
    @Test
    public void testQuery(){
        ScopeTemplateDaoQuery query=new ScopeTemplateDaoQuery();
        query.setPageIndex(1);
        query.setPageSize(10);
        DomainPage page=scopeTemplateBiz.findScopeTemplatePage(query);
        System.out.println(page.getDomains().size());

        AndConditionSet condition=new AndConditionSet();
        condition.addCondition("moduleName","%test%", Condition.MatchType.like);
        query.setCondition(condition);
        page=scopeTemplateBiz.findScopeTemplatePage(query);
        System.out.println(page.getDomains().size());

        List<ScopeTemplate> list=scopeTemplateBiz.findScopeTemplateList(query);
        System.out.println(list.size());
    }

    private ScopeTemplate generateScopeTemplate(){
        ScopeTemplate scopeTemplate = new ScopeTemplate(generateName("scope name"), generateName("remark"));
        Module module= (Module) data.getDomains(Module.class).get(0);
        scopeTemplate.setPermissions(module.getPermissions());
        return scopeTemplate;
    }

    private static String generateName(String name) {
        return System.currentTimeMillis() + name;
    }
}
