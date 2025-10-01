package wta.util;

import net.fabricmc.api.ModInitializer;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApiStatus.Internal
public class Unregistry implements ModInitializer {
	public static final String MOD_ID = "unregistry";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Goodbye new Item()!");
	}
}