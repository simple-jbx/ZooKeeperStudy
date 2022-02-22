package tech.snnukf.zktest;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author simple.jbx
 * @ClassName zkClient
 * @description //TODO
 * @email jb.xue@qq.com
 * @github https://github.com/simple-jbx
 * @date 2022/02/08/ 14:24
 */
public class ZKClientTest {
    private static String connectString = "192.168.232.128:2181,192.168.232.129:2181,192.168.232.130:2181";
    private static int sessionTimeout = 200000;
    private ZooKeeper zkClient = null;

    @Before
    public void init() throws Exception {
        System.out.println("---init start---");
        zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
                    @Override
                    public void process(WatchedEvent watchedEvent) {
                        // 收到事件通知后的回调函数（用户的业务逻辑）
                        System.out.println(watchedEvent.getType() + "--"
                                + watchedEvent.getPath());
                        // 再次启动监听
                        try {
                            List<String> children = zkClient.getChildren("/",
                                    true);
                            for (String child : children) {
                                System.out.println(child);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        System.out.println("---init end---");
    }

    // 创建子节点
    @Test
    public void create() throws Exception {
        // 参数 1：要创建的节点的路径； 参数 2：节点数据 ； 参数 3：节点权限 参数 4：节点的类型
        String nodeCreated = zkClient.create("/people", "male".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(nodeCreated);
    }

    // 获取子节点
    @Test
    public void getChildren() throws Exception {
        List<String> children = zkClient.getChildren("/", true);
        for (String child : children) {
            System.out.println(child);
        }
        // 延时阻塞
        Thread.sleep(Long.MAX_VALUE);
    }

    // 判断 znode 是否存在
    @Test
    public void exist() throws Exception {
        Stat stat = zkClient.exists("/people", false);
        System.out.println(stat == null ? "not exist" : "exist");
    }

}
