package com.example.Modules;


import com.example.SpaceMod;

public abstract class Module {
	
	public final String name, description;
	
	public final ModuleType type;
	
	public boolean enabled = false;
	
	
	public Module(String name, String description, ModuleType type) {
		this.name=name;
		this.description=description;
		this.type=type;
	}
	
	public final boolean isEnabled() {return enabled;}
	
	public final void setEnabled(boolean enable) {
		SpaceMod.LOGGER.info("SetenabledFunction, currently enabled is "+this.enabled);
		if(enabled==enable) {
			return;
		}
		enabled=enable;
		if(enabled) {
			onEnable();
		}else {
			onDisable();
		}
	}
	
	public abstract void onEnable();
	
	public abstract void onDisable();
	
	public abstract void onTick();
	
	public enum ModuleType {
		TEST("Testing");
		final String name;
		ModuleType(String name){
			this.name = name;
		}
		public String getName() {
			return name;
		}
	}
}
