package edu.turtlekit2.warbot.dayz;

import java.util.List;

import edu.turtlekit2.warbot.WarBrain;
import edu.turtlekit2.warbot.message.WarMessage;
import edu.turtlekit2.warbot.percepts.Percept;
import edu.turtlekit2.warbot.waritems.WarFood;

public class BrainExplorer extends WarBrain {
	
	enum State {RECHERCHE,RAPPORTE}
	enum Type {CUEILLEUR,ESPION}
	
	State currentState = State.RECHERCHE;
	Type currentType = Type.CUEILLEUR;
	String order;
	
	List<Percept> percepts;
	List<WarMessage> messages;
	
	public BrainExplorer(){}

	@Override
	public String action() {
			
		percepts = getPercepts();
		messages = getMessage();
		
		//Si la base veut un espion on tranforme l'explorer en espion
		for(WarMessage m : messages)
			if(m.getMessage() == "devientEspion")
				currentType = Type.ESPION;
		
		//Déclare son type à la base
		if(currentType == Type.ESPION)
			broadcastMessage("WarBase","espion",null);
		else
			broadcastMessage("WarBase","cueilleur",null);
		
		//Par défaut, l'explorer avance
		order = "move";
			
		//Selon son etat, l'explorer fera des actions différentes
		switch(currentState){
			//Les cueilleurs recherchent la nourriture la plus proche et vont la chercher
			//Les espions recherchent la base ennemie
			case RECHERCHE:
				if(currentType == Type.ESPION)
					findEnnemyBase();
				if(currentType == Type.CUEILLEUR)
				{
					findEnnemyBase();
					takeFood(closestFood());
					if(sizeBag() == 3)
						currentState = State.RAPPORTE;
				}
	         break;
	         //Rapporte la nourriture quand le sac est plein (3 food)
			 case RAPPORTE:
				 giveFood(getBaseLocation());
				 if(sizeBag() == 0)
						currentState = State.RECHERCHE;
			 break;
			}
		
		while(isBlocked()){
			setRandomHeading();
		}
			
		return order;
	}
	
	private boolean findEnnemyBase() {
		if(percepts.size() > 0)
			for(Percept p : percepts)
				if(p.getType().equals("WarBase") && p.getTeam() != getTeam())
				{
					broadcastMessage("WarRocketLauncher","ennemyBaseLocation",null);
					setHeading(p.getAngle());
					return true;
				}
		
		return false;
	}

	private Percept closestFood()
	{
		Percept food = null;
		if(percepts.size() > 0)
			for(Percept p : percepts)
				if(p.getType().equals("WarFood"))
				{
					if(food == null)
						food = p;
					else
						if(p.getDistance() < food.getDistance())
							food = p;
				}
		
		return food;
	}
	
	private void takeFood(Percept food)
	{
		if(food != null)
		{
			setHeading(food.getAngle());
			if(food.getDistance() < WarFood.MAX_DISTANCE_TAKE)
				order = "take";
		}
	}
	
	private void giveFood(WarMessage location)
	{
		if(location != null)
		 {
			setHeading(location.getAngle());
			if(location.getDistance() < WarFood.MAX_DISTANCE_TAKE)
			{
				setAgentToGive(location.getSender());
				order = "give";
			}
		 }
	}
	
	private WarMessage getBaseLocation()
	{
		WarMessage baseLocation = null;
		broadcastMessage("WarBase","baseLocation",null);
		
		if(messages.size() > 0)
			for(WarMessage m : messages )
				if(m.getMessage() == "baseLocation")
						baseLocation = m;
		
		return baseLocation;
	}
}
