package com.Lumension.android.permission_scanner;

import java.util.ArrayList;
import android.content.Context;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * An edit text extended to support removing and get the count of TextWatchers added to it.
 * 
 * Courtesy of inazaruk from http://stackoverflow.com/questions/6270484/how-to-remove-all-listeners-added-with-addtextchangedlistener
 * @author inazaruk
 *
 */
public class ExtendedEditText extends EditText
{   
    private ArrayList<TextWatcher> mListeners = null;

    public ExtendedEditText(Context ctx)
    {
        super(ctx);
    }

    public ExtendedEditText(Context ctx, AttributeSet attrs)
    {
        super(ctx, attrs);
    }

    public ExtendedEditText(Context ctx, AttributeSet attrs, int defStyle)
    {       
        super(ctx, attrs, defStyle);
    }

    @Override
    public void addTextChangedListener(TextWatcher watcher)
    {       
        if (mListeners == null) 
        {
            mListeners = new ArrayList<TextWatcher>();
        }
        mListeners.add(watcher);

        super.addTextChangedListener(watcher);
    }

    @Override
    public void removeTextChangedListener(TextWatcher watcher)
    {       
        if (mListeners != null) 
        {
            int i = mListeners.indexOf(watcher);
            if (i >= 0) 
            {
                mListeners.remove(i);
            }
        }

        super.removeTextChangedListener(watcher);
    }

    public void clearTextChangedListeners()
    {
        if(mListeners != null)
        {
            for(TextWatcher watcher : mListeners)
            {
                super.removeTextChangedListener(watcher);
            }

            mListeners.clear();
            mListeners = null;
        }
    }
}