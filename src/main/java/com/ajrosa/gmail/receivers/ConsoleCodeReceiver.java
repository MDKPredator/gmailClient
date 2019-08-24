package com.ajrosa.gmail.receivers;

import java.io.IOException;

import com.google.api.client.extensions.java6.auth.oauth2.AbstractPromptReceiver;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;

/**
 * To enter the verification code via command prompt
 * @author Alberto
 *
 */
public class ConsoleCodeReceiver extends AbstractPromptReceiver {

	public String getRedirectUri() throws IOException {
		LocalServerReceiver localServerReceiver = new LocalServerReceiver();
		return localServerReceiver.getRedirectUri();
	}
}
