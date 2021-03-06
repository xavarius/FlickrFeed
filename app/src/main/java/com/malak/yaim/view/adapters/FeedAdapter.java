package com.malak.yaim.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.malak.yaim.R;
import com.malak.yaim.model.Item;
import com.malak.yaim.model.Media;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {
  private List<Item> mImagesMetadata;
  private Pattern mAuthorPattern = Pattern.compile("\"([^\"]*)\"");

  @Inject FeedAdapter() {
    setDataset(Collections.emptyList());
  }

  public void setDataset(final List<Item> items) {
    mImagesMetadata = items;
    notifyDataSetChanged();
  }

  @Override public FeedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final View view = LayoutInflater
        .from(parent.getContext())
        .inflate(R.layout.feed_item, parent, false);

    return new ViewHolder(view);
  }

  @Override public void onBindViewHolder(FeedAdapter.ViewHolder holder, int position) {
    final Context viewContext = holder.mFeedItemImage.getContext();
    final Item item = mImagesMetadata.get(position);

    if (item != null) {
      final String author = reformatAuthorName(item.getAuthor());
      holder.mFeedItemAuthor.setText(author);
      holder.mFeedItemTitle.setText(item.getTitle());
      holder.mFeedItemTags.setText(item.getTags());

      final Media media = item.getMedia();
      if (media != null) {
        final String imageUri = media.getM();

        Glide.with(viewContext)
            .load(imageUri)
            .centerCrop()
            .placeholder(R.drawable.pandas_placeholder)
            .error(R.drawable.pandas_placeholder)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.mFeedItemImage);
      }
    }
  }

  @NonNull private String reformatAuthorName(final String author) {
    final Matcher matcher = mAuthorPattern.matcher(author);
    return matcher.find() ? matcher.group().replaceAll("\"", "") : author;
  }

  @Override public int getItemCount() {
    return mImagesMetadata != null ? mImagesMetadata.size() : 0;
  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.feed_item_image) ImageView mFeedItemImage;
    @BindView(R.id.feed_item_author) TextView mFeedItemAuthor;
    @BindView(R.id.feed_item_title) TextView mFeedItemTitle;
    @BindView(R.id.feed_item_tags) TextView mFeedItemTags;

    ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }
}