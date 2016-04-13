package com.simpletour.domain.company;

import com.simpletour.common.core.domain.CanLogicDelDomain;

import javax.persistence.*;
import java.util.List;

/**
 * Author:  wangLin
 * Mail  :  wl@simpletour.com
 * Date  :  2016/4/6.
 * Remark: 分户对象
 */
@Entity
@Table(name = "SYS_COMPANY")
@SequenceGenerator(name = "company_id", sequenceName = "company_id_seq", allocationSize = 1)
public class Company extends CanLogicDelDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_id")
    @Column(name = "ID")
    protected Long id;

    @Column
    private String name;

    @Column
    private String address;

    //对接人姓名
    @Column
    private String contacts;

    //对接人电话
    @Column
    private String mobile;

    //对接人传真
    @Column
    private String fax;

    //对接人邮箱
    @Column
    private String email;

    //对接人其他联系方式
    @Column
    private String link;

    @Column(columnDefinition = "text")
    private String remark;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<Employee> employees;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<Role> roles;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "SYS_R_COMPANY_PERMISSION", joinColumns = {@JoinColumn(name = "cid")}, inverseJoinColumns = {@JoinColumn(name = "pid")})
    @OrderBy("id")
    private List<Permission> permissions;

    @Version
    private Integer version;

    public Company() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Company(Long id, String name, String address, String contacts, String mobile, String fax, String email, String link, String remark, List<Employee> employees, List<Role> roles, List<Permission> permissions, Integer version) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.contacts = contacts;
        this.mobile = mobile;
        this.fax = fax;
        this.email = email;
        this.link = link;
        this.remark = remark;
        this.employees = employees;
        this.roles = roles;
        this.permissions = permissions;
        this.version = version;
    }

    public Company(String name, String address, String remark) {
        this.name = name;
        this.address = address;
        this.remark = remark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
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
        if (!(object instanceof Company)) {
            return false;
        }
        Company other = (Company) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}