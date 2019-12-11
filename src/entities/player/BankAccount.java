package entities.player;

public class BankAccount {
	private double money;
	
	private Player player;
	
	public BankAccount(Player player) {
		this.player = player;
		this.money = 500.00; //default
	}
	
	public double getMoney() {
		return money;
	}
}
