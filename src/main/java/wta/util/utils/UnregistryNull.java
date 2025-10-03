package wta.util.utils;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import wta.util.utils.mixinInterfaces.SimpleRegistryFI;

public interface UnregistryNull {
	@SuppressWarnings({"unchecked", "OptionalGetWithoutIsPresent"})
	static <T> T setUnregNull(Registry<T> registry, T value){
		if (((SimpleRegistryFI<T>) registry).unregistry$isFrozen()){
			return value instanceof UnregistryNull ? registry.getDefaultEntry().get().value() : value;
		}else{
			return value;
		}
	}

	@SuppressWarnings("unchecked")
	static <T> boolean isUnregNull(Registry<T> registry, T value){
		return value instanceof UnregistryNull && registry.getDefaultEntry().isPresent();
	}
}
