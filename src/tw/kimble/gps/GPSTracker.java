package tw.kimble.gps;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;

public class GPSTracker extends Service {
	
	private final Context mContext;
	
	LocationManager locationManager;
	int status;
	
	public GPSTracker(Context context) {
		mContext = context;
		status = 0x0000;
	}
	
	

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		try {
			locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
			
			status += locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ? 0x0001 : 0x0000;
			status += locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ? 0x0010 : 0x0000;
			
			Log.v(GPSConstant.DEBUG_GPS_TAG, "status : " + Integer.toBinaryString(status));
		} catch (Exception e) {
			// TODO: handle exception
		}	
		
		return null;
	}

}
