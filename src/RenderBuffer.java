import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
/*
 * Clase utilizada para cargar sprites al buffer del canvas.
 */
public class RenderBuffer {
	private BufferedImage view; //Buffer del canvas.
	private int[] pixelArray; //Arreglo de pixeles que actua como buffer.
	
	/*
	 * Constructor que crea el arreglo de pixeles que representarán el buffer
	 * para desplegar en pantalla.
	 */
	public RenderBuffer (int width, int height) {
		view = new BufferedImage (width, height, BufferedImage.TYPE_INT_RGB);
		pixelArray = ((DataBufferInt) view.getRaster().getDataBuffer()).getData();
	}
	
	/*
	 * Método para desplegar en pantalla loque se ha cargado desde el buffer
	 */
	public void render (Graphics g) {
		g.drawImage (view, 0, 0, view.getWidth(), view.getHeight(), null);
	}
	
	/*
	 * Método que carga en el buffer un sprite, se puede poner en cualuier lugar
	 * del buffer del canvas
	 */
	public void renderImage (BufferedImage image, int xPos, int yPos) {
		int [] imagePixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		
		for (int y = 0 ; y < image.getHeight() ; y++)
			for (int x = 0 ; x < image.getWidth() ; x++) {
				if (imagePixels [x + y * image.getWidth()] != 0xFF00DC && ((x + xPos) + (y + yPos) * view.getWidth()) < pixelArray.length)
				pixelArray [(x + xPos) + (y + yPos) * view.getWidth()] = imagePixels [x + y * image.getWidth()];
			}
	}
}
