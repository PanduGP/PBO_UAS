package ui;

import java.awt.Container;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;

import object.Barang;
import object.Transaksi;
import system.*;
import ui.listener.CustActionListener;

public class WindowDetilTrans extends JFrame {
	private Core core;

	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private Vector<Barang> barang = new Vector<Barang>();
	private Vector<String> nmBarang = new Vector<String>();

	private JTable tbl;

	public WindowDetilTrans(Core core, Transaksi index) {
		super("Detil Transaksi Obat Herbal");
		setIconImage(Toolkit.getDefaultToolkit().getImage("D:\\PROJECT\\logo_only.jpg"));
		this.core = core;
		setResizable(false);

		setSize(500, 300);
		setLocation((screenSize.width - getWidth()) / 2,
				(screenSize.height - getHeight()) / 2);
		getContentPane().setLayout(null);
		Container container = this.getContentPane();
		tbl = new JTable(Operator.resultSetToTableModel(Operator
				.getListDetilTransaksi(core.getConnection(),index)));
		Operator.disableTableEdit(tbl);
		JPanel panTbl = new JPanel();
		panTbl.setLayout(new BorderLayout());
		panTbl.add(new JScrollPane(tbl), BorderLayout.CENTER);
		panTbl.setBounds(0, 0, 495, 395);
		panTbl.setBackground(Color.WHITE);
		
		container.add(panTbl);
	}
}
