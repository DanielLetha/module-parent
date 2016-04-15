package com.simpletour.service.traveltrans.error;


import com.simpletour.commons.data.error.IError;

/**
 * Created by Mario on 2015/11/20.
 */
public enum TravelTransportServiceError implements IError {

    /**
     * =======================BUS===========================
     **/
    BUS_NULL("0001", "传入bus或busId不能为空"),
    BUS_BUS_NO_ID_AND_DAY_NULL("0002","传入的车次ID和天数不能为空"),
    BUS_LICENSE_IS_EXISTED("0003","车牌号已存在"),
    BUS_NOT_EXIST("0004","车辆不存在"),
    BUS_NOT_ONLINE("0005","车辆下线"),
    BUS_NOT_AVAILABLE("0006","车辆不可用"),
    BUS_BUS_HAS_BEEN_DISTRIBUTION("0007", "所选车辆已经被占用，请重新选择"),
    /**
     * =======================BUS_NO===========================
     **/
    BUS_NO_NULL("1001", "传入busNo或busNoId不能为空"),
    BUS_NO_NO_FOR_NULL("1002", "传入no不能为空"),
    BUS_NO_LIST_NULL("1003","传入车次ID不能为空"),
    BUS_NO_NOT_EXIST("1004", "车次不存在"),
    BUS_NO_STOPED("1005", "车次已停运"),
    /**
     * =======================BUS_NO_PLAN===========================
     **/
    BUS_NO_PLAN_NULL("2001", "传入busNoPlan或busNoPlanId不能为空"),
    BUS_NO_PLAN_NOT_EXIST("2002","排班不存在"),
    /**
     * =======================BUS_LINE===========================
     **/
    BUS_LINE_NULL("3001", "传入line或者lineId不能为空"),
    BUS_NO_SERIAL_NULL("3002", "车次序列为空"),
    /**
     * =======================BUS_NODE===========================
     **/
    BUS_NODE_LIST_NULL("4001", "传入node链表不能为空"),
    BUS_NODE_NULL("4002", "传入node或nodeId不能为空"),
    BUS_NODE_DESTINATION_NOT_EXISTED("4003", "node节点关联的destination为空"),
    BUS_NODE_TYPE_NOT_EXISTED("4004", "node节点关联的nodeType为空"),
    /**
     * =======================BUS_NODE_STATUS===========================
     **/
    BUS_NODE_STATUS_NULL("5001", "传入nodeStatus不能为空"),
    /**
     * =======================BUS_NODE_TYPE===========================
     **/
    BUS_NODE_TYPE_NULL("6001", "传入nodeType或nodeTypeId不能为空"),
    /**
     * =======================BUS_SEAT===========================
     **/
    BUS_SEAT_NULL("7001", "传入seat或seatId不能为空"),
    /**
     * =======================BUS_SEAT_LAYOUT===========================
     **/
    BUS_SEAT_LAYOUT_NULL("8001", "传入seatLayout或seatLayoutId不能为空"),

    BUS_SEAT_LAYOUT_NOT_AVAILABLE("8002", "座位布局不存在或不可用"),

    EXIST_FOREIGNKEY_CONSTRAINT_IN_DB("9001", "数据库中存在外键依赖关系，删除失败"),

    /**
     * =======================BUS_ASSISTANT===========================
     **/
    BUS_ASSISTANT_NOT_EXISTED("10001", "行车助理不存在"),
    BUS_ASSISTANT_HAS_BEEN_DISTRIBUTE("10001", "行车助理已经被分配");
    ;

    String errorCode;
    String errorMessage;

    private static final String ns = "SER.TRANSPORT";

    TravelTransportServiceError(String errorCode, String errorMessage) {
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
