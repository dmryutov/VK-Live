package ru.rapidapps.vklive.model;

import java.util.ArrayList;

import ru.rapidapps.vklive.R;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendListItem extends ArrayAdapter<String> {
	private final Activity context;
	private final ArrayList<String> web;
	private final ArrayList<Drawable> imageId;

	public FriendListItem(Activity context, ArrayList<String> web, ArrayList<Drawable> imageId) {
	//public FriendListItem(Activity context, String[] web, Drawable[] imageId) {
		super(context, R.layout.friend_list_item, web);
		this.context = context;
		this.web = web;
		this.imageId = imageId;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.friend_list_item, null, true);
		TextView txtTitle = (TextView) rowView.findViewById(R.id.title);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
		txtTitle.setText(web.get(position));
		imageView.setImageDrawable(imageId.get(position));
		return rowView;
	}
}
