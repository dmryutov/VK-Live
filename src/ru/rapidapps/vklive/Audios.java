package ru.rapidapps.vklive;

import java.util.ArrayList;

import ru.rapidapps.vklive.adapter.AudiosArrayAdapter;
import android.app.Fragment;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.perm.kate.api.Api;
import com.perm.kate.api.Audio;
 
public class Audios extends Fragment implements OnItemClickListener
{
	
	private final Handler handler = new Handler();
	private Context context;
	private Long uid;
	public boolean isPlaying;
	int lastPos;
	
		// Массив пользователей
    public ArrayList<Audio> audios;
    public ArrayList<String> names = new ArrayList<String>();
	
		// Апи
	public static Account account = new Account();
	public static Api api;

    Fragment fragment;
    ListView list_audio;
    AudiosArrayAdapter adapter;
    MediaPlayer mp;
     
    public Audios(){}
    
    public Audios(Long id)
    {
    	uid = id;
    }
     
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.audios, container, false);
        context = ((MainActivity) getActivity());
        list_audio = (ListView) rootView.findViewById((R.id.audio_list));
		account = MainActivity.account;
		api = MainActivity.api;

		isPlaying=false;
		lastPos=0;
		list_audio.setOnItemClickListener(this); 
        ShowFriendList(uid == null ? account.user_id : uid);
        
        return rootView; 
    }
    
	void ShowFriendList(final long id) 
	{
		new Thread()
		{
            @Override
            public void run()
            {
                try
                {
                	audios = api.getAudio(id, null, null, null, null, null);
            		
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
        	if (audios != null)
        	{
        		adapter = new AudiosArrayAdapter(context, audios);
        		list_audio.setAdapter(adapter);
        		list_audio.setFastScrollEnabled(true);   			
    		}
        }
    };
    
    @Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		Audio aud = (Audio) list_audio.getItemAtPosition(position);
		
		if (!isPlaying) 
		{
	        isPlaying = true; 
	        mp=new MediaPlayer();
	        try
	        {
	            mp.setDataSource(aud.url);
	            mp.prepare();
	            mp.start();
	            list_audio.getChildAt(lastPos).findViewById(R.id.audio_pp).setBackground(null);
	            list_audio.getChildAt(position).findViewById(R.id.audio_pp).setBackgroundResource(android.R.drawable.ic_media_play);
	            lastPos=position;
	        } catch (Exception e)
	        {
	        	e.printStackTrace();
	        }
	    } 
		else
	    {
	        isPlaying = false;
	        stopPlaying();
	        list_audio.getChildAt(lastPos).findViewById(R.id.audio_pp).setBackgroundResource(android.R.drawable.ic_media_pause);
	    }
	}
    
    private void stopPlaying() 
    {
        mp.release();
        mp = null;
    }

}