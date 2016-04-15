package com.simpletour.domain.traveltrans;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.simpletour.commons.data.dao.query.QueryUtil;
import com.simpletour.commons.data.domain.BaseDomain;
import com.simpletour.commons.data.domain.dependency.DependEntity;
import com.simpletour.commons.data.domain.dependency.Dependency;
import com.simpletour.commons.data.domain.dependency.IDependTracable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 行程节点类型信息
 * Created by Mario on 2015/11/20.
 */
@Entity
@Table(name = "TRANS_NODETYPE")
@JSONType(serialzeFeatures = {SerializerFeature.DisableCircularReferenceDetect})
public class NodeType extends BaseDomain implements IDependTracable {

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    protected Long id;



    /**
     * 节点类型名称
     */
    @Column
    private String name;

    /**
     * icon
     */
    @Column
    private String icon;

    /**
     * 父编号的节点类型id
     */
    @ManyToOne
    @JoinColumn(name = "pid")
    @JSONField(serialize = false)
    private NodeType parent;

    /**
     * 节点类型是否展示
     */
    @Column
    private Boolean display = false;

    @OneToMany(mappedBy = "parent")
    private List<NodeType> nodeTypes;

    /**
     * constructor
     */
    public NodeType() {

    }

    /**
     * constructor
     *
     * @param name
     * @param icon
     * @param parent
     * @param display
     */
    public NodeType(String name, String icon, NodeType parent, Boolean display) {
        this.name = name;
        this.icon = icon;
        this.parent = parent;
        this.display = display;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public NodeType getParent() {
        return parent;
    }

    public void setParent(NodeType parent) {
        this.parent = parent;
    }

    public Boolean isDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }

    public List<NodeType> getNodeTypes() {
        return nodeTypes;
    }

    public void setNodeTypes(List<NodeType> nodeTypes) {
        this.nodeTypes = nodeTypes;
    }

    @Override
    @JSONField(serialize = false)
    public List<Dependency> getDependencies() {
        return Arrays.asList(new Dependency(parent.getEntityKey()));
    }
}
