package ms.com.extensionandlib

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import java.lang.ref.WeakReference

class GetLocation(val activity: WeakReference<Activity>?,
                  val callBack : WeakReference<DataCallBack<Location>>?) {
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var mLastLocation: Location
    private lateinit var mLocationRequest: LocationRequest

    companion object {
        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 11
        private const val SETTING_CODE = 12
        private const val INTERVAL: Long = 2000
        private const val FASTEST_INTERVAL: Long = 1000
    }

    init {
        activity?.get()?.let {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(it)
        }
    }

    fun getLocation() {
        if (!checkPermissions()) {
            requestPermissions()
        } else {
            getLastLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        activity?.get()?.let {
            mFusedLocationClient?.lastLocation?.addOnCompleteListener(it) { task ->
                if (task.isSuccessful && task.result != null) {
                    val result = task.result
                    val lat = result?.latitude
                    val long = result?.longitude
                    Log.d("LocationDcv", "$lat;$long")
                } else {
                    Log.w(ContentValues.TAG, "getLastLocation:exception", task.exception)
                }
            }
        }
    }

    fun getLocationUpdate() {
        val locationManager =
            activity?.get()?.getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager?
        if (locationManager?.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) == false) {
            buildAlertMessageNoGps()
        } else {
            if (checkPermissions()) {
                startLocationUpdates()
            }
        }
    }

    private fun startLocationUpdates() {
        mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = INTERVAL
        mLocationRequest.fastestInterval = FASTEST_INTERVAL
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest)
        val locationSettingsRequest = builder.build()
        val settingsClient = activity?.get()?.let { LocationServices.getSettingsClient(it) }
        settingsClient?.checkLocationSettings(locationSettingsRequest)

        mFusedLocationClient =
            activity?.get()?.let { LocationServices.getFusedLocationProviderClient(it) }
        if (activity?.get()?.applicationContext?.let {
                ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION)
            } != PackageManager.PERMISSION_GRANTED && activity?.get()?.applicationContext?.let {
                ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_COARSE_LOCATION)
            } != PackageManager.PERMISSION_GRANTED) {
            return
        }
        mFusedLocationClient?.requestLocationUpdates(mLocationRequest,
            mLocationCallback,
            Looper.myLooper())
    }

    private fun buildAlertMessageNoGps() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        val builder = activity?.get()?.let { AlertDialog.Builder(it) }
        builder?.setMessage("Bạn cần bật GPS để tiếp tục")?.setCancelable(false)
            ?.setPositiveButton("Đồng Ý") { dialog, id ->
                activity?.get()?.let {
                    ActivityCompat.startActivityForResult(
                        it,
                        intent,
                        SETTING_CODE,
                        null
                    )
                }
            }?.setNegativeButton("No") { dialog, id ->
                dialog.cancel()
            }
        val alert = builder?.create()
        alert?.show()
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation)
        }
    }

    fun onLocationChanged(location: Location) {
        mLastLocation = location
        callBack?.get()?.data(location)
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        mFusedLocationClient?.removeLocationUpdates(mLocationCallback)
    }

    fun onRequestPermissionsResult(requestCode: Int,
                                   permissions: Array<String>,
                                   grantResults: IntArray) {
        Log.i(ContentValues.TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            when {
                grantResults.isEmpty() -> Log.i(ContentValues.TAG, "User interaction was cancelled.")
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> getLocationUpdate()
                else -> {

                }
            }
        }
        if (requestCode == SETTING_CODE) {
            when {
                grantResults.isEmpty() -> Log.d(ContentValues.TAG, "User interaction was cancelled.")
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> getLocationUpdate()
            }

        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SETTING_CODE) {
            getLocationUpdate()
        }
    }

    fun onDestroy() {
        stopLocationUpdates()
    }


    private fun checkPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity?.get()?.applicationContext?.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                activity?.get()?.let {
                    ActivityCompat.requestPermissions(it,
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_PERMISSIONS_REQUEST_CODE)
                }
                false
            }
        } else {
            true
        }
    }

    private fun startLocationPermissionRequest() {
        activity?.get()?.let {
            ActivityCompat.requestPermissions(it,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_PERMISSIONS_REQUEST_CODE)
        }
    }

    private fun requestPermissions() {
        val shouldProvideRationale = activity?.get()?.let {
            ActivityCompat.shouldShowRequestPermissionRationale(it,
                Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        if (shouldProvideRationale == true) {
            startLocationPermissionRequest()

        } else {
            Log.i(ContentValues.TAG, "Requesting permission")
            startLocationPermissionRequest()
        }
    }

}