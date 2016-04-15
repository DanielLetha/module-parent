package com.simpletour.biz.resources.imp;

import com.simpletour.biz.resources.IHotelBiz;
import com.simpletour.biz.resources.error.ResourcesBizError;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.resources.IResourcesDao;
import com.simpletour.domain.resources.Destination;
import com.simpletour.domain.resources.Hotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author XuHui/xuhui@simpletour.com
 *         Date: 2016/3/23
 *         Time: 9:55
 */
@Component
public class HotelBizImp implements IHotelBiz {

    @Autowired
    private IResourcesDao resourcesDao;

    public Hotel addHotel(Hotel hotel) throws BaseSystemException {
        hotelValid(hotel, false);
        return resourcesDao.save(hotel);
    }

    public Hotel updateHotel(Hotel hotel) throws BaseSystemException {
        hotelValid(hotel, true);
        return resourcesDao.save(hotel);
    }

    public DomainPage<Hotel> queryHotelsPagesByConditions(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize, boolean byLike) {
        if (orderBy == null || orderByFiledName == null || orderByFiledName.equals("")) {
            orderBy = IBaseDao.SortBy.DESC;
            orderByFiledName = "id";
        }
        return resourcesDao.queryEntitiesPagesByFieldList(Hotel.class, conditions, orderByFiledName, orderBy, pageIndex, pageSize, byLike);
    }

    //TODO: 待统一添加依赖处理
    public void deleteHotel(long id) throws BaseSystemException {
        resourcesDao.removeEntityById(Hotel.class, id);
    }

    public Hotel getHotelById(long id) {
        return resourcesDao.getEntityById(Hotel.class, id);
    }

    public boolean isExisted(Long id) {
        if(id==null)
            throw new BaseSystemException(ResourcesBizError.EMPTY_ENTITY);
        Hotel hotel = getHotelById(id);
        if (hotel == null || hotel.getDel()){
            return false;
        }
        return true;
    }

    /**
     * 判断住宿点是否存在
     *
     * @param hotel    待判断的住宿点
     * @param isUpdate 是否为更新操作
     * @throws BaseSystemException 所在目的地有同名住宿地（SAME_NAME_RESOURCE_IS_EXISTING）
     */
    private void hotelValid(Hotel hotel, Boolean isUpdate) throws BaseSystemException {
        List<Hotel> hotels = findHotelByNameAndDestination(hotel.getName(), hotel.getDestination());
        if (hotels.size() > 1) {
            BaseSystemException exception = new BaseSystemException(ResourcesBizError.DATA_ERROR_IN_DB);
            exception.setExtMessage("there exist more than one destination which have the same name in the same area with name:" + hotel.getName());
            throw exception;
        }
        if (isUpdate) {
            if (!hotels.isEmpty() && !hotels.get(0).getId().equals(hotel.getId()))
                throw new BaseSystemException(ResourcesBizError.SAME_NAME_RESOURCE_IS_EXISTING);
        } else {
            if (!hotels.isEmpty())
                throw new BaseSystemException(ResourcesBizError.SAME_NAME_RESOURCE_IS_EXISTING);
        }
    }

    private List<Hotel> findHotelByNameAndDestination(String name, Destination destination) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("name", name);
        conditions.put("destination", destination);
        conditions.put("del", Boolean.FALSE);
        return resourcesDao.getEntitiesByFieldList(Hotel.class, conditions);
    }
}
