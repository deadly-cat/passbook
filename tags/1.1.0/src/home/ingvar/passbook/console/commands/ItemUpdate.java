package home.ingvar.passbook.console.commands;

import java.util.Map;

import home.ingvar.passbook.console.CommandException;
import home.ingvar.passbook.console.Parameter;
import home.ingvar.passbook.console.UserCommand;
import home.ingvar.passbook.dao.ItemDAO;
import home.ingvar.passbook.dao.ResultException;
import home.ingvar.passbook.transfer.Item;
import home.ingvar.passbook.transfer.User;

public class ItemUpdate extends UserCommand {

	@Override
	public void execute(Map<Parameter, Object> params) throws CommandException {
		validate(params);
		
		ItemDAO itemDAO = (ItemDAO) params.get(Parameter.ITEM_DAO);
		User owner = (User) params.get(Parameter.USER);
		
		Item update = new Item();
		update.setOwner(owner);
		update.setId(Long.parseLong((String) params.get(Parameter.ID)));
		update.setService((String) params.get(Parameter.SERVICE));
		update.setUsername((String) params.get(Parameter.USERNAME));
		update.setPassword((String) params.get(Parameter.PASSWORD));
		update.setComment((String) params.get(Parameter.COMMENT));
		try {
			itemDAO.update(update);
		} catch(ResultException e) {
			throw new CommandException(e);
		}
	}

	@Override
	protected Parameter[] requiredParams() {
		return new Parameter[] {Parameter.ID};
	}
	
	@Override
	protected Parameter[] optionalParams() {
		return new Parameter[] {Parameter.SERVICE, Parameter.USERNAME, Parameter.PASSWORD, Parameter.COMMENT};
	}
	
	@Override
	public String toString() {
		return "Update exist item";
	}

}
