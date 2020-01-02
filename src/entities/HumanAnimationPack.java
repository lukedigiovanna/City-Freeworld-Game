package entities;

import display.Animation;

public enum HumanAnimationPack {
	
	CHARACTER_0("character_0",11f/16f,12f/16f),
	CHARACTER_1("character_1",11f/16f,12f/16f);
	
	Animation idle, walk, corpse, holdingLongGun, holdingShortGun;
	
	float width, height;
	
	HumanAnimationPack(String folderPath, float width, float height) {
		folderPath = "assets/images/characters/"+folderPath;
		idle = new Animation(folderPath,"idle_",1);
		walk = new Animation(folderPath,"walk_",8);
		corpse = new Animation(folderPath,"corpse_",1);
		holdingLongGun = new Animation(folderPath,"holding_long_gun_",1);
		holdingShortGun = new Animation(folderPath,"holding_short_gun_",1);
		
		this.width = width;
		this.height = height;
	}

}
