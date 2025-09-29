package wta.util.utils;

import net.minecraft.registry.MutableRegistry;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntryInfo;
import net.minecraft.util.Identifier;

public class UnRegistry {
	public static <T> T register(Registry<? super T> registry, RegType type, String id, T entry) {
		return register(registry, type, Identifier.of(id), entry);
	}

	public static <V, T extends V> T register(Registry<V> registry, RegType type, Identifier id, T entry) {
		return register(registry, type, RegistryKey.of(registry.getKey(), id), entry);
	}

	public static <T> T register(Registry<? super T> registry, String id, T entry, RegType type) {
		return register(registry, Identifier.of(id), entry, type);
	}

	public static <V, T extends V> T register(Registry<V> registry, Identifier id, T entry, RegType type) {
		return register(registry, RegistryKey.of(registry.getKey(), id), entry, type);
	}

	public static <V, T extends V> T register(Registry<V> registry, RegistryKey<V> key, T entry, RegType type) {
		return register(registry, type, key, entry);
	}


	public static <V, T extends V> T register(Registry<V> registry, RegType type, RegistryKey<V> key, T entry) {
		((MutableRegistry<V>)registry).add(key, entry, RegistryEntryInfo.DEFAULT);
		return entry;
	}
}
