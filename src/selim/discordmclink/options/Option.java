package selim.discordmclink.options;

import java.io.Serializable;

public class Option<T extends Serializable> implements Comparable<Option<T>>, Serializable {

	private static final long serialVersionUID = 7019638368597678580L;
	private final String displayName;
	private final NamespacedKey id;
	private T value;

	public Option(String displayName, NamespacedKey name, T defValue) {
		if (defValue == null)
			throw new IllegalArgumentException("defValue cannot be null");
		this.displayName = displayName;
		this.id = name;
		this.value = defValue;
	}

	/***
	 * Required for generation of default Options
	 */
	public Option(Option<T> option) {
		this.displayName = option.displayName;
		this.id = option.id;
		this.value = option.value;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public NamespacedKey getId() {
		return this.id;
	}

	public T getValue() {
		return this.value;
	}

	/***
	 * Sets the value to the value of the passed Option
	 */
	public boolean setValue(Option<T> option) {
		return setValue(option.value);
	}

	public boolean setValue(T value) {
		if (value != null && isValidValue(value)) {
			this.value = value;
			return true;
		}
		return false;
	}

	public boolean isValidValue(T value) {
		return value != null;
	}

	@Override
	public String toString() {
		return this.displayName + "(" + this.id + ")" + "=" + this.value;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Option))
			return false;
		Option<?> in = (Option<?>) obj;
		if (in.id.equals(this.id) && in.value.equals(this.value))
			return true;
		return false;
	}

	@Override
	public int compareTo(Option<T> o) {
		return id.compareTo(id);
	}

}