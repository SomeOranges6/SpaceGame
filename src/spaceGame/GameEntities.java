package spaceGame;

import java.util.Random;
import java.awt.Rectangle;
import java.lang.Math;

@SuppressWarnings("serial") //Suppressing the yellow line for a serial ID
abstract class GameEntities extends Rectangle{

	protected int WINB=600, WINH=600;
	public int x=0, y=0, spd=0, size=0; //spd will be normalized via the rotation equation
	protected Random randNum = new Random();
    
    //Inheritence :O
	
	//P.S. ANY CODE UNDER A MULTILINE COMMENT IS CODE TO WORRY ABOUT LATER
   
	
}




	


