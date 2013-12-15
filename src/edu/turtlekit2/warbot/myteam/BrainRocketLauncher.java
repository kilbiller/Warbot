package edu.turtlekit2.warbot.myteam;

import java.util.List;

import edu.turtlekit2.warbot.WarBrain;
import edu.turtlekit2.warbot.message.WarMessage;
import edu.turtlekit2.warbot.myteam.BrainExplorer.Type;
import edu.turtlekit2.warbot.percepts.Percept;

public class BrainRocketLauncher extends WarBrain{
	
	enum State {ATTAQUEBASE}
	enum Type {ATTAQUANT,DEFENSEUR}
	
	State currentState = State.ATTAQUEBASE;
	Type currentType = Type.ATTAQUANT;
	String order;
	
	List<Percept> percepts;
	List<WarMessage> messages;
	
	public BrainRocketLauncher(){}
	
	@Override
	public String action() {
		
		percepts = getPercepts();
		messages = getMessage();
		
		//Par défaut, le WarLauncher bouge au hazard
		while(isBlocked()){
			setRandomHeading();
		}
		order = "move";
		
		switch(currentState){
		case ATTAQUEBASE:
			
			attackEnnemyBase();
				
			//Recharge si besoin
			if(!isReloaded())
				if(!isReloading())
					order = "reload";
		break;
		}
		
		return order;
	}
	
	private void attackEnnemyBase()
	{
		//Detecte si la base connaît l'emplacement de la base ennemie
		if(messages.size() > 0)
			for(WarMessage m : messages)
			{
				if(m.getMessage() == "ennemyBaseLocation")
					setHeading(m.getAngle());
			}
		
		//Sinon cherche par lui même
		if(percepts.size() > 0)
			for(Percept p : percepts)
				if(p.getType().equals("WarBase") && p.getTeam() != getTeam())
				{
					broadcastMessage("WarRocketLauncher","ennemyBaseLocation",null);
					setAngleTurret(p.getAngle());
					order = "fire";
				}
	}
}