package com.example.assignment_2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BroadcastInputFragment extends Fragment {

    private boolean isBatteryMode;
    private static final String CUSTOM_ACTION = "com.example.assignment_2.CUSTOM_INTENT_ALERT";

    private final BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            float batteryPct = (level / (float) scale) * 100;

            if (getView() != null) {
                TextView tvStatus = getView().findViewById(R.id.tv_input_title);
                tvStatus.setText(getString(R.string.battery_status_format, (int) batteryPct));
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_broadcast_input, container, false);

        TextView tvTitle = view.findViewById(R.id.tv_input_title);
        EditText etCustomText = view.findViewById(R.id.et_custom_text);
        Button btnAction = view.findViewById(R.id.btn_input_action);

        if (getArguments() != null) {
            isBatteryMode = getArguments().getBoolean("IS_BATTERY", false);
        }

        if (isBatteryMode) {
            etCustomText.setVisibility(View.GONE);
            btnAction.setVisibility(View.GONE);
            tvTitle.setText(getString(R.string.fetching_battery));

            requireContext().registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        } else {
            tvTitle.setText(getString(R.string.custom_broadcast_title));
            etCustomText.setVisibility(View.VISIBLE);
            btnAction.setVisibility(View.VISIBLE);
            btnAction.setText(getString(R.string.btn_fire_broadcast));

            btnAction.setOnClickListener(v -> {
                String messageToSend = etCustomText.getText().toString().trim();
                if (messageToSend.isEmpty()) {
                    etCustomText.setError(getString(R.string.error_empty_text));
                    return;
                }

                // 1. Package up and fire the global custom broadcast
                Intent intent = new Intent(CUSTOM_ACTION);
                intent.putExtra("PAYLOAD_TEXT", messageToSend);
                intent.setPackage(requireContext().getPackageName());
                requireContext().sendBroadcast(intent);

                // 2. Display a confirmation message so the user knows it worked
                Toast.makeText(getContext(), "Broadcast Sent: " + messageToSend, Toast.LENGTH_SHORT).show();

                // 3. FIX: Pop back immediately to the main interface screen
                getParentFragmentManager().popBackStack();
            });
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (isBatteryMode) {
            try {
                requireContext().unregisterReceiver(batteryReceiver);
            } catch (IllegalArgumentException e) {
                // Safely closed
            }
        }
    }
}