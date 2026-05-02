package es.mqm.webapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.mqm.webapp.dto.AdminChartsDTO;
import es.mqm.webapp.service.ChartsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1/charts")
public class ChartsRestController {

    @Autowired
    private ChartsService chartsService;

    @GetMapping("/")
    public AdminChartsDTO getAdminChartsInfo() {
        return chartsService.getAdminChartsData();
    }
    
}
