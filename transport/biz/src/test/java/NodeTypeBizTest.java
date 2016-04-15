import com.simpletour.biz.traveltrans.INodeTypeBiz;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.traveltrans.ITransportDao;
import com.simpletour.domain.traveltrans.NodeType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Created by WangLin on 2016/3/21.
 */
@ContextConfiguration({"classpath:applicationContext.xml"})
public class NodeTypeBizTest extends AbstractTestNGSpringContextTests {

    @Resource
    private INodeTypeBiz nodeTypeBiz;

    @Resource
    private ITransportDao transportDao;

    /**
     * 制造15条数据
     */
    @Test
    @Rollback(value = false)
    public void saveNodeType(){
        for (int i = 0;i < 15;i++){
            NodeType nodeType = new NodeType();
            nodeType.setName("节点类型" + i);
            nodeType.setDisplay(true);
            NodeType type = transportDao.save(nodeType);
        }
    }


    @Test
    @Rollback(value = false)
    public void findNodeTypeById(){
        try{
//            id为null的情况
//            Optional<NodeType> nodeType = nodeTypeBiz.findNodeTypeById(null);

            //id不存在的情况
//            Optional<NodeType> nodeType = nodeTypeBiz.findNodeTypeById(210L);
//            Assert.assertEquals(nodeType.isPresent(),false);

            //id存在的情况下
            Optional<NodeType> nodeType = nodeTypeBiz.findNodeTypeById(1L);
            Assert.assertEquals(nodeType.isPresent(),true);
        }catch (BaseSystemException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * 没有查询条件时的分页查询
     */
    @Test
    @Rollback(value = false)
    public void findNodeTypePageByConditionsIsEmpty(){
        try{
            HashMap<String, Object> conditionMap = new HashMap<>();
            DomainPage<NodeType> domainPage = nodeTypeBiz.findNodeTypePageByConditions(conditionMap, "id", IBaseDao.SortBy.DESC, 1, 10, false);
            Assert.assertEquals(domainPage.getDomains().size(),10);
        }catch (BaseSystemException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * conditionMap条件为精确的分页查询
     */
    @Test
    @Rollback(value = false)
    public void findNodeTypePageByConditionsWithExact(){
        try{
            HashMap<String, Object> conditionMap = new HashMap<>();
            conditionMap.put("parent.id",1L);
            //conditionMap.put("name","节点类型2");
            DomainPage<NodeType> domainPage = nodeTypeBiz.findNodeTypePageByConditions(conditionMap, "id", IBaseDao.SortBy.DESC, 1, 10, false);
            Assert.assertEquals(domainPage.getDomains().size(),10);
        }catch (BaseSystemException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * conditionMap条件为模糊的分页查询
     */
    @Test
    @Rollback(value = false)
    public void findNodeTypePageByConditionsWithFuzzy(){
        try{
            HashMap<String, Object> conditionMap = new HashMap<>();
            conditionMap.put("name","节点类型");
            DomainPage<NodeType> domainPage = nodeTypeBiz.findNodeTypePageByConditions(conditionMap, "id", IBaseDao.SortBy.DESC, 1, 10, true);
            Assert.assertEquals(domainPage.getDomains().size(),10);
        }catch (BaseSystemException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * 没有查询条件的列表查询
     */
    @Test
    @Rollback(value = false)
    public void findNodeTypeByConditionsIsEmpty(){
        try{
            HashMap<String, Object> conditionMap = new HashMap<>();
            List<NodeType> list = nodeTypeBiz.findNodeTypeByConditions(conditionMap, "id", IBaseDao.SortBy.DESC);
            Assert.assertEquals(list.size(),15);
        }catch (BaseSystemException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * 有查询条件的列表查询,并且为精确查询
     */
    @Test
    @Rollback(value = false)
    public void findNodeTypeByConditionsWithExact(){
        try{
            HashMap<String, Object> conditionMap = new HashMap<>();
            conditionMap.put("display",false);
            conditionMap.put("name","节点类型0");
            conditionMap.put("parent.id",1L);
            List<NodeType> list = nodeTypeBiz.findNodeTypeByConditions(conditionMap, "id", IBaseDao.SortBy.DESC);
            Assert.assertEquals(list.get(0).getName(),"节点类型0");
        }catch (BaseSystemException e){
            System.out.println(e.getMessage());
        }
    }


}
