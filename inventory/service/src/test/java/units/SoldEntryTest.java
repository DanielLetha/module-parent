//package units;
//
//import com.simpletour.domain.inventory.InventoryType;
//import com.simpletour.domain.inventory.SoldEntry;
//import com.simpletour.domain.inventory.query.SoldEntryKey;
//import com.simpletour.service.inventory.ISoldEntryService;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
//import org.testng.Assert;
//import org.testng.annotations.Test;
//
//import javax.annotation.Resource;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
///**
// * 文件描述：销售库存模块测试用例类
// * 创建人员：石广路
// * 创建日期：2015/12/8 14:20
// * 备注说明：null
// */
//@ContextConfiguration(locations={"classpath:applicationContext.xml"})
//public class SoldEntryTest extends AbstractTestNGSpringContextTests {
//    @Resource
//    private ISoldEntryService soldEntryService;
//
//    private long tid;
//
//    private long oid = 12;
//
//    private SoldEntry soldEntry;
//
//    @Test(priority = 1)
//    public void addSoldEntries() {
//        System.out.println("add addSoldEntries start ...");
//        List<SoldEntry> soldEntries = new ArrayList<>(1);
//        soldEntry = new SoldEntry(InventoryType.procurement, 1L, new Date(), true);
//        soldEntry.setQuantity(2);
//        soldEntry.setItemId(11L);
//        soldEntry.setOrderId(oid);
//        soldEntry.setParentId(13L);
//        soldEntries.add(soldEntry);
//
//        soldEntries = soldEntryService.addSoldEntries(soldEntries);
//        soldEntry = soldEntries.get(0);
//        tid = soldEntries.get(0).getId();
//        Assert.assertNotEquals(0, tid);
//
//        int soldQuantity = soldEntryService.getAvailableSoldQuantity(new SoldEntryKey(soldEntry.getInventoryType(), soldEntry.getInventoryTypeId()), soldEntry.getDay());
//        Assert.assertNotEquals(0, soldQuantity);
//
//        System.out.println("add addSoldEntries end");
//    }
//
//    @Test(priority = 2)
//    public void getSoldEntries() {
//        //tid = 11;   // for testing
//        System.out.println("invalidate status of SoldEntry id=" + tid);
//        soldEntryService.invalidateSoldEntriesByOrderId(oid);
//
//        SoldEntryKey soldEntryKey = new SoldEntryKey(soldEntry.getOrderId(), soldEntry.getItemId(), soldEntry.getParentId());
//        //SoldEntryKey soldEntryKey = new SoldEntryKey(12L, 11L, 13L);
//        List<SoldEntry> soldEntries = soldEntryService.getSoldEntriesByUnionIds(soldEntryKey);
//        Assert.assertNotNull(soldEntries);
//        Assert.assertFalse(soldEntries.isEmpty());
//        Assert.assertFalse(soldEntries.get(0).getStatus());
//    }
//}
