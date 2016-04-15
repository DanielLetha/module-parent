package com.simpletour.biz.resources.imp;

import com.simpletour.biz.resources.IEntertainmentBiz;
import com.simpletour.biz.resources.error.ResourcesBizError;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.resources.IResourcesDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import resources.Destination;
import resources.Entertainment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：XuHui/xuhui@simpletour.com
 * Brief：娱乐
 * Date: 2016/3/23
 * Time: 15:36
 */
@Component
public class EntertainmentBizImp implements IEntertainmentBiz {

    @Autowired
    private IResourcesDao resourcesDao;


    @Override
    public Entertainment addEntertainment(Entertainment entertainment) throws BaseSystemException {
        entertainmentValid(entertainment, false);
        return resourcesDao.save(entertainment);
    }

    @Override
    public Entertainment updateEntertainment(Entertainment entertainment) throws BaseSystemException {
        entertainmentValid(entertainment, true);
        return resourcesDao.save(entertainment);
    }

    @Override
    public void deleteEntertainment(long id) throws BaseSystemException {
        resourcesDao.removeEntityById(Entertainment.class, id);
    }

    @Override
    public Entertainment getEntertainmentById(long id) {
        return resourcesDao.getEntityById(Entertainment.class, id);
    }

    @Override
    public boolean isExisted(Long id) {
        if (id == null)
            throw new BaseSystemException(ResourcesBizError.EMPTY_ENTITY);
        Entertainment entertainment = getEntertainmentById(id);
        if (entertainment == null || entertainment.getDel())
            return false;
        return true;
    }

    @Override
    public DomainPage<Entertainment> getEntertainmentsByPage(int page, int pageSize) {
        return getEntertainmentsByConditionPage(null, null, null, page, pageSize);
    }

    @Override
    public DomainPage<Entertainment> getEntertainmentsByConditionPage(String type, String name, String destinationName, int page, int pageSize) throws BaseSystemException {
        Map<String, Object> conditions = new HashMap<>();
        if (name != null && !name.equals(""))
            conditions.put("name", name);
        if (type != null && !type.equals(""))
            conditions.put("type", Entertainment.Type.valueOf(type));
        if (destinationName != null && !destinationName.equals(""))
            conditions.put("destination.name", destinationName);
        conditions.put("del", false);

        return resourcesDao.findEntitiesPagesByFieldList(Entertainment.class, conditions, "id", IBaseDao.SortBy.DESC, page, pageSize);
    }

    /**
     * 判断住宿点是否存在
     *
     * @param entertainment 待判断的住宿点
     * @param isUpdate      是否为更新操作
     * @throws BaseSystemException 所在目的地有同名娱乐（SAME_NAME_RESOURCE_IS_EXISTING）
     */
    private void entertainmentValid(Entertainment entertainment, Boolean isUpdate) throws BaseSystemException {
        List<Entertainment> entertainments = findEntertainmentByNameAndDestination(entertainment.getName(), entertainment.getDestination());
        if (entertainments.size() > 1) {
            BaseSystemException exception = new BaseSystemException(ResourcesBizError.DATA_ERROR_IN_DB);
            exception.setExtMessage("there exist more than one destination which have the same name in the same area with name:" + entertainment.getName());
            throw exception;
        }
        if (entertainments.isEmpty())
            return;
        if (isUpdate && entertainments.get(0).getId().equals(entertainment.getId()))
            return;

        throw new BaseSystemException(ResourcesBizError.SAME_NAME_RESOURCE_IS_EXISTING);
    }

    private List<Entertainment> findEntertainmentByNameAndDestination(String name, Destination destination) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("name", name);
        conditions.put("destination", destination);
        conditions.put("del", Boolean.FALSE);
        return resourcesDao.getEntitiesByFieldList(Entertainment.class, conditions);
    }
}
