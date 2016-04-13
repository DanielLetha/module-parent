package com.simpletour.service.company.error;

import com.simpletour.common.core.error.IError;

/**
 * Created by songfujie on 15/10/24.
 */
public enum EmployeeServiceError implements IError {

    EMPTY_ENTITY("0000", "实体为空"),
    DATA_NULL("0001", "基本数据为空"),
    ID_NULL("0002", "主键为空"),
    UPDATE_FAILD("0003", "员工更新失败"),
    DELETE_FAILD("0004", "员工删除失败"),
    ADD_FAILD("0005", "员工添加失败"),
    JOBNO_EXIST("0006", "工号已存在"),
    JOBNO_NULL("0007", "工号为空"),
    MOBILE_EXIST("0008", "手机号已存在"),
    MOBILE_NULL("0009", "手机号为空"),
    COMPANY_NULL("0010", "所属公司为空或所属公司ID为空"),
    COMPANY_HAS_BEEN_DELETE("0011", "所属公司不存在或已被删除"),
    ROLE_NULL("0012", "角色为空或者角色ID为空"),
    ROLE_HAS_BEEN_DELETE("0013", "角色不存在或已被删除"),
    EMPLOYEE_NOT_EXISTED("0014", "员工不存在"),
    EMPLOYEE_ID_PWD_SALT_NULL("0015", "修改密码基本信息为空");

    String errorCode;
    String errorMessage;

    private static final String ns = "SER.EMPLOYEE";

    EmployeeServiceError(String errorCode, String errorMessage) {
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
