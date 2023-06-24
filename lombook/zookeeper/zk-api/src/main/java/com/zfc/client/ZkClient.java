package com.zfc.client;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static com.zfc.common.Console.SESSION_TIMEOUT;
import static com.zfc.common.Console.ZK_SERVERS;

/**
 * Description: zk客户端
 *
 * @author zfcer
 * @since 2023/6/23
 */
public class ZkClient {
    private static ZooKeeper zkClient;

    @BeforeAll
    static void init() throws IOException {
        zkClient = new ZooKeeper(ZK_SERVERS, SESSION_TIMEOUT, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                // 接收监听事件信息
                System.out.println(watchedEvent.getType() + "--" + watchedEvent.getPath());
                // 再次启动监听
                try {
                    List<String> children = zkClient.getChildren("/", true);
                    //for (String child : children) {
                    //    System.out.println(child);
                    //}
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Test
    public void create() throws KeeperException, InterruptedException {
        /**
         * 参数 1：要创建的节点的路径；
         * 参数 2：节点数据 ；
         * 参数 3：节点权限 ；
         * 参数 4：节点的类型
         */
        zkClient.create("/zfcer",
                "feifei".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);
    }

    @Test
    public void getChildren() throws Exception {
        List<String> children = zkClient.getChildren("/", true);
        for (String child : children) {
            System.out.println(child);
        }
        // 延时阻塞
//        Thread.sleep(Long.MAX_VALUE);
    }

    @Test
    public void exist() throws KeeperException, InterruptedException {
        Stat stat = zkClient.exists("/zfcer", false);
        System.out.println(stat == null ? "not exist" : "exist");
    }
}