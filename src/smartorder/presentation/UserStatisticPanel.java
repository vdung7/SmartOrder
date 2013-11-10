package smartorder.presentation;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.persistence.EntityManager;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import smartorder.bussinesslogic.CustomerHelper;
import smartorder.bussinesslogic.OrderHelper;
import smartorder.entities.Customer;

public class UserStatisticPanel extends JPanel implements ActionListener {
	private JLabel customerInfo, paymentStat, orderDetail;
	private JButton btnRefresh;
	private JList<String> orders;
	private JList<String> customers;
	
	private EntityManager em;
	private DefaultListModel<String> ordersModel;
	private DefaultListModel<String> customersModel;
	
	private String currentUserID = "";
	private String currentOrderID = "";
	private int parentWidth;
	
	public UserStatisticPanel(EntityManager em, int parentWidth) {
		super(new BorderLayout());
		this.em = em;
		this.parentWidth = parentWidth;
		
		initialize();
		arrangeComponents();
	}
	
	private void updateCustomerModel() {
		if (customersModel==null) {
			customersModel = new DefaultListModel<String>();
		} else {
			customersModel.removeAllElements();
		}
		
		em.getTransaction().begin();
		for (Customer c : CustomerHelper.getAll(em)) {
			customersModel.addElement(c.getCustomerID());
		}
		em.getTransaction().commit();
		
	}
	
	private void createOrderModel(String userID) {
		if (ordersModel==null) {
			ordersModel = new DefaultListModel<String>();
		} else {
			ordersModel.removeAllElements();
		}
		
		em.getTransaction().begin();
		for (String oid : OrderHelper.getOrdersInString(em, userID)) {
			ordersModel.addElement(oid);
		}
		em.getTransaction().commit();
	}

	private void initialize() {
		// initialize UI components
		customerInfo = new JLabel();
		paymentStat = new JLabel();
		orderDetail = new JLabel();
		
		btnRefresh = ManagementFrame.createDefaultButton("Refresh");
		
		updateCustomerModel();
		ordersModel = new DefaultListModel<String>();
		
		orders = new JList<String>(ordersModel);
		orders.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		customers = new JList<String>(customersModel);
		customers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// set event listener
		btnRefresh.addActionListener(this);
		
		orders.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// update current orderID of this report
				currentOrderID = orders.getSelectedValue();
				
				// update Order's Details report
				if (currentOrderID!=null) {
					em.getTransaction().begin();
					orderDetail.setText(OrderHelper.getDetailInHTML(em, currentOrderID));
					em.getTransaction().commit();
				}
			}
		});
		
		customers.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// update current userID of this report
				currentUserID = customers.getSelectedValue();
				
				// update Customer's Orders report
				// recreate order model with new value [userID]
				createOrderModel(currentUserID);
				orderDetail.setText("");
				
				if (currentUserID!=null) {
					em.getTransaction().begin();
					// update Customer's Information report
					customerInfo.setText(CustomerHelper.getDetailInHTML(em,
							currentUserID));
					// update Payment Statistics report
					paymentStat.setText(CustomerHelper
							.getPaymentReportByCustomer(em, currentUserID));
					em.getTransaction().commit();
				}
				
			}
		});
		
	}

	private void arrangeComponents() {
		JPanel customersPanel = new JPanel(new BorderLayout());
		JPanel reportPanel = new JPanel(new GridLayout(2, 2));
		JPanel btnPanel = new JPanel(new BorderLayout());
		
		// arrange btnPanel [refresh button]
		btnPanel.add(btnRefresh, BorderLayout.WEST);
		
		// arrange customers list panel
		customersPanel.setBorder(ManagementFrame.createDeafaultBorder("Customer List"));
		customersPanel.add(new JScrollPane(customers), BorderLayout.CENTER);
		
		// arrange report panel
		reportPanel.add(createReportPanel(customerInfo, "Customer's Informations"));
		reportPanel.add(createReportPanel(paymentStat, "Payment's Statistics"));
		reportPanel.add(createReportPanel(orders, "Customer's Orders"));
		reportPanel.add(createReportPanel(orderDetail, "Order's Details"));

		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, customersPanel, reportPanel);
		split.setDividerLocation(parentWidth/3);
		
		add(btnPanel, BorderLayout.NORTH);
		add(split, BorderLayout.CENTER);
	}

	private JPanel createReportPanel(JComponent com, String title){
		
		JPanel reportPnl = new JPanel(new BorderLayout());
		reportPnl.setBorder(ManagementFrame.createDeafaultBorder(title));
		
		if(com instanceof JLabel){
			JLabel label = (JLabel) com;
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setVerticalAlignment(SwingConstants.CENTER);
			reportPnl.add(new JScrollPane(label), BorderLayout.CENTER);
		} else {
			reportPnl.add(new JScrollPane(com), BorderLayout.CENTER);
		}
		
		return reportPnl;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		updateCustomerModel();
	}
}
