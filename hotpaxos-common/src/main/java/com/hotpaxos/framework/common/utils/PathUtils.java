package com.hotpaxos.framework.common.utils;

import com.hotpaxos.framework.common.registry.CommonServiceNode;
import com.hotpaxos.framework.common.registry.NodeType;


/**
 * User: lijinpeng
 * Created by Shanghai on 2019/12/22.
 */
public class PathUtils {
    public static String toServicePath(Integer appId, NodeType nodeType) {
        return HotpaxosConstans.PATH_SEPARATOR + nodeType.getValue() + HotpaxosConstans.PATH_SEPARATOR + appId;
    }

    public static String toGroupPath() {
        return HotpaxosConstans.PATH_SEPARATOR + AppIdUtils.loadAppId();
    }

    public static String toNodeTypePath(NodeType nodeType) {
        return HotpaxosConstans.PATH_SEPARATOR + nodeType.getValue() + toGroupPath();
    }

    public static String toNodeIdPath(CommonServiceNode node) {
        return HotpaxosConstans.PATH_SEPARATOR + node.getNodeId();
    }

    public static String toHostAndPortPath(int port) {
        return NetIPUtil.getLocalIp() + (port > 0 ? HotpaxosConstans.COLON_SEPARATOR + port : HotpaxosConstans.Empty);
    }

    public static String toNodePath(CommonServiceNode node, NodeType nodeType) {
        return toNodeTypePath(nodeType) + toNodeIdPath(node) + HotpaxosConstans.PATH_UNDERLINE + toHostAndPortPath(node.getPort());
    }

    public static String parseDirectoryToHost(String[] directories) {
        String[] nodeName = directories[directories.length - 1].split(HotpaxosConstans.PATH_UNDERLINE);
        return nodeName[nodeName.length - 1];
    }

    public static String[] parseNodePathToDirectory(String nodePath) {
        return nodePath.split(HotpaxosConstans.PATH_SEPARATOR);
    }

//    public static String toReConnectName(Channel channel) {
//        return channel.id().asLongText() + HotpaxosConstans.RECONNECT_LISTENER_NAME;
//    }

}
