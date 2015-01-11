package object;

public class DetilTransaksi {
	private Barang produk;
	private int quantity;
	private Transaksi transaksi;

	public DetilTransaksi(Transaksi transaksi, Barang produk, int quantity) {
		this.transaksi = transaksi;
		this.produk = produk;
		this.quantity = quantity;
	}

	public DetilTransaksi(Barang produk, int quantity) {
		this.produk = produk;
		this.quantity = quantity;
	}

	public Barang getProduk() {
		return produk;
	}

	public int getQuantity() {
		return quantity;
	}

	public Transaksi getTransaksi() {
		return transaksi;
	}

}
