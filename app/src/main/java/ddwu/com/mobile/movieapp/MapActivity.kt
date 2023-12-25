package ddwu.com.mobile.movieapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.StyleSpan
import ddwu.com.mobile.movieapp.data.PlaceRoot
import ddwu.com.mobile.movieapp.databinding.ActivityMapBinding
import ddwu.com.mobile.movieapp.network.IPlaceAPIService
import ddwu.com.mobile.movieapp.ui.PlaceAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale

class MapActivity: AppCompatActivity() {

    private val TAG = "MapActivityTag"

    val mapBinding by lazy {
        ActivityMapBinding.inflate(layoutInflater)
    }

    private lateinit var fusedLocationClient : FusedLocationProviderClient
    private lateinit var geocoder : Geocoder
    private lateinit var currentLoc : Location
    lateinit var adapter : PlaceAdapter

    private lateinit var googleMap : GoogleMap
    var centerMarker : Marker? = null

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mapBinding.root)

        adapter = PlaceAdapter()
        mapBinding.rvPlaces.adapter = adapter
        mapBinding.rvPlaces.layoutManager = LinearLayoutManager(this)

        val retrofit = Retrofit.Builder()
            .baseUrl(resources.getString(R.string.naver_api_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(IPlaceAPIService::class.java)


        mapBinding.btnSearch2.setOnClickListener {
            val keyword = mapBinding.etKeyword.text.toString()

            val apiCall = service.getPlacesByKeyword(
                resources.getString(R.string.client_id),
                resources.getString(R.string.client_secret),
                keyword
            )

            apiCall.enqueue(
                object: Callback<PlaceRoot> {
                    override fun onResponse(call: Call<PlaceRoot>, response: Response<PlaceRoot>) {
                        val placeRoot = response.body()
                        adapter.places = placeRoot?.items
                        adapter.notifyDataSetChanged()
                    }

                    override fun onFailure(call: Call<PlaceRoot>, t: Throwable) {
                    }

                }
            )
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        geocoder = Geocoder(this, Locale.getDefault())
        getLastLocation()   // 최종위치 확인

        mapBinding.btnPermit.setOnClickListener {
            checkPermissions()
//            addMarker(LatLng(37.606320, 127.041808))
            drawLine()
        }

        mapBinding.btnLastLoc.setOnClickListener {
            getLastLocation()
        }

        mapBinding.btnLocStart.setOnClickListener {
            startLocUpdates()
        }

        mapBinding.btnLocStop.setOnClickListener {
            fusedLocationClient.removeLocationUpdates(locCallback)
        }


        mapBinding.btnLocTitle.setOnClickListener {
            geocoder.getFromLocation(37.601025, 127.04153, 5) { addresses ->
                CoroutineScope(Dispatchers.Main).launch {
                    showData("위도: ${currentLoc.latitude}, 경도: ${currentLoc.longitude}")
                    showData(addresses.get(0).getAddressLine(0).toString())
                }
            }
        }

        showData("Geocoder isEnabled: ${Geocoder.isPresent()}")

        val mapFragment: SupportMapFragment
                = supportFragmentManager.findFragmentById(R.id.map)
                as SupportMapFragment

        mapFragment.getMapAsync (mapReadyCallback)

    }


    /*GoogleMap 로딩이 완료될 경우 실행하는 Callback*/
    val mapReadyCallback = object: OnMapReadyCallback {
        override fun onMapReady(map: GoogleMap) {
            googleMap = map
            Log.d(TAG, "GoogleMap is ready")

            googleMap.setOnMarkerClickListener {
                Toast.makeText(this@MapActivity, it.tag.toString(), Toast.LENGTH_SHORT).show()
                false
            }

            googleMap.setOnMapClickListener { latLng ->
                Toast.makeText(this@MapActivity, latLng.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }


    /*마커 추가*/
    fun addMarker(targetLoc: LatLng) {  // LatLng(37.606320, 127.041808)
        val markerOptions: MarkerOptions = MarkerOptions()
        markerOptions.position(targetLoc)
            .title("마커 제목")
            .snippet("마커 말풍선")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))

        centerMarker = googleMap.addMarker(markerOptions)
        centerMarker?.showInfoWindow()
        centerMarker?.tag = "database_id"
    }


    /*선 추가*/
    fun drawLine() {

        val PolylineOptions = PolylineOptions()
            .addSpan(StyleSpan(Color.RED))
            .add(LatLng(37.604151, 127.042453))
            .add(LatLng(37.605347, 127.041207))
            .add(LatLng(37.606038, 127.041344))
            .add(LatLng(37.606220, 127.041674))
            .add(LatLng(37.606631, 127.041595))
            .add(LatLng(37.606823, 127.042380))

        val line = googleMap.addPolyline((PolylineOptions))
    }


    /*위치 정보 수신 시 수행할 동작을 정의하는 Callback*/
    val locCallback : LocationCallback = object : LocationCallback() {
        @SuppressLint("NewApi")
        override fun onLocationResult(locResult: LocationResult) {
            currentLoc = locResult.locations.get(0)
            geocoder.getFromLocation(currentLoc.latitude, currentLoc.longitude, 5) { addresses ->
                CoroutineScope(Dispatchers.Main).launch {
                    showData("위도: ${currentLoc.latitude}, 경도: ${currentLoc.longitude}")
                    showData(addresses?.get(0)?.getAddressLine(0).toString())
                }
            }
            val targetLoc: LatLng = LatLng(currentLoc.latitude, currentLoc.longitude)
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(targetLoc, 17F))
        }
    }

    /*API 33 이전 사용 방식*/
//            CoroutineScope(Dispatchers.Main).launch {
//                val addresses = geocoder.getFromLocation(currentLoc.latitude, currentLoc.longitude, 5)
//                showData("위도: ${currentLoc.latitude}, 경도: ${currentLoc.longitude}")
//                showData(addresses?.get(0)?.getAddressLine(0).toString())
//            }


    /*위치 정보 수신 설정*/
    val locRequest = LocationRequest.Builder(5000)
        .setMinUpdateIntervalMillis(3000)
        .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
        .build()

    /*위치 정보 수신 시작*/
    @SuppressLint("MissingPermission")
    private fun startLocUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locRequest,     // LocationRequest 객체
            locCallback,    // LocationCallback 객체
            Looper.getMainLooper()  // System 메시지 수신 Looper
        )
    }



    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locCallback)
    }

    /*LBSTest 관련*/
    //    최종위치 확인
    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                showData(location.toString())
                currentLoc = location
            } else {
                currentLoc = Location("기본 위치")      // Last Location 이 null 경우 기본으로 설정
                currentLoc.latitude = 37.606816
                currentLoc.longitude = 127.042383
            }
        }
        fusedLocationClient.lastLocation.addOnFailureListener { e: Exception ->
            Log.d(TAG, e.toString())
        }
    }


    fun callExternalMap() {
        val locLatLng   // 위도/경도 정보로 지도 요청 시
                = String.format("geo:%f,%f?z=%d", 37.606320, 127.041808, 17)
        val locName     // 위치명으로 지도 요청 시
                = "https://www.google.co.kr/maps/place/" + "Hawolgok-dong"
        val route       // 출발-도착 정보 요청 시
                = String.format("https://www.google.co.kr/maps?saddr=%f,%f&daddr=%f,%f",
            37.606320, 127.041808, 37.601925, 127.041530)
        val uri = Uri.parse(locLatLng)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }


    private fun showData(data : String) {
        mapBinding.tvData.setText(mapBinding.tvData.text.toString() + "\n${data}")
    }


    fun checkPermissions () {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
            && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            showData("Permissions are already granted")  // textView에 출력
        } else {
            locationPermissionRequest.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ))
        }
    }

    /*registerForActivityResult 는 startActivityForResult() 대체*/
    val locationPermissionRequest
            = registerForActivityResult( ActivityResultContracts.RequestMultiplePermissions() ) {
            permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                showData("FINE_LOCATION is granted")
            }

            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                showData("COARSE_LOCATION is granted")
            }
            else -> {
                showData("Location permissions are required")
            }
        }
    }
}