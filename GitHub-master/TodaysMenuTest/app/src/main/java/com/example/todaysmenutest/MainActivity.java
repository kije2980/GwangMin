package com.example.todaysmenutest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.kakao.auth.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout drawerLayout;
    private View drawerView;
    private ListView lv_menu;
    private Button btn_logout;
    private BackPressCloseHandler backPressCloseHandler;
    private LoginActivity loginActivity;
    private TextView tv_nickName;
    private ImageView iv_profileImage;
    MenuItem mSearch;
    private Button btn_expand, btn_collapse;
    private ArrayList<RestaurantList> allRestaurantList = new ArrayList<>();
    private CurrentLocation currentLocation;
    private SearchPlace searchPlace;
    private FieldSelector fieldSelector;
    private final String apikey = "AIzaSyBTKIAknIvncH9pwlBjQ0mylhnwaFVzZkI";
    private final static String nearbyUrl = "https://maps.googleapis.com";
    private List<Place.Field> fields;
    private NetworkManager networkManager;
    private List<String> idList = null;
    private GetPlaceAndPhoto getPlaceAndPhoto;
    private Handler handler;
    private List<Place> places;
    private MyAsyncTask myAsyncTask;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                "Manifest.permission.ACCESS_FINE_LOCATION") != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "권한이 필요합니다", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
        //현재 위치 정보를 가져오기 위한 클래스
        //Places 객체 초기화
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apikey);
        }
        currentLocation = new CurrentLocation(this);
        networkManager = CreateRetrofit.create(nearbyUrl);
        searchPlace = new SearchPlace(networkManager);
        getPlaceAndPhoto = new GetPlaceAndPhoto(this);


        myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute("치킨");

        this.initializeData();
        RecyclerView view = findViewById(R.id.recyclerViewVertical);
        VerticalAdapter verticalAdapter = new VerticalAdapter(this,allRestaurantList);

        view.setHasFixedSize(true);
        view.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        view.setAdapter(verticalAdapter);

        //카카오 프로필 가져오기
        tv_nickName = findViewById(R.id.nickName);
        iv_profileImage = findViewById(R.id.profileImage);

        Intent intent = getIntent();
        String nickName = intent.getStringExtra("nickName");//닉네임 가져오기
        String profileImageURL = intent.getStringExtra("profileImageUrl"); //사진 Url 가져오기
        String token = intent.getStringExtra("token"); //토큰 가져오기
        tv_nickName.setText(nickName);
        Glide.with(this).load(profileImageURL).into(iv_profileImage);

        //네비게이터 메뉴
        final String[] items = {"최근 본 식당", "내가 쓴 리뷰보기", "설정"};
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
        lv_menu = findViewById(R.id.lv_menu);
        lv_menu.setAdapter(adapter);

        //뒤로가기버튼 핸들러
        backPressCloseHandler = new BackPressCloseHandler(this);

        //로그인 액티비티 클래스
        loginActivity = new LoginActivity();

        //네비게이터 로그아웃 버튼
        btn_logout = findViewById(R.id.btn_logout);

        //검색 확장 축소 버튼
        btn_expand = findViewById(R.id.btnexpand);
        btn_collapse = findViewById(R.id.btncollapse);

        //네비게이터 선언 및 구현
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerView = findViewById(R.id.drawer);

        drawerLayout.setDrawerListener(listener);
        drawerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        if(Session.getCurrentSession().isClosed()) //로그인 안되있는경우
            btn_logout.setText("Login");
        else //로그인 안되있는 경우
            btn_logout.setText("Logout");

        //툴바 메뉴 버튼 설정
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //왼쪽버튼 사용여부 true
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp); //메뉴 이미지 삽입
        toolbar.setTitle("오늘 뭐먹지?");
        toolbar.setTitleTextColor(Color.BLACK);

        //탭바 구현부분
        TabHost tabHost1 = findViewById(R.id.tabHost1);
        tabHost1.setup();//setup 함수를 이용해 tabHost(호스트레이아웃) 초기화

        //첫번째 Tabbar
        TabHost.TabSpec ts1 = tabHost1.newTabSpec("Tab Spec 1"); //TabSpec:탭버튼의 형태와 탭의 내용 관리,tag이름 설정
        ts1.setContent(R.id.content1); //탭의 내용 지정
        ts1.setIndicator("",this.getResources().getDrawable(R.drawable.ic_home_black_24dp,null));
        tabHost1.addTab(ts1);

        //두번째 Tabbar
        TabHost.TabSpec ts2 = tabHost1.newTabSpec("Tab Spec 2");
        ts2.setContent(R.id.content2);
        ts2.setIndicator("레시피");
        tabHost1.addTab(ts2);

        //세번째 Tabbar
        TabHost.TabSpec ts3 = tabHost1.newTabSpec("Tab Spec 3");
        ts3.setContent(R.id.content3);
        ts3.setIndicator("더치페이");
        tabHost1.addTab(ts3);

        //네번째 Tabbar
        TabHost.TabSpec ts4 = tabHost1.newTabSpec("Tab Spec 4");
        ts4.setContent(R.id.content4);
        ts4.setIndicator("밥값내기");
        tabHost1.addTab(ts4);

        btn_logout.setOnClickListener(this);
        btn_expand.setOnClickListener(this);
        btn_collapse.setOnClickListener(this);

        Log.d("endOncreate", "끝남");
    }

    public void initializeData(){
        ArrayList<Restaurant> res_list1 = new ArrayList<>();
        /*res_list1.add(new Restaurant(R.drawable.sushi,"스시일기"));
        res_list1.add(new Restaurant(R.drawable.sushi,"스시일기"));
        res_list1.add(new Restaurant(R.drawable.sushi,"스시일기"));
        res_list1.add(new Restaurant(R.drawable.sushi,"스시일기"));
        res_list1.add(new Restaurant(R.drawable.sushi,"스시일기"));*/
        allRestaurantList.add(new RestaurantList("단골식당",res_list1));

        ArrayList<Restaurant> res_list2 = new ArrayList<>();
        /*res_list2.add(new Restaurant(R.drawable.sushi,"스시일기"));
        res_list2.add(new Restaurant(R.drawable.sushi,"스시일기"));
        res_list2.add(new Restaurant(R.drawable.sushi,"스시일기"));
        res_list2.add(new Restaurant(R.drawable.sushi,"스시일기"));
        res_list2.add(new Restaurant(R.drawable.sushi,"스시일기"));*/
        allRestaurantList.add(new RestaurantList("관심있을만한 식당",res_list2));

        ArrayList<Restaurant> res_list3 = new ArrayList<>();
        /*res_list3.add(new Restaurant(R.drawable.sushi,"스시일기"));
        res_list3.add(new Restaurant(R.drawable.sushi,"스시일기"));
        res_list3.add(new Restaurant(R.drawable.sushi,"스시일기"));
        res_list3.add(new Restaurant(R.drawable.sushi,"스시일기"));
        res_list3.add(new Restaurant(R.drawable.sushi,"스시일기"));*/
        allRestaurantList.add(new RestaurantList("이 시간 인기 있는 식당",res_list3));

        ArrayList<Restaurant> res_list4 = new ArrayList<>();
        /*res_list4.add(new Restaurant(R.drawable.sushi,"스시일기"));
        res_list4.add(new Restaurant(R.drawable.sushi,"스시일기"));
        res_list4.add(new Restaurant(R.drawable.sushi,"스시일기"));
        res_list4.add(new Restaurant(R.drawable.sushi,"스시일기"));
        res_list4.add(new Restaurant(R.drawable.sushi,"스시일기"));*/
        allRestaurantList.add(new RestaurantList("20대 여성 인기메뉴",res_list4));

        ArrayList<Restaurant> res_list5 = new ArrayList<>();
        /*res_list5.add(new Restaurant(R.drawable.sushi,"스시일기"));
        res_list5.add(new Restaurant(R.drawable.sushi,"스시일기"));
        res_list5.add(new Restaurant(R.drawable.sushi,"스시일기"));
        res_list5.add(new Restaurant(R.drawable.sushi,"스시일기"));
        res_list5.add(new Restaurant(R.drawable.sushi,"스시일기"));*/
        allRestaurantList.add(new RestaurantList("회사원 인기메뉴",res_list5));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Toolbar 위에 검색 버튼 inflate
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        mSearch=menu.findItem(R.id.action_search);


        //검색 아이콘 클릭시 확장 및 축소
        mSearch.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                TextView text = findViewById(R.id.txtstatus);
                text.setText("현재 상태 : 확장됨");
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                TextView text=findViewById(R.id.txtstatus);
                text.setText("현재 상태 : 축소됨");
                return true;
            }
        });

        //menuItem을 이용해서 SearchView 변수 생성
        SearchView sv = (SearchView)mSearch.getActionView();

        //검색창 힌트 설정
        sv.setQueryHint("식당을 검색해 주세요");

        //검색창 글씨색 바꾸기
        SearchView.SearchAutoComplete searchAutoComplete = sv.findViewById(androidx.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(Color.GRAY);
        searchAutoComplete.setTextColor(Color.BLACK);

        //확인버튼 활성화
        sv.setSubmitButtonEnabled(true);

        //SarchView의 검색 이벤트
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //검색 버튼을 눌렀을 경우
            @Override
            public boolean onQueryTextSubmit(String query) {
                TextView text = findViewById(R.id.txtstatus);
                text.setText(query+"를 검색합니다.");
                return true;
            }

            //텍스트가 바뀔때마다 호출
            @Override
            public boolean onQueryTextChange(String newText) {
                TextView text = findViewById(R.id.txtsearch);
                text.setText("검색식 : " + newText);
                return true;
            }
        });
       return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { //Menu 버튼 클릭시 Drawer 열림
        switch (item.getItemId()){
            case android.R.id.home:{
                drawerLayout.openDrawer(drawerView);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() { //뒤로가기 버튼 눌렸을때
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

    //네비게이터 슬라이더 터치 슬라이더
    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };

    @Override
    public void onClick(View view) { //로그아웃 버튼 클릭시
        if(view == btn_logout){
            if(Session.getCurrentSession().isOpened()){ //로그인이 되어있는 상태
                loginActivity.onClickLogout(); //로그아웃
                Toast.makeText(MainActivity.this,"로그아웃 되었습니다.",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,LoginActivity.class); //로그인페이지로 이동
                startActivity(intent);
            }
            else if(Session.getCurrentSession().isClosed()){ //로그인이 되어있지 않은 상태
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);

            }

        }
        //검색창 확장
        else if(view == btn_expand)
            mSearch.expandActionView();

        //검색창 축소
        else if(view == btn_collapse)
            mSearch.collapseActionView();
    }

    class MyAsyncTask extends AsyncTask<String, Void, Void>{
        List<String> idList1;
        String location;

        @Override
        protected Void doInBackground(String... strings) {
            currentLocation.connect();
            while(currentLocation.isSuccess()){
            }
            location = currentLocation.getLocation();
            searchPlace.getIdList(location,strings[0]);
            while(searchPlace.isSuccess()){
            }
            getPlaceAndPhoto.getPlace(searchPlace.getList());
            while(getPlaceAndPhoto.isSuccess()){
            }
            Log.d("플레이스",getPlaceAndPhoto.getPlaces()+"");
            Log.d("포토",getPlaceAndPhoto.getpMap()+"");

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }


}
