package com.tyut.shopping_common.service;

/**
 * @version v1.0
 * @apiNote 文件系统业务层接口
 * @author OldGj 2024/6/6
 */
public interface FileService {


    /**
     * 上传文件
     * @param fileBytes 图片文件转成的字节数组
     * @param fileName 文件名
     * @return 上传后的文件访问路径
     */
    String uploadFile(byte[] fileBytes, String fileName);

    /**
     * 删除文件
     * @param filePath 文件路径
     */
    void delete(String filePath);

}
