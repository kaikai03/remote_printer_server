

import server.DrimsPrinterServer;

import java.util.Scanner;
import java.util.Date;
import java.text.SimpleDateFormat;


public class Main {
	public static boolean aotuPing = false;
	
   public static void main(String[] args) {
	   	int port = 11303; 
	   	
	   	
	   	final DrimsPrinterServer printerS = new DrimsPrinterServer(port);
	   	//printerS.start();
	   	Runnable ra = () ->printerS.start();
	   	new Thread(ra).start();
	   
	   	SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
	   	
	   	Scanner input = new Scanner(System.in);
	   	int x = 0;
	   	while(x>=0){
	   		x = input.nextInt();
			System.out.println("输入"+x);
			
			if(x==3) {
				System.out.println("触发3");
				printerS.sendInfoOrderReport("193.QQ");
			}
			if(x==4) {
				System.out.println("触发4");
				printerS.sendInfoOrderReportPCInfo("193.QQ");
			}
			
			if(x==5) {
				System.out.println("触发5");
				printerS.sendPrintOrder("193.QQ","https://test.drims.cn/view/preciousBaby/scale/rd_print.jsp?&tableName=rd_scale_physical&tableDesc=体格生长&itemId=309&detailId=4313&printReporter=吕&printReporterId=3&printSign=8b3de1aa14e8453284f96b8333e01c9c&sendId=2119&visNo=A368&isprint=undefined&growPrint=true");
			}
			if(x==6) {
				System.out.println("触发6");
				printerS.sendPrintOrder("193.QQ_侯","https://test.drims.cn/view/preciousBaby/scale/rd_print.jsp?&tableName=rd_scale_physical&tableDesc=体格生长&itemId=309&detailId=4313&printReporter=吕&printReporterId=3&printSign=8b3de1aa14e8453284f96b8333e01c9c&sendId=2119&visNo=A368&isprint=undefined&growPrint=true");
			}
			if(x==7) {
				System.out.println("触发7");
				printerS.sendPrintOrder("193.QQ_汤","https://test.drims.cn/view/preciousBaby/scale/rd_print.jsp?&tableName=rd_scale_physical&tableDesc=体格生长&itemId=309&detailId=4313&printReporter=吕&printReporterId=3&printSign=8b3de1aa14e8453284f96b8333e01c9c&sendId=2119&visNo=A368&isprint=undefined&growPrint=true");
			}
			
			
			
			if(x>100) {
				System.out.println("触发>10");
				printerS.sendPing(Integer.toString(x));
				
			}
			if(x==99) {
				System.out.println("触发99");
				Main.aotuPing = true;
				Runnable ra2 = () ->{
					while(Main.aotuPing) {
						printerS.sendPing(df.format(new Date()));
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				};
			   	new Thread(ra2).start();
			}
			if(x==98) {
				System.out.println("触发98");
				Main.aotuPing = false;
			}
	   	}
      

   }
}
