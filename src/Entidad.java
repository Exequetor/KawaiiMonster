/*
 * Clase abstracta que representa cualquier objeto dentro de la matriz de terreno o la matriz de entidades
 * vivas. Esta clase contiene métodos útiles para cualquier entidad de estas dos matrices, además de forzar
 * a utilizar métodos necesarios
 */
public abstract class Entidad {
	protected int ID_Entidad; //ID que tiene la entidad.
	protected int [] pos = new int [2]; //Posición x, y de la entidad en la matriz.
	protected int sprite; //ID del sprite asociado que será mostrado cuando sea invocado por el renderer
	/*
	 * Constructor de cualquier entidad que recibe el ID de la entidad
	 */
	public Entidad (int id) {
		ID_Entidad = id;
	}
	
	public Entidad (int id, int x, int y) {
		ID_Entidad = id;
		pos[0] = x;
		pos[1] = y;
	}
	
	public Entidad (Entidad entidad) {
		ID_Entidad = entidad.ID_Entidad;
		pos = entidad.pos;
	}
	/*
	 * Método para configurar la posición de la entidad.
	 */
	public void setPos (int x, int y) {
		try {
			pos[0] = x;
			pos[1] = y;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * Método que retorna el arreglo de la posición de la entidad.
	 */
	public int [] getPos () {
		return pos;
	}
	
	/*
	 * Método que retorna la posición en X de la entidad.
	 */
	public int getPosX () {
		return pos[0];
	}
	
	/*
	 * Método que retorna la posición Y de la entidad
	 */
	public int getPosY () {
		return pos[1];
	}
	
	/*
	 * Método para obtener el identificador de la entidad
	 */
	public int getIDEntidad () {
		return ID_Entidad;
	}
	
	/*
	 * Método para obtener el identificador del sprite asociado a la entidad.
	 */
	public int sprite () {
		return sprite;
	}
	
}
