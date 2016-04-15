package com.simpletour.biz.product.imp;

import com.simpletour.biz.inventory.IStockBiz;
import com.simpletour.biz.product.IProductBiz;
import com.simpletour.biz.product.error.ProductBizError;

import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.dao.query.condition.Condition;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.commons.data.util.TypeConverter;
import com.simpletour.dao.product.IProductDao;
import com.simpletour.dao.traveltrans.ITransportDao;
import com.simpletour.domain.inventory.InventoryType;
import com.simpletour.domain.product.Product;
import com.simpletour.domain.product.ProductPackage;
import com.simpletour.domain.product.TourismRoute;
import com.simpletour.domain.product.TourismRouteLine;
import com.simpletour.domain.resources.Procurement;
import com.simpletour.domain.traveltrans.BusNo;
import com.simpletour.domain.traveltrans.BusNoPlan;
import com.simpletour.domain.traveltrans.Line;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Product 业务模块
 * Created by Jeff.Song on 2015/11/21.
 */
@Component
public class ProductBizImp implements IProductBiz {

    @Resource
    private IProductDao productDao;

    @Resource
    private ITransportDao transportDao;

    @Resource
    private IOrderDao orderDao;

    @Resource
    private IStockBiz stockBiz;

    public boolean isExist(Long id) {
        Product product = getProductById(id);
        if (product == null || product.getDel())
            return false;
        return true;
    }

    public boolean isOnline(Long id) {
        Product product = getProductById(id);
        if (product == null || product.getDel())
            return false;
        return product.getOnline();
    }

    private void verifyTourismTime(Product product) {
        if (product.getDepartTime() == null || product.getArriveTime() == null)
            throw new BaseSystemException(ProductBizError.TIME_FORMAT_ERROR);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        try {
            Date dateDepart = simpleDateFormat.parse(product.getDepartTime());
            Date dateArrive = simpleDateFormat.parse(product.getArriveTime());
            if (dateDepart.after(dateArrive))
                throw new BaseSystemException(ProductBizError.DEPART_TIME_IS_AFTER_ARRIVE_TIME);
        } catch (ParseException e) {
            throw new BaseSystemException(ProductBizError.TIME_FORMAT_ERROR);
        }
    }

    private void verifyAndInitTourismRoute(Product product) {
        List<TourismRoute> tourismRoutes = product.getTourismRouteList();
        int lastOffset = Integer.MIN_VALUE;
        BusNo lastBusNo = null;
        for (int i = 0; i < tourismRoutes.size(); ++i) {
            TourismRoute tourismRoute = tourismRoutes.get(i);
            tourismRoute.setProduct(product);

            // 天数递增
            if (lastOffset > tourismRoute.getOffset())
                throw new BaseSystemException(ProductBizError.TOURISM_ROUTES_DAY_MUST_BE_NON_DECREASING);

            // 相邻两车次不能相同
            if (lastBusNo != null && tourismRoute.getBusNo().getId().longValue() == lastBusNo.getId().longValue())
                throw new BaseSystemException(ProductBizError.TOURISM_ROUTES_BUS_NO_DUPLICATE);

            // 车次时间不能冲突
            if (lastOffset == tourismRoute.getOffset()) {    // 条件若能成立，则必定i>0
                if (lastBusNo.getArriveTime() > tourismRoute.getBusNo().getDepartTime())
                    throw new BaseSystemException(ProductBizError.TOURISM_ROUTES_BUS_NO_TIME_CONFLICT);
            }

            lastOffset = tourismRoute.getOffset();
            lastBusNo = tourismRoute.getBusNo();
        }
    }

    private Product.Type resourceTypeToProductType(Procurement.ResourceType in) {
        switch (in) {
            case catering:
                return Product.Type.catering;
            case hotel:
                return Product.Type.hotel;
            case scenic:
                return Product.Type.scenic;
            case entertainment:
                return Product.Type.entertainment;
            default:
                return null;
        }
    }

    private Set<Product.Type> getTypeSet(Product product) {
        Set<Product.Type> typeSet = new HashSet<>();
        if (product.getProductPackages() != null && !product.getProductPackages().isEmpty()) {
            product.getProductPackages().forEach(pp -> {
                typeSet.add(resourceTypeToProductType(pp.getProcurement().getResourceType()));
            });
        }

        if (product.getTourismRouteList() != null && !product.getTourismRouteList().isEmpty()) {
            typeSet.add(Product.Type.bus);
        }
        return typeSet;
    }

    private boolean determinProductType(Product product, Set<Product.Type> typeSet) {
        if (typeSet.contains(null))
            return false;

        if (typeSet.size() == 1) {
            Product.Type type = typeSet.iterator().next();
            product.setType(type.name());
            return true;
        }
        if (typeSet.size() > 1 && typeSet.contains(Product.Type.bus)) {
            product.setType(StringTools.joinCollection(typeSet, ","));
            return true;
        }
        return false;
    }

    private void determinProductType(Product product) {
        Set<Product.Type> typeSet = getTypeSet(product);
        if (!determinProductType(product, typeSet))
            throw new BaseSystemException(ProductBizError.PRODUCT_TYPE_DIS_MATCH);
    }

    private void verifyAndInitTourism(Product product) {
        verifyTourismTime(product);
        verifyAndInitTourismRoute(product);
    }

    private Integer getProductMaxDay(Product product) {
        // 计算行程最大天数
        Optional<TourismRoute> opMaxOffsetTourismRoute = null;
        if (product.getTourismRouteList() != null && !product.getTourismRouteList().isEmpty()) {
            opMaxOffsetTourismRoute = product.getTourismRouteList().stream().max((o1, o2) -> o1.getOffset().compareTo(o2.getOffset()));
        }

        // 计算产品最大天数
        Optional<ProductPackage> opMaxOffsetPP = null;
        if (product.getProductPackages() != null && !product.getProductPackages().isEmpty()) {
            opMaxOffsetPP = product.getProductPackages().stream().max((a, b) -> a.getOffset().compareTo(b.getOffset()));
        }

        // 行程和产品最大天数取最大值
        int maxDay = 0;
        if (opMaxOffsetTourismRoute != null && opMaxOffsetTourismRoute.isPresent())
            maxDay = opMaxOffsetTourismRoute.get().getOffset() + 1;

        if (opMaxOffsetPP != null && opMaxOffsetPP.isPresent())
            if (opMaxOffsetPP.get().getOffset() + 1 > maxDay)
                maxDay = opMaxOffsetPP.get().getOffset() + 1;
        return maxDay;
    }

    public DomainPage<Product> getProductByConditionPage(Condition conditions, int page, int pageSize) {
        if (conditions == null)
            return productDao.getAllEntitiesByPage(Product.class, page, pageSize, "id", IBaseDao.SortBy.DESC);

        DomainPage<Product> res = productDao.findProductByConditionPage(conditions, page, pageSize);
        return res;
    }

    public List<Product> getProductByCondition(Condition conditions) {
        if (conditions == null)
            return productDao.getAllEntities(Product.class);
        return productDao.findProductByCondition(conditions);
    }

    private List<Product> getProductsByName(String name) {
        HashMap<String, Object> conditions = new HashMap<>(1);
        conditions.put("name", name);
        conditions.put("del", false);
        return productDao.getEntitiesByFieldList(Product.class, conditions);
    }

    private void productUniqueVerifyOnAdded(Product product) {
        List<Product> products = getProductsByName(product.getName());
        if (products.size() > 0)
            throw new BaseSystemException(ProductBizError.PRODUCT_NAME_EXIST);
    }

    private void productUniqueVerifyOnUpdate(Product product) {
        List<Product> products = getProductsByName(product.getName());
        if (products.size() == 0)
            return;

        if (products.size() == 1 && products.get(0).getId().longValue() != product.getId().longValue())
            throw new BaseSystemException(ProductBizError.PRODUCT_NAME_EXIST);

        if (products.size() > 1) {
            BaseSystemException exception = new BaseSystemException(ProductBizError.DATA_ERROR_IN_DB);
            exception.setExtMessage("Exist more than one product with name:" + product.getName());
            throw exception;
        }
    }

    private void verifyAndInitProduct(Product product) {
        if ((product.getTourismRouteList() == null || product.getTourismRouteList().isEmpty())
                && (product.getProductPackages() == null || product.getProductPackages().isEmpty()))
            throw new BaseSystemException(ProductBizError.PRODUCT_MUST_CONTAINS_ATLEAST_ONE_BUS_OR_PROCUREMENT);

        // 当产品包含行程时
        if (product.getTourismRouteList() != null && !product.getTourismRouteList().isEmpty()) {
            verifyAndInitTourism(product);

            // 行程分解到线路
            List<Line> lines = transportDao.getAllEntities(Line.class);
            List<TourismRoute> composit = TourismRoute.decompose(lines, product.getTourismRouteList());
            if (composit == null || composit.isEmpty())
                throw new BaseSystemException(ProductBizError.DECOMPOSE_ROUTE_TO_LINE_FAIL);
        }

        if (product.getProductPackages() != null && !product.getProductPackages().isEmpty()) {
            verifyAndInitProductPackages(product, product.getProductPackages());
        }

        product.setDays(getProductMaxDay(product));

        // 根据productPackages 和 tourismRouteList决定产品类型
        determinProductType(product);

    }

    public Product addProduct(Product product) {
        if (product == null)
            throw new BaseSystemException(ProductBizError.EMPTY_ENTITY);

        productUniqueVerifyOnAdded(product);

        verifyAndInitProduct(product);

        return productDao.save(product);
    }

    public void deleteProductById(Long id) {
        productDao.removeEntityById(Product.class, id);
        stockBiz.deleteStocksByInventoryTypeId(InventoryType.product, id);
    }

    public Product updateProduct(Product product) throws BaseSystemException {
        if (product == null || product.getId() == null)
            throw new BaseSystemException(ProductBizError.EMPTY_ENTITY);

        productUniqueVerifyOnUpdate(product);

        verifyAndInitProduct(product);

        return productDao.save(product);
    }

    public Product getProductById(Long id) {
        Product product = productDao.getEntityById(Product.class, id);
        if (product == null || product.getDel()) {
            return null;
        }
        return product;
    }

    public DomainPage<Product> getProductByConditionsPage(Map<String, Object> conditions, int page, int pageSize) {
        if (conditions == null || conditions.isEmpty())
            return productDao.getAllEntitiesByPage(Product.class, page, pageSize, "id", IBaseDao.SortBy.DESC);

        TypeConverter.getInstance().convertStr2Enum(conditions, Product.class);
        return productDao.findEntitiesPagesByFieldList(Product.class, conditions, "id", IBaseDao.SortBy.DESC, page, pageSize);
    }

    public DomainPage<Product> getProductByConditionsPage(Boolean isOnline, String name, Boolean isDel, int page, int pageSize) {
        Map<String, Object> conditions = new HashMap<>(3);
        if (isOnline != null)
            conditions.put("online", isOnline);
        if (name != null)
            conditions.put("name", name);
        if (isDel != null)
            conditions.put("del", isDel);
        return getProductByConditionsPage(conditions, page, pageSize);
    }

    //ProductPackage

    /**
     * 验证数据，并填充destination字段
     *
     * @param productPackages
     */
    private void verifyAndInitProductPackages(Product product, List<ProductPackage> productPackages) {
        if (productPackages == null || productPackages.isEmpty())
            throw new BaseSystemException(ProductBizError.EMPTY_ENTITY);
        int lastOffset = Integer.MIN_VALUE;
        for (ProductPackage productPackage : productPackages) {
            //  天数递增
            if (lastOffset > productPackage.getOffset())
                throw new BaseSystemException(ProductBizError.PRODUCT_PACKAGES_DAY_MUST_BE_NON_DECREASING);
            lastOffset = productPackage.getOffset();
            productPackage.setProduct(product);
        }
    }

    public Map<Date, Integer> findTourismBusNoPlanCapacity(List<TourismRoute> tourismRoutes, Date start, Date end) {
        if (tourismRoutes == null || tourismRoutes.isEmpty() || start == null || end == null) Collections.emptyMap();

        //将行程按线路聚合到TourismRouteLine上
        List<TourismRouteLine> tourismRouteLines = TourismRouteLine.getTourismRouteLineFromTourismRoutes(tourismRoutes);

        // 查询每一个TourismRouteLine的最小剩余容量
        List<Map<Date, Integer>> overallCapacities = tourismRouteLines.stream().map(tourismRouteLine -> {
            return transportDao.findTourismRouteBusNoPlanCapacity(tourismRouteLine, start, end);
        }).collect(Collectors.toList());

        // 取最小值
        Map<Date, Integer> tourismBusNoCapacityMap = new HashMap<>();

        Date temp = start;
        while (!temp.after(end)) {
            Date current = temp;
            List<Integer> perDayCapacities = overallCapacities.stream().map(tourismLineBusNoCapacities -> tourismLineBusNoCapacities.getOrDefault(current, 0)).collect(Collectors.toList());
            Optional<Integer> min = perDayCapacities.stream().min(Integer::min);
            if (min.isPresent()) tourismBusNoCapacityMap.put(current, min.get());
            else tourismBusNoCapacityMap.put(current, 0);
            temp = Date.from(temp.toInstant().plus(1, ChronoUnit.DAYS));
        }
        return tourismBusNoCapacityMap;
    }

    public Integer findTourismBusNoPlanCapacity(List<TourismRoute> tourismRoutes, Date day) {
        if (tourismRoutes == null || tourismRoutes.isEmpty())
            return 0;

        //将行程按线路聚合到TourismRouteLine上
        List<TourismRouteLine> tourismRouteLines = TourismRouteLine.getTourismRouteLineFromTourismRoutes(tourismRoutes);

        // 查询每一个TourismRouteLine的最小剩余容量
        List<Integer> overallCapacities = tourismRouteLines.stream().map(tourismRouteLine -> {
            return transportDao.findTourismRouteBusNoPlanCapacity(tourismRouteLine, day);
        }).collect(Collectors.toList());

        // 取最小值
        Optional<Integer> finalCapacity = overallCapacities.stream().min(Integer::compare);
        return finalCapacity.isPresent() ? finalCapacity.get() : 0;
    }

    public Map<Date, Integer> findTourismBusNoPlanCapacity1(Product product, Date start, Date end) {
        if (product == null || start == null || end == null) return Collections.emptyMap();
        return findTourismBusNoPlanCapacity(product.getTourismRouteList(), start, end);
    }

    public Integer findTourismBusNoPlanCapacity(Product product, Date day) {
        if (product == null || day == null)
            return 0;
        return findTourismBusNoPlanCapacity(product.getTourismRouteList(), day);
    }

    public Integer findTourismBusNoPlanCapacity(Long productId, Date day) {
        Product product = getProductById(productId);
        if (product == null)
            return 0;
        return findTourismBusNoPlanCapacity(product, day);
    }


    /**
     * 按照指定的排车算法将购买指定日期行程产品的旅客进行排车
     *
     * @param product          行程产品
     * @param day              出行日期
     * @param quantity         订购数量
     * @param allocateStrategy 排车算法
     * @return
     */
    @Override
    public List<List<TourismBusAllocation.BusQuantity>> allocate(Product product, Date day, Integer quantity, TourismBusAllocation.AllocationStrategy allocateStrategy) {
        if (product == null || product.getId() == null) return Collections.emptyList();
        //获取行程产品的线路分解信息
        List<TourismRouteLine> tourismRouteLines = TourismRouteLine.getTourismRouteLineFromTourismRoutes(product.getTourismRouteList());
        if (tourismRouteLines == null || tourismRouteLines.isEmpty())
            return Collections.emptyList();
        List<List<TourismBusAllocation.BusQuantity>> overallAllocations = tourismRouteLines.stream().flatMap(tourismRouteLine -> {
            //获取行程线路的完整车次序列
            Line line = tourismRouteLine.getLine();
            Date offDay = Date.from(day.toInstant().plus(tourismRouteLine.getOffset(), ChronoUnit.DAYS));
            //获取行路内所有车次的座位数据表列表
            List<BusNo.BusNoCapacity> busNoCapacities = line.getBusNoSeries().stream().map(busNoSerial -> {
                BusNo busNo = busNoSerial.getBusNo();
                List<BusNoPlan> busNoPlans = transportDao.getBusNoPlanByBusNoAndDate(busNo, Date.from(offDay.toInstant().plus(busNoSerial.getDay(), ChronoUnit.DAYS)));

                // 查询已销售的凭证书作为已消耗的容量
                List<BusNo.BusCapacity> capacities = busNoPlans.stream().map(busNoPlan -> {
                    List<CertIdentity> certIdentities = orderDao.getCertIdentitiesByConditions(busNo, busNoPlan.getBus(),
                            Date.from(offDay.toInstant().plus(busNoSerial.getDay(), ChronoUnit.DAYS)));
                    return new BusNo.BusCapacity(busNoPlan.getBus(), busNoPlan.getCapacity(), (!(certIdentities == null || certIdentities.isEmpty()) ? certIdentities.size() : 0));
                }).collect(Collectors.toList());

                return new BusNo.BusNoCapacity(busNo, busNo.isTransferable(), capacities);
            }).collect(Collectors.toList());
            //根据排班算法及线路内车次座位数据表列表，为行程分解到该线路内的这部分车次分配车
            List<List<TourismBusAllocation.BusQuantity>> lineAllocations = allocateStrategy.allocate(busNoCapacities, tourismRouteLine, quantity);
            return lineAllocations.stream();
        }).collect(Collectors.toList());
        if (overallAllocations.stream().allMatch(busQuantities -> !(busQuantities == null || busQuantities.isEmpty())))
            return overallAllocations;
        return Collections.emptyList();
    }
}






