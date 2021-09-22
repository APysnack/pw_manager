package pw_manager;

import java.util.ArrayList;
import java.util.List;

public class User {

	int userID;
	int passwordLength;
	String username;
	String encryptedPassword;
	boolean addPermission;
	boolean editPermission;
	boolean deletePermission;

	public User() {

	}

	public User(int userID, String username, String encryptedPassword, boolean addPermission, boolean editPermission,
			boolean deletePermission) {
		this.userID = userID;
		this.username = username;
		this.encryptedPassword = encryptedPassword;
		this.addPermission = addPermission;
		this.editPermission = editPermission;
		this.deletePermission = deletePermission;
	}

	public User(String username, String encryptedPassword, boolean addPermission, boolean editPermission,
			boolean deletePermission) {
		this.username = username;
		this.encryptedPassword = encryptedPassword;
		this.addPermission = addPermission;
		this.editPermission = editPermission;
		this.deletePermission = deletePermission;
	}

	public User(String username, String encryptedPassword, List<Boolean> privilegeList) {
		this.username = username;
		this.encryptedPassword = encryptedPassword;
		this.addPermission = privilegeList.get(0);
		this.editPermission = privilegeList.get(1);
		this.deletePermission = privilegeList.get(2);
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	public void setPasswordLength(int passwordLength) {
		this.passwordLength = passwordLength;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	public void setAddPermission(boolean addPermission) {
		this.addPermission = addPermission;
	}

	public void setEditPermission(boolean editPermission) {
		this.editPermission = editPermission;
	}

	public void setDeletePermission(boolean deletePermission) {
		this.deletePermission = deletePermission;
	}

	public void setUserPermissions(boolean addPermission, boolean editPermission, boolean deletePermission) {
		this.addPermission = addPermission;
		this.editPermission = editPermission;
		this.deletePermission = deletePermission;
	}

	public int getUserID() {
		return userID;
	}

	public int getPasswordLength() {
		return passwordLength;
	}
	
	public String getUsername() {
		return username;
	}

	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	public boolean getAddPermission() {
		return addPermission;
	}

	public boolean getEditPermission() {
		return editPermission;
	}

	public boolean getDeletePermission() {
		return deletePermission;
	}

	public List<Boolean> getUserPermissions() {
		List<Boolean> permissionList = new ArrayList<>();
		permissionList.add(addPermission);
		permissionList.add(editPermission);
		permissionList.add(deletePermission);
		return permissionList;
	}

	@Override
	public String toString() {
		String userInfo = "[name: " + username + "], [password: " + encryptedPassword + "], [add permission: "
				+ addPermission + "], " + "[edit permission: " + editPermission + "], [delete permission: " + deletePermission + "]";
		return userInfo;
	}
}