package smartorder.network;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.swing.*;

import smartorder.common.*;
import smartorder.bussinesslogic.CustomerHelper;
import smartorder.bussinesslogic.OrderHelper;
import smartorder.bussinesslogic.ProductHelper;
import smartorder.entities.Customer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The class extends the Thread class so we can receive and send messages at the
 * same time
 */
public class TCPServer extends Thread {

	public static final int SERVERPORT = 4444;
	public static final String SAMPLE_QUERY = "menu?";
	private boolean running = false;
	private OnMessageReceived messageListener;

	private static int counter = 0;

	/**
	 * Constructor of the class
	 * 
	 * @param messageListener
	 *            listens for the messages
	 */
	public TCPServer(OnMessageReceived messageListener) {
		this.messageListener = messageListener;
	}

	@Override
	public void run() {

		running = true;

		try {
			System.out.println("S: Connecting...");

			// create a server socket. A server socket waits for requests to
			// come in over the network.
			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(SERVERPORT);

			while (true) {
				// create client socket... the method accept() listens for a
				// connection to be made to this socket and accepts it.
				final Socket client = serverSocket.accept();
				// increase connection counter
				counter++;
				System.out.println("S: Receiving... " + getCounter());

				// create new Thread to process a new client connection
				Thread clientThread = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							processClientConnection(client);
							client.close();
						} catch (EOFException e) {
							System.out.println("S: Done a session!");
						} catch (Exception e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(null,
									"ERROR!\n" + e.getMessage());
						} finally {
							// System.out.println("S: Done a session!");
						}
					}
				});
				clientThread.start();
			}
			
		} catch (Exception e) {
			System.out.println("S: Error");
			e.printStackTrace();
		} 

	}

	public static int getCounter() {
		return counter;
	}

	public static void setCounter(int counter) {
		TCPServer.counter = counter;
	}

	private void processClientConnection(Socket client) throws Exception {
		// sends the message to the client
		ObjectOutputStream output = new ObjectOutputStream(
				client.getOutputStream());
		// read the message received from client
		ObjectInputStream input = new ObjectInputStream(client.getInputStream());

		// use for maintain a login session
		boolean login = false;
		String currentUser = "";
		// in this while we wait to receive request from client (it's an
		// infinite loop)
		// this while it's like a listener for messages
		while (running) {

			ClientRequest request = (ClientRequest) input.readObject();

			if (request != null && messageListener != null) {
				// call the method requestReceived from GUI classes
				messageListener.requestReceived(request);

				// process request and prepare a response
				ServerResponse response = new ServerResponse();
				EntityManager em = Persistence.createEntityManagerFactory(
						"SmartOrder").createEntityManager();
				EntityTransaction et = em.getTransaction();
				try {
					et.begin();

					switch (request.getRequestID()) {
					case ClientRequest.LOGIN: // return menu and user's detail
												// when LOGIN
						Customer c = CustomerHelper.checkLogin(em,
								request.getUsername(), request.getPassword());
						if (c != null) {
							response.setUserDetail(c.toArray());
							response.setMenu(ProductHelper.getMenuByAvailable(
									em, true));
							login = true;
							currentUser = request.getUsername();
						}
						break;
					case ClientRequest.LOGOUT: // lock other functions when
												// LOGOUT
						if (login) {
							login = false;
							currentUser = "";
						}
						break;
					case ClientRequest.UPDATE: // return menu again when UPDATE
						if (login) {
							response.setMenu(ProductHelper.getMenuByAvailable(
									em, true));
						}
						break;
					case ClientRequest.REGISTER: // return true if register
													// success
						boolean isReg = CustomerHelper.register(em,
								request.getUsername(), request.getPassword(),
								request.getName(), request.getAddress(),
								request.getFone(), request.getEmail());
						response.setRegistered(isReg);
						break;
					case ClientRequest.ORDER: // return true if order success
						if (login) {
							if (request.getShoppingCart().size() > 0) {
								boolean ok = OrderHelper.makeOrder(em,
													request.getShoppingCart(), currentUser);
								response.setOrdered(ok);
							} else {
								response.setOrdered(false);
							}
						}
						break;
					case ClientRequest.VIEWDETAIL: // return order's details
						if (login) {
							response.setDetail(OrderHelper
									.getDetailInSimpleHTML(em,
											request.getOrderID()));
						}
						break;
					case ClientRequest.VIEWORDERS: // return order list
						if (login) {
							response.setOrderIDs(OrderHelper.getOrdersInString(
									em, currentUser));
						}
						break;
					}

					// send response
					output.writeObject(response);
					output.flush();
					et.commit();
				} catch (Exception e) { // more kinds of exception will be
										// process here
					e.printStackTrace();
					output.writeObject(new ServerResponse());
					output.flush();
					et.rollback();
				}

			}
		}

	}

	// Declare the interface. The method messageReceived(String message) will
	// must be implemented in the ServerBoard
	// class at on startServer button click
	public interface OnMessageReceived {
		public void requestReceived(ClientRequest request);
	}

}
