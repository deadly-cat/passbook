package home.ingvar.passbook.console;

import home.ingvar.passbook.console.commands.Clear;
import home.ingvar.passbook.console.commands.ItemAdd;
import home.ingvar.passbook.console.commands.Exit;
import home.ingvar.passbook.console.commands.Help;
import home.ingvar.passbook.console.commands.Install;
import home.ingvar.passbook.console.commands.ItemDelete;
import home.ingvar.passbook.console.commands.ItemPasswordCopy;
import home.ingvar.passbook.console.commands.ItemsList;
import home.ingvar.passbook.console.commands.Login;
import home.ingvar.passbook.console.commands.Logout;
import home.ingvar.passbook.console.commands.Register;
import home.ingvar.passbook.console.commands.ItemShow;
import home.ingvar.passbook.console.commands.Unregister;
import home.ingvar.passbook.console.commands.ItemUpdate;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Command {
	
	private static final Map<String, Command> list = new HashMap<String, Command>();
	static {
		list.put("help", new Help());
		list.put("install", new Install());
		list.put("exit", new Exit());
		list.put("quit", list.get("exit"));
		list.put("register", new Register());
		list.put("unregister", new Unregister());
		list.put("login", new Login());
		list.put("logout", new Logout());
		list.put("add", new ItemAdd());
		list.put("update", new ItemUpdate());
		list.put("delete", new ItemDelete());
		list.put("show", new ItemShow());
		list.put("copy", new ItemPasswordCopy());
		list.put("list", new ItemsList());
		list.put("clear", new Clear());
	}
	
	public static Command newInstance(String name) throws InstantiationException {
		Command cmd = list.get(name);
		if(cmd == null) {
			throw new InstantiationException("Command " + name + " not found");
		}
		return cmd;
	}
	
	public static String[] list() {
		String[] names = new String[list.size()];
		int count = 0;
		for(Entry<String, Command> cmd : list.entrySet()) {
			names[count++] = cmd.getKey() + " - " + cmd.getValue();
		}
		return names;
	}
	
	public static Map<Parameter, Object> parseParams(String params) {
		Map<Parameter, Object> p = new HashMap<Parameter, Object>();
		Pattern pattern = Pattern.compile("^([A-z]+)|((?<= |^)-([A-z])|--([A-z]{2,}))( ((([^\\s-]+)[ -]?)+)(?= |$)|)");
		Matcher matcher = pattern.matcher(params);
		while(matcher.find()) {
			Parameter param = null;
			String value = null;
			if(matcher.group(1) != null) { //command
				param = Parameter.COMMAND;
				value = matcher.group(1).trim();
			}
			else {
				String name = matcher.group(4);
				if(name == null || name.isEmpty()) {
					name = matcher.group(5);
				}
				name = name.trim();
				param = Parameter.getByName(name);
				value = matcher.group(6);
			}
			
			if(param != null) {
				p.put(param, value);
			}
		}
		return p;
	}
	
	public abstract void execute(Map<Parameter, Object> params) throws CommandException;
	protected abstract Parameter[] requiredParams();
	protected abstract Parameter[] optionalParams();
	
	public void help() {
		//TODO: create help
		System.out.println(this);
		//TODO: write command line command -a value -b value [-c value] [-d value]
		System.out.println("\tRequired parameters:");
		if(requiredParams() != null) {
			for(Parameter p : requiredParams()) {
				System.out.println("\t\t"+p.longName);
			}
		} else {
			System.out.println("\t\tnone");
		}
	}
	
	protected void validate(Map<Parameter, Object> params) throws CommandException {
		Parameter[] required = requiredParams();
		if(required != null) {
			StringBuilder notSet = new StringBuilder();
			for(Parameter p : required) {
				if(!params.containsKey(p)) {
					if(notSet.length() > 0) {
						notSet.append(", ");
					}
					notSet.append(p.longName);
				}
			}
			if(notSet.length() > 0) {
				throw new CommandException("Required parameter(s) not set: " + notSet);
			}
		}
	}
	
}
