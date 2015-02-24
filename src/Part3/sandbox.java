package Part3;

import java.io.IOException;

public class sandbox {
	public static void main(String args[])throws IOException{
		sandbox sb = new sandbox();
		EZ.initialize(1000, 1000);
		sb.sand_fun();
	}
	
	kellysj_actor K_obj1,K_obj2,K_obj3;
	savanna_Animator S_obj1,S_obj2,S_obj3;
	chencal_actor C_obj1,C_obj2,C_obj3,C_obj4;
	
	Reed_Actor R_obj1;//,R_obj2,R_obj3; 
	Mike_Actor M_obj1;//,M_obj2,M_obj3;

	public void sand_fun()throws IOException{
		K_obj1 = new kellysj_actor(450,500,0,1,"Kelly_bug.png","Kelly_script1.txt");
		K_obj2 = new kellysj_actor(550,500,0,1,"Kelly_bug.png","Kelly_script2.txt");
		K_obj3 = new kellysj_actor(500,550,0,1,"Kelly_bug.png","Kelly_script3.txt");
		S_obj1 = new savanna_Animator("savanna_spider.png", "savanna_script1.txt", 200, 650, 0, 1);
		S_obj2 = new savanna_Animator("savanna_nyancat.png", "savanna_script2.txt", 400, 200, 0, 1);
		S_obj3 = new savanna_Animator("savanna_butterfly.png", "savanna_script3.txt", 400, 100, 0, 1);
		C_obj1 = new chencal_actor("chencal_ANIMATORBOY.txt", "chencal_boy.png");
		C_obj2 = new chencal_actor("chencal_ANIMATORGIRL.txt", "chencal_girl.png");
		C_obj3 = new chencal_actor("chencal_ANIMATORHEART.txt", "chencal_Heart.png");
		C_obj4 = new chencal_actor("chencal_ANIMATORMOON.txt", "chencal_moon.png");
		
		M_obj1 = new Mike_Actor("Finn.png", "Mike_script1.txt");
		//M_obj2 = new Mike_Actor("Jake.png", "Mike_script2.txt");
		//M_obj3 = new Mike_Actor("BMO.png", "Mike_script3.txt");
		R_obj1 = new Reed_Actor("StandbyActorNONE1.png", "Reed_script1.txt");
		//R_obj2 = new Reed_Actor("Reed_StandbyActorNONE2.png", "Reed_script2.txt");
		//R_obj3 = new Reed_Actor("Reed_StandbyActorNONE2.png", "Reed_script3.txt");
		while(true){
			K_obj1.update();
			K_obj2.update();
			K_obj3.update();
			S_obj1.go();
			S_obj2.go();
			S_obj3.go();
			C_obj1.go();
			C_obj2.go();
			C_obj3.go();
			C_obj4.go();
			
			R_obj1.go();
			//R_obj2.go();
			//R_obj3.go();
			M_obj1.go();
			//M_obj2.go();
			//M_obj3.go();
			

			EZ.refreshScreen();
		}
	}
}
