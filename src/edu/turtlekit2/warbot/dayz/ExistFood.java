package edu.turtlekit2.warbot.dayz;
import java.util.Scanner;
import java.util.List;

import edu.turtlekit2.warbot.percepts.Percept;

/**
 * Classe de transition permettant de vérifier la présence de nourriture
 * @author Florian
 *
 */
public class ExistFood extends Transition {

	public ExistFood() {}
	@Override
	boolean valide(BrainExplorer be) {
		boolean ok = false;
		List<Percept> liste = be.getPercepts();

		for(Percept p : liste)
		{
			//Si il y a de la nourriture, on vérifie ensuite qu'elle est assez proche
			if(p.getType().equals("WarFood"))
			{
				this.etat = new EtatTransitionFood();
				ok = true;
			}
		}

		return ok;
	}

}
