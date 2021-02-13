package com.example.agendacontactos.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.agendacontactos.R

class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v = inflater.inflate(R.layout.fragment_home, container, false)
        var buttonContacto = v.findViewById<Button>(R.id.buttonContacto)


        buttonContacto.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_contactoFragment)
        }
        return v
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                HomeFragment().apply {
                }
    }
}