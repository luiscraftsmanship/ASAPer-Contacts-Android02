package com.pincelancer.asapercontactsandroid02;

import java.util.ArrayList;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
//import com.livewatch.asapercontactsandroid.R;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import android.app.Activity;
import android.os.Bundle;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.widget.TextView;
 
public class ReadContacts extends SherlockActivity {
	ListView lvItem;
    private Button btnAdd;
    String displayName="", emailAddress="", phoneNumber="";
    ArrayList<String> contactlist=new ArrayList<String>();
    ArrayAdapter<String> itemAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
    	getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		getSupportActionBar().setCustomView(R.layout.import_contacts_menu);
		
		getSupportActionBar().setIcon(R.drawable.cancel_highlighted2x );
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(
        		new ColorDrawable(Color.parseColor("#38393B")));

        
       lvItem = (ListView)this.findViewById(R.id.listView_items);  
       //btnAdd = (Button)this.findViewById(R.id.btnAddItem);
       itemAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,contactlist);
       lvItem.setAdapter(itemAdapter);
       //btnAdd.setOnClickListener(new View.OnClickListener() {
         //  public void onClick(View v) {
               readContacts();
           //}
       //});
    }
        
    private void readContacts()
    {
        ContentResolver cr =getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) 
        {
            displayName="";emailAddress=""; phoneNumber="";
            displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));       
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor emails = cr.query(Email.CONTENT_URI,null,Email.CONTACT_ID + " = " + id, null, null);
            while (emails.moveToNext()) 
            { 
                emailAddress = emails.getString(emails.getColumnIndex(Email.DATA));
                break;
            }
            emails.close();
            if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
            {
                Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{id}, null);
                while (pCur.moveToNext()) 
                {
                     phoneNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    break;
                }
                pCur.close();
            }
                contactlist.add("DisplayName: "+displayName+", PhoneNumber: "+phoneNumber+", EmailAddress: "+ emailAddress+"\n");
                itemAdapter.notifyDataSetChanged();
        }
        cursor.close(); 
    }
 
    private Cursor getContacts() {
        // Run query
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String[] projection = new String[] { ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME };
        String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '" + ("1") + "'";
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME+ " COLLATE LOCALIZED ASC";
        return managedQuery(uri, projection, selection, selectionArgs, sortOrder);
    }
}