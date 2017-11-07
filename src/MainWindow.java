import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.GridBagLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.JLabel;

public class MainWindow {

	private JFrame frmIpConfigurationFor;

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
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmIpConfigurationFor = new JFrame();
		frmIpConfigurationFor.setTitle("IP Configuration for Wi-Fi Connection");
		frmIpConfigurationFor.setResizable(false);
		frmIpConfigurationFor.setBounds(10, 10, 555, 386);
		frmIpConfigurationFor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmIpConfigurationFor.getContentPane().setLayout(null);
	}
}
