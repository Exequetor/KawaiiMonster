
	/*
	 * Clase que controla el retardo antes de que el jugador puda volver a moverse.
	 * @see java.lang.Runnable#run()
	 */
public class BeatMechanism implements Runnable {

	public static boolean beat = false; //Bandera que es usada al momento de un evento de tipo KeyListener.
										//Si beat es verdadero, ejecuta movimiento, si no, se ignora.
	public static final int TIME_GATE = Cons.CLK/4; //Ventana de tiempo para que el jugador pulse una tecla.
	
	@Override
	/*
	 * Método principal con un loop hasta que el juego finalice
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
			while (Main.gameAlive) {
				try {
					Thread.sleep(Cons.CLK - TIME_GATE);
					beat = true;
					Thread.sleep(TIME_GATE);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
					
				
			}
		
	}

}
