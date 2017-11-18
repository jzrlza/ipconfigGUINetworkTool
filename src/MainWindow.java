import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.InputStreamReader;

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
				for(int i = wifiIndex; i < wifiIndex+26; i++) {
					if(a[i].contains("Connection-specific DNS Suffix  . :")) {
						dnsSuffix = a[i].substring(39, a[i].length());
					}
					if(a[i].contains("IPv6 Address. . . . . . . . . . . :")) {
						ipv6 = a[i].substring(39, a[i].length());
					}
					if(a[i].contains("IPv4 Address. . . . . . . . . . . :")) {
						ipv4 = a[i].substring(39, a[i].length());
					}
					if(a[i].contains("Subnet Mask . . . . . . . . . . . :")) {
						subnetMask = a[i].substring(39, a[i].length());
					}
					if(a[i].contains("Physical Address. . . . . . . . . :")) {
						macAd = a[i].substring(39, a[i].length());
					}
				}
				connectedness = "Connected";
			} else {
				for(int i = wifiIndex; i < wifiIndex+7; i++) {
					if(a[i].contains("Connection-specific DNS Suffix  . :")) {
						dnsSuffix = a[i].substring(39, a[i].length());
					}
					if(a[i].contains("Physical Address. . . . . . . . . :")) {
						macAd = a[i].substring(39, a[i].length());
					}
				}
				ipv6 = "Cannot Currently Display, Media Disconnected";
				ipv4 = "Cannot Currently Display, Media Disconnected";
				subnetMask = "Cannot Currently Display, Media Disconnected";
				connectedness = "Disconnected";
			}
		}
		
		//for unix base os.
		else {
			String[] collectedData;
			String line = "";
			int index = 0;
			for(String x : a) {
				if(x.contains("en0: flags=")) {
					for(int i=index;i<index+7;i++) {
						if(a[i].contains("en1: flags=")) {
							break;
						}
						line += a[i]+" -split_here- ";
						if(a[i].contains("status: active")) {
							connected = true;
						}
					}
				}
				if(x.contains("domain ")) {
					line += x;
					break;
				}
				index++;
			}
			collectedData = line.split(" -split_here- ");
			for(String x : collectedData) {
				System.out.println(x);
			}
			macAd = collectedData[1].substring(6);
			if(connected) {
				int endOfIpv6 = collectedData[2].indexOf("prefixlen");
				int endOfIpv = collectedData[3].indexOf("netmask");
				int endOfNet = collectedData[3].indexOf("broadcast");
				ipv6 = collectedData[2].substring(6,endOfIpv6);
				ipv4 = collectedData[3].substring(5,endOfIpv);
				dnsSuffix = collectedData[7].substring(7);
				subnetMask = collectedData[3].substring(endOfIpv+7,endOfNet);
				connectedness = "Connected";
			}
			else {
				dnsSuffix = "Cannot Currently Display, Media Disconnected";
				ipv6 = "Cannot Currently Display, Media Disconnected";
				ipv4 = "Cannot Currently Display, Media Disconnected";
				subnetMask = "Cannot Currently Display, Media Disconnected";
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
			
			if(!System.getProperty("os.name").contains("Window")) {
				command = Runtime.getRuntime().exec("cat /etc/resolv.conf");
				buf = new BufferedReader(new InputStreamReader(command.getInputStream()));
				while ((line = buf.readLine()) != null) {
					output += line + "split_here";
				}
			}//to get DNS of unix base os.

			String[] a = output.split("split_here");
			
			
			return a;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
}
