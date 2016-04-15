package com.simpletour.dao.traveltrans.impl;

import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.dao.jpa.JPABaseDAO;
import com.simpletour.commons.data.dao.query.condition.AndConditionSet;
import com.simpletour.commons.data.dao.query.condition.Condition;
import com.simpletour.commons.data.dao.query.condition.FieldMatcher;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.dao.traveltrans.ITransportDao;
import com.simpletour.dao.traveltrans.util.Tuple;
import com.simpletour.domain.product.TourismRouteLine;
import com.simpletour.domain.traveltrans.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.Query;
import java.math.BigInteger;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Mario on 2015/12/2.
 */
@Repository
public class TransportDaoImpl extends JPABaseDAO implements ITransportDao, IBaseDao {

    @Resource
    IOrderDao orderDao;

    static FieldMatcher lineFieldMatcher = new FieldMatcher();

    static {
        lineFieldMatcher.put("depart", "departtbl.depart")
                .put("arrive", "arrivetbl.arrive")
                .put("days", "daystbl.days")
                .put("tenantId", "line.tenant_id")
                .put("name", "line.name");
    }

    @Override
    public DomainPage findLinesPageByConditions(AndConditionSet condition, int page, int pageSize) {
        String selectStr = "select line.*, departtbl.depart, arrivetbl.arrive, daystbl.days ";
        String countStr = "select count(line.id) ";
        String fromStr = "from trans_line line LEFT JOIN" +
                " (select DISTINCT on (line.id) line.id as id, bno.depart as depart" +
                " from trans_line line LEFT JOIN trans_busnoserial bns on line.id = bns.line_id LEFT JOIN trans_busno bno on bns.bus_no_id = bno.id" +
                " order by line.id, bns.sort) departtbl on line.id = departtbl.id" +
                " LEFT JOIN" +
                " (select DISTINCT on (line.id) line.id as id, bno.arrive as arrive" +
                " from trans_line line LEFT JOIN trans_busnoserial bns on line.id = bns.line_id LEFT JOIN trans_busno bno on bns.bus_no_id = bno.id" +
                " order by line.id, bns.sort desc) arrivetbl on line.id = arrivetbl.id" +
                " LEFT JOIN" +
                " (select line.id as id, max(bns.day) as days" +
                " from trans_line line LEFT JOIN trans_busnoserial bns on line.id = bns.line_id" +
                " GROUP BY line.id order by line.id) daystbl on line.id = daystbl.id ";
        condition.setFieldMatcher(lineFieldMatcher);
        condition.addCondition("tenantId", getTenantId());

        String sql = selectStr + fromStr + "where " + condition + " order by line.id desc";
        Query query = em.createNativeQuery(sql, Line.class);
        condition.setParameter(query, 1);
        query.setFirstResult((page - 1) * pageSize);
        query.setMaxResults(pageSize);
        List<Line> reslist = query.getResultList();

        sql = countStr + fromStr + "where " + condition;
        query = em.createNativeQuery(sql);
        condition.setParameter(query, 1);
        BigInteger count = (BigInteger) query.getSingleResult();
        DomainPage<Line> res = new DomainPage<>(pageSize, page, count.longValue());
        res.setDomains(reslist);
        return res;
    }

    @Override
    @Transactional
    public void deleteBusNoSerialByLineId(Long id) {
        String hql = "DELETE FROM TRANS_BUSNOSERIAL WHERE line_id =:lineId";
        em.createNativeQuery(hql).setParameter("lineId", id).executeUpdate();
    }

    @Override
    public List<Node> findNodeListByBusNo(BusNo busNo) {
        StringBuffer hql = new StringBuffer(" SELECT n FROM ");
        hql.append(Node.class.getName());
        hql.append(" n WHERE n.busNo.id =:id ORDER BY n.day ASC,n.timing ASC ");
        return em.createQuery(hql.toString()).setParameter("id", busNo.getId()).getResultList();
    }

    @Override
    public List<BusNoPlan> findBusNoPlanByConditionsAndDate(Map<String, Object> conditions, Date startDate, Date endDate, String orderByFieldName, SortBy sortBy) {
        StringBuffer buffer = new StringBuffer("SELECT c FROM ").append(BusNoPlan.class.getName()).append(" c WHERE ");
        Set<String> fieldNames = conditions.keySet();
        Iterator<String> iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            if (i == 1) {
                buffer.append(" c.").append(fieldName).append(" = ?").append(i);
            } else {
                buffer.append(" AND c.").append(fieldName).append(" = ?").append(i);
            }
        }
        int count = fieldNames.size();
        if (count > 0) {
            buffer.append(" AND ");
        }
        buffer.append(" c.day >=:startDate ").append(" AND c.day <=:endDate ");
        buffer.append(" ORDER BY c.").append(orderByFieldName).append(" ").append(sortBy.name());
        Query query = em.createQuery(buffer.toString());
        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            query.setParameter(i, conditions.get(fieldName));
        }
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }

    public List<BusNoPlan> getBusNoPlanByBusNoAndSpan(BusNo busNo, Date offday, Date uptoDay) {
        AndConditionSet conditionSet = new AndConditionSet();
        conditionSet.addCondition("c.no", busNo)
                .addCondition("c.day", offday, Condition.MatchType.greaterOrEqual)
                .addCondition("c.day", uptoDay, Condition.MatchType.lessOrEqual);
        return getEntitiesByCondition(BusNoPlan.class, conditionSet);
    }

    public List<BusNoPlan> getBusNoPlanByBusNoAndDate(BusNo busNo, Date day) {
        HashMap<String, Object> conditions = new HashMap<>(2);
        conditions.put("no", busNo);
        conditions.put("day", day);
        return getEntitiesByFieldList(BusNoPlan.class, conditions, "id", SortBy.ASC);
    }

    private List<BusNo.BusNoSpanCapacity> getBusNoSpanCapacitiesOnLine(Line line) {
        return line.getBusNoSeries().stream().map(busNoSerial -> {// 获取线路上每一个车次的排班数据(哪一个车次在第几天，是否可换乘)
            BusNo busNo = busNoSerial.getBusNo();
            return new BusNo.BusNoSpanCapacity(busNo, busNoSerial.getDay(), busNo.isTransferable());
        }).collect(Collectors.toList());
    }

    private List<BusNo.BusNoCapacity> getBusNoCapacitiesOnLine(Line line) {
        return line.getBusNoSeries().stream().map(busNoSerial -> {// 获取线路上每一个车次的排班数据(哪一个车次在第几天，是否可换乘)
            BusNo busNo = busNoSerial.getBusNo();
            return new BusNo.BusNoCapacity(busNo, busNoSerial.getDay(), busNo.isTransferable());
        }).collect(Collectors.toList());
    }

    private BusNoPlanCapacityHelper.BusNoSeriesSpanCapacity fillBusNoSpanUsedCapacities(List<BusNo.BusNoSpanCapacity> busNoSpanCapacities, Date offDay, Date uptoDay) {
        busNoSpanCapacities.forEach(busNoSpanCapacity -> {// 计算当前TourismRouteLine对应的每一个BusNo的容量
            // 根据BusNo, 日期，获取到BusNoPlans(某天的某个车次的排班情况)
            List<BusNoPlan> busNoPlans = getBusNoPlanByBusNoAndSpan(busNoSpanCapacity.getBusNoId(),
                    Date.from(offDay.toInstant().plus(busNoSpanCapacity.getOffset(), ChronoUnit.DAYS)),
                    Date.from(uptoDay.toInstant().plus(busNoSpanCapacity.getOffset(), ChronoUnit.DAYS)));

            if (!(busNoPlans == null || busNoPlans.isEmpty())) {
                // 按照日期分组
                Map<Date, List<BusNoPlan>> groupingBusNoPlans = busNoPlans.stream().collect(Collectors.groupingBy(BusNoPlan::getDay));

                Map<Date, List<BusNo.BusCapacity>> capacities = groupingBusNoPlans.entrySet().stream().map(busNoPlen -> { // 查询每一天各个班次的容量信息
                    // 根据车次，车，日期，查询凭证数量，作为已消耗容量(某天，某个车次的某辆车上销售的凭证数)
                    List<BusNo.BusCapacity> busCapacities = busNoPlen.getValue().stream().map(busNoPlan -> {
                        List<CertIdentity> certIdentities = orderDao.getCertIdentitiesByConditions(busNoSpanCapacity.getBusNoId(), busNoPlan.getBus(), busNoPlan.getDay());
                        return new BusNo.BusCapacity(busNoPlan.getBus(), busNoPlan.getCapacity(), (certIdentities == null || certIdentities.isEmpty()) ? 0 : certIdentities.size());
                    }).collect(Collectors.toList());
                    return new Tuple<>(busNoPlen.getKey(), busCapacities);
                }).collect(Collectors.toMap(Tuple::getFirst, Tuple::getSecond));

                busNoSpanCapacity.setBusCapacities(capacities);   // 填充车次数据(车次，是否可换成，当前车次的所有容量信息)
            }
        });
        return new BusNoPlanCapacityHelper.BusNoSeriesSpanCapacity(busNoSpanCapacities);
    }

    private List<BusNo.BusNoCapacity> fillBusNoUsedCapacities(List<BusNo.BusNoCapacity> busNoCapacities, Date offDay) {
        return busNoCapacities.stream().map(busNoCapacity -> {// 计算当前TourismRouteLine对应的每一个BusNo的容量
            // 根据BusNo, 日期，获取到BusNoPlans(某天的某个车次的排班情况)
            List<BusNoPlan> busNoPlans = getBusNoPlanByBusNoAndDate(busNoCapacity.getBusNoId(), Date.from(offDay.toInstant().plus(busNoCapacity.getOffset(), ChronoUnit.DAYS)));
            List<BusNo.BusCapacity> capacities = busNoPlans.stream().map(busNoPlan -> { // 查询每一个班次的容量信息
                // 根据车次，车，日期，查询凭证数量，作为已消耗容量(某天，某个车次的某辆车上销售的凭证数)
                List<CertIdentity> certIdentities = orderDao.getCertIdentitiesByConditions(busNoCapacity.getBusNoId(), busNoPlan.getBus(), Date.from(offDay.toInstant().plus(busNoCapacity.getOffset(), ChronoUnit.DAYS)));
                return new BusNo.BusCapacity(busNoPlan.getBus(), busNoPlan.getCapacity(), (certIdentities == null || certIdentities.isEmpty()) ? 0 : certIdentities.size());
            }).collect(Collectors.toList());

            return new BusNo.BusNoCapacity(busNoCapacity.getBusNoId(), busNoCapacity.isTransferable(), capacities);   // 填充车次数据(车次，是否可换成，当前车次的所有容量信息)
        }).collect(Collectors.toList());
    }


    public Map<Date, Integer> findTourismRouteBusNoPlanCapacity(TourismRouteLine tourismRouteLine, Date start, Date end) {
        Line line = tourismRouteLine.getLine();
        Date offDay = Date.from(start.toInstant().plus(tourismRouteLine.getOffset(), ChronoUnit.DAYS)),
                uptoDay = Date.from(end.toInstant().plus(tourismRouteLine.getOffset(), ChronoUnit.DAYS));// 计算当前TourismRouteLine对应的日期

        //=========================================这一段代码就是获取和填充该行程线路上的车次在day这一天的所有班次容量信息==================================================================
        // 获取线路上所有车次容量的基本信息(车次，偏移日期，是否可换乘)
        List<BusNo.BusNoSpanCapacity> lineBusNoCapacities = getBusNoSpanCapacitiesOnLine(line);
        // 挑选出该行程对应在线路里的所有车次 作为本段行程在该线路上的 车次容量信息
        List<BusNo.BusNoSpanCapacity> tourismBusNoCapacities = BusNoPlanCapacityHelper.pick1(lineBusNoCapacities, tourismRouteLine);    //获取当前TourismRouteLine对应的线路上的 车次排班数据(可以认为是lineDiagrams的子集)(正确性???)
        if (tourismBusNoCapacities.isEmpty()) return Collections.emptyMap();
        // 填充已使用的容量
        BusNoPlanCapacityHelper.BusNoSeriesSpanCapacity tourismSpanBusNoCapacities = fillBusNoSpanUsedCapacities(tourismBusNoCapacities, offDay, uptoDay);
        tourismSpanBusNoCapacities.setTourismRouteLine(tourismRouteLine);
        //=========================================这一段代码就是获取和填充该行程线路上的车次在day这一天的所有班次容量信息==================================================================

        Map<Date, Integer> tourismBusNoCapacityMap = new HashMap<>();

        Date temp = start;
        while (!temp.after(end)) {
            Map<Date, List<BusNo.BusNoCapacity>> slice = tourismSpanBusNoCapacities.getBusNoSeriesCapacitySlice(temp);
            if (!slice.isEmpty()) {
                // 然后根据容量信息，获得整个TourismRouteLine的最小容量, 作为该段行程在day这一天的所有班次的剩余容量
                Integer result = BusNoPlanCapacityHelper.recursive(slice.get(temp).get(0).getBusCapacities(), slice.get(temp).get(0).isTransferable(), slice.get(temp).subList(1, slice.get(temp).size()));
                tourismBusNoCapacityMap.put(temp, result);
            } else {
                tourismBusNoCapacityMap.put(temp, 0);
            }
            temp = Date.from(temp.toInstant().plus(1, ChronoUnit.DAYS));
        }

        return tourismBusNoCapacityMap;
    }

    public Integer findTourismRouteBusNoPlanCapacity(TourismRouteLine tourismRouteLine, Date day) {
        Line line = tourismRouteLine.getLine();
        Date offDay = Date.from(day.toInstant().plus(tourismRouteLine.getOffset(), ChronoUnit.DAYS));// 计算当前TourismRouteLine对应的日期

        //=========================================这一段代码就是获取和填充该行程线路上的车次在day这一天的所有班次容量信息==================================================================
        // 获取线路上所有车次容量的基本信息(车次，偏移日期，是否可换乘)
        List<BusNo.BusNoCapacity> lineBusNoCapacities = getBusNoCapacitiesOnLine(line);
        // 挑选出该行程对应在线路里的所有车次 作为本段行程在该线路上的 车次容量信息
        List<BusNo.BusNoCapacity> tourismBusNoCapacities = BusNoPlanCapacityHelper.pick(lineBusNoCapacities, tourismRouteLine);    //获取当前TourismRouteLine对应的线路上的 车次排班数据(可以认为是lineDiagrams的子集)(正确性???)
        if (tourismBusNoCapacities.isEmpty())
            return 0;
        // 填充已使用的容量
        tourismBusNoCapacities = fillBusNoUsedCapacities(tourismBusNoCapacities, offDay);
        //=========================================这一段代码就是获取和填充该行程线路上的车次在day这一天的所有班次容量信息==================================================================

        // 然后根据容量信息，获得整个TourismRouteLine的最小容量, 作为该段行程在day这一天的所有班次的剩余容量
        if (tourismBusNoCapacities.size() > 1) {
            return BusNoPlanCapacityHelper.recursive(tourismBusNoCapacities.get(0).getBusCapacities(),
                    tourismBusNoCapacities.get(0).isTransferable(),
                    tourismBusNoCapacities.subList(1, tourismBusNoCapacities.size()));
        }
        // 如果只有一个车次， 则直接把所有的班次的剩余容量加起来就好了
        return tourismBusNoCapacities.get(0).getBusCapacities().stream().map(busCapacity -> busCapacity.getCapacity() - busCapacity.getUsedCapacity()).reduce(0, Integer::sum);
    }

    @Override
    public DomainPage<Bus> findAvailableBus(String license, Integer start, Integer end, int pageIndex, int pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;
        String nativeSql = this.checkBusAvailableSql(license);
        Map<String, Object> params = new HashMap<>();
        params.put("start", start);
        params.put("end", end);
        if (license != null && !license.isEmpty()) {
            params.put("license", "%" + license + "%");
        }
        Query query = this.createNativeQuery(nativeSql.toString(), Bus.class, params);
        query.setFirstResult(((pageIndex - 1) * pageSize));
        query.setMaxResults(pageSize);
        DomainPage domainPage = new DomainPage(pageSize, pageIndex, this.getTotalCount(nativeSql.toString(), params));
        domainPage.setDomains(query.getResultList());
        return domainPage;
    }

    @Override
    public boolean isBusAvailable(String license, Integer start, Integer end) {
        String nativeSql = this.checkBusAvailableSql(license);
        Map<String, Object> params = new HashMap<>();
        params.put("start", start);
        params.put("end", end);
        if (license != null && !license.isEmpty()) {
            params.put("license", "%" + license + "%");
        }
        long totalCount = this.getTotalCount(nativeSql.toString(), params);
        return totalCount > 0 ? true : false;
    }

    /**
     * 核查是否有可用车辆SQL
     *
     * @param license 车牌号
     * @return nativeSql
     * @author 白飞龙
     * @since 1.0-fix
     */
    private String checkBusAvailableSql(String license) {
        Long tenantId = getTenantId();
        StringBuffer sql = new StringBuffer("select s.* from trans_bus s");
        sql.append(" left join (");
        sql.append("select distinct n.bus_id from trans_busnoplan n");
        sql.append(" left join trans_busno o on o.id = n.bus_no_id");
        sql.append(" where");
        sql.append(" ((extract(epoch from n.day) + o.arrive_time) > :start and (extract(epoch from n.day) + o.arrive_time) <= :end)");
        sql.append(" or");
        sql.append(" ((extract(epoch from n.day) + o.depart_time) >= :start and (extract(epoch from n.day) + o.depart_time) < :end)");
        sql.append(")e on s.id = e.bus_id");
        sql.append(" where s.online");
        sql.append(" and e.bus_id is null");
        sql.append(" and s.tenant_id " + (tenantId != null ? ("= " + tenantId) : "is null"));
        if (license != null && !license.isEmpty()) {
            sql.append(" and s.license like :license");
        }
        return sql.toString();
    }

    @Override
    public DomainPage<Assistant> findAvailableAssistant(String assistantName, Integer start, Integer end, int pageIndex, int pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;
        String nativeSql = this.checkAssistantAvailavleSql(assistantName);
        Map<String, Object> params = new HashMap<>();
        params.put("start", start);
        params.put("end", end);
        if (assistantName != null && !assistantName.isEmpty()) {
            params.put("name", "%" + assistantName + "%");
        }
        Query query = this.createNativeQuery(nativeSql.toString(), Assistant.class, params);
        query.setFirstResult(((pageIndex - 1) * pageSize));
        query.setMaxResults(pageSize);
        DomainPage domainPage = new DomainPage(pageSize, pageIndex, this.getTotalCount(nativeSql.toString(), params));
        domainPage.setDomains(query.getResultList());
        return domainPage;
    }

    @Override
    public boolean isAssistantAvailable(String assistantName, Integer start, Integer end) {
        String nativeSql = this.checkAssistantAvailavleSql(assistantName);
        Map<String, Object> params = new HashMap<>();
        params.put("start", start);
        params.put("end", end);
        if (assistantName != null && !assistantName.isEmpty()) {
            params.put("name", "%" + assistantName + "%");
        }
        long totalCount = this.getTotalCount(nativeSql.toString(), params);
        return totalCount > 0 ? true : false;
    }

    /**
     * 核查是否有可用助理SQL
     *
     * @param name 助理名字
     * @return nativeSql
     * @author 白飞龙
     * @since 1.0-fix
     */
    private String checkAssistantAvailavleSql(String name) {
        Long tenantId = getTenantId();
        StringBuffer sql = new StringBuffer("select t.* from trans_assistant t");
        sql.append(" left join (");
        sql.append("select distinct n.assistant_id from trans_busnoplan n");
        sql.append(" left join trans_busno o on o.id = n.bus_no_id");
        sql.append(" where");
        sql.append(" ((extract(epoch from n.day) + o.arrive_time) > :start and (extract(epoch from n.day) + o.arrive_time) <= :end)");
        sql.append(" or");
        sql.append(" ((extract(epoch from n.day) + o.depart_time) >= :start and (extract(epoch from n.day) + o.depart_time) < :end)");
        sql.append(")tmp on t.id = tmp.assistant_id");
        sql.append(" where t.status = 'normal'");
        sql.append(" and t.del = false");
        sql.append(" and t.tenant_id " + (tenantId != null ? ("= " + tenantId) : "is null"));
        sql.append(" and tmp.assistant_id is null");
        if (name != null && !name.isEmpty()) {
            sql.append(" and t.name like :name");
        }
        return sql.toString();
    }

    @Override
    public DomainPage<BusNo> findAvailableBusNo(String no, int pageIndex, int pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;
        Long tenantId = getTenantId();
        Map<String, Object> params = new HashMap<>();
        StringBuffer nativeSql = new StringBuffer("select o.* from trans_busno o");
        nativeSql.append(" left join trans_busnoserial l on l.bus_no_id = o.id");
        nativeSql.append(" where l.id is null");
        nativeSql.append(" and o.tenant_id " + (tenantId != null ? ("= " + tenantId) : "is null"));
        if (no != null && !no.isEmpty()) {
            nativeSql.append(" and o.no like :no");
            params.put("no", "%" + no + "%");
        }
        Query query = this.createNativeQuery(nativeSql.toString(), BusNo.class, params);
        query.setFirstResult(((pageIndex - 1) * pageSize));
        query.setMaxResults(pageSize);
        DomainPage domainPage = new DomainPage(pageSize, pageIndex, this.getTotalCount(nativeSql.toString(), params));
        domainPage.setDomains(query.getResultList());
        return domainPage;
    }

    @Override
    public DomainPage<Node> findNodePageByBusNoId(List<Long> busNoIds, String orderByFieldName, SortBy sortBy, int pageIndex, int pageSize) {
        Long tenantId = getTenantId();
        StringBuffer tmpSql = new StringBuffer("select * from trans_node nd");
        tmpSql.append(" left join trans_busno bn on nd.bus_no_id = bn.id");
        tmpSql.append(" left join tr_destination dt on nd.destination_id = dt.id and bn.tenant_id = dt.tenant_id");
        tmpSql.append(" where bn.tenant_id " + (tenantId != null ? "= " + tenantId : "is null"));
        tmpSql.append(" and nd.bus_no_id in(");
        busNoIds.forEach(id -> tmpSql.append(id + ","));
        String sql = tmpSql.toString();
        if (sql.endsWith(",")) {
            sql = sql.substring(0, sql.length() - 1);
        }
        sql = sql + ") order by " + orderByFieldName + " " + sortBy;
        Query query = this.createNativeQuery(sql.toString(), Node.class, null);
        query.setFirstResult(((pageIndex - 1) * pageSize));
        query.setMaxResults(pageSize);
        DomainPage domainPage = new DomainPage(pageSize, pageIndex, this.getTotalCount(sql.toString(), null));
        domainPage.setDomains(query.getResultList());
        return domainPage;
    }

    /**
     * 组装查询条件
     *
     * @param nativeSql 命令格式占位符(:param)，不支持(?num)格式
     * @param clazz     Domain.class
     * @param params    参数键值对
     * @return Query 对象
     */
    private Query createNativeQuery(String nativeSql, Class<?> clazz, Map<String, Object> params) {
        Query query = em.createNativeQuery(nativeSql, clazz);
        if (params != null) {
            params.entrySet().forEach(entry -> query.setParameter(entry.getKey(), entry.getValue()));
        }
        return query;
    }

    /**
     * 查询总数
     *
     * @param nativeSql 命令格式占位符(:param)，不支持(?index)格式
     * @param params    参数键值对
     * @return Query 对象
     */
    private long getTotalCount(String nativeSql, Map<String, Object> params) {
        Query query = em.createNativeQuery("select count(1) from (" + nativeSql + ")tc");
        if (params != null) {
            params.entrySet().forEach(entry -> query.setParameter(entry.getKey(), entry.getValue()));
        }
        return super.getTotalCount(query).longValue();
    }

}




