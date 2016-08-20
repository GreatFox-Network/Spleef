package me.matsync.Spleef.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import me.matsync.Spleef.Spleef;

public class Yaml {

	static Plugin plugin;
	
	File file;
	FileConfiguration config;
	String resource;
	boolean save;
	
	public Yaml(String filepath, String resource, boolean save) {
		plugin = Spleef.getPlugin();
		
		this.file = getNewFile(filepath, resource);
		this.resource = resource;
		this.save = save;
		
		config = YamlConfiguration.loadConfiguration(file);
		
		createNewYaml(filepath, resource, save);
	}

	public boolean contains(String path) {
        return config.contains(path);
    }
	
	public void createSection(String path) {
		config.createSection(path);
	}
	
	public Object get(String path) {
		return config.get(path);
	}
	
    public boolean getBoolean(String path) {
        return config.getBoolean(path);
    }
    
    public ConfigurationSection getConfigurationSection(String path) {
		return config.getConfigurationSection(path);
	}
    
    public double getDouble(String path) {
        return config.getDouble(path);
    }
    
    public String getFilePath() {
    	return file.getPath();
    }
    
    public int getInt(String path) {
        return config.getInt(path);
    }
    
    public InputStream getResource() {
    	return Spleef.getPlugin().getResource(resource);
    }

	public String getString(String path) {
        return config.getString(path);
    }
	
	public List<String> getStringList(String path) {
        return config.getStringList(path);
    }
	
	public boolean isSaveable() {
		return save;
	}

	public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(file);
    }
	
	public void removeKey(String path) {
        config.set(path, null);
    }
	
	public void save() {
		try {
			config.save(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void set(String path, Object value) {
       config.set(path, value);
	}
	
	private void copyResource(InputStream resource, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
 
            int length;
            byte[] buf = new byte[1024];
 
            while ((length = resource.read(buf)) > 0) {
                out.write(buf, 0, length);
            }
 
            out.close();
            resource.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	private void createNewYaml(String filepath, String resource, boolean save) {
		File file = getNewFile(filepath, resource);
		
		prepareFile(file, resource);
		
		if (file == null) {
			return;
		}
		
		SettingsManager.getInstance().addYaml(this);
	}
	
	private File getNewFile(String filepath, String filename) {
		if (filename.isEmpty() || filename == null) {
			return null;
		}
		
		if (!filename.endsWith(".yml")) {
			filename.replace(filename.substring(filename.length() - 4, filename.length()), ".yml");
		}
		
		return new File(filepath, filename);
	}
	
	private void prepareFile(File file, String resource) {
		if (file.exists()) {
			return;
		}
		
		try {
			file.getParentFile().mkdirs();
			file.createNewFile();
			
			if (!resource.isEmpty() && resource != null) {
				copyResource(plugin.getResource(resource), file);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}