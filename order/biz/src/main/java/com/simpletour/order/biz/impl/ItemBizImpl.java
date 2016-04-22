package com.simpletour.order.biz.impl;

import com.simpletour.biz.product.IProductBiz;
import com.simpletour.biz.product.imp.TourismBusAllocation;
import com.simpletour.biz.sale.IAgreementProductBiz;
import com.simpletour.biz.sale.IAgreementProductPriceBiz;
import com.simpletour.biz.sale.ISaleAppBiz;
import com.simpletour.commons.data.dao.jpa.JPABaseDAO;
import com.simpletour.commons.data.dao.query.ConditionOrderByQuery;
import com.simpletour.commons.data.dao.query.condition.AndConditionSet;
import com.simpletour.commons.data.dao.query.condition.Condition;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.order.Customer;
import com.simpletour.domain.order.Item;
import com.simpletour.domain.product.Product;
import com.simpletour.domain.sale.AgreementProduct;
import com.simpletour.domain.sale.AgreementProductPrice;
import com.simpletour.domain.traveltrans.BusNo;
import com.simpletour.order.biz.IItemBiz;
import com.simpletour.order.biz.error.OrderBizError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Mario on 2016/4/20.
 */
@Repository
public class ItemBizImpl extends JPABaseDAO implements IItemBiz {

    @Autowired
    private IAgreementProductBiz agreementProductBiz;

    @Autowired
    private IProductBiz productBiz;

    @Autowired
    private IAgreementProductPriceBiz agreementProductPriceBiz;

    /**
     * 检查订单项的基本信息是否完整,需要校验的基本信息为
     * 1.联系人姓名
     * 2.联系人电话
     * 3.关联产品id
     * 4.产品使用日期
     * 5.订购数量
     * 6.出行人数量
     *
     * @param item
     * @remark 其中不检查产品名称、类型,该值由业务代码直接赋值,不允许用户自己对订单关联的产品基本信息赋值
     */
    private void validateItemBasic(Item item) {
        if (item == null)
            throw new BaseSystemException(OrderBizError.ORDER_ITEM_NULL);
        if (item.getName() == null || item.getName().isEmpty() || item.getMobile() == null || item.getMobile().isEmpty() ||
                item.getProductId() == null || item.getDay() == null || item.getQuantity() == null)
            throw new BaseSystemException(OrderBizError.ORDER_ITEM_BASIC_NOT_ENOUGH);
        if (item.getCustomers() == null || item.getCustomers().isEmpty())
            throw new BaseSystemException(OrderBizError.ORDER_ITEM_CUSTOMER_NULL);
    }

    /**
     * 检查订单关联的销售端基本信息:
     * 1.检查订单关联的产品是否是在销售端销售范围内
     * 2.检查订单下单时的失效时间是否小于销售端所关联产品设定的失效时间
     *
     * @param item
     */
    private void validateItemRelatedSaleApp(Item item) {
        //检查订单关联的产品是否在销售的范围内
        ConditionOrderByQuery conditionOrderByQuery = new ConditionOrderByQuery();
        AndConditionSet andConditionSet = new AndConditionSet();
        andConditionSet.addCondition("product.id", item.getProductId(), Condition.MatchType.eq);
        andConditionSet.addCondition("agreement.saleApp.id", item.getSaleApp().getId(), Condition.MatchType.eq);
        conditionOrderByQuery.setCondition(andConditionSet);
        List<AgreementProduct> agreementProducts = agreementProductBiz.findAgreementProductListByConditions(conditionOrderByQuery);
        if (agreementProducts == null || agreementProducts.isEmpty())
            throw new BaseSystemException(OrderBizError.ORDER_ITEM_SALEAPP_PRODUCT_NOT_EXISTED);
        //检查订单下单时间是否是在销售产品失效时间之内
        if ((item.getDay().getTime() - agreementProducts.get(0).getDeadline().longValue()) < System.currentTimeMillis())
            throw new BaseSystemException(OrderBizError.ORDER_ITEM_SALEAPP_EXCEED_PRODUCT_DEADLINE);
    }

    /**
     * 1.检查订单项关联的产品并设置订单项中产品的基本信息,保证录入的产品信息与数据库中的信息一致
     * 2.检查产品对应的车次或者元素是否存在,保证数据的存在性
     *
     * @param item
     */
    private void validateItemProduct(Item item) {
        //检验产品是否存在
        Product originProduct = productBiz.getProductById(item.getProductId());
        if (originProduct == null)
            throw new BaseSystemException(OrderBizError.ORDER_ITEM_PRODUCT_NOT_EXISTED);
        //设置订单项产品基本信息
        item.setProductName(originProduct.getName());
        item.setProductType(originProduct.getType());
        if (originProduct.containBus()) {
            if ((originProduct.getTourismRouteList() == null || originProduct.getTourismRouteList().isEmpty()) ||
                    !originProduct.getTourismRouteList().stream().allMatch(tourismRoute -> tourismRoute.getBusNo() != null && tourismRoute.getBusNo().getStatus().equals(BusNo.Status.normal)))
                throw new BaseSystemException(OrderBizError.ORDER_ITEM_PRODUCT_BUS_NOT_EXSITED);
            if (!originProduct.getType().equals(Product.Type.bus.name())) {
                if (originProduct.getProductPackages() == null || originProduct.getProductPackages().isEmpty() ||
                        !originProduct.getProductPackages().stream().allMatch(productPackage -> productPackage.getProcurement() != null && productPackage.getProcurement().getOnline()))
                    throw new BaseSystemException(OrderBizError.ORDER_ITEM_PRODUCT_PROCUREMENT_NOT_EXISTED);
            }
        } else {
            if (originProduct.getProductPackages() == null || originProduct.getProductPackages().isEmpty() ||
                    !originProduct.getProductPackages().stream().allMatch(productPackage -> productPackage.getProcurement() != null && productPackage.getProcurement().getOnline()))
                throw new BaseSystemException(OrderBizError.ORDER_ITEM_PRODUCT_PROCUREMENT_NOT_EXISTED);
        }
    }

    /**
     * 设置协议价格
     *
     * @param item
     */
    private void setSaleProductPrice(Item item) {
        ConditionOrderByQuery conditionOrderByQuery = new ConditionOrderByQuery();
        AndConditionSet andConditionSet = new AndConditionSet();
        andConditionSet.addCondition("product.id", item.getProductId(), Condition.MatchType.eq);
        andConditionSet.addCondition("agreement.saleApp.id", item.getSaleApp().getId(), Condition.MatchType.eq);
        conditionOrderByQuery.setCondition(andConditionSet);
        List<AgreementProductPrice> agreementProductPrices = agreementProductPriceBiz.getAgreementProductPriceList(conditionOrderByQuery);
        if (agreementProductPrices == null || agreementProductPrices.isEmpty())
            throw new BaseSystemException(OrderBizError.ORDER_ITEM_SALEAPP_PRODUCT_PRICE_NULL);
        if (agreementProductPrices.size() > 2)
            throw new BaseSystemException(OrderBizError.ORDER_ITEM_SALEAPP_PRODUCT_PRICE_LIST_SIZE_EXCEPTION);
        //设置订单的成本价,结算价(单价和总价)
        //TODO...需要明确产品协议的具体需求,目前产品协议只支持三种协议:1.没有协议2.只有成人售卖协议3.成人+儿童售卖协议...
        Long adultCount = item.getCustomers().stream().filter(customer -> customer.getType().equals(Customer.Type.ADULT)).count();
        Long childCount = item.getCustomers().stream().filter(customer -> customer.getType().equals(Customer.Type.CHILD)).count();
        List<AgreementProductPrice> agreementProductPricesAdult = agreementProductPrices.stream()
                .filter(agreementProductPrice -> agreementProductPrice.getType().equals(AgreementProductPrice.Type.ADULT)).collect(Collectors.toList());
        if (!(agreementProductPricesAdult == null || agreementProductPricesAdult.isEmpty())) {
            item.setAdultCost(agreementProductPricesAdult.get(0).getCost());
            item.setAdultSettlement(item.getAdultCost() * adultCount.intValue());
        }
        List<AgreementProductPrice> agreementProductPricesChild = agreementProductPrices.stream()
                .filter(agreementProductPrice -> agreementProductPrice.getType().equals(AgreementProductPrice.Type.CHILD)).collect(Collectors.toList());
        if (!(agreementProductPricesChild == null || agreementProductPricesChild.isEmpty())) {
            item.setChildCost(agreementProductPricesChild.get(0).getCost());
            item.setChildSettlement(item.getChildCost() * childCount.intValue());
        }
    }

    /**
     * 对新添加的产品类型包含了车次的订单项进行排车的验证,保证新添加的订单项关联的出行人能够在该行程上能够排到车
     *
     * @param item
     */
    //TODO...
    private void validateIsBusPlanAvailable(Item item) {
        if (item.getId() == null) {
            Integer customerNum = item.getCustomers().size();
            List<Customer> customers = item.getCustomers().stream()
                    .map(customer -> new Customer(item.getOrder(), item, customer.getType(), customer.getName(), customer.getMobile(), customer.getIdType(), customer.getIdNo(), customer.getEmail()))
                    .collect(Collectors.toList());
            Product originProduct = productBiz.getProductById(item.getProductId());
            if (originProduct.containBus()) {
            }
        }
    }

    @Override
    public Item validateItem(Item item) {
        validateItemBasic(item);
        validateItemRelatedSaleApp(item);
        validateItemProduct(item);
        setSaleProductPrice(item);
        validateIsBusPlanAvailable(item);
        return item;
    }
}
