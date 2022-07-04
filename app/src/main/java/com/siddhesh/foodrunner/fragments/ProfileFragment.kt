package com.siddhesh.foodrunner.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.siddhesh.foodrunner.R

class ProfileFragment : Fragment() {
    lateinit var txtName: TextView
    lateinit var txtMobileNo: TextView
    lateinit var txtEmail: TextView
    lateinit var txtAddress: TextView
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        txtName = view.findViewById(R.id.txtName)
        txtMobileNo = view.findViewById(R.id.txtMobileNO_)
        txtEmail = view.findViewById(R.id.txtemail)
        txtAddress = view.findViewById(R.id.txtAddress)
        sharedPreferences = activity!!.getSharedPreferences(
            getString(R.string.preference_file_name),
            Context.MODE_PRIVATE
        )
        txtName.text = sharedPreferences.getString("name", "name")
        txtEmail.text = sharedPreferences.getString("email", "email")
        txtMobileNo.text = sharedPreferences.getString("mobile_number", "0")
        txtAddress.text = sharedPreferences.getString("address", "address")
        return view
    }
}


