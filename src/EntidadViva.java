/*
 * Método que hereda de Entidad y que representa cualquier entidad que pueda moverse, como los monstruos
 * o el jugador. Contiene métodos y atributos útiles para este propósito.
 */
public abstract class EntidadViva extends Entidad {
	
	protected int movimiento;
	protected boolean readyToMove = false;;
	protected int espacios;
	protected int vida; //La cantidad de vida del monstruo
	protected int damage; //El daño que inflije el monstruo al jugador
	protected boolean isAlive;
	
	public EntidadViva (int id, int vida, int damage, int x, int y) {
		super(id, x, y);
		this.vida = vida;
		this.damage = damage;
		if (vida > 0) {
			isAlive = true;
		}
		Thread animation = new Thread (animation());
		animation.start();
	}
	
	/*
	 * Método que precarga el movimiento que realizará en el siguiente turno
	 */
	public void mov (int espacios, int movimiento) {
		this.espacios = espacios;
		this.movimiento = movimiento;
		
	}
	
	/*
	 * Método que espera un turno, útil para los bots
	 */
	public void waitBeat() {
		while (!readyToMove) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		readyToMove = false;
	}
	
	/*
	 * Método que retorna la dirección del siguiente movimiento a realizar en el turno siguiente.
	 */
	public int getMovimiento () {
		return movimiento;
	}
	
	/*
	 * Método que retorna la cantidad de espacios que dará la entidad en el siguiente turno.
	 */
	public int getEspacios () {
		return espacios;
	}
	
	/*
	 * Método que calcula si la entidad está viva o muerta.
	 */
	public boolean isAlive() {
		if (vida > 0) {
			return true;
		}
		return false;
	}
	
	/*
	 * Retorna el contador de vida de la entidad
	 */
	public int getVida () {
		return vida;
	}
	
	/*
	 * Hilo que se ocupa para la animación de sprites de una entidad
	 */
	public abstract Runnable animation ();
	
}
