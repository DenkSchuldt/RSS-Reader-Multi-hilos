package com.sistemasoperativos.denny.rssreader.views.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;

import com.sistemasoperativos.denny.rssreader.fragments.EntriesFragment;
import com.sistemasoperativos.denny.rssreader.models.Entry;
import com.sistemasoperativos.denny.rssreader.utils.Constants;

import java.util.ArrayList;

/**
 * Created by Denny on 8/8/2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

  private int count;
  private SparseArray<EntriesFragment> fragments;
  private CharSequence titles[] = {"Últimas noticias", "Noticias guardadas"};

  public ViewPagerAdapter(FragmentManager fm) {
    super(fm);
    this.count = 2;
    fragments = new SparseArray<>();
  }

  @Override
  public Fragment getItem(int position) {
    EntriesFragment fragment = null;
    switch (position) {
      case 0:
        fragment = EntriesFragment.newInstance(Constants.LATEST_NEWS);
        break;
      case 1:
        fragment = EntriesFragment.newInstance(Constants.SAVED_NEWS);
        break;
    }
    fragments.put(position, fragment);
    return fragment;
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return titles[position];
  }

  @Override
  public int getCount() {
    return count;
  }

  public SparseArray<EntriesFragment> getFragments() {
    return fragments;
  }
}