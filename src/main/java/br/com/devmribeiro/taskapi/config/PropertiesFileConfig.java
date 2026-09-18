package br.com.devmribeiro.taskapi.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesFileConfig {

	private static final Properties PROPERTIES = new Properties();

	static {
		try (InputStream input = PropertiesFileConfig.class.getClassLoader().getResourceAsStream("application.properties")) {

			if (input == null)
				throw new IllegalStateException("application.properties não encontrado");

			PROPERTIES.load(input);

		} catch (IOException e) {
			throw new IllegalStateException("Erro ao carregar application.properties", e);
		}
	}

	public static String getValueStr(String key) {
		return PROPERTIES.getProperty(key);
	}

	public static Integer getValueAsInteger(String key) {
		String value = PROPERTIES.getProperty(key);
		
		try { return Integer.parseInt(value); }
		catch (NumberFormatException e) { e.printStackTrace(); }

		return null;
	}
}