package com.example.mixin.client;

import com.example.utils.EventHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.block.AbstractBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(AbstractBlock.AbstractBlockState.class)
public class AbstractBlockMixin {
	@Inject(method = "onBlockBreakStart(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/PlayerEntity;)V", at = @At("HEAD"))
	private void blockInj(World world, BlockPos pos, PlayerEntity player,CallbackInfo ci) {
		EventHandler.onBlockBreakStart(pos);
	}
}
