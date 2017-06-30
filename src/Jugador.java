/*
 * Clase que representa a la entidad del jugador. Esta clase hereda
 * de Entidad Viva, pero el control de esta entidad está especificada
 * en la clase principal el juego.
 */
public class Jugador extends EntidadViva {
	public Jugador(int vida, int x, int y) {
		super(Cons.ID_JUGADOR, vida, 1, x, y);
		movimiento = Cons.DERECHA;
		sprite = SpriteBank.ID_PLAYER_RIGHT;
	}

	/*
	 * Retorna el sprite segun la dirección en la que está mirando
	 * descrita por la variable movimiento
	 * @see Entidad#sprite()
	 */
	public int sprite () {
		switch (movimiento) {
		case Cons.IZQUIERDA: sprite = SpriteBank.ID_PLAYER_LEFT; break;
		case Cons.DERECHA: sprite = SpriteBank.ID_PLAYER_RIGHT; break;
		}
		return sprite;
	}
	
	@Override
	public Runnable animation() {
		return new Runnable () {
			@Override
			public void run() {
				
			}
		};
	}

}
