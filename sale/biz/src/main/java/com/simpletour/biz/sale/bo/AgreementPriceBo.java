package com.simpletour.biz.sale.bo;

import com.simpletour.domain.sale.AgreementProduct;
import com.simpletour.domain.sale.AgreementProductPrice;
import com.simpletour.domain.sale.AgreementProductPrice.Type;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Brief :  ${用途}
 * @Author: liangfei/liangfei@simpletour.com
 * @Date :  2016/4/22 18:14
 * @Since ： ${VERSION}
 * @Remark: ${Remark}
 */
public class AgreementPriceBo {

    private AgreementProduct agreementProduct;

    private Date date;

    private Map<Type, Price> priceMap;

    public AgreementPriceBo() {
    }

    public AgreementPriceBo(AgreementProductPriceGroupKey groupKey, List<AgreementProductPrice> prices) {
        this(groupKey.getAgreementProduct(), groupKey.getDay(), prices);
    }

    public AgreementPriceBo(AgreementProduct agreementProduct, Date date, List<AgreementProductPrice> prices) {
        this.agreementProduct = agreementProduct;
        this.date = date;
        if (prices != null && prices.size() == 2) {
            this.priceMap = prices.stream().collect(Collectors.toMap(AgreementProductPrice::getType, p -> new Price(p.getId(), p.getCost(), p.getSettlement(), p.getRetail(), p.getVersion())));
        }
    }

    public AgreementProduct getAgreementProduct() {
        return agreementProduct;
    }

    public void setAgreementProduct(AgreementProduct agreementProduct) {
        this.agreementProduct = agreementProduct;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Map<Type, Price> getPriceMap() {
        return priceMap;
    }

    public void setPriceMap(Map<Type, Price> priceMap) {
        this.priceMap = priceMap;
    }


    public List<AgreementProductPrice> asList() {
        return priceMap.keySet().stream().map(type -> new AgreementProductPrice(agreementProduct, type, date, priceMap.get(type).getCost(), priceMap.get(type).getSettlement(), priceMap.get(type).getRetail(), priceMap.get(type).getVersion())).collect(Collectors.toList());
    }

    public static List<AgreementPriceBo> from(List<AgreementProductPrice> prices) {
        if (!(prices == null || prices.isEmpty())) {
            Map<AgreementProductPriceGroupKey, List<AgreementProductPrice>> groupedPriceMap =
                    prices.stream().collect(Collectors.groupingBy(price -> new AgreementProductPriceGroupKey(price.getAgreementProduct(), price.getDate())));
            return groupedPriceMap.keySet().stream().map(groupKey -> new AgreementPriceBo(groupKey, groupedPriceMap.get(groupKey))).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private static class AgreementProductPriceGroupKey {


        private AgreementProduct agreementProduct;

        private Date day;

        public AgreementProductPriceGroupKey(AgreementProduct agreementProduct, Date day) {
            this.agreementProduct = agreementProduct;
            this.day = day;
        }

        public AgreementProduct getAgreementProduct() {
            return agreementProduct;
        }

        public void setAgreementProduct(AgreementProduct agreementProduct) {
            this.agreementProduct = agreementProduct;
        }

        public Date getDay() {
            return day;
        }

        public void setDay(Date day) {
            this.day = day;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AgreementProductPriceGroupKey that = (AgreementProductPriceGroupKey) o;

            if (!agreementProduct.equals(that.agreementProduct)) return false;
            return day.equals(that.day);

        }

        @Override
        public int hashCode() {
            int result = agreementProduct.hashCode();
            result = 31 * result + day.hashCode();
            return result;
        }
    }

}
