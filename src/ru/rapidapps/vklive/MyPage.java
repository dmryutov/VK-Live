package ru.rapidapps.vklive;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
 
public class MyPage extends Fragment {
	
	TextView tv1;
	ImageView imv1;
     
    public MyPage(){}
     
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.user_profile, container, false);
        setupUI(); 
        
        ((MainActivity)getActivity()).ShowUserProfile(MainActivity.account.user_id); // Показ Моей страницы
        
        return rootView; 
    }
    
	// Объвление компонентов
	private void setupUI() {
	
	} 
}