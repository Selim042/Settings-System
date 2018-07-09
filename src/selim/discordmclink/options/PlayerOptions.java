package selim.discordmclink.options;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class PlayerOptions {

	private static final File OPTIONS_FOLDER = new File("options");
	private static final FilenameFilter UUID_NAME_FILTER = new FilenameFilter() {

		@Override
		public boolean accept(File dir, String name) {
			return name.matches(
					"[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}\\.options");
		}
	};

	private static final List<Option<?>> REGISTERED_OPTIONS = new CopyOnWriteArrayList<>();
	private static final Map<UUID, OptionList> OPTIONS = new ConcurrentHashMap<>();

	private static boolean initalized = false;

	public static void init() {
		if (initalized)
			return;
		if (!OPTIONS_FOLDER.isDirectory() && !OPTIONS_FOLDER.exists())
			OPTIONS_FOLDER.mkdirs();
		for (File f : OPTIONS_FOLDER.listFiles(UUID_NAME_FILTER)) {
			try {
				if (!f.exists())
					return;
				FileInputStream inputStream = new FileInputStream(f);
				ObjectInputStream objectInput = new ObjectInputStream(inputStream);
				OptionList inOptions = (OptionList) objectInput.readObject();
				String fileName = f.getName();
				fileName = fileName.substring(0, fileName.indexOf("."));
				addDefaultOptions(inOptions);
				OPTIONS.put(UUID.fromString(fileName), inOptions);
				objectInput.close();
			} catch (IllegalArgumentException | IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public static void flush() {
		if (!OPTIONS_FOLDER.isDirectory() && !OPTIONS_FOLDER.exists())
			OPTIONS_FOLDER.mkdirs();
		for (Entry<UUID, OptionList> e : OPTIONS.entrySet()) {
			File f = new File(OPTIONS_FOLDER, e.getKey().toString() + ".options");
			try {
				if (!f.exists())
					f.createNewFile();
				FileOutputStream outputStream = new FileOutputStream(f);
				ObjectOutputStream objectOutput = new ObjectOutputStream(outputStream);
				objectOutput.writeObject(e.getValue());
				objectOutput.close();
			} catch (IllegalArgumentException | IOException ex) {
				ex.printStackTrace();
			}
		}
	}

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

	private static Option<?> getDefaultOption(NamespacedKey name) {
		for (Option<?> o : REGISTERED_OPTIONS) {
			if (!o.getId().equals(name))
				continue;
			try {
				@SuppressWarnings("rawtypes")
				Constructor<? extends Option> construct = o.getClass().getConstructor(Option.class);
				return construct.newInstance(o);
			} catch (NoSuchMethodException | SecurityException | InstantiationException
					| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static void addDefaultOptions(OptionList options) {
		for (Option<?> o : REGISTERED_OPTIONS) {
			Option<?> option = options.getOption(o.getId());
			if (option == null)
				options.addOption(getDefaultOption(o.getId()));
		}
	}

	public static void main(String... args) {
		registerOption(new OptionBoolean("Test Boolean", new NamespacedKey("test", "bool"), true));
		registerOption(new Option<String>("Test String", new NamespacedKey("test", "string"), "Herro"));
		registerOption(new OptionInteger("Test Integer", new NamespacedKey("test", "int"), 0));
		System.out.println("Default options:");
		OptionList defaults = getDefaultOptions();
		for (NamespacedKey key : defaults.getOptions()) {
			Option<?> o = defaults.getOption(key);
			System.out.println(o);
		}
		init();
		UUID myUUID = UUID.fromString("050e14b7-b041-452a-a13b-e243f2a68d18");
		OptionList options = getOptions(myUUID);
		Option<Integer> optionInt = (Option<Integer>) options.getOption("test:int");
		System.out.println("changed: " + optionInt.setValue(-1));
		System.out.println("My options:");
		for (NamespacedKey key : options.getOptions()) {
			Option<?> o = options.getOption(key);
			System.out.println(o);
		}
		flush();
	}

}
