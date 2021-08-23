package server;


import java.net.InetSocketAddress;
import java.util.Collection;

import org.java_websocket.WebSocketImpl;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class DrimsPrinterServer extends WebSocketServer {
	private static int defaultPort = 11303;
	
	public DrimsPrinterServer() {
        super(new InetSocketAddress(defaultPort));
        WebSocketImpl.DEBUG = false;
    }
	
    public DrimsPrinterServer(int port) {
        super(new InetSocketAddress(port));
        WebSocketImpl.DEBUG = false;
    }

    public DrimsPrinterServer(InetSocketAddress address) {
        super(address);
        WebSocketImpl.DEBUG = false;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
    	System.out.println("onOpen");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    	System.out.println("onClose");
    	String client = getClientName(conn);
    	if(client == null) {
    		// 非正常登录的连接，不做处理。
    		return;
    	}
    	
    	System.out.println("user leave:"+client);
        clientOffline(conn);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println(message);
        // 登录固定格式 login#//clientName
        if(null != message &&message.startsWith("login#//")){
        	//分离出客户端名称
            String clientName = message.replaceFirst("login#//", "");
            
            if(getClientConn(clientName) != null) {
            	System.out.println("已存在设备，禁止重复加入:"+clientName);
            	conn.send("fuckoff#//禁止重复登录,请检查设备名，或联系开发人员检查打印队列");
            	conn.close();
            	return ;
            }
            
            System.out.println("设备加入:"+clientName);
            clientOnline(conn,clientName);//设备 加入
            return ;
        }else if(null != message && message.startsWith("logout")){
        	System.out.println("user logout:"+getClientName(conn));
            clientOffline(conn);
            return ;
        }else if(null != message && message.startsWith("return#//")){
        	//客户端接收到指令，执行完成后，返回信息,格式为return#//flag_msg
        	String flagAndMsg = message.replaceFirst("return#//", "");
        	String[] splited = flagAndMsg.split("_",2);
        	String flag = splited[0];
        	String msg = splited[1];
        	String clientName = getClientName(conn);
            switch (flag) {
            case "PrintSuccess":
                System.out.println(clientName+"~"+"Print_Success");
                break;
            case "PrintError":
                System.out.println(clientName+"~"+"Print_Error:"+msg);
                break;
            case "Pong":
            	//TODO:计算登录时长并写数据库
            	//flag 是发送ping时带的标志，再带回来用于区别延迟乱序的数据包
                System.out.println(clientName+"~"+"Pong:"+msg);
                break;
            case "infoOrder":
            	//TODO:计算登录时长并写数据库
            	//flag 是发送ping时带的标志，再带回来用于区别延迟乱序的数据包
                System.out.println(clientName+"~"+"infoOrder:"+msg);
                break;
                
            default:
            	System.out.println("wrong flag");
                break;
            }
        }else if(null != message && message.startsWith("ping#//")){
        	//暂时没实现客户端ping服务器
        }

        

    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.out.println("on error");
        ex.printStackTrace();
        //连接错误断开会触发onClose，至少登出事件可以不在这里处理
    }
    /**
     * 移除连接
     */
    private void clientOffline(WebSocket conn){
    	//TODO:更新在线状态
        Pool.removeClient(conn);
    }
    /**
     * 加入用户池
     */
    private void clientOnline(WebSocket conn,String clientName){
    	//TODO:更新在线状态
        Pool.addClient(clientName, conn);
    }
    /**
     * 通过连接对象，查询客户端名称
     */
    private String getClientName(WebSocket conn){
        return Pool.getClientByWs(conn);
    }
    
    /**
     * 通过连接对象，查询客户端名称
     */
    private WebSocket getClientConn(String clientName){
        return Pool.getWsByClient(clientName);
    }
    
    
    /**
     * 發消息
     */
    private void sendTo(String clientName, String msg){
    	Pool.sendMessageToClient(clientName, msg);
    }
    
    
    
    /**
     * 获得所有客户端
     */
    public Collection<String> getAllClient(){
    	return Pool.getOnlineclient();
    }
    
    /**
     * 向所有设备发送ping
     */
    public void sendPing(String flag){
    	Pool.sendMessageToAll("ping#//"+flag);
    }
    /**
     * 发送打印指令
     */
    public void sendPrintOrder(String clientName, String printAddress){
    	String msg = "printOrder#//"+printAddress;
    	sendTo(clientName, msg);
    }
    /**
     * 发送获取性能报告指令
     */
    public void sendInfoOrderReport(String clientName){
    	String msg = "infoOrder#//report";
    	sendTo(clientName, msg);
    }
    /**
     * 发送获取pc基本信息报告指令
     */
    public void sendInfoOrderReportPCInfo(String clientName){
    	String msg = "infoOrder#//pcinfo";
    	sendTo(clientName, msg);
    }

    

}