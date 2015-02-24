package EZdemos;


import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class actor {
	int x,start_x,end_x;
	int y,start_y,end_y;
	double angle,start_angle,end_angle;
	double scale,start_scale,end_scale;
	boolean is_moving;
	boolean is_rotating;
	boolean is_spinning;
	boolean is_scaling;
	double start_T_move,dur_T_move;
	double start_T_rotate,dur_T_rotate;
	double start_T_scale,dur_T_scale;
	ArrayList<ArrayList<Integer>> move_script;
	ArrayList<ArrayList<Double>> rotate_script;
	ArrayList<ArrayList<Double>> scale_script;
	ArrayList<ArrayList<String>> sound_script;
	ArrayList<EZImage> image_array;
	String current_sound;
	double play_T_sound;
	boolean has_sound;
	EZGroup image_group;
	String script_filename;
	double spin_rate;
	
	actor(int x_in, int y_in, int angle_in, int scale_in, String img_filename, String script) throws IOException {
		this.x = x_in;
		this.x = x_in;
		this.y = y_in;
		this.angle = angle_in;
		this.scale = scale_in;
		image_group = EZ.addGroup();
		has_sound = false;
		play_T_sound = System.currentTimeMillis();
		move_script = new ArrayList<ArrayList<Integer>>();
		rotate_script = new ArrayList<ArrayList<Double>>();
		scale_script = new ArrayList<ArrayList<Double>>();
		sound_script = new ArrayList<ArrayList<String>>();
		script_filename = script;
		if(img_filename.matches("\\D+\\d$")){
			//System.out.println("it's good");
		}
		else{
			EZImage to_add = EZ.addImage(img_filename, x, y);
			image_group.translateTo(x, y);
			image_group.addElement(to_add);
		}
		is_spinning = false;
		parse_in();
	}
	actor(int x_in, int y_in, int angle_in, int scale_in, String img_filename, String script,double spin_rate_in) throws IOException {
		this.x = x_in;
		this.y = y_in;
		this.angle = angle_in;
		this.scale = scale_in;
		image_group = EZ.addGroup();
		has_sound = false;
		play_T_sound = System.currentTimeMillis();
		move_script = new ArrayList<ArrayList<Integer>>();
		rotate_script = new ArrayList<ArrayList<Double>>();
		scale_script = new ArrayList<ArrayList<Double>>();
		sound_script = new ArrayList<ArrayList<String>>();
		script_filename = script;
		if(img_filename.matches("\\D+\\d$")){
			//System.out.println("it's good");
		}
		else{
			EZImage to_add = EZ.addImage(img_filename, x, y);
			image_group.translateTo(x, y);
			image_group.addElement(to_add);
		}
		spin_rate = spin_rate_in;
		is_spinning = true;
		parse_in();
	}
	public void update(){
		sound_run();
		image_group.pushToBack();
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
		if(this.is_spinning){
			spin_run();
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
			//System.out.println("move set: " + end_x + "," + end_y + "," + dur_T_move);
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
	public void teleport(){
		image_group.translateTo(end_x, end_y);
	}
	public void move_run(){
		if(dur_T_move == 0){
			//System.out.println("TELEPORT: " + end_x + "," + end_y + "," + dur_T_move);
			x = end_x;
			y = end_y;
			image_group.translateTo(x, y);
			is_moving = false;
			return;
		}
		else if(end_x <0 && end_y<0){
			//System.out.println("WAIT: " + x + "," + y + "," + dur_T_move);
			image_group.translateTo(x, y);
			if(System.currentTimeMillis()>=(start_T_move+dur_T_move)){
				is_moving = false;
			}
		}
		else{
			double frac = (System.currentTimeMillis()-start_T_move)/dur_T_move;
			x = (int)(frac*(end_x-start_x))+start_x;
			y = (int)(frac*(end_y-start_y))+start_y;
			//System.out.println((int)(System.currentTimeMillis()-start_T_move)/1000 + ":" + x + "," + y);
			if(System.currentTimeMillis()>=(start_T_move+dur_T_move)){
				x=end_x;
				y=end_y;
				is_moving = false;
			}
			image_group.translateTo(x, y);
		}
	}
	public void rotate_run(){
		double frac = (System.currentTimeMillis()-start_T_rotate)/dur_T_rotate;
		//System.out.println(frac);
		angle = (frac*(end_angle-start_angle))+start_angle;
		if(System.currentTimeMillis()>=(start_T_rotate+dur_T_rotate)){
			angle = end_angle;
			is_rotating = false;
		}
		image_group.rotateTo(angle);
	}
	public void scale_run(){
		double frac = (System.currentTimeMillis()-start_T_scale)/dur_T_scale;
		scale = (frac*(end_scale-start_scale))+start_scale;
		if(System.currentTimeMillis()>=(start_T_scale+dur_T_scale)){
			scale = end_scale;
			is_scaling = false;
		}
		image_group.scaleTo(scale);
	}
	public void set_sound(){
		if(!sound_script.isEmpty()){
			//System.out.println("check set");
			ArrayList<String> params = sound_script.remove(0);
			play_T_sound = play_T_sound + Double.valueOf(params.get(0))*1000;
			current_sound = params.get(1);
			has_sound = true;
			//System.out.println("SOUND SET:   " + params.get(1) + " , " + params.get(0) + " , " + + play_T_sound + " , " + current_sound);
		}
	}
	public void sound_run(){
		if(System.currentTimeMillis() >= play_T_sound && has_sound == true){
			EZSound play_sound = EZ.addSound(current_sound);
			play_sound.play();
			has_sound = false;
			//System.out.println("SOUND PLAY@" + System.currentTimeMillis());
		}
		else if(has_sound == false){
			set_sound();
		}
	}
	public void parse_in(){
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
				if(tokens[0].equals("TELEPORT")){
					ArrayList<Integer> add_params = new ArrayList<Integer>();
					for(int i = 1;i<tokens.length;i++){
						add_params.add(Integer.valueOf(tokens[i]));
						//System.out.println("printing split: " + i + "\n" + tokens[i]);
					}
					add_params.add(2,0);
					/*
					for(int i = 0;i<add_params.size();i++){
						System.out.println("printing TELEPORT: " + i + ":" + add_params.get(i));
					}*/
					move_script.add(add_params);
				}
				if(tokens[0].equals("WAIT")){
					ArrayList<Integer> add_params = new ArrayList<Integer>();
					add_params.add(0, -1);
					add_params.add(1, -1);
					add_params.add(2,Integer.valueOf(tokens[1]));
					/*
					for(int i = 0;i<add_params.size();i++){
						System.out.println("printing WAIT: " + i + ":" + add_params.get(i));
					}*/
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
		String to_print = "";
		for(int i = 0;i<move_script.size();i++){
			for(int n=0;n<move_script.get(i).size();n++){
				to_print = to_print + String.valueOf(move_script.get(i).get(n));
				if(n<move_script.get(i).size()-1){
					to_print = to_print + ",";
				}
			}
			to_print = to_print + "\n";
		}
		//System.out.println(to_print);
	}
	public void spin_run(){
		image_group.rotateBy(spin_rate);
	}
}
