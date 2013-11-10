package smartorder.bussinesslogic;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import smartorder.entities.Customer;
import smartorder.entities.FoodOrder;
import smartorder.entities.OrderDetail;

public class CustomerHelper {

	public static Customer checkLogin(EntityManager em, String userID,
			String password) throws Exception {
		Customer cust = em.find(Customer.class, userID);
		if (cust != null && (cust.getPassword()).equals(password))
			return cust;
		return null;
	}

	public static boolean register(EntityManager em, String newID,
			String password, String name, String address, String phone,
			String email) throws Exception {
		if (em.find(Customer.class, newID) == null) {
			em.persist(new Customer(newID, name, password, address, phone,
					email));
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public static List<Customer> getAll(EntityManager em) {
		Query query = em.createNamedQuery("Customer.findAll");
		return (List<Customer>) query.getResultList();
	}

	public static String getDetailInHTML(EntityManager em, String uid) {
		Customer c = em.find(Customer.class, uid);

		String detail = "<html><ul>";
		detail += "<li><b>Username: </b><font color=blue>" + c.getCustomerID()
				+ "</font><br>";
		detail += "<li><b>Password: </b><i>" + c.getPassword() + "</i><br>";
		detail += "<li><b>Address: </b>" + c.getCustomerAddress() + "<br>";
		detail += "<li><b>Fone: </b>" + c.getCustomerFone() + "<br>";
		detail += "<li><b>Email: </b>" + c.getCustomerEmail() + "<br>";
		detail += "</ul></html>";

		return detail;
	}

	public static String getPaymentReportByCustomer(EntityManager em,
			String userID) {
		// get customer
		Customer c = em.find(Customer.class, userID);

		double totalPayment = 0d;
		int totalOrder = 0;

		// get detailOrders of this customer
		for (FoodOrder fo : c.getOrders()) {
			// count total order
			totalOrder++;
			for (OrderDetail od : fo.getDetails()) {
				// calculate total payment
				totalPayment += od.getPrice() * od.getQuantity();
			}
		}
		// HTML report
		String report = "<html><ul>" + "<li>Total Payment: "
				+ "<font color=red size=+1>$"
				+ String.format("%2.2f", totalPayment) + "</font>"
				+ "<li>Total Order: " + "<font color=red>" + totalOrder
				+ "</font>" + "<li>Payment per Order: " + "<font color=red>$"
				+ String.format("%2.2f", totalPayment / totalOrder) + "</font>"
				+ "</ul></html>";

		return report;
	}

}
