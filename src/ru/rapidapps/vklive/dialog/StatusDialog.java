package ru.rapidapps.vklive.dialog;

import ru.rapidapps.vklive.R;
import ru.rapidapps.vklive.UserInfo;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;

public class StatusDialog extends DialogFragment implements OnClickListener
{

	final String LOG_TAG = "myLogs";
	 
	 public static StatusDialog newInstance() 
	 {
	        StatusDialog f = new StatusDialog();
	        return f;
	    }
	
	
	  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	  {
	    getDialog().setTitle("Статус");
	    View v = inflater.inflate(R.layout.dialog_status, container, false);
	    v.findViewById(R.id.change_status).setOnClickListener(this);	    
	    return v;
	  }
	 
	  public void onClick(View v) 
	  {		  
		  UserInfo.setUserStatus(((EditText)getView().findViewById(R.id.edit_status)).getText().toString());
	  }	
}
