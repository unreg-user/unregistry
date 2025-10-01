package wta.util.utils;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntryInfo;
import net.minecraft.util.Identifier;
import wta.util.utils.mixinInterfaces.SimpleRegistryFI;

public class UnRegistry {
	/**
	 * redone {@link Registry#register(Registry, String, Object)}<br>
	 * register, unregister and reregister<br>
	 * @param type type of action: register, unregister and reregister
	 */
	public static <T> T register(Registry<? super T> registry, String id, T entry, RegType type) {
		return register(registry, Identifier.of(id), entry, type);
	}

	/**
	 * redone {@link Registry#register(Registry, Identifier, Object)}<br>
	 * register, unregister and reregister<br>
	 * @param type type of action: register, unregister and reregister
	 */
	public static <V, T extends V> T register(Registry<V> registry, Identifier id, T entry, RegType type) {
		return register(registry, RegistryKey.of(registry.getKey(), id), entry, type);
	}

	/**
	 * redone {@link Registry#register(Registry, RegistryKey, Object)}<br>
	 * register, unregister and reregister<br>
	 * @param type type of action: register, unregister and reregister
	 */
	@SuppressWarnings("unchecked")
	public static <V, T extends V> T register(Registry<V> registry, RegistryKey<V> key, T entry, RegType type) {
		return (T) ((SimpleRegistryFI<V>)registry).unregistry$add(key, type, entry, RegistryEntryInfo.DEFAULT);
	}
}
