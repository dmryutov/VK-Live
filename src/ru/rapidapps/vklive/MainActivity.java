package ru.rapidapps.vklive;

import java.util.ArrayList;

import ru.rapidapps.vklive.adapter.NavDrawerListAdapter;
import ru.rapidapps.vklive.model.NavDrawerItem;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.perm.kate.api.Api;


public class MainActivity extends Activity {

		//Боковое меню
	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle, mTitle;
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    
    	// Апи
    public static Account account = new Account();
    public static Api api;
	
    public Fragment fragment;
    
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
			// Боковое меню
		mTitle = mDrawerTitle = getTitle();
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load slide menu items
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons); // nav drawer icons from resources
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        
        navDrawerItems = new ArrayList<NavDrawerItem>(); 
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1))); // Моя страница
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1))); // Новости
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1))); // Ответы
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "+2")); // Сообщения
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1))); // Друзья
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1))); // Группы
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1))); // Фотографии
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons.getResourceId(7, -1))); // Видеозаписи
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[8], navMenuIcons.getResourceId(8, -1))); // Аудиозаписи
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[9], navMenuIcons.getResourceId(9, -1))); // Закладки
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[10], navMenuIcons.getResourceId(10, -1))); // Поиск
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[11], navMenuIcons.getResourceId(11, -1))); // Настройки    

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
            displayView(4); // Показ страницы (Новости) при запуске
        }
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
		
		
			// Восстановление сохранённой сессии
        account.restore(this); 	
        if(account.access_token == null) {
        	Intent intent = new Intent(this, LoginActivity.class);
    		startActivity(intent);
        }  
        else
        	api = new Api(account.access_token, getString(R.string.app_id));
	}
	
	
	/****************************** Begin Боковое меню ******************************/
	
    private class SlideMenuClickListener implements ListView.OnItemClickListener {
    	@Override
    	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    		displayView(position); // display view for selected nav drawer item
    	}
    }
    
    	// Клик по пункту меню
	private void displayView(int position) {
        fragment = null;
        switch (position) {
        case 0:
            fragment = new UserProfile();
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
    
    /****************************** End Боковое меню ******************************/
    
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
