package wta.util.mixins.interfaces;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryInfo;
import wta.util.utils.RegType;

public interface MutableRegistryFI<T> {
	RegistryEntry.Reference<T> unregistry$add(RegistryKey<T> key, RegType type, T value, RegistryEntryInfo info);
}
