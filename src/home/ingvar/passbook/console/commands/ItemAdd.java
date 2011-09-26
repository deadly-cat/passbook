package home.ingvar.passbook.console.commands;

import home.ingvar.passbook.console.CommandException;
import home.ingvar.passbook.console.UserCommand;
import home.ingvar.passbook.dao.ItemDAO;
import home.ingvar.passbook.dao.ResultException;
import home.ingvar.passbook.transfer.Item;
import home.ingvar.passbook.transfer.User;

import java.util.Map;

public class ItemAdd extends UserCommand {

	@Override
	public void execute(Map<String, Object> params) throws CommandException {
		if(params.containsKey("help")) {
			help();
			return;
		}
		validate(params);
		
		ItemDAO itemDAO = (ItemDAO) params.get("itemDAO");
		Item item = new Item();
		item.setOwner((User) params.get("user"));
		item.setService((String) params.get("service"));
		item.setUsername((String) params.get("username"));
		item.setPassword((String) params.get("password"));
		item.setComment((String) params.get("comment"));
		try {
			itemDAO.add(item);
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
		return "Add new item";
	}
	
}
