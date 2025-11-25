package com.shanghaimale.gownkg.service;

import com.shanghaimale.gownkg.entity.Multimedia;
import com.shanghaimale.gownkg.repository.MultimediaRepository;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * MultimediaService 类提供了多媒体资源的上传、查询等服务功能。
 * <p>
 * 该类主要负责将多媒体文件上传到 MinIO 存储，并保存相关元数据到数据库中。
 * 同时支持根据 ID 列表或所属时期查询多媒体资源。
 */
@Service
public class MultimediaService {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MultimediaRepository multimediaRepository;

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.bucket-name}")
    private String bucketName;

    /**
     * 上传多媒体资源到 MinIO 并保存元数据到数据库。
     * <p>
     * 该方法将文件上传到 MinIO 存储，并生成唯一的资源名称和访问链接，
     * 然后将资源的相关信息保存到数据库中。
     *
     * @param file         要上传的多媒体文件。
     * @param resourceType 资源类型，例如 "image", "video" 等。
     * @param typeCode     资源的类型代码，用于分类。
     * @return 返回保存后的 Multimedia 对象。
     */
    public Multimedia uploadResource(MultipartFile file, String resourceType, Integer typeCode)
            throws IOException, InvalidKeyException, NoSuchAlgorithmException,
            io.minio.errors.ErrorResponseException, io.minio.errors.InsufficientDataException,
            io.minio.errors.InternalException, io.minio.errors.InvalidResponseException,
            io.minio.errors.ServerException, io.minio.errors.XmlParserException {
        String resourceName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(resourceName)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build());

        String resourceLink = endpoint + "/" + bucketName + "/" + resourceName; // 假设 endpoint 是 MinIO 服务器的访问地址

        Multimedia multimedia = new Multimedia();
        multimedia.setResourceName(resourceName);
        multimedia.setResourceLink(resourceLink);
        multimedia.setResourceType(resourceType);
        multimedia.setTypeCode(typeCode);

        return multimediaRepository.save(multimedia);
    }

    /**
     * 根据 ID 列表查询多媒体资源。
     * <p>
     * 该方法接收一个以分号分隔的字符串形式的 ID 列表，将其转换为整数列表，
     * 并从数据库中查询对应的多媒体资源。
     *
     * @param param 包含多个 ID 的字符串，ID 之间以分号分隔。
     * @return 返回与 ID 列表匹配的多媒体资源列表。
     */
    public List<Multimedia> getResourcesByIds(String param) {
        List<Integer> ids = new ArrayList<>();
        String[] idStrings = param.split(";");
        for (String idString : idStrings) {
            try {
                ids.add(Integer.parseInt(idString));
            } catch (NumberFormatException e) {
                // 处理无效的 ID 格式，可根据实际情况记录日志或忽略
            }
        }
        return multimediaRepository.findByIdIn(ids);
    }

    /**
     * 根据所属时期获取多媒体资源。
     * <p>
     * 该方法目前未实现完整逻辑，仅调用了 {@link #getResourcesByIds(String)} 方法。
     * 需要根据实际需求完善此方法的实现。
     *
     * @param param 包含多个时期的字符串，具体格式需根据业务需求定义。
     * @return 返回与时期相关的多媒体资源列表。
     */
    public List<Multimedia> getResourcesByPeriod(String param) {
        String mediaIds = "";

        List<Integer> ids = new ArrayList<>();
        String[] idStrings = param.split(";");
        for (String idString : idStrings) {
            try {
                ids.add(Integer.parseInt(idString));
            } catch (NumberFormatException e) {
                // 处理无效的 ID 格式，可根据实际情况记录日志或忽略
            }
        }
        return getResourcesByIds(mediaIds);
    }
}
