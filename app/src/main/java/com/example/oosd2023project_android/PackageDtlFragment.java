package com.example.oosd2023project_android;
/**
 * 'fragment_package_dtl.xml' Controller Class
 *  Created by : Deepa Thoppil
 *  Dated : 15-10-2023
 */
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PackageDtlFragment extends Fragment {

    TextView txtPrice,txtPkgDescription,txtStartDate,txtEndDate;
    RequestQueue requestQueue;
    ListView lvProducts;
    public PackageDtlFragment() {
        // Required empty public constructor
    }


    public static PackageDtlFragment newInstance(String param1, String param2) {
        // Create a new instance of the PackageDtlFragment
        PackageDtlFragment fragment = new PackageDtlFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Initialize the RequestQueue for making network requests using Volley
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_package_dtl, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Initialize TextViews to display package details
        txtPrice = getActivity().findViewById(R.id.txtPrice);
        txtPkgDescription = getActivity().findViewById(R.id.txtPkgDescription);
        txtStartDate = getActivity().findViewById(R.id.txtStartDate);
        txtEndDate = getActivity().findViewById(R.id.txtEndDate);
    }

    public void displayPackageDtl(Package item) {
        // Update TextViews with package details
        txtPrice.setText(item.getPkgBasePrice());
        txtPkgDescription.setText(item.getPkgDesc());
        txtStartDate.setText(item.getPkgStartDate());
        txtEndDate.setText(item.getPkgEndDate());
        // Fetch and display product details for the selected package
        getAllProducts(item.getPackageId());
    }

    private void getAllProducts(int packageId) {
        lvProducts = getActivity().findViewById(R.id.lvProducts);
        // Make a network request to fetch package data
        String url =getString(R.string.hostname)+"/api/package/getallproducts/"+packageId;
        JsonArrayRequest pkgProductRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        // Create a list to store product names and suppliers
                        List<String> packageList = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject pkgProductData = response.getJSONObject(i);
                            int j = i+1; // to display serial no for the product
                             String packageName = j +". "+pkgProductData.getString("Product")+" by "+pkgProductData.getString("Supplier");
                            packageList.add(packageName);
                            System.out.println(packageName);
                        }
                        // Create an ArrayAdapter to display product names and suppliers
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, packageList);
                        lvProducts.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Handle the error
                    Toast.makeText(
                            getActivity(),
                            "Error Making Request",
                            Toast.LENGTH_SHORT
                    ).show();
                }
        );
        // Add the product request to the request queue
        requestQueue.add(pkgProductRequest);
    }
}