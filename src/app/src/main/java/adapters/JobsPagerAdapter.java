package adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ui.ExploreFragment;
import ui.MyJobsFragment;

/**
 * An adapter for a ViewPager
 */
public class JobsPagerAdapter extends FragmentStatePagerAdapter {
    private String titles[] = {"Explore", "My Jobs"};

    public JobsPagerAdapter(FragmentManager fm){
        super(fm);
    }

    /**
     * Get the selected Fragment
     * @param i Index of the selected Fragment
     * @return An instance of the selected Fragment
     */
    @Override
    public Fragment getItem(int i) {
        switch(i){
            case 0:
                return ExploreFragment.newInstance();
            case 1:
                return MyJobsFragment.newInstance();
            default:
                return null;
        }
    }

    /**
     * Gets a count of pages in the pager
     * @return The number of pages in the pager
     */
    @Override
    public int getCount() {
        return titles.length;
    }

    /**
     * Gets the title of a page
     * @param i The index of the page in the pager
     * @return A string representation of the page name
     */
    @Override
    public CharSequence getPageTitle(int i){
        return titles[i];
    }
}
