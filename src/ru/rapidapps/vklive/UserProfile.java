package ru.rapidapps.vklive;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.perm.kate.api.Api;
import com.perm.kate.api.City;
import com.perm.kate.api.User;
 
public class UserProfile extends Fragment 
{
	private Activity act;
	private final Handler handler=new Handler();
	
	// Массив пользователей
    public ArrayList<User> user;    
    
    // Массив городов
    public ArrayList<City> city;
    
    public Drawable draw;
    
    public static Account account = new Account();
    public static Api api;
	
	TextView tv1;
	ImageView imv1;
     
    public UserProfile()
    {
    }
     
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.user_profile, container, false);
        setupUI(); 
        
    	// Восстановление сохранённой сессии
        account=MainActivity.account;        
        api = MainActivity.api;
        
        ShowUserProfile(MainActivity.account.user_id); // Показ Моей страницы
        
        return rootView; 
    }
    
    
    void ShowUserProfile(final long user_id) 
	{
        new Thread()
        {
            @Override
            public void run()
            {
            	Collection cid=new ArrayList();
        		Collection uid = new ArrayList();
        		try 
        		{
                	uid.add(user_id);
            		user = api.getProfiles(uid, null, "sex, bdate, city, country, photo_50, photo_100, photo_200_orig, photo_200, photo_400_orig, photo_max, photo_max_orig, online, online_mobile, domain, has_mobile, contacts, connections, site, education, universities, schools, can_post, can_see_all_posts, can_see_audio, can_write_private_message, status, last_seen, common_count, relation, relatives, counters, screen_name, maiden_name, timezone, occupation, activities, interests, music, movies, tv, books, games, about, quotes", null, null, null);
                    draw = grabImageFromUrl(user.get(0).photo_200);
                    cid.add(user.get(0).city);
					city = api.getCities(cid);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
        		runOnUiThread(successRunnable);                
            }

			private void runOnUiThread(Runnable successRunnable)
			{
				handler.post(successRunnable);				
			}
        }.start();
    }
	
	Runnable successRunnable = new Runnable()
	{
        @Override
        public void run()
        {        	  	
        	if (user != null) 
        	{
        		if(city != null)
        		{
        			((TextView) getView().findViewById(R.id.age_city)).setText(getAgeFromBDay(user.get(0).birthdate)+", "+city.get(0).name);
        		}
        		else
        		{
        			Toast.makeText(act.getApplicationContext(), "Город не найден!", Toast.LENGTH_SHORT).show();
        		}      
        		
        	    ((TextView) getView().findViewById(R.id.user_name)).setText(user.get(0).first_name + " " + user.get(0).last_name);
        	    ((TextView) getView().findViewById(R.id.online_status)).setText(user.get(0).online?"Online":"Offline");
        	    try 
        	    {
        	    	((ImageView) getView().findViewById(R.id.online_mobile)).setVisibility(user.get(0).online_mobile?View.VISIBLE:View.GONE);
        			((ImageView) getView().findViewById(R.id.profile_pic)).setImageDrawable(draw);
        		} catch (Exception e) 
        		{
        			e.printStackTrace();
        		}
        	}
    		else 
    		{
    			Toast.makeText(act.getApplicationContext(), "Пользователь не найден!", Toast.LENGTH_SHORT).show();
    		}
        }
    };
    
	//Считаем возраст по дате рождения
	public String getAgeFromBDay(String bday)
    {
		int age;
		String add = "лет";
		Calendar cal=Calendar.getInstance();
    	String[] date=bday.split("\\.");
    	int[] dt=new int[date.length];
    	for(int i=0;i<date.length;i++)
    	{
    		dt[i]=Integer.parseInt(date[i]);
    	}
    	age=cal.get(Calendar.YEAR)-dt[2];
    	if(dt[1]>cal.get(Calendar.MONTH))
    	{
    		age--;
    	}
    	else if(dt[1]==cal.get(Calendar.MONTH))
    	{
    		if(dt[0]>cal.get(Calendar.DAY_OF_MONTH))
    		{
    			age--;
    		}
    	}
    	
    	int tmp=age%10;
    	if(tmp==0 || tmp==5 || tmp==6 || tmp==7 || tmp==8 || tmp==9)
    		add="лет";
    	else if(tmp==2 || tmp==3 || tmp==4)
    		add="года";
    	else if(tmp==1)
    		add="год"; 	

    	return String.valueOf(age)+" "+add;
    }
	
    
    // Получение картинки из Интернета
    public Drawable grabImageFromUrl(String url)  {
		try {
			return Drawable.createFromStream((InputStream)new URL(url).getContent(), "src");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    
    
	// Объвление компонентов
	private void setupUI() {
	
	} 
}