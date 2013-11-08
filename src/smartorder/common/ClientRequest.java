package smartorder.common;

import java.io.Serializable;
import java.util.ArrayList;

public class ClientRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final int LOGIN = 1;
	public static final int REGISTER = 2;
	public static final int UPDATE = 3;
	public static final int ORDER = 4;
	public static final int VIEWORDERS = 5;
	public static final int VIEWDETAIL = 6;
	public static final int LOGOUT = 7;
	 
	private int requestID; 
	private String username, password, name, address, fone, email;	// LOGIN, REGISTER
	private ArrayList<CartItem> shoppingCart;	// ORDER
	private String orderID;	// VIEWDETAIL
	
	// LOGIN
	public ClientRequest(int requestID, String username, String password) {
		super();
		this.requestID = requestID;
		this.username = username;
		this.password = password;
	}
	
	// UPDATE, VIEWORDER
	public ClientRequest(int requestID) {
		super();
		this.requestID = requestID;
	}
	
	// REGISTER
	public ClientRequest(int requestID, String username, String password,
			String name, String address, String fone, String email) {
		super();
		this.requestID = requestID;
		this.username = username;
		this.password = password;
		this.name = name;
		this.address = address;
		this.fone = fone;
		this.email = email;
	}
	
	// ORDER
	public ClientRequest(int requestID, ArrayList<CartItem> shoppingCart) {
		super();
		this.requestID = requestID;
		this.shoppingCart = shoppingCart;
	}
	
	// VIEWDETAIL
	public ClientRequest(int requestID, String orderID) {
		super();
		this.requestID = requestID;
		this.orderID = orderID;
	}

	public int getRequestID() {
		return requestID;
	}

	public void setRequestID(int requestID) {
		this.requestID = requestID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getFone() {
		return fone;
	}

	public void setFone(String fone) {
		this.fone = fone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public ArrayList<CartItem> getShoppingCart() {
		return shoppingCart;
	}

	public void setShoppingCart(ArrayList<CartItem> shoppingCart) {
		this.shoppingCart = shoppingCart;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	@Override
	public String toString() {
		return "ClientRequest [requestID=" + requestID + "]";
	}
	
	
}
