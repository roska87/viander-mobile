package com.bit.viandermobile;

import com.google.android.gms.common.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class ViandPositions {

    private List<Integer> changePosition;
    private List<Integer> positions;

    private ViandPositions(){
        this.positions = new ArrayList<>();
        this.changePosition = new ArrayList<>();
    }

    public static ViandPositions initialize(){
        return new ViandPositions();
    }

    public List<Integer> getPositions() {
        return positions;
    }

    public void addPosition(int position){
        positions.add(position);
    }

    public void addChangePosition(int changePosition){
        this.changePosition.add(changePosition);
    }

    public boolean hasChangePosition(){
        return !CollectionUtils.isEmpty(this.changePosition);
    }

    public List<Integer> getChangePosition(){
        return this.changePosition;
    }

    public void removeChangePosition(int changePosition){
        this.changePosition.remove((Integer) changePosition);
    }

    public void removeChangePosition(){
        this.changePosition = new ArrayList<>();
    }

    public void removePosition(int position){
        positions.remove((Integer) position);
    }

    public void removeAll(){
        positions.clear();
    }

    @Override
    public String toString() {
        return "ViandPositions{" +
                "positions=" + positions +
                '}';
    }
}