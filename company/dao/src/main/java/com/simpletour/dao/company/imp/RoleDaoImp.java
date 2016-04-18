package com.simpletour.dao.company.imp;

import com.simpletour.commons.data.dao.jpa.DependencyHandleDAO;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.company.IRoleDao;
import com.simpletour.dao.company.query.RoleQuery;
import com.simpletour.domain.company.Company;
import com.simpletour.domain.company.Module;
import com.simpletour.domain.company.Permission;
import com.simpletour.domain.company.Role;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 文件描述：角色权限关系DAO层实现类
 * 文件版本：1.0
 * 创建人员：石广路
 * 创建日期：2016/4/8 16:56
 * 备注说明：null
 */
@Repository
public class RoleDaoImp extends DependencyHandleDAO implements IRoleDao {
    static final List<String> ROLE_FIELDS_LIST = new ArrayList<>(10);

    static final List<String> PERMISSION_FIELDS_LIST = new ArrayList<>(8);

    static final List<String> MODULE_FIELDS_LIST = new ArrayList<>(14);

    static final String ROLE_FIELD_NAMES;

    static final String PERMISSION_FIELD_NAMES;

    static final String MODULE_FIELD_NAMES;

    static {
        ROLE_FIELDS_LIST.add("id as role_id");
        //ROLE_FIELDS_LIST.add("createdtime as role_createdtime");
        //ROLE_FIELDS_LIST.add("del as role_del");
        ROLE_FIELDS_LIST.add("name as role_name");
        //ROLE_FIELDS_LIST.add("remark as role_remark");
        ROLE_FIELDS_LIST.add("type as role_type");
//        ROLE_FIELDS_LIST.add("version as role_version");
        ROLE_FIELDS_LIST.add("tenant_id as role_tenant_id");
        //ROLE_FIELDS_LIST.add("code as role_code");
        //ROLE_FIELDS_LIST.add("descr as role_descr");

        String name = ROLE_FIELDS_LIST.toString();
        ROLE_FIELD_NAMES = name.substring(1, name.length() - 1);

        PERMISSION_FIELDS_LIST.add("id as permission_id");
        //PERMISSION_FIELDS_LIST.add("createdtime as permission_createdtime");
        PERMISSION_FIELDS_LIST.add("code as permission_code");
        PERMISSION_FIELDS_LIST.add("name as permission_name");
        PERMISSION_FIELDS_LIST.add("path as permission_path");
//        PERMISSION_FIELDS_LIST.add("version as permission_version");
        PERMISSION_FIELDS_LIST.add("module_id as permission_module_id");
//        PERMISSION_FIELDS_LIST.add("del as permission_del");

        name = PERMISSION_FIELDS_LIST.toString();
        PERMISSION_FIELD_NAMES = name.substring(1, name.length() - 1);

        MODULE_FIELDS_LIST.add("id as module_id");
        //MODULE_FIELDS_LIST.add("createdtime as module_createdtime");
        MODULE_FIELDS_LIST.add("domain as module_domain");
        MODULE_FIELDS_LIST.add("name as module_name");
//        MODULE_FIELDS_LIST.add("version as module_version");
//        MODULE_FIELDS_LIST.add("del as module_del");
//        MODULE_FIELDS_LIST.add("appid as module_appid");
//        MODULE_FIELDS_LIST.add("code as module_code");
//        MODULE_FIELDS_LIST.add("descr as module_descr");
//        MODULE_FIELDS_LIST.add("icon as module_icon");
//        MODULE_FIELDS_LIST.add("menu as module_menu");
//        MODULE_FIELDS_LIST.add("sort as module_sort");
//        MODULE_FIELDS_LIST.add("url as module_url");
//        MODULE_FIELDS_LIST.add("pid as module_pid");

        name = MODULE_FIELDS_LIST.toString();
        MODULE_FIELD_NAMES = name.substring(1, name.length() - 1);
    }

    @Override
    public void deleteRole(Role role) throws BaseSystemException {
//        Long id;
//        if (null != role && null != (id = role.getId())) {
//            try {
//                removeEntity(role);
//                em.createNativeQuery("DELETE FROM SYS_R_ROLE_PERMISSION WHERE rid = " + id).executeUpdate();
//            } catch (BaseSystemException bse) {
//                throw bse;
//            }
//        }
        removeEntity(role);
    }

    @Override
    public DomainPage<Role> getRolesPages(RoleQuery query) {
        StringBuilder sb = new StringBuilder();
        int pageIndex = query.getPageIndex();
        int pageSize = query.getPageSize();
        Long count = getTotalCount(buildQuerySql(query, sb, true));
        DomainPage<Role> domainPage = new DomainPage(pageSize, pageIndex, count);

        if (0L == count) {
            return domainPage;
        }

        sb.delete(0, sb.length());

        Query nativeQuery = buildQuerySql(query, sb, false);
        nativeQuery.setFirstResult((1 >= pageIndex ? 0 : pageIndex - 1) * pageSize);
        nativeQuery.setMaxResults(pageSize);

        List<Object[]> resultsList = nativeQuery.getResultList();
        Map<Long, Role> roleMap = new HashMap<>(count.intValue());

        resultsList.forEach(fields -> {
            Long roleId = Long.valueOf(fields[0].toString());
            Role role;
            List<Permission> permissionsList;

            if (roleMap.containsKey(roleId)) {
                role = roleMap.get(roleId);
                permissionsList = role.getPermissionList();
            } else {
                role = new Role();
                role.setId(roleId);
                role.setName(fields[1].toString());
                role.setType((Integer)fields[2]);

                Company company = new Company();
                company.setId(Long.valueOf(fields[3].toString()));
                role.setCompany(company);

                permissionsList = new ArrayList<>(count.intValue());
            }

            Permission permission = new Permission();
            permission.setId(Long.valueOf(fields[4].toString()));
            permission.setCode(fields[5].toString());
            permission.setName(fields[6].toString());
            permission.setPath(fields[7].toString());

            Module module = new Module();
            module.setId(Long.valueOf(fields[8].toString()));
            module.setDomain(fields[10].toString());
            module.setName(fields[11].toString());

            permission.setModule(module);
            permissionsList.add(permission);

            role.setPermissionList(permissionsList);
            roleMap.put(roleId, role);
        });

        domainPage.setDomains(roleMap.values().stream().collect(Collectors.toList()));

        return domainPage;
    }

    private Query buildQuerySql(RoleQuery query, StringBuilder sb, boolean isCount) {
        if (isCount) {
            sb.append("SELECT COUNT(DISTINCT(temp_role.role_id)) FROM ");
        } else {
            sb.append("SELECT temp_role.*, temp_permission.*, temp_module.* FROM ");
        }

        String roleSql = String.format("(SELECT %s FROM sys_role c WHERE 1 = 1 AND %s %s %s) temp_role",
                ROLE_FIELD_NAMES, filterDelPattern, getTenantIdFilterCondition("AND tenant_id"), getNameFilterPattern(query.getName()));
        roleSql += " INNER JOIN sys_r_role_permission rp ON rp.rid = temp_role.role_id INNER JOIN ";

//        String permissionSql = String.format("(SELECT %s FROM sys_permission c WHERE 1 = 1 AND %s %s) temp_permission",
//                PERMISSION_FIELD_NAMES, filterDelPattern, getNameFilterPattern(query.getPermission()));
        String permissionSql = String.format("(SELECT %s FROM sys_permission c WHERE 1 = 1 %s) temp_permission",
                PERMISSION_FIELD_NAMES, getNameFilterPattern(query.getPermission()));
        permissionSql += " ON rp.pid = temp_permission.permission_id INNER JOIN ";

        String moduleSql = String.format("(SELECT %s FROM sys_module c WHERE 1 = 1 AND %s %s) temp_module",
                MODULE_FIELD_NAMES, filterDelPattern, getNameFilterPattern(query.getModule()));
//        String moduleSql = String.format("(SELECT %s FROM sys_module c WHERE 1 = 1 %s) temp_module",
//                MODULE_FIELD_NAMES, getNameFilterPattern(query.getModule()));
        moduleSql += " ON temp_permission.permission_module_id = temp_module.module_id";

        sb.append(roleSql).append(permissionSql).append(moduleSql);

        return em.createNativeQuery(sb.toString());
    }

    private String getNameFilterPattern(String name) {
        return null == name || name.isEmpty() ? "" : "AND c.name like '%" + name + "%'";
    }
}
