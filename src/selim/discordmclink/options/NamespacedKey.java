package selim.discordmclink.options;

import java.io.Serializable;

public class NamespacedKey implements Comparable<NamespacedKey>, Serializable {

	private static final long serialVersionUID = 7008119699010256739L;
	private final String namespace;
	private final String key;

	public NamespacedKey(String namespace, String key) {
		this.namespace = namespace;
		this.key = key;
	}

	public NamespacedKey(String fullKey) {
		String[] parts = fullKey.split(":");
		if (!fullKey.matches(".*:.*") || parts.length != 2)
			throw new IllegalArgumentException("fullKey must match \"namespace:key\"");
		this.namespace = parts[0];
		this.key = parts[1];
	}

	public String getNamespace() {
		return this.namespace;
	}

	public String getKey() {
		return this.key;
	}

	@Override
	public int hashCode() {
		return (31 * this.namespace.hashCode()) + this.key.hashCode();
	}

	@Override
	public String toString() {
		return this.namespace + ":" + this.key;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof NamespacedKey))
			return false;
		NamespacedKey in = (NamespacedKey) obj;
		return in.namespace.equals(this.namespace) && in.key.equals(this.key);
	}

	@Override
	public int compareTo(NamespacedKey o) {
		return toString().compareTo(o.toString());
	}

}
