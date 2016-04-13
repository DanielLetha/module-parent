package com.simpletour.biz.company.data;

import com.simpletour.dao.company.IModuleDao;
import com.simpletour.dao.company.IScopeTemplateDao;
import com.simpletour.domain.company.Module;
import com.simpletour.domain.company.Permission;
import com.simpletour.domain.company.ScopeTemplate;
import com.simpletour.test.helper.generator.AbstractDataGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * User: XuHui/xuhui@simpletour.com
 * Date: 2016/4/9
 * Time: 16:11
 */
public class ScopeData extends AbstractDataGenerator {

    private IScopeTemplateDao scopeTemplateDao;

    private IModuleDao moduleDao;

    public ScopeData(IModuleDao moduleDao, IScopeTemplateDao scopeTemplateDao){
        ModuleData moduleData=new ModuleData(moduleDao);
        this.scopeTemplateDao=scopeTemplateDao;
        this.addDataGenerators(moduleData);
        this.moduleDao=moduleDao;
    }

    @Override
    public void generator() {
        ScopeTemplate scopeTemplate1 = new ScopeTemplate(generateName("scope name"), generateName("remark"));
        List<Permission> permissions1 = new ArrayList<>();
        permissions1.add(((Module)getDomains(Module.class).get(0)).getPermissions().get(0));
        permissions1.add(((Module)getDomains(Module.class).get(1)).getPermissions().get(1));
        scopeTemplate1.setPermissions(permissions1);
        domains.add(scopeTemplateDao.save(scopeTemplate1));


        ScopeTemplate scopeTemplate2 = new ScopeTemplate(generateName("scope name"), generateName("remark"));
        List<Permission> permissions2 = new ArrayList<>();
        permissions2.add(((Module)getDomains(Module.class).get(0)).getPermissions().get(1));
        permissions2.add(((Module)getDomains(Module.class).get(1)).getPermissions().get(0));
        scopeTemplate2.setPermissions(permissions2);
        domains.add(scopeTemplateDao.save(scopeTemplate2));
    }
}
