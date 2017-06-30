/*
 * Clase que describe un tile de terreno por el que cualquier entidad viva puede pasar.
 */
public class Terreno extends Entidad {

	public Terreno(int x, int y) {
		super(Cons.ID_GROUND1, x, y);
		sprite = SpriteBank.ID_GROUND1;
	}

	@Override
	public int sprite() {
		return SpriteBank.ID_GROUND1;
	}
	
}
