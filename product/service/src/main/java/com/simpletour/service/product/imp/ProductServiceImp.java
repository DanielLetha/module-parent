package com.simpletour.service.product.imp;

import com.simpletour.biz.product.IProductBiz;
import com.simpletour.biz.traveltrans.IBusNoBiz;
import com.simpletour.biz.traveltrans.error.TravelTransportBizError;
import com.simpletour.commons.data.dao.query.condition.Condition;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.product.Product;
import com.simpletour.domain.product.ProductPackage;
import com.simpletour.domain.product.TourismRoute;
import com.simpletour.domain.traveltrans.BusNo;
import com.simpletour.service.product.IProductService;
import com.simpletour.service.product.error.ProductServiceError;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * Created by songfujie on 15/10/22.
 */
@Service
public class ProductServiceImp implements IProductService {

    @Resource
    private IProductBiz productBiz;

    @Resource
    private IStockBiz stockBiz;

    @Resource
    private IBusNoBiz busNoBiz;

    @Resource
    private IProcurementBiz procurementBiz;

    private void verifyAndInitTourismRoute(Product product) {
        List<TourismRoute> tourismRoutes = product.getTourismRouteList();
        for (int i = 0; i < tourismRoutes.size(); ++i) {
            TourismRoute tourismRoute = tourismRoutes.get(i);

            // 填充车次
            if (tourismRoute.getBusNo() == null)
                throw new BaseSystemException(ProductServiceError.EMPTY_ENTITY);
            if (!busNoBiz.isExisted(tourismRoute.getBusNo().getId()))
                throw new BaseSystemException(TravelTransportBizError.BUS_NO_NOT_EXIST);
            if (!busNoBiz.isAvailable(tourismRoute.getBusNo().getId()))
                throw new BaseSystemException(TravelTransportBizError.BUS_NO_HAS_BEEN_STOPED);
            Optional<BusNo> busNo = busNoBiz.findBusNoById(tourismRoute.getBusNo().getId());

            tourismRoute.setBusNo(busNo.get());
            tourismRoute.setRoutes(busNo.get().getDestinations());
        }
    }

    private void verifyAndInitProductPackage(Product product) {
        List<ProductPackage> productPackages = product.getProductPackages();
        for (ProductPackage productPackage : productPackages) {
            if (productPackage == null || productPackage.getProcurement() == null)
                throw new BaseSystemException(ProductServiceError.EMPTY_ENTITY);

            if (!procurementBiz.isExisted(productPackage.getProcurement().getId()))
                throw new BaseSystemException(ResourcesBizError.PROCUREMENT_NOT_EXIST);
            if (!procurementBiz.isAvailable(productPackage.getProcurement().getId()))
                throw new BaseSystemException(ResourcesBizError.PROCUREMENT_IS_OFFLINE);

            Procurement procurement = procurementBiz.getProcurementById(productPackage.getProcurement().getId());
            if (procurement == null)
                throw new BaseSystemException(ResourcesBizError.PROCUREMENT_NOT_EXIST);
            productPackage.setProcurement(procurement);
            productPackage.setDestination(procurement.getDestination());
        }
    }

    private void verifyAndInitProduct(Product product) {
        if (product == null)
            throw new BaseSystemException(ProductServiceError.EMPTY_ENTITY);
        if (product.getTourismRouteList() != null && !product.getTourismRouteList().isEmpty())
            verifyAndInitTourismRoute(product);
        if (product.getProductPackages() != null && !product.getProductPackages().isEmpty())
            verifyAndInitProductPackage(product);
    }

    public DomainPage<Product> getProductByConditionPage(Condition conditions, int page, int pageSize){
        return productBiz.getProductByConditionPage(conditions, page, pageSize);
    }

    public List<Product> getProductByCondition(Condition conditions) {
        return productBiz.getProductByCondition(conditions);
    }

    public Optional<Product> addProduct(Product product) {
        verifyAndInitProduct(product);
        return Optional.of(productBiz.addProduct(product));
    }

    public void deleteProductById(Long id) {
        productBiz.deleteProductById(id);
    }

    public Optional<Product> updateProduct(Product product) throws BaseSystemException {
        verifyAndInitProduct(product);
        return Optional.of(productBiz.updateProduct(product));
    }

    public Optional<Product> getProductById(Long id) {
        return Optional.of(productBiz.getProductById(id));
    }

    public DomainPage<Product> getProductByConditionsPage(Map<String, Object> conditions, int page, int pageSize) {
        return productBiz.getProductByConditionsPage(conditions, page, pageSize);
    }

    public DomainPage<Product> getProductByConditionsPage(Boolean isOnline, String name, Boolean isDel, int page, int pageSize) {
        return productBiz.getProductByConditionsPage(isOnline, name, isDel, page, pageSize);
    }


    // stock
    public Integer findTourismBusNoPlanCapacity(List<TourismRoute> tourismRoutes, Date day) {
        return productBiz.findTourismBusNoPlanCapacity(tourismRoutes, day);
    }

    @Override
    public Map<Date, Integer> findTourismBusNoPlanCapacity(List<TourismRoute> tourismRoutes, Date start, Date end) {
        return productBiz.findTourismBusNoPlanCapacity(tourismRoutes, start, end);
    }

    public Integer findTourismBusNoPlanCapacity(Product product, Date day) {
        if ( product == null || day == null)
            return 0;
        if (!product.containBus())
            throw new BaseSystemException(ProductServiceError.PRODUCT_NOT_COTAIN_BUS);
        return findTourismBusNoPlanCapacity(product.getTourismRouteList(), day);
    }

    @Override
    public Map<Date, Integer> findTourismBusNoPlanCapacity(Product product, Date start, Date end) {
        if(product==null||start==null||end==null) return Collections.emptyMap();
        if (!product.containBus())
            throw new BaseSystemException(ProductServiceError.PRODUCT_NOT_COTAIN_BUS);
        return findTourismBusNoPlanCapacity(product.getTourismRouteList(), start, end);
    }

    public Integer findTourismBusNoPlanCapacity(Long productId, Date day) {
        Product product = productBiz.getProductById(productId);
        if (product == null)
            return 0;
        if (!product.containBus())
            throw new BaseSystemException(ProductServiceError.PRODUCT_NOT_COTAIN_BUS);
        return findTourismBusNoPlanCapacity(product, day);
    }

    @Override
    public Map<Date, Integer> findTourismBusNoPlanCapacity(Long productId, Date start, Date end) {
        Product product = productBiz.getProductById(productId);
        if(product==null||start==null||end==null) return Collections.emptyMap();
        if (!product.containBus())
            throw new BaseSystemException(ProductServiceError.PRODUCT_NOT_COTAIN_BUS);
        return findTourismBusNoPlanCapacity(product.getTourismRouteList(), start, end);
    }

    public Optional<Stock> getStock(Product product, Date day, Boolean isOnline) {
        if (isOnline != null && isOnline == Boolean.FALSE)
            return Optional.empty();
        Optional<Stock> stock = stockBiz.getStock(product, day, isOnline);
        if (!product.containBus())
            return stock;

        if(stock.isPresent() && stock.get().getInventoryType() == InventoryType.product){
            Integer busCapacity = productBiz.findTourismBusNoPlanCapacity(product, day);
            if(stock.get().getAvailableQuantity() > busCapacity){
                stock.get().setAvailableQuantity(busCapacity);
            }
        }
        return stock;
    }

    public List<Stock> getStocks(Product product, Date startDay, Date endDay, Boolean isOnline){
        if (isOnline != null && isOnline == Boolean.FALSE)
            return Collections.emptyList();

        List<Stock> stocks = stockBiz.getStocks(product, startDay, endDay, isOnline);
        if (!product.containBus())
            return stocks;

        Map<Date, Integer> tourismBusNoCapacitiesMap = productBiz.findTourismBusNoPlanCapacity(product.getTourismRouteList(), startDay, endDay);

        for (Stock stock : stocks) {
            Integer busCapacity = tourismBusNoCapacitiesMap.get(stock.getDay());
            if(stock.getAvailableQuantity() > busCapacity){
                stock.setAvailableQuantity(busCapacity);
            }
        }
        return stocks;
    }
}
