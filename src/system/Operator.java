package system;

import java.sql.*;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import object.*;

public class Operator {

	public static ResultSet getListBarang(Connection con) {
		return select(con, "SELECT * FROM products ORDER BY ID");
	}
	
	public static ResultSet getListUser(Connection con) {
		return select(con, "SELECT * FROM users ORDER BY isAdmin");
	}
	
	public static ResultSet getReorder(Connection con) {
		return select(con, "SELECT * FROM products Where Stok <= 99 ORDER BY ID ");
	}

	public static ResultSet getListTransaksi(Connection con) {
		return select(con, "SELECT * FROM transaksi ORDER BY NOtrans");
	}
	
	public static ResultSet getListSupplier(Connection con) {
		return select(con, "SELECT * FROM supplier ORDER BY IDSupplier");
	}

	public static ResultSet getListDetilTransaksi(Connection con,
			Transaksi trn) {
		return select(con, "SELECT dt.NOtrans, p.Nama , dt.quantity FROM detilTransaksi dt join products p on dt.ID = p.ID WHERE NOtrans = "
				+ trn.getId());
	}

	public static int getLastIDTrans(Connection con) {
		ResultSet rs = select(con, "SELECT MAX(NOtrans) FROM transaksi");
		try {
			if (rs.next())
				return rs.getInt(1);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
		return 0;
	}

	public static boolean tambahBarang(Barang brg, Connection con) {
		return update(
				con,
				"INSERT INTO products " + "VALUES('" + brg.getId() + "','"
						+ brg.getNama() + "','" + brg.getIDSupplier() + "',"
						+ brg.getHarga() + "," + brg.getStok() + ")");
	}
	
	public static boolean Reorder(Reorder Re, Connection con) {
		return update(
				con,
				"UPDATE products set Stok = '"+ Re.getStok() +"' WHERE ID = '"+ Re.getId() +"'");
	}
	
	public static boolean tambahUser(User usr, Connection con) {
		return update(
				con,
				"INSERT INTO users " + "VALUES('" + usr.getUsername() + "','"
						+ usr.getPassword() + "','" + usr.isAdmin() +"')");
	}
	
	public static boolean tambahSupplier(Supplier sup, Connection con) {
		return update(
				con,
				"INSERT INTO supplier " + "VALUES('" + sup.getIDSupplier() + "','"
						+ sup.getNama() + "')");
	}

	public static boolean tambahTransaksi(Transaksi trns, Connection con) {
		boolean returnValue = true;
		int id = getLastIDTrans(con) + 1;
		returnValue = update(con, "INSERT INTO transaksi VALUES(" + id + ", '"
				+ trns.getTglAsString() + "','" + trns.getUser().getUsername()
				+ "')");
		for (int i = 0; i < trns.getDetilTransaksi().size(); i++) {
			returnValue = returnValue
					& update(con, "INSERT INTO detilTransaksi "
							+ "VALUES("
							+ id
							+ ", '"
							+ trns.getDetilTransaksi().get(i).getProduk().getId() + "', "
							+ trns.getDetilTransaksi().get(i).getQuantity() + ")");
			returnValue = returnValue
					& update(con, "UPDATE products SET Stok = Stok-"
							+ trns.getDetilTransaksi().get(i).getQuantity()
							+ " WHERE ID= '"
							+ trns.getDetilTransaksi().get(i).getProduk()
									.getId() +"'");
		}
		return returnValue;
	}

	public static boolean hapusBarang(Barang brg, Connection con) {
		return update(con, "DELETE FROM products WHERE ID = '" + brg.getId() + "'");
	}
	
	public static boolean hapusUser(User usr, Connection con) {
		return update(con, "DELETE FROM users WHERE username = '" + usr.getUsername() + "'");
	}
	
	public static boolean hapusSupplier(Supplier sup, Connection con) {
		return update(con, "DELETE FROM supplier WHERE IDSupplier = '" + sup.getIDSupplier() + "'");
	
	}
	public static boolean UpdateSupplier(Supplier sup, Connection con) {
		return update(con, "UPDATE Supplier WHERE IDSupplier = '" + sup.getIDSupplier() + "'");
	
	}

	public static boolean hapusTransaksi(Transaksi trns, Connection con) {
		boolean returnData = true;
		if (trns == null) {
			return false;
		} else {
			returnData = returnData
					& update(con,
							"DELETE FROM detilTransaksi WHERE NOtrans="
									+ trns.getId());
			returnData = returnData
					& update(con, "DELETE FROM transaksi WHERE NOtrans="
							+ trns.getId());
		}
		return returnData;
	}

	public static User checkLogin(User usr, Connection con) {
		ResultSet rs = select(con, "SELECT isAdmin FROM users "
				+ "WHERE username='" + usr.getUsername() + "' AND password='"
				+ usr.getPassword() + "'");
		try {
			if (rs.next()) {
				return new User(usr.getUsername(), usr.getPassword(),
						rs.getBoolean("isAdmin"));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return null;
	}

	private static ResultSet select(Connection con, String query) {
		ResultSet rs = null;
		try {
			rs = con.createStatement().executeQuery(query);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
		return rs;
	}

	private static boolean update(Connection con, String query) {
		try {
			con.createStatement().executeUpdate(query);
			return true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	public static DefaultTableModel resultSetToTableModel(ResultSet rs) {
		try {
			ResultSetMetaData meta = rs.getMetaData();

			Vector<String> col = new Vector<String>();
			int columnCount = meta.getColumnCount();
			for (int column = 1; column <= columnCount; column++) {
				col.add(meta.getColumnName(column));
			}

			Vector<Vector<Object>> data = new Vector<Vector<Object>>();
			while (rs.next()) {
				Vector<Object> vector = new Vector<Object>();
				for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
					vector.add(rs.getObject(columnIndex));
				}
				data.add(vector);
			}

			return new DefaultTableModel(data, col);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return new DefaultTableModel();
	}

	public static void disableTableEdit(JTable tbl) {
		for (int c = 0; c < tbl.getColumnCount(); c++) {
			Class<?> col_class = tbl.getColumnClass(c);
			tbl.setDefaultEditor(col_class, null);
		}
	}
}
