package com.example.utils;


import com.example.Modules.ModuleRegistry;
import com.example.Modules.VillagerRollerModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

public class EventHandler {
	public static void onTick() {
		InputManager.manageInputs();
	}
	public static void onJoin() {
		ModuleRegistry.disableAll();
		ModuleRegistry.getModule(ModuleRegistry.mods.SHORTTPMODULE).setEnabled(true);

		
		//client.player.dismountVehicle();
	}
	public static void onBlockBreakStart(BlockPos pos) {
		VillagerRollerModule.onBlockBreakStart(pos);
		
	}
}
