package com.simpletour.service.resources.imp;

import com.simpletour.biz.resources.*;
import com.simpletour.biz.resources.vo.ProcurementVo;
import com.simpletour.commons.data.dao.IBaseDao;
import com.simpletour.commons.data.domain.BaseDomain;
import com.simpletour.commons.data.domain.DomainPage;
import com.simpletour.commons.data.exception.BaseSystemException;
import com.simpletour.dao.resources.IResourcesDao;
import com.simpletour.domain.resources.*;
import com.simpletour.service.resources.IResourcesService;
import com.simpletour.service.resources.error.ResourcesServiceError;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 文件描述：资源模块业务处理实现类
 * 创建人员：石广路
 * 创建日期：2015/11/25 14:27
 * 备注说明：将资源包名从travelresources改为resources
 * @Since 2.0.0-SNAPSHOT
 */
@Service
public class ResourcesServiceImp implements IResourcesService {
    @Resource
    private IResourcesBiz resourcesBiz;
    @Resource
    private ICateringBiz cateringBiz;
    @Resource
    private IOspBiz opsBiz;
    @Resource
    private IHotelBiz hotelBiz;
    @Resource
    private IScenicBiz scenicBiz;
    @Resource
    private IResourcesDao resourcesDao;
    @Resource
    private IDestinationBiz destinationBiz;
    @Resource
    private IEntertainmentBiz entertainmentBiz;
    @Resource
    private IProcurementBiz procurementBiz;

    @Override
    public Optional<Scenic> addScenic(Scenic scenic) throws BaseSystemException {
        checkEntityNotNull(scenic);
        checkDestinationNotNull(scenic.getDestination());
        checkDestinationExist(scenic.getDestination());
        checkParentScenicExist(scenic.getParent());
        return Optional.ofNullable(scenicBiz.addScenic(scenic));
    }

    @Override
    public Optional<Scenic> updateScenic(Scenic scenic) throws BaseSystemException {
        checkEntityNotNull(scenic);
        checkDestinationNotNull(scenic.getDestination());
        checkDestinationExist(scenic.getDestination());
        checkScenicIdValid(scenic);
        checkParentScenicExist(scenic.getParent());
        return Optional.ofNullable(scenicBiz.updateScenic(scenic));
    }

    @Override
    public void deleteScenic(long id) throws BaseSystemException {
        if (!scenicBiz.isExisted(id)) {
            throw new BaseSystemException(ResourcesServiceError.SCENIC_NOT_EXIST);
        }
        scenicBiz.deleteScenic(id);
    }

    @Override
    public Optional<Scenic> getScenicById(long id) {
        return Optional.ofNullable(scenicBiz.findScenicById(id));
    }

    @Override
    public long getScenicId(Scenic scenic) {
        if (scenic == null || scenic.getDestination() == null) {
            return 0L;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("del", Boolean.FALSE);
        params.put("name", scenic.getName());
        params.put("destination.id", scenic.getDestination().getId());
        List<Scenic> scenicList = scenicBiz.findScenicsPagesByConditions(params);
        if (!(scenicList == null || scenicList.isEmpty() || scenicList.size() != 1)) {
            return scenicList.get(0).getId();
        }
        return 0L;
    }

    @Override
    public DomainPage<Scenic> queryScenicsPagesByConditions(Map<String, Object> conditions, String orderByFiledName
            , IBaseDao.SortBy orderBy, int pageIndex, int pageSize, boolean byLike) {
        return scenicBiz.queryScenicsPagesByConditions(conditions, orderByFiledName, orderBy, pageIndex, pageSize, byLike);
    }

    @Override
    public Optional<OfflineServiceProvider> addOfflineServiceProvider(OfflineServiceProvider offlineServiceProvider) {
        if (offlineServiceProvider == null) {
            throw new BaseSystemException(ResourcesServiceError.EMPTY_ENTITY);
        }
        return Optional.ofNullable(opsBiz.addOfflineServiceProvider(offlineServiceProvider));
    }

    @Override
    public void deleteOfflineServiceProvider(long id) {
        opsBiz.deleteOfflineServiceProvider(id);
    }

    @Override
    public Optional<OfflineServiceProvider> updateOfflineServiceProvider(OfflineServiceProvider offlineServiceProvider) {
        if (offlineServiceProvider == null) {
            throw new BaseSystemException(ResourcesServiceError.EMPTY_ENTITY);
        }
        return Optional.ofNullable(opsBiz.updateOfflineServiceProvider(offlineServiceProvider));
    }

    @Override
    public DomainPage<OfflineServiceProvider> getOfflineServiceProvidersByPage(Boolean isDel, int page, int pageSize) {
        return opsBiz.getOfflineServiceProvidersByPage(isDel, page, pageSize);
    }

    @Override
    public DomainPage<OfflineServiceProvider> getOfflineServiceProvidersByConditionPage(String name, Boolean isDel, int page, int pageSize) {
        return opsBiz.getOfflineServiceProvidersByConditionPage(name, isDel, page, pageSize);
    }

    @Override
    public Optional<OfflineServiceProvider> getOfflineServiceProviderById(long id) {
        return Optional.ofNullable(opsBiz.getOfflineServiceProviderById(id));
    }


    @Override
    public Optional<Catering> addCatering(Catering catering) throws BaseSystemException {
        if (catering == null) {
            throw new BaseSystemException(ResourcesServiceError.EMPTY_ENTITY);
        }
        if(catering.getDestination() == null||catering.getDestination().getId() == null){
            throw new BaseSystemException(ResourcesServiceError.DESTINATION_NULL);
        }
        if(!destinationBiz.isExisted(catering.getDestination().getId())){
            throw new BaseSystemException(ResourcesServiceError.DESTINATION_NOT_EXIST);
        }
        return Optional.ofNullable(cateringBiz.addCatering(catering));
    }

    @Override
    public Optional<Catering> updateCatering(Catering catering) {
        if (catering == null) {
            throw new BaseSystemException(ResourcesServiceError.EMPTY_ENTITY);
        }
        if(catering.getDestination() == null||catering.getDestination().getId() == null){
            throw new BaseSystemException(ResourcesServiceError.DESTINATION_NULL);
        }
        if(!destinationBiz.isExisted(catering.getDestination().getId())){
            throw new BaseSystemException(ResourcesServiceError.DESTINATION_NOT_EXIST);
        }
        return Optional.ofNullable(cateringBiz.updateCatering(catering));
    }

    @Override
    public void deleteCatering(long id, boolean pseudo) {
        cateringBiz.deleteCatering(id,pseudo);
    }

    @Override
    public Optional<Catering> getCateringById(long id) {
        return  Optional.ofNullable(cateringBiz.getCateringById(id));
    }

    @Override
    public long getCateringId(Catering catering) {
        if (catering == null) {
            throw new BaseSystemException(ResourcesServiceError.EMPTY_ENTITY);
        }
        return resourcesBiz.getResourceId(catering);
    }

    @Override
    public DomainPage<Catering> queryCateringsPagesByConditions(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize, boolean byLike) {
        return cateringBiz.queryCateringsPagesByConditions(conditions, orderByFiledName, orderBy, pageIndex, pageSize, byLike);
    }

    //region ------------------------hotel------------------------------------

    /**
     * 住宿点一致性校验：
     *
     * @param hotel
     * @throws BaseSystemException 住宿点、目的地为空（EMPTY_ENTITY）
     *                             目的地不存在（DESTINATION_NOT_EXIST）
     */
    private void verifyHotel(Hotel hotel) throws BaseSystemException {
        if (hotel == null || hotel.getDestination() == null || hotel.getDestination().getId() == null)
            throw new BaseSystemException(ResourcesServiceError.EMPTY_ENTITY);
        if(!destinationBiz.isExisted(hotel.getDestination().getId()))
            throw new BaseSystemException(ResourcesServiceError.DESTINATION_NOT_EXIST);
        Destination destination = destinationBiz.getDestinationById(hotel.getDestination().getId());
        hotel.setDestination(destination);
    }

    @Override
    public Optional<Hotel> addHotel(Hotel hotel) throws BaseSystemException {
        verifyHotel(hotel);
        return Optional.ofNullable(hotelBiz.addHotel(hotel));
    }

    @Override
    public Optional<Hotel> updateHotel(Hotel hotel) throws BaseSystemException {
        verifyHotel(hotel);
        if(!hotelBiz.isExisted(hotel.getId()))
            throw new BaseSystemException(ResourcesServiceError.EMPTY_ENTITY);
        return Optional.ofNullable(hotelBiz.updateHotel(hotel));
    }

    @Override
    public void deleteHotel(long id) {
        if(!hotelBiz.isExisted(id))
            throw new BaseSystemException(ResourcesServiceError.EMPTY_ENTITY);

        hotelBiz.deleteHotel(id);
    }

    @Override
    public Optional<Hotel> getHotelById(long id) {
        return Optional.ofNullable(hotelBiz.getHotelById(id));
    }

    @Override
    public DomainPage<Hotel> queryHotelsPagesByConditions(Map<String, Object> conditions, String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize, boolean byLike) {
        return hotelBiz.queryHotelsPagesByConditions(conditions, orderByFiledName, orderBy, pageIndex, pageSize, byLike);
    }
    //endregion ----------------------------------hotel-------------------------------------

    @Override
    public Optional<Procurement> addProcurement(Procurement procurement) throws BaseSystemException {
        checkEntityNotNull(procurement);
        checkDestinationNotNull(procurement.getDestination());
        checkDestinationExist(procurement.getDestination());
        checkProcurementResourceExist(procurement);
        checkOspExist(procurement);
        return Optional.ofNullable(procurementBiz.addProcurement(procurement));
    }

    @Override
    public Optional<Procurement> updateProcurement(Procurement procurement) throws BaseSystemException {
        checkEntityNotNull(procurement);
        checkProcurementIdExist(procurement);
        checkDestinationNotNull(procurement.getDestination());
        checkDestinationExist(procurement.getDestination());
        checkProcurementResourceExist(procurement);
        checkOspExist(procurement);
        return Optional.ofNullable(procurementBiz.updateProcurement(procurement));
    }

    @Override
    public void deleteProcurement(Long id) throws BaseSystemException {
        if (id == null || !procurementBiz.isExisted(id)) {
            throw new BaseSystemException(ResourcesServiceError.PROCUREMENT_NOT_EXIST);
        }
        procurementBiz.deleteProcurement(id);
    }

    @Override
    public Optional<Procurement> getProcurementById(long id) {
        return Optional.ofNullable(procurementBiz.getProcurementById(id));
    }

    @Override
    public DomainPage<Procurement> queryProcurementsPagesByConditions(Map<String, Object> conditions
            , String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize, boolean byLike) {
        return procurementBiz.queryProcurementsPagesByConditions(conditions, orderByFiledName
                , orderBy, pageIndex, pageSize, byLike);
    }

    @Override
    public DomainPage<ProcurementVo> queryProcurementVoPagesByConditions(Map<String, Object> conditions
            , String orderByFiledName, IBaseDao.SortBy orderBy, int pageIndex, int pageSize) {
        return procurementBiz.queryProcurementVoPagesByConditions(conditions, orderByFiledName
                , orderBy, pageIndex, pageSize);
    }

    public Optional<Area> getAreaById(Long id) {
        return Optional.ofNullable(resourcesBiz.getAreaById(id));
    }

    //region -------------------------destination---------------------------------------------

    /**
     * Destination 一致性校验：
     * 目的地不能为空；
     * 目的地所属区域不能为空；
     * 目的地所属区域必须存在。
     *
     * @author XuHui/xuhui@simpletour.com
     */
    private void verifyDestination(Destination destination) {
        if (destination == null || destination.getArea() == null || destination.getArea().getId() == null)
            throw new BaseSystemException(ResourcesServiceError.EMPTY_ENTITY);
        Area area = resourcesBiz.getAreaById(destination.getArea().getId());
        if (area == null)
            throw new BaseSystemException(ResourcesServiceError.AREA_NOT_EXIST);
        destination.setArea(area);
    }

    public Optional<Destination> addDestination(Destination dest) throws BaseSystemException {
        verifyDestination(dest);

        return Optional.ofNullable(destinationBiz.addDestination(dest));
    }

    public Optional<Destination> updateDestination(Destination dest) throws BaseSystemException {
        verifyDestination(dest);
        if(!destinationBiz.isExisted(dest.getId())){
            throw new BaseSystemException(ResourcesServiceError.DESTINATION_NOT_EXIST);
        }
        return Optional.ofNullable(destinationBiz.updateDestination(dest));
    }

    public void deleteDestination(long id) {
        if(!destinationBiz.isExisted(id)){
            throw new BaseSystemException(ResourcesServiceError.DESTINATION_NOT_EXIST);
        }
        destinationBiz.deleteDestination(id);
    }

    public DomainPage<Destination> getDestinationsByPage(int page) {
        return getDestinationsByPage(page, DEFAULT_PAGE_SIZE);
    }

    public DomainPage<Destination> getDestinationsByPage(int page, int pageSize) {
        return destinationBiz.getDestinationsByPage(page, pageSize);
    }

    public DomainPage<Destination> getDestionationsByConditonPage(String name, String area, int page, int pageSize) {
        return destinationBiz.getDestionationsByConditonPage(name, area, page, pageSize);
    }

    public Optional<Destination> getDestinationById(long id) {
        return Optional.ofNullable(destinationBiz.getDestinationById(id));
    }
    //endregion

    //region ----------------------------------entertainment-------------------------------

    /**
     * 娱乐一致性校验：
     *
     * @param entertainment
     * @throws BaseSystemException 娱乐、目的地为空（EMPTY_ENTITY）
     *                             目的地不存在（DESTINATION_NOT_EXIST）
     */
    private void verifyEntertainment(Entertainment entertainment) throws BaseSystemException {
        if (entertainment == null || entertainment.getDestination() == null || entertainment.getDestination().getId() == null)
            throw new BaseSystemException(ResourcesServiceError.EMPTY_ENTITY);
        if(!destinationBiz.isExisted(entertainment.getDestination().getId()))
            throw new BaseSystemException(ResourcesServiceError.DESTINATION_NOT_EXIST);
        Destination destination = destinationBiz.getDestinationById(entertainment.getDestination().getId());
        entertainment.setDestination(destination);
    }

    public Optional<Entertainment> addEntertainment(Entertainment entertainment) throws BaseSystemException{
        verifyEntertainment(entertainment);
        return Optional.ofNullable(entertainmentBiz.addEntertainment(entertainment));
    }

    public Optional<Entertainment> updateEntertainment(Entertainment entertainment) throws BaseSystemException{
        verifyEntertainment(entertainment);
        if(!entertainmentBiz.isExisted(entertainment.getId()))
            throw new BaseSystemException(ResourcesServiceError.EMPTY_ENTITY);
        return Optional.ofNullable(entertainmentBiz.updateEntertainment(entertainment));
    }

    public void deleteEntertainment(long id) throws BaseSystemException{
        if(!entertainmentBiz.isExisted(id))
            throw new BaseSystemException(ResourcesServiceError.EMPTY_ENTITY);
        entertainmentBiz.deleteEntertainment(id);
    }

    @Override
    public Optional<Entertainment> getEntertainmentById(long id) {
        return Optional.ofNullable(entertainmentBiz.getEntertainmentById(id));
    }

    @Override
    public DomainPage<Entertainment> getEntertainmentsByPage(int page) {
        return entertainmentBiz.getEntertainmentsByPage(page,DEFAULT_PAGE_SIZE);
    }

    @Override
    public DomainPage<Entertainment> getEntertainmentsByPage(int page, int pageSize) {
        return entertainmentBiz.getEntertainmentsByPage(page,pageSize);
    }

    @Override
    public DomainPage<Entertainment> getEntertainmentsByConditionPage(String type, String name, String destinationName, int page, int pageSize) {
        return entertainmentBiz.getEntertainmentsByConditionPage(type,name,destinationName,page,pageSize);
    }
//endregion


    //region ----------------------------check---------------------------------------
    /**
     * 检查实体对象是否为null
     * @param domain
     */
    private void checkEntityNotNull(BaseDomain domain) {
        if (domain == null) {
            throw new BaseSystemException(ResourcesServiceError.EMPTY_ENTITY);
        }
    }

    /**
     * 当更新时，检查景点的ID必须存在
     * @param scenic
     */
    private void checkScenicIdValid(Scenic scenic) {
        if (scenic.getId() == null) {
            throw new BaseSystemException(ResourcesServiceError.SCENIC_ID_ERROR);
        }
        if (!scenicBiz.isExisted(scenic.getId())) {
            throw new BaseSystemException(ResourcesServiceError.SCENIC_NOT_EXIST);
        }
    }

    /**
     * 检查目的地相关的数据不为null
     * @param destination
     */
    private void checkDestinationNotNull(Destination destination) {
        if (destination == null || destination.getId() == null) {
            throw new BaseSystemException(ResourcesServiceError.DESTINATION_NULL);
        }
    }

    /**
     * 检查Destination的是否存在
     * @param destination
     */
    private void checkDestinationExist(Destination destination) {
        if (!destinationBiz.isExisted(destination.getId())) {
            throw new BaseSystemException(ResourcesServiceError.DESTINATION_NOT_EXIST);
        }
    }

    /**
     * 检查父景点是否存在
     * @param parent
     */
    private void checkParentScenicExist(Scenic parent) {
        if (parent != null && parent.getId() == null) {
            throw new BaseSystemException(ResourcesServiceError.SCENIC_PARENT_ID_NULL);
        }
        if (parent != null && !scenicBiz.isExisted(parent.getId())) {
            throw new BaseSystemException(ResourcesServiceError.SCENIC_PARENT_NOT_EXIST);
        }
    }

    /**
     * 检查元素所对应的资源存在
     * @param procurement
     */
    private void checkProcurementResourceExist(Procurement procurement) {
        Procurement.ResourceType type = procurement.getResourceType();
        Long resourceId = procurement.getResourceId();

        if (resourceId == null) {
            throw new BaseSystemException(ResourcesServiceError.PROCUREMENT_RESOURCE_NOT_EXIST);
        }
        if (Procurement.ResourceType.entertainment.equals(type)) {
            if (entertainmentBiz.isExisted(resourceId)) {
                return;
            }
        } else if (Procurement.ResourceType.catering.equals(type)) {
            if (cateringBiz.isExisted(resourceId)) {
                return;
            }
        } else if (Procurement.ResourceType.hotel.equals(type)) {
            if (hotelBiz.isExisted(resourceId)) {
                return;
            }
        } else if (Procurement.ResourceType.scenic.equals(type)) {
            if (scenicBiz.isExisted(resourceId)) {
                return;
            }
        }
        throw new BaseSystemException(ResourcesServiceError.PROCUREMENT_RESOURCE_NOT_EXIST);
    }

    /**
     * 检查供应商存在
     * @param
     */
    private void checkOspExist(Procurement procurement) {
        OfflineServiceProvider osp = procurement.getOsp();
        if (!(osp == null || osp.getId() == null)) {
            if (opsBiz.isExisted(osp.getId())) {
                return;
            }
        }
        throw new BaseSystemException(ResourcesServiceError.PROCUREMENT_OSP_NOT_EXIST);
    }

    /**
     * 更新元素时，检查元素ID所对应的元素存在，并且没有被删除
     */
    private void checkProcurementIdExist(Procurement procurement) {
        Long id = procurement.getId();
        if (id == null || !procurementBiz.isExisted(id)) {
            throw new BaseSystemException(ResourcesServiceError.PROCUREMENT_NOT_EXIST);
        }
    }

    //endregion
}
