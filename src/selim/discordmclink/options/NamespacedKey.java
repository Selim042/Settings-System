package selim.discordmclink.options;

public class NamespacedKey {

	private final String namespace;
	private final String key;

	public NamespacedKey(String namespace, String key) {
		this.namespace = namespace;
		this.key = key;
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

}
