package resources;

/**
 * 文件描述：资源模块联合主键获取接口
 * 创建人员：石广路
 * 创建日期：2015/12/4 15:22
 * 备注说明：null
 */
public interface IUnionEntityKey {
    /**
     * 功能：获取资源与自身所包含的目的地的联合组键
     * 作者：石广路
     * 新增：2015-12-05 14:27
     * 备注：凡是包含有目的地的资源都应实现该接口
     *
     * @return 联合组键
     */
    UnionEntityKey getUnionEntityKey();
}
