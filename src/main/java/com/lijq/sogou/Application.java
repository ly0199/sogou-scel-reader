package com.lijq.sogou;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Created by Lijq on 2017/5/18.
 */
public class Application {

    private static Logger logger = LoggerFactory.getLogger(Application.class);

    /**
     * 主函数
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {


        // 原文件夹路径
        String source_path = "e:/scel/";

        // 输出文件夹路径
        String out_path = "e:/scel/out1/";

        // 获取原文件夹路径
        File file = new File(source_path);
        if (!file.exists()) {
            logger.error("没有可被使用的文件路径");
            return;
        }


        // 文件名称
        String[] files = file.list();
        if (files.length < 0) {
            logger.error("文件夹下没有可以被使用的文件");
            return;
        }


        // out file
        File out = new File(out_path);
        if (!out.exists()) {
            new File(out_path).mkdirs();
        }


        // 归档可以被使用的 scel 文件
        for (int i = 0; i < files.length; i++) {
            String filename = files[i];
            if (filename.endsWith(".scel")) {
                String fn = filename.substring(0, filename.lastIndexOf("."));
                sogou(source_path + filename, out_path + fn + ".txt", true);
            }
        }
    }

    /**
     * 读取scel的词库文件,生成txt格式的文件
     *
     * @param inputPath  输入路径
     * @param outputPath 输出路径
     * @param isAppend   是否拼接追加词库内容 true 代表追加,false代表重建
     **/
    private static void sogou(String inputPath, String outputPath, boolean isAppend) throws IOException {

        logger.info(String.format("准备生成文件：%s 的词汇记录", inputPath));

        File file = new File(inputPath);
        if (!isAppend) {
            if (Files.exists(Paths.get(outputPath), LinkOption.values())) {
                logger.info("存储此文件已经删除");
                Files.deleteIfExists(Paths.get(outputPath));
            }
        }
        RandomAccessFile raf = new RandomAccessFile(outputPath, "rw");

        int count = 0;
        SougouScelModel model = new SougouScelReader().read(file);
        Map<String, List<String>> words = model.getWordMap(); //词<拼音,词>
        Set<Entry<String, List<String>>> set = words.entrySet();
        Iterator<Entry<String, List<String>>> iter = set.iterator();
        while (iter.hasNext()) {
            Entry<String, List<String>> entry = iter.next();
            List<String> list = entry.getValue();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                String word = list.get(i);
                raf.seek(raf.getFilePointer());
                raf.write((word + "\n").getBytes());//写入txt文件
                count++;
            }
        }
        raf.close();
        logger.info(String.format("文件：%s 生成txt成功！, 总计写入: %s 条数据!", inputPath, count));
    }

}