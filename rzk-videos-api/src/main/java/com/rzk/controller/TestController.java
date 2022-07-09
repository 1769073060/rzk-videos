package com.rzk.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @PackageName : com.rzk.controller
 * @FileName : TestController
 * @Description :
 * @Author : rzk
 * @CreateTime : 16/6/2022 下午7:43
 * @Version : v1.0
 */
@RestController
public class TestController {
    @RequestMapping(value = "/downloadAttachment", method = RequestMethod.GET)
    @ResponseBody
    public String downloadAttachment(String urls,HttpServletResponse response) {

        //这里获取的下载链接 http://sk.sit.fosuntech.cn/group1/M00/00/72/CqYKHVn69wyAMl6YAAVf953sp4Y075.pdfhttp://123.124.175.239:9080/Seal/doc/download.jsp?pwd=y5heBEDbtmxY88E-8--9-888888889Te6yu7
        String downLoadPath = "http://123.124.175.239:9080/Seal/doc/download.jsp?pwd=6S7ovZq53QkVGGZJGJJZJGGGGGGGGJJoD692";
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            //响应二进制流
            //response.setContentType("application/octet-stream");
            response.reset();//清除response中的缓存
            //根据网络文件地址创建URL
            URL url = new URL(downLoadPath);
            //获取此路径的连接
            URLConnection conn = url.openConnection();
            Long fileLength = conn.getContentLengthLong();//获取文件大小
            //设置reponse响应头，真实文件名重命名，就是在这里设置，设置编码
            //response.setHeader("Content-Disposition",
                    //"attachment; filename=" + fileName);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Length", String.valueOf(fileLength));

            bis = new BufferedInputStream(conn.getInputStream());//构造读取流
            bos = new BufferedOutputStream(response.getOutputStream());//构造输出流
            byte[] buff = new byte[1024];
            int bytesRead;
            //每次读取缓存大小的流，写到输出流
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
            response.flushBuffer();//将所有的读取的流返回给客户端
        } catch (IOException e) {
        }finally{
            try{
                if(null != bis){
                    bis.close();
                }
                if(null != bos){
                    bos.close();
                }
            }catch(IOException e){

            }
        }
        return null;
    }
}
