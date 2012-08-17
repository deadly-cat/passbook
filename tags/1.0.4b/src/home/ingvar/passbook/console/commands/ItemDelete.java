package home.ingvar.passbook.console.commands;

import java.util.Map;

import home.ingvar.passbook.console.CommandException;
import home.ingvar.passbook.console.Parameter;
import home.ingvar.passbook.console.UserCommand;
import home.ingvar.passbook.dao.ItemDAO;
import home.ingvar.passbook.dao.ResultException;
import home.ingvar.passbook.transfer.Item;
import home.ingvar.passbook.transfer.User;

public class ItemDelete extends UserCommand {

	@Override
	public void execute(Map<Parameter, Object> params) throws CommandException {
		validate(params);
		
		ItemDAO itemDAO = (ItemDAO) params.get(Parameter.ITEM_DAO);
		User owner = (User) params.get(Parameter.USER);
		Item delete = new Item();
		delete.setOwner(owner);
		delete.setService((String) params.get(Parameter.SERVICE));
		delete.setUsername((String) params.get(Parameter.USERNAME));
		try {
			itemDAO.delete(delete);
		} catch(ResultException e) {
			throw new CommandException(e);
		}
	}

	@Override
	protected Parameter[] requiredParams() {
		return new Parameter[] {Parameter.SERVICE, Parameter.USERNAME};
	}
	
	@Override
	protected Parameter[] optionalParams() {
		return null;
	}
	
	@Override
	public String toString() {
		return "Delete exist item";
	}

}
