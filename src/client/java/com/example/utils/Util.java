package com.example.utils;

import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class Util {
	public static boolean breakingBlock = false;
	public static BlockPos breakPos;
	public static int getBit(int num, int bit) {
	    return (num >> bit) & 1;
	}
	public static double roundCoordinate(double n) {
		n = Math.round(n * 100) / 100d;  // Round to 1/100th
	    return Math.nextAfter(n, n + Math.signum(n));  // Fix floating point errors
    }
	public static void cprint(String msg) {
		MinecraftClient.getInstance().player.sendMessage(Text.of(msg));
	}
	public static void breakBlock(BlockPos pos) {
		breakingBlock = true;
		breakPos = pos;
	}
	public static void placeBlock(BlockPos pos) {
		MinecraftClient client = MinecraftClient.getInstance();
		ItemStack itemStack = client.player.getStackInHand(Hand.MAIN_HAND);
		BlockHitResult blockHitResult = new BlockHitResult(new Vec3d(pos.getX(),pos.getY()-1,pos.getZ()), Direction.UP, pos.subtract(new Vec3i(0,1,0)), false);
        int i = itemStack.getCount();
        ActionResult actionResult2 = client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, blockHitResult);
        if (actionResult2.isAccepted()) {
            if (actionResult2.shouldSwingHand()) {
                client.player.swingHand(Hand.MAIN_HAND);
                if (!itemStack.isEmpty() && (itemStack.getCount() != i || client.interactionManager.hasCreativeInventory())) {
                    client.gameRenderer.firstPersonRenderer.resetEquipProgress(Hand.MAIN_HAND);
                }
            }
            return;
        }
        
       
	}
}
