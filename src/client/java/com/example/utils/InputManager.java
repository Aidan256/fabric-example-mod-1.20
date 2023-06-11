package com.example.utils;

import com.example.Modules.ModuleRegistry;
import com.example.Spaceclientmod;
import org.lwjgl.glfw.GLFW;


import net.minecraft.client.MinecraftClient;
import net.minecraft.world.GameMode;

public class InputManager {
	public static float tpdist=4;
	private static boolean shortKey=false;
	private static int usedKey=340;
	public static boolean render = true;
	public static void manageInputs() {
		MinecraftClient client = MinecraftClient.getInstance();
		shortKey = GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_KP_ENTER) == 1 ? true:false;
		
		
		if(GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(), usedKey)==0) {
			usedKey=340;
		}
		
		
		if(checkKey(GLFW.GLFW_KEY_KP_0,true)) {
			client.interactionManager.setGameMode(GameMode.byId(0));
			ModuleRegistry.getModule(ModuleRegistry.mods.FLYMODULE).setEnabled(false);
			ModuleRegistry.getModule(ModuleRegistry.mods.LOSPECIFICMODULE).setEnabled(true);
			
			
		}
		if(checkKey(GLFW.GLFW_KEY_KP_1,true)) {
			Spaceclientmod.LOGGER.info("orderfly");
			ModuleRegistry.getModule(ModuleRegistry.mods.FLYMODULE).setEnabled(true);
		}
		if(checkKey(GLFW.GLFW_KEY_R,true)) {
			render = !render;
		}
		
	}
	
	
	
	public static boolean checkKey(int key,boolean hotkeyRequired) {
		if((shortKey||!hotkeyRequired)&&GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(), key) == 1&&usedKey!=key) {
			usedKey = key;
			return true;
		}
		return false;
	}
	public static boolean isKeyDown(int key) {
		return GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(), key) == 1;
	}
}
