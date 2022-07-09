package com.rzk.utils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * @PackageName : com.rzk.utils
 * @FileName : GetVideoPicture
 * @Description : 获取视频帧，生成图片
 * @Author : rzk
 * @CreateTime : 5/6/2022 上午2:06
 * @Version : v1.0
 */
public class GetVideoPicture {
    public static final Logger log = LoggerFactory.getLogger(GetVideoPicture.class);
    public static File getFile(String url) throws Exception {
        //对本地文件命名
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        System.out.println("fileName"+fileName);
        File file = null;

        URL urlfile;
        InputStream inStream = null;
        OutputStream os = null;
        try {
            file = File.createTempFile("/fastdfs/", fileName.substring(fileName.lastIndexOf("/")+1));
            System.out.println("url"+url);

            //下载
            urlfile = new URL(url);
            inStream = urlfile.openStream();
            os = new FileOutputStream(fileName);

            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = inStream.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != os) {
                    os.close();
                }
                if (null != inStream) {
                    inStream.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return file;
    }
    /**
     * 获取视频第一帧
     * @param videoPath 视频地址
     * @param imgPath 生成图片的名字（包含全路径）
     * @throws Exception
     */
    public static void getVideoPicture(String videoPath,String imgPath) throws Exception{
        File imgFile = new File(imgPath);
        //判断保存的文件的文件夹是否存在，不存在创建。
//        if (!imgFile.getParentFile().exists()) {
//            log.info("保存文件的文件夹不存在，创建。");
//            imgFile.getParentFile().mkdirs();
//        }
        //File videoFile = new File(videoPath);
        log.info("videoPath===>"+videoPath);
        File videoFile = getFile(videoPath);
        if (videoFile.exists()) {
            log.info("视频存在：{}",videoFile);
            //实例化“截取视频首帧”对象
            FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videoFile);
            ff.start();
            int ftp = ff.getLengthInFrames();
            int flag=0;
            Frame frame = null;
            while (flag <= ftp) {
                //获取帧
                frame = ff.grabImage();
                //过滤前1帧，避免出现全黑图片
                if ((flag>1)&&(frame != null)) {
                    break;
                }
                flag++;
            }
            ImageIO.write(frameToBufferedImage(frame), "jpg", imgFile);
            ff.close();
            ff.stop();
        }else {
            log.info("视频不存在：{}",videoFile);
        }
    }

    /**
     * 帧转为流
     * @param frame
     * @return
     */
    private static RenderedImage frameToBufferedImage(Frame frame) {
        //创建BufferedImage对象
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bufferedImage = converter.getBufferedImage(frame);
        return bufferedImage;
    }

    public static void main(String[] args) throws Exception {
//        StringBuffer sb = new StringBuffer();
//        sb.append("");
//        System.out.println(sb.toString());
        //getFile("http://120.79.7.36:88/rzk/M00/00/00/eE8HJGKc-RaAQXMoAAEbdRvRy0A565.mp4");
        try {
            String videoPath = "http://120.79.7.36:88/rzk/M00/00/00/eE8HJGKc-RaAQXMoAAEbdRvRy0A565.mp4";
            System.out.println(videoPath.substring(videoPath.lastIndexOf("/")));

            String imgPath = "http://120.79.7.36:88/rzk/M00/00/00/eE8HJGKc-RaAQXMoAAEbdRvRy0A565.jpg";
            getVideoPicture(videoPath,imgPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //getVideoPicture("http://120.79.7.36:88\\rzk\\M00\\00\\00\\eE8HJGKbsZqAQKrbAAEbdRvRy0A958.mp4","C:\\Users\\ASUS\\Downloads\\图片123.jpg");
    }
}
