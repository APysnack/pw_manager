package pw_manager;
import java.util.ArrayList;


public class Main {
	public static void main(String args[]) {
		DbConnection connect = new DbConnection();
		ArrayList<String> arrList = connect.get_all_from_db();
		System.out.println(arrList);
	}
}
