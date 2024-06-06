package spaceGame;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Random;
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
	public int WINB=600, WINH=600;
	public int ms_sleep = 1000/60; //it's 60fps... but not.
	Random randNum = new Random();
	
	public int currentWave = 0;
	public boolean canUseJets = true;
	public int enemyCD = 1500;
	
	public ArrayList<Projectile> projectileCache = new ArrayList<Projectile>();
	public ArrayList<Enemy> enemyCache = new ArrayList<Enemy>();
	public ArrayList<Background> stars = new ArrayList<Background>();
	public ArrayList<Boss> bossCache = new ArrayList<Boss>(); //Who knows if we do more than one boss at once
	public Player playerstats = new Player();
	public Rectangle player = new Rectangle(playerstats.x, playerstats.y, playerstats.size, playerstats.size);
	public Rectangle limit = new Rectangle(0,200, WINB,400);
	
	public GraphicsConsole game = new GraphicsConsole(WINB,WINH);
	public GraphicsConsole startScreen = new GraphicsConsole(WINB, WINH);
	//for even later: GraphicsConsole pauseScreen = new GraphicsConsole(WINB, WINH)
	
	public Timer playerShoots = new Timer(playerstats.firerate, this);
	public Timer enemyShoots = new Timer(enemyCD, this);
	public Timer easyTimer = new Timer(10000, this);
	
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
				enemyShoots.start();
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
		playerFunctions.checkCollision();
		enemyFunctions.move();
		enemyFunctions.move_Projectiles();
		enemyFunctions.delete_Projectiles();
		enemyFunctions.checkCollision();
		
	}
	
	void drawGraphics() {
		synchronized(game) {
			game.setBackgroundColor(new Color(15,10,15));
			game.clear();
			game.setColor(new Color(15,60,15,204));
			game.drawRect(0, 200, WINB, 400);
			game.setColor(new Color(255,255,255));
			game.fillRect(player.x, player.y, playerstats.size, playerstats.size);
			try {
				for(Projectile p: projectileCache) {
					if(p instanceof Player_lineProjectile) game.setColor(new Color(255,255,255));
					else game.setColor(new Color(255,0,0));
					game.fillRect(p.x, p.y, p.size, p.size);
				}
			} catch(ConcurrentModificationException oops) {}
			game.setColor(new Color(255,0,0));
			try {
				for(Enemy e: enemyCache) {
					game.fillRect(e.x, e.y, e.size, e.size);
				}
			} catch(ConcurrentModificationException oops) {}
		}
	}

	@Override
	public void actionPerformed(ActionEvent ev){ //Timer should be used for wave progression?
		if(ev.getSource() == playerShoots) playerFunctions.shoot(player.x+playerstats.size/2, player.y);
		if(ev.getSource() == enemyShoots) {
			for(Enemy e: enemyCache)
				enemyFunctions.shoot(e.x+e.size/2, e.y+e.size/2);
				enemyCache.add(new Liner(randNum.nextInt(0,WINB-16), 0));
		}
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
				player.x = (int) ((playerstats.spd)*Math.cos(Math.toRadians(normalization)) - (0)*Math.sin(Math.toRadians(normalization)) + player.x);
				player.y = (int) ((playerstats.spd)*Math.sin(Math.toRadians(normalization)) + (0)*Math.cos(Math.toRadians(normalization)) + player.y);
			}
			if(player.y<limit.y) player.y=limit.y;
			if(player.x<limit.x) player.x=0;
			if(player.y>limit.x+limit.width-playerstats.size) player.y=WINH-playerstats.size;
			if(player.x>limit.y+limit.height-playerstats.size) player.x=WINB-playerstats.size;
		}
		public void shoot(int x, int y) {
			Player_lineProjectile referenceBullet = new Player_lineProjectile(0,0);
			projectileCache.add(new Player_lineProjectile(x-referenceBullet.size/2, y));
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
		
		public void checkCollision() {
			for(Projectile p: projectileCache) {
				if(p instanceof Enemy_lineProjectile) {
					if(player.contains(p.x,p.y) || player.contains(p.x+p.size,p.y) || player.contains(p.x,p.y+p.size) || player.contains(p.x+p.size,p.y+p.size)) System.out.println("hurt! D:");
				}
			}
		}
	};
	
	GameFunctions enemyFunctions = new GameFunctions() {
		@SuppressWarnings("unused")
		public void spawnLiner(int x, int y) {
			enemyCache.add(new Liner(x, y));
		}

		public void move() {
			for(Enemy i: enemyCache) {
				if(i instanceof Liner) i.y += i.spd;
			}
			for(int i=0; i<enemyCache.size(); i++) {
				if(enemyCache.get(i).y>WINH) enemyCache.remove(i);
			}
		}

		public void move_Projectiles() {
			for(Projectile i: projectileCache) {
				if(i instanceof Enemy_lineProjectile) {
					i.x = (int) ((i.spd)*Math.cos(Math.toRadians(i.rotation)) - (0)*Math.sin(Math.toRadians(i.rotation)) + i.x);
					i.y = (int) ((i.spd)*Math.sin(Math.toRadians(i.rotation)) + (0)*Math.cos(Math.toRadians(i.rotation)) + i.y);
				}
			}
		}

		public void delete_Projectiles() {
			for(int i=0; i<projectileCache.size(); i++) {
				if(projectileCache.get(i).x < 0 || projectileCache.get(i).x>WINB || projectileCache.get(i).y<0 || projectileCache.get(i).y > WINH) projectileCache.remove(i);
			}
		}

		@SuppressWarnings("unused")
		public void shoot(int x, int y) {
			projectileCache.add(new Enemy_lineProjectile(x, y));
		}

		public void checkCollision() {
			for(Enemy enemy: enemyCache) {
				Rectangle e = new Rectangle(enemy.x, enemy.y, enemy.size, enemy.size);
				for(Projectile p: projectileCache) {
					if(p instanceof Player_lineProjectile) {
						if(e.contains(p.x,p.y) || e.contains(p.x+p.size,p.y) || e.contains(p.x,p.y+p.size) || e.contains(p.x+p.size,p.y+p.size)) System.out.println("killed enemy! :D");
					}
				}
			}
		}
		
	};
}
