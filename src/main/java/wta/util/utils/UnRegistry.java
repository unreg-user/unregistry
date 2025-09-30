package wta.util.utils;

import net.minecraft.registry.MutableRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntryInfo;
import net.minecraft.util.Identifier;
import wta.util.mixins.interfaces.MutableRegistryFI;

import static com.mojang.text2speech.Narrator.LOGGER;

public class UnRegistry {
	public static <T> T register(Registry<? super T> registry, String id, T entry, RegType type) {
		return register(registry, Identifier.of(id), entry, type);
	}

	public static <V, T extends V> T register(Registry<V> registry, Identifier id, T entry, RegType type) {
		return register(registry, RegistryKey.of(registry.getKey(), id), entry, type);
	}

	@SuppressWarnings("unchecked")
	public static <V, T extends V> T register(Registry<V> registry, RegistryKey<V> key, T entry, RegType type) {
		((MutableRegistryFI<V>)registry).unregistry$add(key, type, entry, RegistryEntryInfo.DEFAULT);
		return entry;
	}

	public static void finalizeId(){
		for (Registry<?> registry : Registries.REGISTRIES){
			((MutableRegistryFI<?>) registry).unregistry$finalize();
		}
		LOGGER.info("registries finalized");
	}
}
