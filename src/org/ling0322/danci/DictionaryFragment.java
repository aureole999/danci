package org.ling0322.danci;

import java.util.ArrayList;
import android.os.Bundle;
import android.text.*;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.*;


public class DictionaryFragment 
    extends CustomFragment 
    implements TextWatcher, OnItemClickListener, OnClickListener, OnFocusChangeListener {
    
    public WordlistAdapter adapter;
    private EditText et = null;
    private Button backButton = null;
    public ArrayList<String> wordlist;
    private Dict dict12;
    
    private void showDefinition(String word) {
    	LinearLayout defiContainer = (LinearLayout)getActivity().findViewById(R.id.defiContainer);
    	LinearLayout dictButtonContainer = (LinearLayout)getActivity().findViewById(R.id.dictButtonContainer);
    	View listView = getActivity().findViewById(R.id.listView1);
    	
    	et.setVisibility(View.GONE);
    	listView.setVisibility(View.GONE);
    	defiContainer.setVisibility(View.VISIBLE);
    	dictButtonContainer.setVisibility(View.VISIBLE);
    	defiContainer.removeAllViews();
		View sv = DefinitionView.getDefinitionView(getActivity(), word);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.FILL_PARENT,
			LinearLayout.LayoutParams.FILL_PARENT);
		defiContainer.addView(sv);
		sv.setLayoutParams(lp);
		mainActivity.closeIME();
    }

    private void hideDefinition() {
    	LinearLayout defiContainer = (LinearLayout)getActivity().findViewById(R.id.defiContainer);
    	LinearLayout dictButtonContainer = (LinearLayout)getActivity().findViewById(R.id.dictButtonContainer);
    	View editText = getActivity().findViewById(R.id.editText1);
    	View listView = getActivity().findViewById(R.id.listView1);
    	
    	et.setVisibility(View.VISIBLE);
    	et.selectAll();
    	mainActivity.openIME(et);
    	
    	listView.setVisibility(View.VISIBLE);
    	defiContainer.setVisibility(View.GONE);
    	dictButtonContainer.setVisibility(View.GONE);
    }
    
    private void updateWordList(String word) {
        if (word.equals("")) {
            wordlist.clear();
            adapter.notifyDataSetChanged();
            return ;
        }

        wordlist.clear();
        wordlist.addAll(dict12.getWordList(word));
        adapter.notifyDataSetChanged();
    }
    
    @Override
    public void onStart() {
        super.onStart();
        Log.d("danci", "dict frag start");
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("danci", "dict frag destroy");
    }
   
    @Override
    public void onResume() {
        super.onResume();
        Log.d("danci", "dict frag resume");
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //
        // get the OxfordJm Dict Object
        //
        dict12 = Dict.getInstance(Dict.DICT_12);
        Log.d("danci", "dict frag create view");
        return inflater.inflate(R.layout.dict, container, false);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListView lv = (ListView)getActivity().findViewById(R.id.listView1);
        
        et = (EditText)getActivity().findViewById(R.id.editText1);
        et.addTextChangedListener(this);

        et.setOnClickListener(this);
        et.setOnFocusChangeListener(this);
        wordlist =  new ArrayList<String>();
        
        adapter = new WordlistAdapter(wordlist, getActivity());
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
        
        //
        // Back button event
        //
        backButton = (Button)getActivity().findViewById(R.id.dictBackButton);
        backButton.setOnClickListener(this);
        
        mainActivity = (MainActivity)getActivity();
        Log.d("danci", "dict frag activity created");
    }


    public void afterTextChanged(Editable arg0) { }
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        updateWordList(arg0.toString());
    }
    

    
    public void onPageSelected() {
        et = (EditText)getActivity().findViewById(R.id.editText1);
    	if (et != null && et.getVisibility() == View.VISIBLE) {
    		
            // if not setSelection here, the action bar may be displayed
    		et.setSelection(0, 0);
    		et.requestFocus();
    		et.selectAll();
    		mainActivity.openIME(et);
    	}
    }


    public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
        //
        // start the DefinitionActivity
        //
    	showDefinition(wordlist.get(position));
        
    }

    @Override
    public boolean onBackKey() {
    	hideDefinition();
		return true;
    }
    
    public void onFocusChange(View view, boolean b) {
        if (view == et && b == true) {
            // et.selectAll();
        }
    }

    public void onClick(View view) {
        if (view == et) {
        	
            // et.selectAll();
        } else if (view == backButton) {
        	hideDefinition();
        }
        
    }


}
