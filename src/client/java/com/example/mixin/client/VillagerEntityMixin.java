package com.example.mixin.client;

import com.example.Modules.VillagerRollerModule;
import com.example.utils.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;


import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.util.ActionResult;

@Mixin(VillagerEntity.class)
public class VillagerEntityMixin {
	@Inject(at = @At("HEAD"), method = "interactMob(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;", cancellable = true)
    public void interactMob(CallbackInfoReturnable<ActionResult> cir) {
        if (VillagerRollerModule.currentState == VillagerRollerModule.State.WaitingForTargetVillager) {
        	VillagerRollerModule.currentState = VillagerRollerModule.State.RollingBreakingBlock;
        	VillagerRollerModule.rollingVillager = (VillagerEntity) (Object) this;
        	Util.cprint("We got your villager");
            cir.setReturnValue(ActionResult.CONSUME);
            cir.cancel();
        }
    }
}
