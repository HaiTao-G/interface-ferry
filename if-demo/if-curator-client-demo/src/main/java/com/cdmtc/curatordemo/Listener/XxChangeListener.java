package com.cdmtc.curatordemo.Listener;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName XxChangeListener
 * @Description 3.5版本以下自定义业务监听器
 * @Author Tao-pc
 * @Date 2022/8/31 16:19
 */
@Configuration
public class XxChangeListener implements TreeCacheListener {

    @Override
    public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {
        TreeCacheEvent.Type type = treeCacheEvent.getType();
        //只监听修改事件
        if (type.equals(TreeCacheEvent.Type.NODE_UPDATED)){
            //获取节点json数据
            String childrenData = new String(treeCacheEvent.getData().getData());
            //获取节点路径
            String childrenPath = treeCacheEvent.getData().getPath();
        }
    }
}
