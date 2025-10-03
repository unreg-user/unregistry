package wta.util.mixins.mixin;

import net.fabricmc.fabric.api.block.v1.FabricBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.state.StateManager;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wta.util.mixins.interfaces.RedirectInitIfUnregNull;
import wta.util.utils.UnregistryNull;

@Mixin(Block.class)
public abstract class BlockFixer extends AbstractBlock implements ItemConvertible, FabricBlock, RedirectInitIfUnregNull<Block> {
	@Shadow @Final @Mutable private RegistryEntry.Reference<Block> registryEntry;
	@Shadow @Final @Mutable protected StateManager<Block, BlockState> stateManager;

	@Shadow protected abstract void setDefaultState(BlockState state);

	@SuppressWarnings("DataFlowIssue")
	@Inject(
		  method = "<init>",
		  at = @At("RETURN")
	)
	private void redirectInit(Settings settings, CallbackInfo ci){
		if (UnregistryNull.isUnregNull(Registries.BLOCK, (Block) (Object) this)){
			unregistry$init((Block) (Object) this);
		}
	}

	@SuppressWarnings({"OptionalGetWithoutIsPresent", "deprecation"})
	@Override
	public void unregistry$init(Block self) {
		Block defaultBlock=Registries.BLOCK.getDefaultEntry().get().value();

		registryEntry = defaultBlock.getRegistryEntry();
		stateManager = defaultBlock.getStateManager();
		this.setDefaultState(stateManager.getDefaultState());
		superCopy(defaultBlock.getSettings());
	}

	@Unique
	private void superCopy(AbstractBlock.Settings settings){
		this.collidable = settings.collidable;
		this.lootTableKey = settings.lootTableKey;
		this.resistance = settings.resistance;
		this.randomTicks = settings.randomTicks;
		this.soundGroup = settings.soundGroup;
		this.slipperiness = settings.slipperiness;
		this.velocityMultiplier = settings.velocityMultiplier;
		this.jumpVelocityMultiplier = settings.jumpVelocityMultiplier;
		this.dynamicBounds = settings.dynamicBounds;
		this.requiredFeatures = settings.requiredFeatures;
		this.settings = settings;
	}

	//костыли
	public BlockFixer(Settings settings) {super(settings);}
}
