package smartorder.control;

import java.awt.BorderLayout;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.Order;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import smartorder.bussinesslogic.CustomerHelper;
import smartorder.bussinesslogic.OrderHelper;
import smartorder.bussinesslogic.ProductHelper;
import smartorder.common.CartItem;
import smartorder.entities.Customer;
import smartorder.entities.FoodOrder;
import smartorder.entities.Product;
import smartorder.presentation.SalesReportChart;

public class FunctionTest {
	public static void main(String[] args) {
		EntityManager em = 
				Persistence.createEntityManagerFactory("SmartOrder").createEntityManager();
		EntityTransaction et = em.getTransaction();
	
		try {
			et.begin();
			// uncomment 3 test below in order to test

			// 1st test
			// first time you run this test, 
			// set <property name="eclipselink.ddl-generation" value="drop-and-create-tables"
			// in persistence.xml file and run below method
			//OrderHelper.initSampleData(em);

			/*
				// 2nd test - make order
				// shopping cart - put any productIDs into this cart
				ArrayList<CartItem> shoppingCart = new ArrayList<CartItem>();  
				// user choose some Product
				shoppingCart.add(new CartItem(5, (int)(Math.random()*10)+1));
				shoppingCart.add(new CartItem(6, (int)(Math.random()*10)+1));
				shoppingCart.add(new CartItem(7, (int)(Math.random()*10)+1));
				shoppingCart.add(new CartItem(8, (int)(Math.random()*10)+1));
				shoppingCart.add(new CartItem(10, (int)(Math.random()*10)+1));
				// make order with this shopping cart
				OrderHelper.makeOrder(em, shoppingCart, "vietdung7");
			*/
			
			
			// 3rd test - get and print all FoodOrder (for test)
			//OrderHelper.printAllOrder(em);
			
			// 4th test - register new customer account
			/*
			boolean res = CustomerHelper.register(em, "vantinh22", "123456789", "Duong V Tinh", "NVB TPHCM", "0974443645", "tinh@m.com");
			System.out.println(res?"inserted":"duplicated");
			*/
			
			// 5th test - log in
			/*
			Customer c = CustomerHelper.checkLogin(em, "vietdung7", "888");
			System.out.println(c!=null?"loged in":"invalid username-password");
			*/
			
			// 6th test - view ORders
			/*
			for (String string : OrderHelper.getOrdersInString(em, "jackvt93")) {
				System.out.println(string);
			}
			*/
			
			// 6th test - view MENU
			/*
			for (String string : ProductHelper.getMenuInStringArray(em)) {
				System.out.println(string);
			}
			*/
			
			// 7th test - get detail of an order
			/*
			System.out.println(OrderHelper.getDetail(em, "ORD-2013-11-05-1383643170596"));
			*/
			
			// 8th test - toggle available
			/*
			ProductHelper.toggleAvailable(em, 11);
			*/
			
			// 9th test - update information
			/*
			Product p = em.find(Product.class, 3);
			p.setRefPrice(1.2d);
			*/
			
			// 10th test - view all Customer
			/*
			for (Customer c : CustomerHelper.getAll(em)) {
				System.out.println(c);
			}
			*/
			
			// 11th test - HTML test
			/*
			JFrame f = new JFrame("test");
			JLabel l = new JLabel(CustomerHelper.getPaymentReportByCustomer(em, "jackvt93"));
			l.setVerticalAlignment(SwingConstants.CENTER);
			l.setHorizontalAlignment(SwingConstants.CENTER);
			f.add(l);
			f.setSize(300, 300);
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			f.setVisible(true);
			*/
			
			// 12th test - get orders by date
			/*
			java.sql.Date before = new Date(2013-1900, 11-1, 6);
			java.sql.Date after = new Date(new java.util.Date().getTime());
			System.out.println(before+"\n"+after);
			for (FoodOrder o : OrderHelper.getByDate(em, before , after)) {
				System.out.println(o.getOrderDate()+" ; "+o.getCustomerID());
			}
			*/
			
			// 13th test - chart ^_^
			/*
			java.sql.Date before = new Date(2013-1900, 11-1, 6);
			java.sql.Date after = new Date(new java.util.Date().getTime());
			int pid = 1;
			
			List<FoodOrder> fos = OrderHelper.getByDate(em, before , after);
			System.out.println(before+"\n"+after);
			for (FoodOrder o : fos) {
				System.out.println(o.getOrderDate()+" ; "+o.getCustomerID());
			}
			
			JFrame f = new JFrame("test 2 chart");
			f.setSize(1000, 550);
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			SalesReportChart srch = new SalesReportChart(pid, fos, 1000);
			JPanel p = srch.createReportPanel();
			f.add(new JLabel(em.find(Product.class, pid).getProductName()), BorderLayout.NORTH);
			f.add(p, BorderLayout.CENTER);
			f.setVisible(true);
			*/
			
			et.commit();
			System.out.println("OK");
		} catch (Exception e) {
			e.printStackTrace();
			et.rollback();
		}
		
		
	}
	
	
	public static void initSampleData(EntityManager em) throws Exception{
		// initialize sample products (foods)
		em.persist(new Product("Hotdog", 1.5d, true));
		em.persist(new Product("Cookies & Cream", 2.5d));
		em.persist(new Product("Hot Chocolate", 1.0d));
		em.persist(new Product("Cheese Pizza", 5.5d));
		em.persist(new Product("Tuna Salad", 1.5d));
		em.persist(new Product("Cheeseburger", 1.29d));
		em.persist(new Product("Filet-O-Fish", 3.49d));
		em.persist(new Product("Hamburger", 0.99d, true));
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
