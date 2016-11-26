package com.vehar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.vehar.soundtouchandroid.R;

public class SendClient extends AppCompatActivity implements OnClickListener {

    Button buttonQR = null;
    Button buttonReadQR = null;
    Button buttonStartSending = null;

    TextView textMode = null;

    private boolean isSending = false;

    AudioStreaming streamer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        buttonQR = (Button) findViewById(R.id.buttonQR);
        buttonReadQR = (Button) findViewById(R.id.buttonReadQR);
        buttonStartSending = (Button) findViewById(R.id.buttonStartSending);
        textMode = (TextView) findViewById(R.id.textMode);

        streamer = new AudioStreaming();

        buttonStartSending.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View arg0) {
        int i = arg0.getId();
        if (i == R.id.buttonQR) {
            Intent intentGenQr = new Intent(this, QRCodeServer.class);
            startActivity(intentGenQr);

        } else if (i == R.id.buttonReadQR) {
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
            integrator.setPrompt("Scan a barcode");
            integrator.setBeepEnabled(false);
            integrator.initiateScan();
            integrator.setOrientationLocked(true);

        } else if (i == R.id.buttonStartSending) {
            if (isSending) {
                //TODO: Stop sending
                Toast.makeText(this, "Stopping...", Toast.LENGTH_SHORT).show();

                streamer.stopRecording();
                isSending = !isSending;
            } else {
                Toast.makeText(this, "Starting...", Toast.LENGTH_SHORT).show();

                streamer.startRecording(0);
                isSending = !isSending;
            }


        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                textMode.setText("Sending to IP: " + result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }


    }

}
