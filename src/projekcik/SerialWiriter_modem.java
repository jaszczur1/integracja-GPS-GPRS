package projekcik;

import java.io.IOException;


public class SerialWiriter_modem extends Thread {

	Buffor_modem buffor_modem;
	int id_command=0;

	public SerialWiriter_modem(Buffor_modem buffor_modem) {
		// TODO Auto-generated constructor stub
		this.buffor_modem=buffor_modem ;
	}

	public synchronized void run() {		
		
		while(!Thread.interrupted()){
		
		try {
			buffor_modem.pisz(id_command);
			
			System.err.println("watek id_command "+id_command);
			
			
			
			if(buffor_modem.event_kind==2){
			
				
				id_command++;
				if(id_command==10)
				id_command=7;
				System.err.println("to ja watek !!");
				
			}
		
					
			
			
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}
	}
}