package product;


import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.simpletour.common.core.dao.query.QueryUtil;
import com.simpletour.common.core.domain.BaseDomain;
import com.simpletour.common.core.domain.DependEntity;
import com.simpletour.common.core.hibernate.dialect.LongArrayUserType;
import com.simpletour.domain.resources.Destination;
import com.simpletour.domain.traveltrans.BusNo;
import com.simpletour.domain.traveltrans.Line;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 行程路线
 * <p>
 * Created by Jeff.Song on 2015/11/21.
 */
@TypeDefs({@TypeDef(name = "bigint[]", typeClass = LongArrayUserType.class)})
@Entity
@Table(name = "PROD_TOURISM_ROUTE")
@JSONType(serialzeFeatures = SerializerFeature.DisableCircularReferenceDetect)
public class TourismRoute extends BaseDomain {

    public TourismRoute() {
    }

    public TourismRoute(BusNo busNo, Integer offset) {
        this.busNo = busNo;
        this.offset = offset;
    }

    public TourismRoute(Integer offset, Integer lineOffset, Line line, BusNo busNo) {
        this.offset = offset;
        this.lineOffset = lineOffset;
        this.line = line;
        this.busNo = busNo;
    }

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    /**
     * 属于哪个行程
     */
    @ManyToOne()
    @JoinColumn(name = "product_id")
    @JSONField(serialize = false)
    private Product product;

    /**
     * 行程路线（可能多个）
     * 本段包含的途径目的地序列id
     */
    @Type(type = "bigint[]")
    @Column(name = "routes")
    private Long[] routes;

    /**
     * 行程偏移日期(表示本段在整个行程中所属日期)
     * 0代表第一天、1代表第二天、2代表第三天，以此类推
     */
    @Column(name = "\"offset\"")
    private Integer offset;

    /**
     * 线路偏移日期
     */
    @Column
    private Integer lineOffset;

    /**
     * 车次线路
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id")
    private Line line;

    /**
     * 车次编号
     */
    @ManyToOne()
    @JoinColumn(name = "bus_no_id")
    private BusNo busNo;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long[] getRoutes() {
        return routes;
    }

    public void setRoutes(Long[] routes) {
        this.routes = routes;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLineOffset() {
        return lineOffset;
    }

    public void setLineOffset(Integer lineOffset) {
        this.lineOffset = lineOffset;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public BusNo getBusNo() {
        return busNo;
    }

    public void setBusNo(BusNo busNo) {
        this.busNo = busNo;
    }


    public static List<TourismRoute> decompose(List<Line> lines, List<TourismRoute> routes) {
        if (routes == null || routes.isEmpty()) return Collections.emptyList();//没有待匹配行程车次序列，则分解完成
        //获取车次线路匹配器
        List<BusNoLineMatcher> busNoLineMatchers = new ArrayList<>(lines.size());
        lines.forEach(line -> {
            BusNoLineMatcher matcher = new BusNoLineMatcher(line);
            busNoLineMatchers.add(matcher);
        });

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

    @JSONField(serialize = false)
    public List<DependEntity> getDependEntities() {
        List<DependEntity> dependEntities = new ArrayList<>();
        dependEntities.add(new DependEntity(QueryUtil.getTableName(Product.class), product.getId(), QueryUtil.getTableName(BusNo.class), busNo.getId()));
        dependEntities.add(new DependEntity(QueryUtil.getTableName(Product.class), product.getId(), QueryUtil.getTableName(Line.class), line.getId()));
        if (routes != null) {
            for (Long route : routes) {
                dependEntities.add(new DependEntity(QueryUtil.getTableName(Product.class), product.getId(), QueryUtil.getTableName(Destination.class), route));
            }
        }
        return dependEntities;
    }

}



