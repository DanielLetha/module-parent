package com.simpletour.domain.company;

import com.simpletour.commons.data.domain.BaseDomain;
import com.simpletour.commons.data.domain.LogicalDeletableDomain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Author：XuHui/xuhui@simpletour.com
 * Brief：权限范围
 * Date: 2016/4/6
 * Time: 18:12
 */
@Entity
@Table(name = "SYS_SCOPE_TEMPLATE")
public class ScopeTemplate extends LogicalDeletableDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 权限范围名称
     */
    @Column
    private String name;

    @Column(columnDefinition = "text")
    private String remark;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "SYS_R_TEMPLATE_PERMISSION", joinColumns = {@JoinColumn(name = "tid")}, inverseJoinColumns = {@JoinColumn(name = "pid")})
    @OrderBy("id")
    private List<Permission> permissions=new ArrayList<>();

    @Version
    private Integer version;

    public ScopeTemplate (){}

    public ScopeTemplate(String name,String remark){
        this.name=name;
        this.remark=remark;
    }

    public ScopeTemplate(Long id,String name,String remark,Integer version){
        this(name, remark);
        this.id=id;
        this.version=version;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id=id;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
}
