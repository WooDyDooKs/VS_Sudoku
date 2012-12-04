package ds.sudoku.communication;

import java.util.List;
import java.util.Map;

public abstract class NamedMessage extends Message {

    private String name;

    /**
     * Creates a new NamedMessage with the given name.
     * 
     * @param name
     *            The given name.
     */
    public NamedMessage(String name) {
        this.name = name;
    }

    /**
     * Creates a new NamedMessage with the given name. Additionally stores
     * custom values and properties
     * 
     * @param name
     *            The given name.
     * @param customValues
     *            The custom values stored in this message.
     * @param customProperties
     *            The custom properties stored in this message.
     */
    public NamedMessage(String name, List<String> customValues,
            Map<String, String> customProperties) {
        super(customValues, customProperties);
        this.name = name;
    }

    /**
     * Get the name associated with the message.
     * 
     * @return the name associated with the message.
     */
    public String getName() {
        return name;
    }

}
