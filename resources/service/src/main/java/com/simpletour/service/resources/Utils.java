package com.simpletour.service.resources;//package com.simpletour.service.resources;
//
//import com.simpletour.common.core.exception.BaseSystemException;
//import com.simpletour.service.resources.error.ResourcesServiceError;
//
//import java.math.BigDecimal;
//
///**
// * Created by yangdongfeng on 2015/11/21.
// */
//public class Utils {
//    static final double MAX_LONGITUDE = 180;
//    static final double MAX_LATITUDE = 90;
//    static final double MIN_LONGITUDE = -180;
//    static final double MIN_LATITUDE = -90;
//
//    /**
//     * @param lon
//     * @param lat
//     * @throws BaseSystemException
//     */
//    public static void verifyLongitudeAndLatitude(BigDecimal lon, BigDecimal lat) throws BaseSystemException {
//        verifyLongitudeAndLatitude(lon.doubleValue(), lat.doubleValue());
//    }
//
//    /**
//     *  验证经纬度是否为合法值
//     * @param lon
//     * @param lat
//     * @throws BaseSystemException 出错则抛出异常，无返回值
//     */
//    public static void verifyLongitudeAndLatitude(double lon, double lat) throws BaseSystemException {
//        if (lon > MAX_LONGITUDE || lon < MIN_LONGITUDE)
//            throw new BaseSystemException(ResourcesServiceError.ILLEGAL_LONGITUDE);
//        if (lat > MAX_LATITUDE || lat < MIN_LATITUDE)
//            throw new BaseSystemException(ResourcesServiceError.ILLEGAL_LATITUDE);
//    }
//}
