package smartorder.entities;

import java.io.Serializable;
import java.lang.String;

import javax.persistence.Embeddable;

/**
 * ID class for entity: OrderDetail
 * 
 */
@Embeddable
public class OrderDetailPK implements Serializable {

	private String orderID;
	private int productID;
	private static final long serialVersionUID = 1L;

	public OrderDetailPK() {
	}

	public OrderDetailPK(String orderID, int productID) {
		this.orderID = orderID;
		this.productID = productID;
	}

	public String getOrderID() {
		return this.orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public int getProductID() {
		return this.productID;
	}

	public void setProductID(int productID) {
		this.productID = productID;
	}

	/*
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof OrderDetailPK)) {
			return false;
		}
		OrderDetailPK other = (OrderDetailPK) o;
		return true
				&& (getOrderID() == null ? other.getOrderID() == null
						: getOrderID().equals(other.getOrderID()))
				&& getProductID() == other.getProductID();
	}

	/*
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (getOrderID() == null ? 0 : getOrderID().hashCode());
		result = prime * result + getProductID();
		return result;
	}

}
