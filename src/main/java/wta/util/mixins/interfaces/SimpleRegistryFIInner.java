package wta.util.mixins.interfaces;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryInfo;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface SimpleRegistryFIInner<T> {
	boolean unregistry$unregister(RegistryKey<T> key);
	boolean unregistry$reregister(RegistryKey<T> key, T value, RegistryEntryInfo info, RegistryEntry.Reference<T> reference);
	boolean unregistry$register(RegistryKey<T> key, T value, RegistryEntryInfo info, RegistryEntry.Reference<T> reference);
	RegistryEntry.Reference<T> unregistry$findReference(RegistryKey<T> key, T value);
	void unregistry$finalize();
}
