package Part4;
/*
 * Copyright 2007 Yusuke Yamamoto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import twitter4j.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.7
 */
public class twitter_listener{
	long last_refresh;
	long interval;
	Date last_loaded;
	public static ArrayList<String> raw_feed;
	public String passed_status;
	twitter_listener(){
		passed_status = "";
		last_loaded = new Date(10000);
		interval = 30000;
		last_refresh = System.currentTimeMillis()-interval;
		raw_feed = new ArrayList<String>();
	}
	public static void main(String[] args) {
		twitter_listener tester = new twitter_listener();		
		while(true){
			tester.update();
			/*
			if(tester.wait_over()){
				tester.gethometimeline();
			}
			tester.refresh_pass();
			*/
			//if(raw_feed.size()>0){
			//	tester.print_rawList();
			//}
		}
		
	}
	public void update(){
		if(wait_over()){
			gethometimeline();
		}
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
	private void refresh_pass(){
		if(passed_status.contentEquals("")&&raw_feed.size()>0){
			passed_status = raw_feed.get(0);
			//System.out.println(passed_status);
		}
	}
	
	void print_rawList(){
		String to_print = "";
		for(int i = 0;i<raw_feed.size();i++){
			System.out.println(raw_feed.get(i));
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
}