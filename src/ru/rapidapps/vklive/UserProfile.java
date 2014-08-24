package ru.rapidapps.vklive;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.perm.kate.api.Api;
import com.perm.kate.api.City;
import com.perm.kate.api.Photo;
import com.perm.kate.api.User;

public class UserProfile extends Fragment implements OnClickListener
{
	
	private final Handler handler = new Handler();
	private Context context;
	private Long uid;

	public User user;
	public City city;
	public ArrayList<Drawable> photos;

	public Drawable draw;
	
	public Fragment fragment;
	
		//Апи
	public static Account account = new Account();
	public static Api api;
		
	public UserProfile() {}

	public UserProfile(Long id) 
	{
		uid = id;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View rootView = inflater.inflate(R.layout.user_profile, container, false);
		context = ((MainActivity) getActivity()).getApplicationContext();		
		account = MainActivity.account;
		api = MainActivity.api;
	
		rootView.findViewById(R.id.friends_but).setOnClickListener(this);
		rootView.findViewById(R.id.info_but).setOnClickListener(this);
		rootView.findViewById(R.id.all_photos).setOnClickListener(this);
		rootView.findViewById(R.id.audios_but).setOnClickListener(this);

		ShowUserProfile(uid == null ? account.user_id : uid); // Показ страницы

		return rootView;
	}

	void ShowUserProfile(final long id) 
	{
		new Thread()
		{
			@Override
			public void run() 
			{
				Collection<Long> cid = new ArrayList<Long>();
				Collection<Long> uid = new ArrayList<Long>();
				try 
				{
					uid.add(id);
					user = api.getProfiles(uid, null, "sex, bdate, city, country, photo_50, photo_100, photo_200_orig, photo_200, photo_400_orig, photo_max, photo_max_orig, online, online_mobile, domain, has_mobile, contacts, connections, site, education, universities, schools, can_post, can_see_all_posts, can_see_audio, can_write_private_message, status, last_seen, common_count, relation, relatives, counters, screen_name, maiden_name, timezone, occupation, activities, interests, music, movies, tv, books, games, about, quotes, common_count, followers_count, counters", null, null, null).get(0);
					draw = grabImageFromUrl(user.photo_200);
					cid.add((long)user.city);
					city = api.getCities(cid).get(0);
					//photos=grabImagePreviewsFromUrl(api.getAllPhotos(user.uid, 0, null, false));					
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
			//setPhotosBar();			
			setTextsAndVis();
		}
	};	

	//Обработчик нажатий на кнопки скроллбара
	@Override
	public void onClick(View v)
	{
	    switch(v.getId())
	    {
	    	case R.id.friends_but:
	    		fragment=new Friends(user.uid);	    	      	
	    		break;	    	
	    	case R.id.info_but:
	    		fragment=new UserInfo(user.uid);
	    		break;	  
	    	case R.id.audios_but:
	    		fragment=new Audios(user.uid);
	    		break;
	    	default:
	    		return;	    		
	    }	    
	    FragmentTransaction transaction = getFragmentManager().beginTransaction();
	    transaction.replace(R.id.frame_container, fragment);
	    transaction.addToBackStack(null);
	    transaction.setTransition(0);
	    transaction.commit();
	}	
	  
	//Скроллбар фоток
	private void setPhotosBar()
	{
		LinearLayout lv = (LinearLayout) getView().findViewById (R.id.preview_layout);
		LinearLayout.LayoutParams lp;		
		ImageView iv;		
		
		int height=lv.getLayoutParams().height;
			   
		for (int i=0 ; i<photos.size(); i++)
		{
			iv = new ImageView (context);
			lp=new LinearLayout.LayoutParams(photos.get(i).getIntrinsicWidth()*height/photos.get(i).getIntrinsicHeight(),height);
			lp.setMargins(5, 0, 5, 0);			
			iv.setBackgroundDrawable(photos.get(i));
			lv.addView(iv);
			iv.setLayoutParams(lp);
		}
	}
	
	//Кол-во в полях + проверки на видимость
	private void setTextsAndVis()
	{
		String age,city_name;
		if (user != null) 
		{
			if (city != null)
			{
				city_name=city.name;
			}
			else
			{				
				city_name="Город скрыт";
			}
			if (getAgeFromBDay(user.birthdate) != null)
			{
				age=getAgeFromBDay(user.birthdate);
			}
			else
			{
				age="Возраст скрыт";
			}
			if(user.friends_count!=0)
			{
				((Button) getView().findViewById(R.id.friends_but)).setText(setFriendsText());
				((Button) getView().findViewById(R.id.friends_but)).setVisibility(View.VISIBLE);
			}
			else
			{
				((Button) getView().findViewById(R.id.friends_but)).setVisibility(View.GONE);
			}	
			if(user.mutual_friends_count!=0 && user.uid!=account.user_id)
			{
				((Button) getView().findViewById(R.id.mutual_friends_but)).setText(setMutualFriendsText());
				((Button) getView().findViewById(R.id.mutual_friends_but)).setVisibility(View.VISIBLE);
			}
			else
			{
				((Button) getView().findViewById(R.id.mutual_friends_but)).setVisibility(View.GONE);
			}			
			if(user.followers_count!=0)
			{
				((Button) getView().findViewById(R.id.followers_but)).setText(setFollowersText());
				((Button) getView().findViewById(R.id.followers_but)).setVisibility(View.VISIBLE);
			}
			else
			{
				((Button) getView().findViewById(R.id.followers_but)).setVisibility(View.GONE);
			}	
			if(user.groups_count!=0)
			{
				((Button) getView().findViewById(R.id.groups_but)).setText(setGroupsText());
				((Button) getView().findViewById(R.id.groups_but)).setVisibility(View.VISIBLE);
			}
			else
			{
				((Button) getView().findViewById(R.id.groups_but)).setVisibility(View.GONE);
			}
			if(user.photos_count!=0)
			{
				((TextView)getView().findViewById(R.id.all_photo_count)).setText(String.valueOf(user.photos_count)+" фото");
				((Button) getView().findViewById(R.id.photos_but)).setText(setPhotosText());
				((Button) getView().findViewById(R.id.photos_but)).setVisibility(View.VISIBLE);
			}
			else
			{
				((Button) getView().findViewById(R.id.photos_but)).setVisibility(View.GONE);
				getView().findViewById(R.id.all_photos).setVisibility(View.GONE);
				getView().findViewById(R.id.photo_preview).setVisibility(View.GONE);
				
			}	
			if(user.videos_count!=0)
			{
				((Button) getView().findViewById(R.id.videos_but)).setText(setVideosText());
				((Button) getView().findViewById(R.id.videos_but)).setVisibility(View.VISIBLE);
			}
			else
			{
				((Button) getView().findViewById(R.id.videos_but)).setVisibility(View.GONE);
			}	
			if(user.videos_count!=0)
			{
				((Button) getView().findViewById(R.id.audios_but)).setText(setAudiosText());
				((Button) getView().findViewById(R.id.audios_but)).setVisibility(View.VISIBLE);
			}
			else
			{
				((Button) getView().findViewById(R.id.audios_but)).setVisibility(View.GONE);
			}
			
			((TextView) getView().findViewById(R.id.age_city)).setText(age + ", " + city_name);
			((TextView) getView().findViewById(R.id.user_name)).setText(user.first_name + " " + user.last_name);
			((TextView) getView().findViewById(R.id.online_status)).setText(user.online ? "Online" : "Offline");
			
			try 
			{					
				((ImageView) getView().findViewById(R.id.online_mobile)).setVisibility(user.online_mobile ? View.VISIBLE : View.GONE);
				((ImageView) getView().findViewById(R.id.profile_pic)).setImageDrawable(draw);
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
		} 
		else
		{
			Toast.makeText(context, "Пользователь не найден", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	/***************  BEGIN Функции для подсчета кол-ва чего-нибудь BEGIN *********************/
	
	public SpannableString setAudiosText()
	{
		String add="аудио";		
		
		SpannableString res=new SpannableString(String.valueOf(user.audios_count)+"\n"+add);
		res.setSpan(new RelativeSizeSpan(2f), 0, String.valueOf(user.audios_count).length(),0);
		
		return res;	
	}
	
	public SpannableString setVideosText()
	{
		String add="видео";		
		
		SpannableString res=new SpannableString(String.valueOf(user.videos_count)+"\n"+add);
		res.setSpan(new RelativeSizeSpan(2f), 0, String.valueOf(user.videos_count).length(),0);
		
		return res;	
	}
	
	public SpannableString setPhotosText()
	{
		String add="фото";	
		
		SpannableString res=new SpannableString(String.valueOf(user.photos_count)+"\n"+add);
		res.setSpan(new RelativeSizeSpan(2f), 0, String.valueOf(user.photos_count).length(),0);
		
		return res;	
	}

	public SpannableString setGroupsText()
	{
		String add="групп";
		
		int tmp = user.followers_count % 10;

		if (tmp == 2 || tmp == 3 || tmp == 4)
			add = "группы";
		else if (tmp == 1)
			add = "группа";	
		else
			add = "групп";
		
		SpannableString res=new SpannableString(String.valueOf(user.groups_count)+"\n"+add);
		res.setSpan(new RelativeSizeSpan(2f), 0, String.valueOf(user.groups_count).length(),0);
		
		return res;	
	}
	
	public SpannableString setFollowersText()
	{
		String add="подписчиков";
		
		int tmp = user.followers_count % 10;
					
		if (tmp == 2 || tmp == 3 || tmp == 4)
			add = "подписчика";
		else if (tmp == 1)
			add = "подписчик";	
		else
			add = "подписчиков";
		
		SpannableString res=new SpannableString(String.valueOf(user.followers_count)+"\n"+add);
		res.setSpan(new RelativeSizeSpan(2f), 0, String.valueOf(user.followers_count).length(),0);
		
		return res;	
	}
	
	public SpannableString setMutualFriendsText()
	{
		
		String add="общих";
		SpannableString res = null;
		
		
		if (user.mutual_friends_count % 10 == 1)
			add = "общий";
		else
			add="общих";	
		
		res=new SpannableString(String.valueOf(user.mutual_friends_count)+"\n"+add);
		res.setSpan(new RelativeSizeSpan(2f), 0, String.valueOf(user.mutual_friends_count).length(),0);
		return res;
	}
	
	public SpannableString setFriendsText()
	{
		String add="друзей";
		
		int tmp = user.friends_count % 10;
		
		if (tmp == 2 || tmp == 3 || tmp == 4)
			add = "друга";
		else if (tmp == 1)
			add = "друг";	
		else
			add = "друзей";
		
		SpannableString res=new SpannableString(String.valueOf(user.friends_count)+"\n"+add);
		res.setSpan(new RelativeSizeSpan(2f), 0, String.valueOf(user.friends_count).length(),0);
		
		return res;	
	}	
		// Считаем возраст по дате рождения
	public String getAgeFromBDay(String bday) 
	{		
		int age;
		String add = "лет";
		Calendar cal = Calendar.getInstance();
		if(bday==null)
			return null;
		String[] date = bday.split("\\.");
		int[] dt = new int[date.length];
		for (int i = 0; i < date.length; i++) 
		{
			dt[i] = Integer.parseInt(date[i]);
		}
		if(dt.length==3)
		{
			age = cal.get(Calendar.YEAR) - dt[2];
			if (dt[1] > cal.get(Calendar.MONTH)) 
			{
				age--;
			} else if (dt[1] == cal.get(Calendar.MONTH)) 
			{
				if (dt[0] > cal.get(Calendar.DAY_OF_MONTH)) 
				{
					age--;
				}
			}
	
			int tmp = age % 10;
			if (tmp == 0 || tmp == 5 || tmp == 6 || tmp == 7 || tmp == 8 || tmp == 9)
				add = "лет";
			else if (tmp == 2 || tmp == 3 || tmp == 4)
				add = "года";
			else if (tmp == 1)
				add = "год";
	
			return String.valueOf(age) + " " + add;
		}
		else
			return null;
	}

	
	/*************** END Функции для подсчета кол-ва чего-нибудь END *********************/
	
		// Получение картинки из Интернета
	public Drawable grabImageFromUrl(String url) 
	{
		try
		{
			return Drawable.createFromStream((InputStream) new URL(url).getContent(),"src");
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	ArrayList<Drawable> grabImagePreviewsFromUrl(ArrayList<Photo> ph) 
	{
		ArrayList<Drawable> draw=new ArrayList<Drawable>();
		for(int i=0;i<ph.size();i++)
		{
			try
			{
				draw.add(Drawable.createFromStream((InputStream) new URL(ph.get(i).src_big).getContent(), "src"));
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		return draw;
	}
}