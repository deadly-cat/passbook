package home.ingvar.passbook.console;

public enum Parameter {
	
	COMMAND(null, "command", null),
	HELP("h", "help", "Display information about command"),
	ID("i", "id", "Unique identifier"),
	USERNAME("u", "username", "Set username"),
	PASSWORD("p", "password", "Set password"),
	FULLNAME("f", "fullname", "Set fullname"),
	SERVICE("s", "service", "Set service"),
	COMMENT("c", "comment", "Set comment"),
	USER(null, "user", "Current user"),
	FACTORY(null, "factory", "Dao factory instance"),
	USER_DAO(null, "userDAO", "User DAO instance"),
	ITEM_DAO(null, "itemDAO", "Item DAO instance");
	
	public final String shortName;
	public final String longName;
	public final String description;
	
	public static Parameter getByName(String name) {
		for(Parameter p : Parameter.values()) {
			if(name.equals(p.shortName) || name.equals(p.longName)) {
				return p;
			}
		}
		return null;
	}
	
	Parameter(String shortName, String longName, String description) {
		this.shortName = shortName;
		this.longName  = longName;
		this.description = description;
	}
	
}
