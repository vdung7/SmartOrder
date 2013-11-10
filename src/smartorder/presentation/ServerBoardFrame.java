package smartorder.presentation;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import smartorder.common.CartItem;
import smartorder.common.ClientRequest;
import smartorder.network.TCPServer;

public class ServerBoardFrame extends JFrame implements ActionListener {
	private JButton btnStartServer = ManagementFrame
			.createDefaultButton("Start Server");
	private JTextArea receiveBoard = new JTextArea();

	private TCPServer mServer;

	public ServerBoardFrame() {
		setSize(350, 700);
		setResizable(false);
		setLocationRelativeTo(null);
		setTitle("ServerBoard - Console");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		JPanel btnPanel = new JPanel();
		Box boxBoard = Box.createVerticalBox();

		receiveBoard.setEditable(false);
		btnStartServer.addActionListener(this);
		btnPanel.add(btnStartServer);
		boxBoard.add(new JScrollPane(receiveBoard));

		add(btnPanel, BorderLayout.NORTH);
		add(boxBoard, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// disable the start button
		btnStartServer.setEnabled(false);

		// creates the object OnMessageReceived asked by the TCPServer
		// constructor
		mServer = new TCPServer(new TCPServer.OnMessageReceived() {
			private String idToString(int id) {
				switch (id) {
				case ClientRequest.LOGIN:
					return "LOGIN";
				case ClientRequest.LOGOUT:
					return "LOGOUT";
				case ClientRequest.ORDER:
					return "ORDER";
				case ClientRequest.REGISTER:
					return "REGISTER";
				case ClientRequest.VIEWORDERS:
					return "VIEWORDERS";
				case ClientRequest.VIEWDETAIL:
					return "VIEWDETAIL";
				case ClientRequest.UPDATE:
					return "UPDATE";
				default:
					return "OTHER";
				}
			}

			private String cartToString(ArrayList<CartItem> shoppingCart) {
				String cart = "";
				for (CartItem cartItem : shoppingCart) {
					cart += cartItem.getProductID() + "-"
							+ cartItem.getQuantity() + ", ";
				}
				return cart;
			}

			@Override
			public void requestReceived(ClientRequest request) {
				int id = request.getRequestID();
				receiveBoard.append(idToString(id) + " - ");
				switch (id) {
				case ClientRequest.LOGIN:
					receiveBoard.append(request.getUsername() + "-"
							+ request.getPassword() + "\n");
					break;
				case ClientRequest.LOGOUT:
					receiveBoard.append("\n");
					break;
				case ClientRequest.ORDER:
					receiveBoard.append(cartToString(request.getShoppingCart())
							+ "\n");
					break;
				case ClientRequest.REGISTER:
					receiveBoard.append(request.getUsername() + ";"
							+ request.getPassword() + ";" + request.getName()
							+ ";" + request.getAddress() + ";"
							+ request.getFone() + ";" + request.getEmail()
							+ "\n");
					break;
				case ClientRequest.VIEWORDERS:
					receiveBoard.append("\n");
					break;
				case ClientRequest.VIEWDETAIL:
					receiveBoard.append(request.getOrderID() + "\n");
					break;
				case ClientRequest.UPDATE:
					receiveBoard.append("\n");
					break;
				default:
					receiveBoard
							.append("Oop..! I don't know what happened  >\"<");
					break;
				}
			}
		});

		mServer.start();
	}

}
