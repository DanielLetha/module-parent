package com.simpletour.biz.traveltrans.impl;

import com.simpletour.biz.traveltrans.INodeTypeBiz;
import com.simpletour.biz.traveltrans.error.TravelTransportBizError;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.traveltrans.ITransportDao;
import com.simpletour.domain.traveltrans.NodeType;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Mario on 2015/12/5.
 */
@Component
public class NodeTypeBizImpl implements INodeTypeBiz {

    @Resource
    private ITransportDao iTransportDao;

    @Override
    public Optional<NodeType> findNodeTypeById(Long id) {
        if (id == null) throw new BaseSystemException(TravelTransportBizError.BUS_NODE_TYPE_NULL);
        return Optional.ofNullable(iTransportDao.getEntityById(NodeType.class, id));
    }

    @Override
    public DomainPage<NodeType> findNodeTypePageByConditions(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, Integer pageIndex, Integer pageSize, boolean byLike) {
        return iTransportDao.queryEntitiesPagesByFieldList(NodeType.class, conditions, orderByFiledName, orderBy, pageIndex, pageSize, byLike);
    }

    @Override
    public List<NodeType> findNodeTypeByConditions(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy sortBy) {
        return iTransportDao.getEntitiesByFieldList(NodeType.class, conditions, orderByFiledName, sortBy);
    }

    @Override
    public boolean isExisted(Long id) throws BaseSystemException {
        if (id == null) throw new BaseSystemException(TravelTransportBizError.BUS_NODE_TYPE_NULL);
        NodeType nodeType = iTransportDao.getEntityById(NodeType.class,id);
        if (nodeType == null) return false;
        return true;
    }

    @Override
    public boolean isExisted(NodeType nodeType) throws BaseSystemException {
        if(nodeType==null) throw new BaseSystemException(TravelTransportBizError.BUS_NODE_TYPE_NULL);
        return isExisted(nodeType.getId());
    }
}
