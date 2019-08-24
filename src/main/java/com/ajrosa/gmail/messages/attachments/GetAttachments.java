package com.ajrosa.gmail.messages.attachments;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartBody;

public class GetAttachments {
	
	private String attachedDownloadPath = System.getProperty("user.home");
	
	public GetAttachments(){}
	
	/**
	 * Builder who is passed the path where the attached file is going to be downloaded.
	 * @param attachedDownloadPath Path where the email attachment is saved
	 */
	public GetAttachments(String attachedDownloadPath) {
		this.attachedDownloadPath = attachedDownloadPath;
	}
	
	/**
	 * Get the attachments in a given email.
	 *
	 * @param service Authorized Gmail API instance.
	 * @param userId User's email address. The special value "me"
	 * can be used to indicate the authenticated user.
	 * @param messageId ID of Message containing attachment..
	 * @return List of attached files
	 * @throws IOException
	 */
	public List<String> getAttachments(Gmail service, String userId, String messageId) throws IOException {
		Message message = service.users().messages().get(userId, messageId).execute();
		List<MessagePart> parts = message.getPayload().getParts();
		List<String> filenameList = new ArrayList<String>();
		
		for (MessagePart part : parts) {
			if (part.getFilename() != null && part.getFilename().length() > 0) {
				String filename = part.getFilename();
				String attId = part.getBody().getAttachmentId();
				
				MessagePartBody attachPart = service.users().messages().attachments().get(userId, messageId, attId).execute();
				
				byte[] fileByteArray = Base64.decodeBase64(attachPart.getData());
				
				FileOutputStream fileOutFile = new FileOutputStream(this.attachedDownloadPath + filename);
				fileOutFile.write(fileByteArray);
				fileOutFile.close();
				
				filenameList.add(this.attachedDownloadPath + filename);
			}
		}
		return filenameList;
	}
}
