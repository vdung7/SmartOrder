package smartorder.entities;

import java.io.Serializable;
import java.lang.String;
import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Product
 *
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Product.findAll", query="select p from Product p")
})
public class Product implements Serializable {

	   
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int productID;
	private String productName;
	private double refPrice;
	private static final long serialVersionUID = 1L;

	@OneToMany(cascade=CascadeType.ALL ,mappedBy="product", fetch=FetchType.EAGER)
	private List<OrderDetail> details;
	
	public Product() {
	}   
	
	public Product(String productName, double refPrice) {
		this.productName = productName;
		this.refPrice = refPrice;
	}

	public int getProductID() {
		return this.productID;
	}

	public void setProductID(int productID) {
		this.productID = productID;
	}   
	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}   
	public double getRefPrice() {
		return this.refPrice;
	}

	public void setRefPrice(double refPrice) {
		this.refPrice = refPrice;
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
		result = prime * result
				+ ((productName == null) ? 0 : productName.hashCode());
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
		Product other = (Product) obj;
		if (productName == null) {
			if (other.productName != null)
				return false;
		} else if (!productName.equals(other.productName))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Product [productID=" + productID + ", productName="
				+ productName + ", refPrice=" + refPrice + ", details="
				+ details + "]";
	}
	
}
