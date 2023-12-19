package com.example.foodection

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.foodection.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.LatLngBounds
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private lateinit var auth: FirebaseAuth
    private var db = Firebase.firestore
    private val shopList = ArrayList<ShopOwner>()

    private var activityDetail : Boolean = false
    private lateinit var usernameOwner : String

    private val boundsBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        //Tambahan
        val extras = intent.extras
        if(extras!=null){
            val getActivity = extras.getBoolean("fromDetail")
            activityDetail = getActivity
            if (getActivity){
                usernameOwner = ShopOwner.getInstance().getUserName().toString()
            }
        }
        // Selesai Tambah

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        //--------------
    }

    override fun onMapReady(googleMap: GoogleMap) {
        //INI BANYAK KU UBAH
        mMap = googleMap

        if (activityDetail) {
            db.collection("Toko").whereEqualTo("username", usernameOwner).addSnapshotListener{value, _ ->
                assert(value != null)

                if (!value!!.isEmpty){
                    for (snapshot in value) {
                        val latitude = snapshot.getString("latitude")
                        val longitude = snapshot.getString("longitude")

                        val latitudeLoc = latitude?.toDouble()
                        val longitudeLoc = longitude?.toDouble()
                        val shopLocation= LatLng(latitudeLoc!!, longitudeLoc!!)
                        mMap.addMarker(MarkerOptions().position(shopLocation).title(snapshot.getString("shopName")))
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(shopLocation,15f))
                    }
                }
            }
        }else{
            db.collection("Toko").get().addOnSuccessListener { queryDocumentSnapshots ->
                if(!queryDocumentSnapshots.isEmpty){
                    for (shops in queryDocumentSnapshots){
                        val listShop = shops.toObject(ShopOwner::class.java)
                        shopList.add(listShop)

                        val latitude = listShop.getLatitude()
                        val longitude = listShop.getLongitude()
                        val latitudeShop = latitude?.toDouble()
                        val longitudeShop = longitude?.toDouble()

                        val latLng = LatLng(latitudeShop!!, longitudeShop!!)
                        mMap.addMarker(MarkerOptions().position(latLng).title(listShop.getShopName()))

                        boundsBuilder.include(latLng)
                    }
                }

                val bounds: LatLngBounds = boundsBuilder.build()
                mMap.animateCamera(
                    CameraUpdateFactory.newLatLngBounds(
                        bounds,
                        resources.displayMetrics.widthPixels,
                        resources.displayMetrics.heightPixels,
                        300
                    )
                )
            }
        }

        getMyLocation()
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
    }


    //Ini 2 fungsi ku tambah
    private val requestPermissionLauncer = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {isGranted: Boolean->
        if (isGranted){
            getMyLocation()
        }
    }

    private fun getMyLocation(){
        if(ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ){
            mMap.isMyLocationEnabled = true
        }else{
            requestPermissionLauncer.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}