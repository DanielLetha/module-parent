package com.simpletour.service.company;

import com.simpletour.biz.company.error.ModuleBizError;
import com.simpletour.commons.data.dao.query.condition.AndConditionSet;
import com.simpletour.commons.data.dao.query.condition.Condition;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.company.IModuleDao;
import com.simpletour.dao.company.IPermissionDao;
import com.simpletour.dao.company.query.ModuleDaoQuery;
import com.simpletour.domain.company.Module;
import com.simpletour.domain.company.Permission;
import com.simpletour.service.company.data.ModuleData;
import com.simpletour.service.company.error.ModuleServiceError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Author：XuHui/xuhui@simpletour.com
 * Brief：
 * Date: 2016/4/7
 * Time: 14:52
 */
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class ModuleServiceTest extends AbstractTransactionalTestNGSpringContextTests{
    @Autowired
    private IModuleService moduleService;

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

    @Test
    public void testAddModule() throws Exception {
        Module module=generateModule();
        Optional<Module> module1=moduleService.addModule(module);
        Assert.assertTrue(module1.isPresent());
        Assert.assertNotNull(module1.get().getId());
        Assert.assertNotNull(module1.get().getPermissions());
        Assert.assertEquals(module1.get().getPermissions().size(),2);
    }

    //添加空数据
    @Test
    public void testAddModuleNull() throws Exception{
        try {
            moduleService.addModule(null);
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), ModuleServiceError.MODULE_NULL);
        }
    }

    //添加模块名称已经存在
    @Test
    public void testAddModuleNameExist() throws Exception{
        Module module=generateModule();
        module.setName(((Module)moduleData.getDomains(Module.class).get(0)).getName());
        try {
            moduleService.addModule(module);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(), ModuleBizError.MODULE_NAME_EXIST);
        }
    }

    //添加模块功能列表为空
    @Test
    public void testAddModulePermissionNull() throws Exception{
        Module module=new Module(generateName("test add function null"),generateName("test add domain"));
        try{
            moduleService.addModule(module);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),ModuleServiceError.MODULE_PERMISSION_NULL);
        }
        module.setPermissions(new ArrayList<>());
        try{
            moduleService.addModule(module);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),ModuleServiceError.MODULE_PERMISSION_NULL);
        }
    }

    //添加模块功能列表中存在重复名称或code
    @Test
    public void testAddModulePermissionRepeat() throws Exception{
        Module module =generateModule();
        module.getPermissions().get(0).setName(module.getPermissions().get(1).getName());
        try {
            moduleService.addModule(module);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),ModuleServiceError.MODULE_PERMISSION_REPEAT);
        }
        module.getPermissions().get(0).setName(generateName("test name"));
        module.getPermissions().get(0).setCode(module.getPermissions().get(1).getCode());
        try {
            moduleService.addModule(module);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),ModuleServiceError.MODULE_PERMISSION_REPEAT);
        }
    }

    //添加模块，功能code已经存在；
    @Test
    public void testAddModuleFunctionNotAvaliable(){
        Module module =generateModule();
        module.getPermissions().get(0).setCode(((Module)moduleData.getDomains(Module.class).get(0)).getPermissions().get(0).getCode());
        try {
            moduleService.addModule(module);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),ModuleServiceError.MODULE_PERMISSION_NOT_AVAILABLE);
        }
    }

    @Test
    public void testUpdateModule(){
        Module module= (Module) moduleData.getDomains(Module.class).get(0);
        String name=generateName("update");
        module.setName(name);
        Optional<Module> moduleOptional=moduleService.updateModule(module);
        Assert.assertTrue(moduleOptional.isPresent());
        Assert.assertEquals(moduleOptional.get().getName(),name);
    }

    @Test
    public void testUpdateNull(){
        try{
            moduleService.updateModule(null);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),ModuleServiceError.MODULE_NULL);
        }
        Module module=generateModule();
        module=moduleService.addModule(module).get();
        Long id=module.getId();
        module.setId(null);
        try{
            moduleService.updateModule(module);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),ModuleServiceError.MODULE_NULL);
        }
        module.setId(id);
        Module module1=new Module(Long.MAX_VALUE-1,module.getName(),module.getDomain(),module.getVersion());
        try{
            moduleService.updateModule(module1);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),ModuleServiceError.MODULE_NOT_EXIST);
        }
    }

    @Test
    public void testUpdatePermissionNull(){
        Module module=generateModule();
        module=moduleService.addModule(module).get();
        List<Permission> permissions=module.getPermissions();
        module.setPermissions(null);
        try {
            moduleService.updateModule(module);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),ModuleServiceError.MODULE_PERMISSION_NULL);
        }
        module.setPermissions(new ArrayList<>());
        try{
            moduleService.updateModule(module);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),ModuleServiceError.MODULE_PERMISSION_NULL);
        }
    }

    @Test
    public void testUpdatePermissionRepeat(){
        Module module=generateModule();
        module = moduleService.addModule(module).get();
        module.getPermissions().get(0).setName(module.getPermissions().get(1).getName());
        try{
            moduleService.updateModule(module);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),ModuleServiceError.MODULE_PERMISSION_REPEAT);
        }
        module.getPermissions().get(0).setName(generateName("update"));
        module.getPermissions().get(0).setCode(module.getPermissions().get(1).getCode());
        try{
            moduleService.updateModule(module);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),ModuleServiceError.MODULE_PERMISSION_REPEAT);
        }
    }

    @Test
    public void testUpdatePermissionNotAvailable(){
        Module module= (Module) moduleData.getDomains(Module.class).get(0);
        Module module1=(Module) moduleData.getDomains(Module.class).get(1);
        module.getPermissions().get(0).setCode(module1.getPermissions().get(0).getCode());
        try{
            moduleService.updateModule(module);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),ModuleServiceError.MODULE_PERMISSION_NOT_AVAILABLE);
        }
    }

    @Test
    public void testUpdateModuleNameExist(){
        Module module= (Module) moduleData.getDomains(Module.class).get(0);
        Module module1=(Module) moduleData.getDomains(Module.class).get(1);
        module.setName(module1.getName());
        try{
            moduleService.updateModule(module);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),ModuleBizError.MODULE_NAME_EXIST);
        }
    }

    @Test
    public void testQueryPage(){
        ModuleDaoQuery query = new ModuleDaoQuery();
        query.setPageIndex(1);
        query.setPageSize(10);
        DomainPage page=moduleService.findModulePage(query);
        System.out.println(page.getDomains().size());

        AndConditionSet condition=new AndConditionSet();
        condition.addCondition("name","%module1%", Condition.MatchType.like);
        query.setCondition(condition);
        page=moduleService.findModulePage(query);
        System.out.println(page.getDomains().size());

        condition=new AndConditionSet();
        condition.addCondition("domain","%domain2%", Condition.MatchType.like);
        query.setCondition(condition);
        page=moduleService.findModulePage(query);
        System.out.println(page.getDomains().size());

        condition=new AndConditionSet();
        condition.addCondition("permissionName","%function21%", Condition.MatchType.like);
        query.setCondition(condition);

        page=moduleService.findModulePage(query);
        System.out.println(page.getDomains().size());

        List<Module> list=moduleService.findModuleList(query);
        System.out.println(list.size());
    }

    @Test
    public void testDelete(){
        Module module=generateModule();
        module = moduleService.addModule(module).get();
        moduleService.deleteModule(module.getId());
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