package org.openmrs.module.pharmacy.controller;

import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.model.price.PriceCategory;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.v1_0.controller.BaseRestController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 +"/pharmacy/price/")
public class PriceController extends BaseRestController {

    private final Log log = LogFactory.getLog(getClass());

    @RequestMapping(value = "category/new", method = RequestMethod.GET)
    @ResponseBody
    public String createPriceCategory(@RequestParam("CategoryName") String categoryName)
    {
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        String saveResponse=pharmacyService.createPriceCategory(categoryName);
        String response=new Gson().toJson("failed");
        if(saveResponse!=null)
        {
            response= new Gson().toJson(saveResponse);
        }
        log.info("PriceController.createPriceCategory(), Response:"+response);
        return response;
    }

    @RequestMapping(value = "category/{uuid}/read", method = RequestMethod.GET)
    @ResponseBody
    public String getPriceCategoryByUuid(@PathVariable("uuid") String categoryUuid)
    {
        PharmacyService pharmacyService=Context.getService(PharmacyService.class);
        PriceCategory priceCategory=pharmacyService.getPriceCategoryByUuid(categoryUuid);
        String response=new Gson().toJson("failed");
        if(priceCategory!=null)
        {
            response= new Gson().toJson(priceCategory);
        }
        log.info("PriceController.getPriceCategoryByUuid(), Response:"+response);
        return response;
    }

    @RequestMapping(value = "category/{uuid}/update", method = RequestMethod.GET)
    @ResponseBody
    public String updatePriceCategoryByUuid(@PathVariable("uuid") String categoryUuid,
                                            @RequestParam("CategoryName") String categoryName,
                                            @RequestParam("retired") boolean retired)
    {
        PharmacyService pharmacyService=Context.getService(PharmacyService.class);
        String updateResponse=pharmacyService.updatePriceCategory(categoryUuid,categoryName,retired);
        String response=new Gson().toJson("failed");
        if(updateResponse!=null)
        {
            response= new Gson().toJson(updateResponse);
        }
        log.info("PriceController.updatePriceCategoryByUuid(), Response:"+response);
        return response;
    }

    @RequestMapping(value = "category/name", method = RequestMethod.GET)
    @ResponseBody
    public String getPriceCategoryByName(@RequestParam("CategoryName") String categoryName)
    {
        PharmacyService pharmacyService=Context.getService(PharmacyService.class);
        PriceCategory priceCategory=pharmacyService.getPriceCategoryByName(categoryName);
        String response=new Gson().toJson("failed");
        if(priceCategory!=null)
        {
            response= new Gson().toJson(priceCategory);
        }
        log.info("PriceController.getPriceCategoryByName(), Response:"+response);
        return response;
    }

    @RequestMapping(value = "category/all", method = RequestMethod.GET)
    @ResponseBody
    public String getAllPriceCategories(@RequestParam("IncludeRetired") boolean includeRetired)
    {
        PharmacyService pharmacyService=Context.getService(PharmacyService.class);
        List<PriceCategory> priceCategories=pharmacyService.getAllPriceCategories(includeRetired);
        String response=new Gson().toJson("failed");
        if(priceCategories!=null)
        {
            response= new Gson().toJson(priceCategories);
        }
        log.info("PriceController.getAllPriceCategories(), Response:"+response);
        return response;
    }



}
