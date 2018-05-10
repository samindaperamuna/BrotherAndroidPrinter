package fifthgen.com.brotherandroidprinter.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import fifthgen.com.brotherandroidprinter.R;
import fifthgen.com.brotherandroidprinter.ui.fragment.LabelSettingsFragment;
import fifthgen.com.brotherandroidprinter.ui.fragment.PrinterSettingsFragment;

public class MainActivity extends AppCompatActivity {

    private static final int FRAG_COUNT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set activity title.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.main_title));
        }

        // Load the fragment manager into the ViewPager instance.
        SettingsPagerAdapter pagerAdapter = new SettingsPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        
    }

    /**
     * Adapter to handle the <code>{@link ViewPager}</code> fragment transitions.
     */
    private class SettingsPagerAdapter extends FragmentPagerAdapter {

        SettingsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                default:
                    return new PrinterSettingsFragment();
                case 1:
                    return new LabelSettingsFragment();
            }
        }

        @Override
        public int getCount() {
            return FRAG_COUNT;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                default:
                    return getString(R.string.printer_settings);
                case 1:
                    return getString(R.string.label_settings);
            }
        }
    }
}
