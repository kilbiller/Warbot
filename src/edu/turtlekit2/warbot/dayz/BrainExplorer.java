package edu.turtlekit2.warbot.dayz;
import java.util.List;
import java.util.Scanner;

import edu.turtlekit2.warbot.WarBrain;
import edu.turtlekit2.warbot.message.WarMessage;

/**
 * Classe BrainExplorer
 * @author Florian
 *
 */
public class BrainExplorer extends WarBrain{
	
	private Etat etatcourant;
	private String typeExplorer = "cueilleur";
	
	public BrainExplorer(){
	}
	
	@Override
	public String action() {
		broadcastMessage("WarBase", "present", null);
		
		etatcourant = new Rechercher(this);

		while (!etatcourant.etatFinal())
			etatcourant.exec(this);
		
		List<WarMessage> liste = getMessage();

		for(WarMessage wm : liste)
		{
			if(wm.getMessage().equals("espion"))
				typeExplorer = "espion";
			else if(wm.getMessage().equals("cueilleur"))
				typeExplorer = "cueilleur";
		}
		
		if(etatcourant instanceof Rapporter)
			return "give";
		else if(etatcourant instanceof TakeFood){
			return "take";}
		else 
			return "move";

	}
	
	void changerEtat(Etat etat)
	{
		etatcourant = etat;
	}
	
	public String getJob()
	{
		return typeExplorer;
	}
}
