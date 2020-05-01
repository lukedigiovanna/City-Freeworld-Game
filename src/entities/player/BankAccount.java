package entities.player;

import java.io.Serializable;

public class BankAccount implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private double money;
	
	private Player player;
	
	public BankAccount(Player player) {
		this.player = player;
		this.money = 0.00; //default
	}
	
	public void addMoney(double amount) {
		this.money += amount;
	}
	
	public double getMoney() {
		return money;
	}

	public Player getPlayer() {
		return this.player;
	}
}
