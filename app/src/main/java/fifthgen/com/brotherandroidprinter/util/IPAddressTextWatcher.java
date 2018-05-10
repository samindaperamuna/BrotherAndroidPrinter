package fifthgen.com.brotherandroidprinter.util;

import android.text.Editable;
import android.text.TextWatcher;

import java.util.regex.Pattern;

public class IPAddressTextWatcher implements TextWatcher {

    private static final Pattern PARTIAL_IP_ADDRESS =
            Pattern.compile("^((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9])\\.){0,3}"
                    + "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9]))?$");

    private String mPreviousText;

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (PARTIAL_IP_ADDRESS.matcher(editable).matches()) {
            mPreviousText = editable.toString();
        } else {
            editable.replace(0, editable.length(), mPreviousText);
        }
    }
}
