package com.shanghaimale.gownkg.service;

import com.shanghaimale.gownkg.entity.BelongingPeriod;
import com.shanghaimale.gownkg.entity.DressBaseInfo;
import com.shanghaimale.gownkg.repository.DressBaseInfoRepository;
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
import java.util.UUID;

/**
 * DressBaseInfoService 类提供了与礼服基础信息相关的服务方法。
 * <p>
 * 该类主要负责处理礼服基础信息的保存、查询和删除操作，并支持上传图片、三维模型和视频到 MinIO 存储。
 */
@Service
public class DressBaseInfoService {

    @Autowired
    private DressBaseInfoRepository dressBaseInfoRepository;

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    /**
     * 保存礼服基础信息。
     * <p>
     * 该方法用于保存礼服基础信息，包括上传相关的图片、三维模型和视频到 MinIO，并在数据库中保存这些信息。
     *
     * @param dressBaseInfo 礼服基础信息对象，包含需要保存的基础信息。
     * @param image         礼服图片文件，可选参数。
     * @param threeDModel   礼服三维模型文件，可选参数。
     * @param video         礼服视频文件，可选参数。
     * @return 保存后的礼服基础信息对象。
     * @throws IOException 当文件读取发生错误时抛出此异常。
     */
    public DressBaseInfo saveDressBaseInfo(DressBaseInfo dressBaseInfo, MultipartFile image, MultipartFile threeDModel, MultipartFile video) throws IOException {
        if (image != null) {
            try {
                String imageObjectName = UUID.randomUUID().toString() + "-" + image.getOriginalFilename();
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(imageObjectName)
                        .stream(image.getInputStream(), image.getSize(), -1)
                        .contentType(image.getContentType())
                        .build());
                dressBaseInfo.setImage(imageObjectName);
            } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                     InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException e) {
                // 处理 MinIO 相关异常
                System.err.println("上传图片到 MinIO 时发生错误: " + e.getMessage());
            }
        }
        if (threeDModel != null) {
            try {
                String threeDModelObjectName = UUID.randomUUID().toString() + "-" + threeDModel.getOriginalFilename();
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(threeDModelObjectName)
                        .stream(threeDModel.getInputStream(), threeDModel.getSize(), -1)
                        .contentType(threeDModel.getContentType())
                        .build());
                dressBaseInfo.setThreeDModel(threeDModelObjectName);
            } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                     InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException e) {
                // 处理 MinIO 相关异常
                System.err.println("上传三维模型到 MinIO 时发生错误: " + e.getMessage());
            }
        }
        if (video != null) {
            try {
                String videoObjectName = UUID.randomUUID().toString() + "-" + video.getOriginalFilename();
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(videoObjectName)
                        .stream(video.getInputStream(), video.getSize(), -1)
                        .contentType(video.getContentType())
                        .build());
                dressBaseInfo.setVideo(videoObjectName);
            } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                     InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException e) {
                // 处理 MinIO 相关异常
                System.err.println("上传视频到 MinIO 时发生错误: " + e.getMessage());
            }
        }
        return dressBaseInfoRepository.save(dressBaseInfo);
    }

    /**
     * 获取所有礼服基础信息。
     *
     * @return 包含所有礼服基础信息的列表。
     */
    public List<DressBaseInfo> getAllDressBaseInfos() {
        return dressBaseInfoRepository.findAll();
    }

    /**
     * 根据所属年代查询礼服信息。
     *
     * @param period 所属年代的枚举值。
     * @return 匹配所属年代的礼服基础信息列表。
     */
    public List<DressBaseInfo> getDressByPeriod(BelongingPeriod period) {
        return dressBaseInfoRepository.findByBelongingPeriod(period);
    }

    /**
     * 根据 ID 获取礼服基础信息。
     *
     * @param dressId 礼服基础信息的 ID。
     * @return 对应的礼服基础信息对象，如果未找到则返回 null。
     */
    public DressBaseInfo getDressBaseInfoById(Integer dressId) {
        return dressBaseInfoRepository.findById(dressId).orElse(null);
    }

    /**
     * 根据 ID 删除礼服基础信息。
     *
     * @param dressId 礼服基础信息的 ID。
     */
    public void deleteDressBaseInfo(Integer dressId) {
        dressBaseInfoRepository.deleteById(dressId);
    }
}
