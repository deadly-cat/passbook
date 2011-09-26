package home.ingvar.passbook.console.commands;

import java.util.Map;

import home.ingvar.passbook.console.CommandException;
import home.ingvar.passbook.console.UserCommand;
import home.ingvar.passbook.dao.ItemDAO;
import home.ingvar.passbook.dao.ResultException;
import home.ingvar.passbook.transfer.Item;
import home.ingvar.passbook.transfer.User;

public class ItemUpdate extends UserCommand {

	@Override
	public void execute(Map<String, Object> params) throws CommandException {
		if(params.containsKey("help")) {
			help();
			return;
		}
		validate(params);
		
		ItemDAO itemDAO = (ItemDAO) params.get("itemDAO");
		User owner = (User) params.get("user");
		
		Item update = new Item();
		update.setOwner(owner);
		update.setService((String) params.get("service"));
		update.setUsername((String) params.get("username"));
		update.setPassword((String) params.get("password"));
		update.setComment((String) params.get("comment"));
		try {
			itemDAO.update(update);
		} catch(ResultException e) {
			throw new CommandException(e);
		}
	}

	@Override
	protected String[] requiredParams() {
		return new String[] {"service", "username"};
	}
	
	@Override
	public String toString() {
		return "Update exist item";
	}

}
