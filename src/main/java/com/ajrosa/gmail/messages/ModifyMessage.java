package com.ajrosa.gmail.messages;

import java.io.IOException;
import java.util.List;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.ModifyMessageRequest;

public class ModifyMessage {
	
	/**
     * Modify the labels a message is associated with.
     *
     * @param service Authorized Gmail API instance.
     * @param userId User's email address. The special value "me"
     * can be used to indicate the authenticated user.
     * @param messageId ID of Message to Modify.
     * @param labelsToAdd List of label ids to add.
     * @param labelsToRemove List of label ids to remove.
     * @return Message modified
     * @throws IOException
     */
	public Message modifyMessage(Gmail service, String userId, String messageId, List<String> labelsToAdd, List<String> labelsToRemove) throws IOException {
		ModifyMessageRequest mods = new ModifyMessageRequest().setAddLabelIds(labelsToAdd).setRemoveLabelIds(labelsToRemove);
		Message message = service.users().messages().modify(userId, messageId, mods).execute();
		
		return message;
	}
}
