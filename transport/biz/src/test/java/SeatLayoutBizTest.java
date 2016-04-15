import com.simpletour.biz.traveltrans.ISeatLayoutBiz;
import com.simpletour.biz.traveltrans.error.TravelTransportBizError;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.common.security.token.EncryptedToken;
import com.simpletour.common.security.token.Token;
import com.simpletour.domain.traveltrans.Seat;
import com.simpletour.domain.traveltrans.SeatLayout;
import com.simpletour.commons.test.TestClassWithLazyLoad;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.*;

/**
 * SeatLayoutBizImpl Tester.
 *
 * @author 白飞龙
 * @version 1.0
 * @since <pre>2016-03-21</pre>
 */
@ContextConfiguration({"classpath:applicationContext.xml"})
@Transactional
public class SeatLayoutBizTest extends TestClassWithLazyLoad {

    @Resource
    ISeatLayoutBiz iSeatLayoutBiz;

    private Long seatLayoutId;

    private Optional<SeatLayout> seatLayout;

    /**
     * 座位
     */
    private List<Seat> seatList;

    /**
     * 座位布局类型
     */
    private SeatLayout.Type[] layoutType;

    /**
     * 初始容量为12的座位集合
     */
    @BeforeClass
    public void before() {
        layoutType = SeatLayout.Type.class.getEnumConstants();
        List<Seat> seatList = new ArrayList<>();
        Arrays.asList(1, 2, 3, 4, 5, 6).forEach(index -> {
            seatList.add(new Seat(Seat.Type.SEAT, index, "A"));
            seatList.add(new Seat(Seat.Type.EMPTY, index, "B"));
            seatList.add(new Seat(Seat.Type.SEAT, index, "C"));
        });
        this.seatList = seatList;
        new EncryptedToken("1", "1", "1", "127.0.0.1", Token.ClientType.BROWSER);
    }

    /**
     * 基本保存逻辑验证
     * Method: addSeatLayout(SeatLayout seatLayout)
     */
    @Test(priority = 1)
    public void testAddSeatLayout() throws Exception {
        SeatLayout checkBase = new SeatLayout(
                "白飞龙_test" + new Random().nextInt(10000),
                layoutType[new Random().nextInt(layoutType.length)],
                6, 3, 12, seatList);
        this.seatList.forEach(seat -> seat.setSeatLayout(checkBase));
        this.seatLayout = iSeatLayoutBiz.addSeatLayout(checkBase);
        if (this.seatLayout.isPresent()) {
            this.seatLayoutId = this.seatLayout.get().getId();
        }
        Assert.assertTrue(this.seatLayout.isPresent());
        System.out.println("checkBase: " + this.seatLayout.isPresent());
    }

    /**
     * 验证容量是否正确
     * Method: addSeatLayout(SeatLayout seatLayout)
     */
    @Test(priority = 2)
    public void testCapacity(){
        SeatLayout checkCap = new SeatLayout(
                "白飞龙_test" + new Random().nextInt(10000),
                layoutType[new Random().nextInt(layoutType.length)],
                6, 3, 15, seatList);
        this.seatList.forEach(seat -> seat.setSeatLayout(checkCap));
        try {
            iSeatLayoutBiz.addSeatLayout(checkCap);
            Assert.assertFalse(true);
        } catch (Exception e) {
            System.out.println("checkCap: " + e.getMessage());
            Assert.assertEquals(TravelTransportBizError.BUS_SEAT_LATOUT_CAPACITY_ERROR.getErrorMessage(), e.getMessage());
        }
    }

    /**
     * 验证重名
     * Method: addSeatLayout(SeatLayout seatLayout)
     */
    @Test(priority = 3)
    public void testName(){
        SeatLayout checkName = new SeatLayout(
                seatLayout.get().getName(),
                layoutType[new Random().nextInt(layoutType.length)],
                6, 3, 12, seatList);
        this.seatList.forEach(seat -> seat.setSeatLayout(checkName));
        try {
            iSeatLayoutBiz.addSeatLayout(checkName);
            Assert.assertFalse(true);
        } catch (Exception e) {
            System.out.println("checkName: " + e.getMessage());
            Assert.assertEquals(TravelTransportBizError.BUS_SEAT_LAYOUT_NAME_EXISTED.getErrorMessage(), e.getMessage());
        }
    }


    /**
     * Method: findLayoutById(Long id)
     */
    @Test(priority = 4)
    public void testFindLayoutById() throws Exception {
        Optional<SeatLayout> sLayout = iSeatLayoutBiz.findLayoutById(seatLayoutId);
        Assert.assertEquals(seatLayout.get().getId(), sLayout.isPresent() ? sLayout.get().getId() : null);
    }

    /**
     * Method: updateSeatLayout(SeatLayout seatLayout)
     */
    @Test(priority = 5)
    public void testUpdateSeatLayout() throws Exception {
        SeatLayout layout = new SeatLayout(
                seatLayoutId,
                "update_test" + new Random().nextInt(10000),
                layoutType[new Random().nextInt(layoutType.length)],
                6, 3, 12, seatLayout.get().getSeatList());
        Optional<SeatLayout> result = iSeatLayoutBiz.updateSeatLayout(layout);
        Assert.assertTrue(result.isPresent());
    }

    /**
     * Method: findLayOutPagesByConditions(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy sortBy, Integer pageIndex, Integer pageSize, boolean byLike)
     */
    @Test(priority = 6)
    public void testFindLayOutPagesByConditions() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("name", seatLayout.get().getName());
        DomainPage<SeatLayout> res = iSeatLayoutBiz.findLayOutPagesByConditions(params, "name", IBaseDao.SortBy.ASC, 1, 10, false);
        Assert.assertNotNull(res);
        Assert.assertTrue(res.getDomains().size() > 0);
    }

    /**
     * Method: deleteSeatLayout(SeatLayout seatLayout)
     */
    @Test(priority = 7)
    public void testDeleteSeatLayout() throws Exception {
        Boolean result = iSeatLayoutBiz.deleteSeatLayout(seatLayout.get());
        Assert.assertTrue(result);

    }
}
