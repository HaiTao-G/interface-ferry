package com.cdmtc.curatordemo.service;

import com.alibaba.fastjson.JSON;
import com.cdmtc.curator.client.CuratorClient;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCacheListenerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 业务系统实现CuratorCacheListenerBuilder.ChangeListener接口的event方法
 * event方法就是监听到node更改事件后具体业务实现
 */
@Service
public class WatcherService implements CuratorCacheListenerBuilder.ChangeListener {

    @Autowired
    private CuratorClient curatorClient;

    /**
     *  data和oldData的数据结构:{"data":[49,49],"path":"/1","stat":{"aversion":0,"ctime":1661255532075,"cversion":0,"czxid":19,"dataLength":2,"ephemeralOwner":0,"mtime":1661255574772,"mzxid":20,"numChildren":0,"pzxid":19,"version":1}}
     * @param oldData ---老的数据
     * @param data ---新的数据
     */
    @Override
    public void event(ChildData oldData, ChildData data) {
        //拿到数据
        System.out.println("节点发生变化");
        System.out.println(JSON.toJSONString(oldData));
        System.out.println(JSON.toJSONString(data));
        //删除节点 curatorClient.deleteNode("path");
    }
}
