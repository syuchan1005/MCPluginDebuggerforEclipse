package com.github.syuchan1005.mcplugindebugger;

import java.io.File;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

/**
 * Created by syuchan on 2017/07/04.
 */
public class PluginDataConfig {
	private boolean isInited = false;
	
    private String serverJarFile;
    private String pluginJarFile;
    private String pluginName;
    private String host;
    private Integer port;

    public void init(File baseDir) {
        if (serverJarFile == null) {
            setServerJarFile(baseDir);
        }
        if (pluginJarFile == null) {
            setPluginJarFile(baseDir);
        }
        if (pluginName == null) {
            pluginName = "EX";
        }
        if (host == null) {
            host = "localhost";
        }
        if (port == null) {
            port = 9000;
        }
        load();
        isInited = true;
    }

    public File getServerJarFile() {
        return new File(serverJarFile);
    }

    public void setServerJarFile(File serverJarFile) {
        this.serverJarFile = trimPath(serverJarFile.getAbsolutePath());
        save();
    }

    public File getPluginJarFile() {
        return new File(pluginJarFile);
    }

    public void setPluginJarFile(File pluginJarFile) {
        this.pluginJarFile = trimPath(pluginJarFile.getAbsolutePath());
        save();
    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
        save();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
        save();
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
        save();
    }

    private static String trimPath(String s) {
        if (s.endsWith("!")) {
            return s.substring(0, s.length() - 1);
        }
        return s;
    }
    
    private void save() {
    	if (isInited) {
    		Preferences preferences = InstanceScope.INSTANCE.getNode("com.github.syuchan1005.mcplugindebugger");
        	Preferences node = preferences.node("mcplugindebugger");
        	node.put("serverJarFile", serverJarFile);
        	node.put("pluginJarFile", pluginJarFile);
        	node.put("pluginName", pluginName);
        	node.put("host", host);
        	node.putInt("port", port);
        	try {
				node.flush();
			} catch (BackingStoreException e) {}
    	}
    }
    
    public void load() {
    	Preferences preferences = InstanceScope.INSTANCE.getNode("com.github.syuchan1005.mcplugindebugger");
    	Preferences node = preferences.node("mcplugindebugger");
    	serverJarFile = node.get("serverJarFile", serverJarFile);
    	pluginJarFile = node.get("pluginJarFile", pluginJarFile);
    	pluginName = node.get("pluginName", pluginName);
    	host = node.get("host", host);
    	port = node.getInt("port", port);
    }
}
