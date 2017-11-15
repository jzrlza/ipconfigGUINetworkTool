import java.awt.EventQueue;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.StringTokenizer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class MainWindow {

	private JFrame frmIpConfigurationFor;
	private JTextField connectionStatusText;
	private JTextField ipv4Text;
	private JTextField ipv6Text;
	private JTextField dnsSuffixText;
	private JTextField macAdText;
	private JTextField snMaskText;
	String dnsSuffix, ipv6, ipv4, subnetMask, macAd, connectedness;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frmIpConfigurationFor.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
		refresh();
		
		
	}
	
	 public void refresh() {
		 String[] a = executeCommand();
			boolean connected = false;
			if(System.getProperty("os.name").contains("Window")) {
				int wifiIndex = 0;
				for(String x : a) {
					if(x.contains("Wireless LAN adapter Wi-Fi")) {
						break;
					}
					//System.out.println(x);
					
					wifiIndex++;
				}
				if(!a[wifiIndex+2].contains("Media State . . . . . . . . . . . : Media disconnected")) {
					connected = true;
				}
				if(connected) {
					dnsSuffix = a[wifiIndex+2].substring(39, a[wifiIndex+2].length());
					ipv6 = a[wifiIndex+7].substring(39, a[wifiIndex+7].length());
					ipv4 = a[wifiIndex+11].substring(39, a[wifiIndex+11].length());
					subnetMask = a[wifiIndex+12].substring(39, a[wifiIndex+12].length());
					macAd = a[wifiIndex+4].substring(39, a[wifiIndex+4].length());
					connectedness = "Connected";
				} else {
					dnsSuffix = a[wifiIndex+3].substring(39, a[wifiIndex+3].length());
					ipv6 = "Cannot Currently Display, Media Disconnected";
					ipv4 = "Cannot Currently Display, Media Disconnected";
					subnetMask = "Cannot Currently Display, Media Disconnected";
					macAd = a[wifiIndex+5].substring(39, a[wifiIndex+5].length());
					connectedness = "Disconnected";
				}
				
			}
			

			dnsSuffixText.setText(dnsSuffix);
			ipv6Text.setText(ipv6);
			ipv4Text.setText(ipv4);
			snMaskText.setText(subnetMask);
			macAdText.setText(macAd);
			connectionStatusText.setText(connectedness);
	 }
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmIpConfigurationFor = new JFrame();
		frmIpConfigurationFor.setTitle("IP Configuration for Wi-Fi Connection");
		frmIpConfigurationFor.setResizable(false);
		frmIpConfigurationFor.setBounds(10, 10, 555, 454);
		frmIpConfigurationFor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmIpConfigurationFor.getContentPane().setLayout(null);
		
		JLabel lblConnectionStatus = new JLabel("Connection Status: ");
		lblConnectionStatus.setBounds(27, 29, 179, 16);
		frmIpConfigurationFor.getContentPane().add(lblConnectionStatus);
		
		JLabel lblIpv = new JLabel("IPv4");
		lblIpv.setBounds(27, 86, 112, 16);
		frmIpConfigurationFor.getContentPane().add(lblIpv);
		
		JLabel lblIpv_1 = new JLabel("IPv6");
		lblIpv_1.setBounds(27, 132, 112, 16);
		frmIpConfigurationFor.getContentPane().add(lblIpv_1);
		
		JLabel lblDnsSuffix = new JLabel("DNS Suffix");
		lblDnsSuffix.setBounds(27, 183, 112, 16);
		frmIpConfigurationFor.getContentPane().add(lblDnsSuffix);
		
		JLabel lblMacAddress = new JLabel("MAC Address");
		lblMacAddress.setBounds(27, 236, 112, 16);
		frmIpConfigurationFor.getContentPane().add(lblMacAddress);
		
		JLabel lblSubnetMask = new JLabel("Subnet Mask");
		lblSubnetMask.setBounds(27, 287, 112, 16);
		frmIpConfigurationFor.getContentPane().add(lblSubnetMask);
		
		connectionStatusText = new JTextField();
		connectionStatusText.setEditable(false);
		connectionStatusText.setBounds(208, 26, 310, 22);
		frmIpConfigurationFor.getContentPane().add(connectionStatusText);
		connectionStatusText.setColumns(10);
		
		ipv4Text = new JTextField();
		ipv4Text.setEditable(false);
		ipv4Text.setBounds(208, 83, 310, 22);
		frmIpConfigurationFor.getContentPane().add(ipv4Text);
		ipv4Text.setColumns(10);
		
		ipv6Text = new JTextField();
		ipv6Text.setEditable(false);
		ipv6Text.setBounds(208, 129, 310, 22);
		frmIpConfigurationFor.getContentPane().add(ipv6Text);
		ipv6Text.setColumns(10);
		
		dnsSuffixText = new JTextField();
		dnsSuffixText.setEditable(false);
		dnsSuffixText.setBounds(208, 180, 310, 22);
		frmIpConfigurationFor.getContentPane().add(dnsSuffixText);
		dnsSuffixText.setColumns(10);
		
		macAdText = new JTextField();
		macAdText.setEditable(false);
		macAdText.setBounds(208, 233, 310, 22);
		frmIpConfigurationFor.getContentPane().add(macAdText);
		macAdText.setColumns(10);
		
		snMaskText = new JTextField();
		snMaskText.setEditable(false);
		snMaskText.setBounds(208, 284, 310, 22);
		frmIpConfigurationFor.getContentPane().add(snMaskText);
		snMaskText.setColumns(10);
		
		JButton btnNewButton = new JButton("Refresh");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				refresh();
			}
		});
		btnNewButton.setBounds(12, 328, 512, 78);
		frmIpConfigurationFor.getContentPane().add(btnNewButton);
	}
	
	
	
	private String[] executeCommand() {
		Process command;
		try {
			if(System.getProperty("os.name").contains("Window")) {
				command = Runtime.getRuntime().exec("ipconfig /all");
			} //Windows
			else {
				command = Runtime.getRuntime().exec("ifconfig");
			} //Mac and others
			//command.waitFor(); not work for ipconfig /all
			BufferedReader buf = new BufferedReader(new InputStreamReader(command.getInputStream()));
			String line = "";
			String output = "";
			while ((line = buf.readLine()) != null) {
				output += line + "split_here";
			}
			String[] a = output.split("split_here");
			//for(String x : a) {
				//if(x.contains("Wi-Fi"))
			//	System.out.println(x);
			//}
			return a;

			/*
			 * This code below is for collecting data like ipv4 , 6 and etc. but not done yet.
			 */

			//			int check = 0,loca = 1;
			//			String[] info = new String[6];
			//			for(String x : a) {
			//				System.out.println(x);
			//				if(x.contains("en0: ")) {
			//					check = 1;
			//				}
			//				if(check == 1) {
			//					if(x.contains("inet")) {
			//						info[loca] = x;
			//						loca++;
			//					}
			//				}
			//			}
			//			System.out.println("==============");
			//			System.out.println(info[1]);
			//			System.out.println(info[2]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
}
