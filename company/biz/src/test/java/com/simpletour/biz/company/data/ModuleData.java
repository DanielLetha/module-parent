package com.simpletour.biz.company.data;

import com.simpletour.dao.company.IModuleDao;
import com.simpletour.domain.company.Module;
import com.simpletour.domain.company.Permission;
import com.simpletour.test.helper.generator.AbstractDataGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：XuHui/xuhui@simpletour.com
 * Brief：
 * Date: 2016/4/7
 * Time: 14:53
 */
public class ModuleData extends AbstractDataGenerator {
    private IModuleDao moduleDao;

    public ModuleData(IModuleDao moduleDao) {
        this.moduleDao = moduleDao;
    }

    @Override
    public void generator() {
        Module module1 = new Module(generateName("module1 name"), generateName("domain1"));
        Permission permission11 = new Permission(generateName("function11"), generateName("path11"), generateName("code11"), module1);
        Permission permission12 = new Permission(generateName("function12"), generateName("path12"), generateName("code12"), module1);
        List<Permission> permissions1 = new ArrayList<>();
        permissions1.add(permission11);
        permissions1.add(permission12);
        module1.setPermissions(permissions1);

        Module module2 = new Module(generateName("module2 name"), generateName("domain2"));
        Permission permission21 = new Permission(generateName("function21"), generateName("path21"), generateName("code21"), module2);
        Permission permission22 = new Permission(generateName("function22"), generateName("path22"), generateName("code22"), module2);
        List<Permission> permissions2 = new ArrayList<>();
        permissions2.add(permission21);
        permissions2.add(permission22);
        module2.setPermissions(permissions2);

        domains.add(moduleDao.save(module1));
        domains.add(moduleDao.save(module2));
    }
}
