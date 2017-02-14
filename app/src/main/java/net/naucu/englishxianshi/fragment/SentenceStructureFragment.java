package net.naucu.englishxianshi.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiongbull.jlog.JLog;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.bean.Node;
import net.naucu.englishxianshi.ui.DictionaryTranslationActivity;
import net.naucu.englishxianshi.ui.EBookDbDao;
import net.naucu.englishxianshi.ui.PhraseAnalysis;
import net.naucu.englishxianshi.util.MapString;
import net.naucu.englishxianshi.util.StreamTools;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SentenceStructureFragment extends Fragment {
    public static final String TAG = SentenceStructureFragment.class.getSimpleName();

    private String content;
    private View view;
    private JSONObject jsonObject;
    private SentenceStructureAdapter sentenceStructureAdapter;
    private ListView lv;
    private ArrayList<Node> nodes = new ArrayList<Node>();
    private ArrayList<Node> allNodes = new ArrayList<Node>();
    private Node addNode;
    private NodeListener listener;
    private Context context;

    public SentenceStructureFragment() {
    }

    public SentenceStructureFragment(Context context) {
        this.context = context;
    }

    // private String keyword;
    CallBackValue1 callBackValue1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_grammartranslation, container, false);
        lv = (ListView) view.findViewById(R.id.lv);
        lv.setOverScrollMode(View.OVER_SCROLL_NEVER);

        return view;
    }

    public void analyse(String content) {
        this.content = content;

        nodes.clear();
        allNodes.clear();
        getTree(content);
    }

    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    view.findViewById(R.id.ll_loading).setVisibility(View.GONE);

                    jiegoujiexi((String) msg.obj);
                    initDatas();
                    sentenceStructureAdapter = new SentenceStructureAdapter(context);
                    listener = new NodeListener(sentenceStructureAdapter);
                    lv.setAdapter(sentenceStructureAdapter);
                    lv.setOnItemClickListener(listener);
                    sentenceStructureAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    view.findViewById(R.id.pb_loading).setVisibility(View.GONE);
                    ((TextView) view.findViewById(R.id.tv_loading)).setText("加载失败!");
                    ((TextView) view.findViewById(R.id.tv_loading_info)).setText("选择句子过长,请选择短句");
                    break;

                default:
                    break;
            }
            return false;
        }
    });

    public void jiegoujiexi(String jsonString) {
        jsonObject = new JSONObject();
        jsonObject = JSONObject.parseObject(jsonString);
        JSONArray jsonArray = jsonObject.getJSONArray("result");
        getdigui(jsonArray, null);

    }

    public void getdigui(JSONArray jsonArray, Node node) {
        for (int i = 0; i < jsonArray.size(); i++) {
            jsonObject = jsonArray.getJSONObject(i);
            String text = jsonObject.getString("text");
            String words = jsonObject.getString("words");
            Node pnode = null;
            /**
             * 去除掉无用的标点符号
             * */
            if (text.equals(".") || text.equals(",")||
                    text.equals(":")||text.equals("?")
                    ||text.equals(";")) {
                continue;
            }
            pnode = new Node(node, text, words);
            allNodes.add(pnode);

            if (jsonObject.containsKey("children")) {
                JSONArray ja = jsonObject.getJSONArray("children");
                getdigui(ja, pnode);
            }
        }
    }

    private void initDatas() {
        for (int i = 0; i < allNodes.size(); i++) {
            addNode = allNodes.get(i);
            if (addNode.parent == null) {
                nodes.add(addNode);
            }
        }
    }

    public class SentenceStructureAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private int level = 0;
        Context context;


        public SentenceStructureAdapter(Context context) {
            inflater = LayoutInflater.from(context);
            this.context = context;
        }

        @Override
        public int getCount() {
            return nodes.size();
        }

        @Override
        public Node getItem(int position) {
            return nodes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final Node node = nodes.get(position);
            Node node0 = node;
            while (node0.parent != null) {
                node0 = node0.parent;
                level++;
            }
            for (int i = 0; i < nodes.size(); i++) {
                Node node1 = nodes.get(i);
                if (node1.parent != null) {
                    node1.parent.isHasChild = true;
                }
            }

            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_jiegou, null);
                viewHolder.tv1 = (TextView) convertView.findViewById(R.id.tv);
                viewHolder.tv2 = (TextView) convertView.findViewById(R.id.tv1);
                viewHolder.tv3 = (TextView) convertView.findViewById(R.id.tv2);
                viewHolder.imv = (ImageView) convertView.findViewById(R.id.im_ex);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            convertView.setPadding(40 * level, 0, 0, 0);
            if (MapString.map.containsKey(node.text)) {
                String s = MapString.map.get(node.text);
                if (!s.equals(",") && !s.equals("."))
                    viewHolder.tv1.setText(s);

            } else {
                if (node.text.equals("SBAR") || node.text.equals("SQ") || node.text.equals("SBARQ")) {
                    viewHolder.tv1.setText("原句");
                } else if (node.text.equals("VP")) {
                    viewHolder.tv1.setText("动词短语");
                } else if (node.text.equals("S")) {
                    viewHolder.tv1.setText("短句");
                } else if (!node.text.equals(",") && !node.text.equals(".")) {
                    viewHolder.tv1.setText(node.text);
                }

            }
            viewHolder.tv1.setTextColor(Color.WHITE);
            viewHolder.tv1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), PhraseAnalysis.class);
                    i.putExtra("duanyu", node.text);
                    startActivity(i);
                }
            });

            if (level == 0 || content.equals(node.text1)) {
                if (getActivity() instanceof DictionaryTranslationActivity) {
                    DictionaryTranslationActivity activity = (DictionaryTranslationActivity) getActivity();
                    String inputText = activity.getInputText();
                    viewHolder.tv2.setText(inputText);
                }
            } else {
                /**
                 * 去除跟随着的[]
                 * */
                String[] split = node.text1.split("\\[");
                viewHolder.tv2.setText(split[0]);
            }

            viewHolder.tv2.setTextColor(Color.WHITE);
            if (!node.isHasChild) {
                viewHolder.tv2.setVisibility(View.VISIBLE);
            }
            viewHolder.tv2.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    // Intent i = new Intent(getActivity(),
                    // PhraseAnalysis.class);
                    // i.putExtra("key", node.text);// PRP WHNP等内容
                    // i.putExtra("duanyu", node.text1);
                    // i.putExtra("sentence", keyword);
                    // startActivity(i);

                }
            });
            if (isHasChild(node)) {
                viewHolder.imv.setVisibility(View.VISIBLE);
                if (node.isEX) {
                    if (node.parent == null) {
                        viewHolder.tv2.setVisibility(View.GONE);
                    }
                    viewHolder.imv.setBackgroundResource(R.drawable.open1);
                } else {
                    if (node.parent == null) {
                        viewHolder.tv2.setVisibility(View.VISIBLE);
                    }
                    viewHolder.imv.setBackgroundResource(R.drawable.close);
                }
            } else {
                viewHolder.imv.setVisibility(View.INVISIBLE);
            }

            level = 0;
            return convertView;
        }

    }

    public class ViewHolder {
        ImageView imv;
        TextView tv1;
        TextView tv2;
        TextView tv3;
    }

    public class NodeListener implements OnItemClickListener {
        private SentenceStructureAdapter sentenceStructureAdapter;

        public NodeListener(SentenceStructureAdapter sentenceStructureAdapter) {
            this.sentenceStructureAdapter = sentenceStructureAdapter;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int num = 1;
            int place = 0;
            Node node = sentenceStructureAdapter.getItem(position);

            if (!isHasChild(node)) {
                return;
            }
            findBrother(node);
            for (int i = 0; i < nodes.size(); i++) {
                if (nodes.get(i).equals(node)) {
                    place = i;
                }
            }
            if (!node.isEX) {
                node.isEX = true;
                for (int i = 0; i < allNodes.size(); i++) {
                    Node nodeParent = allNodes.get(i).parent;
                    if (nodeParent == node) {
                        nodes.add(place + num, allNodes.get(i));
                        num++;
                    }
                }
            } else {
                node.isEX = false;
                remove(nodes, node);
            }

            sentenceStructureAdapter.notifyDataSetChanged();
        }

        private void remove(ArrayList<Node> nodes, Node node) {
            int size = nodes.size();
            int i = 0;
            for (; i < size; i++) {
                Node node1 = nodes.get(i);
                if (node1.parent == node) {
                    node1.isEX = false;
                    remove(nodes, node1);
                    nodes.remove(i);
                    size = nodes.size();
                    i = 0;
                }
            }
        }

        private boolean isHasChild(Node node) {
            for (Node node1 : allNodes) {
                if (node1.parent == node) {
                    return true;
                }
            }
            return false;
        }

        private void findBrother(Node node) {
            for (int i = 0; i < nodes.size(); i++) {
                if (node.parent == null) {
                    if (nodes.get(i).parent == null && nodes.get(i).isEX && nodes.get(i) != node) {
                        nodes.get(i).isEX = false;
                        remove(nodes, nodes.get(i));
                    }
                } else if (nodes.get(i).parent == node.parent && nodes.get(i) != node && nodes.get(i).isEX) {
                    nodes.get(i).isEX = false;
                    remove(nodes, nodes.get(i));
                }
            }
        }
    }

    private boolean isHasChild(Node node) {
        for (Node node1 : allNodes) {
            if (node1.parent == node) {
                return true;
            }
        }
        return false;
    }

    URL surl;

    public void getTree(final String keyword) {
        final String type = DictionaryTranslationActivity.YUJU;

        if (view != null) {
            view.findViewById(R.id.ll_loading).setVisibility(View.VISIBLE);
            view.findViewById(R.id.pb_loading).setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.tv_loading)).setText("加载中...");
            ((TextView) view.findViewById(R.id.tv_loading_info)).setText("");
        }
        final EBookDbDao dao = new EBookDbDao(context);
        String json = dao.select(keyword, type);
        if (!json.equals("")) {
            handler.obtainMessage(1, json).sendToTarget();
            return;
        }
        String word = null;
        try {
            word = URLEncoder.encode(keyword, "UTF-8");
            String url = "http://101.201.212.118/parser/getTree?words=" + word + "&phone=1500106";
            surl = new URL(url);
        } catch (Exception e) {
            JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
        }
        new Thread() {
            public void run() {

                String s = null;
                HttpURLConnection con;
                try {
                    con = (HttpURLConnection) surl.openConnection();
                    con.setReadTimeout(5000);
                    con.setRequestMethod("GET");
                    if (con.getResponseCode() == 200) {
                        InputStream is = con.getInputStream();
                        s = StreamTools.streamToString(is);

                    }
                } catch (IOException e) {
                    JLog.e((e == null || e.getMessage() == null) ? "Exception is null" : e.getMessage());
                    s = null;
                } finally {
                    if (s == null || s.length() <= 0) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.obtainMessage(1, s).sendToTarget();
                        dao.add(keyword, s, type);
                    }
                }

            }

            ;
        }.start();

    }

    public interface CallBackValue1 {
        public void SendMessageValue1(String strValue);
    }
}
