package com.simpletour.domain.company;

/**
 * Author：XuHui/xuhui@simpletour.com
 * Brief：模块
 * Date: 2016/4/6
 * Time: 17:53
 */

import com.simpletour.common.core.domain.BaseDomain;
import org.springframework.util.ClassUtils;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "SYS_MODULE")
public class Module extends BaseDomain{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 模块名称
     * Remark: Modified by shiguanglu@simpletour.com
     */
    @Column(nullable = false)
    private String name;

    /**
     * 域名
     * Remark: Modified by shiguanglu@simpletour.com
     */
    @Column(nullable = false)
    private String domain;

    /**
     * 版本号
     */
    @Version
    private Integer version;

    /**
     * 功能列表
     * Remark: Modified by shiguanglu@simpletour.com, set fetch mode as FetchType.EAGER from FetchType.LAZY
     */
    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval=true)
    @OrderBy("id asc")
    private List<Permission> permissions;

    public Module() {
    }

    public Module(String name,String domain){
        this.name=name;
        this.domain=domain;
    }

    public Module(Long id, String name, String domain,Integer version) {
        this(name,domain);
        this.setId(id);
        this.version = version;
    }

    public Long getId(){
        return this.id;
    }

    public void setId(Long id){
        this.id=id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
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

    @Override
    public boolean equals(Object obj) {

        if (null == obj) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (!getClass().equals(ClassUtils.getUserClass(obj))) {
            return false;
        }

        Module that = (Module) obj;

        return null != this.getId() && this.getId().equals(that.getId());
    }
}
