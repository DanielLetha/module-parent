package com.simpletour.test;

import com.simpletour.biz.product.IProductBiz;
import com.simpletour.biz.product.imp.TourismBusAllocation;
import com.simpletour.dao.traveltrans.ITransportDao;
import com.simpletour.domain.order.CertIdentity;
import com.simpletour.domain.product.Product;
import com.simpletour.domain.product.TourismRoute;
import com.simpletour.domain.traveltrans.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangdongfeng on 2015/12/9.
 */
@ContextConfiguration({"classpath:applicationContextForAllocation.xml"})
public class TourismBusAllocationTest extends AbstractTestNGSpringContextTests {

    @Resource
    private IProductBiz productBiz;

    @Resource
    private ITransportDao transportDao;

    class BaseInfo {
        public Long id;
    }

    //=================================================================================================================
    class BusNoInfo extends BaseInfo {
        String name;
        Boolean transferable;
        String depart;
        String arrive;
        BusNo busNo;
        Integer departTime;
        Integer arriveTime;

        BusNoInfo(String name, Boolean transferable, String depart, String arrive, Integer departTime, Integer arriveTime) {
            this.name = name;
            this.transferable = transferable;
            this.depart = depart;
            this.arrive = arrive;
            this.departTime = departTime;
            this.arriveTime = arriveTime;
        }
    }

    BusNoInfo[] busNoInfos = {
            new BusNoInfo("CD02CZS", true, "CD", "CZS", 1, 2),
            new BusNoInfo("CZS02HY", false, "CZS", "HY", 5, 6),
            new BusNoInfo("HY02CD", false, "HY", "CD", 3, 4),

            new BusNoInfo("CD01CZS", false, "CD", "CZS", 1, 2),
            new BusNoInfo("CZS01JZG", true, "CZS", "JZG", 3, 4),

            new BusNoInfo("JZG11CZS", false, "JZG", "CZS", 1, 2),
            new BusNoInfo("CZS11HL", true, "CZS", "HL", 3, 4),
            new BusNoInfo("CZS11JZG", true, "CZS", "JZG", 5, 6),

            new BusNoInfo("JZG01CZS", false, "JZG", "CZS", 1, 2),
            new BusNoInfo("CZS01CD", false, "CZS", "CD", 3, 4),
    };
    //=================================================================================================================

    //=================================================================================================================
    class LineInfo extends BaseInfo {
        String name;
        Line line;

        LineInfo(String name) {
            this.name = name;
        }
    }

    LineInfo[] lineInfos = {
            new LineInfo("01"),
            new LineInfo("02")
    };
    //=================================================================================================================

    //=================================================================================================================
    class TourismRouteInfo extends BaseInfo {
        Integer busNoIdx;
        Integer offset;
        TourismRoute tourismRoute;

        public TourismRouteInfo(Integer busNoIdx, Integer offset) {
            this.busNoIdx = busNoIdx;
            this.offset = offset;
        }
    }

    class TourismInfo extends BaseInfo {
        String name;
        String depart;
        String arrive;
        Product tourism;

        TourismInfo(String name, String depart, String arrive) {
            this.name = name;
            this.depart = depart;
            this.arrive = arrive;
        }
    }

    TourismInfo[] tourismInfos = {
            new TourismInfo("T1", "CZS", "CD"),
            new TourismInfo("T2", "CD", "CD"),
            new TourismInfo("T3", "CD", "CD"),
            new TourismInfo("T4", "CD", "CD")
    };

    TourismRouteInfo[][] tourismRouteInfos = {
            {   // T1
                    new TourismRouteInfo(1, 0),
                    new TourismRouteInfo(2, 1)
            },
            {   // T2
                    new TourismRouteInfo(3, 0),
                    new TourismRouteInfo(4, 0),
                    new TourismRouteInfo(8, 1),
                    new TourismRouteInfo(9, 1),
            },
            {   // T3
                    new TourismRouteInfo(3, 0),
                    new TourismRouteInfo(4, 0),
                    new TourismRouteInfo(5, 2),
                    new TourismRouteInfo(6, 2),
                    new TourismRouteInfo(7, 2),
                    new TourismRouteInfo(8, 3),
                    new TourismRouteInfo(9, 3),
            },
            {   // T4
                    new TourismRouteInfo(3, 0),
                    new TourismRouteInfo(4, 0),
                    new TourismRouteInfo(8, 1),
                    new TourismRouteInfo(1, 1),
                    new TourismRouteInfo(2, 2),
            }
    };
    //=================================================================================================================

    //=================================================================================================================
    class BusNoSerialInfo extends BaseInfo {
        Integer lineIdx;
        Integer busNoIdx;
        Integer day;
        Integer sort;

        BusNoSerialInfo(Integer lineIdx, Integer busNoIdx, Integer day, Integer sort) {
            this.lineIdx = lineIdx;
            this.busNoIdx = busNoIdx;
            this.day = day;
            this.sort = sort;
        }
    }

    BusNoSerialInfo[] busNoSerialInfos = {
            new BusNoSerialInfo(0, 0, 0, 0),
            new BusNoSerialInfo(0, 1, 0, 1),
            new BusNoSerialInfo(0, 2, 1, 2),
            new BusNoSerialInfo(1, 3, 0, 0),
            new BusNoSerialInfo(1, 4, 0, 1),
            new BusNoSerialInfo(1, 5, 1, 2),
            new BusNoSerialInfo(1, 6, 1, 3),
            new BusNoSerialInfo(1, 7, 1, 4),
            new BusNoSerialInfo(1, 8, 2, 5),
            new BusNoSerialInfo(1, 9, 2, 6),
    };
    //=================================================================================================================

    //=================================================================================================================
    class BusInfo extends BaseInfo {
        String license;
        Bus bus;

        BusInfo(String license) {
            this.license = license;
        }
    }

    BusInfo[] busInfos = {
            new BusInfo("川A111111"),
            new BusInfo("川A111112"),
            new BusInfo("川A111113"),
            new BusInfo("川A111114"),
            new BusInfo("川A111115"),

            new BusInfo("川A111116"),
            new BusInfo("川A111117"),
            new BusInfo("川A111118"),
            new BusInfo("川A111119"),
            new BusInfo("川A111110"),
    };
    //=================================================================================================================

    //=================================================================================================================
    class BusNoPlanInfo extends BaseInfo {
        Integer busNoIdx;
        Integer busIdx;
        String day;
        Integer capacity;
        Integer num;
        BusNoPlan busNoPlan;

        BusNoPlanInfo(Integer busNoIdx, Integer busIdx, String day, Integer capacity) {
            this(busNoIdx, busIdx, day, capacity, 1);
        }

        BusNoPlanInfo(Integer busNoIdx, Integer busIdx, String day, Integer capacity, Integer num) {
            this.busNoIdx = busNoIdx;
            this.busIdx = busIdx;
            this.day = day;
            this.capacity = capacity;
            this.num = num;
        }
    }

    BusNoPlanInfo[] busNoPlanInfos = {
            new BusNoPlanInfo(0, 0, "2015-12-11", 37),
            new BusNoPlanInfo(1, 1, "2015-12-11", 37),
            new BusNoPlanInfo(2, 1, "2015-12-11", 37),
            new BusNoPlanInfo(3, 2, "2015-12-11", 37),
            new BusNoPlanInfo(4, 2, "2015-12-11", 37),
            new BusNoPlanInfo(5, 3, "2015-12-11", 37),
            new BusNoPlanInfo(6, 3, "2015-12-11", 37),
            new BusNoPlanInfo(7, 4, "2015-12-11", 37),
            new BusNoPlanInfo(8, 5, "2015-12-11", 37),
            new BusNoPlanInfo(9, 5, "2015-12-11", 37),

            new BusNoPlanInfo(0, 0, "2015-12-12", 37),
            new BusNoPlanInfo(1, 1, "2015-12-12", 37),
            new BusNoPlanInfo(2, 1, "2015-12-12", 37),
            new BusNoPlanInfo(3, 2, "2015-12-12", 37),
            new BusNoPlanInfo(4, 2, "2015-12-12", 37),
            new BusNoPlanInfo(5, 3, "2015-12-12", 37),
            new BusNoPlanInfo(6, 3, "2015-12-12", 37),
            new BusNoPlanInfo(7, 4, "2015-12-12", 37),
            new BusNoPlanInfo(8, 5, "2015-12-12", 37),
            new BusNoPlanInfo(9, 5, "2015-12-12", 37),

            new BusNoPlanInfo(0, 0, "2015-12-13", 37),
            new BusNoPlanInfo(1, 1, "2015-12-13", 37),
            new BusNoPlanInfo(2, 1, "2015-12-13", 37),
            new BusNoPlanInfo(3, 2, "2015-12-13", 37),
            new BusNoPlanInfo(4, 2, "2015-12-13", 37),
            new BusNoPlanInfo(5, 3, "2015-12-13", 37),
            new BusNoPlanInfo(6, 3, "2015-12-13", 37),
            new BusNoPlanInfo(7, 4, "2015-12-13", 37),
            new BusNoPlanInfo(8, 5, "2015-12-13", 37),
            new BusNoPlanInfo(9, 5, "2015-12-13", 37),

            new BusNoPlanInfo(0, 0, "2015-12-14", 37),
            new BusNoPlanInfo(1, 1, "2015-12-14", 37),
            new BusNoPlanInfo(2, 1, "2015-12-14", 37),
            new BusNoPlanInfo(3, 2, "2015-12-14", 37),
            new BusNoPlanInfo(4, 2, "2015-12-14", 37),
            new BusNoPlanInfo(5, 3, "2015-12-14", 37),
            new BusNoPlanInfo(6, 3, "2015-12-14", 37),
            new BusNoPlanInfo(7, 4, "2015-12-14", 37),
            new BusNoPlanInfo(8, 5, "2015-12-14", 37),
            new BusNoPlanInfo(9, 5, "2015-12-14", 37),
    };

    //=================================================================================================================

    //=================================================================================================================
    class CertIdentityInfo {
        Integer busNoIdx;
        Integer busIdx;
        String date;
        Integer num;

        CertIdentityInfo(Integer busNoIdx, Integer busIdx, String date, Integer num) {
            this.busNoIdx = busNoIdx;
            this.busIdx = busIdx;
            this.date = date;
            this.num = num;
        }
    }

    CertIdentityInfo[] certIdentityInfos = {

    };

    //=================================================================================================================
    private void addBus() {
        for (BusInfo busInfo : busInfos) {
            Bus bus = new Bus(null, "", busInfo.license, true);
            busInfo.bus = transportDao.save(bus);
        }
    }

    private void addLines() {
        for (LineInfo lineInfo : lineInfos) {
            Line line = new Line();
            line.setName(lineInfo.name);
            lineInfo.line = transportDao.save(line);
        }
    }

    private void addBusNos() {
        for (BusNoInfo busNoInfo : busNoInfos) {
            BusNo busNo = new BusNo(busNoInfo.name, 1, 1, busNoInfo.departTime, busNoInfo.arriveTime, busNoInfo.depart, busNoInfo.arrive, false, BusNo.Status.normal, busNoInfo.transferable, null);
            busNoInfo.busNo = transportDao.save(busNo);
        }
    }

    private void addBusNoPlan() throws ParseException {
        for (BusNoPlanInfo busNoPlanInfo : busNoPlanInfos) {
            for (int i = 0; i < busNoPlanInfo.num; ++i) {
                BusNoPlan busNoPlan = new BusNoPlan();
                busNoPlan.setBus(busInfos[busNoPlanInfo.busIdx].bus);
                busNoPlan.setNo(busNoInfos[busNoPlanInfo.busNoIdx].busNo);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                busNoPlan.setDay(simpleDateFormat.parse(busNoPlanInfo.day));
                busNoPlan.setCapacity(busNoPlanInfo.capacity);
                busNoPlanInfo.busNoPlan = transportDao.save(busNoPlan);
            }
        }
    }

    private void addCertIdentity() throws ParseException {
        for (CertIdentityInfo certIdentityInfo : certIdentityInfos) {
            for (int i = 0; i < certIdentityInfo.num; ++i) {
                CertIdentity certIdentity = new CertIdentity();
                certIdentity.setBus(busInfos[certIdentityInfo.busIdx].bus);
                certIdentity.setBusNo(busNoInfos[certIdentityInfo.busNoIdx].busNo);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                certIdentity.setDate(simpleDateFormat.parse(certIdentityInfo.date));
            }
        }
    }

    private void addBusNoSerial() {
        for (BusNoSerialInfo busNoSerialInfo : busNoSerialInfos) {
            BusNoSerial busNoSerial = new BusNoSerial(lineInfos[busNoSerialInfo.lineIdx].line,
                    busNoInfos[busNoSerialInfo.busNoIdx].busNo, busNoSerialInfo.day, busNoSerialInfo.sort);
            transportDao.save(busNoSerial);
        }
    }

    private void addTourism() {
        for (int i = 0; i < tourismInfos.length; ++i) {
            TourismInfo tourismInfo = tourismInfos[i];
            TourismRouteInfo[] aTourismRouteInfos = tourismRouteInfos[i];
            List<TourismRoute> tourismRoutes = new ArrayList<>(aTourismRouteInfos.length);
            for (TourismRouteInfo aTourismRouteInfo : aTourismRouteInfos) {
                TourismRoute tourismRoute = new TourismRoute(busNoInfos[aTourismRouteInfo.busNoIdx].busNo, aTourismRouteInfo.offset);
                tourismRoutes.add(tourismRoute);
            }
            Product tourism = new Product();//(0L, tourismInfo.name + ProductTest.generateName(), false, tourismInfo.depart, tourismInfo.arrive, "00:00", "00:01", tourismRoutes);
            tourismInfo.tourism = productBiz.addProduct(tourism);
        }
    }

    private void initTourismRes() {
        addLines();
        addBusNos();
        addBusNoSerial();
        addTourism();
    }

    //    @Test(priority = 0)
    public void testAddTourism() {
        initTourismRes();
        addTourism();
    }

    private void initBusRes() throws ParseException {
        initTourismRes();
        addBus();
        addBusNoPlan();
    }

    //@Test(priority = 1)
    public void testAllocateAlgorithm() throws ParseException {
        initBusRes();

        for (TourismInfo tourismInfo : tourismInfos) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            List<List<TourismBusAllocation.BusQuantity>> result = productBiz.allocate(tourismInfo.tourism, simpleDateFormat.parse("2015-12-11"), 3, TourismBusAllocation.ADAPTIVE);
            for (List<TourismBusAllocation.BusQuantity> busQuantities : result) {
                for (TourismBusAllocation.BusQuantity busQuantity : busQuantities) {
                    System.out.println(busQuantity);
                }
            }
            System.out.println();
        }
    }
}


