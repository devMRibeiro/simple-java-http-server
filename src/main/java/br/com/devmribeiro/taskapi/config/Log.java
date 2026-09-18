package br.com.devmribeiro.taskapi.config;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
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

		        String message = record.getMessage();

		        Object[] params = record.getParameters();

		        if (params != null) {
		            for (int i = 0; i < params.length; i++) {
		                message = message.replace(
		                    "{" + i + "}",
		                    String.valueOf(params[i])
		                );
		            }
		        }

		        StringBuilder output = new StringBuilder();

		        output.append(String.format(
		            "%1$tF %1$tT [%2$s] %3$s%n",
		            new Date(record.getMillis()),
		            record.getLevel().getName(),
		            message
		        ));

		        Throwable throwable = record.getThrown();

		        if (throwable != null) {
		            StringWriter writer = new StringWriter();
		            PrintWriter printer = new PrintWriter(writer);

		            throwable.printStackTrace(printer);

		            output.append(writer);
		        }

		        return output.toString();
		    }
		});

		log.addHandler(handler);
		log.setLevel(Level.INFO);
	}
	
	public static void i(Object... params) {
		log.log(Level.INFO, "", params);
	}

	public static void i(String message, Object... params) {
	    log.log(Level.INFO, message, params);
	}

	public static void w(String message, Object... params) {
	    log.log(Level.WARNING, message, params);
	}

	public static void e(String message, Object... params) {
	    log.log(Level.SEVERE, message, params);
	}

	public static void e(String message, Throwable t) {
	    log.log(Level.SEVERE, message, t);
	}
}