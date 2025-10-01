package wta.util.utils.mixinInterfaces;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.registry.entry.RegistryEntryInfo;
import wta.util.utils.RegType;

public interface SimpleRegistryFI<T> {
	T unregistry$add(RegistryKey<T> key, RegType type, T value, RegistryEntryInfo info);
	void unregistry$registerUnregistryNull(T value);
	T unregistry$getUnregistryNull();
	void unregistry$removeIntrusiveValueToEntry(T value);

	@SuppressWarnings("unchecked")
	static <T> void removeIntrusive(Registry<T> registry, T value){
		((SimpleRegistryFI<T>) registry).unregistry$removeIntrusiveValueToEntry(value);
	}
}