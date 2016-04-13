package com.simpletour.domain.company;


import com.simpletour.common.core.domain.BaseDomain;

import javax.persistence.*;

/**
 * Brief : 超级管理员
 * Author: Hawk
 * Date  : 2016/4/5
 */
@Entity
@Table(name = "SYS_ADMIN")
public class Administrator extends BaseDomain {

    /**
     * 账户状态
     */
    public enum Status {
        dimission("离职"), inservice("在职");

        private String remark;

        Status(String remark) {
            this.remark = remark;
        }

        public String getRemark() {
            return this.remark;
        }
    }

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 工号
     */
    @Column(length = 32)
    private String jobNo;

    /**
     * 登录名
     */
    @Column(length = 20)
    private String name;

    /**
     * 手机号码
     */
    @Column(length = 11)
    private String mobile;

    /**
     * 二次MD5后的值(32位)
     * 1. 直接MD5
     * 2. 加盐 + MD5
     */
    @Column(length = 64)
    private String password;

    /**
     * 第二次MD5时的盐
     */
    @Column(name = "salt", length = 32)
    private String salt;

    /**
     * 状态
     */
    @Column(name = "status", length = 20)
    @Enumerated(EnumType.STRING)
    private Status status = Status.inservice;

    /**
     * 备注
     */
    @Column(columnDefinition = "text")
    private String remark;

    /**
     * 乐观锁
     */
    @Version
    private Integer version;

    public Administrator() {
    }

    public Administrator(String name, String mobile, String password, String salt, Status status) {
        this.name = name;
        this.mobile = mobile;
        this.password = password;
        this.salt = salt;
        this.status = status;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getJobNo() {
        return jobNo;
    }

    public void setJobNo(String jobNo) {
        this.jobNo = jobNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Administrator)) {
            return false;
        }
        Administrator that = (Administrator) obj;
        if (that.getId() != null && this.getId() != null && this.getId().equals(that.getId())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Administrator{" +
                "id=" + id +
                ", jobNo='" + jobNo + '\'' +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", status=" + status +
                ", remark='" + remark + '\'' +
                ", version=" + version +
                '}';
    }
}
