package com.rzk.controller;


import com.rzk.utils.FastDFSUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @PackageName : com.rzk.controller
 * @FileName : FastDFSUtilsController
 * @Description :
 * @Author : rzk
 * @CreateTime : 1/6/2022 上午2:41
 * @Version : v1.0
 */
@RestController
@RequestMapping("/fastdfsUtils")
@Api(value = "文件有关业务的接口", tags = {"文件业务controller"})
public class FastDFSUtilsController {
    @Autowired
    private FastDFSUtil fastDFSUtil;

    @PostMapping("/upload")
    @ApiOperation(value = "文件上传",notes = "文件上传接口")
    @ApiImplicitParam(name = "file", value = "文件", required = true, dataType = "File", paramType = "add")
    public String upload(MultipartFile file) throws IOException {
        String uploadFile = fastDFSUtil.uploadFile(file);
        System.out.println("fullPath = " + uploadFile);
        //group1/M00/00/00/wKgAkGIfF2KAG7qSAAB-jaFjG-Q678.jpg
        return uploadFile;
    }

    @GetMapping("/download")
    @ApiOperation(value = "文件下载",notes = "文件下载接口")
    @ApiImplicitParam(name = "fileUrl", value = "文件地址", required = true, dataType = "File", paramType = "download")
    public void downloadFile(String fileUrl, HttpServletResponse response)
            throws IOException {
        fastDFSUtil.download(fileUrl, null, response);
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "文件删除",notes = "文件删除接口")
    public void delete(String fileUrl) {
        fastDFSUtil.deleteFile(fileUrl);
    }
}
