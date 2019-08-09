package hello.leavesC.common.input;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import hello.leavesC.common.input.model.Emoji;
import hello.leavesC.common.input.utils.EmojiUtils;
import hello.leavesC.common.recycler.common.CommonRecyclerViewHolder;
import hello.leavesC.common.R;

/**
 * 作者：叶应是叶
 * 时间：2017/12/10 19:03
 * 说明：
 */
public class EmojiFragment extends Fragment {

    private static final String EMOTICON_TYPE = "emoticonType";

    private int emoticonType;

    public interface OnEmoticonClickListener {
        void onEmoticonClick(int emoticonType, String emoticonName);
    }

    private OnEmoticonClickListener emoticonClickListener;

    private View view;

    private List<Emoji> emojiList;

    public static final int RECYCLER_VIEW_SPAN_COUNT = 6;

    public EmojiFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            emoticonType = getArguments().getInt(EMOTICON_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_emoticon, container, false);
            RecyclerView rv_emoticonList = view.findViewById(R.id.rv_emoticonList);
            rv_emoticonList.setLayoutManager(new GridLayoutManager(getContext(), RECYCLER_VIEW_SPAN_COUNT));
            initData(rv_emoticonList);
        }
        return view;
    }

    public void initData(RecyclerView rv_emoticonList) {
        emojiList = new ArrayList<>();
        for (String emoticonName : EmojiUtils.getEmojiMap(emoticonType).keySet()) {
            emojiList.add(new Emoji(emoticonName, EmojiUtils.getEmojiId(emoticonType, emoticonName)));
        }
        EmojiAdapter emojiAdapter = new EmojiAdapter(getContext(), emojiList);
        emojiAdapter.setClickListener(new CommonRecyclerViewHolder.OnClickListener() {
            @Override
            public void onClick(int position) {
                emoticonClickListener.onEmoticonClick(emoticonType, emojiList.get(position).getName());
            }
        });
        rv_emoticonList.setAdapter(emojiAdapter);
    }

    public static EmojiFragment newInstance(int emoticonType) {
        EmojiFragment fragment = new EmojiFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EMOTICON_TYPE, emoticonType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEmoticonClickListener) {
            emoticonClickListener = (OnEmoticonClickListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        emoticonClickListener = null;
    }

}
