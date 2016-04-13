package com.simpletour.biz.company;

import com.simpletour.biz.company.data.ModuleData;
import com.simpletour.biz.company.error.PermissionBizError;
import com.simpletour.common.core.exception.BaseSystemException;
import com.simpletour.dao.company.IModuleDao;
import com.simpletour.domain.company.Module;
import com.simpletour.domain.company.Permission;
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
 * Date: 2016/4/12
 * Time: 17:04
 */
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class PermissionBizTest extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    private IModuleDao moduleDao;

    @Autowired
    private IPermissionBiz permissionBiz;

    private ModuleData moduleData;

    @BeforeClass
    public void setUp() {
        moduleData = new ModuleData(moduleDao);
        moduleData.createData();
    }

    @AfterClass
    public void tearDown() throws Exception {
        int size = moduleData.getDomains().size();
        for (int i = size - 1; i >= 0; i--) {
            moduleDao.removeEntity(moduleData.getDomains().get(i));
        }
    }

    //测试根据id获取权限
    @Test
    public void testGetById() {
        Module module = (Module) moduleData.getDomains(Module.class).get(0);
        Permission permission = permissionBiz.getById(module.getPermissions().get(0).getId());
        Assert.assertNotNull(permission);
        permission = permissionBiz.getById(Long.MAX_VALUE);
        Assert.assertNull(permission);
    }

    //测试根据code获取权限，（code唯一）
    @Test
    public void testGetByCode() {
        Module module = (Module) moduleData.getDomains(Module.class).get(0);
        List<Permission> permissions = permissionBiz.getByCode(module.getPermissions().get(0).getCode());
        Assert.assertNotNull(permissions);
        Assert.assertEquals(permissions.size(), 1);
        permissions = permissionBiz.getByCode(module.getPermissions().get(0).getCode() + "test");
        Assert.assertTrue(permissions==null||permissions.isEmpty());
    }

    //测试判断code是否存在
    @Test
    public void testIsCodeExist() {
        Module module = (Module) moduleData.getDomains(Module.class).get(0);
        Assert.assertTrue(permissionBiz.isCodeExist(module.getPermissions().get(0).getCode()));
        Assert.assertFalse(permissionBiz.isCodeExist(module.getPermissions().get(0).getCode() + "test"));
    }

    //测试根据id判断权限是否存在
    @Test
    public void testIsExisted() {
        Module module = (Module) moduleData.getDomains(Module.class).get(0);
        Assert.assertTrue(permissionBiz.isExisted(module.getPermissions().get(0).getId()));
        Assert.assertFalse(permissionBiz.isExisted(Long.MAX_VALUE));
    }

    //测试isAvailable
    @Test
    public void testIsAvailable(){
        Module module=(Module) moduleData.getDomains(Module.class).get(0);
        try{
            permissionBiz.isAvailable(null);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(), PermissionBizError.DATA_NULL);
        }
        Permission permission=new Permission("test","path","code",null);
        Assert.assertTrue(permissionBiz.isAvailable(permission));
        permission.setId(Long.MAX_VALUE);
        try {
            permissionBiz.isAvailable(permission);
            Assert.fail();
        }catch (BaseSystemException e){
            Assert.assertEquals(e.getError(),PermissionBizError.NOT_EXIST);
        }
        permission.setId(module.getPermissions().get(1).getId());
        permission.setCode(module.getPermissions().get(0).getCode());
        Assert.assertFalse(permissionBiz.isAvailable(permission));
        permission.setId(module.getPermissions().get(0).getId());
        Assert.assertTrue(permissionBiz.isAvailable(permission));
    }
}
