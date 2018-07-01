package selim.discordmclink.options;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class OptionList implements Serializable {

	private static final long serialVersionUID = -3894357647030160614L;
	private final List<Option<?>> OPTIONS = new CopyOnWriteArrayList<>();

	public Option<?> getOption(String key) {
		return getOption(new NamespacedKey(key));
	}

	public Option<?> getOption(NamespacedKey key) {
		for (Option<?> o : OPTIONS)
			if (o != null && o.getId().equals(key))
				return o;
		return null;
	}

	public List<NamespacedKey> getOptions() {
		List<NamespacedKey> keys = new LinkedList<>();
		for (Option<?> o : OPTIONS)
			keys.add(o.getId());
		keys.sort(null);
		return keys;
	}

	protected void addOption(Option<?> option) {
		addOption(option, true);
	}

	protected void addOption(Option<?> option, boolean shouldSort) {
		OPTIONS.add(option);
		if (shouldSort)
			OPTIONS.sort(null);
	}

	protected void sort() {
		OPTIONS.sort(null);
	}

}
