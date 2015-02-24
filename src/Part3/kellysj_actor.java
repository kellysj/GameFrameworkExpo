package Part3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class kellysj_actor {

	EZImage image;
	int x,start_x,end_x;
	int y,start_y,end_y;
	double angle,start_angle,end_angle;
	double scale,start_scale,end_scale;
	boolean is_moving;
	boolean is_rotating;
	boolean is_scaling;
	double start_T_move,dur_T_move;
	double start_T_rotate,dur_T_rotate;
	double start_T_scale,dur_T_scale;
	ArrayList<ArrayList<Integer>> move_script;
	ArrayList<ArrayList<Double>> rotate_script;
	ArrayList<ArrayList<Double>> scale_script;
	ArrayList<ArrayList<String>> sound_script;
	String current_sound;
	double play_T_sound;
	boolean has_sound;
	
	kellysj_actor(int x, int y, int angle, int scale, String img_filename, String script_filename) throws IOException {
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.scale = scale;
		has_sound = false;
		play_T_sound = System.currentTimeMillis();
		image = EZ.addImage(img_filename,this.x,this.y);
		move_script = new ArrayList<ArrayList<Integer>>();
		rotate_script = new ArrayList<ArrayList<Double>>();
		scale_script = new ArrayList<ArrayList<Double>>();
		sound_script = new ArrayList<ArrayList<String>>();
		try
		{
			BufferedReader input = new BufferedReader(new FileReader(script_filename));
			String out;
			while((out = input.readLine()) != null){
				//System.out.println(out + "\n" + n);
				String delims = "[ ,]+"; //change this depending on the stuff they give you!!!
				String[] tokens = out.split(delims);
				if(tokens[0].equals("MOVE")){
					ArrayList<Integer> add_params = new ArrayList<Integer>();
					for(int i = 1;i<tokens.length;i++){
						add_params.add(Integer.valueOf(tokens[i]));
						//System.out.println("printing split: " + i + "\n" + tokens[i]);
					}
					move_script.add(add_params);
				}
				else if(tokens[0].equals("ROTATE")){
					ArrayList<Double> add_params = new ArrayList<Double>();
					for(int i = 1;i<tokens.length;i++){
						add_params.add(Double.valueOf((tokens[i])));
						//System.out.println("printing split: " + i + "\n" + tokens[i]);
					}
					rotate_script.add(add_params);
				}
				else if(tokens[0].equals("SCALE")){
					ArrayList<Double> add_params = new ArrayList<Double>();
					for(int i = 1;i<tokens.length;i++){
						add_params.add(Double.valueOf((tokens[i])));
						//System.out.println("printing split: " + i + "\n" + tokens[i]);
					}
					scale_script.add(add_params);
				}
				else if(tokens[0].equals("SOUND")){
					ArrayList<String> add_params = new ArrayList<String>();
					for(int i = 1;i<tokens.length;i++){
						add_params.add((tokens[i]));
						//System.out.println("printing split: " + i + "\n" + tokens[i]);
					}
					sound_script.add(add_params);
				}
			}
			input.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		/*
		//for(int i=0;i < move_script.size();i++){
		while(sound_script.isEmpty() == false){
			System.out.println("SOME DIGITS YO: ");
			ArrayList<String> inner = sound_script.remove(0);
			for(int n=0; n<inner.size();n++){
				System.out.println(inner.get(n));
			}
		}
		if(sound_script.isEmpty() == true){

				System.out.println("it empty son");

		}*/
		
		
		
	}
	public void update(){
		sound_run();
		if(this.is_moving){
			move_run();
		}
		else if(!this.is_moving){
			set_move();
		}
		if(this.is_rotating){
			rotate_run();
		}
		else if(!this.is_rotating){
			set_rotate();
		}
		if(this.is_scaling){
			scale_run();
		}
		else if(!this.is_scaling){
			set_scale();
		}
	}
	public void set_move(){
		if(!move_script.isEmpty()){
			ArrayList<Integer> params = move_script.remove(0);
			start_x = x;
			start_y = y;
			end_y = params.get(1);
			end_x = params.get(0);
			dur_T_move = params.get(2)*1000;
			start_T_move = System.currentTimeMillis();
			is_moving = true;
		}
	}
	public void set_rotate(){
		if(!rotate_script.isEmpty()){
			ArrayList<Double> params = rotate_script.remove(0);
			start_angle = angle;
			end_angle = params.get(0);
			start_T_rotate = System.currentTimeMillis();
			dur_T_rotate = params.get(1)*1000;
			is_rotating = true;
		}
	}
	public void set_scale(){
		if(!scale_script.isEmpty()){
			ArrayList<Double> params = scale_script.remove(0);
			start_scale = scale;
			end_scale = params.get(0);
			start_T_scale = System.currentTimeMillis();
			dur_T_scale = params.get(1)*1000;
			is_scaling = true;
		}
	}
	public void move_run(){
		double frac = (System.currentTimeMillis()-start_T_move)/dur_T_move;
		x = (int)(frac*(end_x-start_x))+start_x;
		y = (int)(frac*(end_y-start_y))+start_y;
		//System.out.println((int)(System.currentTimeMillis()-start_T_move)/1000 + ":" + x + "," + y);
		if(System.currentTimeMillis()>=(start_T_move+dur_T_move)){
			x=end_x;
			y=end_y;
			is_moving = false;
		}
		image.translateTo(x, y);
	}
	public void rotate_run(){
		double frac = (System.currentTimeMillis()-start_T_rotate)/dur_T_rotate;
		//System.out.println(frac);
		angle = (frac*(end_angle-start_angle))+start_angle;
		if(System.currentTimeMillis()>=(start_T_rotate+dur_T_rotate)){
			angle = end_angle;
			is_rotating = false;
		}
		image.rotateTo(angle);
	}
	public void scale_run(){
		double frac = (System.currentTimeMillis()-start_T_scale)/dur_T_scale;
		scale = (frac*(end_scale-start_scale))+start_scale;
		if(System.currentTimeMillis()>=(start_T_scale+dur_T_scale)){
			scale = end_scale;
			is_scaling = false;
		}
		image.scaleTo(scale);
	}
	public void set_sound(){
		if(!sound_script.isEmpty()){
			System.out.println("check set");
			ArrayList<String> params = sound_script.remove(0);
			play_T_sound = play_T_sound + Double.valueOf(params.get(0))*1000;
			current_sound = params.get(1);
			has_sound = true;
			System.out.println("SOUND SET:   " + params.get(1) + " , " + params.get(0) + " , " + + play_T_sound + " , " + current_sound);
		}
	}
	public void sound_run(){
		if(System.currentTimeMillis() >= play_T_sound && has_sound == true){
			EZSound play_sound = EZ.addSound(current_sound);
			play_sound.play();
			has_sound = false;
			System.out.println("SOUND PLAY@" + System.currentTimeMillis());
		}
		else if(has_sound == false){
			set_sound();
		}
	}
}
