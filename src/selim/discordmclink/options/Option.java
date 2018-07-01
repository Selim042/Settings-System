package selim.discordmclink.options;

import java.io.Serializable;

public class Option<T extends Serializable> {

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

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Option))
			return false;
		Option<?> in = (Option<?>) obj;
		if (in.name.equals(this.name) && in.value.equals(this.value))
			return true;
		return false;
	}

}