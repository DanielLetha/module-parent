package com.simpletour.biz.traveltrans;

import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.domain.traveltrans.Assistant;

import java.util.Date;

/**
 * Created by Mario on 2015/12/25.
 */
public interface IAssistantBiz {

    /**
     * 查询可用的行车助理
     *
     * @param assistantName 行车助理的名字
     * @param busNoId       车次id
     * @param date          具体哪一天
     * @param pageIndex     页码
     * @param pageSize      分页大小
     * @return
     */
    DomainPage<Assistant> findAvailableAssistant(String assistantName, Long busNoId, Date date, int pageIndex, int pageSize);

    /**
     * 依据id获取行车助理
     * @param id
     * @return
     */
    Assistant getAssistantById(Long id);

    /**
     * 判断行车助理是否存在
     * @param id
     * @return
     */
    boolean isExisted(Long id);

    /**
     * 判断行车助理是否存在并可用
     * @param id
     * @return
     */
    boolean isAvailable(Long id);
}
