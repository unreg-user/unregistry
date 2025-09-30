package wta.util.events;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.resource.LifecycledResourceManager;
import net.minecraft.server.MinecraftServer;
import wta.util.utils.UnRegistry;

public class FinalizerEvent implements ServerLifecycleEvents.EndDataPackReload {
	@Override
	public void endDataPackReload(MinecraftServer minecraftServer, LifecycledResourceManager lifecycledResourceManager, boolean b) {
		UnRegistry.finalizeId();
	}
}
