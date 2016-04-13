package com.simpletour.biz.company;

import com.simpletour.biz.company.data.ModuleData;
import com.simpletour.biz.company.error.ModuleBizError;
import com.simpletour.common.core.dao.query.condition.AndConditionSet;
import com.simpletour.common.core.dao.query.condition.Condition;
import com.simpletour.common.core.domain.DomainPage;
import com.simpletour.common.core.exception.BaseSystemException;
import com.simpletour.dao.company.IModuleDao;
import com.simpletour.dao.company.query.ModuleDaoQuery;
import com.simpletour.domain.company.Module;
import com.simpletour.domain.company.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * User: XuHui/xuhui@simpletour.com
 * Date: 2016/4/9
 * Time: 14:20
 */
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class ModuleBizTest extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    private IModuleBiz moduleBiz;

    @Autowired
    private IModuleDao moduleDao;

    private ModuleData moduleData;

    @BeforeClass
    public void setUp(){
        moduleData=new ModuleData(moduleDao);
        moduleData.createData();
    }

    @AfterClass
    public void tearDown() throws Exception {
        int size=moduleData.getDomains().size();
        for(int i=size-1;i>=0;i--){
            moduleDao.removeEntity(moduleData.getDomains().get(i));
        }
    }

    //正常添加
    @Test
    public void testAddModule() throws Exception {
        Module module=generateModule();
        Module module1=moduleBiz.addModule(module);
        Assert.assertNotNull(module1);
        Assert.assertNotNull(module1.getId());
        Assert.assertNotNull(module1.getPermissions());
        Assert.assertEquals(module1.getPermissions().size(),2);
    }

    //添加模块名称已经存在
    @Test
    public void testAddModuleNameExist() throws Exception{
        Module module=generateModule();
        module.setName(((Module)moduleData.getDomains(Module.class).get(0)).getName());
        try {
            moduleBiz.addModule(module);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(), ModuleBizError.MODULE_NAME_EXIST);
        }
    }

    //正常更新
    @Test
    public void testUpdateModule(){
        Module module= (Module) moduleData.getDomains(Module.class).get(0);
        String name=generateName("update");
        module.setName(name);
        Module moduleOptional=moduleBiz.updateModule(module);
        Assert.assertNotNull(moduleOptional);
        Assert.assertEquals(moduleOptional.getName(),name);
    }

    //更新模块名称已经存在
    @Test
    public void testUpdateModuleNameExist(){
        Module module= (Module) moduleData.getDomains(Module.class).get(0);
        Module module1=(Module) moduleData.getDomains(Module.class).get(1);
        module.setName(module1.getName());
        try{
            moduleBiz.updateModule(module);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),ModuleBizError.MODULE_NAME_EXIST);
        }
    }

    //删除测试
    @Test
    public void testDelete(){
        Module module=generateModule();
        module = moduleBiz.addModule(module);
        module=moduleBiz.getModuleById(module.getId());
        Assert.assertNotNull(module);
        moduleBiz.deleteModule(module.getId());
        module=moduleBiz.getModuleById(module.getId());
        Assert.assertNull(module);
    }

    //测试page查询
    @Test
    public void testQueryPage(){
        ModuleDaoQuery query = new ModuleDaoQuery();
        query.setPageIndex(1);
        query.setPageSize(10);
        DomainPage page=moduleBiz.findModulePage(query);
        System.out.println(page.getDomains().size());

        AndConditionSet condition=new AndConditionSet();
        condition.addCondition("name","%module1%", Condition.MatchType.like);
        query.setCondition(condition);
        page=moduleBiz.findModulePage(query);
        System.out.println(page.getDomains().size());

        condition=new AndConditionSet();
        condition.addCondition("domain","%domain2%", Condition.MatchType.like);
        query.setCondition(condition);
        page=moduleBiz.findModulePage(query);
        System.out.println(page.getDomains().size());

        condition=new AndConditionSet();
        condition.addCondition("permissionName","%function21%", Condition.MatchType.like);
        query.setCondition(condition);

        page=moduleBiz.findModulePage(query);
        System.out.println(page.getDomains().size());
    }

    //测试list查询
    @Test
    public void testQueryList(){
        ModuleDaoQuery query = new ModuleDaoQuery();
        AndConditionSet condition=new AndConditionSet();
        condition=new AndConditionSet();
        condition.addCondition("permissionName","%function21%", Condition.MatchType.like);
        query.setCondition(condition);

        List<Module> list=moduleBiz.findModuleList(query);
        System.out.println(list.size());
    }

    //测试根据name获取modules；
    @Test
    public void testFindModuleByName(){
        Module module= (Module) moduleData.getDomains(Module.class).get(0);
        List<Module> modules=moduleBiz.findModuleByName(module.getName());
        Assert.assertEquals(modules.size(),1);
    }

    //测试isExisted方法
    @Test
    public void testIsExisted(){
        Module module=(Module) moduleData.getDomains(Module.class).get(0);
        Assert.assertTrue(moduleBiz.isExisted(module.getId()));
        Assert.assertFalse(moduleBiz.isExisted(Long.MAX_VALUE));
        try{
            moduleBiz.isExisted(null);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),ModuleBizError.MODULE_NULL);
        }
    }


    //测试模块是否可用
    @Test
    public void testAvailable(){
        Module moduleExist=(Module) moduleData.getDomains(Module.class).get(0);
        Module module=null;
        try{
            moduleBiz.isAvailable(module);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),ModuleBizError.MODULE_NULL);
        }
        module=generateModule();
        module.setId(null);
        try{
            moduleBiz.isAvailable(module);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),ModuleBizError.MODULE_NULL);
        }
        List<Permission> permissions=module.getPermissions();
        module.setId(moduleExist.getId());
        module.setPermissions(null);
        try{
            moduleBiz.isAvailable(module);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),ModuleBizError.MODULE_PERMISSION_NULL);
        }
        module.setId(Long.MAX_VALUE);
        module.setPermissions(moduleExist.getPermissions());
        try{
            moduleBiz.isAvailable(module);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),ModuleBizError.MODULE_NOT_EXIST);
        }
        module.setId(moduleExist.getId());
        module.setPermissions(permissions);
        try{
            moduleBiz.isAvailable(module);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),ModuleBizError.MODULE_PERMISSION_NULL);
        }
        for(int i=0;i<module.getPermissions().size();i++){
            module.getPermissions().get(i).setId(Long.MAX_VALUE-i);
        }
        try{
            moduleBiz.isAvailable(module);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),ModuleBizError.MODULE_PERMISSION_NOT_EXIST);
        }
    }

    //测试模块列表是否可用
    @Test
    public void testAvailableList(){
        List<Module> modules = new ArrayList<>();
        Module module1=generateModule();
        Module module2=generateModule();
        module1.setId(1l);
        module2.setId(1l);
        modules.add(module1);
        modules.add(module2);
        try{
            moduleBiz.isAvailable(modules);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),ModuleBizError.MODULE_REPEAT);
        }
    }

    private Module generateModule(){
        Module module = new Module(generateName("module1 name"), generateName("domain1"));
        Permission permission11 = new Permission(generateName("function11"), generateName("path11"), generateName("code11"), module);
        Permission permission12 = new Permission(generateName("function12"), generateName("path12"), generateName("code12"), module);
        List<Permission> permissions1 = new ArrayList<>();
        permissions1.add(permission11);
        permissions1.add(permission12);
        module.setPermissions(permissions1);
        return module;
    }

    private static String generateName(String name) {
        return System.currentTimeMillis() + name;
    }

}
