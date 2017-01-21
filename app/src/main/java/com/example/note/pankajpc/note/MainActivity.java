package com.example.note.pankajpc.note;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity {

    private ListView mNavigationItem;
    private DrawerLayout mDrawerLayout;
    private FrameLayout mMainContent;
    Intent intent;
    String [] mNavigationItemArray;
    List <DrawerItem> mDrawerItem = new ArrayList();
    private ActionBarDrawerToggle mDrawerToggle;
    private RecyclerView mRecyclerView;
    private static NoteAdapter mNoteAdapter;
    private static Realm mRealm;
    private static Context context1;
    private RealmResults<NoteModel> mResults;
    SearchView mSearchView;
    String mSearchString=null;
    private ContextMenu contextMenu;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        Realm.init(this);
        context1 = this;
        mRealm = Realm.getDefaultInstance();
        PreferenceManager.setDefaultValues(this, R.xml.preference, false);
        new SharedPreferenceCommon(this);
        //initialize relam database
        initUi();
        initDrawerItem();


        mRecyclerView = (RecyclerView)findViewById(R.id.notes_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mNoteAdapter = new NoteAdapter(this,mResults,mRealm);
        mRecyclerView.setAdapter(mNoteAdapter);
        mNavigationItemArray = getResources().getStringArray(R.array.navigation_list);
        mNavigationItem = (ListView)findViewById(R.id.navigationitems);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_navigation);


        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.app_name,R.string.app_name){

        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);



        //setting custom adapter on Navigation Drawer
        mNavigationItem.setAdapter(new CustomDrawerAdapter(this,R.layout.custom_navigation_listview_row,mDrawerItem));

        //setting onClick Listener on Navigation Drawer
        mNavigationItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Fragment fragment = null;
                mDrawerLayout.closeDrawers();

                switch (mDrawerItem.get(i).getItemName()){
                    case "Add a Note":
                        intent = new Intent(MainActivity.this,AddNote.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        break;
                    case "Settings":
                        intent = new Intent(MainActivity.this,SettingActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        break;
                    case "Search":
                        mSearchView.onActionViewExpanded();
                        break;
                    case "Save Notes To SD":
                        new SaveNoteToSD(context1).execute();
                        break;
                }


            }
        });

    }


    private void initDrawerItem() {
        mDrawerItem.add(new DrawerItem("Notes",R.drawable.ic_notes));
        mDrawerItem.add(new DrawerItem("Add a Note",R.drawable.ic_add));
        mDrawerItem.add(new DrawerItem("Search",R.drawable.ic_search));
        mDrawerItem.add(new DrawerItem("Save Notes To SD",R.drawable.ic_note_save));
        mDrawerItem.add(new DrawerItem("Settings",R.drawable.ic_settings));
    }


    private void initUi() {
        String sortOrder = SharedPreferenceCommon.getSortOrder();
        if(sortOrder.equalsIgnoreCase("mNotePriority")) {
            mResults = mRealm.where(NoteModel.class).findAllSorted(sortOrder, Sort.DESCENDING);
        }
        else{
            mResults = mRealm.where(NoteModel.class).findAllSorted(sortOrder);
        }

    }


   protected void onStart() {
       super.onStart();
       this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
       mResults.addChangeListener(new RealmChangeListener<RealmResults<NoteModel>>() {
           @Override
           public void onChange(RealmResults<NoteModel> element) {
               mNoteAdapter.update(mResults);
           }
       });
   }

    @Override
    //This override displays the items when action bar up button click
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        //handling the menu clicks on menu.xml
        switch (id){
            case R.id.actionAdd:
                startActivity((new Intent(context1,AddNote.class)).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                break;
            case R.id.dateCreated:
                mResults = mRealm.where(NoteModel.class).findAllSorted("mNoteDateTime",Sort.DESCENDING);
                mNoteAdapter.update(mResults);
                break;
            case R.id.alphabetically:
                mResults = mRealm.where(NoteModel.class).findAllSorted("mNoteTitle");
                mNoteAdapter.update(mResults);
                break;
            case R.id.priority:
                mResults = mRealm.where(NoteModel.class).findAllSorted("mNotePriority",Sort.DESCENDING);
                mNoteAdapter.update(mResults);
                break;
            case R.id.actionSave:
                new SaveNoteToSD(this).execute();
                break;
        }

        return true;
    }




    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        mSearchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                String query = newText.toLowerCase();
                mResults = mRealm.where(NoteModel.class).contains("mNoteTitle", query, Case.INSENSITIVE).findAllSorted("mNoteDateTime");
                mNoteAdapter.update(mResults);
                return false;

            }
        });

        return true;

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!mSearchView.isIconified()) {
            mSearchView.onActionViewCollapsed();
        }
    }

    @Override
    public void onBackPressed() {
        if (!mSearchView.isIconified()) {
            mSearchView.onActionViewCollapsed();
        } else {
            super.onBackPressed();
        }
    }


}
