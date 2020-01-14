/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.pharmacy.api.db.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.pharmacy.api.db.PharmacyDAO;
import org.openmrs.module.pharmacy.model.ledger.LedgerRawLine;
import org.openmrs.module.pharmacy.model.ledger.LedgerType;
import org.openmrs.module.pharmacy.model.item.drug.DosageForm;
import org.openmrs.module.pharmacy.model.item.drug.Drug;
import org.openmrs.module.pharmacy.model.item.Item;
import org.openmrs.module.pharmacy.model.item.drug.ItemDrug;
import org.openmrs.module.pharmacy.model.item.ItemDrugMap;
import org.openmrs.module.pharmacy.model.item.ItemNonDrug;
import org.openmrs.module.pharmacy.model.item.itemattribute.Attribute;
import org.openmrs.module.pharmacy.model.item.itemcategory.Category;
import org.openmrs.module.pharmacy.model.item.itemcategory.SubCategory;
import org.openmrs.module.pharmacy.model.item.itemattribute.AttributeType;
import org.openmrs.module.pharmacy.model.item.itemcategory.SubCategoryAttribute;
import org.openmrs.module.pharmacy.model.price.ItemPriceRaw;
import org.openmrs.module.pharmacy.model.price.PriceCategory;
import org.openmrs.module.pharmacy.model.stock.ItemOutstandingStock;
import org.openmrs.module.pharmacy.model.stock.dispense.DispenseMap;
import org.openmrs.module.pharmacy.model.stock.dispense.raw.DispenseMapRaw;
import org.openmrs.module.pharmacy.model.stock.raw.ItemBatchRaw;
import org.openmrs.module.pharmacy.model.stock.raw.ItemOutstandingStockRaw;
import org.openmrs.module.pharmacy.model.stock.raw.ItemStockRaw;
import org.openmrs.module.pharmacy.model.stock.map.LocationStoreMap;
import org.openmrs.module.pharmacy.model.stock.request.raw.ItemRequestRaw;
import org.openmrs.module.pharmacy.model.stock.request.RequestStatusCode;
import org.openmrs.module.pharmacy.model.store.Store;
import org.openmrs.util.PrivilegeConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Implementation of  {@link PharmacyDAO}.
 */
public class HibernatePharmacyDAO implements PharmacyDAO {
    
    protected final Log log = LogFactory.getLog(this.getClass());
	
    private DbSessionFactory sessionFactory;
	
    /**
        * @param sessionFactory sessionFactory to be set
    */
    public void setSessionFactory(DbSessionFactory sessionFactory) {
	    this.sessionFactory = sessionFactory;
    }
    
    /**
        * @return DbSessionFactory instance
    */
    public DbSessionFactory getSessionFactory() {
	    return sessionFactory;
    }

/**
 Stores' & related data
 @Author: Eric Mwailunga
 May,2019
*/

// Beginning of non overriding methods..

    private Map<String,String> checkStoreNameExistence(String nameString)
    {
        Map<String,String> resultMap=new HashMap<>();
        resultMap.put("exists",".");
        resultMap.put("retired",".");

        String hql="select uuid,store_id as 'storeId',name,retired from ph_store where name='"+nameString+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(Store.class));
        List results=query.list();

        log.info("checkStoreNameExistence[hql]:"+hql);
        if(results!=null)
        {
            if(results.size()>0)
            {
                Iterator iterator = results.iterator();
                while(iterator.hasNext())
                {
                    Store store=(Store)iterator.next();
                    if(store.getRetired()==0)
                    {
                        resultMap.replace("exists","exists");
                        resultMap.replace("retired","not retired");

                    }
                    if(store.getRetired()==1)
                    {
                        resultMap.replace("exists","exists");
                        resultMap.replace("retired","retired");
                    }
                    log.info("checkNameExistence[Result Map]:"+resultMap.get("exists") +" : "+ resultMap.get("retired"));
                }

            }
        }

        return resultMap;
    }

//End of non overriding methods..

// Beginning of Overridden methods..

    @Override
    public String createStore(String storeName) {
        Map<String,String> exists= checkStoreNameExistence(storeName);
        String response;
        if(exists.get("exists").equals("exists") && exists.get("retired").equals("retired"))
        {
            response="exists,retired";
        }
        else if(exists.get("exists").equals("exists") && exists.get("retired").equals("not retired"))
        {
            response="exists,not retired";
        }
        else{
            String uuid= UUID.randomUUID().toString();
            String hql="insert into ph_store(name,date_created,creator,uuid) values('"+storeName+"',now(),4,'"+uuid+"')";
            DbSession session=sessionFactory.getCurrentSession();
            Query query=session.createSQLQuery(hql);

            log.info("createStore[hql]:"+hql);
            if(query.executeUpdate()==1)
            {
                return "success";
            }
            else
                return "failure";
        }

        return response;
    }

    @Override
    public String createSubStore(String storeName, String parentStoreUuid) {
        Map<String,String> exists= checkStoreNameExistence(storeName);
        String response;
        if(exists.get("exists").equals("exists") && exists.get("retired").equals("retired"))
        {
            response="exists,retired";
        }
        else if(exists.get("exists").equals("exists") && exists.get("retired").equals("not retired"))
        {
            response="exists,not retired";
        }
        else {
            String subStoreUuid= UUID.randomUUID().toString();
            String hql="insert into ph_store(name,date_created,creator,uuid) values('"+storeName+"',now(),'4','"+subStoreUuid+"')";

            DbSession session=sessionFactory.getCurrentSession();
            Query query;
            query=session.createSQLQuery(hql);

            log.info("createSubStore-store[hql]:"+hql);
            if(query.executeUpdate()==1)
            {
                hql="insert into ph_store_map(store_uuid,sub_store_uuid) values('"+parentStoreUuid+"','"+subStoreUuid+"')";
                query=session.createSQLQuery(hql);

                log.info("createSubStore-map[hql]:"+hql);
                if(query.executeUpdate()==1)
                {
                    return "success";
                }
            }
            return "failure";
        }

        return response;
    }

//Reading data..
    @Override
    public Store getStoreById(int storeId)
    {
        String hql="select uuid,store_id as 'storeId',name,retired from ph_store where store_id='"+storeId+"' and uuid not in (select distinct sub_store_uuid from ph_store_map)";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(Store.class));

        log.info("getStoreById[hql]:"+hql);
        List results=query.list();
        if(results!=null)
        {
            if(results.size()>0)
            {
                Iterator iterator=results.iterator();
                return (Store) iterator.next();
            }
        }
        return null;
    }

    @Override
    public Store getStoreByUuid(String storeUuid) {
        String hql="select uuid,store_id as 'storeId',name,retired from ph_store where uuid='"+storeUuid+"' and uuid not in (select distinct sub_store_uuid from ph_store_map)";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(Store.class));

        log.info("getStoreByUuid[hql]:"+hql);
        List results=query.list();
        if(results!=null)
        {
            if(results.size()>0)
            {
                Iterator iterator=results.iterator();
                return (Store) iterator.next();
            }
        }
        return null;
    }

    @Override
    public Store getStoreByName(String storeName) {

        String hql="select uuid,store_id as 'storeId',name,retired from ph_store where name='"+storeName+"' and uuid not in (select distinct sub_store_uuid from ph_store_map)";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(Store.class));

        log.info("getStoreByName[hql]:"+hql);
        List results=query.list();
        if(results!=null)
        {
            if(results.size()>0)
            {
                Iterator iterator=results.iterator();
                return (Store) iterator.next();
            }
        }
        return null;
    }

    @Override
    public List getStoreBySubName(String storeName) {
        String hql="select uuid,store_id as 'storeId',name,retired from ph_store where name like '%"+storeName+"%' and uuid not in (select distinct sub_store_uuid from ph_store_map)";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(Store.class));

        log.info("getStoreBySubName[hql]:"+hql);
        List stores=query.list();
        if(stores!=null)
        {
            if(stores.size()>0)
            {
                return stores;
            }
        }
        return null;
    }

    @Override
    public List getStoreSubStore(String storeUuid, boolean includeRetired) {

       String hql="select phs.uuid as 'uuid',phs.store_id as 'storeId',phs.name as 'name',phs.retired as 'retired',phsm.store_uuid as 'parentUuid'" +
                " from ph_store phs inner join ph_store_map phsm on phs.uuid=phsm.sub_store_uuid" +
                " where phs.uuid in (select sub_store_uuid from ph_store_map where store_uuid='"+storeUuid+"')";
        if(!includeRetired)
        {
            hql+=" and phs.retired=0";
        }
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(Store.class));

        log.info("getStoreSubStore[hql]:"+hql);
        List stores=query.list();
        if(stores!=null)
        {
            if(stores.size()>0)
            {
                return stores;
            }
        }
        return null;
    }

    @Override
    public List getAllStores(boolean includeRetired) {
        String hql="select uuid,store_id as 'storeId',name,retired from ph_store";
        if(!includeRetired)
        {
            hql+=" where retired=0 and uuid not in (select distinct sub_store_uuid from ph_store_map)";
        }else{
            hql+=" where uuid not in (select distinct sub_store_uuid from ph_store_map)";
        }

        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(Store.class));

        log.info("getAllStores[hql]:"+hql);
        List stores=query.list();
        if(stores!=null)
        {
            if(stores.size()>0)
            {
                return stores;
            }
        }
        return null;
    }

    @Override
    public List getAllStoresFull(boolean includeRetired) {
        return null;
    }

//Sub stores data..

    @Override
    public Store getSubStoreById(int subStoreId)
    {
        String hql="select phs.uuid as 'uuid',phs.store_id as 'storeId',phs.name as 'name',phs.retired as 'retired',phsm.store_uuid as 'parentUuid'" +
                " from ph_store phs inner join ph_store_map phsm on phs.uuid=phsm.sub_store_uuid" +
                " where phs.store_id='"+subStoreId+"' and phs.uuid in (select distinct sub_store_uuid from ph_store_map)";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(Store.class));

        log.info("getSubStoreById[hql]:"+hql);
        List results=query.list();
        if(results!=null)
        {
            if(results.size()>0)
            {
                Iterator iterator=results.iterator();
                return (Store) iterator.next();
            }
        }
        return null;
    }
    @Override
    public Store getSubStoreByUuid(String subStoreUuid)
    {
        String hql="select phs.uuid as 'uuid',phs.store_id as 'storeId',phs.name as 'name',phs.retired as 'retired',phsm.store_uuid as 'parentUuid'" +
                " from ph_store phs inner join ph_store_map phsm on phs.uuid=phsm.sub_store_uuid" +
                " where phs.uuid='"+subStoreUuid+"' and phs.uuid in (select distinct sub_store_uuid from ph_store_map)";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(Store.class));

        log.info("getSubStoreByUuid[hql]:"+hql);
        List results=query.list();
        if(results!=null)
        {
            if(results.size()>0)
            {
                Iterator iterator=results.iterator();
                return (Store) iterator.next();
            }
        }
        return null;
    }
    @Override
    public Store getSubStoreByName(String subStoreName) {

        String hql="select phs.uuid as 'uuid',phs.store_id as 'storeId',phs.name as 'name',phs.retired as 'retired',phsm.store_uuid as 'parentUuid'" +
                " from ph_store phs inner join ph_store_map phsm on phs.uuid=phsm.sub_store_uuid" +
                " where phs.name='"+subStoreName+"' and phs.uuid in (select sub_store_uuid from ph_store_map)";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(Store.class));

        log.info("getSubStoreByName[hql]:"+hql);
        List results=query.list();
        if(results!=null)
        {
            if(results.size()>0)
            {
                Iterator iterator=results.iterator();
                return (Store) iterator.next();
            }
        }
        return null;
    }

    @Override
    public List getSubStoreBySubName(String subStoreSubName) {

        String hql="select phs.uuid as 'uuid',phs.store_id as 'storeId',phs.name as 'name',phs.retired as 'retired',phsm.store_uuid as 'parentUuid'" +
                " from ph_store phs inner join ph_store_map phsm on phs.uuid=phsm.sub_store_uuid" +
                " where phs.name like '%"+subStoreSubName+"%' and phs.uuid in (select sub_store_uuid from ph_store_map)";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(Store.class));

        log.info("getSubStoreBySubName[hql]:"+hql);
        List stores=query.list();
        if(stores!=null)
        {
            if(stores.size()>0)
            {
                return stores;
            }
        }
        return null;
    }

    @Override
    public List getAllSubStore(boolean includeRetired) {
        String hql="select phs.uuid as 'uuid',phs.store_id as 'storeId',phs.name as 'name',phs.retired as 'retired',phsm.store_uuid as 'parentUuid'" +
                " from ph_store phs inner join ph_store_map phsm on phs.uuid=phsm.sub_store_uuid" +
                " where phs.uuid in (select sub_store_uuid from ph_store_map)";
        if(!includeRetired)
        {
            hql+=" and retired=0";
        }
        DbSession session=sessionFactory.getCurrentSession();

        log.info("getAllSubStore[hql]:"+hql);
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(Store.class));
        List stores=query.list();
        if(stores!=null)
        {
            if(stores.size()>0)
            {
                return stores;
            }
        }
        return null;
    }

//Updating stores & sub stores...
    @Override
    public String updateStoreByUuid(String storeUuid, String storeName, boolean retired) {
        int rt;
        if(retired)
        {
            rt=1;
        }else{
            rt=0;
        }
        String hql="update ph_store set name='"+storeName+"',retired="+rt+",date_changed=now(), changed_by=4 where uuid='"+storeUuid+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql);

        log.info("updateStoreByUuid[hql]:"+hql);
        if(query.executeUpdate()==1)
        {
            return "success";
        }
        return "failure";
    }

    @Override
    public String updateSubStoreByUuid(String subStoreUuid, String subStoreName,String parentUuid, boolean retired) {
        int rt;
        if(retired)
        {
            rt=1;
        }else{
            rt=0;
        }
        String hql="update ph_store phs inner join ph_store_map phsm on phs.uuid=phsm.sub_store_uuid set phs.name='"+subStoreName+"',phs.date_changed=now(),phs.changed_by=4,phs.retired="+rt+",phsm.store_uuid='"+parentUuid+"' where phs.uuid='"+subStoreUuid+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql);

        log.info("updateSubStoreByUuid[hql]:"+hql);
        if(query.executeUpdate()==2)
        {
            return "success";
        }
        return "failure";
    }

    @Override
    public String deleteStoreByUuid(String storeUuid)
    {
        String hql="delete from ph_store where uuid='"+storeUuid+"' and uuid not in (select distinct sub_store_uuid from ph_store_map)";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql);

        log.info("deleteStoreByUuid[hql]:"+hql);
        if(query.executeUpdate()==1)
        {
            return "success";
        }
        return "failure";
    }

    @Override
    public String deleteSubStoreByUuid(String subStoreUuid)
    {
        String hql="delete from ph_store_map where sub_store_uuid='"+subStoreUuid+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql);

        log.info("deleteSubStoreByUuid[hql(1)]:"+hql);
        if(query.executeUpdate()==1)
        {
            hql="delete from ph_store where uuid='"+subStoreUuid+"' and uuid not in (select distinct sub_store_uuid from ph_store_map)";
            query=session.createSQLQuery(hql);

            log.info("deleteSubStoreByUuid[hql(2)]:"+hql);
            if(query.executeUpdate()==1) {
                return "success";
            }
        }
        return "failure";
    }



/*
 Items categories registrations & related data
 @Author: Eric Mwailunga
 May,2019
*/

    private Map<String,String> checkSubCategoryNameExistence(String nameString)
    {
        Map<String,String> resultMap=new HashMap<>();
        resultMap.put("exists",".");
        resultMap.put("retired",".");

        String hql="select uuid,sub_category_id as 'subCategoryId',name,retired from ph_item_sub_category where name='"+nameString+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(SubCategory.class));
        List results=query.list();

        log.info("checkSubCategoryNameExistence[hql]:"+hql);
        if(results!=null)
        {
            if(results.size()>0)
            {
                Iterator iterator = results.iterator();
                while(iterator.hasNext())
                {
                    SubCategory subCategory=(SubCategory)iterator.next();
                    if(subCategory.getRetired()==0)
                    {
                        resultMap.replace("exists","exists");
                        resultMap.replace("retired","not retired");
                    }
                    if(subCategory.getRetired()==1)
                    {
                        resultMap.replace("exists","exists");
                        resultMap.replace("retired","retired");
                    }
                }

            }
        }
        return resultMap;
    }

    @Override
    public Category getCategoryByUuid(String categoryUuid)
    {
        String hql="select uuid,category_id as 'categoryId',name,retired from ph_item_category where uuid='"+categoryUuid+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(Category.class));

        log.info("getCategoryByUuid[hql]:"+hql);
        List category=query.list();
        if(category!=null)
        {
            if(category.size()>0)
            {
                Iterator iterator=category.iterator();
                return (Category) iterator.next();
            }
        }
        return null;
    }

    @Override
    public Category getCategoryByName(String categoryName)
    {
        String hql="select uuid,category_id as 'categoryId',name,retired from ph_item_category where name='"+categoryName+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(Category.class));

        log.info("getCategoryByName[hql]:"+hql);
        List category=query.list();
        if(category!=null)
        {
            if(category.size()>0)
            {
                Iterator iterator=category.iterator();
                return (Category) iterator.next();
            }
        }
        return null;
    }
    @Override
    public Category getCategoryByUuidFull(String categoryUuid)
    {
        Category category=this.getCategoryByUuid(categoryUuid);
        if(category!=null)
        {
            String hql="select uuid,sub_category_id as 'subCategoryId',name,item_category_uuid as 'parentUuid',retired" +
                    " from ph_item_sub_category where item_category_uuid='"+categoryUuid+"'";

            DbSession session=sessionFactory.getCurrentSession();
            Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(SubCategory.class));
            log.info("getCategoryByUuidFull[hql]:"+hql);
            List subCategories=query.list();
            List<SubCategory> subCategoryList=new ArrayList<>();
            if(subCategories!=null)
            {
                if(subCategories.size()>0)
                {
                    Iterator it=subCategories.iterator();
                    while(it.hasNext())
                    {
                        SubCategory subCategory=(SubCategory) it.next();
                        subCategory.setParentUuid(null);
                        subCategory.setParentCategory(null);
                        subCategoryList.add(subCategory);
                    }
                }
                category.setSubCategories(subCategoryList);
            }
            return  category;
        }
        return null;
    }

    @Override
    public List getAllCategories(boolean includeRetired)
    {
        String hql="select uuid,category_id as 'categoryId',name,retired from ph_item_category where retired='"+includeRetired+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(Category.class));

        log.info("getAllCategories[hql]:"+hql);
        List categories=query.list();
        if(categories!=null)
        {
            if(categories.size()>0)
            {
                return categories;
            }
        }
        return null;
    }

    @Override
    public List getAllCategoriesFull(boolean includeRetired)
    {
        List categories=this.getAllCategories(includeRetired);
        if(categories!=null)
        {
            if(categories.size()>0)
            {
                Iterator iterator=categories.iterator();
                List<Category> cats=new ArrayList<>();
                while(iterator.hasNext())
                {
                    Category category=(Category) iterator.next();
                    String categoryUuid=category.getUuid();
                    Category cat=this.getCategoryByUuidFull(categoryUuid);

                    if(cat!=null)
                    {
                        cats.add(cat);
                    }
                }
                return cats;
            }
        }
        return null;
    }

    @Override
    public String createSubCategory(String subCategoryName,String parentCategoryUuid)
    {
        Map<String,String> exists=checkSubCategoryNameExistence(subCategoryName);
        String response;
        if(exists.get("exists").equals("exists") && exists.get("retired").equals("retired"))
        {
            response="exists,retired";
        }
        else if(exists.get("exists").equals("exists") && exists.get("retired").equals("not retired"))
        {
            response="exists,not retired";
        }
        else{
            String hql="insert into ph_item_sub_category(name,creator,date_created,uuid,item_category_uuid)" +
                    " values('"+subCategoryName+"','4',now(),uuid(),'"+parentCategoryUuid+"')";
            DbSession session=sessionFactory.getCurrentSession();
            Query query=session.createSQLQuery(hql);

            log.info("createSubCategory[hql]:"+hql);
            if(query.executeUpdate()==1)
            {
                return "success";
            }
            else
                return "failure";
        }


        return response;
    }

    @Override
    public SubCategory getSubCategoryByUuid(String subCategoryUuid)
    {
        String hql="select uuid,sub_category_id as 'subCategoryId',name,item_category_uuid as 'parentUuid',retired" +
                " from ph_item_sub_category where uuid='"+subCategoryUuid+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(SubCategory.class));

        log.info("getSubCategoryByUuid[hql]:"+hql);
        List results=query.list();
        if(results!=null)
        {
            if(results.size()>0)
            {
                Iterator iterator=results.iterator();
                return (SubCategory) iterator.next();
            }
        }
        return null;
    }

    @Override
    public SubCategory getFullSubCategoryByUuid(String subCategoryUuid)
    {
        SubCategory subCategory=this.getSubCategoryByUuid(subCategoryUuid);
        if(subCategory!=null)
        {
            String parentUuid=subCategory.getParentUuid();
            Category category=this.getCategoryByUuid(parentUuid);
            if(category!=null)
            {
                subCategory.setParentCategory(category);
                subCategory.setParentUuid(null);
            }
            return subCategory;
        }
        return null;
    }

    @Override
    public SubCategory getSubCategoryByName(String subCategoryName)
    {
        String hql="select uuid,sub_category_id as 'subCategoryId',name,item_category_uuid as 'parentUuid',retired" +
                " from ph_item_sub_category where name='"+subCategoryName+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(SubCategory.class));

        log.info("getSubCategoryByName[hql]:"+hql);
        List results=query.list();
        if(results!=null)
        {
            if(results.size()>0)
            {
                Iterator iterator=results.iterator();
                return (SubCategory) iterator.next();
            }
        }
        return null;
    }

    @Override
    public List getSubCategoryBySubName(String subCategorySubName)
    {
        String hql="select uuid,sub_category_id as 'subCategoryId',name,item_category_uuid as 'parentUuid',retired" +
                " from ph_item_sub_category where name like '%"+subCategorySubName+"%'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(SubCategory.class));

        log.info("getSubCategoryBySubName[hql]:"+hql);
        List subCategories=query.list();
        if(subCategories!=null)
        {
            if(subCategories.size()>0)
            {
                return subCategories;
            }
        }
        return null;
    }

    @Override
    public List getAllSubCategories(boolean includeRetired)
    {
        String hql="select uuid,sub_category_id as 'subCategoryId',name,item_category_uuid as 'parentUuid',retired" +
                " from ph_item_sub_category";
        if(!includeRetired)
        {
            hql+=" where retired=0";
        }
        DbSession session=sessionFactory.getCurrentSession();

        log.info("getAllSubCategories[hql]:"+hql);
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(SubCategory.class));
        List subCategories=query.list();
        if(subCategories!=null)
        {
            if(subCategories.size()>0)
            {
                return subCategories;
            }
        }
        return null;
    }
    @Override
    public List<SubCategory> getAllSubCategoriesFull(boolean includeRetired)
    {

        List subCategories=this.getAllSubCategories(includeRetired);
        if(subCategories!=null)
        {
            if(subCategories.size()>0)
            {
                List<SubCategory> subCategoriesFull=new ArrayList<>();
                Iterator iterator=subCategories.iterator();
                while(iterator.hasNext())
                {
                    SubCategory subCategory=(SubCategory) iterator.next();
                    String parentUuid=subCategory.getParentUuid();
                    Category category=this.getCategoryByUuid(parentUuid);
                    if(category!=null)
                    {
                        subCategory.setParentCategory(category);
                        subCategory.setParentUuid(null);
                    }
                    subCategoriesFull.add(subCategory);
                }

                return subCategoriesFull;
            }
        }
        return null;
    }

    @Override
    public String updateSubCategoryByUuid(String subCategoryUuid,String subCategoryName,String parentUuid,boolean retire)
    {
        int rt;
        if(retire)
        {
            rt=1;
        }else{
            rt=0;
        }
        String hql="update ph_item_sub_category set name='"+subCategoryName+"',retired="+rt+",date_changed=now(),changed_by='4'," +
                " item_category_uuid='"+parentUuid+"' where uuid='"+subCategoryUuid+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql);

        log.info("updateSubCategoryByUuid[hql]:"+hql);
        if(query.executeUpdate()==1)
        {
            return "success";
        }
        return "failure";
    }

    @Override
    public String deleteSubCategoryByUuid(String subCategoryUuid)
    {
        String hql="delete from ph_item_sub_category where uuid='"+subCategoryUuid+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql);

        log.info("deleteSubCategoryByUuid[hql]:"+hql);
        if(query.executeUpdate()==1)
        {
            return "success";
        }
        return "failure";
    }

/*
 Items categories attributes type registrations & related data
 @Author: Eric Mwailunga
 May,2019
*/

    private Map<String,String> checkSubCategoryAttributeNameExistence(String attributeTypeName,String subCategoryUuid)
    {
        Map<String,String> resultMap=new HashMap<>();
        resultMap.put("exists",".");
        resultMap.put("retired",".");

        String hql="select uuid,attribute_type_id as 'attributeTypeId',name,retired from ph_item_sub_category_attribute_type" +
                " where name='"+attributeTypeName+"' and sub_category_uuid='"+subCategoryUuid+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(AttributeType.class));
        List results=query.list();

        log.info("checkSubCategoryAttributeNameExistence[hql]:"+hql);
        if(results!=null)
        {
            if(results.size()>0)
            {
                Iterator iterator = results.iterator();
                while(iterator.hasNext())
                {
                    AttributeType attributeType =(AttributeType)iterator.next();
                    if(attributeType.getRetired()==0)
                    {
                        resultMap.replace("exists","exists");
                        resultMap.replace("retired","retired");

                    }
                    else if(attributeType.getRetired()==1)
                    {
                        resultMap.replace("exists","exists");
                        resultMap.replace("retired","not retired");
                    }
                }
            }
        }

        return resultMap;
    }

    @Override
    public String createSubCategoryAttributeType(String subCategoryUuid,String attributeTypeName)
    {
        Map<String,String> exists=checkSubCategoryAttributeNameExistence(attributeTypeName,subCategoryUuid);
        String response;
        if(exists.get("exists").equals("exists") && exists.get("retired").equals("retired"))
        {
            response="exists,retired";
        }
        else if(exists.get("exists").equals("exists") && exists.get("retired").equals("not retired"))
        {
            response="exists,not retired";
        }
        else{
            String hql="insert into ph_item_sub_category_attribute_type(name,creator,date_created,uuid,sub_category_uuid)" +
                    " values('"+attributeTypeName+"','4',now(),uuid(),'"+subCategoryUuid+"')";
            DbSession session=sessionFactory.getCurrentSession();
            Query query=session.createSQLQuery(hql);

            log.info("createSubCategoryAttributeType[hql]:"+hql);
            if(query.executeUpdate()==1)
            {
                return "success";
            }
            else
                return "failure";
        }
        return response;
    }

    @Override
    public String createSubCategoryAttributeTypes(String subCategoryUuid,String attributeTypeNames){
        attributeTypeNames=attributeTypeNames.replace("\"","");
        JSONObject jsonObject=new JSONObject(attributeTypeNames);
        JSONArray jsonArray=jsonObject.getJSONArray("attributeTypeNames");
        int counter=0,length=jsonArray.length();
        String response=null;
        int failed=length;
        while(counter<length)
        {
            String attributeTypeName=jsonArray.getJSONObject(counter).getString("name");
            response=this.createSubCategoryAttributeType(subCategoryUuid,attributeTypeName);
            if(response.equals("success"))
            {
                failed--;
            }
            counter++;
        }
        if(failed<length)
        {
            response="success";
        }
        return response;
    }

    @Override
    public List getAttributeTypesBySubCategoryUuid(String subCategoryUuid)
    {
        String hql="select uuid,attribute_type_id as 'attributeTypeId',name,retired from ph_item_sub_category_attribute_type" +
                " where sub_category_uuid='"+subCategoryUuid+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(AttributeType.class));
        List attributeTypes=query.list();

        log.info("getAttributeTypesBySubCategoryUuid[hql]:"+hql);
        if(attributeTypes!=null)
        {
            if(attributeTypes.size()>0)
            {
                return attributeTypes;
            }
        }
        return null;
    }

    @Override
    public String updateSubCategoryAttributeType(String attributeTypeUuid,String attributeTypeName,String subCategoryUuid,boolean retire)
    {
        int rt;
        if(retire){
            rt=1;
        }else{
            rt=0;
        }
        String hql="update ph_item_sub_category_attribute_type set name='"+attributeTypeName+"',retired="+rt+",date_changed=now(),changed_by=4," +
                " sub_category_uuid='"+subCategoryUuid+"'" +
                " where uuid='"+subCategoryUuid+"' and sub_category_uuid='"+subCategoryUuid+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql);

        log.info("updateSubCategoryAttributeType[hql]:"+hql);
        if(query.executeUpdate()==1)
        {
            return "success";
        }
        return "failure";
    }

    @Override
    public String getSubCategoryAttributeTypeByUuid(String attributeTypeUuid)
    {
        String hql="select uuid,attribute_type_id as 'attributeTypeId',name,retired from ph_item_sub_category_attribute_type" +
                " where uuid='"+attributeTypeUuid+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(AttributeType.class));
        List attributeTypes=query.list();

        log.info("getSubCategoryAttributeTypeByUuid[hql]:"+hql);
        if(attributeTypes!=null)
        {
            if(attributeTypes.size()>0)
            {
                Iterator iterator=attributeTypes.iterator();
                if(iterator.hasNext())
                {
                    return ((AttributeType) iterator.next()).getName();
                }

            }
        }
        return null;
    }
/*
 Items categories attributes with their attribute types
 @Author: Eric Mwailunga
 May,2019
*/

    @Override
    public List<Attribute> getAttributesForItemByItemId(int itemId)
    {
        List<Attribute> attributes=new ArrayList<>();
        String hql="select " +
                "pisca.attribute_id as 'id',"+
                "pi.name as 'itemName',"+
                "piscat.name as 'attributeName',"+
                "pisca.value as 'value',"+
                "pisca.uuid as 'uuid' "+
                "from ph_item_sub_category_attribute pisca "+
                "inner join ph_item pi on pi.item_id=pisca.item_id "+
                "inner join ph_item_sub_category_attribute_type piscat on pisca.attribute_type_uuid = piscat.uuid "+
                "where pi.item_id='"+itemId+"'";
        log.info("getAttributesForItemWithItemId[hql]:"+hql);
        DbSession session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(Attribute.class));
        List attributesResult=query.list();
        if(attributesResult!=null)
        {
            if(attributesResult.size()>0)
            {
                Attribute attribute;
                Iterator iterator=attributesResult.iterator();
                while(iterator.hasNext())
                {
                    attribute=(Attribute) iterator.next();
                    attribute.setItemName(null);
                    attributes.add(attribute);
                }
                return attributes;
            }

        }

        return null;
    }
    @Override
    public List<SubCategoryAttribute> getSubCategoryByCategoryUuidWithFullAttributeTypes(String categoryUuid,boolean includeRetired)
    {
        List<SubCategoryAttribute> subCategoriesWithTheirAttributes=new ArrayList<>();
        String hql="select uuid,sub_category_id as 'subCategoryId',name,item_category_uuid as 'parentUuid',retired" +
                " from ph_item_sub_category where item_category_uuid='"+categoryUuid+"'";
        if(!includeRetired)
        {
            hql+=" and retired=0";
        }
        DbSession session=sessionFactory.getCurrentSession();

        log.info("getCategoryByUuidWithFullSubCategoryAttributeTypes[hql(1)]:"+hql);
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(SubCategory.class));
        List subCategories=query.list();
        if(subCategories!=null)
        {
            if(subCategories.size()>0)
            {

                Iterator iterator1=subCategories.iterator();
                while(iterator1.hasNext())
                {
                    SubCategoryAttribute subCategoryAttribute=new SubCategoryAttribute();
                    SubCategory subCategory=(SubCategory) iterator1.next();
                    subCategoryAttribute.setSubCategoryId(subCategory.getSubCategoryId());
                    subCategoryAttribute.setName(subCategory.getName());
                    subCategoryAttribute.setRetired(subCategory.getRetired());

                    List attributesTypeRaw=getAttributeTypesBySubCategoryUuid(subCategory.getUuid());

                    List<AttributeType> attributeTypes = new ArrayList<>();
                    if(attributesTypeRaw!=null) {
                        Iterator iterator2 = attributesTypeRaw.iterator();
                        while (iterator2.hasNext()) {
                            AttributeType attributeType = (AttributeType) iterator2.next();
                            attributeTypes.add(attributeType);
                        }
                    }
                    if (attributeTypes.size() > 0)
                    {
                        subCategoryAttribute.setAttributes(attributeTypes);
                    }else{
                        subCategoryAttribute.setAttributes(null);
                    }
                    subCategoriesWithTheirAttributes.add(subCategoryAttribute);
                }
                return subCategoriesWithTheirAttributes;
            }
        }
        return null;
    }

    @Override
    public String saveDrugItem(String categoryUuid,String name,int drugId)
    {
        String itemUuid= UUID.randomUUID().toString();
        String hql="insert into ph_item(name,category_uuid,date_created,creator,uuid) values('"+name+"','"+categoryUuid+"',now(),'4','"+itemUuid+"')";

        DbSession session=sessionFactory.getCurrentSession();
        Query query;
        query=session.createSQLQuery(hql);

        log.info("saveItem-[hql]:"+hql);
        if(query.executeUpdate()==1)
        {
            Category category=this.getCategoryByUuid(categoryUuid);
            if(category.getName().equals("Drugs"))
            {
                hql="insert into ph_item_drug(item_id,drug_id,creator,date_created) values((select item_id from ph_item where uuid='"+itemUuid+"'),'"+drugId+"','4',now())";
                query=session.createSQLQuery(hql);
                if(query.executeUpdate()==1)
                {
                    return "success";
                }
            }
        }
        return null;
    }

    @Override
    public Item getItemByUuid(String itemUuid)
    {
        String hql="select item_id as 'itemId',uuid as 'itemUuid',name as 'itemName',category_uuid as 'categoryUuid',retired" +
                " from ph_item where uuid='"+itemUuid+"'";
        log.info("getItemByUuid[hql]:"+hql);
        DbSession session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(Item.class));
        List items=query.list();
        if(items!=null)
        {
            if(items.size()>0)
            {
                Iterator iterator=items.iterator();
                if(iterator.hasNext())
                {
                    Item item=(Item) iterator.next();
                    Category category=this.getCategoryByUuid(item.getCategoryUuid());
                    item.setCategoryName(category.getName());
                    return item;
                }
            }
        }

        return null;
    }

    @Override
    public Item getItemById(int itemId)
    {
        String hql="select item_id as 'itemId',uuid as 'itemUuid',name as 'itemName',category_uuid as 'categoryUuid',retired" +
                " from ph_item where item_id='"+itemId+"'";
        log.info("getItemById[hql]:"+hql);
        DbSession session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(Item.class));
        List items=query.list();
        if(items!=null)
        {
            if(items.size()>0)
            {
                Iterator iterator=items.iterator();
                if(iterator.hasNext())
                {
                    Item item=(Item) iterator.next();
                    Category category=this.getCategoryByUuid(item.getCategoryUuid());
                    item.setCategoryUuid(null);
                    item.setCategoryName(category.getName());
                    return item;
                }
            }
        }

        return null;
    }

    @Override
    public Item saveNonDrugItem(String categoryUuid,String name) {
        String itemUuid = UUID.randomUUID().toString();
        String hql = "insert into ph_item(name,category_uuid,date_created,creator,uuid) values('" + name + "','" + categoryUuid + "',now(),'4','" + itemUuid + "')";

        DbSession session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(hql);

        log.info("saveNonDrugItem[hql]:" + hql);
        if (query.executeUpdate() == 1)
        {
            Item item=this.getItemByUuid(itemUuid);
            log.info("saveNonDrugItem[itemId]:"+item.getItemId());
            return item;
        }
        return null;
    }

    @Override
    public String updateNonDrugItem(int itemId,String itemName,boolean retired)
    {
        int rt;
        if(retired)
        {
            rt=1;
        }else{
            rt=0;
        }
        String hql = "update ph_item set name='"+itemName+"',retired='"+rt+"',date_changed=now()" +
                " where item_id='" + itemId + "'";

        DbSession session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(hql);

        log.info("updateNonDrugItem[hql]:" + hql);
        if (query.executeUpdate() == 1)
        {
            return "success";
        }
        return null;
    }

    @Override
    public ItemNonDrug getNonDrugByItemUuidWithTheirAttributes(String itemUuid)
    {
        String hql="select item_id as 'itemId',uuid as 'itemUuid',name as 'itemName',category_uuid as 'categoryUuid',retired" +
                " from ph_item where uuid='"+itemUuid+"'";
        log.info("getNonDrugByNameWithNoAttributes[hql]:"+hql);
        DbSession session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(Item.class));
        List items=query.list();
        if(items!=null)
        {
            if(items.size()>0)
            {
                Iterator iterator=items.iterator();
                if(iterator.hasNext())
                {
                    Item item=(Item) iterator.next();
                    ItemNonDrug itemNonDrug=new ItemNonDrug();

                    Category category=this.getCategoryByUuid(item.getCategoryUuid());

                    itemNonDrug.setItemId(item.getItemId());
                    itemNonDrug.setItemName(item.getItemName());
                    itemNonDrug.setItemUuid(item.getItemUuid());
                    itemNonDrug.setCategoryName(category.getName());

                    itemNonDrug.setAttributes(getAttributesForItemByItemId(item.getItemId()));
                    return itemNonDrug;
                }
            }
        }

        return null;
    }

    @Override
    public ItemDrug getDrugByItemUuidWithTheirAttributes(String itemUuid){

        String hql = "select item_id as 'itemId',uuid as 'itemUuid',name as 'itemName',category_uuid as 'categoryUuid',retired" +
                " from ph_item where uuid='" + itemUuid + "'";

        log.info("getDrugByItemUuidWithTheirAttributes[hql]:" + hql);
        DbSession session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(Item.class));

        List items=query.list();
        if(items!=null)
        {
            if(items.size()>0)
            {
                Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPTS);
                Context.addProxyPrivilege(PrivilegeConstants.MANAGE_CONCEPTS);
                Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPT_DATATYPES);

                Iterator iterator=items.iterator();
                if(iterator.hasNext())
                {
                    ItemDrug itemDrug=new ItemDrug();
                    Item item=(Item) iterator.next();

                    Category category=this.getCategoryByUuid(item.getCategoryUuid());

                    itemDrug.setItemId(item.getItemId());
                    //itemDrug.setItemName(item.getItemName());
                    itemDrug.setItemUuid(item.getItemUuid());
                    itemDrug.setCategoryName(category.getName());

                    ItemDrugMap itemDrugMap=this.getItemDrugMapByItemId(item.getItemId());
                    ConceptService conceptService=Context.getConceptService();
                    org.openmrs.Drug openmrsDrug=conceptService.getDrug(itemDrugMap.getDrugId());
                    Drug drug=new Drug();
                    drug.setDrugId(openmrsDrug.getId());
                    drug.setName(openmrsDrug.getName()+" ("+openmrsDrug.getDosageForm().getName()+")");
                    drug.setStrength(openmrsDrug.getStrength());

                    DosageForm dosageForm=new DosageForm();
                    dosageForm.setName(openmrsDrug.getDosageForm().getName().getName());
                    dosageForm.setConceptId(openmrsDrug.getDosageForm().getConceptId());

                    drug.setDosageForm(dosageForm);
                    itemDrug.setItemName(openmrsDrug.getName()+" ("+openmrsDrug.getDosageForm().getName()+")");
                    itemDrug.setDrug(drug);
                    return itemDrug;
                }

                Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPTS);
                Context.removeProxyPrivilege(PrivilegeConstants.MANAGE_CONCEPTS);
                Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPT_DATATYPES);
            }
        }

        return null;
    }

    @Override
    public Item getNonDrugByNameWithoutTheirAttributes(String itemName)
    {
        Item item;
        Category category= this.getCategoryByName("Non drugs");
        if(category!=null)
        {
            String categoryUuid=category.getUuid();
            String hql="select item_id as 'itemId',uuid as 'itemUuid',name as 'itemName',category_uuid as 'categoryUuid',retired" +
                    " from ph_item where category_uuid='"+categoryUuid+"' and name='"+itemName+"'";
            log.info("getNonDrugByNameWithNoAttributes[hql]:"+hql);
            DbSession session = sessionFactory.getCurrentSession();
            Query query = session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(Item.class));
            List items=query.list();
            if(items!=null)
            {
                if(items.size()>0)
                {
                    Iterator iterator=items.iterator();
                    if(iterator.hasNext())
                    {
                        item=(Item) iterator.next();
                        return item;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public ItemNonDrug getNonDrugByNameWithTheirAttributes(String itemName)
    {
        Item item=this.getNonDrugByNameWithoutTheirAttributes(itemName);
        if(item!=null)
        {
            int itemId=item.getItemId();
            String hql="select item_id as 'itemId',uuid as 'itemUuid',name as 'itemName',category_uuid as 'categoryUuid',retired" +
                    " from ph_item where item_id='"+itemId+"'";
            log.info("getNonDrugByNameWithNoAttributes[hql]:"+hql);
            DbSession session = sessionFactory.getCurrentSession();
            Query query = session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(Item.class));
            List items=query.list();
            if(items!=null)
            {
                if(items.size()>0)
                {
                    Iterator iterator=items.iterator();
                    if(iterator.hasNext())
                    {

                        item=(Item) iterator.next();
                        ItemNonDrug itemNonDrug=new ItemNonDrug();

                        Category category=this.getCategoryByUuid(item.getCategoryUuid());

                        itemNonDrug.setItemId(item.getItemId());
                        itemNonDrug.setItemName(item.getItemName());
                        itemNonDrug.setItemUuid(item.getItemUuid());
                        itemNonDrug.setCategoryName(category.getName());
                        itemNonDrug.setAttributes(getAttributesForItemByItemId(item.getItemId()));
                        return itemNonDrug;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public List<Item> getAllNonDrugsByWithoutTheirAttributes(boolean includeRetired)
    {
        Category category= this.getCategoryByName("Non drugs");
        if(category!=null)
        {
            String categoryUuid=category.getUuid();
            String hql="select item_id as 'itemId',uuid as 'itemUuid',name as 'itemName',category_uuid as 'categoryUuid',retired" +
                    " from ph_item where category_uuid='"+categoryUuid+"'";
            if(!includeRetired)
            {
                hql+=" and retired=0";
            }

            log.info("getAllNonDrugsByWithoutTheirAttributes[hql]:"+hql);
            DbSession session = sessionFactory.getCurrentSession();
            Query query = session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(Item.class));
            List<Item> nonDrugItems=query.list();
            if(nonDrugItems!=null)
            {
                if(nonDrugItems.size()>0)
                {
                    return nonDrugItems;
                }
            }
        }
        return null;
    }

    @Override
    public List<ItemNonDrug> getAllNonDrugsWithTheirAttributes(boolean includeRetired)
    {
        Category category= this.getCategoryByName("Non drugs");
        if(category!=null) {
            String categoryUuid = category.getUuid();
            String hql = "select item_id as 'itemId',uuid as 'itemUuid',name as 'itemName',category_uuid as 'categoryUuid',retired" +
                    " from ph_item where category_uuid='" + categoryUuid + "'";
            if (!includeRetired) {
                hql += " and retired=0";
            }

            log.info("getAllNonDrugsWithTheirAttributes[hql]:" + hql);
            DbSession session = sessionFactory.getCurrentSession();
            Query query = session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(Item.class));

            List items=query.list();
            if(items!=null)
            {
                if(items.size()>0)
                {
                    List<ItemNonDrug> itemNonDrugs=new ArrayList<>();
                    Iterator iterator=items.iterator();
                    while(iterator.hasNext())
                    {
                        Item item=(Item) iterator.next();
                        ItemNonDrug itemNonDrug=new ItemNonDrug();

                        itemNonDrug.setItemId(item.getItemId());
                        itemNonDrug.setItemName(item.getItemName());
                        itemNonDrug.setItemUuid(item.getItemUuid());
                        itemNonDrug.setCategoryName(category.getName());
                        itemNonDrug.setAttributes(getAttributesForItemByItemId(item.getItemId()));
                        itemNonDrugs.add(itemNonDrug);
                    }
                    return itemNonDrugs;
                }
            }
        }
        return null;
    }

    @Override
    public String saveItemAttributeValue(int itemId,String attributeTypeUuid,String value)
    {
        String itemUuid = UUID.randomUUID().toString();
        String hql = "insert into ph_item_sub_category_attribute(item_id,attribute_type_uuid,value,date_created,creator,uuid)" +
                " values('" + itemId + "','" + attributeTypeUuid + "','"+value+"',now(),'4','" + itemUuid + "')";

        DbSession session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(hql);

        log.info("SaveItemAttributeValue[hql]:" + hql);
        if (query.executeUpdate() == 1)
        {
            return "success";
        }
        return null;
    }

    @Override
    public String updateItemAttributeValue(int itemId,String attributeTypeUuid,String value)
    {
        String hql = "update ph_item_sub_category_attribute set value='"+value+"',date_changed=now()" +
                " where attribute_type_uuid='" + attributeTypeUuid + "' and item_id='" + itemId + "'";

        DbSession session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(hql);

        log.info("updateItemAttributeValue[hql]:" + hql);
        if (query.executeUpdate() == 1)
        {
            return "success";
        }
        return null;
    }




    @Override
    public ItemDrugMap getItemDrugMapByItemId(int itemId)
    {
        String hql="select item_id as 'itemId',drug_id as 'drugId'" +
                " from ph_item_drug where item_id='"+itemId+"'";
        log.info("getItemDrugMapByItemId[hql]:"+hql);
        DbSession session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(ItemDrugMap.class));
        List items=query.list();
        if(items!=null)
        {
            if(items.size()>0) {
                Iterator iterator = items.iterator();
                if (iterator.hasNext()) {
                    return (ItemDrugMap) iterator.next();
                }
            }
        }
        return null;
    }

    @Override
    public ItemDrugMap getItemDrugMapByDrugId(int drugId)
    {
        String hql="select item_id as 'itemId',drug_id as 'drugId'" +
                " from ph_item_drug where drug_id='"+drugId+"'";
        log.info("getItemDrugMapByDrugId[hql]:"+hql);
        DbSession session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(ItemDrugMap.class));
        List items=query.list();
        if(items!=null)
        {
            if(items.size()>0) {
                Iterator iterator = items.iterator();
                if (iterator.hasNext()) {
                    return (ItemDrugMap) iterator.next();
                }
            }
        }
        return null;
    }

    @Override
    public List<ItemDrug> getAllDrugItems(boolean includeRetired)
    {
        Category category= this.getCategoryByName("Drugs");
        if(category!=null)
        {
            String categoryUuid = category.getUuid();
            String hql = "select item_id as 'itemId',uuid as 'itemUuid',name as 'itemName',category_uuid as 'categoryUuid',retired" +
                    " from ph_item where category_uuid='" + categoryUuid + "'";
            if (!includeRetired) {
                hql += " and retired=0";
            }

            log.info("getAllDrugItems[hql]:" + hql);
            DbSession session = sessionFactory.getCurrentSession();
            Query query = session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(Item.class));

            List items=query.list();
            if(items!=null)
            {
                if(items.size()>0)
                {
                    Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPTS);
                    Context.addProxyPrivilege(PrivilegeConstants.MANAGE_CONCEPTS);
                    Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPT_DATATYPES);
                    Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPT_CLASSES);
                    Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPT_ATTRIBUTE_TYPES);
                    Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPT_MAP_TYPES);

                    List<ItemDrug> itemDrugs=new ArrayList<>();
                    Iterator iterator=items.iterator();
                    while(iterator.hasNext())
                    {
                        Item item=(Item) iterator.next();
                        ItemDrug itemDrug=new ItemDrug();

                        itemDrug.setItemId(item.getItemId());
                        //itemDrug.setItemName(item.getItemName());
                        itemDrug.setItemUuid(item.getItemUuid());
                        itemDrug.setCategoryName(category.getName());

                        ItemDrugMap itemDrugMap=this.getItemDrugMapByItemId(item.getItemId());
                        ConceptService conceptService=Context.getConceptService();
                        org.openmrs.Drug openmrsDrug=conceptService.getDrug(itemDrugMap.getDrugId());
                        Drug drug=new Drug();
                        drug.setDrugId(openmrsDrug.getId());
                        drug.setName(openmrsDrug.getName()+" ("+openmrsDrug.getDosageForm().getName()+")");
                        drug.setStrength(openmrsDrug.getStrength());

                        DosageForm dosageForm=new DosageForm();
                        dosageForm.setName(openmrsDrug.getDosageForm().getName().getName());
                        dosageForm.setConceptId(openmrsDrug.getDosageForm().getConceptId());

                        drug.setDosageForm(dosageForm);
                        itemDrug.setItemName(openmrsDrug.getName()+" ("+openmrsDrug.getDosageForm().getName()+")");
                        itemDrug.setDrug(drug);
                        itemDrugs.add(itemDrug);
                    }

                    Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPTS);
                    Context.removeProxyPrivilege(PrivilegeConstants.MANAGE_CONCEPTS);
                    Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPT_DATATYPES);
                    Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPT_CLASSES);
                    Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPT_ATTRIBUTE_TYPES);
                    Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPT_MAP_TYPES);
                    return itemDrugs;
                }
            }
        }
        return null;
    }

    @Override
    public List<Item> getAllItems(boolean includeRetired)
    {
        List categories= this.getAllCategories(false);
        if(categories!=null)
        {
            if(categories.size()>0)
            {
                List<Item> items=new ArrayList<>();
                Iterator iteratorCategories=categories.iterator();
                while(iteratorCategories.hasNext())
                {
                    Category category=(Category) iteratorCategories.next();
                    if(category.getName().equals("Drugs"))
                    {
                        List<ItemDrug> itemDrugs=this.getAllDrugItems(includeRetired);
                        if(itemDrugs!=null) {
                            if (itemDrugs.size() > 0) {
                                Iterator iteratorDrugs = itemDrugs.iterator();
                                while (iteratorDrugs.hasNext()) {
                                    ItemDrug itemDrug = (ItemDrug) iteratorDrugs.next();
                                    Item item = new Item();
                                    item.setItemId(itemDrug.getItemId());
                                    item.setItemName(itemDrug.getItemName());
                                    item.setCategoryName(itemDrug.getCategoryName());
                                    item.setItemUuid(itemDrug.getItemUuid());
                                    item.setCategoryUuid(itemDrug.getCategoryUuid());
                                    items.add(item);
                                }
                            }
                        }
                    }
                    if(category.getName().equals("Non drugs"))
                    {
                        List<Item> itemNonDrugs=this.getAllNonDrugsByWithoutTheirAttributes(includeRetired);
                        if(itemNonDrugs!=null) {
                            if (itemNonDrugs.size() > 0) {
                                Iterator iteratorNonDrugs = itemNonDrugs.iterator();
                                while (iteratorNonDrugs.hasNext()) {
                                    Item item = (Item) iteratorNonDrugs.next();
                                    item.setCategoryName(category.getName());
                                    item.setCategoryUuid(null);
                                    items.add(item);
                                }
                            }
                        }
                    }
                }
                return items;
            }
        }
        return null;
    }

    @Override
    public List<Item> getAllItemsBySubName(String itemSubName)
    {
        String hql="select item_id as 'itemId',uuid as 'itemUuid',name as 'itemName',category_uuid as 'categoryUuid',retired" +
        " from ph_item where name like '%"+itemSubName+"%' and retired=0 order by category_uuid asc";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(Item.class));

        log.info("getAllItemsBySubName[hql]:"+hql);
        List<Item> items=query.list();
        if(items!=null)
        {
            if(items.size()>0)
            {
                return items;
            }
        }
        return null;
    }


    @Override
    public List<ItemRequestRaw> getAllItemRequestsInMainStore(int storeId)
    {
        String hql="select request_id as 'requestId',request_group_id as 'requestGroupId',price_category_id as 'priceCategoryId',item_id as 'itemId',uuid," +
                "requesting_store as 'requestingStore',requested_store as 'requestedStore',quantity_requested as 'quantityRequested'," +
                "quantity_issued as 'quantityIssued',status_code as 'statusCode',date_created as 'requestDate',date_issued as 'dateIssued'" +
                " from ph_item_request where requested_store='"+storeId+"' order by date_created desc";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(ItemRequestRaw.class));

        log.info("getAllItemRequestsInMainStore[hql]:"+hql);
        List<ItemRequestRaw> itemRawRequests=query.list();
        if(itemRawRequests!=null)
        {
            if(itemRawRequests.size()>0)
            {
                return itemRawRequests;
            }
        }
        return null;
    }


    @Override
    public List<ItemRequestRaw> getAllItemRequestsByStoreId(int storeId)
    {
        String hql="select request_id as 'requestId',request_group_id as 'requestGroupId',price_category_id as 'priceCategoryId',item_id as 'itemId',uuid," +
                "requesting_store as 'requestingStore',requested_store as 'requestedStore',quantity_requested as 'quantityRequested'," +
                "quantity_issued as 'quantityIssued',status_code as 'statusCode',date_created as 'requestDate'" +
                " from ph_item_request where requesting_store='"+storeId+"' order by date_created desc";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(ItemRequestRaw.class));

        log.info("getAllItemRequestsByStoreId[hql]:"+hql);
        List<ItemRequestRaw> itemRawRequests=query.list();
        if(itemRawRequests!=null)
        {
            if(itemRawRequests.size()>0)
            {
                return itemRawRequests;
            }
        }
        return null;
    }

/*
 Item price & related data
 @Author: Eric Mwailunga
 May,2019
*/

    @Override
    public String createItemSellingPrice(String itemUuid,int priceCategoryId,double sellingPrice)
    {
        Item item=this.getItemByUuid(itemUuid);
        ItemPriceRaw itemPriceRaw =this.getItemSellingPriceByItemIdAndPriceCategoryId(item.getItemId(),priceCategoryId);
        String response;
        if(itemPriceRaw !=null)
        {
            response="exists";
        }
        else{
            String uuid= UUID.randomUUID().toString();
            String hql="insert into ph_item_price(item_id,price_category_id,selling_price,date_created,creator,uuid) " +
                    "values('"+item.getItemId()+"','"+priceCategoryId+"','"+sellingPrice+"',now(),4,'"+uuid+"')";
            DbSession session=sessionFactory.getCurrentSession();
            Query query=session.createSQLQuery(hql);

            log.info("createItemSellingPrice[hql]:"+hql);
            if(query.executeUpdate()==1)
            {
                response="success";
            }
            else
                response="failure";
        }
        return response;
    }

    @Override
    public ItemPriceRaw getItemSellingPriceByItemIdAndPriceCategoryId(int itemId, int priceCategoryId)
    {
        String hql="select uuid,item_price_id as 'itemPriceId',price_category_id as 'categoryId',item_id as 'itemId',selling_price as 'sellingPrice' " +
                "from ph_item_price where item_id='"+itemId+"' and price_category_id='"+priceCategoryId+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(ItemPriceRaw.class));

        log.info("getItemSellingPriceByItemUuidAndPriceCategoryId[hql]:"+hql);
        List results=query.list();
        if(results!=null)
        {
            if(results.size()>0)
            {
                Iterator iterator=results.iterator();
                return (ItemPriceRaw) iterator.next();
            }
        }
        return null;
    }

    @Override
    public ItemPriceRaw getItemSellingPriceByItemPriceId(int itemPriceId)
    {
        String hql="select uuid,item_price_id as 'itemPriceId',price_category_id as 'categoryId',item_id as 'itemId',selling_price as 'sellingPrice' " +
                "from ph_item_price where item_price_id='"+itemPriceId+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(ItemPriceRaw.class));

        log.info("getItemSellingPriceByItemPriceId[hql]:"+hql);
        List results=query.list();
        if(results!=null)
        {
            if(results.size()>0)
            {
                Iterator iterator=results.iterator();
                return (ItemPriceRaw) iterator.next();
            }
        }
        return null;
    }

    @Override
    public ItemPriceRaw getItemSellingPriceByItemPriceUuid(String itemPriceUuid)
    {
        String hql="select uuid,item_price_id as 'itemPriceId',price_category_id as 'categoryId',item_id as 'itemId',selling_price as 'sellingPrice' " +
                "from ph_item_price where uuid='"+itemPriceUuid+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(ItemPriceRaw.class));

        log.info("getItemSellingPriceByItemPriceId[hql]:"+hql);
        List results=query.list();
        if(results!=null)
        {
            if(results.size()>0)
            {
                Iterator iterator=results.iterator();
                return (ItemPriceRaw) iterator.next();
            }
        }
        return null;
    }
    @Override
    public String updateItemSellingPrice(int itemId,int priceCategoryId,double sellingPrice)
    {
        String hql="update ph_item_price set selling_price='"+sellingPrice+"',date_changed=now(), changed_by=4 where item_id='"+itemId+"' and price_category_id='"+priceCategoryId+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql);

        log.info("updatingItemSellingPrice[hql]:"+hql);
        if(query.executeUpdate()==1)
        {
            return "success";
        }
        return "failure";
    }

    @Override
    public List<ItemPriceRaw> getAllItemSellingPrice()
    {
        String hql="select uuid,item_price_id as 'itemPriceId',price_category_id as 'categoryId',item_id as 'itemId',selling_price as 'sellingPrice' " +
                "from ph_item_price order by item_id desc";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(ItemPriceRaw.class));

        log.info("getAllItemSellingPrice[hql]:"+hql);
        List<ItemPriceRaw> itemRawPrices=query.list();
        if(itemRawPrices!=null)
        {
            if(itemRawPrices.size()>0)
            {
                return itemRawPrices;
            }
        }
        return null;
    }
/*
 Price category & related data
 @Author: Eric Mwailunga
 May,2019
*/

    private  Map<String,String> checkPriceCategoryExistence(String categoryName)
    {
        Map<String,String> resultMap=new HashMap<>();
        resultMap.put("exists",".");
        resultMap.put("retired",".");

        String hql="select uuid,price_category_id as 'categoryId',name,retired from ph_item_price_category where name='"+categoryName+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(PriceCategory.class));
        List results=query.list();

        log.info("checkPriceCategoryExistence[hql]:"+hql);
        if(results!=null)
        {
            if(results.size()>0)
            {
                Iterator iterator = results.iterator();
                while(iterator.hasNext())
                {
                    PriceCategory priceCategory=(PriceCategory)iterator.next();
                    if(priceCategory.getRetired()==0)
                    {
                        resultMap.replace("exists","exists");
                        resultMap.replace("retired","not retired");

                    }
                    if(priceCategory.getRetired()==1)
                    {
                        resultMap.replace("exists","exists");
                        resultMap.replace("retired","retired");
                    }
                    log.info("checkNameExistence[Result Map]:"+resultMap.get("exists") +" : "+ resultMap.get("retired"));
                }
            }
        }
        return resultMap;
    }

    @Override
    public String createPriceCategory(String categoryName)
    {
        Map<String,String> existenceTest=this.checkPriceCategoryExistence(categoryName);
        String response;
        if(existenceTest.get("exists").equals("exists") && existenceTest.get("retired").equals("retired"))
        {
            response="exists,retired";
        }
        else if(existenceTest.get("exists").equals("exists") && existenceTest.get("retired").equals("not retired"))
        {
            response="exists,not retired";
        }
        else {

            String uuid= UUID.randomUUID().toString();
            String hql="insert into ph_item_price_category(name,date_created,creator,uuid) values('"+categoryName+"',now(),4,'"+uuid+"')";
            DbSession session=sessionFactory.getCurrentSession();
            Query query=session.createSQLQuery(hql);

            log.info("createStore[hql]:"+hql);
            if(query.executeUpdate()==1)
            {
                return "success";
            }
            else
                return "failure";
        }

        return response;
    }

    @Override
    public PriceCategory getPriceCategoryById(int priceCategoryId)
    {
        String hql="select uuid,price_category_id as 'categoryId',name,retired from ph_item_price_category where price_category_id='"+priceCategoryId+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(PriceCategory.class));

        log.info("getPriceCategoryById[hql]:"+hql);
        List results=query.list();
        if(results!=null)
        {
            if(results.size()>0)
            {
                Iterator iterator=results.iterator();
                return (PriceCategory) iterator.next();
            }
        }
        return null;
    }

    @Override
    public PriceCategory getPriceCategoryByUuid(String categoryUuid)
    {
        String hql="select uuid,price_category_id as 'categoryId',name,retired from ph_item_price_category where uuid='"+categoryUuid+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(PriceCategory.class));

        log.info("getPriceCategoryByUuid[hql]:"+hql);
        List results=query.list();
        if(results!=null)
        {
            if(results.size()>0)
            {
                Iterator iterator=results.iterator();
                return (PriceCategory) iterator.next();
            }
        }
        return null;
    }

    @Override
    public String updatePriceCategory(String categoryUuid,String categoryName,boolean retired)
    {
        int rt;
        if(retired)
        {
            rt=1;
        }else{
            rt=0;
        }
        String hql="update ph_item_price_category set name='"+categoryName+"',retired="+rt+",date_changed=now(), changed_by=4 where uuid='"+categoryUuid+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql);

        log.info("updatePriceCategory[hql]:"+hql);
        if(query.executeUpdate()==1)
        {
            return "success";
        }
        return "failure";
    }

    @Override
    public PriceCategory getPriceCategoryByName(String categoryName)
    {
        String hql="select uuid,price_category_id as 'categoryId',name,retired from ph_item_price_category where name='"+categoryName+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(PriceCategory.class));

        log.info("getPriceCategoryByName[hql]:"+hql);
        List results=query.list();
        if(results!=null)
        {
            if(results.size()>0)
            {
                Iterator iterator=results.iterator();
                return (PriceCategory) iterator.next();
            }
        }

        return null;
    }

    @Override
    public List<PriceCategory> getAllPriceCategories(boolean includeRetired)
    {
        String hql="select uuid,price_category_id as 'categoryId',name,retired from ph_item_price_category";
        if(!includeRetired)
        {
            hql+=" where retired=0";
        }
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(PriceCategory.class));

        log.info("getAllPriceCategories[hql]:"+hql);
        List<PriceCategory> categories=query.list();
        if(categories!=null)
        {
            if(categories.size()>0)
            {
                return categories;
            }
        }
        return null;
    }



/*
LedgerRawLine entries, types & related data
@Author: Eric Mwailunga
May,2019
*/

    private  Map<String,String> checkLedgerTypeExistence(String ledgerName,String operation)
    {
        Map<String,String> resultMap=new HashMap<>();
        resultMap.put("exists",".");
        resultMap.put("retired",".");

        String hql="select uuid,ledger_type_id as 'ledgerTypeId',name,operation,retired " +
                "from ph_item_ledger_type " +
                "where name='"+ledgerName+"' and operation='"+operation+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(LedgerType.class));
        List results=query.list();

        log.info("checkLedgerTypeExistence[hql]:"+hql);
        if(results!=null)
        {
            if(results.size()>0)
            {
                Iterator iterator = results.iterator();
                while(iterator.hasNext())
                {
                    LedgerType ledgerType=(LedgerType)iterator.next();
                    if(ledgerType.getRetired()==0)
                    {
                        resultMap.replace("exists","exists");
                        resultMap.replace("retired","not retired");

                    }
                    if(ledgerType.getRetired()==1)
                    {
                        resultMap.replace("exists","exists");
                        resultMap.replace("retired","retired");
                    }
                    log.info("checkLedgerTypeExistence[Result Map]:"+resultMap.get("exists") +" : "+ resultMap.get("retired"));
                }
            }
        }
        return resultMap;
    }

    @Override
    public String createLedgerType(String ledgerTypeName, String operation){

        Map<String,String> ledgerTypeExists=this.checkLedgerTypeExistence(ledgerTypeName,operation);
        String response;
        if(ledgerTypeExists.get("exists").equals("exists") && ledgerTypeExists.get("retired").equals("retired"))
        {
            response="exists,retired";
        }
        else if(ledgerTypeExists.get("exists").equals("exists") && ledgerTypeExists.get("retired").equals("not retired"))
        {
            response="exists,not retired";
        }
        else {

            String uuid= UUID.randomUUID().toString();
            String hql="insert into ph_item_ledger_type(name,operation,date_created,creator,uuid) " +
                    "values('"+ledgerTypeName+"','"+operation+"',now(),4,'"+uuid+"')";
            DbSession session=sessionFactory.getCurrentSession();
            Query query=session.createSQLQuery(hql);

            log.info("createLedgerType[hql]:"+hql);
            if(query.executeUpdate()==1)
            {
                return "success";
            }
            else
                return "failure";
        }

        return response;
    }

    @Override
    public LedgerType getLedgerTypeById(int ledgerTypeId){
        String hql="select uuid,ledger_type_id as 'ledgerTypeId',name,operation,retired " +
                "from ph_item_ledger_type where ledger_type_id='"+ledgerTypeId+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(LedgerType.class));

        log.info("getLedgerTypeById[hql]:"+hql);
        List results=query.list();
        if(results!=null)
        {
            if(results.size()>0)
            {
                Iterator iterator=results.iterator();
                return (LedgerType) iterator.next();
            }
        }
        return null;
    }

    @Override
    public LedgerType getLedgerTypeByUuid(String ledgerTypeUuid){

        String hql="select uuid,ledger_type_id as 'ledgerTypeId',name,operation,retired " +
                "from ph_item_ledger_type where uuid='"+ledgerTypeUuid+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(LedgerType.class));

        log.info("getLedgerTypeByUuid[hql]:"+hql);
        List results=query.list();
        if(results!=null)
        {
            if(results.size()>0)
            {
                Iterator iterator=results.iterator();
                return (LedgerType) iterator.next();
            }
        }
        return null;
    }

    @Override
    public String updateLedgerTypeByUuid(String ledgerTypeUuid,String ledgerTypeName,String operation,boolean retired)
    {
        int rt;
        if(retired)
        {
            rt=1;
        }else{
            rt=0;
        }
        String hql="update ph_item_ledger_type set name='"+ledgerTypeName+"',operation='"+operation+"',retired="+rt+",date_changed=now(), " +
                "changed_by=4 where uuid='"+ledgerTypeUuid+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql);

        log.info("updatePriceCategory[hql]:"+hql);
        if(query.executeUpdate()==1)
        {
            return "success";
        }
        return "failure";
    }

    @Override
    public List<LedgerType> getAllLedgerTypes(boolean includeRetired)
    {
        String hql="select uuid,ledger_type_id as 'ledgerTypeId',name,operation,retired " +
                "from ph_item_ledger_type";
        if(!includeRetired)
        {
            hql+=" where retired=0";
        }
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(LedgerType.class));

        log.info("getAllLedgerTypes[hql]:"+hql);
        List<LedgerType> categories=query.list();
        if(categories!=null)
        {
            if(categories.size()>0)
            {
                return categories;
            }
        }
        return null;
    }



/*
 LedgerRawLine entries & related data
 @Author: Eric Mwailunga
 May,2019
*/

    @Override
    public String createLedgerEntry(int ledgerTypeId,int itemId,int priceCategoryId,String batchNo,String invoiceNo,double buyingPrice,int quantity, String dateMoved, String expiryDate,int dosageUnits,String remarks)
    {
        String uuid= UUID.randomUUID().toString();

        Date formattedDateDateMoved=null;
        Date formattedDateExpiryDate=null;
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
        try {
            formattedDateDateMoved = simpleDateFormat.parse(dateMoved);
            formattedDateExpiryDate=simpleDateFormat.parse(expiryDate);
        } catch (ParseException e) {
            //e.printStackTrace();
            log.info(e.getMessage());
        }
        if(formattedDateDateMoved!=null && formattedDateExpiryDate!=null)
        {
            String formattedDateMoved=simpleDateFormat.format(formattedDateDateMoved);
            String formattedExpiryDate=simpleDateFormat.format(formattedDateExpiryDate);

            String hql="insert into ph_item_ledger" +
                    "(ledger_type,item_id,price_category_id,batch_no,invoice_no,buying_price,quantity,date_moved,expiry_date,dosing_units,remarks,creator,date_created,uuid) " +
                    "values('"+ledgerTypeId+"','"+itemId+"','"+priceCategoryId+"','"+batchNo+"','"+invoiceNo+"','"+buyingPrice+"','"+quantity+"','"+formattedDateMoved+"','"+formattedExpiryDate+"','"+dosageUnits+"','"+remarks+"','4',now(),'"+uuid+"')";
            DbSession session=sessionFactory.getCurrentSession();
            Query query=session.createSQLQuery(hql);

            log.info("createLedgerEntry[hql]:"+hql);
            if(query.executeUpdate()==1)
            {
                return "success";
            }
        }
        log.info("createLedgerEntry[DateFormatter]:Failed");
        return "failure";
    }

    @Override
    public LedgerRawLine getLedgerEntryByUuid(String entryUuid)
    {
        String hql="select uuid," +
                "ledger_entry_id as 'entryId'," +
                "ledger_type as 'ledgerTypeId', " +
                "item_id as 'itemId', "+
                "price_category_id as 'priceCategoryId', " +
                "batch_no as 'batchNo', " +
                "invoice_no as 'invoiceNo', " +
                "quantity, " +
                "buying_price as 'buyingPrice', " +
                "date_moved as 'dateMoved', " +
                "expiry_date as 'expiryDate', " +
                "dosing_units as 'dosingUnitsId', "+
                "remarks "+
                "from ph_item_ledger where uuid='"+entryUuid+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(LedgerRawLine.class));

        log.info("getLedgerEntryByUuid[hql]:"+hql);
        List results=query.list();
        if(results!=null)
        {
            if(results.size()>0)
            {
                Iterator iterator=results.iterator();
                return (LedgerRawLine) iterator.next();
            }
        }
        return null;
    }

    @Override
    public List<LedgerRawLine> getAllLedgerEntries()
    {
        String hql="select uuid," +
                "ledger_entry_id as 'entryId'," +
                "item_id as 'itemId', "+
                "ledger_type as 'ledgerTypeId', " +
                "price_category_id as 'priceCategoryId', " +
                "batch_no as 'batchNo', " +
                "invoice_no as 'invoiceNo', " +
                "quantity, " +
                "date_moved as 'dateMoved', " +
                "expiry_date as 'expiryDate', " +
                "dosing_units as 'dosingUnitsId', "+
                "remarks "+
                "from ph_item_ledger order by date_created desc";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(LedgerRawLine.class));

        log.info("getLedgerEntryByUuid[hql]:"+hql);
        List results=query.list();
        if(results!=null)
        {
            if(results.size()>0)
            {
                List<LedgerRawLine> ledgerRawLines=new ArrayList<>();
                Iterator iterator=results.iterator();
                while(iterator.hasNext())
                {
                    LedgerRawLine ledgerRawLine=(LedgerRawLine) iterator.next();
                    ledgerRawLines.add(ledgerRawLine);
                }
                return ledgerRawLines;
            }
        }
        return null;
    }


/*
 Stock movement & related data
 @Author: Eric Mwailunga
 May,2019
*/
    @Override
    public LocationStoreMap getStoreMapByLocationId(int locationId)
    {
        String hql="select uuid,map_id as 'mapId',location_id as 'locationId',store_id as 'storeId' " +
                "from ph_location_store_map where location_id='"+locationId+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(LocationStoreMap.class));
        log.info("getDispensingStoreMapByLocationId[hql]:"+hql);
        List storeMap=query.list();
        if(storeMap!=null)
        {
            if(storeMap.size()>0)
            {
                Iterator iterator=storeMap.iterator();
                if(iterator.hasNext())
                    return (LocationStoreMap) iterator.next();
            }
        }
        return null;
    }

    @Override
    public List<LocationStoreMap> getStoreMapByLocationStoreId(int storeId)
    {
        String hql="select uuid,map_id as 'mapId',location_id as 'locationId',store_id as 'storeId' " +
                "from ph_location_store_map where store_id='"+storeId+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(LocationStoreMap.class));

        log.info("getDispensingStoreMapByStoreId[hql]:"+hql);
        List<LocationStoreMap> storeMaps=query.list();
        if(storeMaps!=null)
        {
            if(storeMaps.size()>0)
            {
                return storeMaps;
            }
        }
        return null;
    }


/*
 Stock movement & related data :: Item requests and issuing.
 @Author: Eric Mwailunga
 May,2019
*/
    @Override
    public ItemRequestRaw getRequestById(int requestId)
    {
        String hql="select uuid,request_id as 'requestId',request_group_id as 'requestGroupId',item_id as 'itemId',price_category_id as 'priceCategoryId',requesting_store as 'requestingStore',requested_store as 'requestedStore',quantity_requested as 'quantityRequested',quantity_issued as 'quantityIssued', status_code as 'statusCode' " +
                "from ph_item_request where request_id='"+requestId+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(ItemRequestRaw.class));
        log.info("getRequestById[hql]:"+hql);
        List itemRequest=query.list();
        if(itemRequest!=null)
        {
            if(itemRequest.size()>0)
            {
                Iterator iterator=itemRequest.iterator();
                if(iterator.hasNext())
                    return (ItemRequestRaw) iterator.next();
            }
        }
        return null;
    }

    @Override
    public RequestStatusCode getStatusCodeDefinitionById(int statusId)
    {
        String hql="select uuid,name,source_state as 'sourceState',destination_state as 'destinationState',status_id as 'statusId' " +
                "from ph_item_request_status_code where status_id='"+statusId+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(RequestStatusCode.class));
        log.info("getStatusCodeDefinitionById[hql]:"+hql);
        List requestStatusCode=query.list();
        if(requestStatusCode!=null)
        {
            if(requestStatusCode.size()>0)
            {
                Iterator iterator=requestStatusCode.iterator();
                if(iterator.hasNext())
                    return (RequestStatusCode) iterator.next();
            }
        }
        return null;
    }

    @Override
    public ItemRequestRaw getRequestByUuid(String requestUuid)
    {
        String hql="select uuid,request_id as 'requestId',request_group_id as 'requestGroupId',item_id as 'itemId',price_category_id as 'priceCategoryId',requesting_store as 'requestingStore',requested_store as 'requestedStore',quantity_requested as 'quantityRequested',quantity_issued as 'quantityIssued', status_code as 'statusCode' " +
                "from ph_item_request where uuid='"+requestUuid+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(ItemRequestRaw.class));
        log.info("getRequestByUuid[hql]:"+hql);
        List itemRequest=query.list();
        if(itemRequest!=null)
        {
            if(itemRequest.size()>0)
            {
                Iterator iterator=itemRequest.iterator();
                if(iterator.hasNext()) {
                    return (ItemRequestRaw) iterator.next();
                }
            }
        }
        return null;
    }

    @Override
    public String createItemRequestFromDispensingPoint(String requestGroupId,int itemId,int priceCategoryId,int quantity,int locationId,int sourceStoreId,int destinationStoreId)
    {
        String uuid= UUID.randomUUID().toString();
        String hql="insert into ph_item_request" +
                "(request_group_id,item_id,price_category_id,requesting_store,requested_store,quantity_requested,creator,date_created,status_code,uuid) " +
                "values('"+requestGroupId+"','"+itemId+"','"+priceCategoryId+"','"+sourceStoreId+"','"+destinationStoreId+"','"+quantity+"','4',now(),'1','"+uuid+"')";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql);

        log.info("createItemRequestFromDispensingPoint[hql]:"+hql);
        if(query.executeUpdate()==1)
        {
            return "success";
        }
        return null;
    }

    @Override
    public String saveIssuedBatchToOutstandingStock(int requestId,int itemId,int storeId,String batchNo,int priceCategoryId,int quantity)
    {
        String hql="insert into ph_item_stock_outstanding" +
                "(store_id,item_id,batch_no,price_category_id,quantity,request_id,creator,date_created) " +
                "values('"+storeId+"','"+itemId+"','"+batchNo+"','"+priceCategoryId+"','"+quantity+"','"+requestId+"','4',now())";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql);

        log.info("saveIssuedBatchToOutstandingStock[hql]:"+hql);
        if(query.executeUpdate()==1)
        {
            return "success";
        }
        return null;
    }

    @Override
    public String removeIssuedBatchFromOutstandingStock(int requestId)
    {
        String hql="delete from ph_item_stock_outstanding " +
                "where request_id='"+requestId+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql);

        log.info("removeIssuedBatchFromOutstandingStock[hql]:"+hql);
        if(query.executeUpdate()==1)
        {
            return "success";
        }
        return null;
    }

    @Override
    public String requesterCancellingRequest(int requestId)
    {
        return this.updateRequestState(requestId,2);
    }

    @Override
    public String requesterRejectsConfirmingRequest(int requestId)
    {
        if(this.removeIssuedBatchFromOutstandingStock(requestId).equals("success")){
            return this.updateRequestState(requestId,5);
        }
       return null;
    }

    @Override
    public String requesterConfirmToReceiveItems(int requestId)
    {
        ItemRequestRaw itemRequestRaw=this.getRequestById(requestId);
        if(itemRequestRaw!=null)
        {
            List<ItemOutstandingStockRaw> itemOutstandingStocks=this.getOutstandingStockForRequest(requestId);
            if(itemOutstandingStocks!=null)
            {
                int success=0;
                int toStore=itemRequestRaw.getRequestingStore();
                Iterator iterator=itemOutstandingStocks.iterator();
                while(iterator.hasNext())
                {
                    ItemOutstandingStockRaw itemOutstandingStockRaw=(ItemOutstandingStockRaw) iterator.next();
                    int itemId=itemOutstandingStockRaw.getItemId();
                    String batchNo=itemOutstandingStockRaw.getBatchNo();
                    int fromStore=itemOutstandingStockRaw.getStoreId();
                    int priceCategoryId=itemOutstandingStockRaw.getPriceCategoryId();
                    int quantity=itemOutstandingStockRaw.getQuantity();
                    String response=this.processStockTransfer(itemId,fromStore,toStore,batchNo,priceCategoryId,quantity);
                    if(response.equals("success"))
                    {
                        success++;
                    }
                }
                if(success>0) {
                    this.removeIssuedBatchFromOutstandingStock(requestId);
                    return this.updateRequestState(requestId, 4);
                }
            }
        }
        return null;
    }

    @Override
    public String issuerRejectingRequest(int requestId)
    {
        return this.updateRequestState(requestId,6);
    }

    @Override
    public String updateRequestState(int requestId,int requestCodeId)
    {
        String hql="update ph_item_request " +
                "set status_code='"+requestCodeId+"'";
        DbSession session=sessionFactory.getCurrentSession();
        if(requestCodeId==3)
        {
            hql+=",date_issued=now()";
        }
        hql+=" where request_id='"+requestId+"'";
        Query query=session.createSQLQuery(hql);
        log.info("updateRequestState[hql]:"+hql);
        if(query.executeUpdate()==1)
        {
            return "success";
        }
        return null;
    }

    @Override
    public String updateQuantityIssued(int requestId,int quantityIssued)
    {
        String hql="update ph_item_request " +
                "set quantity_issued='"+quantityIssued+"' " +
                "where request_id='"+requestId+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql);

        log.info("updateQuantityIssued[hql]:"+hql);
        if(query.executeUpdate()==1)
        {
            log.info("success..qtyIssuedUpdate");
            return "success";
        }
        return null;
    }

    @Override
    public String processStockTransfer(int itemId,int fromStoreId,int toStoreId,String batchNo,int priceCategoryId,int quantity)
    {
        ItemBatchRaw itemBatchRaw=this.getItemStockByBatchNo(itemId,batchNo,priceCategoryId,fromStoreId);
        if(itemBatchRaw!=null)
        {
            int fromStoreQuantity=itemBatchRaw.getQuantity();
            int stockToUpdateInFromStore=fromStoreQuantity-quantity;
            if(this.updateItemBatchStockQuantityInStore(itemId,fromStoreId,batchNo,priceCategoryId,stockToUpdateInFromStore).equals("success"))
            {
                ItemBatchRaw itemBatchRaw1=this.getItemStockByBatchNo(itemId,batchNo,priceCategoryId,toStoreId);
                int stockToUpdateInToStore=quantity;
                if(itemBatchRaw1!=null)
                {
                    int toStoreQuantity=itemBatchRaw1.getQuantity();
                    stockToUpdateInToStore=toStoreQuantity+quantity;
                    return this.updateItemBatchStockQuantityInStore(itemId,toStoreId,batchNo,priceCategoryId,stockToUpdateInToStore);
                }
                else
                {
                    return this.insertItemBatchStockQuantityInStore(itemId,toStoreId,batchNo,priceCategoryId,stockToUpdateInToStore);
                }
            }
        }

        return null;
    }

    public String updateItemBatchStockQuantityInStore(int itemId,int storeId,String batchNo,int priceCategoryId,int quantityToSet)
    {
        String hql="update ph_item_stock_by_batch " +
                "set quantity='"+quantityToSet+"' " +
                "where store_id='"+storeId+"' and batch_no='"+batchNo+"' and price_category_id='"+priceCategoryId+"' and item_id='"+itemId+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql);

        log.info("updateItemBatchStockQuantityInStore[hql]:"+hql);
        if(query.executeUpdate()==1)
        {
            return "success";
        }
        return null;
    }

    public String insertItemBatchStockQuantityInStore(int itemId,int storeId,String batchNo,int priceCategoryId,int quantityToSet)
    {
        String hql="insert into ph_item_stock_by_batch(store_id,item_id,batch_no,price_category_id,quantity,creator,date_created,uuid)" +
                " values('"+storeId+"','"+itemId+"','"+batchNo+"','"+priceCategoryId+"','"+quantityToSet+"','4',now(),uuid())";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql);

        log.info("insertItemBatchStockQuantityInStore[hql]:"+hql);
        if(query.executeUpdate()==1)
        {
            return "success";
        }
        return null;
    }


/*
 Stock movement & related data :: Stock status.
 @Author: Eric Mwailunga
 May,2019
*/
    @Override
    public ItemStockRaw getItemQuantityOnHandInStoreByPaymentCategory(int itemId, int priceCategoryId, int storeId)
    {
        String hql="select uuid,store_id as 'storeId',item_id as 'itemId',price_category_id as 'priceCategoryId', quantity " +
                "from ph_item_stock_on_hand where item_id='"+itemId+"' and price_category_id='"+priceCategoryId+"' and store_id='"+storeId+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(ItemStockRaw.class));
        log.info("getItemQuantityOnHandByPaymentCategory[hql]:"+hql);
        List itemStockRw=query.list();
        if(itemStockRw!=null)
        {
            if(itemStockRw.size()>0)
            {
                Iterator it=itemStockRw.iterator();

                if(it.hasNext())
                    return (ItemStockRaw) it.next();
            }
        }
        return null;
    }

    @Override
    public ItemStockRaw getItemQuantityOnHandInStore(int itemId, int storeId)
    {
        String hql="select uuid,store_id as 'storeId',item_id as 'itemId',price_category_id as 'priceCategoryId', quantity " +
                "from ph_item_stock_on_hand where item_id='"+itemId+"' and store_id='"+storeId+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(ItemStockRaw.class));
        log.info("getItemQuantityOnHandInStore[hql]:"+hql);
        List itemStockRaw=query.list();
        if(itemStockRaw!=null)
        {
            if(itemStockRaw.size()>0)
            {
                Iterator iterator=itemStockRaw.iterator();
                if(iterator.hasNext())
                    return (ItemStockRaw) iterator.next();
            }
        }
        return null;
    }

    @Override
    public DispenseMapRaw getDispensingRawMapByVisitTypeAndPaymentCategory(int visitTypeId,int paymentCategoryId)
    {
        String hql="select uuid,visit_type_id as 'visitTypeId',location_id as 'dispensingLocationId'," +
                "payment_category_id as 'paymentCategoryId' " +
                "from ph_visit_payment_dispense_map where visit_type_id='"+visitTypeId+"' and payment_category_id='"+paymentCategoryId+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(DispenseMapRaw.class));
        log.info("getDispensingRawMapByVisitTypeAndPaymentCategory[hql]:"+hql);
        List dispenseMapRaw=query.list();
        if(dispenseMapRaw!=null)
        {
            if(dispenseMapRaw.size()>0)
            {
                Iterator iterator=dispenseMapRaw.iterator();
                if(iterator.hasNext())
                    return (DispenseMapRaw) iterator.next();
            }
        }

        return null;
    }

    @Override
    public ItemBatchRaw getItemStockByBatchNo(int itemId, String batchNo, int priceCategoryId, int storeId)
    {
        String hql="select uuid,store_id as 'storeId',item_id as 'itemId',price_category_id as 'priceCategoryId',batch_no as 'batchNo',quantity " +
                "from ph_item_stock_by_batch where item_id='"+itemId+"' and price_category_id='"+priceCategoryId+"' and store_id='"+storeId+"' and quantity>0";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(ItemBatchRaw.class));
        log.info("getItemStockByBatchNo[hql]:"+hql);
        List itemBatchStock=query.list();
        if(itemBatchStock!=null)
        {
            if(itemBatchStock.size()>0)
            {
                Iterator iterator=itemBatchStock.iterator();
                if(iterator.hasNext())
                    return (ItemBatchRaw) iterator.next();
            }
        }
        return null;
    }



    @Override
    public ItemBatchRaw getItemStockByBatchNoExcludingOutstanding(int itemId, String batchNo, int priceCategoryId, int storeId)
    {
        String hql="select pisbb.uuid,pisbb.store_id as 'storeId',pisbb.item_id as 'itemId',pisbb.price_category_id as 'priceCategoryId',pisbb.batch_no as 'batchNo',pil.expiry_date as 'expiryDate'," +
                "(pisbb.quantity-ifnull((select sum(quantity) from ph_item_stock_outstanding where item_id='"+itemId+"' and batch_no='"+batchNo+"' and price_category_id='"+priceCategoryId+"' and store_id='"+storeId+"'),0)) as 'quantity' " +
                "from ph_item_stock_by_batch pisbb inner join ph_item_ledger pil on (pil.item_id=pisbb.item_id and pil.batch_no=pisbb.batch_no and pil.price_category_id=pisbb.price_category_id) where pisbb.item_id='"+itemId+"' and pisbb.price_category_id='"+priceCategoryId+"' and pisbb.store_id='"+storeId+"' and pisbb.batch_no='"+batchNo+"' and pisbb.quantity>0";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql)
                .addScalar("quantity", StandardBasicTypes.INTEGER)
                .addScalar("uuid",StandardBasicTypes.STRING)
                .addScalar("storeId",StandardBasicTypes.INTEGER)
                .addScalar("priceCategoryId",StandardBasicTypes.INTEGER)
                .addScalar("batchNo",StandardBasicTypes.STRING)
                .addScalar("itemId",StandardBasicTypes.INTEGER)
                .addScalar("expiryDate",StandardBasicTypes.DATE)
                .setResultTransformer(Transformers.aliasToBean(ItemBatchRaw.class));
        log.info("getItemStockByBatchNoExcludingOutstanding[hql]:"+hql);
        List<ItemBatchRaw> itemBatchRaws=query.list();
        if(itemBatchRaws!=null)
        {
            if(itemBatchRaws.size()>0)
            {
                Iterator iterator=itemBatchRaws.iterator();
                if(iterator.hasNext())
                    return (ItemBatchRaw) iterator.next();
            }
        }

        return null;
    }

    @Override
    public List<ItemBatchRaw> getAllStockByBatchesForItem(int itemId,int priceCategoryId,int storeId)
    {
        String hql="select uuid,store_id as 'storeId',item_id as 'itemId',price_category_id as 'priceCategoryId',batch_no as 'batchNo',quantity " +
                "from ph_item_stock_by_batch where item_id='"+itemId+"' and price_category_id='"+priceCategoryId+"' and store_id='"+storeId+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(ItemBatchRaw.class));
        log.info("getAllStockByBatchesForItem[hql]:"+hql);
        List<ItemBatchRaw> itemBatchRaws=query.list();
        if(itemBatchRaws!=null)
        {
            if(itemBatchRaws.size()>0)
            {
                return itemBatchRaws;
            }
        }
        return null;
    }

    @Override
    public List<ItemOutstandingStockRaw> getOutstandingStockForRequest(int requestId)
    {
        String hql="select outstanding_stock_id as 'outstandingStockId',store_id as 'storeId',item_id as 'itemId'" +
                ",price_category_id as 'priceCategoryId',batch_no as 'batchNo',quantity,request_id as 'requestId',creator,date_created as 'dateCreated' " +
                "from ph_item_stock_outstanding where request_id='"+requestId+"'";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(ItemOutstandingStockRaw.class));
        log.info("getOutstandingStockForRequest[hql]:"+hql);
        List<ItemOutstandingStockRaw> itemOutstandingStockRaws=query.list();
        if(itemOutstandingStockRaws!=null)
        {
            if(itemOutstandingStockRaws.size()>0)
            {
                return itemOutstandingStockRaws;
            }
        }
        return null;
    }


    @Override
    public  List<ItemStockRaw> getStockOnHandInStore(int storeId)
    {
        String hql="select pisoh.uuid,pisoh.store_id as 'storeId',pisoh.item_id as 'itemId',pisoh.price_category_id as 'priceCategoryId',pisoh.quantity " +
                "from ph_item_stock_on_hand pisoh inner join ph_item pi on pisoh.item_id=pi.item_id where pisoh.store_id='"+storeId+"' order by pi.name";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(ItemStockRaw.class));
        log.info("getAllStockByBatchesForItem[hql]:"+hql);
        List<ItemStockRaw> itemStockRaws=query.list();
        if(itemStockRaws!=null)
        {
            if(itemStockRaws.size()>0)
            {
                return itemStockRaws;
            }
        }
        return null;
    }

    @Override
    public List<ItemBatchRaw> getStockOnHandByBatchesInStore(int storeId)
    {
        String hql="select pisbb.uuid,pisbb.store_id as 'storeId',pisbb.item_id as 'itemId',pisbb.price_category_id as 'priceCategoryId',pisbb.batch_no as 'batchNo',pisbb.quantity " +
                "from ph_item_stock_by_batch pisbb inner join ph_item pi on pisbb.item_id=pi.item_id where pisbb.store_id='"+storeId+"' order by pi.name";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(ItemBatchRaw.class));
        log.info("getStockOnHandByBatchesInStore[hql]:"+hql);
        List<ItemBatchRaw> itemBatchRaws=query.list();
        if(itemBatchRaws!=null)
        {
            if(itemBatchRaws.size()>0)
            {
                return itemBatchRaws;
            }
        }
        return null;
    }

    /*
    @Override
    public String savePrescriptionOrder(int patientId,int visitId,int itemId,int paymentCategoryId,int targetDispensingPoint)
    {
        Item item=this.getItemById(itemId);
        if(item!=null)
        {
            Category category=this.getCategoryByUuid(item.getCategoryUuid());
            if(category!=null)
            {
                int categoryId=category.getCategoryId();
                String hql="insert into ph_item_prescription_order(category_id,patient_id,visit_id,item_id,payment_category_id," +
                        "target_dispensing_point_id,date_created,uuid)" +
                        " values('"+categoryId+"','"+patientId+"','"+visitId+"',"+itemId+"','"+paymentCategoryId+"','"+targetDispensingPoint+"','"+Context.getAuthenticatedUser().getUserId()+"',now(),uuid())";
                DbSession session=sessionFactory.getCurrentSession();
                Query query=session.createSQLQuery(hql);

                log.info("savePrescriptionOrder[hql]:"+hql);
                if(query.executeUpdate()==1)
                {
                    return "success";
                }
            }
        }
        return null;
    }
    */

    @Override
    public String savePrescriptionOrder(int patientId,int orderId,int visitId,int itemId,int itemCategoryId,int paymentCategoryId,int targetDispensingPoint)
    {
        String hql="insert into ph_item_prescription_order(patient_id,visit_id,item_id,item_category_id,payment_category_id," +
                "target_dispensing_point_id,date_created,uuid)" +
                " values('"+patientId+"','"+orderId+"','"+visitId+"',"+itemId+"','"+itemCategoryId+"','"+paymentCategoryId+"','"+targetDispensingPoint+"','"+Context.getAuthenticatedUser().getUserId()+"',now(),uuid())";
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql);

        log.info("savePrescriptionOrder[hql]:"+hql);
        if(query.executeUpdate()==1)
        {
            return "success";
        }
        return null;
    }

    @Override
    public List<DispenseMapRaw> getAllDispensingMap(boolean includeRetired)
    {
        String hql="select pvpdm.uuid as 'uuid',pvpdm.map_id as 'mapId',pvpdm.visit_type_id as 'visitTypeId'," +
                "pvpdm.location_id as 'dispensingLocationId',pvpdm.payment_category_id as 'paymentCategoryId'," +
                "pvpdm.retired as 'retired',pvpdm.date_created as 'dateCreated'" +
                " from ph_location_store_map plsm inner join ph_visit_payment_dispense_map pvpdm on plsm.location=pvpdm.location ";
        if(!includeRetired)
        {
            hql+=" where pvpdm.retired=0";
        }
        DbSession session=sessionFactory.getCurrentSession();
        Query query=session.createSQLQuery(hql).setResultTransformer(Transformers.aliasToBean(DispenseMapRaw.class));

        log.info("getAllDispensingMap[hql]:"+hql);
        List dispensingMap=query.list();
        if(dispensingMap!=null)
        {
            if(dispensingMap.size()>0)
            {
                return dispensingMap;
            }
        }
        return null;
    }
}