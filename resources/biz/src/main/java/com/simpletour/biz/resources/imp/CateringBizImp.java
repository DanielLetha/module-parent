package com.simpletour.biz.resources.imp;

import com.simpletour.biz.resources.ICateringBiz;
import com.simpletour.biz.resources.error.ResourcesBizError;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.resources.IResourcesDao;
import com.simpletour.domain.resources.Catering;
import com.simpletour.domain.resources.Destination;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Brief :  餐饮点操作的实现类
 * @Author:  liangfei/liangfei@simpletour.com
 * @Date  :  2016/3/23 11:12
 * @Since :  2.0.0-SNAPSHOT
 * @Remark:  无
 */
@Component
public class CateringBizImp implements ICateringBiz {

    @Resource
    private IResourcesDao resourcesDao;

    @Override
    public Catering addCatering(Catering catering) throws BaseSystemException {
        checkNull(catering);
        checkDel(catering);
        List<Catering> existList = findCateringByNameAndDestination(catering.getName(), catering.getDestination());
        if(existList.size() > 0){
            throw new BaseSystemException(ResourcesBizError.CATERING_NAME_MUST_BE_NUIQUE);
        }
        return resourcesDao.save(catering);
    }

    @Override
    public Catering updateCatering(Catering catering) throws BaseSystemException {
        checkNull(catering);
        if (catering.getId() == null) {
            throw new BaseSystemException(ResourcesBizError.CATERING_NOT_EXIST);
        }
        List<Catering> existList = findCateringByNameAndDestination(catering.getName(), catering.getDestination());
        if(existList.size() > 1){
            throw new BaseSystemException(ResourcesBizError.CATERING_NAME_MUST_BE_NUIQUE);
        }
        if(existList.size() == 1 && !existList.get(0).getId().equals(catering.getId())){
            throw new BaseSystemException(ResourcesBizError.CATERING_NAME_MUST_BE_NUIQUE);
        }

        return resourcesDao.save(catering);
    }

    @Override
    public void deleteCatering(long id, boolean pseudo) throws BaseSystemException {
        resourcesDao.removeEntityById(Catering.class, id);
    }

    @Override
    public DomainPage<Catering> queryCateringsPagesByConditions(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize, boolean byLike) {
        return resourcesDao.queryEntitiesPagesByFieldList(Catering.class, conditions, orderByFiledName, orderBy, pageIndex, pageSize, byLike);
    }

    @Override
    public Catering getCateringById(long id) {
        Catering catering = resourcesDao.getEntityById(Catering.class, id);
        if(catering == null || catering.getDel()){
           return null;
        }
        return catering;
    }

    @Override
    public boolean isExisted(long id){
       Catering catering =  getCateringById(id);
        if(catering ==null || catering.getDel()){
            return false;
        }
        return true;
    }

    private void checkDel(Catering catering){
        if(catering.getDel())
            throw  new BaseSystemException(ResourcesBizError.ADD_CATERING_DEL);
    }

    private void checkNull(Catering catering){
        if(catering == null){
            throw new BaseSystemException(ResourcesBizError.EMPTY_ENTITY);
        }
    }

    private List<Catering> findCateringByNameAndDestination(String name, Destination destination) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("name", name);
        conditions.put("destination", destination);
        conditions.put("del", Boolean.FALSE);
        return resourcesDao.getEntitiesByFieldList(Catering.class, conditions);
    }



}
