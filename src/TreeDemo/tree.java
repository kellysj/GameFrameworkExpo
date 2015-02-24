package TreeDemo;
import java.lang.Math;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
public class tree {
	int start_node;
	int start_length;
	int start_angle;
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
	boolean is_waving;
	boolean is_burning;
	String bark_image_file;
	ArrayList<ArrayList<Integer>> fruit_node;
	ArrayList<ArrayList<ArrayList<Integer>>> limb_store;
	ArrayList<ArrayList<ArrayList<Integer>>> temp_limb_store;
	int id_num;
	private EZGroup wave_0;
	private EZGroup wave_1;
	
	tree(int length, int angle, int thick_in, int nodes){
		EZ.initialize(1000, 1500);
		EZ.setBackgroundColor(Color.BLACK);
		id_num = 0;
		start_length = length;
		start_angle = angle;
		thickness = thick_in;
		start_node = nodes;
		node_count = start_node;
		primary_color = Color.CYAN;    //Color of trunk/branches
		secondary_color = Color.GREEN;//Color of leaves
		tertiary_color = Color.MAGENTA;    //Color of super leaves
		limb_thick_delta = -3;
		limb_angle_delta = 3;
		leaf_node_precentage_2 = 0.6;
		leaf_node_precentage_3 = 0.2;
		is_generated = false;
		is_drawn = false;
		bark_image_file = "bark_small.png";
		fruit_node = new ArrayList<ArrayList<Integer>>(0);
		limb_store = new ArrayList<ArrayList<ArrayList<Integer>>>(0);
		wave_0 = EZ.addGroup();
		wave_1 = EZ.addGroup();
	}
	public static void main(String args[]){
		tree tree_test = new tree(100,40,15, 10);
		
		//tree_test.trunk_GEN(500,750,150,20);
		//tree_test.addfruit(20, 8);
		tree_test.tree_init();
	}
	public void tree_init(){
		this.trunk_GEN(500,1400,500,15);
		is_generated = true;
		node_count = 0;
		temp_limb_store = limb_store_backup(limb_store);
		//limb_store_print(limb_store);
		//ystem.out.println("\n\nlimb_store_temp:" + temp_limb_store.size() + "," + limb_store.size());
		//limb_store_print(temp_limb_store);
		while(is_drawn == false){
			this.update();
		}
		temp_limb_store = limb_store_backup(limb_store);
		//this.addfruit((start_node-(int)((double)start_node/5)),10, 20);
		EZ.refreshScreen();
	}
	public void limb_wave(int degrees, int time){
		
	}
	public void trunk_GEN(int trunk_x, int trunk_y, int trunk_length, int trunk_thickness){
		
		ArrayList<Integer> temp_limb = new ArrayList<Integer>();
		temp_limb.add(0, (int)(start_node*1.5)); //int node_num,
		temp_limb.add(1, 0);//int limb_angle, 
		temp_limb.add(2, trunk_x);//int start_x, 
		temp_limb.add(3, trunk_y);//int start_y, 
		temp_limb.add(4, trunk_x);
		temp_limb.add(5, trunk_y-trunk_length);
		temp_limb.add(6, trunk_thickness);//int limb_thickness
		temp_limb.add(7, trunk_length);
		limb_store_add((0), temp_limb);
		id_num = -1;
		wave_0.translateTo(trunk_x,(trunk_y-trunk_length));
		wave_1.translateTo(trunk_x,(trunk_y-trunk_length));
		rec_limb_GEN(start_node,(-start_angle),trunk_x,(trunk_y-trunk_length),thickness,"0", 0);
		rec_limb_GEN(start_node,(start_angle),trunk_x,(trunk_y-trunk_length),thickness,"1", 1);
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
		id_num++;
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
		limb_store_add((start_node-node_num), temp_limb);
		//EZ.addLine(start_x, start_y, end_x, end_y, limb_color, limb_thickness);
		id_string = id_num + "," + id_string;
		node_num = node_num-1;	
		if(node_num>0){
			double rand_n = Math.random();
			if(rand_n >=.7 && node_num>= (start_node/5)){
				double rand_n2 = Math.random();
				//System.out.println("Case1: " + rand_n + ":" + rand_n2);
				if(rand_n2 <= 0.4999999)
				{
					rec_limb_GEN(node_num,limb_angle1,end_x,end_y,limb_thickness+limb_thick_delta,id_string,group_num);
				}
				else if(rand_n2>=0.5){
					rec_limb_GEN(node_num,limb_angle2,end_x,end_y,limb_thickness+limb_thick_delta,id_string,group_num);
				}
				else{
					rec_limb_GEN(node_num,limb_angle1,end_x,end_y,limb_thickness+limb_thick_delta,id_string,group_num);
					rec_limb_GEN(node_num,limb_angle2,end_x,end_y,limb_thickness+limb_thick_delta,id_string,group_num);
				}
			}
			else{
				rec_limb_GEN(node_num,limb_angle1,end_x,end_y,limb_thickness+limb_thick_delta,id_string,group_num);
				rec_limb_GEN(node_num,limb_angle2,end_x,end_y,limb_thickness+limb_thick_delta,id_string,group_num);
			}
		}
	}
	
	public void addfruit(int fruit_node_level, int num_fruit, int fruit_size){
		ArrayList<ArrayList<Integer>> fruit_node_temp = temp_limb_store.get(fruit_node_level);	
			for(int i = 0;i<=num_fruit;i++){
				int rand_cord = (int)(Math.random()*fruit_node_temp.size());
				System.out.println(rand_cord);
				ArrayList<Integer> temp_cord = fruit_node_temp.remove(rand_cord);
				//System.out.println("fruitadd: " + fruit_node.size() + ":" + temp_cord.get(0) + ":" + temp_cord.get(1) + ":" + rand_cord);
				EZ.addCircle(temp_cord.get(2), temp_cord.get(3), fruit_size, fruit_size, Color.ORANGE, true);
				EZ.refreshScreen();
			}
	}
	//update loop does most of the work
	public void update(){
		if(temp_limb_store.get(node_count).size()==0 && node_count == temp_limb_store.size()-1){
			is_drawn = true;
		}
		else if(temp_limb_store.get(node_count).size()==0){
			node_count++;
		}
		else{
			limb_draw(temp_limb_store.get(node_count).remove(0), bark_image_file);
			if(temp_limb_store.get(node_count).size()>0){
				limb_draw(temp_limb_store.get(node_count).remove(temp_limb_store.get(node_count).size()-1), bark_image_file);
			}
		}
		EZ.refreshScreen();
	}
	
	//method overload with image string to put in
	public void limb_draw(ArrayList<Integer> temp_limb, String image_name){
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
			EZImage limb1 = EZ.addImage(image_name, EZ.getWindowWidth()+1000, EZ.getWindowHeight()+1000);
			int x_cent = (int)((double)(end_x-start_x)/2)+start_x;
			limb1.translateTo(x_cent, start_y-((limb1.getHeight())/2));
			int top = start_y-((limb1.getHeight()));
			int trunk_num = (limb_length/limb1.getHeight())-1;
			if(limb_length%limb1.getHeight()!=0){
				trunk_num++;
			}
			System.out.println(trunk_num);
			double scale = limb1.getScale();
			double del_s = 0.2;
			for(int i = 0;i<=trunk_num;i++){
				scale = scale - del_s;
				int y_cent = (top - (int)(limb1.getHeight()*scale)/2);
				EZImage limb = EZ.addImage(image_name,EZ.getWindowWidth()+1000, EZ.getWindowHeight()+1000);
				System.out.println(end_y + "  special2  " + top);
				top = top - (int)(limb1.getHeight()*scale);
				if(top<end_y){
					limb.translateTo(x_cent,end_y+(int)(limb1.getHeight()*(scale))/2);
					limb.scaleTo(scale+del_s);
					return;
				}
				limb.scaleTo(scale);
				limb.translateTo(x_cent, y_cent);
			}
			//System.out.println((double)limb_length/(double)limb.getHeight() + ":" + scale);
		}
		if(node_num <=3){
			//EZImage leaf = EZ.addImage(image_name, EZ.getWindowWidth()+1000, EZ.getWindowHeight()+1000);
			EZCircle leaf = EZ.addCircle(EZ.getWindowWidth()+1000, EZ.getWindowHeight()+1000, 30, 30, Color.green, true);
			leaf.translateTo((int)((double)(end_x-start_x)/2)+start_x, (int)((double)(end_y-start_y)/2)+start_y);
			if(group == 0){
				wave_0.addElement(leaf);
			}
			else if(group == 1){
				wave_1.addElement(leaf);
			}
		}
		else{
			EZImage limb = EZ.addImage(image_name, EZ.getWindowWidth()+1000, EZ.getWindowHeight()+1000);
			double scale =(double)limb_length/(double)limb.getHeight();
			limb.scaleTo(scale);
			//System.out.println((double)limb_length/(double)limb.getHeight() + ":" + scale);
			limb.rotateBy(-limb_angle);
			limb.translateTo((int)((double)(end_x-start_x)/2)+start_x, (int)((double)(end_y-start_y)/2)+start_y);
			if(group == 0){
				wave_0.addElement(limb);
			}
			else if(group == 1){
				wave_1.addElement(limb);
			}
			//System.out.println("DRAWN: " + node_num + "," + limb_angle + "," + start_x + "," + start_y + "," + end_x + "," + end_y + "," + limb_thickness + "," + limb_length + "," + scale);
		}
		System.out.println("DRAWN: " + node_num + "," + limb_angle + "," + start_x + "," + start_y + "," + end_x + "," + end_y + "," + limb_thickness + "," + limb_length + "," + group + "," + id_num);
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
	}
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
	//old method for drawing limbs without an update loop, for use in main method for testing
	public void draw_limbs(String image_name){
		for(int i = 0; i<limb_store.size();i++){
			ArrayList<ArrayList<Integer>> node_list = limb_store.get(i);
			for(int n=0;n<=(node_list.size()/2);n++){
				limb_draw(node_list.get(n),image_name);
				limb_draw(node_list.get((node_list.size()-(n+1))),image_name);
			}
		}
		EZ.refreshScreen();
	}
	//early test method for getting limb_store to work properly
	public void limb_store_testgen(){
		int count = 0;
		for(int i = 0; i<3; i++){
			for(int n = 0; n<3; n++){
				ArrayList<Integer> fake_params = new ArrayList<Integer>();
				int random1 = (int)(10*Math.random());
				int random2 = (int)(10*Math.random());
				fake_params.add(0, i);
				fake_params.add(1, random2);
				fake_params.add(2, count);
				fake_params.add(3, 3);
				fake_params.add(3,10);
				limb_store_add(i, fake_params);
				count++;
			}
		}
	}

}
