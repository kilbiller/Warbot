package edu.turtlekit2.warbot.myteam;

import java.util.List;

import edu.turtlekit2.warbot.WarBrain;
import edu.turtlekit2.warbot.message.WarMessage;
import edu.turtlekit2.warbot.percepts.Percept;

public class BrainRocketLauncher extends WarBrain{
	
	enum State {ATTAQUEBASE,DEFENDBASE}
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
		
		//Si la base veut un defenseur on tranforme le WarRocketLauncher en defanseur
		for(WarMessage m : messages)
		{
			if(m.getMessage() == "devientDefenseur")
			{
				currentType = Type.DEFENSEUR;
				currentState = State.DEFENDBASE;
			}
			if(m.getMessage() == "devientAttaquant")
			{
				currentType = Type.ATTAQUANT;
				currentState = State.ATTAQUEBASE;
			}
		}
		
		//Déclare son type à la base
		if(currentType == Type.ATTAQUANT)
			broadcastMessage("WarBase","attaquant",null);
		else
			broadcastMessage("WarBase","defenseur",null);
		
		//Par défaut, le WarLauncher bouge au hazard
		while(isBlocked()){
			setRandomHeading();
		}
		order = "move";
		
		switch(currentState){
		case ATTAQUEBASE:
			attackEnnemyBase();
		break;
		case DEFENDBASE:
			WarMessage baseLocation = getBaseLocation();
			if(baseLocation != null)
			{
				setHeading(baseLocation.getAngle());
				if(baseLocation.getDistance() < 100)
					attackEnnemyLauncher();
			}
		break;
		}
		
		//Recharge si besoin
		if(!isReloaded())
			if(!isReloading())
				order = "reload";
		
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
	
	private void attackEnnemyLauncher()
	{
		if(percepts.size() > 0)
			for(Percept p : percepts)
				if(p.getType().equals("WarRocketLauncher") && p.getTeam() != getTeam())
				{
					setHeading(p.getAngle());
					setAngleTurret(p.getAngle());
					order = "fire";
				}
	}
	
	private WarMessage getBaseLocation()
	{
		WarMessage baseLocation = null;
		broadcastMessage("WarBase","baseLocation",null);
		
		//Reponse
		if(messages.size() > 0)
			for(WarMessage m : messages )
				if(m.getMessage() == "baseLocation")
						baseLocation = m;
		
		return baseLocation;
	}
}