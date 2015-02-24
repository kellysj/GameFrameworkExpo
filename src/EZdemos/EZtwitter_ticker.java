package EZdemos;
import java.lang.Math;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

import twitter4j.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

public class EZtwitter_ticker {
	ArrayList<String> print_list;
	boolean is_showing;
	ArrayList<String> char_container;
	ArrayList<EZText> conveyor_container;
	EZGroup char_g;
	int main_width;
	int main_height;
	int ticker_height;
	int ticker_width;
	int x_center;
	int y_center;
	int font_width;
	int ticker_end;
	int ticker_start;
	int move_time;
	String font_name;
	int font_size;
	int space_count;
	Color text_color;
	boolean conveyor_loaded;
	boolean ticker_start_empty;
	EZCircle motion_check;
	long loop_start;
	long loop_time;
	int move_start;
	boolean is_first_loop;
	long last_refresh;
	long interval;
	Date last_loaded;
	ArrayList<String> raw_feed;

	EZtwitter_ticker() {
		last_loaded = new Date(1000);//CHANGE THIS DATE OBJECT TO SET TO ONLY FUTURE REPLIES!!!
		interval = 30000;
		last_refresh = System.currentTimeMillis()-interval;
		raw_feed = new ArrayList<String>();
		x_center = EZ.getWindowWidth() / 2;
		y_center = 9 * EZ.getWindowHeight() / 10 + main_height / 2;
		main_width = 8 * (EZ.getWindowWidth()) / 10;
		main_height = EZ.getWindowHeight() / 10;
		space_count = 5;
		loop_time = 20000;
		font_name = "Arial";
		font_size = 30;
		font_width = 18;
		text_color = Color.yellow;
		ticker_height = main_height;
		ticker_width = main_width - 2 * font_width;
		ticker_end = (x_center - ticker_width / 2) - font_size;
		ticker_start = (x_center + ticker_width / 2) + font_size;
		char_g = EZ.addGroup();
		char_container = new ArrayList<String>();
		conveyor_container = new ArrayList<EZText>();
		is_showing = true;
		conveyor_loaded = false;
		ticker_start_empty = true;
		is_first_loop = true;
		//
		// TESTING SHAPES
		
		 EZRectangle main_body = EZ.addRectangle(x_center, y_center, main_width, main_height, Color.blue, true);
		 EZRectangle ticker_body = EZ.addRectangle(x_center, y_center, ticker_width, ticker_height, Color.darkGray, true); 
		 EZ.addCircle(ticker_start, y_center, 30, 30,Color.orange, true);
		 motion_check = EZ.addCircle(ticker_start,y_center, 30, 30, Color.blue, true);
		 EZ.addCircle(ticker_start-font_width, y_center, 10, 10, Color.red,true);
		 EZ.addCircle(ticker_end, y_center, 30, 30, Color.green, true);
		 
		move_start = ticker_start;
		//test_init(); //Loads dummy strings to the raw_feed when you don't want to query twitter
		
		System.out.println("end center:" + ticker_end);
	}

	public static void main(String args[]) {
		EZ.initialize(1024, 768);
		EZ.setBackgroundColor(Color.black);
		EZtwitter_ticker test = new EZtwitter_ticker();
		//test.test_init();
		// test.char_loader();
		// test.print_s_arraylist(test.char_container);
		/*
		 * String[] print = EZText.getAllFontNames(); for(int i =
		 * 0;i<print.length;i++){ System.out.println(print[i]); }
		 */
		// test.print_s_arraylist(test.char_container);
		while (true) {
			test.update();
			//test.listener_update();
			EZ.refreshScreen();
		}

	}
	
	public void listener_update(){
		if(wait_over()){
			gethometimeline();
		}
		print_rawList();
	}

	public void update() {
		if(wait_over()&&char_container.size()<=50){
			gethometimeline();
		}
		if (raw_feed.size() > 0) {
			char_loader();
		}
		if (is_showing == true) {
			if (conveyor_loaded == false){
				conveyor_load();
			}
			if(conveyor_container.size()>0){
				char_conveyor();
			}
		} 
		else {
			print_rawList();
			//System.out.println("ticker_update: nothing to load");
		}
		if (EZInteraction.wasKeyPressed('t')) {
			if (is_showing == true) {
				for (int i = 0; i < conveyor_container.size(); i++) {
					conveyor_container.get(i).hide();
				}
				is_showing = false;
			} else {
				for (int i = 0; i < conveyor_container.size(); i++) {
					conveyor_container.get(i).show();
				}
				is_showing = true;
			}
		}
		// System.out.println(twitter_listener.passed_status);
		EZ.refreshScreen();
	}
	
	private boolean wait_over(){
		if(System.currentTimeMillis()-last_refresh>=interval){
			last_refresh = System.currentTimeMillis();
			return true;
		}
		else{
			return false;
		}
	}
	void print_rawList(){
		for(int i = 0;i<raw_feed.size();i++){
			System.out.println("print list:" + raw_feed.get(i));
		}
		
	}
	private void gethometimeline(){
		try {
			// gets Twitter instance with default credentials
			Twitter twitter = new TwitterFactory().getInstance();
			User user = twitter.verifyCredentials();
			List<Status> statuses = twitter.getMentionsTimeline();
			System.out.println("Showing @" + user.getScreenName() + "'s home timeline.");
			for (int i = statuses.size()-1; i>= 0; i--) {
				Status status = statuses.get(i);
				String out = "@" + status.getUser().getScreenName() + " - " + status.getText();
				 if(status.getCreatedAt().compareTo(last_loaded)>0){
					 last_loaded = status.getCreatedAt();
					 raw_feed.add(out);
					 //System.out.println(out);
					 out = "NEW:" + out;
				 }
				 else{
					 out = "OLD:" + out;
				 }
				 System.out.println(out);
			}
			print_rawList();
		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to get timeline: " + te.getMessage());
		}
	}

	public void char_conveyor() {
		motion_check.translateTo((move_start-(int)((move_start-ticker_end)*(System.currentTimeMillis()-loop_start)/loop_time)),y_center);
		for (int i = 0; i < conveyor_container.size(); i++) {
			//System.out.println("Char convey: " +(int)(System.currentTimeMillis()-loop_start) + "::" +(move_start-(int)((move_start-ticker_end)*(System.currentTimeMillis()-loop_start)/loop_time)));
			conveyor_container.get(i).translateTo((move_start - (int) ((move_start - ticker_end)* (System.currentTimeMillis() - loop_start) / loop_time)) + (i * font_width), y_center);
			//System.out.println("msg: " + conveyor_container.get(i).getMsg() +" : " + conveyor_container.get(i).getXCenter() + ":" +ticker_start);
		}
		//System.out.println("pre_remove:" + conveyor_container.get(0).getXCenter() + ":" + ticker_end);
		//System.out.println("Check Remove:" + conveyor_container.get(0).getXCenter() + ":" + (ticker_end+font_width));
		if (conveyor_container.get(0).getXCenter() <= ticker_end) {
			if (char_container.size() > 0) {
				//System.out.println("Removed:" +conveyor_container.get(0).getXCenter() + ":" +(ticker_end+font_width));
				EZText removing = conveyor_container.remove(0);
				EZ.removeEZElement(removing);
				EZText text_added = EZ.addText(ticker_start, y_center,char_container.remove(0), text_color);
				text_added.setFont(font_name);
				text_added.setFontSize(font_size);
				conveyor_container.add(text_added);
				loop_start = System.currentTimeMillis();
				if (move_start == ticker_start) {
					long factor = loop_time / (move_start - ticker_end);
					move_start = conveyor_container.get(0).getXCenter();
					loop_time = (move_start - ticker_end) * factor;
				}
			}
		}

	}

	public void conveyor_load() {
		 
		if (ticker_start_empty == true) {
				System.out.println("char cont size: " + char_container.size());
				String to_add;
				if (char_container.size() > 0){
					to_add = char_container.remove(0);
				}
				else{
					to_add = " ";
				}
				EZText text_added = EZ.addText(ticker_start, y_center,to_add, text_color);
				text_added.setFont(font_name);
				text_added.setFontSize(font_size);
				text_added.translateTo(ticker_start, y_center);
				//System.out.println("text size:" + text_added.getWidth());
				conveyor_container.add(text_added);
				ticker_start_empty = false;
			} 
		else if (conveyor_container.size() > 0 && conveyor_container.get(conveyor_container.size() - 1).getXCenter() <= (ticker_start - font_width)) {
				//System.out.println("ticker empty");
				ticker_start_empty = true;
			}
		else {
			// System.out.println("Out of text to load");
		}
		if (is_first_loop == true) {
			System.out.println("loop load");
			loop_start = System.currentTimeMillis();
			is_first_loop = false;
		}
	}

	public void check_remove(EZText char_in) {
		if (char_container.size() > 0) {
			System.out.println("Check Remove:" + char_in.getXCenter() + ":"+ (ticker_end + font_width));
			if (char_in.getXCenter() <= ticker_end) {
				System.out.println("Removed:" + char_in.getXCenter() + ":"+ (ticker_end + font_width));
				EZ.removeEZElement(char_in);
				EZText text_added = EZ.addText(ticker_start, y_center,char_container.remove(0), text_color);
				text_added.setFont(font_name);
				text_added.setFontSize(font_size);
				conveyor_container.add(text_added);
			}
		} else {
			System.out.println("Check_remove: nothing to load after removal");
		}
		if (conveyor_loaded == false) {
			System.out.println("what");
			conveyor_loaded = true;
		}
	}

	public void char_loader() {
		if (raw_feed.size() > 0) {
			String to_split = raw_feed.remove(0);
			for (int i = 0; i <= space_count; i++) {
				char_container.add(" ");
			}
			//System.out.println(to_split);
			char[] char_tokens = to_split.toCharArray();
			for (int i = 0; i < char_tokens.length; i++) {
				//System.out.println(char_tokens[i]);
				char_container.add(String.valueOf(char_tokens[i]));
			}
		}
	}

	public void print_s_arraylist(ArrayList<String> in) {
		String to_print = in.size() + ":";
		for (int i = 0; i < in.size(); i++) {
			to_print = to_print + in.get(i);
			if (i < in.size() - 1) {
				to_print = to_print + ",";
			}
		}
		System.out.println(to_print);
	}

	public void test_init() {
		raw_feed.add("goats are cool");
		raw_feed.add("I am a text box");
		raw_feed.add("There is nothing");
		raw_feed.add("goats are cool");
		raw_feed.add("I am a text box");
		raw_feed.add("There is nothing");
		raw_feed.add("goats are cool");
		raw_feed.add("I am a text box");
		raw_feed.add("There is nothing");
		raw_feed.add("goats are cool");
		raw_feed.add("I am a text box");
		raw_feed.add("There is nothing");
		raw_feed.add("goats are cool");
		raw_feed.add("I am a text box");
		raw_feed.add("There is nothing");
	}
}
