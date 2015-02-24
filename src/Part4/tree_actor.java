package Part4;
import java.lang.Math;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
public class tree_actor {
	int start_node;
	int start_length;
	int start_angle;
	int x_base;
	int y_base;
	int trunk_length;
	int trunk_thickness;
	Color primary_color;    //Color of trunk/branches
	Color secondary_color;//Color of leaves
	Color tertiary_color;    //Color of super leaves
	int thickness;
	int limb_thick_delta;
	int limb_angle_delta;
	double leaf_node_precentage_2;
	double leaf_node_precentage_3;
	int node_count;
	boolean is_generated;
	boolean is_drawn;
	boolean has_fruit;
	boolean is_waving;
	boolean is_burning;
	boolean draw_run_finished;
	String bark_image_file;
	String leaf_image_file;
	String fruit_image_file;
	ArrayList<ArrayList<Integer>> fruit_node;
	ArrayList<ArrayList<ArrayList<Integer>>> limb_store;
	ArrayList<ArrayList<ArrayList<Integer>>> temp_limb_store;
	int id_num;
	private EZGroup wave_0;
	private EZGroup wave_1;
	private EZGroup trunk_g;
	EZGroup tree_g;
	long start_wave_period;
	long wave_period;
	double wave_angle;
	boolean image_load;
	long render_interval;
	long render_start;
	EZCircle test_circ;
	
	tree_actor(int x, int y, String bark_image_name,String leaf_image_name, String fruit_image_name, boolean image_on){
		id_num = 0;
		x_base = x;
		y_base = y;
		start_length = 100;
		start_angle = 40;
		thickness = 15;
		start_node = 10;
		trunk_length = 400;
		trunk_thickness = 15;
		node_count = start_node;
		primary_color = Color.CYAN;    //Color of trunk/branches
		secondary_color = Color.MAGENTA;//Color of leaves
		tertiary_color = Color.GREEN;    //Color of super leaves
		limb_thick_delta = -3;
		limb_angle_delta = 3;
		leaf_node_precentage_2 = 0.6;
		leaf_node_precentage_3 = 0.2;
		is_generated = false;
		is_drawn = false;
		has_fruit = false;
		bark_image_file = bark_image_name;
		leaf_image_file = leaf_image_name;
		fruit_image_file = fruit_image_name;
		image_load = image_on;
		fruit_node = new ArrayList<ArrayList<Integer>>(0);
		limb_store = new ArrayList<ArrayList<ArrayList<Integer>>>(0);
		draw_run_finished = false;
		render_interval = 0;
		render_start = System.currentTimeMillis();
		wave_0 = EZ.addGroup();
		wave_1 = EZ.addGroup();
		trunk_g = EZ.addGroup();
		tree_g = EZ.addGroup();
		tree_g.addElement(wave_0);
		tree_g.addElement(wave_1);
		tree_g.addElement(trunk_g);
		this.tree_init();
	}
	public static void main(String args[]){
		EZ.initialize(2000, 1000);
		EZ.setBackgroundColor(Color.black);
		tree_actor tree_test = new tree_actor(EZ.getWindowWidth()/2,9*(EZ.getWindowHeight()/10), "bark_small.png","leaf.png","orange.png",true);
		while(true){
			tree_test.go();
			EZ.refreshScreen();
		}
	}
	
	public void go(){
		/*
		test_circ.pushBackOneLayer();
		test_circ.pushToBack();
		test_circ.pushBackOneLayer();
		wave_0.pushBackOneLayer();
		wave_1.pushBackOneLayer();
		trunk_g.pushBackOneLayer();
		wave_0.pushBackOneLayer();
		wave_1.pushBackOneLayer();
		trunk_g.pushBackOneLayer();
		trunk_g.pushBackOneLayer();
		*/
		tree_g.pushBackOneLayer();
		//tree_g.pushBackOneLayer();
		if(is_drawn == false){
			this.draw_run();
		}
		if(is_drawn == true && has_fruit == false){
			this.addfruit(9*(start_node/10), 15, 30);
		}
		if(is_drawn == true && has_fruit == true && is_waving == false){
			limbwave_set(5,5000);
		}
		if(is_waving == true){
			limbwave_run();
		}
	}

	public void limbwave_set(double degrees, long period){
		System.out.println("limbwave set");
		start_wave_period = System.currentTimeMillis();
		wave_period = period;
		wave_angle = degrees;
		is_waving = true;
	}
	public void limbwave_run(){
		double rotate_arm_to=0;
		long elapsed = System.currentTimeMillis()-start_wave_period;
		if(elapsed <= (wave_period/2)){
			rotate_arm_to = wave_angle*((double)elapsed/((double)wave_period/2));
		}
		else if(elapsed>(wave_period/2)&&elapsed<=wave_period){
			rotate_arm_to = wave_angle-wave_angle*(((double)elapsed-(double)wave_period/2)/((double)wave_period/2));
		}
		else if(elapsed>(wave_period)){
			start_wave_period = System.currentTimeMillis();
			System.out.println("timeset");
		}
		//System.out.println("limbwave running:" + System.currentTimeMillis() + ":" + start_wave_period + ":" + rotate_arm_to + ":" +  (start_wave_period+(wave_period/2)));
		wave_0.rotateTo(rotate_arm_to);
		wave_1.rotateTo(-rotate_arm_to);
	}
	public void tree_init(){
		wave_0.translateTo(x_base, y_base-trunk_length);
		wave_1.translateTo(x_base, y_base-trunk_length);
		this.trunk_GEN();
		temp_limb_store = limb_store_backup(limb_store);
		is_generated = true;
		node_count = 0;
		//limb_store_print(limb_store);
	}
	//update loop does most of the work
	public void draw_run(){
		//System.out.println("TIMES: " + System.currentTimeMillis() + ":" + render_start+render_interval);
		if(System.currentTimeMillis()>=render_start+render_interval){
			if(temp_limb_store.get(node_count).size()==0 && node_count == temp_limb_store.size()-1){
				is_drawn = true;
				temp_limb_store = limb_store_backup(limb_store);
			}
			else if(temp_limb_store.get(node_count).size()==0){
				node_count++;
			}
			else{
				limb_draw(temp_limb_store.get(node_count).remove(0));
				if(temp_limb_store.get(node_count).size()>0){
					limb_draw(temp_limb_store.get(node_count).remove(temp_limb_store.get(node_count).size()-1));
				}
			}
			render_start = System.currentTimeMillis();
		}
		else{
			return;
		}
	}
	public void trunk_GEN(){
		int trunk_length = start_length*4;
		int trunk_thickness = thickness;
		ArrayList<Integer> temp_limb = new ArrayList<Integer>();
		temp_limb.add(0, (int)(start_node*1.5)); //int node_num,
		temp_limb.add(1, 0);//int limb_angle, 
		temp_limb.add(2, x_base);//int start_x, 
		temp_limb.add(3, y_base);//int start_y, 
		temp_limb.add(4, x_base);
		temp_limb.add(5, y_base-trunk_length);
		temp_limb.add(6, trunk_thickness);//int limb_thickness
		temp_limb.add(7, trunk_length);
		limb_store_add((0), temp_limb);
		id_num = -1;
		//rec_limb_GEN((start_node/5),180-(-start_angle),x_base,y_base,thickness,"-2", -2);
		//rec_limb_GEN((start_node/5),180-(start_angle),x_base,(y_base,thickness,"-1", -1);
		rec_limb_GEN(start_node,(-start_angle),x_base,(y_base-trunk_length),thickness,"0", 0);
		rec_limb_GEN(start_node,(start_angle),x_base,(y_base-trunk_length),thickness,"1", 1);
	}
	
	public void rec_limb_GEN(int node_num, int limb_angle, int start_x, int start_y, int limb_thickness, String id_string, int group_num){
		ArrayList<Integer> temp_limb = new ArrayList<Integer>();
		//PRE RENDER CALCULATIONS - make the limb stuff easier to read
		int limb_length = (int)((double)start_length*((double)node_num/(double)start_node));
		int end_x = start_x-(int)(limb_length*Math.sin(Math.toRadians(limb_angle))); //These two ints create end cord for the addline
		int end_y = start_y-(int)(limb_length*Math.cos(Math.toRadians(limb_angle))); //by using trig functions on the supplied angle
		//ANGLE CALCULATION
		int limb_angle1 = limb_angle-(start_angle+(limb_angle_delta*node_num))/2;
		int limb_angle2 = limb_angle+(start_angle+(limb_angle_delta*node_num))/2;
		//System.out.println(node_num + "\nangle: " + limb_angle + "\nstart_x: " + start_x + ":" + (limb_length) + "\nstart_y: " + start_y + ":" + (limb_length) + "\nend_x: " + end_x + "\nend_y: " + end_y);
		//RECURSIVE METHOD STARTS HERE
		if(group_num>=0){
			id_num++;
		}
		else if(group_num<0){
			id_num--;
		}
		temp_limb.add(0, node_num); //int node_num,
		temp_limb.add(1, limb_angle);//int limb_angle, 
		temp_limb.add(2, start_x);//int start_x, 
		temp_limb.add(3, start_y);//int start_y, 
		temp_limb.add(4, end_x);
		temp_limb.add(5, end_y);
		temp_limb.add(6, limb_thickness);//int limb_thickness
		temp_limb.add(7, limb_length); //int limb length
		temp_limb.add(8, group_num);
		temp_limb.add(9, id_num);
		if(group_num<0){
			limb_store_add(0, temp_limb);
		}
		else{
			limb_store_add((start_node-node_num), temp_limb);
		}
		//EZ.addLine(start_x, start_y, end_x, end_y, limb_color, limb_thickness);
		id_string = id_num + "," + id_string;
		node_num = node_num-1;	
		if(node_num>0){
			double rand_n = Math.random();
			if(rand_n >=.8 && node_num>= (start_node/8)){
				double rand_n2 = Math.random();
				//System.out.println("Case1: " + rand_n + ":" + rand_n2);
				if(rand_n2 < 0.5)
				{
					rec_limb_GEN(node_num,limb_angle1,end_x,end_y,limb_thickness+limb_thick_delta,id_string,group_num);
					//System.out.println("Limb Angle: " + limb_angle1);
				}
				else if(rand_n2>=0.5){
					rec_limb_GEN(node_num,limb_angle2,end_x,end_y,limb_thickness+limb_thick_delta,id_string,group_num);
					//System.out.println("Limb Angle: " + limb_angle2);
				}
				else{
					rec_limb_GEN(node_num,limb_angle1,end_x,end_y,limb_thickness+limb_thick_delta,id_string,group_num);
					rec_limb_GEN(node_num,limb_angle2,end_x,end_y,limb_thickness+limb_thick_delta,id_string,group_num);
					//System.out.println("THINGS JUST GOT WEIRD - Limb Angle: " + limb_angle1 + "," + limb_angle2);
				}
			}
			else{
				rec_limb_GEN(node_num,limb_angle1,end_x,end_y,limb_thickness+limb_thick_delta,id_string,group_num);
				rec_limb_GEN(node_num,limb_angle2,end_x,end_y,limb_thickness+limb_thick_delta,id_string,group_num);
			}
		}
	}
	
	public void addfruit(int fruit_node_level, int num_fruit, int fruit_size){
			for(int i = 0;i<=num_fruit;i++){
				int rand_cord = (int)(Math.random()*temp_limb_store.get(fruit_node_level).size());
				//System.out.println(rand_cord);
				ArrayList<Integer> temp_cord = temp_limb_store.get(fruit_node_level).remove(rand_cord);
				//System.out.println("fruitadd: " + fruit_node.size() + ":" + temp_cord.get(0) + ":" + temp_cord.get(1) + ":" + rand_cord);
				if(image_load = true){
					EZImage fruit = EZ.addImage(fruit_image_file, EZ.getWindowWidth() + 1000, EZ.getWindowHeight()+1000);
					fruit.scaleTo((float)fruit_size/((float)fruit.getHeight()));
					fruit.translateTo(temp_cord.get(2), temp_cord.get(3));
					if(temp_cord.get(8)==0){
						wave_0.addElement(fruit);
					}
					if(temp_cord.get(8)==1){
						wave_1.addElement(fruit);
					}
					
				}
				else{
					EZCircle fruit = EZ.addCircle(temp_cord.get(2), temp_cord.get(3), fruit_size, fruit_size, Color.ORANGE, true);
					if(temp_cord.get(8)==0){
						wave_0.addElement(fruit);
					}
					if(temp_cord.get(8)==1){
						wave_1.addElement(fruit);
					}
				}
			}
			has_fruit = true;
	}
	
	//method overload with image string to put in
	public void limb_draw(ArrayList<Integer> temp_limb){
		long duration1 = System.currentTimeMillis();
		int node_num = temp_limb.get(0); //int node_num,
		int limb_angle = temp_limb.get(1);//int limb_angle, 
		int start_x = temp_limb.get(2);//int start_x, 
		int start_y = temp_limb.get(3);//int start_y, 
		int end_x = temp_limb.get(4);
		int end_y = temp_limb.get(5);
		int limb_thickness = temp_limb.get(6);//int limb_thickness
		int limb_length = temp_limb.get(7);
		int group = temp_limb.get(8);
		int id_num = temp_limb.get(9);
		//System.out.println("\nnodenum:" + node_num + "\nlimbang:" + limb_angle + "\nstartx:" + start_x + "\nstarty" + start_y + "\nendx:" + end_x + "\nendy:" + end_y + "\nthickness:" + limb_thickness + "\nlength:" + limb_length);
		//System.out.println((double)limb_length/(double)limb.getHeight() + ":" + scale);
		//System.out.println("DRAWN: " + node_num + "," + limb_angle + "," + start_x + "," + start_y + "," + end_x + "," + end_y + "," + limb_thickness + "," + limb_length + "," + scale);
		if(node_num == (int)(start_node*1.5)){
			if(image_load == true){
				EZImage trunk_start = EZ.addImage(bark_image_file, EZ.getWindowWidth()+1000, EZ.getWindowHeight()+1000);
				trunk_start.pushBackOneLayer();
				trunk_start.pushBackOneLayer();
				trunk_g.addElement(trunk_start);
				int x_cent = (int)((double)(end_x-start_x)/2)+start_x;
				trunk_start.translateTo(x_cent, start_y-((trunk_start.getHeight())/2));
				trunk_g.addElement(trunk_start);
				int top = start_y-((trunk_start.getHeight()));
				int trunk_num = (limb_length/trunk_start.getHeight())-1;
				if(limb_length%trunk_start.getHeight()!=0){
					trunk_num++;
				}
				//System.out.println(trunk_num);
				double scale = trunk_start.getScale();
				double del_s = 0.2;
				for(int i = 0;i<=trunk_num;i++){
					scale = scale - del_s;
					int y_cent = (top - (int)(trunk_start.getHeight()*scale)/2);
					if(image_load == true){
						EZImage trunk = EZ.addImage(bark_image_file,EZ.getWindowWidth()+1000, EZ.getWindowHeight()+1000);
						trunk.pushBackOneLayer();
						trunk.pushBackOneLayer();
						trunk_g.addElement(trunk);
						top = top - (int)(trunk_start.getHeight()*scale);
						if(top<end_y){
							trunk.translateTo(x_cent,end_y+(int)(trunk_start.getHeight()*(scale))/2);
							trunk.scaleTo(scale+del_s);
							return;
						}
						trunk.scaleTo(scale);
						trunk.translateTo(x_cent, y_cent);
					}
				}
			}
			else{
				Color limb_color;
				if(((double)node_num/(double)start_node)<= leaf_node_precentage_3){
					limb_color = tertiary_color;
				}
				else if(((double)node_num/(double)start_node)<= leaf_node_precentage_2){
					limb_color = secondary_color;
				}
				else{
					limb_color = primary_color;
				}
				EZ.addLine(start_x, start_y, end_x, end_y, limb_color, limb_thickness);
				EZ.addLine(start_x, start_y, end_x, end_y, limb_color, limb_thickness);
			}
			//System.out.println((double)limb_length/(double)limb.getHeight() + ":" + scale);
		}
		if(node_num <=3){
			if(image_load==true){
				EZImage leaf = EZ.addImage(leaf_image_file, EZ.getWindowWidth()+1000, EZ.getWindowHeight()+1000);
				leaf.pushBackOneLayer();
				leaf.pushBackOneLayer();
				leaf.translateTo((int)((double)(end_x-start_x)/2)+start_x, (int)((double)(end_y-start_y)/2)+start_y);
				leaf.rotateTo(-limb_angle);
				if(group == 0){
					wave_0.addElement(leaf);
				}
				else if(group == 1){
					wave_1.addElement(leaf);
				}
				else{
					trunk_g.addElement(leaf);
				}
			}
			else{
				EZCircle leaf = EZ.addCircle((int)((double)(end_x-start_x)/2)+start_x, (int)((double)(end_y-start_y)/2)+start_y, 15, 15, tertiary_color, true);
				if(group == 0){
					wave_0.addElement(leaf);
				}
				else if(group == 1){
					wave_1.addElement(leaf);
				}
				else{
					trunk_g.addElement(leaf);
				}
			}

		}
		else{
			if(image_load==true){
				EZImage limb = EZ.addImage(bark_image_file, EZ.getWindowWidth()+1000, EZ.getWindowHeight()+1000);
				limb.pushBackOneLayer();
				limb.pushBackOneLayer();
				double scale =(double)limb_length/(double)limb.getHeight();
				limb.scaleTo(scale);
				limb.rotateTo(-limb_angle);
				limb.translateTo((int)((double)(end_x-start_x)/2)+start_x, (int)((double)(end_y-start_y)/2)+start_y);
				if(group == 0){
					wave_0.addElement(limb);
				}
				else if(group == 1){
					wave_1.addElement(limb);
				}
				else{
					trunk_g.addElement(limb);
				}
			}
			else{
				Color limb_color;
				if(((double)node_num/(double)start_node)<= leaf_node_precentage_3){
					limb_color = tertiary_color;
				}
				else if(((double)node_num/(double)start_node)<= leaf_node_precentage_2){
					limb_color = secondary_color;
				}
				else{
					limb_color = primary_color;
				}
				EZLine line_added = EZ.addLine(start_x, start_y, end_x, end_y, limb_color, limb_thickness);
				if(group == 0){
					wave_0.addElement(line_added);
				}
				else if(group == 1){
					wave_1.addElement(line_added);
				}
				else{
					trunk_g.addElement(line_added);
				}
				
			}
			
			//System.out.println("DRAWN: " + node_num + "," + limb_angle + "," + start_x + "," + start_y + "," + end_x + "," + end_y + "," + limb_thickness + "," + limb_length + "," + scale);
		}
		//System.out.println("DRAWN: " + node_num + "," + limb_angle + "," + start_x + "," + start_y + "," + end_x + "," + end_y + "," + limb_thickness + "," + limb_length + "," + group + "," + id_num);
		duration1 = System.currentTimeMillis()-duration1;
		//System.out.println("TIME total draw: " + duration1);
	}
	//adds something to the 3D array
	public void limb_store_add(int node_level, ArrayList<Integer> params_in){
		ArrayList<ArrayList<Integer>> temp_node_list = new ArrayList<ArrayList<Integer>>();
		if(limb_store.size()>node_level){
			temp_node_list = limb_store.remove(node_level);
		}
		temp_node_list.add(params_in);
		limb_store.add(node_level,temp_node_list);
	}
	//returns a deep clone of the 3d list
	public ArrayList<ArrayList<ArrayList<Integer>>> limb_store_backup(ArrayList<ArrayList<ArrayList<Integer>>> source){
		ArrayList<ArrayList<ArrayList<Integer>>> dest = new ArrayList<ArrayList<ArrayList<Integer>>>(source.size());
		for(int i =0;i<source.size();i++){
			ArrayList<ArrayList<Integer>> temp_limbs = new ArrayList<ArrayList<Integer>>(source.get(i).size());
			for(int n=0;n<source.get(i).size();n++){
				String to_print = i + ":" + n + ": ";
				ArrayList<Integer> params_in = new ArrayList<Integer>(source.get(i).get(n));
				for(int j=0; j<source.get(i).get(n).size();j++){
					params_in.add(source.get(i).get(n).get(j));
					/*to_print = to_print + source.get(i).get(n).get(j);
					if(j<source.get(i).get(n).size()-2){
						to_print = to_print + ",";
					}
					*/
				}
				to_print = "";
				for(int j=0; j<params_in.size();j++){
					
					to_print = to_print + params_in.get(j);
					if(j<params_in.size()-2){
						to_print = to_print + ",";
					}
				}
				//System.out.println(to_print);
				temp_limbs.add(n,params_in);
			}
			dest.add(i, temp_limbs);
		}
		return dest;
	}
	//print a large 3d array
	
	public void limb_store_print(ArrayList<ArrayList<ArrayList<Integer>>> print_in){
		for(int i = 0; i<print_in.size();i++){
			int count = 0;
			//System.out.println("limb_store size:" + limb_store.size());
			ArrayList<ArrayList<Integer>> temp_inner = print_in.get(i);
			for(int n = 0;n<temp_inner.size();n++){
				//System.out.println("node list size:" + temp_inner.size());
				ArrayList<Integer> temp_params = temp_inner.get(n);
				String to_print = i + ":" + n + ":";
				for(int p = 0;p<temp_params.size();p++){
					//System.out.println("params size:" + temp_params.size() + "," + p);
					to_print = to_print + temp_params.get(p);
					if(p<temp_params.size()-1){
						to_print = to_print + ",";
					}
				}
				System.out.println(to_print);
			}
		}
	}
	//
	//OLD METHODS START HERE!!!
	//
	//old limb gen for main method
	public void rec_limb(int node_num, int limb_angle, int start_x, int start_y, int limb_thickness){
		//PRE RENDER CALCULATIONS - make the limb stuff easier to read
		int limb_length = (int)((double)start_length*((double)node_num/(double)start_node));
		int end_x = start_x-(int)(limb_length*Math.sin(Math.toRadians(limb_angle))); //These two ints create end cord for the addline
		int end_y = start_y-(int)(limb_length*Math.cos(Math.toRadians(limb_angle))); //by using trig functions on the supplied angle
		//COLOR SELECTION - based on node count to sorta look like leaves
		Color limb_color;
		if(((double)node_num/(double)start_node)<= leaf_node_precentage_3){
			limb_color = tertiary_color;
		}
		else if(((double)node_num/(double)start_node)<= leaf_node_precentage_2){
			limb_color = secondary_color;
		}
		else{
			limb_color = primary_color;
		}
		//System.out.println(node_num + "\nangle: " + limb_angle + "\nstart_x: " + start_x + ":" + (limb_length) + "\nstart_y: " + start_y + ":" + (limb_length) + "\nend_x: " + end_x + "\nend_y: " + end_y);
		//RECURSIVE METHOD STARTS HERE
		EZ.addLine(start_x, start_y, end_x, end_y, limb_color, limb_thickness);
		node_num = node_num-1;
		if(node_num>0){
			double rand_n = Math.random();
			if(rand_n >=.6 && node_num>= (start_node/5)){
				double rand_n2 = Math.random();
				//System.out.println("Case1: " + rand_n + ":" + rand_n2);
				if(rand_n2 <= 0.4999999)
				{
					rec_limb(node_num,limb_angle-(start_angle+(limb_angle_delta*node_num))/2,end_x,end_y,limb_thickness+limb_thick_delta);
				}
				else if(rand_n2>=0.5){
					rec_limb(node_num,limb_angle+(start_angle+(limb_angle_delta*node_num))/4,end_x,end_y,limb_thickness+limb_thick_delta);
				}
				else{
					rec_limb(node_num,limb_angle-(start_angle+(limb_angle_delta*node_num)),end_x,end_y,limb_thickness+limb_thick_delta);
					rec_limb(node_num,limb_angle+(start_angle+(limb_angle_delta*node_num)),end_x,end_y,limb_thickness+limb_thick_delta);
				}
			}
			else{
			rec_limb(node_num,limb_angle-(start_angle+(limb_angle_delta*node_num)),end_x,end_y,limb_thickness+limb_thick_delta);
			rec_limb(node_num,limb_angle+(start_angle+(limb_angle_delta*node_num)),end_x,end_y,limb_thickness+limb_thick_delta);
			}
		}
		if(node_num == start_node/6){
			ArrayList<Integer> temp_cord = new ArrayList<Integer>();
			temp_cord.add(0, start_x);
			temp_cord.add(1, start_y);
			fruit_node.add(temp_cord);
			//System.out.println("cord added");
		}
		else{
			return;
		}
	}
	//Old trunk gen for main method
	public void trunk(int trunk_x, int trunk_y, int trunk_length, int trunk_thickness){
		EZ.addLine(trunk_x, trunk_y, trunk_x, trunk_y-trunk_length, primary_color, trunk_thickness);
		rec_limb(start_node,(-start_angle),trunk_x,(trunk_y-trunk_length),thickness);
		rec_limb(start_node,(start_angle),trunk_x,(trunk_y-trunk_length),thickness);
	}
	//draws only lines, overload with image string to use the image version now
	/*
	public void limb_draw(ArrayList<Integer> temp_limb){
		int node_num = temp_limb.get(0); //int node_num,
		//int limb_angle = temp_limb.get(1);//int limb_angle, 
		int start_x = temp_limb.get(2);//int start_x, 
		int start_y = temp_limb.get(3);//int start_y, 
		int end_x = temp_limb.get(4);
		int end_y = temp_limb.get(5);
		int limb_thickness = temp_limb.get(6);//int limb_thickness
		//COLOR SELECTION - based on node count to sorta look like leaves
		Color limb_color;
		if(((double)node_num/(double)start_node)<= leaf_node_precentage_3){
			limb_color = tertiary_color;
		}
		else if(((double)node_num/(double)start_node)<= leaf_node_precentage_2){
			limb_color = secondary_color;
		}
		else{
			limb_color = primary_color;
		}
		EZ.addLine(start_x, start_y, end_x, end_y, limb_color, limb_thickness);
	}*/
	//old method for testing something or other, maybe drawing from the limb_store array?
	public void draw_limbs(){
		for(int i = 0; i<limb_store.size();i++){
			ArrayList<ArrayList<Integer>> node_list = limb_store.get(i);
			for(int n=0;n<=(node_list.size()/2);n++){
				limb_draw(node_list.get(n));
				limb_draw(node_list.get((node_list.size()-(n+1))));
			}
		}
	}

}
