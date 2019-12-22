package com.hotpaxos.framework.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * User: lijinpeng
 * Created by Shanghai on 2019/12/22.
 */
@Slf4j
public class NetIPUtil {
    private static volatile InetAddress LOCAL_ADDRESS = null;
    private static volatile InetAddress NET_ADDRESS = null;

    /**
     * 获取外网ip，如果没有外网ip获取内网ip
     *
     * @return ip地址
     */
    public static String getMyIp() {

        // 本地IP，如果没有配置外网IP则返回它
        String localIp = null;
        // 外网IP
        String netIp = null;

        InetAddress netAddress = getNetAddress();
        if (null != netAddress) {
            netIp = netAddress.getHostAddress();
        } else if (null != NET_ADDRESS) {
            localIp = NET_ADDRESS.getHostAddress();
        }

        if (!StringUtils.isEmpty(netIp)) {
            return netIp;
        } else {
            return localIp;
        }
    }

    /**
     * 获取内网ip
     *
     * @return ip地址
     */
    public static String getLocalIp() {

        // 本地IP，如果没有配置外网IP则返回它
        String localIp = null;
        InetAddress localAddress = getLocalAddress();
        if (null != localAddress) {
            localIp = localAddress.getHostAddress();
        }

        return localIp;
    }

    public static InetAddress getLocalAddress() {

        if (null != LOCAL_ADDRESS) {
            return LOCAL_ADDRESS;
        }

        try {
            Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip;
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
                Enumeration address = ni.getInetAddresses();
                while (address.hasMoreElements()) {
                    ip = (InetAddress) address.nextElement();
                    if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && !ip.getHostAddress().contains(":")) {
                        // 内网IP
                        LOCAL_ADDRESS = ip;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            log.error("NetWorkUtil getLocalAddress error", e);
        }
        return LOCAL_ADDRESS;
    }

    public static InetAddress getNetAddress() {

        if (null != NET_ADDRESS) {
            return NET_ADDRESS;
        }

        try {
            Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip;
            // 是否找到外网IP
            boolean finded = false;
            while (netInterfaces.hasMoreElements() && !finded) {
                NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
                Enumeration address = ni.getInetAddresses();
                while (address.hasMoreElements()) {
                    ip = (InetAddress) address.nextElement();
                    if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && !ip.getHostAddress().contains(":")) {
                        // 外网IP
                        finded = true;
                        NET_ADDRESS = ip;
                        break;
                    } else if (null == LOCAL_ADDRESS && ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && !ip.getHostAddress().contains(":")) {
                        // 内网IP
                        LOCAL_ADDRESS = ip;
                    }
                }
            }
        } catch (Exception e) {
            log.error("NetWorkUtil getNetAddress error", e);
        }

        return NET_ADDRESS;
    }
}
