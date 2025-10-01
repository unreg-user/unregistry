package wta.util.mixins.mixin;

import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wta.util.AllInit;

@Mixin(Registries.class)
public class RegistriesFixer {
	@Inject(
		  method = "<clinit>",
		  at = @At("RETURN")
	)
	private static void adderNulls(CallbackInfo ci){
		AllInit.registryNullInit();
	}
}
