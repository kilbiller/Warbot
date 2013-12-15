package edu.turtlekit2.warbot.dayz;

import java.util.Scanner;

/**
 * Transition pour savoir si le sac est vide ou non
 * @author Florian
 *
 */
public class SacPlein extends Transition{

	public SacPlein()
	{}
	
	@Override
	boolean valide(BrainExplorer be) {
		//Si le sac est plein, on doit verifier ou est la base afin de rapporter la nourriture
		if(be.fullBag())
		{
			this.etat = new EtatTransitionBase();
			return true;
		}
	
		return false;
	}
}
