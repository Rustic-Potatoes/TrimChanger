package net.rusticpotatoes.trimChanger.config;

import net.rusticpotatoes.trimChanger.TrimChanger;

public class TrimConfig {
    public static final BooleanConfigKey OPERATOR_RELOAD_KEY = new BooleanConfigKey("operator-reload", false);
    public static final BooleanConfigKey PARTICLE_CLEAR_KEY = new BooleanConfigKey("particle-clear", true);
    public static final BooleanConfigKey ALLOW_CLEAR_KEY = new BooleanConfigKey("command-options.allow-clear", true);
    public static final BooleanConfigKey ALLOW_ABOUT_KEY = new BooleanConfigKey("command-options.allow-about", true);
    public static final BooleanConfigKey ALLOW_HELP_KEY = new BooleanConfigKey("command-options.allow-help", true);
    public static final BooleanConfigKey ALLOW_QUERY_KEY = new BooleanConfigKey("command-options.allow-query", true);
    public static final BooleanConfigKey ALLOW_TRIM_ROOT_KEY = new BooleanConfigKey("command-options.allow-trim-root", true);

    public static boolean getBoolean(BooleanConfigKey configKey) {
        return TrimChanger.getInstance().getConfig().getBoolean(configKey.key, configKey.defaultValue);
    }

    public record BooleanConfigKey(String key, boolean defaultValue) {
        public boolean get() {
            return getBoolean(this);
        }
    }
}



