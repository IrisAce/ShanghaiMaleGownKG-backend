package com.shanghaimale.gownkg.controller;

import com.shanghaimale.gownkg.entity.Multimedia;
import com.shanghaimale.gownkg.service.MultimediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/multimedia")
public class MultimediaController {

    @Autowired
    private MultimediaService multimediaService;

    @PostMapping("/upload")
    public Multimedia uploadResource(@RequestParam("file") MultipartFile file,
                                     @RequestParam("resourceType") String resourceType,
                                     @RequestParam("typeCode") Integer typeCode) throws IOException, InvalidKeyException, NoSuchAlgorithmException, io.minio.errors.ErrorResponseException, io.minio.errors.InsufficientDataException, io.minio.errors.InternalException, io.minio.errors.InvalidResponseException, io.minio.errors.ServerException, io.minio.errors.XmlParserException {
        return multimediaService.uploadResource(file, resourceType, typeCode);
    }

    @GetMapping("/getByIds")
    public List<Multimedia> getResourcesByIds(@RequestParam("param") String param) {
        return multimediaService.getResourcesByIds(param);
    }

    @GetMapping("/getByPeriod")
    public List<Multimedia> getResourcesByPeriod(@RequestParam("param") String param) {
        return multimediaService.getResourcesByIds(param);
    }
}