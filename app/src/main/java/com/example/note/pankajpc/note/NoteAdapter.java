package com.example.note.pankajpc.note;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Pankaj PC on 12-17-2016.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    Context context;
    private Realm mRealm;
    private RealmResults<NoteModel> mResults;
    public View v;
    ViewHolder vh;
    int position1;


    //Default constructor is must while implementing custom adapter
    public NoteAdapter()
    {

    }

    public NoteAdapter(Context context, RealmResults<NoteModel> results, Realm mRealm) {
        this.context = context;
        this.mResults = results;
        this.mRealm = mRealm;
    }

    public void update(RealmResults<NoteModel> results){
        mResults = results;
        notifyDataSetChanged();
    }




    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener,View.OnClickListener {
        public TextView mTitle,mDateTime;
        public ImageView mImageView;


        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            mTitle = (TextView)itemView.findViewById(R.id.noteTitle);
            mDateTime = (TextView)itemView.findViewById(R.id.noteDateTime);
            mImageView = (ImageView)itemView.findViewById(R.id.notePriority);

        }

        //code to show note on click
        @Override
        public void onClick(View view) {
            String title = mResults.get(getAdapterPosition()).getmNoteTitle();
            String description = mResults.get(getAdapterPosition()).getmNoteDescription();
            String timestamp = mResults.get(getAdapterPosition()).getmNoteDateTime();
            int priority = mResults.get(getAdapterPosition()).getmNotePriority();
            Intent i = new Intent(context,ShowNote.class);
            i.putExtra("title",title);
            i.putExtra("description",description);
            i.putExtra("timestamp",timestamp);
            i.putExtra("priority",priority);
            context.startActivity(i);
        }



        //code to delete on long click
        @Override
        public boolean onLongClick(View view) {
            Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE) ;
            vibe.vibrate(100);
            ((MainActivity)context).registerForContextMenu(view);
            position1=getAdapterPosition();
            view.setOnCreateContextMenuListener(onCreateContextMenuListener);
            return false;
        }


    }

    public NoteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(context).inflate(R.layout.custom_row_layout_recyclerview, parent, false);
        //v.setBackgroundColor(Color.parseColor("#FF4000"));
        vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mTitle.setText(mResults.get(position).getmNoteTitle());
       // holder.mTitle.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        SimpleDateFormat curFormater = new SimpleDateFormat("yyMMddHHmmssZ");
        Date dateObj = null;
        try {
            dateObj = curFormater.parse(mResults.get(position).getmNoteDateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat postFormater = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        String newDateStr = postFormater.format(dateObj);
        holder.mDateTime.setText(newDateStr.substring(5,11));
        int priority = mResults.get(position).getmNotePriority();
        if (priority==1 || priority==0)
        {
            holder.mImageView.setImageResource(R.drawable.ic_lowpriority);
        }
        else if(priority==2)
        {
            holder.mImageView.setImageResource(R.drawable.ic_medpriority);
        }
        else if(priority==3)
        {
            holder.mImageView.setImageResource(R.drawable.ic_highpriority);
        }
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }


    public static void add(String time, String description, String title, int priority)
    {

        Realm realm=Realm.getDefaultInstance();
        NoteModel model= new NoteModel(time,description,title,priority);
        realm.beginTransaction();
        realm.copyToRealm(model);
        realm.commitTransaction();
        realm.close();
    }


   View.OnCreateContextMenuListener onCreateContextMenuListener = new View.OnCreateContextMenuListener() {
        @Override
        public void onCreateContextMenu(final ContextMenu contextMenu, final View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            MenuItem.OnMenuItemClickListener onMenuItemClickListener = new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if(menuItem.getTitle().equals("Delete Note")){
                        mRealm.beginTransaction();
                        mResults.get(position1).deleteFromRealm();
                        mRealm.commitTransaction();
                        notifyItemRemoved(position1);

                    }
                    else if(menuItem.getTitle().equals("Send Note")){
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/html");
                        intent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");
                        intent.putExtra(Intent.EXTRA_SUBJECT, mResults.get(position1).getmNoteTitle());
                        intent.putExtra(Intent.EXTRA_TEXT, mResults.get(position1).getmNoteDescription());
                        context.startActivity(Intent.createChooser(intent, "Send Note"));
                    }
                    else if(menuItem.getTitle().equals("Show Note")){
                        String title = mResults.get(position1).getmNoteTitle();
                        String description = mResults.get(position1).getmNoteDescription();
                        String timestamp = mResults.get(position1).getmNoteDateTime();
                        int priority = mResults.get(position1).getmNotePriority();
                        Intent i = new Intent(context,ShowNote.class);
                        i.putExtra("title",title);
                        i.putExtra("description",description);
                        i.putExtra("timestamp",timestamp);
                        i.putExtra("priority",priority);
                        context.startActivity(i);
                    }
                    else if(menuItem.getTitle().equals("Set Reminder")){
                        String title = mResults.get(position1).getmNoteTitle();
                        String description = mResults.get(position1).getmNoteDescription();
                        String timestamp = mResults.get(position1).getmNoteDateTime();
                        int priority = mResults.get(position1).getmNotePriority();
                        Intent i1 = new Intent(context,SetReminder.class);
                        i1.putExtra("title",title);
                        i1.putExtra("description",description);
                        i1.putExtra("timestamp",timestamp);
                        i1.putExtra("priority",priority);
                        context.startActivity(i1);
                    }
                    return false;
                }
            };

            MenuItem menuItem = contextMenu.add("Delete Note");
            MenuItem menuItem1 = contextMenu.add("Send Note");
            MenuItem menuItem2 = contextMenu.add("Show Note");
            MenuItem menuItem3 = contextMenu.add("Set Reminder");
            contextMenu.setHeaderTitle(mResults.get(position1).getmNoteTitle());

            menuItem.setOnMenuItemClickListener(onMenuItemClickListener);
            menuItem1.setOnMenuItemClickListener(onMenuItemClickListener);
            menuItem2.setOnMenuItemClickListener(onMenuItemClickListener);
            menuItem3.setOnMenuItemClickListener(onMenuItemClickListener);



        }

    };



}
