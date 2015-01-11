package ui;

import java.awt.*;

import javax.swing.*;

import system.*;
import ui.listener.CustActionListener;

public class WindowLogin extends JFrame {

	final private Core core;

	private JButton btnLogin;
	private JTextField txUsr;
	private JPasswordField txPsw;
	private JLabel lblUsr, lblPsw;

	private Container container;
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	public WindowLogin(Core core) {
		super("Login");
		setIconImage(Toolkit.getDefaultToolkit().getImage("D:\\PROJECT\\logo_only.jpg"));
		getContentPane().setBackground(Color.LIGHT_GRAY);
		setForeground(SystemColor.textHighlight);
		getContentPane().setForeground(Color.WHITE);
		this.core = core;

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(402, 333);
		setLocation((screenSize.width - getWidth()) / 2,
				(screenSize.height - getHeight()) / 2);
		JLabel labelHeader = new JLabel("<HTML><h2>Welcome to Toko Herbal</h2></HTML>");
		labelHeader.setForeground(Color.DARK_GRAY);
		labelHeader.setBounds(75,12,250,20);
		
		container = this.getContentPane();
		container.setLayout(null);
		//container.setBackground(Color.WHITE);
		btnLogin = new JButton("<HTML><H3>Login</H3></HTML>");
		btnLogin.setForeground(SystemColor.desktop);
		btnLogin.setBackground(Color.LIGHT_GRAY);
		txUsr = new JTextField(15);
		txPsw = new JPasswordField(15);
		lblUsr = new JLabel("<HTML><H3>Username</H3></HTML>");
		lblPsw = new JLabel("<HTML><H3>Password</H3></HTML>");

		lblUsr.setHorizontalAlignment(JLabel.RIGHT);
		lblPsw.setHorizontalAlignment(JLabel.RIGHT);

		lblUsr.setBounds(10, 166, 70, 20);
		txUsr.setBounds(100, 166, 180, 25);
		lblPsw.setBounds(10, 202, 70, 20);
		txPsw.setBounds(100, 202, 180, 25);
		btnLogin.setBounds(75, 238, 235, 30);

		btnLogin.addActionListener(new CustActionListener(core, this, btnLogin));
		container.add(labelHeader);
		container.add(lblUsr);
		container.add(txUsr);
		container.add(lblPsw);
		container.add(txPsw);
		container.add(btnLogin);
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon("D:\\PROJECT\\logo.jpg"));
		label.setBounds(132, 43, 100, 101);
		getContentPane().add(label);
	}

	public String getUser() {
		return txUsr.getText();
	}

	public String getPass() {
		return txPsw.getText();
	}
}
