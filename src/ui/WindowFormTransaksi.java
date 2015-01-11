package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import object.Barang;
import object.DetilTransaksi;
import object.Transaksi;
import object.User;

import system.*;
import ui.listener.CustActionListener;
import ui.listener.CustKeyListener;

public class WindowFormTransaksi extends JFrame {

	final int TGL = 0, KASIR = 1, BARANG = 2, HARGA = 3, JUMLAH = 4;

	private Core core;

	private User user;
	private Transaksi t;

	private JPanel panLeft, panRight, panTable, panGrand;
	private JTextField tfTgl, tfKasir, tfHarga, tfJumlah;
	private JLabel l[] = new JLabel[5];
	private JComboBox cb;
	private JButton btnTambahBarang, btnTambahTransaksi, btnReset;
	private JTable tbl;

	private DefaultTableModel model;

	private Container container;
	
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private Vector<String> nmBarang = new Vector<String>();
	private Vector<Barang> barang = new Vector<Barang>();

	public WindowFormTransaksi(Core core) {
		super("Window Transaksi | " + core.getDateAsString());
		getContentPane().setBackground(Color.LIGHT_GRAY);
		setIconImage(Toolkit.getDefaultToolkit().getImage("D:\\PROJECT\\logo_only.jpg"));
		this.core = core;
		this.user = core.getLoggedInUser();

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setSize(810, 450);
		setLocation((screenSize.width - getWidth()) / 2,
				(screenSize.height - getHeight()) / 2);
		getContentPane().setLayout(null);
		container = this.getContentPane();
		setDefaultCloseOperation(0);
		model = new DefaultTableModel();
		model.addColumn("Nama Item");
		model.addColumn("Jumlah Item");
		model.addColumn("Total Harga");
		tbl = new JTable(model);
		Operator.disableTableEdit(tbl);
		ResultSet rs = Operator.getListBarang(core.getConnection());
		nmBarang.removeAllElements();
		barang.removeAllElements();
		try {
			while (rs.next()) {
				barang.add(new Barang(rs.getString(1), rs.getString(2),rs.getString(3), rs.getInt(4), rs.getInt(5)));
				if (barang.lastElement().getStok() > 0)
					nmBarang.add(barang.lastElement().getNama());
				else
					barang.removeElement(barang.lastElement());
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		cb = new JComboBox(nmBarang);
		tfTgl = new JTextField(core.getDateAsString());
		tfKasir = new JTextField(user.getUsername());
		tfJumlah = new JTextField();
		tfHarga = new JTextField();
		fillFormByIndex(cb.getSelectedIndex());

		panTable = new JPanel(new BorderLayout());
		panTable.setBounds(350, 20, 440, 300);
		panGrand = new JPanel(null);
		panGrand.setBackground(Color.LIGHT_GRAY);
		panGrand.setBounds(270, 310, 440, 300);
		panLeft = new JPanel(null);
		panLeft.setBackground(Color.LIGHT_GRAY);
		panLeft.setBounds(0, 10, 320, 400);

		JMenuBar menu = new JMenuBar();
		this.setJMenuBar(menu);

		JMenu menuUser = new JMenu(
				core.getLoggedInUser().isAdmin() ? "Supervisor " : "Kasir "
						+ core.getLoggedInUser().getUsername());
		JMenuItem miLogOut = new JMenuItem("Log Out");
		miLogOut.addActionListener(new CustActionListener(core, this, miLogOut,
				CustActionListener.LOGOUT));

		
		JMenu menuBarang = new JMenu("Barang");
		JMenuItem miBarangData = new JMenuItem("Data Barang");
		miBarangData.addActionListener(new CustActionListener(core, this,
				miBarangData, CustActionListener.SHOW_DATA_BARANG));
		
		menu.add(menuUser);
		menuUser.add(miLogOut);

		
		menu.add(menuBarang);
		menuBarang.add(miBarangData);
		JLabel lbJudul = new JLabel ("<HTML><H2>Toko Obat Herbal</H2></HTML>");
		lbJudul.setForeground(Color.DARK_GRAY);
		l[TGL] = new JLabel("Tanggal");
		l[KASIR] = new JLabel("Nama Kasir");
		l[BARANG] = new JLabel("Nama Product");
		l[HARGA] = new JLabel("Harga Rp.");
		l[JUMLAH] = new JLabel("Jumlah Item");

		tfTgl.setEnabled(false);
		tfKasir.setEnabled(false);
		tfHarga.setEnabled(false);
		tfTgl.setBounds(115, 45, 170, 25);
		tfKasir.setBounds(115, 75, 170, 25);
		cb.setBounds(115, 105, 170, 25);
		tfHarga.setBounds(115, 135, 170, 25);
		tfJumlah.setBounds(115, 165, 170, 25);

		lbJudul.setBounds(85, 5, 300, 20);
		l[TGL].setBounds(0, 45, 100, 20);
		l[KASIR].setBounds(0, 75, 100, 20);
		l[BARANG].setBounds(0, 105, 100, 20);
		l[HARGA].setBounds(0, 135, 100, 20);
		l[JUMLAH].setBounds(0, 165, 100, 20);

		btnTambahBarang = new JButton("<HTML><H4>Add</H4></HTML>");
		btnTambahBarang.setBounds(105, 210, 170, 30);
		btnTambahBarang.setForeground(Color.BLACK);
		btnTambahBarang.setBackground(Color.WHITE);
		btnTambahBarang.addActionListener(new CustActionListener(this,
				btnTambahBarang));
		btnTambahBarang.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				btnTambahTransaksi.setEnabled(true);
				btnReset.setEnabled(true);
			}
		});
		btnTambahTransaksi = new JButton("<HTML><H4>Save & Print</H4></HTML>");
		btnTambahTransaksi.setBounds(170, 30, 120, 30);
		btnTambahTransaksi.setForeground(Color.BLACK);
		btnTambahTransaksi.setBackground(Color.WHITE);
		btnReset = new JButton("<HTML><H4>Reset</H4></HTML>");
		btnReset.setBounds(320, 30, 120, 30);
		btnReset.setForeground(Color.BLACK);
		btnReset.setBackground(Color.WHITE);
		btnTambahTransaksi.setEnabled(false);
		btnReset.setEnabled(false);
		btnReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				resetForm();
				btnTambahTransaksi.setEnabled(false);
				btnReset.setEnabled(false);
			}
		});
		btnTambahTransaksi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				btnTambahTransaksi.setEnabled(false);
				btnReset.setEnabled(false);
			}
		});
		btnTambahTransaksi.addActionListener(new CustActionListener(core, this,
				btnTambahTransaksi));
		tfJumlah.addKeyListener(new CustKeyListener(this, tfJumlah,
				CustKeyListener.NUMBER_ONLY));
		tfJumlah.addKeyListener(new CustKeyListener(this, tfJumlah,
				CustKeyListener.ON_STOCK));
		cb.addActionListener(new CustActionListener(this, cb));
		panLeft.add(lbJudul);
		panLeft.add(cb);
		panLeft.add(tfTgl);
		panLeft.add(tfKasir);
		panLeft.add(tfHarga);
		panLeft.add(tfJumlah);
		for (int i = 0; i < l.length; i++) {
			l[i].setHorizontalAlignment(JLabel.RIGHT);
			panLeft.add(l[i]);
		}
		panTable.add((JTableHeader) tbl.getTableHeader(), BorderLayout.NORTH);
		panTable.add(new JScrollPane(tbl), BorderLayout.CENTER);
		panGrand.add(btnTambahTransaksi);
		panGrand.add(btnReset);
		panLeft.add(btnTambahBarang);

		container.add(panLeft);
		container.add(panTable);
		container.add(panGrand);

		resetForm();
	}

	public void fillFormByIndex(int index) {
		tfJumlah.setText("1");
		tfHarga.setText(barang.get(index).getHarga() * Integer.parseInt(tfJumlah.getText()) + "");
	}

	public void resetForm() {
		int row = tbl.getRowCount() - 1;
		for (int i = row; i >= 0; i--)
			((DefaultTableModel) tbl.getModel()).removeRow(i);
		t = new Transaksi(core.getDate(), user);
	}

	public void addBarangToTable(DetilTransaksi dt) {
		for (int i = 0; i < tbl.getRowCount(); i++) {
			// test apakah sudah ada
		}
		model.addRow(new String[] { dt.getProduk().getNama(),
				dt.getQuantity() + "",
				dt.getProduk().getHarga() * dt.getQuantity() + "" });
		t.addDetilTransaksi(dt);
	}

	public Vector<Barang> getListBarang() {
		return barang;
	}

	public Barang getSelectedBarang() {
		return barang.get(cb.getSelectedIndex());
	}

	public int getQtyBarang() {
		return Integer.parseInt(tfJumlah.getText());
	}

	public Transaksi getTransaksi() {
		return t;
	}

	public Vector<DetilTransaksi> getDetilTransaksi() {
		return t.getDetilTransaksi();
	}

	public void submitToDB() {
		if (Operator.tambahTransaksi(getTransaksi(), core.getConnection())) {
			JOptionPane.showMessageDialog(this, "Data telah ditambahkan!");
		} else {
			JOptionPane.showMessageDialog(this, "Terjadi kesalahan!");
		}
		if (JOptionPane.showConfirmDialog(this, "Cetak transaksi?", "",
				JOptionPane.YES_NO_OPTION) == 0) {
			core.printReport(t);
		}
		resetForm();
	}
}
