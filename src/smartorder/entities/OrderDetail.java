package smartorder.entities;

import java.io.Serializable;
import java.lang.String;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: OrderDetail
 * 
 */
@Entity
@NamedQueries({ @NamedQuery(name = "OrderDetail.findAll", query = "select d from OrderDetail d") })
public class OrderDetail implements Serializable {

	@EmbeddedId
	private OrderDetailPK orderDetailID;
	private double price;
	private int quantity;
	private String note;
	private static final long serialVersionUID = 1L;

	@ManyToOne
	private Product product;
	@ManyToOne
	private FoodOrder order;

	public OrderDetail() {
		super();
	}

	public OrderDetail(OrderDetailPK orderDetailID, double price, int quantity,
			String note) {
		super();
		this.orderDetailID = orderDetailID;
		this.price = price;
		this.quantity = quantity;
		this.note = note;
	}

	public OrderDetail(OrderDetailPK orderDetailID, double price, int quantity) {
		super();
		this.orderDetailID = orderDetailID;
		this.price = price;
		this.quantity = quantity;
	}

	public OrderDetailPK getOrderDetailID() {
		return orderDetailID;
	}

	public void setOrderDetailID(OrderDetailPK orderDetailID) {
		this.orderDetailID = orderDetailID;
	}

	public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return this.quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public FoodOrder getOrder() {
		return order;
	}

	public void setOrder(FoodOrder order) {
		this.order = order;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((orderDetailID == null) ? 0 : orderDetailID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderDetail other = (OrderDetail) obj;
		if (orderDetailID == null) {
			if (other.orderDetailID != null)
				return false;
		} else if (!orderDetailID.equals(other.orderDetailID))
			return false;
		return true;
	}

}
