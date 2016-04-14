package com.simpletour.biz.company.error;

import com.simpletour.commons.data.error.IError;

/**
 * 文件描述：角色错误信息定义描述类
 * 文件版本：1.0
 * 创建人员：石广路
 * 创建日期：2016/4/6 21:32
 * 备注说明：null
 */
public enum RoleBizError implements IError {
    EMPTY_ENTITY("0000", "实体为空"),
    DATA_NULL("0001", "数据为空"),
    ID_NULL("0002", "主键为空"),
    UPDATE_FAILD("0003", "角色更新失败"),
    DELETE_FAILD("0004", "角色删除失败"),
    ADD_FAILD("0005", "角色添加失败"),
    NOT_EXISTING("0006", "角色不存在"),

    //NAME_EXIST("0006", "角色名已存在"),
    //NAME_NULL("0007", "角色名称不能为空"),
    //CODE_EXIST("0008", "角色代码已存在"),
    //CODE_NULL("0009", "角色代码为空"),
    //CANNOT_DEL_DEPENDENT_EMPLOYEE("0010", "存在依赖员工"),
    //CANNOT_DEL_DEPENDENT_PERMISSION("0011", "存在依赖权限"),

    ROLE_NAME_IS_EMPTY("1000", "角色名称不能为空"),
    ROLE_NAME_LEN_INVALID("1001", "角色名称的长度必须不小于2个字符，不大于20个字符"),
    ROLE_REMARK_LEN_INVALID("1002", "角色备注信息的长度必须不大于10000个字符"),
    INVALID_TENANT_ID("1003", "无效的租户ID"),
    UNIQUE_ROLE_NAME_IN_SAME_COMPANY("1004", "同一个公司内角色名称需要保证唯一"),

    INVALID_COMPANY("1005", "公司信息无效"),
    //INVALID_EMPLOYEE("1006", "雇员信息无效"),
    INVALID_PERMISSION("1007", "权限范围无效"),
    INVALID_MODULE("1008", "功能模块无效")
    ;

    String errorCode;
    String errorMessage;

    private static final String ns = "BIZ.ROLE";

    RoleBizError(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public String getNamespace() {
        return ns;
    }

    @Override
    public String getErrorCode() {
        return ns + "." + errorCode;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
