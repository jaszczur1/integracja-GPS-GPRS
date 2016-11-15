package projekcik;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.SpringLayout;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.util.ArrayList;
import java.util.EventObject;
import java.awt.event.ActionEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;

public class Frame extends JFrame implements ActionListener {

	public JTextArea GPS ;
	public JTextField Fix_quality;
	private JPanel contentPane;
	JTextField Number_of_satellites;
	JTextField Speed;
	JComboBox Select_messege;
	private JComboBox Select_Com_GPS;
	private JComboBox Select_Com_modem;
	private JTextField number_out_message;
	private JButton btnSend;
	private JTextArea message_out;
	JTextArea Prasing_GPS_to_serwer;
	JTextArea Messege_in;
	
	Main m;
	
	Buffor_modem buffor_modem;

	String[] list_port;
	private JTextField Login;
	private JTextField Password;
	public JTextField test_speed;
	

	public Frame(Main m) {

		this.m = m; 
		
		list_port = m.show_list();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);

		GPS = new JTextArea();
		sl_contentPane.putConstraint(SpringLayout.NORTH, GPS, 33, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, GPS, -464, SpringLayout.EAST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, GPS, 194, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, GPS, -15, SpringLayout.EAST, contentPane);
		contentPane.add(GPS);

		Prasing_GPS_to_serwer = new JTextArea();
		sl_contentPane.putConstraint(SpringLayout.EAST, Prasing_GPS_to_serwer, -35, SpringLayout.EAST, contentPane);
		contentPane.add(Prasing_GPS_to_serwer);

		Fix_quality = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, Fix_quality, 15, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, Fix_quality, 37, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, Fix_quality, 180, SpringLayout.WEST, contentPane);
		contentPane.add(Fix_quality);
		Fix_quality.setColumns(10);

		Number_of_satellites = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, Number_of_satellites, 26, SpringLayout.SOUTH, Fix_quality);
		sl_contentPane.putConstraint(SpringLayout.WEST, Number_of_satellites, 37, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, Number_of_satellites, 0, SpringLayout.EAST, Fix_quality);
		contentPane.add(Number_of_satellites);
		Number_of_satellites.setColumns(10);

		Speed = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, Speed, 25, SpringLayout.SOUTH, Number_of_satellites);
		sl_contentPane.putConstraint(SpringLayout.WEST, Speed, 0, SpringLayout.WEST, Fix_quality);
		sl_contentPane.putConstraint(SpringLayout.EAST, Speed, 0, SpringLayout.EAST, Fix_quality);
		Speed.setColumns(10);
		contentPane.add(Speed);

		Messege_in = new JTextArea();
		sl_contentPane.putConstraint(SpringLayout.WEST, Prasing_GPS_to_serwer, 109, SpringLayout.EAST, Messege_in);
		sl_contentPane.putConstraint(SpringLayout.EAST, Messege_in, 293, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.NORTH, Messege_in, 26, SpringLayout.SOUTH, Speed);
		sl_contentPane.putConstraint(SpringLayout.WEST, Messege_in, 0, SpringLayout.WEST, Fix_quality);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, Messege_in, -253, SpringLayout.SOUTH, contentPane);
		contentPane.add(Messege_in);

		message_out = new JTextArea();
		sl_contentPane.putConstraint(SpringLayout.WEST, message_out, 37, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, message_out, -182, SpringLayout.WEST, Prasing_GPS_to_serwer);
		contentPane.add(message_out);

		Select_messege = new JComboBox();
		sl_contentPane.putConstraint(SpringLayout.NORTH, Select_messege, 174, SpringLayout.SOUTH, Speed);
		sl_contentPane.putConstraint(SpringLayout.WEST, Select_messege, 37, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, Select_messege, -172, SpringLayout.WEST, Prasing_GPS_to_serwer);
		sl_contentPane.putConstraint(SpringLayout.NORTH, message_out, 1, SpringLayout.SOUTH, Select_messege);
		Select_messege.addActionListener(this);
		contentPane.add(Select_messege);

		number_out_message = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, number_out_message, 493, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, number_out_message, 0, SpringLayout.WEST, Fix_quality);
		sl_contentPane.putConstraint(SpringLayout.EAST, number_out_message, -628, SpringLayout.EAST, contentPane);
		contentPane.add(number_out_message);
		number_out_message.setColumns(10);

		btnSend = new JButton("Send Message");
		sl_contentPane.putConstraint(SpringLayout.WEST, btnSend, 6, SpringLayout.EAST, number_out_message);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, message_out, -18, SpringLayout.NORTH, btnSend);
		sl_contentPane.putConstraint(SpringLayout.NORTH, btnSend, 492, SpringLayout.NORTH, contentPane);
		btnSend.addActionListener(this);
		contentPane.add(btnSend);
		

		Select_Com_GPS = new JComboBox(list_port);
		sl_contentPane.putConstraint(SpringLayout.NORTH, Select_Com_GPS, 207, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, Select_Com_GPS, 109, SpringLayout.EAST, Messege_in);
		sl_contentPane.putConstraint(SpringLayout.EAST, Select_Com_GPS, -271, SpringLayout.EAST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.NORTH, Prasing_GPS_to_serwer, 37, SpringLayout.SOUTH, Select_Com_GPS);
		Select_Com_GPS.addActionListener(this);
		contentPane.add(Select_Com_GPS);

		Select_Com_modem = new JComboBox(list_port);
		sl_contentPane.putConstraint(SpringLayout.WEST, Select_Com_modem, 443, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, btnSend, -190, SpringLayout.WEST, Select_Com_modem);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, Prasing_GPS_to_serwer, -48, SpringLayout.NORTH, Select_Com_modem);
		sl_contentPane.putConstraint(SpringLayout.NORTH, Select_Com_modem, 0, SpringLayout.NORTH,
				number_out_message);
		Select_Com_modem.addActionListener(this);
		contentPane.add(Select_Com_modem);
		
		Login = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.WEST, Login, 568, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, Select_Com_modem, -65, SpringLayout.WEST, Login);
		sl_contentPane.putConstraint(SpringLayout.NORTH, Login, 0, SpringLayout.NORTH, number_out_message);
		contentPane.add(Login);
		Login.setColumns(10);
		
		Password = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.WEST, Password, 669, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, Password, -35, SpringLayout.EAST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, Login, -26, SpringLayout.WEST, Password);
		sl_contentPane.putConstraint(SpringLayout.NORTH, Password, 0, SpringLayout.NORTH, number_out_message);
		contentPane.add(Password);
		Password.setColumns(10);
		
		test_speed = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.WEST, test_speed, 38, SpringLayout.EAST, btnSend);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, test_speed, -34, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, test_speed, 139, SpringLayout.EAST, btnSend);
		contentPane.add(test_speed);
		test_speed.setColumns(10);
		
	}
			
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == Select_Com_GPS) {
			try {

				buffor_modem=m.connect(list_port[0], Login.getText(),Password.getText());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		if (e.getSource() == Select_Com_modem) {
			try {
				
				
				buffor_modem= m.connect(list_port[1], Login.getText(), Password.getText());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

		if (e.getSource() == btnSend) {
			
			String message_text = message_out.getText();
		String 	phoneNumber = number_out_message.getText();
		System.out.println(message_text+" "+phoneNumber);
	
			try {
			
				buffor_modem.pisz_nowego_sms(phoneNumber, message_text, "Sim900");;
			} catch (InterruptedException | IOException e1) {
				e1.printStackTrace();
			}
		}


         if(e.getSource()==Select_messege){
       	 
        	 if(buffor_modem.list_sms_tresc.size() > 0)
        	 {   	
        		 int x= Select_messege.getSelectedIndex();
        		 try {
				
        			 Messege_in.setText(buffor_modem.list_sms_tresc.get(x).toString());
        			 
				} catch (Exception e2) {
					// TODO: handle exception
				}
       	         }
        	
        	 }
	}
}

