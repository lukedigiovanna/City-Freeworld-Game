package misc;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import main.Program;

public class ImageTools {
	public static final BufferedImage IMAGE_NOT_FOUND = getImageNotFound(),
									  BLANK = getBlank();
	
	public static BufferedImage convertTo8Bit(BufferedImage image) {
		if (image == null)
			return null;
		
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				Color c8 = Color8.to8Bit(new Color(image.getRGB(x, y)));
				image.setRGB(x, y, c8.getRGB());
			}
		}
		return image;
	}
	
	public static BufferedImage convertTo8Bit(Image image) {
		if (image instanceof BufferedImage) 
			return convertTo8Bit((BufferedImage)image);
		else
			return convertTo8Bit(toBufferedImage(image));
	}
	
	public static Image getImage(String filePath) {
		//for now lets just follow the file path to our images folder : "assets/images/"
		if (filePath.indexOf("/") < 0)
			filePath = "assets/images/"+filePath;
		Image image = (new ImageIcon(filePath)).getImage();
		if (image == null) { //ie there is no image with that filePath
			//create the image not found image
			return IMAGE_NOT_FOUND;
		}
		return image;
	}
	
	public static BufferedImage getBufferedImage(String filePath) {
		return toBufferedImage(getImage(filePath));
	}
	
	public static BufferedImage toBufferedImage(Image image) {
		if (image == null || image.getWidth(null) < 0 || image.getHeight(null) < 0)
			return null;
		
		if (image instanceof BufferedImage)
			return (BufferedImage)image;
		
		BufferedImage img = new BufferedImage(image.getWidth(null),image.getHeight(null),BufferedImage.TYPE_INT_ARGB);
		img.getGraphics().drawImage(image, 0, 0, null);
		return img;
	}
	
	public static BufferedImage rescale(BufferedImage image, int newWidth, int newHeight) {
		BufferedImage rescaled = new BufferedImage(newWidth, newHeight, image.getType());
		float widthFactor = (float)image.getWidth()/newWidth, heightFactor = (float)image.getHeight()/newHeight; 
		System.out.println(widthFactor);
		for (int x = 0; x < newWidth; x++) 
			for (int y = 0; y < newHeight; y++) 
				rescaled.setRGB(x, y, image.getRGB((int)(x*widthFactor), (int)(y*heightFactor)));
		return rescaled;
	}
	
	public static BufferedImage rescale(Image image, int newWidth, int newHeight) {
		return rescale(toBufferedImage(image),newWidth,newHeight);
	}
	
	private static BufferedImage getImageNotFound() {
		BufferedImage notFound = new BufferedImage(2,2,BufferedImage.TYPE_INT_ARGB);
		notFound.setRGB(0, 0, Color.black.getRGB());
		notFound.setRGB(1, 1, Color.black.getRGB());
		notFound.setRGB(0, 1, Color.magenta.getRGB());
		notFound.setRGB(1, 0, Color.magenta.getRGB());
		return notFound;
	}
	
	private static BufferedImage getBlank() {
		BufferedImage blank = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
		return blank;
	}
}
