package main.java.constants;

import java.util.HashMap;

public class Constants {

    public final static byte DEFAULT = 0x00;

    private final static String UI_DEFINITION_PATH = "/constants/UIDefinitions.constants";
    private final static String CORE_DEFINITION_PATH = "/constants/CoreDefinitions.constants";

    private static HashMap<String, Integer> integer_parameters = new HashMap<>();

    public static int getInt(String resource_name, Definitions type) {
        try {
            return integer_parameters.get(resource_name);
        } catch (NullPointerException e) {
            switch (type) {
                case CORE_CONSTANTS:
                    integer_parameters.put(resource_name,
                            ParseUtils.parseInt(CORE_DEFINITION_PATH, resource_name));
                    break;
                case UI_CONSTANTS:
                    integer_parameters.put(resource_name,
                            ParseUtils.parseInt(UI_DEFINITION_PATH, resource_name));
                    break;
                default:
                    return 0;
            }
            return getInt(resource_name, type);
        }
    }

}
