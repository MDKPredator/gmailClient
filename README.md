# Gmail client

Gmail client with extra features, such as being able to enter the verification code via console or even a file

## Example of verification with file

```java
String pathClientSecretFile = "/path/to/client_secrets.json";
String pathCredentials = "/path/to/StoredCredential";

String veriticationFile = "/path/to/verification/file/code.txt"

GmailService service = new GmailService(pathClientSecretFile, pathCredentials);
service.validateWithFile(veriticationFile);

Gmail gmail = service.getGmailService();
SendMessage sendMessage = new SendMessage();

MimeMessage email = composeEmail();
sendMessage.sendMessage(gmail, ListMessages.DEFAULT_USER_ID, email);
```
