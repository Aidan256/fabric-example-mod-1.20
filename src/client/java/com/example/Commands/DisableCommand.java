package com.example.Commands;


import com.example.Modules.ModuleRegistry;
import com.example.Modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class DisableCommand extends Command {
	public DisableCommand() {
		super("Disable","Disables a module","disable","d");
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
				module.setEnabled(false);
				MinecraftClient.getInstance().player.sendMessage(Text.of("Disabled "+modName));
				return;
			}
		}
		MinecraftClient.getInstance().player.sendMessage(Text.of("Module not found"));
	}

}
