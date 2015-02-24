package EZdemos;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;


public class animator {

	
	actor sun;
	actor test;
	actor test2;
	actor test3;
	actor background;
	tree_actor tree_test;
	user user_test;
	String test_cord = "100, 300, 2";
	EZtwitter_ticker ticker;
	ArrayList<String> script = new ArrayList<String>();
	EZImage window_background;
	//script.add(50, 50 ,3);
	public static void main(String args[])throws IOException{
		animator sb = new animator();
		EZ.initialize(2000, 1200);
		EZ.setBackgroundColor(Color.blue);
		sb.sand_run();
	}
	public void sand_run()throws IOException{
		ticker = new EZtwitter_ticker();
		//test = new actor(EZ.getWindowWidth()/2,(8*EZ.getWindowHeight()/10),0,1,"Kelly_bug.png","Kelly_script1.txt");
		//test2 = new actor(100+(EZ.getWindowWidth()/2),(8*EZ.getWindowHeight()/10),0,1,"Kelly_bug.png","empty");
		//test3 = new actor(500,550,0,1,"Kelly_bug.png","Kelly_script3.txt");
		//spinner = new actor(500,550,0,1,"Kelly_bug.png","Kelly_script4.txt",0.5);
		sun = new actor( EZ.getWindowWidth()/10,(9*EZ.getWindowHeight()/10),0,1,"sun.png","Kelly_script3.txt",(double)0.05);
		tree_test = new tree_actor(EZ.getWindowWidth()/2,9*(EZ.getWindowHeight()/10), "bark_small.png","leaf.png","orange.png",true,true,true);
		user_test = new user(1500, 900, 0,1,"muscle_torso.png","Kelly_script2.txt");
		background = new actor(EZ.getWindowWidth()/2,EZ.getWindowHeight()/2, 0,1,"space_background.png","kelly_script4.txt");
		while(true){
			ticker.update();
			user_test.update();
			//test.update();
			//test2.update();
			background.update();
			sun.update();
			tree_test.go();
			
			EZ.refreshScreen();
		}
	}
}
