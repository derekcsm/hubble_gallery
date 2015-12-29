package com.derek_s.hubble_gallery.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.derek_s.hubble_gallery.R;
import com.derek_s.hubble_gallery.model.SectionChildObject;
import com.derek_s.hubble_gallery.model.SectionObject;
import com.derek_s.hubble_gallery.ui.activities.ActMain;
import com.derek_s.hubble_gallery.ui.fragments.FragNavigationDrawer;
import com.derek_s.hubble_gallery.ui.widgets.AnimatedExpandableListView;
import com.derek_s.hubble_gallery.utils.NavDataUtils;
import com.derek_s.hubble_gallery.utils.ui.FontFactory;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dereksmith on 15-03-05.
 */
public class SectionsAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {

    public static String TAG = "SectionsAdapter";
    private Context context;
    public static ArrayList<SectionObject> sections;
    private HashMap<String, ArrayList<SectionChildObject>> sectionChildren;
    private static LayoutInflater inflater = null;

    public SectionsAdapter(Context context, Activity activity) {
        this.context = context;
        sections = new ArrayList<>();
        sectionChildren = new HashMap<>();
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItems() {
        sections = NavDataUtils.addAllGroups();
        sectionChildren = NavDataUtils.getSectionChildren();
        notifyDataSetChanged();
    }

    @Override
    public SectionChildObject getChild(int groupPosition, int childPosititon) {
        String query = getGroup(groupPosition).getQuery();
        return sectionChildren.get(query).get(childPosititon);
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        String query = getGroup(groupPosition).getQuery();
        return sectionChildren.get(query).size();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    static class ChildViewHolder {
        @Bind(R.id.tv_group_child)
        TextView tvTitle;

        public ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public View getRealChildView(final int groupPosition, final int childPosition, boolean isLastChild,
                                 View view, ViewGroup parent) {
        final ChildViewHolder holder;

        if (view != null) {
            holder = (ChildViewHolder) view.getTag();
        } else {
            view = inflater.inflate(R.layout.item_group_child, null);
            holder = new ChildViewHolder(view);
            holder.tvTitle = (TextView) view.findViewById(R.id.tv_group_child);
            view.setTag(holder);
        }

        if (FragNavigationDrawer.mCurSelectedPositions.get(1) == childPosition
                && FragNavigationDrawer.mCurSelectedPositions.get(0) == groupPosition) {
            holder.tvTitle.setTextColor(context.getResources().getColor(R.color.seleted_item_color));
            holder.tvTitle.setBackgroundColor(context.getResources().getColor(R.color.focused_color));
        } else {
            holder.tvTitle.setTextColor(context.getResources().getColor(R.color.body_dark_theme));
            holder.tvTitle.setBackgroundResource(R.drawable.selector_default);
        }

        holder.tvTitle.setTypeface(FontFactory.getRegular(context));
        holder.tvTitle.setText(getChild(groupPosition, childPosition).getSectionTitle());
        holder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragNavigationDrawer.updateSelectedItem(groupPosition, childPosition, holder.tvTitle.getText().toString());
                ActMain.instance.fragMain.loadInitialItems(getChild(groupPosition, childPosition).getQuery());
                FragNavigationDrawer.closeDrawer();
                notifyDataSetChanged();
            }
        });

        return view;
    }

    @Override
    public SectionObject getGroup(int groupPosition) {
        return this.sections.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.sections.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    static class GroupViewHolder {
        @Bind(R.id.iv_expand)
        ImageView ivExpand;
        @Bind(R.id.tv_group_title)
        TextView tvTitle;

        public GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View view, ViewGroup parent) {
        final GroupViewHolder holder;

        if (view != null) {
            holder = (GroupViewHolder) view.getTag();
        } else {
            view = inflater.inflate(R.layout.item_group, null);
            holder = new GroupViewHolder(view);
            holder.ivExpand = (ImageView) view.findViewById(R.id.iv_expand);
            holder.tvTitle = (TextView) view.findViewById(R.id.tv_group_title);
            view.setTag(holder);
        }

        if (FragNavigationDrawer.mCurSelectedPositions.get(0) == groupPosition) {
            holder.tvTitle.setTextColor(context.getResources().getColor(R.color.seleted_item_color));
            holder.tvTitle.setBackgroundColor(context.getResources().getColor(R.color.focused_color));
        } else {
            holder.tvTitle.setTextColor(context.getResources().getColor(R.color.body_dark_theme));
            holder.tvTitle.setBackgroundResource(R.drawable.selector_default);
        }

        holder.tvTitle.setTypeface(FontFactory.getMedium(context));
        holder.tvTitle.setText(getGroup(groupPosition).getSectionTitle());
        if (getGroup(groupPosition).getIsExpandable()) {
            holder.ivExpand.setVisibility(View.VISIBLE);
            holder.ivExpand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isExpanded) {
                        ActMain.instance.mNavigationDrawerFragment.lvMenu.collapseGroupWithAnimation(groupPosition);
                    } else {
                        ActMain.instance.mNavigationDrawerFragment.lvMenu.expandGroupWithAnimation(groupPosition);
                    }
                }
            });

            if (isExpanded) {
                holder.ivExpand.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_white_24dp));
            } else {
                holder.ivExpand.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_white_24dp));
            }
        } else {
            holder.ivExpand.setVisibility(View.GONE);
        }

        holder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActMain.instance.fragMain.loadInitialItems(getGroup(groupPosition).getQuery());
                FragNavigationDrawer.closeDrawer();
                FragNavigationDrawer.updateSelectedItem(groupPosition, -1, holder.tvTitle.getText().toString());
                notifyDataSetChanged();
            }
        });

        return view;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
