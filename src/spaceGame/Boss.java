package spaceGame;

@SuppressWarnings("serial")
abstract class Boss extends GameEntities {
	public int hp, iFrames, Loop;
	public int attackrate; //If we get time, replace firerate for methods that do custom attacks
	public String bossTitle, move;
}
	
@SuppressWarnings("serial")
class Boss1 extends Boss {
	Boss1() {
		this.move = "SUMMON";
		this.Loop = 0;
		this.bossTitle="Gargantuar";
		this.hp=1000;
		this.iFrames=0;
		this.size=200;
		this.attackrate = 10000;
		this.x=200;
		this.y=25;
		this.spd = 3;
		
	}
}

