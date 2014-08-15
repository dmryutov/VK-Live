package ru.rapidapps.vklive;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import android.app.Fragment;
import android.app.FragmentManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.perm.kate.api.Api;
import com.perm.kate.api.City;
import com.perm.kate.api.User;

public class UserProfile extends Fragment implements OnClickListener
{
	
	private final Handler handler = new Handler();
	private Context context;
	private Long uid;

	public User user;
	public City city;

	public Drawable draw;
	
	public Fragment fragment;

		//���
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
	   
	        
	    	
		
		ShowUserProfile(uid == null ? account.user_id : uid); // ����� ��������

		return rootView;
	}

	void ShowUserProfile(final long id) 
	{
		new Thread()
		{
			@Override
			public void run() 
			{
				Collection cid = new ArrayList();
				Collection uid = new ArrayList();
				try 
				{
					uid.add(id);
					user = api.getProfiles(uid, null, "sex, bdate, city, country, photo_50, photo_100, photo_200_orig, photo_200, photo_400_orig, photo_max, photo_max_orig, online, online_mobile, domain, has_mobile, contacts, connections, site, education, universities, schools, can_post, can_see_all_posts, can_see_audio, can_write_private_message, status, last_seen, common_count, relation, relatives, counters, screen_name, maiden_name, timezone, occupation, activities, interests, music, movies, tv, books, games, about, quotes, common_count, followers_count, counters", null, null, null).get(0);
					draw = grabImageFromUrl(user.photo_200);
					cid.add(user.city);
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
			if (user != null) 
			{
				if (city != null)
				{
					if(getAgeFromBDay(user.birthdate)!=null)
					{
						((TextView) getView().findViewById(R.id.age_city)).setText(getAgeFromBDay(user.birthdate) + ", " + city.name);
					}
					else
					{
						((TextView) getView().findViewById(R.id.age_city)).setText("������� �����" + ", " + city.name);
					}				
				} 
				else 
				{
					if(getAgeFromBDay(user.birthdate) != null)
					{
						((TextView) getView().findViewById(R.id.age_city)).setText(getAgeFromBDay(user.birthdate) + ", " + "����� �����");
					}
					else
					{
						((TextView) getView().findViewById(R.id.age_city)).setText("������� � ����� ������");
					}
				}			
				
				((Button) getView().findViewById(R.id.mutual_friends_but)).setVisibility(user.uid==account.user_id ? View.GONE : View.VISIBLE);
								
				((TextView) getView().findViewById(R.id.user_name)).setText(user.first_name + " " + user.last_name);
				((TextView) getView().findViewById(R.id.online_status)).setText(user.online ? "Online" : "Offline");
				((Button) getView().findViewById(R.id.friends_but)).setText(setFriendsText());
				
				((Button) getView().findViewById(R.id.mutual_friends_but)).setText(setMutualFriendsText());
				
				((Button) getView().findViewById(R.id.followers_but)).setText(setFollowersText());
				((Button) getView().findViewById(R.id.groups_but)).setText(setGroupsText());
				((Button) getView().findViewById(R.id.photos_but)).setText(setPhotosText());
				((Button) getView().findViewById(R.id.videos_but)).setText(setVideosText());
				((Button) getView().findViewById(R.id.audios_but)).setText(setAudiosText());
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
				Toast.makeText(context, "������������ �� ������", Toast.LENGTH_SHORT).show();
			}
		}
	};
	

	//���������� ������� �� ������ ����������
	@Override
	public void onClick(View v)
	{
	    switch(v.getId())
	    {
	    	case R.id.friends_but:
	    		fragment=new Friends(user.uid);	    	      	
	    		break;
	    }
	    FragmentManager fragmentManager = getFragmentManager();
	    fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
	}	
	   
	
	/***************  BEGIN ������� ��� �������� ���-�� ����-������ BEGIN *********************/
	
	public SpannableString setAudiosText()
	{
		String add="�����";		
		
		SpannableString res=new SpannableString(String.valueOf(user.audios_count)+"\n"+add);
		res.setSpan(new RelativeSizeSpan(2f), 0, String.valueOf(user.audios_count).length(),0);
		
		return res;	
	}
	
	public SpannableString setVideosText()
	{
		String add="�����";		
		
		SpannableString res=new SpannableString(String.valueOf(user.videos_count)+"\n"+add);
		res.setSpan(new RelativeSizeSpan(2f), 0, String.valueOf(user.videos_count).length(),0);
		
		return res;	
	}
	
	public SpannableString setPhotosText()
	{
		String add="����";	
		
		SpannableString res=new SpannableString(String.valueOf(user.user_photos_count)+"\n"+add);
		res.setSpan(new RelativeSizeSpan(2f), 0, String.valueOf(user.user_photos_count).length(),0);
		
		return res;	
	}

	public SpannableString setGroupsText()
	{
		String add="�����";
		
		int tmp = user.followers_count % 10;

		if (tmp == 2 || tmp == 3 || tmp == 4)
			add = "������";
		else if (tmp == 1)
			add = "������";	
		else
			add = "�����";
		
		SpannableString res=new SpannableString(String.valueOf(user.groups_count)+"\n"+add);
		res.setSpan(new RelativeSizeSpan(2f), 0, String.valueOf(user.groups_count).length(),0);
		
		return res;	
	}
	
	public SpannableString setFollowersText()
	{
		String add="�����������";
		
		int tmp = user.followers_count % 10;
					
		if (tmp == 2 || tmp == 3 || tmp == 4)
			add = "����������";
		else if (tmp == 1)
			add = "���������";	
		else
			add = "�����������";
		
		SpannableString res=new SpannableString(String.valueOf(user.followers_count)+"\n"+add);
		res.setSpan(new RelativeSizeSpan(2f), 0, String.valueOf(user.followers_count).length(),0);
		
		return res;	
	}
	
	public SpannableString setMutualFriendsText()
	{
		
		String add="�����";
		SpannableString res = null;
		
		
		if (user.mutual_friends_count % 10 == 1)
			add = "�����";
		else
			add="�����";	
		
		res=new SpannableString(String.valueOf(user.mutual_friends_count)+"\n"+add);
		res.setSpan(new RelativeSizeSpan(2f), 0, String.valueOf(user.mutual_friends_count).length(),0);
		return res;
	}
	
	public SpannableString setFriendsText()
	{
		String add="������";
		
		int tmp = user.friends_count % 10;
		
		if (tmp == 2 || tmp == 3 || tmp == 4)
			add = "�����";
		else if (tmp == 1)
			add = "����";	
		else
			add = "������";
		
		SpannableString res=new SpannableString(String.valueOf(user.friends_count)+"\n"+add);
		res.setSpan(new RelativeSizeSpan(2f), 0, String.valueOf(user.friends_count).length(),0);
		
		return res;	
	}	
		// ������� ������� �� ���� ��������
	public String getAgeFromBDay(String bday) 
	{		
		int age;
		String add = "���";
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
				add = "���";
			else if (tmp == 2 || tmp == 3 || tmp == 4)
				add = "����";
			else if (tmp == 1)
				add = "���";
	
			return String.valueOf(age) + " " + add;
		}
		else
			return null;
	}

	
	/*************** END ������� ��� �������� ���-�� ����-������ END *********************/
	
		// ��������� �������� �� ���������
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