package org.openmrs.module.pharmacy.controller;

import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Privilege;
import org.openmrs.User;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.model.stock.ItemBatch;
import org.openmrs.module.pharmacy.model.stock.ItemStock;
import org.openmrs.module.pharmacy.model.stock.request.ItemRequest;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.v1_0.controller.BaseRestController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 +"/pharmacy/stock")
public class StockController  extends BaseRestController {
    private final Log log = LogFactory.getLog(getClass());

/*
 Stock operations :: Items stock requests
 @Author: Eric Mwailunga
 May,2019
*/

 /*   @RequestMapping(value = "item/request/new", method = RequestMethod.GET)
    @ResponseBody
    public String createItemRequestFromDispensingPoint(@RequestParam("ItemUuid") String itemUuid,
                                                       @RequestParam("RequestGroupId") String requestGroupId,
                                                       @RequestParam("PriceCategoryUuid") String priceCategoryUuid,
                                                       @RequestParam("quantity") int quantity,
                                                       @RequestParam("SourceLocationUuid") String sourceLocationUuid,
                                                       @RequestParam("DestinationStoreId") int destinationStoreId)
    {
        log.info("ItemController.createItemRequestFromDispensingPoint()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        String response=pharmacyService.createItemRequestFromDispensingPoint(requestGroupId,itemUuid,priceCategoryUuid,quantity,sourceLocationUuid,destinationStoreId);
        if(response!=null)
        {
            response=new Gson().toJson(response);
        }else{
            response=new Gson().toJson("failed");
        }

        log.info("ItemController.createItemRequestFromDispensingPoint() Returned data:\n"+response);
        return response;
    }
 */
    @RequestMapping(value = "item/requests/new", method = RequestMethod.GET)
    @ResponseBody
    public String createItemsRequestFromDispensingPoint(@RequestParam("RequestGroupId") String requestGroupId,
                                                        @RequestParam("SourceLocationUuid") String sourceLocationUuid,
                                                        @RequestParam("DestinationStoreId") int destinationStoreId,
                                                        @RequestParam("requests") String requestsJson)
    {
        log.info("ItemController.createItemsRequestFromDispensingPoint()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        String response=pharmacyService.createItemsRequestFromDispensingPoint(requestGroupId,sourceLocationUuid,destinationStoreId,requestsJson);
        if(response!=null)
        {
            response=new Gson().toJson(response);
        }else{
            response=new Gson().toJson("failed");
        }

        log.info("ItemController.createItemRequestFromDispensingPoint() Returned data:\n"+response);
        return response;
    }

    @RequestMapping(value = "item/requests/all", method = RequestMethod.GET)
    @ResponseBody
    public String getAllItemRequestsInDispensingPointByLocationUuid(@RequestParam("LocationUuid") String locationUuid)
    {
        log.info("ItemController.getAllItemRequestsInDispensingPointByLocationUuid()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        List<ItemRequest> itemRequests=pharmacyService.getAllItemRequestsByLocationUuid(locationUuid);
        String response;
        if(itemRequests!=null)
        {
            response=new Gson().toJson(itemRequests);
        }else{
            response=new Gson().toJson("empty");
        }

        log.info("ItemController.getAllItemRequestsInDispensingPointByLocationUuid() Returned data:\n"+response);
        return response;
    }

    @RequestMapping(value = "item/request/{uuid}/cancel", method = RequestMethod.GET)
    @ResponseBody
    public String requesterCancelingRequestByUuid(@PathVariable("uuid") String requestUuid,
                                                  @RequestParam("LocationUuid") String locationUuid)
    {
        log.info("ItemController.requesterCancelingRequestByUuid()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        String response=pharmacyService.requesterCancellingRequestByUuid(requestUuid,locationUuid);
        if(response!=null)
        {
            response=new Gson().toJson(response);
        }else{
            response=new Gson().toJson("failed");
        }

        log.info("ItemController.requesterCancelingRequestByUuid() Returned data:\n"+response);
        return response;
    }

    @RequestMapping(value = "item/request/{uuid}/reject", method = RequestMethod.GET)
    @ResponseBody
    public String requesterRejectsConfirmingRequestByUuid(@PathVariable("uuid") String requestUuid,
                                               @RequestParam("LocationUuid") String locationUuid)
    {
        log.info("ItemController.requesterRejectingRequestByUuid()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        String response=pharmacyService.requesterRejectsConfirmingRequestByUuid(requestUuid,locationUuid);
        if(response!=null)
        {
            response=new Gson().toJson(response);
        }else{
            response=new Gson().toJson("failed");
        }

        log.info("ItemController.requesterRejectingRequestByUuid() Returned data:\n"+response);
        return response;
    }

    @RequestMapping(value = "item/request/{uuid}/confirmreceive", method = RequestMethod.GET)
    @ResponseBody
    public String requesterConfirmToReceiveItemsByRequestUuid(@PathVariable("uuid") String requestUuid,
                                                  @RequestParam("LocationUuid") String locationUuid)
    {
        log.info("ItemController.requesterAcceptingRequestByUuid()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        String response=pharmacyService.requesterConfirmToReceiveItemsByRequestUuid(requestUuid,locationUuid);
        if(response!=null)
        {
            response=new Gson().toJson(response);
        }else{
            response=new Gson().toJson("failed");
        }

        log.info("ItemController.requesterAcceptingRequestByUuid() Returned data:\n"+response);
        return response;
    }

    @RequestMapping(value = "item/requests/main/all", method = RequestMethod.GET)
    @ResponseBody
    public String getAllItemRequestsInMainStore(@RequestParam("LocationUuid") String locationUuid)
    {
        log.info("ItemController.getAllItemRequestsInMainStore()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        List<ItemRequest> itemRequests=pharmacyService.getAllItemRequestsInMainStore(locationUuid);
        String response;
        if(itemRequests!=null)
        {
            response=new Gson().toJson(itemRequests);
        }else{
            response=new Gson().toJson("empty");
        }

        log.info("ItemController.getAllItemRequestsByLocationUuid() Returned data:\n"+response);
        return response;
    }

    @RequestMapping(value = "item/request/main/{uuid}/reject", method = RequestMethod.GET)
    @ResponseBody
    public String issuerRejectingRequestByUuid(@PathVariable("uuid") String requestUuid,
                                               @RequestParam("LocationUuid") String locationUuid)
    {
        log.info("ItemController.issuerRejectingRequestByUuid()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        String response=pharmacyService.issuerRejectingRequestByUuid(requestUuid,locationUuid);
        if(response!=null)
        {
            response=new Gson().toJson(response);
        }else{
            response=new Gson().toJson("failed");
        }

        log.info("ItemController.issuerRejectingRequestByUuid() Returned data:\n"+response);
        return response;
    }

    @RequestMapping(value = "item/request/{uuid}/possiblebatches", method = RequestMethod.GET)
    @ResponseBody
    public String possibleItemsBatchesToIssue(@PathVariable("uuid") String requestUuid)
    {
        log.info("ItemController.possibleItemsBatchesToIssue()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        List<ItemBatch> itemBatches=pharmacyService.possibleItemsBatchesToIssue(requestUuid);
        String response;
        if(itemBatches!=null)
        {
            response=new Gson().toJson(itemBatches);
        }else{
            response=new Gson().toJson("failed");
        }

        log.info("ItemController.possibleItemsBatchesToIssue() Returned data:\n"+response);
        return response;
    }

    @RequestMapping(value = "item/request/{uuid}/issue", method = RequestMethod.GET)
    @ResponseBody
    public String issueItemRequestedByUuid(@PathVariable("uuid") String requestUuid,
                                           @RequestParam("LocationUuid") String locationUuid,
                                           @RequestParam("ItemBatches") String itemBatchesJson)
    {
        log.info("ItemController.issueItemRequestedByUuid()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        String response=pharmacyService.issueItemRequestedByRequestUuid(requestUuid,locationUuid,itemBatchesJson);
        if(response!=null)
        {
            response=new Gson().toJson(response);
        }else{
            response=new Gson().toJson("failed");
        }

        log.info("ItemController.issueItemRequestedByUuid() Returned data:\n"+response);
        return response;
    }


/*
 Stock operations :: Stock status
 @Author: Eric Mwailunga
 May,2019
* /
    @RequestMapping(value = "item/status/{uuid}", method = RequestMethod.GET)
    @ResponseBody
    public String fetchItemStockOnHandByLocationUuid(@RequestParam("LocationUuid") String locationUuid,
                                                     @PathVariable("uuid") String itemUuid)
    {
        log.info("StockController.fetchItemStockOnHandByLocationUuid()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        String response=pharmacyService.fetchItemStockOnHandByLocationUuid(itemUuid,locationUuid);
        if(response!=null)
        {
            response=new Gson().toJson(response);
        }else{
            response=new Gson().toJson("failed");
        }

        log.info("StockController.fetchItemStockOnHandByLocationUuid() Returned data:\n"+response);
        return response;
    }

    @RequestMapping(value = "item/status/{uuid}", method = RequestMethod.GET)
    @ResponseBody
    public String fetchItemStockOnByBatchGivenLocationUuid(@RequestParam("LocationUuid") String locationUuid,
                                                   @PathVariable("uuid") String itemUuid)
    {
        log.info("StockController.fetchItemStockOnByBatchGivenLocationUuid()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        String response=pharmacyService.fetchItemStockOnByBatchGivenLocationUuid(itemUuid,locationUuid);
        if(response!=null)
        {
            response=new Gson().toJson(response);
        }else{
            response=new Gson().toJson("failed");
        }

        log.info("StockController.fetchItemStockOnByBatchGivenLocationUuid() Returned data:\n"+response);
        return response;
    }
*/
    @RequestMapping(value = "item/status/all", method = RequestMethod.GET)
    @ResponseBody
    public String fetchItemStockOnHandAllItemsByPriceCategory(@RequestParam("LocationUuid") String locationUuid)
    {
        log.info("StockController.fetchItemStockStatusByLocationUuid()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        boolean isMainStore=false;
        boolean isDispensing=false;
        String response;
        User user=Context.getAuthenticatedUser();
        Collection<Privilege> privileges=user.getPrivileges();
        if(privileges!=null)
        {
            Iterator iterator=privileges.iterator();
            while(iterator.hasNext())
            {
                Privilege privilege=(Privilege) iterator.next();
                if(privilege.getName().equals("app:pharmacy:main-store:stock-view"))
                {
                    isMainStore=true;
                }
                if(privilege.getName().equals("app:pharmacy:dispensing-point:stock-view")){
                    isDispensing=true;
                }
            }
        }

        if(isMainStore)
        {
            List<ItemStock> itemStocks=pharmacyService.fetchItemStockOnHandAllItemsOnHandByPriceCategoryMainStore(locationUuid);
            if(itemStocks!=null)
            {
                response=new Gson().toJson(itemStocks);
            }else{
                response=new Gson().toJson("empty");
            }
        }
        else if(isDispensing)
        {
            List<ItemStock> itemStocks=pharmacyService.fetchItemStockOnHandAllItemsOnHandByPriceCategory(locationUuid);
            if(itemStocks!=null)
            {
                response=new Gson().toJson(itemStocks);
            }else{
                response=new Gson().toJson("empty");
            }
        }
        else{
            response=new Gson().toJson("empty");
        }
        log.info("StockController.fetchItemStockOnHandAllItemsByPriceCategory() Returned data:\n"+response);
        return response;
    }

    @RequestMapping(value = "item/status/all/detailed", method = RequestMethod.GET)
    @ResponseBody
    public String fetchItemStockOnHandAllItemsByBatches(@RequestParam("LocationUuid") String locationUuid)
    {
        log.info("StockController.fetchItemStockOnHandAllItemsByBatches()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        List<ItemBatch> itemBatches=pharmacyService.fetchItemStockOnHandAllItemsByBatches(locationUuid);
        String response;
        if(itemBatches!=null)
        {
            response=new Gson().toJson(itemBatches);
        }else{
            response=new Gson().toJson("empty");
        }

        log.info("StockController.fetchItemStockOnHandAllItemsByBatches() Returned data:\n"+response);
        return response;
    }
}
