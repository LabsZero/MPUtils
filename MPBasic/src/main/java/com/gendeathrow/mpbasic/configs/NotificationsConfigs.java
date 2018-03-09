package com.gendeathrow.mpbasic.configs;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.gendeathrow.mpbasic.core.MPBasic;
import com.gendeathrow.mputils.utils.MPFileUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public class NotificationsConfigs {

	public static File notification = new File(MPBConfigHandler.MPBConfigDir, "onscreen_notifications.json");
	
	public static HashMap<String, NotificationObject> LoadedNotifications = new HashMap<String, NotificationObject>();
	
	public static void load() {
			if(!notification.exists()) CreateDirectory();
			try {
				readJson();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
	}

	public static void reloadConfig() {
		
		LoadedNotifications.clear();
		
		try {
			readJson();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected static void readJson() throws IOException {
		
		String fileRead = MPFileUtils.readFile(notification);
		
		JsonObject json = new Gson().fromJson(fileRead, JsonObject.class);

		if(json.has("onScreenNotifications")) {
			JsonArray notifications = json.get("onScreenNotifications").getAsJsonArray();
				for(int i = 0; i < notifications.size(); i++)
				{
					JsonObject single = notifications.get(i).getAsJsonObject();
					
					String id = null;
					String soundFile = null;
					List<String> lines = new ArrayList<String>();
					String itemID = ItemStack.EMPTY.getItem().getRegistryName().toString();
					int bgColor = Color.GRAY.getRGB();
					int borderColor = Color.BLACK.getRGB();
					
					if(single.has("id"))
						id = single.get("id").getAsString();
					
					if(single.has("soundlocation"))
						soundFile = single.get("soundlocation").getAsString();
					
					if(single.has("itemstack"))
						itemID = single.get("itemstack").getAsString();
					
					if(single.has("bgColor")) {
						try {
							bgColor = Color.decode(single.get("bgColor").getAsString()).getRGB();
						}catch(NumberFormatException e) {
							MPBasic.logger.error("Error converting Hex number bgColor : '"+ single.get("bgColor").getAsString()+"'");
							e.printStackTrace();
						}
						
					}
					
					if(single.has("borderColor")) {
						try {
							borderColor = Color.decode(single.get("borderColor").getAsString()).getRGB();
						}catch(NumberFormatException e) {
							MPBasic.logger.error("Error converting Hex number borderColor : '"+ single.get("bgColor").getAsString()+"'");
							e.printStackTrace();
						}

					}
					
					if(single.has("lines")) {
						JsonArray jsonlist = single.get("lines").getAsJsonArray();
						Iterator<JsonElement> itr = jsonlist.iterator();
						while(itr.hasNext()) {
							String nx = itr.next().getAsString();
							lines.add(nx);
						}
					}
					
					if(id != null && !lines.isEmpty())
						LoadedNotifications.put(id, new NotificationObject(id, itemID, soundFile, lines, bgColor, borderColor));
						
				}
		}

	}
	
	protected static void CreateDirectory() {
		if(!MPBConfigHandler.MPBConfigDir.exists()) MPBConfigHandler.MPBConfigDir.mkdirs();
		generateExamples();
	}
		
	/**
	 * Creates Default ChangeLogs
	 */
	private static void generateExamples()
	{
		if (notification.getParentFile() != null) {
			notification.getParentFile().mkdirs();
		}

		JsonObject json = new JsonObject();
			JsonArray notificationList = new JsonArray();
				JsonObject darkostoObject = new JsonObject();
					darkostoObject.addProperty("id","darkosto");
					//darkostoObject.addProperty("itemstack", Items.CAKE.getRegistryName().toString());
					darkostoObject.addProperty("soundlocation", SoundEvents.ENTITY_PLAYER_LEVELUP.getRegistryName().toString());
					
					darkostoObject.addProperty("bgColor","#"+ Integer.toHexString(Color.GRAY.getRGB()).substring(2));
					darkostoObject.addProperty("borderColor","#"+ Integer.toHexString(Color.BLACK.getRGB()).substring(2));
					JsonArray darkLines = new JsonArray();
						darkLines.add(TextFormatting.BLUE + "" + TextFormatting.UNDERLINE + "Happy Birthday!!!");
						darkLines.add(TextFormatting.DARK_GREEN + "Happy Birthday to you!");
						darkLines.add(TextFormatting.DARK_GREEN +"Happy Birthday to you!");
						darkLines.add(TextFormatting.DARK_PURPLE +"Happy Birthday Darkosto!");
						darkLines.add(TextFormatting.DARK_GREEN +"Happy Birthday to you!");
					darkostoObject.add("lines", darkLines);
				notificationList.add(darkostoObject);
				JsonObject exampleObject = new JsonObject();
					exampleObject.addProperty("id","example");
					exampleObject.addProperty("soundlocation", SoundEvents.ENTITY_CREEPER_PRIMED.getRegistryName().toString());
					JsonArray exampleLines = new JsonArray();
						exampleLines.add(TextFormatting.RED + "" + TextFormatting.UNDERLINE +"Boom!!!");
						exampleLines.add(TextFormatting.YELLOW +"Did I scare you?");
					exampleObject.add("lines", exampleLines);
				notificationList.add(exampleObject);
		json.add("onScreenNotifications", notificationList);	
			
		
		List<String> jsonLines = new ArrayList<String>();
		jsonLines.add(new GsonBuilder().setPrettyPrinting().create().toJson(json));

		try {
			MPFileUtils.createSaveTextFile(notification, jsonLines);
		} catch (IOException e) {
				e.printStackTrace();
		}
	}
	
	
	public static class NotificationObject {
		
		public String id;
		public String itemstackID;
		public String soundLocation;
		public List<String> lines;
		public int bgColor;
		public int borderColor;
		
		public NotificationObject(String idIn, String itemIn, String soundLocationIn, List<String> linesIn, int bgColorIn, int borderColorIn) {
			this.id = idIn;
			this.itemstackID = itemIn;
			this.soundLocation = soundLocationIn;
			this.lines = linesIn;
			this.bgColor = bgColorIn;
			this.borderColor = borderColorIn;
		}
	}
}
