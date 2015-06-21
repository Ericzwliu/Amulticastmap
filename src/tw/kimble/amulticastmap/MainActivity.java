package tw.kimble.amulticastmap;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.UnknownHostException;

import tw.kimble.gps.GPSConstant;
import tw.kimble.gps.GPSTracker;
import android.app.Activity;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.amulticastmap.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

public class MainActivity extends Activity implements OnMapReadyCallback{
	
	private static final int PORT = 2624;
	
	
	GPSTracker gps;
	DatagramSocket transmitSock;
	MulticastLock multicastLock;
	MulticastSocket receiveSock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		gps = new GPSTracker(this);
		try {
			transmitSock = new DatagramSocket();
			WifiManager wifiManager = (WifiManager) getSystemService(MainActivity.this.WIFI_SERVICE); 
			
			multicastLock = wifiManager.createMulticastLock("pseudo-ssdp");
			multicastLock.acquire();
			
			receiveSock = makeMulticastListenSocket();
			receiveSock.joinGroup(ssdpSiteLocalV6());
			
	
			
			MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
			
			mapFragment.getMapAsync(this);
			
		} catch(UnknownHostException unKnownHostExcption) {
			
		} catch (IOException ioException) {
			// TODO: handle exception
		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onMapReady(GoogleMap map) {
		// TODO Auto-generated method stub
        
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(24.7910828, 121.3848872), 16));
        
        IconGenerator iconGenerator = new IconGenerator(this);
        
        addIcon(map, iconGenerator, "Eric", new LatLng(24.7910928, 121.3818872), iconGenerator.STYLE_RED);
        
        map.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker arg0) {
				// TODO Auto-generated method stub
				
				Toast.makeText(MainActivity.this, "Message Now", 3000).show();
				
				return false;
			}
		});;
	}
	
	private void addIcon(GoogleMap map, IconGenerator iconFactory, String text, LatLng position, int iconStyle){
		iconFactory.setStyle(iconStyle);
		MarkerOptions markerOptions = 
				new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text)))
				.position(position)
				.anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV())
				.snippet(text)
				.title(text);
		
		map.addMarker(markerOptions);
	}
	
	private MulticastSocket makeMulticastListenSocket() throws IOException {
		
		MulticastSocket rval = new MulticastSocket(PORT);
		NetworkInterface nif = NetworkInterface.getByName("wlan0");
		if (null != nif) {
			Log.d(GPSConstant.MAINA_TAG, "picking interface " + nif.getName()
					+ " for transmit");
			rval.setNetworkInterface(nif);
		}
		return rval;
		
	}
	
	public InetAddress ssdpSiteLocalV6() throws UnknownHostException {
		return InetAddress.getByName("FF05::c");
	}
	
	public void clickedTransmit() {
		
	} 
}
