package paula.smarthome;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import java.util.ArrayList;




public class Inicio extends AppCompatActivity {
    private final String TAG = "a";
    private final String serverUri = "tcp://200.5.235.62:1883";

    private MqttConnectOptions options = new MqttConnectOptions();
    MqttAndroidClient client ;



    // Instantiate the RequestQueue.
    String url ="http://200.5.235.62:8080/dispositivos";
    // Request a string response from the provided URL.

    //comentario 01

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final Gson gson = new Gson();
        final RequestQueue queue = Volley.newRequestQueue(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);


        final ArrayList<Dispositivo> dispositivos = new ArrayList<Dispositivo>();
        final GridView listaDispositivos = (GridView) findViewById(R.id.listaDispositivos);
        final DispositivosAdapter dispositivosAdapter = new DispositivosAdapter(getApplicationContext(), dispositivos);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        try {
                        JSONArray Jdispositivos = new JSONArray(response);

                            for (int i = 0; i < Jdispositivos.length(); i++) {

                                JSONObject c = Jdispositivos.getJSONObject(i);
                                Dispositivo d = new Dispositivo();
                                d.setId(c.getString("id"));
                                d.setTipo(c.getString("tipo"));
                                d.setEstado(Boolean.valueOf((c.getString("estado"))));
                                d.setUbicacion(c.getString("ubicacion"));

                                dispositivos.add(d);
                            }
                            listaDispositivos.setAdapter(dispositivosAdapter);

                        } catch (final JSONException e) {
                            Log.e(TAG, "Json parsing error: " + e.getMessage());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),
                                            "Json parsing error: " + e.getMessage(),
                                            Toast.LENGTH_LONG)
                                            .show();
                                }
                            });

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });
        queue.add(stringRequest);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queue.add(stringRequest);
                Snackbar.make(view, "Dispositivos Actualizados", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });







        //Conectarse
    String clientId = MqttClient.generateClientId();

    //(this.getApplicationContext(), "tcp://10.105.231.63:1883",clientId);
        client = new MqttAndroidClient(getApplicationContext(), serverUri, clientId);


        client.setCallback(new MqttCallbackExtended() {
               @Override
               public void connectComplete(boolean reconnect, String serverURI) {

                   if (reconnect) {
                       addToHistory("Reconnected to : " + serverURI);
                       // Because Clean Session is true, we need to re-subscribe
                       //subscribeToTopic("home/features/outlet/change_state",1);
                   } else {
                       addToHistory("Connected to: " + serverURI);
                       //subscribeToTopic("home/features/outlet/change_state",1);
                       //subscribeToTopic();
                   }
               }

            @Override
            public void connectionLost(Throwable cause) {
                addToHistory("The Connection was lost.");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                /*
                switch(topic){
                    case "home/features/outlet/change_state":

                        break;
                    default:
                        break;
                }
                  */
                addToHistory("Incoming message: " + new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
            }
        });


        try {
            options.setUserName("usuario1");
            options.setPassword("password3".toCharArray());
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d(TAG, "onSuccess");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d(TAG, "onFailure");

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        listaDispositivos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Dispositivo dispositivo = dispositivos.get(position);
                dispositivo.setEstado(!dispositivo.getEstado());
                String json = gson.toJson(dispositivo);
                publishMessage(json,"home/feature/outlet/change_state");
                dispositivosAdapter.notifyDataSetChanged();
            }
        });

    }


    private void addToHistory(String mainText){
        System.out.println("LOG: " + mainText);
        Snackbar.make(findViewById(android.R.id.content), mainText, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    //String subscriptionTopic = "home";
    //int qos = 1;
    public void subscribeToTopic(String topic, int qos){

    try {
        IMqttToken subToken = client.subscribe(topic, qos);
        subToken.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                addToHistory("Subscribed!");
                // The message was published
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken,
                                  Throwable exception) {
                // The subscription could not be performed, maybe the user was not
                // authorized to subscribe on the specified topic e.g. using wildcards

            }
        });
    } catch (MqttException e) {
        e.printStackTrace();
    }


    }

    //final String publishTopic = "exampleAndroidPublishTopic";
    //final String publishMessage = "Hello World!";
    public void publishMessage(String mensaje, String topic){

        try {
            MqttMessage message = new MqttMessage();
            message.setPayload(mensaje.getBytes());
            client.publish(topic, message);
            addToHistory("Message Published");
            if(!client.isConnected()){
                addToHistory(client.getBufferedMessageCount() + " messages in buffer.");
            }
        } catch (MqttException e) {
            System.err.println("Error Publishing: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inicio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            System.out.println("Se seleccionó opciones");
            return true;

        }

        return super.onOptionsItemSelected(item);
    }
}
