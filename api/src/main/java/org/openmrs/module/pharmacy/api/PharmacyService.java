/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.pharmacy.api;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.pharmacy.model.item.ItemConcept;
import org.openmrs.module.pharmacy.model.item.Item;
import org.openmrs.module.pharmacy.model.item.drug.ItemDrug;
import org.openmrs.module.pharmacy.model.item.ItemNonDrug;
import org.openmrs.module.pharmacy.model.item.itemcategory.Category;
import org.openmrs.module.pharmacy.model.item.itemcategory.SubCategory;
import org.openmrs.module.pharmacy.model.item.itemcategory.SubCategoryAttribute;
import org.openmrs.module.pharmacy.model.ledger.LedgerLine;
import org.openmrs.module.pharmacy.model.ledger.LedgerType;
import org.openmrs.module.pharmacy.model.misc.StockPoint;
import org.openmrs.module.pharmacy.model.price.ItemPrice;
import org.openmrs.module.pharmacy.model.price.PriceCategory;
import org.openmrs.module.pharmacy.model.stock.ItemBatch;
import org.openmrs.module.pharmacy.model.stock.ItemStock;
import org.openmrs.module.pharmacy.model.stock.dispense.DispenseMap;
import org.openmrs.module.pharmacy.model.stock.raw.ItemBatchRaw;
import org.openmrs.module.pharmacy.model.stock.raw.ItemStockRaw;
import org.openmrs.module.pharmacy.model.stock.request.ItemRequest;
import org.openmrs.module.pharmacy.model.store.Store;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * This service exposes module's core functionality.
 * It is a Spring managed bean which is configured in moduleApplicationContext.xml.
 * <p>
 * It can be accessed only via Context:<br>
 * <code>
 * Context.getService(${module-name-no-spaces}Service.class).someMethod();
 * </code>
 * 
 * @see org.openmrs.api.context.Context
 */
@Transactional
public interface PharmacyService extends OpenmrsService {

/*
 Stores' & related data
 @Author: Eric Mwailunga
 May,2019
*/
    String createStore(String storeName);
    String createSubStore(String storeName,String parentStoreUuid);

    Store getStoreByUuid(String storeUuid);
    Store getStoreByName(String storeName);
    List getStoreBySubName(String nameSubString);
    List getStoreSubStore(String storeUuid,boolean includeRetired);

    List getAllStores(boolean includeRetired);
    List getAllStoresFull(boolean includeRetired);

    Store getSubStoreByUuid(String storeUuid);
    Store getSubStoreByName(String subStoreName);
    List getSubStoreBySubName(String subStoreSearchName);
    List getAllSubStore(boolean includeRetired);

    String updateStoreByUuid(String storeUuid,String storeName,boolean retired);
    String updateSubStoreByUuid(String storeUuid,String storeName,String parentUuid,boolean retired);

    String deleteStoreByUuid(String storeUuid);
    String deleteSubStoreByUuid(String subStoreUuid);


/*
 Items categories registrations & related data
 @Author: Eric Mwailunga
 May,2019
*/
    //String createCategory(String itemCategory); /* Reserved for future development.. */
    Category getCategoryByUuid(String categoryUuid);
    Category getCategoryByUuidFull(String categoryUuid);
    List getAllCategories(boolean includeRetired);
    List getAllCategoriesFull(boolean includeRetired);

/*
 Items subcategories registrations & related data
 @Author: Eric Mwailunga
 May,2019
*/
    String createSubCategory(String subCategoryName,String parentCategoryUuid);
    SubCategory getSubCategoryByUuid(String subCategoryUuid);
    SubCategory getFullSubCategoryByUuid(String subCategoryUuid);
    SubCategory getSubCategoryByName(String subCategoryName);
    List getSubCategoryBySubName(String subCategorySubName);
    List getAllSubCategories(boolean includeRetired);
    List<SubCategory> getAllSubCategoriesFull(boolean includeRetired);
    String updateSubCategoryByUuid(String subCategoryUuid,String subCategoryName,String parentUuid,boolean retire);
    String deleteSubCategoryByUuid(String subCategoryUuid);


/*
 Items categories :: attribute types registrations & related data
 @Author: Eric Mwailunga
 May,2019
*/
    String createSubCategoryAttributeType(String subCategoryUuid,String attributeTypeName);
    String createSubCategoryAttributeTypes(String subCategoryUuid,String attributeTypeNames);
    List getAttributeTypesBySubCategoryUuid(String subCategoryUuid);
    String updateSubCategoryAttributeType(String attributeTypeUuid,String name,String subCategoryUuid,boolean retire);
    List<SubCategoryAttribute> getCategoryByUuidWithFullSubCategoryAttributeTypes(String categoryUuid,boolean includeRetired);

/*
 Items :: attributes values
 @Author: Eric Mwailunga
 May,2019
*/
    String saveNonDrugItem(String categoryUuid,String subCategoryUuid,String attributesJsonString);
    ItemNonDrug getNonDrugByItemUuid(String itemUuid);
    String updateNonDrugItem(String uuid,boolean retired,String attributesJsonString);
    ItemDrug getDrugByItemUuid(String itemUuid);
    String saveDrug(String categoryUuid,int genericNameConceptId,String genericName,String strength,int dosageFormConceptId);
    List<ItemDrug> getDrugsBySubName(String drugSubName);
    //String saveItem(String name,String categoryUuid);

/*
 Drug related Items :: registration & related data operations
 @Author: Eric Mwailunga
 May,2019
*/

    String createDosageForm(String dosageFormName);
    List getAllDosageForms();
    ItemConcept getDosageFormByUuid(String uuid);
    //String updateDosageForm(String Uuid,String dosageFormName,boolean retired);

    String createGenericDrugName(String genericName);
    ItemConcept getGenericDrugNameByUuid(String uuid);
    String updateGenericDrugNameByUuid(String uuid,String name,boolean retired);
    //String getGenericDrugNameByName(String genericName);
    List getGenericDrugNameBySubName(String subName);
    List getAllGenericDrugNames(boolean includeRetired);
    List<ItemDrug> getAllDrugItems(boolean includeRetired);


/*
 Non drug Items :: registration & related data
 @Author: Eric Mwailunga
 May,2019
*/
    //String createNonDrugItem(String subCategoryUuid,String attributeJson);
    //String getNonDrugItem

    Item getNonDrugByNameWithoutTheirAttributes(String itemName);
    ItemNonDrug getNonDrugByNameWithTheirAttributes(String itemName);
    List<Item> getAllNonDrugsWithoutTheirAttributes(boolean includeRetired);
    List<ItemNonDrug> getAllNonDrugsWithTheirAttributes(boolean includeRetired);
    Item getItemByUuid(String itemUuid);
    Item getItemById(int itemId);
    List<Item> getAllItems(boolean includeRetired);
    List<Item> getAllItemsBySubName(String itemName);

/*
 Item price & related data
 @Author: Eric Mwailunga
 May,2019
*/
    String createItemSellingPrice(String itemUuid,int priceCategoryId,double sellingPrice);
    ItemPrice getItemSellingPrice(String itemUuid, int priceCategoryId);
    ItemPrice getItemSellingPriceByItemPriceUuid(String itemPriceUuid);
    String updateItemSellingPrice(String itemUuid,int priceCategoryId,double sellingPrice);
    List<ItemPrice> getAllItemSellingPrice();

/*
 Price category & related data
 @Author: Eric Mwailunga
 May,2019
*/

    String createPriceCategory(String categoryName);
    PriceCategory getPriceCategoryByUuid(String uuid);
    String updatePriceCategory(String categoryUuid,String categoryName,boolean retired);
    PriceCategory getPriceCategoryByName(String categoryName);
    List<PriceCategory> getAllPriceCategories(boolean includeRetired);


/*
 Ledger entry types & related data
 @Author: Eric Mwailunga
 May,2019
*/
    String createLedgerType(String ledgerTypeName, String operation);
    LedgerType getLedgerTypeByUuid(String ledgerTypeUuid);
    String updateLedgerTypeByUuid(String ledgerTypeUuid,String ledgerTypeName,String operation,boolean retired);
    List<LedgerType> getAllLedgerTypes(boolean includeRetired);


/*
 Ledger entries & related data
 @Author: Eric Mwailunga
 May,2019
*/
    String createLedgerEntry(int ledgerTypeId,int itemId,int priceCategoryId,String batchNo,String invoiceNo,double buyingPrice,int quantity, String dateMoved, String expiryDate,int dosageUnits,String remarks);
    LedgerLine getLedgerEntryByUuid(String entryUuid);
    List<LedgerLine> getAllLedgerEntries();

/*
 Stock movement & related data :: Dispensing points with their respective stores.
 @Author: Eric Mwailunga
 May,2019
*/

    StockPoint getStockPointByLocationUuid(String locationUuid);


/*
 Stock movement & related data :: Item requests and issuing.
 @Author: Eric Mwailunga
 May,2019
*/
    String createItemRequestFromDispensingPoint(String requestGroupId,String itemUuid,String priceCategoryUuid,int quantity,String sourceLocationUuid,int destinationStoreId);

    String createItemsRequestFromDispensingPoint(String requestGroupId,String sourceLocationUuid,int destinationStoreId,String requestsJson);
    String requesterCancellingRequestByUuid(String requestUuid,String locationUuid);
    String requesterRejectsConfirmingRequestByUuid(String requestUuid,String locationUuid);
    String requesterConfirmToReceiveItemsByRequestUuid(String requestUuid,String locationUuid);
    List<ItemRequest> getAllItemRequestsByLocationUuid(String locationUuid);


    List<ItemBatch> possibleItemsBatchesToIssue(String requestUuid);
    //String fetchAllItemReceivedRequestsWithStatus();

    String issueItemRequestedByRequestUuid(String requestUuid,String locationUuid,String itemsJson);
    String issuerRejectingRequestByUuid(String requestUuid,String locationUuid);

    //ItemStockRaw getItemQuantityOnHand(String itemUuid);
    //Map hii raw into actual
    ItemStockRaw getItemQuantityOnHandByPaymentCategory(String itemUuid, String priceCategoryUuid,String locationUuid);

    List<ItemRequest> getAllItemRequestsInMainStore(String locationUuid);
    ItemBatch getItemStockByBatchNo(int itemId,String batchNo,int priceCategoryId, int storeId);
    List<ItemBatch> getAllStockByBatchesForItem(int itemId,int priceCategoryId,int storeId);
    List<ItemStock> getDrugStockOnHandForAnItem(int itemId,int priceCategoryId,int storeId);
    //int getTotalQuantityWaitingConfirmation(int itemId,int priceCategoryId,int storeId);
    List<ItemStock> fetchItemStockOnHandAllItemsOnHandByPriceCategory(String locationUuid);
    List<ItemStock> fetchItemStockOnHandAllItemsOnHandByPriceCategoryMainStore(String locationUuid);
    List<ItemBatch> fetchItemStockOnHandAllItemsByBatches(String locationUuid);

/*
 Dispensing' & related data
 @Author: Eric Mwailunga
 May,2019
*/

    String getStockStatusForDrugItemPrescription(String visitUuid,int paymentCategory,String drugUuid);
    String dispenseDrugItemFromDispensingPointsStock(String locationUuid,String visitUuid,int paymentCategory,String drugJsonString);
    String saveDrugItemOrderForDispensing(String visitUuid,int paymentCategory,String drugOrderJsonString);
    List<DispenseMap> getAllDispensingMap(boolean includeRetired);


/*
  Items' billing & related data
  @Author: Eric Mwailunga
  May,2019
*/
    String billOrderedDrugItems(String visitUuid,String paymentCategoryUuid,String drugOrderJsonString);
    String billOrderedNonDrugItems(String visitUuid,String paymentCategoryUuid,String itemsOrderJsonString);
}