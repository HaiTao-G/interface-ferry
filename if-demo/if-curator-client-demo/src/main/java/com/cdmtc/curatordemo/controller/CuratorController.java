package com.cdmtc.curatordemo.controller;

import com.cdmtc.curator.client.CuratorClient;
import com.cdmtc.curatordemo.Listener.XxChangeListener;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CuratorController {

    @Autowired
    private XxChangeListener xxChangeListener;

    @Autowired
    private CuratorClient curatorClient;

    /**
     * zk创建节点
     * @param path
     * @param value
     */
    @PostMapping("add")
    private String add(String path,String value){
        //创建节点
        curatorClient.createNode(CreateMode.PERSISTENT,path,value);
        //对创建的节点进行监听
        curatorClient.watch(path,xxChangeListener);
        return "新增成功!";
    }

    /**
     * 设置节点数据
     * @param path
     * @param value
     * @return
     */
    @PostMapping("update")
    private String update(String path,String value){
        curatorClient.setNodeData(path,value);
        return "设置成功!";
    }

    /**
     * 删除节点数据
     * @param path
     * @return
     */
    @PostMapping("del")
    private String del(String path){
        curatorClient.deleteNode(path);
        return "设置成功!";
    }

}
