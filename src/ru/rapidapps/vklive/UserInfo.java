package ru.rapidapps.vklive;

import java.io.InputStream;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.perm.kate.api.Api;
import com.perm.kate.api.City;
import com.perm.kate.api.User;

public class UserInfo extends Fragment implements OnClickListener
{
	private final Handler handler = new Handler();
	final int DIALOG=1;
	private Context context;
	private Long uid;

	public User user;
	public City city;
	public ArrayList<Drawable> photos;

	public Drawable draw;
	
	public Fragment fragment;
	private RelativeLayout lView;
	Dialog dialog;
		//Апи
	public static Account account = new Account();
	public static Api api;
		
	public UserInfo() {}

	public UserInfo(Long id) 
	{
		uid = id;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View rootView = inflater.inflate(R.layout.user_info, container, false);
		context = ((MainActivity) getActivity()).getApplicationContext();		
		account = MainActivity.account;
		api = MainActivity.api;

		ShowUserInfo(uid == null ? account.user_id : uid); // Показ страницы
		lView =(RelativeLayout) inflater.inflate(R.layout.status_dialog, null);
		
		return rootView;
	}

	void ShowUserInfo(final long id) 
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
			setTexts();
		}
	};
	
	
	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
			case R.id.edit_status:
				((Activity) context).showDialog(DIALOG);
				break;
			
				
		}
		
	}
	
	
	private void setTexts()
	{
		String city_name,age;
		if (user != null) 
		{
			if(user.faculty_name!=null)
			{
				((TextView) getView().findViewById(R.id.faculty_tb)).setText("Факультет:\n"+user.faculty_name);
			}
			else
			{
				((TextView) getView().findViewById(R.id.faculty_tb)).setVisibility(View.GONE);
			}
			if(user.university_name!=null)
			{
				((TextView) getView().findViewById(R.id.university_tb)).setText("Университет:\n"+user.university_name);
			}
			else
			{
				((TextView) getView().findViewById(R.id.university_tb)).setVisibility(View.GONE);
			}
			if(user.mobile_phone!=null)
			{
				((TextView) getView().findViewById(R.id.telephone_tb)).setText("Мобильный телефон:\n"+user.mobile_phone);
			}
			else
			{
				((TextView) getView().findViewById(R.id.telephone_tb)).setVisibility(View.GONE);
			}
			if (city != null)
			{
				city_name=city.name;
				((TextView) getView().findViewById(R.id.city_tb)).setText("Город:\n"+city_name);
			}
			else
			{					
				city_name="";
				((TextView) getView().findViewById(R.id.city_tb)).setVisibility(View.GONE);
			}
			if (user.birthdate != null)
			{
				age=getAgeFromBDay(user.birthdate);
			}
			else
			{				
				age="";
				((TextView) getView().findViewById(R.id.bday_tb)).setVisibility(View.GONE);
			}
			((TextView) getView().findViewById(R.id.age_city)).setText(age + ", " + city_name);
			((TextView) getView().findViewById(R.id.user_name)).setText(user.first_name + " " + user.last_name);
			((TextView) getView().findViewById(R.id.online_status)).setText(user.online ? "Online" : "Offline");
			
			
			
			try 
			{					
				((ImageView) getView().findViewById(R.id.online_mobile)).setVisibility(user.online_mobile ? View.VISIBLE : View.GONE);
				((ImageView) getView().findViewById(R.id.profile_pic)).setImageDrawable(draw);
				((TextView) getView().findViewById(R.id.bday_tb)).setText("День рождения:\n"+user.birthdate+"\n"+new SimpleDateFormat("dd MMMM yyyy",myDateFormatSymbols).format(new SimpleDateFormat("dd.MM.yyyy").parse(user.birthdate)));
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
		
	protected Dialog onCreateDialog(int id) 
	{
		if(id==DIALOG)
		{
		    AlertDialog.Builder adb = new AlertDialog.Builder(context);
		    adb.setTitle("Изменить статус");	    
		    adb.setView(lView);	
		    adb.setPositiveButton("Изменить статус", null);
		    dialog=adb.create();
		    dialog.setOnDismissListener(new OnDismissListener()
		    {
				@Override
				public void onDismiss(DialogInterface dialog)
				{
					EditText text=(EditText)getView().findViewById(R.id.text_status);
				    ((TextView) getView().findViewById(R.id.edit_status)).setText(text.getText().toString());			
				}
		    }); 
		    return dialog;
		}   
	    return onCreateDialog(id);
	  }
	
	private static DateFormatSymbols myDateFormatSymbols = new DateFormatSymbols()
	{
        /**
		 * 
		 */
		private static final long serialVersionUID = 4027506977854030842L;

		@Override
        public String[] getMonths()
        {
            return new String[]{"января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря"};
        }
        
    };
	
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
	
	public Drawable grabImageFromUrl(String url) 
	{
		try 
		{
			return Drawable.createFromStream((InputStream) new URL(url).getContent(), "src");
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}

	
}
