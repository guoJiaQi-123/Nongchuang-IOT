package com.tyut.shopping_file_service.service;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.tyut.shopping_common.result.BusException;
import com.tyut.shopping_common.result.CodeEnum;
import com.tyut.shopping_common.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayInputStream;

/**
 * @version v1.0
 * @author OldGj 2024/6/6
 * @apiNote 文件系统业务层实现类
 */
@DubboService
@Slf4j
public class FileServiceImpl implements FileService {

    @Autowired
    private FastFileStorageClient fastClient;
    @Value("${fdfs.fileUrl}")
    private String fileUrl;

    /**
     * 上传文件
     * @param fileBytes 图片文件转成的字节数组
     * @param fileName 文件名
     * @return 上传后的文件访问路径
     */
    @Override
    public String uploadFile(byte[] fileBytes, String fileName) {
        if (fileBytes.length > 0) {
            try {
                // 1. 将文件字节数组转换为输入流
                ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes);
                // 2. 获取文件后缀名
                String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                // 3. 上传文件
                StorePath storePath = fastClient.uploadFile(inputStream, inputStream.available(), suffix, null);
                // 4. 返回文件路径
                String imageUrl = fileUrl + "/" + storePath.getFullPath();
                log.info("文件名为 {} 的存储文件路径：{}", fileName, imageUrl);
                return imageUrl;
            } catch (Exception e) {
                throw new BusException(CodeEnum.FILE_UPLOAD_ERROR);
            }
        } else {
            throw new BusException(CodeEnum.FILE_UPLOAD_ERROR);
        }
    }


    /**
     * 删除文件
     * @param filePath 文件路径
     */
    @Override
    public void delete(String filePath) {
        fastClient.deleteFile(filePath);
    }
}
