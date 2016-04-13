package com.simpletour.service.company.error;

import com.simpletour.common.core.error.IError;

/**
 * Author:  wangLin
 * Mail  :  wl@simpletour.com
 * Date  :  2016/4/7.
 * Remark: companyService接口
 */
public enum CompanyServiceError implements IError {

    COMPANY_NULL("0000", "公司为null"),
    DATA_NULL("0001", "数据为空"),
    ID_NULL("0002", "主键id为空"),
    UPDATE_FAILD("0003", "公司更新失败"),
    DELETE_FAILD("0004", "公司删除失败"),
    ADD_FAILD("0005", "公司添加失败"),
    NAME_EXIST("0006", "公司名称已存在"),
    NAME_NULL("0007", "公司名称为空"),
    CANNOT_DEL_DEPENDENT_EMPLOYEE("0008", "存在依赖员工"),
    PERMISSION_NOT_EXIST("0009", "公司所包含的功能不存在"),
    MODULE_NOT_EXIST("0010", "与公司所包含功能相关的模块不存在"),
    COMPANY_NOT_EXIST("0011", "公司不存在"),
    EMPLOYEE_DEPEND_ON_COMPANY("0012", "公司被人员引用"),
    COMPANY_MUST_CONTAIN_PERMISSION("0013", "公司必须包含功能"),
    COMPANY_SAVE_FAILURE("0014", "公司保存失败"),
    COMPANY_ROLE_INIT_FAILURE("0015", "公司管理员角色初始化失败"),
    COMPANY_EMPLOYEE_INIT_FAILURE("0015", "公司管理员角色初始化失败"),
    COMPANY_ROLE_PERMISSION_NOT_IS_EXIST("0016", "公司管理员角色初始化时，对应初始化的权限不存在");


    String errorCode;
    String errorMessage;

    private static final String ns = "SER.COMPANY";

    CompanyServiceError(String errorCode, String errorMessage) {
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
