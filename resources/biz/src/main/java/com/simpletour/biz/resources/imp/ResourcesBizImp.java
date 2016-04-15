package com.simpletour.biz.resources.imp;

import com.simpletour.biz.resources.IProcurementBiz;
import com.simpletour.biz.resources.IResourcesBiz;
import com.simpletour.biz.resources.error.ResourcesBizError;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.BaseDomain;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.domain.LogicalDeletableDomain;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.resources.IResourcesDao;
import com.simpletour.domain.resources.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 文件描述：资源模块业务层实现类
 * 创建人员：石广路
 * 创建日期：2015/12/4 15:00
 * 备注说明：null
 */
@Component
public class ResourcesBizImp implements IResourcesBiz {
    @Resource
    private IResourcesDao resourcesDao;

    @Resource
    private IProcurementBiz procurementBiz;

//    @Resource
//    private IStockBiz stockBiz;

    @Override
    public long getResourceId(IUnionEntityKey unionEntityKey) {
        return null != unionEntityKey ? resourcesDao.getResourceId(unionEntityKey.getUnionEntityKey()) : 0;
    }

    @Override
    public <T extends LogicalDeletableDomain> Optional<T> getResourceById(Class<T> tClass, long id) {
        return Optional.ofNullable(resourcesDao.getEntityById(tClass, id));
    }

    @Override
    public <T extends LogicalDeletableDomain> List<T> getResourcesListByDestId(Class<T> tClass, long destId) {
        return resourcesDao.getEntitiesByField(tClass, "destination_id", destId);
    }

    @Override
    public <T extends LogicalDeletableDomain> void deleteResource(Class<T> tClass, long id, boolean pseudo) throws BaseSystemException {
        if (resourcesDao.hasDependedByProcurement(tClass, id)) {
            throw new BaseSystemException(ResourcesBizError.CANNOT_DEL_DEPENDENT_RESOURCE);
        }
        resourcesDao.removeEntityById(tClass, id);
    }

    @Override
    public Optional<Hotel> addHotel(Hotel hotel) throws BaseSystemException {
        validateAddedEntity(hotel);
        return Optional.ofNullable(resourcesDao.save(hotel));
    }

    @Override
    public Optional<Hotel> updateHotel(Hotel hotel) throws BaseSystemException {
        validateUpdatedEntity(hotel);

        Hotel existence = resourcesDao.getResourceById(hotel.getUnionEntityKey());
        if (null != existence && !hotel.getId().equals(existence.getId()) && hotel.getDestination().getId().equals(existence.getDestination().getId()) && hotel.getName().equals(existence.getName())) {
            throw new BaseSystemException(ResourcesBizError.SAME_NAME_RESOURCE_IS_EXISTING);
        }

        Hotel original = resourcesDao.getEntityById(Hotel.class, hotel.getId());
        Optional<Hotel> optional = Optional.ofNullable(resourcesDao.save(hotel));

        if (hasDependenciesChanged(original.getDestination(), hotel.getDestination())) {
            resourcesDao.updateDependency(hotel);
        }

        return optional;
    }

    @Override
    public DomainPage<Hotel> queryHotelsPagesByConditions(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize, boolean byLike) {
        return resourcesDao.queryEntitiesPagesByFieldList(Hotel.class, conditions, orderByFiledName, orderBy, pageIndex, pageSize, byLike);
    }

    private boolean hasOfflineServiceProvider(Long id) throws BaseSystemException {
        if (null == id || 0 >= id) {
            throw new BaseSystemException(ResourcesBizError.ILLEGAL_ID);
        }
        return 0 < resourcesDao.getEntityTotalCount(OfflineServiceProvider.class, "id", id);
    }

    private boolean hasResourceEntity(IUnionEntityKey unionEntityKey) throws BaseSystemException {
        if (null == unionEntityKey) {
            throw new BaseSystemException(ResourcesBizError.EMPTY_ENTITY);
        }

        UnionEntityKey key = unionEntityKey.getUnionEntityKey();
        if (null == key) {
            throw new BaseSystemException(ResourcesBizError.INVALID_RESOURCE_AND_DESTINATION);
        }

        return 0 < resourcesDao.getResourceId(key);
    }

    private void validateAddedEntity(IUnionEntityKey unionEntityKey) throws BaseSystemException {
        if (hasResourceEntity(unionEntityKey)) {
            throw new BaseSystemException(ResourcesBizError.RESOURCE_IS_EXISTING);
        }
        if (!hasResourceEntity(unionEntityKey.getUnionEntityKey().getDestination())) {
            throw new BaseSystemException(ResourcesBizError.DESTINATION_NOT_EXIST);
        }
    }

    private void validateUpdatedEntity(BaseDomain domain) throws BaseSystemException {
        if (null == domain) {
            throw new BaseSystemException(ResourcesBizError.EMPTY_ENTITY);
        }

        Long id = domain.getId();
        if (null == id || 0 >= id) {
            throw new BaseSystemException(ResourcesBizError.ILLEGAL_ID);
        }

        if (domain instanceof Procurement) {
            return;
        }

        IUnionEntityKey unionEntityKey = (IUnionEntityKey)domain;
        if (null == unionEntityKey.getUnionEntityKey()) {
            throw new BaseSystemException(ResourcesBizError.INVALID_RESOURCE_AND_DESTINATION);
        }
    }

    private void validateScenicDependencies(Scenic scenic) throws BaseSystemException {
        Scenic parent = scenic.getParent();
        if (null != parent) {
            Long id = scenic.getId();
            if (null != id && id.equals(parent.getId())) {
                throw new BaseSystemException(ResourcesBizError.CANNOT_DEPEND_SAME_RESOURCE);
            }
            if (!hasResourceEntity(parent)) {
                throw new BaseSystemException(ResourcesBizError.NOT_EXIST_RESOURCE);
            }
            if (!hasResourceEntity(parent.getUnionEntityKey().getDestination())) {
                throw new BaseSystemException(ResourcesBizError.DESTINATION_NOT_EXIST);
            }
        }

        Long destId = scenic.getDestination().getId();
        String name = scenic.getName();
        List<Scenic> scenics = scenic.getScenics();
        if (null != destId && null != name && null != scenics && !scenics.isEmpty()) {
            scenics.stream().filter(item -> null != item && null != item.getId() && !destId.equals(item.getDestination().getId()) && !name.equals(item.getName())).forEach(item -> {
                if (!hasResourceEntity(item)) {
                    throw new BaseSystemException(ResourcesBizError.NOT_EXIST_RESOURCE);
                }
                if (!hasResourceEntity(item.getUnionEntityKey().getDestination())) {
                    throw new BaseSystemException(ResourcesBizError.DESTINATION_NOT_EXIST);
                }
            });
        }
    }

    private boolean hasDependenciesChanged(Destination orgDestination, Destination currDestination) throws BaseSystemException {
        validateUpdatedEntity(orgDestination);
        validateUpdatedEntity(currDestination);
        // 不同的时候则需要更新依赖
        return !orgDestination.getId().equals(currDestination.getId());
    }

    public Area getAreaById(Long id) {
        return resourcesDao.getEntityById(Area.class, id);
    }
}
