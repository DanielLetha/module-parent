package com.simpletour.biz.resources.imp;

import com.simpletour.biz.resources.IScenicBiz;
import com.simpletour.biz.resources.error.ResourcesBizError;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.resources.IResourcesDao;
import org.springframework.stereotype.Component;
import resources.Scenic;
import resources.UnionEntityKey;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Brief : 对景区的数据校验
 * Author: Hawk
 * Date  : 2016/3/21
 */
@Component
public class ScenicBizImp implements IScenicBiz {

    @Resource
    private IResourcesDao resourcesDao;

    @Override
    public Scenic addScenic(Scenic scenic) throws BaseSystemException {
        checkNotNull(scenic);
        checkNotDel(scenic);
        addCheckNameNotRepeat(scenic);
        return resourcesDao.save(scenic);
    }

    @Override
    public void deleteScenic(long id) throws BaseSystemException {
        resourcesDao.removeEntityById(Scenic.class, id);
    }

    @Override
    public Scenic updateScenic(Scenic scenic) throws BaseSystemException {
        checkNotNull(scenic);
        checkIdNotNull(scenic);
        checkVersion(scenic);
        checkUnionEntityKey(scenic);
        updateCheckNameNotRepeat(scenic);
        checkParentNotSelf(scenic);
        return resourcesDao.save(scenic);
    }

    @Override
    public Scenic findScenicById(long id) {
        Scenic scenic = resourcesDao.getEntityById(Scenic.class, id);
        if (scenic != null && (!scenic.getDel())) {
            return scenic;
        }
        return null;
    }

    @Override
    public List<Scenic> findScenicsPagesByConditions(Map<String, Object> conditions) {
        return resourcesDao.getEntitiesByFieldList(Scenic.class, conditions);
    }

    @Override
    public DomainPage<Scenic> queryScenicsPagesByConditions(final Map<String, Object> conditions
            , String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize, boolean byLike) throws BaseSystemException{
        return resourcesDao.queryEntitiesPagesByFieldList(Scenic.class,
                conditions, orderByFiledName, orderBy, pageIndex, pageSize, byLike);
    }

    @Override
    public boolean isExisted(long id) throws BaseSystemException {
        Scenic scenic = findScenicById(id);
        if (scenic == null || scenic.getDel()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 检查景点对象是否为null对象
     * @param scenic
     */
    private void checkNotNull(Scenic scenic) {
        if (scenic == null) {
            throw new BaseSystemException(ResourcesBizError.SCENIC_NULL);
        }
    }

    /**
     * 检查是否已经删除, 检查 del 字段为true
     * @param scenic
     */
    private void checkNotDel(Scenic scenic) {
        if (scenic.getDel()) {
            throw new BaseSystemException(ResourcesBizError.ADD_SCENIC_DEL);
        }
    }

    /**
     * 检查ID不为空
     * @param scenic
     */
    private void checkIdNotNull(Scenic scenic) {
        if (scenic.getId() == null) {
            throw new BaseSystemException(ResourcesBizError.ILLEGAL_ID);
        }
    }

    /**
     * 检查UnionEntityKey是否存在，检查ID是否符合要求
     * @param scenic
     */
    private void checkUnionEntityKey(Scenic scenic) {
        UnionEntityKey key = scenic.getUnionEntityKey();
        if (key == null) {
            throw new BaseSystemException(ResourcesBizError.INVALID_RESOURCE_AND_DESTINATION);
        }
    }

    /**
     * 当更新数据时，检查是否设置了
     * @param scenic
     */
    private void checkVersion(Scenic scenic) {
        if (scenic.getVersion() == null || scenic.getVersion().intValue() < 0) {
            throw new BaseSystemException(ResourcesBizError.SCENIC_UPDATE_VERSION_NULL);
        }
    }

    /**
     * @Brief  : 增加时，检查在同一个目的地下，景点的名字不能重名
     * @before : 需要先调用checkUnionEntityKey,以确保key不为空
     * @param scenic
     */
    private void addCheckNameNotRepeat(Scenic scenic) {
        List<Scenic> scenicList = getAllScenicOnDestination(scenic);
        if (!(scenicList == null || scenicList.isEmpty())) {
            throw new BaseSystemException(ResourcesBizError.SCENIC_NAME_REPAET_ON_DESTINATION);
        }
    }

    /**
     * @Brief  : 更新时，检查在同一个目的地下，景点的名字不能重名
     * @before : 需要先调用checkUnionEntityKey,以确保key不为空
     * @param scenic
     */
    private void updateCheckNameNotRepeat(Scenic scenic) {
        List<Scenic> scenicList = getAllScenicOnDestination(scenic);
        if (!(scenicList == null || scenicList.isEmpty())) {
            if (scenicList.stream().filter(s -> !s.getId().equals(scenic.getId())).count() > 0) {
                throw new BaseSystemException(ResourcesBizError.SCENIC_NAME_REPAET_ON_DESTINATION);
            }
        }
    }

    /**
     * 检查父景点不能为自己
     * @param scenic
     */
    private void checkParentNotSelf(Scenic scenic) {
        if (scenic == null)  {
            throw new BaseSystemException(ResourcesBizError.SCENIC_NULL);
        }
        Scenic parent = scenic.getParent();
        if (parent != null) {
            Long id = scenic.getId();
            if (null != id && id.equals(parent.getId())) {
                throw new BaseSystemException(ResourcesBizError.CANNOT_DEPEND_SAME_RESOURCE);
            }
        }
    }

    /**
     * 获取同一个目的地下所有景点
     * @param scenic
     * @return
     */
    private List<Scenic> getAllScenicOnDestination(Scenic scenic) {
        Map<String, Object> params = new HashMap<>();
        params.put("del", false);
        params.put("name", scenic.getName());
        params.put("destination.id", scenic.getDestination().getId());
        return findScenicsPagesByConditions(params);
    }
}
