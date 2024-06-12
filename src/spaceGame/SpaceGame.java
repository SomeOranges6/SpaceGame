package spaceGame;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Random;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import hsa2.GraphicsConsole;

public class SpaceGame implements ActionListener {
	//Initializing many, many, variables.
	
	//fonts
	Font normalFont = new Font("Tahoma", Font.PLAIN, 32);
	Font bigFont = new Font("Tahoma", Font.BOLD, 64);
	Font gameOverFont = new Font("Tahoma", Font.BOLD, 64);
	
	//window size
	public int WINB=600, WINH=600;
	public int ms_sleep = 1000/60; //it's 60fps... but not.
	
	//random
	Random randNum = new Random();
	
	//intergers
	public int loop2 = 0, printingx = 280;;
	public int charged = 0;
	public int weaponState = 0;
	public int currentWave = 0, waveAccumulator=0, difficulty = 0, kills=0;
	public boolean canUseJets = true;
	public int enemyCD = 1663, starCD = 50;
	public int mx = 0, my = 0, level, show = 0, menu = 1, selectory = 250, loop = 0;
	
	//ArrayLists
	public ArrayList<Powerups> powerCache = new ArrayList<Powerups>();
	public ArrayList<Projectile> projectileCache = new ArrayList<Projectile>();
	public ArrayList<Enemy> enemyCache = new ArrayList<Enemy>();
	public ArrayList<Background> stars = new ArrayList<Background>();
	public ArrayList<Boss> bossCache = new ArrayList<Boss>(); //Who knows if we do more than one boss at once
	public ArrayList<String> intructions = new ArrayList<String>();
	public ArrayList<String> textCache = new ArrayList<String>();
	public ArrayList<BufferedImage> levels = new ArrayList<BufferedImage>();
	public ArrayList<String> prints = new ArrayList<String>();
	
	//STRINGS
	public String printing = "";
	
	//Other Objects
	public Player playerstats = new Player();
	public Boss1 Boss = new Boss1();
		
	//Rectangles
	public Rectangle player = new Rectangle(playerstats.x, playerstats.y, playerstats.size, playerstats.size);
	public Rectangle limit = new Rectangle(0,200, WINB,400);
	public Rectangle rBoss = new Rectangle(Boss.x,Boss.y,Boss.size,Boss.size);
	public Rectangle rtitle = new Rectangle(0,0,400,200);
	public Rectangle r2 = new Rectangle(210, 300, 150, 50);
	public Rectangle r3 = new Rectangle(210, 400, 150, 50);
	public Rectangle r4 = new Rectangle(0, 525, 80, 75);
	public Rectangle r5 = new Rectangle(0,350,100,150);
	public Rectangle r6 = new Rectangle(450,400,100,150);
	public Rectangle r7 = new Rectangle(100, 200, 300, 400);
	public Rectangle b1 = new Rectangle(0,0,0,0);
	public Rectangle b2 = new Rectangle(0,0,0,0);
	
	//Graphic Consoles
	public GraphicsConsole winner = new GraphicsConsole(WINB,WINH);
	public GraphicsConsole game = new GraphicsConsole(WINB,WINH);
	public GraphicsConsole gameOver = new GraphicsConsole(WINB, WINH);
	public GraphicsConsole gci = new GraphicsConsole(WINB,WINH);
	//for even later: GraphicsConsole pauseScreen = new GraphicsConsole(WINB, WINH)
	
	//Timers
	public Timer playerShoots = new Timer(playerstats.firerate, this);
	public Timer enemySpawn = new Timer(enemyCD, this);
	public Timer enemyShoots = new Timer(ms_sleep, this); public int enemyShotAccumulator = 0;
	public Timer starSpawn = new Timer(starCD, this);
	public Timer bossAttack = new Timer(5000,this);
	public Timer easyTimer = new Timer(10000, this); //for rounds lasting too long maybe
	public Timer BossCD = new Timer(2000,this);
	
		
	//images
	public BufferedImage charged1,charged2,BossS,ast,rock,howToPlayMenu, questionButton,title, playButton, quitButton, image, quitButton2, image2,playButton2, tutorialButton,wave1Button,wave2Button,wave3Button,wave4Button,bossButton, selectorImage;
	BufferedImage redEnemy = null;
	BufferedImage blueEnemy = null;
	BufferedImage greenEnemy = null;
	BufferedImage yellowEnemy = null;
	BufferedImage spaceship = null;
	BufferedImage damagedSpaceship = null;
	
	//Clip Objects
	Clip heavensHellSentGift = null;
	Clip pestOfTheCosmos = null;
	Clip interstellarStomper = null;
	Clip backgroundMusic = null;
	
	//Booleans
	boolean wait = false;
	boolean exitIntro = false, shrink = true;
	boolean smash = false;
	boolean drawAttack = false;
	boolean loaded = false;
	
	//IMAGE LOADER
	static BufferedImage loadImage(String fileName) {
		BufferedImage img = null;
			try {
				img = ImageIO.read(new File(fileName).getAbsoluteFile());
			} catch (IOException e) {
				e.printStackTrace();
			    JOptionPane.showMessageDialog(null, "An image failed to load" + fileName, "ERROR", JOptionPane.ERROR_MESSAGE);
			}
			  return img;
	}
	
	//AUDIO LOADER
	void loadSoundtrackAndImages() {
		try {
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("backgroundMusic.wav"));
			backgroundMusic = AudioSystem.getClip();
			backgroundMusic.open(inputStream);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
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
		
	//main	
	public static void main(String[] args) {
		new SpaceGame();
	}
	
	//main game
	SpaceGame() {
		
		//checks if audio loaded
		if(loaded == false) {
			loadSoundtrackAndImages();
			loaded = true;
		}
		
		//setting up one time things
		setup_game();
		
		//set up intro
		setupintro();
		
		//updates and draws intro
		while(!exitIntro) {
			mx = gci.getMouseX();
			my = gci.getMouseY();
			movetitleIntro();
			checkMouseIntro();
			checkKeyboardIntro();
			drawintro();
			gci.sleep(5);
		}
		
		//starts main game
		func_startGame();
		
		//updates main game
		while(true) {
			updScreen();
			drawGraphics();
			game.sleep(ms_sleep);
			gci.sleep(ms_sleep);
		}
	}
	
	
	/*
	 * The method that draws the gameplay initially
	*/
	void setup_game() {
		winner.setVisible(false);
		game.setVisible(false);
		gameOver.setVisible(false);
		game.setBackgroundColor(new Color(15,10,15));
		game.setLocationRelativeTo(null);
		game.clear();
		game.setFont(normalFont);
		
		prints.add("Welcome to the TUTORIAL");
		prints.add("Press wasd or the arrow keys to move");
		prints.add("Nice!");
		prints.add("Now try pressing space bar to boost");
		prints.add("GOOD JOB!!!!");
		prints.add("Next we shall spawn an enemy for you");
		prints.add("and let the shooting commence");
		prints.add("Enemies drop Powerups when they die");
		prints.add("PICK IT UP");
		prints.add("AMAZING");
		prints.add("Looks like your ready for battle");
	}
	
	/*
	 * The method sets up one time things for intro screen
	 */
	void setupintro() {
		gci.setVisible(true);
		gci.setBackgroundColor(Color.BLACK);
		gci.setAntiAlias(true);
		gci.setLocationRelativeTo(null);
		gci.clear();
		
		gci.enableMouseMotion();
		gci.enableMouse();
		
		questionButton = loadImage("question.png");
		title = loadImage("title.png");
		playButton = loadImage("play.png");
		quitButton = loadImage("quit.png");
		quitButton2 = loadImage("selected-quit.png");
		playButton2 = loadImage("selected-play.png");
		tutorialButton = loadImage("tutorial.png");
		wave1Button = loadImage("wave1.png");
		wave2Button = loadImage("wave2.png");
		wave3Button = loadImage("wave3.png");
		wave4Button = loadImage("wave4.png");
		bossButton = loadImage("boss.png");
		selectorImage = loadImage("selector.png");
		howToPlayMenu = loadImage("how.png");
		rock = loadImage("rock.png");
		ast = loadImage("astr.png");
		charged1 = loadImage("red enemy2.png");
		charged2 = loadImage("red enemy3.png");
		BossS = redEnemy;
		
		levels.add(tutorialButton);
		levels.add(wave1Button);
		levels.add(wave2Button);
		levels.add(wave3Button);
		levels.add(wave4Button);
		levels.add(bossButton);
		
		backgroundMusic.loop(backgroundMusic.LOOP_CONTINUOUSLY);
		
	}
	
	/*
	 * checks mouse input/motion for intro
	 */
	void checkMouseIntro() {
		if (r2.contains(mx,my)) image = playButton2;
		else image = playButton;
		if (r3.contains(mx,my)) image2 = quitButton2;
		else image2 = quitButton;
		
		if (gci.getMouseClick() == 1) {
			if (menu == 2) {
				if (r5.contains(mx,my) && show > 0) show -= 1;
				if (r6.contains(mx,my) && show < 5) show += 1;
				if (r7.contains(mx,my)) {
					level = show;
					exitIntro = true;
				}
			}
			if (r2.contains(mx,my))	menu = 2;
			if (r3.contains(mx,my)) gci.close();
			if (r4.contains(mx,my)) menu = 3;
			
		}
		
		
	}
	
	/*
	 * checks keyboard input for intro
	 */
	void checkKeyboardIntro() {
		if (menu == 2) {
			if (gci.getKeyCode() == 10) {
				level = show;
				exitIntro = true;
			}
			
			if (gci.getKeyCode() == 39 && show < 5) show += 1;
			if (gci.getKeyCode() == 37 && show > 0) show -= 1;
		}
		if (gci.getKeyCode() == 27) {
			menu = 1;
		}
		
	}
	
	/*
	 * makes the title grow and shrink
	 */
	void movetitleIntro() {
		if (shrink) {
			rtitle.width-=10;
			rtitle.height-=5;
		}
		else {
			rtitle.width+= 10;
			rtitle.height+=5;
		}
		if (rtitle.width == 400) shrink = true;
		if (rtitle.width == 380) shrink = false;
		gci.sleep(80);
	}
	
	/*
	 * draws the intro screen
	 */
	void drawintro() {
		synchronized (gci) {
			gci.clear();
			
			gci.drawImage(title, (600-rtitle.width)/2 ,50, rtitle.width, rtitle.height);
			switch(menu) {
				case 1:
					gci.drawImage(questionButton, 0, 525, 80, 75);
					gci.drawImage(image, r2.x, r2.y, r2.width, r2.height);
					gci.drawImage(image2, r3.x, r3.y, r3.width, r3.height);
					break;
				case 2:
					gci.drawImage(questionButton, 0, 525, 80, 75);
					gci.drawImage(levels.get(show), 55, 200, 500, 400);	
					gci.drawImage(selectorImage, 0, 350, 600, 150);
					break;
				case 3:
					gci.drawImage(howToPlayMenu, 30, 200, 550, 400);
					break;
			}
			
			if (exitIntro) {
				for (int i = 0; i < 255; i +=5) {
					gci.setColor(new Color(255,255,255,i));
					gci.fillRect(0,0,600,600);
					gci.sleep(5);
				}
			}
		}
	}
	
	/*
	 * This method is the whole function of what getting a game over does
	*/
	void func_gameOver() {
		interstellarStomper.stop();
		game.dispose();
		gci.dispose();
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
	 *this methods plays audio of the given String
	 *@param String AudioFileName audio to play
	 */
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
	
	/*
	 * This method occurs when a player wins/finishes a level
	 */
	void func_winner() {
		interstellarStomper.stop();
		game.dispose();
		gci.dispose();
		heavensHellSentGift.stop(); heavensHellSentGift.setMicrosecondPosition(0);
		pestOfTheCosmos.stop(); pestOfTheCosmos.setMicrosecondPosition(0);
		interstellarStomper.stop();	pestOfTheCosmos.setMicrosecondPosition(0);
		playerShoots.restart(); playerShoots.stop(); 
		enemySpawn.restart(); enemySpawn.stop(); 
		enemyShoots.restart(); enemyShoots.stop(); 
		easyTimer.restart(); easyTimer.stop(); 
		winner.setFont(bigFont);
		winner.setLocationRelativeTo(null);
		winner.setBackgroundColor(new Color(0,0,0));
		winner.clear();
		createSound("winner.wav");
		winner.setColor(new Color(255,255,255));
		winner.setVisible(true);
		switch(level) {
		case 0:
			winner.drawString("TUTORIAL", WINB/5, WINH/2);
			winner.drawString("COMPLETED", WINB/5, 375);
			break;
		case 5:
			winner.drawString("YOU WIN", WINB/5, WINH/2);
			break;
		}
		winner.setFont(normalFont);
		if (level != 0) winner.drawString(String.format("KILLS: %d", kills), WINB/6, WINH/2+75);
		winner.sleep(3000);
		kills = 0;
		winner.dispose();
		new SpaceGame();
	}
	/*
	 * This method is used to disable other consoles and start up the main game
	*/
	void func_startGame() {
			interstellarStomper.stop();
			backgroundMusic.stop();
			game.setVisible(true);
			gci.setVisible(false);
			playerstats.hp *= 3;
			playerstats.maxhp *=3;
			playerstats.iFrames *= 2;
			enemyCD *= 1.5;
			playerShoots.start();
			enemyShoots.start();
			starSpawn.start();
			easyTimer.start();
			if (level != 5) {
				enemySpawn.start();
				heavensHellSentGift.loop(Clip.LOOP_CONTINUOUSLY);
			}
	}
	
	
	/*
	 * This method will update positions and run checks
	*/
	void updScreen() {
		switch(level) {
			//TUTORIAL
			case 0:
				updTUTORIAL();
				break;
			//WAVE 1
			case 1:
				basicUpdate();
				break;
				
			//WAVE 2
			case 2:
				basicUpdate();
				currentWave = 1;
				break;
			//WAVE 3
			case 3:
				basicUpdate();
				currentWave = 2;
				break;
			//WAVE 4
			case 4:
				basicUpdate();
				currentWave = 3;
				break;
			//BOSS LEVEL
			case 5:
				interstellarStomper.loop(Clip.LOOP_CONTINUOUSLY);
				bossAttack.start();
				updateBoss();
				basicUpdate();
				break;
		
		}
	}
	
	/*
	 * This method is used to update the level screens
	 */
	void basicUpdate() {
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
		deletePower();
		movePowerup();
		checkpUPColl();
	}
	
	/*
	 * This method updates the screen for the tutorial
	 */
	
	
	void updTUTORIAL() {
		playerFunctions.move();
		if (loop == prints.size()) func_winner();
		textCache.clear();
		
		for (String s: prints.get(loop).split("")) {
			textCache.add(s);
		}
		
		if (loop2 < prints.get(loop).length()) {
			printing += textCache.get(loop2);
			printingx -=7;
		}
		loop2+=1;
		if ((loop == 2 || loop == 4 || loop == 9) && loop2 > 30) loop2+=2;
		if (loop2 > 100 && wait == false) {
			printingx = 280;
			printing = "";
			loop +=1;
			loop2 = 0;
		}
		
		if (loop == 1) {
			wait = true;
			if (game.getKeyChar() == 'w' || game.getKeyChar() == 'a' || game.getKeyChar() == 's' || game.getKeyChar() == 'd' || game.getKeyCode() == 37 || game.getKeyCode() == 38 || game.getKeyCode() == 39 || game.getKeyCode() == 40) {
				wait = false;
				loop2 +=99;
			}
		}
		
		if (loop == 3) {
			wait = true;
			if (game.getKeyCode() == 32){
				wait = false;
				loop2 +=99;
			}
		}
		
		if (loop >= 6) {
			playerFunctions.move_Projectiles();
			playerFunctions.delete_Projectiles();
			enemyFunctions.checkCollision();
			enemyFunctions.checkDeath();
			deletePower();
			movePowerup();
			checkpUPColl();
		}
		
		if (loop == 6) {
			wait = true;
			if (loop2 == 0)enemyCache.add(new Liner(300,300));
			if (enemyCache.size() == 0) {
				loop2 += 100;
				wait = false;
			}
		}
		
		if (loop == 8) {
			wait = true;
			if (powerCache.size() == 0) {
				loop2 += 100;
				wait = false;
			}
		}
		
	}
	
	
	/*
	 * This method will draw the game
	*/
	void drawGraphics() {
		synchronized(game) {
			//sets background Color
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
			
		
			//draws powerups
			try {
				for (Powerups p: powerCache) {
					if(p.diameter == 5) game.setColor(new Color(200, 200, 100));
					else if(p.diameter == 7) game.setColor(new Color(102,255,204));
					else game.setColor(new Color(20, 200, 20));
					game.fillOval(p.x,p.y,p.diameter,p.diameter);
				}
			} catch(ConcurrentModificationException oops) {}
			
			
			/*
			 * this statment is only true during boss fights
			 * Will draw Bosses's attacks
			 */
			if (drawAttack) {
				if (Boss.move.equals("ROCK")) {
					game.drawImage(rock,b1.x, b1.y, b1.width, b1.height);
					game.drawImage(rock,b2.x, b2.y, b2.width, b2.height);
				}
				if (Boss.move.equals("LASER")) {
					game.setColor(Color.RED);
					game.fillRect(b1.x, b1.y, b1.width, b1.height);
					game.fillRect(b2.x, b2.y, b2.width, b2.height);
				}
				if (Boss.move.equals("AST")) {
					game.drawImage(ast,b1.x, b1.y, b1.width, b1.height);
					game.drawImage(ast,b2.x, b2.y, b2.width, b2.height);
				}
				if (Boss.move.equals("BLAST")) {
					game.setColor(Color.PINK);
					game.fillRect(b1.x, b1.y, b1.width, b1.height);
					game.setColor(Color.RED);
					game.fillRect(b2.x, b2.y, b2.width, b2.height);
				}
			}
			//draws Boss during Boss level
			if (level == 5) {
				game.drawImage(BossS,Boss.x, Boss.y, Boss.size ,Boss.size);
			}
			
			if (level == 0) {
				game.setColor(Color.WHITE);
				game.drawString(printing,printingx,200);
			}
			
			//drawing text and bars
			if(playerstats.hp > 3)game.setColor(new Color(102,255,204, 134));
			else game.setColor(new Color(255,51,102, 134));
			game.drawString(String.format("HP: %d", playerstats.hp), 50, 547);
			game.fillRect(50, 550, 500/450*500-((playerstats.maxhp-playerstats.hp)*500/450*500/playerstats.maxhp), 10);
			if(canUseJets) game.setColor(new Color(102,255,204, 134));
			else game.setColor(new Color(255,51,102, 134));
			game.drawString(String.format("JET: %d", playerstats.jetFuel), 50, 577);
			game.fillRect(50, 580, playerstats.jetFuel, 10);
		}
		
	}
	
	/*
	 * This method moves the stars that spawn
	*/
	void moveStars() {
		for(int b=0; b<stars.size(); b++) {
			stars.get(b).y += stars.get(b).spd;
			if(stars.get(b).y>WINH) stars.remove(b);
		}
	}
	
	//for every powerup in the arraylist, it moves it down 5 pixels
	void movePowerup() {
		for (Powerups p: powerCache) {
			if (p.type != "NOTHING" && level != 0) {
				p.y += 5;
			}
			if (p.type != "NOTHING" && level == 0 && p.y < 500) {
				p.y +=5;
			}
		}
	}
	
	//deletes power if it intersects with the player
	void deletePower() {
		for (int i = 0; i < powerCache.size(); i++) {
			if (powerCache.get(i).intersect) powerCache.remove(i);
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
					createSound("powerup.wav");
					switch(p.diameter) {
					case 5:
						weaponState = randNum.nextInt(1,3+1);
						break;
					case 7:
						playerstats.jetFuel += 55;
						break;
					case 10:
						if (playerstats.hp < playerstats.maxhp)playerstats.hp +=1;
						break;
					}
					//makes it so that delete method knows which powerup to delete
					p.intersect = true;
				}
			}	
		}
	
		
		

		
	//updates the boss
	void updateBoss() {
		
		Boss.rotation++;
		Boss.x = (int) ((50)*Math.cos(Math.toRadians(Boss.rotation))+WINB/2-Boss.size/2);
		
		
		//checks if dead
		if (Boss.hp <= 0) {
			func_winner();
		}
		
		//for testing pruposes, pressing k instantly kills the boss
		if (game.getKeyChar() == 'k') {
			Boss.hp = 0;
		}
		
		//checks if boss got hit by player projectile (Rajdeep's code)
		for(int p=0; p<projectileCache.size(); p++) {
			if(projectileCache.get(p) instanceof Player_lineProjectile) {
				Rectangle projRect = new Rectangle(projectileCache.get(p).x,projectileCache.get(p).y, projectileCache.get(p).size,projectileCache.get(p).size);
				if(rBoss.contains(projRect.x,projRect.y) || rBoss.contains(projRect.x+projRect.width,projRect.y) || rBoss.contains(projRect.x,projRect.y+projRect.height) || rBoss.contains(projRect.x+projRect.width,projRect.y+projRect.height)) {
					projectileCache.remove(p);
					Boss.hp -= 1;
				}
			}
		}
		
		/*
		 * Depending on the bosses's move
		 * it will spawn the attack, and then move it
		 */
		
		//summons two rectangles to smash the player 
		if (Boss.move.equals("ROCK")) {
			
			//moves rectangle to side
			if (b1.x > 0 && b2.x < 550 && smash == false) {
				b1.x -= 5;
				b2.x += 5;
			}
			
			//lowers it to player poisition
			if (b1.y < player.y) {
				if (b1.x == 0 && b2.x == 550) {
					b1.y += 20;
					b2.y += 20;
				}
			}
			
			//the smash variable was used so that it waited before attacking
			if (smash) {
				if (b1.x+ b1.width < 300 && b2.x> 300) {
					b1.x += 30;
					b2.x -= 30;
				}
				if (b1.x + b1.width >= 300 && b2.x <=300) {
					BossCD.stop();
					smash = false;
				}
			}
			
			//if the player ever touches the rectangles, they lose 1 hp
			checkAttackColl();
		}
		
		//the boss shoots a laser from either side of your screen and moves closer to you
		if (Boss.move.equals("LASER")) {
			
			//add resticition so that there is space for the player not to get hit
			if (b1.x < 200) {
				b1.x += 4;
				b2.x -= 4;
				if (b1.height < 600) {
					b1.height += 50;
					b2.height += 50;
				}
			}
			
			//if player touches it, they lose hp
			checkAttackColl();
			
		}
		
		if (Boss.move.equals("AST")) {
			if (charged > 40) {
				b1.width = 100;
				b1.height = 100;
				b2.width = 100;
				b2.height = 100;
				b1.x += player.x/20;
				b1.y += player.y/20;
				b2.x -= (575 - player.x)/20;
				b2.y += player.y/20;
				checkAttackColl();
			}
			charged +=1;
		}
		
		
		if (Boss.move.equals("BLAST")) {
			b1.x = Boss.x+50;
			b2.x = Boss.x+95;
			if (charged == 20) {
				BossS = charged1;
			}
			if (charged == 40) {
				BossS = charged2;
			}
			if (charged == 50) {
				createSound("blastsfx.wav");
			}
			if (charged >= 60) {
				b1.height += 80;
				b2.height += 80;
			}
			charged += 1;
			checkAttackColl();
		}
		
		//the boss summons a total of 10 enemies
		if (Boss.move.equals("SUMMON")) {
			for (int i = 0; i < 10; i ++) {
				enemyCache.add(new Liner(randNum.nextInt(0,WINB-16), 0));
			}
			//stops the boss from spawning 10 enemies every 5 miliseconds
			Boss.move = "NOTHING";
		}
	}
	
	void checkAttackColl() {
		if (b1.intersects(player) || (b2.intersects(player))) {
			if(playerstats.active_iFrames <= 0) {
				playerstats.hp -= 1;
				playerstats.active_iFrames = playerstats.iFrames;
				player.y += 5; 
			}
		}
	}
	
	/*
	 * Timer method where Timers are utilized
	*/
	@Override
	public void actionPerformed(ActionEvent ev){ //Timer should be used for wave progression?
		
		//boss attack cooldown
		if (ev.getSource() == BossCD) {
			smash = true;
		}
		//every # second a new boss move is generated
		if (ev.getSource() == bossAttack) {
			charged = 0;
			BossS = redEnemy;
			drawAttack = true;
			smash = false;
			BossCD.stop();
			switch(randNum.nextInt(5)) {
			case 0:
				BossCD.start();
				b1.x = 100;
				b1.y = 100;
				b1.width = 50;
				b1.height = 100;
				b2.x = 450;
				b2.y = 100;
				b2.width = 50;
				b2.height = 100;
				Boss.move = "ROCK";
				break;
			case 1:
				b1.x = 0;
				b1.y = 0;
				b1.width = 20;
				b1.height = 0;
				b2.x = 580;
				b2.y = 0;
				b2.width = 20;
				b2.height = 0;
				Boss.move = "LASER";
				createSound("lasersfx.wav");
				break;
			case 2:
				Boss.move = "SUMMON";
				break;
			case 3:
				b1.x = 0;
				b1.y = 0;
				b1.width = 0;
				b1.height = 0;
				b2.x = 550;
				b2.y = 0;
				b2.width = 0;
				b2.height = 0;
				Boss.move = "AST";
				createSound("alertsfx.wav");
				break;
			case 4:
				b1.x = Boss.x+50;
				b1.y = 150;
				b1.width = 100;
				b1.height = 10;
				b2.x = Boss.x+95;
				b2.y = 150;
				b2.width = 5;
				b2.height = 10;
				Boss.move = "BLAST";
				break;
			}
		}
		
		
		//player shoots based on a timer
		if (level != 0 || loop >= 6) {
			if(ev.getSource() == playerShoots) {
				playerFunctions.shoot(player.x+playerstats.size/2, player.y+playerstats.size/2);
			}
		}
		
		//infinite enemies spawn based on a timer
		if(ev.getSource() == enemySpawn && level != 0) {
			waveAccumulator++;
			if(waveAccumulator==3) currentWave++;
			easyWaves[currentWave].wave();
			
	}
		
		//keeps track of when enemies should fire. It is coded so they all don't fire at the same time
		if(ev.getSource() == enemyShoots && level != 0) {
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
				enemySpawn.stop();
				enemySpawn.setInitialDelay(10000);
				enemySpawn.restart();
				
			}},
			new Waves() {public void wave() { //Wave 2
				for(int i=0; i<enemyCache.size(); i++) {
					enemyCache.remove(0);
				}
				for(int i=0; i<4; i++) {
					enemyCache.add(new Tanker(i*150, 0));
				}
				enemySpawn.stop();
				enemySpawn.setInitialDelay(10000);
				enemySpawn.restart();
			}},
			new Waves() {public void wave() { //Wave 3
				for(int i=0; i<enemyCache.size(); i++) {
					enemyCache.remove(0);
				}
				for(int i=0; i<4; i++) {
					enemyCache.add(new Rotater(i*150, 0, 90));
				}
				enemySpawn.stop();
				enemySpawn.setInitialDelay(10000);
				enemySpawn.restart();
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
					player.x = (int) ((playerstats.fasterSpd)*Math.cos(Math.toRadians(normalization)) - (0)*Math.sin(Math.toRadians(normalization)) + player.x);
					player.y = (int) ((playerstats.fasterSpd)*Math.sin(Math.toRadians(normalization)) + (0)*Math.cos(Math.toRadians(normalization)) + player.y);
				}
				else {
					player.x = (int) ((playerstats.spd)*Math.cos(Math.toRadians(normalization)) - (0)*Math.sin(Math.toRadians(normalization)) + player.x);
					player.y = (int) ((playerstats.spd)*Math.sin(Math.toRadians(normalization)) + (0)*Math.cos(Math.toRadians(normalization)) + player.y);
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
		public void move_Projectiles() {
			for(Projectile i: projectileCache) {
				if(i instanceof Player_lineProjectile) {
					i.y -= i.spd;
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
						i.x = (int) ((i.spd)*Math.cos(Math.toRadians(i.rotation)) - (0)*Math.sin(Math.toRadians(i.rotation)) + i.x);
						i.y = (int) ((i.spd)*Math.sin(Math.toRadians(i.rotation)) + (0)*Math.cos(Math.toRadians(i.rotation)) + i.y);
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
					//basically repalces the enemy with a powerup when it dies
					powerCache.add(new Powerups(enemyCache.get(e).x, enemyCache.get(e).y));
					enemyCache.remove(e);
					createSound("kill noise.wav");
					kills++;
				}
			}
		}
		
		
		
	};
}
