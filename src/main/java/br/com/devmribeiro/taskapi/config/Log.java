package br.com.devmribeiro.taskapi.config;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Log {
	private static final Logger log = Logger.getLogger("");
	
	static {

		// Remove handlers padrão
		for (var handler : log.getHandlers())
			log.removeHandler(handler);

		ConsoleHandler handler = new ConsoleHandler();

		handler.setLevel(Level.ALL);

		handler.setFormatter(new Formatter() {
			@Override
			public String format(LogRecord record) {

				return String.format("%1$tF %1$tT [%2$s] %3$s%n", new java.util.Date(record.getMillis()),
						record.getLevel().getName(), record.getMessage());
			}
		});

		log.addHandler(handler);
		log.setLevel(Level.INFO);
	}
	
	public static void i(Object o) {
		log.log(Level.INFO, "{0}", o);
	}
	
	public static void w(Object o) {
		log.log(Level.WARNING, "{0}", o);
	}
	
	public static void e(Object o) {
		log.log(Level.SEVERE, "{0}", o);
	}
	
	public static void e(String message, Throwable t) {
		log.log(Level.SEVERE, message, t);
	}
}