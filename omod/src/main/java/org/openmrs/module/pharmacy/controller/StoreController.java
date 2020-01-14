package org.openmrs.module.pharmacy.controller;

import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.model.store.Store;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.v1_0.controller.BaseRestController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

//import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 +"/pharmacy/store")
public class StoreController extends BaseRestController {
    private final Log log = LogFactory.getLog(getClass());

/*
Creating stores' & related data
@Author: Eric Mwailunga
May,2019
*/
    @RequestMapping(value = "new", method = RequestMethod.GET)
    @ResponseBody
    public String createStore(@RequestParam("StoreName") String storeName)
    {
        log.info("StoreController.createStore()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        String response=pharmacyService.createStore(storeName);
        return new Gson().toJson(response);
    }

    @RequestMapping(value = "substore/new", method = RequestMethod.GET)
    @ResponseBody
    public String createSubStore(@RequestParam("StoreName") String storeName,
                                 @RequestParam("ParentStoreUuid") String parentStoreUuid)
    {
        log.info("StoreController.createSubStore()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        String response=pharmacyService.createSubStore(storeName,parentStoreUuid);
        return new Gson().toJson(response);
    }


/*
Reading stores' & related data
@Author: Eric Mwailunga
May,2019
*/

    @RequestMapping(value = "{uuid}/read", method = RequestMethod.GET)
    @ResponseBody
    public String getStoreByUuid(@PathVariable("uuid") String storeUuid) {
        log.info("StoreController.getStoreByUuid()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        Store store=pharmacyService.getStoreByUuid(storeUuid);
        String response;
        if(store!=null){
            response=new Gson().toJson(store);
        }else{
            response=new Gson().toJson("empty");
        }
        return response;
    }

    @RequestMapping(value = "substore/{uuid}/read", method = RequestMethod.GET)
    @ResponseBody
    public String getSubStoreByUuid(@PathVariable("uuid") String subStoreUuid) {
        log.info("StoreController.getSubStoreByUuid()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        Store store=pharmacyService.getSubStoreByUuid(subStoreUuid);
        String response;
        if(store!=null){
            response=new Gson().toJson(store);
        }else{
            response=new Gson().toJson("empty");
        }
        return response;
    }
    @RequestMapping(value = "name", method = RequestMethod.GET)
    @ResponseBody
    public String getStoreByName(@RequestParam("StoreName") String storeName)
    {
        log.info("StoreController.getStoreByName()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        Store store=pharmacyService.getStoreByName(storeName);
        String response;
        if(store!=null){
            response=new Gson().toJson(store);
        }else{
            response=new Gson().toJson("empty");
        }
        return response;
    }

    @RequestMapping(value = "name/search", method = RequestMethod.GET)
    @ResponseBody
    public String getStoreBySubName(@RequestParam("containing") String nameSubString)
    {
        log.info("StoreController.getStoreByName()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        List stores=pharmacyService.getStoreBySubName(nameSubString);
        String response;
        if(stores!=null){
            response=new Gson().toJson(stores);
        }else{
            response=new Gson().toJson("empty");
        }
        log.info("StoreController.getStoreByName() Returned data:\n"+response);
        return response;
    }

    @RequestMapping(value = "{uuid}/substores", method = RequestMethod.GET)
    @ResponseBody
    public String getStoreSubStore(@PathVariable("uuid") String storeUuid,@RequestParam("IncludeRetired") boolean includeRetired)
    {
        log.info("StoreController.getStoreSubStore()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        List stores;
        stores = pharmacyService.getStoreSubStore(storeUuid,includeRetired);
        String response;
        if(stores!=null){
            response=new Gson().toJson(stores);
        }else{
            response=new Gson().toJson("empty");
        }
        log.info("StoreController.getStoreSubStore() Returned data:\n"+response);
        return response;
    }

    @RequestMapping(value = "all", method = RequestMethod.GET)
    @ResponseBody
    public String getAllStores(@RequestParam("IncludeRetired") boolean includeRetired)
    {
        log.info("StoreController.getAllStores()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        List stores=pharmacyService.getAllStores(includeRetired);
        String response;
        if(stores!=null){
            response=new Gson().toJson(stores);
        }else{
            response=new Gson().toJson("empty");
        }
        log.info("StoreController.getAllStores() Returned data:\n"+response);
        return response;
    }

    @RequestMapping(value = "all/full", method = RequestMethod.GET)
    @ResponseBody
    public String getAllStoresFull(@RequestParam("IncludeRetired") boolean includeRetired)
    {
        log.info("StoreController.getAllStoresFull()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        List stores=pharmacyService.getAllStoresFull(includeRetired);
        String response;
        if(stores!=null){
            response=new Gson().toJson(stores);
        }else{
            response=new Gson().toJson("empty");
        }
        log.info("StoreController.getAllStoresFull() Returned data:\n"+response);
        return response;
    }

    @RequestMapping(value = "substore/name", method = RequestMethod.GET)
    @ResponseBody
    public String getSubStoreByName(@RequestParam("SubStoreName")String subStoreName){
        log.info("StoreController.getSubStoreByName()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        Store store=pharmacyService.getSubStoreByName(subStoreName);
        String response;
        if(store!=null){
            response=new Gson().toJson(store);
        }else{
            response=new Gson().toJson("empty");
        }
        return response;
    }

    @RequestMapping(value = "substore/name/search", method = RequestMethod.GET)
    @ResponseBody
    public String getSubStoreBySubName(@RequestParam("containing") String subStoreName){
        log.info("StoreController.getSubStoreBySubName()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        List stores=pharmacyService.getSubStoreBySubName(subStoreName);
        String response;
        if(stores!=null){
            response=new Gson().toJson(stores);
        }else{
            response=new Gson().toJson("empty");
        }
        return response;
    }

    @RequestMapping(value = "substore/all", method = RequestMethod.GET)
    @ResponseBody
    public String getAllSubStore(@RequestParam("IncludeRetired") boolean includeRetired)
    {
        log.info("StoreController.getAllSubStore()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        List stores=pharmacyService.getAllSubStore(includeRetired);
        String response;
        if(stores!=null){
            response=new Gson().toJson(stores);
        }else{
            response=new Gson().toJson("empty");
        }
        log.info("StoreController.getAllSubStore() Returned data:\n"+response);
        return response;
    }

/*
Updating/Editing stores' & related data
@Author: Eric Mwailunga
May,2019
*/

    @RequestMapping(value = "{uuid}/update", method = RequestMethod.GET)
    @ResponseBody
    public String updateStoreByUuid(@PathVariable("uuid") String storeUuid,
                                    @RequestParam("StoreName") String storeName,
                                    @RequestParam("retired") boolean retired)
    {
        log.info("StoreController.updateStoreByUuid()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        String response=pharmacyService.updateStoreByUuid(storeUuid,storeName,retired);
        return new Gson().toJson(response);
    }

    @RequestMapping(value = "substore/{uuid}/update", method = RequestMethod.GET)
    @ResponseBody
    public String updateSubStoreByUuid(@PathVariable("uuid") String storeUuid,
                                    @RequestParam("StoreName") String storeName,
                                    @RequestParam("ParentUuid") String parentUuid,
                                    @RequestParam("retired") boolean retired)
    {
        log.info("StoreController.updateSubStoreByUuid()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        String response=pharmacyService.updateSubStoreByUuid(storeUuid,storeName,parentUuid,retired);
        return new Gson().toJson(response);
    }

    @RequestMapping(value = "{uuid}/delete", method = RequestMethod.GET)
    @ResponseBody
    public String deleteStoreByUuid(@PathVariable("uuid") String storeUuid)
    {
        log.info("StoreController.deleteStoreByUuid()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        String response=pharmacyService.deleteStoreByUuid(storeUuid);
        return new Gson().toJson(response);
    }

    @RequestMapping(value = "substore/{uuid}/delete", method = RequestMethod.GET)
    @ResponseBody
    public String deleteSubStoreByUuid(@PathVariable("uuid") String subStoreUuid)
    {
        log.info("StoreController.deleteSubStoreByUuid()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        String response=pharmacyService.deleteSubStoreByUuid(subStoreUuid);
        return new Gson().toJson(response);
    }
}
