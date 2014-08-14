package ru.rapidapps.vklive;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import ru.rapidapps.vklive.adapter.NavDrawerListAdapter;
import ru.rapidapps.vklive.model.NavDrawerItem;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.perm.kate.api.Api;
import com.perm.kate.api.City;
import com.perm.kate.api.User;


public class MainActivity extends Activity {

	
	
		//������� ����
	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle, mTitle;
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    
    	// ���
    public static Account account = new Account();
    public static Api api;
	
    	// ������ �������������
    public ArrayList<User> user;
    
    
    	// ������ �������
    public ArrayList<City> city;
    
    public Fragment fragment;
    public Drawable draw;
    
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
			// ������� ����
		mTitle = mDrawerTitle = getTitle();
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load slide menu items
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons); // nav drawer icons from resources
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        
        navDrawerItems = new ArrayList<NavDrawerItem>(); 
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1))); // ��� ��������
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1))); // �������
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1))); // ������
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "+2")); // ���������
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1))); // ������
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1))); // ������
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1))); // ����������
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons.getResourceId(7, -1))); // �����������
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[8], navMenuIcons.getResourceId(8, -1))); // �����������
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[9], navMenuIcons.getResourceId(9, -1))); // ��������
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[10], navMenuIcons.getResourceId(10, -1))); // �����
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[11], navMenuIcons.getResourceId(11, -1))); // ���������    

        navMenuIcons.recycle(); // Recycle the typed array
        adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems); // setting the nav drawer list adapter
        mDrawerList.setAdapter(adapter);
        getActionBar().setDisplayHomeAsUpEnabled(true); // enabling action bar app icon and behaving it as toggle button
        getActionBar().setHomeButtonEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.app_name,  R.string.app_name) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // calling onPrepareOptionsMenu() to show action bar icons
            }
 
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // calling onPrepareOptionsMenu() to hide action bar icons
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        if (savedInstanceState == null) {
            displayView(0); // ����� �������� (��� ��������) ��� �������
        }
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
		
		
			// �������������� ���������� ������
        account.restore(this); 	
        if(account.access_token == null) {
        	Intent intent = new Intent(this, LoginActivity.class);
    		startActivity(intent);
        }  
        else
        	api = new Api(account.access_token, getString(R.string.app_id));
	}
	
	/****************************** Begin ������� ���� ******************************/
	
    private class SlideMenuClickListener implements ListView.OnItemClickListener {
    	@Override
    	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    		displayView(position); // display view for selected nav drawer item
    	}
    }
    
    	// ���� �� ������ ����
	private void displayView(int position) {
        fragment = null;
        switch (position) {
        case 0:
            fragment = new MyPage();
            break;
        case 1:
            fragment = new News();
            break;
        case 2:
            fragment = new Feedback();
            break;
        case 3:
            fragment = new Messages();
            break;
        case 4:
            fragment = new Friends();
            break;
        case 5:
            fragment = new Communities();
            break;
        case 6:
            fragment = new Photos();
            break;
        case 7:
            fragment = new Videos();
            break;
        case 8:
            fragment = new Music();
            break;
        case 9:
            fragment = new Bookmarks();
            break;
        case 10:
            fragment = new Search();
            break;
        case 11:
            fragment = new Settings();
            break;
        }
        
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
 
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) // toggle nav drawer on selecting action bar app icon/title
            return true;
        switch (item.getItemId()) { // Handle action bar actions click
        case R.id.action_settings:
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
 
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList); // if nav drawer is opened, hide the action items
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
 
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
 
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState(); // Sync the toggle state after onRestoreInstanceState has occurred
    }
 
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    
    /****************************** End ������� ���� ******************************/
    
    /****************************** Begin �������� ������������ ******************************/
	
    // ����� �������
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
        			((TextView) fragment.getView().findViewById(R.id.age_city)).setText(getAgeFromBDay(user.get(0).birthdate)+", "+city.get(0).name);
        		}
        		else
        		{
        			Toast.makeText(getApplicationContext(), "����� �� ������!", Toast.LENGTH_SHORT).show();
        		}      
        		
        	    ((TextView) fragment.getView().findViewById(R.id.user_name)).setText(user.get(0).first_name + " " + user.get(0).last_name);
        	    ((TextView) fragment.getView().findViewById(R.id.online_status)).setText(user.get(0).online?"Online":"Offline");
        	    try 
        	    {
        	    	((ImageView) fragment.getView().findViewById(R.id.online_mobile)).setVisibility(user.get(0).online_mobile?View.VISIBLE:View.GONE);
        			((ImageView) fragment.getView().findViewById(R.id.profile_pic)).setImageDrawable(draw);
        		} catch (Exception e) 
        		{
        			e.printStackTrace();
        		}
        	}
    		else 
    		{
    			Toast.makeText(getApplicationContext(), "������������ �� ������!", Toast.LENGTH_SHORT).show();
    		}
        }
    };
    
	//������� ������� �� ���� ��������
	public String getAgeFromBDay(String bday)
    {
		int age;
		String add = "���";
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
    		add="���";
    	else if(tmp==2 || tmp==3 || tmp==4)
    		add="����";
    	else if(tmp==1)
    		add="���"; 	

    	return String.valueOf(age)+" "+add;
    }
	
    
    // ��������� �������� �� ���������
    public Drawable grabImageFromUrl(String url)  {
		try {
			return Drawable.createFromStream((InputStream)new URL(url).getContent(), "src");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    
    
    /****************************** End �������� ������������ ******************************/
    
	/*void logOut() {
		api = null;
		account.access_token = null;
		account.user_id = 0;
		account.save(LoginActivity.LoginFormContext);
		layout1.setVisibility(LoginActivity.layout1.INVISIBLE);
		layout2.setVisibility(LoginActivity.layout2.VISIBLE);
		finish();
	}*/
}
