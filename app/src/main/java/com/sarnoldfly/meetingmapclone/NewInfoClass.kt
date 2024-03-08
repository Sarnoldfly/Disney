package com.sarnoldfly.meetingmapclone

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class NewInfoClass (var cont:Context, var activ:Activity, var map:GoogleMap) {




    fun localizacionPermitida():Boolean{
        /*Comprobamos si está el permiso concedido o no. Pasamos requireContext en vez de this,
        ya que estamos dentro de un fragment y necesitamos el contexto*/

        return ContextCompat.checkSelfPermission(cont,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    fun activarLocalizacion(){
        /*si el mapa no ha sido inicializado, salimos, ya que aun no podemos pedir nada*/
        if(map==null) return

        if(localizacionPermitida()){
            /*el usuario ha permitido la localizacion*/
            map.isMyLocationEnabled = true
        }else{
            /*El usuario no ha permitido la localizacion, entonces se lo pedimos*/
            pedirLocalizacion()
        }
    }


    fun pedirLocalizacion(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(activ, Manifest.permission.ACCESS_FINE_LOCATION)){
            /*Se pidió el permiso, pero el usuario no lo aceptó*/
            Toast.makeText(cont,"Ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()
        }else{
            /*Es la primera vez que se piden los permisos. Le pasamos el ultimo argumento, con una constante que hemos creado como
            * companion object de esta clase, para almacenar si el usuario acepta o no el permiso*/
            ActivityCompat.requestPermissions(activ, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                Fragment01.REQUEST_CODE_LOCATION)
        }
    }

}