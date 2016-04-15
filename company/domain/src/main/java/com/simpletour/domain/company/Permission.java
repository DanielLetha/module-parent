package com.simpletour.domain.company;

import com.simpletour.commons.data.domain.BaseDomain;

import javax.persistence.*;
import java.util.List;

/**
 * 角色权限项对象
 * Created by Liangfei on 15-11-5.
 * Modified by XuHui on 15-12-02
 */
@Entity
@Table(name = "SYS_PERMISSION")
public class Permission extends BaseDomain {

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    protected Long id;

    /**
     * 功能名称
     * Remark: Modified by shiguanglu@simpletour.com
     */
    @Column(nullable = false)
    private String name;

    /**
     * 功能路径
     * Remark: Modified by shiguanglu@simpletour.com
     */
    @Column(nullable = false)
    private String path;

    /**
     * 功能代码
     * Remark: Modified by shiguanglu@simpletour.com
     */
    @Column(nullable = false)
    private String code;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "SYS_R_ROLE_PERMISSION", joinColumns = {@JoinColumn(name = "pid")}, inverseJoinColumns = {@JoinColumn(name = "rid")})
    @OrderBy("id")
    private List<Role> roleList;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "SYS_R_COMPANY_PERMISSION", joinColumns = {@JoinColumn(name = "pid")}, inverseJoinColumns = {@JoinColumn(name = "cid")})
    @OrderBy("id")
    private List<Company> companyList;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "SYS_R_TEMPLATE_PERMISSION", joinColumns = {@JoinColumn(name = "pid")}, inverseJoinColumns = {@JoinColumn(name = "tid")})
    @OrderBy("id")
    private List<ScopeTemplate> scopeTemplateList;

    /**
     * 关联的模块
     */
    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;

    @Version
    private Integer version;

    public Permission() {
    }

    public Permission(String name,String path,String code,Module module){
        this.name=name;
        this.path=path;
        this.code=code;
        this.module=module;
    }

    public Permission(Long id,String name,String path,String code,Module module,Integer version){
        this(name, path, code, module);
        this.id=id;
        this.version=version;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public List<Company> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(List<Company> companyList) {
        this.companyList = companyList;
    }

    public List<ScopeTemplate> getScopeTemplateList() {
        return scopeTemplateList;
    }

    public void setScopeTemplateList(List<ScopeTemplate> scopeTemplateList) {
        this.scopeTemplateList = scopeTemplateList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getId() != null ? this.getId().hashCode() : 0);
        return hash;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Permission)) {
            return false;
        }
        Permission other = (Permission) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }
}
