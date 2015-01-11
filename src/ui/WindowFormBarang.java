package ui;

import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.event.AncestorListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;

import object.Barang;
import object.Supplier;
import system.*;
import ui.listener.CustActionListener;
import ui.listener.CustKeyListener;

public class WindowFormBarang extends JFrame {
	private Core core;

	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private JTextField tfID, tfNama, tfStok, tfIDSupplier, tfHarga;
	private JTable tbl;
	private JButton buttonTambah , buttonDelete;
	private JComboBox cbSupplier;
	private JLabel lbID, lbBarang, lbNama, lbStok, lbIDSupplier, lbHarga, lbJudul;

	private Vector<Barang> barang = new Vector<Barang>();
	private Vector<String> nmSupplier = new Vector<String>();
	private Vector<Supplier> supplier = new Vector<Supplier>();

	public WindowFormBarang(final Core core) {
		super("Window Maintenance | " + core.getDateAsString());
		getContentPane().setBackground(Color.LIGHT_GRAY);
		setIconImage(Toolkit.getDefaultToolkit().getImage("D:\\PROJECT\\logo_only.jpg"));
		this.core = core;
		setResizable(false);

		setSize(810, 450);
		setLocation((screenSize.width - getWidth()) / 2,
				(screenSize.height - getHeight()) / 2);
		getContentPane().setLayout(null);
		Container container = this.getContentPane();
		//container.setBackground(Color.WHITE);
		setDefaultCloseOperation(0);
		JMenuBar menu = new JMenuBar();
		this.setJMenuBar(menu);

		JMenu menuUser = new JMenu(
				core.getLoggedInUser().isAdmin() ? "Supervisor " : "Kasir "
						+ core.getLoggedInUser().getUsername());
		JMenuItem miLogOut = new JMenuItem("Log Out");
		miLogOut.addActionListener(new CustActionListener(core, this, miLogOut,
				CustActionListener.LOGOUT));
		JMenuItem miUsers = new JMenuItem("Users");
		miUsers.addActionListener(new CustActionListener(core, this, miUsers,
				CustActionListener.USER));

		JMenu menuTrans = new JMenu("Transaksi");
		JMenuItem miTransData = new JMenuItem("Data Transaksi");
		miTransData.addActionListener(new CustActionListener(core, this,
				miTransData, CustActionListener.SHOW_DATA_TRANSAKSI));
		
		JMenu menuBarang = new JMenu("Product");
		JMenuItem miBarangCetak = new JMenuItem("Cetak Stok Product");
		miBarangCetak.addActionListener(new CustActionListener(core, this,
				miBarangCetak, CustActionListener.CETAK_BARANG));
		JMenuItem miReorder = new JMenuItem("Re-Order Product");
		miReorder.addActionListener(new CustActionListener(core, this,
				miReorder, CustActionListener.REORDER));
		
		JMenu menuSupplier = new JMenu("Supplier");
		JMenuItem mimenuSupplier = new JMenuItem("Suppliers");
		mimenuSupplier.addActionListener(new CustActionListener(core, this,
				mimenuSupplier, CustActionListener.SHOW_DATA_SUPPLIER));
		
		menu.add(menuUser);
		menuUser.add(miUsers);
		menuUser.add(miLogOut);
		menu.add(menuTrans);
		menuTrans.add(miTransData);
		menu.add(menuBarang);
		menuBarang.add(miBarangCetak);
		menuBarang.add(miReorder);
		menu.add(menuSupplier);
		menuSupplier.add(mimenuSupplier);
		
		ResultSet rs = Operator.getListBarang(core.getConnection());
		try {
			while (rs.next()) {
				barang.add(new Barang(rs.getString(1), rs.getString(2),rs.getString(3), rs.getInt(4), rs.getInt(5)));
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}

		tbl = new JTable(Operator.resultSetToTableModel(Operator
				.getListBarang(core.getConnection())));
		Operator.disableTableEdit(tbl);
		JPanel panTbl = new JPanel();

		panTbl.setLayout(new BorderLayout());
		panTbl.setBackground(Color.WHITE);
		panTbl.add(new JScrollPane(tbl), BorderLayout.CENTER);

		tfID = new JTextField();
		tfNama = new JTextField();
		tfIDSupplier = new JTextField();
		
		ResultSet rs1 = Operator.getListSupplier(core.getConnection());
		nmSupplier.removeAllElements();
		supplier.removeAllElements();
		try {
			while (rs1.next()) {
				supplier.add(new Supplier(rs1.getString(1), rs1.getString(2)));
				nmSupplier.add(supplier.lastElement().getNama()+" ("+ supplier.lastElement().getIDSupplier() +")");
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		cbSupplier = new JComboBox(nmSupplier);
		
		tfHarga = new JTextField();
		tfStok = new JTextField();
		fillFormByIndex(cbSupplier.getSelectedIndex());
		tfID.setBounds(615, 35, 170, 25);
		tfNama.setBounds(615, 65, 170, 25);
		tfIDSupplier.setVisible(false);
		cbSupplier.setBounds(615, 95, 170, 25);
		tfHarga.setBounds(615, 125, 170, 25);
		tfHarga.addKeyListener(new CustKeyListener(core, this, tfHarga,
				CustKeyListener.NUMBER_ONLY));
		tfStok.setBounds(615, 155, 170, 25);
		tfStok.addKeyListener(new CustKeyListener(core, this, tfStok,
				CustKeyListener.NUMBER_ONLY));

		panTbl.setBounds(10, 10, 500, 380);
		
		lbJudul = new JLabel ("<HTML><H2>Produk Obat Herbal</H2></HTML>");
		lbJudul.setForeground(Color.DARK_GRAY);
		lbID = new JLabel("ID Produk");
		lbNama = new JLabel("Nama Produk");
		lbStok = new JLabel("Jumlah Produk");
		lbIDSupplier = new JLabel("ID Suppler");
		lbHarga = new JLabel("Harga satuan");

		lbJudul.setBounds(580, 5, 300, 20);
		lbID.setBounds(500, 35, 100, 20); 
		lbID.setHorizontalAlignment(JLabel.RIGHT);
		lbNama.setBounds(500, 65, 100, 20);
		lbNama.setHorizontalAlignment(JLabel.RIGHT);
		lbIDSupplier.setBounds(500, 95, 100, 20);
		lbIDSupplier.setHorizontalAlignment(JLabel.RIGHT);
		lbHarga.setBounds(500, 125, 100, 20);
		lbHarga.setHorizontalAlignment(JLabel.RIGHT);
		lbStok.setBounds(500, 155, 100, 20);
		lbStok.setHorizontalAlignment(JLabel.RIGHT);

		buttonTambah = new JButton("Add");
		buttonDelete = new JButton("Delete");

		buttonTambah.setBounds(705, 185, 80, 25);
		buttonTambah.setForeground(Color.BLACK);
		buttonTambah.setBackground(Color.WHITE);
		buttonTambah.addActionListener(new CustActionListener(core, this,tbl,
				buttonTambah, CustActionListener.TAMBAH_BARANG));
		buttonDelete.setBounds(615, 185, 80, 25);
		buttonDelete.setForeground(Color.BLACK);
		buttonDelete.setBackground(Color.WHITE);
		buttonDelete.addActionListener(new CustActionListener(core, this,tbl,
				buttonDelete, CustActionListener.HAPUS_BARANG));
		cbSupplier.addActionListener(new CustActionListener(this, cbSupplier));
		// Add Content
		container.add(lbJudul);
		container.add(tfID);
		container.add(tfNama);
		container.add(cbSupplier);
		container.add(tfHarga);
		container.add(tfStok);
		container.add(panTbl);
		container.add(lbID);
		container.add(lbNama);
		container.add(lbIDSupplier);
		container.add(lbHarga);
		container.add(lbStok);

		container.add(buttonDelete);
		container.add(buttonTambah);
	}

	public void fillFormByIndex(int index) {
		tfIDSupplier.setText(supplier.get(index).getIDSupplier());
	}

	public Vector<Barang> getListBarang() {
		return barang;
	}

	public Barang getSelectedBarang() {
		return barang.get(tbl.getSelectedRow());
	}

	public void submitToDB() {
		if (Operator.tambahBarang(getBarang(), core.getConnection())) {
			JOptionPane.showMessageDialog(this, "Data telah ditambahkan!");
		} else {
			JOptionPane.showMessageDialog(this, "Terjadi kesalahan!");
		}
		
		((DefaultTableModel)tbl.getModel()).addRow(new Object[]{tfID.getText(),tfNama.getText(),tfIDSupplier.getText(),tfHarga.getText(),tfStok.getText()});

		tfID.setText("");
		tfNama.setText("");
		tfIDSupplier.setText("");
		cbSupplier.setSelectedIndex(0);
		tfHarga.setText("");
		tfStok.setText("");
	}

	public void resetForm() {
		tfID.setText("");
		tfNama.setText("");
		tfIDSupplier.setText("");
		cbSupplier.setSelectedIndex(0);
		tfHarga.setText("");
		tfStok.setText("");
		
		if (tbl.getSelectedRow() >= 0) {
			((DefaultTableModel) tbl.getModel())
					.removeRow(tbl.getSelectedRow());
		}
	}

	public Barang getBarang() {
		return new Barang(tfID.getText(),tfNama.getText(),tfIDSupplier.getText(),Integer.parseInt(tfHarga.getText()),Integer.parseInt(tfStok.getText()));
	}
}
