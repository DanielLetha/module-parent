package com.simpletour.service.sale.imp;

import com.simpletour.biz.sale.ISaleAppBiz;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.sale.SaleApp;
import com.simpletour.service.sale.ISaleAppService;
import com.simpletour.service.sale.error.SaleAppServiceError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @Brief :  销售端服务的实现类
 * @Author: liangfei/liangfei@simpletour.com
 * @Date :  2016/4/7 16:55
 * @Since ： ${VERSION}
 * @Remark: ${Remark}
 */
@Service
public class SaleAppServiceImp implements ISaleAppService {

    @Autowired
    private ISaleAppBiz saleAppBiz;

    @Override
    public Optional<SaleApp> addSaleApp(SaleApp saleApp) {
        checkNotNull(saleApp);
        checkNotDel(saleApp);
        //设置key和secret的值(仅添加时需要)
        saleApp.setKey(getKeyString());
        saleApp.setSecret(getSecretString());
        return Optional.ofNullable(saleAppBiz.addSaleApp(saleApp));
    }

    @Override
    public void deleteSaleApp(Long id) throws BaseSystemException {
        if (id == null || id <= 0L)
            throw new BaseSystemException(SaleAppServiceError.SALE_APP_ID_NULL);

        Optional<SaleApp> saleApp = Optional.ofNullable(saleAppBiz.findSaleAppById(id));
        if (!saleApp.isPresent())
            throw new BaseSystemException(SaleAppServiceError.SALE_APP_NOT_EXIST);
        //TODO 处理对于协议的依赖性检查
        saleAppBiz.deleteSaleApp(id, true);

    }

    @Override
    public Optional<SaleApp> updateSaleApp(SaleApp saleApp) {
        checkNotNull(saleApp);
        checkNotDel(saleApp);
        Optional<SaleApp> saleAppOptional = Optional.ofNullable(saleAppBiz.findSaleAppById(saleApp.getId()));
        if (!saleAppOptional.isPresent())
            throw new BaseSystemException(SaleAppServiceError.SALE_APP_NOT_EXIST);
        return Optional.of(saleAppBiz.updateSaleApp(saleApp));

    }

    @Override
    public DomainPage<SaleApp> querySaleAppsPagesByConditions(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize, boolean byLike) {
        return saleAppBiz.querySaleAppByCondition(conditions, orderByFiledName, orderBy, pageIndex, pageSize, byLike);
    }

    @Override
    public Optional<SaleApp> getSaleAppById(long id) {
        return Optional.ofNullable(saleAppBiz.findSaleAppById(id));
    }

    private void checkNotNull(SaleApp saleApp) {
        if (saleApp == null)
            throw new BaseSystemException(SaleAppServiceError.SALE_APP_EMPTY);
    }

    private void checkNotDel(SaleApp saleApp) {
        if (saleApp.getDel())
            throw new BaseSystemException(SaleAppServiceError.SALE_APP_DEL);
    }
    /**
     * 生成key随机字符串的方法
     */
    private String getKeyString(){
        String s = UUID.randomUUID().toString();
        return s.replace("-","");
    }
    /**
     * 生成secret随机字符串的方法
     */
    private String getSecretString(){
        String s = UUID.randomUUID().toString();
        return s.replace("-","");
    }

}
