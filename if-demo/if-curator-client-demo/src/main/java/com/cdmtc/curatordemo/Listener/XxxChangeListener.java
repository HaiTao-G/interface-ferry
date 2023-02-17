//package com.cdmtc.curatordemo.Listener;
//
//import com.alibaba.fastjson.JSON;
//import com.cdmtc.curator.client.CuratorClient;
//import com.cdmtc.curatordemo.service.WatcherService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.curator.framework.recipes.cache.ChildData;
//import org.apache.curator.framework.recipes.cache.CuratorCacheListenerBuilder;
//import org.springframework.stereotype.Component;
//
///**
// * @ClassName XxxChangeListener
// * @Description 3.5版本以上自定义业务监听器
// * @Author Tao-pc
// * @Date 2022/8/25 9:04
// */
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class XxxChangeListener implements CuratorCacheListenerBuilder.ChangeListener{
//
//    // 声明业务服务
//    final WatcherService watcherService;
//
//    // 声明连接
//    final CuratorClient curatorClient;
//
//    /**
//     *  data和oldData的数据结构:{"data":[49,49],"path":"/1","stat":{"aversion":0,"ctime":1661255532075,"cversion":0,"czxid":19,"dataLength":2,"ephemeralOwner":0,"mtime":1661255574772,"mzxid":20,"numChildren":0,"pzxid":19,"version":1}}
//     * @param oldData ---老的数据
//     * @param data ---新的数据
//     */
//    @Override
//    public void event(ChildData oldData, ChildData data) {
//        //拿到数据
//        System.out.println("节点发生变化");
//        System.out.println(JSON.toJSONString(oldData));
//        System.out.println(JSON.toJSONString(data));
//        //业务操作 watcherService
//        //删除节点 curatorClient.deleteNode("path");
//    }
//}
