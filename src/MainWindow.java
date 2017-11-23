import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;


public class MainWindow {

	private String [] hexaCheatSheet = {"0x00000000", "0x80000000", "0xc0000000", "0xe0000000","0xf0000000","0xf8000000","0xfc000000", "0xfe000000", "0xff000000", "0xff800000", "0xffc00000", "0xffe00000", "0xfff00000", "0xfff80000", "0xfffc0000", "0xfffe0000", "0xffff0000", "0xffff8000", "0xffffc000", "0xffffe000", "0xfffff000", "0xfffff800", "0xfffffc00", "0xfffffe00","0xffffff00", "0xffffff80", "0xffffffc0","0xffffffe0", "0xfffffff0", "0xfffffff8", "0xfffffffc", "0xfffffffe","0xffffffff"};
	private String [] subnetDottedDeci= {"0.0.0.0", "128.0.0.0", "192.0.0.0", "224.0.0.0", "240.0.0.0", "248.0.0.0", "252.0.0.0", "254.0.0.0", "	255.0.0.0", "255.128.0.0", "255.192.0.0", "255.224.0.0", "255.240.0.0", "255.248.0.0", "255.252.0.0", "255.254.0.0", "255.255.0.0", "255.255.128.0", "255.255.192.0", "255.255.224.0", "255.255.240.0", "255.255.248.0", "255.255.252.0", "255.255.254.0", "255.255.255.0", "255.255.255.128", "255.255.255.192", "255.255.255.224", "255.255.255.240", "255.255.255.248", "255.255.255.252", "255.255.255.254", "255.255.255.255"};
	private JFrame frmIpConfigurationFor;
	private JTextField connectionStatusText;
	private JTextField ipv4Text;
	private JTextField ipv6Text;
	private JTextField dnsSuffixText;
	private JTextField macAdText;
	private JTextField snMaskText;
	String dnsSuffix, ipv6, ipv4, subnetMask, macAd, connectedness;
	private JTextField urlTextField;
	private JButton goBtn;
	private JComboBox<String> commandBox;
	private JTextField txtResult;

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
		String[] a = executeCommand(command());
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
					for(int i=index;;i++) {
						if(a[i].contains("nameserver")) {
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
				dnsSuffix = collectedData[collectedData.length-1];
				subnetMask = collectedData[3].substring(endOfIpv+7,endOfNet);
				for(int i = 0; i < hexaCheatSheet.length; i++) {
					if(hexaCheatSheet[i].equals(subnetMask.trim())) {
						subnetMask = subnetDottedDeci[i];
						break;
					}
					else {
						System.out.println("Roied");
					}
				}
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
		frmIpConfigurationFor.setBounds(10, 10, 555, 470);
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
		btnNewButton.setBounds(6, 315, 512, 29);
		frmIpConfigurationFor.getContentPane().add(btnNewButton);

		urlTextField = new JTextField();
		urlTextField.setBounds(132, 356, 268, 26);
		frmIpConfigurationFor.getContentPane().add(urlTextField);
		urlTextField.setColumns(10);
		urlTextField.setText("enter url here");

		goBtn = new JButton("Go!!");
		goBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(System.getProperty("os.name").contains("Window")) {
					if(commandBox.getSelectedItem().toString().equalsIgnoreCase("ping")) {
						String[] a = executeCommand(new String[] {"ping "+urlTextField.getText()});
						for(String x : a) {
							System.out.println(x);
						}
						txtResult.setText(a[a.length-1]);
					}
				} else {
					if(commandBox.getSelectedItem().toString().equalsIgnoreCase("ping")) {
						String[] a = executeCommand(new String[] {"ping -i .1  -t 5 "+urlTextField.getText()});
						for(String x : a) {
							System.out.println(x);
						}
						txtResult.setText(a[a.length-2]);
					}
				}
				
			}
		});
		goBtn.setBounds(416, 356, 117, 29);
		frmIpConfigurationFor.getContentPane().add(goBtn);

		commandBox = new JComboBox<String>();
		commandBox.setBounds(27, 356, 81, 27);
		frmIpConfigurationFor.getContentPane().add(commandBox);
		commandBox.addItem("Ping");

		txtResult = new JTextField();
		txtResult.setEditable(false);
		txtResult.setText("result");
		txtResult.setBounds(27, 394, 500, 26);
		frmIpConfigurationFor.getContentPane().add(txtResult);
		txtResult.setColumns(10);
	}

	private String[] command() {
		String[] a;
		try {
			if(System.getProperty("os.name").contains("Window")) {
				a = new String[] {"ipconfig /all"};
			} //Windows
			else {
				a = new String[] {"ifconfig","cat /etc/resolv.conf"};
			} //Mac and others

			return a;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String[] executeCommand(String[] commands) {
		Process command;
		String output = "";
		BufferedReader buf;
		try {
			for(String x : commands) {
				command = Runtime.getRuntime().exec(x);
				buf = new BufferedReader(new InputStreamReader(command.getInputStream()));
				String line = "";
				while ((line = buf.readLine()) != null) {
					System.out.println(line);
					output += line + "split_here";
				}
			}

			String[] a = output.split("split_here");
			return a;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
}
