

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
				printerS.sendPrintOrder("193.QQ","發送的打印地址");
			}
			if(x>10) {
				System.out.println("触发>10");
				printerS.sendPing(Integer.toString(x));
				
			}
	  }
      

   }
}
