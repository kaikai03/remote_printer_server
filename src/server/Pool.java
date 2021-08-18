package server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.java_websocket.WebSocket;

public class Pool {
    private static final Map<WebSocket, String> clientsCollection = new HashMap<WebSocket, String>();

    /**
     * 向连接池中添加连接对象 
     * 
     */
    public static void addClient(String clientName, WebSocket conn) {
        clientsCollection.put(conn, clientName); 
    }

    /**
     * 获取所有连接池中的用户
     * 
     */
    public static Collection<String> getOnlineclient() {
        List<String> clients = new ArrayList<String>();
        Collection<String> client = clientsCollection.values();
        for (String u : client) {
            clients.add(u);
        }
        return clients;
    }

    /**
     * 移除连接池中的连接
     * 
     */
    public static boolean removeClient(WebSocket conn) {
        if (clientsCollection.containsKey(conn)) {
            clientsCollection.remove(conn); // 移除连接
            return true;
        } else {
            return false;
        }
    }

    /**
     * 通过连接获取其对应的客户端名称
     */
    public static String getClientByWs(WebSocket conn) {
        return clientsCollection.get(conn);
    }	

    /**
     * 根据客户端名称获取连接对象,如果意外重复,取第一个
     * important！需要控制，clientName禁止重复，尚未实现
     */
    public static WebSocket getWsByClient(String clientName) {
        Set<WebSocket> keySet = clientsCollection.keySet();
        synchronized (keySet) {
            for (WebSocket conn : keySet) {
                String Client = clientsCollection.get(conn);
                if (Client.equals(clientName)) {
                    return conn;
                }
            }
        }
        return null;
    }
    
    /**
     * 向特定的用户发送数据
     */
    public static void sendMessageByConn(WebSocket conn, String message) {
        if (null != conn && null != clientsCollection.get(conn)) {
            conn.send(message);
        }
    }
    
    public static void sendMessageToClient(String clientName, String message) {
    	WebSocket conn = Pool.getWsByClient(clientName);
        if (null != conn) {
            conn.send(message);
        }
    }

    /**
     *   广播
     */
    public static void sendMessageToAll(String message) {
        Set<WebSocket> keySet = clientsCollection.keySet();
        synchronized (keySet) {
            for (WebSocket conn : keySet) {
                String client = clientsCollection.get(conn);
                if (client != null) {
                    conn.send(message);
                }
            }
        }
    }

}