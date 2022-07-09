import com.github.tobato.fastdfs.exception.FdfsUnsupportStorePathException;
import com.rzk.utils.AvatarHelper;
import com.rzk.utils.MinioUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * @PackageName : PACKAGE_NAME
 * @FileName : test
 * @Description :
 * @Author : rzk
 * @CreateTime : 3/6/2022 下午5:10
 * @Version : v1.0
 */
public class test {
    public static void main(String[] args) throws Exception {
        String avatar = AvatarHelper.createBase64Avatar(Math.abs("springboot.io".hashCode()));
        System.out.println(AvatarHelper.BASE64_PREFIX + avatar);


//        String name = "NBoo0BPnZtYT3c3cd215f6a925f5ab9984f1020f5dc4.mp4";
//        int indexOf = name.lastIndexOf(".mp4");
//        String substring = name.substring(0, indexOf);
//        System.out.println(substring);


//            MinioUtils minioUtils = new MinioUtils();
//            String address = "http://124.221.132.14:9999/rzk/vod_1654629487979.mp4";
//            //获取视频的第一帧图片输出流
//            InputStream first = MinioUtils.randomGrabberFFmpegImage(address);
//            //获取文件名
//            String fileName = address.substring(address.lastIndexOf("/"),address.lastIndexOf(".")) .concat(".jpg");
//            //将流转化为multipartFile
//            MultipartFile multipartFile = new MockMultipartFile("file",fileName,"image/jpg", first);
//            //上传图片（通过feign）
//            String upload = minioUtils.upload(multipartFile);
//            //保存文件地址
//            System.out.println(upload);



//        String filePath = "/rzk/M00/00/00/eE8HJGKZus2AZU43AAQNy6YyKlw067.png";
//        String group = getGroupName(filePath);
//        int pathStartPos = filePath.indexOf(group) + group.length() + 1;
//        String path = filePath.substring(pathStartPos, filePath.length());
//        System.out.println(path);
    }
    private static String getGroupName(String filePath) {
        String[] paths = filePath.split("/");
        if (paths.length == 1) {
            throw new FdfsUnsupportStorePathException("解析文件路径错误,有效的路径样式为(group/path) 而当前解析路径为".concat(filePath));
        } else {
            String[] var2 = paths;
            int var3 = paths.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                String item = var2[var4];
                if (item.indexOf("rzk") != -1) {
                    return item;
                }
            }

            throw new FdfsUnsupportStorePathException("解析文件路径错误,被解析路径url没有group,当前解析路径为".concat(filePath));
        }
    }
}

