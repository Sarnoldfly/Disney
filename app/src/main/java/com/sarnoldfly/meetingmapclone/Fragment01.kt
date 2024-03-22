package com.sarnoldfly.meetingmapclone

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView.FindListener
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.net.Uri
import android.content.Intent
import android.util.Log
import android.widget.TextView
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Fragment01 : Fragment()  {

    lateinit var madeCharacters :TextView
    lateinit var root :View


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        root = inflater.inflate(R.layout.fragment_01, container, false)
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        madeCharacters = root.findViewById(R.id.madeCharacters)


        madeCharacters()
    }

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openrouteservice.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    
    fun madeCharacters(){
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(APIService::class.java).giveMeCharacters()

            if(call.isSuccessful){
                Log.i("PEPE:", "OK")                //var call = call.body()?.data
                //var funcionData =call.body()?.data
                callCharacters(call.body()?.data)
            }else{
                Log.i("PEPE:", "NO")
            }
        }
    }
    private fun callCharacters (nameList: List<Data>?) {
        //stringBuilder es un método que te crea un String con los valores que le demos
        //val stringBuilder = StringBuilder()
        if(nameList != null) {
            //Si se quiere poner un scrollview, se necesita utilizar el runOnUiThread
            //Si no, se puede mandar al Stringbuilder, y lanzarlo desde otra función
            activity?.runOnUiThread {
                madeCharacters.text="Fragment 1 \n"
                nameList.forEach {
                    madeCharacters.append("Personaje: ${it.name} \n")
                }
            }
                //apend y el ("nombreDelTextview".textView =


        }

    }

}