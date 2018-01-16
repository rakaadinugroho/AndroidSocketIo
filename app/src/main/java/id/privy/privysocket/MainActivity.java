package id.privy.privysocket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONObject;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {
    private Socket socket;
    private TextView text_data;
    private EditText text_input;
    private Button send_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text_data   = findViewById(R.id.privy_textdata);
        text_input  = findViewById(R.id.privy_inputdata);
        send_data   = findViewById(R.id.send_data);
        try {
            socket  = IO.socket("http://192.168.100.109:3030");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.on("ktp-reader", onNewMessage);
        socket.connect();

        send_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mydata   = text_input.getText().toString();
                sendmessage(mydata);
            }
        });
    }

    private void sendmessage(String data) {
        socket.emit("cekevent",data);
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.e("data", args[0].toString());
                        text_data.setText(args[0].toString());
                    } catch (Exception e) {

                    }

                }
            });
        }
    };
}
