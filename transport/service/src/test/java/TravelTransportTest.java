import com.simpletour.biz.traveltrans.bo.BusNoBo;
import com.simpletour.biz.traveltrans.bo.BusNoPlanBo;
import com.simpletour.biz.traveltrans.bo.DomainPageBo;
import com.simpletour.biz.traveltrans.error.TravelTransportBizError;
import com.simpletour.common.security.token.ThreadLocalToken;
import com.simpletour.common.security.token.Token;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.commons.test.TestClassWithLazyLoad;
import com.simpletour.domain.resources.Area;
import com.simpletour.domain.resources.Destination;
import com.simpletour.domain.traveltrans.*;
import com.simpletour.service.traveltrans.ITravelTransportService;
import com.simpletour.service.traveltrans.error.TravelTransportServiceError;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.*;

import static com.simpletour.commons.test.Utils.generateName;

/**
 * Created by Mario on 2015/11/20.
 */
@ContextConfiguration({"classpath:applicationContext.xml"})
@Transactional
public class TravelTransportTest extends TestClassWithLazyLoad {

    @Resource
    private ITravelTransportService iTravelTransportService;

    private Long seatLayoutId;

    private Long nodeTypeId;

    private Assistant assistant;

    private Optional<SeatLayout> seatLayout;

    private Optional<NodeType> nodeType;

    private Optional<BusNo> busNo;

    private Optional<Line> line;

    private Optional<BusNoPlan> busNoPlan;

    private Destination destination;

    private Area area;

    private List<Node> nodeList;

    private NodeType firstNodeType;

    private NodeType secondNodeType;

    private NodeType thirdNodeType;

    private Destination firstNodeDestination;

    private Destination secondNodeDestination;

    private Destination thirdNodeDestination;

    private Optional<BusNo> savedBusNo;

    private Optional<BusNo> findBusNo;

    /**
     * 座位
     */
    private List<Seat> seatList;

    /**
     * 座位布局类型
     */
    private SeatLayout.Type[] layoutType;

    private Long busId;

    private Optional<Bus> bus;

    /**
     * 初始化测试数据
     * @throws Exception
     */
    @BeforeClass(alwaysRun = true)
    public void beforeClass() throws Exception {
        super.beforeClass();
        layoutType = SeatLayout.Type.class.getEnumConstants();
        List<Seat> seatList = new ArrayList<>();
        Arrays.asList(1, 2, 3, 4, 5, 6).forEach(index -> {
            seatList.add(new Seat(Seat.Type.SEAT, index, "A"));
            seatList.add(new Seat(Seat.Type.EMPTY, index, "B"));
            seatList.add(new Seat(Seat.Type.SEAT, index, "C"));
        });
        this.seatList = seatList;

        ThreadLocalToken.setToken(new Token("1", "1", "1", "1", "1", "1", Token.ClientType.BROWSER) {
            @Override
            public String toCipherString() {
                return null;
            }
        });
    }

    /**
     * 清除测试数据
     */
    @AfterClass(alwaysRun = true)
    public void afterClass() {
        super.afterClass();
        iTravelTransportService.deleteBus(bus.get());
        iTravelTransportService.deleteSeatLayout(seatLayout.get());
    }

    /**
     * ===================================SEAT_LAYOUT====================================
     **/
    /**
     * 基本保存逻辑验证
     * Method: addSeatLayout(SeatLayout seatLayout)
     */
    @Test(priority = 1)
    public void addSeatLayout() {
        SeatLayout checkBase = new SeatLayout(
                "白飞龙_test" + new Random().nextInt(10000),
                layoutType[new Random().nextInt(layoutType.length)],
                6, 3, 12, seatList);
        seatList.forEach(seat -> seat.setSeatLayout(checkBase));
        seatLayout = iTravelTransportService.addSeatLayout(checkBase);
        if (seatLayout.isPresent()) {
            seatLayoutId = seatLayout.get().getId();
        }
        Assert.assertTrue(seatLayout.isPresent());
        System.out.println("checkBase: " + seatLayout.isPresent());
    }

    /**
     * 空对象测试
     * Method: addSeatLayout(SeatLayout seatLayout)
     */
    @Test(priority = 2)
    public void addSeatLayoutEmpty() throws Exception {
        try {
            iTravelTransportService.addSeatLayout(null);
            Assert.assertFalse(true);
        } catch (Exception e) {
            Assert.assertEquals(TravelTransportServiceError.BUS_SEAT_LAYOUT_NULL.getErrorMessage(), e.getMessage());
        }

    }

    /**
     * Method: findLayoutById(Long id)
     */
    @Test(priority = 3)
    public void findLayoutById() {
        seatLayout = iTravelTransportService.findLayoutById(seatLayoutId);
        Assert.assertTrue(seatLayout.isPresent());
    }

    /**
     * Method: updateSeatLayout(SeatLayout seatLayout)
     */
    @Test(priority = 4)
    public void updateSeatLayout() {
        SeatLayout layout = new SeatLayout(
                seatLayoutId,
                "update_test" + new Random().nextInt(10000),
                layoutType[new Random().nextInt(layoutType.length)],
                6, 3, 12, seatLayout.get().getSeatList());
        Optional<SeatLayout> result = iTravelTransportService.updateSeatLayout(layout);
        Assert.assertTrue(result.isPresent());
    }

    /**
     * Method: findLayOutPagesByConditions(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, Integer pageIndex, Integer pageSize, boolean byLike)
     */
    @Test(priority = 5)
    public void findLayOutPagesByConditions() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", seatLayout.get().getName());
        DomainPage<SeatLayout> res = iTravelTransportService.findLayOutPagesByConditions(params, "name", IBaseDao.SortBy.ASC, 1, 10, false);
        Assert.assertNotNull(res);
    }

    /**
     * ===================================BUS====================================
     **/
    /**
     * 基本保存逻辑测试
     * Method: addBus(Bus bus)
     */
    @Test(priority = 21)
    public void testAddBus() throws Exception {
        Bus tmpOneBus = new Bus(seatLayout.get(), "白飞龙_test", "川W00000", true);
        bus = iTravelTransportService.addBus(tmpOneBus);
        Assert.assertTrue(bus.isPresent());
        busId = bus.get().getId();
    }

    /**
     * 空对象测试
     * Method: addBus(Bus bus)
     */
    @Test(priority = 22)
    public void testAddBusEmpty() throws Exception {
        try {
            iTravelTransportService.addBus(null);
            Assert.assertFalse(true);
        } catch (Exception e) {
            Assert.assertEquals(TravelTransportServiceError.BUS_NULL.getErrorMessage(), e.getMessage());
        }

    }

    /**
     * 车牌号非空测试
     * Method: addBus(Bus bus)
     */
    @Test(priority = 23)
    public void testLicenseEmpty() throws Exception {
        Bus tmpBus = new Bus(seatLayout.get(), "白飞龙_test_one", "", true);
        try {
            bus = iTravelTransportService.addBus(tmpBus);
            Assert.assertFalse(true);
        } catch (Exception e) {
            Assert.assertEquals(TravelTransportServiceError.BUS_NULL.getErrorMessage(), e.getMessage());
        }

    }

    /**
     * 座位布局不可用测试
     * Method: addBus(Bus bus)
     */
    @Test(priority = 24)
    public void testAddSeatLayoutDisabled() throws Exception {
        Bus tmpBus = new Bus(null, "白飞龙_test", "川Z11111", true);
        try {
            iTravelTransportService.addBus(tmpBus);
            Assert.assertFalse(true);
        } catch (Exception e) {
            Assert.assertEquals(TravelTransportServiceError.BUS_SEAT_LAYOUT_NOT_AVAILABLE.getErrorMessage(), e.getMessage());
        }

    }

    /**
     * Method: findBusById(Long id)
     */
    @Test(priority = 25)
    public void testFindBusById() throws Exception {
        bus = iTravelTransportService.findBusById(busId);
        Assert.assertTrue(bus.isPresent());
        busId = bus.get().getId();
    }

    /**
     * 正常更新逻辑测试
     * Method: updateBus(Bus bus)
     */
    @Test(priority = 26)
    public void testUpdateBus() throws Exception {
        Bus tmpBus = bus.get();
        tmpBus.setLicense("川W1111W");
        bus = iTravelTransportService.updateBus(tmpBus);
        Assert.assertEquals(bus.get().getLicense(), "川W1111W");
    }

    /**
     * 非空更新测试
     * Method: updateBus(Bus bus)
     */
    @Test(priority = 27)
    public void testUpdateBusEmpty() throws Exception {
        try {
            bus = iTravelTransportService.updateBus(new Bus());
            Assert.assertFalse(true);
        } catch (Exception e) {
            Assert.assertEquals(TravelTransportServiceError.BUS_NULL.getErrorMessage(), e.getMessage());
        }
    }

    /**
     * 座位布局不可用更新测试
     * Method: updateBus(Bus bus)
     */
    @Test(priority = 28)
    public void testUpdateSLayoutDisabled() throws Exception {
        Bus tmpBus = new Bus(bus.get());
        tmpBus.setLayout(null);
        try {
            bus = iTravelTransportService.updateBus(tmpBus);
            Assert.assertFalse(true);
        } catch (Exception e) {
            Assert.assertEquals(TravelTransportServiceError.BUS_SEAT_LAYOUT_NOT_AVAILABLE.getErrorMessage(), e.getMessage());
        }
    }

    /**
     * Method: findBusPageByConditions(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, Integer pageIndex, Integer pageSize, boolean byLike)
     */
    @Test(priority = 29)
    public void testFindBusPageByConditions() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("model", "白飞龙");
        DomainPage<Bus> results = iTravelTransportService.findBusPageByConditions(params, "model", IBaseDao.SortBy.ASC, 1, 10, true);
        Assert.assertNotNull(results);
        Assert.assertTrue(results.getDomains().size() > 0);
    }

    /**
     * Method: findBusByConditions(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy)
     */
    @Test(priority = 30)
    public void testFindBusByConditions() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("model", "白飞龙_test");
        List<Bus> results = iTravelTransportService.findBusByConditions(params, "model", IBaseDao.SortBy.ASC);
        Assert.assertNotNull(results);
        Assert.assertTrue(results.size() > 0);
    }

    /**
     * Method: findAvailableBus(String license, Long busNoId, Date day, int pageIndex, int pageSize)
     */
    @Test(priority = 31)
    public void testFindAvailableBus() throws Exception {
        try {
            iTravelTransportService.findAvailableBus(bus.get().getLicense(), -1L, new Date(), 1, 10);
            Assert.assertFalse(true);
        } catch (Exception e) {
            Assert.assertEquals(TravelTransportBizError.BUS_NO_SEARCH_EXCEPTION.getErrorMessage(), e.getMessage());
        }
    }

    /**
     * ===================================NODE====================================
     **/
    @Test(priority = 41, enabled = false)
    public void addArea() {
        Area area = new Area();
        area.setName("中国");
//        this.area = iResourcesDao.save(area);
    }

    @Test(priority = 42, enabled = false, dependsOnMethods = "addArea")
    public void addDestination() {
        Destination destination = new Destination();
        destination.setTenantId(1L);
        destination.setName("目的地");
        destination.setAddress("详细地址");
        destination.setArea(this.area);
//        this.destination = iResourcesDao.save(destination);
    }

    /**
     * ===================================NODE_TYPE====================================
     **/
    @Test(priority = 43, enabled = false)
    public void addNodeType() {
        NodeType nodeType = new NodeType("经停点", "/img", null, true);
//        this.nodeTypeId = iTransportDao.save(nodeType).getId();
    }

    //id为null
    @Test
    @Rollback(value = false)
    public void findNodeTypeByIdIsNull(){
        try {
            Optional<NodeType> nodeType = iTravelTransportService.findNodeTypeById(null);
        } catch (BaseSystemException e){
            Assert.assertEquals(e.getMessage(), TravelTransportServiceError.BUS_NODE_TYPE_NULL.getErrorMessage());
        }
    }

    @Test(priority = 44, dependsOnMethods = "addNodeType")
    public void findNodeTypeById() {
        this.nodeType = iTravelTransportService.findNodeTypeById(nodeTypeId);
        Assert.assertTrue(this.nodeType.isPresent());
    }

    @Test(priority = 45, dependsOnMethods = {"findNodeTypeById"})
    public void findNodeTypePageByConditions() {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("name", this.nodeType.get().getName());
        DomainPage<NodeType> domainPage = iTravelTransportService.findNodeTypePageByConditions(conditions, "id", IBaseDao.SortBy.ASC, 1, 10, false);
        Assert.assertTrue(domainPage.getDomains().size() > 0);
    }

    @Test(priority = 46, dependsOnMethods = "findNodeTypeById")
    public void findNodeTypeByConditions() {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("name", this.nodeType.get().getName());
        List<NodeType> list = iTravelTransportService.findNodeTypeByConditions(conditions, "id", IBaseDao.SortBy.ASC);
        Assert.assertTrue(list.size() > 0);
    }

    /**
     * ===================================BUS_NO====================================
     **/
    /**
     * 数据的准备 添加nodeType和destination各两个数据
     */
    @Test(enabled = false)
    @Rollback(value = false)
    public void addNodeTypeForNode(){
        try {
//            this.firstNodeType = iTransportDao.save(new NodeType("出发点", null, null, true));
//            this.secondNodeType = iTransportDao.save(new NodeType("经停点", null, null, true));
//            this.thirdNodeType = iTransportDao.save(new NodeType("目的点", null, null, true));
        } catch (BaseSystemException e){
            System.out.println(e.getMessage());
        }
    }

    @Test(enabled = false)
    @Rollback(value = false)
    public void addDestinationForNode(){
        try {
//            this.firstNodeDestination = iTransportDao.save(new Destination("花水湾", "花水湾", "出发了", iTransportDao.getEntityById(Area.class, 510129L), BigDecimal.valueOf(103.27),BigDecimal.valueOf(30.57)));
//            this.secondNodeDestination = iTransportDao.save(new Destination("宽窄巷子", "青羊区长顺上街", "很好玩", iTransportDao.getEntityById(Area.class, 510105L), BigDecimal.valueOf(0.00),BigDecimal.valueOf(0.00)));
//            this.thirdNodeDestination = iTransportDao.save(new Destination("成都终点站", "软件园c区", "到站了", iTransportDao.getEntityById(Area.class, 510105L), BigDecimal.valueOf(0.00),BigDecimal.valueOf(0.00)));
        } catch (BaseSystemException e){
            System.out.println(e.getMessage());
        }
    }
    //添加busNo时，busNoBo为null
    @Test
    @Rollback(value = false)
    public void addBusNoBusNOBoIsNull(){
        try {
            Optional<BusNo> busNo = iTravelTransportService.addBusNo(null);
        } catch (BaseSystemException e){
            Assert.assertEquals(e.getMessage(), TravelTransportServiceError.BUS_NO_NULL.getErrorMessage());
        }
    }

    //添加车次时busNo包含的节点，其中某个节点的节点类型nodeType不存在，目的地存在
    @Test(dependsOnMethods = {"addNodeTypeForNode","addDestinationForNode"})
    @Rollback(value = false)
    public void addBusNoWithoutNodeType(){
        try {
            ArrayList<Node> nodes = new ArrayList<>();
            BusNo busNo = new BusNo("HSW000CD",96,8100,71100,79200,"花水湾","成都",false, BusNo.Status.normal,false,nodes);
            BusNo busNoWithNodes = setAllNodesWithoutNodeType(busNo);
            Optional<BusNo> busNoOptional = iTravelTransportService.addBusNo(new BusNoBo(busNoWithNodes));
        } catch (BaseSystemException e){
            //Assert.assertEquals(e.getMessage(), TravelTransportServiceError. BUS_NODE_TYPE_NULL.getErrorMessage());
             Assert.assertEquals(e.getMessage(), TravelTransportServiceError.BUS_NODE_TYPE_NOT_EXISTED.getErrorMessage());
        }
    }

    //添加车次时busNo包含的节点，其中某个节点的节点类型nodeType存在，目的地不存在
    @Test(dependsOnMethods = {"addNodeTypeForNode","addDestinationForNode"})
    @Rollback(value = false)
    public void addBusNoWithoutDestination(){
        try {
            ArrayList<Node> nodes = new ArrayList<>();
            BusNo busNo = new BusNo("HSW520CD",96,8100,71100,79200,"花水湾","成都",false, BusNo.Status.normal,false,nodes);
            BusNo busNoWithNodes = setAllNodesWithoutDestination(busNo);
            Optional<BusNo> busNoOptional = iTravelTransportService.addBusNo(new BusNoBo(busNoWithNodes));
        } catch (BaseSystemException e){
            //Assert.assertEquals(e.getMessage(), TravelTransportServiceError. BUS_NODE_DESTINATION_ID_NULL.getErrorMessage());
             Assert.assertEquals(e.getMessage(), TravelTransportServiceError.BUS_NODE_DESTINATION_NOT_EXISTED.getErrorMessage());
        }
    }

    //正常添加
    @Test(dependsOnMethods = {"addNodeTypeForNode","addDestinationForNode"})
    @Rollback(value = false)
    public void addBusNoNormal(){
        try {
            ArrayList<Node> nodes = new ArrayList<>();
            BusNo busNo = new BusNo("HSW521CD",96,8100,71100,79200,"花水湾正常","成都正常",false, BusNo.Status.normal,false,nodes);
            busNo.setTenantId(1L);
            BusNo busNoWithNodes = setAllNodes(busNo);
            this.savedBusNo = iTravelTransportService.addBusNo(new BusNoBo(busNoWithNodes));
        } catch (BaseSystemException e){
            Assert.assertEquals(e.getMessage(), TravelTransportBizError. BUS_NODE_DEPARTTIME_LATE.getErrorMessage());
        }
    }

    //删除busNo时,busNo为null或者busNo的id为null
    @Test
    @Rollback(value = false)
    public void doDeleteBusNo(){
        try {
            BusNo busNo = new BusNo();
            busNo.setId(null);
            // boolean b = travelTransportService.deleteBusNo(null);
            boolean b = iTravelTransportService.deleteBusNo(busNo);
        } catch (BaseSystemException e){
            Assert.assertEquals(e.getMessage(), TravelTransportServiceError.BUS_NO_NULL.getErrorMessage());
        }
    }

    //更新busNo时,busNo为null或者busNo的id为null
    @Test
    @Rollback(value = false)
    public void updateBusNoIsNull(){
        try {
            BusNoBo busNoBo = new BusNoBo();
            busNoBo.setId(null);
            // boolean b = travelTransportService.deleteBusNo(null);
            Optional<BusNo> busNo = iTravelTransportService.updateBusNo(busNoBo);
        } catch (BaseSystemException e){
            Assert.assertEquals(e.getMessage(), TravelTransportServiceError.BUS_NO_NULL.getErrorMessage());
        }
    }

    //更新车次时，某个节点没有nodeType  注意：测试时需要把nodes集合改为及时加载
    @Test(dependsOnMethods = {"findBusNoById"})
    @Rollback(value = false)
    public void updateBusNoWithoutNodeType(){
        try {
            this.findBusNo.get().getNodes().clear();
            BusNo busNo = setAllNodesWithoutNodeType(this.findBusNo.get());
            iTravelTransportService.updateBusNo(new BusNoBo(busNo));
        } catch (BaseSystemException e){
            Assert.assertEquals(e.getMessage(), TravelTransportServiceError. BUS_NODE_TYPE_NOT_EXISTED.getErrorMessage());
        }
    }

    //编辑车次时busNo包含的节点，其中某个节点的节点类型nodeType存在，目的地不存在
    @Test(dependsOnMethods = {"findBusNoById"})
    @Rollback(value = false)
    public void updateBusNoWithoutDestination(){
        try {
            this.findBusNo.get().getNodes().clear();
            BusNo busNo = setAllNodesWithoutDestination(this.findBusNo.get());
            iTravelTransportService.updateBusNo(new BusNoBo(busNo));
        } catch (BaseSystemException e){
            Assert.assertEquals(e.getMessage(),  TravelTransportServiceError.BUS_NODE_DESTINATION_NOT_EXISTED.getErrorMessage());
        }
    }

    //根据车次号查询车次,车次号no为为null或者长度为0
    @Test
    @Rollback(value = false)
    public void findBusNoWithNoIsNull(){
        try {
            //Optional<BusNo> busNoByNo = travelTransportService.findBusNoByNo(null, true);
            Optional<BusNo> busNoByNo = iTravelTransportService.findBusNoByNo("", true);
        } catch (BaseSystemException e){
            Assert.assertEquals(e.getMessage(), TravelTransportServiceError.BUS_NO_NO_FOR_NULL.getErrorMessage());
        }
    }
    //根据id查询车次,id为null
    @Test
    @Rollback(value = false)
    public void findBusNoWithIdIsNull(){
        try {
            Optional<BusNo> busNoById = iTravelTransportService.findBusNoById(null);
        } catch (BaseSystemException e){
            Assert.assertEquals(e.getMessage(), TravelTransportServiceError.BUS_NO_NULL.getErrorMessage());
        }
    }

    //通过查询车次
    @Test(dependsOnMethods = {"addBusNoNormal"})
    @Rollback(value = false)
    public void findBusNoById(){
        try {
            // Optional<BusNo> busNo = busNoBiz.findBusNoById(null);
            this.findBusNo = iTravelTransportService.findBusNoById(this.savedBusNo.get().getId());
            Assert.assertTrue(this.findBusNo.isPresent());
        } catch (BaseSystemException e){
            //Assert.assertEquals(e.getMessage(),TravelTransportBizError.BUS_NO_NULL.getErrorMessage());
            System.out.println(e.getMessage());
        }
    }

    //给车次设置节点，节点数为2，其中某个节点的节点类型nodeType不存在，目的地存在
    private BusNo setAllNodesWithoutNodeType(BusNo busNo){

        NodeType nodeType = new NodeType();
        //nodeType.setId(null);//id为null
        nodeType.setId(222L); //节点类型nodeType的id不存在
        Node firstNode = new Node(busNo, nodeType, this.firstNodeDestination, null, 0, 71100, 0);

        Node secondNode = new Node(busNo, this.secondNodeType, this.secondNodeDestination, null, 0, 79200, 0);

        busNo.getNodes().add(firstNode);
        busNo.getNodes().add(secondNode);

        return busNo;
    }
    //给车次设置节点，节点数为2，其中某个节点的destination不存在，目的地存在
    private BusNo setAllNodesWithoutDestination(BusNo busNo){
        Node firstNode = new Node(busNo, this.firstNodeType, this.secondNodeDestination, null, 0, 71100, 0);

        Destination destination = new Destination();
        //destination.setId(null); //节点的目的地id为null
        destination.setId(222L); //目的地不存在
        Node secondNode = new Node(busNo, this.secondNodeType, destination, null, 0, 79200, 0);

        busNo.getNodes().add(firstNode);
        busNo.getNodes().add(secondNode);

        return busNo;
    }

    //给车次设置节点，节点数为3，并且节点相关的nodeType和destination存在
    private BusNo setAllNodes(BusNo busNo){
        Node firstNode = new Node(busNo, this.firstNodeType, this.firstNodeDestination, null, 0, 71100, 0);
        Node secondNode = new Node(busNo, this.secondNodeType, this.secondNodeDestination, null, 0, 79200, 0);
        Node thirdNode = new Node(busNo, this.thirdNodeType, this.thirdNodeDestination, null, 0, 81200, 0);

        busNo.getNodes().add(firstNode);
        busNo.getNodes().add(secondNode);
        busNo.getNodes().add(thirdNode);

        return busNo;
    }

    /**
     * ===================================BUS_NO_PLAN====================================
     **/
    @Test(priority = 81, enabled = false)
    public void addAssistant() {
        Assistant assistant = new Assistant();
        assistant.setName("牛逼的包车大姐");
//        this.assistant = iTransportDao.save(assistant);
    }

    @Test(priority = 82, dependsOnMethods = {"addAssistant", "findBusNoById"})
    public void findAvailableAssistant() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2015);
        calendar.set(Calendar.MONTH, 11);
        calendar.set(Calendar.DATE, 29);
        DomainPage<Assistant> domainPage = iTravelTransportService.findAvailableAssistant(this.assistant.getName(), this.busNo.get().getId(), calendar.getTime(), 1, 10);
        Assert.assertTrue(domainPage.getDomains().size() >= 0);
    }

    @Test(priority = 83, enabled = false, dependsOnMethods = {"findBusNoById", "testFindBusById", "addAssistant"})
    public void addBusNoPlan() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2015);
        calendar.set(Calendar.MONTH, 11);
        calendar.set(Calendar.DATE, 29);

        BusNoPlan busNoPlan = new BusNoPlan();
        busNoPlan.setNo(this.busNo.get());
        busNoPlan.setDay(calendar.getTime());
        busNoPlan.setAssistant(this.assistant);
        busNoPlan.setBus(this.bus.get());
        busNoPlan.setCapacity(37);
        this.busNoPlan = iTravelTransportService.addBusNoPlan(busNoPlan);
        Assert.assertTrue(this.busNoPlan.isPresent());
    }

    @Test(priority = 84, dependsOnMethods = "addBusNoPlan")
    public void findBusNoPlanById() {
        Optional<BusNoPlanBo> res = iTravelTransportService.findBusNoPlanById(this.busNoPlan.get().getId());
        Assert.assertTrue(res.isPresent());
    }

    @Test(priority = 85, dependsOnMethods = {"findBusNoPlanById"})
    public void updateBusNoPlan() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2015);
        calendar.set(Calendar.MONTH, 11);
        calendar.set(Calendar.DATE, 29);
        this.busNoPlan.get().setDay(calendar.getTime());
        this.busNoPlan = iTravelTransportService.updateBusNoPlan(this.busNoPlan.get());
        Assert.assertTrue(this.busNoPlan.isPresent());
    }

    @Test(priority = 86, dependsOnMethods = "findBusNoPlanById")
    public void findBusNoPlanByConditions() {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("assistant.name", this.busNoPlan.get().getAssistant().getName());
        List<BusNoPlanBo> busNoPlanList = iTravelTransportService.findBusNoPlanByConditions(conditions, "id", IBaseDao.SortBy.ASC);
        Assert.assertTrue(busNoPlanList.size() > 0);
    }

    @Test(priority = 87, dependsOnMethods = "findBusNoPlanById")
    public void findBusNoPlanPagesByConditions() {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("assistant.name", this.busNoPlan.get().getAssistant().getName());
        DomainPageBo<BusNoPlanBo> busNoPlanBoDomainPageBo = iTravelTransportService.findBusNoPlanPagesByConditions(conditions, "id", IBaseDao.SortBy.ASC, 1, 10, false);
        Assert.assertTrue(busNoPlanBoDomainPageBo.getDomains().size() > 0);
    }

    @Test(priority = 88, dependsOnMethods = "findBusNoPlanById")
    public void findBusNoPlanByConditionsAndDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2015);
        calendar.set(Calendar.MONTH, 11);
        calendar.set(Calendar.DATE, 26);
        Date start = calendar.getTime();
        calendar.clear();
        calendar.set(Calendar.YEAR, 2015);
        calendar.set(Calendar.MONTH, 11);
        calendar.set(Calendar.DATE, 30);
        Date end = calendar.getTime();

        Map<String, Object> conditions = new HashMap<>();
        conditions.put("assistant.name", this.busNoPlan.get().getAssistant().getName());
        List<BusNoPlanBo> busNoPlanBos = iTravelTransportService.findBusNoPlanByConditionsAndDate(conditions, start, end, "id", IBaseDao.SortBy.ASC);
        Assert.assertTrue(busNoPlanBos.size() > 0);
    }

    @Test(priority = 89, dependsOnMethods = "findBusNoPlanById")
    public void findAvailableBusSeat() {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("assistant.name", this.busNoPlan.get().getAssistant().getName());
        int availableSeat = iTravelTransportService.findAvailableBusSeat(conditions);
        Assert.assertTrue(availableSeat > 0);
    }

    /**
     * ===================================LINE====================================
     **/
    @Test(priority = 101, dependsOnMethods = "findBusNoById")
    public void addLine() {
        Line line = new Line();
        line.setName("九黄线" + generateName());

        BusNoSerial busNoSerial = new BusNoSerial(line, this.busNo.get(), 1, 1);
        line.getBusNoSeries().add(busNoSerial);

        BusNoSerial busNoSerial1 = new BusNoSerial(line, this.busNo.get(), 2, 2);
        line.getBusNoSeries().add(busNoSerial1);

        this.line = iTravelTransportService.addLine(line);
        Assert.assertTrue(this.line.isPresent());
    }

    @Test(priority = 102, dependsOnMethods = "addLine")
    public void findLineById() {
        this.line = iTravelTransportService.findLineById(this.line.get().getId());
        Assert.assertTrue(this.line.isPresent());
    }

    @Test(priority = 103, dependsOnMethods = "findLineById")
    public void updateLine() {
        Line line = new Line();
        line.setId(this.line.get().getId());
        line.setName("牛卧槽" + generateName());
        line.setVersion(this.line.get().getVersion());
        this.line = iTravelTransportService.updateLine(line);
    }

    @Test(priority = 104, dependsOnMethods = "findLineById")
    public void findLineByConditions() {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("name", this.line.get().getName());
        List<Line> list = iTravelTransportService.findLineByConditions(conditions, "id", IBaseDao.SortBy.ASC);
        Assert.assertTrue(list.size() > 0);
    }

    @Test(priority = 105, dependsOnMethods = "findLineById")
    public void findLinePagesByConditions() {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("name", this.line.get().getName());
        DomainPage<Line> domainPage = iTravelTransportService.findLinePagesByConditions(conditions, "id", IBaseDao.SortBy.ASC, 1, 10, false);
        Assert.assertTrue(domainPage.getDomains().size() > 0);
    }

    /**
     * ===================================NODE_STATUS====================================
     **/
    @Test(priority = 121, dependsOnMethods = {"testFindBusById", "findBusNoById"})
    public void addNodeStatus() {
        NodeStatus nodeStatus = new NodeStatus(this.busNo.get(), this.bus.get(), new Date(), this.nodeList.get(0), 1234, NodeStatus.Status.arrived);
        Optional<NodeStatus> nodeStatusOptional = iTravelTransportService.addNodeStatus(nodeStatus);
        Assert.assertTrue(nodeStatusOptional.isPresent());
    }

    @Test(priority = 122, dependsOnMethods = "addNodeStatus")
    public void findNodeStatusPagesByConditions() {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("bus.id", this.bus.get().getId());
        DomainPage<NodeStatus> domainPage = iTravelTransportService.findNodeStatusPagesByConditions(conditions, "id", IBaseDao.SortBy.ASC, 1, 10, false);
        Assert.assertTrue(domainPage.getDomains().size() > 0);
    }

    @Test(priority = 123, dependsOnMethods = "addNodeStatus")
    public void findNodeStatusByConditions() {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("bus.id", this.bus.get().getId());
        List<NodeStatus> list = iTravelTransportService.findNodeStatusByConditions(conditions, "id", IBaseDao.SortBy.ASC);
        Assert.assertTrue(list.size() > 0);
    }

    @Test(priority = 124, dependsOnMethods = {"findLineById"})
    public void findAvailableBusNo() {
        DomainPage<BusNo> domainPage = iTravelTransportService.findAvailableBusNo(null, 1, 10);
        Assert.assertTrue(domainPage.getDomains().size() >= 0);
    }
}
