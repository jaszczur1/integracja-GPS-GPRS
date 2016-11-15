package projekcik;

import java.awt.List;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberInputStream;
import java.io.OutputStream;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.IllformedLocaleException;
import java.util.Stack;

import javax.swing.plaf.SliderUI;

public class Buffor_modem {

	static InputStream out_gps;
	static OutputStream out_modem;
	static InputStream in_modem;

	static Frame frame;

	static String answer_GPS;

	public Buffor_modem(OutputStream out_modem, InputStream in_modem, Frame frame, InputStream out_gps, String login,
			String password) {
		this.in_modem = in_modem;
		this.out_modem = out_modem;
		this.frame = frame;
		this.out_gps = out_gps;
		this.login = login;
		this.password = password;
	}

	String login;
	String password;
	int event_kind = 0;
	String latitude = "";
	String longitude = "";
	double speed = 0;

	String AT_set_postion_htpp = "";
	String At_command_sms = "";
//	int id_command = 0;
	int type_command;
	int id_sms = 0;

	String test_answer;

	boolean test_modemu() throws IOException, InterruptedException {

		
		Thread.sleep(1000);
		String test = new String("AT");

		byte[] test_byte = test.getBytes();

		out_modem.write(test_byte);

		Thread.sleep(200);
		
		try {

			test_answer = new String(opowiedz_sms_zmodemu());
			System.out.println(test_answer);

		} catch (Exception e) {

			frame.Prasing_GPS_to_serwer.setText("W³¹cz modem");
		}

		
		
		
		while (test_answer==null) {

			frame.Prasing_GPS_to_serwer.setText("zresetuj modem");
			test_answer = opowiedz_sms_zmodemu();

			Thread.sleep(2000);
		}

		frame.Prasing_GPS_to_serwer.setText("test ok");

		return true;
	}

	void set_login_password(String login, String password) {
		this.login = login;
		this.password = password;
		System.err.println(password + " dane logowania" + login);
	}

	String pisz_nowego_sms_Switch(int i, String phoneNumber, String message_text, String message_text1)
			throws InterruptedException {
		String At_sms = null;
		switch (i) {

		case 0:
			At_sms = "AT+CMGS=\"" + phoneNumber + "\"\r\n";
			Thread.sleep(300);
			System.err.println(At_sms);
			
			
			break;
		case 1:
			At_sms = message_text + " " + message_text1 + '\032';
			System.err.println(At_sms);
			Thread.sleep(800);
			break;
		}
		return At_sms;
	}

	void pisz_nowego_sms(String phoneNumber, String message_text, String message_text1)
			throws IOException, InterruptedException {

		byte[] b1 = new byte[1024];
		int j = 0;
		while (j < 2) {
			Thread.sleep(300);
			System.out.println("pisze nowego sms");
			System.out.println(phoneNumber + " " + message_text);
			b1 = pisz_nowego_sms_Switch(j, phoneNumber, message_text, message_text1).getBytes();
			out_modem.write(b1);
			
			j++;
		}
	}

	String _smsy_czytaj_wszystkie(int j) throws InterruptedException {
		String At_sms = null;
		switch (j) {
		case 0:
			At_sms = "AT+CMGF=1\r\n";

			break;
		case 1:
			At_sms = "AT+CMGL=\"ALL\"\r\n";
			break;

		}
		return At_sms;
	}

	public String opowiedz_sms_zmodemu() throws InterruptedException {


		System.out.println("czytam _SMS");

		String answer_modem = null;

		byte[] buffer = new byte[2048];
		int len = -1;
		try {
			while ((len = in_modem.read(buffer)) > -1) {
				if (len > 0) {
					answer_modem = new String(buffer, 0, len);
					
				}
				if (len == 0) {
					
					break;
				} 
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return answer_modem;
	}

	String _sms_czytaj() throws IOException, InterruptedException {

		byte[] b = new byte[2048];

		for (int i = 1; i < 2; i++) {

			b = _smsy_czytaj_wszystkie(i).getBytes();

			out_modem.write(b);
			Thread.sleep(1300);
		}

		return opowiedz_sms_zmodemu();
	}

	ArrayList<String> list_sms_tresc;

	public ArrayList<String> list_sms_tresc_zwroc() {
		return list_sms_tresc;
	}

	int licznik_sms = 0;

	byte[] b = new byte[2048];

	public void odczyt_wiadomosc() throws InterruptedException, IOException {

		String answer = _sms_czytaj();


		String answer_numer_tel = answer;

		int left = 0;
		int right = 0;
		int right2 = 0;
		String answer_pom;

		String answer_tresc_sms = "";

		int i = 1;

		int y = 1;

		if (list_sms_tresc == null) {

			System.out.println("tworze liste");
			list_sms_tresc = new ArrayList<String>();
		}

		if (frame.Select_messege.getItemCount() == 0) {

			System.out.println("zape³niam pusta liste");

			
			
			
			while ((left = answer_numer_tel.indexOf("+CMGL: " + i)) > 0) {

				if (left < 0)
					return;

				answer_numer_tel = answer_numer_tel.substring(left);

				answer_pom = answer_numer_tel;

				right = answer_numer_tel.indexOf("\n");

				answer_numer_tel = answer_numer_tel.substring(0, right);

				answer_numer_tel = answer_numer_tel.substring(7, right);

				frame.Select_messege.addItem(answer_numer_tel);

				answer_tresc_sms = answer_pom.substring(right + 1);

				right2 = answer_tresc_sms.indexOf("\n");

				answer_tresc_sms = answer_tresc_sms.substring(0, right2);

				list_sms_tresc.add(answer_tresc_sms);
				licznik_sms++;


				answer_numer_tel = answer_pom;
				i++;
			}

		}
		if (frame.Select_messege.getItemCount() > 0) {

			System.out.println(frame.Select_messege.getItemCount());

			System.out.println("lista zape³niona sprawdzam liste ponownie");

			int licznik = -1; // zrobic intód dla przeszukiwani;

			try {

				while ((left = answer_numer_tel.indexOf("+CMGL: " + y)) > 0) {


					licznik++;
		
					answer_numer_tel = answer_numer_tel.substring(left);

					answer_pom = answer_numer_tel;

					right = answer_numer_tel.indexOf("\n");

					answer_numer_tel = answer_numer_tel.substring(0, right);

					answer_numer_tel = answer_numer_tel.substring(7, right);

					if (licznik < frame.Select_messege.getItemCount()) {

						frame.Select_messege.insertItemAt(answer_numer_tel, licznik);
						frame.Select_messege.removeItemAt(licznik);

					}
					if (licznik == frame.Select_messege.getItemCount()) {

						System.out.println("DODAJE NA KOCIEC");

						frame.Select_messege.addItem(answer_numer_tel);

					

					}

					answer_tresc_sms = answer_pom.substring(right + 1);

					right2 = answer_tresc_sms.indexOf("\n");

					answer_tresc_sms = answer_tresc_sms.substring(0, right2);

					if (licznik < list_sms_tresc.size()) {

						list_sms_tresc.add(licznik, answer_tresc_sms);
						list_sms_tresc.remove(licznik);

					}

					if (licznik == list_sms_tresc.size()) {

						System.err.println("nadszed³ nowy sms zapis");

						list_sms_tresc.add(answer_tresc_sms);

						String odp = new String(answer_tresc_sms);
							
						System.err.println(odp);
						
						int left_pom = odp.indexOf("o");
						int right_pom = odp.indexOf("k");

						System.out.println(left_pom + "wynik " + right_pom);

						try {

							odp = odp.substring(left_pom, right_pom + 1);

						} catch (Exception e) {
							// TODO: handle exception
						}

						System.out.println(odp);

						if (odp.equals("ok")) {
							System.out.println("pokaz lokalizacje");

							

							int answer_numer_tel_pom = answer_numer_tel.indexOf("+");
							answer_numer_tel = answer_numer_tel.substring(answer_numer_tel_pom);
							answer_numer_tel_pom = answer_numer_tel.indexOf("\"");
							answer_numer_tel = answer_numer_tel.substring(0, answer_numer_tel_pom);

													

							pisz_nowego_sms(answer_numer_tel, latitude, longitude);
						}

					}

					answer_numer_tel = answer_pom;
					y++;
				}

			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	}

	int start = 1;

	String _http(int id_command_htpp) throws InterruptedException {

		switch (id_command_htpp) {
		case 0:
			AT_set_postion_htpp = "AT\r\n";
			break;
		case 1:
			AT_set_postion_htpp = "AT+SAPBR=3,1,\"Contype\",\"GPRS\"\r\n";
			System.out.println("set gps");
			break;
		case 2:
			AT_set_postion_htpp = "AT+SAPBR=3,1,\"APN\",\"internet\"\r\n";
			System.out.println("set apn");
			break;
		case 3:
			AT_set_postion_htpp = "AT+SAPBR=1,1\r\n";
			System.out.println("enable gps");
			Thread.sleep(1000);
			break;
		case 4:
			AT_set_postion_htpp = "AT+SAPBR=2,1\r\n";
			System.out.println("get ip aderss");
			Thread.sleep(1000);
			break;
		case 5:
			AT_set_postion_htpp = "AT+HTTPINIT\r\n";
			break;
		case 6:
			AT_set_postion_htpp = "AT+HTTPPARA=\"CID\",1\r\n";
			break;
		case 7:
			System.out.println("otweiranie strony");
			AT_set_postion_htpp = "AT+HTTPPARA=\"URL\",\"http://rpi83.webd.pl/update_database.php?login=" + login
					+ "&password=" + password + "&event_kind=" + event_kind + "&latitude=" + latitude + "&longitude="
					+ longitude + "&speed=" + speed + "\"\r\n";
			break;
		case 8:
			AT_set_postion_htpp = "AT+HTTPACTION=0\r\n";
			break;
	
		}

		return AT_set_postion_htpp;
	}

	boolean czy_mozan_pisac = true;

	boolean czy_mozna_czytac = false;

	public synchronized void pisz(int id_command) throws InterruptedException, IOException {
		
		
		String At = "";

		At = _http(id_command);

		byte[] b = new byte[1024];

		b = At.getBytes();

		this.out_modem.write(b);
		
		czy_mozna_czytac = true;
		
		wait();
		Thread.sleep(1000);
	}

	public synchronized void czytam() throws InterruptedException, IOException {
		
		
		
		String answer_modem = null;
	
		if (czy_mozna_czytac == true) {
	
			byte[] buffer = new byte[1024];
			int len = -1;

			try {
				while ((len = Buffor_modem.in_modem.read(buffer)) > -1) {
					if (len > 0) {
						answer_modem = new String(buffer, 0, len);
						System.out.println(answer_modem);
					}
					if (len == 0) {

						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			czy_mozna_czytac = false;


			Thread.sleep(1000);

			notifyAll();
		}
	}
}