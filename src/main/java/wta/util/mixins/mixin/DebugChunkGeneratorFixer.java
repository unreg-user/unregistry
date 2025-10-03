package wta.util.mixins.mixin;

import com.jcraft.jorbis.Block;
import net.minecraft.registry.Registries;
import net.minecraft.world.gen.chunk.DebugChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wta.util.mixins.interfaces.SimpleRegistryFIInner;

@Mixin(DebugChunkGenerator.class)
public class DebugChunkGeneratorFixer {
	@SuppressWarnings("unchecked")
	@Inject(
		  method = "<clinit>",
		  at = @At("HEAD")
	)
	private static void clearer(CallbackInfo ci){
		((SimpleRegistryFIInner<Block>) Registries.BLOCK).unregistry$finalize();
	}
}
