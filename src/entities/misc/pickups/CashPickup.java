package entities.misc.pickups;

import entities.player.Player;

public class CashPickup extends Pickupable {
	
	private float value;
	public CashPickup(float x, float y, float value) {
		super("cash_icon",x,y,0.6f,0.6f);
		this.value = value;
	}
		
	public void onPickup(Player player) {
		player.addMoney(this.value);
	}
}
