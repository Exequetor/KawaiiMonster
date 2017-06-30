import java.util.Random;
/*
 * Clase monstruo que hereda de entidad viva e implementa Runnable. Esta clase es runnable
 * porque la entidad instanciada a través de esta clase, tendrá un comportamiento de bot.
 * Esta clase obliga a implementar el método comportamiento para que el bot pueda funcionar.
 */
public abstract class Monstruo extends EntidadViva implements Runnable {

	public Monstruo(int id, int vida, int damage, int x, int y) {
		super(id, vida, damage, x, y);
		Thread behaviour = new Thread (this);
		behaviour.start();
	}

	/*
	 * Método abstracto que obliga a las clases que hereden de esta clasem a definir
	 * el método de comportamiento, que describirá el comportamiento del bot o monstruo
	 */
	public abstract void comportamiento();
	
	/*
	 * Método util para el comportamiento. Este método genera un movimiento de acuerdo
	 * a las condificiones especificadas para seguir al jugador.
	 */
	public void movToPlayer (int espacios) {
		int pX = 0, pY = 0;
		Random rand = new Random ();
		boolean flag = true;
		for (int y = 0 ; y < Critico.lifeMap.length ; y++)
			for (int x = 0 ; x < Critico.lifeMap[0].length ; x++)
				if (Critico.lifeMap[y][x] != null)
					if (Critico.lifeMap[y][x].ID_Entidad == Cons.ID_JUGADOR) {
						pX = x;
						pY = y;
						y = Critico.lifeMap.length;
						x = Critico.lifeMap[0].length;
					}
		switch (rand.nextInt(2)) {
		case 0:
			if (getPosX() < pX) {
				mov(espacios, Cons.DERECHA);
				flag = false;
			}
			if (getPosX() > pX && flag) {
				mov(espacios, Cons.IZQUIERDA);
				flag = false;
			}
		case 1:
			if (getPosY() < pY && flag) {
				mov(espacios, Cons.ABAJO);
				flag = false;
				flag = false;
			}
			if (getPosY() > pY && flag) {
				mov(espacios, Cons.ARRIBA);
				flag = false;
			}
		break;
		}
		
	}
	
	public void run () {
		while (isAlive())
			comportamiento ();
	}
	
}
