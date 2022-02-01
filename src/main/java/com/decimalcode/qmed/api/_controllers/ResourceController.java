package com.decimalcode.qmed.api._controllers;

import com.decimalcode.qmed.api.counsel.service.CounselModel;
import com.decimalcode.qmed.api.counsel.service.CounselService;
import com.decimalcode.qmed.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ResourceController {

    private final CounselService counselService;

    @Autowired
    public ResourceController(CounselService counselService) {
        this.counselService = counselService;
    }

    @GetMapping("/counsel")
    public ApiResponse<CounselModel> getConsultation(@RequestParam("diagnosis") String diagnosis) {
        return counselService.findDiagnosis(diagnosis);
    }

}
