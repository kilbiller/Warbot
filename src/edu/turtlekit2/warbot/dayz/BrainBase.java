package edu.turtlekit2.warbot.dayz;

import java.util.List;

import edu.turtlekit2.warbot.WarBrain;
import edu.turtlekit2.warbot.message.WarMessage;
import edu.turtlekit2.warbot.percepts.Percept;

public class BrainBase extends WarBrain{
	
	List<Percept> percepts;
	List<WarMessage> messages;
	String order;
	
	public BrainBase(){}

	@Override
	public String action() {
		percepts = getPercepts();
		messages = getMessage();
		
		//Action par défaut
		order = "idle";
		
		//Recupère le nombre de chaque unité allié en jeu
		int nbEspion = getTypeNumber("espion");
		int nbCueilleur = getTypeNumber("cueilleur");
		int nbDefenseur = getTypeNumber("defenseur");
		int nbAttaquant = getTypeNumber("attaquant");
		boolean baseUnderAttack = isAttacked();
		
		for(WarMessage m : messages){
			//Si on demande les coordonnées de la base on les envoient au demandeur
			if(m.getMessage() == "baseLocation")
				reply(m, "baseLocation", null);
			
			//Si il n'y a pas assez d'espions, on en fait
			if(nbEspion < 2 && m.getMessage() == "cueilleur")
			{
				reply(m, "devientEspion", null);
				nbCueilleur--;
				nbEspion++;
			}
			
			//Maintien un ratio de 2 attaquant pour 3 defenseur
			if( ((float)nbAttaquant/(float)nbDefenseur < 2.0f/3.0f) && m.getMessage() == "defenseur")
			{
				reply(m, "devientAttaquant", null);
				nbDefenseur--;
				nbAttaquant++;
			}
		}
		
		if(baseUnderAttack && (nbAttaquant + nbDefenseur) > 0)
			broadcastMessage("WarRocketLauncher","devientDefenseur",null);
		
		if(getEnergy() > 12000){
			//Par défaut,on crée un explorer
			setNextAgentCreate("Explorer");
			
			//Si il y assez d'explorer OU que la base est attaquée OU qu'il n'y a plus assez de RocketLauncher
			//on crée un Launcher
			if((nbEspion + nbCueilleur) > 7 || baseUnderAttack || (nbAttaquant + nbDefenseur) < 2)
				setNextAgentCreate("RocketLauncher");
			
			order = "create";
		}
		
		//Mange si nourriture disponible
		if(!emptyBag()) order = "eat";
		
		return order;
	}
	
	private int getTypeNumber(String type)
	{
		int nb = 0;
		for(WarMessage m : messages)
			if(m.getMessage() == type)
				nb++;
		
		return nb;
	}
	
	private boolean isAttacked()
	{
		if(percepts.size() > 0)
			for(Percept p : percepts)
				if(p.getType().equals("WarRocketLauncher") && p.getTeam() != getTeam())
					return true;
		
		return false;
	}
}
