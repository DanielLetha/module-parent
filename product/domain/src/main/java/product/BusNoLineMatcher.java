package product;

import com.simpletour.domain.traveltrans.BusNoSerial;
import com.simpletour.domain.traveltrans.Line;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangdongfeng on 2015/11/30.
 */

/**
 * 根据线路(Line)以及线路对应的车次序列(List<BusNoSerial>)，匹配行程线路(List<TourismRoute>)
 */
public class BusNoLineMatcher {

    private int index, size, pos;   // index 线路剩余未匹配的起始索引
    // size 线路对应车次序列的大小
    // pos 每次匹配成功，更行pos，一次匹配成功后，则表示匹配成功的最后一个车次所在索引

    private Line line;

    private List<BusNoSerial> busNoSeries;

    BusNoLineMatcher(Line line) {
        index = pos = 0;
        this.size = line.getBusNoSeries() == null ? 0 : line.getBusNoSeries().size();
        this.busNoSeries = line.getBusNoSeries();
        this.line = line;
    }

    /**
     * 查找匹配的车次序列
     *
     * @param routes
     * @return
     */
    public List<TourismRoute> find(List<TourismRoute> routes) {
        Integer offset = 0, i = pos = index;
        List<TourismRoute> result = new ArrayList<>();
        for (int j = 0; i < size && j < routes.size(); ++i) {
            if (busNoSeries.get(i).getBusNo().getId().longValue() == routes.get(j).getBusNo().getId().longValue()) {     // 如果线路车次序列当前车次和当前route车次相等
                if (j == 0)
                    offset = routes.get(j).getOffset() - busNoSeries.get(i).getDay();       //如果是当前待匹配routes的第一个route，则设置routes对应车次序列的偏移
                if (routes.get(j).getOffset() - busNoSeries.get(i).getDay() == offset) {      //要求当前待匹配routes中，匹配到的每一个route对应车次序列的偏移要相同，否则匹配结束
                    routes.get(j).setLine(line);        //匹配成功，设置route.line route.lineOffset
                    routes.get(j).setLineOffset(offset);
                    result.add(routes.get(j));          // 匹配成功，加入到结果中
                    pos = i;
                    ++j;
                } else if (j > 0) break;
            }
        }
        return result;
    }

    public void move() {
        index = pos + 1;
    }
}

