package units;

import com.simpletour.biz.inventory.IStockBiz;
import com.simpletour.biz.inventory.error.InventoryBizError;
import com.simpletour.biz.product.IProductBiz;
import com.simpletour.biz.product.error.ProductBizError;
import com.simpletour.biz.resources.error.ResourcesBizError;
import com.simpletour.common.core.dao.IBaseDao;
import com.simpletour.common.core.domain.DomainPage;
import com.simpletour.common.core.error.DefaultError;
import com.simpletour.common.core.exception.BaseSystemException;
import com.simpletour.dao.inventory.IStockDao;
import com.simpletour.domain.inventory.InventoryType;
import com.simpletour.domain.inventory.SoldEntry;
import com.simpletour.domain.inventory.Stock;
import com.simpletour.domain.inventory.query.IStockTraceable;
import com.simpletour.domain.inventory.query.StockKey;
import com.simpletour.domain.inventory.query.StockQuery;
import com.simpletour.domain.order.Order;
import com.simpletour.domain.order.OrderItem;
import com.simpletour.domain.order.OrderStatus;
import com.simpletour.domain.order.Source;
import com.simpletour.domain.product.Product;
import com.simpletour.domain.product.ProductPackage;
import com.simpletour.domain.resources.*;
import com.simpletour.service.inventory.ISoldEntryService;
import com.simpletour.service.inventory.IStockService;
import com.simpletour.service.resources.IResourcesService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 文件描述：库存模块测试用例类
 * 创建人员：石广路
 * 创建日期：2015/11/25 20:06
 * 备注说明：null
 */
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class StockTest extends AbstractTestNGSpringContextTests {
    @Resource
    IStockService stockService;

    @Resource
    ISoldEntryService soldEntryService;

    @Resource
    IResourcesService resourcesService;

    @Resource
    IProductBiz productBiz;

    @Resource
    private IStockDao stockDao; // 由于产品模块没有提供真删除的功能，这里需要直接调用JAP的删除接口来进行物理删除

    @Resource
    private IStockBiz stockBiz;

    Product product;

    private long tid;

    private long procurementId;

    private long prodStockId;

    private Date stockDate;

    private Procurement procurement;

    private Date paseDate(String date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date day = null;
        try {
            day = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail("invalid startDate!");
        }
        return day;
    }

    private Scenic bindResource() {
        Destination dest = null;
        Scenic scenic = null;
        DomainPage<Destination> destsList = resourcesService.getDestionationsByConditonPage("尊胜塔林", "四川省-甘孜藏族自治州", 1, 1);

        if (0 == destsList.getDomainTotalCount()) {
            try {
                Area area = new Area();
                area.setId(513300L);
                dest = new Destination();
                dest.setTenantId(3L);
                dest.setArea(area);
                dest.setName("尊胜塔林");
                dest.setAddress("四川省-甘孜藏族自治州-稻城县");
                dest.setLon(new BigDecimal(161.112));
                dest.setLat(new BigDecimal(85.209));
                dest.setRemark("国家级自然景区");
                Optional<Destination> resDest = resourcesService.addDestination(dest);
                Assert.assertEquals(resDest.isPresent(), true);
                dest = resDest.get();
            } catch (BaseSystemException e) {
                e.printStackTrace();
                Assert.fail("add Destination failed!");
            }
        } else {
            dest = destsList.getDomains().get(0);
            Assert.assertNotNull(dest);
        }

        Map<String, Object> conditions = new HashMap<String, Object>(2);
        conditions.put("name", "尊胜塔林风景区门票1张");
        conditions.put("address", "四川省-甘孜藏族自治州-稻城县");
        DomainPage<Scenic> scenicsList = resourcesService.queryScenicsPagesByConditions(conditions, "id", IBaseDao.SortBy.ASC, 0, resourcesService.DEFAULT_PAGE_SIZE, false);
        Assert.assertNotNull(scenicsList);

        if (0 == scenicsList.getDomainTotalCount()) {
            scenic = new Scenic();
            scenic.setName(conditions.get("name").toString());
            scenic.setAddress(conditions.get("address").toString());
            scenic.setRemark("甘孜藏族自治州旅游景点");
            scenic.setLon(new BigDecimal(161.112));
            scenic.setLat(new BigDecimal(85.209));
            scenic.setDestination(dest);
            scenic.setTenantId(dest.getTenantId());

            try {
                Optional<Scenic> optScenic = resourcesService.addScenic(scenic);
                Assert.assertTrue(optScenic.isPresent());
                scenic = optScenic.get();
            } catch (BaseSystemException e) {
                e.printStackTrace();
                Assert.fail("add Scenic failed!");
            }
        } else {
            scenic = scenicsList.getDomains().get(0);
        }

        Assert.assertNotNull(scenic);

        return scenic;
    }

    private OfflineServiceProvider getServiceProvider() {
        OfflineServiceProvider offlineServiceProvider = null;
        DomainPage<OfflineServiceProvider> ospsList = resourcesService.getOfflineServiceProvidersByConditionPage("中国青旅", null, 1, resourcesService.DEFAULT_PAGE_SIZE);

        if (0 == ospsList.getDomainTotalCount()) {
            offlineServiceProvider = new OfflineServiceProvider();
            offlineServiceProvider.setName("中国青旅");
            offlineServiceProvider.setTenantId(2L);
            offlineServiceProvider.setRemark("中国青旅国际旅行社");
            try {
                Optional<OfflineServiceProvider> res = resourcesService.addOfflineServiceProvider(offlineServiceProvider);
                Assert.assertTrue(res.isPresent());
                offlineServiceProvider = res.get();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                Assert.fail("add OfflineServiceProvider failed!");
            }
        } else {
            offlineServiceProvider = ospsList.getDomains().get(0);
            Assert.assertNotNull(offlineServiceProvider);
        }

        return offlineServiceProvider;
    }

    private Procurement addProcurement() {
        Map<String, Object> conditions = new HashMap(4);
        conditions.put("resourceType", "scenic");
        conditions.put("online", true);
        conditions.put("name", "尊胜塔林风景区门票1张");
        conditions.put("destination.name", "尊胜塔林");
        DomainPage<Procurement> procurementsList = resourcesService.queryProcurementsPagesByConditions(conditions, "id", IBaseDao.SortBy.ASC, 0, resourcesService.DEFAULT_PAGE_SIZE, false);
        Assert.assertNotNull(procurementsList);

        Procurement procurement = null;
        if (0 == procurementsList.getDomainTotalCount()) {
            procurement = new Procurement();
            procurement.setTenantId(2L);
            procurement.setOnline(true);
            procurement.setRemark("上线景点");
            procurement.setOsp(getServiceProvider());

            Scenic scenic = bindResource();
            procurement.setResourceType(Procurement.ResourceType.scenic);
            procurement.setResourceId(scenic.getId());
            procurement.setName(scenic.getName());
            procurement.setDestination(scenic.getDestination());
            procurement.setTenantId(scenic.getTenantId());

            try {
                Optional<Procurement> optProcurement = resourcesService.addProcurement(procurement);
                Assert.assertTrue(optProcurement.isPresent());
                procurement = optProcurement.get();
                tid = procurement.getId();
                Assert.assertNotEquals(0, tid);
                System.out.println("add Procurement id=" + tid + " successfully!");
            } catch (BaseSystemException e) {
                e.printStackTrace();
                Assert.fail("add Procurement failed!");
            }
        } else {
            procurement = procurementsList.getDomains().get(0);
            Assert.assertNotNull(procurement);
        }

        procurementId = procurement.getId();

        return procurement;
    }

    private Stock addStock(IStockTraceable stockTraceable, InventoryType inventoryType, Long inventoryTypeId, int quantity, double price) {
        Date day = new Date();
        Stock stock = new Stock();
        stock.setOnline(true);
        stock.setDay(day);
        stock.setInventoryType(inventoryType);
        stock.setInventoryTypeId(inventoryTypeId);
        stock.setQuantity(quantity);
        stock.setPrice(BigDecimal.valueOf(price));

        try {
            Optional<Stock> optObj = stockService.addStock(stock);
            Assert.assertTrue(optObj.isPresent());
            stock = optObj.get();
            if (InventoryType.product != inventoryType) {
                tid = stock.getId();
                Assert.assertNotEquals(0, tid);
            }
            soldEntryService.addSoldEntry(new SoldEntry(stock.getInventoryType(), stock.getInventoryTypeId(), stock.getDay(), true));
        } catch (BaseSystemException e) {
            if (InventoryBizError.STOCK_IS_EXISTING.getErrorMessage().equals(e.getError().getErrorMessage())) {
                System.out.println("the Stock is existing!");
                Optional<Stock> optObj = stockService.getStock(stockTraceable, day, true);
                Assert.assertTrue(optObj.isPresent());
                stock = optObj.get();
                if (InventoryType.product != inventoryType) {
                    tid = stock.getId();
                    Assert.assertNotEquals(0, tid);
                }
            } else if (InventoryBizError.STOCK_DEPENDENCY_NOT_EXIST.getErrorMessage().equals(e.getError().getErrorMessage())) {
                System.err.println("not exist such procurement stock dependency!");
                return null;
            } else {
                e.printStackTrace();
                Assert.fail("add Stock failed!");
            }
        }

        System.out.println("add Stock id=" + tid + " successfully!");
        return stock;
    }

    private void addProductStock(Procurement procurement) {
        List<ProductPackage> productPackages = new ArrayList<>(1);
        productPackages.add(new ProductPackage(procurement, 0));
        product = new Product("石广路-订购尊胜塔林风景区门票10张", true, "<p>于2015-12-22订购尊胜塔林风景区门票10张。</p>", productPackages);

        try {
            product = productBiz.addProduct(product);
        } catch (BaseSystemException e) {
            if (ProductBizError.PRODUCT_NAME_EXIST.getErrorMessage().equals(e.getError().getErrorMessage())) {
                System.out.println("the Product is existing!");
                Map<String, Object> conditions = new HashMap<>(2);
                conditions.put("online", true);
                conditions.put("name", product.getName());
                DomainPage<Product> productDomainPage = productBiz.getProductByConditionsPage(conditions, 1, 10);
                Assert.assertNotEquals(productDomainPage.getDomains().size(), 0);
                product = productBiz.getProductById(productDomainPage.getDomains().get(0).getId());
                Assert.assertNotNull(product);
            } else {
                e.printStackTrace();
                Assert.fail("add Product failed!");
            }
        }

        Stock productStock = addStock(product, InventoryType.product, product.getId(), 26, 165.0);
        if (null != productStock) {
            prodStockId = productStock.getId();
        }
    }

    @Test(priority = 1)
    public void addStock() {
        procurement = addProcurement();
        Stock stock = addStock(procurement, InventoryType.procurement, procurement.getId(), 21, 169.9);
        if (null != stock) {
            stockDate = stock.getDay();
            addProductStock(procurement);
        }
        //Optional<Procurement> procurementOptional = resourcesService.getProcurementById(35);
        //addProductStock(procurementOptional.get());
    }

    @Test(priority = 2)
    public void updateStock() {
        //tid = 18;    // just for test
        System.out.println("update Stock id=" + tid + " quantity and price start...");
        Optional<Stock> optObj = stockService.getStockRecordById(tid);
        Assert.assertTrue(optObj.isPresent());

        Stock stock = optObj.get();
        Integer quantity = stock.getQuantity();
        stock.setQuantity(null == quantity || 0 >= quantity ? 1 : quantity + 1);
        stock.setPrice(stock.getPrice().add(BigDecimal.valueOf(0.2)));

        try {
            optObj = stockService.updateStock(stock);
            Assert.assertTrue(optObj.isPresent());
            Assert.assertNotEquals(quantity, optObj.get().getQuantity());
            System.out.println("update Stock quantity and price successfully!");
        } catch (BaseSystemException e) {
            e.printStackTrace();
            Assert.fail("update Stock quantity and price failed!");
        }
    }

    @Test(priority = 3, dependsOnMethods = "addStock")
    public void getStock() {
        //Date day = stockDate;
        //Date day = paseDate("2015-12-24");
        Date day = new Date();
        System.out.println("get day=" + day);

        Procurement procurement = addProcurement();
        Optional<Stock> optObj = stockService.getStock(procurement, day, true);
        Assert.assertTrue(optObj.isPresent());

        Product prod = null;
        //Tourism tourism = null;
        try {
            prod = (Product) stockService.getStockWithDependencies(new StockKey(InventoryType.product, product.getId()), day);
            Optional<Stock> productStock = stockService.getStock(prod, day, true);
            Assert.assertTrue(productStock.isPresent());
            //Date date = paseDate("2015-12-25");
//            tourism = (Tourism)stockService.checkStockDependencies(new StockKey(InventoryType.tourism, 8L), day);
//            Optional<Stock> tourismStock = stockService.getRelationalStock(tourism, day);
//            Assert.assertNotNull(tourismStock.get());
        } catch (BaseSystemException e) {
            e.printStackTrace();
            System.err.println("unable to get the stock dependencies cause by not exist!");
        }

        Optional<Stock> relationalStock = stockService.getRelationalStock(procurement, day);
        Assert.assertTrue(relationalStock.get().equals(optObj.get()));

        System.out.println("get Stock id=" + optObj.get().getId() + " successfully!");
    }

    @Test(priority = 4, dependsOnMethods = "addStock")
    public void getStocks() {
        Date startDate = paseDate("2015-12-08");
        Date endDate = new Date();

//        Optional<Procurement> procurementOptional = resourcesService.getProcurementById(51);
//        List<Stock> stocksList = stockService.getStocks(procurementOptional.get(), paseDate("2015-12-23"), paseDate("2015-12-26"), null);
//        Assert.assertNotNull(stocksList);

        Procurement procurement = addProcurement();
        List<Stock> stocks = stockService.getStocks(procurement, startDate, endDate, true);
        Assert.assertNotNull(stocks);
        Assert.assertFalse(stocks.isEmpty());
        System.out.println("get stocks size=" + stocks.size() + " successfully!");

        List<Stock> stockList = stockService.getRelationalStocks(procurement, startDate, endDate);
        Assert.assertNotNull(stockList);
        Assert.assertFalse(stockList.isEmpty());

        StockKey stockKey = new StockKey(InventoryType.procurement, null);
        DomainPage domainPage = stockService.getStocksPagesByConditions(stockKey, startDate, endDate, "day", IBaseDao.SortBy.DESC, 1, 10);
        Assert.assertNotEquals(0, domainPage.getDomainTotalCount());

        // 示例："尊胜塔林风景" null inventoryTypeId
        //StockQuery stockQuery = new StockQuery(stockKey, paseDate("2015-12-13"), paseDate("2015-12-19"), true, "test", "day", IBaseDao.SortBy.DESC, 10, 1);

        StockQuery stockQuery = new StockQuery(new StockKey(InventoryType.product, null), paseDate("2015-12-25"), null, true, StockQuery.InventoryStatus.ONLINE, null, "inventoryTypeId", IBaseDao.SortBy.DESC, 10, 1);
        //StockQuery stockQuery = new StockQuery(new StockKey(InventoryType.bus_no, null), paseDate("2015-12-22"), paseDate("2015-12-24"), true, StockQuery.InventoryStatus.NORMAL, null, "inventoryTypeId", IBaseDao.SortBy.DESC, 10, 1);
        DomainPage domainPage2 = stockService.getStocksQuantitiesPagesByRelations(stockQuery);
        Assert.assertNotEquals(0, domainPage2.getDomainTotalCount());

        startDate = paseDate("2015-12-08");
        List<Stock> onlineStocks = stockService.getOnlineStocks(procurement, startDate, endDate);
        Assert.assertNotNull(onlineStocks);
        Assert.assertFalse(onlineStocks.isEmpty());
        System.out.println("get onlineStocks size=" + stocks.size() + " successfully!");

        System.out.println("get stockList size=" + stockList.size() + " successfully!");
    }

    @Test(priority = 5, dependsOnMethods = "addStock")
    public void checkStockQuantity() {
        Date date = new Date();
        Source source = new Source(2L, "简途网站", true, true, "test source");

        OrderItem orderItem = new OrderItem(date, null, product.getId(), Collections.emptyList());
        orderItem.setQuantity(10);
        List<OrderItem> orderItems = new ArrayList<>(1);
        orderItems.add(orderItem);

        OrderStatus orderStatus = new OrderStatus(1L, OrderStatus.Operation.BOOKING, OrderStatus.Status.WAITING_FOR_PAYMENT, date.getTime());
        List<OrderStatus> OrderStatuses = new ArrayList<>(1);
        OrderStatuses.add(orderStatus);

        Order order = new Order(2L, false, 1L, source, "简途通", "1", "安俊朗", "18381078210", date, "测试订单", orderItems, OrderStatuses);

        try {
            stockService.checkDemandQuantity(order);
            System.out.println("booking this order successfully!");
        } catch (BaseSystemException e) {
            System.err.println("unable to support this order, cause by: " + e.getMessage());
        }
    }

    @Test(priority = 6, dependsOnMethods = "addStock")
    public void getStocksPrice() {
//        StockQuery stockQuery = new StockQuery(new StockKey(InventoryType.tourism, 273L), paseDate("2015-11-16"), paseDate("2015-11-30"), true);
//        BigDecimal minPrice = stockService.getStocksPriceInTimeframe(stockQuery, true);
//        System.out.println("minPrice is " + minPrice);

        try {
            StockQuery singleQuery = new StockQuery(new StockKey(InventoryType.procurement, tid), stockDate, true);
            BigDecimal lowestPrice = stockBiz.getStocksPriceInTimeframe(singleQuery, true);
            System.out.println("lowestPrice is " + lowestPrice);
        } catch (BaseSystemException bse) {
            String errorMsg = bse.getMessage();
            if (InventoryBizError.GET_STOCK_PRICE_FAILED.getErrorMessage().equals(errorMsg)) {
                System.err.println("unable to get the lowest price of the procurement(" + tid + "), because of " + errorMsg);
            } else {
                bse.printStackTrace();
            }
        }

        StockQuery singleQuery = new StockQuery(new StockKey(InventoryType.procurement, procurementId), stockDate, true);
        BigDecimal lowestPrice = stockBiz.getStocksPriceInTimeframe(singleQuery, true);
        Assert.assertNotNull(lowestPrice);
        System.out.println("lowestPrice is " + lowestPrice);
    }

    @Test(priority = 7, dependsOnMethods = {"getStock", "getStocks"})
    public void deleteStock() {
        //tid = 5;    // for test
        System.out.println("delete Stock id=" + tid);

        stockService.deleteStock(tid);
        Optional<Stock> optObj = stockService.getStockRecordById(tid);
        Assert.assertFalse(optObj.isPresent());

        List<Long> ids = new ArrayList<>(1);
        ids.add(tid + 1);
        stockService.deleteStocks(ids);
        optObj = stockService.getStockRecordById(tid + 1);
        Assert.assertFalse(optObj.isPresent());

        if (0 < prodStockId) {
            stockService.deleteStock(prodStockId);
            Assert.assertFalse(stockService.getStockRecordById(prodStockId).isPresent());
        }

        try {
            stockDao.removeEntityById(Product.class, product.getId(), false);
        } catch (BaseSystemException bse) {
            String errorMsg = bse.getMessage();
            if (DefaultError.NOT_SAME_TENANT_ID.getErrorMessage().equals(errorMsg)) {
                System.err.println("unable to delete product(" + product.getId() + "), because of " + errorMsg);
            } else {
                bse.printStackTrace();
            }
        }

        try {
            resourcesService.deleteProcurement(procurementId);
        } catch (BaseSystemException bse) {
            String errorMsg = bse.getMessage();
            if (ResourcesBizError.CANNOT_DEL_DEPENDENT_PROCUREMENT.getErrorMessage().equals(errorMsg)) {
                System.err.println("unable to delete procurement(" + procurementId + "), because of " + errorMsg);
            } else {
                bse.printStackTrace();
            }
        }

        System.out.println("delete Stock complete!");
    }

    @Test(priority = 8, dependsOnMethods = {"addStock", "deleteStock"})
    public void deleteStockByInventoryTypeId() {
        stockBiz.deleteStocksByInventoryTypeId(InventoryType.procurement, procurement.getId());
        Optional<Stock> stockOpt = stockService.getStock(procurement, stockDate, true);
        Assert.assertFalse(stockOpt.isPresent());
    }
}