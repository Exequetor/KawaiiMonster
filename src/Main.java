
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.Canvas;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class Main extends JFrame implements Runnable, KeyListener {
	
	public static boolean gameAlive = true; //Bandera que indica si el juego sigue corriendo o si ya terminó.
	public static boolean win = false; //Bandera que indica si se ganó el juego o se perdió.
	
	private Canvas canvas = new Canvas (); //Canvas del JFrame.
	private RenderBuffer renderer; //Buffer para los gráficos que se muestran en pantalla.
	public static Jugador jugador = new Jugador (3,1,1); //Objeto jugador que será representado por el personaje principal
	protected static int screenSizeX; //Tamaño real en X del JFrame para que se puedan meter Tiles exactos de sprites de 64x64.
	protected static int screenSizeY; //Tamaño real en Y del JFrame para que se puedan meter Tiles exactos de sprites de 64x64.
	
	/*
	 * Constructor principal del juego, crea la instancia del JFrame principal donde se desarrollará todo el juego.
	 */
	public Main () {
		//Se obtiene el tamaño real del JFrame para que quepan Tiles de sprites exactos.
		for (int i = 0 ; i < 1000 ; i+= Cons.TILE_SIZE)
			screenSizeX = i;
		for (int j = 0 ; j < 720 ; j+= Cons.TILE_SIZE)
			screenSizeY = j;
		setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		setTitle ("Kawaii Monsters");
		setBounds (0, 0, screenSizeX, screenSizeY);
		setLocationRelativeTo (null);
		//JLabel que indica que los sprites están siendo cargados a memoria para su rápido
		//acceso al buffer de sprites.
		JLabel label = new JLabel ("Cargando sprites...");
		label.setFont(label.getFont().deriveFont(30.0f));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		add(label);
		addKeyListener(this);
		setVisible (true);
		SpriteBank.loadSprites(); //Método que carga los sprites a memoria desde disco duro.
		add(canvas);
		label.setVisible(false); //La etiqueta se vuelve invisible al finalizar la carga de sprites
		canvas.createBufferStrategy(3); //Se crea el bufferStrategy para RGB sin canal Alpha.
		renderer = new RenderBuffer (getWidth(), getHeight());  //Se crea el buffer de pixeles del tamaño del JFrame.
		
	}
	
	/*
	 * Método que despliega en pantalla lo que hay en el buffer del canvas.
	 */
	public void render () {
		BufferStrategy bufferStrategy = canvas.getBufferStrategy();
		Graphics g = bufferStrategy.getDrawGraphics ();
		super.paint(g);

		renderer.render(g);
		
		g.dispose();
		bufferStrategy.show();
	}

	
	/*
	 * Método que actualiza el buffer del canvas antes de ser desplegado en pantalla
	 */
	public void update () {
		//Se despliega primero la matriz del terreno
		for (int y = 0; y < screenSizeY/Cons.TILE_SIZE ; y++)
			for (int x = 0 ; x < screenSizeX/Cons.TILE_SIZE ; x++) {
				renderer.renderImage(SpriteBank.bank.get(Critico.world[y][x].sprite()), (x) * Cons.TILE_SIZE, (y) * Cons.TILE_SIZE);
				if (Critico.lifeMap[y][x] != null)
					renderer.renderImage(SpriteBank.bank.get(Critico.lifeMap[y][x].sprite()), (x) * Cons.TILE_SIZE, (y) * Cons.TILE_SIZE);
			}
		//Se despliega la matriz de entidades vivas como los monstruos y el ugador
		renderer.renderImage(SpriteBank.bank.get(SpriteBank.ID_TEXT_LIFE), 0, 10);
		for (int i = 0 ; i < jugador.getVida() ;i++) {
			renderer.renderImage(SpriteBank.bank.get(SpriteBank.ID_HEART), i * Cons.TILE_SIZE/2 + SpriteBank.bank.get(SpriteBank.ID_TEXT_LIFE).getWidth(), 10);
		}
		//Si ya se cumplió una condición para finalizar el juego, se despliega un mensaje de derrota o victoria
		//a través del buffer.
		if (!gameAlive) {
			if (!win) 
				renderer.renderImage(SpriteBank.bank.get(SpriteBank.ID_GAME_OVER), getWidth()/2 - SpriteBank.bank.get(SpriteBank.ID_GAME_OVER).getWidth()/2, getHeight()/2 - SpriteBank.bank.get(SpriteBank.ID_GAME_OVER).getHeight()/2);
			else {
				renderer.renderImage(SpriteBank.bank.get(SpriteBank.ID_WIN), getWidth()/2 - SpriteBank.bank.get(SpriteBank.ID_GAME_OVER).getWidth()/2, getHeight()/2 - SpriteBank.bank.get(SpriteBank.ID_GAME_OVER).getHeight()/2);
				//System.out.println("Win");
			}
		
		}
	}
	
	@Override
	/*
	 * Método principal para el hilo principal del juego, se encarga de inicializar
	 *  otros hilos principales del juego y de refrescar el canvas.
	 * a 60 cuadros por segundo.
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		//Se utiliza el tiempo utilizado por el sistema para que no haya desincronización
		//por bloqueo del procesador.
		long lastTime = System.nanoTime();
		double nanoConversion = 1000000000.0 / 60; //60 frames por segundo
		double changeInSeconds = 0;
		//Hilo que se encarga de darle un delay a los turnos del jugador.
		Thread beatMechanism = new Thread (new BeatMechanism ());
		beatMechanism.start();
		//Se inicializa el terreno y la matriz de entidades vivas.
		Critico.inicializarMundo(jugador);
		//Loop principal del juego que se encarga del refrescado de pantalla.
		while (true) {
			long now = System.nanoTime();
			changeInSeconds += (now - lastTime) / nanoConversion;
			while (changeInSeconds >= 1) {
				update ();
				changeInSeconds = 0;
			}
			render ();
			lastTime = now;
		}
	
	}
	/*
	 * Método main llamado por la JVM. Se deslinda del método estático llamando un constructor
	 * de la misma clase. 
	 */
	public static void main(String[] args) {
		Main game = new Main ();
		Thread gameThread = new Thread (game);
		gameThread.start();
		
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	}
	
	/*
	 * KeyListener para que el jugador pueda moverse por el mundo. Forma parte de la sincronización del turno global.
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent e) {
			//En una situación normal, las teclas WASD hacen que el jugador se mueva.
			if (BeatMechanism.beat && Main.gameAlive) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_W: jugador.mov(1, Cons.ARRIBA); break;
				case KeyEvent.VK_A: jugador.mov(1, Cons.IZQUIERDA); break;
				case KeyEvent.VK_S: jugador.mov(1, Cons.ABAJO); break;
				case KeyEvent.VK_D: jugador.mov(1, Cons.DERECHA); break;
				}
				BeatMechanism.beat = false;
				Critico.executeMov();
			}
			//Cuando el juego ha finalizado el jugador puede salirse con la tecla Enter.
			if (e.getKeyCode() == KeyEvent.VK_ENTER && !gameAlive) {
				System.exit(0);
			}
			//El juego no termina si el jugador ganó y sigue vivo.
			if (!win)
				gameAlive = jugador.isAlive();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}
}
