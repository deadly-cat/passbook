package home.ingvar.passbook.console.commands;

import java.util.Map;

import home.ingvar.passbook.console.Command;
import home.ingvar.passbook.console.CommandException;

public class Help extends Command {

	@Override
	public void execute(Map<String, Object> params) throws CommandException {
		if(params.containsKey("help")) {
			help();
			return;
		}
		validate(params);
		
		for(String cmd : list()) {
			System.out.println(cmd);
		}
	}

	@Override
	protected String[] requiredParams() {
		return null;
	}
	
	@Override
	public String toString() {
		return "Display command list and short description";
	}

}
