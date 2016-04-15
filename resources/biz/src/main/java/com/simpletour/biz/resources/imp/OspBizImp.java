package com.simpletour.biz.resources.imp;

import com.simpletour.biz.resources.IOspBiz;
import com.simpletour.biz.resources.error.ResourcesBizError;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.resources.IResourcesDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import resources.OfflineServiceProvider;

import java.util.HashMap;
import java.util.List;

/**
 * @Brief :  供应商biz的实现类
 * @Author:  liangfei/liangfei@simpletour.com
 * @Date  :  2016/3/23 20:29
 * @Since :  2.0.0-SNAPSHOT
 * @Remark:  无
 */
@Component
public class OspBizImp implements IOspBiz {

    @Autowired
    private IResourcesDao resourcesDao;

    @Override
    public OfflineServiceProvider addOfflineServiceProvider(OfflineServiceProvider offlineServiceProvider) throws BaseSystemException {
        checkNull(offlineServiceProvider);
        checkDel(offlineServiceProvider);
        List<OfflineServiceProvider> existList = findOspByName(offlineServiceProvider);
        if (existList.size() > 0) {
            throw new BaseSystemException(ResourcesBizError.SAME_NAME_RESOURCE_IS_EXISTING);
        }
        return resourcesDao.save(offlineServiceProvider);
    }

    @Override
    public void deleteOfflineServiceProvider(long id) throws BaseSystemException {
        resourcesDao.removeEntityById(OfflineServiceProvider.class, id);
    }

    @Override
    public OfflineServiceProvider updateOfflineServiceProvider(OfflineServiceProvider offlineServiceProvider) throws BaseSystemException {
        checkNull(offlineServiceProvider);
        if (offlineServiceProvider.getId() == null) {
            throw new BaseSystemException(ResourcesBizError.OSP_NOT_EXIST);
        }
        List<OfflineServiceProvider> existOsps = findOspByName(offlineServiceProvider);
        if(existOsps.size() > 1){
            throw new BaseSystemException(ResourcesBizError.OSP_NAME_MUST_BE_NUIQUE);
        }

        if (existOsps.size() == 1 && !existOsps.get(0).getId().equals(offlineServiceProvider.getId())) {
            throw new BaseSystemException(ResourcesBizError.OSP_NAME_MUST_BE_NUIQUE);
        }
        return resourcesDao.save(offlineServiceProvider);    }

    @Override
    public DomainPage<OfflineServiceProvider> getOfflineServiceProvidersByPage(Boolean isDel, int page, int pageSize) {
        return getOfflineServiceProvidersByConditionPage(null, isDel, page, pageSize);
    }

    @Override
    public DomainPage<OfflineServiceProvider> getOfflineServiceProvidersByConditionPage(String name, Boolean isDel, int page, int pageSize) throws BaseSystemException {
        HashMap<String, Object> kv = new HashMap<String, Object>(2);
        if (name != null && !name.equals(""))
            kv.put("name", name);
        if (isDel != null)
            kv.put("del", Boolean.FALSE);
        if (kv.size() == 0)
            return resourcesDao.getAllEntitiesByPage(OfflineServiceProvider.class, page, pageSize, "id", IBaseDao.SortBy.DESC);

        return resourcesDao.findEntitiesPagesByFieldList(OfflineServiceProvider.class, kv, "id", IBaseDao.SortBy.DESC, page, pageSize);    }

    @Override
    public OfflineServiceProvider getOfflineServiceProviderById(long id) {
        OfflineServiceProvider osp = resourcesDao.getEntityById(OfflineServiceProvider.class, id);
        if(osp ==null || osp.getDel()){
            return null;
        }
        return osp;
    }

    @Override
    public boolean isExisted(long id) {
        OfflineServiceProvider offlineServiceProvider =  getOfflineServiceProviderById(id);
        if(offlineServiceProvider == null||offlineServiceProvider.getDel()){
            return false;
        }
        return true;
    }

    private void checkDel(OfflineServiceProvider offlineServiceProvider) {
        if (offlineServiceProvider.getDel())
            throw new BaseSystemException(ResourcesBizError.ADD_OSP_DEL);
    }

    private void checkNull(OfflineServiceProvider offlineServiceProvider) {
        if (offlineServiceProvider == null)
            throw new BaseSystemException(ResourcesBizError.EMPTY_ENTITY);
    }

    private List<OfflineServiceProvider> findOspByName(OfflineServiceProvider offlineServiceProvider) {
        HashMap<String, Object> conditions = new HashMap<>(2);
        conditions.put("name", offlineServiceProvider.getName());
        conditions.put("del", Boolean.FALSE);
        return resourcesDao.getEntitiesByFieldList(OfflineServiceProvider.class, conditions);
    }

}
