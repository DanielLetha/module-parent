import com.simpletour.biz.resources.IDestinationBiz;
import com.simpletour.biz.traveltrans.IBusBiz;
import com.simpletour.biz.traveltrans.IBusNoBiz;
import com.simpletour.biz.traveltrans.IBusNoPlanBiz;
import com.simpletour.biz.traveltrans.ISeatLayoutBiz;
import com.simpletour.biz.traveltrans.bo.BusNoBo;
import com.simpletour.biz.traveltrans.error.TravelTransportBizError;
import com.simpletour.common.core.exception.BaseSystemException;
import com.simpletour.common.security.token.EncryptedToken;
import com.simpletour.common.security.token.Token;
import com.simpletour.dao.traveltrans.ITransportDao;
import com.simpletour.domain.resources.Area;
import com.simpletour.domain.resources.Destination;
import com.simpletour.domain.traveltrans.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.*;

/**
 * User: XuHui/xuhui@simpletour.com
 * Date: 2016/3/24
 * Time: 14:25
 */
@ContextConfiguration({"classpath:applicationContext.xml"})
public class BusNoPlanBizTest extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    private IBusNoPlanBiz busNoPlanBiz;
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
    private SeatLayout seatLayout;
    private Assistant assistant;

    @BeforeClass
    public void setUp() {
        new EncryptedToken("0", "0", "1", "2", Token.ClientType.BROWSER);
        setUpNodeType();
        setUpDestination();
        setUpBusNo();
        setUpBus();
        setUpAssistant();
    }

    @AfterTest
    public void teardown() {
        tearDownAssistant();
        tearDownBus();
        tearDownBusNo();
        tearDownDestination();
        tearDownNodeType();
    }


    @Test
    public void testAddBusNoPlan() {
        BusNoPlan busNoPlan = generateBusNoPlan();
        busNoPlan = busNoPlanBiz.addBusNoPlan(busNoPlan);
        Assert.assertNotNull(busNoPlan);
    }

    @Test
    public void testUpdateBusNoPlan() {
        BusNoPlan busNoPlan = generateBusNoPlan();
        busNoPlan=busNoPlanBiz.addBusNoPlan(busNoPlan);
        busNoPlan.setBus(bus2);
        busNoPlan=busNoPlanBiz.updateBusNoPlan(busNoPlan);
        Assert.assertEquals(busNoPlan.getBus().getId(),bus2.getId());
    }

    @Test
    public void testDeleteBusNoPlan(){
        BusNoPlan busNoPlan=generateBusNoPlan();
        busNoPlan=busNoPlanBiz.addBusNoPlan(busNoPlan);
        Long id=busNoPlan.getId();
        busNoPlanBiz.deleteBusNoPlanById(id);
        Assert.assertNull(busNoPlanBiz.getBusNoPlanById(id));
    }

    @Test
    public void testGetBusNoPlanById(){
        BusNoPlan busNoPlan=generateBusNoPlan();
        busNoPlan = busNoPlanBiz.addBusNoPlan(busNoPlan);
        Long id=busNoPlan.getId();
        BusNoPlan busNoPlan1 = busNoPlanBiz.getBusNoPlanById(id);
        Assert.assertEquals(busNoPlan1.getId(), busNoPlan.getId());
        Assert.assertEquals(busNoPlan1.getBus().getId(), busNoPlan.getBus().getId());
        Assert.assertEquals(busNoPlan1.getNo().getId(),busNoPlan.getNo().getId());
    }

    @Test
    public void testIsExisted(){
        BusNoPlan busNoPlan=generateBusNoPlan();
        busNoPlan = busNoPlanBiz.addBusNoPlan(busNoPlan);
        Assert.assertTrue(busNoPlanBiz.isExisted(busNoPlan.getId()));
        Assert.assertFalse(busNoPlanBiz.isExisted(Long.MAX_VALUE));
    }

    @Test
    public void testIsExistedNull(){
        try {
            busNoPlanBiz.isExisted(null);
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), TravelTransportBizError.BUS_NO_PLAN_NULL);
        }
    }

    private BusNoPlan generateBusNoPlan() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2116, 4, 5);
        BusNoPlan busNoPlan = new BusNoPlan();
        busNoPlan.setNo(busNo);
        busNoPlan.setDay(calendar.getTime());
        busNoPlan.setAssistant(assistant);
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
        List<Node> nodes = new ArrayList<>();
        Node firstNode = new Node(busNo, this.nodeType1, this.destination1, null, 0, 71100, 0);
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
    }

    private void tearDownBus() {
        if (bus1 != null)
            transportDao.removeEntity(bus1);
        if (bus2 != null)
            transportDao.removeEntity(bus2);
        if (seatLayout != null)
            transportDao.removeEntity(seatLayout);
    }

    private void setUpAssistant() {
        assistant = new Assistant(generateName(), generateName(), "123123", generateName(), generateName(), generateName());
        assistant = transportDao.save(assistant);
    }

    private void tearDownAssistant() {
        if (assistant != null)
            transportDao.removeEntity(assistant);
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
