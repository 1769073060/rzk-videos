package com.rzk.utils;

import com.github.tobato.fastdfs.domain.proto.mapper.DynamicFieldType;
import com.github.tobato.fastdfs.domain.proto.mapper.FdfsColumn;
import com.github.tobato.fastdfs.exception.FdfsUnsupportStorePathException;
import org.apache.commons.lang3.Validate;

/**
 * @PackageName : com.rzk.utils
 * @FileName : StorePath
 * @Description :
 * @Author : rzk
 * @CreateTime : 4/6/2022 上午1:28
 * @Version : v1.0
 */
public class StorePath {
    @FdfsColumn(
            index = 0,
            max = 16
    )
    private String group;
    @FdfsColumn(
            index = 1,
            dynamicField = DynamicFieldType.allRestByte
    )
    private String path;
    private static final String SPLIT_GROUP_NAME_AND_FILENAME_SEPERATOR = "/";
    private static final String SPLIT_GROUP_NAME = "rzk";

    public StorePath() {
    }

    public StorePath(String group, String path) {
        this.group = group;
        this.path = path;
    }

    public String getGroup() {
        return this.group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFullPath() {
        return this.group.concat("/").concat(this.path);
    }

    public String toString() {
        return "StorePath [group=" + this.group + ", path=" + this.path + "]";
    }

    public static com.github.tobato.fastdfs.domain.fdfs.StorePath parseFromUrl(String filePath) {
        Validate.notNull(filePath, "解析文件路径不能为空", new Object[0]);
        String group = getGroupName(filePath);
        int pathStartPos = filePath.indexOf(group) + group.length() + 1;
        String path = filePath.substring(pathStartPos, filePath.length());
        return new com.github.tobato.fastdfs.domain.fdfs.StorePath(group, path);
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

