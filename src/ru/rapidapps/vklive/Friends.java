package ru.rapidapps.vklive;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import ru.rapidapps.vklive.model.FriendListItem;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.perm.kate.api.Api;
import com.perm.kate.api.User;
 
public class Friends extends Fragment {
	
	private final Handler handler = new Handler();
	private Activity context;
	
		// Массив пользователей
    public ArrayList<User> user;
    public ArrayList<String> names = new ArrayList<String>();
	
		// Апи
	public static Account account = new Account();
	public static Api api;
	
	public ArrayList<Drawable> image_list = new ArrayList<Drawable>();

    public Drawable draw;
	
	TextView tv1;
	ImageView imv1;
     
    public Friends(){}
     
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.friends, container, false);
        context = ((MainActivity) getActivity());
		setupUI();

		account = MainActivity.account;
		api = MainActivity.api;
        
        ShowFriendList(); // Показ списка друзей
        
        return rootView; 
    }
    
		// Показ профиля
	void ShowFriendList() {
		new Thread(){
            @Override
            public void run(){
                try {
            		user = api.getFriends(account.user_id, "photo_50, first_name, last_name", 0, 5, "hints", null, null);
            		for (int i = 0; i < user.size(); i++) {
                		names.add(user.get(i).first_name + " " + user.get(i).last_name);
                		image_list.add(grabImageFromUrl(user.get(i).photo));
                	}  
                    
            		user = api.getFriends(account.user_id, "photo_50, first_name, last_name", 0, 20, "name", null, null);
            		names.add("");
            		image_list.add(grabImageFromUrl(user.get(0).photo));
            		for (int i = 0; i < user.size(); i++) {
                		names.add(user.get(i).first_name + " " + user.get(i).last_name);
                		image_list.add(grabImageFromUrl(user.get(i).photo));
                	}

                    runOnUiThread(successRunnable);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            private void runOnUiThread(Runnable successRunnable) {
				handler.post(successRunnable);
			}
        }.start();
    }
	
	Runnable successRunnable = new Runnable(){
        @Override
        public void run() {

        	FriendListItem adapter = new FriendListItem(context, names, image_list);
        	
        	((ListView) getView().findViewById(R.id.listView1)).setAdapter(adapter);
        	((ListView) getView().findViewById(R.id.listView1)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	    @Override
        	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        	    	// OnClick
        	    }
        	});
        }
    };
    
		// Получение картинки из Интернета
    public Drawable grabImageFromUrl(String url) {
		try {
			return Drawable.createFromStream((InputStream) new URL(url).getContent(), "src");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    
	// Объвление компонентов
	private void setupUI() {

	} 
}