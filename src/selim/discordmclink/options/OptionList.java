package selim.discordmclink.options;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import selim.discordmclink.options.PlayerOptions.Option;

public class OptionList {

	private final Map<NamespacedKey, Option<?>> OPTIONS = new ConcurrentHashMap<>();

	public Option<?> getOption(NamespacedKey key) {
		if (!OPTIONS.containsKey(key))
			return null;
		return OPTIONS.get(key);
	}

	public Set<NamespacedKey> getOptions() {
		return OPTIONS.keySet();
	}

	protected void addOption(Option<?> option) {
		OPTIONS.put(option.getName(), option);
	}

}
