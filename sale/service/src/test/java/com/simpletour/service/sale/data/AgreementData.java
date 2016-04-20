package com.simpletour.service.sale.data;

import com.simpletour.commons.test.generator.AbstractDataGenerator;
import com.simpletour.dao.sale.IAgreementDao;
import com.simpletour.domain.sale.Agreement;
import com.simpletour.domain.sale.SaleApp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YuanYuan/yuanyuan@simpletour.com on 2016/4/20.
 *
 * @since 2.0-SNAPSHOT
 */
public class AgreementData extends AbstractDataGenerator {
    private IAgreementDao agreementDao;
    private List<SaleApp> appList = new ArrayList<>();

    public AgreementData(IAgreementDao agreementDao) {
        this.agreementDao = agreementDao;
    }

    @Override
    public void generator() {
        Agreement agreement1 = generateAgreement("saleApp1");
        domains.add(agreementDao.save(agreement1));

        Agreement agreement2 = generateAgreement("saleApp2");
        agreement2.setEnabled(false);
        domains.add(agreementDao.save(agreement2));
    }

    public Agreement generateAgreement(String appName) {
        SaleApp saleApp = new SaleApp(generateName(appName), generateName("key"), generateName("secret")
                , generateName("contact"), generateName("mobile"), generateName("fax")
                , generateName("email"), generateName("link"), 30, generateName("remark"));
        saleApp = agreementDao.save(saleApp);
        appList.add(saleApp);
        return new Agreement(saleApp, true, generateName("remark"));
    }

    public void clear() {
        domains.forEach(agreementDao::remove);
        appList.forEach(agreementDao::remove);
    }
}
