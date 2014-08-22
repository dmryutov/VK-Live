package ru.rapidapps.vklive.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import ru.rapidapps.vklive.ImageLoader;
import ru.rapidapps.vklive.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.perm.kate.api.User;

public class ContactsArrayAdapter extends ArrayAdapter<User> implements Filterable, SectionIndexer {
		
	private Activity activity;
	    private final List<User> list, user_list;
        public static List<User> original;
	    private Filter filter;
	    public int favorite_user_count;
		private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();
	    HashMap<String, Integer> alphaIndexer;
	    String[] sections;
	    
	    private static LayoutInflater inflater = null;
	    public ImageLoader imageLoader; 

	    public ContactsArrayAdapter(Activity context, List<User> list, int favorite_user_count) {
	        super(context, R.layout.friend_list_item, list);
	        this.activity = context;
	        this.list = list;
	        this.favorite_user_count = favorite_user_count;
	        this.original = new ArrayList<User>(list);
	        
	        
	        this.user_list = new ArrayList<User>();
	        
	        alphaIndexer = new HashMap<String, Integer>();
	        int size = list.size();
	        
	        sectionHeader.add(user_list.size());
	        alphaIndexer.put("*", user_list.size());
	        User u = new User();
        	u.first_name = "Важные";
        	user_list.add(u);
	        for (int x = 0; x < favorite_user_count; x++) {
            	user_list.add(list.get(x));
	        }
	        for (int x = favorite_user_count; x < size; x++) {
	        	User s = list.get(x);	            
	            String ch = s.first_name.substring(0, 1);	            
	            ch = ch.toUpperCase();
	            if (!alphaIndexer.containsKey(ch)) {
	            	alphaIndexer.put(ch, user_list.size());
	            	sectionHeader.add(user_list.size());

	            	u = new User();
                	u.first_name = ch;
	            	user_list.add(u);
	            }
	            user_list.add(s);
	        }
	        
	        
	    
	        Set<String> sectionLetters = alphaIndexer.keySet();
            ArrayList<String> sectionList = new ArrayList<String>(sectionLetters); 
            Collections.sort(sectionList);
            sections = new String[sectionList.size()];
            sectionList.toArray(sections);        

            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            imageLoader = new ImageLoader(activity.getApplicationContext());
	    }

	    class ViewHolder {
	    	protected TextView name;
	        protected ImageView image;
	        protected ImageView online;
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	    	View view = null;
	       
	    	int rowType = getItemViewType(position);
	    	if (convertView == null) {
	    		LayoutInflater inflator = activity.getLayoutInflater();
	    		final ViewHolder viewHolder = new ViewHolder();
	    			
	            switch (rowType) {
				case 0:
					view = inflater.inflate(R.layout.friend_list_item, null);
					viewHolder.name = (TextView) view.findViewById(R.id.title);
		            viewHolder.image = (ImageView) view.findViewById(R.id.icon);
		            viewHolder.online = (ImageView) view.findViewById(R.id.status);
					break;
				case 1:
					view = inflater.inflate(R.layout.friend_list_header, null);
					viewHolder.name = (TextView) view.findViewById(R.id.textSeparator);
					break;
				}
	            view.setTag(viewHolder);
	        } else
	            view = convertView;
	    	ViewHolder holder = (ViewHolder) view.getTag();
	        final User usr = user_list.get(position);
	               
	        switch (rowType) {
			case 0:
				holder.name.setText(usr.first_name + " " + usr.last_name);
				if (usr.online_mobile)
		        	holder.online.setImageResource(R.drawable.ic_online_mobile);
		        else if (usr.online)
			    	holder.online.setImageResource(R.drawable.ic_online);
			    else
			    	holder.online.setImageBitmap(null);
			    
			    imageLoader.DisplayImage(usr.photo, holder.image);
				break;
			case 1:
				holder.name.setText(usr.first_name);
				break;
	        } 
	            
	        return view;
	    }

	    @Override
		public Filter getFilter() {
			if (filter == null){
				filter = new ContactsFilter();
			}
			return filter;
		}
	    
	    public class ContactsFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {            	
            	constraint = constraint.toString().toLowerCase();
            	FilterResults result = new FilterResults();
            	
            	if (constraint != null && constraint.toString().length() > 0) {
	                List<User> founded = new ArrayList<User>();
	                for (User u : original) {
	                    if (u.first_name.toLowerCase().contains(constraint) || u.last_name.toLowerCase().contains(constraint))
	                        founded.add(u);
	                }
	                result.values = founded;
	                result.count = founded.size();
	            } else {
	                result.values = original;
	                result.count = original.size();
	            }
                return result;
            }
 
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                clear();
                for (User u : (List<User>) filterResults.values) {
                    add(u);
                }
                notifyDataSetChanged(); 
            }
        }

		@Override
		public int getPositionForSection(int section) {
			return alphaIndexer.get(sections[section]);
		}
		
		@Override
		public int getSectionForPosition(int position) {
			return 0;
		}

		@Override
		public Object[] getSections() {
			return sections;
		}
		
		@Override
		public int getCount() {
			return user_list.size();
		}
	    
		@Override
		public int getItemViewType(int position) {
			return sectionHeader.contains(position) ? 1 : 0;
		}
	 
		@Override
		public int getViewTypeCount() {
			return 2;
		}	
	}