package com.shanghaimale.gownkg.controller;

import com.shanghaimale.gownkg.entity.DressStyleInfo;
import com.shanghaimale.gownkg.service.DressStyleInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/dress-style-info")
public class DressStyleInfoController {

    @Autowired
    private DressStyleInfoService dressStyleInfoService;

    @PostMapping
    public DressStyleInfo saveDressStyleInfo(@ModelAttribute DressStyleInfo dressStyleInfo,
                                             @RequestParam Integer dressId,
                                             @RequestParam(required = false) MultipartFile image,
                                             @RequestParam(required = false) MultipartFile threeDModel,
                                             @RequestParam(required = false) MultipartFile video) throws IOException {
        return dressStyleInfoService.saveDressStyleInfo(dressStyleInfo, dressId, image, threeDModel, video);
    }

    @GetMapping
    public List<DressStyleInfo> getAllDressStyleInfos() {
        return dressStyleInfoService.getAllDressStyleInfos();
    }

    @GetMapping("/{styleId}")
    public DressStyleInfo getDressStyleInfoById(@PathVariable Integer styleId) {
        return dressStyleInfoService.getDressStyleInfoById(styleId);
    }

    @DeleteMapping("/{styleId}")
    public void deleteDressStyleInfo(@PathVariable Integer styleId) {
        dressStyleInfoService.deleteDressStyleInfo(styleId);
    }
}