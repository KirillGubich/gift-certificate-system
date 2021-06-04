package com.epam.esm.web.controller;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.maintenance.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tags")
public class TagController {

    private CommonService<TagDto> service;

    @Autowired
    public void setService(CommonService<TagDto> service) {
        this.service = service;
    }

    @GetMapping
    public String readAllTags() {
        service.readAll();
        return "test";
    }
}
