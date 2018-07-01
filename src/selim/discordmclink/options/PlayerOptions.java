package selim.discordmclink.options;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

// Just looking for a bit of feedback here, first time doing anything like this
// I plan on writing/reading the Options to files named by the UUID
public class PlayerOptions {

	private static final List<Option<?>> REGISTERED_OPTIONS = new CopyOnWriteArrayList<>();
	private static final Map<UUID, OptionList> OPTIONS = new ConcurrentHashMap<>();

	public static void registerOption(Option<?> option) {
		if (!REGISTERED_OPTIONS.contains(option))
			REGISTERED_OPTIONS.add(option);
	}

	public static OptionList getOptions(UUID uuid) {
		if (OPTIONS.containsKey(uuid))
			return OPTIONS.get(uuid);
		OptionList def = getDefaultOptions();
		OPTIONS.put(uuid, def);
		return def;
	}

	private static OptionList getDefaultOptions() {
		OptionList defaults = new OptionList();
		for (Option<?> o : REGISTERED_OPTIONS) {
			try {
				@SuppressWarnings("rawtypes")
				Constructor<? extends Option> construct = o.getClass().getConstructor(Option.class);
				defaults.addOption(construct.newInstance(o));
			} catch (NoSuchMethodException | SecurityException | InstantiationException
					| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
				continue;
			}
		}
		return defaults;
	}

	public static void main(String... args) {
		registerOption(new Option<Boolean>(new NamespacedKey("test", "bool"), true));
		registerOption(new Option<String>(new NamespacedKey("test", "string"), "Herro"));
		System.out.println("Default options:");
		OptionList defaults = getDefaultOptions();
		for (NamespacedKey key : defaults.getOptions()) {
			Option<?> o = defaults.getOption(key);
			System.out.println(o.getName() + "=" + o.getValue());
		}
		UUID myUUID = UUID.fromString("050e14b7-b041-452a-a13b-e243f2a68d18");
		OptionList options = getOptions(myUUID);
		System.out.println("My options:");
		for (NamespacedKey key : options.getOptions()) {
			Option<?> o = options.getOption(key);
			System.out.println(o.getName() + "=" + o.getValue());
		}
	}

	public static class Option<T extends Serializable> {

		private final NamespacedKey name;
		private T value;

		public Option(NamespacedKey name, T defValue) {
			if (defValue == null)
				throw new IllegalArgumentException("defValue cannot be null");
			this.name = name;
			this.value = defValue;
		}

		/***
		 * Required for generation of default Options
		 */
		public Option(Option<T> option) {
			this.name = option.name;
			this.value = option.value;
		}

		public NamespacedKey getName() {
			return this.name;
		}

		public T getValue() {
			return this.value;
		}

		public boolean setValue(T value) {
			if (value != null) {
				this.value = value;
				return true;
			}
			return false;
		}

		@Override
		public String toString() {
			return this.name + "=" + this.value;
		}

	}

}
