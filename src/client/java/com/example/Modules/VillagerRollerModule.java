package com.example.Modules;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;


import com.example.utils.Util;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.VillagerProfession;

public class VillagerRollerModule extends Module {
	
	private static File desiredTradesFile;
	
	private class RollerEnchantment{
		
		public Enchantment enchant;
		public int level;
		public int maxPrice;
		
		public RollerEnchantment(Enchantment enchant, int level, int maxPrice) {
			this.enchant=enchant;
			this.level=level;
			this.maxPrice=maxPrice;
		}
	}
	
	public static ArrayList<RollerEnchantment> desiredTrades = new ArrayList<RollerEnchantment>();
	
	
	public static State currentState = State.Disabled;
	private static BlockPos rollingBlockPos;
	private static Block rollingBlock;
	public static VillagerEntity rollingVillager;
	
	private static MinecraftClient client;
	
	public VillagerRollerModule() {
		super("villroll", "finds the good trades", ModuleType.TEST);
	}

	@Override
	public void onEnable() {
		currentState = State.WaitingForTargetBlock;
		Util.cprint("Attack block you want to roll");
		System.out.println("test");
		client = MinecraftClient.getInstance();
		refreshTradeList();
	}

	private void refreshTradeList() {
		desiredTrades.clear();
		desiredTradesFile = new File("desiredTradesFile.txt");
		try {
			if (desiredTradesFile.createNewFile()) {
			    System.out.println("File created: " + desiredTradesFile.getName());
			  } else {
			    System.out.println("File already exists.");

			  }
		} catch (IOException e) {
			Util.cprint("error occured opening file");
			e.printStackTrace();
			return;
		}
		try {
			try (Scanner myReader = new Scanner(desiredTradesFile)) {
				while (myReader.hasNextLine()) {
				    String data = myReader.nextLine();
				    String[] messageSplit = data.trim().split(":");
				    
				    desiredTrades.add(new RollerEnchantment(Registries.ENCHANTMENT.get(new Identifier(messageSplit[0])), messageSplit.length>1 ? Integer.parseInt(messageSplit[1]):0, messageSplit.length>2 ? Integer.parseInt(messageSplit[2]):65));
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			Util.cprint("error occured scanning file");
			e.printStackTrace();
			return;
		}
		
	}
	
	private static void removeEnchantFromList(int i) throws IOException {
		desiredTrades.remove(i);
		File inputFile = desiredTradesFile;
        File tempFile = new File("myTempFile.txt");

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        
        String currentLine;
        int j = 0;
        while((currentLine = reader.readLine()) != null) {
            if(j==i) {
            	j++;
            	continue;
            }
            writer.write(currentLine + System.getProperty("line.separator"));
            j++;
        }
        writer.close(); 
        reader.close(); 
        inputFile.delete();
        boolean successful = tempFile.renameTo(inputFile);
        System.out.println(successful);
	}
	
	@Override
	public void onDisable() {
		currentState = State.Disabled;

	}
	private int tickCount;
	private final int delay=5;
	Vec3d oldPos;
	@Override
	public void onTick() {
		
		
		switch(currentState) {
			case RollingBreakingBlock:
				assert client.world != null;
	            if (client.world.getBlockState(rollingBlockPos) == Blocks.AIR.getDefaultState()) {
	                currentState = State.RollingWaitingForVillagerProfessionClear;
	                
                	oldPos = new Vec3d(client.player.getBlockPos().getX()+0.5,client.player.getPos().getY(),client.player.getBlockPos().getZ()+0.5);
                	client.player.setPos(rollingBlockPos.getX()+0.5,rollingBlockPos.getY(),rollingBlockPos.getZ()+0.5);
                	tickCount = 0;
	                
	            } else {
	            	
	            	client.player.getInventory().selectedSlot = 8;
	                Util.breakBlock(rollingBlockPos);
	            }
				break;
			
			case RollingWaitingForVillagerProfessionClear:
				if (rollingVillager.getVillagerData().getProfession() == VillagerProfession.NONE) {
					
	                currentState = State.RollingPlacingBlock;
	                
	            }
				tickCount = 0;
				break;
			
			case RollingPlacingBlock:
				client.player.getInventory().selectedSlot = 7;
				if(tickCount>=delay) {
					client.player.setPosition(oldPos);
				}
				if(tickCount>(delay*2)) {
					
					Util.placeBlock(rollingBlockPos);
					currentState = State.RollingWaitingForVillagerProfessionNew;
				}
				tickCount++;
				break;
			
			case RollingWaitingForVillagerProfessionNew:
				if (rollingVillager.getVillagerData().getProfession() != VillagerProfession.NONE) {
	                currentState = State.RollingWaitingForVillagerTrades;
	                assert client.interactionManager != null;
	                client.interactionManager.interactEntity(client.player, rollingVillager, Hand.MAIN_HAND);
	            }
				break;
			
			default:
				break;
		}
		
		
	}
	public static void onBlockBreakStart(BlockPos pos) {
		if(currentState==State.WaitingForTargetBlock) {
			rollingBlockPos = pos;
			assert client.world!=null;
			rollingBlock = client.world.getBlockState(pos).getBlock();
			currentState = State.WaitingForTargetVillager;
			Util.cprint("Rolling block selected, now interact with villager you want to roll");
		}
		
	}
	
	public static void triggerTradeCheck(TradeOfferList offers) {
		for(TradeOffer offer : offers) {
			if(!offer.getSellItem().getName().getString().equals("Enchanted Book")) continue;
			
			Map<Enchantment, Integer> offerEnchants = EnchantmentHelper.get(offer.getSellItem());
			for (Map.Entry<Enchantment, Integer> enchant : offerEnchants.entrySet()) {
				
				for(int i=0;i<desiredTrades.size();i++) {
					RollerEnchantment desired = desiredTrades.get(i);
					if(enchant.getKey()!=desired.enchant||offer.getOriginalFirstBuyItem().getCount() >desired.maxPrice||(desired.level==0&&enchant.getValue()!=desired.enchant.getMaxLevel()||(desired.level>0&&enchant.getValue()!=desired.level))) continue;
					try {
						removeEnchantFromList(i);
					} catch (IOException e) {
						
						e.printStackTrace();
					}
					Util.cprint("Ladies and gentlemen, we got him");
					currentState = State.Disabled;
					return;
				}
			}
		}
		currentState = State.RollingBreakingBlock;
		assert client.player != null;
        client.player.closeHandledScreen();
        currentState = State.RollingBreakingBlock;
	}
	
	public static enum State {
        Disabled,
        WaitingForTargetBlock,
        WaitingForTargetVillager,
        RollingBreakingBlock,
        RollingWaitingForVillagerProfessionClear,
        RollingPlacingBlock,
        RollingWaitingForVillagerProfessionNew,
        RollingWaitingForVillagerTrades
    }

	
}
