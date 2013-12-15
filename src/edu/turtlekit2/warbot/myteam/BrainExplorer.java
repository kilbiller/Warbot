package edu.turtlekit2.warbot.myteam;

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
			
		//Par défaut, l'explorer bouge au hazard
		while(isBlocked()){
			setRandomHeading();
		}
		order = "move";
			
		//Selon son etat, l'explorer fera des actions différentes
		switch(currentState){
			//Recherche la nourriture la plus proche et va la chercher
			case RECHERCHE:
				//TODO : Séparer les explorer en cueilleurs et en espions
				findEnnemyBase();
				Percept closestFood = findClosestFood();
				if(closestFood != null)
				{
					//TODO : Corriger un bug qui bloque l'exporer quand il est trop pres du mur et d'une nourriture (rare)
					setHeading(closestFood.getAngle());
					if(closestFood.getDistance() < WarFood.MAX_DISTANCE_TAKE)
						order = "take";
					 
					if(sizeBag() == 3)
						currentState = State.RAPPORTE;
				}
	         break;
	         //Rapporte la nourriture quand le sac est plein (3 food)
			 case RAPPORTE:
				 //Demande a la base son emplacement
				 WarMessage baseLocation = getBaseLocation();
				 
				 if(baseLocation != null)
				 {
					 setHeading(baseLocation.getAngle());
				
					if(baseLocation.getDistance() < WarFood.MAX_DISTANCE_TAKE)
					{
						setAgentToGive(baseLocation.getSender());
						order = "give";
						
						if(sizeBag() == 0)
							currentState = State.RECHERCHE;
					}
					
				 }
			 break;
			}
			
			return order;
	}
	
	private void findEnnemyBase() {
		if(percepts.size() > 0)
			for(Percept p : percepts)
				if(p.getType().equals("WarBase") && p.getTeam() != getTeam())
					broadcastMessage("WarRocketLauncher","ennemyBaseLocation",null);
	}

	private Percept findClosestFood()
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
	
	private WarMessage getBaseLocation()
	{
		WarMessage baseLocation = null;
		
		broadcastMessage("WarBase","baseLocation",null);
		
		//Reponse
		if(messages.size() > 0)
		{
			for(WarMessage m : messages )
				if(m.getMessage() == "baseLocation")
						baseLocation = m;
		}
		
		return baseLocation;
	}
}
