package edu.turtlekit2.warbot.nothingTeam;


import edu.turtlekit2.warbot.WarBrain;

public class BrainRocketLauncher extends WarBrain{
	
	public BrainRocketLauncher(){
		
	}
	
	@Override
	public String action() {
		while(isBlocked()){
			setRandomHeading();
		}
		return "move";
	}
}
