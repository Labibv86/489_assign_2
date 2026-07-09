package com.example.assignment_2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BroadcastDisplayFragment extends Fragment {

    private TextView tvReceivedMessage;
    private static final String CUSTOM_ACTION = "com.example.assignment_2.CUSTOM_INTENT_ALERT";

    private final BroadcastReceiver customReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && CUSTOM_ACTION.equals(intent.getAction())) {
                String msg = intent.getStringExtra("PAYLOAD_TEXT");
                if (tvReceivedMessage != null && msg != null) {
                    // FIX 3: Solved concatenation warning by using String.format layout architecture
                    String formattedDisplay = String.format("Broadcast Received Successfully!\n\nMessage Content:\n\"%s\"", msg);
                    tvReceivedMessage.setText(formattedDisplay);
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_broadcast_display, container, false);
        tvReceivedMessage = view.findViewById(R.id.tv_display_msg);

        IntentFilter filter = new IntentFilter(CUSTOM_ACTION);

        // FIX: ContextCompat automatically and safely applies the security flags on newer devices
        // while ignoring them gracefully on older Android API levels down to your min SDK.
        androidx.core.content.ContextCompat.registerReceiver(
                requireContext(),
                customReceiver,
                filter,
                androidx.core.content.ContextCompat.RECEIVER_NOT_EXPORTED
        );

        if (getArguments() != null) {
            String transmittedMessage = getArguments().getString("BROADCAST_MSG");

            Intent intent = new Intent(CUSTOM_ACTION);
            intent.putExtra("PAYLOAD_TEXT", transmittedMessage);

            intent.setPackage(requireContext().getPackageName());

            requireContext().sendBroadcast(intent);
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            requireContext().unregisterReceiver(customReceiver);
        } catch (IllegalArgumentException e) {
            // Already safely removed
        }
    }
}