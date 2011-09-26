package home.ingvar.passbook.console.commands;

import home.ingvar.passbook.console.Command;
import home.ingvar.passbook.console.CommandException;
import home.ingvar.passbook.console.Parameter;

import java.util.Map;

public class Clear extends Command {

	@Override
	public void execute(Map<Parameter, Object> params) throws CommandException {
		validate(params);
		
		StringBuilder cleaner = new StringBuilder();
		for(int i = 0; i < 500; i++) {
			cleaner.append("\n");
		}
		System.out.println(cleaner);
	}

	@Override
	protected Parameter[] requiredParams() {
		return null;
	}
	
	@Override
	protected Parameter[] optionalParams() {
		return null;
	}
	
	@Override
	public String toString() {
		return "Clear console";
	}

}
