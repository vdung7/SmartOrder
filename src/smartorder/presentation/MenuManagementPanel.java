package smartorder.presentation;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.persistence.EntityManager;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import smartorder.bussinesslogic.ProductHelper;
import smartorder.entities.Product;

public class MenuManagementPanel extends JPanel implements ActionListener {
	private JButton btnEdit, btnSave, btnAdd, btnClear, btnToggleAvailable;
	private JTextField txtNewName, txtNewPrice, txtName, txtPrice;
	private JLabel currentItemID;

	private JList<Product> availables, unavailables;
	private DefaultListModel<Product> avaiModel, unavaiModel;

	private EntityManager em;
	private Product currentProduct;
	private int parentHeight;

	public MenuManagementPanel(EntityManager em, int parentHeight) {
		super(new GridLayout(1, 3));
		this.em = em;
		this.parentHeight = parentHeight;

		createListModel();
		initialize();
		arrangeComponents();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o.equals(btnAdd)) {
			try {
				// validate value before persist
				String name = txtNewName.getText();
				double refPrice = Double.parseDouble(txtNewPrice.getText());
				if (name.equals("") || name == null)
					throw new NumberFormatException();

				// persist new Product into database
				Product p = new Product(name, refPrice, false);
				em.getTransaction().begin();
				em.persist(p);
				em.getTransaction().commit();

				// update JList model
				unavaiModel.addElement(p);

			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(MenuManagementPanel.this,
						"Invalid Input!");
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(MenuManagementPanel.this,
						"Fail to persist database... Try again");
			} finally {
				JOptionPane.showMessageDialog(MenuManagementPanel.this,
						"New Product added!");
			}

		} else if (o.equals(btnClear)) {
			txtNewName.setText("");
			txtNewPrice.setText("");

		} else if (o.equals(btnEdit) && currentProduct != null) {
			if (btnEdit.getText().equals("Edit")) {
				// unlock input
				setEditable(true);
				// change action
				btnEdit.setText("Cancel");
			} else {
				// lock input
				setEditable(false);
				// change action
				btnEdit.setText("Edit");
			}

		} else if (o.equals(btnSave)) {
			try {
				// validate value before persist
				String name = txtName.getText();
				double refPrice = Double.parseDouble(txtPrice.getText());
				if (name.equals("") || name == null)
					throw new NumberFormatException();

				em.getTransaction().begin();
				currentProduct.setProductName(name);
				currentProduct.setRefPrice(refPrice);
				em.getTransaction().commit();

				// update models
				createListModel();

				// lock input
				setEditable(false);
				// change action
				btnEdit.setText("Edit");

			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(MenuManagementPanel.this,
						"Invalid Input!");
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(MenuManagementPanel.this,
						"Fail to update database... Try again");
			} finally {
				JOptionPane.showMessageDialog(MenuManagementPanel.this,
						"Product updated!");
			}

		} else if (o.equals(btnToggleAvailable) && currentProduct != null) {
			try {
				boolean toggleFlag = currentProduct.isAvailable();
				em.getTransaction().begin();
				currentProduct.setAvailable(!currentProduct.isAvailable());
				em.getTransaction().commit();

				// update JList models
				if (!toggleFlag) {
					unavaiModel.removeElement(currentProduct);
					avaiModel.addElement(currentProduct);
				} else {
					avaiModel.removeElement(currentProduct);
					unavaiModel.addElement(currentProduct);
				}

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(MenuManagementPanel.this,
						"Fail to update database...");

			}

		}
	}

	private void setEditable(boolean editable) {
		txtName.setEditable(editable);
		txtPrice.setEditable(editable);
		btnSave.setEnabled(editable);
	}

	private void createListModel() {
		// clean models
		if (avaiModel == null)
			avaiModel = new DefaultListModel<Product>();
		else
			avaiModel.removeAllElements();
		if (unavaiModel == null)
			unavaiModel = new DefaultListModel<Product>();
		else
			unavaiModel.removeAllElements();

		// fill data into models
		for (Product p : ProductHelper.getAll(em)) {
			if (p.isAvailable())
				avaiModel.addElement(p);
			else
				unavaiModel.addElement(p);
		}

	}

	private void initialize() {
		// initialize UI components
		btnEdit = ManagementFrame.createDefaultButton("Edit");
		btnSave = ManagementFrame.createDefaultButton("Save");
		btnAdd = ManagementFrame.createDefaultButton("Add");
		btnClear = ManagementFrame.createDefaultButton("Clear");
		btnToggleAvailable = ManagementFrame.createDefaultButton("<->");

		txtNewName = new JTextField();
		txtNewPrice = new JTextField();
		txtName = new JTextField();
		txtPrice = new JTextField();
		setEditable(false);

		availables = new JList<Product>(avaiModel);
		unavailables = new JList<Product>(unavaiModel);
		availables.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		unavailables.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		currentItemID = new JLabel();

		// set event listeners
		btnEdit.addActionListener(this);
		btnSave.addActionListener(this);
		btnAdd.addActionListener(this);
		btnClear.addActionListener(this);
		btnToggleAvailable.addActionListener(this);

		availables.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!availables.isSelectionEmpty()) {
					currentProduct = em.find(Product.class, availables
							.getSelectedValue().getProductID());
					fillData(currentProduct);

				}
			}
		});
		unavailables.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!unavailables.isSelectionEmpty()) {
					currentProduct = em.find(Product.class, unavailables
							.getSelectedValue().getProductID());
					fillData(currentProduct);

				}
			}
		});

	}

	private void arrangeComponents() {
		JPanel newItemPanel = new JPanel(new BorderLayout());
		JPanel editItemPanel = new JPanel(new BorderLayout());
		JPanel availablesPanel = new JPanel(new BorderLayout());
		JPanel unavailablesPanel = new JPanel(new BorderLayout());

		newItemPanel.setBorder(ManagementFrame.createDeafaultBorder("New Item"));
		editItemPanel.setBorder(ManagementFrame.createDeafaultBorder("Selected Item"));
		availablesPanel.setBorder(ManagementFrame.createDeafaultBorder("Available Items - Customer's Menu"));

		Box itemBox = Box.createVerticalBox();
		Box newItemBox = Box.createVerticalBox();
		Box newNameBox = Box.createHorizontalBox();
		Box newPriceBox = Box.createHorizontalBox();
		Box newBtnBox = Box.createHorizontalBox();
		newNameBox.add(new JLabel("new Name "));
		newNameBox.add(txtNewName);
		newPriceBox.add(new JLabel("Reference Price $ "));
		newPriceBox.add(txtNewPrice);
		newBtnBox.add(btnAdd);
		newBtnBox.add(btnClear);
		newItemBox.add(newNameBox);
		newItemBox.add(Box.createVerticalStrut(8));
		newItemBox.add(newPriceBox);
		newItemBox.add(Box.createVerticalStrut(8));
		newItemBox.add(newBtnBox);
		newItemPanel.add(newItemBox);

		Box editItemBox = Box.createVerticalBox();
		Box editIdBox = Box.createHorizontalBox();
		Box editNameBox = Box.createHorizontalBox();
		Box editPriceBox = Box.createHorizontalBox();
		Box editBtnBox = Box.createHorizontalBox();
		editIdBox.add(new JLabel("ID: "));
		editIdBox.add(currentItemID);
		editNameBox.add(new JLabel("Name "));
		editNameBox.add(txtName);
		editPriceBox.add(new JLabel("Reference Price $ "));
		editPriceBox.add(txtPrice);
		editBtnBox.add(btnEdit);
		editBtnBox.add(btnSave);
		editItemBox.add(editIdBox);
		editItemBox.add(Box.createVerticalStrut(8));
		editItemBox.add(editNameBox);
		editItemBox.add(Box.createVerticalStrut(8));
		editItemBox.add(editPriceBox);
		editItemBox.add(Box.createVerticalStrut(8));
		editItemBox.add(editBtnBox);
		editItemPanel.add(editItemBox);

		itemBox.add(newItemPanel);
		itemBox.add(editItemPanel);
		itemBox.add(Box.createVerticalStrut((parentHeight / 2)));

		JPanel list = new JPanel(new BorderLayout());
		list.add(new JScrollPane(unavailables), BorderLayout.CENTER);
		list.setBorder(ManagementFrame.createDeafaultBorder("Unavailable Items"));
		unavailablesPanel.add(list, BorderLayout.CENTER);
		JPanel btn = new JPanel();
		btn.add(Box.createVerticalStrut(parentHeight / 2));
		btn.add(btnToggleAvailable);
		unavailablesPanel.add(btn, BorderLayout.EAST);
		availablesPanel.add(new JScrollPane(availables), BorderLayout.CENTER);

		add(itemBox);
		add(unavailablesPanel);
		add(availablesPanel);

	}

	private void fillData(Product p) {
		currentItemID.setText("" + p.getProductID());
		txtName.setText(p.getProductName());
		txtPrice.setText("" + String.format("%2.2f", p.getRefPrice()));
	}

}
