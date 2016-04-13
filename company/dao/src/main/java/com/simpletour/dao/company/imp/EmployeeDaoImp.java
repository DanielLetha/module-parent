package com.simpletour.dao.company.imp;

import com.simpletour.common.core.dao.IBaseDao;
import com.simpletour.common.core.dao.jpa.JPABaseDAO;
import com.simpletour.dao.company.IEmployeeDao;
import org.springframework.stereotype.Repository;

/**
 * Created by zt on 15/10/19.
 */
@Repository
public class EmployeeDaoImp extends JPABaseDAO implements IEmployeeDao, IBaseDao {

    @Override
    public Integer queryEmployeeByComanyWithJobNoMax(Long companyId) {
        String sql = "SELECT MAX(job_no) FROM sys_employee WHERE tenant_id= " + companyId;
        return (Integer) em.createNativeQuery(sql).getSingleResult();
    }
}
