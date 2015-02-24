package Part4;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class savanna_Animator {
		
		private EZImage actorImage;
		private int x, y, startx, starty;
		private int destx, desty;
		private long starttime;
		private double duration;
		boolean interpolation;
		int posx;
		int posy;
		
		double s;
		double startscale;
		float targetscale;
		
		private double a;
		private double origa;
		private double targa;
		
		double dur;
		int soundtag;
		
		
		String actorFile;String command = "";	
		BufferedReader fileReader = null;
		String line = "";	
		StringTokenizer St = new StringTokenizer(line);
		
   EZSound sound2 = EZ.addSound("JustMyPart.wav");
		
		
		public savanna_Animator(String filename, String txtname, int posx, int posy, int a, float s){
			x=posx;y=posy;
			
			actorImage = EZ.addImage(filename, posx, posy);
			actorImage.translateTo(x,y);
			actorImage.scaleTo(s);
			actorImage.rotateTo(a);
			interpolation = false;
			actorFile = txtname;
			
			{
				try {
					fileReader = new BufferedReader(new FileReader(actorFile));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}	
			}
		}
		
		public void go(){
			if (interpolation == false){
				readCommand();
			}
		
		if (interpolation == true){
			
			if(command.equals("move")){
			move(destx, desty, duration);
			}
			
			if(command.equals("scale")){
				scale(targetscale, duration);
			}
			
			if (command.equals("wait")){
				waitfor(duration);
			}
			
			if(command.equals("rotate")){
				rotate(targa, duration);
			}
			
			if(command.equals("moverot")){
				moverot(destx, desty, targa, duration);
			}
			
			if(command.equals("scalerot")){
				scalerot(targa, targetscale, duration);
			}
			
			if(command.equals("scalemove")){
				scalemove(destx, desty, targetscale, duration);
			}
			if(command.equals("smr")){
				scalemoverot(destx, desty, targetscale, a, duration);
			}
			
			if(command.equals("teleport")){
				teleport(posx, posy);
			}
			
			if(command.equals("play")){
				playsound(soundtag);
			}
			
			if(command.equals("stop")){
				stopsound(soundtag);
			}
			
		}
			
		}


		private void readCommand() {
			
			while(interpolation == false){
				
				try {
					line = fileReader.readLine();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				if (line==null){
					break;
				}
				StringTokenizer St = new StringTokenizer(line);
				
					command = St.nextToken();
						 
		if(command.equals("move")){
			posx = Integer.parseInt(St.nextToken());
			posy = Integer.parseInt(St.nextToken());
			dur = Float.parseFloat(St.nextToken());
			
			
			
			destx = posx; desty = posy; duration = dur*1000;
			starttime = System.currentTimeMillis();
			startx=x; starty=y;
			origa = a; duration = dur*1000;
			
			interpolation = true;
			
		}
		
		if (command.equals("scale")){
			
			targetscale = Float.parseFloat(St.nextToken());
			dur = Float.parseFloat(St.nextToken());
			
			s = actorImage.getScale();
			
			startscale = s; duration = dur*1000;
			starttime = System.currentTimeMillis();
			
			interpolation = true;
			
		}
		
		if (command.equals("wait")){
			dur = Float.parseFloat(St.nextToken());
			
			duration = dur*1000;
			starttime = System.currentTimeMillis();
			
			interpolation = true;
		}
		
		if (command.equals("rotate")){
			
			targa = Integer.parseInt(St.nextToken());
			dur = Float.parseFloat(St.nextToken());
			
			a = actorImage.getRotation();
			
			origa = a; duration = dur*1000;
			starttime = System.currentTimeMillis();
			
			interpolation = true;
			
		}
		
		if(command.equals("moverot")){
			posx = Integer.parseInt(St.nextToken());
			posy = Integer.parseInt(St.nextToken());
			targa = Integer.parseInt(St.nextToken());
			dur = Float.parseFloat(St.nextToken());
			
			a = actorImage.getRotation();
			destx = posx; desty = posy; duration = dur*1000;
			
			starttime = System.currentTimeMillis();
			startx=x; starty=y;
			origa = a;
			
			interpolation = true;
			
		}
		
		if(command.equals("scalemove")){
			posx = Integer.parseInt(St.nextToken());
			posy = Integer.parseInt(St.nextToken());
			targetscale = Float.parseFloat(St.nextToken());
			dur = Float.parseFloat(St.nextToken());
			
			s = actorImage.getScale();
			startscale = s;
			
			destx = posx; desty = posy; duration = dur*1000;
			starttime = System.currentTimeMillis();
			startx=x; starty=y;
			
			interpolation = true;
		
		}
		
		if(command.equals("scalerot")){
			targetscale = Float.parseFloat(St.nextToken());
			targa = Integer.parseInt(St.nextToken());
			dur = Float.parseFloat(St.nextToken());
			
			s = actorImage.getScale();
			a = actorImage.getRotation();
			
			origa = a;
			startscale = s;duration = dur*1000;
			starttime = System.currentTimeMillis();
			
			interpolation = true;
		
		}
		
		if(command.equals("smr")){
			posx = Integer.parseInt(St.nextToken());
			posy = Integer.parseInt(St.nextToken());
			targetscale = Float.parseFloat(St.nextToken());
			targa = Integer.parseInt(St.nextToken());
			dur = Float.parseFloat(St.nextToken());
			
			s = actorImage.getScale();
			a = actorImage.getRotation();
			
			origa = a;
			startscale = s;
			destx = posx; desty = posy; duration = dur*1000;
			starttime = System.currentTimeMillis();
			startx=x; starty=y;
			
			interpolation = true;
		
		}
		
		if(command.equals("teleport")){
			posx = Integer.parseInt(St.nextToken());
			posy = Integer.parseInt(St.nextToken());
			
			interpolation = true;
		}
		
		if(command.equals("play")){
			
			soundtag = Integer.parseInt(St.nextToken());
			
			interpolation = true;
		}
		if(command.equals("stop")){
			
			soundtag = Integer.parseInt(St.nextToken());
			
			interpolation = true;
		}
		
			}
			
	}

		private void move(int posx, int posy, double duration) {
			
			
			if (interpolation == true) {
				float normTime = (float) (System.currentTimeMillis() - starttime)/ (float) duration;
				
				x = (int) (startx + ((float) (destx - startx) *  normTime));
				y = (int) (starty + ((float) (desty - starty) *  normTime));
				
				if ((System.currentTimeMillis() - starttime) >= duration) {
					interpolation = false;
					x = destx; y = desty;
				}
				actorImage.translateTo(x,y);
			}
	
		}
		
		private void scale(float targetscale, double duration) {
			if (interpolation == true) {
				float normTime = (float) (System.currentTimeMillis() - starttime)/ (float) duration;
				
				s = (float) (startscale + ((float) (targetscale - startscale) *  normTime));
				
				if ((System.currentTimeMillis() - starttime) >= duration) {
					interpolation = false;
					s = targetscale;
				}
				
				actorImage.scaleTo(s);
			}
			
		}
		
		private void waitfor(double duration) {
			if (interpolation == true) {
			if ((System.currentTimeMillis() - starttime) >= duration) {
				interpolation = false;
			}
		}
	}
		
		private void rotate(double a, double duration){
			
			if (interpolation == true) {
				
				float normTime = (float) (System.currentTimeMillis() - starttime)/ (float) duration;
				
				a = (int) (origa + ((float) (targa - origa) *  normTime));
				
				if ((System.currentTimeMillis() - starttime) >= duration) {
					interpolation = false;
					a = targa;
				}
				actorImage.rotateTo(a);
			}
		
		}
		
		private void moverot(int posx, int posy, double a, double duration){
			
			if (interpolation == true) {
				float normTime = (float) (System.currentTimeMillis() - starttime)/ (float) duration;
				
				x = (int) (startx + ((float) (destx - startx) *  normTime));
				y = (int) (starty + ((float) (desty - starty) *  normTime));
				
				a = (int) (origa + ((float) (targa - origa) *  normTime));
				
				if ((System.currentTimeMillis() - starttime) >= duration) {
					interpolation = false;
					x = destx; y = desty;
					a = targa;
				}
				actorImage.translateTo(x,y);
				actorImage.rotateTo(a);
			}
	
		}
		private void scalemove(int posx, int posy, float targetscale, double duration){
			if (interpolation == true) {
				float normTime = (float) (System.currentTimeMillis() - starttime)/ (float) duration;
				
				x = (int) (startx + ((float) (destx - startx) *  normTime));
				y = (int) (starty + ((float) (desty - starty) *  normTime));
				
				s = (float) (startscale + ((float) (targetscale - startscale) *  normTime));
				
				if ((System.currentTimeMillis() - starttime) >= duration) {
					interpolation = false;
					x = destx; y = desty;
				}
				actorImage.translateTo(x,y);
				actorImage.scaleTo(s);
			}
		}

		
		private void scalemoverot(int posx, int posy, float targetscale, double a, double duration){
			if (interpolation == true) {
				float normTime = (float) (System.currentTimeMillis() - starttime)/ (float) duration;
				
				x = (int) (startx + ((float) (destx - startx) *  normTime));
				y = (int) (starty + ((float) (desty - starty) *  normTime));
				
				s = (float) (startscale + ((float) (targetscale - startscale) *  normTime));
				
				a = (int) (origa + ((float) (targa - origa) *  normTime));
				
				if ((System.currentTimeMillis() - starttime) >= duration) {
					interpolation = false;
					x = destx; y = desty;
				}
				actorImage.translateTo(x,y);
				actorImage.scaleTo(s);
				actorImage.rotateTo(a);
		}
}
		
		
		private void scalerot(double a, float targetscale, double duration){
			if (interpolation == true) {
				float normTime = (float) (System.currentTimeMillis() - starttime)/ (float) duration;
				
				
				a = (int) (origa + ((float) (targa - origa) *  normTime));
				
				s = (float) (startscale + ((float) (targetscale - startscale) *  normTime));
				
				if ((System.currentTimeMillis() - starttime) >= duration) {
					interpolation = false;
			
				}
				
				actorImage.rotateTo(a);
				actorImage.scaleTo(s);
			}
		}
		
		private void teleport(int posx, int posy){
			if (interpolation == true) {
				
				actorImage.translateTo(posx, posy);
				
				x = posx; y = posy;
				
				interpolation = false;
			}
		}
		
		private void playsound(int soundtag){
			if (interpolation == true){
				if(soundtag == 1){
					sound2.play();
				}
				if (soundtag == 2){
					//rave.play();
				}
			}
			if (soundtag == 3){
				//laser.play();
			}
			
		
		if (soundtag == 4){
			//explosion.play();
		}
				
				interpolation = false;
			}
		
		
		private void stopsound(int soundtag){
			if (interpolation == true){
				if(soundtag == 1){
					sound2.stop();
				}
				if (soundtag == 2){
					//rave.stop();
				}
				
				if (soundtag == 3){
					//laser.stop();
				}
				
				if (soundtag == 4){
					//explosion.stop();
				}
				
				interpolation = false;
			}
				
		}
		
	}



