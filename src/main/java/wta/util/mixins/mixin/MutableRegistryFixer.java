package wta.util.mixins.mixin;

import net.minecraft.registry.MutableRegistry;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryInfo;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import wta.util.mixins.interfaces.MutableRegistryFI;
import wta.util.utils.RegType;

@Mixin(MutableRegistry.class)
public interface MutableRegistryFixer<T> extends Registry<T>, MutableRegistryFI<T> {
	@Shadow RegistryEntry.Reference<T> add(RegistryKey<T> key, T value, RegistryEntryInfo info);

	@Override
	default RegistryEntry.Reference<T> unregistry$add(RegistryKey<T> key, RegType type, T value, RegistryEntryInfo info) {
		if (type==RegType.REG){
			return this.add(key, value, info);
		}
		throw Util.throwOrPause(new IllegalStateException("Cannot "+type+" in "+this.getClass()));
	}

	@Override
	default void unregistry$finalize(){}
}