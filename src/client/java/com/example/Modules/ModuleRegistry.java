package com.example.Modules;

import java.util.ArrayList;
import java.util.List;


import net.minecraft.util.Util;

public class ModuleRegistry {
	private static final List<Module> modules = Util.make(new ArrayList<>(),ModuleRegistry::initModules);
	
	private static void initModules(List<Module> modules) {

		modules.add(new VillagerRollerModule());

	}
	public static List<Module> getModules(){
		return modules;
	}
	public static Module getModule(mods mod){
		return modules.get(mod.id);
	}
	public static void disableAll() {
		for(Module mod : modules) {
			mod.setEnabled(false);
		}
	}
	
	
	
	
	public enum mods{
		FLYMODULE(0),
		BOATFLYMODULE(1),
		SHORTTPMODULE(2),
		VILLAGERROLLERMODULE(3),
		LOSPECIFICMODULE(4),
		WORLDGUARDBYPASSMODULE(5),
		VAULTBOATMODULE(6),
		;
		private mods(int index) {
			id = index;
		}
		private final int id;
	}
	
}
