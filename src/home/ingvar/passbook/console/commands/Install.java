package home.ingvar.passbook.console.commands;

import java.util.Map;

import home.ingvar.passbook.console.Command;
import home.ingvar.passbook.console.CommandException;
import home.ingvar.passbook.dao.DaoFactory;
import home.ingvar.passbook.dao.ResultException;


public class Install extends Command {

	@Override
	public void execute(Map<String, Object> params) throws CommandException {
		if(params.containsKey("help")) {
			help();
			return;
		}
		validate(params);
		
		try {
			DaoFactory factory = (DaoFactory) params.get("factory");
			factory.install();
			System.out.println("Database was created successful");
		} catch(ResultException e) {
			throw new CommandException(e);
		}
	}

	@Override
	protected String[] requiredParams() {
		return null;
	}
	
	@Override
	public String toString() {
		return "Create database and initialize its structure";
	}

}
