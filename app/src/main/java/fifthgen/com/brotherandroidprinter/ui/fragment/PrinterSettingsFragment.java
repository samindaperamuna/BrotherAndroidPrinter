package fifthgen.com.brotherandroidprinter.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.brother.ptouch.sdk.LabelInfo;
import com.phearme.macaddressedittext.MacAddressEditText;

import java.util.Map;
import java.util.TreeMap;

import fifthgen.com.brotherandroidprinter.R;
import fifthgen.com.brotherandroidprinter.util.IPAddressTextWatcher;
import fifthgen.com.brotherandroidprinter.util.PreferenceKeys;
import fifthgen.com.brotherandroidprinter.util.PrinterHelper;

public class PrinterSettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener,
        View.OnClickListener {

    private Spinner labelMakeListSpinner;
    private Spinner labelTypeListSpinner;
    private TextInputLayout ipTextInputLayout;
    private TextInputEditText ipTextInputEdit;
    private TextInputLayout macTextInputLayout;
    private MacAddressEditText macTextInputEdit;

    private Map<Integer, String> labelTypes;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_printer_settings, container, false);

        labelMakeListSpinner = view.findViewById(R.id.labelMakeListSpinner);
        labelMakeListSpinner.setOnItemSelectedListener(this);

        labelTypeListSpinner = view.findViewById(R.id.labelTypeListSpinner);

        ipTextInputLayout = view.findViewById(R.id.ipTextInputLayout);

        ipTextInputEdit = view.findViewById(R.id.ipTextInputEdit);
        ipTextInputEdit.addTextChangedListener(new IPAddressTextWatcher());

        macTextInputLayout = view.findViewById(R.id.macTextInputLayout);
        macTextInputEdit = view.findViewById(R.id.macTextInputEdit);

        ImageButton saveButton = view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);

        ImageButton testButton = view.findViewById(R.id.testButton);
        testButton.setOnClickListener(this);

        loadPreferences();

        return view;
    }

    private void loadPreferences() {
        Activity activity = getActivity();

        if (activity != null) {
            SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);

            String labelMake = sharedPref.getString(PreferenceKeys.LABEL_MAKE, "");
            String labelType = sharedPref.getString(PreferenceKeys.LABEL_TYPE, "");

            String ip = sharedPref.getString(PreferenceKeys.IP, "");
            String mac = sharedPref.getString(PreferenceKeys.MAC, "");

            if (!labelMake.isEmpty()) {
                SpinnerAdapter adapter = labelMakeListSpinner.getAdapter();
                if (adapter != null) {
                    for (int i = 0; i < adapter.getCount(); i++) {
                        if (labelMake.equals(adapter.getItem(i))) {
                            labelMakeListSpinner.setSelection(i);
                            break;
                        }
                    }
                }
            }

            if (!labelType.isEmpty()) {
                SpinnerAdapter adapter = labelTypeListSpinner.getAdapter();
                if (adapter != null) {
                    for (int i = 0; i < adapter.getCount(); i++) {
                        if (labelType.equals(adapter.getItem(i))) {
                            labelTypeListSpinner.setSelection(i);
                            break;
                        }
                    }
                }
            }

            if (!ip.isEmpty()) {
                ipTextInputEdit.setText(ip);
            }

            if (!mac.isEmpty()) {
                macTextInputEdit.setText(mac);
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (parent.getId() == R.id.labelMakeListSpinner) {
            String selection = parent.getItemAtPosition(pos).toString();
            labelTypes = new TreeMap<>();

            switch (selection) {
                case "PT":
                    for (LabelInfo.PT type : LabelInfo.PT.values()) {
                        labelTypes.put(type.getId(), type.name());
                    }
                    break;
                case "QL700":
                    for (LabelInfo.QL700 type : LabelInfo.QL700.values()) {
                        labelTypes.put(type.getId(), type.name());
                    }
                    break;
                case "QL1100":
                    for (LabelInfo.QL1100 type : LabelInfo.QL1100.values()) {
                        labelTypes.put(type.getId(), type.name());
                    }
                    break;
                case "PT3":
                    for (LabelInfo.PT3 type : LabelInfo.PT3.values()) {
                        labelTypes.put(type.ordinal(), type.name());
                    }
                    break;
                case "QL1115":
                    for (LabelInfo.QL1115 type : LabelInfo.QL1115.values()) {
                        labelTypes.put(type.getId(), type.name());
                    }
                    break;
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(parent.getContext().getApplicationContext(),
                    android.R.layout.simple_spinner_item, labelTypes.values().toArray(new String[labelTypes.size()]));
            labelTypeListSpinner.setAdapter(adapter);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if (parent.getId() == R.id.labelMakeListSpinner) {
            if (!labelTypeListSpinner.getAdapter().isEmpty())
                labelTypeListSpinner.setSelection(0);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveButton:
                if (validateForm()) {
                    Activity activity = getActivity();

                    if (activity != null) {
                        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();

                        editor.putString(PreferenceKeys.LABEL_MAKE, labelMakeListSpinner.getSelectedItem().toString());
                        editor.putString(PreferenceKeys.LABEL_TYPE, labelTypeListSpinner.getSelectedItem().toString());

                        String item = labelTypeListSpinner.getSelectedItem().toString();

                        for (int key : labelTypes.keySet()) {
                            if (labelTypes.get(key).equals(item)) {
                                editor.putInt(PreferenceKeys.LABEL_NAME_INDEX, key);
                                break;
                            }
                        }

                        editor.putString(PreferenceKeys.IP, ipTextInputEdit.getText().toString());
                        editor.putString(PreferenceKeys.MAC, macTextInputEdit.getText().toString());

                        editor.apply();
                        Toast.makeText(activity, "Settings saved successfully!", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            case R.id.testButton:
                Activity activity = getActivity();

                if (activity != null) {
                    String[] text = {"Test User", "Test Company", "Test Position"};
                    PrinterHelper printerHelper = new PrinterHelper(activity);
                    printerHelper.print(text);
                }
                break;
        }
    }

    private boolean validateForm() {
        if (ipTextInputEdit.getText().toString().equals("")) {
            ipTextInputLayout.setError(getString(R.string.empty_ip));
            return false;
        } else if (!ipTextInputEdit.getText().toString().matches(Patterns.IP_ADDRESS.toString())) {
            ipTextInputLayout.setError(getString(R.string.invalid_ip));
            return false;
        } else if (macTextInputEdit.getText().toString().equals("")) {
            macTextInputLayout.setError(getString(R.string.empty_mac));
            return false;
        }

        return true;
    }
}
