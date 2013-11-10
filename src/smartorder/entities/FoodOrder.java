package smartorder.entities;

import java.io.Serializable;
import java.lang.String;
import java.sql.Date;
import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Order
 * 
 */
@Entity
@NamedQueries({
		@NamedQuery(name = "FoodOrder.findAll", query = "select o from FoodOrder o"),
		@NamedQuery(name = "FoodOrder.findByDate", query = "select o from FoodOrder o where o.orderDate>=?1 and o.orderDate<=?2"),
		@NamedQuery(name = "FoodOrder.findByCustomer", query = "select o.orderID from FoodOrder o where o.customerID=?1") })
public class FoodOrder implements Serializable {

	@Id
	private String orderID;
	private String customerID;
	private java.sql.Date orderDate;

	@ManyToOne
	private Customer customer;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "order", fetch = FetchType.EAGER)
	private List<OrderDetail> details;

	private static final long serialVersionUID = 1L;

	public FoodOrder() {
	}

	public FoodOrder(String orderID, String customerID, Date orderDate) {
		this.orderID = orderID;
		this.customerID = customerID;
		this.orderDate = orderDate;
	}

	public String getOrderID() {
		return this.orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public String getCustomerID() {
		return this.customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public java.sql.Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(java.sql.Date orderDate) {
		this.orderDate = orderDate;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<OrderDetail> getDetails() {
		return details;
	}

	public void setDetails(List<OrderDetail> details) {
		this.details = details;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((orderID == null) ? 0 : orderID.hashCode());
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
		FoodOrder other = (FoodOrder) obj;
		if (orderID == null) {
			if (other.orderID != null)
				return false;
		} else if (!orderID.equals(other.orderID))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return orderID;
	}
	
	

}
