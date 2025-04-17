package io.promptics.jobagent;

import lombok.Data;

@Data
public class MessageAnalysis {
    private Intent intent;
    private String reason;
    private boolean isCorrected;
    private String previousMessage;
    private String originalMessage;
    private String correctedMessage;

    public boolean getIsCorrected() {
        return isCorrected;
    }

    public void setIsCorrected(boolean corrected) {
        isCorrected = corrected;
    }

    public boolean isCorrected() {
        return isCorrected;
    }

    public enum Intent {
        QNA("QnA"),
        VERIFICATION("Verification"),
        INVALID("Invalid");

        private final String value;

        Intent(String value) {
            this.value = value;
        }
    }
}
