package wta.util.mixins.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.CrafterBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wta.util.utils.RegType;
import wta.util.utils.UnRegistry;

@Mixin(Blocks.class)
public class Test {
	@Shadow @Final @Mutable public static Block CRAFTER;

	@Inject(
		  method = "<clinit>",
		  at = @At("RETURN")
	)
	private static void reloader(CallbackInfo ci){
		CRAFTER=UnRegistry.register(
			  Registries.BLOCK,
			  Identifier.ofVanilla("crafter"),
			  null,
			  RegType.UNREG
		);
	}

	@Mixin(BlockEntityType.class)
	private static class beTest{
		/*/@Shadow @Final @Mutable public static BlockEntityType<CrafterBlockEntity> CRAFTER;

		@Inject(
			  method = "<clinit>",
			  at = @At("RETURN")
		)
		private static void reloader(CallbackInfo ci){
			CRAFTER=UnRegistry.register(
				  Registries.BLOCK_ENTITY_TYPE,
				  Identifier.ofVanilla("crafter"),
				  null,
				  RegType.UNREG
			);
		}/*/
	}
}
