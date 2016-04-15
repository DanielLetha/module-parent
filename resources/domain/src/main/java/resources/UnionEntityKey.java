package resources;


import com.simpletour.commons.data.domain.LogicalDeletableDomain;

/**
 * 文件描述：资源和目的地模块联合主键
 * 创建人员：石广路
 * 创建日期：2015/12/4 15:23
 * 备注说明：仅适用于对包含有目的地的资源实体进行重复性检查
 */
public class UnionEntityKey {
    Class<? extends LogicalDeletableDomain> clazz = null;

    String name;

    Destination destination;

    public UnionEntityKey(Class<? extends LogicalDeletableDomain> clazz, String name, Destination destination) {
        this.clazz = clazz;
        this.name = name;
        this.destination = destination;
    }

    public Class<? extends LogicalDeletableDomain> getClazz() {
        return clazz;
    }

    public void setClazz(Class<? extends LogicalDeletableDomain> clazz) {
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }
}
