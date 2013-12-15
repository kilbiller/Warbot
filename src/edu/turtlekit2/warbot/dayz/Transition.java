package edu.turtlekit2.warbot.dayz;

import edu.turtlekit2.warbot.dayz.BrainExplorer;

public abstract class Transition {
	 Etat etat;
	 
	 void execTransition(BrainExplorer be){
		 be.changerEtat(etat);
		 etat.activite(be);
	 }
	
	 abstract boolean valide(BrainExplorer be);

}
