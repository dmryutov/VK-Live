package ru.rapidapps.vklive.adapter;

import java.util.ArrayList;
import java.util.List;

import ru.rapidapps.vklive.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.perm.kate.api.Audio;

public class AudiosArrayAdapter extends ArrayAdapter<Audio> 
{
	private final List<Audio> audio_list;
	public int favorite_user_count;
	    
	private static LayoutInflater inflater = null;
	
	    public AudiosArrayAdapter(Context context, List<Audio> list)
	    {
	        super(context, R.layout.audios_item, list);
	        
	        this.audio_list = new ArrayList<Audio>();
	        
            audio_list.addAll(list);
	        
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    }

	    class ViewHolder
	    {
	    	protected TextView title;
	        protected TextView artist;
	        protected ImageView pp;
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) 
	    {
	    	View view = null;
	       
	    	if (convertView == null) 
	    	{
	    		final ViewHolder viewHolder = new ViewHolder();
	    		
					view = inflater.inflate(R.layout.audios_item, parent,false);
					viewHolder.title = (TextView) view.findViewById(R.id.audio_title);
		            viewHolder.artist = (TextView) view.findViewById(R.id.audio_artist);
		            viewHolder.pp = (ImageView) view.findViewById(R.id.audio_pp);
				
	            view.setTag(viewHolder);
	        } 
	    	else
	            view = convertView;
	    	
	    	ViewHolder holder = (ViewHolder) view.getTag();
	        final Audio aud = audio_list.get(position);
	        
				holder.title.setText(aud.title);
				holder.artist.setText(aud.artist);
				/*if (//is playing)
					holder.pp.setImageResource(R.drawable.ic_online_mobile);
		        else if (usr.online)
			    	holder.pp.setImageResource(R.drawable.ic_online);
			    else
			    	holder.pp.setImageBitmap(null);*/
			 
	        return view;
	    }

	}

