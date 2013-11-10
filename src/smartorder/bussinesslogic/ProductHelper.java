package smartorder.bussinesslogic;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import smartorder.entities.Product;

public class ProductHelper {

	public static ArrayList<String> getMenuInStringArray(EntityManager em)
			throws Exception {
		ArrayList<String> menu = new ArrayList<>();

		Query query = em.createNamedQuery("Product.findAllAvailable");
		query.setParameter(1, true);
		@SuppressWarnings("unchecked")
		List<Product> pmenu = (List<Product>) query.getResultList();

		for (int i = 0; i < pmenu.size(); i++) {
			menu.add(pmenu.get(i).toString());
		}

		return menu;
	}

	public static ArrayList<String> getMenuByAvailable(EntityManager em,
			boolean available) {
		ArrayList<String> menu = new ArrayList<String>();

		Query query = em.createNamedQuery("Product.findAllAvailable");
		query.setParameter(1, available);
		@SuppressWarnings("unchecked")
		List<Product> pmenu = (List<Product>) query.getResultList();

		for (int i = 0; i < pmenu.size(); i++) {
			menu.add(pmenu.get(i).toString());
		}

		return menu;
	}

	public static void toggleAvailable(EntityManager em, int pid) {
		Product p = em.find(Product.class, pid);
		boolean isAvailable = p.isAvailable();
		p.setAvailable(!isAvailable);
	}

	@SuppressWarnings("unchecked")
	public static List<Product> getAll(EntityManager em) {
		Query query = em.createNamedQuery("Product.findAll");
		return (List<Product>) query.getResultList();
	}

}
