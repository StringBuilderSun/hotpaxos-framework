package com.hotpaxos.client.bean;

/**
 * User: lijinpeng
 * Created by Shanghai on 2019/12/21.
 */
public class DemoPerson {

    private String host;
    private String port;

    public DemoPerson(String host, String port) {
        this.host = host;
        this.port = port;
        System.out.println("连接机器ip:" + this.host+" 端口:"+this.port);
    }
}
