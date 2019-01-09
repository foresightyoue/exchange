package org.dromara.soul.common.constant;

public final class ZkPathConstants implements Constants {
    /**
     * The constant SELECTOR_PARENT.
     */
    public static final String SELECTOR_PARENT = "/soul/selector";

    /**
     * The constant SELECTOR_JOIN_RULE.
     */
    public static final String SELECTOR_JOIN_RULE = "-";

    private static final String PLUGIN_PARENT = "/soul/plugin";

    private static final String RULE_PARENT = "/soul/rule";

    /**
     * The constant APP_AUTH_PARENT.
     */
    public static final String APP_AUTH_PARENT = "/soul/auth";

    /**
     * acquire app_auth_path.
     *
     * @param appKey appKey
     * @return app_auth_path string
     */
    public static String buildAppAuthPath(final String appKey) {
        return String.join("/", APP_AUTH_PARENT, appKey);
    }

    /**
     * buildPluginParentPath.
     *
     * @return zk path for plugin
     */
    public static String buildPluginParentPath() {
        return String.join("/", PLUGIN_PARENT);
    }

    /**
     * buildPluginRealPath.
     *
     * @param pluginName pluginName
     * @return zk path for plugin
     */
    public static String buildPluginPath(final String pluginName) {
        return String.join("/", PLUGIN_PARENT, pluginName);
    }

    /**
     * buildSelectorParentPath.
     *
     * @param pluginName pluginName
     * @return zk path for selector
     */
    public static String buildSelectorParentPath(final String pluginName) {
        return String.join("/", SELECTOR_PARENT, pluginName);
    }

    /**
     * buildSelectorRealPath.
     *
     * @param pluginName pluginName
     * @param selectorId selectorId
     * @return zk full path for selector
     */
    public static String buildSelectorRealPath(final String pluginName, final String selectorId) {
        return String.join("/", SELECTOR_PARENT, pluginName, selectorId);
    }

    /**
     * buildRuleParentPath.
     *
     * @param pluginName pluginName
     * @return zk rule parent path.
     */
    public static String buildRuleParentPath(final String pluginName) {
        return String.join("/", RULE_PARENT, pluginName);
    }

    /**
     * buildRulePath.
     *
     * @param pluginName pluginName
     * @param selectorId selectorId
     * @param ruleId     ruleId
     * @return /soul/rule/pluginName/selectorId-ruleId
     */
    public static String buildRulePath(final String pluginName, final String selectorId, final String ruleId) {
        return String.join("/", buildRuleParentPath(pluginName), selectorId + SELECTOR_JOIN_RULE + ruleId);
    }
}
