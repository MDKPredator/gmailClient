package com.ajrosa.gmail;

/**
 * Different options with which this client can be configured
 * @author Alberto
 *
 */
public class Configuration {
	
	public static final String optHelp					= "h";
	public static final String optSecretFile			= "s";
	public static final String optCredentialsFile		= "c";
	public static final String optCodeVerificationFile	= "v";

	private String pathClientSecretFile;
	private String pathCredentials;
	private String fileCodeInput;
	
	public String getPathClientSecretFile() {
		return pathClientSecretFile;
	}
	public void setPathClientSecretFile(String pathClientSecretFile) {
		this.pathClientSecretFile = pathClientSecretFile;
	}
	public String getPathCredentials() {
		return pathCredentials;
	}
	public void setPathCredentials(String pathCredentials) {
		this.pathCredentials = pathCredentials;
	}
	public String getFileCodeInput() {
		return fileCodeInput;
	}
	public void setFileCodeInput(String fileCodeInput) {
		this.fileCodeInput = fileCodeInput;
	}
}
