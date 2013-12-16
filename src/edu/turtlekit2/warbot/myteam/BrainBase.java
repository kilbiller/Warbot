package edu.turtlekit2.warbot.myteam;

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
		
		//Recupère le nombre d'espions en jeu
		int nbEspion = getTypeNumber("espion");
		int nbDefenseur = getTypeNumber("defenseur");
		boolean baseUnderAttack = isAttacked();
		
		for(WarMessage m : messages){
			//Si on demande les coordonnées de la base on les envoient au demandeur
			if(m.getMessage() == "baseLocation")
				reply(m, "baseLocation", null);
			
			//Si il n'y a pas assez d'espions, on en fait
			if(nbEspion < 2 && m.getMessage() == "cueilleur")
			{
				sendMessage(m.getSender(), "devientEspion", null);
				nbEspion++;
			}
			
			//TODO: Faire que les 3 PLUS PROCHE de la base deviennent defenseurs
			//Si la base est attaqué et qu'il n'y a pas assez de defenseurs, on en fait
			if(baseUnderAttack && nbDefenseur < 3 && m.getMessage() == "attaquant")
			{
				sendMessage(m.getSender(), "devientDefenseur", null);
				nbDefenseur++;
			}
		}
		
		if(!baseUnderAttack)
			broadcastMessage("WarRocketLauncher","devientAttaquant",null);
		
		if(getEnergy() > 12000){
			setNextAgentCreate("Explorer");
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
