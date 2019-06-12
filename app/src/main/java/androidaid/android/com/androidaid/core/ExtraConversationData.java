package androidaid.android.com.androidaid.core;

public class ExtraConversationData {
    private String recipient; //who is this message for?
    private String extraText; //all of the other text from the screen

    public ExtraConversationData() {

    }

    public ExtraConversationData(String recipient, String extraText) {
        this.recipient = recipient;
        this.extraText = extraText;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getExtraText() {
        return extraText;
    }
}
