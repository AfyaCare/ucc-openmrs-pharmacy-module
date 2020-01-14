package org.openmrs.module.pharmacy.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.v1_0.controller.BaseRestController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 +"/pharmacy/dispense")
public class DispenseController extends BaseRestController {
    private final Log log = LogFactory.getLog(getClass());


    @RequestMapping(value = "/stock/drug/status", method = RequestMethod.GET)
    @ResponseBody
    public String getDrugItemStockStatus(@RequestParam("VisitUuid") String visitUuid,
                                         //@RequestParam("LocationUuid") String locationUuid,
                                         @RequestParam("PaymentCategoryUuid") String paymentCategoryUuid,
                                         @RequestParam("DrugUuid") String drugUuid)
    {
        log.info("DispenseController.getDrugItemStockStatus()...");
        ConceptService conceptService=Context.getConceptService();
        int paymentCategory=conceptService.getConceptByUuid(paymentCategoryUuid).getConceptId();
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        String stockResponse=pharmacyService.getStockStatusForDrugItemPrescription(visitUuid,paymentCategory,drugUuid);
        String response;
        if(stockResponse!=null)
        {
            JSONObject jsonObject=new JSONObject(stockResponse);
            if(jsonObject.getString("quantity").equals("feasible"))
            {
                response=new Gson().toJson("success");
            }else if(jsonObject.getString("quantity").equals("not-feasible")){
                response = new Gson().toJson(new JSONObject().put("alternativeStores",jsonObject.getJSONArray("alternativeStores")));
            }else{
                response = new Gson().toJson("Out of stock");
            }
        }else{
            response=new Gson().toJson("failed");
        }

        log.info("DispenseController.getDrugItemStockStatus() Returned data:\n"+response);
        return response;
    }


    @RequestMapping(value = "/drug/orders", method = RequestMethod.GET)
    @ResponseBody
    public String saveDrugItemOrderForDispensing(@RequestParam("VisitUuid") String visitUuid,
                                                 //@RequestParam("LocationUuid") String locationUuid,
                                                 @RequestParam("PaymentCategoryUuid") String paymentCategoryUuid,
                                                 @RequestParam("DrugUuids") String drugOrderJsonString)
    {
        log.info("DispenseController.saveDrugItemOrderForDispensing()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        ConceptService conceptService=Context.getConceptService();
        int paymentCategory=conceptService.getConceptByUuid(paymentCategoryUuid).getConceptId();

        String response;
        String responseRaw=pharmacyService.saveDrugItemOrderForDispensing(visitUuid,paymentCategory,drugOrderJsonString);
        if(responseRaw!=null)
        {
            response=new Gson().toJson(responseRaw);
        }else{
            response=new Gson().toJson("failed");
        }
        log.info("DispenseController.saveDrugItemOrderForDispensing() Returned data:\n"+response);
        return response;
    }


    @RequestMapping(value = "/drug/dispense", method = RequestMethod.GET)
    @ResponseBody
    public String dispenseDrugItemFromDispensingPointsStock(@RequestParam("VisitUuid") String visitUuid,
                                         @RequestParam("LocationUuid") String locationUuid,
                                         @RequestParam("PaymentCategory") int paymentCategory,
                                         @RequestParam("DrugUuids") String drugJsonString)
    {
        log.info("DispenseController.dispenseDrugItemFromDispensingPointsStock()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        String response=null;
        String responseRaw=pharmacyService.dispenseDrugItemFromDispensingPointsStock(locationUuid,visitUuid,paymentCategory,drugJsonString);
        if(responseRaw!=null)
        {
            response=new Gson().toJson(responseRaw);
        }else{
            response=new Gson().toJson("failed");
        }

        log.info("DispenseController.dispenseDrugItemFromDispensingPointsStock() Returned data:\n"+response);

        return response;
    }
}
