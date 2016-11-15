package projekcik;

import java.awt.EventQueue;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

public class Main {

	static Main m;

	String GPS, modem;
	String[] gets_port;
	
	static Frame frame;

	InputStream in_gps;
	
	InputStream in_modem;
	OutputStream out_modem;
	
	static Buffor_modem buffor_modem;
	
	public Main() {
		
	}
	ArrayList get_port = new ArrayList();

	String[] show_list() {
		Enumeration e = CommPortIdentifier.getPortIdentifiers();

		int i = 0;
		System.out.println("dostepne porty :");

		while (e.hasMoreElements()) {

			get_port.add(((CommPortIdentifier) e.nextElement()).getName());
			i++;
			gets_port = new String[i];
			for (int j = 0; j < get_port.size(); j++) {
				gets_port[j] = (String) get_port.get(j);
			}
		}
		return gets_port;
	}

	Buffor_modem connect(String portName, String login, String password) throws Exception {
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		if (portIdentifier.isCurrentlyOwned()) {
			System.out.println("Error: Port is currently in use");
		} else {
			int timeout = 2000;
			CommPort commPort = portIdentifier.open(this.getClass().getName(), timeout);

			if (commPort instanceof SerialPort) {
				SerialPort serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(57600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
						SerialPort.PARITY_NONE);
				

				if (portName.equals("COM1")) {
					System.err.println("port gps");				
	
					in_gps = serialPort.getInputStream();
			
			}
				
				if (portName.equals("COM14")) {
					
					System.err.println("port modem");

					in_modem = serialPort.getInputStream();
					out_modem = serialPort.getOutputStream();

				} 
					if(in_gps != null && out_modem != null){
						
                       
						 System.err.println("porty otawrte");
						 
						 buffor_modem= new Buffor_modem(out_modem, in_modem, frame, in_gps, login , password);
				
						 SerialReader_gps serialReader_gps = new SerialReader_gps(in_gps, buffor_modem, frame);
						 
						 Thread.sleep(2000);
						 serialReader_gps.start();
						 
							
					}
			}
				else {
					System.out.println("Error: Only serial ports are handled by this example.");
				}	
		}                                 
		return buffor_modem;
	}
	
	public static void main(String[] args) {
		
		m = new Main();
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				
				try {
					Thread.sleep(500);

					frame = new Frame(m);

					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
