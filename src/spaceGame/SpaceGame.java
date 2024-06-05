package spaceGame;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import hsa2.GraphicsConsole;

public class SpaceGame implements ActionListener {
	Font menuFont = new Font("Tahoma", Font.BOLD, 32);
	int WINB=600, WINH=600;
	int ms_sleep = 1000/60; //it's 60fps... but not.
	
	int currentWave = 0;
	boolean canUseJets = true;
	
	ArrayList<Projectile> projectileCache = new ArrayList<Projectile>();
	ArrayList<Enemy> enemyCache = new ArrayList<Enemy>();
	ArrayList<Background> stars = new ArrayList<Background>();
	ArrayList<Boss> bossCache = new ArrayList<Boss>(); //Who knows if we do more than one boss at once
	Player player = new Player();
	Rectangle limit = new Rectangle(0,200, WINB,400);
	
	GraphicsConsole game = new GraphicsConsole(WINB,WINH);
	GraphicsConsole startScreen = new GraphicsConsole(WINB, WINH);
	//for even later: GraphicsConsole pauseScreen = new GraphicsConsole(WINB, WINH)
	
	Timer playerShoots = new Timer(player.firerate, this);
	Timer easyTimer = new Timer(10000, this);
	
	public static void main(String[] args) {
		new SpaceGame();
	}
	
	SpaceGame() {
		setup_game();
		func_startScreen();
		while(true) {
			updScreen();
			drawGraphics();
			game.sleep(ms_sleep);
			startScreen.sleep(ms_sleep);
		}
	}
	
	void setup_game() {
		game.setVisible(false);
		game.setBackgroundColor(new Color(15,10,15));
		game.clear();
	}
	
	void func_startScreen() {
		Rectangle easyButton = new Rectangle(100,400,400,100);
		boolean mouseTrigger = false;
		
		startScreen.setVisible(true);
		startScreen.enableMouse();
		startScreen.enableMouseMotion();
		startScreen.setBackgroundColor(new Color(15,10,15));
		startScreen.clear();
		startScreen.setColor(new Color(255, 255, 255));
		startScreen.fillRect(easyButton.x, easyButton.y, easyButton.width, easyButton.height);
		while(true) {
			startScreen.sleep(ms_sleep);
			if(easyButton.contains(startScreen.getMouseX(), startScreen.getMouseY()) && startScreen.getMouseClick() > 0 && mouseTrigger == false) {
				game.setVisible(true);
				startScreen.setVisible(false);
				playerShoots.start();
				easyTimer.start();
				break;
			}
			if(startScreen.getMouseClick() > 0) mouseTrigger = true;
			else mouseTrigger = false;
		}
	}
	
	void updScreen() {
		playerFunctions.move();
		playerFunctions.move_Projectiles();
		playerFunctions.delete_Projectiles();
		
	}
	
	void drawGraphics() {
		synchronized(game) {
			game.setBackgroundColor(new Color(15,10,15));
			game.clear();
			game.setColor(new Color(15,60,15,204));
			game.drawRect(0, 200, WINB, 400);
			game.setColor(new Color(255,255,255));
			game.fillRect(player.x, player.y, player.size, player.size);
			try {
				for(Projectile p: projectileCache) {
					if(p instanceof Player_lineProjectile) game.setColor(new Color(255,255,255));
					else game.setColor(new Color(255,0,0));
					game.fillRect(p.x, p.y, p.size, p.size);
				}
			} catch(ConcurrentModificationException oops) {}
		}
	}

	@Override
	public void actionPerformed(ActionEvent ev){ //Timer should be used for wave progression?
		if(ev.getSource() == playerShoots) playerFunctions.shoot();
	}
	
	Waves[] easyWaves = {
		new Waves() {public void wave() { //Wave 1
			//these print statements are jokes. Put what each wave does here, delay spawns with Thread.sleep(int ms);
			System.out.println("aaaaaa aliens"); 
			}},
		new Waves() {public void wave() { //Wave 2
			System.out.println("eeeeeee more");
			}},
		new Waves() {public void wave() { //Wave 3
			System.out.println("stap it");
			}},
		new Waves() {public void wave() { //Wave 4
			System.out.println("why u bulli me?");
			}},
		new Waves() {public void wave() { //Boss Wave
			System.out.println("ooo scary boss");
		}}
		
	};
	
	GameFunctions playerFunctions = new GameFunctions() {
		public void move() {
			int normalization = 0;
			if(game.isKeyDown(KeyEvent.VK_W) || game.isKeyDown(KeyEvent.VK_UP)) {
				normalization = -90;
			}
			if(game.isKeyDown(KeyEvent.VK_A) || game.isKeyDown(KeyEvent.VK_LEFT)) {
				normalization = 180;
			}
			if(game.isKeyDown(KeyEvent.VK_S) || game.isKeyDown(KeyEvent.VK_DOWN)) {
				normalization = 90;
			}
			if(game.isKeyDown(KeyEvent.VK_D) || game.isKeyDown(KeyEvent.VK_RIGHT)) {
				normalization = 0;
			}
			if((game.isKeyDown(KeyEvent.VK_W) || game.isKeyDown(KeyEvent.VK_UP)) && (game.isKeyDown(KeyEvent.VK_A) || game.isKeyDown(KeyEvent.VK_LEFT))) {
				normalization = -135;
			}
			if((game.isKeyDown(KeyEvent.VK_W) || game.isKeyDown(KeyEvent.VK_UP)) && (game.isKeyDown(KeyEvent.VK_D) || game.isKeyDown(KeyEvent.VK_RIGHT))) {
				normalization = -45;
			}
			if((game.isKeyDown(KeyEvent.VK_S) || game.isKeyDown(KeyEvent.VK_DOWN)) && (game.isKeyDown(KeyEvent.VK_A) || game.isKeyDown(KeyEvent.VK_LEFT))) {
				normalization = 135;
			}
			if((game.isKeyDown(KeyEvent.VK_S) || game.isKeyDown(KeyEvent.VK_DOWN)) && (game.isKeyDown(KeyEvent.VK_D) || game.isKeyDown(KeyEvent.VK_RIGHT))) {
				normalization = 45;
			}
			if(game.isKeyDown(KeyEvent.VK_S) || game.isKeyDown(KeyEvent.VK_A) || game.isKeyDown(KeyEvent.VK_D) || game.isKeyDown(KeyEvent.VK_W) || (game.isKeyDown(KeyEvent.VK_DOWN) || game.isKeyDown(KeyEvent.VK_LEFT) || game.isKeyDown(KeyEvent.VK_RIGHT) || game.isKeyDown(KeyEvent.VK_UP))) {
				player.x = (int) ((player.spd)*Math.cos(Math.toRadians(normalization)) - (0)*Math.sin(Math.toRadians(normalization)) + player.x);
				player.y = (int) ((player.spd)*Math.sin(Math.toRadians(normalization)) + (0)*Math.cos(Math.toRadians(normalization)) + player.y);
			}
			if(player.y<limit.y) player.y=limit.y;
			if(player.x<limit.x) player.x=0;
			if(player.y>limit.x+limit.width-player.size) player.y=WINH-player.size;
			if(player.x>limit.y+limit.height-player.size) player.x=WINB-player.size;
		}
		public void shoot() {
			Player_lineProjectile referenceBullet = new Player_lineProjectile(0,0);
			projectileCache.add(new Player_lineProjectile(player.x+player.size/2-referenceBullet.size/2, player.y));
		}
		public void move_Projectiles() {
			for(Projectile i: projectileCache) {
				if(i instanceof Player_lineProjectile) {
					i.y -= i.spd;
				}
			}
		}
		public void delete_Projectiles() {
			for(int i=0; i<projectileCache.size(); i++) {
				if(projectileCache.get(i) instanceof Player_lineProjectile && projectileCache.get(i).y<0) {
					projectileCache.remove(i);
				}
			}
		}
	};
}
