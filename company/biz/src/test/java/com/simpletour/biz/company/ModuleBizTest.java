package com.simpletour.biz.company;


import com.simpletour.biz.company.data.ModuleData;
import com.simpletour.biz.company.error.ModuleBizError;
import com.simpletour.commons.data.dao.query.condition.AndConditionSet;
import com.simpletour.commons.data.dao.query.condition.Condition;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
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
            moduleDao.remove(moduleData.getDomains().get(i));
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

    //测试isExisted方法
    @Test
    public void testIsModuleExisted(){
        Module module=(Module) moduleData.getDomains(Module.class).get(0);
        Assert.assertTrue(moduleBiz.isModuleExisted(module.getId()));
        Assert.assertFalse(moduleBiz.isModuleExisted(Long.MAX_VALUE));
        try{
            moduleBiz.isModuleExisted(null);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),ModuleBizError.MODULE_NULL);
        }
    }


    //测试模块是否可用
    @Test
    public void testModuleAvailable(){
        Module moduleExist=(Module) moduleData.getDomains(Module.class).get(0);
        Module module=null;
        try{
            moduleBiz.isModuleAvailable(module);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),ModuleBizError.MODULE_NULL);
        }
        module=generateModule();
        module.setId(null);
        try{
            moduleBiz.isModuleAvailable(module);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),ModuleBizError.MODULE_NULL);
        }
        List<Permission> permissions=module.getPermissions();
        module.setId(moduleExist.getId());
        module.setPermissions(null);
        try{
            moduleBiz.isModuleAvailable(module);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),ModuleBizError.MODULE_PERMISSION_NULL);
        }
        module.setId(Long.MAX_VALUE);
        module.setPermissions(moduleExist.getPermissions());
        try{
            moduleBiz.isModuleAvailable(module);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),ModuleBizError.MODULE_NOT_EXIST);
        }
        module.setId(moduleExist.getId());
        module.setPermissions(permissions);
        try{
            moduleBiz.isModuleAvailable(module);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),ModuleBizError.MODULE_PERMISSION_NULL);
        }
        for(int i=0;i<module.getPermissions().size();i++){
            module.getPermissions().get(i).setId(Long.MAX_VALUE-i);
        }
        try{
            moduleBiz.isModuleAvailable(module);
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
            moduleBiz.isModuleAvailable(modules);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),ModuleBizError.MODULE_REPEAT);
        }
    }

    //测试根据id获取权限
    @Test
    public void testGetById() {
        Module module = (Module) moduleData.getDomains(Module.class).get(0);
        Permission permission = moduleBiz.getPermissionById(module.getPermissions().get(0).getId());
        Assert.assertNotNull(permission);
        permission = moduleBiz.getPermissionById(Long.MAX_VALUE);
        Assert.assertNull(permission);
    }

    //测试根据code获取权限，（code唯一）
    @Test
    public void testGetByCode() {
        Module module = (Module) moduleData.getDomains(Module.class).get(0);
        Permission permissions = moduleBiz.getPermissionByCode(module.getPermissions().get(0).getCode());
        Assert.assertNotNull(permissions);
        permissions = moduleBiz.getPermissionByCode(module.getPermissions().get(0).getCode() + "test");
        Assert.assertNull(permissions);
    }

    //测试判断code是否存在
    @Test
    public void testIsCodeExist() {
        Module module = (Module) moduleData.getDomains(Module.class).get(0);
        Assert.assertTrue(moduleBiz.isPermissionCodeExist(module.getPermissions().get(0).getCode()));
        Assert.assertFalse(moduleBiz.isPermissionCodeExist(module.getPermissions().get(0).getCode() + "test"));
    }

    //测试根据id判断权限是否存在
    @Test
    public void testIsExisted() {
        Module module = (Module) moduleData.getDomains(Module.class).get(0);
        Assert.assertTrue(moduleBiz.isPermissionExisted(module.getPermissions().get(0).getId()));
        Assert.assertFalse(moduleBiz.isPermissionExisted(Long.MAX_VALUE));
    }

    //测试isAvailable
    @Test
    public void testIsAvailable(){
        Module module=(Module) moduleData.getDomains(Module.class).get(0);
        try{
            moduleBiz.isPermissionAvailable(null);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(), ModuleBizError.PERMISSION_NULL);
        }
        Permission permission=new Permission("test","path","code",null);
        Assert.assertTrue(moduleBiz.isPermissionAvailable(permission));
        permission.setId(Long.MAX_VALUE);
        try {
            moduleBiz.isPermissionAvailable(permission);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),ModuleBizError.PERMISSION_NOT_EXIST);
        }
        permission.setId(module.getPermissions().get(1).getId());
        permission.setCode(module.getPermissions().get(0).getCode());
        Assert.assertFalse(moduleBiz.isPermissionAvailable(permission));
        permission.setId(module.getPermissions().get(0).getId());
        Assert.assertTrue(moduleBiz.isPermissionAvailable(permission));
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
