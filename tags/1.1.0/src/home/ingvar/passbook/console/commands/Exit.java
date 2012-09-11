package home.ingvar.passbook.console.commands;

import java.util.Map;

import home.ingvar.passbook.console.Command;
import home.ingvar.passbook.console.CommandException;
import home.ingvar.passbook.console.Parameter;

public class Exit extends Command {

	@Override
	public void execute(Map<Parameter, Object> params) throws CommandException {
		validate(params);
		
		System.out.println("Program was halt");
		System.exit(0);
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
		return "Exit the program";
	}
	
}
