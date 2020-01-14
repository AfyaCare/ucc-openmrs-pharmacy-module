package org.openmrs.module.pharmacy.controller;

import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.model.item.ItemConcept;
import org.openmrs.module.pharmacy.model.item.Item;
import org.openmrs.module.pharmacy.model.item.drug.ItemDrug;
import org.openmrs.module.pharmacy.model.item.ItemNonDrug;
import org.openmrs.module.pharmacy.model.price.ItemPrice;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.v1_0.controller.BaseRestController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 +"/pharmacy/item")
public class ItemController extends BaseRestController {
    private final Log log = LogFactory.getLog(getClass());

/*
 Drug related data :: Dosage forms
 @Author: Eric Mwailunga
 May,2019
*/
    @RequestMapping(value = "dosageform/new", method = RequestMethod.GET)
    @ResponseBody
    public String createDosageForm(@RequestParam("DosageFormName") String dosageFormName)
    {
        log.info("ItemController.createDosageForm()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        String response=pharmacyService.createDosageForm(dosageFormName);
        if(response!=null)
        {
            response=new Gson().toJson(response);
        }else{
            response=new Gson().toJson("failed");
        }

        log.info("ItemController.createDosageForm() Returned data:\n"+response);
        return response;
    }

    @RequestMapping(value = "dosageform/{uuid}/read", method = RequestMethod.GET)
    @ResponseBody
    public String getDosageFormByUuid(@RequestParam("DosageFormUuid") String dosageFormUuid)
    {
        log.info("ItemController.getDosageFormByUuid()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        ItemConcept dosageForm=pharmacyService.getDosageFormByUuid(dosageFormUuid);
        String response;
        if(dosageForm!=null)
        {
            response=new Gson().toJson(dosageForm);
        }
        else{
            response=new Gson().toJson("empty");
        }
        log.info("ItemController.getDosageFormByUuid() Returned data:\n"+response);
        return response;
    }

    @RequestMapping(value = "dosageform/all", method = RequestMethod.GET)
    @ResponseBody
    public String getAllDosageForms()
    {
        log.info("ItemController.getAllDosageForms()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        List dosageForms=pharmacyService.getAllDosageForms();
        String response;
        if(dosageForms!=null)
        {
            response=new Gson().toJson(dosageForms);
        }
        else{
            response=new Gson().toJson("empty");
        }
        log.info("ItemController.getAllDosageForms() Returned data:\n"+response);
        return response;
    }

/*
 Drug related data :: Drugs generic Names
 @Author: Eric Mwailunga
 May,2019
*/
    @RequestMapping(value = "drug/generic/new", method = RequestMethod.GET)
    @ResponseBody
    public String createGenericDrugName(@RequestParam("GenericDrugName") String genericDrugName)
    {
        log.info("ItemController.createDrugGenericName()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        String response=pharmacyService.createGenericDrugName(genericDrugName);
        if(response!=null)
        {
            response=new Gson().toJson(response);
        }else{
            response=new Gson().toJson("failed");
        }
        log.info("ItemController.createDrugGenericName() Returned data:\n"+response);
        return response;
    }

    @RequestMapping(value = "drug/generic/{uuid}/read", method = RequestMethod.GET)
    @ResponseBody
    public String getGenericDrugNameByUuid(@PathVariable("uuid") String genericDrugNameUuid)
    {
        log.info("ItemController.getGenericDrugNameByUuid()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        ItemConcept genericDrugName=pharmacyService.getGenericDrugNameByUuid(genericDrugNameUuid);
        String response;
        if(genericDrugName!=null)
        {
            response=new Gson().toJson(genericDrugName);
        }
        else{
            response=new Gson().toJson("empty");
        }
        log.info("ItemController.getGenericDrugNameByUuid() Returned data:\n"+response);
        return response;
    }

    @RequestMapping(value = "drug/generic/{uuid}/update", method = RequestMethod.GET)
    @ResponseBody
    public String updateGenericDrugNameByUuid(@PathVariable("uuid") String genericDrugNameUuid,
                                              @RequestParam("Name") String name,
                                              @RequestParam("retired") boolean retired)
    {
        log.info("ItemController.updateGenericDrugNameByUuid()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        String updateResponse=pharmacyService.updateGenericDrugNameByUuid(genericDrugNameUuid,name,retired);
        String response;
        if(updateResponse!=null)
        {
            response=new Gson().toJson(updateResponse);
        }
        else{
            response=new Gson().toJson("failed");
        }
        log.info("ItemController.updateGenericDrugNameByUuid() Returned data:\n"+response);
        return response;
    }

    @RequestMapping(value = "drug/generic/name/search", method = RequestMethod.GET)
    @ResponseBody
    public String getGenericDrugNameBySubNames(@RequestParam("DrugSubName") String drugSubName)
    {
        log.info("ItemController.getGenericDrugNameBySubNames()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        List genericDrugNames=pharmacyService.getGenericDrugNameBySubName(drugSubName);
        String response;
        if(genericDrugNames!=null)
        {
            response=new Gson().toJson(genericDrugNames);
        }
        else{
            response=new Gson().toJson("empty");
        }
        log.info("ItemController.getGenericDrugNameBySubNames() Returned data:\n"+response);
        return response;
    }

    @RequestMapping(value = "drug/generic/all", method = RequestMethod.GET)
    @ResponseBody
    public String getAllGenericDrugNames(@RequestParam("IncludeRetired") boolean includeRetired)
    {
        log.info("ItemController.getAllGenericDrugNames()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        List genericDrugNames=pharmacyService.getAllGenericDrugNames(includeRetired);
        String response;
        if(genericDrugNames!=null)
        {
            response=new Gson().toJson(genericDrugNames);
        }
        else{
            response=new Gson().toJson("empty");
        }
        log.info("ItemController.getAllGenericDrugNames() Returned data:\n"+response);
        return response;
    }
/*
 Drug related data :: Actual drugs
 @Author: Eric Mwailunga
 May,2019
*/

    @RequestMapping(value = "drug/new", method = RequestMethod.GET)
    @ResponseBody
    public String createNewDrugName(@RequestParam("CategoryUuid") String categoryUuid,
                                    @RequestParam("GenericConceptId") int genericConceptId,
                                    @RequestParam("GenericName") String genericName,
                                    @RequestParam("Strength") String strength,
                                    @RequestParam("DosageConceptId") int dosageFormConceptId)
    {
        log.info("ItemController.createNewDrugName()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        String saveResponse=pharmacyService.saveDrug(categoryUuid,genericConceptId,genericName,strength,dosageFormConceptId);
        String response;
        if(saveResponse!=null)
        {
            response=new Gson().toJson(saveResponse);
        }
        else{
            response=new Gson().toJson("failed");
        }
        log.info("ItemController.createNewDrugName() Returned data:\n"+response);
        return response;
    }

    @RequestMapping(value = "drug/name/search", method = RequestMethod.GET)
    @ResponseBody
    public String searchDrugBySubName(@RequestParam("DrugSubName") String drugName)
    {
        log.info("ItemController.searchDrugBySubName()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        List<ItemDrug> itemDrugs =pharmacyService.getDrugsBySubName(drugName);
        String response;
        if(itemDrugs!=null)
        {
            response=new Gson().toJson(itemDrugs);
        }
        else{
            response=new Gson().toJson("empty");
        }
        log.info("ItemController.searchDrugBySubName() Returned data:\n"+response);

        return response;
    }

    @RequestMapping(value = "drug/{item_uuid}/update", method = RequestMethod.GET)
    @ResponseBody
    public String updateDrug(@PathVariable("item_uuid") String itemUuid,
                             @RequestParam("DrugName") String drugName)
    {
        log.info("ItemController.searchDrugByName()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        //String updateResponse=pharmacyService.updateDrug(drugName);
        String response=null;
       /* if(updateResponse!=null)
        {
            response=new Gson().toJson(updateResponse);
        }
        else{
            response=new Gson().toJson("empty");
        }
        log.info("ItemController.searchDrugByName() Returned data:\n"+response);
        */
        return response;
    }

    @RequestMapping(value = "drug/all", method = RequestMethod.GET)
    @ResponseBody
    public String getAllDrugs(@RequestParam("IncludeRetired") boolean includeRetired)
    {
        log.info("ItemController.getAllDrugs()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        List<ItemDrug> drugItemsList=pharmacyService.getAllDrugItems(includeRetired);
        String response;
        if(drugItemsList!=null)
        {
            response=new Gson().toJson(drugItemsList);
        }
        else{
            response=new Gson().toJson("empty");
        }
        log.info("ItemController.getAllDrugs() Returned data:\n"+response);

        return response;
    }

    @RequestMapping(value = "nondrug/new", method = RequestMethod.GET)
    @ResponseBody
    public String createNewNonDrug(@RequestParam("CategoryUuid") String categoryUuid,
                                    @RequestParam("SubCategoryUuid") String subCategoryUuid,
                                    @RequestParam("Attributes") String attributesJsonString)
    {
        log.info("ItemController.createNewNonDrug()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        String saveResponse=pharmacyService.saveNonDrugItem(categoryUuid,subCategoryUuid,attributesJsonString);
        String response;
        if(saveResponse!=null)
        {
            response=new Gson().toJson(saveResponse);
        }
        else{
            response=new Gson().toJson("failed");
        }
        log.info("ItemController.createNewNonDrug() Returned data:\n"+response);
        return response;
    }

    @RequestMapping(value = "nondrug/name", method = RequestMethod.GET)
    @ResponseBody
    public String getNonDrugByName(@RequestParam("ItemName") String itemName,
                                   @RequestParam("WithAttributes") boolean withAttributes)
    {
        log.info("ItemController.getNonDrugByName()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        String response;
        if(!withAttributes)
        {
            Item item=pharmacyService.getNonDrugByNameWithoutTheirAttributes(itemName);
            if(item!=null)
            {
                response=new Gson().toJson(item);
            }else{
                response= new Gson().toJson("empty");
            }
        }else{

            ItemNonDrug itemNonDrug=pharmacyService.getNonDrugByNameWithTheirAttributes(itemName);
            if(itemNonDrug!=null)
            {
                response=new Gson().toJson(itemNonDrug);
            }else{
                response= new Gson().toJson("empty");
            }
        }

        log.info("ItemController.getNonDrugByName() Returned data:\n"+response);
        return response;
    }

    @RequestMapping(value = "nondrug/{uuid}/update", method = RequestMethod.GET)
    @ResponseBody
    public String updateNonDrugName(@PathVariable("uuid") String uuid,
                                    @RequestParam("retired") boolean retired,
                                    @RequestParam("Attributes") String attributesJsonString)
    {
        log.info("ItemController.updateNonDrugName()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        String updateResponse=pharmacyService.updateNonDrugItem(uuid,retired,attributesJsonString);
        String response;
        if(updateResponse!=null)
        {
            response=new Gson().toJson(updateResponse);
        }
        else{
            response=new Gson().toJson("failed");
        }

        log.info("ItemController.updateNonDrugName() Returned data:\n"+response);
        return response;
    }

    @RequestMapping(value = "nondrug/all", method = RequestMethod.GET)
    @ResponseBody
    public String getAllNonDrugItems(@RequestParam("IncludeRetired") boolean includeRetired,
                                     @RequestParam("WithAttributes") boolean withAttributes)
    {
        log.info("ItemController.getAllNonDrugItems()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        String response;
        if(!withAttributes)
        {
            List<Item> itemList=pharmacyService.getAllNonDrugsWithoutTheirAttributes(includeRetired);
            if(itemList!=null)
            {
                response=new Gson().toJson(itemList);
            }else{
                response= new Gson().toJson("empty");
            }
        }else
            {
            List<ItemNonDrug> itemNonDrugsList=pharmacyService.getAllNonDrugsWithTheirAttributes(includeRetired);
            if(itemNonDrugsList!=null)
            {
                response=new Gson().toJson(itemNonDrugsList);
            }else{
                response= new Gson().toJson("empty");
            }
        }

        log.info("ItemController.getAllNonDrugItems() Returned data:\n"+response);
        return response;
    }



/*
 All pharmacy data :: Actual items
 @Author: Eric Mwailunga
 May,2019
*/
    @RequestMapping(value = "all", method = RequestMethod.GET)
    @ResponseBody
    public String getAllItems(@RequestParam("IncludeRetired") boolean includeRetired)
    {
        log.info("ItemController.getAllItems()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        List<Item> itemsList=pharmacyService.getAllItems(includeRetired);
        String response;
        if(itemsList!=null)
        {
            response=new Gson().toJson(itemsList);
        }else{
            response= new Gson().toJson("empty");
        }

        log.info("ItemController.getAllItems() Returned data:\n"+response);
        return response;
    }

    @RequestMapping(value = "{uuid}/read", method = RequestMethod.GET)
    @ResponseBody
    public String getItemByUuid(@PathVariable("uuid") String itemUuid)
    {
        log.info("ItemController.getItemByUuid()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        Item item=pharmacyService.getItemByUuid(itemUuid);
        String response=new Gson().toJson("empty");
        if(item!=null)
        {
            if(item.getCategoryName().equals("Drugs"))
            {
                ItemDrug itemDrug=pharmacyService.getDrugByItemUuid(itemUuid);
                if(itemDrug!=null) {
                    response=new Gson().toJson(itemDrug);
                }
            }

            if(item.getCategoryName().equals("Non drugs"))
            {
                ItemNonDrug itemNonDrug=pharmacyService.getNonDrugByItemUuid(itemUuid);
                if(itemNonDrug!=null) {
                    response = new Gson().toJson(itemNonDrug);
                }
            }
        }
        log.info("ItemController.getItemByUuid() Returned data:\n"+response);
        return response;
    }

    @RequestMapping(value = "/read/id", method = RequestMethod.GET)
    @ResponseBody
    public String getItemById(@RequestParam("itemId") int itemId)
    {
        log.info("ItemController.getItemById()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        Item item=pharmacyService.getItemById(itemId);
        String response=new Gson().toJson("empty");
        if(item!=null)
        {
            if(item.getCategoryName().equals("Drugs"))
            {
                ItemDrug itemDrug=pharmacyService.getDrugByItemUuid(item.getItemUuid());
                if(itemDrug!=null) {
                    response=new Gson().toJson(itemDrug);
                }
            }

            if(item.getCategoryName().equals("Non drugs"))
            {
                ItemNonDrug itemNonDrug=pharmacyService.getNonDrugByItemUuid(item.getItemUuid());
                if(itemNonDrug!=null) {
                    response = new Gson().toJson(itemNonDrug);
                }
            }
        }
        log.info("ItemController.getItemById() Returned data:\n"+response);
        return response;
    }


    @RequestMapping(value = "all/name/search", method = RequestMethod.GET)
    @ResponseBody
    public String getAllItemsBySubName(@RequestParam("ItemSubName") String itemName)
    {
        log.info("ItemController.getAllItemsBySubName()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        List<Item> items=pharmacyService.getAllItemsBySubName(itemName);
        String response=new Gson().toJson("empty");
        if(items!=null)
        {
            response = new Gson().toJson(items);
        }
        log.info("ItemController.getAllItemsBySubName() Returned data:\n"+response);
        return response;
    }


/*
 Item price related data
 @Author: Eric Mwailunga
 May,2019
*/
    @RequestMapping(value = "{uuid}/price/new", method = RequestMethod.GET)
    @ResponseBody
    public String createItemSellingPrice(@PathVariable("uuid") String itemUuid,
                                         @RequestParam("PriceCategoryId") int priceCategoryId,
                                         @RequestParam("SellingPrice") double sellingPrice)
    {
        log.info("ItemController.createItemSellingPrice()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        String response=pharmacyService.createItemSellingPrice(itemUuid,priceCategoryId,sellingPrice);
        if(response!=null)
        {
            response=new Gson().toJson(response);
        }else{
            response=new Gson().toJson("failed");
        }
        log.info("ItemController.createItemSellingPrice() Returned data:\n"+response);
        return response;
    }

    @RequestMapping(value = "{uuid}/price/read", method = RequestMethod.GET)
    @ResponseBody
    public String getItemSellingPrice(@PathVariable("uuid") String itemUuid,
                                      @RequestParam("PriceCategoryId") int priceCategoryId)
    {
        log.info("ItemController.getItemSellingPrice()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        ItemPrice itemPrice =pharmacyService.getItemSellingPrice(itemUuid,priceCategoryId);
        String response;
        if(itemPrice !=null)
        {
            response=new Gson().toJson(itemPrice);
        }else{
            response=new Gson().toJson("failed");
        }
        log.info("ItemController.getItemSellingPrice() Returned data:\n"+response);
        return response;
    }

    @RequestMapping(value = "price/{uuid}/read", method = RequestMethod.GET)
    @ResponseBody
    public String getItemSellingPriceByUuid(@PathVariable("uuid") String itemPriceUuid)
    {
        log.info("ItemController.getItemSellingPriceByUuid()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        ItemPrice itemPrice =pharmacyService.getItemSellingPriceByItemPriceUuid(itemPriceUuid);
        String response;
        if(itemPrice !=null)
        {
            response=new Gson().toJson(itemPrice);
        }else{
            response=new Gson().toJson("failed");
        }
        log.info("ItemController.getItemSellingPriceByUuid() Returned data:\n"+response);
        return response;
    }

    @RequestMapping(value = "{uuid}/price/update", method = RequestMethod.GET)
    @ResponseBody
    public String updateItemSellingPrice(@PathVariable("uuid") String itemUuid,
                                      @RequestParam("PriceCategoryId") int priceCategoryId,
                                      @RequestParam("SellingPrice") double sellingPrice)
    {
        log.info("ItemController.updateItemSellingPrice()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        String saveResponse =pharmacyService.updateItemSellingPrice(itemUuid,priceCategoryId,sellingPrice);
        String response;
        if(saveResponse !=null)
        {
            response=new Gson().toJson(saveResponse);
        }else{
            response=new Gson().toJson("failed");
        }
        log.info("ItemController.updateItemSellingPrice() Returned data:\n"+response);
        return response;
    }


    @RequestMapping(value = "price/all", method = RequestMethod.GET)
    @ResponseBody
    public String getAllItemSellingPrice()
    {
        log.info("ItemController.getAllItemSellingPrice()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        List<ItemPrice> itemPrices =pharmacyService.getAllItemSellingPrice();
        String response;
        if(itemPrices !=null)
        {
            response=new Gson().toJson(itemPrices);
        }else{
            response=new Gson().toJson("failed");
        }
        log.info("ItemController.getAllItemSellingPrice() Returned data:\n"+response);
        return response;
    }

}
