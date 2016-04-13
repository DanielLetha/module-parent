//package com.simpletour.service.system.imp;
//
//import com.simpletour.common.core.exception.BaseSystemException;
//import com.simpletour.domain.system.Role;
//import com.simpletour.service.system.IRoleService;
//import com.simpletour.service.system.error.RoleServiceError;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
//import org.testng.Assert;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.Test;
//
///**
// * Created by zt on 2015/12/20.
// */
//@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
//public class RoleServiceImpTest extends AbstractTransactionalTestNGSpringContextTests {
//
//    private String timestamp1;
//    private String timestamp2;
//    @Autowired
//    IRoleService roleService;
//
//    @BeforeClass
//    public void initData() {
//        timestamp1 = new Long(System.currentTimeMillis()).toString();
//        timestamp2 = new Long(System.currentTimeMillis()+20).toString();
//    }
//    /**
//     * 输入测试
//     */
//    @Test
//    public void testSaveAndDelRole(){
//        Role role=new Role();
//        // name 为空,添加失败
//        try {
//            role = roleService.addRole(role).get();
//            Assert.fail();
//        } catch (BaseSystemException e) {
//            Assert.assertEquals(e.getError(), RoleServiceError.NAME_NULL);
//        }
//        // code 为空,添加失败
//        role.setName("测试角色"+ timestamp1);
//        try {
//            roleService.addRole(role);
//            Assert.fail();
//        } catch (BaseSystemException e) {
//            Assert.assertEquals(e.getError(), RoleServiceError.CODE_NULL);
//        }
//        role.setCode("test"+ timestamp1);
//        role  = roleService.addRole(role).get();
//        Assert.assertNotNull(role);
//        // 不改变值更新一次数据
//        try {
//            roleService.updateRole(role);
//        } catch (BaseSystemException e) {
//            Assert.fail();
//        }
//        Long saveId = role.getId();
//        // 重复更新失败
//        Role role2=new Role();
//        role2.setName(role.getName());
//        try {
//            roleService.addRole(role2);
//            Assert.fail();
//        } catch (BaseSystemException e) {
//            Assert.assertEquals(e.getError(), RoleServiceError.NAME_EXIST);
//        }
//        role2.setName("测试角色"+ timestamp2);
//        role2.setCode(role.getCode());
//        try {
//            roleService.addRole(role2);
//            Assert.fail();
//        } catch (BaseSystemException e) {
//            Assert.assertEquals(e.getError(), RoleServiceError.CODE_EXIST);
//        }
//
//        //删除测试(ID不存在)
//        try {
//            roleService.deleteRole(System.currentTimeMillis());
//            Assert.fail();
//        } catch (BaseSystemException e) {
//            Assert.assertEquals(e.getError(), RoleServiceError.DATA_NULL);
//        }
//        //删除测试正常
//        try {
//            roleService.deleteRole(saveId);
//        } catch (BaseSystemException e) {
//            Assert.fail();
//        }
//    }
//}