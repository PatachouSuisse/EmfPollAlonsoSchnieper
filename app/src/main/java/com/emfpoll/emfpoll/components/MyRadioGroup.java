package com.emfpoll.emfpoll.components;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.RadioButton;

/*
 * Pris depuis https://stackoverflow.com/questions/10461005/how-to-group-radiobutton-from-different-linearlayouts
 *  Résout le problème de RadioButton qui ne sont pas des enfants directs de RadioGroup
 *  ATTENTION : cette classe ne reconnaît pas les RadioButtons ajoutés après l'ajout de la view enfant
 */
public class MyRadioGroup extends LinearLayout {

    private ArrayList<RadioButton> mRadioButtons = new ArrayList<RadioButton>();

    public MyRadioGroup(Context context) {
        super(context);
    }

    public MyRadioGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRadioGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        parseChild(child);
    }

    public void parseChild(final View child) {
        if(child instanceof RadioButton) {
            mRadioButtons.add((RadioButton) child);
            child.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    for(int i = 0; i < mRadioButtons.size(); i++) {
                        Checkable view = (Checkable) mRadioButtons.get(i);
                        view.setChecked(view == v);
                    }
                }
            });
        } else if(child instanceof ViewGroup) {
            parseChildren((ViewGroup)child);
        }
    }

    public void parseChildren(final ViewGroup child) {
        for (int i = 0; i < child.getChildCount();i++) {
            parseChild(child.getChildAt(i));
        }
    }

    public int getCheckedRadioButtonId() {
        for(RadioButton rb : mRadioButtons) {
            if (rb.isChecked()) return rb.getId();
        }
        return -1;
    }

}