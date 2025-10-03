package wta.util.mixins.interfaces;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface RedirectInitIfUnregNull<T> {
	void unregistry$init(T self);
}
