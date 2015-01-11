package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import object.*;
import system.*;
import ui.listener.CustActionListener;

public class WindowUsers extends JFrame {
	private Core core;
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private JTable tbl;
	private JCheckBox ckIsAdmin;
	private boolean admin;
	private JTextField tfUsername, tfPassword;
	private JLabel lbTambahUser, lbNewPass, lbAdmin;

	private Vector<User> users = new Vector<User>();
	public WindowUsers(Core core) {
		super("Maintenance Users");
		setIconImage(Toolkit.getDefaultToolkit().getImage("D:\\PROJECT\\logo_only.jpg"));
		getContentPane().setBackground(Color.LIGHT_GRAY);
		this.core = core;

		setResizable(false);
		setSize(750, 360);
		setLocation((screenSize.width - getWidth()) / 2,(screenSize.height - getHeight()) / 2);
		
		ResultSet rs = Operator.getListUser(core.getConnection());
		try {
			while (rs.next()) {
				users.add(new User(rs.getString(1), rs.getString(2), rs.getBoolean(3)));
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
		tbl = new JTable(Operator.resultSetToTableModel(Operator
				.getListUser(core.getConnection())));
		Operator.disableTableEdit(tbl);
		getContentPane().setLayout(null);
		JPanel pan = new JPanel(null);
		pan.setBackground(Color.WHITE);
		pan.setBounds(10, 10, getWidth()-270, 300);
		pan.setLayout(new BorderLayout());
		pan.add(tbl, BorderLayout.CENTER);
		pan.add(new JScrollPane(tbl), BorderLayout.CENTER);
		pan.add(tbl.getTableHeader(), BorderLayout.NORTH);
		
		lbTambahUser = new JLabel("Username");
		lbNewPass = new JLabel("Password");
		lbAdmin = new JLabel("Admin");
		
		lbTambahUser.setBounds(510, 10, 80, 25);
		lbNewPass.setBounds(510, 40, 80, 25);
		lbAdmin.setBounds(510, 70, 80, 25);
		
		tfUsername = new JTextField();
		tfPassword = new JTextField();
		ckIsAdmin = new JCheckBox(" Yes");
		ckIsAdmin.setBackground(Color.LIGHT_GRAY);
		
		tfUsername.setBounds(595, 10, 120, 25);
		tfPassword.setBounds(595, 40, 120, 25);
		ckIsAdmin.setBounds(592, 70, 80, 25);
		
		JButton buttonTambah = new JButton("Add");
		JButton buttonDelete = new JButton("Delete");

		buttonTambah.setBounds(630, 120, 80, 25);
		buttonTambah.setForeground(Color.BLACK);
		buttonTambah.setBackground(Color.WHITE);
		buttonTambah.addActionListener(new CustActionListener(core, this,tbl,
				buttonTambah, CustActionListener.TAMBAH_USER));
		buttonDelete.setBounds(530, 120, 80, 25);
		buttonDelete.setForeground(Color.BLACK);
		buttonDelete.setBackground(Color.WHITE);
		buttonDelete.addActionListener(new CustActionListener(core, this,tbl,
				buttonDelete, CustActionListener.HAPUS_USER));
		
		getContentPane().add(lbTambahUser);
		getContentPane().add(tfUsername);
		getContentPane().add(lbNewPass);
		getContentPane().add(tfPassword);
		getContentPane().add(lbAdmin);
		getContentPane().add(ckIsAdmin);
		getContentPane().add(buttonTambah);
		getContentPane().add(buttonDelete);
		getContentPane().add(pan);
	}
	
	public Vector<User> getListUser() {
		return users;
	}
	
	public User getSelectedUser() {
		return users.get(tbl.getSelectedRow());
	}

	public void submitToDB() {
		String isadmin;
		if (ckIsAdmin.isSelected() == true)
		{
			isadmin = "true";
		}
		else
		{
			isadmin = "false";
		}
		if (Operator.tambahUser(getUser(), core.getConnection())) {
			JOptionPane.showMessageDialog(this, "Data User telah ditambahkan!");
		} else {
			JOptionPane.showMessageDialog(this, "Terjadi kesalahan!");
		}
		
		((DefaultTableModel)tbl.getModel()).addRow(new Object[]{tfUsername.getText(),tfPassword.getText(),isadmin});

		tfUsername.setText("");
		tfPassword.setText("");
		ckIsAdmin.setSelected(false);
	}

	public void resetForm() {
		tfUsername.setText("");
		tfPassword.setText("");
		ckIsAdmin.setSelected(false);
		
		if (tbl.getSelectedRow() >= 0) {
			((DefaultTableModel) tbl.getModel())
					.removeRow(tbl.getSelectedRow());
		}
	}

	public User getUser() {
		boolean isadmin;
		if (ckIsAdmin.isSelected() == true)
		{
			isadmin = true;
		}
		else
		{
			isadmin = false;
		}
		return new User(tfUsername.getText(),tfPassword.getText(),isadmin);
	}
}
