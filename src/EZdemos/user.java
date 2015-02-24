package EZdemos;


import java.util.ArrayList;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class user {
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
	ArrayList<EZImage> image_array;
	String current_sound;
	double play_T_sound;
	boolean has_sound;
	String script_file;
	EZGroup body;
	EZGroup torso;
	EZGroup left_arm;
	EZGroup right_arm;
	EZGroup left_leg;
	EZGroup right_leg;
	EZGroup left_calf;
	EZGroup right_calf;
	EZGroup left_forearm;
	EZGroup right_forearm;
	EZGroup head;
	boolean user_mode;
	int left_shoulder;
	int right_shoulder;
	int left_elbow;
	int right_elbow;
	int left_hip;
	int right_hip;
	int left_knee;
	int right_knee;
	int head_size;
	double limb_anim_start;
	double limb_swing_period;
	double limb_swing_angle_legs;
	double limb_swing_angle_arms;
	boolean limb_swinging;
	boolean limb_swing_done;
	long start_t;
	long end;
	
	user(int x_in, int y_in, int angle, int scale, String img_filename, String script_filename) throws IOException {
		start_t = System.currentTimeMillis();
		end = 100*1000;
		user_mode = true;
		limb_swing_done = true;
		this.x = x_in;
		this.y = y_in;
		this.angle = angle;
		this.scale = scale;
		script_file = script_filename;
		has_sound = false;
		play_T_sound = System.currentTimeMillis();
		move_script = new ArrayList<ArrayList<Integer>>();
		rotate_script = new ArrayList<ArrayList<Double>>();
		scale_script = new ArrayList<ArrayList<Double>>();
		sound_script = new ArrayList<ArrayList<String>>();
		
		limb_swing_period = 2000;
		limb_swing_angle_legs = 5;
		limb_swing_angle_arms = 20;
		
		body = EZ.addGroup();
		torso = EZ.addGroup();
		left_arm = EZ.addGroup();
		right_arm = EZ.addGroup();
		left_leg = EZ.addGroup();
		right_leg = EZ.addGroup();
		head = EZ.addGroup();
		
		int head_height = head.getHeight();
		int head_width = (int)(5*head_height/3);
		int biceps_L = (int)(3*head_height);
		int biceps_W = (int)(.2*head_width);
		int thigh_L = (int)(4.5*head_height);
		int thigh_W =(int)(.4*head_width);
		int torso_L = (int)(2.8*head_height);
		int torso_W = (int)(4*head_width/3);
		
		
		body.translateTo(x, y);
		//System.out.println("body center: " + body.getWorldXCenter() + "," + body.getWorldYCenter());
		
		torso.translateTo(body.getWorldXCenter(), body.getWorldYCenter());
		//System.out.println("body center: " + body.getWorldXCenter() + "," + body.getWorldYCenter());
		
		EZRectangle torso_r = EZ.addRectangle(x, y, torso_W, torso_L, Color.magenta, false);
		EZImage torso_i = EZ.addImage("torso.png", x, y);
		torso.addElement(torso_i);
		//System.out.println("torso center: " + torso.getWorldXCenter() + "," + torso.getWorldYCenter());
		
		body.addElement(torso);
		//System.out.println("body center: " + body.getWorldXCenter() + "," + body.getWorldYCenter());
		
		//THE HEADDDDDD
		EZImage head_i = EZ.addImage("head.png", x, y - 13*torso_i.getHeight()/20);
		head.translateTo(x, y - 9*torso_i.getHeight()/20);
		head.addElement(head_i);
		body.addElement(head);
		
		//LEFT ARRRRRMMMM
		EZRectangle left_arm_r = EZ.addRectangle(x+(2*head_width/3), y-torso_L/2+biceps_L/2, biceps_W, biceps_L, Color.red, false);
		EZImage left_arm_i = EZ.addImage("left_arm.png", x+(2*head_width/3), y-torso_L/2+biceps_L/2);
		left_arm_i.translateTo((x+(torso_i.getWidth()/3))+(left_arm_i.getWidth()/2), (left_arm_i.getHeight()/2)+(y-(torso_i.getHeight()/2)));
		left_arm.translateTo((x+(torso_i.getWidth()/3)), (left_arm_i.getHeight()/2)+(y-(torso_i.getHeight()/2)));
		left_arm.addElement(left_arm_i);
		//System.out.println("L arm center: " + left_arm.getWorldXCenter() + "," + left_arm.getWorldYCenter());
		body.addElement(left_arm);
		left_arm.rotateTo(70);
		
		//RIGHT ARMMMMMM
		EZRectangle right_arm_r = EZ.addRectangle(x-(2*head_width/3), y-torso_L/2+biceps_L/2, biceps_W, biceps_L, Color.red, false);
		EZImage right_arm_i = EZ.addImage("right_arm.png", x-(2*head_width/3), y-torso_L/2+biceps_L/2);
		right_arm_i.translateTo((x-(torso_i.getWidth()/3))-(right_arm_i.getWidth()/2), (right_arm_i.getHeight()/2)+(y-(torso_i.getHeight()/2)));
		right_arm.translateTo((x-(torso_i.getWidth()/3)), (right_arm_i.getHeight()/2)+(y-(torso_i.getHeight()/2)));
		right_arm.addElement(right_arm_i);
		//System.out.println("L arm center: " + right_arm.getWorldXCenter() + "," + right_arm.getWorldYCenter());
		body.addElement(right_arm);
		right_arm.rotateTo(-70);

		
		//RIGHT LEEEEG
		EZRectangle right_leg_r = EZ.addRectangle(x-head_width/2, y+torso_L/2+thigh_L/2, thigh_W, thigh_L, Color.red, false);
		EZImage right_leg_i = EZ.addImage("right_leg.png", x-head_width/2, y+torso_L/2+thigh_L/2);
		right_leg_i.translateTo((x-(7*torso_i.getWidth()/20))+(right_leg_i.getWidth()/2), (right_leg_i.getHeight()/2)+(y+(torso_i.getHeight()/8)));
		right_leg.translateTo((x-(7*torso_i.getWidth()/20))+(right_leg_i.getWidth()/2), (y+(2*torso_i.getHeight()/10)) );
		right_leg.addElement(right_leg_i);
		//System.out.println("R leg center: " + right_leg.getWorldXCenter() + "," + right_leg.getWorldYCenter());
		body.addElement(right_leg);
		

		
		//LEFT LEEEEEG
		EZRectangle left_leg_r = EZ.addRectangle(x+head_width/2, y+torso_L/2+thigh_L/2, thigh_W, thigh_L, Color.red, false);
		EZImage left_leg_i = EZ.addImage("left_leg.png", x+head_width/2, y+torso_L/2+thigh_L/2);
		left_leg_i.translateTo((x+(2*torso_i.getWidth()/3))-(left_leg_i.getWidth()/2), (left_leg_i.getHeight()/2)+(y+(torso_i.getHeight()/8)));
		left_leg.translateTo((x+(6*torso_i.getWidth()/10))-(left_leg_i.getWidth()/2), (y+(2*torso_i.getHeight()/10)));
		left_leg.addElement(left_leg_i);
		//System.out.println("L leg center: " + left_leg.getWorldXCenter() + "," + left_leg.getWorldYCenter());
		body.addElement(left_leg);
		body.scaleTo(.75);
		
		/*//!!!
		EZCircle special = EZ.addCircle(x-torso_W/2, y-torso_L/2, 20, 20, Color.yellow, true);
		System.out.println("spec circ center: " + special.getWorldXCenter() + "," + special.getWorldYCenter());
		//!!!*/
		parse_in();
	}
	public static void main(String args[]) throws IOException {
		EZ.initialize(2000, 1300);
		EZ.setBackgroundColor(Color.black);
		user test = new user(1000, 500, 0,1,"muscle_torso.png","Kelly_script2.txt");
		while(true){
			test.update();
			EZ.refreshScreen();
		}
	}
	public void update(){
		sound_run();
		body.pullToFront();
		if(System.currentTimeMillis()>= start_t + end){
			body.hide();
		}
		if(EZInteraction.isKeyDown('s')==true){
			body.translateBy(0, 10);
			limb_swinging = true;
	    }
		if(EZInteraction.isKeyDown('w')==true){
			body.translateBy(0, -10);
			limb_swinging = true;
	    }
		if(EZInteraction.isKeyDown('d')==true){
			body.translateBy(10, 0);
			limb_swinging = true;
	    }
		if(EZInteraction.isKeyDown('a')==true){
			body.translateBy(-10, 0);
			limb_swinging = true;
	    }
		if(limb_swinging == true && limb_swing_done == true){
			limb_swing_set();
		}
		if(limb_swinging == true && limb_swing_done == false){
			limb_swing_run();
		}
		if(this.is_moving && user_mode == false){
			move_run();
		}
		else if(!this.is_moving&& user_mode == false){
			set_move();
		}
		if(this.is_rotating&& user_mode == false){
			rotate_run();
		}
		else if(!this.is_rotating&& user_mode == false){
			set_rotate();
		}
		if(this.is_scaling&& user_mode == false){
			scale_run();
		}
		else if(!this.is_scaling&& user_mode == false){
			set_scale();
		}
	}
	public void limb_swing_set(){
		limb_anim_start = System.currentTimeMillis();
		limb_swing_done = false;
	}
	public void limb_swing_run(){
		double rotate_arm_to=0;
		double rotate_leg_to=0;
		double elapsed = (System.currentTimeMillis()-limb_anim_start);
		if(elapsed <= (limb_swing_period/4)){
			rotate_arm_to = limb_swing_angle_arms*(elapsed/(limb_swing_period/4));
			rotate_leg_to = limb_swing_angle_legs*(elapsed/(limb_swing_period/4));
		}
		else if(elapsed>(limb_swing_period/4)&&elapsed<=limb_swing_period/2){
			rotate_arm_to = limb_swing_angle_arms-limb_swing_angle_arms*((elapsed-limb_swing_period/4)/(limb_swing_period/4));
			rotate_leg_to = limb_swing_angle_legs-limb_swing_angle_legs*((elapsed-limb_swing_period/4)/(limb_swing_period/4));
		}
		else if(elapsed>(limb_swing_period/2)&&elapsed<=3*limb_swing_period/4){
			rotate_arm_to = -limb_swing_angle_arms*((elapsed-limb_swing_period/2)/(limb_swing_period/4));
			rotate_leg_to = -limb_swing_angle_legs*((elapsed-limb_swing_period/2)/(limb_swing_period/4));
		}
		else if(elapsed>(3*limb_swing_period/4)&&elapsed<=limb_swing_period){
			rotate_arm_to = -limb_swing_angle_arms+limb_swing_angle_arms*((elapsed-3*limb_swing_period/4)/(limb_swing_period/4));
			rotate_leg_to = -limb_swing_angle_legs+limb_swing_angle_legs*((elapsed-3*limb_swing_period/4)/(limb_swing_period/4));
		}
		else if(elapsed>(limb_swing_period)){
			limb_swing_done = true;
			limb_swinging = false;
			
			/*
			limb_anim_start = System.currentTimeMillis();
			System.out.println("timeset");
			*/
		}
		//System.out.println("limbwave running:" + System.currentTimeMillis() + ":" + start_wave_period + ":" + rotate_arm_to + ":" +  (start_wave_period+(wave_period/2)));
		System.out.println(rotate_arm_to + "," + rotate_leg_to);
		right_leg.rotateTo(rotate_leg_to);
		left_leg.rotateTo(-rotate_leg_to);
		right_arm.rotateTo(rotate_arm_to);
		left_arm.rotateTo(-rotate_arm_to);
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
		body.translateTo(x, y);
	}
	public void rotate_run(){
		double frac = (System.currentTimeMillis()-start_T_rotate)/dur_T_rotate;
		//System.out.println(frac);
		angle = (frac*(end_angle-start_angle))+start_angle;
		if(System.currentTimeMillis()>=(start_T_rotate+dur_T_rotate)){
			angle = end_angle;
			is_rotating = false;
		}
		body.rotateTo(angle);
	}
	public void scale_run(){
		double frac = (System.currentTimeMillis()-start_T_scale)/dur_T_scale;
		scale = (frac*(end_scale-start_scale))+start_scale;
		if(System.currentTimeMillis()>=(start_T_scale+dur_T_scale)){
			scale = end_scale;
			is_scaling = false;
		}
		body.scaleTo(scale);
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
	public void parse_in(){
		try
		{
			BufferedReader input = new BufferedReader(new FileReader(script_file));
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
}
