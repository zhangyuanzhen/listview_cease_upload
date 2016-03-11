package com.example.zhangyuanzhen.listview_cease_upload;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Bean> list = new ArrayList<>();
    private ListView listView;
    private Context context;
    private int start_index, end_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        init();
        listView = (ListView) findViewById(R.id.listview);
        final MyAdapter myAdapter = new MyAdapter();
        listView.setAdapter(myAdapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_IDLE:
                        Toast.makeText(context, "IDLE", Toast.LENGTH_SHORT).show();
                        myAdapter.setScrollState(false);
                        //设置为停止滚动
                        //当前屏幕中listview的子项的个数
                        int count = view.getChildCount();
                        Log.e("MainActivity",count+"");
                        for (int i = 0; i < count; i++) {
                            //获取到item的name
                            TextView textview = (TextView) view.getChildAt(i).findViewById(R.id.textview);
                            //获取到item的头像
                            ImageView imageView= (ImageView) view.getChildAt(i).findViewById(R.id.image);

                            if (textview.getTag() != null) { //非null说明需要加载数据
                                textview.setText(textview.getTag().toString());//直接从Tag中取出我们存储的数据name并且赋值
                                textview.setTag(null);//设置为已加载过数据
                            }

                            if (!imageView.getTag().equals("1")){//!="1"说明需要加载数据
                                int image_url=(int)imageView.getTag();//直接从Tag中取出我们存储的数据image——url
                                imageView.setImageResource(image_url);
                                imageView.setTag("1");//设置为已加载过数据
                            }
                        }
                        break;
                    case SCROLL_STATE_FLING:
                        myAdapter.setScrollState(true);
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
                        myAdapter.setScrollState(true);
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                start_index = firstVisibleItem;
                end_index = firstVisibleItem + visibleItemCount;
            }
        });
    }

    private void init() {
        for (int i = 0; i < 100; i++) {
            Bean bean = new Bean();
            bean.setName("Name" + i);
            list.add(bean);
        }
    }

    class MyAdapter extends BaseAdapter {

        private boolean scrollState = false;

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Toast.makeText(context, "getView", Toast.LENGTH_SHORT).show();
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.listview_item, null);
                viewHolder = new ViewHolder();
                TextView textView = (TextView) convertView.findViewById(R.id.textview);
                ImageView imageView = (ImageView) convertView.findViewById(R.id.image);

                viewHolder.imageView = imageView;
                viewHolder.textView = textView;

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Bean bean = list.get(position);
            if (scrollState) {
                viewHolder.imageView.setImageResource(R.mipmap.ic_launcher);
                viewHolder.imageView.setTag(R.mipmap.test);
                viewHolder.textView.setText("加载中");
                viewHolder.textView.setTag(bean.getName());
            } else {
                viewHolder.imageView.setImageResource(R.mipmap.test);
                viewHolder.imageView.setTag("1");
                viewHolder.textView.setText(bean.getName());
                viewHolder.textView.setTag(null);
            }

            return convertView;
        }

        public void setScrollState(boolean scrollState) {
            this.scrollState = scrollState;
        }

    }

    class ViewHolder {
        TextView textView;
        ImageView imageView;
    }
}
