package smartorder.bussinesslogic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import smartorder.entities.Customer;
import smartorder.entities.FoodOrder;
import smartorder.entities.OrderDetail;
import smartorder.entities.OrderDetailPK;
import smartorder.entities.Product;
import smartorder.common.CartItem;

public class OrderHelper {

	public static boolean makeOrder(EntityManager em,
			ArrayList<CartItem> shoppingCart, String userID) throws Exception {
		// Customer make a Order:
		// 1. user(customer) log in
		Customer user = em.find(Customer.class, userID);

		// 2. prepare a blank FoodOrder
		java.util.Date today = new Date();
		java.sql.Date date = new java.sql.Date(today.getTime());
		String orderID = "ORD-" + date + "-" + date.getTime();
		FoodOrder order = new FoodOrder(orderID, user.getCustomerID(), date);
		order.setCustomer(user);

		// 3. create OrderDetail for each Product
		List<OrderDetail> details = new ArrayList<OrderDetail>();
		for (CartItem i : shoppingCart) {
			// check valid product
			Product p = em.find(Product.class, i.getProductID());
			if(!p.isAvailable())
				return false;
			
			// fill detail to order
			OrderDetailPK detailID = new OrderDetailPK(orderID,
					i.getProductID());
			OrderDetail detail = new OrderDetail(detailID, p.getRefPrice(),
					i.getQuantity(), "");
			detail.setProduct(p);
			detail.setOrder(order);
			details.add(detail);
		}
		order.setDetails(details);

		// 4. make Order, insert it into Database
		em.persist(order);

		System.out.println("make Order ok - " + orderID);
		return true;
	}

	public static ArrayList<String> getOrdersInString(EntityManager em,
			String userID) {
		ArrayList<String> orders = new ArrayList<String>();

		Query query = em.createNamedQuery("FoodOrder.findByCustomer");
		query.setParameter(1, userID);
		@SuppressWarnings("unchecked")
		List<String> l = (List<String>) query.getResultList();
		for (int i = 0; i < l.size(); i++) {
			orders.add(l.get(i));
		}
		return orders;

	}

	public static void printAllOrder(EntityManager em) {
		Query query = em.createNamedQuery("FoodOrder.findAll");
		@SuppressWarnings("unchecked")
		List<FoodOrder> orders = (List<FoodOrder>) query.getResultList();

		for (FoodOrder order : orders) {
			System.out
					.println("-----------------------------------------------------");
			System.out.println("Order - " + order.getOrderID());
			System.out.println("Date : " + order.getOrderDate());
			System.out.println("Customer : "
					+ order.getCustomer().getCustomerName());
			for (OrderDetail detail : order.getDetails()) {
				System.out.printf("\t%-20s %2.2fx%d\n", detail.getProduct()
						.getProductName(), detail.getPrice(), detail
						.getQuantity());
			}
			System.out
					.println("-----------------------------------------------------");
		}

	}

	public static String getDetail(EntityManager em, String orderID) {
		FoodOrder order = em.find(FoodOrder.class, orderID);
		String detail = "";

		if (order != null) {
			detail += "----------------------\n";
			detail += "Order - " + order.getOrderID() + "\n";
			detail += "Date : " + order.getOrderDate() + "\n";
			detail += "Customer : " + order.getCustomer().getCustomerName()
					+ "\n";
			double total = 0d;
			for (OrderDetail d : order.getDetails()) {
				String fs = String.format("\t%-20s %2.2fx%d\n", d.getProduct()
						.getProductName(), d.getPrice(), d.getQuantity());
				detail += fs;
				total += d.getPrice() * d.getQuantity();
			}

			detail += "Total: " + total + "\n";
			detail += "----------------------\n";
		}
		return detail;
	}

	public static String getDetailInHTML(EntityManager em, String orderID) {
		FoodOrder order = em.find(FoodOrder.class, orderID);
		String detail = "";

		if (order != null) {
			detail += "<html>";
			detail += "<b><font color=red>OrderID : " + order.getOrderID()
					+ "</font><br>";
			detail += "Date : <font color=blue>" + order.getOrderDate()
					+ "</font><br>";
			detail += "Customer : <font color=blue>"
					+ order.getCustomer().getCustomerName() + "</font><br><hr>";
			detail += "<table border='0'>";
			double total = 0d;
			for (OrderDetail d : order.getDetails()) {
				double price = d.getPrice() * d.getQuantity();
				total += price;
				detail += "<tr>";
				detail += "<td>" + d.getProduct().getProductName() + "</td>";
				detail += "<td>" + String.format("%2.2f", d.getPrice()) + " x "
						+ d.getQuantity() + " = </td>";
				detail += "<td>" + String.format("$ %2.2f", price) + "</td>";
				detail += "</tr>";
			}
			detail += "<tr>";
			detail += "<td></td>";
			detail += "<td><font color=red>Total = </font></td>";
			detail += "<td><font color=red>" + String.format("$ %2.2f", total)
					+ "</font></td>";
			detail += "</tr>";
			detail += "</table>";
			detail += "</b>";
			detail += "</html>";
		}
		return detail;
	}

	public static String getDetailInSimpleHTML(EntityManager em, String orderID) {
		FoodOrder order = em.find(FoodOrder.class, orderID);
		String detail = "";

		if (order != null) {
			detail += "<b><font color=red>OrderID : " + order.getOrderID()
					+ "</font><br>";
			detail += "Date : <font color=blue>" + order.getOrderDate()
					+ "</font><br>";
			detail += "Customer : <font color=blue>"
					+ order.getCustomer().getCustomerName() + "</font><br>"
					+ "______________________________<br>";
			double total = 0d;
			for (OrderDetail d : order.getDetails()) {
				double price = d.getPrice() * d.getQuantity();
				total += price;
				detail += d.getProduct().getProductName() + " --- ";
				detail += String.format("%2.2f", d.getPrice()) + "x"
						+ d.getQuantity() + " = ";
				detail += String.format("$ %2.2f", price) + "<br>";
			}
			detail += "______________________________<br>";
			detail += "<font color=red>Total = </font>";
			detail += "<font color=red>" + String.format("$ %2.2f", total)
					+ "</font>";
			detail += "</b>";
		}
		return detail;
	}

	@SuppressWarnings("unchecked")
	public static List<FoodOrder> getByDate(EntityManager em,
			java.sql.Date before, java.sql.Date after) {
		Query query = em.createNamedQuery("FoodOrder.findByDate");
		query.setParameter(1, before);
		query.setParameter(2, after);
		return (List<FoodOrder>) query.getResultList();
	}

}
