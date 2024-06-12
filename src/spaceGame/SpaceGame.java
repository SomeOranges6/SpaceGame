package spaceGame;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Random;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import hsa2.GraphicsConsole;

public class SpaceGame implements ActionListener {
	//Initializing many, many, variables.
	Font normalFont = new Font("Tahoma", Font.PLAIN, 16);
	Font bigFont = new Font("Tahoma", Font.BOLD, 64);
	public int WINB=600, WINH=600;
	public int ms_sleep = 1000/60; //it's 60fps... but not.
	Random randNum = new Random();
	
	public int currentWave = 0, difficulty = 0, kills=0;
	//P.S. I tried to get a timeSurvived variable going. It was never accurate.
	public boolean canUseJets = true;
	public int enemyCD = 2503, starCD = 50;
	public int weaponState = 0;
	
	public ArrayList<Projectile> projectileCache = new ArrayList<Projectile>();
	public ArrayList<Enemy> enemyCache = new ArrayList<Enemy>();
	public ArrayList<Boss> bossCache = new ArrayList<Boss>(); //Who knows if we do more than one boss at once
	public Player playerstats = new Player();
	public Rectangle player = new Rectangle(playerstats.x, playerstats.y, playerstats.size, playerstats.size);
	public Rectangle limit = new Rectangle(0,200, WINB,400);
	public Rectangle powerup = new Rectangle();
	public ArrayList<Powerups> powerCache = new ArrayList<Powerups>();
	
	public GraphicsConsole game = new GraphicsConsole(WINB,WINH);
	public GraphicsConsole startScreen = new GraphicsConsole(WINB, WINH);
	public GraphicsConsole gameOver = new GraphicsConsole(WINB, WINH);
	//for even later: GraphicsConsole pauseScreen = new GraphicsConsole(WINB, WINH)
	
	public Timer playerShoots = new Timer(playerstats.firerate, this);
	public Timer enemySpawn = new Timer(enemyCD, this);
	public Timer enemyShoots = new Timer(ms_sleep, this); public int enemyShotAccumulator = 0;
	public Timer easyTimer = new Timer(10000, this); //for rounds lasting too long maybe

	public ArrayList<Integer[]> stars = new ArrayList<>();
	
	boolean loaded = false;
	Clip heavensHellSentGift = null;
	Clip pestOfTheCosmos = null;
	Clip interstellarStomper = null;
	BufferedImage spaceship = null;
	BufferedImage damagedSpaceship = null;
	BufferedImage redEnemy = null;
	BufferedImage blueEnemy = null;
	BufferedImage greenEnemy = null;
	BufferedImage yellowEnemy = null;
	void loadSoundtrackAndImages() {
		try {
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("Terraria Calamity Mod Music - Heaven's Hell-Sent Gift - Theme of The Astral Meteor.wav"));
			heavensHellSentGift = AudioSystem.getClip();
			heavensHellSentGift.open(inputStream);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		try {
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("Terraria Calamity Mod Music - Pest of The Cosmos - Theme of Astrum Deus.wav"));
			pestOfTheCosmos = AudioSystem.getClip();
			pestOfTheCosmos.open(inputStream);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		try {
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("Terraria Calamity Mod Music - Interstellar Stomper  Theme of Astrum Aureus.wav"));
			interstellarStomper = AudioSystem.getClip();
			interstellarStomper.open(inputStream);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		try {
			spaceship = ImageIO.read(new File("spaceship.png").getAbsoluteFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			damagedSpaceship = ImageIO.read(new File("damaged spaceship.png").getAbsoluteFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			redEnemy = ImageIO.read(new File("red enemy.png").getAbsoluteFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			blueEnemy = ImageIO.read(new File("blue enemy.png").getAbsoluteFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			greenEnemy = ImageIO.read(new File("green enemy.png").getAbsoluteFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			yellowEnemy = ImageIO.read(new File("yellow enemy.png").getAbsoluteFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new SpaceGame();
	}
	
	SpaceGame() {
		if(loaded == false) {
			loadSoundtrackAndImages();
			loaded = true;
		}	
		setup_game();
		func_startScreen();
		while(true) {
			updScreen();
			drawGraphics();
			game.sleep(ms_sleep);
			startScreen.sleep(ms_sleep);
		}
	}
	
	/*
	 * The method that draws the gameplay initially
	*/
	void setup_game() {
		game.setVisible(false);
		gameOver.setVisible(false);
		game.setBackgroundColor(new Color(15,10,15));
		game.setLocationRelativeTo(null);
		game.clear();
		game.setFont(normalFont);
	}
	
	/*
	 * This method is the whole function of what getting a game over does
	 * It shows a game over screen then creates a new instance of SpaceGame()
	*/
	void func_gameOver() {
		game.dispose();
		startScreen.dispose();
		heavensHellSentGift.stop(); heavensHellSentGift.setMicrosecondPosition(0);
		pestOfTheCosmos.stop(); pestOfTheCosmos.setMicrosecondPosition(0);
		interstellarStomper.stop();	pestOfTheCosmos.setMicrosecondPosition(0);
		playerShoots.restart(); playerShoots.stop(); 
		enemySpawn.restart(); enemySpawn.stop(); 
		enemyShoots.restart(); enemyShoots.stop(); 
		easyTimer.restart(); easyTimer.stop(); 
		gameOver.setFont(bigFont);
		gameOver.setLocationRelativeTo(null);
		gameOver.setBackgroundColor(new Color(0,0,0));
		gameOver.clear();
		createSound("Touhou Death Sound.wav");
		gameOver.setColor(new Color(255,255,255));
		gameOver.setVisible(true);
		gameOver.drawString("GAME OVER", WINB/6, WINH/2);
		gameOver.setFont(normalFont);
		gameOver.drawString(String.format("KILLS: %d", kills), WINB/6, WINH/2+75);
		gameOver.sleep(3000);
		kills = 0;
		gameOver.dispose();
		new SpaceGame();
	}
	
	/*
	 * This method is the whole function of the starting menu
	*/
	void func_startScreen() {
		Rectangle easyButton = new Rectangle(100,400,400,100);
		boolean mouseTrigger = false;
		
		//setup start screen
		startScreen.setVisible(true);
		startScreen.enableMouse();
		startScreen.enableMouseMotion();
		startScreen.setFont(bigFont);
		startScreen.setBackgroundColor(new Color(15,10,15));
		startScreen.setLocationRelativeTo(null);
		startScreen.clear();
		startScreen.setColor(new Color(255, 255, 255));
		startScreen.fillRect(easyButton.x, easyButton.y, easyButton.width, easyButton.height);
		startScreen.drawString("SPACE GAME", WINB/6, WINH/6);
		startScreen.setColor(new Color(0,255,0));
		startScreen.drawRect(easyButton.x, easyButton.y, easyButton.width, easyButton.height);
		startScreen.drawString("EASY", easyButton.x+120, easyButton.y+75);
		startScreen.setColor(new Color(255, 255, 255));
		startScreen.setFont(normalFont);
		startScreen.drawString("Shoot as many aliens as possible!", WINB/6, WINH/2-90);
		startScreen.drawString("Dodge enemy bullets. Don't let your HP drop to 0!", WINB/6, WINH/2-60);
		startScreen.drawString("WASD or Arrow Keys to move,", WINB/6, WINH/2);
		startScreen.drawString("Hold SPACE to speed up with jets!", WINB/6, WINH/2+30);
		
		//start screen loop begins
		while(true) {
			startScreen.sleep(ms_sleep);
			//If easy mode button is pressed, it executes a few things
			if(easyButton.contains(startScreen.getMouseX(), startScreen.getMouseY()) && startScreen.getMouseClick() > 0 && mouseTrigger == false) {
				game.setVisible(true);
				startScreen.setVisible(false);
				playerstats.hp *= 3;
				playerstats.maxhp *= 3;
				playerstats.iFrames *= 2;
				//playerstats.hp = 1;
				enemyCD *= 1.5;
				playerShoots.start();
				enemySpawn.start();
				enemyShoots.start();
				starSpawn.start();
				easyTimer.start();
				difficulty = 1;
				heavensHellSentGift.loop(Clip.LOOP_CONTINUOUSLY);
				break;
			}
			if(startScreen.getMouseClick() > 0) mouseTrigger = true;
			else mouseTrigger = false;
		}
	}
	
	/*
	 * This method will update positions and run checks
	*/
	void updScreen() {
		deletePower();
		movePowerup();
		checkpUPColl();
		playerFunctions.move();
		playerFunctions.move_Projectiles();
		playerFunctions.delete_Projectiles();
		playerFunctions.checkCollision();
		playerFunctions.checkDeath();
		enemyFunctions.move();
		enemyFunctions.move_Projectiles();
		enemyFunctions.delete_Projectiles();
		enemyFunctions.checkCollision();
		enemyFunctions.checkDeath();
	}
	
	/*
	 * This method will draw the game
	*/
	void drawGraphics() {
		synchronized(game) {
			
			game.setBackgroundColor(new Color(15,10,15));
			game.clear();
			//draw movement limit
			game.setColor(new Color(15,60,15,203));
			game.drawRect(0, 200, WINB, 400); 
			
			//drawing stars
			try {
				for(Background b: stars) {
					game.setColor(new Color(b.R,b.G,b.B, 25));
					game.drawOval(b.x, b.y, b.size, b.size);
				}
			} catch(ConcurrentModificationException oops) {}
			
			//drawing player
			if(playerstats.active_iFrames>0) {
				game.setColor(new Color(102,255,102, 51));
				game.drawImage(damagedSpaceship, player.x-playerstats.size/2, player.y-playerstats.size/2, playerstats.size*2, playerstats.size*2);
			}
			else {
				game.setColor(new Color(102,255,102, 102));
				game.drawImage(spaceship, player.x-playerstats.size/2, player.y-playerstats.size/2, playerstats.size*2, playerstats.size*2);
			}
			game.fillOval(player.x, player.y, playerstats.size, playerstats.size);
			
			
			//drawing projectiles
			try {
				for(Projectile p: projectileCache) {
					if(p instanceof Player_lineProjectile) game.setColor(new Color(255,255,255));
					else game.setColor(new Color(255,0,0));
					game.fillRect(p.x, p.y, p.size, p.size);
				}
			} catch(ConcurrentModificationException oops) {}
			
			//drawing enemies
			game.setColor(new Color(255,0,0));
			try {
				for(Enemy e: enemyCache) {
					if(e instanceof Liner || e instanceof Rotater) game.drawImage(redEnemy, e.x, e.y, e.size, e.size);
					else if (e instanceof Tanker) game.drawImage(blueEnemy, e.x, e.y, e.size, e.size);
				}
			} catch(ConcurrentModificationException oops) {}
			
			//draw powerups
			try {
				for (Powerups p: powerCache) {
					if(p.diameter == 5) game.setColor(new Color(200, 200, 100));
					else if(p.diameter == 7) game.setColor(new Color(102,255,204));
					else game.setColor(new Color(20, 200, 20));
					game.fillOval(p.x,p.y,p.diameter,p.diameter);
				}
			} catch(ConcurrentModificationException oops) {}
			
			
			//drawing text and bars
			if(playerstats.hp > 3)game.setColor(new Color(102,255,204, 134));
			else game.setColor(new Color(255,51,102, 134));
			game.drawString(String.format("HP: %d", playerstats.hp), 50, 547);
			game.fillRect(50, 550, 500/450*500-((playerstats.maxhp-playerstats.hp)*500/450*500/playerstats.maxhp), 10);
			if(canUseJets) game.setColor(new Color(102,255,204, 134));
			else game.setColor(new Color(255,51,102, 134));
			game.drawString(String.format("JET: %d", playerstats.jetFuel), 50, 577);
			//game.drawString(String.format("WEAPON: %d", weaponState), WINB-135, 547); (I don't like it lol)
			game.fillRect(50, 580, playerstats.jetFuel, 10);
			
		}
	}
	
	/*
	 * This method moves the stars that spawn
	*/
	boolean initial = true;
	public void drawBackground() {
		if(initial) {
			for(int i = 0; i < 300 ; i++) {
				stars.add( new Integer[]{rand.nextInt(10, 590),rand.nextInt(10, 590)});
		    }
			initial = false;
		}
		
		for(Integer[] coord : stars) {
			int c = rand.nextInt(56, 256);
			int b = 246;
			gcGame.setColor(new Color(c, c, b, 20));
			gcGame.fillPolygon(GeneralUtil.fourPointStar(coord[0],coord[1], 2));
		
			coord[1]--;
			gcGame.setColor(new Color(c - 10, c - 10, b - 10, 60));
			gcGame.fillPolygon(GeneralUtil.fourPointStar(coord[0], coord[1], 1));
		}
		
		stars.add(new Integer[]{rand.nextInt(10, 590),gcGame.getHeight() - 30});
		if(stars.get(0)[1] > 600)
			stars.remove(0);
		
   }

	/*
	 * Timer method where Timers are utilized
	*/
	@Override
	public void actionPerformed(ActionEvent ev){ //Timer should be used for wave progression?
		//player shoots based on a timer
		if(ev.getSource() == playerShoots) {
			playerFunctions.shoot(player.x+playerstats.size/2, player.y+playerstats.size/2);
		}
		
		//infinite enemies spawn based on a timer
		if(ev.getSource() == enemySpawn) {
				/*int chance = randNum.nextInt(1,7+1);
				if(chance==7) enemyCache.add(new Tanker(randNum.nextInt(0,WINB-20), 0));
				else if(chance>5) {
					int randomPointX = randNum.nextInt(50,WINB-68);
					enemyCache.add(new Rotater(randomPointX, -50, 50, 0));
					enemyCache.add(new Rotater(randomPointX, -50, 50, 90));
					enemyCache.add(new Rotater(randomPointX, -50, 50, 180));
					enemyCache.add(new Rotater(randomPointX, -50, 50, 270));
				}
				else {
					enemyCache.add(new Liner(randNum.nextInt(0,WINB-16), 0));
					if (chance<3)enemyCache.get(enemyCache.size()-1).rotation = 90;
				}*/
				easyWaves[0].wave();
		}
		
		//keeps track of when enemies should fire. It is coded so they all don't fire at the same time
		if(ev.getSource() == enemyShoots) {
			enemyShotAccumulator += ms_sleep;
			try {
				for(Enemy e: enemyCache) {
					if(e instanceof Liner || e instanceof Rotater) {
						e.untilFire += ms_sleep;
						if(e.untilFire>e.firerate) e.untilFire=0;
						int bufferCheck = e.untilFire % e.firerate;
						if(bufferCheck == e.fireBuffer) {
							enemyFunctions.shoot(e.x+e.size/2, e.y+e.size/2);
						}
					}
				}
			} catch(ConcurrentModificationException oops) {}
		}
		
		//spawn and move stars in the background
		if(ev.getSource() == starSpawn) {
			stars.add(new Background());
			moveStars();
		}
	}
	
	shootType[] playerWeapons = {
		new shootType() { public void shoot(int x, int y) {
				playerstats.firerate = 500;
				playerShoots.stop();
				playerShoots.setInitialDelay(playerstats.firerate);
				playerShoots.restart();
				Player_lineProjectile projRect = new Player_lineProjectile(0,0);
				projectileCache.add(new Player_lineProjectile(x-projRect.size/2, y));
				createSound("player laser shoot.wav");
			}
		},
		new shootType() { public void shoot(int x, int y) {
				playerstats.firerate = 450;
				playerShoots.stop();
				playerShoots.setInitialDelay(playerstats.firerate);
				playerShoots.restart();
				Player_lineProjectile projRect = new Player_lineProjectile(0,0);
				projectileCache.add(new Player_lineProjectile(x-projRect.size/2-playerstats.size/2, y));
				projectileCache.add(new Player_lineProjectile(x-projRect.size/2+playerstats.size/2, y));
				createSound("player laser shoot.wav");
				createSound("player laser shoot.wav");
			}
		},
		new shootType() { public void shoot(int x, int y) {
				playerstats.firerate = 1050;
				playerShoots.stop();
				playerShoots.setInitialDelay(playerstats.firerate);
				playerShoots.restart();
				Player_lineProjectile projRect = new Player_lineProjectile(0,0);
				projectileCache.add(new Player_lineProjectile(x-projRect.size/2, y, 45));
				projectileCache.add(new Player_lineProjectile(x-projRect.size/2, y, 67));
				projectileCache.add(new Player_lineProjectile(x-projRect.size/2, y, 90));
				projectileCache.add(new Player_lineProjectile(x-projRect.size/2, y, 113));
				projectileCache.add(new Player_lineProjectile(x-projRect.size/2, y, 135));
				createSound("player laser shoot.wav");
				createSound("player laser shoot.wav");
				createSound("player laser shoot.wav");
			}
		},
		new shootType() { public void shoot(int x, int y) {
				playerstats.firerate = 220;
				playerShoots.stop();
				playerShoots.setInitialDelay(playerstats.firerate);
				playerShoots.restart();
				Player_lineProjectile projRect = new Player_lineProjectile(0,0);
				projectileCache.add(new Player_lineProjectile(x-projRect.size/2, y));
				createSound("player laser shoot.wav");
			}
		},
	};
	
	/*
	 * An array of methods that contain the actions for the easy difficulty waves
	*/
	Waves[] easyWaves = {
		new Waves() {public void wave() { //Wave 1
			for(int i=0; i<5; i++) {
				enemyCache.add(new Liner(75+i*100, 0, 90));
			}
			for(int i=0; i<5; i++) {
				enemyCache.add(new Liner(75+i*100, -60, 90));
				enemyCache.get(enemyCache.size()-1).fireBuffer = 500;
			}
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
	
	/*
	 * Interface that contains methods for player movement
	*/
	GameFunctions playerFunctions = new GameFunctions() {
		/*
		 * Moves the player and runs player position checks and jet fuel check
		*/
		public void move() {
			//normalize player movement
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
			
			//Move the player
			if(game.isKeyDown(KeyEvent.VK_S) || game.isKeyDown(KeyEvent.VK_A) || game.isKeyDown(KeyEvent.VK_D) || game.isKeyDown(KeyEvent.VK_W) || (game.isKeyDown(KeyEvent.VK_DOWN) || game.isKeyDown(KeyEvent.VK_LEFT) || game.isKeyDown(KeyEvent.VK_RIGHT) || game.isKeyDown(KeyEvent.VK_UP))) {
				if(game.isKeyDown(KeyEvent.VK_SPACE) && canUseJets) {
					playerstats.jetFuel -= playerstats.fuelDrain;
					player.x = (int) ((playerstats.fasterSpd)*Math.cos(Math.toRadians(normalization))+ player.x);
					player.y = (int) ((playerstats.fasterSpd)*Math.sin(Math.toRadians(normalization))+ player.y);
				}
				else {
					player.x = (int) ((playerstats.spd)*Math.cos(Math.toRadians(normalization))+ player.x);
					player.y = (int) ((playerstats.spd)*Math.sin(Math.toRadians(normalization))+ player.y);
				}
			}
			
			//Run jet fuel and position checks
			if(playerstats.jetFuel<=playerstats.fuelDrain-1) canUseJets = false;
			if(playerstats.jetFuel>=200) canUseJets = true;
			if(playerstats.jetFuel>500) playerstats.jetFuel = 500;
			if(playerstats.jetFuel<500 && !game.isKeyDown(KeyEvent.VK_SPACE)) playerstats.jetFuel +=1;
			if(player.y<limit.y) player.y=limit.y;
			if(player.x<limit.x) player.x=0;
			if(player.y>limit.x+limit.width-playerstats.size) player.y=WINH-playerstats.size;
			if(player.x>limit.y+limit.height-playerstats.size) player.x=WINB-playerstats.size;
		}
		
		/*
		 * Fires projectiles from the player based on weapon
		*/
		public void shoot(int x, int y) {
			playerWeapons[weaponState].shoot(x, y);
		}
		
		/*
		 * Moves all player projectiles
		*/
		public void move_Projectiles() {
			for(Projectile i: projectileCache) {
				if(i instanceof Player_lineProjectile) {
					i.x = (int) ((i.spd)*Math.cos(Math.toRadians(i.rotation))+ i.x);
					i.y = (int) ((i.spd)*Math.sin(Math.toRadians(i.rotation))+ i.y);
				}
			}
		}
		
		/*
		 * Delete projectiles based on their positions
		*/
		public void delete_Projectiles() {
			for(int i=0; i<projectileCache.size(); i++) {
				if(projectileCache.get(i) instanceof Player_lineProjectile && projectileCache.get(i).y<0) {
					projectileCache.remove(i);
				}
			}
		}
		
		/*
		 * Check if the player collides with an enemy bullet, deleting enemy bullet and changing player's stats
		*/
		public void checkCollision() {
			if(playerstats.hp > playerstats.maxhp) playerstats.hp = playerstats.maxhp; 
			playerstats.active_iFrames -= ms_sleep;
			if(playerstats.active_iFrames<=0) {
				for(int p=0; p<projectileCache.size(); p++) {
					if(projectileCache.get(p) instanceof Enemy_lineProjectile) {
						Rectangle projRect = new Rectangle(projectileCache.get(p).x, projectileCache.get(p).y, projectileCache.get(p).size, projectileCache.get(p).size);
						if(player.contains(projRect.x, projRect.y) || player.contains(projRect.x+projRect.width, projRect.y) || player.contains(projRect.x, projRect.y+projRect.height) || player.contains(projRect.x+projRect.width, projRect.y+projRect.height)) {
							projectileCache.remove(p);
							playerstats.hp -= 1;
							if(playerstats.hp > 1) createSound("damaged sound effect.wav");
							playerstats.active_iFrames = playerstats.iFrames;
							player.y += 5; 
						}	
					}
				}
				for(int e=0; e<enemyCache.size(); e++) {
					if(enemyCache.get(e) instanceof Tanker) {
						Rectangle projRect = new Rectangle(enemyCache.get(e).x, enemyCache.get(e).y, enemyCache.get(e).size, enemyCache.get(e).size);
						if(player.contains(projRect.x, projRect.y) || player.contains(projRect.x+projRect.width, projRect.y) || player.contains(projRect.x, projRect.y+projRect.height) || player.contains(projRect.x+projRect.width, projRect.y+projRect.height)) {
							playerstats.hp -= 1;
							if(playerstats.hp > 1) createSound("damaged sound effect.wav");
							playerstats.active_iFrames = playerstats.iFrames;
							player.y += 5; 
						}	
					}
				}
			}
		}
		
		/*
		 * Checks if the player is dead
		*/
		public void checkDeath() {
			if(playerstats.hp<=0) {
				func_gameOver();
			}
		}
	};
	
	/*
	 * Interface for enemy methods
	*/
	GameFunctions enemyFunctions = new GameFunctions() {
		
		/*
		 * Moves all enemies
		*/
		public void move() {
			try {
				for(Enemy i: enemyCache) {
					if(i instanceof Liner) {
						i.x = (int) ((i.spd)*Math.cos(Math.toRadians(i.rotation))+ i.x);
						i.y = (int) ((i.spd)*Math.sin(Math.toRadians(i.rotation))+ i.y);
						if(i.x>=WINB-i.size || i.x<=0) {
							if(i.rotation > 90) i.rotation=45;
							else i.rotation=135;
						}
					}
					if(i instanceof Tanker) {
						i.rotation = i.findRotation(i.x, i.y, player.x, player.y);
						i.x = (int) ((i.spd)*Math.cos(Math.toRadians(i.rotation))+ i.x);
						i.y = (int) ((i.spd)*Math.sin(Math.toRadians(i.rotation))+ i.y);
					}
					if(i instanceof Rotater) {
						i.pointY += i.spd;
						i.rotation += i.rotationSpd;
						i.x = (int) ((i.displacement)*Math.cos(Math.toRadians(i.rotation))+ i.pointX);
						i.y = (int) ((i.displacement)*Math.sin(Math.toRadians(i.rotation))+ i.pointY);
					}
				}
				for(int i=0; i<enemyCache.size(); i++) {
					if(enemyCache.get(i).y>WINH) enemyCache.remove(i);
				}
			} catch(ConcurrentModificationException oops) {}
		}

		/*
		 * Moves all enemy projectiles
		*/
		public void move_Projectiles() {
			try {
				for(Projectile i: projectileCache) {
					if(i instanceof Enemy_lineProjectile) {
						i.x = (int) ((i.spd)*Math.cos(Math.toRadians(i.rotation))+ i.x);
						i.y = (int) ((i.spd)*Math.sin(Math.toRadians(i.rotation))+ i.y);
					}
				}
			} catch(ConcurrentModificationException oops) {}
		}

		/*
		 * Deletes enemy projectiles based on position
		*/
		public void delete_Projectiles() {
			for(int i=0; i<projectileCache.size(); i++) {
				if(projectileCache.get(i).x < 0 || projectileCache.get(i).x>WINB || projectileCache.get(i).y<0 || projectileCache.get(i).y > WINH) projectileCache.remove(i);
			}
		}

		/*
		 * Fires shots from enemy
		*/
		public void shoot(int x, int y) {
			projectileCache.add(new Enemy_lineProjectile(x, y));
			projectileCache.get(projectileCache.size()-1).rotation = projectileCache.get(projectileCache.size()-1).findRotation(projectileCache.get(projectileCache.size()-1).x, projectileCache.get(projectileCache.size()-1).y, player.x, player.y);
		}

		/*
		 * Check if enemies are hit by a bullet
		*/
		public void checkCollision() {
			for(int enemy=0; enemy<enemyCache.size(); enemy++) {
				Rectangle e = new Rectangle(enemyCache.get(enemy).x, enemyCache.get(enemy).y, enemyCache.get(enemy).size, enemyCache.get(enemy).size);
				for(int p=0; p<projectileCache.size(); p++) {
					if(projectileCache.get(p) instanceof Player_lineProjectile) {
						Rectangle projRect = new Rectangle(projectileCache.get(p).x,projectileCache.get(p).y, projectileCache.get(p).size,projectileCache.get(p).size);
						if(e.contains(projRect.x,projRect.y) || e.contains(projRect.x+projRect.width,projRect.y) || e.contains(projRect.x,projRect.y+projRect.height) || e.contains(projRect.x+projRect.width,projRect.y+projRect.height)) {
							projectileCache.remove(p);
							try {
							enemyCache.get(enemy).hp -= 1;
							} catch(IndexOutOfBoundsException oops) {}
						}
						
					}
				}
			}
		}
		
		/*
		 * Checks if the enemy is dead
		*/
		public void checkDeath() {
			for(int e = 0; e<enemyCache.size(); e++) {
				if(enemyCache.get(e).hp <= 0) {
					if(randNum.nextInt(1,2+1) == 2) powerCache.add(new Powerups(enemyCache.get(e).x, enemyCache.get(e).y));
					enemyCache.remove(e);
					createSound("kill noise.wav");
					kills++;
				}
			}
		}
		
	};
	
	void movePowerup() {
		for (Powerups p: powerCache) {
				p.y += 5;
		}
	}

	//deletes power if it intersects with the player
	void deletePower() {
		for (int i = 0; i < powerCache.size(); i++) {
			if (powerCache.get(i).intersect || powerCache.get(i).y>WINH) powerCache.remove(i);
		}
	}

	//checks if the player intersects with powerup, if so a powrup is given
	void checkpUPColl() {

		for (Powerups p: powerCache) {
			// creates a rectangle to check if the player intersect it
			Rectangle powerup = new Rectangle(p.x,p.y,p.diameter,p.diameter);

			//depending on the powerup's diameter, a specific stat is given
			//generally, the smaller the diameter, the better the power up
			if (powerup.intersects(player)) {
				switch(p.diameter) {
				case 5:
					weaponState = randNum.nextInt(1,3+1);
					break;
				case 7:
					playerstats.jetFuel += 55;
					break;
				case 10:
					playerstats.hp +=1;
					break;
				}
				//makes it so that delete method knows which powerup to delete
				p.intersect = true;
			}
		}	
	}
	
	void createSound(String audioFileName) {
		Clip playerShootNoise = null;
			try {
				AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(audioFileName));
				playerShootNoise = AudioSystem.getClip();
				playerShootNoise.open(inputStream);
				playerShootNoise.start();
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}

	}
	
}
