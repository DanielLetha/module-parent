package com.simpletour.biz.traveltrans.impl;

import com.simpletour.biz.traveltrans.ILineBiz;
import com.simpletour.biz.traveltrans.bo.BusNoLineMatcher;
import com.simpletour.biz.traveltrans.bo.LineBusNoSerial;
import com.simpletour.biz.traveltrans.bo.Tuple;
import com.simpletour.biz.traveltrans.error.TravelTransportBizError;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.dao.query.condition.AndConditionSet;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.product.imp.IProductDao;
import com.simpletour.dao.traveltrans.ITransportDao;
import com.simpletour.domain.product.Product;
import com.simpletour.domain.product.TourismRoute;
import com.simpletour.domain.product.TourismRouteLine;
import com.simpletour.domain.traveltrans.Line;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Mario on 2015/12/1.
 */
@Component
public class LineBizImpl implements ILineBiz {

    @Resource
    private ITransportDao iTransportDao;

    @Resource
    IProductDao iProductDao;

    private void verifyLine(Line line) {
        // line name verify
        if (line == null || line.getName() == null || line.getName().isEmpty())
            throw new BaseSystemException(TravelTransportBizError.BUS_LINE_NULL);
        // line must contains bus no serial
        if (line.getBusNoSeries() == null || line.getBusNoSeries().isEmpty())
            throw new BaseSystemException(TravelTransportBizError.BUS_LINE_BUS_NO_SERIAL_INVALID);
    }

    @Override
    public Optional<Line> addLine(Line line) {
        verifyLine(line);

        //判断添加的路线是否在数据库中已经存在
        Line original = iTransportDao.getEntityByField(Line.class, "name", line.getName());
        if (original != null) throw new BaseSystemException(TravelTransportBizError.BUS_LINE_EXISTED);

        return Optional.ofNullable(iTransportDao.save(line));
    }

    @Override
    @Transactional
    public Optional<Line> updateLine(Line line) {
        verifyLine(line);

        //对线路名称进行重名的判断
        Line original = iTransportDao.getEntityById(Line.class, line.getId());
        if (original == null) throw new BaseSystemException(TravelTransportBizError.BUS_LINE_NOT_EXIST);
        if (!original.getName().equals(line.getName())) {
            //在更改了名字情况下，查看新的名字是否被占用
            Line existed = iTransportDao.getEntityByField(Line.class, "name", line.getName());
            if (existed != null) throw new BaseSystemException(TravelTransportBizError.BUS_LINE_EXISTED);
        }

        //删除line关联的busNoSerial
        iTransportDao.save(line);
        //检查行程是否能够分解到线路上
        List<TourismRoute> tourismRoutes = iProductDao.getEntitiesByField(TourismRoute.class, "line.id", line.getId());
        List<Long> tourismIds = tourismRoutes.stream().map(n -> n.getId()).distinct().collect(Collectors.toList());
        if (!(tourismIds == null || tourismIds.isEmpty())) {
            List<Line> lines = iTransportDao.getAllEntities(Line.class);
            tourismIds.stream().map(tourismId -> new Tuple<>(tourismId, iProductDao.getEntityById(Product.class, tourismId))).forEach(routes -> {
                List<TourismRoute> composite = TourismRoute.decompose(lines, routes.getSecond().getTourismRouteList());
                if (composite == null || composite.isEmpty() || composite.size() != routes.getSecond().getTourismRouteList().size())
                    throw new BaseSystemException(TravelTransportBizError.BUS_LINE_CANT_DECOMPOSE);
                iProductDao.save(routes.getSecond());
            });
        }
        return Optional.ofNullable(line);
    }

    @Override
    public boolean deleteLineById(Line line) {
        if (line == null || line.getId() == null) throw new BaseSystemException(TravelTransportBizError.BUS_LINE_NULL);
        iTransportDao.removeEntity(line);
        return true;
    }

    @Override
    public Optional<Line> findLineById(Long id) {
        if (id == null) throw new BaseSystemException(TravelTransportBizError.BUS_LINE_NULL);
        return Optional.ofNullable(iTransportDao.getEntityById(Line.class, id));
    }


    public boolean isExisted(Long id) {
        return iTransportDao.getEntityById(Line.class, id) != null;
    }

    @Override
    public List<Line> findLineByConditions(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy sortBy) {
        return iTransportDao.getEntitiesByFieldList(Line.class, conditions, orderByFiledName, sortBy);
    }

    @Override
    public DomainPage<Line> findLinePagesByConditions(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy sortBy, Integer pageIndex, Integer pageSize, boolean byLike) {
        return iTransportDao.queryEntitiesPagesByFieldList(Line.class, conditions, orderByFiledName, sortBy, pageIndex, pageSize, byLike);
    }

    @Override
    public DomainPage findLinesPageByConditions(AndConditionSet condition, int page, int pageSize) {
        if (condition == null || condition.isEmpty())
            return iTransportDao.getAllEntitiesByPage(Line.class, page, pageSize);
        return iTransportDao.findLinesPageByConditions(condition, page, pageSize);
    }

    @Override
    public List<TourismRoute> decompose(List<Line> lines, List<TourismRoute> routes) {

        //没有待匹配行程车次序列或者没有线路，则分解完成
        if (routes == null || routes.isEmpty() || lines==null || lines.isEmpty()) return Collections.emptyList();
        //获取车次线路匹配器
        List<BusNoLineMatcher> busNoLineMatchers = lines.stream().map(BusNoLineMatcher::new).collect(Collectors.toList());

        List<TourismRoute> scalarResult = new ArrayList<>();
        //初始化待匹配行程车次序列
        List<TourismRoute> restRoutes = routes; // restRoutes, 剩余的Route，开始初始化为所有的routes
        while (!restRoutes.isEmpty()) {
            List<TourismRoute> bestMatchSeries = new ArrayList<>();
            BusNoLineMatcher bestMatcher = null;
            for (BusNoLineMatcher busNoLineMatcher : busNoLineMatchers) {//用所有的车次线路匹配器去匹配待匹配行程车次序列
                List<TourismRoute> matchedSeries = busNoLineMatcher.find(restRoutes);//获取待匹配行程车次序列与线路车次序列的最长公共子串，一个匹配可能有多个结果，这个地方应该全部输出，找出满足下面条件的子序列
                if (matchedSeries.size() > bestMatchSeries.size()) {//保证每次匹配的线路最佳（匹配越长，认为匹配度越好）
                    bestMatcher = busNoLineMatcher;         //保存当前最优的matcher，以便在本轮匹配完成之后，本轮最优的matcher跳过已经匹配的序列
                    bestMatchSeries = matchedSeries;       //保存当前最优的结果
                }
            }
            if (bestMatchSeries.isEmpty())      // 当当前的route无法匹配到任何线路时，跳出循环，并返回
                break;
            scalarResult.addAll(bestMatchSeries);       //将本次已匹配的序列加入的scalarResult中
            bestMatcher.move();//跳过已经匹配过的序列
            restRoutes = routes.subList(scalarResult.size(), routes.size());    //匹配了一部分route，restRoute更新为剩余未匹配的route
        }
        return restRoutes.isEmpty() ? scalarResult : Collections.emptyList();   //只有restRoutes为空，即所有route都匹配到线路后，才认为分解成功
    }

    @Override
    public List<LineBusNoSerial> pick(TourismRouteLine tourismRouteLine) {
        if(tourismRouteLine==null) return Collections.emptyList();

        List<LineBusNoSerial> lineBusNoSeries = LineBusNoSerial.from(tourismRouteLine.getLine());
        if(!(lineBusNoSeries ==null|| lineBusNoSeries.isEmpty() || tourismRouteLine.getBusNoSeries()==null||tourismRouteLine.getBusNoSeries().isEmpty())){
            List<LineBusNoSerial> selectedBusNoSeries = new ArrayList<>();
            for(int i = 0, j = 0; i < lineBusNoSeries.size() && j < tourismRouteLine.getBusNoSeries().size(); ++i){
                if(lineBusNoSeries.get(i).getBusNo().equals(tourismRouteLine.getBusNoSeries().get(j))){//线路内的第i个车次包含在行程分解线路序列内
                    selectedBusNoSeries.add(lineBusNoSeries.get(i));
                    j += 1;
                }else if(!(selectedBusNoSeries.isEmpty()|| selectedBusNoSeries.get(selectedBusNoSeries.size()-1).isTransferable())){  // 虎哥定理：可换乘的传递性
                    selectedBusNoSeries.get(selectedBusNoSeries.size()-1).setTransferable(lineBusNoSeries.get(i).isTransferable());
                }
            }
            return selectedBusNoSeries;
        }
        return Collections.emptyList();
    }
}
