package com.simpletour.biz.company.error;

import com.simpletour.common.core.error.IError;

/**
 * User: XuHui/xuhui@simpletour.com
 * Date: 2016/4/9
 * Time: 14:44
 */
public enum PermissionBizError implements IError{

    EMPTY_ENTITY("0000", "实体为空"),
    DATA_NULL("0001", "数据为空"),
    ID_NULL("0002", "主键为空"),
    UPDATE_FAILD("0003", "权限更新失败"),
    DELETE_FAILD("0004", "权限删除失败"),
    ADD_FAILD("0005", "权限添加失败"),
    CODE_EXIST("0006", "权限代码已存在"),
    CODE_NULL("0007", "权限代码为空"),
    NAME_EXIST("0008", "权限名称已存在"),
    NAME_NULL("0009", "权限名称已为空"),
    CANNOT_DEL_DEPENDENT_ROLE("0010", "存在依赖角色"),
    NOT_EXIST("0011","功能不存在"),
            ;

    String errorCode;
    String errorMessage;

    private static final String ns = "BIZ.PERMISSION";

    PermissionBizError(String errorCode, String errorMessage) {
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
