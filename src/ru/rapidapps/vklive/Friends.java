package ru.rapidapps.vklive;

import java.util.ArrayList;

import ru.rapidapps.vklive.adapter.ContactsArrayAdapter;
import ru.rapidapps.vklive.adapter.ContactsOnlineArrayAdapter;
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
import android.widget.TabHost;

import com.perm.kate.api.Api;
import com.perm.kate.api.User;
 
public class Friends extends Fragment {
	
	private final Handler handler = new Handler();
	private Activity context;
	private Long uid;
	public int favorite_user_count;
	
		// ћассив пользователей
    public ArrayList<User> friends, online_friends;
    public ArrayList<String> names = new ArrayList<String>();
	
		// јпи
	public static Account account = new Account();
	public static Api api;
	
	public ArrayList<Drawable> image_list = new ArrayList<Drawable>();

    public Drawable draw;
    Fragment fragment;
    ListView list_all, list_online;
    ContactsArrayAdapter adapter_all;
    ContactsOnlineArrayAdapter adapter_online;
    
    TabHost tabHost;
     
    public Friends(){}
    
    public Friends(Long id) {
    	uid = id;
    }
     
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.friend_list, container, false);
        context = ((MainActivity) getActivity());
        list_all = (ListView) rootView.findViewById((R.id.listView1));
		list_online = (ListView) rootView.findViewById((R.id.listView2));
	
		account = MainActivity.account;
		api = MainActivity.api;

			// ¬кладки
		tabHost = (TabHost) rootView.findViewById(android.R.id.tabhost);
        tabHost.setup(); // инициализаци€
        
        ShowFriendList(uid == null ? account.user_id : uid); // ѕоказ списка друзей
        
        return rootView; 
    }
    
	void ShowFriendList(final long id) {
		new Thread(){
            @Override
            public void run(){
                try {
                	ArrayList<User> us;
                	friends = new ArrayList<User>();
                	online_friends = new ArrayList<User>();
                	us = api.getFriends(id, "photo_50, first_name, last_name, online", 0, 5, "hints", null, null);
                	for (User u : us) {                    	
	                    	friends.add(u);                        
	                }
                	favorite_user_count = us.size();
	                us = api.getFriends(id, "photo_50, first_name, last_name, online", 0, 0, "name", null, null);
                	for (User u : us) {                    	
                    	friends.add(u);
                    	if (u.online)
	                    	online_friends.add(u);
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
        	TabHost.TabSpec tabSpec; // создаем вкладку и указываем тег
            tabSpec = tabHost.newTabSpec("tag1"); // название вкладки
            tabSpec.setIndicator(String.valueOf(friends.size()) +" друзей"); // указываем id компонента из FrameLayout, он и станет содержимым
            tabSpec.setContent(R.id.tab1); // добавл€ем в корневой элемент
            tabHost.addTab(tabSpec);
            
            tabSpec = tabHost.newTabSpec("tag2");
            tabSpec.setIndicator(String.valueOf(online_friends.size()) + " онлайн");
            tabSpec.setContent(R.id.tab2);        
            tabHost.addTab(tabSpec);
            
            tabHost.setCurrentTabByTag("tag1"); // втора€ вкладка будет выбрана по умолчанию
        	
        	if (friends != null) {
        		adapter_all = new ContactsArrayAdapter(context, friends, favorite_user_count);
        		list_all.setAdapter(adapter_all);
        		list_all.setFastScrollEnabled(true);
    			list_all.setOnItemClickListener(new OnItemClickListener() {
    				@Override
    				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    					User us = (User) list_all.getItemAtPosition(position);
    					fragment = new UserProfile(us.uid);
    					FragmentManager fragmentManager = getFragmentManager();
    					fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
    				}
    			});
    			
    			adapter_online = new ContactsOnlineArrayAdapter(context, online_friends);
    			list_online.setAdapter(adapter_online);
    			list_online.setOnItemClickListener(new OnItemClickListener() {
    				@Override
    				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    					User us = (User) list_online.getItemAtPosition(position);
    					fragment = new UserProfile(us.uid);
    					FragmentManager fragmentManager = getFragmentManager();
    					fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
    				}
    			});
    			
    			((EditText) getView().findViewById(R.id.contactsSearch)).addTextChangedListener(new TextWatcher() {
    				@Override
    				public void onTextChanged(CharSequence s, int start, int before, int count) {
    					adapter_all.getFilter().filter(s.toString());
    					adapter_online.getFilter().filter(s.toString());
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
        }
    };

}