package object;

public class Supplier {
	private String IDSupplier, Nama;

	public Supplier (String IDSupplier, String Nama)
	{
		this.IDSupplier=IDSupplier;
		this.Nama=Nama;
	}
	public String getNama() {
		return Nama;
	}

	public void setNama(String nama) {
		Nama = nama;
	}

	public String getIDSupplier() {
		return IDSupplier;
	}

	public void setIDSupplier(String iDSupplier) {
		IDSupplier = iDSupplier;
	}
}
