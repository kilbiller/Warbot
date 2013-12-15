package edu.turtlekit2.warbot.dayz;

import java.util.List;

import edu.turtlekit2.warbot.WarBrain;
import edu.turtlekit2.warbot.message.WarMessage;
import edu.turtlekit2.warbot.percepts.Percept;

public class BrainBase extends WarBrain{
	
	private int nbExplorer;

	public BrainBase(){
	}

	@Override
	public String action() {
		nbExplorer = 0;
		if(!emptyBag()){
			return "eat";
		}

		List<WarMessage> liste = getMessage();
		
		for(WarMessage m : liste){
			if(m.getMessage().equals("present"))
			{
				if((nbExplorer % 2) == 0)
				{
					reply(m, "espion", null);
				}else
				{
					reply(m, "cueilleur", null);
				}
				nbExplorer = nbExplorer +1;
			}else
			{
				reply(m, "ici", null);
			}
		}
		
		if(getEnergy() > 12000){
			setNextAgentCreate("Explorer");
			return "create";
		}
		
		List<Percept> listeP = getPercepts();
		boolean ennemy = false;
		
		for(Percept p : listeP)
		{
			if((p.getTeam() != getTeam()) && (p.getType().equals("WarRocketLauncher")))
			{
				ennemy = true;
			}
		}
		
		if(ennemy == true)
		{
			broadcastMessage("WarRocketLauncher", "ennemy", null);
		}
		
		return "action";
	}
}
