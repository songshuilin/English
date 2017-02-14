package net.naucu.englishxianshi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import net.naucu.englishxianshi.BaseApplication;
import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.db.Manager;
import net.naucu.englishxianshi.db.dao.Books;

import org.xutils.x;

import java.io.File;
import java.util.List;

public class DownloadHomeBooksAdapter extends BaseAdapter {
    private Context context;
    private List<Books> booksChild;
    private boolean isSelect;
    private OnChecked checked;

    public DownloadHomeBooksAdapter(Context context, List<Books> booksChild, boolean isSelect) {
        this.context = context;
        this.booksChild = booksChild;
        this.isSelect = isSelect;
    }

    @Override
    public int getCount() {
        return booksChild.size();
    }

    @Override
    public Object getItem(int position) {
        return booksChild.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_homebooksgrid, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.Im_CoverImageUrl = (ImageView) convertView.findViewById(R.id.Im_CoverImageUrl);
            viewHolder.tv_BooksName = (TextView) convertView.findViewById(R.id.tv_BooksName);
            viewHolder.tv_BooksIntroduction = (TextView) convertView.findViewById(R.id.tv_BooksIntroduction);
            viewHolder.cb_chooses = (CheckBox) convertView.findViewById(R.id.cb_chooses);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (isSelect) {
            viewHolder.cb_chooses.setVisibility(View.VISIBLE);
        } else {
            viewHolder.cb_chooses.setVisibility(View.GONE);
        }
        viewHolder.tv_BooksName.setText(booksChild.get(position).getBooksName());
        viewHolder.tv_BooksName.setTextColor(Color.BLACK);
        viewHolder.tv_BooksIntroduction.setText(booksChild.get(position).getBooksAuthor());
        viewHolder.tv_BooksIntroduction.setTextColor(Color.BLACK);
        x.image().bind(viewHolder.Im_CoverImageUrl, booksChild.get(position).getPic(), BaseApplication.getImageOption());
        viewHolder.cb_chooses.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                booksChild.get(position).setLogos(isChecked);
                int count = 0;
                for (int i = 0; i < booksChild.size(); i++) {
                    if (booksChild.get(i).getLogos() == true) {
                        count++;
                    }
                }
                checked.Count(count);
            }
        });
        checked.Count(0);
        return convertView;
    }

    class ViewHolder {
        ImageView Im_CoverImageUrl;
        TextView tv_BooksName;
        TextView tv_BooksIntroduction;
        CheckBox cb_chooses;
    }

    public interface OnChecked {
        void Count(int i);
    }

    public void setOnChecked(OnChecked checked) {
        if (booksChild.size() == 0) {
            checked.Count(0);
        }
        this.checked = checked;
    }

    public void SelectedDelete(BaseApplication application) {
        for (int i = 0; i < booksChild.size(); i++) {
            if (booksChild.get(i).getLogos() == true) {
                if (booksChild.get(i).getBooksLocalUrl() != null) {
                    new File(booksChild.get(i).getBooksLocalUrl()).delete();
                }
                Manager.DeleteMyBooks(booksChild.get(i).getId());
            }
        }
    }
}
