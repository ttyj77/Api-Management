package com.ipa.openapi_inzent.controller;

import com.google.gson.JsonObject;
import com.ipa.openapi_inzent.model.ApiDTO;
import com.ipa.openapi_inzent.model.ApiDetailsDTO;
import com.ipa.openapi_inzent.service.ApiDetailsService;
import com.ipa.openapi_inzent.service.ApiService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@Controller
public class ApiController {
    ApiService apiService;
    ApiDetailsService apiDetailsService;

    @Autowired
    public ApiController(ApiService apiService, ApiDetailsService apiDetailsService) {
        this.apiService = apiService;
        this.apiDetailsService = apiDetailsService;
    }

    @GetMapping("")
    public String apis(Model model) {
        model.addAttribute("list", apiService.selectAll());
        return "/apis/index";
    }

    @GetMapping("/selectOne")
    @ResponseBody
    public JsonObject apiOne(int apiId) {
        JsonObject object = new JsonObject();
        ApiDTO apiDTO = apiService.selectOne(apiId);

        object.addProperty("apiId", apiId);
        object.addProperty("apiName", apiDTO.getName());
        object.addProperty("apiContext", apiDTO.getContext());
        object.addProperty("apiExplanation", apiDTO.getExplanation());
        object.addProperty("apiDisclosure", apiDTO.isDisclosure());

        return object;
    }

    @GetMapping("/details/{id}")
    public String details(Model model, @PathVariable int id, HttpServletResponse response) {

        System.out.println(response.getStatus()); //200

        ApiDTO a = apiService.selectOne(id);
        List<ApiDetailsDTO> ad = apiDetailsService.selectAll(id);
//        System.out.println("ad.get(0).getOperationId() = " + ad.get(0).getOperationId());
        System.out.println("ad = " + ad);
//        model.addAttribute("operationId",ad.get(0).getOperationId());
        model.addAttribute("api", a);
        model.addAttribute("apiList", ad);

        return "/apis/details";
    }

    @GetMapping("/delete/{id}")
    public String apiDelete(@PathVariable int id) {
        apiService.delete(id);
        return "redirect:/api";
    }

    @PostMapping("/insert")
    public String insert(ApiDTO apiDTO) {
        System.out.println("apiDTO = " + apiDTO);

        apiService.insert(apiDTO);
        return "redirect:/api";
    }

    @GetMapping("/trash")
    public String apiTrash() {
        return "/apis/trash";
    }

    @GetMapping("/resourceModal")
    public String resourceModal() {
        return "/apis/resourceModal";
    }
}
