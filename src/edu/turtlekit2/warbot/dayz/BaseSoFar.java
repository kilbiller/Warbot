package edu.turtlekit2.warbot.dayz;

import java.util.List;
import java.util.Scanner;

import edu.turtlekit2.warbot.message.WarMessage;
import edu.turtlekit2.warbot.percepts.Percept;
import edu.turtlekit2.warbot.waritems.WarFood;

/**
 * Classe de transition permettant de savoir si la base n'est pas trop loin pour donner la nourriture
 * @author Florian
 *
 */
public class BaseSoFar extends Transition {

	public BaseSoFar() {}
	@Override
	boolean valide(BrainExplorer be) {
		boolean ok = false;
		boolean trouve = false;
		Percept base = null;
		List<Percept> liste = be.getPercepts();
		List<WarMessage> listeM = be.getMessage();

		for(Percept p : liste)
		{
			if(p.getType().equals("WarBase") && be.getTeam() == p.getTeam())
			{
				base = p; trouve = true;
				if(base.getDistance() < WarFood.MAX_DISTANCE_TAKE)
				{
					ok = true;
				}else
				{
					be.setHeading(p.getAngle());
				}
			}
	
		}
		
		if(ok)
		{
			be.setAgentToGive(base.getId());
			this.etat = new Rapporter();
		}else
		{
			if(!trouve)
			{
				if(listeM.size()==0){
					be.broadcastMessage("WarBase", "where", null);
				}else if(listeM.size()>0){
					be.setHeading(listeM.get(0).getAngle());
				}
			}else
			{
				be.setHeading(base.getAngle());
			}
			
			this.etat = new Echap();
		}
		
		return true;
	}


}
