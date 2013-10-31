package smartorder.presentation;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import smartorder.bussinesslogic.OrderHelper;

public class ConsoleTest {
	public static void main(String[] args) {
		EntityManager em = 
				Persistence.createEntityManagerFactory("SmartOrder").createEntityManager();
		EntityTransaction et = em.getTransaction();
		System.out.println("OK");
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
				shoppingCart.add(new CartItem(4, (int)(Math.random()*10)+1));
				shoppingCart.add(new CartItem(5, (int)(Math.random()*10)+1));
				shoppingCart.add(new CartItem(6, (int)(Math.random()*10)+1));
				shoppingCart.add(new CartItem(7, (int)(Math.random()*10)+1));
				shoppingCart.add(new CartItem(11, (int)(Math.random()*10)+1));
				// make order with this shopping cart
				OrderHelper.makeOrder(em, shoppingCart);
			 */

			// 3rd test - get and print all FoodOrder (for test)
			OrderHelper.printAllOrder(em);

			et.commit();
		} catch (Exception e) {
			e.printStackTrace();
			et.rollback();
		}

	}
}
