package wta.util;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import wta.util.utils.UnregistryNulls;
import wta.util.utils.mixinInterfaces.SimpleRegistryFI;

public class AllInit {
	public static void registryNullInit(){
		regRegNull(Registries.ITEM, UnregistryNulls.itemUnregNull);
		regRegNull(Registries.BLOCK, UnregistryNulls.blockUnregNull);
		//regRegNull(Registries.BLOCK_ENTITY_TYPE, UnregistryNulls.blockEntityTypeUnregNull);
	}

	@SuppressWarnings("unchecked")
	private static <T> void regRegNull(Registry<T> registry, T nullValue){
		((SimpleRegistryFI<T>) registry).unregistry$registerUnregistryNull(nullValue);
	}
}
