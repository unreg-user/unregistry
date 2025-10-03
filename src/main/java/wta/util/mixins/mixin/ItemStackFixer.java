package wta.util.mixins.mixin;

import net.fabricmc.fabric.api.item.v1.FabricItemStack;
import net.minecraft.component.ComponentHolder;
import net.minecraft.component.ComponentMapImpl;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wta.util.utils.UnregistryNull;
import wta.util.utils.mixinInterfaces.RegistryContainerFI;

@Mixin(ItemStack.class)
public abstract class ItemStackFixer implements ComponentHolder, FabricItemStack, RegistryContainerFI {
	@Shadow @Final @Mutable private Item item;
	@Shadow @Final @Mutable ComponentMapImpl components;
	@Shadow private int count;

	@Unique private boolean isUnregNull=false;

	@Inject(
		  method = "<init>(Lnet/minecraft/item/ItemConvertible;ILnet/minecraft/component/ComponentMapImpl;)V",
		  at = @At("RETURN")
	)
	private void nuller(ItemConvertible item, int count, ComponentMapImpl components, CallbackInfo ci){
		if (item instanceof UnregistryNull) this.isUnregNull = true;
	}

	@Override
	public boolean unregistry$valueIsUnregNull(){
		return isUnregNull;
	}
}
