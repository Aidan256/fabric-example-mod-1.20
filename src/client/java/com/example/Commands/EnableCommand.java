package com.example.Commands;

import com.example.Modules.ModuleRegistry;
import com.example.Modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class EnableCommand extends Command {
	public EnableCommand() {
		super("Enable","Enables a module","enable","e");
	}
	@Override
	public void execute(String[] args) {
		if(args.length==0) {
			MinecraftClient.getInstance().player.sendMessage(Text.of("Provide module name"));
			return;
		}
		String modName = args[0];
		for(Module module : ModuleRegistry.getModules()) {
			if(module.name.equalsIgnoreCase(modName)){
				module.setEnabled(true);
				MinecraftClient.getInstance().player.sendMessage(Text.of("Enabled "+modName));
				return;
			}
		}
		MinecraftClient.getInstance().player.sendMessage(Text.of("Module not found"));
	}

}
