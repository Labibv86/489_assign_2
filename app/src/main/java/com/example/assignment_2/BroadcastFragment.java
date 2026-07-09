package com.example.assignment_2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.os.BatteryManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BroadcastFragment extends Fragment {

    private Spinner spinner;

    private final BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int batteryPct = (int) ((level / (float) scale) * 100);
            Toast.makeText(context, "System Broadcast Caught! Battery: " + batteryPct + "%", Toast.LENGTH_LONG).show();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_broadcast, container, false);

        spinner = view.findViewById(R.id.broadcast_spinner);
        Button btnProceed = view.findViewById(R.id.btn_proceed);

        String[] options = {"Custom broadcast receiver", "System battery notification receiver"};

        // FIX: Added basic context null check to satisfy warning
        Context context = getContext();
        if (context != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, options);
            spinner.setAdapter(adapter);
        }

        // Inside your existing BroadcastFragment.java, find your button click logic and update it:
        btnProceed.setOnClickListener(v -> {
            int selectedPosition = spinner.getSelectedItemPosition();

            BroadcastInputFragment inputFragment = new BroadcastInputFragment();
            Bundle args = new Bundle();

            // Simplify the if-else block directly into a single logical condition
            args.putBoolean("IS_BATTERY", selectedPosition != 0);

            inputFragment.setArguments(args);

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, inputFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        // FIX: Ensure context is valid before unregistering receiver
        Context currentContext = getContext();
        if (currentContext != null) {
            try {
                currentContext.unregisterReceiver(batteryReceiver);
            } catch (Exception e) {
                // Safe catch if it wasn't registered
            }
        }
    }
}