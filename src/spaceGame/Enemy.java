package spaceGame;

@SuppressWarnings("serial")
abstract class Enemy extends GameEntities {
	public int hp, iFrames;
	public int firerate, fireBuffer, untilFire;
}

@SuppressWarnings("serial")
//Average line-moving joe
class Liner extends Enemy {
	Liner() {
		this.hp=1;
		this.iFrames=25;
		this.size=16;
		this.firerate=1000;
		this.fireBuffer=0;
		this.untilFire=0;
		this.x=WINB/2;
		this.y=0;
		this.spd = 1;
	}
	
	Liner(int x, int y) {
		this.hp=1;
		this.iFrames=25;
		this.size=16;
		this.firerate=1000;
		this.fireBuffer=0;
		this.untilFire=0;
		this.x=x;
		this.y=y;
		this.spd = 1;
	}

}

//Can take more damage
@SuppressWarnings("serial")
class Tanker extends Enemy {
	Tanker() {
		this.hp=4;
		this.iFrames=75;
		this.size=20;
		this.firerate=1000;
		this.fireBuffer=0;
		this.untilFire=0;
		this.x=WINB/2;
		this.y=0;
		this.spd = 2;
	}
	
	Tanker(int x, int y) {
		this.hp=4;
		this.iFrames=75;
		this.size=20;
		this.firerate=1000;
		this.fireBuffer=0;
		this.untilFire=0;
		this.x=x;
		this.y=y;
		this.spd = 2;
	}
	
}
/*
//Rotates around a point that may move
class Rotater extends Enemy {

}

//shoots really fast, essentially limiting the arena
class Beamer extends Enemy {

}

//Explodes on death and a bit tougher
class Demolitioner extends Enemy {

	void specialAbility() {
		//explodes on death
	}
}

*/
