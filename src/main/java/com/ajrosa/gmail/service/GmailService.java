package com.ajrosa.gmail.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.VerificationCodeReceiver;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;

import com.ajrosa.gmail.receivers.ConsoleCodeReceiver;
import com.ajrosa.gmail.receivers.FileCodeReceiver;

public class GmailService {

	public static final int BROWSER 		= 1;
	public static final int COMMAND_LINE 	= 2;
	public static final int FILE 			= 3;
	
	/** Path to file client_secrets.json */
	private String pathClientSecretFile;
	
	/** How the gmail access verification code will be retrieved */
	private int codeInput = BROWSER;
	
	/** File from which the validation code will be read when codeInput is {@link GmailService#FILE} */
	private String fileCodeInput;
	
	/** Application name. */
	private final String APPLICATION_NAME = "Gmail Client";

	/** Directory to store user credentials for this application. */
	private static File DATA_STORE_DIR = 
			new File(System.getProperty("user.home"), ".credentials/gmail-credentials");

	/** Global instance of the {@link FileDataStoreFactory}. */
	private static FileDataStoreFactory DATA_STORE_FACTORY;

	/** Global instance of the JSON factory. */
	private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/** Global instance of the HTTP transport. */
	private static HttpTransport HTTP_TRANSPORT;

	/** Global instance of the scopes required by this quickstart. */
	private final List<String> SCOPES = 
			Arrays.asList(
					GmailScopes.MAIL_GOOGLE_COM, 
					GmailScopes.GMAIL_READONLY,
					GmailScopes.GMAIL_MODIFY);

	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Constructor
	 * @param pathClientSecretFile Path to file client_secrets.json
	 */
	public GmailService(String pathClientSecretFile) {
		this.pathClientSecretFile = pathClientSecretFile;
	}
	
	/**
	 * Constructor
	 * @param pathClientSecretFile Path to file client_secrets.json
	 * @param pathCredentials Path to save the file with Gmail login credentials
	 */
	public GmailService(String pathClientSecretFile, String pathCredentials) {
		this.pathClientSecretFile = pathClientSecretFile;
		DATA_STORE_DIR = new File(pathCredentials);
		try {
			DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Creates an authorized Credential object.
	 * 
	 * @return an authorized Credential object.
	 * @throws IOException
	 */
	private Credential authorize() throws IOException {
		// Load client secrets.
		FileInputStream fis = new FileInputStream(this.pathClientSecretFile);
		Credential credential = null;
		try {
			InputStream in = fis; //GmailQuickstart.class.getResourceAsStream("/client_secrets.json");
			GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
					JSON_FACTORY, new InputStreamReader(in));
	
			// Build flow and trigger user authorization request.
			GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
					HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
					.setDataStoreFactory(DATA_STORE_FACTORY)
					.setAccessType("offline").build();
			
			VerificationCodeReceiver codeReceiver = null;
			
			if (this.codeInput == BROWSER) {
				codeReceiver = new LocalServerReceiver();
			} else if (this.codeInput == COMMAND_LINE) {
				codeReceiver = new ConsoleCodeReceiver();
			} else if (this.codeInput == FILE) {
				codeReceiver = new FileCodeReceiver(this.fileCodeInput);
			}
			
			credential = new AuthorizationCodeInstalledApp(flow,codeReceiver).authorize("user");
			
			System.out.println("Credentials stored in " + DATA_STORE_DIR.getAbsolutePath());
		} finally {
			if (fis != null) {
				fis.close();
			}
		}
		return credential;
	}

	/**
	 * Build and return an authorized Gmail client service.
	 * 
	 * @return an authorized Gmail client service
	 * @throws IOException
	 */
	public Gmail getGmailService() throws IOException {
		Credential credential = authorize();
		return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build();
	}
	
	public void validateWithFile(String fileCodeInput) {
		this.fileCodeInput = fileCodeInput;
		this.codeInput = FILE;
	}

	public void setCodeInput(int codeInput) {
		this.codeInput = codeInput;
	}
}
