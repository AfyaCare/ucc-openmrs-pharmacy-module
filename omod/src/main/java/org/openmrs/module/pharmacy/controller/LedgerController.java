package org.openmrs.module.pharmacy.controller;

import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.model.ledger.LedgerLine;
import org.openmrs.module.pharmacy.model.ledger.LedgerType;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.v1_0.controller.BaseRestController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 +"/pharmacy/ledger")
public class LedgerController extends BaseRestController {

    private final Log log = LogFactory.getLog(getClass());


/*
 LedgerRawLine entries & related data
 @Author: Eric Mwailunga
 May,2019
*/
    @RequestMapping(value = "entry/new", method = RequestMethod.GET)
    @ResponseBody
    public String createLedgerEntry(@RequestParam("LedgerTypeId") int ledgerTypeId,
                                    @RequestParam("ItemId") int itemId,
                                    @RequestParam("PriceCategoryId") int priceCategoryId,
                                    @RequestParam("BatchNo") String batchNo,
                                    @RequestParam("InvoiceNo") String invoiceNo,
                                    @RequestParam("BuyingPrice") double buyingPrice,
                                    @RequestParam("quantity") int quantity,
                                    @RequestParam("DateMoved") String dateMoved,
                                    @RequestParam("ExpiryDate") String expiryDate,
                                    @RequestParam("DosageFormId") int dosageUnits,
                                    @RequestParam("Remarks") String remarks)
    {
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        String saveResponse=pharmacyService.
                createLedgerEntry(ledgerTypeId,itemId,priceCategoryId,batchNo,invoiceNo,buyingPrice,quantity,dateMoved,expiryDate,dosageUnits,remarks);
        String response=new Gson().toJson("failed");
        if(saveResponse!=null)
        {
            response= new Gson().toJson(saveResponse);
        }
        log.info("LedgerController.createLedgerEntry(), Response:"+response);
        return response;
    }

    @RequestMapping(value = "{uuid}/read", method = RequestMethod.GET)
    @ResponseBody
    public String getLedgerEntryByUuid(@PathVariable("uuid") String ledgerEntryUuid)
    {
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        LedgerLine ledgerLine=pharmacyService.getLedgerEntryByUuid(ledgerEntryUuid);
        String response;
        if(ledgerLine!=null)
        {
            response= new Gson().toJson(ledgerLine);
        }
        else {
            response=new Gson().toJson("empty");
        }
        log.info("LedgerController.getLedgerEntryByUuid(), Response:"+response);
        return response;
    }

    @RequestMapping(value = "all", method = RequestMethod.GET)
    @ResponseBody
    public String getAllLedgerEntries()
    {
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        List<LedgerLine> ledgerLines=pharmacyService.getAllLedgerEntries();
        String response;
        if(ledgerLines!=null)
        {
            response= new Gson().toJson(ledgerLines);
        }
        else {
            response=new Gson().toJson("empty");
        }
        log.info("LedgerController.getAllLedgerEntries(), Response:"+response);
        return response;
    }

    /*
 LedgerRawLine entry types & related data
 @Author: Eric Mwailunga
 May,2019
*/
    @RequestMapping(value = "type/new", method = RequestMethod.GET)
    @ResponseBody
    public String createLedgerType(@RequestParam("LedgerTypeName") String ledgerTypeName,
                                   @RequestParam("Operation") String operation)
    {
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        String saveResponse=pharmacyService.createLedgerType(ledgerTypeName,operation);
        String response;
        if(saveResponse!=null)
        {
            response= new Gson().toJson(saveResponse);
        }
        else {
            response=new Gson().toJson("empty");
        }
        log.info("LedgerController.createPriceCategory(), Response:"+response);
        return response;
    }

    @RequestMapping(value = "type/{uuid}/read", method = RequestMethod.GET)
    @ResponseBody
    public String getLedgerTypeByUuid(@PathVariable("uuid") String ledgerTypeUuid)
    {
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        LedgerType ledgerType=pharmacyService.getLedgerTypeByUuid(ledgerTypeUuid);
        String response;
        if(ledgerType!=null)
        {
            response= new Gson().toJson(ledgerType);
        }
        else {
            response=new Gson().toJson("empty");
        }
        log.info("LedgerController.getLedgerTypeByUuid(), Response:"+response);
        return response;
    }

    @RequestMapping(value = "type/{uuid}/update", method = RequestMethod.GET)
    @ResponseBody
    public String updateLedgerTypeByUuid(@PathVariable("uuid") String ledgerTypeUuid,
                                         @RequestParam("LedgerTypeName") String ledgerTypeName,
                                         @RequestParam("Operation") String operation,
                                         @RequestParam("retired") boolean retired)
    {
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        String updateResponse=pharmacyService.updateLedgerTypeByUuid(ledgerTypeUuid,ledgerTypeName,operation,retired);
        String response;
        if(updateResponse!=null)
        {
            response= new Gson().toJson(updateResponse);
        }
        else {
            response=new Gson().toJson("empty");
        }
        log.info("LedgerController.updateLedgerTypeByUuid(), Response:"+response);
        return response;
    }

    @RequestMapping(value = "type/all", method = RequestMethod.GET)
    @ResponseBody
    public String getAllLedgerTypes(@RequestParam("IncludeRetired") boolean includeRetired)
    {
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        List<LedgerType> saveResponse=pharmacyService.getAllLedgerTypes(includeRetired);
        String response;
        if(saveResponse!=null)
        {
            response= new Gson().toJson(saveResponse);
        }
        else {
            response=new Gson().toJson("empty");
        }
        log.info("LedgerController.getAllLedgerTypes(), Response:"+response);
        return response;
    }
}
