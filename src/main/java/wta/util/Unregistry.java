package wta.util;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wta.util.events.FinalizerEvent;
import wta.util.utils.UnRegistry;

public class Unregistry implements ModInitializer {
	public static final String MOD_ID = "unregistry";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		UnRegistry.finalizeId();
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register(new FinalizerEvent());

		LOGGER.info("Goodbye new Item()!");
	}
}