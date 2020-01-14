package org.openmrs.module.pharmacy.controller;

import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.model.item.itemcategory.Category;
import org.openmrs.module.pharmacy.model.item.itemcategory.SubCategory;
import org.openmrs.module.pharmacy.model.item.itemcategory.SubCategoryAttribute;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.v1_0.controller.BaseRestController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 +"/pharmacy/category")
public class CategoryController extends BaseRestController {
    private final Log log = LogFactory.getLog(getClass());

/*
Creating categories & related data
@Author: Eric Mwailunga
May,2019
*/
    @RequestMapping(value = "new", method = RequestMethod.GET)
    @ResponseBody
    public String createCategory(@RequestParam("CategoryName") String categoryName)
    {
        /*
        * Reserved for future development
        * Eric M. May.2019
        */

        log.info("CategoryController.createCategory()...");
        //PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        return new Gson().toJson("empty");
    }

    @RequestMapping(value = "all", method = RequestMethod.GET)
    @ResponseBody
    public String getAllCategories(@RequestParam("IncludeRetired") boolean includeRetired)
    {
        log.info("CategoryController.getAllCategories()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        List categories=pharmacyService.getAllCategories(includeRetired);
        String response;
        if(categories!=null)
        {
            response=new Gson().toJson(categories);
        }
        else {
            response=new Gson().toJson("empty");
        }

        log.info("CategoryController.getAllCategories() Returned data:\n"+response);
        return response;
    }

    @RequestMapping(value = "{uuid}/read", method = RequestMethod.GET)
    @ResponseBody
    public String getCategoryByUuid(@PathVariable("uuid") String categoryUuid)
    {
        log.info("CategoryController.getCategoryByUuid()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        Category category=pharmacyService.getCategoryByUuid(categoryUuid);
        String response;
        if(category!=null)
        {
            response=new Gson().toJson(category);
        }
        else{
            response=new Gson().toJson("empty");
        }
        log.info("CategoryController.getCategoryByUuid() Returned data:\n"+response);
        return response;
    }

    @RequestMapping(value = "{uuid}/read/full", method = RequestMethod.GET)
    @ResponseBody
    public String getCategoryByUuidFull(@PathVariable("uuid") String categoryUuid)
    {
        log.info("CategoryController.getCategoryByUuidFull()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        Category category=pharmacyService.getCategoryByUuidFull(categoryUuid);
        String response;
        if(category!=null)
        {
            response=new Gson().toJson(category);
        }
        else{
            response=new Gson().toJson("empty");
        }
        log.info("CategoryController.getCategoryByUuidFull() Returned data:\n"+response);
        return response;
    }

    @RequestMapping(value = "all/full", method = RequestMethod.GET)
    @ResponseBody
    public String getAllCategoriesFull(@RequestParam("IncludeRetired") boolean includeRetired)
    {
        log.info("CategoryController.getAllCategoriesFull()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        List categories=pharmacyService.getAllCategoriesFull(includeRetired);
        String response;
        if(categories!=null)
        {
            response=new Gson().toJson(categories);
        }
        else {
            response=new Gson().toJson("empty");
        }

        log.info("CategoryController.getAllCategoriesFull() Returned data:\n"+response);
        return response;
    }

/*
 Creating subcategories & related data
 @Author: Eric Mwailunga
 May,2019
*/
    @RequestMapping(value = "subcategory/new", method = RequestMethod.GET)
    @ResponseBody
    public String createSubCategory(@RequestParam("CategoryName") String subCategoryName,
                                    @RequestParam("ParentCategoryUuid") String parentUuid)
    {
        log.info("CategoryController.createSubCategory()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        String response=pharmacyService.createSubCategory(subCategoryName,parentUuid);

        log.info("CategoryController.createSubCategory() Returned data:\n"+response);
        return new Gson().toJson(response);
    }

    @RequestMapping(value = "subcategory/{uuid}/read", method = RequestMethod.GET)
    @ResponseBody
    public String getSubCategoryByUuid(@PathVariable("uuid") String subCategoryUuid)
    {
        log.info("CategoryController.getSubCategoryByUuid()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        SubCategory subCategory=pharmacyService.getSubCategoryByUuid(subCategoryUuid);
        String response;
        if(subCategory!=null)
        {
            response=new Gson().toJson(subCategory);
        }
        else{
            response=new Gson().toJson("empty");
        }
        log.info("CategoryController.getSubCategoryByUuid() Returned data:\n"+response);
        return response;
    }

    @RequestMapping(value = "subcategory/{uuid}/read/full", method = RequestMethod.GET)
    @ResponseBody
    public String getFullSubCategoryByUuid(@PathVariable("uuid") String subCategoryUuid)
    {
        log.info("CategoryController.getFullSubCategoryByUuid()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        SubCategory subCategory=pharmacyService.getFullSubCategoryByUuid(subCategoryUuid);
        String response;
        if(subCategory!=null)
        {
            response=new Gson().toJson(subCategory);
        }
        else{
            response=new Gson().toJson("empty");
        }
        log.info("CategoryController.getSubCategoryByUuid() Returned data:\n"+response);
        return response;
    }
    @RequestMapping(value = "subcategory/name", method = RequestMethod.GET)
    @ResponseBody
    public String getSubCategoryByName(@RequestParam("SubCategoryName") String subCategoryName)
    {
        log.info("CategoryController.getSubCategoryByName()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        SubCategory subCategory=pharmacyService.getSubCategoryByName(subCategoryName);
        String response;
        if(subCategory!=null)
        {
            response=new Gson().toJson(subCategory);
        }
        else{
            response=new Gson().toJson("empty");
        }
        log.info("CategoryController.getSubCategoryBySubName() Returned data:\n"+response);
        return response;
    }

    @RequestMapping(value = "subcategory/name/search", method = RequestMethod.GET)
    @ResponseBody
    public String getSubCategoryBySubName(@RequestParam("SubCategoryName") String subCategorySubName)
    {
        log.info("CategoryController.getSubCategoryByName()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        List subCategories=pharmacyService.getSubCategoryBySubName(subCategorySubName);

        String response;
        if(subCategories!=null)
        {
            response=new Gson().toJson(subCategories);
        }
        else{
            response=new Gson().toJson("empty");
        }
        log.info("CategoryController.getSubCategoryBySubName() Returned data:\n"+response);
        return response;
    }

    @RequestMapping(value = "subcategory/all", method = RequestMethod.GET)
    @ResponseBody
    public String getAllSubCategories(@RequestParam("IncludeRetired") boolean includeRetired)
    {
        log.info("CategoryController.getAllSubCategories()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        List subCategories=pharmacyService.getAllSubCategories(includeRetired);
        String response;
        if(subCategories!=null)
        {
            response=new Gson().toJson(subCategories);
        }
        else{
            response=new Gson().toJson("empty");
        }
        log.info("CategoryController.getAllSubCategories() Returned data:\n"+response);
       return response;
    }

    @RequestMapping(value = "subcategory/all/full", method = RequestMethod.GET)
    @ResponseBody
    public String getAllSubCategoriesFull(@RequestParam("IncludeRetired") boolean includeRetired)
    {
        log.info("CategoryController.getAllSubCategoriesFull()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        List<SubCategory> subCategoriesFull=pharmacyService.getAllSubCategoriesFull(includeRetired);
        String response;
        if(subCategoriesFull!=null)
        {
            response=new Gson().toJson(subCategoriesFull);
        }
        else{
            response=new Gson().toJson("empty");
        }
        log.info("CategoryController.getAllSubCategoriesFull() Returned data:\n"+response);
        return response;
    }

    @RequestMapping(value = "subcategory/{uuid}/update", method = RequestMethod.GET)
    @ResponseBody
    public String updateSubCategoryByUuid(@PathVariable("uuid") String subCategoryUuid,
                                       @RequestParam("SubCategoryName") String subCategoryName,
                                       @RequestParam("ParentUuid") String parentUuid,
                                       @RequestParam("retired") boolean retired)
    {
        log.info("CategoryController.updateSubCategoryByUuid()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        String response=pharmacyService.updateSubCategoryByUuid(subCategoryUuid,subCategoryName,parentUuid,retired);

        log.info("CategoryController.updateSubCategoryByUuid() Returned data:\n"+response);
        return new Gson().toJson(response);
    }

    @RequestMapping(value = "subcategory/{uuid}/delete", method = RequestMethod.GET)
    @ResponseBody
    public String deleteSubCategoryByUuid(@PathVariable("uuid") String subCategoryUuid)
    {
        log.info("CategoryController.deleteSubCategoryByUuid()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        String response=pharmacyService.deleteSubCategoryByUuid(subCategoryUuid);

        log.info("CategoryController.deleteSubCategoryByUuid() Returned data:\n"+response);
        return new Gson().toJson(response);
    }


/*
 Creating subcategories attribute types & related data
 @Author: Eric Mwailunga
 May,2019
*/

    @RequestMapping(value = "subcategory/{uuid}/attributetype/new", method = RequestMethod.GET)
    @ResponseBody
    public String createSubCategoryAttributeType(@PathVariable("uuid") String subCategoryUuid,
                                                 @RequestParam("AttributeTypeName") String attributeTypeName)
    {
        log.info("CategoryController.createSubCategoryAttributeType()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        String response=pharmacyService.createSubCategoryAttributeType(subCategoryUuid,attributeTypeName);

        log.info("CategoryController.createSubCategoryAttributeType() Returned data:\n"+response);
        return new Gson().toJson(response);
    }

    //It receives the array of attribute types for specified subcategory
    @RequestMapping(value = "subcategory/{uuid}/attributetypes/new", method = RequestMethod.GET)
    @ResponseBody
    public String createSubCategoryAttributeTypes(@PathVariable("uuid") String subCategoryUuid,
                                                 @RequestParam("AttributeTypeNames") String attributeTypeNames)
    {
        log.info("CategoryController.createSubCategoryAttributeTypes()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        String response=pharmacyService.createSubCategoryAttributeTypes(subCategoryUuid,attributeTypeNames);

        log.info("CategoryController.createSubCategoryAttributeTypes() Returned data:\n"+response);
        return new Gson().toJson(response);
    }

    @RequestMapping(value = "subcategory/{sub_cat_uuid}/attributetypes", method = RequestMethod.GET)
    @ResponseBody
    public String getAttributeTypesBySubCategoryUuid(@PathVariable("sub_cat_uuid") String subCategoryUuid)
    {
        log.info("CategoryController.getSubCategoryAttributeTypeBySubCategoryUuid()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        List attributeTypes=pharmacyService.getAttributeTypesBySubCategoryUuid(subCategoryUuid);
        String response;
        if(attributeTypes!=null)
        {
            response=new Gson().toJson(attributeTypes);
        }
        else{
            response=new Gson().toJson("empty");
        }
        log.info("CategoryController.getAttributeTypesBySubCategoryUuid() Returned data:\n"+response);
        return response;
    }

    @RequestMapping(value = "subcategory/{sub_cat_uuid}/attributetype/{att_type_uuid}/update", method = RequestMethod.GET)
    @ResponseBody
    public String updateSubCategoryAttributeType(@PathVariable("sub_cat_uuid") String subCategoryUuid,
                                                 @PathVariable("att_type_uuid") String attributeTypeUuid,
                                                 @RequestParam("AttributeTypeName") String attributeTypeName,
                                                 @RequestParam("retired") boolean retired)
    {
        log.info("CategoryController.updateSubCategoryAttributeType()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        String response=pharmacyService.updateSubCategoryAttributeType(attributeTypeUuid,attributeTypeName,subCategoryUuid,retired);
        if(response!=null)
        {
            response=new Gson().toJson(response);
        }
        else{
            response=new Gson().toJson("empty");
        }
        log.info("CategoryController.updateSubCategoryAttributeType() Returned data:\n"+response);
        return response;
    }


    /*
    @RequestMapping(value = "subcategory/all/attributetypes", method = RequestMethod.GET)
    @ResponseBody
    public String getAllSubCategoriesWithAttributeTypes(@RequestParam("IncludeRetired") boolean includeRetired)
    {
        log.info("CategoryController.getAllSubCategoriesWithAttributeTypes()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        List<SubCategoryAttribute> subCatWithTheirAttributeTypes=pharmacyService.getAllSubCategoriesWithAttributeTypes(includeRetired);
        String response;
        if(subCatWithTheirAttributeTypes!=null)
        {
            response=new Gson().toJson(subCatWithTheirAttributeTypes);
        }
        else{
            response=new Gson().toJson("empty");
        }
        log.info("CategoryController.getAllSubCategoriesWithAttributeTypes() Returned data:\n"+response);
        return response;
    }
    */

    @RequestMapping(value = "{uuid}/subcategory/all/attributetypes", method = RequestMethod.GET)
    @ResponseBody
    public String getSubCategoryByCategoryUuidWithFullAttributeTypes(@PathVariable("uuid") String categoryUuid,
                                                        @RequestParam("IncludeRetired") boolean includeRetired)
    {
        log.info("CategoryController.getSubCategoryByCategoryUuidWithFullAttributeTypes()...");
        PharmacyService pharmacyService= Context.getService(PharmacyService.class);
        List<SubCategoryAttribute> subCatsWithTheirAttributeTypes=pharmacyService
                .getCategoryByUuidWithFullSubCategoryAttributeTypes(categoryUuid,includeRetired);
        String response;
        if(subCatsWithTheirAttributeTypes!=null)
        {
            response=new Gson().toJson(subCatsWithTheirAttributeTypes);
        }
        else{
            response=new Gson().toJson("empty");
        }
        log.info("CategoryController.getCategoryByUuidWithFullSubCategoryAttributeTypes() Returned data:\n"+response);
        return response;
    }
}
