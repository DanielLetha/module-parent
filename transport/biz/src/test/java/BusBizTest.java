import com.simpletour.biz.traveltrans.IBusBiz;
import com.simpletour.biz.traveltrans.ISeatLayoutBiz;
import com.simpletour.biz.traveltrans.error.TravelTransportBizError;
import com.simpletour.common.security.token.EncryptedToken;
import com.simpletour.common.security.token.Token;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.test.TestClassWithLazyLoad;
import com.simpletour.domain.traveltrans.Bus;
import com.simpletour.domain.traveltrans.Seat;
import com.simpletour.domain.traveltrans.SeatLayout;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.*;

/**
 * BusBizImpl Tester.
 *
 * @author 白飞龙
 * @version 1.0
 * @since <pre>2016-03-23</pre>
 */
@ContextConfiguration({"classpath:applicationContext.xml"})
@Transactional
public class BusBizTest extends TestClassWithLazyLoad {

    @Resource
    IBusBiz iBusBiz;

    @Resource
    ISeatLayoutBiz iSeatLayoutBiz;

    private Long busId;

    private Optional<Bus> bus;

    private Optional<Bus> secondBus;

    /**
     * 座位布局
     */
    private Optional<SeatLayout> seatLayout;

    /**
     * 查询条件
     */
    private Map<String, Object> params;


    @BeforeClass
    public void before() throws Exception {
        SeatLayout.Type[] layoutType = SeatLayout.Type.class.getEnumConstants();
        List<Seat> seatList = new ArrayList<>();
        Arrays.asList(1, 2, 3).forEach(index -> seatList.add(new Seat(Seat.Type.SEAT, index, "A")));
        SeatLayout checkBase = new SeatLayout(
                "白飞龙_test" + new Random().nextInt(10000),
                layoutType[new Random().nextInt(layoutType.length)],
                3, 1, 3, seatList);
        seatList.forEach(seat -> seat.setSeatLayout(checkBase));
        this.seatLayout = iSeatLayoutBiz.addSeatLayout(checkBase);
        params = new HashMap<>();
        params.put("model", "白飞龙");

        new EncryptedToken("1", "1", "1", "127.0.0.1", Token.ClientType.BROWSER);
    }

    @AfterClass
    public void after() throws Exception {
        iSeatLayoutBiz.deleteSeatLayout(seatLayout.get());
    }

    /**
     * 基本保存逻辑测试
     * Method: addBus(Bus bus)
     */
    @Test(priority = 1)
    public void testAddBus() throws Exception {
        Bus tmpOneBus = new Bus(seatLayout.get(), "白飞龙_test_one", "川W00000", true);
        Bus tmpSecondBus = new Bus(seatLayout.get(), "白飞龙_test_two", "川Z00000", true);
        bus = iBusBiz.addBus(tmpOneBus);
        secondBus = iBusBiz.addBus(tmpSecondBus);
        Assert.assertTrue(bus.isPresent());
        Assert.assertTrue(secondBus.isPresent());
        busId = bus.get().getId();
    }

    /**
     * 车牌号唯一性测试
     * Method: addBus(Bus bus)
     */
    @Test(priority = 2)
    public void testUniqueLicense() throws Exception {
        Bus tmpBus = new Bus(seatLayout.get(), "白飞龙_test_one", bus.get().getLicense(), true);
        try {
            bus = iBusBiz.addBus(tmpBus);
            Assert.assertFalse(true);
        } catch (Exception e) {
            Assert.assertEquals(TravelTransportBizError.BUS_LICENSE_EXISTED.getErrorMessage(), e.getMessage());
        }

    }

    /**
     * Method: findBusById(Long id)
     */
    @Test(priority = 4)
    public void testFindBusById() throws Exception {
        bus = iBusBiz.findBusById(busId);
        Assert.assertTrue(bus.isPresent());
        busId = bus.get().getId();
    }

    /**
     * 正常更新逻辑测试
     * Method: updateBus(Bus bus)
     */
    @Test(priority = 5)
    public void testUpdateBus() throws Exception {
        Bus tmpBus = bus.get();
        tmpBus.setLicense("川W1111W");
        bus = iBusBiz.updateBus(tmpBus);
        Assert.assertEquals(bus.get().getLicense(), "川W1111W");
    }

    /**
     * 车牌号唯一性更新测试
     * Method: updateBus(Bus bus)
     */
    @Test(priority = 6)
    public void testUpdateUniqueLicense() throws Exception {
        Bus tmpBus = new Bus(bus.get());
        tmpBus.setLicense(secondBus.get().getLicense());
        try {
            bus = iBusBiz.updateBus(tmpBus);
            Assert.assertFalse(true);
        } catch (Exception e) {
            Assert.assertEquals(TravelTransportBizError.BUS_LICENSE_EXISTED.getErrorMessage(), e.getMessage());
        }
    }

    /**
     * 对象更新非空测试
     * Method: updateBus(Bus bus)
     */
    @Test(priority = 7)
    public void testUpdateEmpty() throws Exception {
        Bus tmpBus = new Bus(secondBus.get());
        try {
            iBusBiz.deleteBus(secondBus.get());
            bus = iBusBiz.updateBus(tmpBus);
            Assert.assertFalse(true);
        } catch (Exception e) {
            Assert.assertEquals(TravelTransportBizError.BUS_UPDATE_ABNORMALLY.getErrorMessage(), e.getMessage());
        }
    }

    /**
     * Method: findBusPageByConditions(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, Integer pageIndex, Integer pageSize, boolean byLike)
     */
    @Test(priority = 8)
    public void testFindBusPageByConditions() throws Exception {
        DomainPage<Bus> results = iBusBiz.findBusPageByConditions(params, "model", IBaseDao.SortBy.ASC, 1, 10, true);
        Assert.assertNotNull(results);
        Assert.assertTrue(results.getDomains().size() > 0);
    }

    /**
     * Method: findBusByConditions(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy)
     */
    @Test(priority = 9)
    public void testFindBusByConditions() throws Exception {
        params.put("model", "白飞龙_test_one");
        List<Bus> results = iBusBiz.findBusByConditions(params, "model", IBaseDao.SortBy.ASC);
        Assert.assertNotNull(results);
        Assert.assertTrue(results.size() > 0);
    }

    /**
     * Method: findAvailableBus(String license, Long busNoId, Date day, int pageIndex, int pageSize)
     */
    @Test(priority = 10)
    public void testFindAvailableBus() throws Exception {
        new EncryptedToken("1", "1", "1", "127.0.0.1", Token.ClientType.BROWSER);
        try {
            iBusBiz.findAvailableBus(null, -1L, new Date(), 1, 10);
            Assert.assertFalse(true);
        } catch (Exception e) {
            Assert.assertEquals(TravelTransportBizError.BUS_NO_SEARCH_EXCEPTION.getErrorMessage(), e.getMessage());
        }
    }

    /**
     * Method: deleteBus(Bus bus)
     */
    @Test(priority = 11)
    public void testDeleteBus() throws Exception {
        Boolean result = iBusBiz.deleteBus(bus.get());
        Assert.assertTrue(result);
    }
}
