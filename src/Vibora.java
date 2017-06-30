/*
 * Clase que describe al monstruo tipo vibora.
 */
public class Vibora extends Monstruo {

	public Vibora(int x, int y) {
		super(Cons.ID_VIBORA, 3, 2, x, y);
		sprite = SpriteBank.ID_VIBORA;
	}
	/*
	 * El comportamiento del monstruo vibora es seguir al jugador cada dos turnos.
	 * @see Monstruo#comportamiento()
	 */
	@Override
	public void comportamiento() {
		movToPlayer(1);
		waitBeat();
		waitBeat();
	}

	@Override
	public Runnable animation() {
		return null;
	}

}
