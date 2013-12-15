package edu.turtlekit2.warbot.dayz;

import java.util.List;
import java.util.Scanner;

import edu.turtlekit2.warbot.percepts.Percept;
import edu.turtlekit2.warbot.waritems.WarFood;

/**
 * Classe de transition permettant de savoir si la nourriture est assez proche, sinon l'agent doit s'en approcher avant de la prendre
 * @author Florian
 *
 */
public class SoFar extends Transition {

	public SoFar() {}
	@Override
	boolean valide(BrainExplorer be) {
		boolean ok = false;
		List<Percept> liste = be.getPercepts();
		
		for(Percept p : liste)
		{
			if(p.getType().equals("WarFood"))
			{
				if(p.getDistance() < WarFood.MAX_DISTANCE_TAKE)
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
			this.etat = new TakeFood();
		}else
		{
			this.etat = new Echap();
		}
		return true;
	}

}
