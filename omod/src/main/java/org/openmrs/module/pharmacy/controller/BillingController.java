package org.openmrs.module.pharmacy.controller;

import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.v1_0.controller.BaseRestController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 +"/pharmacy/billing")
public class BillingController extends BaseRestController {

    private final Log log = LogFactory.getLog(getClass());

/*
  Billing and related data :: Drugs and non drugs
  @Author: Eric Mwailunga
  May,2019
*/
    @RequestMapping(value = "order/drugs", method = RequestMethod.GET)
    @ResponseBody
    public String billOrderedDrugItems(@RequestParam("VisitUuid") String visitUuid,
                                       @RequestParam("PaymentCategoryUuid") String paymentCategoryUuid,
                                       @RequestParam("DrugUuids") String drugOrderJsonString)
    {
        log.info("ItemController.billOrderedDrugItems()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        String success=pharmacyService.billOrderedDrugItems(visitUuid,paymentCategoryUuid,drugOrderJsonString);
        String response=new Gson().toJson("failed");
        if(success!=null)
        {
            response=new Gson().toJson("success");
        }
        log.info("ItemController.billOrderedDrugItems() Returned data:\n"+response);
        return response;
    }

    @RequestMapping(value = "order/nondrugs", method = RequestMethod.GET)
    @ResponseBody
    public String billOrderedNonDrugItems(@RequestParam("VisitUuid") String visitUuid,
                                          @RequestParam("PaymentCategoryUuid") String paymentCategoryUuid,
                                          @RequestParam("ItemUuids") String itemsOrderJsonString)
    {
        log.info("ItemController.billOrderedNonDrugItems()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        String success=pharmacyService.billOrderedNonDrugItems(visitUuid,paymentCategoryUuid,itemsOrderJsonString);
        String response=new Gson().toJson("failed");
        if(success!=null)
        {
            response=new Gson().toJson("success");
        }
        log.info("ItemController.billOrderedNonDrugItems() Returned data:\n"+response);
        return response;
    }
}
