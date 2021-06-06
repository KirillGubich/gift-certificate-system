package com.epam.esm.web.controller;

import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.maintenance.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/certificates")
public class CertificateController {

    private CommonService<GiftCertificateDto> service;

    @Autowired
    public void setService(CommonService<GiftCertificateDto> service) {
        this.service = service;
    }

    @GetMapping
    public String getAllCertificates() {
        service.readAll();
        return "test";
    }

}
