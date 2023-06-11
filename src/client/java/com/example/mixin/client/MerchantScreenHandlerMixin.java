package com.example.mixin.client;

import com.example.Modules.VillagerRollerModule;
import com.example.Modules.VillagerRollerModule.State;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;


import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.village.TradeOfferList;

@Mixin(MerchantScreenHandler.class)
public class MerchantScreenHandlerMixin {
    @Inject(at = @At("TAIL"), method = "setOffers(Lnet/minecraft/village/TradeOfferList;)V", cancellable = false)
    public void setOffers(TradeOfferList offers, CallbackInfo ci) {
	    if(VillagerRollerModule.currentState==State.RollingWaitingForVillagerTrades) {
	    	//((MerchantScreenHandler)(Object)this).close(MinecraftClient.getInstance().player);
	        VillagerRollerModule.triggerTradeCheck(offers);
	        //VillagerRollerModule.currentState = State.Disabled;
    	}
    }
}