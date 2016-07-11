package com.rz.support;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Zhou on 2016/6/29.
 */
public class FooterEmptyView extends FootView {
    @Override
    public View onCreateView(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_recyclerview_empty,parent,false);
        return view;
    }
}
