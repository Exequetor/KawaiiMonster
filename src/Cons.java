/*
 * Clase que contiene todas las constantes del juego
 */

public class Cons {
	//ID de las identides de la matriz
	//ID terrenos
	public static final int ID_GROUND1 = 0;
	public static final int ID_PARED = 1;
	//ID Jugador
	public static final int ID_JUGADOR = 100;
	
	//ID Monstruos
	public static final int ID_TRIPA = 10;
	public static final int ID_SLIME = 11;
	public static final int ID_VIBORA = 12;
	
	//Constantes del tamaño global del mundo
	public static final int WORLD_X = 100;
	public static final int WORLD_Y = 100;
	
	//Constantes que se usan para indicar la dirección de las entidades vivas para moverse
	public static final int IZQUIERDA = 0;
	public static final int ARRIBA = 1;
	public static final int DERECHA = 2;
	public static final int ABAJO = 3;
	
	public static final int CLK = 100; //Tiempo mínimo de espera entre cada movimiento.
	public static final int TILE_SIZE = 64; //Tamaño de un tile para los sprites.

}
