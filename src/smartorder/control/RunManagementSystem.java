package smartorder.control;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import smartorder.presentation.ManagementFrame;

public class RunManagementSystem {
	public static void main(String[] args) {

		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// connect Database
				EntityManager em = Persistence.createEntityManagerFactory(
						"SmartOrder").createEntityManager();
				try {
					new ManagementFrame(em).setVisible(true);
				} catch (Exception e) {
					em.getTransaction().rollback();
					JOptionPane.showMessageDialog(null, "Error!\n" + e.getMessage());
				}
			}
		});

	}
}
