package utility;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import java.net.URI;
import java.util.List;

/**
 * Created by Ketan on 10/31/2015.
 */
public class Request {
    public static String httpGet(String url,List<NameValuePair> params) throws JSONException
    {
        try
        {
            //Below code for request url and retrieve data in json format
            HttpClient httpClient;
            HttpGet httpGet;
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 20000);
            httpClient = new DefaultHttpClient(httpParams);
            String query=null;
            if(params!=null){
                query = URLEncodedUtils.format(params, "utf-8");
            }
            URI uri = URIUtils.createURI("http", "api.openweathermap.org",0, "/data/2.5/weather", query, null); //can be null
            httpGet = new HttpGet(uri);
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-type", "application/json");
            HttpResponse serverResponse = httpClient.execute(httpGet);
            int statusCode=serverResponse.getStatusLine().getStatusCode();
            if(statusCode==200){
                Log.i("API", "ResponseCode: " + serverResponse.getStatusLine());
                //responseString = EntityUtils.toString(serverResponse.getEntity(), "UTF-8");
                return EntityUtils.toString(serverResponse.getEntity(), "UTF-8");
                //return responseString;
            }
            Log.i("Response",statusCode+"");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
