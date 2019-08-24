package com.ajrosa.gmail.receivers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.google.api.client.extensions.java6.auth.oauth2.VerificationCodeReceiver;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;

/**
 * To enter the verification code by file
 * @author Alberto
 *
 */
public class FileCodeReceiver implements VerificationCodeReceiver {
	
	private String fileCodeInput;
	
	public FileCodeReceiver(String fileCodeInput) {
		this.fileCodeInput = fileCodeInput;
	}

	public String getRedirectUri() throws IOException {
		LocalServerReceiver localServerReceiver = new LocalServerReceiver();
		return localServerReceiver.getRedirectUri();
	}

	public String waitForCode() throws IOException {
		if (this.fileCodeInput == null) {
			throw new IOException("No file defined");
		}
		
		String code = null;
		File file = null;
		FileReader fr = null;
		BufferedReader br = null;

		try {
			// Opening of the file and creation of BufferedReader to be able to 
			// make a comfortable reading (to have the readLine() method).
			file = new File(this.fileCodeInput);
			
			// If the file does not exist, the process is stopped while the 
			// verification code is being generated.
			if (!file.exists()) {
				System.out.println("Waiting code in file " + this.fileCodeInput);
			}
			while (!file.exists()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {}
			}
			
			fr = new FileReader(file);
			br = new BufferedReader(fr);

			// Reading the file
			String linea;
			while ((linea = br.readLine()) != null) {
				code = linea;
			}
			
			if (code == null || (code != null && code.isEmpty())) {
				System.out.println("Code not found");
			}
		} finally {
			try {
				if (null != fr) {
					fr.close();
				}
			} catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
			}
		}
		
		return code;
	}

	public void stop() throws IOException {}

}
