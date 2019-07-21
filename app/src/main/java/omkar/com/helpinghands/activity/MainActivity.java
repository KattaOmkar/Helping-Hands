package omkar.com.helpinghands.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import omkar.com.FirestoreHelper.FireService;
import omkar.com.helpinghands.R;
import omkar.com.helpinghands.fragments.HomeFragment;
import omkar.com.helpinghands.fragments.dummy.DummyContent;
import omkar.com.helpinghands.fragments.investorFragment;
import omkar.com.helpinghands.fragments.lenderInfoFragment;
import omkar.com.helpinghands.fragments.loansFragment;
import omkar.com.helpinghands.fragments.myBorrowsFragment;
import omkar.com.helpinghands.fragments.newLoanFragment;
import omkar.com.models.User;
import omkar.com.other.CircleTransformer;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener,
        investorFragment.OnFragmentInteractionListener, lenderInfoFragment.OnFragmentInteractionListener,
        loansFragment.OnFragmentInteractionListener, newLoanFragment.OnFragmentInteractionListener,
        myBorrowsFragment.OnListFragmentInteractionListener {

    private static final String urlNavHeaderBg = "https://api.androidhive.info/images/nav-menu-header-bg.jpg";
    private static final String urlProfileImg = "https://lh3.googleusercontent.com/eCtE_G34M9ygdkmOpYvCag1vBARCmZwnVS6rS5t4JLzJ6QgQSBquM0nuTsCpLhYbKljoyS-txg";
    // tags used to attach the fragments
    private static final String TAG_HOME = "home";

    private static final String TAG_INVESTOR = "investor";
    private static final String TAG_LENDORS = "lenders";
    private static final String TAG_LENDOR_INFO = "lender_info";
    private static final String TAG_LOANS = "loans";
    private static final String TAG_BORROWS = "borrows";
    private static final String TAG_LENTS = "lents";
    private static final String TAG_NEWLOANS = "new_loans";
    private static final String TAG_SETTINGS = "settings";
    // index to identify current nav menu item
    public static int navItemIndex = 0;
    public static String CURRENT_TAG = TAG_LOANS;
    private FirebaseAuth mAuth;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private FireService fireService;
    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.fireService = new FireService().init();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            SendUserToLoginActivity();
        } else {


            toolbar = (Toolbar) findViewById(R.id.toolbar_app_bar_main);
//        setSupportActionBar(toolbar);

            mHandler = new Handler();

            drawer = (DrawerLayout) findViewById(R.id.drawer_layout_act_main);
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            fab = (FloatingActionButton) findViewById(R.id.fab);

            // Navigation view header
            navHeader = navigationView.getHeaderView(0);
            txtName = (TextView) navHeader.findViewById(R.id.nav_header_title);
            txtWebsite = (TextView) navHeader.findViewById(R.id.nav_header_subtitle);
//        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
            imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

            // load toolbar titles from string resources
            activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

            // load nav menu header data
            User user = fireService.populateUserWithRefData(currentUser.getEmail());
            try {
                loadNavHeader(user);
            } catch (NullPointerException npe) {
                Log.d(TAG_HOME, npe.getMessage());
            }


            // initializing navigation menu
            setUpNavigationView();

            if (savedInstanceState == null) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            SendUserToLoginActivity();
        }
        //display home page logic
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    public void updateUsername(User user) {
        loadNavHeader(user);
    }

    public void loadNavHeader(User user) {

        if (user == null) {
            txtName.setText("Not Loaded");
            txtWebsite.setText(mAuth.getCurrentUser().getEmail());
        }
//            ((MyProperties) this.getApplication()).setUser(user);
        txtName.setText(user.getName());
        txtWebsite.setText(user.getEmail());

        // loading header background image
        Glide.with(this).load(urlNavHeaderBg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
//                .into(imgNavHeaderBg);

        // Loading profile image
        Glide.with(this).load(urlProfileImg)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransformer(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);

        // showing dot next to notifications label
        navigationView.getMenu().getItem(3).setActionView(R.layout.manu_dot);
    }


    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            toggleFab();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
//            case 0:
//                // home
//                HomeFragment homeFragment = new HomeFragment();
//                return homeFragment;
            case 1:
                // photos

                myBorrowsFragment myBorrowsFragment = new myBorrowsFragment();
                Bundle b = new Bundle();
                b.putString("TYPE", String.valueOf(0));
                myBorrowsFragment.setArguments(b);
                return myBorrowsFragment;
            case 2:
                // movies fragment
                myBorrowsFragment myLentsFragment = new myBorrowsFragment();
                Bundle bb = new Bundle();
                bb.putString("TYPE", String.valueOf(1));
                myLentsFragment.setArguments(bb);
                return myLentsFragment;
            case 0:
                // notifications fragment
                loansFragment loansFragment = new loansFragment();
                return loansFragment;
//            case 3:
//                // notifications fragment
//                loansFragment loansFragment = new loansFragment();
//                return loansFragment;

//            case 4:
//                // settings fragment
//                newLoanFragment newLoanFragment = new newLoanFragment();
//                return newLoanFragment;
            default:
                return new HomeFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
//                    case R.id.nav_home:
//                        navItemIndex = 0;
//                        CURRENT_TAG = TAG_HOME;
//                        break;
                    case R.id.nav_myborrows:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_BORROWS;
                        break;
                    case R.id.nav_mylents:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_LENTS;
                        break;
                    case R.id.nav_loans:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_LOANS;
                        break;
//                    case R.id.nav_new_loan:
//                        navItemIndex = 4;
//                        CURRENT_TAG = TAG_NEWLOANS;
//                        break;
//                    case R.id.nav_about_us:
//                        // launch new intent instead of loading fragment
//                        startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
//                        drawer.closeDrawers();
//                        return true;
//                    case R.id.nav_privacy_policy:
//                        // launch new intent instead of loading fragment
//                        startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));
//                        drawer.closeDrawers();
//                        return true;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main, menu);
        }

        // when fragment is notifications, load the menu created for notifications
//        if (navItemIndex == 3) {
//            getMenuInflater().inflate(R.menu.notifications, menu);
//        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();
            mAuth.signOut();
            SendUserToLoginActivity();
            return true;
        }

        // user is in notifications fragment
        // and selected 'Mark all as Read'
//        if (id == R.id.action_mark_all_read) {
//            Toast.makeText(getApplicationContext(), "All notifications marked as read!", Toast.LENGTH_LONG).show();
//        }

        // user is in notifications fragment
        // and selected 'Clear All'
//        if (id == R.id.action_clear_notifications) {
//            Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_LONG).show();
//        }

        return super.onOptionsItemSelected(item);
    }

    // show or hide the fab
    private void toggleFab() {
        if (navItemIndex == 0)
            fab.show();
        else
            fab.hide();
    }

    protected void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

//    public class AsyncTaskLoadUser extends AsyncTask<User, Void, Void> {
//
//        protected Void doInBackground(User... users) {
//            Log.d(TAG_HOME, "/*/*/*/*/*/*/*/*/*/*/*/*/" + users[0].getEmail());
//            loadNavHeader(users[0]);
//        }
//
//
//
//        protected void onPostExecute(User result) {
//            //this method is invoked when the background computation is finished
//            //the result of the computation is passed as the parameter to this method
//            Log.d(TAG_HOME, "__________" + result.getEmail() + "_________");
//            loadNavHeader(result);
//        }
//    }
}
