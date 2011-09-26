package home.ingvar.passbook.console.commands;

import home.ingvar.passbook.console.Command;
import home.ingvar.passbook.console.CommandException;

import java.util.Map;

public class Clear extends Command {

	@Override
	public void execute(Map<String, Object> params) throws CommandException {
		if(params.containsKey("help")) {
			help();
			return;
		}
		validate(params);
		
		StringBuilder cleaner = new StringBuilder();
		for(int i = 0; i < 500; i++) {
			cleaner.append("\n");
		}
		System.out.println(cleaner);
	}

	@Override
	protected String[] requiredParams() {
		return null;
	}
	
	@Override
	public String toString() {
		return "Clear console";
	}

}
