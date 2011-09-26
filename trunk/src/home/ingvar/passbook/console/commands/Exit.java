package home.ingvar.passbook.console.commands;

import java.util.Map;

import home.ingvar.passbook.console.Command;
import home.ingvar.passbook.console.CommandException;

public class Exit extends Command {

	@Override
	public void execute(Map<String, Object> params) throws CommandException {
		if(params.containsKey("help")) {
			help();
			return;
		}
		validate(params);
		
		System.out.println("Program was halt");
		System.exit(0);
	}

	@Override
	protected String[] requiredParams() {
		return null;
	}
	
	@Override
	public String toString() {
		return "Exit the program";
	}
	
}
