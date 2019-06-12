package androidaid.android.com.androidaid.utilities;

import java.util.ArrayList;

import androidaid.android.com.androidaid.core.ExtraConversationData;
import androidaid.android.com.androidaid.program_flow.Constants;

public class CoreUtils {
    /**
     * Parses the specified source (ArrayList representing the extra on-screen text data of messenger/instagram conversation)
     * @param sourceText all of the source text
     * @param source Constants.SOURCE_INSTAGRAM, Constants.SOURCE_MESSENGER or Constants.SOURCE_SMS
     * @return
     */
    public static ExtraConversationData parseExtraConversationData(ArrayList<String> sourceText, String source) {
        System.out.println("[sproc32.CoreUtils.parseExtraConversationData]: Parsing extra conversation data from " + source + " for text: " ); StringUtils.displayArray(sourceText);
        String recipient = "";
        String extraText = "";

        switch(source) {
            case Constants.SOURCE_INSTAGRAM:
                recipient = sourceText.get(0);
                extraText = getExtraText(sourceText, 0);
                break;
            case Constants.SOURCE_MESSENGER:
                for(int i = 0; i<sourceText.size(); i++) {
                    if(sourceText.get(i).matches("[0-9]+") == false) {
                        recipient = sourceText.get(i);
                        extraText = getExtraText(sourceText, i);
                        break;
                    }
                }
                break;
        }

        if(source.equals(Constants.SOURCE_INSTAGRAM) || source.equals(Constants.SOURCE_MESSENGER) || source.equals(Constants.SOURCE_SMS)) {
            return new ExtraConversationData(recipient, extraText);
        } else {
            System.out.println("[sproc32.CoreUtils.parseExtraConversationData]: Error acquiring ExtraConversationData for source text");
            return null;
        }
    }

    /*
    Gets extra text from the specified ArrayList. For most of the social networks, the element at index 0 in the arraylist is the username, and
    the extra text is all what is not index 0
     */
    private static String getExtraText(ArrayList<String> text, int indexOfRecipient) {
        StringBuilder result = new StringBuilder();
        if(text.size()>=0) {
            for(int i = 1; i<text.size(); i++) {
                if(StringUtils.checkIfNotForbidden(text.get(i), Constants.DATABASE_FORBIDDEN_FIELD_TEXT_VALUES) && i != indexOfRecipient) {
                    result.append(text.get(i)).append(Constants.STORAGE_TEXT_ADDITIONAL_SCREEN_INFORMATION_SEPARATOR);
                }
            }
            return result.toString();
        } else {
            System.out.println("[sproc32.CoreUtils.getExtraText]: Acquiring exra text failed. Is the source array empty? Specified array size: " + text.size());
            return "";
        }
    }
}
