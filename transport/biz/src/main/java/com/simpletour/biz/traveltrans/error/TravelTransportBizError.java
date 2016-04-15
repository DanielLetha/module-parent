package com.simpletour.biz.traveltrans.error;


import com.simpletour.commons.data.error.IError;

/**
 * Created by Mario on 2015/11/20.
 */
public enum TravelTransportBizError implements IError {

    /**
     * =======================BUS===========================
     **/
    BUS_NULL("0001", "传入bus或bus基本信息不能为空"),
    BUS_EXISTED("0002", "已有同名的bus"),
    BUS_UPDATE_ABNORMALLY("0003", "更新bus异常"),
    BUS_BUS_NO_ID_AND_DAY_NULL("0004", "传入的车次ID和天数不能为空"),
    BUS_BUS_ORIGINAL_NULL("0005", "原有的bus不存在"),
    BUS_BUS_HAS_BEEN_DISTRIBUTION("0006", "所选车辆已经被占用，请重新选择"),
    BUS_LICENSE_EXISTED("0007", "车牌号已被占用"),
    /**
     * =======================BUS_NO===========================
     **/
    BUS_NO_NULL("1001", "传入busNo或busNoId不能为空"),
    BUS_NO_NO_FOR_NULL("1002", "传入no不能为空"),
    BUS_NO_NULL_FOR_BUS_NO_AND_NODE("1003", "传入busNO或busNode不能为空"),
    BUS_NO_NO_IS_EXISTED("1004", "该车次号已经存在"),
    BUS_NO_UPDATE_ABNORMALLY("1005", "更新busNo异常"),
    BUS_NO_SEARCH_EXCEPTION("1006", "查询车次异常"),
    BUS_NO_LIST_NULL("1007", "传入的车次ID列表为空"),
    BUS_NO_PLAN_DEPEND_ON_BUS_NO("1008","车次计划buNoPlan依赖该车次,该车次无法删除"),
    TOURISM_ROUTE_DEPEND_ON_BUS_NO("1009","线路tourismRoute依赖该车次,该车次无法删除"),
    BUS_NO_NOT_EXIST("1010", "车次不存在"),
    BUS_NO_HAS_BEEN_STOPED("1011", "车次已停运"),

    /**
     * =======================BUS_NO_PLAN===========================
     **/
    BUS_NO_PLAN_NULL("2001", "传入busNoPlan或busNoPlanId不能为空"),
    BUS_NO_PLAN_ORIGINAL_NULL("2002", "原有busNoPlan为空"),
    BUS_NO_PLAN_DELETE_FAILD("2003", "车上已经有乘客,车次计划删除失败"),
    BUS_NO_PLAN_ADD_FAILD("2004", "车次计划添加失败"),
    /**
     * =======================BUS_LINE===========================
     **/
    BUS_LINE_NULL("3001", "传入line或者lineId不能为空"),
    BUS_LINE_CANT_DECOMPOSE("3002", "线路无法分解"),
    BUS_LINE_EXISTED("3003", "线路已经存在"),
    BUS_LINE_UPDATE_FAILD("3004", "线路更新失败"),
    BUS_LINE_BUS_NO_SERIAL_INVALID("3005", "线路车次序列错误"),
    BUS_LINE_NOT_EXIST("3006", "线路不存在"),
    /**
     * =======================BUS_NODE===========================
     **/
    BUS_NODE_LIST_NULL("4001", "传入node链表不能为空"),
    BUS_NODE_NULL("4002", "传入node或nodeId不能为空"),
    BUS_NODE_UPDATE_ERROR("4003", "node更新失败"),
    BUS_NODE_DESTINATION_ID_NULL("4004", "node节点中的destination的id不能为空"),
    BUS_NODE_DESTINATION_NULL("4005", "node节点关联的destination为空"),
    BUS_NODE_ARRIVETIME_EARLY("4006", "节点抵达时间早于上一个节点离开时间"),
    BUS_NODE_DEPARTTIME_LATE("4007", "节点离开时间早于到达时间"),
    BUS_NODE_DAY_EARLY("4008", "节点天数不能比上一个节点天数小"),
    BUS_NODE_LIST_LENGTH_FALSE("4009", "传入的node节点不能小于两个"),
    /**
     * =======================BUS_NODE_STATUS===========================
     **/
    BUS_NODE_STATUS_NULL("5001", "传入nodeStatus不能为空"),

    /**
     * =======================BUS_NODE_TYPE===========================
     **/
    BUS_NODE_TYPE_NULL("6001", "传入nodeType或nodeTypeId不能为空"),
    BUS_NODE_TYPE_NOT_EXISTED("6002", "nodeType不存在"),
    /**
     * =======================BUS_SEAT===========================
     **/
    BUS_SEAT_NULL("7001", "传入seat或seatId不能为空"),

    /**
     * =======================BUS_SEAT_LAYOUT===========================
     **/
    BUS_SEAT_LAYOUT_NULL("8001", "传入seatLayout或seatLayoutId不能为空"),
    BUS_SEAT_LAYOUT_NOT_EXIST("8002", "关联seatLayout不存在"),
    BUS_SEAT_LAYOUT_NAME_EXISTED("8003", "存在同名的座位模板"),
    BUS_SEAT_LATOUT_UPDATE_FAILD("8004", "座位模板更新失败"),
    BUS_SEAT_LATOUT_CAPACITY_ERROR("8005", "座位布局总容量错误"),
    /**
     * =======================BUS_SERIAL===========================
     **/
    BUS_SERIAL_NULL("9001", "传入bus_serial或busSerialId不能为空"),
    /**
     * =======================BUS_ASSISTANT===========================
     **/
    BUS_ASSISTANT_NOT_EXISTED("10001", "行车助理不存在或已禁用"),
    BUS_ASSISTANT_HAS_BEEN_DISTRIBUTE("10002", "行车助理已经被分配"),
    BUS_ASSISTANT_NULL("10003", "行车助理为空");

    String errorCode;
    String errorMessage;

    private static final String ns = "BIZ.TRANSPORT";

    TravelTransportBizError(String errorCode, String errorMessage) {
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
