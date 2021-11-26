package structures;

import java.util.ArrayList;
import java.util.List;

public class User {

    int userID;
    String username;
    String encryptedPassword;
    String saltValue;
    int passwordLength;
    boolean addPermission;
    boolean editPermission;
    boolean deletePermission;
    String encryptedNumber;

    public User() {

    }

    public User(int userID, String username, String encryptedPassword, String saltValue, boolean addPermission, boolean editPermission,
                boolean deletePermission) {
        this.userID = userID;
        this.username = username;
        this.encryptedPassword = encryptedPassword;
        this.saltValue = saltValue;
        this.addPermission = addPermission;
        this.editPermission = editPermission;
        this.deletePermission = deletePermission;
    }

    public User(int userID, String username, String encryptedPassword, String saltValue, boolean addPermission, boolean editPermission,
                boolean deletePermission, String encryptedNumber) {
        this.userID = userID;
        this.username = username;
        this.encryptedPassword = encryptedPassword;
        this.saltValue = saltValue;
        this.addPermission = addPermission;
        this.editPermission = editPermission;
        this.deletePermission = deletePermission;
        this.encryptedNumber = encryptedNumber;
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

    public User(int userID, String username, String encryptedPassword, int passwordLength, boolean addPermission, boolean editPermission,
                boolean deletePermission) {
        this.userID = userID;
        this.username = username;
        this.encryptedPassword = encryptedPassword;
        this.passwordLength = passwordLength;
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

    public User(String username, String encryptedPassword, int pwLength, boolean addPermission, boolean editPermission,
                boolean deletePermission) {
        this.username = username;
        this.encryptedPassword = encryptedPassword;
        this.addPermission = addPermission;
        this.editPermission = editPermission;
        this.deletePermission = deletePermission;
        this.passwordLength = pwLength;
    }

    public User(String username, String encryptedPassword, String saltValue, int pwLength, boolean addPermission, boolean editPermission,
                boolean deletePermission) {
        this.username = username;
        this.encryptedPassword = encryptedPassword;
        this.saltValue = saltValue;
        this.addPermission = addPermission;
        this.editPermission = editPermission;
        this.deletePermission = deletePermission;
        this.passwordLength = pwLength;
    }

    public User(String username, String encryptedPassword, String saltValue, int pwLength, boolean addPermission, boolean editPermission,
                boolean deletePermission, String encryptedNumber) {
        this.username = username;
        this.encryptedPassword = encryptedPassword;
        this.saltValue = saltValue;
        this.addPermission = addPermission;
        this.editPermission = editPermission;
        this.deletePermission = deletePermission;
        this.passwordLength = pwLength;
        this.encryptedNumber = encryptedNumber;
    }

    public User(String username, String encryptedPassword, List<Boolean> privilegeList) {
        this.username = username;
        this.encryptedPassword = encryptedPassword;
        this.addPermission = privilegeList.get(0);
        this.editPermission = privilegeList.get(1);
        this.deletePermission = privilegeList.get(2);
    }

    public User(String username, String encryptedPassword, int passwordLength, List<Boolean> privilegeList) {
        this.username = username;
        this.encryptedPassword = encryptedPassword;
        this.passwordLength = passwordLength;
        this.addPermission = privilegeList.get(0);
        this.editPermission = privilegeList.get(1);
        this.deletePermission = privilegeList.get(2);
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setSaltVal(String saltVal) {
        this.saltValue = saltVal;
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

    public void setEncryptedNumber(String encryptedNumber) {
        this.encryptedNumber = encryptedNumber;
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

    public String getSaltVal() {
        return this.saltValue;
    }

    public String getEncryptedNumber() {
        return encryptedNumber;
    }

    public ArrayList<Boolean> getUserPermissions() {
        ArrayList<Boolean> permissionList = new ArrayList<>();
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
