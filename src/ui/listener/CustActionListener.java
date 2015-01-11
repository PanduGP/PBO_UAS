package ui.listener;

import java.awt.event.*;
import java.sql.Connection;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.sun.org.apache.bcel.internal.generic.FMUL;

import object.*;
import system.*;
import ui.*;

public class CustActionListener implements ActionListener {

	public final static int LOGIN = 0, FORM_TRANSAKSI_ADDBARANG = 1,
			FORM_TRANSAKSI_SUBMIT = 2, FORM_TRANSAKSI_SELECTITEM = 3,
			LOGOUT = 4, SHOW_DATA_BARANG = 5, SHOW_DATA_TRANSAKSI = 6,
			CETAK_BARANG = 7, HAPUS_BARANG = 8, TAMBAH_BARANG = 9,
			HAPUS_TRANS = 10, FORM_PRODUK_SELECTITEM = 11, REORDER = 12, SHOW_DATA_SUPPLIER = 13, USER = 14, 
			SHOW_DETIL_TRANSAKSI = 15, HAPUS_USER = 16, TAMBAH_USER = 17, TAMBAH_SUPPLIER = 18, HAPUS_SUPPLIER = 19, UPDATE_STOK = 20, RESTOK = 21 ;
	private Core core;

	private JFrame jf;
	private WindowLogin frmLogin;
	private WindowReport frmReport;
	private WindowFormBarang frmFormBarang;
	private WindowFormTransaksi frmFormTrans;
	private WindowDataTransaksi frmDataTrans;
	private WindowDataBarang frmDataBarang;
	private WindowReorder frmReorder;
	private WindowSupplier frmSupplier;
	private WindowUsers frmUser;
	private WindowDetilTrans frmDetilTrans;

	private JButton btn;
	private JComboBox cb, cbSupplier;
	private JMenuItem mi, miReorder, miSupplier, miUser;
	private JTable tbl;

	private JTextField tf;

	private int mode;

	public CustActionListener(Core core, WindowLogin frm, JButton btn) {
		this.core = core;
		this.frmLogin = frm;
		this.btn = btn;
		mode = LOGIN;
	}

	public CustActionListener(Core core, WindowFormTransaksi frm, JButton btn) {
		this.core = core;
		this.frmFormTrans = frm;
		this.btn = btn;
		this.mode = FORM_TRANSAKSI_SUBMIT;
	}

	public CustActionListener(WindowFormTransaksi frm, JButton btn) {
		this.frmFormTrans = frm;
		this.btn = btn;
		this.mode = FORM_TRANSAKSI_ADDBARANG;
	}

	public CustActionListener(WindowFormTransaksi frm, JComboBox cb) {
		this.frmFormTrans = frm;
		this.cb = cb;
		mode = FORM_TRANSAKSI_SELECTITEM;
	}
	
	public CustActionListener(WindowFormBarang frm, JComboBox cbSupplier) {
		this.frmFormBarang = frm;
		this.cbSupplier = cbSupplier;
		mode = FORM_PRODUK_SELECTITEM;
	}

	public CustActionListener(Core core, JFrame jf, JMenuItem mi, int mode) {
		this.core = core;
		this.jf = jf;
		this.mi = mi;
		this.mode = mode;
	}

	public CustActionListener(Core core, WindowFormBarang frm, JMenuItem mi,
			int mode) {
		this.core = core;
		this.frmFormBarang = frm;
		this.mi = mi;
		this.mode = mode;
	}
	
	public CustActionListener(Core core, WindowReorder frmReorder, JMenuItem miReorder,
			int mode) {
		this.core = core;
		this.frmReorder = frmReorder;
		this.miReorder = miReorder;
		this.mode = mode;
	}
	
	public CustActionListener(Core core, WindowReorder frmReorder, JTable tbl,JButton btn,
			int mode) {
		this.core = core;
		this.frmReorder = frmReorder;
		this.btn = btn;
		this.tbl = tbl;
		this.mode = mode;
	}
	
	public CustActionListener(Core core, WindowSupplier frmSupplier, JMenuItem miSupplier,
			int mode) {
		this.core = core;
		this.frmSupplier = frmSupplier;
		this.miSupplier = miSupplier;
		this.mode = mode;
	}
	
	public CustActionListener(Core core, WindowUsers frmUser, JMenuItem miUser,
			int mode) {
		this.core = core;
		this.frmUser = frmUser;
		this.miUser = miUser;
		this.mode = mode;
	}

	public CustActionListener(Core core, WindowUsers frmUser, JTable tbl,JButton btn,
			int mode) {
		this.core = core;
		this.frmUser = frmUser;
		this.btn = btn;
		this.tbl = tbl;
		this.mode = mode;
	}
	
	public CustActionListener(Core core, WindowSupplier frmSupplier, JTable tbl,JButton btn,
			int mode) {
		this.core = core;
		this.frmSupplier = frmSupplier;
		this.btn = btn;
		this.tbl = tbl;
		this.mode = mode;
	}
	
	public CustActionListener(Core core, WindowFormBarang frm, JTable tbl,JButton btn,
			int mode) {
		this.core = core;
		this.frmFormBarang = frm;
		this.btn = btn;
		this.tbl = tbl;
		this.mode = mode;
	}

	public CustActionListener(Core core, WindowDataTransaksi frm, JTable tbl,
			JButton btn, int mode) 
	{
		this.core = core;
		this.tbl = tbl;
		this.frmDataTrans = frm;
		this.btn = btn;
		this.mode = mode;
	}
	
	public CustActionListener(Core core, WindowDetilTrans frmDetilTrans, JTable tbl,
			JButton btn, int mode) 
	{
		this.core = core;
		this.tbl = tbl;
		this.frmDetilTrans = frmDetilTrans;
		this.btn = btn;
		this.mode = mode;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (mode) {
		case LOGIN:
			User user = Operator.checkLogin(new User(frmLogin.getUser(),frmLogin.getPass(), false), core.getConnection());
			if (user == null) {
				JOptionPane.showMessageDialog(frmLogin,"Username atau password Salah \n Ulangi Lagi!");
			} else {
				frmLogin.setVisible(false);
				core.login(user);
			}
			break;
		case FORM_TRANSAKSI_SELECTITEM:
			int index = cb.getSelectedIndex();
			frmFormTrans.fillFormByIndex(index);
			break;
		case FORM_TRANSAKSI_ADDBARANG:
			frmFormTrans.addBarangToTable(new DetilTransaksi(frmFormTrans.getSelectedBarang(), frmFormTrans.getQtyBarang()));
			break;
		case FORM_TRANSAKSI_SUBMIT:
			frmFormTrans.submitToDB();
			break;
		case LOGOUT:
			core.logout();
			break;
		case SHOW_DATA_BARANG:
			if (core.frmDataBarang == null) {
			} else {
				core.frmDataBarang.dispose();
			}
			core.frmDataBarang = new WindowDataBarang(core);
			core.frmDataBarang.setVisible(true);
			break;
		case SHOW_DATA_TRANSAKSI:
			if (core.frmDataTrans == null) {
			} else {
				core.frmDataTrans.dispose();
			}
			core.frmDataTrans = new WindowDataTransaksi(core);
			core.frmDataTrans.setVisible(true);
			break;
		case CETAK_BARANG:
			core.printReport(frmFormBarang.getListBarang());
			break;
		case TAMBAH_BARANG:
			try {
			frmFormBarang.submitToDB();
			} catch (Exception ex)
			{
				JOptionPane.showMessageDialog(frmFormBarang,"Inputkan ID Product !");
			}
			break;
		case HAPUS_BARANG:
			try{
				if(tbl == null){
				}else{
					if (Operator.hapusBarang(frmFormBarang.getSelectedBarang(),core.getConnection())) {JOptionPane.showMessageDialog(frmFormBarang,"Data Product dihapus");
					}
					frmFormBarang.resetForm();
				}
			}catch (Exception ex)
			{
				JOptionPane.showMessageDialog(frmFormBarang,"Pilih Product yg Akan Dihapus !");
			}
			break;
		case HAPUS_TRANS:
			try{
				if(tbl == null){
				}else{
					if (Operator.hapusTransaksi(frmDataTrans.getTransaksi(),core.getConnection())) {JOptionPane.showMessageDialog(frmDataTrans,"Data transaksi dihapus");
					}
					frmDataTrans.resetForm();
				}
			}catch (Exception ex)
			{
				JOptionPane.showMessageDialog(frmDataTrans,"Pilih Transaksi yg Akan Dihapus !");
			}
			break;
		case FORM_PRODUK_SELECTITEM:
			int index1 = cbSupplier.getSelectedIndex();
			frmFormBarang.fillFormByIndex(index1);
			break;
		case REORDER:
			if (core.frmReorder == null) {
			} else {
				core.frmReorder.dispose();
			}
			core.frmReorder = new WindowReorder(core);
			core.frmReorder.setVisible(true);
			break;
			
		case SHOW_DATA_SUPPLIER:
			if (core.frmSupplier == null) {
			} else {
				core.frmSupplier.dispose();
			}
			core.frmSupplier = new WindowSupplier(core);
			core.frmSupplier.setVisible(true);
			break;
		case USER:
			if (core.frmUser == null) {
			} else {
				core.frmUser.dispose();
			}
			core.frmUser = new WindowUsers(core);
			core.frmUser.setVisible(true);
			break;
		case SHOW_DETIL_TRANSAKSI:
			if (core.frmDetilTrans == null) {
			} else {
				core.frmDetilTrans.dispose();
			}
			try{
			core.frmDetilTrans = new WindowDetilTrans(core, frmDataTrans.getTransaksi());
			core.frmDetilTrans.setVisible(true);
			}catch (Exception ex)
			{
			}
			break;
		case HAPUS_USER:
			try{
				if(tbl == null){
				}else{
					if (Operator.hapusUser(frmUser.getSelectedUser(),core.getConnection())) {JOptionPane.showMessageDialog(frmUser,"Data User dihapus");
					}
					frmUser.resetForm();
				}
			}catch (Exception ex)
			{
				JOptionPane.showMessageDialog(frmUser,"Pilih User yg Akan Dihapus !");
			}
			break;
		case TAMBAH_USER:
			frmUser.submitToDB();
			break;
		case TAMBAH_SUPPLIER:
			frmSupplier.submitToDB();
			break;
		case HAPUS_SUPPLIER:
			try {
				if(tbl == null){
				}else{
					if (Operator.hapusSupplier(frmSupplier.getSelectedSupplier(),core.getConnection())) {JOptionPane.showMessageDialog(frmSupplier,"Data Supplier dihapus");
					}
					frmSupplier.resetForm();
				}
				}catch (Exception ex)
			{
				JOptionPane.showMessageDialog(frmSupplier,"Pilih Supplier yg Akan Dihapus !");
			}
			break;
			
		case UPDATE_STOK:
			try {
			frmReorder.submitToDB();
			} catch (Exception ex)
			{
				JOptionPane.showMessageDialog(frmReorder,"Pilih Barang yang akan di Re-Stok !");
			}
			break;
		case RESTOK:
			try {
				if(tbl == null){
				}else{frmReorder.ReStok(frmReorder.getSelectedReorder());}
			} catch (Exception ex)
			{
				JOptionPane.showMessageDialog(frmReorder,"Pilih Barang yang akan di Re-Stok !");
			}
			break;
		}

	}
}
