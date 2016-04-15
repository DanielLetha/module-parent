package com.simpletour.service.traveltrans.impl;

import com.simpletour.biz.inventory.IStockBiz;
import com.simpletour.biz.order.ICertBiz;
import com.simpletour.biz.resources.IDestinationBiz;
import com.simpletour.biz.traveltrans.*;
import com.simpletour.biz.traveltrans.bo.BusNoBo;
import com.simpletour.biz.traveltrans.bo.BusNoPlanBo;
import com.simpletour.biz.traveltrans.bo.DomainPageBo;
import com.simpletour.biz.traveltrans.bo.NodeBo;
import com.simpletour.biz.traveltrans.error.TravelTransportBizError;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.dao.query.condition.AndConditionSet;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.domain.inventory.InventoryType;
import com.simpletour.domain.inventory.Stock;
import com.simpletour.domain.order.CertIdentity;
import com.simpletour.domain.traveltrans.*;
import com.simpletour.service.traveltrans.ITravelTransportService;
import com.simpletour.service.traveltrans.error.TravelTransportServiceError;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 车次模块业务处理实现类
 * Created by Mario on 2015/11/20.
 */
@Service
public class TravelTransportServiceImpl implements ITravelTransportService {

    @Resource
    private IBusBiz iBusBiz;

    @Resource
    private IBusNoBiz iBusNoBiz;

    @Resource
    private ILineBiz iLineBiz;

    @Resource
    private IBusNoPlanBiz busNoPlanBiz;

    @Resource
    private IStockBiz stockBiz;

    @Resource
    private ICertBiz certBiz;

    @Resource
    private INodeTypeBiz iNodeTypeBiz;

    @Resource
    private INodeStatusBiz iNodeStatusBiz;

    @Resource
    private ISeatLayoutBiz iSeatLayoutBiz;

    @Resource
    private IAssistantBiz assistantBiz;

    @Resource
    private IDestinationBiz iDestinationBiz;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Optional<Bus> addBus(Bus bus) {
        if (bus == null || bus.getLicense() == null || bus.getLicense().isEmpty()) {
            throw new BaseSystemException(TravelTransportServiceError.BUS_NULL);
        }
        if (bus.getLayout() == null || bus.getLayout().getId() == null || !iSeatLayoutBiz.isExisted(bus.getLayout().getId())) {
            //座位布局不可用
            throw new BaseSystemException(TravelTransportServiceError.BUS_SEAT_LAYOUT_NOT_AVAILABLE);
        }
        return iBusBiz.addBus(bus);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean doDeleteBus(Bus bus) {
        if (bus == null || bus.getId() == null) {
            throw new BaseSystemException(TravelTransportServiceError.BUS_NULL);
        }
        return iBusBiz.deleteBus(bus);
    }

    @Override
    public boolean deleteBus(Bus bus) {
        try {
            return doDeleteBus(bus);
        } catch (DataIntegrityViolationException e) {
            throw new BaseSystemException(TravelTransportServiceError.EXIST_FOREIGNKEY_CONSTRAINT_IN_DB);
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Optional<Bus> updateBus(Bus bus) {

        if (bus == null || bus.getId() == null || bus.getLicense() == null || bus.getLicense().isEmpty()) {
            throw new BaseSystemException(TravelTransportServiceError.BUS_NULL);
        }
        if (bus.getLayout() == null || bus.getLayout().getId() == null || !iSeatLayoutBiz.isExisted(bus.getLayout().getId())) {
            //座位布局不可用
            throw new BaseSystemException(TravelTransportServiceError.BUS_SEAT_LAYOUT_NOT_AVAILABLE);
        }
        return iBusBiz.updateBus(bus);
    }

    @Override
    public DomainPage<Bus> findBusPageByConditions(final Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, Integer pageIndex, Integer pageSize, boolean byLike) {
        return iBusBiz.findBusPageByConditions(conditions, orderByFiledName, orderBy, pageIndex, pageSize, byLike);
    }

    @Override
    public List<Bus> findBusByConditions(final Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy) {
        return iBusBiz.findBusByConditions(conditions, orderByFiledName, orderBy);
    }

    @Override
    public DomainPage<Bus> findAvailableBus(String license, Long busNoId, Date day, int pageIndex, int pageSize) {
        if (busNoId == null || day == null) {
            throw new BaseSystemException(TravelTransportServiceError.BUS_BUS_NO_ID_AND_DAY_NULL);
        }
        return iBusBiz.findAvailableBus(license, busNoId, day, pageIndex, pageSize);
    }


    @Override
    public Optional<Bus> findBusById(Long id) {
        if (id == null) {
            throw new BaseSystemException(TravelTransportServiceError.BUS_NULL);
        }
        return iBusBiz.findBusById(id);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Optional<BusNo> addBusNo(BusNoBo busNoBo) {
        if (busNoBo == null)
            throw new BaseSystemException(TravelTransportServiceError.BUS_NO_NULL);
        //判断一致性
        judgeBusNoConsistency(busNoBo);
        return iBusNoBiz.addBusNo(busNoBo);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean doDeleteBusNo(BusNo busNo) {
        if (busNo == null || busNo.getId() == null)
            throw new BaseSystemException(TravelTransportServiceError.BUS_NO_NULL);
        return iBusNoBiz.deleteBusNo(busNo);
    }

    @Override
    public boolean deleteBusNo(BusNo busNo) {
        try {
            return doDeleteBusNo(busNo);
        } catch (DataIntegrityViolationException e) {
            throw new BaseSystemException(TravelTransportServiceError.EXIST_FOREIGNKEY_CONSTRAINT_IN_DB);
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Optional<BusNo> updateBusNo(BusNoBo busNoBo) {
        if (busNoBo == null || busNoBo.getId() == null)
            throw new BaseSystemException(TravelTransportServiceError.BUS_NO_NULL);
        //判断一致性
        judgeBusNoConsistency(busNoBo);
        return iBusNoBiz.updateBusNo(busNoBo);
    }

    @Override
    public Optional<BusNo> findBusNoByNo(String no, Boolean eager) {
        if (no == null || no.isEmpty()) throw new BaseSystemException(TravelTransportServiceError.BUS_NO_NO_FOR_NULL);
        return iBusNoBiz.findBusNoByNo(no, eager);
    }

    @Override
    public Optional<BusNo> findBusNoById(Long id) {
        if (id == null) throw new BaseSystemException(TravelTransportServiceError.BUS_NO_NULL);
        return iBusNoBiz.findBusNoById(id);
    }

    @Override
    public DomainPage<BusNo> findBusNoPageByConditions(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, Integer pageIndex, Integer pageSize, boolean byLike) {
        return iBusNoBiz.findBusNoPageByConditions(conditions, orderByFiledName, orderBy, pageIndex, pageSize, byLike);
    }

    @Override
    public List<BusNo> findBusNoByConditions(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy sortBy) {
        return iBusNoBiz.findBusNoByConditions(conditions, orderByFiledName, sortBy);
    }

    @Override
    public DomainPage<BusNo> findAvailableBusNo(String no, int pageIndex, int pageSize) {
        return iBusNoBiz.findAvailableBusNo(no, pageIndex, pageSize);
    }

    //region -----------------------busNoPlan-------------------------------
    private void verifyBusNoPlan(BusNoPlan busNoPlan) throws BaseSystemException {
        if (busNoPlan == null)
            throw new BaseSystemException(TravelTransportServiceError.BUS_NO_PLAN_NULL);
        if (busNoPlan.getNo() == null || busNoPlan.getNo().getId() == null)
            throw new BaseSystemException(TravelTransportServiceError.BUS_NO_NULL);
        if (busNoPlan.getBus() == null || busNoPlan.getBus().getId() == null)
            throw new BaseSystemException(TravelTransportServiceError.BUS_NULL);

        if (!iBusNoBiz.isAvailable(busNoPlan.getNo().getId()))
            throw new BaseSystemException(TravelTransportServiceError.BUS_NO_STOPED);

        if (!iBusBiz.isAvailable(busNoPlan.getBus().getId()))
            throw new BaseSystemException(TravelTransportServiceError.BUS_NOT_ONLINE);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Optional<BusNoPlan> addBusNoPlan(BusNoPlan busNoPlan) {
        verifyBusNoPlan(busNoPlan);
        Optional<BusNo> busNoOptional = iBusNoBiz.findBusNoById(busNoPlan.getNo().getId());
        BusNo busNo;
        if (busNoOptional.isPresent() && iBusNoBiz.isAvailable(busNoOptional.get().getId())) {
            busNo = busNoOptional.get();
        } else {
            throw new BaseSystemException(TravelTransportServiceError.BUS_NO_STOPED);
        }
        Integer start = (int) busNoPlan.getDay().toInstant().atOffset(ZoneOffset.ofHours(8)).toEpochSecond() + 8 * 3600 + busNo.getDepartTime();
        Integer end = (int) busNoPlan.getDay().toInstant().atOffset(ZoneOffset.ofHours(8)).toEpochSecond() + 8 * 3600 + busNo.getArriveTime();
        //车辆校验
        Optional<Bus> busOptional = iBusBiz.findBusById(busNoPlan.getBus().getId());
        Bus bus;
        if(busOptional.isPresent()&&iBusBiz.isAvailable(busOptional.get().getId())){
            bus=busOptional.get();
        }else{
            throw new BaseSystemException(TravelTransportServiceError.BUS_NOT_ONLINE);
        }
        if (!busNoPlanBiz.isBusAvailable(bus.getLicense(), start, end))
            throw new BaseSystemException(TravelTransportServiceError.BUS_BUS_HAS_BEEN_DISTRIBUTION);

        // 行车助理校验
        if (busNoPlan.getAssistant() != null && busNoPlan.getAssistant().getId() != null) {
            Assistant assistant = assistantBiz.getAssistantById(busNoPlan.getAssistant().getId());
            if (!busNoPlanBiz.isAssistantAvailable(assistant.getName(), start, end))
                throw new BaseSystemException(TravelTransportServiceError.BUS_ASSISTANT_HAS_BEEN_DISTRIBUTE);
        }

        busNoPlan = busNoPlanBiz.addBusNoPlan(busNoPlan);
        Optional<Stock> optional = stockBiz.getStock(busNoPlan.getNo(), busNoPlan.getDay(), true);
        if (optional.isPresent()) {
            Stock stock = optional.get();
            stock.setQuantity(stock.getQuantity() + busNoPlan.getCapacity());
            stockBiz.updateStock(stock);
        } else {
            Stock stock = new Stock(InventoryType.bus_no, busNoPlan.getNo().getId(), busNoPlan.getDay(), busNoPlan.getCapacity(), BigDecimal.ZERO, true);
            stockBiz.addStock(stock);
        }
        return Optional.ofNullable(busNoPlan);
    }


    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Optional<BusNoPlan> updateBusNoPlan(BusNoPlan busNoPlan) {
        verifyBusNoPlan(busNoPlan);
        if (!busNoPlanBiz.isExisted(busNoPlan.getId()))
            throw new BaseSystemException(TravelTransportServiceError.BUS_NO_PLAN_NOT_EXIST);

        Optional<BusNo> busNoOptional = iBusNoBiz.findBusNoById(busNoPlan.getNo().getId());
        BusNo busNoNow;
        if (busNoOptional.isPresent() && iBusNoBiz.isAvailable(busNoOptional.get().getId())) {
            busNoNow = busNoOptional.get();
        } else {
            throw new BaseSystemException(TravelTransportServiceError.BUS_NO_STOPED);
        }

        Integer start = (int) busNoPlan.getDay().toInstant().atOffset(ZoneOffset.ofHours(8)).toEpochSecond() + 8 * 3600 + busNoNow.getDepartTime();
        Integer end = (int) busNoPlan.getDay().toInstant().atOffset(ZoneOffset.ofHours(8)).toEpochSecond() + 8 * 3600 + busNoNow.getArriveTime();

        BusNoPlan original = busNoPlanBiz.getBusNoPlanById(busNoPlan.getId());
        if (!original.getBus().getId().equals(busNoPlan.getBus().getId())) {
            Optional<Bus> busOptional = iBusBiz.findBusById(busNoPlan.getBus().getId());
            Bus bus;
            if(busOptional.isPresent()&&iBusBiz.isAvailable(busOptional.get().getId())){
                bus=busOptional.get();
            }else{
                throw new BaseSystemException(TravelTransportServiceError.BUS_NOT_ONLINE);
            }
            if (!busNoPlanBiz.isBusAvailable(bus.getLicense(), start, end))
                throw new BaseSystemException(TravelTransportServiceError.BUS_BUS_HAS_BEEN_DISTRIBUTION);
        }

        if (busNoPlan.getAssistant() != null) {
            if ((original.getAssistant() == null || !original.getAssistant().getId().equals(busNoPlan.getAssistant().getId()))) {
                Assistant assistant = assistantBiz.getAssistantById(busNoPlan.getAssistant().getId());
                if (!busNoPlanBiz.isAssistantAvailable(assistant.getName(), start, end))
                    throw new BaseSystemException(TravelTransportServiceError.BUS_ASSISTANT_HAS_BEEN_DISTRIBUTE);
            }
        }

        //判断更新的bus是否能装下现有已经分配的座位
        List<CertIdentity> certIdentities = certBiz.validCertIdentity(busNoPlan, original);

        busNoPlan = busNoPlanBiz.updateBusNoPlan(busNoPlan);
        //更新库存
        Date date = original.getDay();
        BusNo busNo = original.getNo();
        Integer originCapacity = original.getCapacity();
        Optional<Stock> optional = stockBiz.getStock(busNo, date, true);
        if (optional.isPresent()) {
            Stock stock = optional.get();
            stock.setQuantity(stock.getQuantity() + (busNoPlan.getCapacity() - originCapacity));
            stockBiz.updateStock(stock);
        } else {
            Stock stock = new Stock(InventoryType.bus_no, busNoPlan.getNo().getId(), date, busNoPlan.getCapacity() - originCapacity, BigDecimal.valueOf(-1), false);
            stockBiz.addStock(stock);
        }

        //换车
        certBiz.updateCertIdentity(certIdentities, busNoPlan, original);
        return Optional.ofNullable(busNoPlan);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean deleteBusNoPlanById(Long id) {
        if (!busNoPlanBiz.isExisted(id))
            throw new BaseSystemException(TravelTransportServiceError.BUS_NO_PLAN_NOT_EXIST);
        BusNoPlan busNoPlan = busNoPlanBiz.getBusNoPlanById(id);
        List<CertIdentity> certIdentities = getCertIdentitesByBusNoPlan(busNoPlan);
        if (certIdentities != null && !certIdentities.isEmpty())
            throw new BaseSystemException(TravelTransportBizError.BUS_NO_PLAN_DELETE_FAILD);
        busNoPlanBiz.deleteBusNoPlanById(id);
        Optional<Stock> optional = stockBiz.getStock(busNoPlan.getNo(), busNoPlan.getDay(), true);
        if (optional.isPresent()) {
            Stock stock = optional.get();
            stock.setQuantity(stock.getQuantity() - busNoPlan.getCapacity());
            stockBiz.updateStock(stock);
        }
        return true;
    }

    //查询该车次计划中的旅客
    private List<CertIdentity> getCertIdentitesByBusNoPlan(BusNoPlan busNoPlan) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("busNo.id", busNoPlan.getNo().getId());
        conditions.put("bus.id", busNoPlan.getBus().getId());
        conditions.put("date", busNoPlan.getDay());
        List<CertIdentity> certIdentities = certBiz.findCertIdentityByConditions(conditions);
        return certIdentities;
    }

    private Integer getBusNoPlanSoldQuantity(BusNoPlan busNoPlan) {
        return getCertIdentitesByBusNoPlan(busNoPlan).size();
    }

    @Override
    public Optional<BusNoPlanBo> findBusNoPlanById(Long id) {
        if (id == null) throw new BaseSystemException(TravelTransportServiceError.BUS_NO_PLAN_NULL);
        BusNoPlan busNoPlan = busNoPlanBiz.getBusNoPlanById(id);
        if (busNoPlan == null) throw new BaseSystemException(TravelTransportServiceError.BUS_NO_PLAN_NOT_EXIST);
        BusNoPlanBo busNoPlanBo = new BusNoPlanBo(busNoPlan, getBusNoPlanSoldQuantity(busNoPlan));
        return Optional.ofNullable(busNoPlanBo);
    }

    @Override
    public List<BusNoPlanBo> findBusNoPlanByConditions(Map<String, Object> conditions, String orderByFieldName, IBaseDao.SortBy sortBy) {
        List<BusNoPlan> busNoPlanList = busNoPlanBiz.findBusNoPlanByConditions(conditions, orderByFieldName, sortBy);
        List<BusNoPlanBo> busNoPlanBos = busNoPlanList.stream().map(n ->
                new BusNoPlanBo(n, getBusNoPlanSoldQuantity(n))
        ).collect(Collectors.toList());
        return busNoPlanBos;
    }

    @Override
    public List<BusNoPlanBo> findBusNoPlanByConditionsAndDate(Map<String, Object> conditions, Date startDate, Date endDate, String orderFyFieldName, IBaseDao.SortBy sortBy) {
        List<BusNoPlan> busNoPlanList = busNoPlanBiz.findBusNoPlanByConditionsAndDate(conditions, startDate, endDate, orderFyFieldName, sortBy);
        List<BusNoPlanBo> busNoPlanBos = busNoPlanList.stream().map(n ->
                new BusNoPlanBo(n, getBusNoPlanSoldQuantity(n))
        ).collect(Collectors.toList());
        return busNoPlanBos;
    }

    @Override
    public DomainPageBo<BusNoPlanBo> findBusNoPlanPagesByConditions(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, Integer pageIndex, Integer pageSize, boolean byLike) {
        DomainPage<BusNoPlan> domainPage = busNoPlanBiz.findBusNoPlanPagesByConditions(conditions, orderByFiledName, orderBy, pageIndex, pageSize, byLike);
        DomainPageBo<BusNoPlanBo> domainPageBo = new DomainPageBo<>(domainPage);
        List<BusNoPlanBo> busNoPlanBos = domainPage.getDomains().stream().map(n ->
                new BusNoPlanBo(n, getBusNoPlanSoldQuantity(n))
        ).collect(Collectors.toList());
        domainPageBo.setDomains(busNoPlanBos);
        return domainPageBo;
    }

    @Override
    public int findAvailableBusSeat(Map<String, Object> conditions) {
        return busNoPlanBiz.findAvailableBusSeat(conditions);
    }

    //endregion

    private void verifyLine(Line line) {
        if (line == null || line.getName() == null || line.getName().isEmpty())
            throw new BaseSystemException(TravelTransportServiceError.BUS_LINE_NULL);
        if (line.getBusNoSeries() == null)
            throw new BaseSystemException(TravelTransportServiceError.BUS_NO_SERIAL_NULL);

        for (BusNoSerial busNoSerial : line.getBusNoSeries()) {
            if (busNoSerial.getBusNo() == null)
                throw new BaseSystemException(TravelTransportServiceError.BUS_NO_NO_FOR_NULL);

            if (!iBusNoBiz.isExisted(busNoSerial.getBusNo().getId()))
                throw new BaseSystemException(TravelTransportServiceError.BUS_NO_NOT_EXIST);
            if (!iBusNoBiz.isAvailable(busNoSerial.getBusNo().getId()))
                throw new BaseSystemException(TravelTransportServiceError.BUS_NO_STOPED);
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Optional<Line> addLine(Line line) {
        verifyLine(line);
        return iLineBiz.addLine(line);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Optional<Line> updateLine(Line line) {
        verifyLine(line);
        return iLineBiz.updateLine(line);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean doDeleteLineById(Line line) {
        if (line == null || line.getId() == null)
            throw new BaseSystemException(TravelTransportServiceError.BUS_LINE_NULL);
        return iLineBiz.deleteLineById(line);
    }

    @Override
    public boolean deleteLineById(Line line) {
        try {
            return doDeleteLineById(line);
        } catch (DataIntegrityViolationException exception) {
            throw new BaseSystemException(TravelTransportServiceError.EXIST_FOREIGNKEY_CONSTRAINT_IN_DB);
        }
    }

    @Override
    public Optional<Line> findLineById(Long id) {
        if (id == null) throw new BaseSystemException(TravelTransportServiceError.BUS_LINE_NULL);
        return iLineBiz.findLineById(id);
    }

    @Override
    public List<Line> findLineByConditions(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy sortBy) {
        return iLineBiz.findLineByConditions(conditions, orderByFiledName, sortBy);
    }

    @Override
    public DomainPage<Line> findLinePagesByConditions(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy sortBy, Integer pageIndex, Integer pageSize, boolean byLike) {
        return iLineBiz.findLinePagesByConditions(conditions, orderByFiledName, sortBy, pageIndex, pageSize, byLike);
    }

    @Override
    public DomainPage findLinesPageByConditions(AndConditionSet condition, int page, int pageSize) {
        return iLineBiz.findLinesPageByConditions(condition, page, pageSize);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Optional<NodeStatus> addNodeStatus(NodeStatus nodeStatus) {
        if (nodeStatus == null) throw new BaseSystemException(TravelTransportServiceError.BUS_NODE_STATUS_NULL);
        return iNodeStatusBiz.addNodeStatus(nodeStatus);
    }

    @Override
    public DomainPage<NodeStatus> findNodeStatusPagesByConditions(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy sortBy, Integer pageIndex, Integer pageSize, boolean byLike) {
        return iNodeStatusBiz.findNodeStatusPagesByConditions(conditions, orderByFiledName, sortBy, pageIndex, pageSize, byLike);
    }

    @Override
    public List<NodeStatus> findNodeStatusByConditions(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy sortBy) {
        return iNodeStatusBiz.findNodeStatusByConditions(conditions, orderByFiledName, sortBy);
    }

    @Override
    public Optional<NodeType> findNodeTypeById(Long id) {
        if (id == null) throw new BaseSystemException(TravelTransportServiceError.BUS_NODE_TYPE_NULL);
        return iNodeTypeBiz.findNodeTypeById(id);
    }

    @Override
    public DomainPage<NodeType> findNodeTypePageByConditions(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, Integer pageIndex, Integer pageSize, boolean byLike) {
        return iNodeTypeBiz.findNodeTypePageByConditions(conditions, orderByFiledName, orderBy, pageIndex, pageSize, byLike);
    }

    @Override
    public List<NodeType> findNodeTypeByConditions(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy sortBy) {
        return iNodeTypeBiz.findNodeTypeByConditions(conditions, orderByFiledName, sortBy);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Optional<SeatLayout> addSeatLayout(SeatLayout seatLayout) {
        if (seatLayout == null || seatLayout.getName() == null || seatLayout.getName().isEmpty()) {
            throw new BaseSystemException(TravelTransportServiceError.BUS_SEAT_LAYOUT_NULL);
        }
        return iSeatLayoutBiz.addSeatLayout(seatLayout);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Boolean deleteSeatLayout(SeatLayout seatLayout) {
        if (seatLayout == null || seatLayout.getId() == null) {
            throw new BaseSystemException(TravelTransportServiceError.BUS_SEAT_LAYOUT_NULL);
        }
        return iSeatLayoutBiz.deleteSeatLayout(seatLayout);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Optional<SeatLayout> updateSeatLayout(SeatLayout seatLayout) {
        if (seatLayout == null || seatLayout.getId() == null || seatLayout.getName() == null || seatLayout.getName().isEmpty()) {
            throw new BaseSystemException(TravelTransportServiceError.BUS_SEAT_LAYOUT_NULL);
        }
        return iSeatLayoutBiz.updateSeatLayout(seatLayout);
    }

    @Override
    public Optional<SeatLayout> findLayoutById(Long id) {
        if (id == null) {
            throw new BaseSystemException(TravelTransportServiceError.BUS_SEAT_LAYOUT_NULL);
        }
        return iSeatLayoutBiz.findLayoutById(id);
    }

    @Override
    public DomainPage<SeatLayout> findLayOutPagesByConditions(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, Integer pageIndex, Integer pageSize, boolean byLike) {
        return iSeatLayoutBiz.findLayOutPagesByConditions(conditions, orderByFiledName, orderBy, pageIndex, pageSize, byLike);
    }

    @Override
    public DomainPage<Assistant> findAvailableAssistant(String assistantName, Long busNoId, Date date, int pageIndex, int pageSize) {
        if (busNoId == null || date == null)
            throw new BaseSystemException(TravelTransportServiceError.BUS_BUS_NO_ID_AND_DAY_NULL);
        return assistantBiz.findAvailableAssistant(assistantName, busNoId, date, pageIndex, pageSize);
    }

    /**
     * 判断车次的一致性
     */
    private void judgeBusNoConsistency(BusNoBo busNoBo) {
        //busNo相关的节点一致性的判断
        if (busNoBo.getNodeBos() != null && !busNoBo.getNodeBos().isEmpty()) {
            for (NodeBo nodeBo : busNoBo.getNodeBos()) {
                if (nodeBo.getDestination() == null || nodeBo.getDestination().getId() == null ||
                        !iDestinationBiz.isExisted(nodeBo.getDestination().getId())) {
                    throw new BaseSystemException(TravelTransportServiceError.BUS_NODE_DESTINATION_NOT_EXISTED);
                }
                if (nodeBo.getNodeType() == null || nodeBo.getNodeType().getId() == null ||
                        !iNodeTypeBiz.isExisted(nodeBo.getNodeType().getId())) {
                    throw new BaseSystemException(TravelTransportServiceError.BUS_NODE_TYPE_NOT_EXISTED);
                }
            }
        }
    }
}
