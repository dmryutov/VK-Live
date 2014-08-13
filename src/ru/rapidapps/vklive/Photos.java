package ru.rapidapps.vklive;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
 
public class Photos extends Fragment {
	
	TextView tv1;
	ImageView imv1;
     
    public Photos(){}
     
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.photos, container, false);
        setupUI();
        
        // ACTIONS
        
        return rootView; 
    }
    
	// Объвление компонентов
	private void setupUI() {
  
	} 
}