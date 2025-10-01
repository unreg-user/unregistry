package wta.util.mixins.mixin;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wta.util.utils.mixinInterfaces.ItemStackFI;

@Mixin(ItemGroup.EntriesImpl.class)
public class FabricItemGroupFixer {
	@Inject(
		  method = "add",
		  at = @At("HEAD"),
		  cancellable = true
	)
	private void unAdder(ItemStack stack, ItemGroup.StackVisibility visibility, CallbackInfo ci){
		if (((ItemStackFI) (Object) stack).unregistry$isUnregNull()){
			ci.cancel();
		}
	}
}
