package com.zfc.serverstatus;

import org.apache.zookeeper.*;

import java.io.IOException;

import static com.zfc.common.Console.SESSION_TIMEOUT;
import static com.zfc.common.Console.ZK_SERVERS;

/**
 * Description: 服务端注册
 *
 * @author zfcer
 * @since 2023/6/24
 */
public class DistributeServer {
    private static final String PARENT_NODE = "/servers";
    private ZooKeeper zk = null;

    public void getConnect() throws IOException {
        zk = new ZooKeeper(ZK_SERVERS, SESSION_TIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
            }
        });
    }

    // 注册服务器
    public void registServer(String hostname) throws Exception {
        String create = zk.create(PARENT_NODE + "/server",
                hostname.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(hostname + " is online " + create);
    }

    // 业务功能
    public void business(String hostname) throws Exception {
        System.out.println(hostname + " is working ...");
        Thread.sleep(Long.MAX_VALUE);
    }

    public static void main(String[] args) throws Exception {
        // 1 获取 zk 连接
        DistributeServer server = new DistributeServer();
        server.getConnect();
        // 2 利用 zk 连接注册服务器信息
        server.registServer(args[0]);
        // 3 启动业务功能
        server.business(args[0]);
    }
}
