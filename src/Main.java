

import server.DrimsPrinterServer;

import java.util.Scanner;

public class Main {
   public static void main(String[] args) {
	   	int port = 11303; 
	   	final DrimsPrinterServer printerS = new DrimsPrinterServer(port);
	   	//printerS.start();
	   	Runnable ra = () ->printerS.start();
	   	new Thread(ra).start();
	   
	   	
	   	
	   	
	   	Scanner input = new Scanner(System.in);
	   	int x = 0;
	   	while(x>=0){
	   		x = input.nextInt();
			System.out.println("输入"+x);
			if(x==5) {
				System.out.println("触发5");
				printerS.sendPrintOrder("193.QQ","https://test.drims.cn/view/preciousBaby/scale/rd_print.jsp?&tableName=rd_scale_physical&tableDesc=体格生长&itemId=309&detailId=4313&printReporter=吕&printReporterId=3&printSign=8b3de1aa14e8453284f96b8333e01c9c&sendId=2119&visNo=A368&isprint=undefined&growPrint=true");
			}
			if(x==6) {
				System.out.println("触发6");
				printerS.sendPrintOrder("193.QQ222","https://test.drims.cn/view/preciousBaby/scale/rd_print.jsp?&tableName=rd_scale_physical&tableDesc=体格生长&itemId=309&detailId=4313&printReporter=吕&printReporterId=3&printSign=8b3de1aa14e8453284f96b8333e01c9c&sendId=2119&visNo=A368&isprint=undefined&growPrint=true");
			}
			
			if(x>10) {
				System.out.println("触发>10");
				printerS.sendPing(Integer.toString(x));
				
			}
	  }
      

   }
}
