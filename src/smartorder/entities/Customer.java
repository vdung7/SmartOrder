package smartorder.entities;

import java.io.Serializable;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Customer
 * 
 */
@Entity
@NamedQueries({ @NamedQuery(name = "Customer.findAll", query = "select c from Customer c") })
public class Customer implements Serializable {

	@Id
	private String customerID;
	private String customerName;
	private String password;
	private String customerAddress;
	private String customerFone;
	private String customerEmail;
	private static final long serialVersionUID = 1L;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "customer", fetch = FetchType.EAGER)
	private List<FoodOrder> orders;

	public Customer() {
	}

	public Customer(String customerID) {
		super();
		this.customerID = customerID;
	}

	public Customer(String customerID, String customerName, String password,
			String customerAddress, String customerFone, String customerEmail) {
		this.customerID = customerID;
		this.customerName = customerName;
		this.password = password;
		this.customerAddress = customerAddress;
		this.customerFone = customerFone;
		this.customerEmail = customerEmail;
	}

	public String getCustomerID() {
		return this.customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCustomerAddress() {
		return this.customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public String getCustomerFone() {
		return this.customerFone;
	}

	public void setCustomerFone(String customerFone) {
		this.customerFone = customerFone;
	}

	public String getCustomerEmail() {
		return this.customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public List<FoodOrder> getOrders() {
		return orders;
	}

	public void setOrders(List<FoodOrder> orders) {
		this.orders = orders;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((customerID == null) ? 0 : customerID.hashCode());
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
		Customer other = (Customer) obj;
		if (customerID == null) {
			if (other.customerID != null)
				return false;
		} else if (!customerID.equals(other.customerID))
			return false;
		return true;
	}

	
	
	@Override
	public String toString() {
		return "Customer [customerID=" + customerID + ", customerName="
				+ customerName + ", password=" + password
				+ ", customerAddress=" + customerAddress + ", customerFone="
				+ customerFone + ", customerEmail=" + customerEmail
				+ ", orders=" + orders + "]";
	}

	public ArrayList<String> toArray() {
		ArrayList<String> res = new ArrayList<>();
		res.add(this.getCustomerName());
		res.add(this.getCustomerAddress());
		res.add(this.getCustomerFone());
		res.add(this.getCustomerEmail());
		return res;
	}
}
