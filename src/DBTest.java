import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import org.json.*;

public class DBTest {

    // don't change! Is for connection building

    public String makeGETRequest(String urlName) {
        BufferedReader rd = null;
        StringBuilder sb = null;
        String line = null;
        try {
            URL url = new URL(urlName);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            sb = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                sb.append(line + '\n');
            }
            conn.disconnect();
            return sb.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";

    }

    // Can strat to edit here :)

    // here we make a JSON array and we can iterate through it to get the individual
    // objects and data. KEY is the name of the colomn
    public ArrayList<String> parseJSON(String jsonString) {
        int LightID = 0;
        int LightOn = 0;
        String LightName = "";
        String LightFunction = "";
        String SwitchType = "";
        ArrayList<String> data = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(jsonString);
            for (int i = 0; i < array.length(); i++) {
                JSONObject curObject = array.getJSONObject(i);
                // var += curObject.getString("Date") + " ";

                LightID = curObject.getInt("LightID");
                LightOn = curObject.getInt("LightOn");
                LightName = curObject.getString("LightName");
                LightFunction = curObject.getString("LightFunction");
                SwitchType = curObject.getString("SwitchType");

                data.add(String.valueOf(LightID));
                data.add(String.valueOf(LightOn));
                data.add(LightName);
                data.add(LightFunction);
                data.add(SwitchType);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }

    public void toggleValue(int LightID) {

    }

    public static void main(String[] args) {
        DBTest rc = new DBTest();
        String response = rc.makeGETRequest("https://studev.groept.be/api/a23ib2b05/Get_all_lights");
        ArrayList<String> st = new ArrayList<>();
        st = rc.parseJSON(response);
        for (String s : st) {
            System.out.println(s);
        }

    }
}
