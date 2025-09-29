package wta.util;

import net.fabricmc.api.ModInitializer;

import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Unregistry implements ModInitializer {
	public static final String MOD_ID = "unregistry";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Goodbye new Item()!");
	}
}