package com.simpletour.biz.resources.imp;

import com.simpletour.biz.resources.IProcurementBiz;
import com.simpletour.biz.resources.error.ResourcesBizError;
import com.simpletour.biz.resources.vo.ProcurementVo;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.resources.IResourcesDao;
import com.simpletour.domain.resources.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Brief : 元素的业务逻辑检查的实现
 * Author: Hawk
 * Date  : 2016/3/24
 */
@Component
public class ProcurementBizImp implements IProcurementBiz {

    @Resource
    private IResourcesDao resourcesDao;

    @Override
    public Procurement addProcurement(Procurement procurement) throws BaseSystemException {
        checkNotNull(procurement);
        checkNotDel(procurement);
        checkDestinationConsistency(procurement);
        addCheckNameNotRepeat(procurement);
        return resourcesDao.save(procurement);
    }

    @Override
    public Procurement updateProcurement(Procurement procurement) throws BaseSystemException {
        checkNotNull(procurement);
        checkIdNotNull(procurement);
        checkVersion(procurement);
        checkDestinationConsistency(procurement);
        updateCheckNameNotRepeat(procurement);
        return resourcesDao.save(procurement);
    }

    @Override
    public void deleteProcurement(long id) throws BaseSystemException {
        resourcesDao.removeEntityById(Procurement.class, id);
    }

    @Override
    public Procurement getProcurementById(Long id) {
        if (null != id && 0 < id) {
            Procurement procurement = resourcesDao.getEntityById(Procurement.class, id);
            if (null != procurement && !procurement.getDel()) {
                return procurement;
            }
        }
        return null;
    }

    @Override
    public List<Procurement> findProcurementsByConditions(Map<String, Object> conditions) {
        return resourcesDao.getEntitiesByFieldList(Procurement.class, conditions);
    }

    @Override
    public DomainPage<Procurement> queryProcurementsPagesByConditions(Map<String, Object> conditions
            , String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize, boolean byLike) {
        return resourcesDao.queryEntitiesPagesByFieldList(Procurement.class, conditions, orderByFiledName
                , orderBy, pageIndex, pageSize, byLike);
    }

    @Override
    public DomainPage<ProcurementVo> queryProcurementVoPagesByConditions(Map<String, Object> conditions
            , String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize) {

        DomainPage domainPage = resourcesDao.queryProcurement(conditions, orderByFiledName
                , orderBy, pageIndex, pageSize);
        if (domainPage != null) {

            // 将Procurement转换成ProcurementVo(需要获取ResourceName)
            List<Object[]> dataList = domainPage.getDomains();
            List<ProcurementVo> procurementVoList = dataList.stream().map(data -> {
                long id = Long.parseLong(data[0].toString());
                long resourceId = Long.parseLong(data[3].toString());
                boolean online = Boolean.parseBoolean(data[4].toString());
                String name = (String) data[1];
                String resourceType = (String) data[2];
                String destination = (String) data[5];
                ProcurementVo vo = new ProcurementVo(id, Procurement.ResourceType.valueOf(resourceType).getRemark()
                        , null, null, name, destination, online);
                if (conditions.containsKey("resourceType")) {
                    vo.setResourceName((String) data[6]);
                } else {
                    String resourceName = getResourceName(resourceType, resourceId);
                    vo.setResourceName(resourceName);
                }
                return vo;
            }).collect(Collectors.toList());
            DomainPage<ProcurementVo> result = new DomainPage<>(pageSize
                    , pageIndex, domainPage.getDomainTotalCount());
            result.getDomains().addAll(procurementVoList);
            return result;
        }
        return null;
    }

    @Override
    public boolean isExisted(long id) {
        Procurement procurement = getProcurementById(id);
        if (procurement == null || procurement.getDel()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public boolean isAvailable(long id) {
        Procurement procurement = getProcurementById(id);
        if (procurement == null || procurement.getDel()) {
            return Boolean.FALSE;
        }
        return procurement.getOnline();
    }

    /**
     * 检查元素对象是否为null对象
     * @param procurement
     */
    private void checkNotNull(Procurement procurement) {
        if (procurement == null) {
            throw new BaseSystemException(ResourcesBizError.PROCUREMENT_NULL);
        }
    }

    /**
     * 检查是否已经删除, 检查 del 字段为true
     * @param procurement
     */
    private void checkNotDel(Procurement procurement) {
        if (procurement.getDel()) {
            throw new BaseSystemException(ResourcesBizError.PROCUREMENT_ADD_AND_DEL);
        }
    }

    /**
     * 检查ID不为空
     * @param procurement
     */
    private void checkIdNotNull(Procurement procurement) {
        if (procurement.getId() == null) {
            throw new BaseSystemException(ResourcesBizError.ILLEGAL_ID);
        }
    }

    /**
     * 更新时，检查version是否为空
     * @param procurement
     */
    private void checkVersion(Procurement procurement) {
        if (procurement.getVersion() == null || procurement.getVersion().intValue() < 0) {
            throw new BaseSystemException(ResourcesBizError.PROCUREMENT_UPDATE_VERSION_NULL);
        }
    }

    /**
     * 检查元素的目的地与元素所对应的资源的目的地一致
     * @param procurement
     */
    private void checkDestinationConsistency(Procurement procurement) {
        Destination procurementDes = procurement.getDestination();
        Procurement.ResourceType resourceType = procurement.getResourceType();
        Long resourceId = procurement.getResourceId();
        Destination resourceDes = getResourceById(resourceType, resourceId);
        if (resourceDes == null) {
            throw new BaseSystemException(ResourcesBizError.PROCUREMENT_RESOURCE_ERROR);
        }
        if (procurementDes != null && resourceDes != null
                && !procurementDes.getId().equals(resourceDes.getId())) {
            throw new BaseSystemException(ResourcesBizError.PROCUREMENT_RESOURCE_DESTINATION_NOMATCH);
        }
    }

    /**
     * 获取资源所对应的对象
     * @param resourceType 资源类型
     * @param resourceId 资源ID
     * @return Destination 资源所对应的目的地
     */
    private Destination getResourceById(Procurement.ResourceType resourceType, Long resourceId) {
        Map<String, Object> params = new HashMap<>();
        params.put("del", Boolean.FALSE);
        params.put("id", resourceId);
        Destination resourceDestination = null;
        if (Procurement.ResourceType.catering.equals(resourceType)) {
            List<Catering> cateringList = resourcesDao.getEntitiesByFieldList(Catering.class, params);
            if (checkSingleResult(cateringList)) {
                resourceDestination = cateringList.get(0).getDestination();
            }
        } else if (Procurement.ResourceType.entertainment.equals(resourceType)) {
            List<Entertainment> entertainmentList = resourcesDao.getEntitiesByFieldList(Entertainment.class, params);
            if (checkSingleResult(entertainmentList)) {
                resourceDestination = entertainmentList.get(0).getDestination();
            }
        } else if (Procurement.ResourceType.hotel.equals(resourceType)) {
            List<Hotel> hotelList = resourcesDao.getEntitiesByFieldList(Hotel.class, params);
            if (checkSingleResult(hotelList)) {
                resourceDestination = hotelList.get(0).getDestination();
            }
        } else if (Procurement.ResourceType.scenic.equals(resourceType)) {
            List<Scenic> scenicList = resourcesDao.getEntitiesByFieldList(Scenic.class, params);
            if (checkSingleResult(scenicList)) {
                resourceDestination = scenicList.get(0).getDestination();
            }
        }
        return resourceDestination;
    }

    /**
     * 检查结果集是否只有一个
     * @param resultList
     * @return
     */
    private boolean checkSingleResult(List resultList) {
        if (!(resultList == null || resultList.isEmpty() || resultList.size() != 1)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 根据ResourceType和ResourceId获取资源名字
     * @param resourceType
     * @param resourceId
     */
    private String getResourceName(String resourceType, long resourceId) {
        if (Procurement.ResourceType.hotel.name().equals(resourceType)) {
            Hotel hotel = resourcesDao.getEntityById(Hotel.class, resourceId);
            if (!(hotel == null || hotel.getDel())) {
                return hotel.getName();
            }
        } else if (Procurement.ResourceType.scenic.name().equals(resourceType)) {
            Scenic scenic = resourcesDao.getEntityById(Scenic.class, resourceId);
            if (!(scenic == null || scenic.getDel())) {
                return scenic.getName();
            }
        } else if (Procurement.ResourceType.catering.name().equals(resourceType)) {
            Catering catering = resourcesDao.getEntityById(Catering.class, resourceId);
            if (!(catering == null || catering.getDel())) {
                return catering.getName();
            }
        } else if (Procurement.ResourceType.entertainment.name().equals(resourceType)) {
            Entertainment entertainment = resourcesDao.getEntityById(Entertainment.class, resourceId);
            if (!(entertainment == null || entertainment.getDel())) {
                return entertainment.getName();
            }
        }
        throw new BaseSystemException(ResourcesBizError.PROCUREMENT_RESOURCE_ERROR);
    }

    /**
     * @Brief  : 增加时，检查在同一个目的地下，景点的名字不能重名
     * @before : 需要先调用checkUnionEntityKey,以确保key不为空
     * @param procurement
     */
    private void addCheckNameNotRepeat(Procurement procurement) {
        List<Procurement> procurementList = getAllProcurementOnDestination(procurement);
        if (!(procurementList == null || procurementList.isEmpty())) {
            throw new BaseSystemException(ResourcesBizError.PROCUREMENT_NAME_REPEAT_ON_ONE_DESTINATION);
        }
    }

    /**
     * @Brief  : 更新时，检查在同一个目的地下，景点的名字不能重名
     * @before : 需要先调用checkUnionEntityKey,以确保key不为空
     * @param procurement
     */
    private void updateCheckNameNotRepeat(Procurement procurement) {
        List<Procurement> procurementList = getAllProcurementOnDestination(procurement);
        if (!(procurementList == null || procurementList.isEmpty())) {
            if (procurementList.stream().filter(p -> !p.getId().equals(procurement.getId())).count() > 0) {
                throw new BaseSystemException(ResourcesBizError.PROCUREMENT_NAME_REPEAT_ON_ONE_DESTINATION);
            }
        }
    }

    /**
     * 获取同一个目的地下所有元素
     * @param procurement
     * @return
     */
    private List<Procurement> getAllProcurementOnDestination(Procurement procurement) {
        Map<String, Object> params = new HashMap<>();
        params.put("del", false);
        params.put("name", procurement.getName());
        params.put("destination.id", procurement.getDestination().getId());
        return findProcurementsByConditions(params);
    }


 }
