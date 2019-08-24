package com.ajrosa.gmail;

import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.google.api.services.gmail.Gmail;

import com.ajrosa.gmail.service.GmailService;

/**
 * Launches the process for authorizing and downloading Gmail access credentials.
 * It will only be needed the first time and in case the token is expired.
 * 
 * API Reference: https://developers.google.com/gmail/api/v1/reference/
 * For the file client_secrets.json you have to activate the API in https://console.developers.google.com/
 * 
 * @author Alberto
 *
 */
public class AuthorizeMain {

	public static void main(String[] args) {
		GmailService gmailService = null;
		
		try {
			Configuration configuration = readOptions(args);
			
			if (configuration.getPathClientSecretFile() != null) {
				if (configuration.getPathCredentials() == null) {
					gmailService = new GmailService(configuration.getPathClientSecretFile());
				} else {
					gmailService = new GmailService(configuration.getPathClientSecretFile(), configuration.getPathCredentials());
				}
			}
			
			// Si se ha rellenado el parametro -v la validacion del 
			// codigo se hace mediante la lectura de un fichero
			if (configuration.getFileCodeInput() != null) {
				gmailService.validateWithFile(configuration.getFileCodeInput());
			}
			
			Gmail gmail = gmailService.getGmailService();
			
			if (gmail == null) {
				System.out.println("Error downloading credentials");
				System.exit(1);
			} else {
				System.out.println("Credentials downloaded successfully");
				System.exit(0);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.exit(1);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}
	
	/**
	 * Read past options as parameter
	 * @param args Arguments of entry to the application
	 * @return Configuration with the options passed by parameter
	 * @throws ParseException
	 */
	private static Configuration readOptions(String[] args) throws ParseException {
		CommandLine commandLine;
        Options options = new Options();
		CommandLineParser parser = new GnuParser();
		Configuration configuration = new Configuration();
		
		Option optAyuda = new Option(Configuration.optHelp, "help", false, "Show this help");
		Option optSecret = new Option(Configuration.optSecretFile, "secret", true, "Path to file client_secrets.json. To get it, activate the Gmail API at https://console.developers.google.com/");
		Option optCredentials = new Option(Configuration.optCredentialsFile, "credentials", true, "Location and name of the credentials file");
		Option optVerification = new Option(Configuration.optCodeVerificationFile, "verification", true, "Location and name of the file where the verification code provided by Google will be saved.");
		
		options.addOption(optAyuda);
        options.addOption(optSecret);
        options.addOption(optCredentials);
        options.addOption(optVerification);
        
        commandLine = parser.parse(options, args);
		
		if (commandLine.hasOption(Configuration.optHelp)) {
			help(options);
		}
		
		if (commandLine.hasOption(Configuration.optSecretFile)) {
			String pathFile = commandLine.getOptionValue(Configuration.optSecretFile);
			configuration.setPathClientSecretFile(pathFile);
        } else {
        	System.out.println("The parameter -s is mandatory");
        	System.exit(-1);
        }
		
		if (commandLine.hasOption(Configuration.optCredentialsFile)) {
			String pathFile = commandLine.getOptionValue(Configuration.optCredentialsFile);
			configuration.setPathCredentials(pathFile);
        }
		
		if (commandLine.hasOption(Configuration.optCodeVerificationFile)) {
			String pathFile = commandLine.getOptionValue(Configuration.optCodeVerificationFile);
			configuration.setFileCodeInput(pathFile);
        }
		
		return configuration;
	}
	
	/**
	 * Muestra la ayuda con las distintas opciones
	 * @param options Opciones de la aplicacion
	 */
	private static void help(Options options) {
		// This prints out some help
		HelpFormatter formater = new HelpFormatter();
		
		formater.printHelp("Authorize", options);
		System.exit(0);
	}
}
