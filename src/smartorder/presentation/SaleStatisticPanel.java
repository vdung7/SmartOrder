package smartorder.presentation;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.persistence.EntityManager;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import smartorder.bussinesslogic.OrderHelper;
import smartorder.bussinesslogic.ProductHelper;
import smartorder.entities.Product;

public class SaleStatisticPanel extends JPanel {
	private JPanel chartPanel;
	private JComboBox<Product> cbProduct;
	private JComboBox<String> cbDateRange;
	private JButton btnGo;

	private DefaultComboBoxModel<Product> model;
	private EntityManager em;
	private int parentWidth;

	public SaleStatisticPanel(final EntityManager em, int parentWidth) {
		super(new BorderLayout());
		this.em = em;
		this.parentWidth = parentWidth;

		cbDateRange = new JComboBox<String>();
		cbProduct = new JComboBox<Product>();
		cbDateRange.setAutoscrolls(false);
		cbProduct.setAutoscrolls(false);

		cbDateRange.addItem("Last 7 day");
		cbDateRange.addItem("Last 30 day");
		cbDateRange.addItem("Yesterday");
		cbDateRange.setSelectedIndex(1);

		updateProductList();
		cbProduct.setModel(model);

		btnGo = ManagementFrame.createDefaultButton("Go");

		JPanel configPanel = new JPanel();
		configPanel.add(new JLabel("Select Product"));
		configPanel.add(cbProduct);
		configPanel.add(new JLabel("Select Date Range"));
		configPanel.add(cbDateRange);
		configPanel.add(btnGo);

		chartPanel = new JPanel();
		refreshChartPanel();

		add(configPanel, BorderLayout.NORTH);
		add(chartPanel);

		btnGo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshChartPanel();
			}
		});
	}

	public void updateProductList() {
		System.out.println("updating combobox model...");
		if (model == null) {
			model = new DefaultComboBoxModel<Product>();
		} else {
			model.removeAllElements();
		}
		em.getTransaction().begin();
		for (Product p : ProductHelper.getAll(em)) {
			model.addElement(p);
		}
		em.getTransaction().commit();
		System.out.println("combobox model updated!");
	}

	private void refreshChartPanel() {
		// get time before and after(today)
		Date before;
		Date today = new Date(new java.util.Date().getTime());
		switch (cbDateRange.getSelectedIndex()) {
		case 0:
			before = getBeforeDate(SalesReportChart.LAST_7_DAYS);
			break;
		case 1:
			before = getBeforeDate(SalesReportChart.LAST_30_DAYS);
			break;
		case 2:
			before = getBeforeDate(SalesReportChart.YESTERDAY);
			break;
		default:
			before = getBeforeDate(SalesReportChart.LAST_30_DAYS);
			break;
		}

		SaleStatisticPanel.this.remove(chartPanel);

		int pid = model.getElementAt(cbProduct.getSelectedIndex())
				.getProductID();
		chartPanel.removeAll();
		chartPanel = new SalesReportChart(pid, OrderHelper.getByDate(em, before, today), parentWidth).createReportPanel();
		chartPanel.revalidate();

		SaleStatisticPanel.this.add(chartPanel, BorderLayout.CENTER);
		SaleStatisticPanel.this.revalidate();
	}

	private Date getBeforeDate(int days) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.add(Calendar.DATE, -days);
		java.sql.Date range = new java.sql.Date(cal.getTime().getTime());
		return range;
	}

	public DefaultComboBoxModel<Product> getModel() {
		return model;
	}

}
