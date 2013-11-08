package smartorder.common;

import java.io.Serializable;

public class CartItem implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int productID;
	private int quantity;
	public CartItem(int productID, int quantity) {
		this.productID = productID;
		this.quantity = quantity;
	}
	public int getProductID() {
		return productID;
	}
	public void setProductID(int productID) {
		this.productID = productID;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	
}
