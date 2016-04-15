package com.simpletour.domain.company;

import com.simpletour.commons.data.domain.LogicalDeletableDomain;

import javax.persistence.*;
import java.util.List;

/**
 * 文件描述：角色权限关系实体类
 * 文件版本：1.0
 * 创建人员：石广路
 * 创建日期：2016/4/7 12:03
 * 备注说明：按租户ID进行区分隔离
 */
@Entity
@Table(name = "SYS_ROLE")
public class Role extends LogicalDeletableDomain {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    protected Long id;

    /**
     * 角色名称
     */
    @Column(nullable = false)
    private String name;

    /**
     * 系统可见类型，0：业务系统可见，1：核心系统可见
     */
    @Column
    private Integer type;

    /**
     * 所属公司ID
     */
    @ManyToOne
    @JoinColumn(name = "tenant_id")
    private Company company;

    /**
     * 备注信息
     */
    @Column(columnDefinition = "text")
    private String remark;

//    @OneToMany(targetEntity = Employee.class, mappedBy = "role")
//    private List<Employee> employeeList;

    /**
     * 模块功能列表
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "SYS_R_ROLE_PERMISSION", joinColumns = {@JoinColumn(name = "rid")}, inverseJoinColumns = {@JoinColumn(name = "pid")})
    @OrderBy("id")
    private List<Permission> permissionList;

    /**
     * 版本
     */
    @Version
    private Integer version;

    public Role() {
    }

    public Role(String name, Integer type, Company company, String remark, List<Permission> permissionList) {
        this.name = name;
        this.type = type;
        this.company = company;
        this.remark = remark;
        this.permissionList = permissionList;
    }

    public Role(Long id, String name, Integer type, Company company, String remark, List<Permission> permissionList, Integer version) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.company = company;
        this.remark = remark;
        this.permissionList = permissionList;
        this.version = version;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

//    public List<Employee> getEmployeeList() {
//        return employeeList;
//    }

//    public void setEmployeeList(List<Employee> employeeList) {
//        this.employeeList = employeeList;
//    }

    public List<Permission> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(List<Permission> permissionList) {
        this.permissionList = permissionList;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getId() != null ? this.getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (null == object || !(object instanceof Role)) {
            return false;
        }
        Role other = (Role) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }
}
