package wta.util.mixins.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wta.util.utils.RegType;
import wta.util.utils.UnRegistry;

@Mixin(Items.class)
public class Test {
	@Shadow @Mutable @Final public static Item STICK;

	@Inject(
		  method = "<clinit>",
		  at = @At("RETURN")
	)
	private static void reloader(CallbackInfo ci){
		STICK=UnRegistry.register(
			  Registries.ITEM,
			  Identifier.ofVanilla("stick"),
			  null,
			  RegType.UNREG
		);
	}
}
