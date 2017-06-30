import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class SpriteBank {
	public static Map <Integer, BufferedImage> bank = new HashMap <Integer, BufferedImage> ();
	
	public static final int ID_GROUND1 = 0;
	
	public static final int ID_SLIME = 500;
	public static final int ID_VIBORA = 501;
	
	public static final int ID_PLAYER_LEFT = 1000;
	public static final int ID_PLAYER_RIGHT = 1001;
	public static final int ID_HEART = 1100;
	public static final int ID_TEXT_LIFE = 1101;
	
	public static final int ID_GAME_OVER = 2000;
	public static final int ID_WIN = 2001;
	
	public static void loadSprites () {
		bank.put(ID_GROUND1, loadSprite ("Resources" + File.separator + "dirt1.png"));
		bank.put(ID_PLAYER_LEFT, loadSprite ("Resources" + File.separator + "player_idle_left.png"));
		bank.put(ID_PLAYER_RIGHT, loadSprite ("Resources" + File.separator + "player_idle_right.png"));
		bank.put(ID_SLIME, loadSprite ("Resources" + File.separator + "slime_idle.png"));
		bank.put(ID_HEART, loadSprite ("Resources" + File.separator + "heart.png"));
		bank.put(ID_TEXT_LIFE, loadSprite ("Resources" + File.separator + "vida.png"));
		bank.put(ID_VIBORA, loadSprite ("Resources" + File.separator + "viborita.png"));
		bank.put(ID_GAME_OVER, loadSprite ("Resources" + File.separator + "gameOver.png"));
		bank.put(ID_WIN, loadSprite ("Resources" + File.separator + "win.png"));
	}
	
	public static BufferedImage loadSprite (String path) {
		try {
			BufferedImage loadedImage = ImageIO.read(Main.class.getResource(path));
			BufferedImage formatted = new BufferedImage (loadedImage.getWidth(), loadedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
			formatted.getGraphics().drawImage(loadedImage, 0, 0, null);
			return formatted;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
