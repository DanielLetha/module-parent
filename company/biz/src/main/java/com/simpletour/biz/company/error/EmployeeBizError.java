package com.simpletour.biz.company.error;

import com.simpletour.common.core.error.IError;

/**
 * Created by songfujie on 15/10/24.
 */
public enum EmployeeBizError implements IError {

    JOBNO_NULL("0000", "账号为空"),
    MOBILE_NULL("0001", "电话号码为空"),
    EMPLOYEE_NULL("0002", "员工实体对象为空"),
    EMPLOYEE_NULL_OR_ID_NULL("0003", "员工实体对象为空或ID"),
    EMPLOYEE_BASIC_INFO_NULL("0004", "员工实体对象基本信息不完整"),
    EMPLOYEE_ORIGIN_NOT_EXISTED("0005", "该员工不存在或已被删除"),
    EMPLOYEE_ID_NULL("0006", "ID为空"),
    JOBNO_EXISTED("0007", "工号已存在"),
    MOBILE_EXISTED("0008", "电话号码已存在"),
    EMPLOYEE_ID_PWD_SALT("0009", "修改密码所需基本信息为空"),
    EMPLOYEE_SET_JOB_NO_BASIC_INFO_WRONG("0010", "设置人员账号基本信息不完整"),
    EMPLOYEE_SET_JOB_NO_EXCEPTION("0011", "设置人员账号异常"),
    JOB_NO_NOT_SAME("0012", "人员工号和原有工号不相同"),
    SYSTEM_ERROR("1000", "程序异常");

    String errorCode;
    String errorMessage;

    private static final String ns = "SER.EMPLOYEE";

    EmployeeBizError(String errorCode, String errorMessage) {
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
