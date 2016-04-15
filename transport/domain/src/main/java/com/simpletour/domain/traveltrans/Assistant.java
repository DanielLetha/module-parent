package com.simpletour.domain.traveltrans;

import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.simpletour.commons.data.domain.LogicalDeletableDomain;

import javax.persistence.*;

/**
 * 行车助理信息
 * Created by simpletour on 2015/12/9.
 */
@Entity
@Table(name = "TRANS_ASSISTANT")
@JSONType(serialzeFeatures = {SerializerFeature.DisableCircularReferenceDetect})
public class Assistant extends LogicalDeletableDomain {

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    protected Long id;

    /**
     * 供应商编号
     */
    @Column(name = "tenant_id")
    private Long tenantId;

    /**
     * 姓名
     */
    @Column(name = "name")
    private String name;

    /**
     * 昵称
     */
    @Column(name = "nick_name")
    private String nickName;

    /**
     * 电话
     */
    @Column(name = "mobile")
    private String mobile;

    /**
     * 登录账号
     */
    @Column(name = "account")
    private String account;

    /**
     * 登录密码
     */
    @Column(name = "passwd")
    private String password;

    /**
     * 安全随机数
     */
    @Column(name = "salt")
    private String salt;

    /**
     * 邮箱
     */
    @Column(name = "email")
    private String email;

    /**
     * 微信号
     */
    @Column(name = "wechat")
    private String wechat;

    /**
     * qq号
     */
    @Column(name = "qq")
    private String qq;

    /**
     * 状态
     */
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.normal;

    /**
     * 备注
     */
    @Column(name = "remark", columnDefinition = "text")
    private String remark;

    /**
     * 行车助理最后一次登录的设备Token
     */
    @Column(name = "device_token")
    private String deviceToken;

    /**
     * 行车助理最后一次登录的设备类型（ios/android）
     */
    @Column(name = "device_type")
    private String deviceType;

    public Assistant() {
    }

    public Assistant(String name, String nickName, String mobile, String account, String password, String salt) {
        this.name = name;
        this.nickName = nickName;
        this.mobile = mobile;
        this.account = account;
        this.password = password;
        this.salt = salt;
    }

    /**
     * 助理信息状态
     */
    public enum Status {

        normal("正常"), banned("禁用");

        private String remark;

        Status(String remark) {
            this.remark = remark;
        }

        public String getRemark() {
            return remark;
        }

    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
