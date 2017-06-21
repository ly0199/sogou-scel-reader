package com.lijq.sogou;

import com.mongodb.MongoClient;

/**
 * Created by Lijq on 2017/3/28.
 */
public class MongoManager {


    private static final String host = "localhost";
    private static final int port = 27017;


    private static MongoClient mongoClient = null;

    private static class MongoManagerHolder {
        public static MongoManager instance = new MongoManager();
    }

    public static MongoManager getInstance() {
        return MongoManagerHolder.instance;
    }

    private MongoManager() {
        if (mongoClient == null) {
            try {
                mongoClient = new MongoClient(host, port);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }





       /*public static void main(String[] args) {
        ServerAddress serverAddress = new ServerAddress("localhost", 27017);
        List<ServerAddress> addrs = new ArrayList<ServerAddress>();
        addrs.add(serverAddress);
        MongoCredential credential = MongoCredential.createCredential("username", "database", "password".toCharArray());
        List<MongoCredential> credentials = new ArrayList<MongoCredential>();
        credentials.add(credential);
        // 通过连接认证获取MongoDB连接
        MongoClient mongoClient = new MongoClient(addrs, credentials);
        // 连接到数据库
        MongoDatabase mongoDatabase = mongoClient.getDatabase("databaseName");
    }*/

}
