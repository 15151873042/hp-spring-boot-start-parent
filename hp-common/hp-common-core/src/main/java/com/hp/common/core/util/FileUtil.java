package com.hp.common.core.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 下载类
 * 
 */
public class FileUtil {
    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    private FileUtil() {
    }

    /**
     * 判断路径
     * @param path
     */
    public static void isExist(String path) {
        // 判断文件夹是否存在,如果不存在则创建文件夹
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    /**
     * 删除临时文件
     * 
     * @param filePath
     */
    public static void deleteTempFile(String filePath) {
        File file = new File(filePath);
        if (!file.isDirectory()) {
            file.delete();
        }
    }

    /**
     * 下载文件
     * 
     * @param path
     * @return 数据句柄
     */
    public static DataHandler download(String path) {
        String fileName = path.substring(path.lastIndexOf(File.separator) + 1);
        if (null == fileName || fileName.isEmpty()) try {
            throw new FileNotFoundException("file name is empty");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        File downloadFile = new File(path);
        if (!downloadFile.exists()) try {
            throw new FileNotFoundException(fileName + " does not exist");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new DataHandler(new FileDataSource(downloadFile) {
            @Override
            public String getContentType() {
                return "application/octet-stream";
            }
        });
    }

    /**
     * 读取txt里的单行内容
     *
     * @param fileP 文件路径
     * @return
     */
    public static String readTxtFile(String fileP) {
        try {

            String filePath = String.valueOf(Thread.currentThread().getContextClassLoader().getResource(""))
                              + "../../"; //项目路径
            filePath = filePath.replaceAll("file:/", "");
            filePath = filePath.replaceAll("%20", " ");
            filePath = filePath.trim() + fileP.trim();

            String encoding = "utf-8";
            File file = new File(filePath);
            if (file.isFile() && file.exists()) { // 判断文件是否存在
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding); // 考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    bufferedReader.close();
                    return lineTxt;
                }
                read.close();
            } else {
                logger.error("找不到指定的文件,查看此路径是否正确:" + filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("读取文件内容出错", e);
        }
        return "";
    }

    /**
     * 写txt里的单行内容
     *
     * @param fileP 文件路径
     * @param content 写入的内容
     */
    public static void writeFile(String fileP, String content) {
        String filePath = String.valueOf(Thread.currentThread().getContextClassLoader().getResource(""))
                          + "../../"; //项目路径
        filePath = (filePath.trim() + fileP.trim()).substring(6).trim();
        PrintWriter pw;
        try {
            pw = new PrintWriter(new FileWriter(filePath));
            pw.print(content);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("写入文件出错", e);
        }
    }
}
