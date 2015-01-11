package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import object.Barang;
import object.Reorder;
import system.*;
import ui.listener.CustActionListener;
import ui.listener.CustKeyListener;

public class WindowReorder extends JFrame {
	private Core core;
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private JTable tbl;
	private JTextField tfID, tfNama, tfStok, tfIDSupplier, tfHarga;
	private JLabel lbID, lbNama, lbStok, lbIDSupplier, lbHarga, lbJudul;
	
	private Vector<Reorder> reorder = new Vector<Reorder>();
	
	public WindowReorder(Core core) {
		super("Data Product Re-Order");
		getContentPane().setBackground(Color.LIGHT_GRAY);
		this.core = core;

		setResizable(false);
		setSize(750, 360);
		setLocation((screenSize.width - getWidth()) / 2,
				(screenSize.height - getHeight()) / 2);
		
		ResultSet rs = Operator.getReorder(core.getConnection());
		try {
			while (rs.next()) {
				reorder.add(new Reorder(rs.getString(1), rs.getString(2),rs.getString(3), rs.getInt(4), rs.getInt(5)));
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
		tbl = new JTable(Operator.resultSetToTableModel(Operator
				.getReorder(core.getConnection())));
		Operator.disableTableEdit(tbl);
		getContentPane().setLayout(null);
		JPanel pan = new JPanel();
		pan.setBackground(Color.WHITE);
		pan.setBounds(10, 10, getWidth()-270, 300);
		pan.setLayout(new BorderLayout());
		pan.add(tbl, BorderLayout.CENTER);
		pan.add(new JScrollPane(tbl), BorderLayout.CENTER);
		pan.add(tbl.getTableHeader(), BorderLayout.NORTH);

		tfID = new JTextField();
		tfNama = new JTextField();
		tfIDSupplier = new JTextField();
		tfHarga = new JTextField();
		tfStok = new JTextField();
		tfStok.setBackground(Color.WHITE);
		
		lbID = new JLabel("ID");
		lbNama = new JLabel("Nama");
		lbIDSupplier = new JLabel("ID Supplier");
		lbHarga = new JLabel("Harga");
		lbStok = new JLabel("Re-Stok");
		
		lbID.setBounds(510, 10, 80, 25);
		lbNama.setBounds(510, 40, 80, 25);
		lbIDSupplier.setBounds(510, 70, 80, 25);
		lbHarga.setBounds(510, 100, 80, 25);
		lbStok.setBounds(510, 130, 80, 25);
		
		tfID.setBounds(595, 10, 120, 25);
		tfNama.setBounds(595, 40, 120, 25);
		tfIDSupplier.setBounds(595, 70, 120, 25);
		tfHarga.setBounds(595, 100, 120, 25);
		tfStok.setBounds(595, 130, 120, 25);
		tfStok.addKeyListener(new CustKeyListener(core, this, tfStok,
				CustKeyListener.NUMBER_ONLY));
		
		tfID.setEnabled(false);
		tfNama.setEnabled(false);
		tfIDSupplier.setEnabled(false);
		tfHarga.setEnabled(false);
		
		JButton buttonReStok = new JButton("Re-Stok");
		JButton buttonSave = new JButton("Save");
		
		buttonReStok.setBounds(510, 165, 100, 25);
		buttonReStok.setForeground(Color.BLACK);
		buttonReStok.setBackground(Color.WHITE);
		buttonReStok.addActionListener(new CustActionListener(core, this,tbl,
				buttonReStok, CustActionListener.RESTOK));
		buttonSave.setBounds(635, 165, 80, 25);
		buttonSave.setForeground(Color.BLACK);
		buttonSave.setBackground(Color.WHITE);
		buttonSave.addActionListener(new CustActionListener(core, this,tbl,
				buttonSave, CustActionListener.UPDATE_STOK));
		
		getContentPane().add(lbID);
		getContentPane().add(tfID);
		getContentPane().add(lbNama);
		getContentPane().add(tfNama);
		getContentPane().add(lbIDSupplier);
		getContentPane().add(tfIDSupplier);
		getContentPane().add(lbHarga);
		getContentPane().add(tfHarga);
		getContentPane().add(lbStok);
		getContentPane().add(tfStok);
		getContentPane().add(buttonReStok);
		getContentPane().add(buttonSave);
		getContentPane().add(pan);
	}
	
	public Vector<Reorder> getListReorder() {
		return reorder;
	}

	public Reorder getSelectedReorder() {
		return reorder.get(tbl.getSelectedRow());
	}
	
	public void ReStok(Reorder Re)
	{
		tfID.setText(Re.getId());
		tfNama.setText(Re.getNama());
		tfIDSupplier.setText(Re.getIDSupplier());
		tfHarga.setText("" + Re.getHarga() + "");
		tfStok.setText("" + Re.getStok() + "");
	}
	
	public void submitToDB() {
		if (Operator.Reorder(getReorder(), core.getConnection())) {
			JOptionPane.showMessageDialog(this, "Stok Telah Bertambah !");
		} else {
			JOptionPane.showMessageDialog(this, "Terjadi kesalahan!");
		}
		
		tfID.setText("");
		tfNama.setText("");
		tfIDSupplier.setText("");
		tfHarga.setText("");
		tfStok.setText("");
		
		if (tbl.getSelectedRow() >= 0) {
			((DefaultTableModel) tbl.getModel())
					.removeRow(tbl.getSelectedRow());
		}
	}

	public void resetForm() {
		tfID.setText("");
		tfNama.setText("");
		tfIDSupplier.setText("");
		tfHarga.setText("");
		tfStok.setText("");
		
		if (tbl.getSelectedRow() >= 0) {
			((DefaultTableModel) tbl.getModel())
					.removeRow(tbl.getSelectedRow());
		}
	}

	public Reorder getReorder() {
		return new Reorder(tfID.getText(),tfNama.getText(),tfIDSupplier.getText(),Integer.parseInt(tfHarga.getText()),Integer.parseInt(tfStok.getText()));
	}
}
