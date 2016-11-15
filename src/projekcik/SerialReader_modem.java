package projekcik;

import java.io.IOException;
import java.io.InputStream;

public class SerialReader_modem extends Thread {

	
	Buffor_modem buffor_modem;

	public SerialReader_modem(Buffor_modem buffor_modem) {
		// TODO Auto-generated constructor stub
		
		this.buffor_modem = buffor_modem;
	}

	public synchronized void run() {

		
		
		while (!Thread.interrupted()) {

			try {
				buffor_modem.czytam();
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