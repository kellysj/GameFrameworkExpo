package Part3;

	import java.io.*;
	import java.util.*;

	public class chencal_actor {
		private int position_x;
		private int position_y;
		private int des_x;
		private int des_y;
		private int startx;
		private int starty;
		private long starttime;
		private BufferedReader script_buffer;
		private float duration;
		private EZImage image;
		private Boolean is_moving = false;
		private String command = null;
		private int position_theta=0;
		private float current_scale = 1;
		private float des_scale;
		private float start_scale;
		private int startangle;
		private int command_rotatea;


		
		// Constructor
		public chencal_actor(String script_filename, String image_filename) {
			String temp1 = null;
			
			// Pull the script.
			try {
				script_buffer = new BufferedReader(new FileReader(script_filename));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Read initial position.
			try {
				temp1 = script_buffer.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Parse initial conditions.
			StringTokenizer st = new StringTokenizer(temp1);
			
			// Discard POS command.
			st.nextToken();
			
			// Record x and y positions.
			position_x = Integer.parseInt(st.nextToken());
			position_y = Integer.parseInt(st.nextToken());
			
			// Load image.
			image = EZ.addImage(image_filename, position_x, position_y);		
		}
		
		public void go() {
			String temp1 = null;
			
			if(is_moving == false) {
				// Pull next line of the script.
				try {
					temp1 = script_buffer.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					return;
				}
				//System.out.println("buffer "+temp1);
				
				// Check if there are things left in the buffer.
				if(temp1 == null) return;
				
				// Parse next command
				StringTokenizer st = new StringTokenizer(temp1);
				command = st.nextToken();
				if(command.contentEquals("COMMAND")) {
					des_x = Integer.parseInt(st.nextToken());
					des_y = Integer.parseInt(st.nextToken());
					starttime = System.currentTimeMillis();
					command_rotatea = Integer.parseInt(st.nextToken());
					des_scale = Float.parseFloat(st.nextToken());
					duration = Float.parseFloat(st.nextToken()) * 1000;
					startx=position_x; starty=position_y;
					startangle=position_theta;
					start_scale=current_scale;
					is_moving = true;

				}
				else return;
			}
			else {
				if(command.contentEquals("COMMAND")) {
					float normTime = (float) (System.currentTimeMillis() - starttime)/ (float) duration;
					
					position_x = (int) (startx + ((float) (des_x - startx) *  normTime));
					position_y = (int) (starty + ((float) (des_y - starty) *  normTime));
					
					
					position_theta = (int) (startangle + ((float) (command_rotatea - startangle) *  normTime));
						
					
					current_scale = (float) (start_scale + ((float) (des_scale - start_scale) *  normTime));
					
					if ((System.currentTimeMillis() - starttime) >= duration) {
						is_moving = false;
						position_x = des_x; position_y = des_y;
						position_theta = command_rotatea;
						current_scale = des_scale;
					}
					image.translateTo(position_x,position_y);	

					image.rotateTo(position_theta);
				
					
					image.scaleTo(current_scale);
			
					
					return;
				}
				else return;
				}
			
			}
		}
		



