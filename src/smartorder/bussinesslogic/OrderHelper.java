package smartorder.bussinesslogic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import smartorder.entities.Customer;
import smartorder.entities.FoodOrder;
import smartorder.entities.OrderDetail;
import smartorder.entities.OrderDetailPK;
import smartorder.entities.Product;
import smartorder.presentation.CartItem;

public class OrderHelper {
	
	
	public static void makeOrder(EntityManager em, ArrayList<CartItem> shoppingCart) throws Exception {
		// Customer make a Order:	
		// 1. user(customer) log in
		Customer user = em.find(Customer.class, "vietdung7");
		
		// 2. prepare a blank FoodOrder
		java.util.Date today = new Date();
		java.sql.Date date = new java.sql.Date(today.getTime());
		String orderID = "ORD-"+date+"-"+date.getTime();
		FoodOrder order = new FoodOrder(orderID, user.getCustomerID(), date);
		
		// 3. create OrderDetail for each Product
		List<OrderDetail> details = new ArrayList<OrderDetail>();
		for (CartItem i : shoppingCart) {
			// fill detail to order
			Product p = em.find(Product.class, i.getProductID());
			OrderDetailPK detailID = new OrderDetailPK(orderID, i.getProductID());
			OrderDetail detail = 
					new OrderDetail(detailID, p.getRefPrice(), i.getQuantity(), ""); 
			detail.setProduct(p);
			detail.setOrder(order);
			details.add(detail);
		}
		order.setDetails(details);
		
		// 4. make Order, insert it into Database
		em.persist(order);
		
		System.out.println("make Order ok - " + orderID);
		
	}

	public static void printAllOrder(EntityManager em) {
		Query query = em.createNamedQuery("FoodOrder.findAll");
		List<FoodOrder> orders = (List<FoodOrder>) query.getResultList();
		
		for (FoodOrder order : orders) {
			System.out.println("-----------------------------------------------------");
			System.out.println("Order - " + order.getOrderID());
			System.out.println("Date : " + order.getOrderDate());
			for (OrderDetail detail : order.getDetails()) {
				System.out.printf("\t%-20s %2.2fx%d\n", 
						detail.getProduct().getProductName(),detail.getPrice(),detail.getQuantity());
			}
			System.out.println("-----------------------------------------------------");
		}
		
	}
	
	public static void initSampleData(EntityManager em) throws Exception{
		// initialize sample products (foods)
		em.persist(new Product("Hotdog", 1.5d));
		em.persist(new Product("Cookies & Cream", 2.5d));
		em.persist(new Product("Hot Chocolate", 1.0d));
		em.persist(new Product("Cheese Pizza", 5.5d));
		em.persist(new Product("Tuna Salad", 1.5d));
		em.persist(new Product("Cheeseburger", 1.29d));
		em.persist(new Product("Filet-O-Fish", 3.49d));
		em.persist(new Product("Hamburger", 0.99d));
		em.persist(new Product("French Fries Sm", 1.39d));
		em.persist(new Product("French Fries Med", 1.99d));
		em.persist(new Product("French Fries Lg", 2.29d));

		// initialize sample customers account
		em.persist(new Customer("vietdung7", "Viet Dung", "123456789", "Q.HM TPHCM", "0987356443", "vd@mail.com"));
		em.persist(new Customer("vanthi114", "Van Thi", "123456789", "Q.GV TPHCM", "0986676443", "thinv@mail.com"));
		em.persist(new Customer("jackvt93", "Luan Vu", "123456789", "Q.1 TPHCM", "0987354321", "jackvt@mail.com"));

		System.out.println("Success initialize sample database");

	}

}
