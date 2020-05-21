package entities.player;

import java.io.Serializable;

public class BankAccount implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private double money;
	private double totalIncome;
	
	private Player player;
	
	public BankAccount(Player player) {
		this.player = player;
		this.money = 50000.00; //default
		this.totalIncome = 0.00;
	}
	
	public void addMoney(double amount) {
		this.money += amount;
		this.totalIncome += amount;
	}
	
	public boolean useMoney(double amount) {
		if (this.money >= amount) {
			this.money-=amount;
			return true;
		} else {
			return false;
		}
	}
	
	public double getMoney() {
		return money;
	}
	
	public double getTotalIncome() {
		return this.totalIncome;
	}

	public Player getPlayer() {
		return this.player;
	}
}
