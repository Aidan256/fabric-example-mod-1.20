package com.example.mixin.client;

import com.example.utils.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Direction;


@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	@Inject(method = "handleBlockBreaking", at = @At("HEAD"), cancellable = true)
	private void mix(boolean breaking, CallbackInfo ci) {
		if(Util.breakingBlock) {
			MinecraftClient client = MinecraftClient.getInstance();
			if(!(client.world.getBlockState(Util.breakPos) == Blocks.AIR.getDefaultState())) {
				Direction direction;
				if (!client.world.getBlockState(Util.breakPos).isAir() ) {
					client.interactionManager.updateBlockBreakingProgress(Util.breakPos, direction = Direction.UP);
	                client.particleManager.addBlockBreakingParticles(Util.breakPos, direction);
	                client.player.swingHand(Hand.MAIN_HAND);
	            }
				if(client.world.getBlockState(Util.breakPos).isAir()) {
					Util.breakingBlock=false;
				}
			}
			ci.cancel();
		}
	}
}
