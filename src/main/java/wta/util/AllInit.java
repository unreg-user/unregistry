package wta.util;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import wta.util.utils.mixinInterfaces.SimpleRegistryFI;
import wta.util.utils.UnregistryNull;

public class AllInit {
	public static void registryNullInit(){
		regRegNull(Registries.ITEM, UnregistryNull.itemUnregNull);
	}

	@SuppressWarnings("unchecked")
	private static <T> void regRegNull(Registry<T> registry, T nullValue){
		((SimpleRegistryFI) registry).unregistry$registerUnregistryNull(nullValue);
	}
}
