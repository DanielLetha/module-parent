import com.simpletour.biz.traveltrans.IBusNoBiz;
import com.simpletour.biz.traveltrans.bo.BusNoBo;
import com.simpletour.biz.traveltrans.error.TravelTransportBizError;
import com.simpletour.common.security.token.EncryptedToken;
import com.simpletour.common.security.token.Token;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.traveltrans.ITransportDao;
import com.simpletour.domain.traveltrans.BusNo;
import com.simpletour.domain.traveltrans.Node;
import com.simpletour.domain.traveltrans.NodeType;
import org.hibernate.LazyInitializationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import resources.Area;
import resources.Destination;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Author:  WangLin
 * Mail  :  wl@simpletour.com
 * Date  :  2016/3/22 15:17
 * since :  2.0.0-SNAPSHOT
 * Remark:  车次的单元测试  注意：测试更新操作时需要把nodes集合改为及时加载
 *
 */
@ContextConfiguration({"classpath:applicationContext.xml"})
@Transactional
public class BusNoBizTest extends AbstractTestNGSpringContextTests {

    @Resource
    private IBusNoBiz busNoBiz;

    @Resource
    private ITransportDao transportDao;

    private NodeType firstNodeType;

    private NodeType secondNodeType;

    private NodeType thirdNodeType;

    private Destination firstNodeDestination;

    private Destination secondNodeDestination;

    private Destination thirdNodeDestination;

    private Long savedId;

    private Optional<BusNo> savedBusNo;
    /**
     * 构造token信息
     */
    @BeforeClass(alwaysRun = true)
    public void cerateTokenAndData(){
        new EncryptedToken("0","0","1","2", Token.ClientType.BROWSER);
        try {
            this.firstNodeType = transportDao.save(new NodeType("出发点", null, null, true));
            this.secondNodeType = transportDao.save(new NodeType("经停点", null, null, true));
            this.thirdNodeType = transportDao.save(new NodeType("目的点", null, null, true));
            this.firstNodeDestination = transportDao.save(new Destination("花水湾", "花水湾", "出发了", transportDao.getEntityById(Area.class, 510129L), BigDecimal.valueOf(103.27),BigDecimal.valueOf(30.57)));
            this.secondNodeDestination = transportDao.save(new Destination("宽窄巷子", "青羊区长顺上街", "很好玩", transportDao.getEntityById(Area.class, 510105L), BigDecimal.valueOf(0.00),BigDecimal.valueOf(0.00)));
            this.thirdNodeDestination = transportDao.save(new Destination("成都终点站", "软件园c区", "到站了", transportDao.getEntityById(Area.class, 510105L), BigDecimal.valueOf(0.00),BigDecimal.valueOf(0.00)));
        } catch (BaseSystemException e){
            Assert.fail();
        }
    }

    //添加车次时busNo为null
    @Test(priority = 1)
    public void addBusNoWithBusNoIsNull(){
        try {
            Optional<BusNo> busNo = busNoBiz.addBusNo(null);
            Assert.fail();
        } catch (BaseSystemException e){
            Assert.assertEquals(e.getMessage(), TravelTransportBizError. BUS_NO_NULL_FOR_BUS_NO_AND_NODE.getErrorMessage());
        }

    }
    //添加车次时busNo中包含的节点为null
    @Test(priority = 2)
    public void addBusNoWithNodesIsNull(){
        try {
            BusNo busNo = new BusNo("HSW119CD",96,8100,71100,79200,"花水湾","成都",false, BusNo.Status.normal,false,null);
            Optional<BusNo> busNoOptional = busNoBiz.addBusNo(new BusNoBo(busNo));
            Assert.fail();
        } catch (BaseSystemException e){
            Assert.assertEquals(e.getMessage(), TravelTransportBizError. BUS_NO_NULL_FOR_BUS_NO_AND_NODE.getErrorMessage());
        }
    }

    //添加车次时busNo中包含的节点为empty
    @Test(priority = 3)
    public void addBusNoWithNodesIsEmpty(){
        try {
            ArrayList<Node> nodes = new ArrayList<>();
            BusNo busNo = new BusNo("HSW119CD",96,8100,71100,79200,"花水湾","成都",false, BusNo.Status.normal,false,nodes);
            Optional<BusNo> busNoOptional = busNoBiz.addBusNo(new BusNoBo(busNo));
            Assert.fail();
        } catch (BaseSystemException e){
            Assert.assertEquals(e.getMessage(), TravelTransportBizError. BUS_NO_NULL_FOR_BUS_NO_AND_NODE.getErrorMessage());
        }
    }

    //添加车次时busNo包含的节点数小于2
    @Test(priority = 4)
    public void addBusNoWithNodesSizeLT2(){
        try {
            ArrayList<Node> nodes = new ArrayList<>();
            BusNo busNo = new BusNo("HSW520CD",96,8100,71100,79200,"花水湾","成都",false, BusNo.Status.normal,false,nodes);
            BusNo busNoWithNodes = setOneNodes(busNo);
            Optional<BusNo> busNoOptional = busNoBiz.addBusNo(new BusNoBo(busNoWithNodes));
            Assert.fail();
        } catch (BaseSystemException e){
            Assert.assertEquals(e.getMessage(), TravelTransportBizError. BUS_NODE_LIST_LENGTH_FALSE.getErrorMessage());
        }
    }

    //添加车次时busNo包含的节点，并且节点相关的nodeType和destination存在,但是其中一个节点的arriveTime大于departTime
    @Test(priority = 5)
    public void addBusNoWithoutNodesArriveTimeGTDepartTime(){
        try {
            ArrayList<Node> nodes = new ArrayList<>();
            BusNo busNo = new BusNo("HSW520CD",96,8100,71100,79200,"花水湾","成都",false, BusNo.Status.normal,false,nodes);
            BusNo busNoWithNodes = setNodesArriveTimeGTDepartTime(busNo);
            Optional<BusNo> busNoOptional = busNoBiz.addBusNo(new BusNoBo(busNoWithNodes));
            Assert.fail();
        } catch (BaseSystemException e){
            Assert.assertEquals(e.getMessage(), TravelTransportBizError. BUS_NODE_DEPARTTIME_LATE.getErrorMessage());
        }
    }
    //正常添加
    @Test(priority = 6)
    public void addBusNoNormal(){
        try {
            ArrayList<Node> nodes = new ArrayList<>();
            BusNo busNo = new BusNo("HSW521CD",96,8100,71100,79200,"花水湾正常","成都正常",false, BusNo.Status.normal,false,nodes);
            busNo.setTenantId(1L);
            BusNo busNoWithNodes = setAllNodes(busNo);
            this.savedBusNo = busNoBiz.addBusNo(new BusNoBo(busNoWithNodes));
            this.savedId = this.savedBusNo.get().getId();
            Assert.assertTrue(this.savedBusNo.isPresent());
        } catch (BaseSystemException e){
            Assert.fail();
        }
    }

    //添加车次时busNo的车次号no是否已经存在
    @Test(priority = 7)
    public void addBusNoWithNoIsExist(){
        try {
            ArrayList<Node> nodes = new ArrayList<>();
            BusNo busNo = new BusNo(this.savedBusNo.get().getNo(),96,8100,71100,79200,"花水湾","成都",false, BusNo.Status.normal,false,nodes);
            BusNo busNoWithNodes = setAllNodes(busNo);
            Optional<BusNo> busNoOptional = busNoBiz.addBusNo(new BusNoBo(busNoWithNodes));
            Assert.fail();
        } catch (BaseSystemException e){
            Assert.assertEquals(e.getMessage(), TravelTransportBizError. BUS_NO_NO_IS_EXISTED.getErrorMessage());
        }
    }

    //通过车次号查询车次,但是车次号为null 或者empty
    @Test(priority = 8)
    public void findBusNoByNoWithNoIsNull(){
        try {
           // busNoBiz.findBusNoByNo(null,true);
            busNoBiz.findBusNoByNo("",true);
            Assert.fail();
        } catch (BaseSystemException e){
            Assert.assertEquals(e.getMessage(), TravelTransportBizError.BUS_NO_NO_FOR_NULL.getErrorMessage());
        }
    }
    //通过车次号查询车次,并且查询出相关节点
    @Test(priority = 9)
    public void findBusNoByNoWithNodes(){
        try {
            Optional<BusNo> busNo = busNoBiz.findBusNoByNo(this.savedBusNo.get().getNo(), true);
            Assert.assertEquals(busNo.get().getNodes().size(),3);
        } catch (BaseSystemException e){
            Assert.fail();
        }
    }
    //通过车次号查询车次,不需要查询出相关节点
    @Test(priority = 10)
    public void findBusNoByNoWithoutNodes(){
        try {
            Optional<BusNo> busNo = busNoBiz.findBusNoByNo(this.savedBusNo.get().getNo(), false);
            Assert.assertEquals(busNo.get().getNodes().size(),0);
            Assert.fail();
        } catch (LazyInitializationException e){
            System.out.println(e.getMessage());
        }
    }

    //通过查询车次
    @Test(priority = 11)
    public void findBusNoById(){
        try {
           // Optional<BusNo> busNo = busNoBiz.findBusNoById(null);
            Optional<BusNo> busNoById = busNoBiz.findBusNoById(this.savedBusNo.get().getId());
            Assert.assertTrue(busNoById.isPresent());
        } catch (BaseSystemException e){
            Assert.fail();
        }
    }

    //更新车次，节点数小于2  注意：测试时需要把nodes集合改为及时加载
    @Test(priority = 12)
    public void updateBusNoWithNodesSizeLT2(){
        try {
            Optional<BusNo> busNoOptional = addBusNo();
            busNoOptional.get().getNodes().clear();
            BusNo busNo = setOneNodes(busNoOptional.get());
            busNoBiz.updateBusNo(new BusNoBo(busNo));
            Assert.fail();
        } catch (BaseSystemException e){
            Assert.assertEquals(e.getMessage(), TravelTransportBizError.BUS_NODE_LIST_LENGTH_FALSE.getErrorMessage());
        }
    }

    //编辑车次时busNo包含的节点,其中一个被删除，删除前节点数为3，删除后为2
    @Test(priority = 13)
    public void updateBusNoWithRemoveOneNode(){
        try {
            Optional<BusNo> busNoOptional = addBusNo();
            busNoOptional.get().getNodes().remove(0);
            Optional<BusNo> busNo = busNoBiz.updateBusNo(new BusNoBo(busNoOptional.get()));
            Assert.assertEquals(busNo.get().getNodes().size(),2);
        } catch (BaseSystemException e){
            Assert.fail();
        }
    }
    //编辑车次时busNo包含的节点增加一个，增加前节点数为3，增加后为4
    @Test(priority = 14)
    public void updateBusNoWithAddOneNode(){
        try {
            Optional<BusNo> busNoOptional = addBusNo();
            Node addNode = new Node(busNoOptional.get(), this.firstNodeType, this.firstNodeDestination, null, 0, 92100, 0);
            busNoOptional.get().getNodes().add(addNode);
            Optional<BusNo> busNo = busNoBiz.updateBusNo(new BusNoBo(busNoOptional.get()));
            Assert.assertEquals(busNo.get().getNodes().size(),4);
        } catch (BaseSystemException e){
            Assert.fail();
        }
    }
    //编辑车次时busNo的批次号和其中某个节点类型改变，节点数不变
    @Test(priority = 15)
    public void updateBusNoWithModifyName(){
        try {
            Optional<BusNo> busNoOptional = addBusNo();
            busNoOptional.get().setNo("修改后的批次号1");
            busNoOptional.get().getNodes().get(0).setNodeType(this.secondNodeType);
            Optional<BusNo> busNo = busNoBiz.updateBusNo(new BusNoBo(busNoOptional.get()));
            Assert.assertEquals("修改后的批次号1",busNo.get().getNo());
            Assert.assertEquals(busNo.get().getNodes().get(0).getNodeType().getId(),this.secondNodeType.getId());
        } catch (BaseSystemException e){
            Assert.fail();
        }
    }
    //编辑车次时busNo的批次号和其中某个节点类型改变，节点数减1
    @Test(priority = 16)
    public void updateBusNoWithModifyNameAndRemoveOneNode(){
        try {
            Optional<BusNo> busNoOptional = addBusNo();
            busNoOptional.get().setNo("修改后的批次号2");
            //节点类型修改前的验证
            Assert.assertEquals(busNoOptional.get().getNodes().get(0).getNodeType().getName(),this.firstNodeType.getName());

            busNoOptional.get().getNodes().get(0).setNodeType(this.secondNodeType);
            busNoOptional.get().getNodes().remove(2);
            Optional<BusNo> busNo = busNoBiz.updateBusNo(new BusNoBo(busNoOptional.get()));

            Assert.assertEquals("修改后的批次号2",busNo.get().getNo());
            Assert.assertEquals(busNo.get().getNodes().size(),2);
            //节点类型修改后的验证
            Assert.assertEquals(busNo.get().getNodes().get(0).getNodeType().getName(),this.secondNodeType.getName());
        } catch (BaseSystemException e){
            Assert.fail();
        }
    }
     //编辑车次时busNo，被编辑的busNo不存在
     @Test(priority = 17)
    public void updateBusNoWithIdNotIsExist(){
        try {
            Optional<BusNo> busNoById = addBusNo();
            busNoById.get().setId(busNoById.get().getId().longValue() + 1);
            Optional<BusNo> busNo = busNoBiz.updateBusNo(new BusNoBo(busNoById.get()));
            Assert.fail();
        } catch (BaseSystemException e){
            Assert.assertEquals(e.getMessage(), TravelTransportBizError.BUS_NO_UPDATE_ABNORMALLY.getErrorMessage());
        }
    }
    //编辑车次时busNo的批次号被修改，但是修改后的名字数据库已经存在
    //这里用的数据来自 192.168.2.109:5432/simpletour_20160223数据库
//    @Test
//    @Rollback(value = false)
//    public void updateBusNoWithNoIsExist(){
//        try {
//            Optional<BusNo> busNo = busNoBiz.findBusNoById(228L);
//            busNo.get().setNo("CD056YWG");
//            busNoBiz.updateBusNo(new BusNoBo(busNo.get()));
//            Assert.fail();
//        } catch (BaseSystemException e){
//            Assert.assertEquals(e.getMessage(), TravelTransportBizError.BUS_NO_NO_IS_EXISTED.getErrorMessage());
//        }
//    }


    //根据条件分页查询批次,全部精确查询
    @Test(priority = 18)
    public void findBusNoPageByConditionsWithExact(){
        try {
            HashMap<String, Object> conditionMap = new HashMap<>();
            conditionMap.put("no","HSW521CD");
            conditionMap.put("depart","花水湾正常");
            conditionMap.put("arrive","成都正常");
            conditionMap.put("status", BusNo.Status.normal);
            DomainPage<BusNo> domainPage = busNoBiz.findBusNoPageByConditions(conditionMap, "id", IBaseDao.SortBy.DESC, 1, 10, false);
            Assert.assertEquals(domainPage.getDomains().size(),1);
        } catch (BaseSystemException e){
            Assert.fail();
        }
    }
    //根据条件分页查询批次,全部模糊查询
    @Test(priority = 19)
    public void findBusNoPageByConditionsWithFuzzy(){
        try {
            HashMap<String, Object> conditionMap = new HashMap<>();
            conditionMap.put("no","HSW52");
            conditionMap.put("depart","水湾正");
            conditionMap.put("arrive","成都正常");
            conditionMap.put("status", BusNo.Status.normal);
           // DomainPage<BusNo> domainPage = busNoBiz.findBusNoPageByConditions(conditionMap, "id", IBaseDao.SortBy.DESC, 1, 10,true);
            DomainPage<BusNo> domainPage = busNoBiz.findBusNoPageByConditions(conditionMap, "id", IBaseDao.SortBy.DESC, 1, 10,false);
            //Assert.assertEquals(domainPage.getDomains().size(),1);
            Assert.assertEquals(domainPage.getDomains().size(),0);
        } catch (BaseSystemException e){
            Assert.fail();
        }
    }
    //根据条件列表查询批次，全部精确匹配
    @Test(priority = 20)
    public void findBusNoByConditions(){
        try {
            HashMap<String, Object> conditionMap = new HashMap<>();
            conditionMap.put("no","HSW521CD");
            conditionMap.put("depart","花水湾正常");
            conditionMap.put("arrive","成都正常");
            conditionMap.put("status", BusNo.Status.normal);
            List<BusNo> busNos = busNoBiz.findBusNoByConditions(conditionMap, "id", IBaseDao.SortBy.DESC);
            Assert.assertEquals(busNos.size(),1);
        } catch (BaseSystemException e){
            Assert.fail();
        }
    }

    //删除BusNo时，busNo为null 或者 busNo.getId()为null
    @Test(priority = 21)
    public void deleteBusNo(){
        try {
            //boolean b = busNoBiz.deleteBusNo(null);
            BusNo busNo = new BusNo();
            busNo.setId(null);
            busNoBiz.deleteBusNo(busNo);
            Assert.fail();
        } catch (BaseSystemException e){
            Assert.assertEquals(e.getMessage(), TravelTransportBizError.BUS_NO_NULL.getErrorMessage());
        }
    }

    //正常删除车次busNo
    @Test(priority = 22)
    public void deleteBusNoNormal(){
        try {
            boolean busNo = busNoBiz.deleteBusNo(this.savedBusNo.get());
            Assert.assertTrue(busNo);
        } catch (BaseSystemException e){
            Assert.fail();
        }
    }

    private Optional<BusNo> addBusNo(){
        Optional<BusNo> busNoOptional = null;
        try {
            ArrayList<Node> nodes = new ArrayList<>();
            BusNo busNo = new BusNo("",96,8100,71100,79200,"花水湾正常","成都正常",false, BusNo.Status.normal,false,nodes);
            busNo.setTenantId(1L);
            busNo.setNo(String.valueOf(new Date().getTime()));
            BusNo busNoWithNodes = setAllNodes(busNo);
            busNoOptional = busNoBiz.addBusNo(new BusNoBo(busNoWithNodes));
        } catch (BaseSystemException e){
            Assert.fail();
        }
        return busNoOptional;
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

    //给车次设置节点，节点数为1，并且节点相关的nodeType和destination存在
    private BusNo setOneNodes(BusNo busNo){
        Node firstNode = new Node(busNo, this.firstNodeType, this.secondNodeDestination, null, 0, 71100, 0);
        busNo.getNodes().add(firstNode);
        return busNo;
    }

    //给车次设置节点，节点数为3，并且节点相关的nodeType和destination存在,但是其中一个节点的arriveTime大于departTime
    private BusNo setNodesArriveTimeGTDepartTime(BusNo busNo){

        Node firstNode = new Node(busNo, this.firstNodeType, this.firstNodeDestination, null, 0, 8888, -88);
        Node secondNode = new Node(busNo, this.secondNodeType, this.secondNodeDestination, null, 0, 79200, 0);
        Node thirdNode = new Node(busNo, this.thirdNodeType, this.thirdNodeDestination, null, 0, 81200, 0);

        busNo.getNodes().add(firstNode);
        busNo.getNodes().add(secondNode);
        busNo.getNodes().add(thirdNode);

        return busNo;
    }

    @AfterClass
    public void clearData() throws Exception {
        List<Node> nodes = transportDao.getAllEntities(Node.class);
        if (nodes !=null && !nodes.isEmpty()){
            for(Node node : nodes){
                transportDao.removeEntity(node);
            }
        }
        List<NodeType> nodeTypes = transportDao.getAllEntities(NodeType.class);
        if (nodeTypes !=null && !nodeTypes.isEmpty()){
            for(NodeType nodeType : nodeTypes){
                transportDao.removeEntity(nodeType);
            }
        }
        List<Destination> destinations = transportDao.getAllEntities(Destination.class);
        if (destinations !=null && !destinations.isEmpty()){
            for(Destination destination : destinations){
                transportDao.removeEntity(destination);
            }
        }
        List<BusNo> busNos = transportDao.getAllEntities(BusNo.class);
        if (busNos !=null && !busNos.isEmpty()){
            for(BusNo busNo : busNos){
                transportDao.removeEntity(busNo);
            }
        }
    }



}
