import java.util.HashMap;
import java.util.Map;
import java.util.Random;
/*
 * Clase que controla todos los recursos críticos como la matriz de terreno y la matriz
 * de Entidades Vivas
 */
public class Critico {
	protected static Entidad[][] world = new Entidad [Cons.WORLD_Y][Cons.WORLD_X]; //Matriz que representa el terreno
	protected static EntidadViva [][] lifeMap = new EntidadViva [Cons.WORLD_Y][Cons.WORLD_X]; //Matriz que representa entidades vivas
	protected static int win = 10; //Monstruos que el jugador debe matar para ganar. 
	protected static Map <Integer, Integer> controlMap = new HashMap <Integer, Integer> (); //Controla el respawn de monstruso 
	
	/*
	 * Inicialización del terreno, integración del jugador a la matriz de entidades vivas y generación del primer
	 * monstruo.
	 */
	public static void inicializarMundo (Jugador jug) {
		//Generación del mundo
		for (int y = 0 ; y < Cons.WORLD_Y ; y++) {
			for (int x = 0 ; x < Cons.WORLD_X ; x++)
				world[y][x] = new Terreno (x, y);
			lifeMap[jug.getPosY()][jug.getPosY()] = jug; //Jugador puesto en la matriz de entidades vivas.
			lifeMap[5][5] = new Slime (5, 5); //Instancia del primer monstruo en la matriz de entidades vivas.
			//Se inicializan los mapas de control para controlar el spawn de los monstruos.
			controlMap.put(Cons.ID_SLIME, 0); 
			controlMap.put(Cons.ID_VIBORA, 0);
		}
	}
	/*
	 * Método crítico que se encarga de las operaciones de la matriz. Es un método sincronizado que debe ejecutarse
	 * de forma atómica. Ejecuta cualquier movimiento encolado de todas las entidades físicas, se encarga de
	 * la generación de daño entre enemigos, planificación de movimientos y comprobación de entidades que
	 * están muertas
	 */
	public synchronized static void executeMov () {
		Random rand = new Random ();
		//Primero se ejecuta el movimiento del jugador
		for (int y = 0 ; y < lifeMap.length ; y++) {
			for (int x = 0 ; x < lifeMap[y].length ; x++)
				if (lifeMap[y][x] != null)
				if (lifeMap[y][x].ID_Entidad == Cons.ID_JUGADOR)
					mov(lifeMap[y][x], x, y);
		}
		
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//Se establece que todas los bots están listos para realizar su movimiento
		for (int y = 0 ; y < lifeMap.length ; y++) {
			for (int x = 0 ; x < lifeMap[y].length ; x++)
				if (lifeMap[y][x] != null)
					lifeMap[y][x].readyToMove = true;
		}
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//Se ejecuta el movimiento de todos los bots
		for (int y = 0 ; y < lifeMap.length ; y++) {
			for (int x = 0 ; x < lifeMap[y].length ; x++)
				if (lifeMap[y][x] != null)
					mov(lifeMap[y][x], x, y);
		}
		//Se intenta generar monstruos aleatorios hasta llegar al máximo de posibles unidades generaas
		//descrito por el mapa de control.
		if (rand.nextInt(6) == 5) {
			for (int y = rand.nextInt(Main.screenSizeY/Cons.TILE_SIZE) ; y < Main.screenSizeY/Cons.TILE_SIZE - 2 ; y++)
				for (int x = rand.nextInt(Main.screenSizeX/Cons.TILE_SIZE) ; x < Main.screenSizeX/Cons.TILE_SIZE - 2 ; x++)
					if (lifeMap[y][x] == null && controlMap.get(Cons.ID_SLIME) < 7) {
						lifeMap[y][x] = new Slime (x, y);
						controlMap.put(Cons.ID_SLIME, controlMap.get(Cons.ID_SLIME) + 1);
						y = Main.screenSizeY/Cons.TILE_SIZE - 2;
						x = Main.screenSizeX/Cons.TILE_SIZE - 2;
					}
		}
		if (rand.nextInt(10) == 5) {
			for (int y = rand.nextInt(Main.screenSizeY/Cons.TILE_SIZE) ; y < Main.screenSizeY/Cons.TILE_SIZE - 2 ; y++)
				for (int x = rand.nextInt(Main.screenSizeX/Cons.TILE_SIZE) ; x < Main.screenSizeX/Cons.TILE_SIZE - 2 ; x++)
					if (lifeMap[y][x] == null && controlMap.get(Cons.ID_VIBORA) < 2) {
						lifeMap[y][x] = new Vibora (x, y);
						controlMap.put(Cons.ID_VIBORA, controlMap.get(Cons.ID_VIBORA) + 1);
						y = Main.screenSizeY/Cons.TILE_SIZE - 2 ;
						x = Main.screenSizeX/Cons.TILE_SIZE - 2;
					}
		}
		//Se verifica si el jugador ha ganado o no al finalizar todos los movimientos y generaciones de enemigos.
		if (win == 0) {
			Main.gameAlive = false;
			Main.win = true;
		}
			
		//System.out.println(win);
	}
	/*
	 * Método que describe cómo es ejecutado un movimiento de una entidad viva. Contiene todos los comportamientos
	 * para un movimiento de cualquier entidad viva.
	 */
	public synchronized static void mov(EntidadViva entidad, int posX, int posY) {
		int offset;
		if (entidad.espacios > 0)
			switch (entidad.movimiento) {
			//Si la entidad se moverá a la izquierda
			case Cons.IZQUIERDA:
				offset = posX - entidad.espacios;
				//Se verifica que no se salga de la matriz
				if (posX >= entidad.espacios)
					//Se verifica que el destino no sea una pared
					if (world[posY][offset].ID_Entidad != Cons.ID_PARED) {
						//Se verifica que no exista ninguna entidad en su paso, de lo contrario decide si atacar
						//o pasar el turno.
						if (lifeMap[posY][offset] == null) {
							lifeMap[posY][posX] = null;
							lifeMap[posY][offset] = entidad;
							entidad.pos[0] = offset;
							entidad.espacios = 0;
						} else {
							//Si la entidad disparadora es el jugador, entonces inflije daño a la entidad viva
							if (entidad.ID_Entidad == Cons.ID_JUGADOR) {
								lifeMap[posY][offset].vida -= entidad.damage;
								if (!lifeMap[posY][offset].isAlive()) {
									win--;
									lifeMap[posY][offset] = null;
								}
							} else
								//Si la entidad disparadora es un bot, no hace daño a otro bot per si al jugador
								if (lifeMap[posY][offset].ID_Entidad == Cons.ID_JUGADOR) {
									lifeMap[posY][offset].vida -= entidad.damage;
									if (!lifeMap[posY][offset].isAlive())
										lifeMap[posY][offset] = null;
								}
							
						}
						entidad.espacios = 0;
					}
				break;
			case Cons.DERECHA:
				offset = posX + entidad.espacios;
					if (offset < Cons.WORLD_X)
						if (world[posY][offset].ID_Entidad != Cons.ID_PARED) {
							if (lifeMap[posY][offset] == null) {
								lifeMap[posY][posX] = null;
								lifeMap[posY][offset] = entidad;
								entidad.pos[0] = offset;
							} else {
								if (entidad.ID_Entidad == Cons.ID_JUGADOR) {
									lifeMap[posY][offset].vida -= entidad.damage;
									if (!lifeMap[posY][offset].isAlive()) {
										win--;
										lifeMap[posY][offset] = null;
									}
								} else
									if (lifeMap[posY][offset].ID_Entidad == Cons.ID_JUGADOR) {
										lifeMap[posY][offset].vida -= entidad.damage;
										if (!lifeMap[posY][offset].isAlive())
											lifeMap[posY][offset] = null;
									}
							}
							entidad.espacios = 0;
						}
				break;
			case Cons.ARRIBA:
				offset = posY - entidad.espacios;
				if (posY >= entidad.espacios)
					if (world[offset][posX].ID_Entidad != Cons.ID_PARED) {
						if (lifeMap[offset][posX] == null) {
							lifeMap[posY][posX] = null;
							lifeMap[offset][posX] = entidad;
							entidad.pos[1] = offset;
						} else {
							if (entidad.ID_Entidad == Cons.ID_JUGADOR) {
								lifeMap[offset][posX].vida -= entidad.damage;
								if (!lifeMap[offset][posX].isAlive()) {
									win--;
									lifeMap[offset][posX] = null;
								}
							} else
								if (lifeMap[offset][posX].ID_Entidad == Cons.ID_JUGADOR) {
									lifeMap[offset][posX].vida -= entidad.damage;
									if (!lifeMap[offset][posX].isAlive())
										lifeMap[offset][posX] = null;
								}
						}
						entidad.espacios = 0;
					}
				break;
			case Cons.ABAJO:
				offset = posY + entidad.espacios;
				if (posX < Cons.WORLD_Y)
					if (world[offset][posX].ID_Entidad != Cons.ID_PARED) {
						if (lifeMap[offset][posX] == null) {
							lifeMap[posY][posX] = null;
							lifeMap[offset][posX] = entidad;
							entidad.pos[1] = offset;
						} else {
							if (entidad.ID_Entidad == Cons.ID_JUGADOR) {
								lifeMap[offset][posX].vida -= entidad.damage;
								if (!lifeMap[offset][posX].isAlive()) {
									win--;
									lifeMap[offset][posX] = null;
								}
							} else
								if (lifeMap[offset][posX].ID_Entidad == Cons.ID_JUGADOR) {
									lifeMap[offset][posX].vida -= entidad.damage;
									if (!lifeMap[offset][posX].isAlive())
										lifeMap[offset][posX] = null;
								}
						}
						entidad.espacios = 0;
					}
				break;
		}
	}
	
}

