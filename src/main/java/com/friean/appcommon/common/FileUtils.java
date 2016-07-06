package com.friean.appcommon.common;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * auther friean
 * time 2015/5/28 on 11:00
 * Description:文件处理工具类
 */
public class FileUtils {
    /**
     * 是否有存储卡
     *
     * @return true表示 有sdcard false表示没有sdcard
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取存储卡的绝对路径
     *
     * @return sdcard的绝对路径
     */
    public static String getExternalStoragePath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }


    /**
     * 计算指定目录的大小
     *
     *
     */
    public static long getFileSize(File f)
    {
        long size = 0;
        File flist[] = f.listFiles();
        for (File aFlist : flist) {
            if (aFlist.isDirectory()) {
                size = size + getFileSize(aFlist);
            } else {
                size = size + aFlist.length();
            }
        }
        return size;
    }


    /**
     * 转换文件大小
     */
    public static String FormetFileSize(long fileS)
    {// 转换文件大小
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString;
        if (fileS <= 0){
            fileSizeString="0B";
        }else if (fileS > 0 && fileS < 1024)
        {
            fileSizeString = df.format((double) fileS) + "B";
        }
        else if (fileS < 1048576)
        {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        }
        else if (fileS < 1073741824)
        {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        }
        else
        {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }


    /**
     * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理
     *
     */
    public static void deleteFilesByDirectory(final File directory) {
        new Thread(new Runnable() {
            @SuppressWarnings("ResultOfMethodCallIgnored")
            @Override
            public void run() {
                if (directory != null && directory.exists() && directory.isDirectory()) {
                    for (File item : directory.listFiles()) {
                        if (item.isFile()){
                            item.delete();
                        }else {
                            deleteFilesByDirectory(item);
                        }
                    }
                }
            }
        }).start();
    }

    /**
     * 字符串内容写入文件中
     *
     * @param file        文件对象
     * @param fileContent 字符串内容
     */
    public static void writeToFile(File file, String fileContent) {
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(fileContent);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取文件中的字符串
     *
     * @param file 文件对象
     * @return 返回的字符串
     */
    public static String readFileContent(File file) {
        StringBuilder fileContentBuilder = new StringBuilder();
        if (file.exists()) {
            String stringLine;
            try {
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                while ((stringLine = bufferedReader.readLine()) != null) {
                    fileContentBuilder.append(stringLine).append("\n");
                }
                bufferedReader.close();
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileContentBuilder.toString();
    }

    /**
     * 将对象集合存入本地缓存文件
     *
     * @param file file
     * @param list ArrayList
     */
    public static void writeObjectsToFile(File file, ArrayList list) {
        ObjectOutputStream obj = null;
        try {
            obj = new ObjectOutputStream(new FileOutputStream(file));
            obj.writeObject(list);
            obj.flush();
            System.out.println("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (obj != null) {
                    obj.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从文件中获取缓存对象
     *
     * @param file file
     * @return ArrayList<Object>
     */
    public static ArrayList<Object> readObjectsFromFile(File file) {
        ArrayList<Object> ret = null;
        ObjectInputStream inputStream;
        try {
            inputStream = new ObjectInputStream(new FileInputStream(file));
            Object ob = inputStream.readObject();
            if (ob instanceof ArrayList) {
                ret = (ArrayList<Object>) ob;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
}

