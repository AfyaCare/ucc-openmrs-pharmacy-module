/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.pharmacy.api.db;

import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.model.item.Item;
import org.openmrs.module.pharmacy.model.item.drug.ItemDrug;
import org.openmrs.module.pharmacy.model.item.ItemDrugMap;
import org.openmrs.module.pharmacy.model.item.ItemNonDrug;
import org.openmrs.module.pharmacy.model.item.itemattribute.Attribute;
import org.openmrs.module.pharmacy.model.item.itemcategory.Category;
import org.openmrs.module.pharmacy.model.item.itemcategory.SubCategory;
import org.openmrs.module.pharmacy.model.item.itemcategory.SubCategoryAttribute;
import org.openmrs.module.pharmacy.model.ledger.LedgerRawLine;
import org.openmrs.module.pharmacy.model.ledger.LedgerType;
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

import java.util.List;

/**
 *  Database methods for {@link PharmacyService}.
 */
public interface PharmacyDAO {

/*
 Stores' & related data
 @Author: Eric Mwailunga
 May,2019
*/
    String createStore(String storeName);
    String createSubStore(String storeName,String parentStoreUuid);

    Store getStoreById(int storeId);
    Store getStoreByUuid(String storeUuid);
    Store getStoreByName(String storeName);
    List getStoreBySubName(String storeName);
    List getStoreSubStore(String storeUuid,boolean includeRetired);

    List getAllStores(boolean includeRetired);
    List getAllStoresFull(boolean includeRetired);

    Store getSubStoreById(int subStoreId);
    Store getSubStoreByUuid(String subStoreUuid);
    Store getSubStoreByName(String subStoreName);
    List getSubStoreBySubName(String subStoreSearchName);
    List getAllSubStore(boolean includeRetired);

    String updateStoreByUuid(String storeUuid,String storeName,boolean retired);
    String deleteStoreByUuid(String storeUuid);
    String updateSubStoreByUuid(String storeUuid,String storeName,String parentUuid,boolean retired);
    String deleteSubStoreByUuid(String storeUuid);

/*
 Items categories registrations & related data
 @Author: Eric Mwailunga
 May,2019
*/

    //String createCategory(String itemCategory); /* Reserved for future development.. */
    Category getCategoryByUuid(String categoryUuid);
    Category getCategoryByName(String categoryName);
    Category getCategoryByUuidFull(String categoryUuid);
    List getAllCategories(boolean includeRetired);
    List getAllCategoriesFull(boolean includeRetired);

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
 Items categories attribute types registrations & related data
 @Author: Eric Mwailunga
 May,2019
*/
    String createSubCategoryAttributeType(String subCategoryUuid,String attributeTypeName);
    String createSubCategoryAttributeTypes(String subCategoryUuid,String attributeTypeNames);
    String getSubCategoryAttributeTypeByUuid(String attributeTypeUuid);
    List getAttributeTypesBySubCategoryUuid(String subCategoryUuid);
    String updateSubCategoryAttributeType(String attributeTypeUuid,String name,String subCategoryUuid,boolean retire);

    List<SubCategoryAttribute> getSubCategoryByCategoryUuidWithFullAttributeTypes(String categoryUuid,boolean includeRetired);
    //List<SubCategoryAttribute> getAllSubCategoriesWithAttributeTypes(boolean includeRetired);

/*
 Items registrations & related data
 @Author: Eric Mwailunga
 May,2019
*/
    String saveDrugItem(String categoryUuid,String name,int drugId);
    Item saveNonDrugItem(String categoryUuid,String name);
    String updateNonDrugItem(int itemId,String name,boolean retired);

    Item getItemByUuid(String itemUuid);
    Item getItemById(int itemId);
    ItemDrugMap getItemDrugMapByItemId(int itemId);
    ItemDrugMap getItemDrugMapByDrugId(int itemId);
    String saveItemAttributeValue(int itemId,String attributeTypeUuid,String value);
    String updateItemAttributeValue(int itemId,String attributeTypeUuid,String value);

    ItemNonDrug getNonDrugByItemUuidWithTheirAttributes(String itemUuid);
    ItemDrug getDrugByItemUuidWithTheirAttributes(String itemUuid);
    Item getNonDrugByNameWithoutTheirAttributes(String itemName);
    ItemNonDrug getNonDrugByNameWithTheirAttributes(String itemName);
    List<Item> getAllNonDrugsByWithoutTheirAttributes(boolean includeRetired);
    List<ItemNonDrug> getAllNonDrugsWithTheirAttributes(boolean includeRetired);
    List<Attribute> getAttributesForItemByItemId(int itemId);
    List<ItemDrug> getAllDrugItems(boolean includeRetired);
    List<Item> getAllItems(boolean includeRetired);
    List<Item> getAllItemsBySubName(String itemSubName);

    List<ItemRequestRaw> getAllItemRequestsInMainStore(int storeId);
    List<ItemRequestRaw> getAllItemRequestsByStoreId(int storeId);

/*
 Item price & related data
 @Author: Eric Mwailunga
 May,2019
*/
    String createItemSellingPrice(String itemUuid,int priceCategoryId,double sellingPrice);
    ItemPriceRaw getItemSellingPriceByItemIdAndPriceCategoryId(int itemId, int priceCategoryId);
    ItemPriceRaw getItemSellingPriceByItemPriceId(int itemPriceId);
    ItemPriceRaw getItemSellingPriceByItemPriceUuid(String itemPriceUuid);
    String updateItemSellingPrice(int itemId,int priceCategoryId,double sellingPrice);
    List<ItemPriceRaw> getAllItemSellingPrice();
/*
 Price category & related data
 @Author: Eric Mwailunga
 May,2019
*/

    String createPriceCategory(String categoryName);
    PriceCategory getPriceCategoryByUuid(String uuid);
    PriceCategory getPriceCategoryById(int priceCategoryId);
    String updatePriceCategory(String categoryUuid,String categoryName,boolean retired);
    PriceCategory getPriceCategoryByName(String categoryName);
    List<PriceCategory> getAllPriceCategories(boolean includeRetired);


/*
LedgerRawLine entry types & related data
@Author: Eric Mwailunga
May,2019
*/

    String createLedgerType(String ledgerTypeName, String operation);
    LedgerType getLedgerTypeById(int ledgerTypeId);
    LedgerType getLedgerTypeByUuid(String ledgerTypeUuid);
    String updateLedgerTypeByUuid(String ledgerTypeUuid,String ledgerTypeName,String operation,boolean retired);
    List<LedgerType> getAllLedgerTypes(boolean includeRetired);


/*
 LedgerRawLine entries & related data
 @Author: Eric Mwailunga
 May,2019
*/

    String createLedgerEntry(int ledgerTypeId,int itemId,int priceCategoryId,String batchNo,String invoiceNo,double buyingPrice,int quantity, String dateMoved, String expiryDate,int dosageUnits,String remarks);
    LedgerRawLine getLedgerEntryByUuid(String entryUuid);
    List<LedgerRawLine> getAllLedgerEntries();

/*
 Stock movement & related data
 @Author: Eric Mwailunga
 May,2019
*/
    LocationStoreMap getStoreMapByLocationId(int locationId);
    List<LocationStoreMap> getStoreMapByLocationStoreId(int storeId);


/*
 Stock movement & related data :: Item requests and issuing.
 @Author: Eric Mwailunga
 May,2019
*/
    ItemRequestRaw getRequestByUuid(String requestUuid);
    ItemRequestRaw getRequestById(int requestId);
    RequestStatusCode getStatusCodeDefinitionById(int statusId);
    String createItemRequestFromDispensingPoint(String requestGroupId,int itemId,int priceCategoryId,int quantity,int locationId,int sourceStoreId,int destinationStoreId);
    String saveIssuedBatchToOutstandingStock(int requestId,int itemId,int storeId,String batchNo,int priceCategoryId,int quantity);
    String removeIssuedBatchFromOutstandingStock(int requestId);
    String requesterCancellingRequest(int requestId);
    String requesterRejectsConfirmingRequest(int requestId);
    String requesterConfirmToReceiveItems(int requestId);

    String issuerRejectingRequest(int requestId);
    String updateRequestState(int requestId,int requestCodeId);
    String updateQuantityIssued(int requestId,int quantityIssued);
    String processStockTransfer(int fromStoreId,int toStoreId,int itemId,String batchNo,int paymentCategoryId,int quantity);

/*
 Stock movement & related data :: Stock status.
 @Author: Eric Mwailunga
 May,2019
*/
    ItemStockRaw getItemQuantityOnHandInStoreByPaymentCategory(int itemId, int priceCategoryId, int storeId);
    ItemBatchRaw getItemStockByBatchNo(int itemId, String batchNo, int priceCategoryId, int storeId);
    ItemBatchRaw getItemStockByBatchNoExcludingOutstanding(int itemId, String batchNo, int priceCategoryId, int storeId);

    List<ItemBatchRaw> getAllStockByBatchesForItem(int itemId,int priceCategoryId,int storeId);
    List<ItemOutstandingStockRaw> getOutstandingStockForRequest(int requestId);

/*
 Stock movement & related data :: Dispensing
 @Author: Eric Mwailunga
 May,2019
*/
    List<ItemBatchRaw> getStockOnHandByBatchesInStore(int storeId);
    List<ItemStockRaw> getStockOnHandInStore(int storeId);
    ItemStockRaw getItemQuantityOnHandInStore(int itemId, int storeId);

    DispenseMapRaw getDispensingRawMapByVisitTypeAndPaymentCategory(int visitTypeId,int paymentCategoryId);
    //String savePrescriptionOrder(int patientId,int visitId,int itemId,int paymentCategoryId,int targetDispensingPoint);
    String savePrescriptionOrder(int patientId,int orderId,int visitId,int itemId,int itemCategoryId,int paymentCategoryId,int targetDispensingPoint);
    List<DispenseMapRaw> getAllDispensingMap(boolean includeRetired);
}