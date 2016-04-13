package com.simpletour.dao.company;

import com.simpletour.common.core.dao.IBaseDao;

/**
 * Created by zt on 15/10/19.
 */
public interface IEmployeeDao extends IBaseDao {
    /**
     * 根据companyId查找当前属于该公司jobNo最大的员工
     *
     * @param companyId 公司id
     * @return
     */
    Integer queryEmployeeByComanyWithJobNoMax(Long companyId);
}
