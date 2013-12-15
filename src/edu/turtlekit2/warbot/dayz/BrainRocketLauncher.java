package edu.turtlekit2.warbot.dayz;

import java.util.List;

import edu.turtlekit2.warbot.WarBrain;
import edu.turtlekit2.warbot.message.WarMessage;
import edu.turtlekit2.warbot.percepts.Percept;
import edu.turtlekit2.warbot.waritems.WarFood;

public class BrainRocketLauncher extends WarBrain{
	
	public BrainRocketLauncher(){
		
	}
	
	@Override
	public String action() {
		//Si un missile n'est pas chargé, on vérifie si il y a encore des missiles
		if(!isReloaded()){
			if(!isReloading()){
				return "reload";
			}
		}
		
		//On récupère la liste des agents autour
		List<Percept> listeP = getPercepts();
		//On récupère la liste des messages envoyés à cette agent
		List<WarMessage> listeM = getMessage();
		
		//On initialise la recherche d'agent
		Percept bestPercept = null;
		
		//Si il n'y a pas de message
		if(listeM.size() == 0){
			for(Percept p : listeP){
				//Si l'agent est la base ennemie, on attaque
				if(p.getType().equals("WarBase") && !p.getTeam().equals(getTeam())){
					bestPercept = p;
				}
			}
			
			//Si il y a une base ennemie
			if(bestPercept != null){
				//On envoie un message a tous les Launcher pour attaquer
				broadcastMessage("WarRocketLauncher", "base", null);
				setAngleTurret(bestPercept.getAngle());
				return "fire";
			}else{
				//Si l'agent est bloqué
				while(isBlocked()){
					setRandomHeading();
				}
				return "move";
			}
		}else{
			//Si il y a un message
			for(Percept p : listeP){
				if(p.getType().equals("WarBase") && !p.getTeam().equals(getTeam())){
					bestPercept = p;
				}
			}
			
			if(bestPercept != null){
				broadcastMessage("WarRocketLauncher", "base", null);
				setAngleTurret(bestPercept.getAngle());
				return "fire";
			}else if(listeM.size()>0){
				//On vérifie si il y a un message provenant de la base signifiant qu'il y a des ennemies
				boolean attackBase = false;				
				boolean ennemyBase = false;

				for(WarMessage tmp : listeM)
				{
					if(tmp.toString() == "ennemy")
					{
						if(tmp.getDistance() < WarFood.MAX_DISTANCE_TAKE)
						{
							attackBase = true;
							setHeading(tmp.getAngle());
						}
					}else
					{
						if(tmp.toString() == "baseEnnemy")
						{
							if(attackBase != true && tmp.getDistance() < WarFood.MAX_DISTANCE_TAKE)
							{
								ennemyBase = true;
								setHeading(tmp.getAngle());
							}
						}
						
					}
				}
				
				if(attackBase == false && ennemyBase == false)
				{
					WarMessage tmp = listeM.get(0);
					reply(tmp, "j'arrive", null);
					setHeading(tmp.getAngle());
				}
			}
		}
		
		return "move";
	}
}
