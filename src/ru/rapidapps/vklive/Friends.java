package ru.rapidapps.vklive;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
 
public class Friends extends Fragment {
	
	TextView tv1;
	ImageView imv1;
     
    public Friends(){}
     
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.friends, container, false);
        setupUI();
        
        ((MainActivity)getActivity()).ShowFriendList(); // Показ Моей страницы
        
        return rootView; 
    }
    
	// Объвление компонентов
	private void setupUI() {

	} 
}