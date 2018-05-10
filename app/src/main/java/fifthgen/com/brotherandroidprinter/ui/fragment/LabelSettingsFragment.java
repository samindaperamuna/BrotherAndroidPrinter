package fifthgen.com.brotherandroidprinter.ui.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import fifthgen.com.brotherandroidprinter.R;
import fifthgen.com.brotherandroidprinter.util.ImageUtil;
import fifthgen.com.brotherandroidprinter.util.PrinterHelper;

public class LabelSettingsFragment extends Fragment implements View.OnClickListener {

    private ImageView previewImageView;

    private TextInputLayout nameInputLayout;
    private TextInputEditText nameEditText;
    private TextInputLayout companyInputLayout;
    private TextInputEditText companyEditText;
    private TextInputLayout positionTextInputLayout;
    private TextInputEditText positionEditText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_label_settings, container, false);

        previewImageView = view.findViewById(R.id.previewImageView);

        nameInputLayout = view.findViewById(R.id.nameInputLayout);
        nameEditText = view.findViewById(R.id.nameEditText);
        companyInputLayout = view.findViewById(R.id.companyInputLayout);
        companyEditText = view.findViewById(R.id.companyEditText);
        positionTextInputLayout = view.findViewById(R.id.positionTextInputLayout);
        positionEditText = view.findViewById(R.id.positionEditText);

        ImageButton previewButton = view.findViewById(R.id.previewButton);
        previewButton.setOnClickListener(this);

        ImageButton printButton = view.findViewById(R.id.printButton);
        printButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.previewButton:
                if (validateForm()) {
                    String[] text = {nameEditText.getText().toString(),
                            companyEditText.getText().toString(),
                            positionEditText.getText().toString()};
                    Bitmap bitmap = ImageUtil.textAsBitMap(text);
                    previewImageView.setImageBitmap(bitmap);
                    previewImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                }
                break;
            case R.id.printButton:
                if (validateForm()) {
                    String[] text = {nameEditText.getText().toString(),
                            companyEditText.getText().toString(),
                            positionEditText.getText().toString()};

                    Activity activity = getActivity();

                    if (activity != null) {
                        PrinterHelper printerHelper = new PrinterHelper(activity);
                        printerHelper.print(text);
                    }
                }
                break;
        }
    }

    private boolean validateForm() {
        if (nameEditText.getText().toString().equals("")) {
            nameInputLayout.setError(getString(R.string.full_name_error));
            return false;
        } else if (companyEditText.getText().toString().equals("")) {
            companyInputLayout.setError(getString(R.string.company_error));
            return false;
        } else if (positionEditText.getText().toString().equals("")) {
            positionTextInputLayout.setError(getString(R.string.position_error));
            return false;
        }

        return true;
    }
}
