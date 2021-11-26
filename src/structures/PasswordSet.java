package structures;

import java.util.ArrayList;

public class PasswordSet {

    String applicationName;
    ArrayList<Password> passwordList;

    public PasswordSet() {

    }

    public PasswordSet(String applicationName, ArrayList<Password> passwordList) {
        this.applicationName = applicationName;
        this.passwordList = passwordList;
    }

    public PasswordSet(String applicationName) {
        this.applicationName = applicationName;
        passwordList = new ArrayList<Password>();
    }


    public void setPasswordList(ArrayList<Password> passwordList){
        this.passwordList = passwordList;
    }

    public ArrayList<Password> getPasswordList(){
        return this.passwordList;
    }

    public void setAppName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getAppName() {
        return this.applicationName;
    }

    public void addPassword(Password password) {
        passwordList.add(password);
    }
}
