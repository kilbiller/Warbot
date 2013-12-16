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
		int nbEspion = getEspionNumber();
		
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
		}
		
		if(getEnergy() > 12000){
			setNextAgentCreate("Explorer");
			order = "create";
		}
		
		//TODO : Protection de la base
		//Pour protéger le base il faudrait faire une transaction:
		//base : help!
		//warlaunchers : nous sommes dispo
		//base : je prend les 2/3 tanks les plus proches
		//warlaunchers choisi : ok on arrive
		//warlaunchers pas choisi : on continue notre vie
		//Si par la suite la base est safe, retourne les tanks en mode defense en mode attaque
		
		//Mange si nourriture disponible
		if(!emptyBag()) order = "eat";
		
		return order;
	}
	
	private int getEspionNumber()
	{
		int nb = 0;
		for(WarMessage m : messages)
			if(m.getMessage() == "espion")
				nb++;
		
		return nb;
	}
}
