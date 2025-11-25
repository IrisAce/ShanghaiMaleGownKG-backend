package com.shanghaimale.gownkg.service;

import com.shanghaimale.gownkg.entity.DressBaseInfo;
import com.shanghaimale.gownkg.entity.DressStyleInfo;
import com.shanghaimale.gownkg.repository.DressBaseInfoRepository;
import com.shanghaimale.gownkg.repository.DressStyleInfoRepository;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * DressStyleInfoService 类提供了与礼服样式信息相关的服务方法。
 * <p>
 * 该类主要负责处理礼服样式信息的保存、查询和删除操作，并支持上传图片、三维模型和视频到 MinIO 存储。
 */
@Service
public class DressStyleInfoService {

    @Autowired
    private DressStyleInfoRepository dressStyleInfoRepository;

    @Autowired
    private DressBaseInfoRepository dressBaseInfoRepository;

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    /**
     * 保存礼服样式信息。
     * <p>
     * 该方法用于保存礼服样式信息，包括上传相关的图片、三维模型和视频到 MinIO，并在数据库中保存这些信息。
     *
     * @param dressStyleInfo 礼服样式信息对象，包含需要保存的样式信息。
     * @param dressId        礼服基础信息的 ID，用于关联礼服样式信息。
     * @param image          礼服图片文件，可选参数。
     * @param threeDModel    礼服三维模型文件，可选参数。
     * @param video          礼服视频文件，可选参数。
     * @return 保存后的礼服样式信息对象，如果找不到对应的礼服基础信息则返回 null。
     * @throws IOException 当文件读取发生错误时抛出此异常。
     */
    public DressStyleInfo saveDressStyleInfo(DressStyleInfo dressStyleInfo, Integer dressId, MultipartFile image, MultipartFile threeDModel, MultipartFile video) throws IOException {
        Optional<DressBaseInfo> dressBaseInfoOptional = dressBaseInfoRepository.findById(dressId);
        if (dressBaseInfoOptional.isPresent()) {
            DressBaseInfo dressBaseInfo = dressBaseInfoOptional.get();
            dressStyleInfo.setDressBaseInfo(dressBaseInfo);

            // 如果提供了图片文件，则上传到 MinIO。
            if (image != null) {
                try {
                    String imageObjectName = UUID.randomUUID().toString() + "-" + image.getOriginalFilename();
                    minioClient.putObject(PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(imageObjectName)
                            .stream(image.getInputStream(), image.getSize(), -1)
                            .contentType(image.getContentType())
                            .build());
                    dressStyleInfo.setImage(imageObjectName);
                } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                         InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException e) {
                    System.err.println("上传图片到 MinIO 时发生错误: " + e.getMessage());
                }
            }

            // 如果提供了三维模型文件，则上传到 MinIO。
            if (threeDModel != null) {
                try {
                    String threeDModelObjectName = UUID.randomUUID().toString() + "-" + threeDModel.getOriginalFilename();
                    minioClient.putObject(PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(threeDModelObjectName)
                            .stream(threeDModel.getInputStream(), threeDModel.getSize(), -1)
                            .contentType(threeDModel.getContentType())
                            .build());
                    dressStyleInfo.setThreeDModel(threeDModelObjectName);
                } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                         InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException e) {
                    System.err.println("上传三维模型到 MinIO 时发生错误: " + e.getMessage());
                }
            }

            // 如果提供了视频文件，则上传到 MinIO。
            if (video != null) {
                try {
                    String videoObjectName = UUID.randomUUID().toString() + "-" + video.getOriginalFilename();
                    minioClient.putObject(PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(videoObjectName)
                            .stream(video.getInputStream(), video.getSize(), -1)
                            .contentType(video.getContentType())
                            .build());
                    dressStyleInfo.setVideo(videoObjectName);
                } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                         InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException e) {
                    System.err.println("上传视频到 MinIO 时发生错误: " + e.getMessage());
                }
            }

            // 将礼服样式信息保存到数据库。
            return dressStyleInfoRepository.save(dressStyleInfo);
        }
        // 如果未找到对应的礼服基础信息，返回 null。
        return null;
    }

    /**
     * 获取所有礼服样式信息。
     *
     * @return 包含所有礼服样式信息的列表。
     */
    public List<DressStyleInfo> getAllDressStyleInfos() {
        return dressStyleInfoRepository.findAll();
    }

    /**
     * 根据 ID 获取礼服样式信息。
     *
     * @param styleId 礼服样式信息的 ID。
     * @return 对应的礼服样式信息对象，如果未找到则返回 null。
     */
    public DressStyleInfo getDressStyleInfoById(Integer styleId) {
        return dressStyleInfoRepository.findById(styleId).orElse(null);
    }

    /**
     * 根据 ID 删除礼服样式信息。
     *
     * @param styleId 礼服样式信息的 ID。
     */
    public void deleteDressStyleInfo(Integer styleId) {
        dressStyleInfoRepository.deleteById(styleId);
    }
}
