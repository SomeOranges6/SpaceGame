package spaceGame;

@SuppressWarnings("serial")
class Background extends GameEntities {
	public int size;
	public int R, G, B;
	
	Background() {
		this.x=randNum.nextInt(0,WINB-this.size+1);
		this.y=0;
		this.size=randNum.nextInt(1,5+1);
		this.spd=randNum.nextInt(1,4+1);
		this.R = randNum.nextInt(0,255+1);
		this.G = randNum.nextInt(0,255+1);
		this.B = randNum.nextInt(0,255+1);
	}
}
