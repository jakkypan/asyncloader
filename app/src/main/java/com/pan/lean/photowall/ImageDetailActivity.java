/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */

package com.pan.lean.photowall;

import com.pan.lean.photowall.provider.Images;
import com.pan.lean.photowall.util.ImageCache;
import com.pan.lean.photowall.util.ImageFetcher;
import com.pan.lean.photowall.util.ImageResizer;
import com.pan.lean.photowall.util.ImageWorker;
import com.pan.lean.photowall.util.Utils;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;

public class ImageDetailActivity extends FragmentActivity implements
        OnClickListener {
    private static final String IMAGE_CACHE_DIR = "images";
    public static final String EXTRA_IMAGE = "extra_image";

    private ImagePagerAdapter mAdapter;
    private ImageResizer mImageWorker;
    private ViewPager mPager;

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_detail_pager);

        // Fetch screen height and width, to use as our max size when loading images
        // as this
        // activity runs full screen
        final DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        final int height = displaymetrics.heightPixels;
        final int width = displaymetrics.widthPixels;
        final int longest = height > width ? height : width;

        // The ImageWorker takes care of loading images into our ImageView children
        // asynchronously
        mImageWorker = new ImageFetcher(this, longest);
        mImageWorker.setAdapter(Images.imageWorkerUrlsAdapter);
        mImageWorker.setImageCache(ImageCache.findOrCreateCache(this,
                IMAGE_CACHE_DIR));
        mImageWorker.setImageFadeIn(true);

        // Set up ViewPager and backing adapter
        mAdapter = new ImagePagerAdapter(getSupportFragmentManager(),
                mImageWorker.getAdapter().getSize());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mPager.setPageMargin((int) getResources().getDimension(
                R.dimen.image_detail_pager_margin));

        // Set up activity to go full screen
        getWindow().addFlags(LayoutParams.FLAG_FULLSCREEN);

        // Enable some additional newer visibility and ActionBar features to create
        // a more immersive
        // photo viewing experience
        if (Utils.hasActionBar()) {
            final ActionBar actionBar = getActionBar();

            // Enable "up" navigation on ActionBar icon and hide title text
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);

            // Start low profile mode and hide ActionBar
            mPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
            actionBar.hide();

            // Hide and show the ActionBar as the visibility changes
            mPager
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                        @Override
                        public void onSystemUiVisibilityChange(int vis) {
                            if ((vis & View.SYSTEM_UI_FLAG_LOW_PROFILE) != 0) {
                                actionBar.hide();
                            } else {
                                actionBar.show();
                            }
                        }
                    });
        }

        // Set the current item based on the extra passed in to this activity
        final int extraCurrentItem = getIntent().getIntExtra(EXTRA_IMAGE,
                -1);
        if (extraCurrentItem != -1) {
            mPager.setCurrentItem(extraCurrentItem);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Home or "up" navigation
                final Intent intent = new Intent(this, ImageGridActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case R.id.clear_cache:
                final ImageCache cache = mImageWorker.getImageCache();
                if (cache != null) {
                    mImageWorker.getImageCache().clearCaches();
                    // DiskLruCache.clearCache(this, ImageFetcher.HTTP_CACHE_DIR);
                    Toast.makeText(this, R.string.clear_cache_complete,
                            Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Called by the ViewPager child fragments to load images via the one
     * ImageWorker
     *
     * @return
     */
    public ImageWorker getImageWorker() {
        return mImageWorker;
    }

    /**
     * The main adapter that backs the ViewPager. A subclass of
     * FragmentStatePagerAdapter as there could be a large number of items in the
     * ViewPager and we don't want to retain them all in memory at once but
     * create/destroy them on the fly.
     */
    private class ImagePagerAdapter extends FragmentStatePagerAdapter {
        private final int mSize;

        public ImagePagerAdapter(FragmentManager fm, int size) {
            super(fm);
            mSize = size;
        }

        @Override
        public int getCount() {
            return mSize;
        }

        @Override
        public Fragment getItem(int position) {
            return ImageDetailFragment.newInstance(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            final ImageDetailFragment fragment = (ImageDetailFragment) object;
            // As the item gets destroyed we try and cancel any existing work.
            fragment.cancelWork();
            super.destroyItem(container, position, object);
        }
    }

    /**
     * Set on the ImageView in the ViewPager children fragments, to enable/disable
     * low profile mode when the ImageView is touched.
     */
    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        final int vis = mPager.getSystemUiVisibility();
        if ((vis & View.SYSTEM_UI_FLAG_LOW_PROFILE) != 0) {
            mPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        } else {
            mPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        }
    }
}
