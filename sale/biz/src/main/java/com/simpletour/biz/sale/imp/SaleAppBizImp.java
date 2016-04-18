package com.simpletour.biz.sale.imp;


import com.simpletour.biz.sale.ISaleAppBiz;
import com.simpletour.biz.sale.error.SaleAppBizError;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.sale.ISaleAppDao;
import com.simpletour.domain.sale.SaleApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Brief : 销售端biz的实现类
 * @Author: liangfei/liangfei@simpletour.com
 * @Date :  2016/4/6 20:03
 * @Since ： ${VERSION}
 * @Remark: ${Remark}
 */
@Component
public class SaleAppBizImp implements ISaleAppBiz {

    @Autowired
    private ISaleAppDao saleAppDao;


    @Override
    public SaleApp addSaleApp(SaleApp saleApp) throws BaseSystemException {
        checkNull(saleApp);
        checkDel(saleApp);
        List<SaleApp> saleAppList = findSaleAppByName(saleApp);
        if (!saleAppList.isEmpty() || saleAppList.size() > 0) {
            throw new BaseSystemException(SaleAppBizError.SALE_APP_NAME_MUST_BE_UNIQUE);
        }
        checkKeyUnique(saleApp);
        checkSecretUnique(saleApp);
        return saleAppDao.save(saleApp);
    }

    @Override
    public SaleApp updateSaleApp(SaleApp saleApp) throws BaseSystemException {
        checkNull(saleApp);
        checkDel(saleApp);
        checkKeyAndSecretNull(saleApp);
        checkKeyAndSecretSame(saleApp);
        List<SaleApp> saleAppList = findSaleAppByName(saleApp);
        if (saleAppList.size() > 1) {
            throw new BaseSystemException(SaleAppBizError.SALE_APP_NAME_MUST_BE_UNIQUE);
        }
        if (saleAppList.size() == 1 && !saleAppList.get(0).getId().equals(saleApp.getId())) {
            throw new BaseSystemException(SaleAppBizError.SALE_APP_NAME_MUST_BE_UNIQUE);
        }
        return saleAppDao.save(saleApp);
    }

    @Override
    public void deleteSaleApp(long id, boolean pseudo) throws BaseSystemException {
        saleAppDao.removeEntityById(SaleApp.class, id);
    }

    @Override
    public SaleApp findSaleAppById(long id) {
        SaleApp saleApp = saleAppDao.getEntityById(SaleApp.class, id);
        if (saleApp == null || saleApp.getDel()) {
            return null;
        }
        return saleApp;

    }

    @Override
    public DomainPage<SaleApp> querySaleAppByCondition(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize, boolean byLike) {
        return saleAppDao.queryEntitiesPagesByFieldList(SaleApp.class, conditions, orderByFiledName, orderBy, pageIndex, pageSize, byLike);
    }

    @Override
    public boolean isExist(long id) {
        SaleApp saleApp = findSaleAppById(id);
        if (saleApp == null || saleApp.getDel()) {
            return false;
        }
        return true;
    }

    private void checkNull(SaleApp saleApp) {
        if (saleApp == null)
            throw new BaseSystemException(SaleAppBizError.SALE_APP_EMPTY);
    }

    private void checkDel(SaleApp saleApp) {
        if (saleApp.getDel())
            throw new BaseSystemException(SaleAppBizError.SAL_APP_DEL);
    }

    private List<SaleApp> findSaleAppByName(SaleApp saleApp) {
        Map condition = new HashMap<>();
        condition.put("name", saleApp.getName());
        condition.put("del", Boolean.FALSE);
        return saleAppDao.getEntitiesByFieldList(SaleApp.class, condition);

    }

    private void checkKeyUnique(SaleApp saleApp) {
        Map condition = new HashMap<>();
        condition.put("key", saleApp.getKey());
        condition.put("del", Boolean.FALSE);
        List<SaleApp> saleAppList = saleAppDao.getEntitiesByFieldList(SaleApp.class, condition);
        if (!saleAppList.isEmpty() || saleAppList.size() > 0)
            throw new BaseSystemException(SaleAppBizError.SALE_APP_KEY_MUST_BE_UNIQUE);
    }

    private void checkSecretUnique(SaleApp saleApp) {
        Map condition = new HashMap<>();
        condition.put("secret", saleApp.getSecret());
        condition.put("del", Boolean.FALSE);
        List<SaleApp> saleAppList = saleAppDao.getEntitiesByFieldList(SaleApp.class, condition);
        if (!saleAppList.isEmpty() || saleAppList.size() > 0)
            throw new BaseSystemException(SaleAppBizError.SALE_APP_SECRET_MUST_BE_UNIQUE);
    }

    private void checkKeyAndSecretNull(SaleApp saleApp) {
        if (saleApp.getKey() == null) {
            throw new BaseSystemException(SaleAppBizError.SALE_APP_KEY_MUST_NOT_NULL);
        }
        if (saleApp.getSecret() == null) {
            throw new BaseSystemException(SaleAppBizError.SALE_APP_SECRET_MUST_NOT_NULL);
        }
    }

    private void checkKeyAndSecretSame(SaleApp saleApp) {
        SaleApp saleApp1 = saleAppDao.getEntityById(SaleApp.class, saleApp.getId());
        if (!(saleApp1.getKey().equals(saleApp.getKey()) && saleApp1.getSecret().equals(saleApp.getSecret()))) {
            throw new BaseSystemException(SaleAppBizError.SALE_APP_KEY_SECRET_NOT_SAME);

        }
    }

}
