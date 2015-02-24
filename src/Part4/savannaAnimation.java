package Part4;
import java.awt.Color;


public class savannaAnimation {


	public static void main(String[] args) throws java.io.IOException{
		
		// Setup EZ graphics system.
		EZ.initialize(1024, 768);

		EZ.setBackgroundColor(new Color(255, 255, 255));
		
		
		savanna_Animator background = new savanna_Animator("Clouds.png", "Cloud1.txt", 4000, 350, 0, 1);
		savanna_Animator blueship = new savanna_Animator("BlueSpaceship.png", "BlueSpaceship.txt", -500, 250, 0, 1);
		savanna_Animator redship = new savanna_Animator("RedSpaceship.png", "RedSpaceship.txt", -500, 550, 0, 1);
		
		savanna_Animator enemy1 = new savanna_Animator("EnemyShip.png", "enemy1.txt", 1200, 550, 0, 1);
		savanna_Animator enemy2 = new savanna_Animator("EnemyShip.png", "enemy2.txt", 1200, 550, 0, 1);
		savanna_Animator enemy3 = new savanna_Animator("EnemyShip.png", "enemy3.txt", 1200, 550, 0, 1);
		
		savanna_Animator ebullet1 = new savanna_Animator("ebullets.png", "ebullet1.txt", 1200, 550, 0, 1);
		savanna_Animator ebullet2 = new savanna_Animator("ebullets.png", "ebullet2.txt", 1200, 550, 0, 1);
		savanna_Animator ebullet3 = new savanna_Animator("ebullets.png", "ebullet3.txt", 1200, 550, 0, 1);
		
		savanna_Animator transition = new savanna_Animator("GradiantChange.png", "Transition.txt", 6000, 400, 0, 1);
		
		savanna_Animator rcircle = new savanna_Animator("RedCircle.png", "RedCircle.txt", 3000, 400, 0, 1);
		savanna_Animator ocircle = new savanna_Animator("OjCircle.png", "OjCircle.txt", 3000, 400, 0, 1);
		savanna_Animator wcircle = new savanna_Animator("WhiteCircle.png", "WhiteCircle.txt", 3000, 400, 0, 1);
		savanna_Animator gcircle = new savanna_Animator("GreenCircle.png", "GreenCircle.txt", 3000, 400, 0, 1);
		
		savanna_Animator finale = new savanna_Animator("Final.png", "Final.txt", 3000, 400, 0, 1);

		
	
	while(true){
		background.go();
		transition.go();
		blueship.go();
		redship.go();
		
		enemy1.go();
		enemy2.go();
		enemy3.go();
		
		ebullet1.go();
		ebullet2.go();
		ebullet3.go();
		
		rcircle.go();
		ocircle.go();
		wcircle.go();
		gcircle.go();
		
		finale.go();
		
		EZ.refreshScreen();
	}
	}
}
