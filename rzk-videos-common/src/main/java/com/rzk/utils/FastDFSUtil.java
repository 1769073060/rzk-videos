package com.rzk.utils;

import com.github.tobato.fastdfs.domain.conn.FdfsWebServer;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 * @PackageName : com.rzk
 * @FileName : FastDFS
 * @Description : FastDFS工具类
 * @Author : rzk
 * @CreateTime : 2022/5/31 16:38
 * @Version : 1.0.0
 */
@Component
public class FastDFSUtil {
    private static final String DEFAULT_CHARSET = "UTF-8";

    @Autowired
    private FastFileStorageClient fastFileStorageClient;
    @Autowired
    private FdfsWebServer fdfsWebServer;


    /**
     * 上传文件
     * @param file 文件对象
     * @return 文件访问地址
     * @throws IOException
     */
    public String uploadFile(MultipartFile file) throws IOException {
        StorePath storePath = fastFileStorageClient.uploadFile(
                file.getInputStream(),
                file.getSize(),
                FilenameUtils.getExtension(file.getOriginalFilename()),
                null);
        return getResAccessShortUrl(storePath);
    }


    /**
     * 将一段字符串生成一个文件上传
     * @param content 文件内容
     * @param fileExtension
     * @return
     */
    public String uploadFile(String content, String fileExtension) {
        byte[] buff = content.getBytes(Charset.forName("UTF-8"));
        ByteArrayInputStream stream = new ByteArrayInputStream(buff);
        StorePath storePath = fastFileStorageClient.uploadFile(stream,buff.length, fileExtension,null);
        return getResAccessUrl(storePath);
    }


    /**
     * 封装图片完整URL地址
     * @param storePath
     * @return
     */
    private String getResAccessUrl(StorePath storePath){
        String filePath = fdfsWebServer.getWebServerUrl()+storePath.getFullPath();
        return filePath;
    }

    /**
     * 封装图片后半段URL地址
     * @param storePath
     * @return
     */
    private String getResAccessShortUrl(StorePath storePath){
        String filePath = "/"+storePath.getFullPath();
        return filePath;
    }

    /**
     * 删除文件
     * @param fileUrl 文件路径
     */
    public void deleteFile(String fileUrl){
        if (StringUtils.isEmpty(fileUrl)){
            return;
        }
        try {
            StorePath storePath = com.rzk.utils.StorePath.parseFromUrl(fileUrl);
            System.out.println("storePath{}"+storePath);
            fastFileStorageClient.deleteFile(storePath.getGroup(),storePath.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据组和路径删除文件
     * @param group
     * @param path
     */
    public void deleteFile(String group,String path){
        fastFileStorageClient.deleteFile(group, path);
    }


    /**
     * 下载文件
     *
     * @param fileUrl 文件URL
     * @return 文件字节
     * @throws IOException
     */
    public byte[] downloadFile(String fileUrl) throws IOException {
        String group = fileUrl.substring(0, fileUrl.indexOf("/"));
        String path = fileUrl.substring(fileUrl.indexOf("/") + 1);
        DownloadByteArray downloadByteArray = new DownloadByteArray();
        byte[] bytes = fastFileStorageClient.downloadFile(group, path, downloadByteArray);
        return bytes;
    }

    public void download(String path, String fileName, HttpServletResponse response){
        //获取文件
        StorePath storePath = com.rzk.utils.StorePath.parseFromUrl(path);
        if (StringUtils.isBlank(fileName)){
            fileName = FilenameUtils.getName(storePath.getPath());
        }
        byte[] bytes = fastFileStorageClient.downloadFile(storePath.getGroup(), storePath.getPath(), new DownloadByteArray());
        response.reset();
        ServletOutputStream outputStream = null;

        try {
            response.setHeader("Content-Disposition","attachment;filename=" + URLEncoder.encode(fileName,DEFAULT_CHARSET));
            response.setCharacterEncoding(DEFAULT_CHARSET);

            //设置前置下载不打开
            //response.setContentType("application/force-download");

            outputStream = response.getOutputStream();
            outputStream.write(bytes);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
