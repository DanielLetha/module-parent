import com.simpletour.biz.traveltrans.ILineBiz;
import com.simpletour.biz.traveltrans.error.TravelTransportBizError;
import com.simpletour.common.core.dao.IBaseDao;
import com.simpletour.common.core.domain.BaseDomain;
import com.simpletour.common.core.domain.DomainPage;
import com.simpletour.common.core.exception.BaseSystemException;
import com.simpletour.dao.traveltrans.ITransportDao;
import com.simpletour.domain.traveltrans.BusNoSerial;
import com.simpletour.domain.traveltrans.Line;
import com.simpletour.test.helper.*;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by yangdongfeng on 2016/3/23.
 */
@ContextConfiguration({"classpath:applicationContext.xml"})
public class LineTest extends TestClassWithLazyLoadAndTenantId implements DomainAdder {

    @Resource
    ILineBiz lineBiz;

    TransportDataGenerator dataGenerator;

    Line line;

    @Resource
    ITransportDao dao;

    @BeforeClass
    public void beforeClass() throws Exception {
        super.beforeClass();
        dataGenerator = new TransportDataGenerator(dao, dao);
    }

    @Override
    public BaseDomain addDomain(BaseDomain baseDomain) {
        if (baseDomain instanceof Line)
            return lineBiz.addLine((Line)baseDomain).get();
        throw new IllegalArgumentException("unhandled domain type");
    }

    @Test(priority = -1)
    public void addLine() {
        dataGenerator.addBusNos(-1);
        dataGenerator.addLines(-1, this);
        line = dataGenerator.getLineInfos()[0].line;
        Assert.assertNotNull(line);
        String oldName = line.getName();

        // same name
        try {
            Line tl = new Line(line.getBusNoSeries(), oldName);
            lineBiz.addLine(tl);
            Assert.fail();
        } catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), TravelTransportBizError.BUS_LINE_EXISTED);
        }

        // illegal bus no serial
        try {
            Line tl = new Line(null, Utils.generateName() + "test name line");
            lineBiz.addLine(tl);
            Assert.fail();
        }catch (BaseSystemException e) {
            Assert.assertEquals(e.getError(), TravelTransportBizError.BUS_LINE_BUS_NO_SERIAL_INVALID);
        }
    }

    @Test
    public void isLineExist() {
        Assert.assertFalse(lineBiz.isExisted(0L));
        Assert.assertTrue(lineBiz.isExisted(line.getId()));
    }

    @Test
    public void updateLine() {
        line.setName(Utils.generateName()+"updated line name");
        lineBiz.updateLine(line);

        BusNoSerial rmvBNS = line.getBusNoSeries().get(0);
        Integer oldSize = line.getBusNoSeries().size();
        line.getBusNoSeries().remove(0);
        lineBiz.updateLine(line);
        Line newLine = lineBiz.findLineById(line.getId()).get();
        Assert.assertNotNull(newLine);
        Assert.assertEquals(newLine.getBusNoSeries().size()+1, oldSize.intValue());

        newLine.getBusNoSeries().add(rmvBNS);
        line = lineBiz.updateLine(newLine).get();
        Assert.assertEquals(line.getBusNoSeries().size(), oldSize.intValue());

        //TODO 更新线路时的行程重新分解测试
    }

    @Test()
    public void findLineById() {
        Optional<Line> res = lineBiz.findLineById(this.line.getId());
        Assert.assertTrue(res.isPresent());
        this.line = res.get();
        Assert.assertTrue(res.isPresent());
    }

    @Test()
    public void findLineByConditions() {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("name", this.line.getName());
        List<Line> list = lineBiz.findLineByConditions(conditions, "id", IBaseDao.SortBy.ASC);
        Assert.assertTrue(list.size() > 0);
    }

    @Test()
    public void findLinePagesByConditions() {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("name", this.line.getName());
        DomainPage<Line> domainPage = lineBiz.findLinePagesByConditions(conditions, "id", IBaseDao.SortBy.ASC, 1, 10, false);
        Assert.assertTrue(domainPage.getDomains().size() > 0);
    }

    @Test(priority = 99)
    public void deleteLine() {
        lineBiz.deleteLineById(line);
    }

}
