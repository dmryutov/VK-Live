package ru.rapidapps.vklive;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import ru.rapidapps.vklive.adapter.ContactsArrayAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.perm.kate.api.Api;
import com.perm.kate.api.User;
 
public class Friends extends Fragment {
	
	private final Handler handler = new Handler();
	private Activity context;
	private Long uid;
	
		// Массив пользователей
    public ArrayList<User> friends;
    public ArrayList<String> names = new ArrayList<String>();
	
		// Апи
	public static Account account = new Account();
	public static Api api;
	
	public ArrayList<Drawable> image_list = new ArrayList<Drawable>();

    public Drawable draw;
    Fragment fragment;
    ListView list;
    ContactsArrayAdapter adapter;
     
    public Friends(){}
    
    public Friends(Long id)
    {
    	uid=id;
    }
     
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.friends, container, false);
        context = ((MainActivity) getActivity());
		setupUI();

		account = MainActivity.account;
		api = MainActivity.api;	
		list=(ListView) rootView.findViewById((R.id.listView1));
		
        
        ShowFriendList(uid == null ? account.user_id : uid); // Показ списка друзей
        
        return rootView; 
    }
    
	void ShowFriendList(final long id) {
		new Thread(){
            @Override
            public void run(){
                try {
            		/*user = api.getFriends(account.user_id, "photo_50, first_name, last_name", 0, 5, "hints", null, null);
            		for (int i = 0; i < user.size(); i++) {
                		names.add(user.get(i).first_name + " " + user.get(i).last_name);
                		image_list.add(grabImageFromUrl(user.get(i).photo));
                	} */ 
            		friends = api.getFriends(id, "photo_50, first_name, last_name, online", 0, 0, "name", null, null);
            		/*names.add("");
            		image_list.add(grabImageFromUrl(user.get(0).photo));
            		for (int i = 0; i < user.size(); i++) {
                		names.add(user.get(i).first_name + " " + user.get(i).last_name);
                		image_list.add(grabImageFromUrl(user.get(i).photo));
                	}*/

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

        	
        	if (friends != null) {
        		adapter = new ContactsArrayAdapter(context, friends);
        		list.setAdapter(adapter);
    			list.setOnItemClickListener(new OnItemClickListener() {
    				@Override
    				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    					User us = (User) list.getItemAtPosition(position);
    					
    					fragment=new UserProfile(us.uid);
    					FragmentManager fragmentManager = getFragmentManager();
    					fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
    					
    					/*
    					helper.WriteDebug("ОТКРЫВАЕМ Контакт " + us.uid);
    		
    					Intent intent = new Intent(ContactsActivity.this, ConversationActivity.class);
    					intent.putExtra("uid", us.uid);
    					intent.putExtra("name", us.first_name + " " + us.last_name);
    					startActivity(intent);	
    					*/
    				}
    			});
    			
    		
    			((EditText) getView().findViewById(R.id.contactsSearch)).addTextChangedListener(new TextWatcher() {
    				@Override
    				public void onTextChanged(CharSequence s, int start, int before, int count) {
    					helper.WriteInfo("Ищу: " + s.toString());
    					adapter.getFilter().filter(s.toString());
    				}

    				@Override
    				public void afterTextChanged(Editable s) {
    					// TODO Auto-generated method stub
    					
    				}

    				@Override
    				public void beforeTextChanged(CharSequence s, int start,
    						int count, int after) {
    					// TODO Auto-generated method stub
    					
    				}
    			});	
    		}
        	
        	
        	
        	/*FriendListItem adapter = new FriendListItem(context, names, image_list);
        	
        	((ListView) getView().findViewById(R.id.listView1)).setAdapter(adapter);
        	((ListView) getView().findViewById(R.id.listView1)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	    @Override
        	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        	    	// OnClick
        	    }
        	});*/
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