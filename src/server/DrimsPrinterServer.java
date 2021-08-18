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
    	System.out.println(conn);
    	System.out.println(remote);
    	System.out.println(reason);
    	System.out.println("user leave:"+getClientName(conn));
    	
        clientOffline(conn);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println(message);
        // 登录固定格式 login#//clientName
        if(null != message &&message.startsWith("login#//")){
        	//分离出客户端名称
            String clientName = message.replaceFirst("login#//", "");
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
        	String[] splited = flagAndMsg.split("_");
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
            default:
            	System.out.println("wrong flag");
                break;
            }
        }else if(null != message && message.startsWith("ping#//")){
        	
        }
        
        else if(null != message && message.equals("Hello 2")){
        	//回复测试测试测试测试测试测试测试
        	System.out.println("sendTo");
        	sendTo("193.QQ", "fuck");
//        	sendPrintOrder("193.QQ", "测试地址");
            return ;
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
     * 发送ping
     */
    public void sendPing(String flag){
    	Pool.sendMessageToAll("ping#//"+flag);
    }
    /**
     * 发送打印指令
     */
    public void sendPrintOrder(String clientName, String printAddress){
//    	String msg = "printOrder#//"+printAddress;
    	String msg = "printOrder#//";
    	String msg2 = "https://test.drims.cn/view/preciousBaby/scale/rd_print.jsp?&tableName=rd_scale_physical&tableDesc=体格生长&itemId=309&detailId=4313&printReporter=吕&printReporterId=3&printSign=8b3de1aa14e8453284f96b8333e01c9c&sendId=2119&visNo=A368&isprint=undefined&growPrint=true";
    	sendTo(clientName, msg+msg2);
    }
    

}