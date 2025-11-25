package com.shanghaimale.gownkg.controller;


import com.shanghaimale.gownkg.entity.DressMatchingInfo;
import com.shanghaimale.gownkg.service.DressMatchingInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/dress-matching-info")
public class DressMatchingInfoController {

    @Autowired
    private DressMatchingInfoService dressMatchingInfoService;

    @PostMapping
    public DressMatchingInfo saveDressMatchingInfo(@ModelAttribute DressMatchingInfo dressMatchingInfo,
                                                   @RequestParam Integer dressId,
                                                   @RequestParam(required = false) MultipartFile image,
                                                   @RequestParam(required = false) MultipartFile threeDModel,
                                                   @RequestParam(required = false) MultipartFile video) throws IOException {
        return dressMatchingInfoService.saveDressMatchingInfo(dressMatchingInfo, dressId, image, threeDModel, video);
    }

    @GetMapping
    public List<DressMatchingInfo> getAllDressMatchingInfos() {
        return dressMatchingInfoService.getAllDressMatchingInfos();
    }

    @GetMapping("/{matchingId}")
    public DressMatchingInfo getDressMatchingInfoById(@PathVariable Integer matchingId) {
        return dressMatchingInfoService.getDressMatchingInfoById(matchingId);
    }

    @DeleteMapping("/{matchingId}")
    public void deleteDressMatchingInfo(@PathVariable Integer matchingId) {
        dressMatchingInfoService.deleteDressMatchingInfo(matchingId);
    }
}