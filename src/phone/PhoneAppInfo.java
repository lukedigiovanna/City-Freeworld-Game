package phone;

import java.awt.image.BufferedImage;

import misc.ImageTools;
import misc.StringTools;

/**
 * Holds basic data about the phone such as name and icon
 */
public enum PhoneAppInfo {
	
	CONTACTS("contacts"),
	MAPS("maps"),
	MESSAGES("messages"),
	EMAIL("email"),
	INTERNET("internet");
	
	public BufferedImage icon;
	public String displayName, id;
	
	PhoneAppInfo(String id) {
		this.id = id;
		this.displayName = StringTools.underscoreToDisplay(this.id);
		this.icon = ImageTools.getImage("phone/app_icons/"+this.id+".png");
	}
}
