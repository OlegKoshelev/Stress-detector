package sample.InSituStress.Controller;

import javafx.scene.control.TextField;

public class NumberTextField extends TextField {
    String numberRegEx = "[0-9]*";

    @Override
    public void replaceText(int start, int end, String text) {
        if(validate(text)) {
            super.replaceText(start, end, text);
        }
    }

    private boolean validate(String text) {
        return (numberRegEx.equals("") || text.matches(numberRegEx));
    }
}
