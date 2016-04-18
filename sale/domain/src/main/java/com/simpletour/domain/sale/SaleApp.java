package com.simpletour.domain.sale;



import com.simpletour.commons.data.domain.LogicalDeletableDomain;

import javax.persistence.*;

/**
 * @Brief : 销售端
 * @Author: liangfei/liangfei@simpletour.com
 * @Date :  2016/4/5 15:07
 * @Since ： 1.0-SNAPSHOT
 * @Remark: 销售端的实体类
 */
@Entity
@Table(name = "SALE_APP")
public class SaleApp extends LogicalDeletableDomain {

    /**
     * 销售端主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    /**
     * 销售端名称
     */
    @Column(name = "name")
    private String name;
    /**
     * 销售端认证信息（销售端key）
     */
    @Column(name = "key")
    private String key;
    /**
     * 销售端认证信息（销售端secret）
     */
    @Column(name = "secret")
    private String secret;
    /**
     * 对接人（姓名）
     */
    @Column(name = "contact")
    private String contact;
    /**
     * 对接人（电话）
     */
    @Column(name = "mobile")
    private String mobile;
    /**
     * 对接人（传真）
     */
    @Column(name = "fax")
    private String fax;
    /**
     * 对接人（邮件）
     */
    @Column(name = "email")
    private String email;
    /**
     * 对接人（其他联系方式）
     */
    @Column(name = "link")
    private String link;
    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;
    /**
     * 乐观锁
     */
    @Version
    private Integer version;

    public SaleApp(String name, String key, String secret, String contact, String mobile, String fax, String email, String link, String remark) {
        this.name = name;
        this.key = key;
        this.secret = secret;
        this.contact = contact;
        this.mobile = mobile;
        this.fax = fax;
        this.email = email;
        this.link = link;
        this.remark = remark;
    }

    public SaleApp() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
