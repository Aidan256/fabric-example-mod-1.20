package com.example.mixin.client;

import com.example.Modules.ModuleRegistry;
import com.example.Modules.Module;
import com.example.utils.EventHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


import net.minecraft.client.network.ClientPlayerEntity;

@Mixin(ClientPlayerEntity.class)
public class OnTickGrabberMixin {
	@Inject(at = @At("HEAD"), method = "tick")
	public void tick(CallbackInfo info) {
		EventHandler.onTick();
		for (Module module : ModuleRegistry.getModules()) {
			if(module.isEnabled()) {
				module.onTick();
			}
		}
	}
}
