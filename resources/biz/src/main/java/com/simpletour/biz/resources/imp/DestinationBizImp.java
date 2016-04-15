package com.simpletour.biz.resources.imp;

import com.simpletour.biz.resources.IDestinationBiz;
import com.simpletour.biz.resources.error.ResourcesBizError;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import org.springframework.stereotype.Component;
import resources.Area;
import resources.Destination;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @author XuHui/xuhui@simpletour.com
 *         Date: 2016/3/23
 *         Time: 10:02
 */
@Component
public class DestinationBizImp implements IDestinationBiz {

    @Resource
    private com.simpletour.dao.resources.IResourcesDao IResourcesDao;

    public Destination addDestination(Destination dest) throws BaseSystemException {
        destinationValid(dest, false);
        return IResourcesDao.save(dest);
    }

    public Destination updateDestination(Destination dest) throws BaseSystemException {
        destinationValid(dest, true);
        return IResourcesDao.save(dest);
    }

    public void deleteDestination(long id) throws BaseSystemException {
        IResourcesDao.removeEntityById(Destination.class, id);
    }

    public DomainPage<Destination> getDestinationsByPage(int page, int pageSize) {
        return getDestionationsByConditonPage(null, null, page, pageSize);
    }

    public DomainPage<Destination> getDestionationsByConditonPage(String name, String area, int page, int pageSize) throws BaseSystemException {
        HashMap<String, Object> kv = new HashMap<String, Object>(3);
        if (name != null && !name.equals(""))
            kv.put("name", name);
        if (area != null && !area.equals(""))
            kv.put("area.name", area);
        kv.put("del", false);

        if (kv.size() == 0)
            return IResourcesDao.getAllEntitiesByPage(Destination.class, page, pageSize, "id", IBaseDao.SortBy.DESC);
        return IResourcesDao.findEntitiesPagesByFieldList(Destination.class, kv, "id", IBaseDao.SortBy.DESC, page, pageSize);
    }

    public Destination getDestinationById(long id) {
        return IResourcesDao.getEntityById(Destination.class, id);
    }

    public boolean isExisted(Long id) {
        if(id==null)
            throw new BaseSystemException(ResourcesBizError.EMPTY_ENTITY);
        Destination destination=getDestinationById(id);
        if (destination == null || destination.getDel())
            return false;
        return true;
    }

    /**
     * 校验目的地在同一地区是否重名
     *
     * @param destination
     * @param isUpdate
     */
    private void destinationValid(Destination destination, Boolean isUpdate) {
        List<Destination> existDestinations = findDestinationByNameAndArea(destination.getName(), destination.getArea());

        if (existDestinations.size() > 1) {   // 数据库数据有错
            BaseSystemException exception = new BaseSystemException(ResourcesBizError.DATA_ERROR_IN_DB);
            exception.setExtMessage("there exist more than one destination which have the same name in the same area with name:" + destination.getName());
            throw exception;
        }

        if (isUpdate) {
            if (existDestinations.size() == 1 && existDestinations.get(0).getId().longValue() != destination.getId().longValue())    // 同一区域内不能有同名目的地
                throw new BaseSystemException(ResourcesBizError.DESTINATION_NAME_UNDER_SAME_AREA_MUST_BE_UNIQUE);
        } else {
            if (existDestinations.size() > 0)    // 添加只需保证同一区域内不能有同名目的地
                throw new BaseSystemException(ResourcesBizError.DESTINATION_NAME_UNDER_SAME_AREA_MUST_BE_UNIQUE);
        }
    }

    private List<Destination> findDestinationByNameAndArea(String name, Area area) {
        HashMap<String, Object> conditions = new HashMap<>(2);
        conditions.put("area", area);
        conditions.put("name", name);
        conditions.put("del", Boolean.FALSE);
        return IResourcesDao.getEntitiesByFieldList(Destination.class, conditions);
    }
}
