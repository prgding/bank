package pojo;

public class User {
	private long id;
	private String username;
	private double balance;

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", username='" + username + '\'' +
				", balance=" + balance +
				'}';
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getusername() {
		return username;
	}

	public void setusername(String username) {
		this.username = username;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public User(long id, String username, double balance) {
		this.id = id;
		this.username = username;
		this.balance = balance;
	}
}
