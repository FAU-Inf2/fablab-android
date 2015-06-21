package de.fau.cs.mad.fablab.android.view.common.binding;

import android.support.v4.view.ViewPager;

import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;

public class ViewPagerCommandBinding implements Binding<ViewPager, Void>, ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    private Command<Void> mCommand;

    @Override
    public void bind(ViewPager viewPager, Command<Void> command) {
        mCommand = command;
        this.viewPager = viewPager;
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        int totalChilds = viewPager.getAdapter().getCount();
        if(i == totalChilds && mCommand.isExecutable())
        {
            mCommand.execute(null);
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
