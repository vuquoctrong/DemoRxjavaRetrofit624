package com.rikkei.demorxjavaretrofit.ui;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.rikkei.demorxjavaretrofit.ui.base.BaseFragment;

import io.reactivex.annotations.Nullable;


public class NavigationManager<T extends BaseFragment> {

    private FragmentManager fragmentManager;
    private T currentFragment;
    private int container;

    public NavigationManager(FragmentManager fragmentManager, @IdRes int container) {
        this.fragmentManager = fragmentManager;
        this.container = container;
    }

    private void openFragment(Class<T> fragment, @Nullable Bundle bundle, boolean addToBackStack, boolean isOpenAsRoot) {
        if (currentFragment != null && currentFragment.getClass().getName().equalsIgnoreCase(fragment.getName())) {
            return;
        }

        try {
            currentFragment = fragment.newInstance();
            if (bundle != null) {
                currentFragment.setArguments(bundle);
            }
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (isOpenAsRoot) {
                fragmentTransaction.replace(container, currentFragment, "ROOT");
            } else {
                fragmentTransaction.replace(container, currentFragment, fragment.getName());
            }
            if (addToBackStack) {
                fragmentTransaction.addToBackStack(fragment.getName());
            }
            fragmentTransaction.commit();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void addFragment(Class<T> fragment, @Nullable Bundle bundle, boolean addToBackStack) {
        if (currentFragment != null && currentFragment.getClass().getName().equalsIgnoreCase(fragment.getName())) {
            return;
        }

        try {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.hide(currentFragment);
            currentFragment = fragment.newInstance();
            if (bundle != null) {
                currentFragment.setArguments(bundle);
            }
            fragmentTransaction.add(container, currentFragment, fragment.getName());
            if (addToBackStack) {
                fragmentTransaction.addToBackStack(fragment.getName());
            }
            fragmentTransaction.commit();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void addFragment(Class<T> fragment, @Nullable Bundle bundle) {
        addFragment(fragment, bundle, true);
    }

    public void addFragmentNoAddToBackStack(Class<T> fragment, @Nullable Bundle bundle) {
        addFragment(fragment, bundle, false);
    }

    /**
     * Pops all the queued fragments
     */
    private void popEveryFragment() {
        fragmentManager.popBackStackImmediate("ROOT", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    /**
     * display the next Fragment and add to back stack
     */
    public void open(Class<T> fragment, @Nullable Bundle bundle) {
        openFragment(fragment, bundle, true, false);
    }

    /**
     * display the next Fragment and no add to back stack
     */
    public void openNoAddToBackStack(Class<T> fragment, @Nullable Bundle bundle) {
        openFragment(fragment, bundle, false, false);
    }

    /**
     * clear back stack and open fragment as root
     */
    public void openAsRoot(Class<T> fragment, @Nullable Bundle bundle) {
        popEveryFragment();
        openFragment(fragment, bundle, false, true);
    }

    /**
     * back to previous fragment
     */
    public boolean navigateBack(@Nullable Bundle bundle) {
        if (fragmentManager.getBackStackEntryCount() <= 1) {
            return false;
        } else {
            fragmentManager.popBackStackImmediate();
            int currentSize = fragmentManager.getBackStackEntryCount();
            String currentFragmentName = fragmentManager.getBackStackEntryAt(currentSize - 1).getName();
            currentFragment = (T) fragmentManager.findFragmentByTag(currentFragmentName);
            if (currentFragment != null && bundle != null) {
                currentFragment.setArguments(bundle);
            }
            if (currentFragment != null && currentFragment.isHidden()) {
                fragmentManager.beginTransaction().show(currentFragment).commit();
            }
            return true;
        }
    }

    /**
     * back to specify fragment
     */
    public boolean navigateBackTo(Class<T> fragment, @Nullable Bundle bundle) {
        if (fragmentManager.getBackStackEntryCount() <= 1) {
            return false;
        } else {
            String name = fragment.getName();
            if (fragmentManager.findFragmentByTag(name) != null) {
                fragmentManager.popBackStackImmediate(name, 0);
                int currentSize = fragmentManager.getBackStackEntryCount();
                String currentFragmentName = fragmentManager.getBackStackEntryAt(currentSize - 1).getName();
                currentFragment = (T) fragmentManager.findFragmentByTag(currentFragmentName);
                if (currentFragment != null && bundle != null) {
                    currentFragment.setArguments(bundle);
                }
                if (currentFragment != null && currentFragment.isHidden()) {
                    fragmentManager.beginTransaction().show(currentFragment).commit();
                }
                return true;
            } else {
                return false;
            }
        }
    }

    public T getCurrentFragment() {
        return currentFragment;
    }
}
