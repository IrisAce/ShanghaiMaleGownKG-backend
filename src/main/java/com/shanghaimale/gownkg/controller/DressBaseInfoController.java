package com.shanghaimale.gownkg.controller;

import com.shanghaimale.gownkg.entity.BelongingPeriod;
import com.shanghaimale.gownkg.entity.DressBaseInfo;
import com.shanghaimale.gownkg.service.DressBaseInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/dress-base-info")
public class DressBaseInfoController {
    @Autowired
    private DressBaseInfoService dressBaseInfoService;

    @PostMapping
    public DressBaseInfo saveDressBaseInfo(@ModelAttribute DressBaseInfo dressBaseInfo,
                                           @RequestParam(required = false) MultipartFile image,
                                           @RequestParam(required = false) MultipartFile threeDModel,
                                           @RequestParam(required = false) MultipartFile video) throws IOException {
//        log.info(image.getName());
        return dressBaseInfoService.saveDressBaseInfo(dressBaseInfo, image, threeDModel, video);
    }

    @GetMapping
    public List<DressBaseInfo> getAllDressBaseInfos() {
        return dressBaseInfoService.getAllDressBaseInfos();
    }

    @GetMapping("/period")
    public List<DressBaseInfo> getDressByPeriod(@RequestParam("period") BelongingPeriod period) {
        return dressBaseInfoService.getDressByPeriod(period);
    }

    @GetMapping("/{dressId}")
    public DressBaseInfo getDressBaseInfoById(@PathVariable Integer dressId) {
        return dressBaseInfoService.getDressBaseInfoById(dressId);
    }

    @DeleteMapping("/{dressId}")
    public void deleteDressBaseInfo(@PathVariable Integer dressId) {
        dressBaseInfoService.deleteDressBaseInfo(dressId);
    }
}