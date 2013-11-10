package smartorder.common;

import java.io.Serializable;
import java.util.ArrayList;

public class ServerResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	private ArrayList<String> menu; // item format : "id;name;price"

	// array format (in order) : fullname, address, phone, email
	private ArrayList<String> userDetail;

	private boolean isRegistered, isOrdered;
	private ArrayList<String> orderIDs;
	private String detail;

	public ServerResponse() {
		// nothing to do here ^_^
	}

	public ArrayList<String> getUserDetail() {
		return userDetail;
	}

	public void setUserDetail(ArrayList<String> userDetail) {
		this.userDetail = userDetail;
	}

	public ArrayList<String> getMenu() {
		return menu;
	}

	public void setMenu(ArrayList<String> menu) {
		this.menu = menu;
	}

	public boolean isRegistered() {
		return isRegistered;
	}

	public void setRegistered(boolean isRegistered) {
		this.isRegistered = isRegistered;
	}

	public boolean isOrdered() {
		return isOrdered;
	}

	public void setOrdered(boolean isOrdered) {
		this.isOrdered = isOrdered;
	}

	public ArrayList<String> getOrderIDs() {
		return orderIDs;
	}

	public void setOrderIDs(ArrayList<String> orderIDs) {
		this.orderIDs = orderIDs;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

}
