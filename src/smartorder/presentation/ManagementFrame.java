package smartorder.presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;

import javax.persistence.EntityManager;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ManagementFrame extends JFrame {
	public static final int DEFAULT_WIDTH = 1000;
	public static final int DEFAULT_HEIGHT = 650;
	public static final Color DEFAULT_COLOR_BACKGROUND = new Color(0, 153, 0); // dark
																				// green
	public static final Color DEFAULT_COLOR_TEXT_inBACKGROUND = Color.WHITE;
	public static final Color DEFAULT_COLOR_TEXT = new Color(0, 153, 0); // dark
																			// green
	public static final Color DEFAULT_COLOR = new Color(0, 153, 0); // dark
																	// green

	private JTabbedPane tabbedPane;

	private MenuManagementPanel menuPanel;
	private SaleStatisticPanel salePanel;
	private UserStatisticPanel userPanel;

	private EntityManager em;

	public ManagementFrame(EntityManager em) {
		this.em = em;

		initialize();

		addHeader();
		addFooter();
		// to be continue ....

	}

	private void initialize() {
		setTitle("SmartOrder Management System");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setLayout(new BorderLayout());
		setResizable(false);

		tabbedPane = new JTabbedPane();

		menuPanel = new MenuManagementPanel(em, DEFAULT_HEIGHT);
		salePanel = new SaleStatisticPanel(em, DEFAULT_WIDTH);
		userPanel = new UserStatisticPanel(em, DEFAULT_WIDTH);

		tabbedPane.addTab("Menu Management", null, menuPanel, "Edit your Menu");
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
		tabbedPane.addTab("Sales Statistics", null, salePanel, "Sales Reports");
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
		tabbedPane.addTab("User Statistics", null, userPanel, "User Statistics Reports");
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

		// tab change listener
		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				salePanel.updateProductList();
			}
		});

		// Add the tabbedPane to this panel.
		add(tabbedPane, BorderLayout.CENTER);

		// The following line enables to use scrolling tabs.
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

	}

	public static JButton createDefaultButton(String text) {
		JButton button = new JButton(text);
		button.setBackground(DEFAULT_COLOR_BACKGROUND);
		button.setForeground(DEFAULT_COLOR_TEXT_inBACKGROUND);
		return button;
	}

	private void addFooter() {
		JPanel header = new JPanel(new BorderLayout());
		Font font = new Font("", Font.BOLD, 40);
		
		JLabel headerLabel = new JLabel("Smart Order");
		headerLabel.setFont(font);
		headerLabel.setBackground(DEFAULT_COLOR_BACKGROUND);
		headerLabel.setForeground(DEFAULT_COLOR_TEXT_inBACKGROUND);
		headerLabel.setVerticalAlignment(SwingConstants.CENTER);
		headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		header.setBackground(DEFAULT_COLOR_BACKGROUND);
		header.add(headerLabel, BorderLayout.CENTER);
		
		add(header, BorderLayout.NORTH);
	}

	private void addHeader() {
		JPanel footer = new JPanel(new BorderLayout());
		
		JLabel footerLabel = new JLabel("<html>Developed by <font color=red>CyberSoft Team 1</font> - [VietDung & VanThi]</html>");
		footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		footer.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		footer.add(footerLabel, BorderLayout.CENTER);
		add(footer, BorderLayout.SOUTH);
		
	}
	
	public static Border createDeafaultBorder(String title){
		TitledBorder mBorder = new TitledBorder(title);
		mBorder.setTitleColor(ManagementFrame.DEFAULT_COLOR_TEXT);
		return mBorder;
	}
	
	// test
	public static void main(String[] args) {
		new ManagementFrame(null).setVisible(true);
	}

}
