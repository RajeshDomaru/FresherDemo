package com.example.fresher.utils;

import androidx.appcompat.widget.AppCompatEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilsClass {

    public static boolean isValidStringFromEditText(AppCompatEditText appCompatEditText) {

        String textValue = validStringFromEditText(appCompatEditText);

        return isValidString(textValue);

    }

    public static boolean isValidDateFromEditText(AppCompatEditText appCompatEditText) {

        String textValue = validStringFromEditText(appCompatEditText);

        if (isValidString(textValue)) {

            String dateRegex = "^(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/([12][0-9]{3})$";

            Pattern pattern = Pattern.compile(dateRegex);

            Matcher matcher = pattern.matcher(textValue);

            return matcher.matches(); // 12/12/2020 => if true

        }

        return false;

    }

    public static boolean isValidAmountEditText(AppCompatEditText appCompatEditText) {

        String textValue = validStringFromEditText(appCompatEditText);

        if (!textValue.equalsIgnoreCase(".")) {

            try {

                Float.parseFloat(textValue);

                return true;

            } catch (Exception exception) {

                exception.printStackTrace();

            }

        }

        return false;

    }

    public static float validFloatFromEditText(AppCompatEditText appCompatEditText) {

        float floatValue = 0.0F;

        if (isValidAmountEditText(appCompatEditText)) {

            String textValue = validStringFromEditText(appCompatEditText);

            floatValue = Float.parseFloat(textValue);

        }

        return floatValue;

    }

    public static String validStringFromEditText(AppCompatEditText appCompatEditText) {

        String textValue = "";

        if (appCompatEditText != null && appCompatEditText.getText() != null) { // "" || " Abarna20 " || " Swathi" || "Rajesh "

            textValue = appCompatEditText.getText().toString().trim();

        }

        return textValue;

    }

    public static boolean isValidString(String textValue) {

        boolean isValue = false;

        if (textValue != null && !textValue.equalsIgnoreCase("") && textValue.length() > 0) {

            isValue = true;

        }

        return isValue;

//      (textValue != null && !textValue.equalsIgnoreCase("") && textValue.length() > 0);

    }

    public static String validString(String textValue) {

        if (isValidString(textValue)) {

            return textValue;

        }

        return "";

    }

    /*private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }*/

}
