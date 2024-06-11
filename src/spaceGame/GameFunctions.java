package spaceGame;

interface Waves  {
	void wave() throws InterruptedException;
}

interface GameFunctions {
	void move();
	void move_Projectiles();
	void delete_Projectiles();
	void shoot(int x, int y);
	void checkCollision();
	void checkDeath();
}

interface shootType {
	void shoot(int x, int y);
}