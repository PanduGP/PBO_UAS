package object;

public class Barang {
	private int Stok, harga;
	private String ID, Nama, IDSupplier;

	public Barang(String ID, String Nama, String IDSupplier, int Harga, int Stok){
		this.ID=ID;
		this.Nama=Nama;
		this.IDSupplier=IDSupplier;
		this.Stok=Stok;
		this.harga=Harga;
	}
	
	public Barang(String nama, String IDSupplier ,int harga, int Stok){
		this.Nama=nama;
		this.Stok=Stok;
		this.harga=harga;
		this.IDSupplier=IDSupplier;
	}

	public String getId() {
		return ID;
	}

	public int getStok() {
		return Stok;
	}

	public int getHarga() {
		return harga;
	}

	public String getNama() {
		return Nama;
	}

	public String getIDSupplier() {
		return IDSupplier;
	}
}
