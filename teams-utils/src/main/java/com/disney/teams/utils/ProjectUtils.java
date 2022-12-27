package com.disney.teams.utils;

import com.disney.teams.model.pagination.IPageList;
import com.disney.teams.model.pagination.PageList;
import com.disney.teams.utils.type.ClassUtils;
import com.disney.teams.utils.type.MethodUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * @author arron.zhou
 * @version 1.0.0
 * @date 2022/12/17
 * Description:
 * Modification  History:
 * Date         Author        Version        Description
 * ------------------------------------------------------
 * 2022/12/17       arron.zhou      1.0.0          create
 */
public class ProjectUtils {

    public static <T, BEAN> List<T> projectAll(Collection<BEAN> poList, Function<BEAN, T> func) {
        if(poList == null || poList.isEmpty()){
            return Collections.emptyList();
        }
        List<T> list = new ArrayList<>();
        for(BEAN po : poList){
            T fieldData = func.apply(po);
            list.add(fieldData);
        }
        return list;
    }

    public static <T, BEAN> List<T> projectAll(Collection<BEAN> poList, String propertyName) {
        return projectAll(poList, po -> MethodUtils.invokeGetterMethod(po, propertyName));
    }

    public static <BEAN> List<BEAN> projectAll(Collection<BEAN> poList, String... propertyNames) {
        return projectAll(poList, po -> {
            BEAN n = (BEAN) ClassUtils.newInstance(po.getClass());
            for(String propertyName : propertyNames) {
                MethodUtils.invokeSetterMethod(n, propertyName, MethodUtils.invokeGetterMethod(po, propertyName));
            }
            return n;
        });
    }

    private static <T, BEAN> PageList<T> clonePagedList(IPageList<BEAN> pagedList){
        PageList<T> newPagedList = new PageList<>();
        newPagedList.setPageNo(pagedList.getPageNo());
        newPagedList.setPageSize(pagedList.getPageSize());
        newPagedList.setTotalRecordCount(pagedList.getTotalRecordCount());
        return newPagedList;
    }

    public static <T, BEAN> IPageList<T> projectPage(IPageList<BEAN> pagedList, Function<BEAN, T> func) {
        if(pagedList == null){
            return null;
        }
        PageList<T> newPagedList = clonePagedList(pagedList);
        List<T> content = projectAll(pagedList.getContent(), func);
        newPagedList.setContent(content);
        return newPagedList;
    }

    public static <T, BEAN> IPageList<T> projectPage(IPageList<BEAN> pagedList, String propertyName) {
        return projectPage(pagedList, po -> MethodUtils.invokeGetterMethod(po, propertyName));
    }

    public static <BEAN> IPageList<BEAN> projectPage(IPageList<BEAN> pagedList, String... propertyNames) {
        if(pagedList == null){
            return null;
        }
        PageList<BEAN> newPagedList = clonePagedList(pagedList);
        List<BEAN> content = projectAll(pagedList.getContent(), propertyNames);
        newPagedList.setContent(content);
        return newPagedList;
    }

//    public static <ID extends Serializable, PO extends GenericEntity<ID>> List<ID> projectAllId(Collection<PO> poList) {
//        if(poList == null || poList.isEmpty()){
//            return Collections.emptyList();
//        }
//        List<ID> list = new ArrayList<>();
//        for(PO po : poList){
//            list.add(po == null ? null : po.getId());
//        }
//        return list;
//    }
//
//    public static <ID extends Serializable, PO extends GenericEntity<ID>> PagedList<ID> projectPageId(PagedList<PO> pagedList) {
//        if(pagedList == null){
//            return null;
//        }
//        PageList<ID> newPagedList = clonePagedList(pagedList);
//        List<ID> content = projectAllId(pagedList.getContent());
//        newPagedList.setContent(content);
//        return newPagedList;
//    }
}
