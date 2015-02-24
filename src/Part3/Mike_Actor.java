package Part3;
//Borrowed code from Reed
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.Scanner;


// This utilizes Jason's Flipbook Code Example
public class Mike_Actor {

	private int numFrames;		// Keeps track of how many frames are in the animation sequence
	private long duration;		// duration to play the animation over (in milliseconds)
	private long starttime;		// keep track of starting time of animation

	private boolean loopIt;		// determine whether animation should loop or not
	private boolean starting;	// Flag to indicate that you are starting animation from frame zero
	private boolean stopped;	// Flag to indicate if the animation has stopped.
	private boolean visibility;	// Flag to determine if the images should be visible or not
	
	private EZImage[] frames;	// Hold all the animation frames
	private EZGroup group;		// Holds the EZGroup
	
	// These are default values and will keep the actor off the screen until moved to a position.
	private int currentPosX = -100;
	private int currentPosY = -100;
	private double currentDegrees = 0;
	private double currentScale = 1.0;
	
	private int standbyFrames = 2;
	
	private Command currentCommand;
	
	private Scanner commands;
	private EZSound soundBackground;
	// Constructor accepts the following parameters
	// filename - contains an array of filenames of images to read.
	// dur - duration over which animation frames should play.
	// posX, posY - location of the animated object.
	Mike_Actor(String imagename, String filename) throws FileNotFoundException {
		commands = new Scanner(new FileReader(filename));
		String [] standbyFilenames = new String[standbyFrames];
		
		for(int i = 0; i < standbyFrames; i++){
			standbyFilenames[i] = "imageName" + (i + 1) + ".png";
		}
		
		// Make an EZgroup to gather up all the individual EZimages.
		group = EZ.addGroup();
		numFrames = standbyFrames;
		
		// Make an array to hold the EZImages
		frames = new EZImage[numFrames];
		
		// Load each image
		for(int i = 0; i < numFrames; i++){
			frames[i] = EZ.addImage(standbyFilenames[i], 0, 0);
			frames[i].hide();
			group.addElement(frames[i]);
		}

		setLoop(true);
		starting = true;
		stopped = false;
		visibility = true;
	}


	void translateTo(int posX, int posY) {
		group.translateTo(posX,posY);
	}

	void rotateTo(double angle){
		group.rotateTo(angle);
	}
	
	void scaleTo(double scale){
		group.scaleTo(scale);
	}
	void setLoop(boolean loop){
		loopIt = loop;
	}
	void restart(){
		starting = true;
	}
	
	void stop(){
		stopped = true;
	}
	
	// Hide each animation frame
	void hide(){
		visibility = false;
		for(int i =0; i < numFrames; i++) frames[i].hide();
	}
	
	void show() {
		visibility = true;
	}
	boolean go(){
		if (stopped) return false;
		// If the animation is starting for the first time save the starttime
		if (starting){
			parseCommand(commands);
			starttime = System.currentTimeMillis();
			starting = false;
		}

		duration = currentCommand.duration() * 1000;
		if(currentCommand.duration() != -1){
			// If the duration for the animation is exceeded and if looping is enabled
			// then restart the animation from the beginning.
			long check;
			if ((check = (System.currentTimeMillis() - starttime)) > duration) {
				currentPosX = group.getXCenter();
				currentPosY = group.getYCenter();
				currentDegrees = group.getRotation();
				currentScale = group.getScale();
				restart();
				if (loopIt) {
					restart();
					return true;
				}
				// Otherwise there is no more animation to play so stop.
				return false;
			}
			// Based on the current frame and elapsed time figure out what frame to show.
			float normTime = (float) (System.currentTimeMillis() - starttime)/ (float) duration;
			int currentFrame = (int) (((float) numFrames) *  normTime);
			if (currentFrame > numFrames-1) currentFrame = numFrames-1;
			runCommand(currentCommand, normTime);
			// Hide all the frames first
			for (int i=0; i < numFrames; i++) {
				if (i != currentFrame) frames[i].hide();
			}
			
			// Then show all the frame you want to see.
			if (visibility)
				frames[currentFrame].show();
			else 
				frames[currentFrame].hide();
			return true;
		}
		else{
			runCommand(currentCommand);
			parseCommand(commands);
			starting = true;
			return false;
		}

	}	
	
	public void parseCommand(Scanner command){
		if(command.hasNextLine()){
			currentCommand = new Command(command.nextLine());
		}
	}
	
	private class Command{
		private String commandName = "";
		private String commandParameters = "";
		private int commandDuration = -1;
		
		Command(String commandString){
			int index = commandString.indexOf(' ');
			commandName = commandString.substring(0, index);
			commandParameters = commandString.substring(index + 1, commandString.length());
			index = commandString.lastIndexOf(' ');
			String check = commandString.substring(index + 1, commandString.length());
			try{
				commandDuration = Integer.parseInt(check);
			} catch(NumberFormatException e){
				commandDuration = -1;
			}
		}
		
		public String name(){
			return commandName;
		}
		public String parameters(){
			return commandParameters;
		}
		public int duration(){
			return commandDuration;
		}
	}
	public void runCommand(Command command){
		switch(command.name()){
			case "PLAY":
				setLoop(false);
				soundBackground = EZ.addSound(command.parameters());
				soundBackground.play();
				break;
			case "STOP":
				soundBackground.stop();
				break;
			case "LOOP":
				soundBackground.loop();
				break;
			case "END":	
				break;
			case "default":
				break;
		}
	}
	public void runCommand(Command command, float normTime){
		// Note that time from command is in seconds
		switch(command.name()){
			case "CONTROL":
				// CONTROL x y degrees scale duration
				setLoop(true);
				String [] parameters = command.parameters().split("\\s+");
				group.translateTo(currentPosX + (int)((float)(Integer.parseInt(parameters[0]) - currentPosX) * normTime + 0.5), group.getXCenter());
				group.translateTo(group.getXCenter(), currentPosY + (int)((float)(Integer.parseInt(parameters[1]) - currentPosY) * normTime + 0.5));
				group.rotateTo((double)Math.round((currentDegrees + (Double.parseDouble(parameters[2]) - currentDegrees) * (double)normTime) * 100) / 100);
				group.scaleTo((double)Math.round((currentScale + (Double.parseDouble(parameters[3]) - currentScale) * (double)normTime) * 100) / 100);
				break;
				
  			case "DEFAULT":
				break;
		}
	}

}

//Thanks to Reed for letting me borrow his code