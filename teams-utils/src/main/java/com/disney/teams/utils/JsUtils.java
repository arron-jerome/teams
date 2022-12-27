package com.disney.teams.utils;

import com.disney.teams.utils.thread.AutoRemove;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @author arron.zhou
 * @version 1.0.0
 * @date 2022/12/17
 * Description:
 * Modification  History:
 * Date         Author        Version        Description
 * ------------------------------------------------------
 * 2022/12/17       arron.zhou      1.0.0          create
 */
public class JsUtils {

    private static final ThreadLocal<ScriptEngine> jsEngine = new ThreadLocal<>();
    private static final ScriptEngineManager factory = new ScriptEngineManager();

    private static final AutoRemove AUTO_REMOVE = new AutoRemove(jsEngine);

    private static ScriptEngine get() {
        ScriptEngine engine = jsEngine.get();
        if (engine == null) {
            engine = factory.getEngineByName("JavaScript");
            jsEngine.set(engine);
        }
        return engine;
    }

    private static String formatText(String text) {
        return text == null ? null : text.replace("'", "\\'");
    }

    public static String encodeURI(String text) {
        if (text == null) {
            return "";
        }
        try {
            text = formatText(text);
            Object rs = get().eval(String.format("encodeURI('%s')", text));
            return (String) rs;
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public static String encodeURIComponent(String text) {
        if (text == null) {
            return "";
        }
        try {
            text = formatText(text);
            Object rs = get().eval(String.format("encodeURIComponent('%s')", text));
            return (String) rs;
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object eval(String script) throws ScriptException {
        return get().eval(script);
    }

    public static AutoRemove safeEngine() {
        get();
        return AUTO_REMOVE;
    }
}
