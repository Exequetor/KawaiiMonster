
/*
 * Clase que define al monstruo slime.
 */
public class Slime extends Monstruo {

	public Slime(int x, int y) {
		super(Cons.ID_SLIME, 1, 1, x, y);
		sprite = SpriteBank.ID_SLIME;
	}
	/*
	 * Su comportamiento se caracteriza por saltar arriba y abajo cada dos turnos.
	 * @see Monstruo#comportamiento()
	 */
	@Override
	public void comportamiento() {
		mov(1, Cons.ARRIBA);
		waitBeat();
		waitBeat();
		mov(1,Cons.ABAJO);
		waitBeat();
		waitBeat();
	}

	@Override
	public Runnable animation() {
		// TODO Auto-generated method stub
		return null;
	}

}
