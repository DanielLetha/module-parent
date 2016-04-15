package com.simpletour.domain.company;


import com.simpletour.commons.data.domain.EntityKey;
import com.simpletour.commons.data.domain.LogicalDeletableDomain;
import com.simpletour.commons.data.domain.dependency.Dependency;
import com.simpletour.commons.data.domain.dependency.IDependTracable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description 后台用户对象
 * @Author zt on 15-11-19.
 * @UpdateUser mario on 16-04-08
 * @Since 2.0
 */
@Entity
@Table(name = "SYS_EMPLOYEE")
public class Employee extends LogicalDeletableDomain implements IDependTracable{

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    protected Long id;

    /**
     * 是否为公司管理员:true为是,false为不是
     */
    @Column(name = "is_admin")
    private Boolean isAdmin = Boolean.FALSE;

    /**
     * 工号
     */
    @Column(name = "job_no")
    private Integer jobNo;

    /**
     * 人员姓名
     */
    @Column
    private String name;

    /**
     * 头像
     */
    @Column
    private String avater;

    /**
     * 电话号码
     */
    @Column
    private String mobile;

    /**
     * 密码
     */
    @Column
    private String passwd;

    /**
     * 盐
     */
    @Column
    private String salt;

    /**
     * 所属公司
     */
    @ManyToOne
    @JoinColumn(name = "tenant_id")
    private Company company;

    /**
     * 所属角色
     */
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    /**
     * 备注
     */
    @Column
    private String remark;

    /**
     * 版本号
     */
    @Version
    private Integer version;

    public Employee() {
    }

    public Employee(String remark, int jobNo, Boolean isAdmin, String name, String avater, String mobile, String passwd, String salt) {
        this.remark = remark;
        this.jobNo = jobNo;
        this.isAdmin = isAdmin;
        this.name = name;
        this.avater = avater;
        this.mobile = mobile;
        this.passwd = passwd;
        this.salt = salt;
    }

    public Employee(Boolean isAdmin,String name,String mobile,String avater,Company company, Role role) {
        this.isAdmin = isAdmin;
        this.name = name;
        this.mobile = mobile;
        this.avater = avater;
        this.company = company;
        this.role = role;
    }

    public Employee(Long id, Boolean isAdmin, int jobNo, String name, String avater, String mobile, String passwd, String salt, Company company, Role role, String remark, Integer version) {
        this.id = id;
        this.isAdmin = isAdmin;
        this.jobNo = jobNo;
        this.name = name;
        this.avater = avater;
        this.mobile = mobile;
        this.passwd = passwd;
        this.salt = salt;
        this.company = company;
        this.role = role;
        this.remark = remark;
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

    public Integer getJobNo() {
        return jobNo;
    }

    public void setJobNo(Integer jobNo) {
        this.jobNo = jobNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public String getAvater() {
        return avater;
    }

    public void setAvater(String avater) {
        this.avater = avater;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public List<Dependency> getDependencies() {
        return Arrays.asList(new Dependency(company.getEntityKey()),new Dependency(role.getEntityKey()));
    }
}
