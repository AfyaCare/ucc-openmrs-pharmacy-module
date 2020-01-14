/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.pharmacy.api.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openmrs.*;
import org.openmrs.api.ConceptService;
import org.openmrs.api.LocationService;
import org.openmrs.api.OrderService;
import org.openmrs.api.VisitService;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.api.db.PharmacyDAO;
import org.openmrs.module.pharmacy.model.item.*;
import org.openmrs.module.pharmacy.model.item.drug.ItemDrug;
import org.openmrs.module.pharmacy.model.item.itemcategory.Category;
import org.openmrs.module.pharmacy.model.item.itemcategory.SubCategory;
import org.openmrs.module.pharmacy.model.item.itemcategory.SubCategoryAttribute;
import org.openmrs.module.pharmacy.model.ledger.LedgerLine;
import org.openmrs.module.pharmacy.model.ledger.LedgerRawLine;
import org.openmrs.module.pharmacy.model.ledger.LedgerType;
import org.openmrs.module.pharmacy.model.item.drug.DosageForm;
import org.openmrs.module.pharmacy.model.item.drug.Drug;
import org.openmrs.module.pharmacy.model.misc.StockPoint;
import org.openmrs.module.pharmacy.model.price.ItemPrice;
import org.openmrs.module.pharmacy.model.price.ItemPriceRaw;
import org.openmrs.module.pharmacy.model.price.PriceCategory;
import org.openmrs.module.pharmacy.model.stock.ItemBatch;
import org.openmrs.module.pharmacy.model.stock.ItemStock;
import org.openmrs.module.pharmacy.model.stock.dispense.DispenseMap;
import org.openmrs.module.pharmacy.model.stock.dispense.DispensingPoint;
import org.openmrs.module.pharmacy.model.stock.dispense.VisitType;
import org.openmrs.module.pharmacy.model.stock.dispense.raw.DispenseMapRaw;
import org.openmrs.module.pharmacy.model.stock.raw.ItemBatchRaw;
import org.openmrs.module.pharmacy.model.stock.raw.ItemStockRaw;
import org.openmrs.module.pharmacy.model.stock.map.LocationStoreMap;
import org.openmrs.module.pharmacy.model.stock.request.ItemRequest;
import org.openmrs.module.pharmacy.model.stock.request.raw.ItemRequestRaw;
import org.openmrs.module.pharmacy.model.stock.request.RequestStatusCode;
import org.openmrs.module.pharmacy.model.store.Store;
import org.openmrs.util.PrivilegeConstants;

import java.util.*;

/**
 * Implementation of {@link PharmacyService}.
 */
public class PharmacyServiceImpl extends BaseOpenmrsService implements PharmacyService {
	
    protected final Log log = LogFactory.getLog(this.getClass());
    private PharmacyDAO pharmacyDAO;

    public PharmacyDAO getPharmacyDAO() {
        return pharmacyDAO;
    }

    public void setPharmacyDAO(PharmacyDAO pharmacyDAO) {
        this.pharmacyDAO = pharmacyDAO;
    }


    @Override
    public String createStore(String storeName) {
        return pharmacyDAO.createStore(storeName);
    }

    @Override
    public String createSubStore(String storeName, String parentStoreUuid) {
        return pharmacyDAO.createSubStore(storeName,parentStoreUuid);
    }


    @Override
    public Store getStoreByUuid(String storeUuid) {
        return pharmacyDAO.getStoreByUuid(storeUuid);
    }

    @Override
    public Store getStoreByName(String storeName) {
        return pharmacyDAO.getStoreByName(storeName);
    }

    @Override
    public List getStoreBySubName(String nameSubString) {
        return pharmacyDAO.getStoreBySubName(nameSubString);
    }

    @Override
    public List getStoreSubStore(String storeUuid, boolean includeRetired) {
        return pharmacyDAO.getStoreSubStore(storeUuid,includeRetired);
    }

    @Override
    public List getAllStores(boolean includeRetired) {
        return pharmacyDAO.getAllStores(includeRetired);
    }

    @Override
    public List getAllStoresFull(boolean includeRetired) {
        return pharmacyDAO.getAllStoresFull(includeRetired);
    }


    @Override
    public Store getSubStoreByUuid(String subStoreUuid){
        return pharmacyDAO.getSubStoreByUuid(subStoreUuid);
    }
    @Override
    public Store getSubStoreByName(String subStoreName)
    {
        return pharmacyDAO.getSubStoreByName(subStoreName);
    }

    @Override
    public List getSubStoreBySubName(String subStoreSearchName)
    {
        return pharmacyDAO.getSubStoreBySubName(subStoreSearchName);
    }
    @Override
    public List getAllSubStore(boolean includeRetired) {
        return pharmacyDAO.getAllSubStore(includeRetired);
    }



    @Override
    public String updateStoreByUuid(String storeUuid, String storeName, boolean retired) {
        return pharmacyDAO.updateStoreByUuid(storeUuid,storeName,retired);
    }
    @Override
    public String deleteStoreByUuid(String storeUuid)
    {
        return pharmacyDAO.deleteStoreByUuid(storeUuid);
    }



    @Override
    public String deleteSubStoreByUuid(String subStoreUuid)
    {
        return pharmacyDAO.deleteSubStoreByUuid(subStoreUuid);
    }
    @Override
    public String updateSubStoreByUuid(String storeUuid,String storeName,String parentUuid,boolean retired)
    {
        return pharmacyDAO.updateSubStoreByUuid(storeUuid,storeName,parentUuid,retired);
    }


/*
 Creating categories & related data
 @Author: Eric Mwailunga
 May,2019
*/
    @Override
    public Category getCategoryByUuid(String categoryUuid)
    {
        return pharmacyDAO.getCategoryByUuid(categoryUuid);
    }
    @Override
    public Category getCategoryByUuidFull(String categoryUuid)
    {
        return pharmacyDAO.getCategoryByUuidFull(categoryUuid);
    }
    @Override
    public List getAllCategories(boolean includeRetired)
    {
        return pharmacyDAO.getAllCategories(includeRetired);
    }
    @Override
    public List getAllCategoriesFull(boolean includeRetired){
        return pharmacyDAO.getAllCategoriesFull(includeRetired);
    }

/*
 Creating subcategories & related data
 @Author: Eric Mwailunga
 May,2019
*/
    @Override
    public String createSubCategory(String subCategoryName,String parentCategoryUuid)
    {
        return pharmacyDAO.createSubCategory(subCategoryName,parentCategoryUuid);
    }

    @Override
    public SubCategory getSubCategoryByUuid(String subCategoryUuid)
    {
        return pharmacyDAO.getSubCategoryByUuid(subCategoryUuid);
    }

    @Override
    public SubCategory getFullSubCategoryByUuid(String subCategoryUuid)
    {
        return pharmacyDAO.getFullSubCategoryByUuid(subCategoryUuid);
    }

    @Override
    public SubCategory getSubCategoryByName(String subCategoryName)
    {
        return pharmacyDAO.getSubCategoryByName(subCategoryName);
    }

    @Override
    public List getSubCategoryBySubName(String subCategorySubName)
    {
        return pharmacyDAO.getSubCategoryBySubName(subCategorySubName);
    }

    @Override
    public List getAllSubCategories(boolean includeRetired){
        return pharmacyDAO.getAllSubCategories(includeRetired);
    }

    @Override
    public List<SubCategory> getAllSubCategoriesFull(boolean includeRetired){
        return pharmacyDAO.getAllSubCategoriesFull(includeRetired);
    }

    @Override
    public String updateSubCategoryByUuid(String subCategoryUuid,String subCategoryName,String parentUuid,boolean retire)
    {
        return pharmacyDAO.updateSubCategoryByUuid(subCategoryUuid,subCategoryName,parentUuid,retire);
    }

    @Override
    public String deleteSubCategoryByUuid(String subCategoryUuid)
    {
        return pharmacyDAO.deleteSubCategoryByUuid(subCategoryUuid);
    }

/*
 Creating categories' attributes & related data
 @Author: Eric Mwailunga
 May,2019
*/
    @Override
    public String createSubCategoryAttributeType(String subCategoryUuid,String attributeTypeName)
    {
        return pharmacyDAO.createSubCategoryAttributeType(subCategoryUuid,attributeTypeName);
    }

    @Override
    public String createSubCategoryAttributeTypes(String subCategoryUuid,String attributeTypeNames)
    {
        return pharmacyDAO.createSubCategoryAttributeTypes(subCategoryUuid,attributeTypeNames);
    }

    @Override
    public List getAttributeTypesBySubCategoryUuid(String subCategoryUuid)
    {
        return pharmacyDAO.getAttributeTypesBySubCategoryUuid(subCategoryUuid);
    }

    @Override
    public String updateSubCategoryAttributeType(String attributeTypeUuid,String name,String subCategoryUuid,boolean retire)
    {
        return pharmacyDAO.updateSubCategoryAttributeType(attributeTypeUuid,name,subCategoryUuid,retire);
    }

   /* @Override
    public List<SubCategoryAttribute> getAllSubCategoriesWithAttributeTypes(boolean includeRetired)
    {
        return pharmacyDAO.getAllSubCategoriesWithAttributeTypes(includeRetired);
    }
    */
   @Override
   public List<SubCategoryAttribute> getCategoryByUuidWithFullSubCategoryAttributeTypes(String categoryUuid,boolean includeRetired)
   {
       return pharmacyDAO.getSubCategoryByCategoryUuidWithFullAttributeTypes(categoryUuid,includeRetired);
   }

/*
 Items and Concepts related data values
 @Author: Eric Mwailunga
 May,2019
*/

    private ConceptName setConceptNameProperties(String name)
    {
        ConceptName conceptName=new ConceptName();
        List<Locale> localeList = new ArrayList<>(Context.getConceptService().getLocalesOfConceptNames());
        conceptName.setName(name);
        int counter=0,length=localeList.size();
        while(counter<length)
        {
            if(localeList.get(counter).toString().equals("en"))
            {
                conceptName.setLocale(localeList.get(counter));
            }
            counter++;
        }

        return conceptName;
    }

    private ItemConcept getConceptByUuid(String uuid)
    {
        ConceptService conceptService=Context.getConceptService();
        Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPTS);
        Concept concept=conceptService.getConceptByUuid(uuid);

        if(concept!=null)
        {
            ItemConcept itemConcept =new ItemConcept();
            itemConcept.setConceptId(concept.getConceptId());
            itemConcept.setName(concept.getName().toString());
            itemConcept.setUuid(concept.getUuid());
            return itemConcept;
        }
        Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPTS);
        return null;
    }

    private ItemConcept getConceptById(int conceptId)
    {
        ConceptService conceptService=Context.getConceptService();
        Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPTS);
        Concept concept=conceptService.getConcept(conceptId);

        if(concept!=null)
        {
            ItemConcept itemConcept =new ItemConcept();
            itemConcept.setUuid(concept.getUuid());
            itemConcept.setConceptId(concept.getConceptId());
            itemConcept.setName(concept.getName().toString());
            return itemConcept;
        }
        Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPTS);
        return null;
    }

    @Override
    public String createDosageForm(String dosageFormName){

        Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPTS);
        Context.addProxyPrivilege(PrivilegeConstants.MANAGE_CONCEPTS);
        Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPT_DATATYPES);
        Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPT_CLASSES);
        Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPT_ATTRIBUTE_TYPES);

        ConceptService conceptService=Context.getConceptService();
        Concept concept;
        concept=conceptService.getConceptByName(dosageFormName);
        boolean success=false;
        if(concept!=null)
        {
            return "exists";
        }
        else {

            ConceptName conceptName=this.setConceptNameProperties(dosageFormName);

            Concept dosageFormConcept=new Concept();
            dosageFormConcept.setPreferredName(conceptName);
            dosageFormConcept.setFullySpecifiedName(conceptName);
            dosageFormConcept.setConceptClass(conceptService.getConceptClass(11));
            dosageFormConcept.setDatatype(conceptService.getConceptDatatype(4));
            dosageFormConcept.setCreator(Context.getAuthenticatedUser());
            Concept savedDosageForm=conceptService.saveConcept(dosageFormConcept);

            if (savedDosageForm!=null) {
                Concept dosingUnits = conceptService.getConcept(57);
                dosingUnits.addSetMember(savedDosageForm);
                success=true;
            }

        }

        Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPTS);
        Context.removeProxyPrivilege(PrivilegeConstants.MANAGE_CONCEPTS);
        Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPT_DATATYPES);
        Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPT_CLASSES);
        Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPT_ATTRIBUTE_TYPES);

        if(success)
            return "success";
        else
            return null;
    }

    @Override
    public ItemConcept getDosageFormByUuid(String uuid)
    {
        ItemConcept itemConcept =this.getConceptByUuid(uuid);
        if(itemConcept !=null)
        {
            return itemConcept;
        }
        return null;
    }

    @Override
    public List getAllDosageForms()
    {
        ConceptService conceptService=Context.getConceptService();

        Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPTS);

        Concept concept=conceptService.getConcept(57);
        List conceptList=concept.getSetMembers();

        Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPTS);
        if(conceptList!=null)
        {
            if(conceptList.size()>0)
            {
                List<ItemConcept> itemConceptList =new ArrayList<>();
                Iterator iterator=conceptList.iterator();
                while(iterator.hasNext())
                {
                    Concept resultConcept=(Concept) iterator.next();
                    String conceptUuid=resultConcept.getUuid();
                    String conceptName=resultConcept.getName().toString();
                    int conceptId=resultConcept.getConceptId();

                    ItemConcept itemConcept =new ItemConcept();
                    itemConcept.setConceptId(conceptId);
                    itemConcept.setName(conceptName);
                    itemConcept.setUuid(conceptUuid);

                    itemConceptList.add(itemConcept);
                }
                return itemConceptList;
            }
        }
        return  null;
    }

/*
 Items subcategories attributes values
 @Author: Eric Mwailunga
 May,2019
*/

    @Override
    public String createGenericDrugName(String genericDrugName)
    {
        Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPTS);
        Context.addProxyPrivilege(PrivilegeConstants.MANAGE_CONCEPTS);
        Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPT_DATATYPES);
        Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPT_CLASSES);
        Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPT_ATTRIBUTE_TYPES);

        ConceptService conceptService=Context.getConceptService();
        Concept concept;
        concept=conceptService.getConceptByName(genericDrugName);
        boolean success=false;
        if(concept!=null)
        {
            return "exists";
        }
        else
        {
            ConceptName conceptName=this.setConceptNameProperties(genericDrugName);

            Concept genericDrugConcept=new Concept();
            genericDrugConcept.setPreferredName(conceptName);
            genericDrugConcept.setFullySpecifiedName(conceptName);

            genericDrugConcept.setConceptClass(conceptService.getConceptClass(3));
            genericDrugConcept.setDatatype(conceptService.getConceptDatatype(4));
            genericDrugConcept.setCreator(Context.getAuthenticatedUser());

            Concept savedGenericName=conceptService.saveConcept(genericDrugConcept);
            if(savedGenericName!=null)
            {
                success=true;
            }
        }

        Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPTS);
        Context.removeProxyPrivilege(PrivilegeConstants.MANAGE_CONCEPTS);
        Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPT_DATATYPES);
        Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPT_CLASSES);
        Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPT_ATTRIBUTE_TYPES);

        if(success)
            return "success";
        else
            return null;
    }

    @Override
    public ItemConcept getGenericDrugNameByUuid(String uuid)
    {
        ItemConcept itemConcept =this.getConceptByUuid(uuid);
        if(itemConcept !=null)
        {
            return itemConcept;
        }
        return null;
    }

    @Override
    public List getGenericDrugNameBySubName(String subName)
    {

        ConceptService conceptService=Context.getConceptService();

        Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPTS);
        Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPT_ATTRIBUTE_TYPES);
        Context.addProxyPrivilege(PrivilegeConstants.MANAGE_CONCEPTS);
        Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPT_DATATYPES);
        Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPT_CLASSES);

        List<Concept> genericNamesList=conceptService.getConceptsByClass(conceptService.getConceptClassByName("Drug"));
        if(genericNamesList!=null)
        {
            if(genericNamesList.size()>0)
            {
                List<ItemConcept> itemConceptList =new ArrayList<>();
                Iterator iterator=genericNamesList.iterator();
                while(iterator.hasNext())
                {
                    Concept resultConcept=(Concept) iterator.next();
                    String conceptName=resultConcept.getName().toString();
                    if(conceptName.contains(subName))
                    {
                        int conceptId=resultConcept.getConceptId();
                        String conceptUuid=resultConcept.getUuid();
                        ItemConcept itemConcept =new ItemConcept();
                        itemConcept.setName(conceptName);
                        itemConcept.setUuid(conceptUuid);
                        itemConcept.setConceptId(conceptId);
                        itemConceptList.add(itemConcept);
                    }
                }
                return itemConceptList;
            }
        }

        Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPTS);
        Context.removeProxyPrivilege(PrivilegeConstants.MANAGE_CONCEPTS);
        Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPT_DATATYPES);
        Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPT_CLASSES);
        Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPT_ATTRIBUTE_TYPES);

        return null;
    }
    @Override
    public String updateGenericDrugNameByUuid(String uuid,String genericDrugName,boolean retired)
    {
        Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPTS);
        Context.addProxyPrivilege(PrivilegeConstants.MANAGE_CONCEPTS);
        Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPT_DATATYPES);
        Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPT_CLASSES);
        Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPT_ATTRIBUTE_TYPES);

        ConceptService conceptService=Context.getConceptService();
        Concept concept;
        concept=conceptService.getConceptByUuid(uuid);
        boolean success=false;
        if(concept!=null)
        {
            ConceptName conceptName=this.setConceptNameProperties(genericDrugName);

            conceptName.setChangedBy(Context.getAuthenticatedUser());
            concept.setPreferredName(conceptName);
            concept.setFullySpecifiedName(conceptName);
            concept.setRetired(retired);
            if(retired) {
                concept.setRetiredBy(Context.getAuthenticatedUser());
            }
            concept.setCreator(concept.getCreator());
            concept.setChangedBy(Context.getAuthenticatedUser());

            concept.setConceptClass(conceptService.getConceptClass(3));
            concept.setDatatype(conceptService.getConceptDatatype(4));

            Concept savedGenericName=conceptService.saveConcept(concept);
            if(savedGenericName!=null)
            {
                success=true;
            }
        }

        Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPTS);
        Context.removeProxyPrivilege(PrivilegeConstants.MANAGE_CONCEPTS);
        Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPT_DATATYPES);
        Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPT_CLASSES);
        Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPT_ATTRIBUTE_TYPES);

        if(success)
            return "success";
        else
            return null;
    }

    @Override
    public List getAllGenericDrugNames(boolean includeRetired)
    {

        ConceptService conceptService=Context.getConceptService();

        Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPTS);
        Context.addProxyPrivilege(PrivilegeConstants.MANAGE_CONCEPTS);
        Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPT_DATATYPES);
        Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPT_CLASSES);
        Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPT_ATTRIBUTE_TYPES);

        List<Concept> genericNamesList=conceptService.getConceptsByClass(conceptService.getConceptClassByName("Drug"));
        if(genericNamesList!=null)
        {
            if(genericNamesList.size()>0)
            {
                List<ItemConcept> itemConceptList =new ArrayList<>();
                Iterator iterator=genericNamesList.iterator();
                while(iterator.hasNext())
                {
                    Concept resultConcept=(Concept) iterator.next();
                    int conceptId=resultConcept.getConceptId();
                    String conceptUuid=resultConcept.getUuid();
                    String conceptName=resultConcept.getName().toString();

                    ItemConcept itemConcept =new ItemConcept();
                    itemConcept.setConceptId(conceptId);
                    itemConcept.setName(conceptName);
                    itemConcept.setUuid(conceptUuid);

                    itemConceptList.add(itemConcept);
                }
                return itemConceptList;
            }
        }

        Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPTS);
        Context.removeProxyPrivilege(PrivilegeConstants.MANAGE_CONCEPTS);
        Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPT_DATATYPES);
        Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPT_CLASSES);
        Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPT_ATTRIBUTE_TYPES);

        return null;
    }


/*
 Drug items registration
 @Author: Eric Mwailunga
 May,2019
*/

    private String saveDrugItem(String categoryUuid,String name,int drugId)
    {
        return pharmacyDAO.saveDrugItem(categoryUuid,name,drugId);
    }

    @Override
    public String saveDrug(String categoryUuid,int genericNameConceptId,String genericName,String strength,int dosageFormConceptId)
    {
        Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPTS);
        Context.addProxyPrivilege(PrivilegeConstants.MANAGE_CONCEPTS);
        Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPT_DATATYPES);
        Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPT_CLASSES);
        Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPT_ATTRIBUTE_TYPES);
        Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPT_MAP_TYPES);

        ConceptService conceptService=Context.getConceptService();
        Concept genericConcept=conceptService.getConcept(genericNameConceptId);
        Concept dosageConcept=conceptService.getConcept(dosageFormConceptId);


        String drugName=genericName.trim() + " " + strength.trim();
        org.openmrs.Drug drug=new org.openmrs.Drug();
        drug.setConcept(genericConcept);
        drug.setName(drugName);
        drug.setDosageForm(dosageConcept);
        drug.setStrength(strength);

        org.openmrs.Drug savedDrug=conceptService.saveDrug(drug);
        boolean success=false;
        if(savedDrug!=null)
        {
            int drugId=savedDrug.getDrugId();
            log.info("SaveDrug[DrugId]: "+drugId);
            String response=this.saveDrugItem(categoryUuid,drugName,drugId);
            if(response.equals("success"))
            {
                success=true;
            }
        }

        Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPTS);
        Context.removeProxyPrivilege(PrivilegeConstants.MANAGE_CONCEPTS);
        Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPT_DATATYPES);
        Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPT_CLASSES);
        Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPT_ATTRIBUTE_TYPES);
        Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPT_MAP_TYPES);

        if(success)
            return "success";
        else
            return null;
    }

    @Override
    public List<ItemDrug> getDrugsBySubName(String drugSubName)
    {
        Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPTS);
        Context.addProxyPrivilege(PrivilegeConstants.MANAGE_CONCEPTS);
        Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPT_DATATYPES);
        Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPT_CLASSES);
        Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPT_ATTRIBUTE_TYPES);
        Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPT_MAP_TYPES);

        ConceptService conceptService=Context.getConceptService();

        List<org.openmrs.Drug> drugs=conceptService.getDrugs(drugSubName);
        List<ItemDrug> itemDrugs=new ArrayList<>();
        if(drugs!=null)
        {
            if(drugs.size()>0)
            {
                Iterator iterator=drugs.iterator();
                while(iterator.hasNext())
                {
                    org.openmrs.Drug openmrsDrug=(org.openmrs.Drug) iterator.next();
                    ItemDrugMap itemDrugMap=pharmacyDAO.getItemDrugMapByDrugId(openmrsDrug.getId());
                    if(itemDrugMap!=null)
                    {
                        Item item=pharmacyDAO.getItemById(itemDrugMap.getItemId());
                        ItemDrug itemDrug=new ItemDrug();

                        DosageForm dosageForm=new DosageForm();
                        dosageForm.setConceptId(openmrsDrug.getDosageForm().getConceptId());
                        dosageForm.setName(openmrsDrug.getDosageForm().getName().getName());
                        Drug drug=new Drug();
                        drug.setDrugId(openmrsDrug.getDrugId());
                        drug.setStrength(openmrsDrug.getStrength());
                        drug.setDosageForm(dosageForm);
                        drug.setName(openmrsDrug.getName());
                        String itemName=drug.getName()+" ("+drug.getDosageForm().getName()+")";

                        itemDrug.setItemName(itemName);
                        itemDrug.setItemId(itemDrugMap.getItemId());
                        itemDrug.setItemUuid(item.getItemUuid());
                        itemDrug.setRetired(item.getRetired());
                        itemDrug.setDrug(drug);
                        itemDrugs.add(itemDrug);
                    }
                }
            }
        }

        Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPTS);
        Context.removeProxyPrivilege(PrivilegeConstants.MANAGE_CONCEPTS);
        Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPT_DATATYPES);
        Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPT_CLASSES);
        Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPT_ATTRIBUTE_TYPES);
        Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPT_MAP_TYPES);
        if(itemDrugs!=null)
        {
            if(itemDrugs.size()>0)
                return itemDrugs;
        }
        return null;
    }


    @Override
    public String saveNonDrugItem(String categoryUuid,String subCategoryUuid,String attributesJsonString)
    {
        log.info("Attributes::Before\n"+attributesJsonString);
        attributesJsonString=attributesJsonString.replace("\"","");
        log.info("Attributes::After\n"+attributesJsonString);
        JSONObject jsonObject=new JSONObject(attributesJsonString);
        JSONArray jsonArray = jsonObject.getJSONArray("attributes");

        String itemName=pharmacyDAO.getSubCategoryByUuid(subCategoryUuid).getName();
        int counter=0;
        while(counter<jsonArray.length())
        {
            String attributeTypename=jsonArray.getJSONObject(counter).getString("attributeTypename");
            String value=jsonArray.getJSONObject(counter).getString("value");
            if(attributeTypename.equalsIgnoreCase("Name"))
            {
                itemName=value;
            }
            counter++;
        }

        Item item=pharmacyDAO.saveNonDrugItem(categoryUuid,itemName);

        if(item!=null)
        {
            counter=0;
            int successCounter=0;
            while(counter<jsonArray.length())
            {
                String attributeTypeUuid=jsonArray.getJSONObject(counter).getString("attributeTypeUuid");
                String value=jsonArray.getJSONObject(counter).getString("value");
                if(!value.isEmpty())
                {
                    String saveResponse=pharmacyDAO.saveItemAttributeValue(item.getItemId(),attributeTypeUuid,value);
                    if(saveResponse.equals("success"))
                    {
                        successCounter++;
                    }
                }
                counter++;
            }
            if(successCounter>0)
            {
                return "success";
            }
        }
        return null;
    }

    @Override
    public ItemDrug getDrugByItemUuid(String itemUuid)
    {
        return pharmacyDAO.getDrugByItemUuidWithTheirAttributes(itemUuid);
    }

    @Override
    public ItemNonDrug getNonDrugByItemUuid(String itemUuid)
    {
        return pharmacyDAO.getNonDrugByItemUuidWithTheirAttributes(itemUuid);
    }

    @Override
    public String updateNonDrugItem(String uuid,boolean retired,String attributesJsonString)
    {
        Item item=this.getItemByUuid(uuid);
        int itemId=item.getItemId();

        attributesJsonString=attributesJsonString.replace("\"","");
        JSONObject jsonObject=new JSONObject(attributesJsonString);
        JSONArray jsonArray = jsonObject.getJSONArray("attributes");

        String itemName=item.getItemName();
        int counter=0;
        while(counter<jsonArray.length())
        {
            String attributeTypename=jsonArray.getJSONObject(counter).getString("attributeTypename");
            String value=jsonArray.getJSONObject(counter).getString("value");
            if(attributeTypename.equalsIgnoreCase("Name"))
            {
                log.info(".");
                itemName=value;
            }
            counter++;
        }

        String updateItem=pharmacyDAO.updateNonDrugItem(itemId,itemName,retired);
        if(updateItem!=null)
        {
            counter=0;
            int successCounter=0;
            String updateItemAttributes;
            while(counter<jsonArray.length()) {
                String attributeTypeUuid = jsonArray.getJSONObject(counter).getString("attributeTypeUuid");
                String value = jsonArray.getJSONObject(counter).getString("value");
                if (!value.isEmpty()) {
                    updateItemAttributes = pharmacyDAO.updateItemAttributeValue(itemId,attributeTypeUuid,value);
                    if(updateItemAttributes.equals("success"))
                    {
                        successCounter++;
                    }
                }
            }
            if(successCounter>0)
            {
                return "success";
            }
        }
        return null;
    }

    @Override
    public Item getNonDrugByNameWithoutTheirAttributes(String itemName)
    {
        return pharmacyDAO.getNonDrugByNameWithoutTheirAttributes(itemName);
    }


    @Override
    public ItemNonDrug getNonDrugByNameWithTheirAttributes(String itemName)
    {
        return pharmacyDAO.getNonDrugByNameWithTheirAttributes(itemName);
    }

    @Override
    public List<Item> getAllNonDrugsWithoutTheirAttributes(boolean includeRetired)
    {
        return pharmacyDAO.getAllNonDrugsByWithoutTheirAttributes(includeRetired);
    }

    @Override
    public List<ItemNonDrug> getAllNonDrugsWithTheirAttributes(boolean includeRetired)
    {
        return pharmacyDAO.getAllNonDrugsWithTheirAttributes(includeRetired);
    }


    @Override
    public List<ItemDrug> getAllDrugItems(boolean includeRetired)
    {
        return pharmacyDAO.getAllDrugItems(includeRetired);
    }

    @Override
    public Item getItemByUuid(String itemUuid){
        return pharmacyDAO.getItemByUuid(itemUuid);
    }

    @Override
    public Item getItemById(int itemId)
    {
        Item item=pharmacyDAO.getItemById(itemId);
        item.setCategoryName(getCategoryByUuid(item.getCategoryUuid()).getName());
        return item;
    }

    @Override
    public List<Item> getAllItems(boolean includeRetired)
    {
        return pharmacyDAO.getAllItems(includeRetired);
    }

    @Override
    public List<Item> getAllItemsBySubName(String itemSubName)
    {
        List<Item> items=pharmacyDAO.getAllItemsBySubName(itemSubName);
        List<Item> itemsToReturn=new ArrayList<>();
        if(items!=null)
        {
            if(items.size()>0)
            {
                Iterator iterator=items.iterator();
                while(iterator.hasNext())
                {
                    Item item=(Item) iterator.next();
                    Category category=pharmacyDAO.getCategoryByUuid(item.getCategoryUuid());
                    if(category.getName().equalsIgnoreCase("Drugs"))
                    {
                        ItemDrug itemDrug=pharmacyDAO.getDrugByItemUuidWithTheirAttributes(item.getItemUuid());
                        item.setItemName(itemDrug.getItemName());
                    }
                    itemsToReturn.add(item);
                }
            }
        }
        return itemsToReturn;
    }

/*
 Item price & related data
 @Author: Eric Mwailunga
 May,2019
*/
    @Override
    public String createItemSellingPrice(String itemUuid,int priceCategoryId,double sellingPrice)
    {
        return pharmacyDAO.createItemSellingPrice(itemUuid,priceCategoryId,sellingPrice);
    }

    @Override
    public ItemPrice getItemSellingPrice(String itemUuid, int priceCategoryId)
    {
        Item item=this.getItemByUuid(itemUuid);
        ItemPriceRaw itemPriceRaw=pharmacyDAO.getItemSellingPriceByItemIdAndPriceCategoryId(item.getItemId(),priceCategoryId);
        if(itemPriceRaw!=null)
        {
            PriceCategory priceCategory=pharmacyDAO.getPriceCategoryById(itemPriceRaw.getCategoryId());
            if(priceCategory!=null)
            {
                ItemPrice itemPrice=new ItemPrice();
                itemPrice.setItemPriceId(itemPrice.getItemPriceId());
                itemPrice.setItemName(item.getItemName());
                itemPrice.setSellingPrice(itemPriceRaw.getSellingPrice());
                itemPrice.setPriceCategory(priceCategory);
                return itemPrice;
            }
        }
        return null;
    }

    @Override
    public ItemPrice getItemSellingPriceByItemPriceUuid(String itemPriceUuid)
    {
        ItemPrice itemPrice=new ItemPrice();
        ItemPriceRaw itemPriceRaw=pharmacyDAO.getItemSellingPriceByItemPriceUuid(itemPriceUuid);
        if(itemPriceRaw!=null)
        {
            Item item=pharmacyDAO.getItemById(itemPriceRaw.getItemId());
            if(item!=null)
            {
                PriceCategory priceCategory=pharmacyDAO.getPriceCategoryById(itemPriceRaw.getCategoryId());
                if(priceCategory!=null)
                {
                    String itemName=item.getItemName();
                    if(item.getCategoryName().equals("Drugs"))
                    {
                        ItemDrug itemDrug=pharmacyDAO.getDrugByItemUuidWithTheirAttributes(item.getItemUuid());
                        itemName=itemDrug.getItemName();
                    }
                    itemPrice.setItemPriceId(itemPrice.getItemPriceId());
                    itemPrice.setItemName(itemName);
                    itemPrice.setSellingPrice(itemPriceRaw.getSellingPrice());
                    itemPrice.setPriceCategory(priceCategory);
                }
            }
        }
        return itemPrice;

    }
    @Override
    public String updateItemSellingPrice(String itemUuid,int priceCategoryId,double sellingPrice)
    {
        Item item=this.getItemByUuid(itemUuid);
        int itemId=item.getItemId();
        return pharmacyDAO.updateItemSellingPrice(itemId,priceCategoryId,sellingPrice);
    }

    @Override
    public List<ItemPrice> getAllItemSellingPrice()
    {
        List<ItemPrice> itemPriceList=new ArrayList<>();
        List<ItemPriceRaw> itemPriceRaws=pharmacyDAO.getAllItemSellingPrice();
        if(itemPriceRaws!=null)
        {
            if(itemPriceRaws.size()>0)
            {
                Iterator iterator=itemPriceRaws.iterator();
                while(iterator.hasNext())
                {
                    ItemPriceRaw itemPriceRaw=(ItemPriceRaw) iterator.next();
                    Item item=pharmacyDAO.getItemById(itemPriceRaw.getItemId());
                    PriceCategory priceCategory=pharmacyDAO.getPriceCategoryById(itemPriceRaw.getCategoryId());
                    String itemName=item.getItemName();
                    if(item.getCategoryName().equals("Drugs"))
                    {
                        ItemDrug itemDrug=pharmacyDAO.getDrugByItemUuidWithTheirAttributes(item.getItemUuid());
                        itemName=itemDrug.getItemName();
                    }
                    ItemPrice itemPrice=new ItemPrice();
                    itemPrice.setItemPriceId(itemPrice.getItemPriceId());
                    itemPrice.setItemName(itemName);
                    itemPrice.setSellingPrice(itemPriceRaw.getSellingPrice());
                    itemPrice.setPriceCategory(priceCategory);
                    itemPriceList.add(itemPrice);
                }
                return itemPriceList;
            }
        }
        return null;
    }

/*
 Price category & related data
 @Author: Eric Mwailunga
 May,2019
*/

    @Override
    public String createPriceCategory(String categoryName)
    {
        return pharmacyDAO.createPriceCategory(categoryName);
    }

    @Override
    public PriceCategory getPriceCategoryByUuid(String uuid)
    {
        return pharmacyDAO.getPriceCategoryByUuid(uuid);
    }

    @Override
    public String updatePriceCategory(String categoryUuid,String categoryName,boolean retired)
    {
        return pharmacyDAO.updatePriceCategory(categoryUuid,categoryName,retired);
    }

    @Override
    public PriceCategory getPriceCategoryByName(String categoryName)
    {
        return pharmacyDAO.getPriceCategoryByName(categoryName);
    }

    @Override
    public List<PriceCategory> getAllPriceCategories(boolean includeRetired)
    {
        return pharmacyDAO.getAllPriceCategories(includeRetired);
    }


/*
LedgerRawLine entries, types & related data
@Author: Eric Mwailunga
May,2019
*/

    @Override
    public String createLedgerType(String ledgerTypeName,String operation)
    {
        return pharmacyDAO.createLedgerType(ledgerTypeName,operation);
    }

    @Override
    public LedgerType getLedgerTypeByUuid(String ledgerTypeUuid)
    {
        return pharmacyDAO.getLedgerTypeByUuid(ledgerTypeUuid);
    }

    @Override
    public String updateLedgerTypeByUuid(String ledgerTypeUuid,String ledgerTypeName,String operation,boolean retired)
    {
        return pharmacyDAO.updateLedgerTypeByUuid(ledgerTypeUuid,ledgerTypeName,operation,retired);
    }

    @Override
    public List<LedgerType> getAllLedgerTypes(boolean includeRetired)
    {
        return pharmacyDAO.getAllLedgerTypes(includeRetired);
    }


/*
 LedgerRawLine entries & related data
 @Author: Eric Mwailunga
 May,2019
*/

    @Override
    public String createLedgerEntry(int ledgerTypeId,int itemId,int priceCategoryId,String batchNo,String invoiceNo,double buyingPrice,int quantity, String dateMoved, String expiryDate,int dosageUnits,String remarks){
        return pharmacyDAO.createLedgerEntry(ledgerTypeId,itemId,priceCategoryId,batchNo,invoiceNo,buyingPrice,quantity,dateMoved,expiryDate,dosageUnits,remarks);
    }

    @Override
    public LedgerLine getLedgerEntryByUuid(String entryUuid)
    {

        LedgerRawLine ledgerRawLine=pharmacyDAO.getLedgerEntryByUuid(entryUuid);
        Item item=pharmacyDAO.getItemById(ledgerRawLine.getItemId());
        LedgerType ledgerType=pharmacyDAO.getLedgerTypeById(ledgerRawLine.getLedgerTypeId());
        PriceCategory priceCategory=pharmacyDAO.getPriceCategoryById(ledgerRawLine.getPriceCategoryId());

        String itemName=item.getItemName();
        if(item.getCategoryName().equals("Drugs"))
        {
            ItemDrug itemDrug=pharmacyDAO.getDrugByItemUuidWithTheirAttributes(item.getItemUuid());
            itemName=itemDrug.getItemName();
        }
        LedgerLine ledgerLine=new LedgerLine();
        ledgerLine.setEntryId(ledgerRawLine.getEntryId());
        ledgerLine.setItem(itemName);
        ledgerLine.setLedgerType(ledgerType.getName()+"("+ledgerType.getOperation()+")");
        ledgerLine.setPaymentCategory(priceCategory.getName());
        ledgerLine.setUnits(this.getConceptById(ledgerRawLine.getDosingUnitsId()).getName());
        ledgerLine.setBatchNo(ledgerRawLine.getBatchNo());
        ledgerLine.setInvoiceNo(ledgerRawLine.getInvoiceNo());
        ledgerLine.setBuyingPrice(ledgerRawLine.getBuyingPrice());
        ledgerLine.setExpiryDate(ledgerRawLine.getExpiryDate());
        ledgerLine.setDateMoved(ledgerRawLine.getDateMoved());
        ledgerLine.setQuantity(ledgerRawLine.getQuantity());
        ledgerLine.setRemarks(ledgerRawLine.getRemarks());
        ledgerLine.setUuid(ledgerRawLine.getUuid());
        return ledgerLine;
    }

    @Override
    public List<LedgerLine> getAllLedgerEntries()
    {
        List<LedgerRawLine> ledgerRawLines=pharmacyDAO.getAllLedgerEntries();
        List<LedgerLine> ledgerLines=new ArrayList<>();
        if(ledgerRawLines!=null)
        {
            if(ledgerRawLines.size()>0)
            {
                Iterator iterator=ledgerRawLines.iterator();
                while(iterator.hasNext())
                {
                    LedgerRawLine ledgerRawLine=(LedgerRawLine) iterator.next();
                    LedgerLine ledgerLine=this.getLedgerEntryByUuid(ledgerRawLine.getUuid());
                    ledgerLines.add(ledgerLine);
                }
            }
        }
        return ledgerLines;
    }


/*
 Stock movement & related data :: Dispensing points with their respective stores.
 @Author: Eric Mwailunga
 May,2019
*/

    @Override
    public StockPoint getStockPointByLocationUuid(String locationUuid)
    {
        Location location=this.getLocationByUuid(locationUuid);
        StockPoint stockPoint =new StockPoint();
        boolean success=false;
        if(location!=null)
        {
            int locationId=location.getLocationId();
            LocationStoreMap locationStoreMap =pharmacyDAO.getStoreMapByLocationId(locationId);
            if(locationStoreMap !=null)
            {
                Store store=pharmacyDAO.getSubStoreById(locationStoreMap.getStoreId());
                if(store!=null)
                {
                    stockPoint.setId(locationId);
                    stockPoint.setName(location.getName());
                    stockPoint.setStore(store);
                    success=true;
                }
            }
        }

        if(success) {
            return stockPoint;
        }
        return null;
    }


/*
 Stock movement & related data :: Item requests and issuing.
 @Author: Eric Mwailunga
 May,2019
*/
    @Override
    public String createItemRequestFromDispensingPoint(String requestGroupId,String itemUuid,String priceCategoryUuid,int quantity,String sourceLocationUuid,int destinationStoreId)
    {
        Item item=pharmacyDAO.getItemByUuid(itemUuid);
        PriceCategory priceCategory=pharmacyDAO.getPriceCategoryByUuid(priceCategoryUuid);
        StockPoint stockPoint =this.getStockPointByLocationUuid(sourceLocationUuid);

        if(item!=null&&priceCategory!=null&&stockPoint !=null)
        {
            return pharmacyDAO.createItemRequestFromDispensingPoint(requestGroupId,item.getItemId(),priceCategory.getCategoryId(),quantity, stockPoint.getId(), stockPoint.getStore().getStoreId(),destinationStoreId);
        }
        return null;
    }

    @Override
    public String createItemsRequestFromDispensingPoint(String requestGroupId,String sourceLocationUuid,int destinationStoreId,String requestsJson) {
        log.info("requestsJsonObject::Before\n" + requestsJson);
        requestsJson = requestsJson.replace("\"", "");
        log.info("requestsJsonObject::After\n" + requestsJson);
        JSONObject jsonObject = new JSONObject(requestsJson);
        JSONArray jsonArray = jsonObject.getJSONArray("requests");

        int counter = 0;
        int successCounter=0;
        while (counter < jsonArray.length())
        {
            String itemUuid = jsonArray.getJSONObject(counter).getString("ItemUuid");
            String priceCategoryUuid = jsonArray.getJSONObject(counter).getString("PriceCategoryUuid");
            int quantity = jsonArray.getJSONObject(counter).getInt("quantity");
            int locationId = this.getStockPointByLocationUuid(sourceLocationUuid).getId();
            int storeId=this.getStockPointByLocationUuid(sourceLocationUuid).getStore().getStoreId();
            PriceCategory priceCategory=pharmacyDAO.getPriceCategoryByUuid(priceCategoryUuid);
            Item item=pharmacyDAO.getItemByUuid(itemUuid);

            String saveResponse = pharmacyDAO.createItemRequestFromDispensingPoint(requestGroupId,item.getItemId(),priceCategory.getCategoryId(),quantity,locationId,storeId,destinationStoreId);
            if (saveResponse.equals("success")) {
                successCounter++;
            }
            counter++;
        }
        if(successCounter>0)
        {
            return "success";
        }
        return null;
    }

    @Override
    public String requesterCancellingRequestByUuid(String requestUuid,String locationUuid)
    {
        StockPoint stockPoint =this.getStockPointByLocationUuid(locationUuid);
        if(stockPoint!=null)
        {
            ItemRequestRaw itemRequestRaw=pharmacyDAO.getRequestByUuid(requestUuid);
            if(itemRequestRaw!=null)
            {
                if(itemRequestRaw.getStatusCode()==1 && itemRequestRaw.getRequestingStore()==stockPoint.getStore().getStoreId())
                return pharmacyDAO.requesterCancellingRequest(itemRequestRaw.getRequestId());
            }
        }
       return null;
    }

    @Override
    public String requesterRejectsConfirmingRequestByUuid(String requestUuid,String locationUuid)
    {
        StockPoint stockPoint =this.getStockPointByLocationUuid(locationUuid);
        if(stockPoint!=null)
        {
            ItemRequestRaw itemRequestRaw=pharmacyDAO.getRequestByUuid(requestUuid);
            if(itemRequestRaw!=null)
            {
                if(itemRequestRaw.getStatusCode()==3 && itemRequestRaw.getRequestingStore()==stockPoint.getStore().getStoreId()) {
                    return pharmacyDAO.requesterRejectsConfirmingRequest(itemRequestRaw.getRequestId());
                }
            }

        }
        return null;
    }

    @Override
    public String requesterConfirmToReceiveItemsByRequestUuid(String requestUuid,String locationUuid)
    {
        StockPoint stockPoint =this.getStockPointByLocationUuid(locationUuid);
        if(stockPoint!=null)
        {
            ItemRequestRaw itemRequestRaw=pharmacyDAO.getRequestByUuid(requestUuid);
            if(itemRequestRaw!=null)
            {
                if(itemRequestRaw.getStatusCode()==3 && itemRequestRaw.getRequestingStore()==stockPoint.getStore().getStoreId()) {
                    return pharmacyDAO.requesterConfirmToReceiveItems(itemRequestRaw.getRequestId());
                }
            }
        }
        return null;
    }


    @Override
    public String issuerRejectingRequestByUuid(String requestUuid,String locationUuid)
    {
        Location location=this.getLocationByUuid(locationUuid);
        if(location!=null)
        {
            LocationStoreMap locationStoreMap=pharmacyDAO.getStoreMapByLocationId(location.getId());
            Store store =pharmacyDAO.getStoreById(locationStoreMap.getStoreId());
            if(store!=null)
            {
                ItemRequestRaw itemRequestRaw=pharmacyDAO.getRequestByUuid(requestUuid);
                if(itemRequestRaw!=null)
                {
                    if(itemRequestRaw.getStatusCode()==1 && itemRequestRaw.getRequestedStore()==store.getStoreId()) {
                        return pharmacyDAO.issuerRejectingRequest(itemRequestRaw.getRequestId());
                    }
                }
            }
        }
        return null;
    }


    @Override
    public List<ItemBatch> possibleItemsBatchesToIssue(String requestUuid)
    {
        ItemRequestRaw itemRequestRaw=pharmacyDAO.getRequestByUuid(requestUuid);
        if(itemRequestRaw!=null)
        {
            int itemId=itemRequestRaw.getItemId();
            int storeId=itemRequestRaw.getRequestedStore();
            int priceCategoryId=itemRequestRaw.getPriceCategoryId();
            List<ItemBatchRaw> itemBatchRaws=pharmacyDAO.getAllStockByBatchesForItem(itemId,priceCategoryId,storeId);
            if(itemBatchRaws!=null)
            {
                if(itemBatchRaws.size()>0)
                {
                    List<ItemBatch> itemsBatchToReturn=new ArrayList<>();
                    Iterator iterator=itemBatchRaws.iterator();
                    while(iterator.hasNext())
                    {
                        ItemBatchRaw itemBatchRaw=(ItemBatchRaw) iterator.next();
                        String batchNo=itemBatchRaw.getBatchNo();
                        ItemBatchRaw itemBatchRow=pharmacyDAO.getItemStockByBatchNoExcludingOutstanding(itemRequestRaw.getItemId(),batchNo,priceCategoryId,storeId);
                        if(itemBatchRow!=null)
                        {
                            ItemBatch itemBatch=new ItemBatch();

                            itemBatch.setBatchNo(itemBatchRow.getBatchNo());
                            itemBatch.setItem(pharmacyDAO.getItemById(itemBatchRow.getItemId()));
                            itemBatch.setUuid(itemBatchRow.getUuid());
                            itemBatch.setStore(pharmacyDAO.getStoreById(itemBatchRaw.getStoreId()));
                            itemBatch.setQuantity(itemBatchRow.getQuantity());
                            itemBatch.setExpiryDate(itemBatchRow.getExpiryDate());
                            DateTime expiryDate=new DateTime(itemBatchRow.getExpiryDate());

                            int daysToExpiryDate = Days.daysBetween(new DateTime(),expiryDate).getDays();
                            log.info("ExpiryDate:"+expiryDate);
                            if(daysToExpiryDate>0)
                            {
                                itemBatch.setItemStatus("valid");
                            }
                            if(daysToExpiryDate<0)
                            {
                                itemBatch.setItemStatus("expired");
                            }

                            itemsBatchToReturn.add(itemBatch);
                        }
                    }
                    return itemsBatchToReturn;
                }
            }
        }
        return null;
    }

   @Override
    public String issueItemRequestedByRequestUuid(String requestUuid,String locationUuid,String itemBatchesJson)
    {
        ItemRequestRaw itemRequestRaw=pharmacyDAO.getRequestByUuid(requestUuid);
        Location location=this.getLocationByUuid(locationUuid);
        if(itemRequestRaw!=null)
        {
            if(location!=null)
            {
                LocationStoreMap locationStoreMap=pharmacyDAO.getStoreMapByLocationId(location.getId());
                int storeId=locationStoreMap.getStoreId();
                if(itemRequestRaw.getStatusCode()==1)
                {
                    ItemStockRaw itemStockRaw=pharmacyDAO.getItemQuantityOnHandInStoreByPaymentCategory(itemRequestRaw.getItemId(),itemRequestRaw.getPriceCategoryId(),storeId);
                    if(itemStockRaw!=null)
                    {
                        log.info("ItemBatchesJson::Before\n" + itemBatchesJson);
                        itemBatchesJson = itemBatchesJson.replace("\"", "");
                        log.info("ItemBatchesJson::After\n" + itemBatchesJson);
                        JSONObject jsonObject = new JSONObject(itemBatchesJson);
                        JSONArray jsonArray = jsonObject.getJSONArray("batches");
                        int failures=0;
                        String failedBatches=";batch";
                        int counter=0;
                        while(counter<jsonArray.length())
                        {
                            JSONObject batchObject=jsonArray.getJSONObject(counter);
                            String batchObjectBatchNo=batchObject.get("batchNo").toString();
                            int batchObjectQuantity=Integer.parseInt(batchObject.get("quantity").toString());
                            ItemBatchRaw itemBatchRaw=pharmacyDAO.getItemStockByBatchNoExcludingOutstanding(itemRequestRaw.getItemId(),batchObjectBatchNo,itemRequestRaw.getPriceCategoryId(),storeId);
                            if(batchObjectQuantity>itemBatchRaw.getQuantity())
                            {
                                failures++;
                                failedBatches+=batchObjectBatchNo+"*";
                            }
                            counter++;
                        }
                        if(failures==0)
                        {
                            counter=0;
                            int success=0;
                            int totalIssuedQuantity=0;
                            while(counter<jsonArray.length())
                            {
                                JSONObject batchObject=jsonArray.getJSONObject(counter);
                                int batchObjectQuantity=Integer.parseInt(batchObject.get("quantity").toString());
                                String batchObjectBatchNo=batchObject.get("batchNo").toString();
                                String saveResponse=pharmacyDAO.saveIssuedBatchToOutstandingStock(itemRequestRaw.getRequestId(),itemRequestRaw.getItemId(),storeId,batchObjectBatchNo,itemRequestRaw.getPriceCategoryId(),batchObjectQuantity);
                                if(saveResponse.equals("success"))
                                {
                                    totalIssuedQuantity+=batchObjectQuantity;
                                    success++;
                                }
                                counter++;
                            }
                            if(success>0)
                            {
                                String updateIssued=pharmacyDAO.updateQuantityIssued(itemRequestRaw.getRequestId(),totalIssuedQuantity);
                                String updateStatusCode=pharmacyDAO.updateRequestState(itemRequestRaw.getRequestId(),3);
                                if(updateIssued.equals("success"))
                                {
                                    if(updateStatusCode.equals("success"))
                                    {
                                        return "success";
                                    }
                                }
                            }
                        }else{
                            return "Retry;insufficient balance"+failedBatches;
                        }
                    }
                }else{
                    RequestStatusCode requestStatusCode=pharmacyDAO.getStatusCodeDefinitionById(itemRequestRaw.getStatusCode());
                    if(requestStatusCode!=null)
                    {
                        return requestStatusCode.getName();
                    }
                }
            }
        }
        return null;
    }


    @Override
    public ItemStockRaw getItemQuantityOnHandByPaymentCategory(String itemUuid, String priceCategoryUuid,String locationUuid)
    {
        Item item=pharmacyDAO.getItemByUuid(itemUuid);
        PriceCategory priceCategory=pharmacyDAO.getPriceCategoryByUuid(priceCategoryUuid);
        StockPoint stockPoint =this.getStockPointByLocationUuid(locationUuid);
        if(item!=null&&priceCategory!=null&& stockPoint !=null)
        {
            return pharmacyDAO.getItemQuantityOnHandInStoreByPaymentCategory(item.getItemId(),priceCategory.getCategoryId(), stockPoint.getStore().getStoreId());
        }
        return null;
    }


    @Override
    public List<ItemRequest> getAllItemRequestsByLocationUuid(String locationUuid)
    {
        StockPoint stockPoint =this.getStockPointByLocationUuid(locationUuid);
        if(stockPoint!=null)
        {
            List<ItemRequestRaw> itemRequestRawList=pharmacyDAO.getAllItemRequestsByStoreId(stockPoint.getStore().getStoreId());
            if(itemRequestRawList!=null)
            {
                if(itemRequestRawList.size()>0)
                {
                    List<ItemRequest> itemRequests=new ArrayList<>();
                    Iterator iterator=itemRequestRawList.iterator();
                    while(iterator.hasNext())
                    {
                        ItemRequestRaw itemRequestRaw=(ItemRequestRaw) iterator.next();
                        ItemRequest itemRequest=new ItemRequest();
                        itemRequest.setRequestId(itemRequestRaw.getRequestId());
                        itemRequest.setRequestGroupId(itemRequestRaw.getRequestGroupId());
                        itemRequest.setItem(pharmacyDAO.getItemById(itemRequestRaw.getItemId()));
                        itemRequest.setQuantityRequested(itemRequestRaw.getQuantityRequested());
                        itemRequest.setRequestDate(itemRequestRaw.getRequestDate());
                        itemRequest.setPriceCategory(pharmacyDAO.getPriceCategoryById(itemRequestRaw.getPriceCategoryId()));
                        itemRequest.setQuantityIssued(itemRequestRaw.getQuantityIssued());
                        itemRequest.setDateIssued(itemRequestRaw.getDateIssued());
                        itemRequest.setRequestingStore(pharmacyDAO.getSubStoreById(itemRequestRaw.getRequestingStore()));
                        itemRequest.setRequestedStore(pharmacyDAO.getStoreById(itemRequestRaw.getRequestedStore()));
                        itemRequest.setStatus(pharmacyDAO.getStatusCodeDefinitionById(itemRequestRaw.getStatusCode()));
                        itemRequest.setUuid(itemRequestRaw.getUuid());
                        itemRequests.add(itemRequest);
                    }
                    return itemRequests;
                }
            }
        }
        return null;
    }


    private Location getLocationByUuid(String locationUuid)
    {
        LocationService locationService=Context.getLocationService();
        return locationService.getLocationByUuid(locationUuid);
    }

    private Location getLocationById(int locationId)
    {
        LocationService locationService=Context.getLocationService();
        return locationService.getLocation(locationId);
    }

    @Override
    public List<ItemRequest> getAllItemRequestsInMainStore(String locationUuid)
    {
        Location location=this.getLocationByUuid(locationUuid);
        if(location!=null)
        {
            LocationStoreMap locationStoreMap=pharmacyDAO.getStoreMapByLocationId(location.getId());
            if(locationStoreMap!=null)
            {
                int storeId=locationStoreMap.getStoreId();
                List<ItemRequestRaw> itemRequestRawList=pharmacyDAO.getAllItemRequestsInMainStore(storeId);
                if(itemRequestRawList!=null)
                {
                    if(itemRequestRawList.size()>0)
                    {
                        List<ItemRequest> itemRequests=new ArrayList<>();
                        Iterator iterator=itemRequestRawList.iterator();
                        while(iterator.hasNext())
                        {
                            ItemRequestRaw itemRequestRaw=(ItemRequestRaw) iterator.next();
                            ItemRequest itemRequest=new ItemRequest();
                            itemRequest.setRequestGroupId(itemRequestRaw.getRequestGroupId());
                            itemRequest.setRequestId(itemRequestRaw.getRequestId());
                            itemRequest.setItem(pharmacyDAO.getItemById(itemRequestRaw.getItemId()));
                            itemRequest.setPriceCategory(pharmacyDAO.getPriceCategoryById(itemRequestRaw.getPriceCategoryId()));
                            itemRequest.setQuantityRequested(itemRequestRaw.getQuantityRequested());
                            itemRequest.setRequestDate(itemRequestRaw.getRequestDate());
                            itemRequest.setQuantityIssued(itemRequestRaw.getQuantityIssued());
                            itemRequest.setDateIssued(itemRequestRaw.getDateIssued());
                            itemRequest.setRequestingStore(pharmacyDAO.getSubStoreById(itemRequestRaw.getRequestingStore()));
                            itemRequest.setRequestedStore(pharmacyDAO.getStoreById(itemRequestRaw.getRequestedStore()));
                            itemRequest.setStatus(pharmacyDAO.getStatusCodeDefinitionById(itemRequestRaw.getStatusCode()));
                            itemRequest.setUuid(itemRequestRaw.getUuid());
                            itemRequests.add(itemRequest);
                        }
                        return itemRequests;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public ItemBatch getItemStockByBatchNo(int itemId,String batchNo,int priceCategoryId, int storeId)
    {
        ItemBatchRaw itemBatchRaw=pharmacyDAO.getItemStockByBatchNo(itemId,batchNo,priceCategoryId,storeId);
        if(itemBatchRaw!=null)
        {
            Item item=pharmacyDAO.getItemById(itemBatchRaw.getItemId());
            PriceCategory priceCategory=pharmacyDAO.getPriceCategoryById(itemBatchRaw.getPriceCategoryId());
            Store store=pharmacyDAO.getStoreById(itemBatchRaw.getStoreId());
            ItemBatch itemBatch=new ItemBatch();
            itemBatch.setBatchNo(itemBatchRaw.getBatchNo());
            itemBatch.setItem(item);
            itemBatch.setPriceCategory(priceCategory);
            itemBatch.setStore(store);
            itemBatch.setUuid(itemBatchRaw.getUuid());
            itemBatch.setExpiryDate(itemBatchRaw.getExpiryDate());
            return itemBatch;
        }
        return null;
    }

    @Override
    public List<ItemBatch> getAllStockByBatchesForItem(int itemId,int priceCategoryId,int storeId)
    {

        return null;
    }

    @Override
    public List<ItemStock> getDrugStockOnHandForAnItem(int itemId, int priceCategoryId, int storeId)
    {

        return null;
    }


    @Override
    public List<ItemStock> fetchItemStockOnHandAllItemsOnHandByPriceCategory(String locationUuid)
    {
        Location location=this.getLocationByUuid(locationUuid);
        if(location!=null)
        {
            List<ItemStock> itemStocks=new ArrayList<>();
            LocationStoreMap locationStoreMap=pharmacyDAO.getStoreMapByLocationId(location.getId());
            if(locationStoreMap!=null){
                List<ItemStockRaw> itemStockRaws=pharmacyDAO.getStockOnHandInStore(locationStoreMap.getStoreId());
                if(itemStockRaws!=null)
                {
                    if(itemStockRaws.size()>0)
                    {
                        Iterator iterator=itemStockRaws.iterator();
                        while(iterator.hasNext())
                        {
                            ItemStockRaw itemStockRaw=(ItemStockRaw) iterator.next();
                            ItemStock itemStock=new ItemStock();
                            itemStock.setItem(pharmacyDAO.getItemById(itemStockRaw.getItemId()));
                            itemStock.setPriceCategoryId(pharmacyDAO.getPriceCategoryById(itemStockRaw.getPriceCategoryId()));
                            itemStock.setQuantity(itemStockRaw.getQuantity());
                            itemStock.setStore(pharmacyDAO.getStoreById(itemStockRaw.getStoreId()));
                            itemStock.setUuid(itemStockRaw.getUuid());
                            itemStocks.add(itemStock);
                        }
                    }
                }
                return itemStocks;
            }
        }
        return null;
    }

    @Override
    public List<ItemStock> fetchItemStockOnHandAllItemsOnHandByPriceCategoryMainStore(String locationUuid)
    {
        Location location=this.getLocationByUuid(locationUuid);
        if(location!=null)
        {
            List<ItemStock> itemStocks=new ArrayList<>();
            LocationStoreMap locationStoreMap=pharmacyDAO.getStoreMapByLocationId(location.getId());
            if(locationStoreMap!=null)
            {
                List<ItemStockRaw> itemStockRaws=pharmacyDAO.getStockOnHandInStore(1);
                Iterator iterator=itemStockRaws.iterator();
                while(iterator.hasNext())
                {
                    ItemStockRaw itemStockRaw=(ItemStockRaw)iterator.next();
                    int itemId=itemStockRaw.getItemId();
                    int storeId=itemStockRaw.getStoreId();
                    int priceCatId=itemStockRaw.getPriceCategoryId();
                    ItemStock itemStock=new ItemStock();
                    itemStock.setItem(pharmacyDAO.getItemById(itemId));
                    itemStock.setStore(pharmacyDAO.getStoreById(storeId));
                    itemStock.setPriceCategoryId(pharmacyDAO.getPriceCategoryById(priceCatId));
                    itemStock.setQuantity(itemStockRaw.getQuantity());
                    itemStock.setUuid(itemStockRaw.getUuid());
                    itemStocks.add(itemStock);
                }
                return itemStocks;
            }
        }
        return null;
    }

    @Override
    public List<ItemBatch> fetchItemStockOnHandAllItemsByBatches(String locationUuid)
    {
        Location location=this.getLocationByUuid(locationUuid);
        if(location!=null)
        {
            List<ItemBatch> itemBatches=new ArrayList<>();
            LocationStoreMap locationStoreMap=pharmacyDAO.getStoreMapByLocationId(location.getId());
            if(locationStoreMap!=null){
                List<ItemBatchRaw> itemBatchRaws=pharmacyDAO.getStockOnHandByBatchesInStore(locationStoreMap.getStoreId());
                if(itemBatchRaws!=null)
                {
                    if(itemBatchRaws.size()>0)
                    {
                        Iterator iterator=itemBatchRaws.iterator();
                        while(iterator.hasNext())
                        {
                            ItemBatchRaw itemBatchRaw=(ItemBatchRaw)iterator.next();
                            ItemBatch itemBatch=new ItemBatch();
                            itemBatch.setBatchNo(itemBatchRaw.getBatchNo());
                            itemBatch.setItem(pharmacyDAO.getItemById(itemBatchRaw.getItemId()));
                            itemBatch.setUuid(itemBatchRaw.getUuid());
                            itemBatch.setStore(pharmacyDAO.getStoreById(itemBatchRaw.getStoreId()));
                            itemBatch.setQuantity(itemBatchRaw.getQuantity());
                            itemBatch.setExpiryDate(itemBatchRaw.getExpiryDate());
                            DateTime expiryDate=new DateTime(itemBatchRaw.getExpiryDate());
                            int daysToExpiryDate = Days.daysBetween(new DateTime(),expiryDate).getDays();
                            if(daysToExpiryDate>0)
                            {
                                itemBatch.setItemStatus("valid");
                            }
                            if(daysToExpiryDate<0)
                            {
                                itemBatch.setItemStatus("expired");
                            }
                            itemBatches.add(itemBatch);
                        }
                    }
                }
                return itemBatches;
            }
        }
        return null;
    }

/*
 Dispensing' & related data
 @Author: Eric Mwailunga
 May,2019
*/
    @Override
    public String getStockStatusForDrugItemPrescription(String visitUuid,int paymentCategory,String drugUuid)
    {
        VisitService visitService=Context.getVisitService();
        Visit visit=visitService.getVisitByUuid(visitUuid);
        if(visit!=null)
        {
            int visitTypeId=visit.getVisitType().getVisitTypeId();
            DispenseMapRaw dispenseMapRaw=pharmacyDAO.getDispensingRawMapByVisitTypeAndPaymentCategory(visitTypeId,paymentCategory);

            //int visitId=visit.getVisitId();
            ConceptService conceptService=Context.getConceptService();
            String payment=conceptService.getConcept(paymentCategory).getName().getName();
            org.openmrs.Drug drug=conceptService.getDrugByUuid(drugUuid);
            if(drug!=null)
            {
                ItemDrugMap itemDrugMap=pharmacyDAO.getItemDrugMapByDrugId(drug.getId());
                if(itemDrugMap!=null)
                {
                    int itemId=itemDrugMap.getItemId();
                    int priceCatId=0;
                    int locationId=dispenseMapRaw.getDispensingLocationId();
                    StockPoint stockPoint=this.getStockPointByLocationUuid(this.getLocationById(locationId).getUuid());
                    List<PriceCategory> priceCategories=pharmacyDAO.getAllPriceCategories(false);
                    Iterator iterator=priceCategories.iterator();
                    while(iterator.hasNext())
                    {
                        PriceCategory priceCategory=(PriceCategory) iterator.next();
                        String paymentName=priceCategory.getName();
                        log.info("Payment category:"+paymentName);
                        if(paymentName.equals(payment))
                        {
                            priceCatId=priceCategory.getCategoryId();
                        }
                    }
                    if(priceCatId!=0)
                    {
                        ItemStockRaw itemStockRaw=pharmacyDAO.getItemQuantityOnHandInStoreByPaymentCategory(itemId,priceCatId,stockPoint.getStore().getStoreId());
                        if(itemStockRaw!=null)
                        {
                            JSONObject responseJsonObject=new JSONObject();
                            responseJsonObject.put("itemId",itemId);
                            responseJsonObject.put("quantity","not-feasible");
                            JSONArray jsonAlternativeStoresArray=new JSONArray();
                            JSONObject storeJsonObject=new JSONObject();

                            int quantity=itemStockRaw.getQuantity();
                            log.info("StockStatus:"+quantity);
                            if(quantity>0)
                            {
                                responseJsonObject.remove("quantity");
                                responseJsonObject.put("quantity","feasible");
                            }
                            else
                            {
                                List<DispenseMap> dispenseMaps=this.getAllDispensingMap(false);
                                if(dispenseMaps!=null)
                                {
                                    if(dispenseMaps.size()>0)
                                    {
                                        Iterator iterator2=dispenseMaps.iterator();
                                        while(iterator2.hasNext())
                                        {
                                            DispenseMap dispenseMap=(DispenseMap)iterator2.next();
                                            StockPoint stockPointX=this.getStockPointByLocationUuid(this.getLocationById(dispenseMap.getDispensingPoint().getDispensingPointId()).getUuid());
                                            if(stockPointX!=null)
                                            {
                                                itemStockRaw=pharmacyDAO.getItemQuantityOnHandInStoreByPaymentCategory(itemId,priceCatId,stockPointX.getStore().getStoreId());
                                                if(itemStockRaw!=null)
                                                {
                                                    quantity=itemStockRaw.getQuantity();
                                                    storeJsonObject.put("store",dispenseMap.getDispensingPoint().getName());
                                                    if(quantity>0)
                                                    {
                                                        storeJsonObject.put("quantity","feasible");
                                                    }else{
                                                        storeJsonObject.put("quantity","not-feasible");
                                                    }
                                                    jsonAlternativeStoresArray.put(storeJsonObject);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            responseJsonObject.put("alternativeStores",jsonAlternativeStoresArray);
                            return responseJsonObject.toString();
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public List<DispenseMap> getAllDispensingMap(boolean includeRetired)
    {
        List<DispenseMapRaw> dispenseMapRaws=pharmacyDAO.getAllDispensingMap(false);
        List<DispenseMap> dispenseMaps=new ArrayList<>();
        if(dispenseMapRaws!=null)
        {
            if(dispenseMapRaws.size()>0)
            {
                Iterator iterator=dispenseMapRaws.iterator();
                while(iterator.hasNext())
                {
                    DispenseMap dispenseMap=new DispenseMap();
                    DispenseMapRaw dispenseMapRaw=(DispenseMapRaw) iterator.next();
                    dispenseMap.setMapId(dispenseMapRaw.getMapId());
                    dispenseMap.setDateCreated(dispenseMapRaw.getDateCreated());
                    dispenseMap.setRetired(dispenseMapRaw.getRetired());
                    dispenseMap.setUuid(dispenseMapRaw.getUuid());
                    Location location=this.getLocationById(dispenseMapRaw.getDispensingLocationId());
                    if(location!=null)
                    {
                        String name=location.getName();
                        int locationId=location.getLocationId();
                        DispensingPoint dispensingPoint=new DispensingPoint();
                        dispensingPoint.setDispensingPointId(locationId);
                        String[] names=name.split("-");
                        dispensingPoint.setName(names[1].trim());
                        dispenseMap.setDispensingPoint(dispensingPoint);
                    }
                    org.openmrs.VisitType openmrsVisitType=Context.getVisitService().getVisitType(dispenseMapRaw.getVisitTypeId());
                    if(openmrsVisitType!=null)
                    {
                        VisitType visitType=new VisitType();
                        visitType.setVisitTypeId(openmrsVisitType.getVisitTypeId());
                        visitType.setName(openmrsVisitType.getName());
                        dispenseMap.setVisitType(visitType);
                    }
                    PriceCategory priceCategory=pharmacyDAO.getPriceCategoryById(dispenseMapRaw.getPaymentCategoryId());
                    if(priceCategory!=null)
                    {
                        dispenseMap.setPriceCategory(priceCategory);
                    }
                    dispenseMaps.add(dispenseMap);
                }
            }
            return dispenseMaps;
        }
        return null;
    }

    @Override
    public String dispenseDrugItemFromDispensingPointsStock(String locationUuid,String visitUuid,int paymentCategory,String drugJsonString)
    {
        JSONObject jsonObject=new JSONObject(drugJsonString);
        JSONArray jsonArray=jsonObject.getJSONArray("visitDrugOrders");

        Visit visit=Context.getVisitService().getVisitByUuid(visitUuid);
        int visitId=visit.getVisitId();
        int visitTypeId=visit.getVisitType().getVisitTypeId();
        Location location=this.getLocationByUuid(locationUuid);
        if(location!=null)
        {
            LocationStoreMap locationStoreMap = pharmacyDAO.getStoreMapByLocationId(location.getId());
            if (locationStoreMap != null)
            {

            }
        }
        return null;
    }


    @Override
    public String saveDrugItemOrderForDispensing(String visitUuid,int paymentCategoryId,String drugOrderJsonString)
    {
        Visit visit=Context.getVisitService().getVisitByUuid(visitUuid);
        int visitId=visit.getVisitId();
        int visitTypeId=visit.getVisitType().getVisitTypeId();
        if(paymentCategoryId!=0)
        {
            DispenseMapRaw dispenseMapRaw=pharmacyDAO.getDispensingRawMapByVisitTypeAndPaymentCategory(visitTypeId,paymentCategoryId);
            int targetDispensingPoint=dispenseMapRaw.getDispensingLocationId();
            int patientId=visit.getPatient().getPatientId();

            JSONObject jsonObject=new JSONObject(drugOrderJsonString);
            JSONArray jsonArray=jsonObject.getJSONArray("visitDrugOrders");
            int length=jsonArray.length();
            int count=0;
            if(length>0)
            {
                int success=0;
                while(count<length)
                {
                    JSONObject orderJson=jsonArray.getJSONObject(count);
                    String orderUuid=orderJson.getString("uuid");
                    if(orderUuid!=null)
                    {
                        OrderService orderService =Context.getOrderService();
                        Order order=orderService.getOrderByUuid(orderUuid);
                        int orderId=order.getOrderId();
                        String drugUuid=orderJson.getJSONObject("drug").getString("uuid");
                        org.openmrs.Drug drug=Context.getConceptService().getDrugByUuid(drugUuid);
                        ItemDrugMap itemDrugMap=pharmacyDAO.getItemDrugMapByDrugId(drug.getId());
                        if(itemDrugMap!=null)
                        {
                            int itemId=itemDrugMap.getItemId();
                            Item item=pharmacyDAO.getItemById(itemId);
                            if(item!=null)
                            {
                                int itemCategoryId=pharmacyDAO.getCategoryByUuid(item.getCategoryUuid()).getCategoryId();
                                if(pharmacyDAO.savePrescriptionOrder(patientId,orderId,visitId,itemId,itemCategoryId,paymentCategoryId,targetDispensingPoint).equals("success"))
                                {
                                    success++;
                                }
                            }
                        }
                    }
                    count++;
                }
                if(success>0)
                {
                    return "success";
                }
            }
        }
        return null;
    }


/*
  Dispensing' & related data
  @Author: Eric Mwailunga
  May,2019
*/
    public String billOrderedDrugItems(String visitUuid,String paymentCategoryUuid,String drugOrderJsonString)
    {
        Visit visit=Context.getVisitService().getVisitByUuid(visitUuid);
        if(visit!=null)
        {
            int visitId=visit.getVisitId();
            int visitTypeId=visit.getVisitType().getVisitTypeId();
            JSONObject jsonObject=new JSONObject(drugOrderJsonString);
            JSONArray jsonArray=jsonObject.getJSONArray("visitDrugOrders");
            
        }
        return null;
    }

    public String billOrderedNonDrugItems(String visitUuid,String paymentCategoryUuid,String itemsOrderJsonString)
    {
    
        return null;
    }
}