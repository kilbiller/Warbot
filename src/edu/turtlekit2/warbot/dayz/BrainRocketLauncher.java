package edu.turtlekit2.warbot.dayz;

import java.util.List;

import edu.turtlekit2.warbot.WarBrain;
import edu.turtlekit2.warbot.message.WarMessage;
import edu.turtlekit2.warbot.percepts.Percept;
import edu.turtlekit2.warbot.waritems.WarFood;

public class BrainRocketLauncher extends WarBrain{
	
	enum State {ATTAQUEBASE,DEFENDBASE}
	enum Type {ATTAQUANT,DEFENSEUR}
	
	State currentState = State.DEFENDBASE;
	Type currentType = Type.DEFENSEUR;
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
		
		//Par défaut, le WarLauncher avance
		order = "move";
		
		switch(currentState){
		case ATTAQUEBASE:
			attackEnnemyBase();
		break;
		case DEFENDBASE:
			defendBase();
		break;
		}
		
		//Recharge si besoin
		if(!isReloaded())
			if(!isReloading())
				order = "reload";
		
		//Va chercher de la nourriture si plus d'energie
		if(getEnergy() < 500)
			takeFood(closestFood());
		 
		//Mange si nourriture disponible
		if(!emptyBag()) order = "eat";
		
		while(isBlocked()){
			setRandomHeading();
		}
		
		return order;
	}
	
	private void attackEnnemyBase()
	{
		//Se dirige vers un allié proche de la base ennemie
		if(messages.size() > 0)
			for(WarMessage m : messages)
			{
				if(m.getMessage() == "ennemyBaseLocation")
					setHeading(m.getAngle());
			}
		
		//Puis cherche la base par lui même
		if(percepts.size() > 0)
			for(Percept p : percepts)
				if(p.getType().equals("WarBase") && p.getTeam() != getTeam())
				{
					broadcastMessage("WarRocketLauncher","ennemyBaseLocation",null);
					setAngleTurret(p.getAngle());
					order = "fire";
				}
				else //Si la base n'a pas été trouvée attaque les tanks a vue
                {
					if(p.getType().equals("WarRocketLauncher") && p.getTeam() != getTeam())
                    {
						setAngleTurret(p.getAngle());
                        order = "fire";
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
	
	private void defendBase()
	{
		WarMessage base = getBaseLocation();
		if(base != null)
		{
			setHeading(base.getAngle());
			if(base.getDistance() < 100)
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
		}
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
}