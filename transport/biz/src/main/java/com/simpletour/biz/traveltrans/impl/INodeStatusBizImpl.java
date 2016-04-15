package com.simpletour.biz.traveltrans.impl;

import com.simpletour.biz.traveltrans.INodeStatusBiz;
import com.simpletour.biz.traveltrans.error.TravelTransportBizError;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.traveltrans.ITransportDao;
import com.simpletour.domain.traveltrans.NodeStatus;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Mario on 2015/12/3.
 */
@Component
public class INodeStatusBizImpl implements INodeStatusBiz {
    @Resource
    ITransportDao iTransportDao;

    @Override
    public Optional<NodeStatus> addNodeStatus(NodeStatus nodeStatus) {
        if (nodeStatus.getBusNo().getId() == null || nodeStatus.getDay() == null)
            throw new BaseSystemException(TravelTransportBizError.BUS_NODE_STATUS_NULL);
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("busNo.id", nodeStatus.getBusNo().getId());
        conditions.put("day", nodeStatus.getDay());
        List<NodeStatus> stati = iTransportDao.getEntitiesByFieldList(NodeStatus.class, conditions);
        if (!(stati == null || stati.isEmpty())) {
            Optional<NodeStatus> status = stati.stream().sorted((s1, s2) -> (int) (s2.getId() - s1.getId())).findFirst();
            if (com.simpletour.biz.traveltrans.enums.NodeStatus.of(status.get().getStatus().getValue()).accept(com.simpletour.biz.traveltrans.enums.NodeStatus.of(nodeStatus.getStatus().getValue()))) {
                return Optional.ofNullable(iTransportDao.save(nodeStatus));
            }
            return Optional.ofNullable(status.get());
        } else if (com.simpletour.biz.traveltrans.enums.NodeStatus.finished.getValue().equals(nodeStatus.getStatus().getValue())) {
            NodeStatus nodeStatus1 = new NodeStatus(nodeStatus.getBusNo(), nodeStatus.getBus(), nodeStatus.getDay(), nodeStatus.getNode(), nodeStatus.getTiming(), NodeStatus.Status.arrived);
            iTransportDao.save(nodeStatus1);
            return Optional.ofNullable(iTransportDao.save(nodeStatus));
        } else if (com.simpletour.biz.traveltrans.enums.NodeStatus.arrived.getValue().equals(nodeStatus.getStatus().getValue())) {
            return Optional.ofNullable(iTransportDao.save(nodeStatus));
        }
        return Optional.empty();
    }

    @Override
    public DomainPage<NodeStatus> findNodeStatusPagesByConditions(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy sortBy, Integer pageIndex, Integer pageSize, boolean byLike) {
        return iTransportDao.queryEntitiesPagesByFieldList(NodeStatus.class, conditions, orderByFiledName, sortBy, pageIndex, pageSize, byLike);
    }

    @Override
    public List<NodeStatus> findNodeStatusByConditions(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy sortBy) {
        return iTransportDao.getEntitiesByFieldList(NodeStatus.class, conditions, orderByFiledName, sortBy);
    }
}
