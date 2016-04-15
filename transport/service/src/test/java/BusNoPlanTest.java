import com.simpletour.biz.resources.IDestinationBiz;
import com.simpletour.biz.traveltrans.IBusBiz;
import com.simpletour.biz.traveltrans.IBusNoBiz;
import com.simpletour.biz.traveltrans.ISeatLayoutBiz;
import com.simpletour.biz.traveltrans.bo.BusNoBo;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.traveltrans.ITransportDao;
import com.simpletour.domain.traveltrans.*;
import com.simpletour.service.traveltrans.ITravelTransportService;
import com.simpletour.service.traveltrans.error.TravelTransportServiceError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import resources.Area;
import resources.Destination;

import java.math.BigDecimal;
import java.util.*;

/**
 * Author：XuHui/xuhui@simpletour.com
 * Brief：
 * Date: 2016/3/25
 * Time: 15:35
 */
@ContextConfiguration({"classpath:applicationContext.xml"})
public class BusNoPlanTest extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    private ITravelTransportService travelTransportService;
    @Autowired
    private IDestinationBiz destinationBiz;
    @Autowired
    private ITransportDao transportDao;
    @Autowired
    private IBusNoBiz busNoBiz;
    @Autowired
    private ISeatLayoutBiz seatLayoutBiz;
    @Autowired
    private IBusBiz busBiz;

    private NodeType nodeType1;
    private NodeType nodeType2;
    private Destination destination1;
    private Destination destination2;
    private BusNo busNo;
    private Bus bus1;
    private Bus bus2;
    private Bus bus3;
    private Bus bus4;
    private SeatLayout seatLayout;
    private Assistant assistant1;
    private Assistant assistant2;
    private Assistant assistant3;
    private Assistant assistant4;
    private List<Node> nodes = new ArrayList<>();

    private BusNoPlan busNoPlanExist1;
    private BusNoPlan busNoPlanExist2;


    @BeforeClass
    public void setUp() {
        new EncryptedToken("0", "0", "1", "2", Token.ClientType.BROWSER);
        setUpNodeType();
        setUpDestination();
        setUpBusNo();
        setUpBus();
        setUpAssistant();
        setUpBusNoPlan();
    }

    @AfterTest
    public void teardown() {
        tearDownBusNoPlan();
        tearDownAssistant();
        tearDownBus();
        tearDownBusNo();
        tearDownDestination();
        tearDownNodeType();
    }

    //test:正常添加
    @Test
    public void testAddBusNoPlan() {
        BusNoPlan busNoPlan = generateBusNoPlan();
        Optional<BusNoPlan> busNoPlanOptional = travelTransportService.addBusNoPlan(busNoPlan);
        Assert.assertTrue(busNoPlanOptional.isPresent());
        Assert.assertNotNull(busNoPlanOptional.get().getId());
    }

    //test:车次、车辆为空
    @Test
    public void testAddBusNoPlanNull() {
        BusNoPlan busNoPlan = null;
        try {
            travelTransportService.addBusNoPlan(busNoPlan);
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), TravelTransportServiceError.BUS_NO_PLAN_NULL);
        }

        busNoPlan = generateBusNoPlan();
        busNoPlan.setNo(null);
        try {
            travelTransportService.addBusNoPlan(busNoPlan);
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), TravelTransportServiceError.BUS_NO_NULL);
        }

        busNoPlan.setNo(new BusNo());
        try {
            travelTransportService.addBusNoPlan(busNoPlan);
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), TravelTransportServiceError.BUS_NO_NULL);
        }

        busNoPlan.setNo(busNo);
        busNoPlan.setBus(null);
        try {
            travelTransportService.addBusNoPlan(busNoPlan);
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), TravelTransportServiceError.BUS_NULL);
        }

        busNoPlan.setBus(new Bus());
        try {
            travelTransportService.addBusNoPlan(busNoPlan);
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), TravelTransportServiceError.BUS_NULL);
        }
    }

    //test:车辆停运
    @Test
    public void testAddBusNoPlanBusNotAvailable() {
        Bus bus = new Bus(seatLayout, "白飞龙_test_one", generateName(), false);
        bus = travelTransportService.addBus(bus).get();
        BusNoPlan busNoPlan = generateBusNoPlan();
        busNoPlan.setBus(bus);
        try {
            travelTransportService.addBusNoPlan(busNoPlan);
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), TravelTransportServiceError.BUS_NOT_ONLINE);
        }
    }

    //test:车次停运
    @Test
    public void testAddBusNoPlanBusNoNotAvailable() {
        BusNo busNo = new BusNo(generateName(), 96, 8100, 71100, 79200, "花水湾正常", "成都正常", false, BusNo.Status.stop, false, nodes);
        BusNoBo busNoBo = new BusNoBo(busNo);
        busNoBo.setStatus(BusNo.Status.stop);
        busNo = travelTransportService.addBusNo(busNoBo).get();
        BusNoPlan busNoPlan = generateBusNoPlan();
        busNoPlan.setNo(busNo);
        try {
            travelTransportService.addBusNoPlan(busNoPlan);
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), TravelTransportServiceError.BUS_NO_STOPED);
        }
    }

    // TODO: 2016/3/25 Available测试
    //test: 测试车辆被使用
    @Test
    public void testAddBusNoPlanBusDistribution() {
        BusNoPlan busNoPlan2 = generateBusNoPlan();
        busNoPlan2.setBus(bus3);
        try {
            busNoPlan2 = travelTransportService.addBusNoPlan(busNoPlan2).get();
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), TravelTransportServiceError.BUS_BUS_HAS_BEEN_DISTRIBUTION);
        }
    }

    //test:测试行车助理占用
    @Test
    public void testAddBusNoPlanAssistantDistribution() {
        BusNoPlan busNoPlan2 = generateBusNoPlan();
        busNoPlan2.setAssistant(assistant3);
        try {
            busNoPlan2 = travelTransportService.addBusNoPlan(busNoPlan2).get();
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), TravelTransportServiceError.BUS_ASSISTANT_HAS_BEEN_DISTRIBUTE);
        }
    }

    //test:正常更新
    @Test
    public void testUpdateBusNoPlan() {
        BusNoPlan busNoPlan1 = generateBusNoPlan();
        busNoPlan1 = travelTransportService.addBusNoPlan(busNoPlan1).get();
        busNoPlan1.setAssistant(assistant2);
        busNoPlan1 = travelTransportService.updateBusNoPlan(busNoPlan1).get();
        Assert.assertEquals(busNoPlan1.getAssistant().getId(), assistant2.getId());
    }

    //test:车次、车辆为空
    @Test
    public void testUpdateBusNoPlanNull() {
        BusNoPlan busNoPlan = generateBusNoPlan();
        busNoPlan = travelTransportService.addBusNoPlan(busNoPlan).get();

        try {
            travelTransportService.updateBusNoPlan(null);
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), TravelTransportServiceError.BUS_NO_PLAN_NULL);
        }

        busNoPlan = generateBusNoPlan();
        busNoPlan.setNo(null);
        try {
            travelTransportService.updateBusNoPlan(busNoPlan);
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), TravelTransportServiceError.BUS_NO_NULL);
        }

        busNoPlan.setNo(new BusNo());
        try {
            travelTransportService.updateBusNoPlan(busNoPlan);
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), TravelTransportServiceError.BUS_NO_NULL);
        }

        busNoPlan.setNo(busNo);
        busNoPlan.setBus(null);
        try {
            travelTransportService.updateBusNoPlan(busNoPlan);
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), TravelTransportServiceError.BUS_NULL);
        }

        busNoPlan.setBus(new Bus());
        try {
            travelTransportService.updateBusNoPlan(busNoPlan);
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), TravelTransportServiceError.BUS_NULL);
        }
    }

    //test:测试不存在
    @Test
    public void testUpdateBusNoPlanNotExist() {
        BusNoPlan busNoPlan = generateBusNoPlan();
        busNoPlan = travelTransportService.addBusNoPlan(busNoPlan).get();
        busNoPlan.setId(Long.MAX_VALUE);
        try {
            travelTransportService.updateBusNoPlan(busNoPlan);
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), TravelTransportServiceError.BUS_NO_PLAN_NOT_EXIST);
        }
    }

    //test: 测试车辆被使用
    @Test
    public void testUpdateBusDistribution() {
        BusNoPlan busNoPlan1 = busNoPlanExist2;
        busNoPlan1.setBus(bus3);
        try {
            travelTransportService.updateBusNoPlan(busNoPlan1);
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), TravelTransportServiceError.BUS_BUS_HAS_BEEN_DISTRIBUTION);
        }
    }

    //test:测试行车助理被已排班
    @Test
    public void testUpdateAssistantDistribution() {
        BusNoPlan busNoPlan1 = busNoPlanExist2;

        busNoPlan1.setAssistant(assistant3);
        try {
            travelTransportService.updateBusNoPlan(busNoPlan1);
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), TravelTransportServiceError.BUS_ASSISTANT_HAS_BEEN_DISTRIBUTE);
        }
    }

    @Test
    public void testDeltetBusNoPlanNotExist() {
        BusNoPlan busNoPlan = generateBusNoPlan();
        busNoPlan = travelTransportService.addBusNoPlan(busNoPlan).get();
        Long id = busNoPlan.getId();
        travelTransportService.deleteBusNoPlanById(id);
        try {
            travelTransportService.findBusNoPlanById(id);
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), TravelTransportServiceError.BUS_NO_PLAN_NOT_EXIST);
        }
    }

    private BusNoPlan generateBusNoPlan() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2016);
        calendar.set(Calendar.MONTH, 5);
        calendar.set(Calendar.DATE, 4);

        BusNoPlan busNoPlan = new BusNoPlan();
        busNoPlan.setNo(busNo);
        busNoPlan.setDay(calendar.getTime());
        busNoPlan.setAssistant(assistant1);
        busNoPlan.setBus(bus1);
        busNoPlan.setCapacity(37);
        return busNoPlan;
    }

    private void setUpNodeType() {
        nodeType1 = transportDao.save(new NodeType("出发点", null, null, true));
        nodeType2 = transportDao.save(new NodeType("途径点", null, null, true));
    }

    private void tearDownNodeType() {
        if (nodeType1 != null)
            transportDao.removeEntity(nodeType1);
        if (nodeType2 != null)
            transportDao.removeEntity(nodeType2);
    }

    private void setUpDestination() {
        Area area = new Area();
        area.setId(110100L);
        destination1 = destinationBiz.addDestination(new Destination(generateName(), "1 address", "1 remark", area, new BigDecimal(170), new BigDecimal(70)));
        destination2 = destinationBiz.addDestination(new Destination(generateName(), "2 address", "2 remark", area, new BigDecimal(170), new BigDecimal(70)));
    }

    private void tearDownDestination() {
        if (destination1 != null)
            transportDao.removeEntity(destination1);
        if (destination2 != null)
            transportDao.removeEntity(destination2);
    }

    private void setUpBusNo() {
        Node firstNode = new Node(busNo, this.nodeType1, this.destination1, null, 0, 0, 0);
        Node secondNode = new Node(busNo, this.nodeType2, this.destination2, null, 0, 79200, 0);
        nodes.add(firstNode);
        nodes.add(secondNode);
        busNo = new BusNo(generateName(), 96, 8100, 71100, 79200, "花水湾正常", "成都正常", false, BusNo.Status.normal, false, nodes);
        busNo = busNoBiz.addBusNo(new BusNoBo(busNo)).get();
    }

    private void tearDownBusNo() {
        if (busNo != null)
            transportDao.removeEntity(busNo);
    }

    private void setUpBus() {
        SeatLayout.Type[] layoutType = SeatLayout.Type.class.getEnumConstants();
        List<Seat> seatList = new ArrayList<>();
        Arrays.asList(1, 2, 3, 4, 5, 6).forEach(index -> {
            seatList.add(new Seat(Seat.Type.SEAT, index, "A"));
            seatList.add(new Seat(Seat.Type.EMPTY, index, "B"));
            seatList.add(new Seat(Seat.Type.SEAT, index, "C"));
        });
        SeatLayout checkBase = new SeatLayout(
                "白飞龙_test" + new Random().nextInt(10000),
                layoutType[new Random().nextInt(layoutType.length)],
                6, 3, 12, seatList);
        seatList.forEach(seat -> seat.setSeatLayout(checkBase));
        this.seatLayout = seatLayoutBiz.addSeatLayout(checkBase).get();
        bus1 = new Bus(seatLayout, "白飞龙_test_one", generateName(), true);
        bus1 = busBiz.addBus(bus1).get();

        bus2 = new Bus(seatLayout, "白飞龙_test_one", generateName(), true);
        bus2 = busBiz.addBus(bus2).get();

        bus3 = new Bus(seatLayout, "白飞龙_test_one", generateName(), true);
        bus3 = busBiz.addBus(bus3).get();

        bus4 = new Bus(seatLayout, "白飞龙_test_one", generateName(), true);
        bus4 = busBiz.addBus(bus4).get();
    }

    private void tearDownBus() {
        if (bus1 != null)
            transportDao.removeEntity(bus1);
        if (bus2 != null)
            transportDao.removeEntity(bus2);
        if (bus3 != null)
            transportDao.removeEntity(bus3);
        if (bus4 != null)
            transportDao.removeEntity(bus4);
        if (seatLayout != null)
            transportDao.removeEntity(seatLayout);
    }

    private void setUpAssistant() {
        assistant1 = new Assistant(generateName(), generateName(), "123123", generateName(), generateName(), generateName());
        assistant1 = transportDao.save(assistant1);
        assistant2 = new Assistant(generateName(), generateName(), "123123", generateName(), generateName(), generateName());
        assistant2 = transportDao.save(assistant2);
        assistant3 = new Assistant(generateName(), generateName(), "123123", generateName(), generateName(), generateName());
        assistant3 = transportDao.save(assistant3);
        assistant4 = new Assistant(generateName(), generateName(), "123123", generateName(), generateName(), generateName());
        assistant4 = transportDao.save(assistant4);
    }

    private void tearDownAssistant() {
        if (assistant1 != null)
            transportDao.removeEntity(assistant1);
        if (assistant2 != null)
            transportDao.removeEntity(assistant2);
        if (assistant3 != null)
            transportDao.removeEntity(assistant3);
        if (assistant4 != null)
            transportDao.removeEntity(assistant4);
    }

    private void setUpBusNoPlan() {
        busNoPlanExist1 = generateBusNoPlan();
        busNoPlanExist1.setAssistant(assistant3);
        busNoPlanExist1.setBus(bus3);
        Optional<BusNoPlan> busNoPlanOptional = travelTransportService.addBusNoPlan(busNoPlanExist1);
        Assert.assertTrue(busNoPlanOptional.isPresent());
        Assert.assertNotNull(busNoPlanOptional.get().getId());
        busNoPlanExist1 = busNoPlanOptional.get();

        busNoPlanExist2 = generateBusNoPlan();
        busNoPlanExist2.setAssistant(assistant4);
        busNoPlanExist2.setBus(bus4);
        Optional<BusNoPlan> busNoPlanOptional2 = travelTransportService.addBusNoPlan(busNoPlanExist2);
        Assert.assertTrue(busNoPlanOptional.isPresent());
        Assert.assertNotNull(busNoPlanOptional.get().getId());
        busNoPlanExist2 = busNoPlanOptional2.get();
    }

    private void tearDownBusNoPlan() {
        if (busNoPlanExist1 != null)
            transportDao.removeEntity(busNoPlanExist1);
        if (busNoPlanExist2 != null)
            transportDao.removeEntity(busNoPlanExist2);
    }


    public static String generateName() {
        String name = "";
        for (int i = 0; i < 10; ++i) {
            Integer idx = Double.valueOf(Math.random() * 25).intValue();
            char le = (char) ('a' + idx);
            name += le;
        }
        return new Date().getTime() + name;
    }

}
