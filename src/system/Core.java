package system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.sql.rowset.JdbcRowSet;
import javax.swing.*;
import javax.swing.table.*;

import ui.*;
import object.*;

public class Core {

	final public static boolean GUI = true;
	final public static int BARANG = 0, DETIL_TRANSAKSI = 1, TRANSAKSI = 2,
			USER = 3;

	public WindowLogin frmLogin = new WindowLogin(this);
	public WindowReport frmReport;
	public WindowFormTransaksi frmFormTrans;
	public WindowFormBarang frmFormBarang;
	public WindowDataTransaksi frmDataTrans;
	public WindowDataBarang frmDataBarang;
	public WindowReorder frmReorder;
	public WindowSupplier frmSupplier;
	public WindowUsers frmUser;
	public WindowDetilTrans frmDetilTrans;
	
	config conn = new config();
	
	private Connection con;
	private User loggedUser;

	private static Calendar tgl = Calendar.getInstance();
	private static SimpleDateFormat formatter = new SimpleDateFormat(
			"E, dd MMMM yyyy");

	public Core(boolean GUI) {
		tesKoneksi();
			frmLogin.setVisible(true);
	}

	private void tesKoneksi() {
		try {
			Class.forName(conn.DATABASE_DRIVER);
			con = DriverManager.getConnection(conn.URL, conn.USERNAME, conn.PASSWORD);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			System.exit(0);
		}
	}

	public void printReport(Transaksi trns) {
		int ID = Operator.getLastIDTrans(con);

		String header = "\n"
				+ "\t\t                Toko Obat Herbal "
				+ "\n\t\t\t         We Make you healthy without Drugs"
				+ "\n NO Transaksi : " + ID
				+ "\n Kasir        : " + loggedUser.getUsername()
				+ "\n ============================================================================", data = "", footer = "\n"
				+ "\n ----------------------------------------------------------------------------"
				+ "\n Total : " + trns.getTotalItem() + " Item\t  Rp." + trns.getTotalHrg()
				+ "\n ============================================================================"
				+ "\n Tgl " + trns.getTglAsString();
		for (int i = 0; i < trns.getDetilTransaksi().size(); i++) {
			DetilTransaksi dt = trns.getDetilTransaksi().get(i);
			data = data + "\n " + dt.getProduk().getNama() + "\t"
					+ dt.getQuantity() + "x\t" + dt.getQuantity()
					* dt.getProduk().getHarga();
		}
		frmReport = new WindowReport(this,
				new String[] { header, data, footer });
		frmReport.setVisible(true);
	}

	public void printReport(Vector<Barang> brg) {
		String header = "\n"
				+ "\t\t                Toko Obat Herbal "
				+ "\n\t\t\t    We Make you healthy without Drugs"
				+ "\n\t\t     Stok barang tgl : " + getDateAsString()
				+ "\n Nama : " + loggedUser.getUsername()
				+ "\n ============================================================================", data = "", footer = "\n ============================================================================";
		int no = 1;
		for (int i = 0; i < brg.size(); i++) {
			
			Barang _brg = brg.get(i);
			data = data + "\n  "+ (no)+ ". "+ _brg.getNama();
						
			data += " | Stok : (" + _brg.getStok() + ")";
			no++;
		}
		frmReport = new WindowReport(this,
				new String[] { header, data, footer });
		frmReport.setVisible(true);
	}

	public void login(User usr) {
		this.loggedUser = new User(usr);
		if (usr.isAdmin()) {
			frmFormBarang = new WindowFormBarang(this);
			frmFormBarang.setVisible(true);
		} else {
			frmFormTrans = new WindowFormTransaksi(this);
			frmFormTrans.setVisible(true);
		}
	}

	public void logout() {
		if (loggedUser.isAdmin()) {
			frmFormBarang.dispose();
		} else {
			frmFormTrans.dispose();
		}
		frmLogin.dispose();
		frmLogin = new WindowLogin(this);
		frmLogin.setVisible(true);
		loggedUser = null;
	}

	public User getLoggedInUser() {
		return loggedUser;
	}

	public Connection getConnection() {
		return con;
	}

	public Date getDate() {
		return (Date) tgl.getTime();
	}

	public String getDateAsString() {
		return formatter.format(tgl.getTime());
	}

	public void reloadForm() {

	}
}
