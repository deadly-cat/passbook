package home.ingvar.passbook.lang;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class Creator {

	public static void main(String[] args) throws IOException {
		generateI18n(getLangFile("passbook_en.properties"), "Labels");
	}
	
	public static void generateI18n(File file, String className) throws IOException {
		StringBuilder cnt = new StringBuilder(); //content
		cnt.append(Creator.class.getPackage()).append(";\n\n");
		cnt.append("public interface ").append(className).append(" {\n\n");
		
		Scanner in = new Scanner(file);
		while(in.hasNextLine()) {
			String line = in.nextLine().trim();
			if(!line.isEmpty()) {
				String[] m = line.split("=");
				String name = m[0].trim().replaceAll("\\.", "_").replaceAll("-", "_").toUpperCase();
				cnt.append("\tString ").append(name).append(" = \"").append(m[0].trim()).append("\";\n");
			}
		}
		cnt.append("\n}\n");
		
		File classFile = new File(file.getParent().replaceAll("bin", "src") + "/" + className + ".java");
		FileOutputStream out = new FileOutputStream(classFile);
		out.write(cnt.toString().getBytes("UTF-8"));
		out.flush();
		out.close();
		System.out.println("File created: " + classFile.getName());
	}
	
	private static File getLangFile(String name) {
		return new File(Creator.class.getResource(name).getFile());
	}
	
}
