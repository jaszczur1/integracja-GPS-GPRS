package projekcik;

import java.io.IOException;
import java.io.InputStream;


public class SerialReader_gps extends Thread {

	InputStream in;
	Buffor_modem buffor_modem;
	SerialWiriter_modem wiriter;
	SerialReader_modem reader;
	Frame frame;

	public SerialReader_gps(InputStream in, Buffor_modem buffor_modem, Frame frame) {
		this.in = in;
		this.buffor_modem = buffor_modem;
		wiriter = new SerialWiriter_modem(buffor_modem);
		

		reader = new SerialReader_modem(buffor_modem);
		
		this.frame = frame;
	}

	String answer_position = "";
	String answer_speed = "";
	
	

	int speed = 0;
	
		static float Latitude2Decimal(String lat, String NS) {
			float med = Float.parseFloat(lat.substring(2)) / 60.0f;
			med += Float.parseFloat(lat.substring(0, 2));
			if (NS.startsWith("S")) {
				med = -med;
			}
			return med;
		}
		
	
		static float Longitude2Decimal(String lon, String WE) {
			float med = Float.parseFloat(lon.substring(3))/60.0f;
			med +=  Float.parseFloat(lon.substring(0, 3));
			if(WE.startsWith("W")) {
				med = -med;
			}
			return med;
		}
	

	@SuppressWarnings("deprecation")
	public synchronized void run() {
		
		try {
	
			buffor_modem.test_modemu();
		} catch (IOException e1) {
			
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			
			e1.printStackTrace();
		}
		
		byte[] buffer = new byte[1024];
		int len = -1;

		try {
			while ((len = this.in.read(buffer)) > -1) {

				answer_position = new String(buffer, 0, len);
				answer_speed = new String(buffer, 0, len);

				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				int left = answer_position.indexOf("$GPGGA");
				if (left > 0) {
					answer_position = answer_position.substring(left, len);
					int right = answer_position.indexOf("\n");
					answer_position = answer_position.substring(0, right);
					frame.GPS.setText(answer_position);

				}

				int left1 = answer_speed.indexOf("$GPVTG");
				if (left1 > 0) {
					answer_speed = answer_speed.substring(left1, len);
					int right1 = answer_speed.indexOf("\n");
					answer_speed = answer_speed.substring(0, right1);

				}

				try {

					String tablica_position[] = answer_position.split(",");
					String tablica_speed[] = answer_speed.split(",");

					frame.Fix_quality.setText(tablica_position[6]);
					frame.Number_of_satellites.setText(tablica_position[7]);
					frame.Speed.setText(tablica_speed[7]);
					
					buffor_modem.latitude= Float.toString(Latitude2Decimal(tablica_position[2], tablica_position[3]));
					
					buffor_modem.longitude=Float.toString(Longitude2Decimal(tablica_position[4], tablica_position[5]));
					

				} catch (Exception e) {
					
				}

				
				
				try {
					speed = Integer.parseInt(frame.test_speed.getText());

				} catch (Exception e) {
					// TODO: handle exception
				}

				if (speed < 10 && !wiriter.isAlive()) {

					try {
						buffor_modem.odczyt_wiadomosc();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("tylko odczyt wiadomosci");
					frame.Prasing_GPS_to_serwer.setText(""
							+ "tylko odczyt wiadomosci");
				}

				if (speed >= 10 && buffor_modem.event_kind == 0) {

					System.out.println("start watku");

					buffor_modem.event_kind = 1;
					wiriter.start();
					reader.start();

				}
				
				
				if (speed >= 10 && buffor_modem.event_kind == 1) {

					wiriter.id_command++;
					
				
					if (wiriter.id_command ==10)
						wiriter.id_command = 7;
					
	
				}			
				
				if (speed < 10 && buffor_modem.event_kind == 1) {

					System.out.println("stop watku zapis do bazy");

					if (wiriter.id_command >= 7) {
						wiriter.id_command = 6;

						buffor_modem.event_kind = 2;

						System.out.println("ustawieinie komedy na 7 i event kind na 2");
					}

				}
					
				System.err.println("speed"+speed+" ktora komenda "  + wiriter.id_command +" buffor_modem_event "+buffor_modem.event_kind);


				if (speed < 10 && buffor_modem.event_kind == 2 && wiriter.id_command == 9) {

					System.out.println("usypianie");
					wiriter.suspend();
					reader.suspend();
					wiriter.id_command = 1;
				}

				if (speed >= 10 && wiriter.isAlive() && buffor_modem.event_kind == 2) {

					System.out.println("resume watku");

					buffor_modem.event_kind = 1;
					wiriter.resume();
					reader.resume();
				}

			

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}