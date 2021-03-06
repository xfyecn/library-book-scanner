package io.github.shardbytes.lbs.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;

import io.github.shardbytes.lbs.gui.terminal.TermUtils;
import io.github.shardbytes.lbs.objects.Group;

public class ClassDatabase{
	
	private static ClassDatabase instance;
	
	private TreeMap<String, ArrayList<Group>> classList;
	
	static{
		instance = new ClassDatabase();
	}
	
	private ClassDatabase(){
		classList = new TreeMap<>();
	}
	
	public static ClassDatabase getInstance(){
		return instance;
	}
	
	public TreeMap<String, ArrayList<Group>> getClassList(){
		return classList;
	}
	
	public void load(String path){
		StringBuilder sb = new StringBuilder();
		try(FileInputStream input = new FileInputStream(new File(path))){
			/*
			 * Files.lines(new File(Load.C_DATABASE_PATH).toPath()).forEach(sb::append);
			 * rip Java 8 streams solution
			 */
			
			CharsetDecoder decoder = Charset.forName("ISO-8859-2").newDecoder();
			decoder.onMalformedInput(CodingErrorAction.IGNORE);
			
			InputStreamReader reader = new InputStreamReader(input, decoder);
			BufferedReader bufferedReader = new BufferedReader(reader);
			
			String temp = bufferedReader.readLine();
			while(temp != null){
				sb.append(temp);
				temp = bufferedReader.readLine();
			}
			
			bufferedReader.close();
			reader.close();
			
			JSONObject obj = new JSONObject(sb.toString());
			JSONArray namesArr = obj.names();
			ArrayList<String> names = new ArrayList<>();
			
			/*
			 * Debugging stuff to write everything out:
			 *
			 * System.out.println("namesArr = " + namesArr);
			 *
			 * for(Object o : namesArr){
			 * 	System.out.println("o = " + o);
			 * 	System.out.println("o.toString() = " + o.toString());
			 * }
			 */
			
			/*
			 * Change JSONArray (Object) to ArrayList<String>
			 */
			namesArr.forEach((name) -> names.add(name.toString()));
			
			/*
			 * Clear classList to remove duplicates
			 */
			classList.clear();
			
			names.forEach((name) -> {
				ArrayList<Group> arg = new ArrayList<>();
				
				obj.getJSONArray(name).forEach((group) -> arg.add(new Group(group.toString(), name)));
				classList.put(name, arg);
				
			});
			TermUtils.println("Class database loaded");
			
		}catch(Exception e){
			classList.clear();
			TermUtils.printerr("Cannot load class database");
		}
		
	}
	
	public void save(String path){
		JSONObject obj = new JSONObject();
		
		if(!classList.isEmpty()){
			classList.forEach((name, arl) -> {
				if(!arl.isEmpty()){
					JSONArray arr = new JSONArray();
					arl.forEach((group) -> arr.put(group.getName()));
					obj.put(name, arr);
					
				}
				
			});
			
		}
		
		try{
			Path p = Paths.get(path);
			Files.write(p, obj.toString().getBytes(Charset.forName("ISO-8859-2")));
		}catch(IOException e){
			TermUtils.printerr("Cannot save class database");
			TermUtils.printerr(e.getMessage());
		}
		
		TermUtils.println("Class database saved");
		
	}
	
}
