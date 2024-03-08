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
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Fragment01 : Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationClickListener  {

    private lateinit var map: GoogleMap
    private lateinit var loki:NewInfoClass

    var poly :Polyline?=null
    var start = ""
    var end = ""



    companion object {
        const val REQUEST_CODE_LOCATION = 0
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_01, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        makeMap()


    }




    private fun makeMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapa) as SupportMapFragment
        mapFragment.getMapAsync(this)//ese this, nos avisa que tenemos que hacer un Callback de onmapready
    }




    override fun onMapReady(nicomap: GoogleMap) {
        map=nicomap
        map.setOnMyLocationClickListener (this)
        loki = NewInfoClass(requireContext(), requireActivity(),map)
        loki.activarLocalizacion()

    }
    fun traceRoute(valor:Int){
        if(valor==1){
            poly?.remove()
            poly = null
            end ="0.030832 , 40.074487"
            crearRuta()

        }
        else if(valor==2){
            poly?.remove()
            poly = null
            end = "0.116460 , 40.061803"
            crearRuta()

        }
        else if(valor==3){
            poly?.remove()
            poly = null
            end = "0.008946 , 40.025295"
            crearRuta()

        }
    }

    override fun onMyLocationClick(p0: Location) {
        start = "${p0.longitude}, ${p0.latitude}"
    }





    /*Tengo que crear esta función por si el usuario ha minimizado nuestra app y ha revocado los permisos*/
    override fun onResume() {
        super.onResume()
        /*Primero comprobamos si el mapa está cargado*/
        if(!::map.isInitialized) return
        if(!loki.localizacionPermitida()){
            map.isMyLocationEnabled = false
            Toast.makeText(requireContext(),"Para activar la ubicación, ve a ajustes y acepta el permiso", Toast.LENGTH_LONG).show()

            /*map.animateCamera(CameraUpdateFactory.newLatLngZoom(loki, 18f),4000, null)*/
        }
    }
    /*Esta funcion es para que el botón de ubicación me lleve a mi ubicación o no. Si devuelve false, me lleva*/







    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openrouteservice.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    /*Ahora necesitamos crear una API Service, que será la interfaz que llame al método GET de ese endpoint
    La hemos creado como interfaz (Kotlin Class), ServicioApi*/
    fun crearRuta(){
        Log.i("PEPE CREAR RUTA START: ",start)
        Log.i("PEPE CREAR RUTA END: ",end)
        /*Las llamadas a internet no se pueden hacer en el hilo principal, hay que hacerlo en corutinas
        para ello ejecutamos CoroutineScope(Dispatcher)  */


        CoroutineScope(Dispatchers.IO).launch {//con esto lanzamos la corrutina,
            /*Lanzamos el hilo a través de la interfaz que hemos creado y la func conseguirRuta de la misma*/
            val llamada = getRetrofit().create(APIService::class.java).givemeroute("5b3ce3597851110001cf62482f4f1876a2644f81ac0aaf6d860f35de",start,end)
            /*getRetrofit() puede devolver codigos de error si nos hemos equivocado al construir la url, si la web no está
            disponible, etc. En ese caso, no entraría en el siguiente if:*/
            if(llamada.isSuccessful){
                /*Si la llamada ha recogido la ruta, ya la tengo en esta variable*/
                /*Necesitamos permiso de internet en el manifest, para poder acceder a la web de rutas*/
                Log.i("PEPE TENGO RUTA:", "OK")
                dibujarRuta(llamada.body())
            }else{
                Log.i("PEPE TENGO RUTA:","NO")
            }
        }
    }

    private fun dibujarRuta(respuestaDeRuta: RouteResponse?) {
        /*Rellenamos el poliline con los datos que nos ha devuelto el servicio*/
        val polilineOptions = PolylineOptions()


        respuestaDeRuta?.features?.first()?.geometry?.coordinates?.forEach{
            /*para cada linea de la listaagregamos a la variable polilineOptions, el valor capturado(lati,longit)*/
            polilineOptions.add(LatLng(it[1],it[0]))
            /*Ponemos 1 primero, pq la api devuelve primero la latitud y no la longitud*/
        }
        activity?.runOnUiThread{
            /*Ejecutamos la acción de dibujado en la corutina*/
            poly = map.addPolyline(polilineOptions)
        }
    }










    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            REQUEST_CODE_LOCATION -> if(grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                map.isMyLocationEnabled=true
            }else{
                /*El usuario ha rechazado el permiso*/
                Toast.makeText(requireContext(),"Para activar la ubicación, ve a ajustes y acepta el permiso", Toast.LENGTH_SHORT).show()
            }
            /*Si se hubiera pasado otro permiso que no fuera el de ubicacion, que no va a pasar*/
            else -> {}

        }

    }

}
/*
0.061803, 0.116460 renega
40.025295, 0.008946 terrenito
40.074487, 0.030832desierto de las palmas
        */