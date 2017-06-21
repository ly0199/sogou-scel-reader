package com.lijq.sogou;

import com.google.common.collect.Lists;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.WriteModel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

/**
 * Created by Lijq on 2017/5/18.
 */
public class Write2Mongodb {

    private static String source_path = "e:/scel/out/";

    public static void main(String[] args) throws Exception {

        // 获取原文件夹路径
        File file = new File(source_path);
        if (!file.exists()) {
            System.out.println("没有可被使用的文件路径");
            return;
        }

        // 文件名称
        String[] files = file.list();
        if (files.length < 0) {
            System.out.println("文件夹下没有可以被使用的文件");
        }

        MongoCollection<Document> collection = MongoManager.getInstance()
                .getMongoClient()
                .getDatabase("movie")
                .getCollection("t_dict");

        // 归档可以被使用的 scel 文件
        System.out.println("files.length = " + files.length);
        for (int i = 0; i < files.length; i++) {
            String filename = files[i];
            if (filename.endsWith(".txt")) {
                System.out.println(String.format("写入 %s 文件到Mongodb", source_path + filename));
                List<String> list = txt2String(source_path + filename);

                if (CollectionUtils.isNotEmpty(list)) {
                    List<WriteModel<Document>> updates = Lists.newArrayList();
                    for (String str : list) {
                        Document query = new Document();
                        query.put("dic", str);

                        Document update = new Document();
                        update.put("dic", str);

                        UpdateOneModel<Document> updateOneModel = new UpdateOneModel<Document>(
                                query,  // find part
                                new Document("$set", update), // update part
                                new UpdateOptions().upsert(true) // options like upsert
                        );

                        updates.add(updateOneModel);
                    }
                    BulkWriteResult bulkWriteResult = collection.bulkWrite(updates);
                }
            }
        }
    }

    /**
     * 读取文本行
     *
     * @param filename
     * @return
     */
    public static List<String> txt2String(String filename) {
        File file = new File(filename);
        List<String> list = Lists.newArrayList();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                //String line = System.lineSeparator() + s;
                String line = s.trim();
                if (StringUtils.isNoneBlank(line))
                    list.add(line);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
