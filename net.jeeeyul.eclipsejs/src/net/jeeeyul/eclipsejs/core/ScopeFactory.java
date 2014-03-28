package net.jeeeyul.eclipsejs.core;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import net.jeeeyul.eclipsejs.EclipseJSCore;
import net.jeeeyul.eclipsejs.api.IO;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;
import org.osgi.framework.Bundle;

public class ScopeFactory {
	private static ScopeFactory instance;

	private IO io = new IO();

	public static ScopeFactory getInstance() {
		if (instance == null) {
			instance = new ScopeFactory();
		}
		return instance;
	}

	private ScopeFactory() {
	}

	public ScriptableObject create() {
		Context ctx = Context.getCurrentContext();
		ScriptableObject scope = ctx.initStandardObjects();

		Bundle bundle = EclipseJSCore.getDefault().getBundle();
		Enumeration<URL> entries = bundle.findEntries("runtime", "*.js", true);
		while (entries.hasMoreElements()) {
			URL url = (URL) entries.nextElement();
			try {
				String script = io.readInputStream(url.openStream(), "UTF-8");
				ctx.evaluateString(scope, script, url.getPath(), 1, null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		ScriptableObject.putProperty(scope, "IO",
				Context.javaToJS(new IO(), scope));

		return scope;
	}

}