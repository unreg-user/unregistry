package wta.util.mixins.mixin;

import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.Property;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wta.util.utils.UnregistryNull;
import wta.util.utils.mixinInterfaces.RegistryContainerFI;
import wta.util.utils.mixinInterfaces.SimpleRegistryFI;

@Mixin(AbstractBlock.AbstractBlockState.class)
public class AbstractBlockStateFixer implements RegistryContainerFI {
	@Unique private boolean isUnregNull=false;

	/*/@ModifyArg(
		  method = "<init>",
		  at = @At(
				value = "INVOKE",
				target = "Lnet/minecraft/state/State;<init>(Ljava/lang/Object;Lit/unimi/dsi/fastutil/objects/Reference2ObjectArrayMap;Lcom/mojang/serialization/MapCodec;)V"
		  ),
		  index=0
	)
	private static Object setBlockToAir(Object block) {
		return UnregistryNull.setUnregNull(Registries.BLOCK, (Block) block);
	}/*/

	@Inject(
		  method = "<init>",
		  at = @At(
			    value = "INVOKE",
			    target = "Lnet/minecraft/fluid/Fluid;getDefaultState()Lnet/minecraft/fluid/FluidState;"
		  )
	)
	private void InitIsUnregNull(Block block, Reference2ObjectArrayMap<Property<?>, Comparable<?>> propertyMap, MapCodec<BlockState> codec, CallbackInfo ci) {
		isUnregNull=block instanceof UnregistryNull;
	}

	/*/@ModifyVariable(
		  method = "<init>",
		  at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/Fluid;getDefaultState()Lnet/minecraft/fluid/FluidState;"),
		  argsOnly = true
	)
	private Block setSettingsToAir(Block block) {
		return UnregistryNull.setUnregNull(Registries.BLOCK, block);
	}/*/

	@Override
	public boolean unregistry$valueIsUnregNull(){
		return isUnregNull;
	}
}
