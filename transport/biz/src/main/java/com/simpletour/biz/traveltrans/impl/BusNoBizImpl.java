package com.simpletour.biz.traveltrans.impl;

import com.simpletour.biz.traveltrans.IBusNoBiz;
import com.simpletour.biz.traveltrans.bo.BusNoBo;
import com.simpletour.biz.traveltrans.bo.NodeBo;
import com.simpletour.biz.traveltrans.error.TravelTransportBizError;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.resources.IResourcesDao;
import com.simpletour.dao.traveltrans.ITransportDao;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Mario on 2015/11/27.
 */
@Component
public class BusNoBizImpl implements IBusNoBiz {

    @Resource
    private ITransportDao iTransportDao;

    @Resource
    private IResourcesDao iResourcesDao;

    //校验busNo中的node是否是按照顺序排列
    void validateBusNoNode(List<NodeBo> nodeBos) {
        if (nodeBos == null || nodeBos.isEmpty())
            throw new BaseSystemException(TravelTransportBizError.BUS_NODE_LIST_NULL);
        if (nodeBos.size() < 2) throw new BaseSystemException(TravelTransportBizError.BUS_NODE_LIST_LENGTH_FALSE);
        //对传入的node节点进行判断,判断是否时间是正序的
        Integer lastDay = 0;
        Integer lastDepartTime = -1;
        for (int i = 0; i < nodeBos.size(); i++) {
            NodeBo nodeBo = nodeBos.get(i);
            if (nodeBo.getArriveTime() != null && nodeBo.getDepartTime() != null) {
                Integer arriveTime = nodeBo.getArriveTime() + nodeBo.getDay() * 86400;
                Integer departTime = nodeBo.getDepartTime() + nodeBo.getDay() * 86400;
                if (arriveTime <= lastDepartTime)
                    throw new BaseSystemException(TravelTransportBizError.BUS_NODE_ARRIVETIME_EARLY);
                if (departTime < arriveTime)
                    throw new BaseSystemException(TravelTransportBizError.BUS_NODE_DEPARTTIME_LATE);
                lastDepartTime = nodeBo.getDepartTime() + nodeBo.getDay() * 86400;
                if (nodeBo.getDay() < lastDay)
                    throw new BaseSystemException(TravelTransportBizError.BUS_NODE_DAY_EARLY);
                lastDay = nodeBo.getDay();
            }
        }
    }

    //根据busNoId更新node信息
    private void updateNodesByBusNoId(BusNo busNo) {
        if (busNo.getId() == null || busNo.getNodes() == null || busNo.getNodes().isEmpty())
            throw new BaseSystemException(TravelTransportBizError.BUS_NODE_NULL);
        //遍历出有id并且需要更新节点
        List<Node> toBeUpdateNodes = busNo.getNodes().stream().filter(node1 -> node1.getId() != null).collect(Collectors.toList());
        //查询数据库中已经存在的nodes
        List<Node> originalNodes = iTransportDao.getEntitiesByField(Node.class, "busNo.id", busNo.getId());
        //删除掉已经不存的节点
        List<Node> toBeDeleteNodes = originalNodes.stream().filter(node -> !toBeUpdateNodes.contains(node)).collect(Collectors.toList());
        toBeDeleteNodes.stream().forEach(node -> iTransportDao.removeEntityById(Node.class, node.getId(), false));
    }



    @Override
    public Optional<BusNo> addBusNo(BusNoBo busNoBo) {
        if (busNoBo == null || busNoBo.getNodeBos() == null || busNoBo.getNodeBos().isEmpty())
            throw new BaseSystemException(TravelTransportBizError.BUS_NO_NULL_FOR_BUS_NO_AND_NODE);
        //对添加的BusNo中的no进行判断,判断是否在数据库中有相同的名字
        if (busNoBo.getNo() == null || busNoBo.getNo().isEmpty())
            throw new BaseSystemException(TravelTransportBizError.BUS_NO_NO_FOR_NULL);
        BusNo busNoExist = iTransportDao.getEntityByField(BusNo.class, "no", busNoBo.getNo());
        if (busNoExist != null) throw new BaseSystemException(TravelTransportBizError.BUS_NO_NO_IS_EXISTED);
        this.validateBusNoNode(busNoBo.getNodeBos());
        BusNo busNo = busNoBo.as();
        //将destination的id遍历出来组成一个数组
        List<Long> destinations = busNo.getNodes().stream().map(n -> n.getDestination().getId()).collect(Collectors.toList());
        busNo.setDestinations(destinations.toArray(new Long[destinations.size()]));
        return Optional.ofNullable(iTransportDao.save(busNo));
    }

    @Override
    public boolean deleteBusNo(BusNo busNo) {
        if (busNo == null || busNo.getId() == null)
            throw new BaseSystemException(TravelTransportBizError.BUS_NO_NULL);
        iTransportDao.removeEntityById(BusNo.class, busNo.getId(), false);
        return true;
    }

    @Override
    public Optional<BusNo> updateBusNo(BusNoBo busNoBo) {
        if (busNoBo == null || busNoBo.getNodeBos() == null || busNoBo.getNodeBos().isEmpty())
            throw new BaseSystemException(TravelTransportBizError.BUS_NO_NULL_FOR_BUS_NO_AND_NODE);
        //判断更新后的busNo中的no是否在数据库中已经存在
        if (busNoBo.getNo() == null) throw new BaseSystemException(TravelTransportBizError.BUS_NO_NO_FOR_NULL);
        //1.先从数据库中查询出原始的busNo
        BusNo original = iTransportDao.getEntityById(BusNo.class, busNoBo.getId());
        if (original == null) throw new BaseSystemException(TravelTransportBizError.BUS_NO_UPDATE_ABNORMALLY);
        //2.判断在数据库中是否有同名的busNo
        if (!original.getNo().equals(busNoBo.getNo())) {
            BusNo busNoExist = iTransportDao.getEntityByField(BusNo.class, "no", busNoBo.getNo());
            if (busNoExist != null) throw new BaseSystemException(TravelTransportBizError.BUS_NO_NO_IS_EXISTED);
        }
        //3.校验node节点信息
        this.validateBusNoNode(busNoBo.getNodeBos());
        BusNo busNo = busNoBo.as();
        List<Long> destinations = busNoBo.getNodeBos().stream().map(n -> n.getDestination().getId()).collect(Collectors.toList());
        busNo.setDestinations(destinations.toArray(new Long[destinations.size()]));
        this.updateNodesByBusNoId(busNo);
        return Optional.ofNullable(iTransportDao.save(busNo));
    }

    @Override
    public Optional<BusNo> findBusNoByNo(String no, Boolean eager) {
        if (no == null || no.isEmpty()) throw new BaseSystemException(TravelTransportBizError.BUS_NO_NO_FOR_NULL);
        if (eager) {
            BusNo busNo = iTransportDao.getEntityByField(BusNo.class, "no", no);
            if (!(busNo == null || busNo.getId() == null)) {
                List<Node> nodeList = iTransportDao.findNodeListByBusNo(busNo);
                busNo.setNodes(nodeList);
            }
            return Optional.ofNullable(busNo);
        } else {
            return Optional.ofNullable(iTransportDao.getEntityByField(BusNo.class, "no", no));
        }
    }

    @Override
    public Optional<BusNo> findBusNoById(Long id) {
        if (id == null) throw new BaseSystemException(TravelTransportBizError.BUS_NO_NULL);
        return Optional.ofNullable(iTransportDao.getEntityById(BusNo.class, id));
    }

    @Override
    public DomainPage<BusNo> findBusNoPageByConditions(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, Integer pageIndex, Integer pageSize, boolean byLike) {
        return iTransportDao.queryEntitiesPagesByFieldList(BusNo.class, conditions, orderByFiledName, orderBy, pageIndex, pageSize, byLike);
    }

    @Override
    public List<BusNo> findBusNoByConditions(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy sortBy) {
        return iTransportDao.getEntitiesByFieldList(BusNo.class, conditions, orderByFiledName, sortBy);
    }

    @Override
    public DomainPage<BusNo> findAvailableBusNo(String no, int pageIndex, int pageSize) {
        return iTransportDao.findAvailableBusNo(no, pageIndex, pageSize);
    }

    @Override
    public boolean isExisted(Long id) {
        if (id == null) throw  new BaseSystemException(TravelTransportBizError.BUS_NO_NULL);
        BusNo busNo = iTransportDao.getEntityById(BusNo.class, id);
        if (busNo == null)
            return false;
        return true;
    }

    @Override
    public boolean isAvailable(Long id) {
        if (id == null) throw  new BaseSystemException(TravelTransportBizError.BUS_NO_NULL);
        BusNo busNo = iTransportDao.getEntityById(BusNo.class, id);
        if (busNo == null)
            return false;
        return busNo.getStatus().equals(BusNo.Status.normal);
    }
}
