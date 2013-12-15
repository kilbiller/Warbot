package edu.turtlekit2.warbot.dayz;

import java.util.LinkedList;
import java.util.List;

import edu.turtlekit2.warbot.dayz.BrainExplorer;

public abstract class Etat {
	 List<Transition> transitions = new LinkedList<Transition>();
	 
	 public Etat() {}
	 
	 void exec(BrainExplorer be){
		 boolean tvalide=false;
		 
		 for(Transition x : transitions)
		 {
			 if(x.valide(be)){
				 x.execTransition(be);
				 tvalide=true; 
				 break;
			 }
		 }
		 
		 if(!tvalide)
		 	this.activite(be);
	 }
	 
	 abstract void activite(BrainExplorer be);
	
	 boolean etatFinal(){
		 return false;
	 }
}
