package tw.kimble.gps;


public class GPSConstant {
	
	// The tag will be debug mode identifier by using Log.d(identifier, value)
	public static final String MAINA_TAG = "MAINA_TAG";
	public static final String RECEIVER_TASK_TAG = "RECEIVER_TASK_TAG";
	public static final String DEBUG_GPS_TAG = "DEBUG_GPS_TAG";
	
	// The minimum distance to change Updates in 10 meters
	public static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
	
	//The minimum time between updates in [ minute * (milliseconds) ]
	public static final long MIN_TIME_BW_UPDATES = 1 * (1000 * 60) ; 

}
