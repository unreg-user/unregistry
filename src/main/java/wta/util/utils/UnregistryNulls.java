package wta.util.utils;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import wta.util.utils.mixinInterfaces.SimpleRegistryFI;

public class UnregistryNulls {
	public static ItemUnregNull itemUnregNull=new ItemUnregNull();
	public static BlockUnregNull blockUnregNull=new BlockUnregNull();
	//public static BlockEntityType<? extends BlockEntity> blockEntityTypeUnregNull=BlockEntityType.Builder.create(BlockEntityUnregNull::new, new BlockEntityUnregNull.BlockWithEntityUnregNull()).build();

	static class ItemUnregNull extends Item implements UnregistryNull{
		private ItemUnregNull() {
			super(new Item.Settings());
			SimpleRegistryFI.removeIntrusive(Registries.ITEM, this);
		}
	}
	static class BlockUnregNull extends Block implements UnregistryNull{
		public BlockUnregNull() {
			super(Registries.BLOCK.getDefaultEntry().isPresent() ? Registries.BLOCK.getDefaultEntry().get().value().getSettings() : AbstractBlock.Settings.create());
			SimpleRegistryFI.removeIntrusive(Registries.BLOCK, this);
		}
	}
	/*static class BlockEntityUnregNull extends BlockEntity implements UnregistryNull{
		public BlockEntityUnregNull(BlockPos pos, BlockState state) {
			super(blockEntityTypeUnregNull, pos, state);
		}

		public static class BlockWithEntityUnregNull extends BlockUnregNull implements BlockEntityProvider {
			public BlockWithEntityUnregNull() {
				super();
			}

			@Override
			public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
				return new BlockEntityUnregNull(pos, state);
			}
		}
	}*/
}
